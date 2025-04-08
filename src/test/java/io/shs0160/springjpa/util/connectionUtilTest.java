package io.shs0160.springjpa.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class connectionUtilTest {

    @Test
    @DisplayName("DB Connection Test")
    void testConnection() throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
    }

}