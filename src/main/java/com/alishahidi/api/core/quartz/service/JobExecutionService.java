package com.alishahidi.api.core.quartz.service;

import com.alishahidi.api.core.quartz.api.JobExecutionDto;
import com.alishahidi.api.core.quartz.api.JobExecutionMapper;
import com.alishahidi.api.core.quartz.entity.JobExecutionEntity;
import com.alishahidi.api.core.quartz.entity.JobStatus;
import com.alishahidi.api.core.quartz.repository.JobExecutionRepository;
import com.alishahidi.api.core.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobExecutionService {

    JobExecutionRepository jobExecutionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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


    public ApiResponse<List<JobExecutionDto>> getJobsByStatus(JobStatus status) {
        String username = getAuthenticatedUsername();
        return ApiResponse.success(JobExecutionMapper.INSTANCE.toDTOList(jobExecutionRepository.findByStatusAndUsername(status, username)));
    }

    public ApiResponse<List<JobExecutionDto>> getAllJobsForUser() {

        String username = getAuthenticatedUsername();
        return ApiResponse.success(JobExecutionMapper.INSTANCE.toDTOList(jobExecutionRepository.findByUsername(username)));
    }

    private String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
