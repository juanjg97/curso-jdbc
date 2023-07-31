package datasources;

import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        JdbcDataSource datasource=new JdbcDataSource();
        datasource.setURL("jdbc:h2:~/test");
        datasource.setUser("sa");
        datasource.setPassword("");

        try(
            Connection h2Connection=datasource.getConnection()
        ){
            RunScript.execute(h2Connection,new FileReader("src/main/resources/schema.sql"));
            System.out.println("Script Excecuted\n================================");
        }

    }
}
