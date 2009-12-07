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
import com.allen_sauer.gwt.log.client.LogRecord;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import java.util.Set;
import java.util.Map.Entry;

// CHECKSTYLE_JAVADOC_OFF
public final class ServerLogImplLog4J implements ServerLog {
  private final Logger logger;

  {
    logger = Logger.getLogger("gwt-log");
  }

  public void log(LogRecord record) {
    Set<Entry<String, String>> set = record.getMapEntrySet();
    for (Entry<String, String> entry : set) {
      MDC.put(entry.getKey(), entry.getValue());
    }
    logger.log(mapGWTLogLevelToImplLevel(record.getLevel()), record.getMessage());
    for (Entry<String, String> entry : set) {
      MDC.remove(entry.getKey());
    }
  }

  private Level mapGWTLogLevelToImplLevel(int gwtLogLevel) {
    // Identity mapping since gwt-log log4j levels have integer identity.
    switch (gwtLogLevel) {
      case Log.LOG_LEVEL_TRACE:
        return Level.TRACE;
      case Log.LOG_LEVEL_DEBUG:
        return Level.DEBUG;
      case Log.LOG_LEVEL_INFO:
        return Level.INFO;
      case Log.LOG_LEVEL_WARN:
        return Level.WARN;
      case Log.LOG_LEVEL_ERROR:
        return Level.ERROR;
      case Log.LOG_LEVEL_FATAL:
        return Level.FATAL;
      case Log.LOG_LEVEL_OFF:
        return Level.OFF;
      default:
        throw new IllegalArgumentException();
    }
  }
}
