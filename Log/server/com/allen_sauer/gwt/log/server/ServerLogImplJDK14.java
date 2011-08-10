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
import com.allen_sauer.gwt.log.shared.LogRecord;

import java.util.logging.Level;
import java.util.logging.Logger;

// CHECKSTYLE_JAVADOC_OFF
public final class ServerLogImplJDK14 implements ServerLog {
  private final Logger logger;

  {
    logger = Logger.getLogger("gwt-log");
  }

  @Override
  public int getCurrentLogLevel() {
    if (!isFatalEnabled()) {
      return Log.LOG_LEVEL_OFF;
    } else if (!isErrorEnabled()) {
      return Log.LOG_LEVEL_FATAL;
    } else if (!isWarnEnabled()) {
      return Log.LOG_LEVEL_ERROR;
    } else if (!isInfoEnabled()) {
      return Log.LOG_LEVEL_WARN;
    } else if (!isDebugEnabled()) {
      return Log.LOG_LEVEL_INFO;
    } else if (!isTraceEnabled()) {
      return Log.LOG_LEVEL_DEBUG;
    } else {
      return Log.LOG_LEVEL_TRACE;
    }
  }

  @Override
  public boolean isDebugEnabled() {
    return logger.isLoggable(Level.CONFIG);
  }

  @Override
  public boolean isErrorEnabled() {
    return logger.isLoggable(Level.SEVERE);
  }

  @Override
  public boolean isFatalEnabled() {
    return logger.isLoggable(Level.SEVERE);
  }

  @Override
  public boolean isInfoEnabled() {
    return logger.isLoggable(Level.INFO);
  }

  @Override
  public boolean isLoggingEnabled() {
    return logger.isLoggable(Level.OFF);
  }

  @Override
  public boolean isTraceEnabled() {
    return logger.isLoggable(Level.FINE);
  }

  @Override
  public boolean isWarnEnabled() {
    return logger.isLoggable(Level.WARNING);
  }

  @Override
  public void log(LogRecord record) {
    String category = record.getCategory();
    Logger log = category != null ? Logger.getLogger(category) : logger;
    log.log(mapGWTLogLevelToImplLevelObject(record.getLevel()), record.getMessage(),
        record.getThrowable());
  }

  @Override
  public int mapGWTLogLevelToImplLevel(int gwtLogLevel) {
    return mapGWTLogLevelToImplLevelObject(gwtLogLevel).intValue();
  }

  @Override
  public void setCurrentImplLogLevel(int level) {
    logger.setLevel(Level.parse("" + level));
  }

  private Level mapGWTLogLevelToImplLevelObject(int gwtLogLevel) {
    switch (gwtLogLevel) {
      case Log.LOG_LEVEL_TRACE:
        return Level.FINE;
      case Log.LOG_LEVEL_DEBUG:
        return Level.CONFIG;
      case Log.LOG_LEVEL_INFO:
        return Level.INFO;
      case Log.LOG_LEVEL_WARN:
        return Level.WARNING;
      case Log.LOG_LEVEL_ERROR:
        return Level.SEVERE;
      case Log.LOG_LEVEL_FATAL:
        return Level.SEVERE;
      case Log.LOG_LEVEL_OFF:
        return Level.OFF;
      default:
        throw new IllegalArgumentException();
    }
  }

}
