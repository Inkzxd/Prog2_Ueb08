import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class InventoryDialog {

    private Scanner scanner;
    private Inventory inventory;

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
        inventory = new Inventory();
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
                System.out.println("Shutting down...");
                System.exit(0);
                break;
            default:
                throw new IllegalArgumentException("Invalid function");
        }
    }

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

    private void removeProduct () {
        if (inventory.getSize() == 0) {
            System.out.println("Inventory is empty");
            return;
        }
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        Product product = inventory.findProductById(productId);
        if (product == null) {
            System.out.println("Product not found");
            return;
        } else {
            inventory.removeProduct(productId);
            System.out.println("Product removed: \n" + product); 
        }
    }

    private void findProductById () {
        if (inventory.getSize() == 0) {
            System.out.println("Inventory is empty");
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

    private void showProductsByCategory () {
        if (inventory.getSize() == 0) {
            System.out.println("Inventory is empty");
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

    private void showAllProducts () {
        System.out.println(inventory);
    }

    private void sortProductsByName () {
        inventory.sortProductsByName();
        System.out.println(inventory);
    }

    private void sortProductsByPrice () {
        inventory.sortProductsByPrice();
        System.out.println(inventory);
    }

    private void showLowStockProducts () {
        System.out.println("Please enter stock threshold: ");
        int threshold = scanner.nextInt();
        System.out.println(inventory.getLowStockProducts(threshold));
    }

    private void filterProducts () {
        System.out.println("Please enter filter (name, category, price): ");
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
                System.out.println("Please enter price: ");
                double price = scanner.nextDouble();
                predicate = product -> product.getPrice() == price;
                break;
            default:
                throw new IllegalArgumentException("Invalid filter");
        }
        List<Product> filteredProducts = inventory.filterProducts(predicate);
        System.out.println("Filtered products: \n" + filteredProducts);
    }

    private void changePricesPercentage () {
        System.out.println("Please enter percentage: ");
        double percentage = scanner.nextDouble();
        inventory.applyToProducts(product -> product.setPrice(product.getPrice() * (1 + percentage / 100)));
        System.out.println("Prices for all products changed by " + percentage + "%");
    }

    public static void main (String[] args) {
        new InventoryDialog().start();
    }
    
}
