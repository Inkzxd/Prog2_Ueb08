import java.util.Scanner;

public class InventoryDialog {

    private Scanner scanner = new Scanner(System.in);

    private void start () {
        System.out.println("Welcome to the inventory management system!");
        String input = scanner.nextLine();
        System.out.println("You entered: " + input);
    }

    public static void main (String[] args) {
        new InventoryDialog().start();
    }
    
}
