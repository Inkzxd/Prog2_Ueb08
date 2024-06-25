import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class MainClass {

   public static void main (String[] args) {
      Scanner scanner = new Scanner(System.in);
      boolean useFifo;

      System.out.print("Soll FIFO verwendet werden? (y/n): ");
      String answer = scanner.next();
      if (answer.charAt(0) == 'y') {
         useFifo = true;
      } else {
         useFifo = false;
      }
      System.out.println("Von welchem Wert soll die Anzahl der Wiederholungen angezeigt werden?");
      int number = scanner.nextInt();
      if (number > 27) {
         System.out.println("Die groesstmoegliche Quersumme ist 27.");
         System.exit(1);
      }
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
               System.out.println("Consumed: " + value + ", Quersumme = " + consumer.calculateCrosstotal(value));
            }
         }
      }
      
      System.out.println("Anzahl verschiedener Ergebnisse: " + consumer.numberOfDifferentResults());
      System.out.println("Anzahl Vorkommen von " + number + ": " + consumer.numberOfOccurrences(number));
      System.out.println("Quersummen aufsteigend: " + consumer.getCrossTotalsAscending());
      System.out.println("Quersummen absteigend: " + consumer.getCrossTotalDescending());
      System.out.println("Zeitangaben der Quersummen von "  + number +": " + consumer.getTimestamps(number));
   }  
}