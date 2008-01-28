/**
 * Copyright 2008 Fred Sauer.
 */
package com.allen_sauer.gwt.log.server;

public abstract class ServerLogImpl {
  public abstract void clear();

  public abstract void debug(String message, Throwable e);

  public abstract void error(String message, Throwable e);

  public abstract void fatal(String message, Throwable e);

  public abstract int getCurrentLogLevel();

  //  public abstract int getLowestLogLevel();

  public abstract void info(String message, Throwable e);

  public abstract boolean isDebugEnabled();

  public abstract boolean isErrorEnabled();

  public abstract boolean isFatalEnabled();

  public abstract boolean isInfoEnabled();

  public abstract boolean isLoggingEnabled();

  public abstract boolean isWarnEnabled();

  public abstract int mapGWTLogLevelToImplLevel(int gwtLogLevel);

  public abstract int mapImplLevelToGWTLogLevel(int implLogLevel);

  public abstract void setCurrentLogLevel(int level);

  public abstract void warn(String message, Throwable e);
}
