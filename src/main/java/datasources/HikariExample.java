package datasources;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;


public class HikariExample {
    public static void main(String[] args) throws SQLException {

        tryConnectionsHikariPc();

    }

    static void tryConnectionsHikariPc() throws SQLException {

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:h2:~/test");
            config.setUsername("sa");
            config.setPassword("");
            config.setMaximumPoolSize(5);
            config.setConnectionTimeout(5000);

            HikariDataSource hikariDatasource = new HikariDataSource(config);

            Connection connection1 = hikariDatasource.getConnection();
            Connection connection2 = hikariDatasource.getConnection();
            Connection connection3 = hikariDatasource.getConnection();
            Connection connection4 = hikariDatasource.getConnection();
            Connection connection5 = hikariDatasource.getConnection();
            Connection connection6 = hikariDatasource.getConnection();
            connection1.close();
            connection2.close();
            connection3.close();
            connection4.close();
            connection5.close();
            connection6.close();

            hikariDatasource.close();

    }
}
