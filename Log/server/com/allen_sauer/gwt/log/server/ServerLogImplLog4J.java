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
package com.allen_sauer.gwt.log.server;

import com.allen_sauer.gwt.log.client.ServerSideLog;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

//CHECKSTYLE_JAVADOC_OFF
public final class ServerLogImplLog4J extends ServerLogImpl {
  private final Logger logger = Logger.getLogger("gwt-log");

  public void clear() {
    // do nothing
  }

  public void debug(String message, Throwable e) {
    logger.debug(message, e);
  }

  public void error(String message, Throwable e) {
    logger.error(message, e);
  }

  public void fatal(String message, Throwable e) {
    logger.fatal(message, e);
  }

  public int getCurrentLogLevel() {
    return logger.getEffectiveLevel().toInt();
  }

  public void info(String message, Throwable e) {
    logger.info(message, e);
  }

  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  public boolean isErrorEnabled() {
    return logger.getLevel().toInt() >= Level.ERROR_INT;
  }

  public boolean isFatalEnabled() {
    return logger.getLevel().toInt() >= Level.FATAL_INT;
  }

  public boolean isInfoEnabled() {
    return logger.getLevel().toInt() >= Level.INFO_INT;
  }

  public boolean isLoggingEnabled() {
    return logger.getLevel().toInt() >= Level.OFF_INT;
  }

  public boolean isWarnEnabled() {
    return logger.getLevel().toInt() >= Level.WARN_INT;
  }

  public int mapGWTLogLevelToImplLevel(int gwtLogLevel) {
    // Identity mapping since gwt-log log4j levels have integer identity.
    switch (gwtLogLevel) {
      case ServerSideLog.LOG_LEVEL_DEBUG:
      case ServerSideLog.LOG_LEVEL_INFO:
      case ServerSideLog.LOG_LEVEL_WARN:
      case ServerSideLog.LOG_LEVEL_ERROR:
      case ServerSideLog.LOG_LEVEL_FATAL:
      case ServerSideLog.LOG_LEVEL_OFF:
        return gwtLogLevel;
      default:
        throw new IllegalArgumentException();
    }
  }

  public void setCurrentImplLogLevel(int level) {
    logger.setLevel(Level.toLevel(level));
  }

  public void warn(String message, Throwable e) {
    logger.warn(message, e);
  }

  protected int mapImplLevelToGWTLogLevel(int implLogLevel) {
    // Identity mapping since gwt-log log4j levels have integer identity.
    switch (implLogLevel) {
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
