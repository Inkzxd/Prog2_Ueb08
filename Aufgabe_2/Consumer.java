import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * Class that represents a consumer of numbers.
 * @author Christian Petry, Zudong Xhang
 * @version 1.0
 */
public class Consumer {
    private HashMap<Integer, List<Long>> counter;

    /**
     * Constructor for objects of class Consumer
     */
    public Consumer() {
       this.counter = new HashMap<>();
    }

    /**
     * Consumes a value and updates the counter accordingly.
     * @param value the value to be consumed
     */
    public void consume (int value) {
        int crossTotal = calculateCrosstotal(value);
        long timestamp = System.currentTimeMillis();

        counter.putIfAbsent(crossTotal, new ArrayList<>());
        counter.get(crossTotal).add(timestamp);
    }

    /**
     * Calculates the cross total of a given number.
     * @param  value the number for which the cross total is to be calculated
     * @return the cross total of the given number
     */
    public int calculateCrosstotal  (int value) {
        int sum = 0;
        while (value > 0) {
            sum += value % 10;
            value /= 10;
        }
        return sum;
    }

    /**
     * Calculates the number of different results in the counter.
     * @return the number of different results
     */
    public int numberOfDifferentResults () {
        return counter.size();
    }

    /**
     * Calculates the number of occurrences of a given cross total in the counter.
     * @param  crossTotal the cross total for which the number of occurrences is to be calculated
     * @return the number of occurrences of the given cross total
     */
    public int numberOfOccurrences (int crossTotal) {
        return counter.get(crossTotal).size();
    }

    /**
     * Returns a collection of cross totals in ascending order.
     * @return a collection of integers representing the cross totals in ascending order
     */
    public TreeSet<Integer> getCrossTotalsAscending () {
        return new TreeSet<>(counter.keySet());
    }

    /**
     * Returns a collection of cross totals in descending order.
     * @return a collection of integers representing the cross totals in descending order
     */
    public TreeSet<Integer> getCrossTotalDescending () {
        TreeSet<Integer> sorted = new TreeSet<>(Collections.reverseOrder());
        sorted.addAll(counter.keySet());
        return sorted;
    }
    
    /**
     * Retrieves the collection of timestamps associated with the given cross total.
     *
     * @param  crossTotal the cross total for which the timestamps are to be retrieved
     * @return the collection of timestamps associated with the given cross total,
     *         or an empty collection if no timestamps are found
     */
    public Collection<Long> getTimestamps (int crossTotal) {
        return counter.get(crossTotal);
    }
}