package org.example;

import com.github.javafaker.Faker;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class TransactionExample2 {
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        //Uso de Try with resources para cerrar automáticamente la conexión y los statements
        try (
                Connection h2Connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
                PreparedStatement insertStatement = h2Connection.prepareStatement("INSERT INTO employee (name,last_name,salary) VALUES (?,?,?)");
                PreparedStatement selectStatement = h2Connection.prepareStatement("SELECT * FROM employee");
        ) {
            //Sceipt para crear la tabla y se pone autocommit en false.
            RunScript.execute(h2Connection, new FileReader("src/main/resources/schema.sql"));
            h2Connection.setAutoCommit(false);

            //Creación de los datos
        //Podemos ponert un indice para hacer fallar la transacción y comprobar el rollback

            Integer tryErrorAt=null;
            int dataSize =100;
            ArrayList<Employee> data = createData(dataSize,tryErrorAt);

            //Inserción y consulta de datos
            insertData(data,insertStatement,h2Connection);
            getData(h2Connection,selectStatement);

        }

    }

    public static ArrayList<Employee> createData(int datSize, Integer tryErrorAt){
        Faker faker = new Faker();
        ArrayList<Employee> employees = new ArrayList<>();

        for(int i = 0; i <datSize;i++) {
            employees.add(
                    new Employee(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            (1000 + (5000 - 1000) * (new Random()).nextDouble())
                    )
            );
        }
        if(tryErrorAt!=null){
            (employees.get(tryErrorAt)).setName(null);
        }
        return employees;
    }

    public static void insertData(ArrayList<Employee> employees,
                                  PreparedStatement insertStatement,
                                  Connection h2Connection) throws SQLException {
        System.out.println("\n=================================\nInsert Data");
        int i=1;
        try{
            for (Employee employee : employees) {
                insertStatement.setString(1, employee.getName());
                insertStatement.setString(2, employee.getLast_name());
                insertStatement.setDouble(3, employee.getSalary());
                insertStatement.addBatch();
                i++;
            }
            h2Connection.commit();
            int [] executeBatch = insertStatement.executeBatch();
            for (int j :executeBatch) {
                System.out.println("Rows impacted: "+j);
            }

        }catch(SQLException e){
            String eMsg=e.getMessage();
        }finally{
            h2Connection.setAutoCommit(true);
        }

    }

    public static void getData(Connection h2Connection,
                               PreparedStatement selectStatement) throws SQLException{
        System.out.println("\n=================================\nGetting Data");
        ResultSet resultSet=selectStatement.executeQuery(); //Uso de executeQuery

        while (resultSet.next()){
            System.out.printf("id: [%d] Name: [%s] LastName: [%s] Salary: [%f]\n",
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4)
            );
        }
    }

}



