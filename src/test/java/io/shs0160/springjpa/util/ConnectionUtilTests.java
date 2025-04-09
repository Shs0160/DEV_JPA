package io.shs0160.springjpa.util;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class ConnectionUtilTests {

    @Test
    @DisplayName("DB 커넥션 테스트")
    void connectionTest() throws Exception {
        Connection conn = ConnectionUtil.getConnection();
        log.info("conn = {}", conn);

    }
    @Test
    @DisplayName("Pool")
    void test_1() throws Exception{
        //접속정보를 풀한테 넘겨줌 -> 대신 커넥션을 따와준다.
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                ConnectionUtil.MysqlDbConnectionConstant.URL,
                ConnectionUtil.MysqlDbConnectionConstant.USERNAME,
                ConnectionUtil.MysqlDbConnectionConstant.PASSWORD
        );

        Connection conn1 = dataSource.getConnection();
        Connection conn2 = dataSource.getConnection();

        log.info("conn1 = {}", conn1);
        log.info("conn2 = {}", conn2);
        conn1.close();
        conn2.close();


    }

    @Test
    @DisplayName("hikari")
    void hikari_test() throws Exception {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(ConnectionUtil.MysqlDbConnectionConstant.URL);
        hikariDataSource.setUsername(ConnectionUtil.MysqlDbConnectionConstant.USERNAME);
        hikariDataSource.setPassword(ConnectionUtil.MysqlDbConnectionConstant.PASSWORD);

        hikariDataSource.setMaximumPoolSize(5);

        Connection conn1 = hikariDataSource.getConnection();
        Connection conn2 = hikariDataSource.getConnection();
        Connection conn3 = hikariDataSource.getConnection();

        Thread.sleep(2000);
    }


}
