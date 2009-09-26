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
package com.allen_sauer.gwt.log.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

import com.allen_sauer.gwt.log.client.impl.LogImpl;

/**
 * Static logging functions for client code.
 */
public final class Log {
  /**
   * Constant <code>int</code> value <CODE>10000</CODE>, representing <code>DEBUG</code> logging level,
   * to display debugging messages or higher.
   */
  public static final int LOG_LEVEL_DEBUG = 10000;
  /**
   * Constant <code>int</code> value <CODE>40000</CODE>, representing <code>ERROR</code> logging level,
   * to display error messages or higher.
   */
  public static final int LOG_LEVEL_ERROR = 40000;
  /**
   * Constant <code>int</code> value <CODE>50000</CODE>, representing <code>FATAL</code> logging level,
   * to display fatal messages or higher.
   */
  public static final int LOG_LEVEL_FATAL = 50000;

  /**
   * Constant <code>int</code> value <CODE>20000</CODE>, representing <code>INT</code> logging level,
   * to display informational messages or higher.
   */
  public static final int LOG_LEVEL_INFO = 20000;

  /**
   * Logging disabled. Equivalent to <CODE>Integer.MAX_VALUE</CODE>.
   */
  public static final int LOG_LEVEL_OFF = Integer.MAX_VALUE;

  /**
   * Constant <code>int</code> value <CODE>5000</CODE>, representing <code>TRACE</code> logging level,
   * to display trace messages or higher.
   */
  public static final int LOG_LEVEL_TRACE = 5000;

  /**
   * Constant <code>int</code> value <CODE>30000</CODE>, representing <code>WARN</code> logging level,
   * to display warning messages or higher.
   */
  public static final int LOG_LEVEL_WARN = 30000;

  private static LogImpl impl;

  static {
    impl = (LogImpl) GWT.create(LogImpl.class);
    impl.init();
  }

  /**
   * Register a new logger.
   * 
   * @param logger the logger to add.
   */
  public static void addLogger(Logger logger) {
    impl.addLogger(logger);
  }

  /**
   * Supported loggers will have their output cleared.
   * Alternatively, some loggers may either insert
   * separator text, or may do nothing.
   */
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
   * @param e the native JavaScript exception object to be logged
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
   * @param e the exception to be logged
   */
  public static void debug(String message, Throwable e) {
    impl.debug(message, e);
  }

