import java.util.Queue;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.Iterator;


public class MainClass {
   private static final String AUFRUF = "Aufruf:  java Loesung20_1 { 'fifo' | 'prio' }" ;
   private static final String VERSUCH = "Versuchen Sie es noch einmal !!!" ;


   private Queue<Integer> tasks;
   private Producer producer;
   private Consumer consumer;
   private Random random;



   public MainClass (String choice) {
      if (choice.equalsIgnoreCase("fifo")) {
         tasks = new ArrayDeque<Integer>();
      } else {
         if (choice.equalsIgnoreCase( "prio" )) {
            tasks = new PriorityQueue<Integer>();
         } else {
            System.out.println( AUFRUF + "\n" + VERSUCH );
            System.exit(1);
         }
      }
      producer   = new Producer();
      consumer   = new Consumer();
      random = new Random();
   }
  
 
   public void start() {
      Set<Integer> results ;
      System.out.println( "------Start Producing and Consuming------" );

      for (int i = 0 ;  i < 10000 ;  i++) {
         if ( random.nextInt( 2 ) > 0 ) {
            tasks.add( Integer.valueOf( producer.produce() ));     // produce
         } else {
            try {
               consumer.consume( tasks.remove() );             // consume
            } catch(NoSuchElementException e){ 
               System.out.println( "The queue is empty" );
            }
         }
      }

      System.out.println( "------Done------" );
      System.out.println( "Here is the result:" );
    
      System.out.println( "Used Basetime in Nanos: " + consumer.getBaseNanos() );
   
      System.out.println( "Number of items that have not yet been consumed: "
                          + tasks.size() );

      System.out.println( "The following items have not been consumed: " );

      for (Integer i : tasks) {
         System.out.print( i + ", " );
      }
   
      System.out.println( "\n\n" + consumer.numberOfDifferentResults() +
                          " different cross totals have been calculated" );
   
      System.out.println( "\nThe following cross totals have been calculated " +
                          "(ascending order):" );
    
      results = consumer.getCrossTotalsAscending();

      for(Integer i : results) {
         System.out.print( i + ", " );
      }

      System.out.println( "\n\nThe following cross totals have been calculated " +
                          "(descending order):" );
    
      results = consumer.getCrossTotalsDescending();

      for(Integer i : results) {
         System.out.print( i + ", " );
      }

      System.out.println( "\n\nTimestamps per result " +
                          "(ascending timestamp order):\n" );

      for (Integer i : results) {
         Deque<Long> deque = consumer.getTimestampsForResult(i);
         Iterator<Long> iterator = deque.iterator();
    
         System.out.print( i + " ==>> " );

         while (iterator.hasNext()) {
            System.out.print( iterator.next() + ", ");
         }
         System.out.println( "\n" );
      }
      
      System.out.println( "Timestamps per result " +
                          "(descending timestamp order):\n" );

      for (Integer i : results) {
         Deque<Long> deque = consumer.getTimestampsForResult( i );
         Iterator<Long> iterator = deque.descendingIterator();
    
         System.out.print(i + " ==>> ");

         while (iterator.hasNext()) {
            System.out.print( iterator.next() + ", " );
         }
         System.out.println( "\n" );
      }
   }
  
   public static void main( String[] args ) throws InterruptedException {
      if (args.length == 0) {
         System.out.println(AUFRUF + "\n" + VERSUCH);
         System.exit(1);
      } else {
         MainClass loesung = new MainClass(args[0]);
         loesung.start();
      }
      System.exit( 0 );
   }
}