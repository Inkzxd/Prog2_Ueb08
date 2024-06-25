import java.util.Random;

/**
 * Class that represents a producer of numbers.	
 * @author Christian Petry, Zudong Xhang
 * @version 1.0
 */
public class Producer {
   // Random number generator
   private Random random;

   /**
    * Constructor for the producer.
    */
   public Producer () {
      this.random = new Random();
   }

   /**
    * Generates a random product number between 0 and 1000 and prints it to the console.
    * @return the generated product number
    */
    public int produce () {
      int product = random.nextInt(1001);

      System.out.println("Product: " + product);

      return product;
   }
}