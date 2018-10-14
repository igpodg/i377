package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import test.model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/orders")
public class OrdersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected static OrderDao orderDao = new OrderDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] inputArray = new byte[request.getContentLength()];
        request.getInputStream().read(inputArray);
        String input = new String(inputArray).trim();
        Order order = objectMapper.readValue(input, Order.class);
        order = orderDao.insertOrder(order);
        String output = objectMapper.writeValueAsString(order);

        response.setContentType("application/json");
        response.getWriter().print(output);
    }

    private void doGetAllOrders(HttpServletRequest request, HttpServletResponse response)
            throws JsonProcessingException, IOException {

        List<Order> allOrders = orderDao.getOrderList();
        ObjectMapper objectMapper = new ObjectMapper();
        String output = objectMapper.writeValueAsString(allOrders);

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
            doGetAllOrders(request, response);
            return;
        }

        Order order = orderDao.getOrderById(receivedId);
        if (order == null) {
            doGetAllOrders(request, response);
            return;
        }

        String output = new ObjectMapper().writeValueAsString(order);
        response.setContentType("application/json");
        response.getWriter().print(output);
    }
}
