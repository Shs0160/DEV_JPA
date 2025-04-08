package io.shs0160.springjpa.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class ConnectionUtil {

    public static class MysqlDbConnectionConstant {
        public static final String URL = "jdbc:mysql://localhost:3306/my_jdbc";
        public static final String USERNAME = "dev";
        public static final String PASSWORD = "10040160";

    }

    public static class H2DbConnectionConstant {
        public static final String URL = "jdbc:h2:./dev";
        public static final String USERNAME = "sa";
        public static final String PASSWORD = "";
    }

    public static Connection getConnection() {
        //접속 정보만 주면 우리 라이브러리에 추가 되어있는 드라이버를 찾아서 접속을 시도함
        try {
            Connection connection = DriverManager.getConnection(
                    MysqlDbConnectionConstant.URL,
                    MysqlDbConnectionConstant.USERNAME,
                    MysqlDbConnectionConstant.PASSWORD);
            log.info("connection = {}", connection);
            /*Connection connection = DriverManager.getConnection(
                    H2DbConnectionConstant.URL,
                    H2DbConnectionConstant.USERNAME,
                    H2DbConnectionConstant.PASSWORD);
            log.info("connection = {}", connection);*/
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
