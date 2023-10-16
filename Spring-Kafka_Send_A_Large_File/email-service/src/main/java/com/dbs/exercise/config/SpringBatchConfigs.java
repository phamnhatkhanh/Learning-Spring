package com.dbs.exercise.config;

import com.dbs.exercise.model.EmailInfo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfigs {

    @Bean
    public JobLauncher jobLauncher() {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return jobLauncher;
    }
    /**
     * Used to configure spring batch job

     * @param itemReader : Item reader of Spring batch step
     * @param itemProcessor : Item processor of Spring batch step
     * @param itemWriter : Item writer of Spring batch step
     * @return Configured Spring Batch job
     */
    @Bean
    public Job job(
                   ItemReader<EmailInfo> itemReader, ItemProcessor<EmailInfo, EmailInfo> itemProcessor,
                   ItemWriter<EmailInfo> itemWriter) {

        Step step = new StepBuilder("emailInfo-file-load")
                .<EmailInfo, EmailInfo> chunk(10)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        Job job = new JobBuilder("emailInfo-file-load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();

        return  job;
    }

    /**
     * Configures item reader for the batch job

     * @return Configured item reader to read file
     */
    @Bean
    public FlatFileItemReader<EmailInfo> flatFileItemReader() {
        FlatFileItemReader<EmailInfo> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("emailAddresses.csv"));
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setName("emailInfo-file-read");
        flatFileItemReader.setLineMapper(lineMapper());
        return  flatFileItemReader;
    }

    /**
     * Configures line mapper which maps file lines to objects
     * @return The configured line mapper
     */
    @Bean
    public LineMapper<EmailInfo> lineMapper() {
        DefaultLineMapper<EmailInfo> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setStrict(false);
        tokenizer.setNames(new String[] {"accountId", "emailAddress"});

        BeanWrapperFieldSetMapper<EmailInfo> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(EmailInfo.class);

        defaultLineMapper.setLineTokenizer(tokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }
}
