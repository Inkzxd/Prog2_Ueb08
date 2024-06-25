import java.text.DecimalFormat;

/**
 * Class to create a product object.
 * @author Christian Petry, Zudong Xhang
 * @version 1.0
 */
public class Product {
    // Attributes
    private int productId, quantity;
    private String name, category;
    private double price;

    // DecimalFormat used to format prices
    private DecimalFormat df = new DecimalFormat("#.00");

    /**
     * Creates a new Product object.
     * @param productId ID of the product   
     * @param name name of the product
     * @param category category of the product
     * @param price price of the product
     * @param quantity quantity of the product
     */
    public Product(int productId, String name, String category, double price, int quantity) {
        setProductId(productId);
        setName(name);
        setCategory(category);
        setPrice(price);
        setQuantity(quantity);
    }

    /**
     * Validates the given product ID.
     * @param  productId the ID of the product to be validated
     * @throws IllegalArgumentException if the product ID is less than 0
     */
    public static void validateProductId (int productId) {
        if (productId < 0) {
            throw new IllegalArgumentException("Die Produkt-ID muss groesser als 0 sein");
        }
    }

    /**
     * Validates the given name.
     * @param  name the name to be validated
     * @throws IllegalArgumentException if the name is null or empty
     */
    public static void validateName (String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Der Name darf nicht leer sein");
        }
    }

    /**
     * Validates the given category.
     * @param  category  the category to be validated
     * @throws IllegalArgumentException if the category is null or empty
     */
    public static void validateCategory (String category) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Die Produktkategorie darf nicht leer sein");
        }
    }

    /**
     * Validates the given price.
     * @param  price  the price to be validated
     * @throws IllegalArgumentException if the price is less than 0
     */
    public static void validatePrice (double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Der Preis muss hoeher als 0 sein");
        }
    }

    /**
     * Validates the given quantity.
     * @param  quantity the quantity to be validated
     * @throws IllegalArgumentException if the quantity is less than 0
     */
    public static void validateQuantity (int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Die Anzahl muss hoeher als 0 sein");
        }
    }

    /**
     * Retrieves the product ID.
     * @return the product ID
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Retrieves the name of the product.
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the category of the product.
     * @return the category of the product
     */
    public String getCategory() {
        return category;
    }

    /**
     * Retrieves the price of the product.
     * @return the price of the product as a double
     */
    public double getPrice() {
        return price;
    }

    /**
     * Retrieves the quantity of the product.
     * @return the quantity of the product
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the product ID after validating it.
     * @param  productId the ID of the product to be set
     * @throws IllegalArgumentException if the product ID is less than 0
     */
    public void setProductId(int productId) {
        validateProductId(productId);
        this.productId = productId;
    }

    /**
     * Sets the name of the object.
     * @param  name  the new name to be set
     * @throws IllegalArgumentException if the name is null or empty
     */
    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    /**
     * Sets the category of the object after validating it.
     * @param  category  the new category to be set
     */
    public void setCategory(String category) {
        validateCategory(category);
        this.category = category;
    }

    /**
     * Sets the price of the object after validating it.
     * @param  price  the new price to be set
     * @throws IllegalArgumentException if the price is less than 0
     */
    public void setPrice(double price) {
        validatePrice(price);
        this.price = price;
    }

    /**
     * Sets the quantity of the object after validating it.
     * @param  quantity  the new quantity to be set
     * @throws IllegalArgumentException if the quantity is less than 0
     */
    public void setQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    /**
     * Returns a string representation of the Product object.
     * @return a string representation of the Product object
     */
    @Override
    public String toString() {
        return "Product-Name: " + getName() + "\n" +
                "Produkt-ID: " + getProductId() + "\n" +
                "Kategorie: " + getCategory() + "\n" +
                "Preis: " + df.format(getPrice()) + " Euro\n" +
                "Anzahl: " + getQuantity();
    }
}