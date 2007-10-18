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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;

import java.util.ArrayList;
import java.util.Iterator;

public class Log {

  /**
   * Current log level for this application with <code>final</code> qualifier
   * to ensure GWT compiler optimization, including dead code removal.
   */
  public static final int COMPILE_TIME_LOG_LEVEL;
  public static final int LOG_LEVEL_DEBUG = 10000;
  public static final int LOG_LEVEL_ERROR = 40000;
  public static final int LOG_LEVEL_FATAL = 50000;
  public static final int LOG_LEVEL_INFO = 20000;
  public static final int LOG_LEVEL_OFF = Integer.MAX_VALUE;
  public static final int LOG_LEVEL_WARN = 30000;

  private static final ArrayList loggers = new ArrayList();

  private static final String PREFIX_DEBUG = "[DEBUG]";
  private static final String PREFIX_ERROR = "[ERROR]";
  private static final String PREFIX_FATAL = "[FATAL]";
  private static final String PREFIX_INFO = "[INFO]";
  private static final String PREFIX_WARN = "[WARN]";

  static {
    COMPILE_TIME_LOG_LEVEL = LOG_LEVEL_DEBUG;
    initLoggers();
  }

  public static void addLogger(Logger logger) {
    if (logger.isSupported()) {
      loggers.add(logger);
    }
  }

  public static void clear() {
    for (Iterator iterator = loggers.iterator(); iterator.hasNext();) {
      Logger logger = (Logger) iterator.next();
      logger.clear();
    }
  }

  public static void debug(String message) {
    debug(message, (Throwable) null);
  }

  public static void debug(String message, JavaScriptObject e) {
    debug(message, LogUtil.convertJavaScriptObjectToException(e));
  }

  public static void debug(String message, Throwable e) {
    if (COMPILE_TIME_LOG_LEVEL <= LOG_LEVEL_DEBUG) {
      message = format(PREFIX_DEBUG, message);
      for (Iterator iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = (Logger) iterator.next();
        logger.debug(message, e);
      }
    }
  }

  public static void debug2(String message) {
    if (COMPILE_TIME_LOG_LEVEL <= LOG_LEVEL_DEBUG) {
      message = format(PREFIX_DEBUG, message);
      for (Iterator iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = (Logger) iterator.next();
        logger.debug(message, null);
      }
    }
  }

  public static void error(String message) {
    error(message, (Throwable) null);
  }

  public static void error(String message, JavaScriptObject e) {
    error(message, LogUtil.convertJavaScriptObjectToException(e));
  }

  public static void error(String message, Throwable e) {
    if (COMPILE_TIME_LOG_LEVEL <= LOG_LEVEL_ERROR) {
      message = format(PREFIX_ERROR, message);
      for (Iterator iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = (Logger) iterator.next();
        logger.error(message, e);
      }
    }
  }

  public static void fatal(String message) {
    fatal(message, (Throwable) null);
  }

  public static void fatal(String message, JavaScriptObject e) {
    fatal(message, LogUtil.convertJavaScriptObjectToException(e));
  }

  public static void fatal(String message, Throwable e) {
    if (COMPILE_TIME_LOG_LEVEL <= LOG_LEVEL_FATAL) {
      message = format(PREFIX_FATAL, message);
      for (Iterator iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = (Logger) iterator.next();
        logger.fatal(message, e);
      }
    }
  }

  public static void info(String message) {
    info(message, (Throwable) null);
  }

  public static void info(String message, JavaScriptObject e) {
    info(message, LogUtil.convertJavaScriptObjectToException(e));
  }

  public static void info(String message, Throwable e) {
    if (COMPILE_TIME_LOG_LEVEL <= LOG_LEVEL_INFO) {
      message = format(PREFIX_INFO, message);
      for (Iterator iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = (Logger) iterator.next();
        logger.info(message, e);
      }
    }
  }

  public static boolean isDebugEnabled() {
    return COMPILE_TIME_LOG_LEVEL <= LOG_LEVEL_DEBUG;
  }

  public static boolean isErrorEnabled() {
    return COMPILE_TIME_LOG_LEVEL <= LOG_LEVEL_ERROR;
  }

  public static boolean isFatalEnabled() {
    return COMPILE_TIME_LOG_LEVEL <= LOG_LEVEL_FATAL;
  }

  public static boolean isInfoEnabled() {
    return COMPILE_TIME_LOG_LEVEL <= LOG_LEVEL_INFO;
  }

  public static boolean isWarnEnabled() {
    return COMPILE_TIME_LOG_LEVEL <= LOG_LEVEL_WARN;
  }

  public static void warn(String message) {
    warn(message, (Throwable) null);
  }

  public static void warn(String message, JavaScriptObject e) {
    warn(message, LogUtil.convertJavaScriptObjectToException(e));
  }

  public static void warn(String message, Throwable e) {
    if (COMPILE_TIME_LOG_LEVEL <= LOG_LEVEL_WARN) {
      message = format(PREFIX_WARN, message);
      for (Iterator iterator = loggers.iterator(); iterator.hasNext();) {
        Logger logger = (Logger) iterator.next();
        logger.warn(message, e);
      }
    }
  }

  private static String format(String prefix, String message) {
    return prefix + " " + message.replaceAll("\n", "\n" + prefix);
  }

  private static void initLoggers() {
    ConsoleLogger consoleLogger = new ConsoleLogger();
    FirebugLogger firebugLogger = new FirebugLogger();

    if (firebugLogger.isSupported()) {
      addLogger(firebugLogger);
    } else if (consoleLogger.isSupported()) {
      addLogger(consoleLogger);
    }

    // During GWT development certain failures may prevent the DOM/UI from working
    try {
      addLogger(new DivLogger());
    } catch (Throwable ex) {
      Window.alert("WARNING: Unable to instantiate '" + DivLogger.class + "' due to " + ex.toString());
    }
    addLogger(new GWTLogger());
    addLogger(new SystemLogger());

    clear();
  }

  public boolean removeLogger(Logger logger) {
    return loggers.remove(logger);
  }
}