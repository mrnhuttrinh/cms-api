package com.ecash.cmsapi.jobs;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CardStatementJob
{
  private static final Logger LOGGER = LoggerFactory.getLogger(CardStatementJob.class);

  @Autowired
  private SimpleJobLauncher jobLauncher;

  @Autowired
  private Job processCardStatementJob;

  // @Scheduled(fixedDelay = 5000)
  @Scheduled(cron = "${cron.expression}")
  public void perform() throws Exception
  {
    LOGGER.info("Job Started at: %s", new Date());
    JobParameters param = new JobParametersBuilder().addString("CardStatementJob",
        String.valueOf(System.currentTimeMillis())).toJobParameters();
    JobExecution execution = jobLauncher.run(processCardStatementJob, param);
    LOGGER.info("Job finished with status :", execution.getStatus());
  }

}
