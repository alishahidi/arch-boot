package com.alishahidi.api.core.quartz.scheduler;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuartzJobManager {

    Scheduler scheduler;

    public void scheduleJob(String jobName, String jobGroup, Class<? extends Job> jobClass, JobDataMap jobData) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroup)
                .usingJobData(jobData)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName + "Trigger", jobGroup)
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();

        scheduler.start();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
