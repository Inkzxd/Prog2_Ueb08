import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class Consumer {
    private HashMap<Integer, LinkedList<Long>> counter;
    private long                               baseNanos;

 
    public Consumer() {
        this.counter = new HashMap<>();
        baseNanos = System.nanoTime();
    }
    

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
    


    public int crossTotal( int i ) {
        int result = 0;
   
        while (0 != i ) {
            result += i % 10;            // add last digit to result
            i      /= 10;                // remove last digit 
        }
        return result;
    }
    
   
    public int numberOfDifferentResults() {
        return this.counter.keySet().size();
    }
    
   
    public int numberOfOccurrences( int i ) {
       if ( this.counter.containsKey( i ) ) {
        return this.counter.get( i ).size();
       }
       return 0;
    }
    
   
    public Set<Integer> getCrossTotalsAscending() {
        return this.counter.keySet();
    }
    
   
    public Set<Integer> getCrossTotalsDescending() {
    }
    
   
    public Deque<Long> getTimestampsForResult( int i ) {
        return this.counter.get( i );
    }
    
   
    public long getBaseNanos() {
        return this.baseNanos;
    }
}