/**
 * Copyright 2008 Fred Sauer.
 */
package com.allen_sauer.gwt.log.client;

import com.google.gwt.core.client.JavaScriptObject;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LogServerSide {
  public static final int LOG_LEVEL_DEBUG = 10000;
  public static final int LOG_LEVEL_ERROR = 40000;
  public static final int LOG_LEVEL_FATAL = 50000;
  public static final int LOG_LEVEL_INFO = 20000;
  public static final int LOG_LEVEL_OFF = Integer.MAX_VALUE;
  public static final int LOG_LEVEL_WARN = 30000;

  private static final Logger impl = Logger.getLogger("gwt-log");

  static {
    setCurrentLogLevel(LOG_LEVEL_DEBUG);
  }

  public static void addLogger(Logger logger) {
    throw new UnsupportedOperationException();
  }

  public static void clear() {
    // do nothing
  }

  public static void debug(String message) {
    debug(message, (Throwable) null);
  }

  public static void debug(String message, JavaScriptObject e) {
    throw new UnsupportedOperationException();
  }

  public static void debug(String message, Throwable e) {
    impl.debug(message, e);
  }

  public static void error(String message) {
    error(message, (Throwable) null);
  }

  public static void error(String message, JavaScriptObject e) {
    throw new UnsupportedOperationException();
  }

  public static void error(String message, Throwable e) {
    impl.error(message, e);
  }

  public static void fatal(String message) {
    fatal(message, (Throwable) null);
  }

  public static void fatal(String message, JavaScriptObject e) {
    throw new UnsupportedOperationException();
  }

  public static void fatal(String message, Throwable e) {
    impl.fatal(message, e);
  }

  public static ConsoleLogger getConsoleLogger() {
    throw new UnsupportedOperationException();
  }

  public static int getCurrentLogLevel() {
    return impl.getEffectiveLevel().toInt();
  }

  public static String getCurrentLogLevelString() {
    return impl.getEffectiveLevel().toString();
  }

  public static DivLogger getDivLogger() {
    throw new UnsupportedOperationException();
  }

  public static FirebugLogger getFirebugLogger() {
    throw new UnsupportedOperationException();
  }

  public static GWTLogger getGwtLogger() {
    throw new UnsupportedOperationException();
  }

  public static int getLowestLogLevel() {
    return Level.ALL_INT;
  }

  public static String getLowestLogLevelString() {
    return Level.ALL.toString();
  }

  public static SystemLogger getSystemLogger() {
    throw new UnsupportedOperationException();
  }

  public static void info(String message) {
    info(message, (Throwable) null);
  }

  public static void info(String message, JavaScriptObject e) {
    throw new UnsupportedOperationException();
  }

  public static void info(String message, Throwable e) {
    impl.info(message, e);
  }

  public static final boolean isDebugEnabled() {
    return impl.isDebugEnabled();
  }

  public static boolean isErrorEnabled() {
    return impl.getLevel().toInt() >= Level.ERROR_INT;
  }

  public static boolean isFatalEnabled() {
    return impl.getLevel().toInt() >= Level.FATAL_INT;
  }

  public static boolean isInfoEnabled() {
    return impl.getLevel().toInt() >= Level.INFO_INT;
  }

  public static boolean isLoggingEnabled() {
    return impl.getLevel().toInt() >= Level.OFF_INT;
  }

  public static boolean isWarnEnabled() {
    return impl.getLevel().toInt() >= Level.WARN_INT;
  }

  public static boolean removeLogger(Logger logger) {
    throw new UnsupportedOperationException();
  }

  public static final void setCurrentLogLevel(int level) {
    impl.setLevel(Level.toLevel(level));
  }

  public static void setUncaughtExceptionHandler() {
    throw new UnsupportedOperationException();
  }

  public static void warn(String message) {
    warn(message, (Throwable) null);
  }

  public static void warn(String message, JavaScriptObject e) {
    throw new UnsupportedOperationException();
  }

  public static void warn(String message, Throwable e) {
    impl.warn(message, e);
  }
}
