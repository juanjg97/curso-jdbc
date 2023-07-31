package datasources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ConnectiosTime {

    public static void main(String[] args) throws SQLException {
        ArrayList<Integer> numberOfConnections =
                new ArrayList<>(Arrays.asList(1, 10, 20,30,100,1000));

        tryConnections(numberOfConnections);
    }

    static void tryConnections(ArrayList<Integer>numberOfConnections) throws SQLException {
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
}
