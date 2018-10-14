package test.model;

public class OrderRow {
    private String itemName;
    private Integer quantity;
    private Integer price;

    public OrderRow() {}

    public OrderRow(String itemName, Integer quantity, Integer price) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItemName() {
        return this.itemName;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Integer getPrice() {
        return this.price;
    }

    @Override
    public String toString() {
        return "OrderRow{" +
                "itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
