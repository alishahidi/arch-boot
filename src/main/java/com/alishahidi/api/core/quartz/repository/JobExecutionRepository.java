package com.alishahidi.api.core.quartz.repository;

import com.alishahidi.api.core.quartz.entity.JobExecutionEntity;
import com.alishahidi.api.core.quartz.entity.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobExecutionRepository extends JpaRepository<JobExecutionEntity, Long> {
    @Query("SELECT j FROM JobExecutionEntity j WHERE j.status = :status AND j.username = :username")
    List<JobExecutionEntity> findByStatusAndUsername(@Param("status") JobStatus status, @Param("username") String username);

    @Query("SELECT j FROM JobExecutionEntity j WHERE j.username = :username")
    List<JobExecutionEntity> findByUsername(@Param("username") String username);
}
