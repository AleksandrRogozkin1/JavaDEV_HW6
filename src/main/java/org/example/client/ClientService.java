package org.example.client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private static final String ADD_NEW_USER_WITH_NAME="INSERT INTO client (name) VALUES(?)";
    private static final String GET_BY_ID="SELECT id, name FROM client WHERE id = ?";
    private static final String SET_NAME_FOR_USER="UPDATE client SET name = ? WHERE id = ?";
    private static final String DELETE_USER_BY_ID="DELETE  FROM client WHERE id = ?";
    private static final String GET_ALL_CLIENTS="SELECT id, name FROM client ORDER BY id";
    private Connection connection;
    private PreparedStatement addNewUser;
    private PreparedStatement getUserByID;
    private PreparedStatement setNameForUser;
    private PreparedStatement deleteUserById;
    private PreparedStatement getAllClients;
    public ClientService(Connection connection){
        this.connection=connection;
        try {
            this.addNewUser=connection.prepareStatement(ADD_NEW_USER_WITH_NAME);
            this.getUserByID=connection.prepareStatement(GET_BY_ID);
            this.setNameForUser=connection.prepareStatement(SET_NAME_FOR_USER);
            this.deleteUserById=connection.prepareStatement(DELETE_USER_BY_ID);
            this.getAllClients=connection.prepareStatement(GET_ALL_CLIENTS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public long create(String name){
        if (name == null || name.length() < 3 || name.length() > 30) {
            throw new IllegalArgumentException("Invalid name: " + name);
        }
        try {
            this.addNewUser.setString(1,name);
            return this.addNewUser.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String findUserById(long id){
        try {
            this.getUserByID.setLong(1,id);
            ResultSet resultSet= this.getUserByID.executeQuery();
            return resultSet.next() ? resultSet.getString("name"):null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setName(long id, String name){
        try {
            this.setNameForUser.setString(1,name);
            this.setNameForUser.setLong(2,id);
            this.setNameForUser.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteById(long id){
        try {
            this.deleteUserById.setLong(1,id);
            this.deleteUserById.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Client> listAll(){
        List<Client> clients = new ArrayList<>();
        try ( ResultSet resultSet=this.getAllClients.executeQuery();){

            while (resultSet.next()){
                Client client=new Client(resultSet.getLong("id"),
                        resultSet.getString("name"));
                clients.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return clients;

    }

//public Optional<Client> findUserById(Long id) {
//    try {
//        this.getUserByID.setLong(1, id);
//        try (ResultSet resultSet = this.getUserByID.executeQuery()) {
//            if(resultSet.next()) {
//                Client user = new Client(resultSet.getLong("id"),
//                        resultSet.getString("name"));
//
//                return Optional.of(user);
//            }
//        } catch(SQLException e) {
//            System.out.println("Select user exception. Reason: " + e.getMessage());
//        }
//    } catch(SQLException e) {
//        System.out.println("Select user exception. Reason: " + e.getMessage());
//    }
//    return Optional.empty();
//}
}
