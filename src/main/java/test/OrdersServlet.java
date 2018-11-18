package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import test.model.Order;
import test.model.ValidationError;
import test.model.ValidationErrors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.io.IOException;
import java.util.*;

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

        response.setContentType("application/json");

        Set<ConstraintViolation<Order>> errorsSet = Validation
                .buildDefaultValidatorFactory().getValidator().validate(order);
        List<ConstraintViolation<Order>> errors = new ArrayList<>(errorsSet);
        if (!errors.isEmpty()) {
            boolean isSizeViolated = false;
            for (ConstraintViolation<Order> error : errors) {
                if (error.getPropertyPath().toString().equals("orderNumber") &&
                    error.getMessageTemplate().contains("javax.validation.constraints.Size")) {

                    isSizeViolated = true;
                }
            }
            if (isSizeViolated) {
                ValidationError tooShortNumberError = new ValidationError();
                tooShortNumberError.setCode("too_short_number");
                List<ValidationError> errorList = new ArrayList<>();
                errorList.add(tooShortNumberError);
                ValidationErrors validationErrors = new ValidationErrors();
                validationErrors.setErrors(errorList);

                String errorOutput = objectMapper.writeValueAsString(validationErrors);
                response.setStatus(400);
                response.getWriter().print(errorOutput);
                return;
            }
        }

        order = orderDao.insertOrder(order);
        String output = objectMapper.writeValueAsString(order);
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

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String receivedIdString = request.getParameter("id");
        Long receivedId;
        try {
            receivedId = Long.parseLong(receivedIdString);
        } catch (Exception e) {
            orderDao.deleteOrders();
            return;
        }

        orderDao.deleteOrderById(receivedId);
        response.setContentType("application/json");
    }
}
