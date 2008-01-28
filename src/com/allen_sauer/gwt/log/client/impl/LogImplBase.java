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
package com.allen_sauer.gwt.log.client.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;

import com.allen_sauer.gwt.log.client.ConsoleLogger;
import com.allen_sauer.gwt.log.client.DivLogger;
import com.allen_sauer.gwt.log.client.FirebugLogger;
import com.allen_sauer.gwt.log.client.GWTLogger;
import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.Logger;
import com.allen_sauer.gwt.log.client.SystemLogger;
import com.allen_sauer.gwt.log.client.util.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Common implementation for all classes that are not expected to be compiled out,
 * i.e. all {@link LogImpl} subclasses except for {@link LogImplOff}).
 */
public abstract class LogImplBase extends LogImpl {
  static final String LOG_LEVEL_TEXT_DEBUG = LogUtil.levelToString(Log.LOG_LEVEL_DEBUG);
  static final String LOG_LEVEL_TEXT_ERROR = LogUtil.levelToString(Log.LOG_LEVEL_ERROR);
  static final String LOG_LEVEL_TEXT_FATAL = LogUtil.levelToString(Log.LOG_LEVEL_FATAL);
  static final String LOG_LEVEL_TEXT_INFO = LogUtil.levelToString(Log.LOG_LEVEL_INFO);
  static final String LOG_LEVEL_TEXT_OFF = LogUtil.levelToString(Log.LOG_LEVEL_OFF);
  static final String LOG_LEVEL_TEXT_WARN = LogUtil.levelToString(Log.LOG_LEVEL_WARN);

  static {
    setVersion();
  }

  static JavaScriptException convertJavaScriptObjectToException(JavaScriptObject e) {
    return new JavaScriptException(javaScriptExceptionName(e), javaScriptExceptionDescription(e));
  }

  private static String format(String prefix, String message) {
    return prefix + " " + message.replaceAll("\n", "\n" + prefix);
  }

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

  private static native void setVersion()
  /*-{
    $GWT_LOG_VERSION = "@GWT_LOG_VERSION@";
  }-*/;

  private static String toPrefix(String logLevelText) {
    return "[" + logLevelText + "]";
  }

  private int currentLogLevel = getLowestLogLevel();

  private ArrayList loggers = new ArrayList();

  public LogImplBase() {
    addLogger(new GWTLogger());
    addLogger(new SystemLogger());
    addLogger(new FirebugLogger());
    addLogger(new ConsoleLogger());

    // GWT hacking may prevent the DOM/UI from working properly
    try {
      addLogger(new DivLogger());
    } catch (Throwable ex) {
      Window.alert("WARNING: Unable to instantiate '" + DivLogger.class + "' due to "
          + ex.toString());
    }

    clear();
  }

  public void addLogger(Logger logger) {
    if (logger.isSupported()) {
      loggers.add(logger);
    }
  }

  public void clear() {
    for (Iterator iterator = loggers.iterator(); iterator.hasNext();) {
      Logger logger = (Logger) iterator.next();
      logger.clear();
    }
  }

  public void debug(String message, JavaScriptObject e) {
    if (isDebugEnabled()) {
      debug(message, convertJavaScriptObjectToException(e));
    }
  }

  public void debug(String message, Throwable e) {
    if (isDebugEnabled()) {
      message = format(toPrefix(LOG_LEVEL_TEXT_DEBUG), message);
      for (Iterator iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = (Logger) iterator.next();
        logger.debug(message, e);
      }
    }
  }

  public void error(String message, JavaScriptObject e) {
    if (isErrorEnabled()) {
      error(message, convertJavaScriptObjectToException(e));
    }
  }

  public void error(String message, Throwable e) {
    if (isErrorEnabled()) {
      message = format(toPrefix(LOG_LEVEL_TEXT_ERROR), message);
      for (Iterator iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = (Logger) iterator.next();
        logger.error(message, e);
      }
    }
  }

  public void fatal(String message, JavaScriptObject e) {
    if (isFatalEnabled()) {
      fatal(message, convertJavaScriptObjectToException(e));
    }
  }

