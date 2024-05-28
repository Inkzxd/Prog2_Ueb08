package de.htwsaar.esch.Codeopolis.DomainModel;

import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.*;
import java.text.DecimalFormat;
import java.util.NoSuchElementException;

public class Depot {
    private Silo[] silos;

    /**
     * Constructs a Depot object with the specified number of silos and capacity per silo.
     *
     * @param numberOfSilos    The number of silos in the depot.
     * @param capacityPerSilo  The capacity per silo.
     */
    public Depot(int numberOfSilos, int capacityPerSilo) {
        this.silos = new Silo[numberOfSilos];
        for (int i = 0; i < numberOfSilos; i++) {
            this.silos[i] = new Silo(capacityPerSilo);
        }
    }
    
    /**
     * Constructs a Depot object with the specified array of silos.
     * Each silo in the array is deeply copied to ensure that the Depot has its own separate instances.
     *
     * @param silosArray The array of Silo objects to be copied into the depot.
     */
    public Depot(Silo[] silosArray) {
        if (silosArray == null) {
            this.silos = new Silo[0];
        } else {
            this.silos = new Silo[silosArray.length];
            for (int i = 0; i < silosArray.length; i++) {
                // Assuming Silo has a copy constructor to create a deep copy
                this.silos[i] = new Silo(silosArray[i]);
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
        int fillLevel = 0;

        DepotIterator iterator = new DepotIterator(grainType);
        while (iterator.hasNext()) {
            Silo.Status status = iterator.next();
            fillLevel += status.getFillLevel();
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
    public Silo[] getSilos() {
        // Create a new array of Silo with the same length as the original
        Silo[] silosCopy = new Silo[this.silos.length];
        for (int i = 0; i < this.silos.length; i++) {
            // Assume Silo has a copy constructor to create a deep copy of each Silo object
            silosCopy[i] = new Silo(this.silos[i]);
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

        for (int i = 0; i < this.silos.length; i++) {
            totalBushels += silos[i].getFillLevel();
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
            totalCapacity += status.getCapacity();
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
        for (Silo silo : silos) {
            if (silo.getGrainType() == harvest.getGrainType() || silo.getFillLevel() == 0) {
            	harvest = silo.store(harvest);
                if(harvest == null) {
                    return true;
                }
            }
        }
        defragment();
        for (Silo silo : silos) {
            if (silo.getGrainType() == harvest.getGrainType() || silo.getFillLevel() == 0) {
            	harvest = silo.store(harvest);
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
        for (Silo silo : silos) {
            if (silo.getGrainType() == grainType) {
                int amountTaken = silo.takeOut(amount);
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
    		for(int i = 0; i < this.silos.length; i++) {
    			silos[i].emptySilo();
    		}
    		return totalAmountOfBushels;
    	}
    	int partion = amount / this.silos.length;
    	int remainder = amount % this.silos.length;
    	for(int i = 0; i < this.silos.length; i++) {
    		if(this.silos[i].getFillLevel() < partion) {
    			remainder += partion - this.silos[i].getFillLevel();
    			this.silos[i].emptySilo();
    		}
    		else {	
    			this.silos[i].takeOut(partion);
    		}
    	}
    	int j = 0;
    	while(remainder > 0) {
    		if(this.silos[j].getFillLevel() > 0) {
    			this.silos[j].takeOut(1);
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
        Silo[] newSilos = new Silo[silos.length + numberOfSilos];
        System.arraycopy(silos, 0, newSilos, 0, silos.length);
        for (int i = silos.length; i < newSilos.length; i++) {
            newSilos[i] = new Silo(capacityPerSilo);
        }
        silos = newSilos;
        this.takeOut((int)(numberOfSilos * GameConfig.DEPOT_EXPANSION_COST)); //#Issue42
    }

    /**
     * Performs defragmentation on the depot to redistribute grain across silos.
     */
    public void defragment() {
        Harvest[] allHarvests = new Harvest[getTotalHarvestCount()];

        int index = 0;
        for (Silo silo : silos) {
            Harvest[] siloHarvests = silo.emptySilo();
            if(siloHarvests != null) {
	            for (Harvest harvest : siloHarvests) {
	                allHarvests[index++] = harvest;
	            }
            }
        }

        // Add all harvests back. Store method takes care that silos are not fragmented. 
        for (Harvest harvest : allHarvests) {
            if (harvest != null) {
                store(harvest);
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
        for (Silo silo : silos) {
            totalCount += silo.getHarvestCount();
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
        for (Silo silo : silos) {
            totalDecayedAmount += silo.decay(currentYear);
        }
        return totalDecayedAmount;
    }


    /**
     * Checks if the depot is fully occupied with grain.
     * 
     * @return {@code true} if the total fill level of all silos equals or exceeds the total capacity of the storage system, {@code false} otherwise.
     */
	public boolean full() {
		if(this.getTotalFillLevel()>=this.totalCapacity())
			return true;
		return false;
	}
	
	/**
	 * Calculates the total capacity of the depot by summing the capacities of all silos.
	 * 
	 * @return The total capacity of the storage system.
	 */
	public int totalCapacity() {
		int totalCapacity = 0;
		for(int i=0; i<this.silos.length; i++) {
			totalCapacity += this.silos[i].getCapacity();
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
            StringBuilder builder = new StringBuilder();
            DecimalFormat df = new DecimalFormat("0.00");

            int i=0;

            String visualize() {

                return builder.toString();
            }

            void appendSiloInfo(Silo silo) {
                builder.append("Silo ").append(i + 1).append(": ");

                String grainName = (silo.getGrainType() != null) ? silo.getGrainType().toString() : "EMPTY";
                builder.append(grainName).append("\n");

                int fillLevel = silo.getFillLevel();
                int capacity = silo.getCapacity();
                double fillPercentage = (double) fillLevel / capacity * 100;
                double emptyPercentage = 100 - fillPercentage;

                // Absolute amount of grain
                builder.append("Amount of Grain: ").append(fillLevel).append(" units\n");

                // ASCII-ART representation of the fill level
                int fillBarLength = 20;
                int filledBars = (int) (fillPercentage / 100 * fillBarLength);
                int emptyBars = fillBarLength - filledBars;

                builder.append("|");
                for (int j = 0; j < filledBars; j++) {
                    builder.append("=");
                }
                for (int j = 0; j < emptyBars; j++) {
                    builder.append("-");
                }
                builder.append("| ").append(df.format(fillPercentage)).append("% filled\n");
                builder.append("Capacity: ").append(capacity).append(" units\n\n");
                i++;
            }
        }


        DepotVisualizer result = new DepotVisualizer();
        for (Silo silo : silos) {
            result.appendSiloInfo(silo);
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
            while(currentIndex < silos.length && silos[currentIndex].getGrainType() != grainType && silos[currentIndex].getFillLevel() > 0){
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
            return currentIndex < silos.length;
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
            Silo.Status status = silos[currentIndex].getStatus();
            currentIndex++;
            advanceIndex();
            return status;
        }
    }

}
