/*
 * Copyright 2008 Fred Sauer
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

import com.google.gwt.core.client.JavaScriptObject;

import com.allen_sauer.gwt.log.client.util.ServerSideLogUtil;
import com.allen_sauer.gwt.log.server.ServerLogImpl;
import com.allen_sauer.gwt.log.server.ServerLogImplJDK14;
import com.allen_sauer.gwt.log.server.ServerLogImplLog4J;

//CHECKSTYLE_JAVADOC_OFF
public final class ServerSideLog {
  public static final int LOG_LEVEL_DEBUG = 10000;
  public static final int LOG_LEVEL_ERROR = 40000;
  public static final int LOG_LEVEL_FATAL = 50000;
  public static final int LOG_LEVEL_INFO = 20000;
  public static final int LOG_LEVEL_OFF = Integer.MAX_VALUE;
  public static final int LOG_LEVEL_WARN = 30000;

  private static ServerLogImpl impl;

  private static final String UNSUPPORTED_METHOD_TEXT = "This method available only when running on the client";

  static {
    if (impl == null) {
      try {
        impl = new ServerLogImplLog4J();
      } catch (Throwable e) {
      }
    }

    if (impl == null) {
      try {
        impl = new ServerLogImplJDK14();
      } catch (Throwable e) {
      }
    }
    if (impl == null) {
      System.err.println("ERROR: gwt-log failed to initialize valid server-side logging implementation");
    }

    setCurrentLogLevel(LOG_LEVEL_DEBUG);
  }

  public static void addLogger(ServerSideLogger logger) {
    throw new UnsupportedOperationException(UNSUPPORTED_METHOD_TEXT);
  }

  public static void clear() {
    impl.clear();
  }

  public static void debug(String message) {
    debug(message, (Throwable) null);
  }

  public static void debug(String message, JavaScriptObject e) {
    throw new UnsupportedOperationException(UNSUPPORTED_METHOD_TEXT);
  }

  public static void debug(String message, Throwable e) {
    impl.debug(message, e);
  }

  /**
   * @deprecated For internal gwt-log use only.
   */
  @Deprecated
  public static void diagnostic(String message, Throwable e) {
    impl.diagnostic(message, e);
  }

  public static void error(String message) {
    error(message, (Throwable) null);
  }

  public static void error(String message, JavaScriptObject e) {
    throw new UnsupportedOperationException(UNSUPPORTED_METHOD_TEXT);
  }

  public static void error(String message, Throwable e) {
    impl.error(message, e);
  }

  public static void fatal(String message) {
    fatal(message, (Throwable) null);
  }

  public static void fatal(String message, JavaScriptObject e) {
    throw new UnsupportedOperationException(UNSUPPORTED_METHOD_TEXT);
  }

  public static void fatal(String message, Throwable e) {
    impl.fatal(message, e);
  }

  public static ServerSideConsoleLogger getConsoleLogger() {
    throw new UnsupportedOperationException(UNSUPPORTED_METHOD_TEXT);
  }

  public static int getCurrentLogLevel() {
    return impl.getCurrentLogLevel();
  }

  public static String getCurrentLogLevelString() {
    return ServerSideLogUtil.levelToString(getCurrentLogLevel());
  }

  public static ServerSideDivLogger getDivLogger() {
    throw new UnsupportedOperationException(UNSUPPORTED_METHOD_TEXT);
  }

  public static ServerSideFirebugLogger getFirebugLogger() {
    throw new UnsupportedOperationException(UNSUPPORTED_METHOD_TEXT);
  }

  public static ServerSideGWTLogger getGwtLogger() {
    throw new UnsupportedOperationException(UNSUPPORTED_METHOD_TEXT);
  }

  public static <T extends ServerSideLogger> ServerSideLogger getLogger(Class<T> clazz) {
    throw new UnsupportedOperationException(UNSUPPORTED_METHOD_TEXT);
  }

  public static int getLowestLogLevel() {
    return LOG_LEVEL_DEBUG;
  }

  public static String getLowestLogLevelString() {
    return ServerSideLogUtil.levelToString(getLowestLogLevel());
  }

  public static ServerSideSystemLogger getSystemLogger() {
    throw new UnsupportedOperationException(UNSUPPORTED_METHOD_TEXT);
  }

  public static String getVersion() {
    String version = "@GWT_LOG_VERSION@";
    return version.matches("^@.+@$") ? "0.0.0" : version;
  }

  public static void info(String message) {
    info(message, (Throwable) null);
  }

  public static void info(String message, JavaScriptObject e) {
    throw new UnsupportedOperationException(UNSUPPORTED_METHOD_TEXT);
  }

  public static void info(String message, Throwable e) {
    impl.info(message, e);
  }

  public static boolean isDebugEnabled() {
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

  public static void setCurrentLogLevel(int level) {
    impl.setCurrentImplLogLevel(impl.mapGWTLogLevelToImplLevel(level));
  }

  public static void setUncaughtExceptionHandler() {
    throw new UnsupportedOperationException(UNSUPPORTED_METHOD_TEXT);
  }

  public static void warn(String message) {
    warn(message, (Throwable) null);
  }

  public static void warn(String message, JavaScriptObject e) {
    throw new UnsupportedOperationException(UNSUPPORTED_METHOD_TEXT);
  }

  public static void warn(String message, Throwable e) {
    impl.warn(message, e);
  }
}
