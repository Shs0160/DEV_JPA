package io.shs0160.springjpa.transaction.inner;

import io.shs0160.springjpa.dao.SimpleCrudRepository;
import io.shs0160.springjpa.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@RequiredArgsConstructor
public class SimpleJdbcCrudTransactionRepository implements SimpleCrudRepository {

    private final DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private void closeConnection(Connection connection, Statement statement, ResultSet resultSet) {
        JdbcUtils.closeResultSet(resultSet);
        JdbcUtils.closeStatement(statement);
        JdbcUtils.closeConnection(connection);
    }

    @Override
    public Member save(Member member) throws SQLException {

        String sql = "insert into member (username, password) values (?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, member.getUsername());
            pstmt.setString(2, member.getPassword());

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();

            if ( rs.next() ) {
                int idx = rs.getInt(1);
                member.setMemberId(idx);
            }

            return member;

        } catch ( SQLException e ) {
            throw e;
        } finally {
            closeConnection(conn, pstmt, rs);
        }

    }

    @Override
    public Optional<Member> findById(Integer id) throws SQLException {

        String sql = "select * from member where member_id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if ( resultSet.next() ) {
                Member findMember = new Member(
                        resultSet.getInt("member_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
                return Optional.of(findMember);
            } else {
                return Optional.empty();
            }

        } catch ( SQLException e ) {
            throw e;
        } finally {
            closeConnection(connection, preparedStatement, resultSet);
        }

    }

    @Override
    public void update(Member member) {

        String sql = "update member set password = ? where member_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getPassword());
            pstmt.setInt(2, member.getMemberId());
            pstmt.executeUpdate();

        }catch (SQLException e){

        }finally {
            closeConnection(conn, pstmt, null);
        }

    }

    @Override
    public void remove(Integer id) {

        //soft Delete -> 논리적 삭제 (데이터는 남겨져있음)
        //Hard Delete -> 물리적 삭제 (데이터가 영구적으로 삭제됨)
        String sql = "delete from member where member_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            //DB에 결과를 반영
            pstmt.executeUpdate();

        }catch (SQLException e){

        }finally {
            closeConnection(conn, pstmt, null);
        }


    }
}
