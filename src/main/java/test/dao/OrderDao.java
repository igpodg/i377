package test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import test.model.Order;
import test.model.OrderRow;

import java.sql.*;
import java.util.List;

@Repository
public class OrderDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Order save(Order order) {
        String sql = "insert into orders (id, orderNumber) " +
                "values (next value for seq1, ?)";

        GeneratedKeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement pstmt = conn.prepareStatement(sql, new String[] {"id"});
            pstmt.setString(1, order.getOrderNumber());

            return pstmt;
        }, holder);
        long generatedId = holder.getKey().longValue();
        order.setId(generatedId);
        if (order.getOrderRows() == null)
            return order;

        String sql2 = "insert into orderrows (id, itemName, quantity, price) " +
                "values (?, ?, ?, ?)";
        holder = new GeneratedKeyHolder();

        //System.out.println(order);
        jdbcTemplate.update(conn -> {
            PreparedStatement pstmt = null;
            List<OrderRow> allOrderRows = order.getOrderRows();
            for (int i = 0; i < allOrderRows.size(); i++) {
                pstmt = conn.prepareStatement(sql2);
                pstmt.setLong(1, generatedId);
                pstmt.setString(2, allOrderRows.get(i).getItemName());
                pstmt.setInt(3, allOrderRows.get(i).getQuantity());
                pstmt.setInt(4, allOrderRows.get(i).getPrice());
                if (i != (allOrderRows.size() - 1))
                    pstmt.executeUpdate();
            }
            return pstmt;
        }, holder);

        return order;
    }

    public List<Order> findAll() {
        String sql = "select id from orders";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            String sql2 = "select * from orders left join " +
                    "orderrows on orders.id = orderrows.id where orders.id = ?";
            return jdbcTemplate.queryForObject(sql2,
                    new Object[] {rs.getLong("id")},(rs2, rowNum2) -> {

                Order outputOrder = new Order(rs2.getLong("id"),
                        rs2.getString("orderNumber"));
                setRowsToOrder(rs2, outputOrder);

                return outputOrder;
            });
        });
    }

    private void setRowsToOrder(ResultSet rs2, Order outputOrder) throws SQLException {
        do {
            String itemName = rs2.getString("itemName");
            if (itemName != null) {
                OrderRow newRow = new OrderRow(itemName,
                        rs2.getInt("quantity"), rs2.getInt("price"));
                outputOrder.add(newRow);
            }
        } while (rs2.next());
    }

    public void delete(Long orderId) {
        String sql = "delete from orderrows where id=?";
        jdbcTemplate.update(sql, orderId);

        sql = "delete from orders where id=?";
        jdbcTemplate.update(sql, orderId);
    }

    public Order findById(Long orderId) {
        String sql = "select * from orders left join " +
                "orderrows on orders.id = orderrows.id where orders.id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] {orderId}, (rs, rowNum) -> {
            Order outputOrder = new Order(rs.getLong("id"), rs.getString("orderNumber"));
            setRowsToOrder(rs, outputOrder);

            return outputOrder;
        });
    }
}
