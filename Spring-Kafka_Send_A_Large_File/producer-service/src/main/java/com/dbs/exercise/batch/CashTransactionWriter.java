package com.dbs.exercise.batch;

import com.dbs.exercise.model.CashTransaction;
import com.dbs.exercise.service.KafkaProducerService;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class CashTransactionWriter extends JdbcBatchItemWriter<CashTransaction>  {
    @Autowired
    KafkaProducerService kafkaProducerService;

    public CashTransactionWriter(DataSource dataSource) {
        setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        setSql("INSERT INTO Cash_Transaction (account_id, pay_or_receive, amount, currency_code) " +
                "VALUES (:accountId, :payOrReceive, :amount, :currencyCode)");
        setDataSource(dataSource);
    }

    /**
     * Write step of spring batch file process job
     * @param cashTransactions : List of processed batch items of file read job
     */
    @Override
    public void write(Chunk<? extends CashTransaction> cashTransactions) {
        cashTransactions.forEach(tx->{
            System.out.println("Cash Transaction from file written to kafka : " + tx.toString());
            kafkaProducerService.publishCashTransaction(tx);
        });
    }

}

