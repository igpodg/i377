package test.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
@Table(name = "order_rows")
public class OrderRow {
    @Column(name = "item_name")
    private String itemName;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    @Min(1)
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
