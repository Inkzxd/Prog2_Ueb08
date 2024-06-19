import java.util.HashMap;
import java.util.List;

public class Inventory {
    
    public Inventory () {
        List<Product> inventory = new HashMap();
    }

    public void addProduct(Product product) {
        inventory.add(product);
    }

    public boolean removeProduct(int productId) {
        for (Product product : inventory) {
            if (product.getProductId() == productId) {
                inventory.remove(product);
                return true;
            }
        }
        return false;
    }

    public Product findProductById (int productId) {
        for (Product product : inventory) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null;
    }

    public List<Product> findProductsByCategory (String category) {
        List<Product> productsByCategory = new HashMap();
        for (Product product : inventory) {
            if (product.getCategory().equals(category)) {
                productsByCategory.add(product);
            }
        }
        return productsByCategory;
    }

    public List<Product> getAllProducts () {
        return inventory;
    }

    public void sortProductsByName () {
        inventory.sort((product1, product2) -> product1.getName().compareTo(product2.getName()));
    }

    public void sortProductsByPrice () {
        inventory.sort((product1, product2) -> Double.compare(product1.getPrice(), product2.getPrice()));
    }

    public List<Product> getLowStockProducts (int threshold) {
        List<Product> lowStockProducts = new HashMap();
        for (Product product : inventory) {
            if (product.getQuantity() < threshold) {
                lowStockProducts.add(product);
            }
        }
        return lowStockProducts;
    }

    public List<Product> filterProducts (Predicate<Product> predicate) {
        List<Product> filteredProducts = new HashMap();
        for (Product product : inventory) {
            if (predicate.test(product)) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    public void applyToProducts (Consumer<Product> consumer) {
        for (Product product : inventory) {
            consumer.accept(product);
        }
    }
}
