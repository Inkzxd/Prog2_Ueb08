package de.htwsaar.esch.Codeopolis.DomainModel;

import de.htwsaar.esch.Codeopolis.DomainModel.Game.GrainType;
import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.*;
import java.text.DecimalFormat;
import java.util.NoSuchElementException;

public class Depot {
    private LinkedList<Silo> silos;

    /**
     * Constructs a Depot object with the specified number of silos and capacity per silo.
     *
     * @param numberOfSilos    The number of silos in the depot.
     * @param capacityPerSilo  The capacity per silo.
     */
    public Depot(int numberOfSilos, int capacityPerSilo) {
        this.silos = new LinkedList<>();
        for (int i = 0; i < numberOfSilos; i++) {
            this.silos.addLast(new Silo(capacityPerSilo));
        }
    }
    
    /**
     * Constructs a Depot object with the specified LinkedList of silos.
     * Each silo in the list is deeply copied to ensure that the Depot has its own separate instances.
     *
     * @param silosList The LinkedList of Silo objects to be copied into the depot.
     */
    public Depot(LinkedList<Silo> silosList) {
        this.silos = new LinkedList<>();
        if (silosList != null) {
            for (int i = 0; i < silosList.size(); i++) {
                // Assuming Silo has a copy constructor to create a deep copy
                this.silos.addLast(new Silo(silosList.get(i)));
            }
        }
    }

    /**
     * Retrieves the current fill level of the depot for a specific grain type.
     *
     * @param grainType The grain type for which to retrieve the fill level.
     * @return The total amount of grain stored in the depot for the specified grain type.
     */
    public int getFillLevel(Game.GrainType grainType) {
        int totalFillLevel = 0;
        for (int i = 0; i < silos.size(); i++) {
            if (silos.get(i).getGrainType() == grainType) {
                totalFillLevel += silos.get(i).getFillLevel();
            }
        }
        return totalFillLevel;
    }
    
    /**
     * Creates and returns a copy of the silos LinkedList.
     * This method creates a new LinkedList and populates it with copies of the Silo objects,
     * ensuring that modifications to the returned list do not affect the original silos.
     *
     * @return A copy of the silos LinkedList.
     */
    public LinkedList<Silo> getSilos() {
        // Create a new LinkedList of Silo
        LinkedList<Silo> silosCopy = new LinkedList<>();
        for (int i = 0; i < silos.size(); i++) {
            // Assume Silo has a copy constructor to create a deep copy of each Silo object
            silosCopy.addLast(new Silo(silos.get(i)));
        }
        return silosCopy;
    }

    /**
     * Gets the total amount of bushels (grain) stored in the depot.
     *
     * @return The total amount of bushels stored in the depot.
     */
    public int getTotalFillLevel(){
        int totalBushels = 0;
        for (int i = 0; i < silos.size(); i++) {
            totalBushels += silos.get(i).getFillLevel();
        }
        return totalBushels;
    }
    
    /**
     * Retrieves the capacity of the depot for a specific grain type.
     *
     * @param grainType The grain type for which to retrieve the capacity.
     * @return The total capacity of the depot for the specified grain type.
     */
    public int getCapacity(Game.GrainType grainType) {
        int totalCapacity = 0;
        for (int i = 0; i < silos.size(); i++) {
            if (silos.get(i).getGrainType() == grainType || silos.get(i).getGrainType() == null) {
                totalCapacity += silos.get(i).getCapacity();
            }
        }
        return totalCapacity;
    }

