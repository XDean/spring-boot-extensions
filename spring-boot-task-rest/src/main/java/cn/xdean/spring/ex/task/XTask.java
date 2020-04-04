package cn.xdean.spring.ex.task;

public interface XTask {
    String id();

    void run(XTaskLogger logger) throws Exception;

    default String cron() {
        return "";
    }
}
