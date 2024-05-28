package de.htwsaar.esch.Codeopolis.DomainModel;

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
     * Constructs a Depot object with the specified array of silos.
     * Each silo in the array is deeply copied to ensure that the Depot has its own separate instances.
     *
     * @param silosArray The array of Silo objects to be copied into the depot.
     */
    public Depot(LinkedList<Silo> silosList) {
        this.silos = new LinkedList<>();
        LinkedList<Silo>.LinkedIterator<Silo> iterator = silosList.iterator();
        while (iterator.hasNext()) {
            this.silos.addLast(new Silo(iterator.next()));
        }
    }

    /**
     * Retrieves the current fill level of the depot for a specific grain type.
     *
     * @param grainType The grain type for which to retrieve the fill level.
     * @return The total amount of grain stored in the depot for the specified grain type.
     */
    public int getFillLevel(Game.GrainType grainType) {
        int fillLevel = 0;
        DepotIterator iterator = new DepotIterator(grainType);
        while (iterator.hasNext()) {
            Silo.Status status = iterator.next();
            fillLevel += status.getCurrentFillLevel();
        }
        return fillLevel;
    }
    
    /**
     * Creates and returns a copy of the silos array.
     * This method creates a new array and populates it with copies of the Silo objects,
     * ensuring that modifications to the returned array do not affect the original silos.
     *
     * @return A copy of the silos array.
     */
    public LinkedList<Silo> getSilos() {
        // Create a new array of Silo with the same length as the original
        LinkedList<Silo> silosCopy = new LinkedList<>();
        LinkedList<Silo>.LinkedIterator<Silo> iterator = this.silos.iterator();
        while (iterator.hasNext()) {
            silosCopy.addLast(new Silo(iterator.next()));
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
        LinkedList<Silo>.LinkedIterator<Silo> iterator = this.silos.iterator();

        while (iterator.hasNext()) {
            totalBushels += iterator.next().getFillLevel();
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
        DepotIterator iterator = new DepotIterator(grainType);
        while (iterator.hasNext()) {
            Silo.Status status = iterator.next();
            totalCapacity += status.getCurrentCapacity();
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
        LinkedList<Silo>.LinkedIterator<Silo> iterator = this.silos.iterator();

        while (iterator.hasNext()) {
            Silo currentSilo = iterator.next();
            if (currentSilo.getGrainType() == harvest.getGrainType() || currentSilo.getFillLevel() == 0) {
                harvest = currentSilo.store(harvest);
                if (harvest == null) {
                    return true;
                }
            }
        }
        defragment();
        LinkedList<Silo>.LinkedIterator<Silo> iterator2 = this.silos.iterator();
        while (iterator2.hasNext()) {
            Silo currentSilo = iterator2.next();
            if (currentSilo.getGrainType() == harvest.getGrainType() || currentSilo.getFillLevel() == 0) {
                harvest = currentSilo.store(harvest);
                if (harvest == null) {
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
        LinkedList<Silo>.LinkedIterator<Silo> iterator = this.silos.iterator();
        while (iterator.hasNext() && amount > 0) {
            Silo currentSilo = iterator.next();
            if (currentSilo.getGrainType() == grainType) {
                int taken = currentSilo.takeOut(amount);
                amount -= taken;
                takenAmount += taken;
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
    		LinkedList<Silo>.LinkedIterator<Silo> iterator = this.silos.iterator();
            while (iterator.hasNext()) {
                Silo currentSilo = iterator.next();
                currentSilo.emptySilo();
            }
    		return totalAmountOfBushels;
    	}

    	int partion = amount / this.silos.size();
    	int remainder = amount % this.silos.size();

        LinkedList<Silo>.LinkedIterator<Silo> iterator2 = this.silos.iterator();
    	while (iterator2.hasNext()) {
            Silo currentSilo = iterator2.next();
            if (currentSilo.getFillLevel() < partion) {
                remainder += partion - currentSilo.getFillLevel();
                currentSilo.emptySilo();
            } else {
                currentSilo.takeOut(partion);
            }
        }

    	int j = 0;
    	while(remainder > 0) {
    		if(this.silos.get(j).getFillLevel() > 0) {
    			this.silos.get(j).takeOut(1);
    			remainder--;
    		}
    		j = (j+1)%Game.GrainType.values().length;	
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
        this.takeOut((int) (numberOfSilos * GameConfig.DEPOT_EXPANSION_COST));
    }

    /**
     * Performs defragmentation on the depot to redistribute grain across silos.
     */
    public void defragment() {
        LinkedList<Harvest> allHarvests = new LinkedList<>();
        LinkedList<Silo>.LinkedIterator<Silo> iterator = this.silos.iterator();

       while (iterator.hasNext()) {
            Silo currentSilo = iterator.next();
            LinkedList<Harvest> removedHarvests = currentSilo.emptySilo();
            if (removedHarvests != null) {
                LinkedList<Harvest>.LinkedIterator<Harvest> harvestIterator = removedHarvests.iterator();
                while (harvestIterator.hasNext()) {
                    allHarvests.addLast(harvestIterator.next());
                }
            }
        }
    }

    /**
     * Retrieves the total count of harvests across all silos.
     *
     * @return The total count of harvests stored in all silos combined.
     */
    private int getTotalHarvestCount() {
        int totalCount = 0;
        LinkedList<Silo>.LinkedIterator<Silo> iterator = this.silos.iterator();
        while (iterator.hasNext()) {
            Silo currentSilo = iterator.next();
            totalCount += currentSilo.getHarvestCount();
        }
        return totalCount;
    }


    /**
     * Simulates the decay of grain in the depot over time.
     *
     * @return The total amount of grain that decayed in the depot.
     */
    public int decay(int currentYear) {
        int totalDecayedAmount = 0;
        LinkedList<Silo>.LinkedIterator<Silo> iterator = this.silos.iterator();
        while (iterator.hasNext()) {
            Silo currentSilo = iterator.next();
            totalDecayedAmount += currentSilo.decay(currentYear);
        }
        return totalDecayedAmount;
    }


    /**
     * Checks if the depot is fully occupied with grain.
     * 
     * @return {@code true} if the total fill level of all silos equals or exceeds the total capacity of the storage system, {@code false} otherwise.
     */
	public boolean full() {
		if(this.getTotalFillLevel()>=this.totalCapacity()){
            return true;
        } 
		return false;
	}
	
	/**
	 * Calculates the total capacity of the depot by summing the capacities of all silos.
	 * 
	 * @return The total capacity of the storage system.
	 */
	public int totalCapacity() {
		int totalCapacity = 0;
        LinkedList<Silo>.LinkedIterator<Silo> iterator = this.silos.iterator();
		while (iterator.hasNext()) {
            Silo currentSilo = iterator.next();
            totalCapacity += currentSilo.getCapacity();
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


        // creating the local class
        class DepotVisualizer {

            private StringBuilder builder = new StringBuilder();
            private DecimalFormat df = new DecimalFormat("0.00");

            private int index = 0;

            // adding information to a silo
            public void appendSiloInfo(Silo silo) {

                builder.append("Silo ").append(index + 1).append(": ");

                String grainName = (silo.getGrainType() != null) ? silo.getGrainType().toString() : "EMPTY";
                builder.append(grainName).append("\n");

                int fillLevel = silo.getFillLevel();
                int capacity = silo.getCapacity();

                double fillPercentage = (double) fillLevel / capacity * 100;
                int fillBarLength = 20;

                int filledBars = (int) (fillPercentage / 100 * fillBarLength);
                int emptyBars = fillBarLength - filledBars;

                builder.append("Amount of Grain: ").append(fillLevel).append(" units\n");
                builder.append("|");

                for (int j = 0; j < filledBars; j++) {
                    builder.append("=");
                }

                for (int j = 0; j < emptyBars; j++) {
                    builder.append("-");
                }

                builder.append("| ").append(df.format(fillPercentage)).append("% filled\n");
                builder.append("Capacity: ").append(capacity).append(" units\n\n");

                index++;
            }

            public String visualize() {
                return builder.toString();
            }
        }

        silos.sort();
        DepotVisualizer result = new DepotVisualizer();
        for (int i = 0; i < silos.size(); i++) {

            result.appendSiloInfo(silos.get(i));
        }
        return result.visualize();
    }

    /**
     * Provides an iterator over silos of a specific grain type.
     * @param grainType The grain type for the iterator to focus on.
     * @return An iterator over SiloStatus for the specified grain type.
     */
    public Iterator iterator(Game.GrainType grainType) {
        return new DepotIterator(grainType);
    }

    /**
     * This interface defines an iterator specifically for iterating over {@link Silo.Status} objects.
     * It allows for the sequential traversal of silo status objects within a depot, based on a specific grain type.
     * The iterator provides access to the next {@link Silo.Status} instance as long as there are more elements available.
     *
     * Methods:
     * - {@code hasNext()} - Checks if there are more {@link Silo.Status} objects available to iterate over.
     * - {@code next()} - Returns the next {@link Silo.Status} object and advances the iterator.
     */
    public interface Iterator {
        /**
         * Checks if there are further silo status objects available for iteration.
         *
         * @return {@code true} if more silo status objects are available; {@code false} otherwise.
         */
        boolean hasNext();

        /**
         * Returns the next {@link Silo.Status} object in the iteration.
         * This method should only be called if {@code hasNext()} returns {@code true}.
         *
         * @return The next {@link Silo.Status} object.
         * @throws NoSuchElementException if no more elements are available.
         */
        Silo.Status next();
    }


    /**
     * Provides an iterator over silos of a specific grain type within the depot.
     * This iterator allows for the traversal of only those silos that store a specified type of grain,
     * returning the status of each through {@link Silo.Status} objects.
     *
     * This class implements the {@link Iterator} interface, enabling sequential access to the silo statuses.
     * It maintains an internal cursor to track the current index of silos being accessed to ensure
     * that only silos matching the specified grain type are considered.
     *
     * Usage Example:
     * <pre>
     * DepotIterator iterator = depot.new DepotIterator(Game.GrainType.WHEAT);
     * while (iterator.hasNext()) {
     *     Silo.SiloStatus status = iterator.next();
     *     // Process status
     * }
     * </pre>
     *
     * @see Silo.Status
     */
    private class DepotIterator implements Iterator {
        private int currentIndex = 0;
        private Game.GrainType grainType;

        /**
         * Constructs an iterator for silos containing a specific type of grain.
         *
         * @param grainType The grain type that this iterator will traverse.
         */
        public DepotIterator(Game.GrainType grainType) {
            this.grainType = grainType;
            advanceIndex();
        }

        /**
         * Advances the cursor to the next silo that contains the specified grain type.
         */
        private void advanceIndex() {
            while(currentIndex < silos.size() && silos.get(currentIndex).getGrainType() != grainType && silos.get(currentIndex).getFillLevel() > 0){
                currentIndex++;
            }
        }

        /**
         * Determines if there are any more silos that contain the specified grain type.
         *
         * @return {@code true} if there are additional silos to be iterated over; {@code false} otherwise.
         */
        @Override
        public boolean hasNext() {
            return currentIndex < silos.size();
        }

        /**
         * Returns the status of the next silo containing the specified grain type.
         * This method should only be called if {@code hasNext()} returns {@code true}.
         *
         * @return The {@link Silo.Status} of the next relevant silo.
         * @throws NoSuchElementException if there are no more silos to iterate over.
         */
        @Override
        public Silo.Status next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more silos available of the specified grain type.");
            }
            Silo.Status status = silos.get(currentIndex).getStatus();
            currentIndex++;
            advanceIndex();
            return status;
        }
    }
}