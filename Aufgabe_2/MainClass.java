import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class MainClass {

   public static void main (String[] args) {
      Scanner scanner = new Scanner(System.in);
      boolean useFifo;

      System.out.print("Use FIFO queue? (y/n): ");
      String answer = scanner.next();
      if (answer.charAt(0) == 'y') {
         useFifo = true;
      } else {
         useFifo = false;
      }
      System.out.println("Of which number should the number of occurrences be shown?");
      int number = scanner.nextInt();
      scanner.close();

      Queue<Integer> queue;
      if (useFifo) {
         queue = new LinkedList<>();
      } else {
         queue = new PriorityQueue<>();
      }

      Producer producer = new Producer();
      Consumer consumer = new Consumer();
      Random random = new Random();

      for (int i = 0; i < 10000; i++) {
         if (random.nextInt(2) > 0) {
            int value = producer.produce();
            queue.add(value);
            System.out.println("Produced: " + value); 
         } else {
            if (!queue.isEmpty()) {
               int value = queue.poll();
               consumer.consume(value);
               System.out.println("Consumed: " + value + ", Cross total = " + consumer.calculateCrosstotal(value));
            }
         }
      }
      
      System.out.println("Number of different results: " + consumer.numberOfDifferentResults());
      System.out.println("Number of occurrences of " + number + ": " + consumer.numberOfOccurrences(number));
      System.out.println("Cross totals ascending: " + consumer.getCrossTotalsAscending());
      System.out.println("Cross totals descending: " + consumer.getCrossTotalDescending());
      System.out.println("Timestamps for cross total "  + number +": " + consumer.getTimestamps(number));
   }  
}