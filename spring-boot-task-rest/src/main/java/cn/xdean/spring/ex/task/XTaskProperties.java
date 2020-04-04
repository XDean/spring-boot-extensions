package cn.xdean.spring.ex.task;

import lombok.Data;

import java.time.Duration;

@Data
public class XTaskProperties {

    Cleanup cleanup;

    @Data
    public static class Cleanup {
        String cron = "0 0 0 * * *";
        Duration maxDuration = Duration.ofDays(7);
        int maxCountPerTask = 30;
    }
}
