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

import com.google.gwt.core.client.JavaScriptObject;

import com.allen_sauer.gwt.log.client.LogRecord;
import com.allen_sauer.gwt.log.client.LogUtil;
import com.allen_sauer.gwt.log.client.Logger;

// CHECKSTYLE_JAVADOC_OFF

public abstract class LogImpl {
  public abstract void addLogger(Logger logger);

  public abstract void clear();

  public abstract void debug(String category, String message, JavaScriptObject e);

  public abstract void debug(String category, String message, Throwable e);

  /**
   * Diagnostic (internal) messages have an implied category of 'gwt-log'
   */
  public abstract void diagnostic(String message, Throwable e);

  public abstract void error(String category, String message, JavaScriptObject e);

  public abstract void error(String category, String message, Throwable e);

  public abstract void fatal(String category, String message, JavaScriptObject e);

  public abstract void fatal(String category, String message, Throwable e);

  public abstract int getCurrentLogLevel();

  public final String getCurrentLogLevelString() {
    return LogUtil.levelToString(getCurrentLogLevel());
  }

  public abstract <T extends Logger> T getLogger(Class<T> clazz);

  public abstract int getLowestLogLevel();

  public final String getLowestLogLevelString() {
    return LogUtil.levelToString(getLowestLogLevel());
  }

  public abstract void info(String category, String message, JavaScriptObject e);

  public abstract void info(String category, String message, Throwable e);

  public abstract void init();

  public abstract boolean isDebugEnabled();

  public abstract boolean isErrorEnabled();

  public abstract boolean isFatalEnabled();

  public abstract boolean isInfoEnabled();

  public abstract boolean isLoggingEnabled();

  public abstract boolean isTraceEnabled();

  public abstract boolean isWarnEnabled();

  public abstract void log(LogRecord record);

  public abstract int setCurrentLogLevel(int level);

  public abstract void setUncaughtExceptionHandler();

  public abstract void trace(String category, String message, JavaScriptObject e);

  public abstract void trace(String category, String message, Throwable e);

  public abstract void warn(String category, String message, JavaScriptObject e);

  public abstract void warn(String category, String message, Throwable e);
}
