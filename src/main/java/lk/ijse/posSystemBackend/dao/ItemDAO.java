package lk.ijse.posSystemBackend.dao;

import lk.ijse.posSystemBackend.dto.CustomerDTO;
import lk.ijse.posSystemBackend.dto.ItemDTO;

import java.sql.Connection;

public interface ItemDAO {

    String saveItem(ItemDTO itemDTO, Connection connection) throws Exception;
    boolean updateItem(String code,ItemDTO itemDTO,Connection connection) throws Exception;
    boolean deleteItem(String code, Connection connection) throws Exception;
}
