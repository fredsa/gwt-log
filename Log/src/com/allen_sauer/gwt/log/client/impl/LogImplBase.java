/*
 * Copyright 2009 Fred Sauer
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
package com.allen_sauer.gwt.log.client.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;

import com.allen_sauer.gwt.log.client.ConsoleLogger;
import com.allen_sauer.gwt.log.client.DivLogger;
import com.allen_sauer.gwt.log.client.FirebugLogger;
import com.allen_sauer.gwt.log.client.GWTLogger;
import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.LogMessageFormatter;
import com.allen_sauer.gwt.log.client.Logger;
import com.allen_sauer.gwt.log.client.RemoteLogger;
import com.allen_sauer.gwt.log.client.SystemLogger;
import com.allen_sauer.gwt.log.client.WindowLogger;
import com.allen_sauer.gwt.log.client.util.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Common implementation for all classes that are not expected to be compiled
 * out, i.e. all {@link LogImpl} subclasses except for {@link LogImplOff}).
 */
public abstract class LogImplBase extends LogImpl {
  // CHECKSTYLE_JAVADOC_OFF

  static final LogMessageFormatter FORMATTER = GWT.create(LogMessageFormatter.class);

  static final String LOG_LEVEL_TEXT_DEBUG = LogUtil.levelToString(Log.LOG_LEVEL_DEBUG);

  static final String LOG_LEVEL_TEXT_ERROR = LogUtil.levelToString(Log.LOG_LEVEL_ERROR);

  static final String LOG_LEVEL_TEXT_FATAL = LogUtil.levelToString(Log.LOG_LEVEL_FATAL);

  static final String LOG_LEVEL_TEXT_INFO = LogUtil.levelToString(Log.LOG_LEVEL_INFO);

  static final String LOG_LEVEL_TEXT_OFF = LogUtil.levelToString(Log.LOG_LEVEL_OFF);

  static final String LOG_LEVEL_TEXT_TRACE = LogUtil.levelToString(Log.LOG_LEVEL_TRACE);

  static final String LOG_LEVEL_TEXT_WARN = LogUtil.levelToString(Log.LOG_LEVEL_WARN);

