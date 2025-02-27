package com.alishahidi.api.core.quartz.repository;

import com.alishahidi.api.core.quartz.entity.JobExecutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobExecutionRepository extends JpaRepository<JobExecutionEntity, Long> {
}
