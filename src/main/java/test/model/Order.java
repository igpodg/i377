package test.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    @SequenceGenerator(name = "my_seq", sequenceName = "order_sequence", allocationSize = 1)
    private Long id;

    @Size(min = 2)
    @Column(name = "order_number")
    private String orderNumber;

    //@NotNull
    @Valid
    @ElementCollection(fetch = FetchType.EAGER) // fetch on first call
    @CollectionTable(
            name = "order_rows",
            joinColumns=@JoinColumn(name = "orders_id",
                    referencedColumnName = "id")
    )
    private List<OrderRow> orderRows;

    public Order() {}

    public Order(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Order(Long id, String orderNumber) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.orderRows = null;
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
        return "Order{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", orderRows=" + orderRows +
                '}';
    }
}
