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
package com.allen_sauer.gwt.log.server;

import com.allen_sauer.gwt.log.client.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

// CHECKSTYLE_JAVADOC_OFF
public final class ServerLogImplJDK14 extends ServerLogImpl {
  private final Logger logger;

  public ServerLogImplJDK14() {
    logger = Logger.getLogger("gwt-log");
  }

  @Override
  public void clear() {
    // do nothing
  }

  @Override
  public void debug(String message, Throwable e) {
    logger.log(Level.FINE, message, e);
  }

  @Override
  public void diagnostic(String message, Throwable e) {
    logger.log(Level.SEVERE, message, e);
  }

  @Override
  public void error(String message, Throwable e) {
    logger.log(Level.SEVERE, message, e);
  }

  @Override
  public void fatal(String message, Throwable e) {
    logger.log(Level.SEVERE, message, e);
  }

  @Override
  public int getCurrentLogLevel() {
    return logger.getLevel().intValue();
  }

  @Override
  public void info(String message, Throwable e) {
    logger.log(Level.INFO, message, e);
  }

  @Override
  public boolean isDebugEnabled() {
    return logger.getLevel().intValue() >= Level.FINE.intValue();
  }

  @Override
  public boolean isErrorEnabled() {
    return logger.getLevel().intValue() >= Level.SEVERE.intValue();
  }

  @Override
  public boolean isFatalEnabled() {
    return logger.getLevel().intValue() >= Level.SEVERE.intValue();
  }

  @Override
  public boolean isInfoEnabled() {
    return logger.getLevel().intValue() >= Level.INFO.intValue();
  }

  @Override
  public boolean isLoggingEnabled() {
    return logger.getLevel().intValue() >= Level.OFF.intValue();
  }

  @Override
  public boolean isTraceEnabled() {
    return logger.getLevel().intValue() >= Level.FINEST.intValue();
  }

  @Override
  public boolean isWarnEnabled() {
    return logger.getLevel().intValue() >= Level.WARNING.intValue();
  }

  @Override
  public int mapGWTLogLevelToImplLevel(int gwtLogLevel) {
    switch (gwtLogLevel) {
      case Log.LOG_LEVEL_TRACE:
        return Level.FINEST.intValue();
      case Log.LOG_LEVEL_DEBUG:
        return Level.FINE.intValue();
      case Log.LOG_LEVEL_INFO:
        return Level.INFO.intValue();
      case Log.LOG_LEVEL_WARN:
        return Level.WARNING.intValue();
      case Log.LOG_LEVEL_ERROR:
        return Level.SEVERE.intValue();
      case Log.LOG_LEVEL_FATAL:
        return Level.SEVERE.intValue();
      case Log.LOG_LEVEL_OFF:
        return Level.OFF.intValue();
      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
  public void setCurrentImplLogLevel(int level) {
    logger.setLevel(Level.parse("" + level));
  }

  @Override
  public void trace(String message, Throwable e) {
    logger.log(Level.FINEST, message, e);
  }

  @Override
  public void warn(String message, Throwable e) {
    logger.log(Level.WARNING, message, e);
  }

  @Override
  protected int mapImplLevelToGWTLogLevel(int implLogLevel) {
    if (implLogLevel == Level.FINEST.intValue()) {
      return Log.LOG_LEVEL_TRACE;
    } else if (implLogLevel == Level.FINE.intValue()) {
      return Log.LOG_LEVEL_DEBUG;
    } else if (implLogLevel == Level.INFO.intValue()) {
      return Log.LOG_LEVEL_INFO;
    } else if (implLogLevel == Level.WARNING.intValue()) {
      return Log.LOG_LEVEL_WARN;
      // } else if (implLogLevel == Level.SEVERE.intValue()) {
      // return ServerSideLog.LOG_LEVEL_ERROR;
    } else if (implLogLevel == Level.SEVERE.intValue()) {
      return Log.LOG_LEVEL_FATAL;
    } else if (implLogLevel == Level.OFF.intValue()) {
      return Log.LOG_LEVEL_OFF;
    } else {
      throw new IllegalArgumentException();
    }
  }
}
