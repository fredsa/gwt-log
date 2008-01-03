/*
 * Copyright 2007 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.allen_sauer.gwt.log.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

import com.allen_sauer.gwt.log.client.impl.LogImpl;

public final class Log {
  public static final int LOG_LEVEL_DEBUG = 10000;
  public static final int LOG_LEVEL_ERROR = 40000;
  public static final int LOG_LEVEL_FATAL = 50000;
  public static final int LOG_LEVEL_INFO = 20000;
  public static final int LOG_LEVEL_OFF = Integer.MAX_VALUE;
  public static final int LOG_LEVEL_WARN = 30000;

  private static final LogImpl impl = (LogImpl) GWT.create(LogImpl.class);

  public static void addLogger(Logger logger) {
    impl.addLogger(logger);
  }

  public static void clear() {
    impl.clear();
  }

  public static void debug(String message) {
    debug(message, (Throwable) null);
  }

  public static void debug(String message, JavaScriptObject e) {
    impl.debug(message, e);
  }

  public static void debug(String message, Throwable e) {
    impl.debug(message, e);
  }

  public static void error(String message) {
    error(message, (Throwable) null);
  }

  public static void error(String message, JavaScriptObject e) {
    impl.error(message, e);
  }

  public static void error(String message, Throwable e) {
    impl.error(message, e);
  }

  public static void fatal(String message) {
    fatal(message, (Throwable) null);
  }

  public static void fatal(String message, JavaScriptObject e) {
    impl.fatal(message, e);
  }

  public static void fatal(String message, Throwable e) {
    impl.fatal(message, e);
  }

  public static ConsoleLogger getConsoleLogger() {
    return impl.getLoggerConsole();
  }

  public static int getCurrentLogLevel() {
    return impl.getCurrentLogLevel();
  }

  public static String getCurrentLogLevelString() {
    return impl.getCurrentLogLevelString();
  }

  public static DivLogger getDivLogger() {
    return impl.getLoggerDiv();
  }

  public static FirebugLogger getFirebugLogger() {
    return impl.getLoggerFirebug();
  }

  public static GWTLogger getGwtLogger() {
    return impl.getLoggerGWT();
  }

  public static int getLowestLogLevel() {
    return impl.getLowestLogLevel();
  }

  public static String getLowestLogLevelString() {
    return impl.getLowestLogLevelString();
  }

  public static LoggerSystem getSystemLogger() {
    return impl.getLoggerSystem();
  }

  public static void info(String message) {
    info(message, (Throwable) null);
  }

  public static void info(String message, JavaScriptObject e) {
    impl.info(message, e);
  }

  public static void info(String message, Throwable e) {
    impl.info(message, e);
  }

  public static final boolean isDebugEnabled() {
    return impl.isDebugEnabled();
  }

  public static boolean isErrorEnabled() {
    return impl.isErrorEnabled();
  }

  public static boolean isFatalEnabled() {
    return impl.isFatalEnabled();
  }

  public static boolean isInfoEnabled() {
    return impl.isInfoEnabled();
  }

  public static boolean isLoggingEnabled() {
    return impl.isLoggingEnabled();
  }

  public static boolean isWarnEnabled() {
    return impl.isWarnEnabled();
  }

  public static final void setCurrentLogLevel(int level) {
    impl.setCurrentLogLevel(level);
  }

  public static void setUncaughtExceptionHandler() {
    impl.setUncaughtExceptionHandler();
  }

  public static void warn(String message) {
    warn(message, (Throwable) null);
  }

  public static void warn(String message, JavaScriptObject e) {
    impl.warn(message, e);
  }

  public static void warn(String message, Throwable e) {
    impl.warn(message, e);
  }

  public boolean removeLogger(Logger logger) {
    return impl.removeLogger(logger);
  }
}