package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSSWORD);
        log.info("connection={}, close={}", con1, con1.getClass());
        log.info("connection={}, close={}", con2, con2.getClass());
    }

    @Test
    public void dataSourceDriverManager() throws Exception {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSSWORD);
        useDataSource(dataSource);
    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();
        log.info("connection={}, close={}", con1, con1.getClass());
        log.info("connection={}, close={}", con2, con2.getClass());
    }
}
