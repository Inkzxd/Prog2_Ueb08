import java.util.Random;

public class Producer {
    private Random random;

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