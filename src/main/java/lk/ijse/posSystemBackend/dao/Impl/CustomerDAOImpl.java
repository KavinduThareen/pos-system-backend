package lk.ijse.posSystemBackend.dao.Impl;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.posSystemBackend.dao.CustomerDAO;
import lk.ijse.posSystemBackend.dto.CustomerDTO;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAOImpl implements CustomerDAO {


    private static final String SAVE_CUS = "INSERT INTO customer (id, name, address, salory) VALUES (?, ?, ?, ?)";
    public static String GET_CUS = "SELECT * FROM customer WHERE id=?";
    public static String UPDATE_CUS = "UPDATE customer SET name=?, address=?, salory=? WHERE id=?";
    public static String DELETE_CUS = "DELETE FROM customer WHERE id=?";

    private static final String GET_ALL_CUS = "SELECT * FROM customer";


    @Override
    public String saveCustomer(CustomerDTO customerDTO, Connection connection) throws Exception {
        try  {
            var ps = connection.prepareStatement(SAVE_CUS);
            ps.setString(1, customerDTO.getId());
            ps.setString(2, customerDTO.getName());
            ps.setString(3, customerDTO.getAddress());
            ps.setString(4, customerDTO.getSalory());

            int result = ps.executeUpdate();
            if (result > 0) {
                return "{\"message\":\"Customer saved successfully\"}";
            } else {
                return "{\"message\":\"Failed to save customer\"}";
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public boolean deleteCustomer(String id, Connection connection) throws Exception {
        try (var ps = connection.prepareStatement(DELETE_CUS)) {
            ps.setString(1, id);
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }






//    @Override
//    public CustomerDTO getCustomer(String id, Connection connection) throws Exception {
//        try {
//            CustomerDTO customerDTO = new CustomerDTO();
//            PreparedStatement ps = connection.prepareStatement(GET_CUS);
//            ps.setString(1, id);
//            ResultSet rst = ps.executeQuery();
//            if (rst.next()) {
//                customerDTO.setId(rst.getString("id"));
//                customerDTO.setName(rst.getString("name"));
//                customerDTO.setAddress(rst.getString("address"));
//                customerDTO.setSalory(rst.getString("salory"));
//            }
//            return customerDTO;
//        } catch (Exception e) {
//            throw new SQLException(e.getMessage());
//        }
//    }





    @Override
    public boolean updateCustomer(String id, CustomerDTO customerDTO, Connection connection) throws Exception {

        try {
            var ps = connection.prepareStatement(UPDATE_CUS);
            ps.setString(1, customerDTO.getName());
            ps.setString(2, customerDTO.getAddress());
            ps.setString(3, customerDTO.getSalory());
            ps.setString(4, id);
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }









}
