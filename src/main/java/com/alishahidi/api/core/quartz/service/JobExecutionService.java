package com.alishahidi.api.core.quartz.service;

import com.alishahidi.api.core.quartz.entity.JobExecutionEntity;
import com.alishahidi.api.core.quartz.entity.JobStatus;
import com.alishahidi.api.core.quartz.repository.JobExecutionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobExecutionService {

    JobExecutionRepository jobExecutionRepository;

    public JobExecutionEntity createJobEntry(String jobName, String jobGroup, String ip, String serverIp, String username, String jobParameters) {
        JobExecutionEntity jobExecution = JobExecutionEntity.builder()
                .jobName(jobName)
                .jobGroup(jobGroup)
                .status(JobStatus.PENDING)
                .ip(ip)
                .serverIp(serverIp)
                .username(username)
                .jobParameters(jobParameters)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        return jobExecutionRepository.save(jobExecution);
    }

    public void updateJobStatus(Long jobId, JobStatus status, String errorMessage, String stackTrace) {
        JobExecutionEntity jobExecution = jobExecutionRepository.findById(jobId)
                .orElseThrow(() -> new IllegalStateException("Job execution not found"));

        jobExecution.setStatus(status);
        jobExecution.setCreatedAt(new Date());

        if (status == JobStatus.FAILED) {
            jobExecution.setErrorMessage(errorMessage);
            jobExecution.setStackTrace(stackTrace);
        }

        jobExecutionRepository.save(jobExecution);
    }
}