  /**
     * Log an internal <code>gwt-log</code> diagnostic message.
     * 
     * @deprecated For internal gwt-log use only.
     * 
     * @param message the text to be logged
     * @param e the native JavaScript exception object to be logged
     */
  @Deprecated
  public static void diagnostic(String message, Throwable e) {
    impl.diagnostic(message, e);
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
   * @param e the native JavaScript exception object to be logged
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
   * @param e the exception to be logged
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
   * @param e the native JavaScript exception object to be logged
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
   * @param e the exception to be logged
   */
  public static void fatal(String message, Throwable e) {
    impl.fatal(message, e);
  }

  /**
   * Get the current gwt-log log level.
   * 
   * @return the current gwt-log log level
   */
  public static int getCurrentLogLevel() {
    return impl.getCurrentLogLevel();
  }

  /**
   * Get a text representation of the current gwt-log log level.
   * 
   * @return the current gwt-log log level
   */
  public static String getCurrentLogLevelString() {
    return impl.getCurrentLogLevelString();
  }

  /**
   * @param clazz the class of the desired logger
   * @param <T> the desired type of the {@link Logger}
   * @return the desired logger instance or <code>null</code> if no such longer exists
   */
  public static <T extends Logger> T getLogger(Class<T> clazz) {
    return impl.getLogger(clazz);
  }

  /**
   * Get compile time gwt-log log level.
   * 
   * @return lowest (compile time) gwt-log log level
   */
  public static int getLowestLogLevel() {
    return impl.getLowestLogLevel();
  }

  /**
   * Get compile time gwt-log log level as text.
   * 
   * @return lowest (compile time) gwt-log log level
   */
  public static String getLowestLogLevelString() {
    return impl.getLowestLogLevelString();
  }

  /**
   * Retrieve a human readable string version for gwt-log.
   * Formatting of this text is not guaranteed to remain
   * consistent.
   * 
   * @return the human readable version text
   */
  public static String getVersion() {
    String version = "@GWT_LOG_VERSION@";
    return version.matches("^@.+@$") ? "0.0.0" : version;
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
   * @param e the native JavaScript exception object to be logged
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
   * @param e the exception to be logged
   */
  public static void info(String message, Throwable e) {
    impl.info(message, e);
  }

  /**
   * Guard utility method to prevent expensive parameter evaluation
   * side effects when logging is set at a higher level, e.g.
   * <pre>
   *   // parameter(s) are evaluated even if method call does nothing
   *   Log.debug(...);
   *   
   *   if (Log.isDebugEnabled()) {
   *     // code inside the guard is only conditionally evaluated
   *     Log.debug(...);
   *   }
   * </pre>   
   * 
   * @return <code>true</code> if the current log level is at least <code>DEBUG</code>
   */
  public static boolean isDebugEnabled() {
    return impl.isDebugEnabled();
  }

  /**
   * Guard utility method to prevent expensive parameter evaluation
   * side effects when logging is set at a higher level.
   * 
   * @see #isDebugEnabled()
   * 
   * @return <code>true</code> if the current log level is at least <code>ERROR</code>
   */
  public static boolean isErrorEnabled() {
    return impl.isErrorEnabled();
  }

  /**
   * Guard utility method to prevent expensive parameter evaluation
   * side effects when logging is set at a higher level.
   * 
   * @see #isDebugEnabled()
   * 
   * @return <code>true</code> if the current log level is at least <code>FATAL</code>
   */
  public static boolean isFatalEnabled() {
    return impl.isFatalEnabled();
  }

  /**
   * Guard utility method to prevent expensive parameter evaluation
   * side effects when logging is set at a higher level.
   * 
   * @see #isDebugEnabled()
   * 
   * @return <code>true</code> if the current log level is at least <code>INFO</code>
   */
  public static boolean isInfoEnabled() {
    return impl.isInfoEnabled();
  }

  /**
   * Guard utility method to prevent expensive parameter evaluation
   * side effects when logging is disabled.
   * 
   * @see #isTraceEnabled()
   * @see #isDebugEnabled()
   * @see #isInfoEnabled()
   * @see #isWarnEnabled()
   * @see #isErrorEnabled()
   * @see #isFatalEnabled()
   * 
   * @return <code>true</code> if the current log level is not <code>OFF</code>
   */
  public static boolean isLoggingEnabled() {
    return impl.isLoggingEnabled();
  }

  /**
   * Guard utility method to prevent expensive parameter evaluation
   * side effects when logging is set at a higher level, e.g.
   * <pre>
   *   // parameter(s) are evaluated even if method call does nothing
   *   Log.trace(...);
   *   
   *   if (Log.isTraceEnabled()) {
   *     // code inside the guard is only conditionally evaluated
   *     Log.trace(...);
   *   }
   * </pre>   
   * 
   * @return <code>true</code> if the current log level is at least <code>TRACE</code>
   */
  public static boolean isTraceEnabled() {
    return impl.isTraceEnabled();
  }

  /**
   * Guard utility method to prevent expensive parameter evaluation
   * side effects when logging is set at a higher level.
   * 
   * @see #isDebugEnabled()
   * 
   * @return <code>true</code> if the current log level is at least <code>WARN</code>
   */
  public static boolean isWarnEnabled() {
    return impl.isWarnEnabled();
  }

  /**
   * Set the current gwt-log log level to a requested level.
   * The actual level may be higher than the requested level
   * due to the compile time log level that is currently in
   * effect.
   * 
   * @param level the new gwt-log log level
   * @return the resulting gwt-log log level
   */
  public static int setCurrentLogLevel(int level) {
    return impl.setCurrentLogLevel(level);
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
   * Log a <code>TRACE</code> level message with no
   * exception information.
   * 
   * @see Log#trace(String, JavaScriptObject)
   * @see Log#trace(String, Throwable)
   * 
   * @param message the text to be logged
   */
  public static void trace(String message) {
    trace(message, (Throwable) null);
  }

  /**
   * Log a <code>TRACE</code> level message from within
   * a JSNI try/catch block, e.g.
   * <pre>
   *   private native void jsniTryCatchExample()
   *   /&#42;-{
   *     try {
   *       // throws exception
   *       non_existant_variable.non_existant_method();
   *     } catch(e) {
   *       &#64;com.allen_sauer.gwt.log.client.Log::trace(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)("Caught JSNI Exception", e);
   *     }
   *   }-&#42;/;
   * </pre>   
   * 
   * @see Log#trace(String)
   * @see Log#trace(String, Throwable)
   * 
   * @param message the text to be logged
   * @param e the native JavaScript exception object to be logged
   */
  public static void trace(String message, JavaScriptObject e) {
    impl.trace(message, e);
  }

  /**
   * Log a <code>TRACE</code> level message from within
   * a Java try/catch block, e.g.
   * <pre>
   *   private native void javaTryCatchExample() {
   *     try {
   *       throw new RuntimeException();
   *     } catch(e) {
   *       Log.trace("Caught Java Exception", e);
   *     }
   *   }
   * </pre>   
   * 
   * @see Log#trace(String)
   * @see Log#trace(String, JavaScriptObject)
   * 
   * @param message the text to be logged
   * @param e the exception to be logged
   */
  public static void trace(String message, Throwable e) {
    impl.trace(message, e);
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
   * @param e the native JavaScript exception object to be logged
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
   * @param e the exception to be logged
   */
  public static void warn(String message, Throwable e) {
    impl.warn(message, e);
  }

  /**
   * Default private constructor, to be used by GWT module initialization only.
   */
  private Log() {
  }
}