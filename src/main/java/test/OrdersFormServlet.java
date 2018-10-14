package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import test.model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/orders/form")
public class OrdersFormServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Order order = new Order();
        byte[] inputArray = new byte[request.getContentLength()];
        request.getInputStream().read(inputArray);
        String input = URLDecoder.decode(new String(inputArray), "UTF-8");
        String[] keysAndValues = input.split("&");
        for (String s : keysAndValues) {
            String[] keyAndValue = s.split("=");
            switch (keyAndValue[0]) {
                case "orderNumber":
                    order.setOrderNumber(keyAndValue[1]);
                    break;
            }
        }
        order = OrdersServlet.orderDao.insertOrder(order);

        response.setContentType("application/json");
        response.getWriter().print(order.getId());
    }
}
