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
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;

import com.allen_sauer.gwt.log.client.ConsoleLogger;
import com.allen_sauer.gwt.log.client.DivLogger;
import com.allen_sauer.gwt.log.client.FirebugLogger;
import com.allen_sauer.gwt.log.client.GWTLogger;
import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.Logger;
import com.allen_sauer.gwt.log.client.RemoteLogger;
import com.allen_sauer.gwt.log.client.SystemLogger;
import com.allen_sauer.gwt.log.client.WindowLogger;
import com.allen_sauer.gwt.log.client.util.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Common implementation for all classes that are not expected to be compiled out,
 * i.e. all {@link LogImpl} subclasses except for {@link LogImplOff}).
 */
public abstract class LogImplBase extends LogImpl {
  // CHECKSTYLE_JAVADOC_OFF

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

  /**
   * TODO move the message formatting and addition of log level prefix(es) to the Loggers as it really doesn't belong here
   */
  private static String format(String prefix, String message) {
    return prefix + " " + message.replaceAll("\n", "\n" + prefix + " ");
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

  /**
   * TODO Update when GWT provides generic library version tracking ability
   */
  private static native void setVersion()
  /*-{
    $wnd.$GWT_LOG_VERSION = "@GWT_LOG_VERSION@";
  }-*/;

  private static String toPrefix(String logLevelText) {
    return "[" + logLevelText + "]";
  }

  private int currentLogLevel = getLowestLogLevel();

  private ArrayList<Logger> loggers = new ArrayList<Logger>();

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
      message = format(toPrefix(LOG_LEVEL_TEXT_DEBUG), message);
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
    final String msg = format(toPrefix("gwt-log"), message);
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
      message = format(toPrefix(LOG_LEVEL_TEXT_ERROR), message);
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
      message = format(toPrefix(LOG_LEVEL_TEXT_FATAL), message);
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

  @Override
  public final <T extends Logger> T getLogger(Class<T> clazz) {
    // TODO Replace string comparisons with Class expressions in GWT 1.5
    String className = clazz.toString().replaceAll(".* ", "");
    for (Logger logger : loggers) {
      if (logger.getClass().getName().equals(className)) {
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
      message = format(toPrefix(LOG_LEVEL_TEXT_INFO), message);
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
    setCurrentLogLevel(getLowestLogLevel());

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
  public boolean isWarnEnabled() {
    return getLowestLogLevel() <= Log.LOG_LEVEL_WARN && getCurrentLogLevel() <= Log.LOG_LEVEL_WARN;
  }

  @Override
  public final int setCurrentLogLevel(int level) {
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

    if (level != currentLogLevel) {
      diagnostic("Temporarily setting the current (runtime) log level filter to '"
          + LogUtil.levelToString(level) + "'", null);
      currentLogLevel = level;
    }

    return currentLogLevel;
  }

  @Override
  public final void setUncaughtExceptionHandler() {
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
      public void onUncaughtException(Throwable e) {
        Log.fatal("Uncaught Exception:", e);
      }
    });
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
      message = format(toPrefix(LOG_LEVEL_TEXT_WARN), message);
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