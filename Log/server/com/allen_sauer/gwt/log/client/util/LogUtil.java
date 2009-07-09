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
package com.allen_sauer.gwt.log.client.util;

import com.allen_sauer.gwt.log.client.Log;

// CHECKSTYLE_JAVADOC_OFF
public class LogUtil {
  private static final String LOG_LEVEL_TEXT_DEBUG = "DEBUG";
  private static final String LOG_LEVEL_TEXT_ERROR = "ERROR";
  private static final String LOG_LEVEL_TEXT_FATAL = "FATAL";
  private static final String LOG_LEVEL_TEXT_INFO = "INFO";
  private static final String LOG_LEVEL_TEXT_OFF = "OFF";
  private static final String LOG_LEVEL_TEXT_TRACE = "TRACE";
  private static final String LOG_LEVEL_TEXT_WARN = "WARN";

  public static String levelToString(int level) {
    switch (level) {
      case Log.LOG_LEVEL_TRACE:
        return LOG_LEVEL_TEXT_TRACE;
      case Log.LOG_LEVEL_DEBUG:
        return LOG_LEVEL_TEXT_DEBUG;
      case Log.LOG_LEVEL_INFO:
        return LOG_LEVEL_TEXT_INFO;
      case Log.LOG_LEVEL_WARN:
        return LOG_LEVEL_TEXT_WARN;
      case Log.LOG_LEVEL_ERROR:
        return LOG_LEVEL_TEXT_ERROR;
      case Log.LOG_LEVEL_FATAL:
        return LOG_LEVEL_TEXT_FATAL;
      case Log.LOG_LEVEL_OFF:
        return LOG_LEVEL_TEXT_OFF;
      default:
        throw new IllegalArgumentException();
    }
  }

  public static int stringToLevel(String logLevelString) {
    throw new UnsupportedOperationException(); // never used server-side
  }
}
