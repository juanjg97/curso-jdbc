package org.example;

import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;

public class TransactionExample {

    public static void main(String[] args) throws FileNotFoundException, SQLException {

        try (
            Connection h2Connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
            PreparedStatement insertStatement = h2Connection.prepareStatement("INSERT INTO employee (name,last_name,salary) VALUES (?,?,?)");
            PreparedStatement selectStatement =  h2Connection.prepareStatement("SELECT * FROM employee");
        ) {
            RunScript.execute(h2Connection, new FileReader("src/main/resources/schema.sql"));
            h2Connection.setAutoCommit(false);

            ArrayList<Employee> employees = new ArrayList<>();
            employees.add(new Employee("Juan", "Jose", 123.45));
            employees.add(new Employee("Carlos", "Alejandro", 567.45));

            try{
                insertData(employees,insertStatement);
                h2Connection.commit();
                System.out.println("Commit successful");
            }catch(SQLException e){
                h2Connection.rollback();
                System.out.println("Rollingback because: " + e.getMessage());
            }finally{
                h2Connection.setAutoCommit(true);
            }

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


    public static void insertData(ArrayList<Employee> employees,
                                   PreparedStatement insertStatement) throws SQLException {
        for (Employee employee : employees) {
            insertStatement.setString(1, employee.getName());
            insertStatement.setString(2, employee.getLast_name());
            insertStatement.setDouble(3, employee.getSalary());
            insertStatement.executeUpdate();
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

    public String getLast_name() {
        return last_name;
    }

    public double getSalary() {
        return salary;
    }
}


