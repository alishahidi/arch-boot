package com.alishahidi.api.core.util;

import com.alishahidi.api.core.security.user.User;
import lombok.experimental.UtilityClass;
import org.quartz.JobDataMap;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.InetAddress;
import java.net.UnknownHostException;

@UtilityClass
public class QuartzUtils {

    public void setDefaultServerFields(JobDataMap jobDataMap) {
        jobDataMap.put("ip",  getServerIp());
        jobDataMap.put("serverIp", getClientIp());
        jobDataMap.put("username", getUsername());
    }

    private String getServerIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "0.0.0.0";
        }
    }

    private String getClientIp() {
        try {
            return IpUtils.getClientIp();
        } catch (Exception e) {
            return "0.0.0.0";
        }
    }

    private String getUsername() {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getUsername();
        } catch (Exception e) {
            return "unknown";
        }
    }
}
