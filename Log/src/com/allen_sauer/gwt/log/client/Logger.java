package com.allen_sauer.gwt.log.client;

public interface Logger {
  void clear();

  void debug(String message, Throwable throwable);

  void error(String message, Throwable throwable);

  void fatal(String message, Throwable throwable);

  void info(String message, Throwable throwable);

  boolean isSupported();

  void warn(String message, Throwable throwable);
}