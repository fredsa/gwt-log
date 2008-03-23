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

import com.google.gwt.core.client.JavaScriptObject;

import com.allen_sauer.gwt.log.client.ConsoleLogger;
import com.allen_sauer.gwt.log.client.DivLogger;
import com.allen_sauer.gwt.log.client.FirebugLogger;
import com.allen_sauer.gwt.log.client.GWTLogger;
import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.Logger;
import com.allen_sauer.gwt.log.client.SystemLogger;

/**
 * Implementation of {@link LogImpl} that is designed to be compiled out
 * via GWT compiler dead code elimination.
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
  public void debug(String message, JavaScriptObject e) {
  }

  @Override
  public void debug(String message, Throwable e) {
  }

  @Override
  public void diagnostic(String message, Throwable e) {
  }

  @Override
  public void error(String message, JavaScriptObject e) {
  }

  @Override
  public void error(String message, Throwable e) {
  }

  @Override
  public void fatal(String message, JavaScriptObject e) {
  }

  @Override
  public void fatal(String message, Throwable e) {
  }

  @Override
  public int getCurrentLogLevel() {
    return Log.LOG_LEVEL_OFF;
  }

  @Override
  public Logger getLogger(Class clazz) {
    return null;
  }

  @Override
  public ConsoleLogger getLoggerConsole() {
    return null;
  }

  @Override
  public DivLogger getLoggerDiv() {
    return null;
  }

  @Override
  public FirebugLogger getLoggerFirebug() {
    return null;
  }

  @Override
  public GWTLogger getLoggerGWT() {
    return null;
  }

  @Override
  public SystemLogger getLoggerSystem() {
    return null;
  }

  @Override
  public int getLowestLogLevel() {
    return Log.LOG_LEVEL_OFF;
  }

  @Override
  public void info(String message, JavaScriptObject e) {
  }

  @Override
  public void info(String message, Throwable e) {
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
  public boolean isWarnEnabled() {
    return false;
  }

  @Override
  public int setCurrentLogLevel(int level) {
    return Log.LOG_LEVEL_OFF;
  }

  @Override
  public void setUncaughtExceptionHandler() {
  }

  @Override
  public void warn(String message, JavaScriptObject e) {
  }

  @Override
  public void warn(String message, Throwable e) {
  }
}
