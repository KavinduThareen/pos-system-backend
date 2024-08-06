//package lk.ijse.posSystemBackend.Controller;
//
//import jakarta.json.bind.Jsonb;
//import jakarta.json.bind.JsonbBuilder;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lk.ijse.posSystemBackend.dto.CustomerDTO;
//import lk.ijse.posSystemBackend.dto.ItemDTO;
//
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//@WebServlet(urlPatterns = "/order")
//public class Orders extends HttpServlet {
//
//    private Connection connection;
//    private static final String GET_ALL_CUS = "SELECT * FROM customer";
//    private static final String GET_ALL_ITEM = "SELECT * FROM items";
//
//    @Override
//    public void init() throws ServletException {
//        try {
//            var ctx = new InitialContext();
//            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/RegisPortal");
//            this.connection = pool.getConnection();
//        } catch (SQLException | NamingException e) {
//            e.printStackTrace();
//            throw new ServletException(e);
//        }
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//
//        try (var writer = resp.getWriter()) {
//            Jsonb jsonb = JsonbBuilder.create();
//            List<CustomerDTO> customers = new ArrayList<>();
//
//            try (var stmt = connection.createStatement();
//                 var rs = stmt.executeQuery(GET_ALL_CUS)) {
//                while (rs.next()) {
//                    CustomerDTO customerDTO = new CustomerDTO();
//                    customerDTO.setId(rs.getString("id"));
//                    customerDTO.setName(rs.getString("name"));
//                    customerDTO.setAddress(rs.getString("address"));
//                    customerDTO.setSalory(rs.getString("salory"));
//                    customers.add(customerDTO);
//                }
//            }
//
//            // Ensure the JSON response is properly formatted and complete
//            String jsonResponse = jsonb.toJson(customers);
//            writer.write(jsonResponse);
//        } catch (SQLException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            try (PrintWriter writer = resp.getWriter()) {
//                writer.write("{\"message\": \"Database error\"}");
//            }
//            e.printStackTrace();
//        }
//    }
//}
//
//
//
//









package lk.ijse.posSystemBackend.Controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.posSystemBackend.dto.CustomerDTO;
import lk.ijse.posSystemBackend.dto.ItemDTO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/order")
public class Orders extends HttpServlet {

    private Connection connection;
    private static final String GET_ALL_CUS = "SELECT * FROM customer";
    private static final String GET_ALL_ITEM = "SELECT * FROM items";

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type"); // Expecting 'type' to differentiate between customers and items

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();

            if ("customer".equals(type)) {
                List<CustomerDTO> customers = new ArrayList<>();
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(GET_ALL_CUS)) {
                    while (rs.next()) {
                        CustomerDTO customerDTO = new CustomerDTO();
                        customerDTO.setId(rs.getString("id"));
                        customerDTO.setName(rs.getString("name"));
                        customerDTO.setAddress(rs.getString("address"));
                        customerDTO.setSalory(rs.getString("salory"));
                        customers.add(customerDTO);
                    }
                }
                String jsonResponse = jsonb.toJson(customers);
                writer.write(jsonResponse);
            } else if ("item".equals(type)) {
                List<ItemDTO> itemDTOS = new ArrayList<>();
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(GET_ALL_ITEM)) {
                    while (rs.next()) {
                        ItemDTO itemDTO = new ItemDTO();
                        itemDTO.setCode(rs.getString("code"));
                        itemDTO.setName(rs.getString("name"));
                        itemDTO.setQty(rs.getString("qty"));
                        itemDTO.setPrice(rs.getString("price"));
                        itemDTOS.add(itemDTO);
                    }
                }
                String jsonResponse = jsonb.toJson(itemDTOS);
                writer.write(jsonResponse);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("{\"message\": \"Invalid type parameter\"}");
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter writer = resp.getWriter()) {
                writer.write("{\"message\": \"Database error\"}");
            }
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

























