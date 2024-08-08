package lk.ijse.posSystemBackend.Controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.posSystemBackend.dao.Impl.CustomerDAOImpl;
import lk.ijse.posSystemBackend.dto.CustomerDTO;


import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



@WebServlet(urlPatterns = "/customer")
public class Customer extends HttpServlet {

    private Connection connection;
//    private static final String SAVE_CUS = "INSERT INTO customer (id, name, address, salory) VALUES (?, ?, ?, ?)";
//    public static String GET_CUS = "SELECT * FROM customer WHERE id=?";
//    public static String UPDATE_CUS = "UPDATE customer SET name=?, address=?, salory=?, WHERE id=?";
//    public static String DELETE_CUS = "DELETE FROM customer WHERE id=?";

    private static final String GET_ALL_CUS = "SELECT * FROM customer";


    @Override
    public void init() throws ServletException {
        try {
            var ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/RegisPortal");
            this.connection = pool.getConnection();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }


    //save
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//
//        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            try (PrintWriter writer = resp.getWriter()) {
//                writer.write("{\"message\": \"Invalid content type\"}");
//            }
//            return;
//        }
//
//        try (var reader = req.getReader(); var writer = resp.getWriter()) {
//            Jsonb jsonb = JsonbBuilder.create();
//            CustomerDTO customer = jsonb.fromJson(reader, CustomerDTO.class);
//
//            var ps = connection.prepareStatement(SAVE_CUS);
//            ps.setString(1, customer.getId());
//            ps.setString(2, customer.getName());
//            ps.setString(3, customer.getAddress());
//            ps.setString(4, customer.getSalory());
//
//            int result = ps.executeUpdate();
//            if (result > 0) {
//                resp.setStatus(HttpServletResponse.SC_CREATED);
//                writer.write("{\"message\": \"Customer saved successfully\"}");
//            } else {
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                writer.write("{\"message\": \"Failed to save customer\"}");
//            }
//        } catch (SQLException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            try (PrintWriter writer = resp.getWriter()) {
//                writer.write("{\"message\": \"Database error\"}");
//            }
//            e.printStackTrace();
//        }
//    }










    //layed support save


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try (PrintWriter writer = resp.getWriter()) {
                writer.write("{\"message\": \"Invalid content type\"}");
            }
            return;
        }
        try (var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            var customerDAOIMPL = new CustomerDAOImpl();
            CustomerDTO customer = jsonb.fromJson(req.getReader(), CustomerDTO.class);
//            customer.setId(Util.generateId());

            //Save data in the DB
            writer.write(customerDAOIMPL.saveCustomer(customer, connection));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }

    }

















    // not layed correct code load table
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            List<CustomerDTO> customers = new ArrayList<>();

            try (var stmt = connection.createStatement();
                 var rs = stmt.executeQuery(GET_ALL_CUS)) {
                while (rs.next()) {
                    CustomerDTO customerDTO = new CustomerDTO();
                    customerDTO.setId(rs.getString("id"));
                    customerDTO.setName(rs.getString("name"));
                    customerDTO.setAddress(rs.getString("address"));
                    customerDTO.setSalory(rs.getString("salory"));
                    customers.add(customerDTO);
                }
            }

            jsonb.toJson(customers, writer);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter writer = resp.getWriter()) {
                writer.write("{\"message\": \"Database error\"}");
            }
            e.printStackTrace();
        }
    }





// layed get exampel load tables

//@Override
//protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//    resp.setContentType("application/json");
//    resp.setCharacterEncoding("UTF-8");
//
//    try (PrintWriter writer = resp.getWriter()) {
//        CustomerDAOImpl customerDAOIMPL = new CustomerDAOImpl();
//        Jsonb jsonb = JsonbBuilder.create();
//
//
//        String customerId = req.getParameter("cusId");
//        if (customerId == null || customerId.isEmpty()) {
//            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required");
//            return;
//        }
//
//        CustomerDTO customer = customerDAOIMPL.getCustomer(customerId, connection);
//        if (customer.getId() == null) {
//            sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Customer not found");
//            return;
//        }
//
//        jsonb.toJson(customer, writer);
//        resp.setStatus(HttpServletResponse.SC_OK);
//    } catch (Exception e) {
//        sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while fetching the customer data.");
//        e.printStackTrace();
//    }
//}






