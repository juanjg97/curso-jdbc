package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class ShellExample {
    public static String readCommand() throws IOException {
        System.out.printf("\njuan's shell ==>");
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        return br.readLine();
    }

    public static void main(String[] args) throws SQLException, IOException {

        Connection h2Connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
        String command = readCommand();

        while (!"quit".equals(command)) {
            try {
                Statement statement = h2Connection.createStatement();
                boolean resultType = statement.execute(command);
                if (resultType) {
                    ResultSet rs = statement.getResultSet();
                    while (rs.next()) {
                        ResultSetMetaData rsMeta = rs.getMetaData();
                        for (int i = 1; i < rsMeta.getColumnCount()+1; i++) {

                            String value = rs.getString(i); // Utiliza el nombre de la columna
                            System.out.print("\t" + value);
                        }
                        System.out.println();
                    }
                } else {
                    System.out.println("Rows impacted: " + statement.getUpdateCount());
                }
            } catch (SQLException e) {
                System.out.printf("\nError %s excecuting statement: %s", e.getMessage(), command);
            }finally{
                command = readCommand();
            }

        }

        h2Connection.close();

    }
}