  public void fatal(String message, Throwable e) {
    if (isFatalEnabled()) {
      message = format(toPrefix(LOG_LEVEL_TEXT_FATAL), message);
      for (Iterator iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = (Logger) iterator.next();
        logger.fatal(message, e);
      }
    }
  }

  public int getCurrentLogLevel() {
    return currentLogLevel;
  }

  public Logger getLogger(Class clazz) {
    // TODO Replace string comparisons with Class expressions in GWT 1.5
    String className = clazz.toString().replaceAll(".* ", "");
    for (Iterator iterator = loggers.iterator(); iterator.hasNext();) {
      Logger logger = (Logger) iterator.next();
      if (GWT.getTypeName(logger).equals(className)) {
        return logger;
      }
    }
    return null;
  }

  public ConsoleLogger getLoggerConsole() {
    return (ConsoleLogger) getLogger(ConsoleLogger.class);
  }

  public DivLogger getLoggerDiv() {
    return (DivLogger) getLogger(DivLogger.class);
  }

  public FirebugLogger getLoggerFirebug() {
    return (FirebugLogger) getLogger(FirebugLogger.class);
  }

  public GWTLogger getLoggerGWT() {
    return (GWTLogger) getLogger(GWTLogger.class);
  }

  public SystemLogger getLoggerSystem() {
    return (SystemLogger) getLogger(SystemLogger.class);
  }

  public void info(String message, JavaScriptObject e) {
    if (isInfoEnabled()) {
      info(message, convertJavaScriptObjectToException(e));
    }
  }

  public void info(String message, Throwable e) {
    if (isInfoEnabled()) {
      message = format(toPrefix(LOG_LEVEL_TEXT_INFO), message);
      for (Iterator iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = (Logger) iterator.next();
        logger.info(message, e);
      }
    }
  }

  public boolean isDebugEnabled() {
    return getLowestLogLevel() <= Log.LOG_LEVEL_DEBUG
        && getCurrentLogLevel() <= Log.LOG_LEVEL_DEBUG;
  }

  public boolean isErrorEnabled() {
    return getLowestLogLevel() <= Log.LOG_LEVEL_ERROR
        && getCurrentLogLevel() <= Log.LOG_LEVEL_ERROR;
  }

  public boolean isFatalEnabled() {
    return getLowestLogLevel() <= Log.LOG_LEVEL_FATAL
        && getCurrentLogLevel() <= Log.LOG_LEVEL_FATAL;
  }

  public boolean isInfoEnabled() {
    return getLowestLogLevel() <= Log.LOG_LEVEL_INFO && getCurrentLogLevel() <= Log.LOG_LEVEL_INFO;
  }

  public boolean isLoggingEnabled() {
    return getLowestLogLevel() != Log.LOG_LEVEL_OFF && getCurrentLogLevel() != Log.LOG_LEVEL_OFF;
  }

  public boolean isWarnEnabled() {
    return getLowestLogLevel() <= Log.LOG_LEVEL_WARN && getCurrentLogLevel() <= Log.LOG_LEVEL_WARN;
  }

  public boolean removeLogger(Logger logger) {
    return loggers.remove(logger);
  }

  public int setCurrentLogLevel(int level) {
    if (level < getLowestLogLevel()) {
      Window.alert("Unable to lower runtime log level to " + level
          + " due to compile time minimum of " + getLowestLogLevel());
      level = getLowestLogLevel();
    }
    currentLogLevel = level;
    return currentLogLevel;
  }

  public void setUncaughtExceptionHandler() {
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
      public void onUncaughtException(Throwable e) {
        Log.fatal("Uncaught Exception:", e);
      }
    });
  }

  public void warn(String message, JavaScriptObject e) {
    if (isWarnEnabled()) {
      warn(message, convertJavaScriptObjectToException(e));
    }
  }

  public void warn(String message, Throwable e) {
    if (isWarnEnabled()) {
      message = format(toPrefix(LOG_LEVEL_TEXT_WARN), message);
      for (Iterator iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = (Logger) iterator.next();
        logger.warn(message, e);
      }
    }
  }
}
