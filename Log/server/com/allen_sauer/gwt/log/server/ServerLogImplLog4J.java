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
package com.allen_sauer.gwt.log.server;

import com.allen_sauer.gwt.log.client.Log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

//CHECKSTYLE_JAVADOC_OFF
public final class ServerLogImplLog4J extends ServerLogImpl {
  private final Logger logger;

  {
    logger = Logger.getLogger("gwt-log");
    // try {
    // } catch (Throwable e) {
    // e.printStackTrace();
    // throw new RuntimeException(e);
    // }
  }

  @Override
  public void clear() {
    // do nothing
  }

  @Override
  public void debug(String message, Throwable e) {
    logger.debug(message, e);
  }

  @Override
  public void diagnostic(String message, Throwable e) {
    logger.fatal(message, e);
  }

  @Override
  public void error(String message, Throwable e) {
    logger.error(message, e);
  }

  @Override
  public void fatal(String message, Throwable e) {
    logger.fatal(message, e);
  }

  @Override
  public int getCurrentLogLevel() {
    return logger.getEffectiveLevel().toInt();
  }

  @Override
  public void info(String message, Throwable e) {
    logger.info(message, e);
  }

  @Override
  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  @Override
  public boolean isErrorEnabled() {
    return logger.getLevel().toInt() >= Level.ERROR_INT;
  }

  @Override
  public boolean isFatalEnabled() {
    return logger.getLevel().toInt() >= Level.FATAL_INT;
  }

  @Override
  public boolean isInfoEnabled() {
    return logger.getLevel().toInt() >= Level.INFO_INT;
  }

  @Override
  public boolean isLoggingEnabled() {
    return logger.getLevel().toInt() >= Level.OFF_INT;
  }

  @Override
  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }

  @Override
  public boolean isWarnEnabled() {
    return logger.getLevel().toInt() >= Level.WARN_INT;
  }

  @Override
  public int mapGWTLogLevelToImplLevel(int gwtLogLevel) {
    // Identity mapping since gwt-log log4j levels have integer identity.
    switch (gwtLogLevel) {
      case Log.LOG_LEVEL_TRACE:
      case Log.LOG_LEVEL_DEBUG:
      case Log.LOG_LEVEL_INFO:
      case Log.LOG_LEVEL_WARN:
      case Log.LOG_LEVEL_ERROR:
      case Log.LOG_LEVEL_FATAL:
      case Log.LOG_LEVEL_OFF:
        return gwtLogLevel;
      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
  public void setCurrentImplLogLevel(int level) {
    logger.setLevel(Level.toLevel(level));
  }

  @Override
  public void trace(String message, Throwable e) {
    logger.trace(message, e);
  }

  @Override
  public void warn(String message, Throwable e) {
    logger.warn(message, e);
  }

  @Override
  protected int mapImplLevelToGWTLogLevel(int implLogLevel) {
    // Identity mapping since gwt-log log4j levels have integer identity.
    switch (implLogLevel) {
      case Level.TRACE_INT:
      case Level.DEBUG_INT:
      case Level.INFO_INT:
      case Level.WARN_INT:
      case Level.ERROR_INT:
      case Level.FATAL_INT:
      case Level.OFF_INT:
        return implLogLevel;
      default:
        throw new IllegalArgumentException();
    }
  }
}
