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
package com.allen_sauer.gwt.log.client.util;

import com.google.gwt.i18n.client.DateTimeFormat;

import com.allen_sauer.gwt.log.client.Log;

import java.util.Date;

/**
 * Class with logging related utility methods.
 */
public class LogUtil {
  // CHECKSTYLE_JAVADOC_OFF

  private static final String LOG_LEVEL_TEXT_DEBUG = "DEBUG";
  private static final String LOG_LEVEL_TEXT_ERROR = "ERROR";
  private static final String LOG_LEVEL_TEXT_FATAL = "FATAL";
  private static final String LOG_LEVEL_TEXT_INFO = "INFO";
  private static final String LOG_LEVEL_TEXT_OFF = "OFF";
  private static final String LOG_LEVEL_TEXT_TRACE = "TRACE";
  private static final String LOG_LEVEL_TEXT_WARN = "WARN";
  private static final String SPACES;

  static {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < 500; i++) {
      b.append(' ');
    }
    SPACES = b.toString();
  }

  public static String formatDate(Date date, String formatMask) {
    // TODO don't instantiate a new DateTimeFormat each time this method is called
    return DateTimeFormat.getFormat(formatMask).format(date);
  }

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

  public static String padLeft(String text, int minLength) {
    int len = text.length();
    return len < minLength ? SPACES.substring(0, minLength - len) + text : text;
  }

  public static String padRight(String text, int minLength) {
    int len = text.length();
    return len < minLength ? text + SPACES.substring(0, minLength - len) : text;
  }

  // TODO add support for numeric values
  public static int stringToLevel(String logLevelString) {
    if (LOG_LEVEL_TEXT_TRACE.equals(logLevelString)) {
      return Log.LOG_LEVEL_TRACE;
    } else if (LOG_LEVEL_TEXT_DEBUG.equals(logLevelString)) {
      return Log.LOG_LEVEL_DEBUG;
    } else if (LOG_LEVEL_TEXT_INFO.equals(logLevelString)) {
      return Log.LOG_LEVEL_INFO;
    } else if (LOG_LEVEL_TEXT_WARN.equals(logLevelString)) {
      return Log.LOG_LEVEL_WARN;
    } else if (LOG_LEVEL_TEXT_ERROR.equals(logLevelString)) {
      return Log.LOG_LEVEL_ERROR;
    } else if (LOG_LEVEL_TEXT_FATAL.equals(logLevelString)) {
      return Log.LOG_LEVEL_FATAL;
    } else if (LOG_LEVEL_TEXT_OFF.equals(logLevelString)) {
      return Log.LOG_LEVEL_OFF;
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static String trim(String text, int maxLength) {
    return text.substring(0, maxLength);
  }
}
