package com.alishahidi.api.core.garbagecollector;

import com.alishahidi.api.core.util.ExecutorUtils;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GarbageCollectorManager {

    List<GarbageCollector> collectors;
    ScheduledExecutorService scheduler;

    @Autowired
    public GarbageCollectorManager(List<GarbageCollector> collectors_) {
        collectors = collectors_;
        scheduler = ExecutorUtils.scheduledThreadPool(collectors.size());
    }


    @PostConstruct
    public void init() {
        for (GarbageCollector collector : collectors) {
            Long interval = collector.getTime();
            if (interval != null && interval > 0) {
                scheduleGarbageCollection(collector, interval);
            }
        }
    }

    private void scheduleGarbageCollection(GarbageCollector collector, Long interval) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                collector.collect();
            } catch (Exception e) {
                System.out.println("Error in " + collector.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }, 0, interval, TimeUnit.MILLISECONDS);
    }
}
