/*
 * Copyright 2014 Fred Sauer
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
import com.allen_sauer.gwt.log.shared.LogRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//CHECKSTYLE_JAVADOC_OFF
public final class ServerLogImplSLF4J implements ServerLog {
  private final Logger logger;

  {
    logger = LoggerFactory.getLogger(Log.class);
  }

  @Override
  public int getCurrentLogLevel() {
    return logger.isTraceEnabled() ? Log.LOG_LEVEL_TRACE : logger.isDebugEnabled()
        ? Log.LOG_LEVEL_DEBUG : logger.isInfoEnabled() ? Log.LOG_LEVEL_INFO
            : logger.isWarnEnabled() ? Log.LOG_LEVEL_WARN : logger.isErrorEnabled()
                ? Log.LOG_LEVEL_ERROR : Log.LOG_LEVEL_OFF;
  }

  @Override
  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  @Override
  public boolean isErrorEnabled() {
    return logger.isErrorEnabled();
  }

  @Override
  public boolean isFatalEnabled() {
    return logger.isErrorEnabled();
  }

  @Override
  public boolean isInfoEnabled() {
    return logger.isInfoEnabled();
  }

  @Override
  public boolean isLoggingEnabled() {
    return logger.isErrorEnabled();
  }

  @Override
  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }

  @Override
  public boolean isWarnEnabled() {
    return logger.isWarnEnabled();
  }

  @Override
  public void log(LogRecord record) {
    switch (record.getLevel()) {
      case Log.LOG_LEVEL_OFF:
        return;
      case Log.LOG_LEVEL_ERROR:
      case Log.LOG_LEVEL_FATAL:
        logger.error(record.getMessage(), record.getThrowable());
        return;
      case Log.LOG_LEVEL_WARN:
        logger.warn(record.getMessage(), record.getThrowable());
        return;
      case Log.LOG_LEVEL_INFO:
        logger.info(record.getMessage(), record.getThrowable());
        return;
      case Log.LOG_LEVEL_DEBUG:
        logger.debug(record.getMessage(), record.getThrowable());
        return;
      case Log.LOG_LEVEL_TRACE:
        logger.trace(record.getMessage(), record.getThrowable());
        return;
      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
  public int mapGWTLogLevelToImplLevel(int level) {
    return level;
  }

  @Override
  public void setCurrentImplLogLevel(int level) {
    logger.warn("Ignoring attempt to set implementation log level to " + level);
  }
}
