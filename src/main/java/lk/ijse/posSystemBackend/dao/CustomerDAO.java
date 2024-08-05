package lk.ijse.posSystemBackend.dao;

import lk.ijse.posSystemBackend.dto.CustomerDTO;

import java.sql.Connection;

public interface CustomerDAO {

    String saveCustomer(CustomerDTO customerDTO, Connection connection) throws Exception;
//    boolean updateCustomer(String id,CustomerDTO customerDTO,Connection connection) throws Exception;

    boolean deleteCustomer(String id, Connection connection) throws Exception;
    CustomerDTO getCustomer (String id,Connection connection) throws Exception;
}
