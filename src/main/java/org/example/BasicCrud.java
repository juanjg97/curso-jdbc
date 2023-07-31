package org.example;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.h2.tools.RunScript;

public class BasicCrud {
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        Connection h2Connection = createConnection();

        insertData(h2Connection);
        getData(h2Connection);
        deleteData(h2Connection);
        getData(h2Connection);


        System.out.println("Closing...");
        h2Connection.close();
        System.out.println("Closed");
    }

    static Connection createConnection() throws SQLException, FileNotFoundException {
        Properties properties = new Properties();
        properties.setProperty("user", "sa");
        properties.setProperty("password", "");

        System.out.println("Connecting...");
        Connection h2Connection = DriverManager.getConnection("jdbc:h2:~/test",properties);

        System.out.println("Connected"); //Ejecución de un script (específico para h2)
        System.out.println("Executing Script");
        RunScript.execute(h2Connection,new FileReader("src/main/resources/schema.sql"));
        System.out.println("Script Excecuted\n================================");
        return h2Connection;
    }

    //executeUpdate
    static void insertData(Connection h2Connection) throws SQLException {
        //INSERT INTO
        System.out.println("Inserting DATA");
        PreparedStatement insertStatement = h2Connection.prepareStatement("INSERT INTO employee (name,last_name,salary) VALUES (?,?,?)");

        //-----
        insertStatement.setString(1,"Juan");
        insertStatement.setString(2,"Jose");
        insertStatement.setDouble(3,123.45);
        int rowsAffected = insertStatement.executeUpdate(); //Uso de executeUpdate();
        System.out.println("Rows affected: " + rowsAffected);

        //-----
        insertStatement.setString(1,"Carlos");
        insertStatement.setString(2,"Alejandro");
        insertStatement.setDouble(3,567.45);

        boolean execute = insertStatement.execute(); //Si usamos execute necesitamos usar getUpdateCount()
        System.out.println("==>Is resultSet: " + execute);

        rowsAffected = insertStatement.getUpdateCount(); //Uso de getUpdateCount();
        System.out.println("Rows affected: " + rowsAffected);

        insertStatement.close();
    }

    static void getData(Connection h2Connection) throws SQLException {
        System.out.println("=================================\nGetting Data");
        PreparedStatement selectStatement =  h2Connection.prepareStatement("SELECT * FROM employee");

        boolean excecute = selectStatement.execute(); //Si usamos execute necesitamos usar getResultSet
        System.out.println("==>Is resultSet: " + excecute);

        //ResultSet resultSet=selectStatement.executeQuery(); //Uso de executeQuery
        ResultSet resultSet=selectStatement.getResultSet();   //Uso de getResultSet


        while (resultSet.next()){
            System.out.printf("id: [%d] Name: [%s] LastName: [%s] Salary: [%f]\n",
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4)
            );
        }
        selectStatement.close();
    }

    static void deleteData(Connection h2Connection) throws SQLException {
        System.out.println("=================================\nDeleting Data");
        //DELETE FROM
        PreparedStatement deleteStatement = h2Connection.prepareStatement("DELETE FROM employee");
        int rowsDeleted = deleteStatement.executeUpdate();
        System.out.println("Rows deleted: " + rowsDeleted);

        deleteStatement.close();
    }



}
