/*
 * Copyright 2010 Fred Sauer
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
package com.allen_sauer.gwt.log.client;

import com.allen_sauer.gwt.log.shared.LogRecord;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Fake RemoteLogger, which is used by default when the gwt-log-RemoteLogger
 * module has not been inherited by the application.
 */
public class RemoteLogger extends NullLogger {
  private final ArrayList<Logger> loggers = new ArrayList<Logger>();

  /**
   * Retrieves a previously added logger or null if the logger was not added.
   * 
   * @param <T> the type of logger to be retrieved
   * @param clazz the class of the logger to retrieve
   * @return the desired logger instance or null
   */
  @SuppressWarnings("unchecked")
  public final <T extends Logger> T getLogger(Class<T> clazz) {
    for (Logger logger : loggers) {
      if (logger.getClass() == clazz) {
        return (T) logger;
      }
    }
    return null;
  }

  /**
   * Adds a logger.
   * 
   * @param logger the logger to add
   */
  public void loggersAdd(Logger logger) {
    if (logger.isSupported()) {
      loggers.add(logger);
    }
  }

  /**
   * Call {@link #clear()} on all the loggers.
   */
  public final void loggersClear() {
    //self
    clear();

    //others
    for (Iterator<Logger> iterator = loggers.iterator(); iterator.hasNext();) {
      Logger logger = iterator.next();
      try {
        logger.clear();
      } catch (RuntimeException e) {
        reportAndRemoveLogger(iterator, logger, e);
      }
    }
  }

  /**
   * Call {@link #log(LogRecord)} on all the loggers. In this implementation we just pass the call
   * to all our loggers immediately.
   * 
   * @see RemoteLoggerImpl#log(LogRecord)
   * 
   * @param record the LogRecord to log.
   */
  public void loggersLog(LogRecord record) {
    for (Iterator<Logger> iterator = loggers.iterator(); iterator.hasNext();) {
      Logger logger = iterator.next();
      try {
        logger.log(record);
      } catch (RuntimeException e1) {
        reportAndRemoveLogger(iterator, logger, e1);
      }
    }
  }

  /**
   * Call {@link #setCurrentLogLevel(int)} on all the loggers.
   * 
   * @param level the new log level
   */
  public void loggersSetCurrentLogLevel(int level) {
    //self
    setCurrentLogLevel(level);

    //others
    for (Iterator<Logger> iterator = loggers.iterator(); iterator.hasNext();) {
      Logger logger = iterator.next();
      try {
        logger.setCurrentLogLevel(level);
      } catch (RuntimeException e) {
        reportAndRemoveLogger(iterator, logger, e);
      }
    }
  }

  @SuppressWarnings("deprecation")
  private void reportAndRemoveLogger(
      final Iterator<Logger> iterator, final Logger logger, final RuntimeException e) {
    iterator.remove();
    loggers.remove(logger);
    Log.diagnostic("Removing '" + logger.getClass().getName() + "' due to unexecpted exception", e);
  }

}
