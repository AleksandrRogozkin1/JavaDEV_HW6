package org.example.services;

import org.example.props.PropertyReader;
import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class Database {

    private static Database instance;
   private Connection connection;


    private Database(){
        try {
            String connectionURL = PropertyReader.getConnectionURL();
            String user=PropertyReader.getUserForH2();
            String password=PropertyReader.getPasswordH2();
            this.connection=DriverManager.getConnection(connectionURL);
            flywayMigration(connectionURL,user,password);
        }catch (SQLException ex){
            throw new RuntimeException("Connection exception!") ;
        }
    }

    public static Database getInstance(){
       if (instance == null) {
           instance = new Database();
       }
       return instance;
   }
   public Connection getConnection(){
       return connection;
   }
   public int executeUpdate(String query){
        try (Statement statement=instance.getConnection().createStatement()){
            return statement.executeUpdate(query);
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
   }
   public void closeConnection(){
       try {
           connection.close();
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
   }
  public void flywayMigration(String connectionURL,String user,String password){
      Flyway flyway = Flyway.configure().dataSource(connectionURL, user, password).load();
      flyway.migrate();
  }

}
