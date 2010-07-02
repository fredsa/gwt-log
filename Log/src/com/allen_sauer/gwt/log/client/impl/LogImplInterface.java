package com.allen_sauer.gwt.log.client.impl;

import com.google.gwt.core.client.JavaScriptObject;

import com.allen_sauer.gwt.log.client.Logger;
import com.allen_sauer.gwt.log.shared.LogRecord;

public interface LogImplInterface {

  public abstract void addLogger(Logger logger);

  public abstract void clear();

  public abstract void debug(String category, String message, JavaScriptObject e);

  public abstract void debug(String category, String message, Throwable e);

  /**
   * Diagnostic (internal) messages have an implied category of 'gwt-log'
   */
  public abstract void diagnostic(String message, Throwable e);

  public abstract void error(String category, String message, JavaScriptObject e);

  public abstract void error(String category, String message, Throwable e);

  public abstract void fatal(String category, String message, JavaScriptObject e);

  public abstract void fatal(String category, String message, Throwable e);

  public abstract int getCurrentLogLevel();

  public abstract String getCurrentLogLevelString();

  public abstract <T extends Logger> T getLogger(Class<T> clazz);

  public abstract int getLowestLogLevel();

  public abstract String getLowestLogLevelString();

  public abstract void info(String category, String message, JavaScriptObject e);

  public abstract void info(String category, String message, Throwable e);

  public abstract void init();

  public abstract boolean isDebugEnabled();

  public abstract boolean isErrorEnabled();

  public abstract boolean isFatalEnabled();

  public abstract boolean isInfoEnabled();

  public abstract boolean isLoggingEnabled();

  public abstract boolean isTraceEnabled();

  public abstract boolean isWarnEnabled();

  public abstract void log(LogRecord record);

  public abstract int setCurrentLogLevel(int level);

  public abstract void setUncaughtExceptionHandler();

  public abstract void trace(String category, String message, JavaScriptObject e);

  public abstract void trace(String category, String message, Throwable e);

  public abstract void warn(String category, String message, JavaScriptObject e);

  public abstract void warn(String category, String message, Throwable e);

}