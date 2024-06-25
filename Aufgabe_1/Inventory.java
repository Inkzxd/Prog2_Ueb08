import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Class that represents an inventory of products using an ArrayList.
 * @author Christian Petry, Zudong Xhang
 * @version 1.0
 */
public class Inventory {

    // ArrayList used to store the product objects
    private ArrayList<Product> inventory;
    
    /**
     * Constructor for an empty inventory.
     */
    public Inventory () {
        this.inventory = new ArrayList<>();
    }

    /**
     * Adds a product to the inventory.
     * @param  product  the product to be added
     * @throws IllegalArgumentException if a product with the same ID already exists in the inventory
     */
    public void addProduct(Product product) {
        findProductById(product.getProductId());
        if (findProductById(product.getProductId()) == null) {
            inventory.add(product);
        } else {
            throw new IllegalArgumentException("Product with this ID already exists");
        }
    }

    /**
     * Removes a product from the inventory.
     * @param  productId   the ID of the product to be removed
     * @return true if the product was successfully removed, false otherwise
     */
    public boolean removeProduct(int productId) {
        return inventory.remove(findProductById(productId));
    }

    /**
     * Finds a product in the inventory based on its ID.
     * @param  productId   the ID of the product to be found
     * @return the product with the specified ID, or null if not found
     */
    public Product findProductById (int productId) {
        for (Product product : inventory) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * Finds a list of products in the inventory based on the specified category.
     * @param  category  the category of products to search for
     * @return a list of products that match the specified category
     */
    public List<Product> findProductsByCategory (String category) {
        List<Product> productsByCategory = new ArrayList<>();
        for (Product product : inventory) {
            if (product.getCategory().equals(category)) {
                productsByCategory.add(product);
            }
        }
        return productsByCategory;
    }

    /**
     * Returns a list of all products in the inventory.
     * @return a list of Product objects representing all products in the inventory
     */
    public List<Product> getAllProducts () {
        return inventory;
    }

    /**
     * Sorts the products in the inventory by name in ascending order.
     * This method uses the `sort` method of the `List` interface to sort the products in the inventory
     * based on their names. It uses a lambda expression as the comparator for the sorting operation.
     * The lambda expression compares the names of two products using the `compareTo` method of the
     * `String` class.
     */
    public void sortProductsByName () {
        inventory.sort((product1, product2) -> product1.getName().compareTo(product2.getName()));
    }

    /**
     * Sorts the products in the inventory by price in ascending order.
     */
    public void sortProductsByPrice () {
        inventory.sort((product1, product2) -> Double.compare(product1.getPrice(), product2.getPrice()));
    }

    /**
     * Retrieves a list of products in the inventory with a quantity below the specified threshold.
     * @param  threshold  the minimum quantity required for a product to be considered low stock
     * @return a list of Product objects representing low stock products
     */
    public List<Product> getLowStockProducts (int threshold) {
        List<Product> lowStockProducts = new ArrayList<>();
        for (Product product : inventory) {
            if (product.getQuantity() < threshold) {
                lowStockProducts.add(product);
            }
        }
        return lowStockProducts;
    }

    /**
     * Filters the products in the inventory based on the given predicate and returns a list of filtered products.
     * @param  predicate  a predicate that determines which products should be included in the filtered list
     * @return a list of products that satisfy the given predicate
     */
    public List<Product> filterProducts (Predicate<Product> predicate) {
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : inventory) {
            if (predicate.test(product)) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    /**
     * Applies the specified operation to each product in the inventory.
     * @param  consumer  the operation to apply to each product
     */
    public void applyToProducts (Consumer<Product> consumer) {
        for (Product product : inventory) {
            consumer.accept(product);
        }
    }

    /**
     * Returns the size of the inventory.
     * @return the size of the inventory
     */
    public int getSize() {
        return inventory.size();
    }

    /**
     * Returns a string representation of the inventory, including the number of products and their details.
     * @return a string representation of the inventory
     */
    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("Inventory overview: \n").append("Number of products: ").append(getSize()).append("\n").append("------------------\n");
        for (Product product : inventory) {
            sb.append(product.toString()).append("\n------------------\n");
        }
        return sb.toString();
    }
}