package com.alishahidi.api.core.quartz.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        name = "job_executions"
)
public class JobExecutionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String jobName;
    String jobGroup;

    @Enumerated(EnumType.STRING)
    JobStatus status;

    String ip;
    String serverIp;
    String username;
    String jobParameters;

    @Column(columnDefinition = "TEXT")
    String errorMessage;

    @Column(columnDefinition = "TEXT")
    String stackTrace;

    @CreationTimestamp
    Date createdAt;
    @UpdateTimestamp
    Date updatedAt;
}
