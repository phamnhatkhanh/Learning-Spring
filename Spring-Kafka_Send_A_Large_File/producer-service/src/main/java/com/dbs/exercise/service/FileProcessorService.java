package com.dbs.exercise.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileProcessorService {


	@Autowired
	private  JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@Autowired
	private KafkaProducerService kafkaProducerService;

	/**
	 * Processes cash transactions file using spring batch
	 * @return The status of the batch job
	 */
	public BatchStatus processInputTransactionsFile() {

		JobParameters parameters = new JobParametersBuilder()
				.addLong("Time", System.currentTimeMillis()).toJobParameters();

		try {
			JobExecution jobExecution = jobLauncher.run(job, parameters);

			System.out.println("File Reading Status : " + jobExecution.getStatus());
			System.out.println("File is being read..");
			while (jobExecution.isRunning()){
				System.out.print(".");
			}
			System.out.println("File Reading Status : " + jobExecution.getStatus());
			kafkaProducerService.publishNotification("Batch Completed");

			return jobExecution.getStatus();
		} catch (Exception e) {
			kafkaProducerService.publishNotification("Batch Failed");
			e.printStackTrace();
			return BatchStatus.FAILED;
		}
	}
}
