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
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        Connection h2Connection = createConnection();

        //firstExample(h2Connection);
        secondExample(h2Connection);
    }

    static Connection createConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "sa");
        properties.setProperty("password", "");
        System.out.println("Connecting...");
        return DriverManager.getConnection("jdbc:h2:~/test",properties);
    }

    static void firstExample(Connection h2Connection) throws SQLException, FileNotFoundException {

        System.out.println("Connected"); //Ejecución de un script (específico para h2)
        System.out.println("Executing Script");
        RunScript.execute(h2Connection,new FileReader("src/main/resources/schema.sql"));
        System.out.println("Script Excecuted");

        //INSERT INTO
        PreparedStatement insertStatement = h2Connection.prepareStatement("INSERT INTO employee (name,last_name,salary) VALUES (?,?,?)");

        insertStatement.setString(1,"Juan");insertStatement.setString(2,"Jose");insertStatement.setDouble(3,123.45);
        int rowsAffected = insertStatement.executeUpdate();
        System.out.println("Rows affected: " + rowsAffected);

        insertStatement.setString(1,"Carlos");insertStatement.setString(2,"Alejandro");insertStatement.setDouble(3,567.45);
        rowsAffected = insertStatement.executeUpdate();
        System.out.println("Rows affected: " + rowsAffected);

        //DELETE FROM
        PreparedStatement deleteStatement = h2Connection.prepareStatement("DELETE FROM employee");

        int rowsDeleted = deleteStatement.executeUpdate();
        System.out.println("Rows deleted: " + rowsDeleted);

        deleteStatement.close();
        insertStatement.close();

        System.out.println("Closing...");
        h2Connection.close();
        System.out.println("Closed");
    }

    static void secondExample(Connection h2Connection) throws SQLException, FileNotFoundException {

        System.out.println("Connected");System.out.println("Executing Script");
        RunScript.execute(h2Connection,new FileReader("src/main/resources/schema.sql")); //Ejecución de un script (específico para h2)
        System.out.println("Script Excecuted");

        //INSERT INTO
        PreparedStatement insertStatement = h2Connection.prepareStatement("INSERT INTO employee (name,last_name,salary) VALUES (?,?,?)");

        insertStatement.setString(1,"Juan");insertStatement.setString(2,"Jose");insertStatement.setDouble(3,123.45);
        int rowsAffected = insertStatement.executeUpdate();
        System.out.println("Rows affected: " + rowsAffected);

        insertStatement.setString(1,"Carlos");insertStatement.setString(2,"Alejandro");insertStatement.setDouble(3,567.45);
        rowsAffected = insertStatement.executeUpdate();
        System.out.println("Rows affected: " + rowsAffected);

        insertStatement.close();

        PreparedStatement selectStatement =  h2Connection.prepareStatement("SELECT * FROM employee");


        ResultSet resultSet=selectStatement.executeQuery();
        while (resultSet.next()){
            System.out.printf("\nid: [%d] Name: [%s] LastName: [%s] Salary: [%f]",
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4)
            );
        }

        selectStatement.close();




        System.out.println("Closing...");
        h2Connection.close();
        System.out.println("Closed");
    }



}
