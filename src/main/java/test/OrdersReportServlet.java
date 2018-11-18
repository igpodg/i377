package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import test.model.Order;
import test.model.OrderReport;
import test.model.OrderRow;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/orders/report")
public class OrdersReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected static OrderDao orderDao = new OrderDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        OrderReport report = new OrderReport();
        List<Order> orderList = orderDao.getOrderList();
        report.setCount(orderList.size());
        int turnover = 0;
        for (Order order : orderList) {
            List<OrderRow> orderRows = order.getOrderRows();
            for (OrderRow orderRow : orderRows) {
                turnover += orderRow.getQuantity() * orderRow.getPrice();
            }
        }
        report.setTurnoverWithoutVAT(turnover);
        report.setAverageOrderAmount(turnover / orderList.size());
        int turnoverVat = (int) (turnover * 0.2);
        report.setTurnoverVAT(turnoverVat);
        report.setTurnoverWithVAT(turnover + turnoverVat);

        String output = new ObjectMapper().writeValueAsString(report);
        response.setContentType("application/json");
        response.getWriter().print(output);
    }
}
