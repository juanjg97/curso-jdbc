package datasources;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ConnectiosTime {

    public static void main(String[] args) throws SQLException {
        ArrayList<Integer> numberOfConnections =
                new ArrayList<>(Arrays.asList(1, 10, 20,30,100,500));

        tryConnectionsDm(numberOfConnections);
        tryConnectionsPc(numberOfConnections);
    }

    static void tryConnectionsDm(ArrayList<Integer>numberOfConnections) throws SQLException {
        System.out.println("============Time Driver Manager Connection");
        for(Integer connections:numberOfConnections) {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i <connections; i++) {
                Connection h2Connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
                h2Connection.close();
            }
            System.out.println("Total time for: "
                    +connections
                    +" connections: "
                    +(System.currentTimeMillis()-startTime)+"ms");
        }
    }

    static void tryConnectionsPc(ArrayList<Integer>numberOfConnections) throws SQLException {
        System.out.println("============Time Using Pool Connection");
        for(Integer connections:numberOfConnections) {
            long startTime = System.currentTimeMillis();
            JdbcConnectionPool connectionPool =
                    JdbcConnectionPool.create("jdbc:h2:~/test","sa","");
            for (int i = 0; i <connections; i++) {
                Connection connection = connectionPool.getConnection();
                connection.close();
            }
            System.out.println("Total time for: "
                    +connections
                    +" connections: "
                    +(System.currentTimeMillis()-startTime)+"ms");
        }
    }
}
