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

public final class ServerLogImplLog4J extends ServerLogImpl {
  private final Logger logger = Logger.getLogger("gwt-log");

  public final void clear() {
    // do nothing
  }

  public final void debug(String message, Throwable e) {
    logger.debug(message, e);
  }

  public final void error(String message, Throwable e) {
    logger.error(message, e);
  }

  public final void fatal(String message, Throwable e) {
    logger.fatal(message, e);
  }

  public final int getCurrentLogLevel() {
    return logger.getEffectiveLevel().toInt();
  }

  public final void info(String message, Throwable e) {
    logger.info(message, e);
  }

  public final boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  public final boolean isErrorEnabled() {
    return logger.getLevel().toInt() >= Level.ERROR_INT;
  }

  public final boolean isFatalEnabled() {
    return logger.getLevel().toInt() >= Level.FATAL_INT;
  }

  public final boolean isInfoEnabled() {
    return logger.getLevel().toInt() >= Level.INFO_INT;
  }

  public final boolean isLoggingEnabled() {
    return logger.getLevel().toInt() >= Level.OFF_INT;
  }

  public final boolean isWarnEnabled() {
    return logger.getLevel().toInt() >= Level.WARN_INT;
  }

  /**
   * Identity mapping since gwt-log log4j levels have integer identity.
   */
  public int mapGWTLogLevelToImplLevel(int gwtLogLevel) {
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

  /**
   * Identity mapping since gwt-log log4j levels have integer identity.
   */
  public int mapImplLevelToGWTLogLevel(int implLogLevel) {
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

  public final void setCurrentLogLevel(int level) {
    logger.setLevel(Level.toLevel(level));
  }

  public final void warn(String message, Throwable e) {
    logger.warn(message, e);
  }
}
