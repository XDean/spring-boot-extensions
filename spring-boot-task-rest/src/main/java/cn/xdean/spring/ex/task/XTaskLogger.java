package cn.xdean.spring.ex.task;

public interface XTaskLogger {
  void error(String message);

  void warn(String message);

  void info(String message);
}
