package org.example;

import org.example.client.ClientService;
import org.example.services.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection connection= Database.getInstance().getConnection();
        ClientService clientService= new ClientService(connection);
        clientService.create("abdur");

        clientService.setName(3,"NOTarsenal");
        String res=clientService.findUserById(3L);
        System.out.println("Result "+res);

        System.out.println("Result "+clientService.listAll());



    }
}
