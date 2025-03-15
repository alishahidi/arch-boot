package com.alishahidi.api.core.util;

import com.alishahidi.api.core.security.user.User;
import lombok.experimental.UtilityClass;
import org.quartz.JobDataMap;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@UtilityClass
public class QuartzUtils {

    public void setDefaultServerFields(JobDataMap jobDataMap) {
        jobDataMap.put("ip", getClientIp());
        jobDataMap.put("serverIp", getServerIp());
        jobDataMap.put("username", getUsername());
    }

    private static String getServerIp() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();

                if (iface.isLoopback() || iface.isVirtual() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address) {
                        String ip = addr.getHostAddress();
                        if (!ip.startsWith("127.") && !ip.startsWith("169.254.")) {
                            return ip;
                        }
                    }
                }
            }
        } catch (SocketException ignored) {
        }
        return "127.0.0.1";
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
