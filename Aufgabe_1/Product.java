import java.text.DecimalFormat;

public class Product {
    private int productId, quantity;
    private String name, category;
    private double price;

    private DecimalFormat df = new DecimalFormat("#.00");

    public Product(int productId, String name, String category, double price, int quantity) {
        setProductId(productId);
        setName(name);
        setCategory(category);
        setPrice(price);
        setQuantity(quantity);
    }

    public static void validateProductId (int productId) {
        if (productId < 0) {
            throw new IllegalArgumentException("Product ID must be greater than 0");
        }
    }

    public static void validateName (String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be empty");
        }
    }

    public static void validateCategory (String category) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category must not be empty");
        }
    }

    public static void validatePrice (double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
    }

    public static void validateQuantity (int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProductId(int productId) {
        validateProductId(productId);
        this.productId = productId;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public void setCategory(String category) {
        validateCategory(category);
        this.category = category;
    }

    public void setPrice(double price) {
        validatePrice(price);
        this.price = price;
    }

    public void setQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product name: " + getName() + "\n" +
                "Product ID: " + getProductId() + "\n" +
                "Category: " + getCategory() + "\n" +
                "Price: " + df.format(getPrice()) + " Euro\n" +
                "Quantity: " + getQuantity();
    }

    public static void main(String[] args) {
        Product product = new Product(1, "Product 1", "Category 1", 10.0, 5);
        System.out.println(product.toString());
    }
}
