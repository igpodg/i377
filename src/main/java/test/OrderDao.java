package test;

import test.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    public Order insertOrder(Order order) {
        String sql = "insert into orders (id, orderNumber, orderRows) " +
                "values (next value for seq1, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DatabaseContextListener.DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, new String[] {"id"})) {

            pstmt.setString(1, order.getOrderNumber());
            pstmt.setString(2, null);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();

            return new Order(rs.getLong("id"), order.getOrderNumber());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order getOrderById(Long id) {
        String sql = "select id, orderNumber, orderRows from orders where id = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseContextListener.DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next()
                ? new Order(
                        rs.getLong("id"),
                        rs.getString("orderNumber"))
                : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Order> getOrderList() {
        List<Order> orderList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DatabaseContextListener.DATABASE_URL);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("select * from orders");
            while (rs.next()) {
                Order p = new Order(
                        rs.getLong("id"),
                        rs.getString("orderNumber"));
                orderList.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderList;
    }

}
