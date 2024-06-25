import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * Class that represents a consumer of numbers.
 * @author Christian Petry, Zudong Xhang
 * @version 1.0
 */
public class Consumer {
    private HashMap<Integer, LinkedList<Long>> counter;
    private long baseNanos;


    /**
     * Constructor for objects of class Consumer
     */
    public Consumer() {
        this.counter = new HashMap<>();
        baseNanos = System.nanoTime();
    }
    

    /**
     * Consumes an integer, calculates the cross total, records the timestamp,
     * and updates the counter with the timestamp for the cross total.
     *
     * @param  i  the integer to consume
     */
    public void consume(int i) {
        int result ;
        long timestamp;

        result = crossTotal(i);
        timestamp = System.nanoTime() - baseNanos;
   
        System.out.println( "I consumed "                      + i + 
                            " and calculated the cross total " + result +
                            ".  I recorded the timestamp: "    + timestamp );

        if (counter.get(result) == null) {
            LinkedList<Long> longList = new LinkedList<>();
            longList.add(timestamp);
            counter.put(result, longList);
        } else {
            counter.get(result).add(timestamp);
        }
    }
    


    /**
     * Calculates the cross total of a given integer by summing up the digits of the integer.
     *
     * @param  i  the integer to calculate the cross total from
     * @return    the cross total of the integer
     */
    public int crossTotal( int i ) {
        int result = 0;
   
        while (0 != i ) {
            result += i % 10;            // add last digit to result
            i      /= 10;                // remove last digit 
        }
        return result;
    }
    
   
    /**
     * Returns the number of different results stored in the counter.
     *
     * @return the number of different results
     */
    public int numberOfDifferentResults() {
        return this.counter.keySet().size();
    }
    
   
    /**
     * A description of the entire Java function.
     *
     * @param  i  description of parameter
     * @return    description of return value
     */
    public int numberOfOccurrences( int i ) {
       if ( this.counter.containsKey( i ) ) {
        return this.counter.get( i ).size();
       }
       return 0;
    }
    
   
    /**
     * Returns a set of integers representing the cross totals in ascending order.
     *
     * @return a set of integers in ascending order
     */
    public Set<Integer> getCrossTotalsAscending() {
        return this.counter.keySet();
    }
    
   
    /**
     * Returns a set of integers representing the cross totals in descending order.
     *
     * @return a set of integers in descending order
     */
    public Set<Integer> getCrossTotalsDescending() {
        return this.counter.keySet().descendingSet();	
    }
    
   
    /**
     * Retrieves the timestamps for a specific result.
     *
     * @param  i the result for which to retrieve the timestamps
     * @return    a deque containing the timestamps for the specified result
     */
    public Deque<Long> getTimestampsForResult( int i ) {
        return this.counter.get( i );
    }
    
   
    /**
     * Retrieves the base nanoseconds value.
     *
     * @return the base nanoseconds value
     */
    public long getBaseNanos() {
        return this.baseNanos;
    }
}