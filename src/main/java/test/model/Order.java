package test.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
    private Long id;
    private String orderNumber;
    private List<OrderRow> orderRows;

    public Order() {}

    public Order(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<OrderRow> getOrderRows() {
        return this.orderRows;
    }

    public void add(OrderRow orderRow) {
        if (this.orderRows == null) {
            this.orderRows = new ArrayList<>();
        }

        this.orderRows.add(orderRow);
    }

    @Override
    public String toString() {
        return "test.model.Order{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", orderRows=" + orderRows +
                '}';
    }
}
