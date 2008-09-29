/*
 * Copyright 2008 Fred Sauer
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

//CHECKSTYLE_JAVADOC_OFF
public abstract class ServerLogImpl {
  public abstract void clear();

  public abstract void debug(String message, Throwable e);

  public abstract void diagnostic(String message, Throwable e);

  public abstract void error(String message, Throwable e);

  public abstract void fatal(String message, Throwable e);

  public abstract int getCurrentLogLevel();

  public abstract void info(String message, Throwable e);

  // public abstract int getLowestLogLevel();

  public abstract boolean isDebugEnabled();

  public abstract boolean isErrorEnabled();

  public abstract boolean isFatalEnabled();

  public abstract boolean isInfoEnabled();

  public abstract boolean isLoggingEnabled();

  public abstract boolean isTraceEnabled();

  public abstract boolean isWarnEnabled();

  /**
   * Map gwt-log int level to implementation int level.
   * 
   * @param gwtLogLevel the gwt-log int log level
   * @return the implementation specific int log level
   */
  public abstract int mapGWTLogLevelToImplLevel(int gwtLogLevel);

  /**
   * Set the current implementation log level.
   * 
   * @param implLogLevel the implementation specific int log level
   */
  public abstract void setCurrentImplLogLevel(int implLogLevel);

  public abstract void trace(String message, Throwable e);

  public abstract void warn(String message, Throwable e);

  /**
   * Map implementation int level to gwt-log int level.
   * 
   * @param implLogLevel the implementation specific int log level
   * @return the gwt-log int log level
   */
  protected abstract int mapImplLevelToGWTLogLevel(int implLogLevel);
}
