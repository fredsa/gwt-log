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

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.LogRecord;
import com.allen_sauer.gwt.log.client.Logger;

/**
 * Implementation of {@link LogImplInterface} that is designed to be compiled out via GWT compiler
 * dead code
 * elimination.
 */
public final class LogImplOff extends LogImpl {
  // CHECKSTYLE_JAVADOC_OFF
  public void addLogger(Logger logger) {
  }

  public void clear() {
  }

  public void debug(String category, String message, JavaScriptObject e) {
  }

  public void debug(String category, String message, Throwable e) {
  }

  public void diagnostic(String message, Throwable e) {
  }

  public void error(String category, String message, JavaScriptObject e) {
  }

  public void error(String category, String message, Throwable e) {
  }

  public void fatal(String category, String message, JavaScriptObject e) {
  }

  public void fatal(String category, String message, Throwable e) {
  }

  public int getCurrentLogLevel() {
    return Log.LOG_LEVEL_OFF;
  }

  public <T extends Logger> T getLogger(Class<T> clazz) {
    return null;
  }

  public int getLowestLogLevel() {
    return Log.LOG_LEVEL_OFF;
  }

  public void info(String category, String message, JavaScriptObject e) {
  }

  public void info(String category, String message, Throwable e) {
  }

  public void init() {
  }

  public boolean isDebugEnabled() {
    return false;
  }

  public boolean isErrorEnabled() {
    return false;
  }

  public boolean isFatalEnabled() {
    return false;
  }

  public boolean isInfoEnabled() {
    return false;
  }

  public boolean isLoggingEnabled() {
    return false;
  }

  public boolean isTraceEnabled() {
    return false;
  }

  public boolean isWarnEnabled() {
    return false;
  }

  public void log(LogRecord record) {
  }

  public int setCurrentLogLevel(int level) {
    return Log.LOG_LEVEL_OFF;
  }

  public void setUncaughtExceptionHandler() {
  }

  public void trace(String category, String message, JavaScriptObject e) {
  }

  public void trace(String category, String message, Throwable e) {
  }

  public void warn(String category, String message, JavaScriptObject e) {
  }

  public void warn(String category, String message, Throwable e) {
  }
}
