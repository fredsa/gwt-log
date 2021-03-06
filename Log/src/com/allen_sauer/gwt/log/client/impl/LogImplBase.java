/*
 * Copyright 2009 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.allen_sauer.gwt.log.client.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;

import com.allen_sauer.gwt.log.client.ConsoleLogger;
import com.allen_sauer.gwt.log.client.DivLogger;
import com.allen_sauer.gwt.log.client.GWTLogger;
import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.LogUtil;
import com.allen_sauer.gwt.log.client.Logger;
import com.allen_sauer.gwt.log.client.RemoteLogger;
import com.allen_sauer.gwt.log.client.SystemLogger;
import com.allen_sauer.gwt.log.client.WindowLogger;
import com.allen_sauer.gwt.log.shared.LogRecord;

/**
 * Common implementation for all classes that are not expected to be compiled out, i.e. all
 * {@link LogImplInterface} subclasses except for {@link LogImplOff}).
 */
public abstract class LogImplBase extends LogImpl {
  // CHECKSTYLE_JAVADOC_OFF

  static final String LOG_LEVEL_TEXT_DEBUG = LogUtil.levelToString(Log.LOG_LEVEL_DEBUG);

  static final String LOG_LEVEL_TEXT_ERROR = LogUtil.levelToString(Log.LOG_LEVEL_ERROR);

  static final String LOG_LEVEL_TEXT_FATAL = LogUtil.levelToString(Log.LOG_LEVEL_FATAL);

  static final String LOG_LEVEL_TEXT_INFO = LogUtil.levelToString(Log.LOG_LEVEL_INFO);

  static final String LOG_LEVEL_TEXT_OFF = LogUtil.levelToString(Log.LOG_LEVEL_OFF);

  static final String LOG_LEVEL_TEXT_TRACE = LogUtil.levelToString(Log.LOG_LEVEL_TRACE);

  static final String LOG_LEVEL_TEXT_WARN = LogUtil.levelToString(Log.LOG_LEVEL_WARN);

  static {
    setVersion();
    StyleInjector.injectAtStart(LogClientBundle.INSTANCE.css().getText());
  }

  static JavaScriptException convertJavaScriptObjectToException(JavaScriptObject e) {
    return new JavaScriptException(javaScriptExceptionName(e), javaScriptExceptionDescription(e));
  }

  private static int getRequestedRuntimeLogLevel() {
    String logLevelString = Location.getParameter("log_level");
    int lowestLogLevel = Log.getLowestLogLevel();
    return logLevelString == null ? lowestLogLevel : Math.max(lowestLogLevel,
        LogUtil.stringToLevel(logLevelString));
  }

  private static native boolean handleOnError(String msg, String url, int line)
  /*-{
		@com.allen_sauer.gwt.log.client.Log::fatal(Ljava/lang/String;)("Uncaught JavaScript exception [" + msg + "] in " + url + ", line " + line);
		return true;
  }-*/;

  private static native String javaScriptExceptionDescription(JavaScriptObject e)
  /*-{
		try {
			return e.message;
		} catch (ex) {
			return "[e has no message]";
		}
  }-*/;

  private static native String javaScriptExceptionName(JavaScriptObject e)
  /*-{
		try {
			return e.name;
		} catch (ex) {
			return "[e has no name]";
		}
  }-*/;

  /**
   * TODO Update when GWT provides generic library version tracking ability
   */
  private static native void setVersion()
  /*-{
		$wnd.$GWT_LOG_VERSION = "@GWT_LOG_VERSION@";
  }-*/;

  private int currentLogLevel = getLowestLogLevel();

  /**
   * Either a real remote logger, or a proxy remote logger.
   */
  private final RemoteLogger remoteLogger = GWT.create(RemoteLogger.class);

  public LogImplBase() {
  }

  @Override
  public final void addLogger(Logger logger) {
    remoteLogger.loggersAdd(logger);
  }

  @Override
  public final void clear() {
    remoteLogger.loggersClear();
  }

  @Override
  public final void debug(String category, String message, JavaScriptObject e) {
    if (isDebugEnabled()) {
      log(Log.LOG_LEVEL_DEBUG, category, message, convertJavaScriptObjectToException(e));
    }
  }

  @Override
  public final void debug(String category, String message, Throwable e) {
    if (isDebugEnabled()) {
      log(Log.LOG_LEVEL_DEBUG, category, message, e);
    }
  }

  @Override
  public void diagnostic(String message, final Throwable e) {
    log(Log.LOG_LEVEL_OFF, "gwt-log", message, e);
  }

  @Override
  public final void error(String category, String message, JavaScriptObject e) {
    if (isErrorEnabled()) {
      log(Log.LOG_LEVEL_ERROR, category, message, convertJavaScriptObjectToException(e));
    }
  }

  @Override
  public final void error(String category, String message, Throwable e) {
    if (isErrorEnabled()) {
      log(Log.LOG_LEVEL_ERROR, category, message, e);
    }
  }

  @Override
  public final void fatal(String category, String message, JavaScriptObject e) {
    if (isFatalEnabled()) {
      log(Log.LOG_LEVEL_FATAL, category, message, convertJavaScriptObjectToException(e));
    }
  }

  @Override
  public final void fatal(String category, String message, Throwable e) {
    if (isFatalEnabled()) {
      log(Log.LOG_LEVEL_FATAL, category, message, e);
    }
  }

  @Override
  public final int getCurrentLogLevel() {
    return currentLogLevel;
  }

  @Override
  public final <T extends Logger> T getLogger(Class<T> clazz) {
    return remoteLogger.getLogger(clazz);
  }

