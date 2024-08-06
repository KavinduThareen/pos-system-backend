package lk.ijse.posSystemBackend.Controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.posSystemBackend.dao.Impl.CustomerDAOImpl;
import lk.ijse.posSystemBackend.dao.Impl.ItemDAOImpl;
import lk.ijse.posSystemBackend.dto.CustomerDTO;
import lk.ijse.posSystemBackend.dto.ItemDTO;
import lk.ijse.posSystemBackend.util.Util;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/item")
public class Item extends HttpServlet {

    private Connection connection;
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
            var itemDAOImpl = new ItemDAOImpl();
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

//            itemDTO.setCode(Util.generateId());

            //Save data in the DB
            writer.write(itemDAOImpl.saveItem(itemDTO, connection));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }

    }




    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            List<ItemDTO> itemDTOS = new ArrayList<>();

            try (var stmt = connection.createStatement();
                 var rs = stmt.executeQuery(GET_ALL_ITEM)) {
                while (rs.next()) {
                    ItemDTO itemDTO = new ItemDTO();
                    itemDTO.setCode(rs.getString("code"));
                    itemDTO.setName(rs.getString("name"));
                    itemDTO.setQty(rs.getString("qty"));
                    itemDTO.setPrice(rs.getString("price"));
                    itemDTOS.add(itemDTO);
                }
            }

            jsonb.toJson(itemDTOS, writer);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter writer = resp.getWriter()) {
                writer.write("{\"message\": \"Database error\"}");
            }
            e.printStackTrace();
        }
    }







    private void sendErrorResponse(HttpServletResponse resp, int statusCode, String message) throws IOException {
        resp.setStatus(statusCode);
        try (PrintWriter writer = resp.getWriter()) {
            writer.write(String.format("{\"message\": \"%s\"}", message));
        }
    }









    // layed update
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid content type");
            return;
        }

        try (PrintWriter writer = resp.getWriter()) {
            ItemDAOImpl itemDAOImpl = new ItemDAOImpl();

            Jsonb jsonb = JsonbBuilder.create();

            String itemCode = req.getParameter("code");
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

            if (itemDAOImpl.updateItem(itemCode, itemDTO, connection)) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                writer.write("{\"message\": \"item updated successfully\"}");
            } else {
                sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Update failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while updating the customer.");
        }
    }





    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = resp.getWriter()) {
            String code = req.getParameter("code");
            ItemDAOImpl itemDAOImpl = new ItemDAOImpl();

            if (code == null || code.isEmpty()) {
                sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "item code is required");
                return;
            }

            if (itemDAOImpl.deleteItem(code, connection)) {
                writer.write("{\"message\": \"item deleted successfully\"}");
                resp.setStatus(HttpServletResponse.SC_OK);  // Changed from NO_CONTENT to OK
            } else {
                sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            }
        } catch (Exception e) {
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while deleting the item.");
            e.printStackTrace();
        }
    }









}
