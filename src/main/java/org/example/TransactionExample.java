package org.example;

import com.github.javafaker.Faker;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class TransactionExample {

    public static void main(String[] args) throws FileNotFoundException, SQLException {

        //Uso de Try with resources para cerrar automáticamente la conexión y los statements
        try (
                Connection h2Connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
                PreparedStatement insertStatement = h2Connection.prepareStatement("INSERT INTO employee (name,last_name,salary) VALUES (?,?,?)");
                PreparedStatement selectStatement = h2Connection.prepareStatement("SELECT * FROM employee");
        ) {
            //Sceipt para crear la tabla y se pone autocommit en false.
            RunScript.execute(h2Connection, new FileReader("src/main/resources/schema.sql"));
            h2Connection.setAutoCommit(false);

            //Ponemos un indice para hacer fallar la transacción y comprobar el rollback
            int tryErrorAt=8;

            insertData(createData(tryErrorAt),insertStatement,h2Connection);
            getData(h2Connection,selectStatement);

        }

    }

    public static ArrayList<Employee> createData(int tryErrorAt){
        Faker faker = new Faker();
        ArrayList<Employee> employees = new ArrayList<>();

        for(int i = 0; i <10;i++) {
            employees.add(
                    new Employee(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            (1000 + (5000 - 1000) * (new Random()).nextDouble())
                    )
            );
        }

        (employees.get(tryErrorAt)).setName(null);
        return employees;
    }

    public static void insertData(ArrayList<Employee> employees,
                                       PreparedStatement insertStatement,
                                       Connection h2Connection) throws SQLException {
        System.out.println("\n=================================\nInsert Data");
        Savepoint savePoint = null;
        int i=1;
        try{
            for (Employee employee : employees) {
                insertStatement.setString(1, employee.getName());
                insertStatement.setString(2, employee.getLast_name());
                insertStatement.setDouble(3, employee.getSalary());
                insertStatement.executeUpdate();
                savePoint=h2Connection.setSavepoint("SavePoint"+ i);
                i++;
            }

        }catch(SQLException e){
            String eMsg=e.getMessage();
            if(savePoint!=null){
                String sp= savePoint.getSavepointName();
                System.out.printf("\nRollingback at [%s] because: %s ",sp,eMsg);
                h2Connection.rollback(savePoint);
            }else{
                System.out.printf("\nRollingback because: %s ",eMsg);
                h2Connection.rollback();
            }
        }finally{
            if(savePoint != null){
                h2Connection.releaseSavepoint(savePoint);
            }
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

class Employee {
    private String name;
    private String last_name;
    private double salary;

    public Employee(String name, String last_name, double salary) {
        this.name = name;
        this.last_name = last_name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public double getSalary() {
        return salary;
    }
}


