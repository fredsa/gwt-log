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
package com.allen_sauer.gwt.log.client;

import com.allen_sauer.gwt.log.shared.LogRecord;

/**
 * Logger which sends output via <code>window.console</code>. This logger is compatible with
 * <a href="http://www.getfirebug.com/">Firebug</a>, <a
 * href="http://www.getfirebug.com/lite.html">Firebug Lite</a>, Chrome Developer Tools, Safari Web
 * Inspector, IE Developer Toolbar and any others. Messages are logged via
 * <code>window.console.debug()</code>, <code>window.console.info()</code>,
 * <code>window.console.warn()</code>, <code>window.console.error()</code> and, as a fallback,
 * <code>window.console.log()</code>.
 */
public final class ConsoleLogger implements Logger {
  // CHECKSTYLE_JAVADOC_OFF

  public void clear() {
  }

  public native boolean isSupported() /*-{
    // Consciously using 'window' rather than '$wnd'
    // See http://code.google.com/p/fbug/issues/detail?id=2914
    if (window.console == null
        || (typeof (window.console.log) != 'function' && typeof (window.console.log) != 'object')) {
      return false;
    }
    if (typeof window.console.error == "undefined") {
      window.console.error = window.console.log;
    }
    if (typeof window.console.warn == "undefined") {
      window.console.warn = window.console.log;
    }
    if (typeof window.console.info == "undefined") {
      window.console.info = window.console.log;
    }
    if (typeof window.console.debug == "undefined") {
      window.console.debug = window.console.log;
    }
    return true;
  }-*/;

  public void log(LogRecord record) {
    logMessage(record.getLevel(),
        record.getFormattedMessage() + LogUtil.stackTraceToString(record.getThrowable()));
  }

  public void setCurrentLogLevel(int level) {
  }

  private native void logMessage(int logLevel, String message) /*-{
    // Consciously using 'window' rather than '$wnd'
    // See http://code.google.com/p/fbug/issues/detail?id=2914
    if (logLevel >= @com.allen_sauer.gwt.log.client.Log::LOG_LEVEL_ERROR) {
      window.console.error(message);
    } else if (logLevel >= @com.allen_sauer.gwt.log.client.Log::LOG_LEVEL_WARN) {
      window.console.warn(message);
    } else if (logLevel >= @com.allen_sauer.gwt.log.client.Log::LOG_LEVEL_INFO) {
      window.console.info(message);
    } else {
      window.console.debug(message);
    }
  }-*/;
}
