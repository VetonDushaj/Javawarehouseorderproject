import java.util.*;
import java.io.*;

class Order {
    private int partID;
    private List<ProductQuantity> productList;
    private String partDescription;
    private double partTotalCost;
    private Date partDate;

    public Order(int partID, List<ProductQuantity> productList, String partDescription, Date partDate) {
        this.partID = partID;
        this.productList = productList;
        this.partDescription = partDescription;
        this.partDate = partDate;
        this.partTotalCost = getCost(productList);
    }

    public double getCost(List<ProductQuantity> productList) {
        double totalCost = 0.0;
        for (ProductQuantity pq : productList) {
            totalCost += pq.getProduct().getPrice() * pq.getQuantity();
        }
        return totalCost;
    }

    public int getPartID() { return partID; }
    public List<ProductQuantity> getProductList() { return productList; }
    public String getPartDescription() { return partDescription; }
    public double getPartTotalCost() { return partTotalCost; }
    public Date getPartDate() { return partDate; }
}

class Product {
    private String description;
    private String type;
    private double price;
    private int yearOfProduction;
    private String capacity;
    private String processing;

    public Product(String description, String type, double price, int yearOfProduction, String capacity, String processing) {
        this.description = description;
        this.type = type;
        this.price = price;
        this.yearOfProduction = yearOfProduction;
        this.capacity = capacity;
        this.processing = processing;
    }

    public String getDescription() { return description; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public int getYearOfProduction() { return yearOfProduction; }
    public String getCapacity() { return capacity; }
    public String getProcessing() { return processing; }
}

class ProductQuantity {
    private Product product;
    private int quantity;

    public ProductQuantity(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}

class Warehouse {
    private HashMap<String, ProductQuantity> storage = new HashMap<>();

    public ProductQuantity getProductQuantity(String description) {
        return storage.get(description);
    }

    public void addProductQuantity(Product product, int quantity) {
        storage.put(product.getDescription(), new ProductQuantity(product, quantity));
    }
}

class OrderTest {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();
        warehouse.addProductQuantity(new Product("Laptop", "Electronics", 1200.00, 2023, "16GB", "Intel"), 10);
        warehouse.addProductQuantity(new Product("Smartphone", "Electronics", 800.00, 2023, "128GB", "Qualcomm"), 20);
        warehouse.addProductQuantity(new Product("Tablet", "Electronics", 500.00, 2023, "64GB", "Apple"), 15);
        warehouse.addProductQuantity(new Product("Monitor", "Electronics", 300.00, 2023, "27inch", "LED"), 25);
        warehouse.addProductQuantity(new Product("Keyboard", "Electronics", 50.00, 2023, "Mechanical", "Cherry MX"), 50);

        List<ProductQuantity> customerProducts = new ArrayList<>();
        customerProducts.add(new ProductQuantity(new Product("Laptop", "Electronics", 1200.00, 2023, "16GB", "Intel"), 1));
        customerProducts.add(new ProductQuantity(new Product("Smartphone", "Electronics", 800.00, 2023, "128GB", "Qualcomm"), 2));
        customerProducts.add(new ProductQuantity(new Product("Mouse", "Electronics", 25.00, 2023, "Wireless", "Logitech"), 1));

        List<ProductQuantity> orderProducts = new ArrayList<>();
        for (ProductQuantity pq : customerProducts) {
            ProductQuantity warehouseProduct = warehouse.getProductQuantity(pq.getProduct().getDescription());
            if (warehouseProduct == null) {
                System.out.println("Product " + pq.getProduct().getDescription() + " is unavailable.");
            } else {
                orderProducts.add(pq);
            }
        }

        Collections.sort(orderProducts, Comparator.comparing(pq -> pq.getProduct().getDescription()));

        Order order = new Order(1, orderProducts, "Customer Order", new Date());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("order.txt"))) {
            writer.write("Order ID: " + order.getPartID() + "\n");
            writer.write("Order Date: " + order.getPartDate() + "\n");
            writer.write("Order Description: " + order.getPartDescription() + "\n");
            writer.write("Order Total Cost: $" + order.getPartTotalCost() + "\n");
            writer.write("Products:\n");
            for (ProductQuantity pq : order.getProductList()) {
                writer.write(pq.getProduct().getDescription() + ", Quantity: " + pq.getQuantity() + ", Cost: $" + pq.getProduct().getPrice() * pq.getQuantity() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}