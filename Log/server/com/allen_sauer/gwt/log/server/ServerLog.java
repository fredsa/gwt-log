package com.allen_sauer.gwt.log.server;

import com.allen_sauer.gwt.log.shared.LogRecord;

public interface ServerLog {

  int getCurrentLogLevel();

  boolean isDebugEnabled();

  boolean isErrorEnabled();

  boolean isFatalEnabled();

  boolean isInfoEnabled();

  boolean isLoggingEnabled();

  boolean isTraceEnabled();

  boolean isWarnEnabled();

  void log(LogRecord record);

  int mapGWTLogLevelToImplLevel(int level);

  void setCurrentImplLogLevel(int level);
}