//correct update
//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//
//        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
//            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid content type");
//            return;
//        }
//
//        try (var reader = req.getReader(); var writer = resp.getWriter()) {
//            Jsonb jsonb = JsonbBuilder.create();
//            CustomerDTO customer = jsonb.fromJson(reader, CustomerDTO.class);
//
//            if (customer.getId() == null) {
//                sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "ID is required for update");
//                return;
//            }
//
//            StringBuilder sql = new StringBuilder("UPDATE customer SET ");
//            List<Object> parameters = new ArrayList<>();
//
//            if (customer.getName() != null) {
//                sql.append("name=?, ");
//                parameters.add(customer.getName());
//            }
//            if (customer.getAddress() != null) {
//                sql.append("address=?, ");
//                parameters.add(customer.getAddress());
//            }
//            if (customer.getSalory() != null) {
//                sql.append("salory=?, ");
//                parameters.add(customer.getSalory());
//            }
//
//            sql.setLength(sql.length() - 2); // Remove the last comma and space
//            sql.append(" WHERE id=?");
//            parameters.add(customer.getId());
//
//            try (var ps = connection.prepareStatement(sql.toString())) {
//                for (int i = 0; i < parameters.size(); i++) {
//                    ps.setObject(i + 1, parameters.get(i));
//                }
//
//                int result = ps.executeUpdate();
//                if (result > 0) {
//                    writer.write("{\"message\": \"Customer updated successfully\"}");
//                } else {
//                    sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Customer not found");
//                }
//            }
//        } catch (SQLException e) {
//            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
//            e.printStackTrace();
//        }
//    }
//
    private void sendErrorResponse(HttpServletResponse resp, int statusCode, String message) throws IOException {
        resp.setStatus(statusCode);
        try (PrintWriter writer = resp.getWriter()) {
            writer.write(String.format("{\"message\": \"%s\"}", message));
        }
    }









    // layed update not work this
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid content type");
            return;
        }

        try (PrintWriter writer = resp.getWriter()) {
            CustomerDAOImpl customerDAOIMPL = new CustomerDAOImpl();

            Jsonb jsonb = JsonbBuilder.create();

            String customerId = req.getParameter("id");
            CustomerDTO customer = jsonb.fromJson(req.getReader(), CustomerDTO.class);

            if (customerDAOIMPL.updateCustomer(customerId, customer, connection)) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                writer.write("{\"message\": \"Customer updated successfully\"}");
            } else {
                sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Update failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while updating the customer.");
        }
    }







//delete
//    @Override
//    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//
//        String cusId = req.getParameter("id"); // Assuming 'id' is the parameter name
//
//        if (cusId == null || cusId.isEmpty()) {
//            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required");
//            return;
//        }
//
//        try (var ps = connection.prepareStatement(DELETE_CUS)) {
//            ps.setString(1, cusId);
//            int result = ps.executeUpdate();
//
//            if (result > 0) {
//                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
//                resp.getWriter().write("{\"message\": \"Customer deleted successfully\"}");
//            } else {
//                sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Customer not found");
//            }
//        } catch (SQLException e) {
//            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
//            e.printStackTrace();
//        }
//    }








    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = resp.getWriter()) {
            String cusId = req.getParameter("cusId");
            CustomerDAOImpl customerDAOIMPL = new CustomerDAOImpl();

            if (cusId == null || cusId.isEmpty()) {
                sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required");
                return;
            }

            if (customerDAOIMPL.deleteCustomer(cusId, connection)) {
                writer.write("{\"message\": \"Customer deleted successfully\"}");
                resp.setStatus(HttpServletResponse.SC_OK);  // Changed from NO_CONTENT to OK
            } else {
                sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            }
        } catch (Exception e) {
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while deleting the customer.");
            e.printStackTrace();
        }
    }





}


