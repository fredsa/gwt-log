/**
 * Copyright 2008 Fred Sauer.
 */
package com.allen_sauer.gwt.log.client.util;

import com.allen_sauer.gwt.log.client.ServerSideLog;

public class ServerSideLogUtil {
  private static final String LOG_LEVEL_TEXT_ALL = "ALL";
  private static final String LOG_LEVEL_TEXT_DEBUG = "DEBUG";
  private static final String LOG_LEVEL_TEXT_ERROR = "ERROR";
  private static final String LOG_LEVEL_TEXT_FATAL = "FATAL";
  private static final String LOG_LEVEL_TEXT_INFO = "INFO";
  private static final String LOG_LEVEL_TEXT_OFF = "OFF";
  private static final String LOG_LEVEL_TEXT_WARN = "WARN";

  public static String levelToString(int level) {
    switch (level) {
      case ServerSideLog.LOG_LEVEL_ALL:
        return LOG_LEVEL_TEXT_ALL;
      case ServerSideLog.LOG_LEVEL_DEBUG:
        return LOG_LEVEL_TEXT_DEBUG;
      case ServerSideLog.LOG_LEVEL_INFO:
        return LOG_LEVEL_TEXT_INFO;
      case ServerSideLog.LOG_LEVEL_WARN:
        return LOG_LEVEL_TEXT_WARN;
      case ServerSideLog.LOG_LEVEL_ERROR:
        return LOG_LEVEL_TEXT_ERROR;
      case ServerSideLog.LOG_LEVEL_FATAL:
        return LOG_LEVEL_TEXT_FATAL;
      case ServerSideLog.LOG_LEVEL_OFF:
        return LOG_LEVEL_TEXT_OFF;
      default:
        throw new IllegalArgumentException();
    }
  }
}
