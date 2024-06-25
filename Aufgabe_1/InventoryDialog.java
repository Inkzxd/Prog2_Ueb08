import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class InventoryDialog {

    // Scanner and inventory objects
    private Scanner scanner;
    private Inventory inventory;

    // Menu options as static final integers
    private static final int ADD_PRODUCT = 1;
    private static final int REMOVE_PRODUCT = 2;
    private static final int FIND_PRODUCT_BY_ID = 3;
    private static final int SHOW_PRODUCTS_BY_CATEGORY = 4;
    private static final int SHOW_ALL_PRODUCTS = 5;
    private static final int SORT_PRODUCTS_BY_NAME = 6;
    private static final int SORT_PRODUCTS_BY_PRICE = 7;
    private static final int SHOW_LOW_STOCK_PRODUCTS = 8;
    private static final int FILTER_PRODUCTS = 9;
    private static final int CHANGE_PRICES_PERCENTAGE = 10;
    private static final int PROGRAM_EXIT = 11;

    // Error messages
    private static final String INVALID_NUMBER = "Invalid number, please enter a number between " + ADD_PRODUCT + " and " + PROGRAM_EXIT + ".";
    private static final String INVALID_INPUT = "Invalid input, please enter a number.";
    private static final String INVENTORY_EMPTY = "Inventory is empty, please add a product first.";


    /**
     * Constructor, initializes scanner
     */
    public InventoryDialog () {
        scanner = new Scanner(System.in);
    }

    /**
     * Runs the main loop of the program, allowing the user to interact with the inventory.
     * The function continuously prompts the user for input, calls the appropriate function based on the input,
     * and handles any exceptions that may occur.
     * @throws IllegalArgumentException if an invalid number is entered
     * @throws InputMismatchException if the user enters an invalid input
     * @throws Exception if an unexpected error occurs
     */
    private void start () {
        inventory = new Inventory();
        int function = -1;
        do {
            try {
                function = userInput();
                executeFunction(function);
            } catch (InputMismatchException e) {
                System.err.println(INVALID_INPUT);
                return;
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        } while (function != PROGRAM_EXIT);
    }

    /**
     * Prompts the user for input to navigate the main menu of the program.
     * Displays the menu options based on the size of the inventory.
     * Returns the selected menu option as an integer.
     * @return the selected menu option as an integer
     * @throws InputMismatchException if the user enters an invalid input
     */
    private int userInput () {
        System.out.println("-------- Main menu --------\n"   +
        ADD_PRODUCT                             + " - Add product\n"                +
                                                "---------------------------");              
        if (inventory.getSize() > 0) {
            System.out.println(REMOVE_PRODUCT   + " - Remove product\n"             +
            FIND_PRODUCT_BY_ID                  + " - Find product by ID\n"         +
            SHOW_PRODUCTS_BY_CATEGORY           + " - Show products by category\n"  +
            SHOW_ALL_PRODUCTS                   + " - Show all products\n"          +
            SORT_PRODUCTS_BY_NAME               + " - Sort products by name\n"      +
            SORT_PRODUCTS_BY_PRICE              + " - Sort products by price\n"     +
            SHOW_LOW_STOCK_PRODUCTS             + " - Show low stock products\n"    +
            FILTER_PRODUCTS                     + " - Filter products\n"            +
            CHANGE_PRICES_PERCENTAGE            + " - Change prices percentage\n"   +
                                                  "---------------------------");
        }
        System.out.println(PROGRAM_EXIT         + " - Exit\n");
        System.out.print("Enter menu option: ");
        return scanner.nextInt();
    }

    /**
     * Executes the appropriate function based on the given function integer.
     * @param  function  the integer representing the function to execute
     * @throws IllegalArgumentException if the function integer is invalid
     */
    private void executeFunction (int function) {
        switch (function) {
            case ADD_PRODUCT:
                addProduct();
                break;
            case REMOVE_PRODUCT:
                removeProduct();
                break;
            case FIND_PRODUCT_BY_ID:
                findProductById();
                break;
            case SHOW_PRODUCTS_BY_CATEGORY:
                showProductsByCategory();
                break;
            case SHOW_ALL_PRODUCTS:
                showAllProducts();
                break;
            case SORT_PRODUCTS_BY_NAME:
                sortProductsByName();
                break;
            case SORT_PRODUCTS_BY_PRICE:
                sortProductsByPrice();
                break;
            case SHOW_LOW_STOCK_PRODUCTS:
                showLowStockProducts();
                break;
            case FILTER_PRODUCTS:
                filterProducts();
                break;
            case CHANGE_PRICES_PERCENTAGE:
                changePricesPercentage();
                break;
            case PROGRAM_EXIT: 
                shutdownProgram();
                break;
            default:
                System.err.println(INVALID_NUMBER);
        }
    }

    /**
     * Adds a new product to the inventory.
     * @throws InputMismatchException if the user input is not of the expected type
     */
    private void addProduct () {
        System.out.println("Enter product details:");
        System.out.print("Product ID: ");
        int productId = scanner.nextInt();
        System.out.print("Name: ");
        String name = scanner.next();
        scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.next();
        System.out.print("Price: ");
        double price = scanner.nextDouble();
        System.out.print("Quantity: ");
        int quantity = scanner.nextInt();
        Product product = new Product(productId, name, category, price, quantity);
        inventory.addProduct(product);
    }

    /**
     * Removes a product from the inventory based on the given product ID. If the inventory is empty, it prints a message. 
     * If the product is not found, it prints a message. Otherwise, it removes the product from the inventory and prints a confirmation message.
     */
    private void removeProduct () {
        if (inventory.getSize() == 0) {
            System.out.println(INVENTORY_EMPTY);
            return;
        }
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        Product product = inventory.findProductById(productId);
        if (product == null) {
            System.out.println("No product with ID " + productId + " found");
            return;
        } else {
            inventory.removeProduct(productId);
            System.out.println("Product removed: \n" + product); 
        }
    }

    /**
     * Finds a product in the inventory based on its ID. If the inventory is empty, it prints a message. 
     * If the product is not found, it prints a message. Otherwise, it prints the details of the found product.
     */
    private void findProductById () {
        if (inventory.getSize() == 0) {
            System.out.println(INVENTORY_EMPTY);
            return;
        }
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        Product product = inventory.findProductById(productId);
        if (product == null) {
            System.out.println("No product with ID " + productId + " found");
        } else {
            System.out.println("Product found: \n" + product);
        }
    }

    /**
     * Displays the products in the inventory that belong to a specified category.
     */
    private void showProductsByCategory () {
        if (inventory.getSize() == 0) {
            System.out.println(INVENTORY_EMPTY);
            return;
        }
        System.out.print("Enter category: ");
        String category = scanner.next();
        List<Product> products = inventory.findProductsByCategory(category);
        if (products.isEmpty()) {
            System.out.println("No products found in category " + category);
        } else {
            System.out.println("Products in category " + category + ": \n" + products.toString());
        }
    }

    /**
     * Displays all products in the inventory.
     */
    private void showAllProducts () {
        if (inventory.getSize() == 0) {
            System.out.println(INVENTORY_EMPTY);
            return;
        }
        System.out.println(inventory.toString());
    }

    /**
     * Sorts the products in the inventory by name in ascending order.
     * This method calls the sortProductsByName method of the inventory object
     * and prints the sorted inventory.
     */
    private void sortProductsByName () {
        if (inventory.getSize() == 0) {
            System.out.println(INVENTORY_EMPTY);
            return;
        }
        inventory.sortProductsByName();
        System.out.println(inventory.toString());
    }

    /**
     * Sorts the products in the inventory by price in ascending order.
     */
    private void sortProductsByPrice () {
        if (inventory.getSize() == 0) {
            System.out.println(INVENTORY_EMPTY);
            return;
        }
        inventory.sortProductsByPrice();
        System.out.println(inventory.toString());
    }

    /**
     * Prompts the user to enter a stock threshold and prints the low stock products
     * in the inventory based on that threshold.
     *
     * @return         	void
     */
    private void showLowStockProducts () {
        if (inventory.getSize() == 0) {
            System.out.println(INVENTORY_EMPTY);
            return;
        }
        System.out.println("Please enter stock threshold: ");
        int threshold = scanner.nextInt();
        System.out.println(inventory.getLowStockProducts(threshold));
    }

    /**
     * Prompts the user to enter a filter type (name, category, price, quantity) and filters the products in the inventory based on the user input.
     * The filtered products are then displayed to the user.
     */
    private void filterProducts () {
        if (inventory.getSize() == 0) {
            System.out.println(INVENTORY_EMPTY);
            return;
        }
        System.out.println("Please enter filter (name, category, price, quantity): ");
        String filter = scanner.next().toLowerCase();
        Predicate<Product> predicate;
        switch (filter) {
            case "name":
                System.out.print("Enter name: ");
                String name = scanner.next();
                predicate = product -> product.getName().equals(name);
                break;
            case "category":
                System.out.print("Enter category: ");
                String category = scanner.next();
                predicate = product -> product.getCategory().equals(category);
                break;
            case "price":
                System.out.println("Please enter minimum price: ");
                double price = scanner.nextDouble();
                predicate = product -> product.getPrice() >= price;
                break;
            case "quantity":
                System.out.println("Please enter minimum quantity: ");
                int quantity = scanner.nextInt();
                predicate = product -> product.getQuantity() >= quantity;
                break;
            default:
                throw new IllegalArgumentException("Invalid filter");
        }
        String filteredProducts = inventory.filterProducts(predicate).toString();
        System.out.println("Filtered products: \n" + filteredProducts.toString());
    }

    /**
     * Prompts the user to enter a percentage and changes the prices of all products in the inventory by the given percentage.
     */
    private void changePricesPercentage () {
        if (inventory.getSize() == 0) {
            System.out.println(INVENTORY_EMPTY);
            return;
        }
        System.out.println("Please enter percentage: ");
        double percentage = scanner.nextDouble();
        inventory.applyToProducts(product -> product.setPrice(product.getPrice() * (1 + percentage / 100)));
        System.out.println("Prices for all products changed by " + percentage + "%");
    }

    /**
     * Shuts down the program by printing a message, closing the scanner, and exiting the program with a status code of 0.
     */
    private void shutdownProgram () {
        System.out.println("Exiting program.");
        scanner.close();
        System.exit(0);
    }

    /**
     * The main method that creates a new instance of the InventoryDialog class and calls its start method.
     * @param  args  the command line arguments passed to the program
     */
    public static void main (String[] args) {
        new InventoryDialog().start();
    }
}
