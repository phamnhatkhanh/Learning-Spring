package com.dbs.exercise.config;

import com.dbs.exercise.batch.CashTransactionProcessor;
import com.dbs.exercise.batch.CashTransactionWriter;
import com.dbs.exercise.model.CashTransaction;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfigs {


//    @Bean
//    public JobLauncher jobLauncher() {
//        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
//        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
//        return jobLauncher;
//    }


    /**
     * Configures item reader for the batch job
     * @return Configured item reader to read file
     */
    @Bean
    public FlatFileItemReader<CashTransaction> flatFileItemReader() {
        FlatFileItemReader<CashTransaction> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("inputTransactions.csv"));
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setName("transaction-file-read");
        flatFileItemReader.setLineMapper(lineMapper());
        return  flatFileItemReader;
    }

    /**
     * Configures line mapper which maps file lines to objects
     * @return The configured line mapper
     */
    @Bean
    public LineMapper<CashTransaction> lineMapper() {
        DefaultLineMapper<CashTransaction> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setStrict(false);
        tokenizer.setNames(new String[] {"accountId", "payOrRecieve", "amount", "currencyCode"});

        BeanWrapperFieldSetMapper<CashTransaction> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(CashTransaction.class);

        defaultLineMapper.setLineTokenizer(tokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }
    @Bean
    public CashTransactionProcessor processor() {
        return new CashTransactionProcessor();
    }
    @Bean
    public CashTransactionWriter writer(DataSource dataSource) {
        return new CashTransactionWriter(dataSource);
    }
    @Bean("stepPerson1")
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, CashTransactionWriter writer) {
        return new StepBuilder("transaction-file-load",jobRepository)
        .<CashTransaction, CashTransaction> chunk(10,transactionManager)
                .reader(flatFileItemReader())
                .processor(processor())
                .writer(writer)
                .build();

    }
    @Bean
    public Job runJob(JobRepository jobRepository,@Qualifier("stepPerson1") Step step1 ){
        return new JobBuilder("transaction-file-load",jobRepository)
                .flow(step1).end().build();
    }

}