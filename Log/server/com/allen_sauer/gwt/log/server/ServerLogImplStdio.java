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

import com.allen_sauer.gwt.log.client.LogRecord;

// CHECKSTYLE_JAVADOC_OFF
public final class ServerLogImplStdio implements ServerLog {

  public void log(LogRecord record) {
    System.err.println(record.getMessage());
    Throwable e = record.getThrowable();
    if (e != null) {
      e.printStackTrace();
    }
  }

  // private int level;
  //
  // @Override
  // public void clear() {
  // }
  //
  // @Override
  // public void debug(String message, Throwable e) {
  // logToSystemErr(message, e);
  // }
  //
  // @Override
  // public void diagnostic(String message, Throwable e) {
  // logToSystemErr(message, e);
  // }
  //
  // @Override
  // public void error(String message, Throwable e) {
  // logToSystemErr(message, e);
  // }
  //
  // @Override
  // public void fatal(String message, Throwable e) {
  // logToSystemErr(message, e);
  // }
  //
  // @Override
  // public int getCurrentLogLevel() {
  // return level;
  // }
  //
  // @Override
  // public void info(String message, Throwable e) {
  // logToSystemErr(message, e);
  // }
  //
  // @Override
  // public boolean isDebugEnabled() {
  // return level >= Log.LOG_LEVEL_DEBUG;
  // }
  //
  // @Override
  // public boolean isErrorEnabled() {
  // return level >= Log.LOG_LEVEL_ERROR;
  // }
  //
  // @Override
  // public boolean isFatalEnabled() {
  // return level >= Log.LOG_LEVEL_FATAL;
  // }
  //
  // @Override
  // public boolean isInfoEnabled() {
  // return level >= Log.LOG_LEVEL_INFO;
  // }
  //
  // @Override
  // public boolean isLoggingEnabled() {
  // return level >= Log.LOG_LEVEL_OFF;
  // }
  //
  // @Override
  // public boolean isTraceEnabled() {
  // return level >= Log.LOG_LEVEL_TRACE;
  // }
  //
  // @Override
  // public boolean isWarnEnabled() {
  // return level >= Log.LOG_LEVEL_WARN;
  // }
  //
  // @Override
  // public int mapGWTLogLevelToImplLevel(int gwtLogLevel) {
  // return gwtLogLevel;
  // }
  //
  // @Override
  // public void setCurrentImplLogLevel(int level) {
  // this.level = level;
  // }
  //
  // @Override
  // public void trace(String message, Throwable e) {
  // logToSystemErr(message, e);
  // }
  //
  // @Override
  // public void warn(String message, Throwable e) {
  // logToSystemErr(message, e);
  // }
  //
  // @Override
  // protected int mapImplLevelToGWTLogLevel(int implLogLevel) {
  // return implLogLevel;
  // }
  //
  // private void logToSystemErr(String message, Throwable e) {
  // System.err.println(message);
  // if (e != null) {
  // e.printStackTrace();
  // }
  // }
}
