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

  /**
   * Log a <code>DEBUG</code> level message with no
   * exception information.
   * 
   * @see Log#debug(String, JavaScriptObject)
   * @see Log#debug(String, Throwable)
   * 
   * @param message the text to be logged
   */
  public static void debug(String message) {
    debug(message, (Throwable) null);
  }

  /**
   * Log a <code>DEBUG</code> level message from within
   * a JSNI try/catch block, e.g.
   * <pre>
   *   private native void jsniTryCatchExample()
   *   /&#42;-{
   *     try {
   *       // throws exception
   *       non_existant_variable.non_existant_method();
   *     } catch(e) {
   *       &#64;com.allen_sauer.gwt.log.client.Log::debug(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)("Caught JSNI Exception", e);
   *     }
   *   }-&#42;/;
   * </pre>   
   * 
   * @see Log#debug(String)
   * @see Log#debug(String, Throwable)
   * 
   * @param message the text to be logged
   * @param e the native JavaScript exception object
   */
  public static void debug(String message, JavaScriptObject e) {
    impl.debug(message, e);
  }

  /**
   * Log a <code>DEBUG</code> level message from within
   * a Java try/catch block, e.g.
   * <pre>
   *   private native void javaTryCatchExample() {
   *     try {
   *       throw new RuntimeException();
   *     } catch(e) {
   *       Log.debug("Caught Java Exception", e);
   *     }
   *   }
   * </pre>   
   * 
   * @see Log#debug(String)
   * @see Log#debug(String, JavaScriptObject)
   * 
   * @param message the text to be logged
   * @param e the native JavaScript exception object
   */
  public static void debug(String message, Throwable e) {
    impl.debug(message, e);
  }

  /**
   * Log a <code>ERROR</code> level message with no
   * exception information.
   * 
   * @see Log#error(String, JavaScriptObject)
   * @see Log#error(String, Throwable)
   * 
   * @param message the text to be logged
   */
  public static void error(String message) {
    error(message, (Throwable) null);
  }

  /**
   * Log a <code>ERROR</code> level message from within
   * a JSNI try/catch block, e.g.
   * <pre>
   *   private native void jsniTryCatchExample()
   *   /&#42;-{
   *     try {
   *       // throws exception
   *       non_existant_variable.non_existant_method();
   *     } catch(e) {
   *       &#64;com.allen_sauer.gwt.log.client.Log::error(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)("Caught JSNI Exception", e);
   *     }
   *   }-&#42;/;
   * </pre>   
   * 
   * @see Log#error(String)
   * @see Log#error(String, Throwable)
   * 
   * @param message the text to be logged
   * @param e the native JavaScript exception object
   */
  public static void error(String message, JavaScriptObject e) {
    impl.error(message, e);
  }

  /**
   * Log a <code>ERROR</code> level message from within
   * a Java try/catch block, e.g.
   * <pre>
   *   private native void javaTryCatchExample() {
   *     try {
   *       throw new RuntimeException();
   *     } catch(e) {
   *       Log.error("Caught Java Exception", e);
   *     }
   *   }
   * </pre>   
   * 
   * @see Log#error(String)
   * @see Log#error(String, JavaScriptObject)
   * 
   * @param message the text to be logged
   * @param e the native JavaScript exception object
   */
  public static void error(String message, Throwable e) {
    impl.error(message, e);
  }

  /**
   * Log a <code>FATAL</code> level message with no
   * exception information.
   * 
   * @see Log#fatal(String, JavaScriptObject)
   * @see Log#fatal(String, Throwable)
   * 
   * @param message the text to be logged
   */
  public static void fatal(String message) {
    fatal(message, (Throwable) null);
  }

  /**
   * Log a <code>FATAL</code> level message from within
   * a JSNI try/catch block, e.g.
   * <pre>
   *   private native void jsniTryCatchExample()
   *   /&#42;-{
   *     try {
   *       // throws exception
   *       non_existant_variable.non_existant_method();
   *     } catch(e) {
   *       &#64;com.allen_sauer.gwt.log.client.Log::fatal(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)("Caught JSNI Exception", e);
   *     }
   *   }-&#42;/;
   * </pre>   
   * 
   * @see Log#fatal(String)
   * @see Log#fatal(String, Throwable)
   * 
   * @param message the text to be logged
   * @param e the native JavaScript exception object
   */
  public static void fatal(String message, JavaScriptObject e) {
    impl.fatal(message, e);
  }

  /**
   * Log a <code>FATAL</code> level message from within
   * a Java try/catch block, e.g.
   * <pre>
   *   private native void javaTryCatchExample() {
   *     try {
   *       throw new RuntimeException();
   *     } catch(e) {
   *       Log.fatal("Caught Java Exception", e);
   *     }
   *   }
   * </pre>   
   * 
   * @see Log#fatal(String)
   * @see Log#fatal(String, JavaScriptObject)
   * 
   * @param message the text to be logged
   * @param e the native JavaScript exception object
   */
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

  /**
   * Log a <code>INFO</code> level message with no
   * exception information.
   * 
   * @see Log#info(String, JavaScriptObject)
   * @see Log#info(String, Throwable)
   * 
   * @param message the text to be logged
   */
  public static void info(String message) {
    info(message, (Throwable) null);
  }

  /**
   * Log a <code>INFO</code> level message from within
   * a JSNI try/catch block, e.g.
   * <pre>
   *   private native void jsniTryCatchExample()
   *   /&#42;-{
   *     try {
   *       // throws exception
   *       non_existant_variable.non_existant_method();
   *     } catch(e) {
   *       &#64;com.allen_sauer.gwt.log.client.Log::info(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)("Caught JSNI Exception", e);
   *     }
   *   }-&#42;/;
   * </pre>   
   * 
   * @see Log#info(String)
   * @see Log#info(String, Throwable)
   * 
   * @param message the text to be logged
   * @param e the native JavaScript exception object
   */
  public static void info(String message, JavaScriptObject e) {
    impl.info(message, e);
  }

  /**
   * Log a <code>INFO</code> level message from within
   * a Java try/catch block, e.g.
   * <pre>
   *   private native void javaTryCatchExample() {
   *     try {
   *       throw new RuntimeException();
   *     } catch(e) {
   *       Log.info("Caught Java Exception", e);
   *     }
   *   }
   * </pre>   
   * 
   * @see Log#info(String)
   * @see Log#info(String, JavaScriptObject)
   * 
   * @param message the text to be logged
   * @param e the native JavaScript exception object
   */
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

  public static boolean removeLogger(Logger logger) {
    return impl.removeLogger(logger);
  }

  public static final void setCurrentLogLevel(int level) {
    impl.setCurrentLogLevel(level);
  }

  /**
   * Installs an UncaughtExceptionHandler that will trap and
   * log <code>FATAL</code> messages, but only if <i>both</i>
   * the compile time and runtime <code>log_level</code>
   * is set to <code>FATAL</code> or lower.
   */
  public static void setUncaughtExceptionHandler() {
    impl.setUncaughtExceptionHandler();
  }

  /**
   * Log a <code>WARN</code> level message with no
   * exception information.
   * 
   * @see Log#warn(String, JavaScriptObject)
   * @see Log#warn(String, Throwable)
   * 
   * @param message the text to be logged
   */
  public static void warn(String message) {
    warn(message, (Throwable) null);
  }

  /**
   * Log a <code>WARN</code> level message from within
   * a JSNI try/catch block, e.g.
   * <pre>
   *   private native void jsniTryCatchExample()
   *   /&#42;-{
   *     try {
   *       // throws exception
   *       non_existant_variable.non_existant_method();
   *     } catch(e) {
   *       &#64;com.allen_sauer.gwt.log.client.Log::warn(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)("Caught JSNI Exception", e);
   *     }
   *   }-&#42;/;
   * </pre>   
   * 
   * @see Log#warn(String)
   * @see Log#warn(String, Throwable)
   * 
   * @param message the text to be logged
   * @param e the native JavaScript exception object
   */
  public static void warn(String message, JavaScriptObject e) {
    impl.warn(message, e);
  }

  /**
   * Log a <code>WARN</code> level message from within
   * a Java try/catch block, e.g.
   * <pre>
   *   private native void javaTryCatchExample() {
   *     try {
   *       throw new RuntimeException();
   *     } catch(e) {
   *       Log.warn("Caught Java Exception", e);
   *     }
   *   }
   * </pre>   
   * 
   * @see Log#warn(String)
   * @see Log#warn(String, Throwable)
   * 
   * @param message the text to be logged
   * @param e the native JavaScript exception object
   */
  public static void warn(String message, Throwable e) {
    impl.warn(message, e);
  }
}