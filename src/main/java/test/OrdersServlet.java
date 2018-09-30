package test;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/orders")
public class OrdersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Long id = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        byte[] inputArray = new byte[request.getContentLength()];
        request.getInputStream().read(inputArray);
        String input = new String(inputArray).trim();
        String output = String.format("{\"id\": \"%d\",", id);
        id++;
        String[] keysAndValues = input.trim().substring(1, input.length()-1).trim().split(",");
        for (String keyValue : keysAndValues) {
            String[] split = keyValue.trim().split("\"");
            String key = "";
            String value = "";

            int counter = 0;
            for (String string : split) {
                string = string.trim();
                if (string.isEmpty() || (string.length() == 1 && string.charAt(0) == ':'))
                    continue;
                if (string.charAt(0) == ':')
                    string = string.substring(1);
                if (counter % 2 == 0)
                    key = string;
                else
                    value = string.trim();
                counter++;
            }
            if (!value.equals("null"))
                output += String.format("\"%s\": \"%s\",", key, value);
        }
        output = output.replaceFirst(".$", "}");

        response.setContentType("application/json");
        response.getWriter().print(output);
    }
}
