import java.util.InputMismatchException;
import java.util.Scanner;

public class InventoryDialog {

    private Scanner scanner;
    private Product product;

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
    private static final int PROGRAM_EXIT = 0;

    public InventoryDialog () {
        scanner = new Scanner(System.in);
    }

    private void start () {
        Inventory inventory = new Inventory();
        int function = -1;
        do {
            try {
                function = userInput();
                executeFunction(function);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid input");
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        } while (function != PROGRAM_EXIT);
    }

    private int userInput () {
        System.out.println("-------- Main menu --------\n"              +
                            ADD_PRODUCT                                 + " - Add product\n"                +
                            REMOVE_PRODUCT                              + " - Remove product\n"             +
                            FIND_PRODUCT_BY_ID                          + " - Find product by ID\n"         +
                            SHOW_PRODUCTS_BY_CATEGORY                   + " - Show products by category\n"  +
                            SHOW_ALL_PRODUCTS                           + " - Show all products\n"          +
                            SORT_PRODUCTS_BY_NAME                       + " - Sort products by name\n"      +
                            SORT_PRODUCTS_BY_PRICE                      + " - Sort products by price\n"     +
                            SHOW_LOW_STOCK_PRODUCTS                     + " - Show low stock products\n"    +
                            FILTER_PRODUCTS                             + " - Filter products\n"            +
                            CHANGE_PRICES_PERCENTAGE                    + " - Change prices percentage\n"   +
                            PROGRAM_EXIT                                + " - Exit\n");
        return scanner.nextInt();
    }

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
                break;
            default:
                throw new IllegalArgumentException("Invalid function");
        }
    }

    private void addProduct () {
        // TODO
    }

    private void removeProduct () {
        // TODO
    }

    private void findProductById () {
        // TODO
    }

    private void showProductsByCategory () {
        // TODO
    }

    private void showAllProducts () {
        // TODO
    }

    private void sortProductsByName () {
        // TODO
    }

    private void sortProductsByPrice () {
        // TODO
    }

    private void showLowStockProducts () {
        // TODO
    }

    private void filterProducts () {
        // TODO
    }

    private void changePricesPercentage () {
        // TODO
    }

    public static void main (String[] args) {
        new InventoryDialog().start();
    }
    
}
