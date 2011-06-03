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
import com.allen_sauer.gwt.log.client.Logger;
import com.allen_sauer.gwt.log.shared.LogRecord;

/**
 * Implementation of {@link LogImplInterface} that is designed to be compiled out via GWT compiler
 * dead code
 * elimination.
 */
public final class LogImplOff extends LogImpl {
  // CHECKSTYLE_JAVADOC_OFF
  @Override
  public void addLogger(Logger logger) {
  }

  @Override
  public void clear() {
  }

  @Override
  public void debug(String category, String message, JavaScriptObject e) {
  }

  @Override
  public void debug(String category, String message, Throwable e) {
  }

  @Override
  public void diagnostic(String message, Throwable e) {
  }

  @Override
  public void error(String category, String message, JavaScriptObject e) {
  }

  @Override
  public void error(String category, String message, Throwable e) {
  }

  @Override
  public void fatal(String category, String message, JavaScriptObject e) {
  }

  @Override
  public void fatal(String category, String message, Throwable e) {
  }

  @Override
  public int getCurrentLogLevel() {
    return Log.LOG_LEVEL_OFF;
  }

  @Override
  public <T extends Logger> T getLogger(Class<T> clazz) {
    return null;
  }

  @Override
  public int getLowestLogLevel() {
    return Log.LOG_LEVEL_OFF;
  }

  @Override
  public void info(String category, String message, JavaScriptObject e) {
  }

  @Override
  public void info(String category, String message, Throwable e) {
  }

  @Override
  public void init() {
  }

  @Override
  public boolean isDebugEnabled() {
    return false;
  }

  @Override
  public boolean isErrorEnabled() {
    return false;
  }

  @Override
  public boolean isFatalEnabled() {
    return false;
  }

  @Override
  public boolean isInfoEnabled() {
    return false;
  }

  @Override
  public boolean isLoggingEnabled() {
    return false;
  }

  @Override
  public boolean isTraceEnabled() {
    return false;
  }

  @Override
  public boolean isWarnEnabled() {
    return false;
  }

  @Override
  public void log(LogRecord record) {
  }

  @Override
  public int setCurrentLogLevel(int level) {
    return Log.LOG_LEVEL_OFF;
  }

  @Override
  public void setUncaughtExceptionHandler() {
  }

  @Override
  public void trace(String category, String message, JavaScriptObject e) {
  }

  @Override
  public void trace(String category, String message, Throwable e) {
  }

  @Override
  public void warn(String category, String message, JavaScriptObject e) {
  }

  @Override
  public void warn(String category, String message, Throwable e) {
  }
}
