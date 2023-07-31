package datasources;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ConnectiosTime {

    public static void main(String[] args) throws SQLException {
        ArrayList<Integer> numberOfConnections =
                new ArrayList<>(Arrays.asList(1, 10, 20,30,100));

        tryConnectionsDm(numberOfConnections);
        tryConnectionsH2Pc(numberOfConnections);
        tryConnectionsHikariPc(numberOfConnections);
        tryConnectionsDbcP2Pc(numberOfConnections);
        tryConnections3CPO(numberOfConnections);
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

    static void tryConnectionsH2Pc(ArrayList<Integer>numberOfConnections) throws SQLException {
        System.out.println("\n============Time Using H2 Pool Connection");
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

    static void tryConnectionsHikariPc(ArrayList<Integer>numberOfConnections) throws SQLException {
        System.out.println("\n============Time Using Hikari Pool Connection");
        for(Integer connections:numberOfConnections) {
            long startTime = System.currentTimeMillis();
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:h2:~/test");
            config.setUsername("sa");
            config.setPassword("");

            HikariDataSource hikariDatasource=new  HikariDataSource(config);

            for (int i = 0; i <connections; i++) {
                Connection connection = hikariDatasource.getConnection();
                connection.close();
            }
            hikariDatasource.close();

            System.out.println("Total time for: "
                    +connections
                    +" connections: "
                    +(System.currentTimeMillis()-startTime)+"ms");
        }
    }

    static void tryConnectionsDbcP2Pc(ArrayList<Integer>numberOfConnections) throws SQLException {
        System.out.println("\n============Time Using DbcP2");
        for(Integer connections:numberOfConnections) {
            long startTime = System.currentTimeMillis();
            BasicDataSource dbcpDatasource=new BasicDataSource();
            dbcpDatasource.setUrl("jdbc:h2:~/test");
            dbcpDatasource.setUsername("sa");
            dbcpDatasource.setPassword("");

            for (int i = 0; i <connections; i++) {
                Connection connection =dbcpDatasource.getConnection();
                connection.close();
            }
            dbcpDatasource.close();

            System.out.println("Total time for: "
                    +connections
                    +" connections: "
                    +(System.currentTimeMillis()-startTime)+"ms");
        }
    }

    static void tryConnections3CPO(ArrayList<Integer>numberOfConnections) throws SQLException {
        System.out.println("\n============Time Using 3CPO");
        for(Integer connections:numberOfConnections) {
            long startTime = System.currentTimeMillis();

            ComboPooledDataSource c3po= new ComboPooledDataSource();
            c3po.setJdbcUrl("jdbc:h2:~/test");
            c3po.setUser("sa");
            c3po.setPassword("");

            for (int i = 0; i <connections; i++) {
                Connection connection=c3po.getConnection();
                connection.close();
            }
            c3po.close();
            System.out.println("Total time for: "
                    +connections
                    +" connections: "
                    +(System.currentTimeMillis()-startTime)+"ms");
        }
    }
}
