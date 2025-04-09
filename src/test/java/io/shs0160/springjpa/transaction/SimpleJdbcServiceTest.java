package io.shs0160.springjpa.transaction;

import io.shs0160.springjpa.dao.SimpleCrudRepository;
import io.shs0160.springjpa.member.Member;
import io.shs0160.springjpa.transaction.inner.SimpleJdbcCrudTransactionRepository;
import io.shs0160.springjpa.transaction.inner.SimpleJdbcService;
import io.shs0160.springjpa.util.ConnectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SimpleJdbcServiceTest {

    SimpleCrudRepository repository;
    SimpleJdbcService simpleJdbcService;

    @BeforeEach
    void init(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                ConnectionUtil.MysqlDbConnectionConstant.URL,
                ConnectionUtil.MysqlDbConnectionConstant.USERNAME,
                ConnectionUtil.MysqlDbConnectionConstant.PASSWORD

        );
        repository = new SimpleJdbcCrudTransactionRepository(dataSource);

        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);


        simpleJdbcService = new SimpleJdbcService(repository, transactionManager);
    }


    @Test
    @DisplayName("정상 커밋")
    void commit() throws Exception {

        Member saveReq1 = new Member(0, "member100", "member100");
        Member saveReq2 = new Member(0, "member200", "member200");

        simpleJdbcService.logic1(saveReq1, false);
        simpleJdbcService.logic1(saveReq2, false);

    }

    @Test
    @DisplayName("롤백")
    void rollback() throws Exception {

        Member saveReq1 = new Member(0, "member100", "member100");
        Member saveReq2 = new Member(0, "member200", "member200");

        simpleJdbcService.logic1(saveReq1, true);
        simpleJdbcService.logic1(saveReq2, true);

    }

}