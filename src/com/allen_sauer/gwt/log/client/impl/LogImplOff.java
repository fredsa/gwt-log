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
import com.allen_sauer.gwt.log.client.FirebugLogger;
import com.allen_sauer.gwt.log.client.GWTLogger;
import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.Logger;
import com.allen_sauer.gwt.log.client.LoggerDIV;
import com.allen_sauer.gwt.log.client.LoggerSystem;

/**
 * Implementation of {@link LogImpl} that is designed to be compiled out
 * via GWT compiler dead code elimination.
 */
public class LogImplOff extends LogImpl {
  public void addLogger(Logger logger) {
  }

  public final void clear() {
  }

  public final void debug(String message, JavaScriptObject e) {
  }

  public final void debug(String message, Throwable e) {
  }

  public void error(String message, JavaScriptObject e) {
  }

  public void error(String message, Throwable e) {
  }

  public void fatal(String message, JavaScriptObject e) {
  }

  public void fatal(String message, Throwable e) {
  }

  public final int getCurrentLogLevel() {
    return Log.LOG_LEVEL_OFF;
  }

  public String getCurrentLogLevelString() {
    return LOG_LEVEL_TEXT_OFF;
  }

  public Logger getLogger(Class clazz) {
    return null;
  }

  public ConsoleLogger getLoggerConsole() {
    return null;
  }

  public LoggerDIV getLoggerDiv() {
    return null;
  }

  public FirebugLogger getLoggerFirebug() {
    return null;
  }

  public GWTLogger getLoggerGWT() {
    return null;
  }

  public LoggerSystem getLoggerSystem() {
    return null;
  }

  public final int getLowestLogLevel() {
    return Log.LOG_LEVEL_OFF;
  }

  public String getLowestLogLevelString() {
    return LOG_LEVEL_TEXT_OFF;
  }

  public void info(String message, JavaScriptObject e) {
  }

  public void info(String message, Throwable e) {
  }

  public final boolean isDebugEnabled() {
    return false;
  }

  public final boolean isErrorEnabled() {
    return false;
  }

  public final boolean isFatalEnabled() {
    return false;
  }

  public final boolean isInfoEnabled() {
    return false;
  }

  public final boolean isLoggingEnabled() {
    return false;
  }

  public final boolean isWarnEnabled() {
    return false;
  }

  public boolean removeLogger(Logger logger) {
    return false;
  }

  public int setCurrentLogLevel(int level) {
    return Log.LOG_LEVEL_OFF;
  }

  public void setUncaughtExceptionHandler() {
  }

  public void warn(String message, JavaScriptObject e) {
  }

  public void warn(String message, Throwable e) {
  }
}
