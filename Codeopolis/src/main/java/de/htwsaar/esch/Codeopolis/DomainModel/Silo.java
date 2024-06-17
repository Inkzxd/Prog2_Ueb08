package de.htwsaar.esch.Codeopolis.DomainModel;

import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.*;
import java.util.Iterator;
import de.htwsaar.esch.Codeopolis.Util.LinkedList;
import java.io.Serializable;

/**
 * The Silo class represents a storage unit for a specific type of grain.
 */
public class Silo implements Serializable, Comparable<Silo>{
    private LinkedList<Harvest> stock;
    private final int capacity;
    private int fillLevel;

    @Override
    public int compareTo(Silo other) {
        return Integer.compare(this.fillLevel, other.fillLevel);
    }

    public class Status {
        private final int capacity;
        private final int fillLevel;

        private Status() {
            this.capacity = Silo.this.capacity;
            this.fillLevel = Silo.this.fillLevel;
        }

        public int getCapacity() {
            return capacity;
        }

        public int getFillLevel() {
            return fillLevel;
        }
    }

    /**
     * Constructs a Silo object with the specified initial capacity.
     *
     * @param capacity The initial capacity of the silo.x
     */
    public Silo(int capacity) {
        this.capacity = capacity;
        this.stock = new LinkedList<>();
        this.fillLevel = 0;
    }
    
    /**
     * Copy constructor for the Silo class.
     * Creates a new Silo object as a deep copy of another Silo object.
     * This constructor is used to ensure that each property of the Silo,
     * including mutable objects, is copied and independent of the original object.
     *
     * @param other The Silo object to copy.
     */
    public Silo(Silo other) {
        this.capacity = other.capacity;
        this.fillLevel = other.fillLevel;

        this.stock = new LinkedList<>();
        this.stock = other.getStockCopy();
    }

    protected LinkedList<Harvest> getStockCopy() {
        LinkedList<Harvest> copy = new LinkedList<Harvest>();
        stock.forEach(copy::addLast);
        return copy;
    }

    /**
     * Copies the stock from another Silo object to this one.
     * This method creates a deep copy of each Harvest object.
     *
     * @param other The Silo object to copy from.
     */
    private void copyStock(LinkedList<Harvest> other){
        Iterator<Harvest> othersIterator = other.iterator();
        while(othersIterator.hasNext()){
           this.stock.addLast(Harvest.createHarvest(othersIterator.next()));
        }
    }

    /**
     * Stores a harvest in the silo if there is available capacity.
     *
     * @param harvest The harvest to be stored in the silo.
     * @return The amount of grain that could not be stored due to capacity limitations.
     */
    public Harvest store(Harvest harvest) {
    	 // Check if the grain type matches the existing grain in the silo
        if (fillLevel > 0 && stock.get(0).getGrainType() != harvest.getGrainType()) {
            throw new IllegalArgumentException("The grain type of the given Harvest does not match the grain type of the silo");
        }
        
        // Check if there is enough space in the silo
        if (fillLevel >= capacity) {
            return harvest; // The silo is already full, cannot be stored
        }
        
        if(fillLevel < capacity) {
	        // Check if the entire harvest can be stored
	        int remainingCapacity = this.capacity - this.fillLevel;
	        if(harvest.getAmount() <= remainingCapacity) {
	        	this.stock.addLast(harvest);
	        	this.fillLevel += harvest.getAmount();
	        	return null;
	        }
	        else {
	        	// Split the harvest and store the remaining amount
	            Harvest remainingHarvest = harvest.split(remainingCapacity);
	            stock.addLast(remainingHarvest); // Store the remaining harvest in the current depot
	            this.fillLevel += remainingHarvest.getAmount();
	            return harvest; // Return the surplus amount
	        }
        }
        else {
            // Depot is full, return the amount of grain that could not be stored
            return harvest;
        }
    }
    
    /**
     * Empties the silo by removing all stored harvests and returning them.
     * 
     * @return An array containing all the removed harvests from the silo.
     *         If the silo is empty, an empty array is returned.
     */
    public LinkedList<Harvest> emptySilo() {
        if (this.stock.isEmpty()) {
            return null;
        } else {
            LinkedList<Harvest> removedHarvests = new LinkedList<Harvest>();
            this.stock.forEach(removedHarvests::addLast);
            this.stock.clear();
            this.fillLevel = 0;
            return removedHarvests;
        }
    }



    /**
     * Takes out a specified amount of grain from the silo.
     *
     * @param amount The amount of grain to be taken out.
     * @return The actual amount of grain taken out from the silo.
     */
    public int takeOut(int amount) {
        int takenAmount = 0;
        for (int i = 0; i < stock.size() && amount > 0; i++) {
            Harvest currentHarvest = stock.get(i);
            int taken = currentHarvest.remove(amount);
            amount -= taken;
            takenAmount += taken;

            if (currentHarvest.getAmount() == 0) {
                // Remove empty harvest
                stock.remove(i);
                i--; // Check the same index again, as a new harvest may have been moved here
            }
        }
        this.fillLevel -= takenAmount;
        return takenAmount;
    }

    /**
     * Gets the current fill level of the silo.
     *
     * @return The number of harvests currently stored in the silo.
     */
    public int getFillLevel() {
    	return this.fillLevel;
    }

    /**
     * Gets the capacity of the silo.
     *
     * @return The maximum number of harvests the silo can store.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Gets the grain type stored in the silo.
     *
     * @return A string representation of the grain type.
     */
    public Game.GrainType getGrainType() {
        // Assuming each silo stores only one type of grain, we can retrieve the grain type from the first stored harvest
        if (fillLevel > 0 && stock.get(0) != null) {
            return stock.get(0).getGrainType();
        }
        else {
            return null; 
        }
    }
    
    /**
     * Retrieves the number of harvests currently stored in the silo.
     *
     * @return The number of harvests stored in the silo.
     */
    public int getHarvestCount() {
        return this.stock.size();
    }
    
    /**
     * Simulates the decay of grain in all harvests stored in the silo over time.
     *
     * @param currentYear The current year used to calculate the decay.
     * @return The total amount of grain that decayed in all harvests in the silo.
     */
    public int decay(int currentYear) {
        double totalDecayAmount = this.stock.sum((harvest)-> (double) harvest.decay(currentYear));
        fillLevel -= totalDecayAmount;
        return (int) totalDecayAmount;
    }

    /**
     * Retrieves the status of the silo, including its capacity and fill level.
     *
     * @return A SiloStatus object representing the current state of the silo.
     */
    public Status getStatus() {
        return new Status();
    }
}

