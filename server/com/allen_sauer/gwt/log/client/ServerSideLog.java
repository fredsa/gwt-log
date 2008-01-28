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
import com.allen_sauer.gwt.log.server.ServerLogImplJavaUtil;
import com.allen_sauer.gwt.log.server.ServerLogImplLog4J;

public class ServerSideLog {
  public static final int LOG_LEVEL_DEBUG = 10000;
  public static final int LOG_LEVEL_ERROR = 40000;
  public static final int LOG_LEVEL_FATAL = 50000;
  public static final int LOG_LEVEL_INFO = 20000;
  public static final int LOG_LEVEL_OFF = Integer.MAX_VALUE;
  public static final int LOG_LEVEL_WARN = 30000;

  private static ServerLogImpl impl;
  static {
    if (impl == null) {
      try {
        System.err.println("impl := log4j");
        impl = new ServerLogImplLog4J();
      } catch (Throwable e) {
      }
    }

    if (impl == null) {
      try {
        System.err.println("impl := javautil");
        impl = new ServerLogImplJavaUtil();
      } catch (Throwable e) {
      }
    }
    if (impl == null) {
      System.err.println("ERROR: gwt-log failed to initialize valid server-side logging implementation");
    }

    System.err.println("setCurrentLogLevel(DEBUG)...");
    setCurrentLogLevel(LOG_LEVEL_DEBUG);
    System.err.println("...setCurrentLogLevel(DEBUG)");
  }

  public static final void addLogger(ServerSideLogger logger) {
    throw new UnsupportedOperationException();
  }

  public static final void clear() {
    impl.clear();
  }

  public static final void debug(String message) {
    debug(message, (Throwable) null);
  }

  public static final void debug(String message, JavaScriptObject e) {
    throw new UnsupportedOperationException();
  }

  public static final void debug(String message, Throwable e) {
    impl.debug(message, e);
  }

  public static final void error(String message) {
    error(message, (Throwable) null);
  }

  public static final void error(String message, JavaScriptObject e) {
    throw new UnsupportedOperationException();
  }

  public static final void error(String message, Throwable e) {
    impl.error(message, e);
  }

  public static final void fatal(String message) {
    fatal(message, (Throwable) null);
  }

  public static final void fatal(String message, JavaScriptObject e) {
    throw new UnsupportedOperationException();
  }

  public static final void fatal(String message, Throwable e) {
    impl.fatal(message, e);
  }

  public static final ServerSideConsoleLogger getConsoleLogger() {
    throw new UnsupportedOperationException();
  }

  public static final int getCurrentLogLevel() {
    return impl.getCurrentLogLevel();
  }

  public static final String getCurrentLogLevelString() {
    return ServerSideLogUtil.levelToString(getCurrentLogLevel());
  }

  public static final ServerSideDivLogger getDivLogger() {
    throw new UnsupportedOperationException();
  }

  public static final ServerSideFirebugLogger getFirebugLogger() {
    throw new UnsupportedOperationException();
  }

  public static final ServerSideGWTLogger getGwtLogger() {
    throw new UnsupportedOperationException();
  }

  public static final int getLowestLogLevel() {
    return LOG_LEVEL_DEBUG;
  }

  public static final String getLowestLogLevelString() {
    return ServerSideLogUtil.levelToString(getLowestLogLevel());
  }

  public static final ServerSideSystemLogger getSystemLogger() {
    throw new UnsupportedOperationException();
  }

  public static final void info(String message) {
    info(message, (Throwable) null);
  }

  public static final void info(String message, JavaScriptObject e) {
    throw new UnsupportedOperationException();
  }

  public static final void info(String message, Throwable e) {
    impl.info(message, e);
  }

  public static final boolean isDebugEnabled() {
    return impl.isDebugEnabled();
  }

  public static final boolean isErrorEnabled() {
    return impl.isErrorEnabled();
  }

  public static final boolean isFatalEnabled() {
    return impl.isFatalEnabled();
  }

  public static final boolean isInfoEnabled() {
    return impl.isInfoEnabled();
  }

  public static final boolean isLoggingEnabled() {
    return impl.isLoggingEnabled();
  }

  public static final boolean isWarnEnabled() {
    return impl.isWarnEnabled();
  }

  public static final boolean removeLogger(ServerSideLogger logger) {
    throw new UnsupportedOperationException();
  }

  public static final void setCurrentLogLevel(int level) {
    impl.setCurrentLogLevel(impl.mapGWTLogLevelToImplLevel(level));
  }

  public static final void setUncaughtExceptionHandler() {
    throw new UnsupportedOperationException();
  }

  public static final void warn(String message) {
    warn(message, (Throwable) null);
  }

  public static final void warn(String message, JavaScriptObject e) {
    throw new UnsupportedOperationException();
  }

  public static final void warn(String message, Throwable e) {
    impl.warn(message, e);
  }
}