  static {
    setVersion();
    StyleInjector.injectStylesheetAtStart(LogClientBundle.INSTANCE.css().getText());
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

  @SuppressWarnings("unused")
  private static native boolean handleOnError(String msg, String url, int line)
  /*-{
    @com.allen_sauer.gwt.log.client.Log::fatal(Ljava/lang/String;)("Uncaught JavaScript exception [" + msg + "] in " + url + ", line " + line);
    return true;
  }-*/;

  private static native String javaScriptExceptionDescription(JavaScriptObject e)
  /*-{
    try {
     return e.message;
    } catch(ex) {
     return "[e has no message]";
    }
  }-*/;

  private static native String javaScriptExceptionName(JavaScriptObject e)
  /*-{
    try {
     return e.name;
    } catch(ex) {
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

  private final ArrayList<Logger> loggers = new ArrayList<Logger>();

  public LogImplBase() {
  }

  @Override
  public final void addLogger(Logger logger) {
    if (logger.isSupported()) {
      loggers.add(logger);
    }
  }

  @Override
  public final void clear() {
    for (Iterator<Logger> iterator = loggers.iterator(); iterator.hasNext();) {
      Logger logger = iterator.next();
      try {
        logger.clear();
      } catch (RuntimeException e1) {
        iterator.remove();
        diagnostic("Removing '" + logger.getClass().getName() + "' due to unexecpted exception", e1);
      }
    }
  }

  @Override
  public final void debug(String message, JavaScriptObject e) {
    if (isDebugEnabled()) {
      debug(message, convertJavaScriptObjectToException(e));
    }
  }

  @Override
  public final void debug(String message, Throwable e) {
    if (isDebugEnabled()) {
      message = FORMATTER.format(LOG_LEVEL_TEXT_DEBUG, message);
      for (Iterator<Logger> iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = iterator.next();
        try {
          logger.debug(message, e);
        } catch (RuntimeException e1) {
          iterator.remove();
          diagnostic("Removing '" + logger.getClass().getName() + "' due to unexecpted exception",
              e1);
        }
      }
    }
  }

  @Override
  public void diagnostic(String message, final Throwable e) {
    final String msg = FORMATTER.format("gwt-log", message);
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        for (Iterator<Logger> iterator = loggers.iterator(); iterator.hasNext();) {
          Logger logger = iterator.next();
          try {
            logger.diagnostic(msg, e);
          } catch (RuntimeException e1) {
            iterator.remove();
            diagnostic(
                "Removing '" + logger.getClass().getName() + "' due to unexecpted exception", e1);
          }
        }
      }
    });
  }

  @Override
  public final void error(String message, JavaScriptObject e) {
    if (isErrorEnabled()) {
      error(message, convertJavaScriptObjectToException(e));
    }
  }

  @Override
  public final void error(String message, Throwable e) {
    if (isErrorEnabled()) {
      message = FORMATTER.format(LOG_LEVEL_TEXT_ERROR, message);
      for (Iterator<Logger> iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = iterator.next();
        try {
          logger.error(message, e);
        } catch (RuntimeException e1) {
          iterator.remove();
          diagnostic("Removing '" + logger.getClass().getName() + "' due to unexecpted exception",
              e1);
        }
      }
    }
  }

  @Override
  public final void fatal(String message, JavaScriptObject e) {
    if (isFatalEnabled()) {
      fatal(message, convertJavaScriptObjectToException(e));
    }
  }

  @Override
  public final void fatal(String message, Throwable e) {
    if (isFatalEnabled()) {
      message = FORMATTER.format(LOG_LEVEL_TEXT_FATAL, message);
      for (Iterator<Logger> iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = iterator.next();
        try {
          logger.fatal(message, e);
        } catch (RuntimeException e1) {
          iterator.remove();
          diagnostic("Removing '" + logger.getClass().getName() + "' due to unexecpted exception",
              e1);
        }
      }
    }
  }

  @Override
  public final int getCurrentLogLevel() {
    return currentLogLevel;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T extends Logger> T getLogger(Class<T> clazz) {
    for (Logger logger : loggers) {
      if (logger.getClass() == clazz) {
        return (T) logger;
      }
    }
    return null;
  }

  @Override
  public final ConsoleLogger getLoggerConsole() {
    return getLogger(ConsoleLogger.class);
  }

  @Override
  public final DivLogger getLoggerDiv() {
    return getLogger(DivLogger.class);
  }

  @Override
  public final FirebugLogger getLoggerFirebug() {
    return getLogger(FirebugLogger.class);
  }

  @Override
  public final GWTLogger getLoggerGWT() {
    return getLogger(GWTLogger.class);
  }

  @Override
  public final SystemLogger getLoggerSystem() {
    return getLogger(SystemLogger.class);
  }

  @Override
  public final void info(String message, JavaScriptObject e) {
    if (isInfoEnabled()) {
      info(message, convertJavaScriptObjectToException(e));
    }
  }

  @Override
  public final void info(String message, Throwable e) {
    if (isInfoEnabled()) {
      message = FORMATTER.format(LOG_LEVEL_TEXT_INFO, message);
      for (Iterator<Logger> iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = iterator.next();
        try {
          logger.info(message, e);
        } catch (RuntimeException e1) {
          iterator.remove();
          diagnostic("Removing '" + logger.getClass().getName() + "' due to unexecpted exception",
              e1);
        }
      }
    }
  }

  @Override
  public void init() {
    addLogger((Logger) GWT.create(GWTLogger.class));
    addLogger((Logger) GWT.create(SystemLogger.class));
    addLogger((Logger) GWT.create(FirebugLogger.class));
    addLogger((Logger) GWT.create(ConsoleLogger.class));
    addLogger((Logger) GWT.create(RemoteLogger.class));

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
  public final int setCurrentLogLevel(int level) {
    level = setCurrentLogLevelLoggers(level);

    if (level != currentLogLevel) {
      diagnostic("Temporarily setting the current (runtime) log level filter to '"
          + LogUtil.levelToString(level) + "'", null);
      currentLogLevel = level;
    }

    return currentLogLevel;
  }

  private int setCurrentLogLevelLoggers(int level) {
    if (level < getLowestLogLevel()) {
      Window.alert("Unable to lower runtime log level to " + level
          + " due to compile time minimum of " + getLowestLogLevel());
      level = getLowestLogLevel();
    }

    for (Iterator<Logger> iterator = loggers.iterator(); iterator.hasNext();) {
      Logger logger = iterator.next();
      try {
        logger.setCurrentLogLevel(level);
      } catch (RuntimeException e1) {
        iterator.remove();
        diagnostic("Removing '" + logger.getClass().getName() + "' due to unexecpted exception", e1);
      }
    }
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

  @Override
  public final void setUncaughtExceptionHandler() {
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
      public void onUncaughtException(Throwable e) {
        Log.fatal("Uncaught Exception:", e);
      }
    });
    setErrorHandler();
  }

  @Override
  public final void trace(String message, JavaScriptObject e) {
    if (isTraceEnabled()) {
      trace(message, convertJavaScriptObjectToException(e));
    }
  }

  @Override
  public final void trace(String message, Throwable e) {
    if (isTraceEnabled()) {
      message = FORMATTER.format(LOG_LEVEL_TEXT_TRACE, message);
      for (Iterator<Logger> iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = iterator.next();
        try {
          logger.trace(message, e);
        } catch (RuntimeException e1) {
          iterator.remove();
          diagnostic("Removing '" + logger.getClass().getName() + "' due to unexecpted exception",
              e1);
        }
      }
    }
  }

  @Override
  public final void warn(String message, JavaScriptObject e) {
    if (isWarnEnabled()) {
      warn(message, convertJavaScriptObjectToException(e));
    }
  }

  @Override
  public final void warn(String message, Throwable e) {
    if (isWarnEnabled()) {
      message = FORMATTER.format(LOG_LEVEL_TEXT_WARN, message);
      for (Iterator<Logger> iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = iterator.next();
        try {
          logger.warn(message, e);
        } catch (RuntimeException e1) {
          iterator.remove();
          diagnostic("Removing '" + logger.getClass().getName() + "' due to unexecpted exception",
              e1);
        }
      }
    }
  }
}