package io.shs0160.springjpa.transaction;

import com.zaxxer.hikari.HikariDataSource;
import io.shs0160.springjpa.member.Member;
import io.shs0160.springjpa.util.ConnectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.*;

@Slf4j
public class TransactionTests {
    Connection con = ConnectionUtil.getConnection();
    ResultSet rs;
    PreparedStatement stmt;


    @AfterEach
    void close(){

        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
        //에러에 대한 핸들링이 필요
        if(stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }

        if(con != null){
            try {
                con.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("DataBase와 연결시 auto commit 파라미터를 false로 해서 auto commit 끄기")
    void autoCommit_test() throws Exception{
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(ConnectionUtil.MysqlDbConnectionConstant.URL);
        dataSource.setUsername(ConnectionUtil.MysqlDbConnectionConstant.USERNAME);
        dataSource.setPassword(ConnectionUtil.MysqlDbConnectionConstant.PASSWORD);

        //dataSource.setAutoCommit(false);

        try{

            con = ConnectionUtil.getConnection();
            con.setAutoCommit(false);

            Member saveReq = new Member(0, "_test", "_test");
            String sql = "insert into member (username, password) values (?, ?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, saveReq.getUsername());
            stmt.setString(2, saveReq.getPassword());
            stmt.executeUpdate();



            con.commit();

        }catch (SQLException e){
            throw e;
        }
    }


}
