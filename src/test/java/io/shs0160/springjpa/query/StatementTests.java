package io.shs0160.springjpa.query;

import io.shs0160.springjpa.member.Member;
import io.shs0160.springjpa.util.ConnectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class StatementTests {

    Connection con = ConnectionUtil.getConnection();
    Statement stmt;
    ResultSet rs;
    PreparedStatement pstmt;

    @BeforeEach
    void init(){
        con = ConnectionUtil.getConnection();
    }

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

        if(pstmt != null){
            try {
                pstmt.close();
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
    @DisplayName("JDBC - 회원가입")
    void insert_into_test() throws Exception {
        //쿼리입력

        Member admin = genMember("admin", "admin");
        Member member = genMember("member", "member");

        String sql1 = genInsertQuery(admin);
        String sql2 = genInsertQuery(member);

        stmt = con.createStatement();

        //파괴적인 행위 -> 값을 삭제, 삽입 등
        //비파괴적인 행위  -> 값 조회
        int resultRows = stmt.executeUpdate(sql1);
        log.info("Insert result: {}", resultRows);

        resultRows = stmt.executeUpdate(sql2);
        log.info("Insert result: {}", resultRows);

    }

    @Test
    @DisplayName("JDBC - 로그인")
    void select_test() throws Exception {
        //아이디랑 비밀번호가 일치해야함
        //로그인이 되었다면 -> row 조회 가능
        Member user1 = genMember("member", "member");
        Member user1_ng = genMember("member", "1234");
        String sql1 = genSelectQuery(user1);
        String sql2 = genSelectQuery(user1_ng);
        stmt = con.createStatement();
        //rs도 닫아줘야함
        rs = stmt.executeQuery(sql1);

        /*String findUsername = "";
        String findPassword = "";*/
        Member findMember = new Member();
        //커서의 처으은 컬럼명을 가리키고 있기 때문에
        //다음줄이 있는지 확인해야함
        if(rs.next()){
            findMember.setMemberId(rs.getInt("member_id"));
            findMember.setUsername(rs.getString("username"));
            findMember.setPassword(rs.getString("password"));

        }

        /*log.info("Select result: {}", findMember.getUsername());
        log.info("Select result: {}", findMember.getPassword());*/

        assertThat(findMember.getMemberId()).isEqualTo(2);
        assertThat(findMember.getUsername()).isEqualTo("member");
        assertThat(findMember.getPassword()).isEqualTo("member");
        rs.close();

        rs = stmt.executeQuery(sql2);
        findMember = new Member();
        //커서의 처으은 컬럼명을 가리키고 있기 때문에
        //다음줄이 있는지 확인해야함
        if(rs.next()){
            findMember.setMemberId(rs.getInt("member_id"));
            findMember.setUsername(rs.getString("username"));
            findMember.setPassword(rs.getString("password"));

        }
        assertThat(findMember.getUsername()).isNull();
        assertThat(findMember.getPassword()).isNull();
    }

    @Test
    @DisplayName("Statement Test, SQL Injection")
    void statement_test() throws Exception {
        Member admin = genMember("admin", "");
        String query = genSelectQuery(admin);
        stmt = con.createStatement();
        rs = stmt.executeQuery(query);

        Member findMember = new Member();
        if(rs.next()){
            findMember.setMemberId(rs.getInt("member_id"));
            findMember.setUsername(rs.getString("username"));
            findMember.setPassword(rs.getString("password"));
        }
        log.info("Find Member: {}", findMember);
        assertThat(findMember.getUsername()).isEqualTo("admin");
        assertThat(findMember.getPassword()).isEqualTo("admin");

    }

    @Test
    @DisplayName("test")
    void _test() throws Exception {
        Member nuSafe = genMember("admin", "' or '' = ''");
        String sql1 = "SELECT m.member_id, m.username, m.password FROM member as m WHERE m.username = ? AND m.password = ?";
        pstmt = con.prepareStatement(sql1);
        pstmt.setString(1, nuSafe.getUsername());
        pstmt.setString(2, nuSafe.getPassword());
        rs = pstmt.executeQuery();
        Member findMember = new Member();
        if(rs.next()){
            findMember.setMemberId(rs.getInt("member_id"));
            findMember.setUsername(rs.getString("username"));
            findMember.setPassword(rs.getString("password"));
        }
        log.info("Find Member: {}", findMember);

        assertThat(findMember.getUsername()).isNull();
        assertThat(findMember.getPassword()).isNull();

    }

    private static String genSelectQuery(Member member) {
        return "SELECT m.member_id, m.username, m.password FROM member as m WHERE m.username = '%s' AND m.password = '%s'".formatted(member.getUsername(), member.getPassword());
    }

    private static String genInsertQuery(Member admin) {
        return "INSERT INTO member (username, password) VALUES ('%s', '%s')".formatted(admin.getUsername(), admin.getPassword());
    }

    private static Member genMember(String username, String password) {
        return new Member(0, username, password);
    }

}
