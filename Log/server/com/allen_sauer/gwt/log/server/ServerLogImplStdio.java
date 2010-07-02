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

// CHECKSTYLE_JAVADOC_OFF
public final class ServerLogImplStdio implements ServerLog {

  private int level;

  public int getCurrentLogLevel() {
    return level;
  }

  public boolean isDebugEnabled() {
    return level >= Log.LOG_LEVEL_DEBUG;
  }

  public boolean isErrorEnabled() {
    return level >= Log.LOG_LEVEL_ERROR;
  }

  public boolean isFatalEnabled() {
    return level >= Log.LOG_LEVEL_FATAL;
  }

  public boolean isInfoEnabled() {
    return level >= Log.LOG_LEVEL_INFO;
  }

  public boolean isLoggingEnabled() {
    return level >= Log.LOG_LEVEL_OFF;
  }

  public boolean isTraceEnabled() {
    return level >= Log.LOG_LEVEL_TRACE;
  }

  public boolean isWarnEnabled() {
    return level >= Log.LOG_LEVEL_WARN;
  }

  public void log(LogRecord record) {
    System.err.println(record.getMessage());
    Throwable e = record.getThrowable();
    if (e != null) {
      e.printStackTrace();
    }
  }

  public int mapGWTLogLevelToImplLevel(int gwtLogLevel) {
    return gwtLogLevel;
  }

  public void setCurrentImplLogLevel(int level) {
    this.level = level;
  }
}
