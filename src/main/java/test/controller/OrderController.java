package test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import test.dao.OrderDao;
import test.model.Order;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderDao dao;

    @GetMapping(value = "orders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Order> getOrders() {
        return dao.findAll();
    }

    @GetMapping(value = "orders/{orderId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Order getOrderById(@PathVariable("orderId") Long orderId) {
        return dao.findById(orderId);
    }

    @PostMapping(value = "orders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Order save(@Valid @RequestBody Order order) {
        return dao.save(order);
    }

    @DeleteMapping(value = "orders/{orderId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteOrder(@PathVariable("orderId") Long orderId) {
        dao.delete(orderId);
    }

    @DeleteMapping(value = "orders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteAllOrders() {
        dao.deleteAll();
    }
}
