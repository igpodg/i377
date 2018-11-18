package test;

import test.model.Order;
import test.model.OrderRow;
import test.util.DataSourceProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    public Order insertOrder(Order order) {
        String sql = "insert into orders (id, orderNumber) " +
                "values (next value for seq1, ?)";
        Long generatedId;

        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, new String[] {"id"})) {

            pstmt.setString(1, order.getOrderNumber());

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();

            generatedId = rs.getLong("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        sql = "insert into orderrows (id, itemName, quantity, price) " +
                "values (?, ?, ?, ?)";
        try (Connection conn = DataSourceProvider.getDataSource().getConnection()) {

            PreparedStatement pstmt;
            Order outputOrder = new Order(generatedId, order.getOrderNumber());

            List<OrderRow> allOrderRows = order.getOrderRows();
            if (allOrderRows != null) {
                for (OrderRow row : allOrderRows) {
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setLong(1, generatedId);
                    pstmt.setString(2, row.getItemName());
                    pstmt.setInt(3, row.getQuantity());
                    pstmt.setInt(4, row.getPrice());
                    pstmt.executeUpdate();
                    outputOrder.add(row);
                }
            }

            return outputOrder;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order getOrderById(Long id) {
        String sql = "select * from orders left join " +
                "orderrows on orders.id = orderrows.id where orders.id = ?";

        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next())
                return null;
            Order outputOrder = new Order(rs.getLong("id"), rs.getString("orderNumber"));
            do {
                String itemName = rs.getString("itemName");
                if (itemName != null) {
                    OrderRow newRow = new OrderRow(itemName,
                            rs.getInt("quantity"), rs.getInt("price"));
                    outputOrder.add(newRow);
                }
            } while (rs.next());

            return outputOrder;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Order> getOrderList() {
        List<Order> orderList = new ArrayList<>();
        String sql = "select * from orders left join " +
                "orderrows on orders.id = orderrows.id";

        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next())
                return orderList;
            boolean isNextAvailable = true;
            while (isNextAvailable) {
                long currentId = rs.getLong("id");
                Order newOrder = new Order(currentId, rs.getString("orderNumber"));
                do {
                    String itemName = rs.getString("itemName");
                    if (itemName != null) {
                        OrderRow newRow = new OrderRow(itemName,
                                rs.getInt("quantity"), rs.getInt("price"));
                        newOrder.add(newRow);
                    }
                    isNextAvailable = rs.next();
                } while (isNextAvailable && rs.getLong("id") == currentId);
                orderList.add(newOrder);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderList;
    }

    public void deleteOrderById(Long id) {
        String sql1 = "delete from orderrows where id=?";

        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql1)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql2 = "delete from orders where id=?";

        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteOrders() {
        String sql1 = "delete from orderrows";

        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql2 = "delete from orders";

        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
