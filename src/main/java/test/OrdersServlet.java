package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import test.model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/orders")
public class OrdersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected static Long id = 1L;

    protected static Map<Long, Order> savedOrders = new HashMap<>();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] inputArray = new byte[request.getContentLength()];
        request.getInputStream().read(inputArray);
        String input = new String(inputArray).trim();
        Order order = objectMapper.readValue(input, Order.class);
        order.setId(id);
        savedOrders.put(id, order);
        id++;
        String output = objectMapper.writeValueAsString(order);

        response.setContentType("application/json");
        response.getWriter().print(output);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String receivedIdString = request.getParameter("id");
        Long receivedId;
        try {
            receivedId = Long.parseLong(receivedIdString);
        } catch (Exception e) {
            return;
        }

        Order order = savedOrders.get(receivedId);
        if (order == null) {
            return;
        }

        String output = new ObjectMapper().writeValueAsString(order);
        response.setContentType("application/json");
        response.getWriter().print(output);
    }
}