  @Override
  public final void info(String category, String message, JavaScriptObject e) {
    if (isInfoEnabled()) {
      log(Log.LOG_LEVEL_INFO, category, message, convertJavaScriptObjectToException(e));
    }
  }

  @Override
  public final void info(String category, String message, Throwable e) {
    if (isInfoEnabled()) {
      log(Log.LOG_LEVEL_INFO, category, message, e);
    }
  }

  @Override
  public void init() {
    addLogger((Logger) GWT.create(GWTLogger.class));
    addLogger((Logger) GWT.create(SystemLogger.class));
    addLogger((Logger) GWT.create(ConsoleLogger.class));

    // GWT hacking may prevent the DOM/UI from working properly
    try {
      addLogger((Logger) GWT.create(DivLogger.class));
    } catch (Throwable ex) {
      Window.alert("WARNING: Unable to instantiate '" + DivLogger.class + "' due to "
          + ex.toString());
    }

    // GWT hacking may prevent the DOM/UI from working properly
    try {
      addLogger((Logger) GWT.create(WindowLogger.class));
    } catch (Throwable ex) {
      Window.alert("WARNING: Unable to instantiate '" + WindowLogger.class + "' due to "
          + ex.toString());
    }

    // notify loggers
    setCurrentLogLevelLoggers(getRequestedRuntimeLogLevel());

    clear();
  }

  @Override
  public boolean isDebugEnabled() {
    return getLowestLogLevel() <= Log.LOG_LEVEL_DEBUG
        && getCurrentLogLevel() <= Log.LOG_LEVEL_DEBUG;
  }

  @Override
  public boolean isErrorEnabled() {
    return getLowestLogLevel() <= Log.LOG_LEVEL_ERROR
        && getCurrentLogLevel() <= Log.LOG_LEVEL_ERROR;
  }

  @Override
  public final boolean isFatalEnabled() {
    return getLowestLogLevel() <= Log.LOG_LEVEL_FATAL
        && getCurrentLogLevel() <= Log.LOG_LEVEL_FATAL;
  }

  @Override
  public boolean isInfoEnabled() {
    return getLowestLogLevel() <= Log.LOG_LEVEL_INFO && getCurrentLogLevel() <= Log.LOG_LEVEL_INFO;
  }

  @Override
  public final boolean isLoggingEnabled() {
    return getLowestLogLevel() != Log.LOG_LEVEL_OFF && getCurrentLogLevel() != Log.LOG_LEVEL_OFF;
  }

  @Override
  public boolean isTraceEnabled() {
    return getLowestLogLevel() <= Log.LOG_LEVEL_TRACE
        && getCurrentLogLevel() <= Log.LOG_LEVEL_TRACE;
  }

  @Override
  public boolean isWarnEnabled() {
    return getLowestLogLevel() <= Log.LOG_LEVEL_WARN && getCurrentLogLevel() <= Log.LOG_LEVEL_WARN;
  }

  @Override
  public void log(LogRecord record) {
    if (record.getLevel() >= getLowestLogLevel()) {
      sendToLoggers(record);
    }
  }

  public void sendToLoggers(LogRecord record) {
    remoteLogger.loggersLog(record);
  }

  @Override
  public final int setCurrentLogLevel(int level) {
    level = setCurrentLogLevelLoggers(level);

    if (level != currentLogLevel) {
      diagnostic(
          "Temporarily setting the current (runtime) log level filter to '"
              + LogUtil.levelToString(level) + "'", null);
      currentLogLevel = level;
    }

    return currentLogLevel;
  }

  @Override
  public final void setUncaughtExceptionHandler() {
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
      @Override
      public void onUncaughtException(Throwable e) {
        Log.fatal("Uncaught Exception:", e);
      }
    });
    setErrorHandler();
  }

  @Override
  public final void trace(String category, String message, JavaScriptObject e) {
    if (isTraceEnabled()) {
      log(Log.LOG_LEVEL_TRACE, category, message, convertJavaScriptObjectToException(e));
    }
  }

  @Override
  public final void trace(String category, String message, Throwable e) {
    if (isTraceEnabled()) {
      log(Log.LOG_LEVEL_TRACE, category, message, e);
    }
  }

  @Override
  public final void warn(String category, String message, JavaScriptObject e) {
    if (isWarnEnabled()) {
      log(Log.LOG_LEVEL_WARN, category, message, convertJavaScriptObjectToException(e));
    }
  }

  @Override
  public final void warn(String category, String message, Throwable e) {
    if (isWarnEnabled()) {
      log(Log.LOG_LEVEL_WARN, category, message, e);
    }
  }

  private void log(int level, String category, String message, Throwable e) {
    LogRecord record = new LogRecord(category, level, message, e);
    sendToLoggers(record);
  }

  private int setCurrentLogLevelLoggers(int level) {
    if (level < getLowestLogLevel()) {
      Window.alert("Unable to lower runtime log level to " + level
          + " due to compile time minimum of " + getLowestLogLevel());
      level = getLowestLogLevel();
    }

    remoteLogger.loggersSetCurrentLogLevel(level);
    return level;
  }

  private native void setErrorHandler()
  /*-{
    if ($wnd != window) {
      window.onerror = @com.allen_sauer.gwt.log.client.impl.LogImplBase::handleOnError(Ljava/lang/String;Ljava/lang/String;I);
    }

    var oldOnError = $wnd.onerror;
    $wnd.onerror = function(msg, url, line) {
      var result, oldResult;
      try {
        result = @com.allen_sauer.gwt.log.client.impl.LogImplBase::handleOnError(Ljava/lang/String;Ljava/lang/String;I)(msg, url, line);
      } finally {
        oldResult = oldOnError && oldOnError(msg, url, line);
      }
      if (result != null) return result;
      if (oldResult != null) return oldResult;
    }
  }-*/;

}