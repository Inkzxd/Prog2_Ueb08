package de.htwsaar.esch.Codeopolis.DomainModel;

import de.htwsaar.esch.Codeopolis.DomainModel.LinkedList.LinkedListIterator;
import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.*;
import java.io.Serializable;

/**
 * The Silo class represents a storage unit for a specific type of grain.
 */
public class Silo implements Serializable, Comparable<Silo> {
    private LinkedList<Harvest> stock;
    private final int capacity;
    private int fillLevel;
    private LinkedList<Harvest>.LinkedListIterator harvestIterator;

    public class Status {
        private final int currentCapacity;
        private final int currentFillLevel;

        private Status() {
            this.currentCapacity = capacity;
            this.currentFillLevel = fillLevel;
        }

        public int getCurrentCapacity() {
            return currentCapacity;
        }

        public int getCurrentFillLevel() {
            return currentFillLevel;
        }
    }

    /**
     * Constructs a Silo object with the specified initial capacity.
     *
     * @param capacity The initial capacity of the silo.
     */
    public Silo(int capacity) {
        this.capacity = capacity;
        this.fillLevel = 0;
        this.stock = new LinkedList<>();
        this.harvestIterator = stock.iterator();
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

        for (int i = 0; i < other.stock.size(); i++) {
            this.stock.addLast(other.stock.get(i).copy());
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
        if (fillLevel > 0 && !stock.isEmpty() && stock.get(0).getGrainType() != harvest.getGrainType()) {
            throw new IllegalArgumentException("The grain type of the given Harvest does not match the grain type of the silo");
        }
        
        // Check if there is enough space in the silo
        if (fillLevel >= capacity) {
            return harvest; // The silo is already full, cannot be stored
        }
        
        int remainingCapacity = this.capacity - this.fillLevel;
        if(harvest.getAmount() <= remainingCapacity) {
            this.stock.addLast(harvest);
            this.fillLevel += harvest.getAmount();
            return null;
        } else {
            Harvest remainingHarvest = harvest.split(remainingCapacity);
            this.stock.addLast(remainingHarvest); // Store the remaining harvest in the current depot
            this.fillLevel += remainingHarvest.getAmount();
            return harvest; // Return the surplus amount
        }
    }
    
    /**
     * Empties the silo by removing all stored harvests and returning them.
     * 
     * @return An array containing all the removed harvests from the silo.
     *         If the silo is empty, an empty array is returned.
     */
    public LinkedList<Harvest> emptySilo() {
        if (stock.isEmpty()) {
            return new LinkedList<>();
        } else {
            LinkedList<Harvest> removedHarvests = new LinkedList<>();
            for (int i = 0; i < stock.size(); i++) {
                removedHarvests.addLast(stock.get(i));
            }
            stock.clear();
            fillLevel = 0;
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

        while (amount > 0 && !stock.isEmpty()) {
            Harvest currentHarvest = stock.get(0);
            int taken = currentHarvest.remove(amount);
            amount -= taken;
            takenAmount += taken;

            if (currentHarvest.getAmount() == 0) {
                stock.removeFirst();
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
        if (fillLevel > 0 && !stock.isEmpty()) {
            return stock.get(0).getGrainType();
        } else {
            return null;
        }
    }

    /**
     * Retrieves the number of harvests currently stored in the silo.
     *
     * @return The number of harvests stored in the silo.
     */
    public int getHarvestCount() {
        return stock.size();
    }

    /**
     * Simulates the decay of grain in all harvests stored in the silo over time.
     *
     * @param currentYear The current year used to calculate the decay.
     * @return The total amount of grain that decayed in all harvests in the silo.
     */
    public int decay(int currentYear) {
        int totalDecayedAmount = 0;
        while (harvestIterator.hasNext()) {
            Harvest currentHarvest = harvestIterator.next();
            totalDecayedAmount += currentHarvest.decay(currentYear);
        }
        fillLevel -= totalDecayedAmount;
        return totalDecayedAmount;
    }

    public Status getStatus() {
        return new Status();
    }

    @Override
    public int compareTo(Silo other) {
        if (this.fillLevel < other.fillLevel) {
            return -1;
        } else if (this.fillLevel > other.fillLevel) {
            return 1;
        } else {
            return 0;
        }
    }
}