    /**
     * Stores a harvest in the depot.
     *
     * @param harvest The harvest to be stored in the depot.
     * @return True if the harvest was successfully stored, false otherwise.
     */
    public boolean store(Harvest harvest) {
        for (int i = 0; i < silos.size(); i++) {
            if (silos.get(i).getGrainType() == harvest.getGrainType() || silos.get(i).getFillLevel() == 0) {
                harvest = silos.get(i).store(harvest);
                if(harvest == null) {
                    return true;
                }
            }
        }
        defragment();
        for (int i = 0; i < silos.size(); i++) {
            if (silos.get(i).getGrainType() == harvest.getGrainType() || silos.get(i).getFillLevel() == 0) {
                harvest = silos.get(i).store(harvest);
                if(harvest == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Takes out a specified amount of grain from the depot for a specific grain type.
     *
     * @param amount    The amount of grain to be taken out.
     * @param grainType The grain type for which to take out the grain.
     * @return The actual amount of grain taken out from the depot.
     */
    public int takeOut(int amount, Game.GrainType grainType) {
        int takenAmount = 0;
        for (int i = 0; i < silos.size(); i++) {
            if (silos.get(i).getGrainType() == grainType) {
                int amountTaken = silos.get(i).takeOut(amount);
                amount -= amountTaken;
                takenAmount += amountTaken;
                if (amount <= 0) {
                    break;
                }
            }
        }
        return takenAmount;
    }
    
    /**
     * Takes out the specified amount of grain from the silo, distributing it evenly among the stored bushels.
     * If the specified amount exceeds the total amount of grain in the silo, all grain is removed and returned.
     * If the specified amount is less than the total amount of grain, the grain is taken out evenly from each bushel,
     * with any remaining grain distributed among the bushels in a round-robin fashion.
     *
     * @param amount The amount of grain to be taken out from the silo.
     * @return The actual amount of grain taken out from the silo.
     */
    public int takeOut(int amount) {
        if(amount >= this.getTotalFillLevel()){
            int totalAmountOfBushels =  this.getTotalFillLevel();
            for (int i = 0; i < silos.size(); i++) {
                silos.get(i).emptySilo();
            }
            return totalAmountOfBushels;
        }
        int partion = amount / this.silos.size();
        int remainder = amount % this.silos.size();
        for (int i = 0; i < silos.size(); i++) {
            if (silos.get(i).getFillLevel() < partion) {
                remainder += partion - silos.get(i).getFillLevel();
                silos.get(i).emptySilo();
            }
            else {    
                silos.get(i).takeOut(partion);
            }
        }
        int j = 0;
        while (remainder > 0) {
            if (this.silos.get(j).getFillLevel() > 0) {
                this.silos.get(j).takeOut(1);
                remainder--;
            }
            j = (j+1) % this.silos.size();
        }
        return amount;
    }

    /**
     * Expands the depot by adding more silos with the specified capacity per silo.
     *
     * @param numberOfSilos    The number of silos to add.
     * @param capacityPerSilo  The capacity per silo.
     */
    public void expand(int numberOfSilos, int capacityPerSilo) {
        for (int i = 0; i < numberOfSilos; i++) {
            this.silos.addLast(new Silo(capacityPerSilo));
        }
        this.takeOut((int)(numberOfSilos * GameConfig.DEPOT_EXPANSION_COST)); //#Issue42
    }

    /**
     * Performs defragmentation on the depot to redistribute grain across silos.
     */
    public void defragment() {
        LinkedList<Harvest> allHarvests = new LinkedList<>();

        //int index = 0;
        for (int i = 0; i < silos.size(); i++) {
            LinkedList<Harvest> siloHarvests = silos.get(i).emptySilo();
            if(siloHarvests != null) {
                for (int j = 0; j < siloHarvests.size(); j++) {
                    allHarvests.addLast(siloHarvests.get(j));
                }
            }
        }

        // Add all harvests back. Store method takes care that silos are not fragmented. 
        for (int i = 0; i < allHarvests.size(); i++) {
            if (silos.get(i) != null) {
                silos.get(i).store(allHarvests.get(i));
            }
        }
    }

    /**
     * Retrieves the total count of harvests across all silos.
     *
     * @return The total count of harvests stored in all silos combined.
     */
    /*private int getTotalHarvestCount() {
        int totalCount = 0;
        for (int i = 0; i < silos.size(); i++) {
            totalCount += silos.get(i).getHarvestCount();
        }
        return totalCount;
    }*/

    /**
     * Simulates the decay of grain in the depot over time.
     *
     * @return The total amount of grain that decayed in the depot.
     */
    public int decay(int currentYear) {
        int totalDecayedAmount = 0;
        for (int i = 0; i < silos.size(); i++) {
            totalDecayedAmount += silos.get(i).decay(currentYear);
        }
        return totalDecayedAmount;
    }

    /**
     * Checks if the depot is fully occupied with grain.
     * 
     * @return {@code true} if the total fill level of all silos equals or exceeds the total capacity of the storage system, {@code false} otherwise.
     */
    public boolean full() {
        return this.getTotalFillLevel() >= this.totalCapacity();
    }

    /**
     * Calculates the total capacity of the depot by summing the capacities of all silos.
     * 
     * @return The total capacity of the storage system.
     */
    public int totalCapacity() {
        int totalCapacity = 0;
        for (int i = 0; i < silos.size(); i++) {
            totalCapacity += silos.get(i).getCapacity();
        }
        return totalCapacity;
    }

    /**
     * Retrieves the total amount of grain categorized by grain type.
     * 
     * @return An array containing the total amount of grain for each grain type, indexed by the grain type constants defined in the {@code GameConfig} class.
     */
    public int[] getBushelsCategorizedByGrainType() {
        int[] result = new int[Game.GrainType.values().length];
        for(Game.GrainType grainType : Game.GrainType.values()) {
            result[grainType.ordinal()] = getFillLevel(grainType);
        }
        return result;
    }

    /**
     * Returns a string representation of the depot, including information about each silo's grain type, fill level, capacity, and absolute amount of grain.
     *
     * @return A string containing information about the depot, including each silo's grain type, fill level, capacity, and absolute amount of grain.
     */
    @Override
    public String toString() {
        class DepotVisualizer {
            private StringBuilder visualRepresentation = new StringBuilder();
            private DecimalFormat df = new DecimalFormat("0.00");

            // Methode zur Hinzufügung von Silo-Informationen zur Darstellung
            public void appendSiloInfo(Silo silo) {
                visualRepresentation.append("Silo ").append(silo.getGrainType() != null ? silo.getGrainType() : "EMPTY").append("\n");
                visualRepresentation.append("Amount of Grain: ").append(silo.getFillLevel()).append(" units\n");

                int capacity = silo.getCapacity();
                double fillPercentage = (double) silo.getFillLevel() / capacity * 100;

                // ASCI-ART representation of the fill level
                int fillBarLength = 20;
                int filledBars = (int) (fillPercentage / 100 * fillBarLength);
                int emptyBars = fillBarLength - filledBars;

                visualRepresentation.append("|");
                for (int j = 0; j < filledBars; j++) {
                    visualRepresentation.append("=");
                }
                for (int j = 0; j < emptyBars; j++) {
                    visualRepresentation.append("-");
                }
                visualRepresentation.append("| ").append(df.format(fillPercentage)).append("% filled\n");
                visualRepresentation.append("Capacity: ").append(capacity).append(" units\n\n");
            }

            // Methode zur Visualisierung der gesammelten Informationen
            public String visualize() {
                return visualRepresentation.toString();
            }
        }
        DepotVisualizer result = new DepotVisualizer();
        for (int i = 0; i < silos.size(); i++) {
            result.appendSiloInfo(silos.get(i));
        }
        return result.visualize();
    }

    public Iterator iterator(Game.GrainType grainType) {
        return new DepotIterator(grainType);
    }

    public interface Iterator {
        /**
         * Checks if there are further objects available for iteration.
         * 
         * @return {@code true} if there are more objects available, {@code false} otherwise.
         */
        boolean hasNext();

        /**
         * Returns the next {@link Silo.Status} object in the iteration.
         * This method should be called if {@code hasNext()} returns {@code true}.
         * 
         * @return The next {@link Silo.Status} object.
         * @throws NoSuchElementException if no more elements are available.
         */
        Silo.Status next();
    }

    private class DepotIterator implements Iterator {

        private int currentIndex;
        private GrainType grainType;

        public DepotIterator(GrainType grainType) {
            this.grainType = grainType;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < silos.size();
        }

        @Override
        public Silo.Status next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            for (int i = currentIndex; i < silos.size(); i++) {
                if (silos.get(i).getGrainType() == grainType) {
                    currentIndex++;
                    return silos.get(i).getStatus();
                }
            }
            return null;
        }
    }
}
