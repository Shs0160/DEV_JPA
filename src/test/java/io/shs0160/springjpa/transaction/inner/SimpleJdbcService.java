package io.shs0160.springjpa.transaction.inner;

import io.shs0160.springjpa.dao.SimpleCrudRepository;
import io.shs0160.springjpa.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SimpleJdbcService {

    private final SimpleCrudRepository repository;
    private final PlatformTransactionManager transactionManager;

    public void logic1(Member saveReq, boolean isRollback) throws Exception {
        TransactionStatus transaction = transactionManager.getTransaction(
                new DefaultTransactionDefinition()
                //기본적인 설정의 트랜잭션을 만들어줌


        );
        //트랜잭션을 서비스에서 관리할 수 있도록

        try{

            Member saved = repository.save(saveReq);
            Optional<Member> memberOptional = repository.findById(saved.getMemberId());

            Member findMember = memberOptional.orElseThrow();

            log.info(findMember.getUsername());

            if(isRollback){
                transactionManager.rollback(transaction);
                return;
            }
            transactionManager.commit(transaction);



        }catch (Exception e){
            transactionManager.rollback(transaction);
        }

    }

}
