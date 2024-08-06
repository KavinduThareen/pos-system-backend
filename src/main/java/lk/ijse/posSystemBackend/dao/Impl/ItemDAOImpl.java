package lk.ijse.posSystemBackend.dao.Impl;

import lk.ijse.posSystemBackend.dao.ItemDAO;
import lk.ijse.posSystemBackend.dto.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;

public class ItemDAOImpl implements ItemDAO {


    private static final String SAVE_ITEM = "INSERT INTO items (code, name, qty, price) VALUES (?, ?, ?, ?)";
    public static String GET_ITEM = "SELECT * FROM items WHERE code=?";
    public static String UPDATE_ITEM = "UPDATE items SET name=?, qty=?, price=? WHERE code=?";
    public static String DELETE_ITEM = "DELETE FROM items WHERE code=?";



    @Override
    public String saveItem(ItemDTO itemDTO, Connection connection) throws Exception {
        try  {
            var ps = connection.prepareStatement(SAVE_ITEM);
            ps.setString(1, itemDTO.getCode());
            ps.setString(2, itemDTO.getName());
            ps.setString(3, itemDTO.getQty());
            ps.setString(4, itemDTO.getPrice());

            int result = ps.executeUpdate();
            if (result > 0) {
                return "{\"message\":\"Item saved successfully\"}";
            } else {
                return "{\"message\":\"Failed to save Item\"}";
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public boolean updateItem(String code, ItemDTO itemDTO, Connection connection) throws Exception {

        try {
            var ps = connection.prepareStatement(UPDATE_ITEM);
            ps.setString(1, itemDTO.getName());
            ps.setString(2, itemDTO.getQty());
            ps.setString(3, itemDTO.getPrice());
            ps.setString(4, code);
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public boolean deleteItem(String code, Connection connection) throws Exception {
        try (var ps = connection.prepareStatement(DELETE_ITEM)) {
            ps.setString(1, code);
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }
}
