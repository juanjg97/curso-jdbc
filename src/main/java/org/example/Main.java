package org.example;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.h2.tools.RunScript;

public class Main {
    public static void main(String[] args) {
        try {
            Properties properties = new Properties();
            properties.setProperty("user", "sa");
            properties.setProperty("password", "");
            System.out.println("Connecting...");

            Connection connection = DriverManager.getConnection("jdbc:h2:~/test",properties); // Establish the connection to the H2 database

            System.out.println("Connected"); //Ejecución de un script (específico para h2)

            System.out.println("Executing Script");
            RunScript.execute(connection,new FileReader("src/main/resources/schema.sql"));
            System.out.println("Script Excecuted");


            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT INTO employee (name,last_name,salary) VALUES (?,?,?)");

            insertStatement.setString(1,"Juan");
            insertStatement.setString(2,"José");
            insertStatement.setDouble(3,123.45);
            int rowsAffected = insertStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            insertStatement.setString(1,"Carlos");
            insertStatement.setString(2,"Alejandro");
            insertStatement.setDouble(3,567.45);
            rowsAffected = insertStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            PreparedStatement deleteStatement = connection
                    .prepareStatement("DELETE FROM employee");

            int rowsDeleted = deleteStatement.executeUpdate();
            System.out.println("Rows deleted: " + rowsDeleted);

            deleteStatement.close();
            insertStatement.close();

            System.out.println("Closing...");
            connection.close();
            System.out.println("Closed");
        } catch (SQLException e) {
            // Handle any exceptions that occur during the connection process
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
