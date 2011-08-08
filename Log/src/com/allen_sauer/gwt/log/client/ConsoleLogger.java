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
 * Logger which sends output via <code>$wnd.console</code>. This logger is compatible with <a
 * href="http://www.getfirebug.com/">Firebug</a>, <a
 * href="http://www.getfirebug.com/lite.html">Firebug Lite</a>, Chrome Developer Tools, Safari Web
 * Inspector, IE Developer Toolbar and any others. Messages are logged via
 * <code>$wnd.console.debug()</code>, <code>$wnd.console.info()</code>,
 * <code>$wnd.console.warn()</code>, <code>$wnd.console.error()</code> and, as a fallback,
 * <code>$wnd.console.log()</code>.
 */
public final class ConsoleLogger implements Logger {
  // CHECKSTYLE_JAVADOC_OFF

  @Override
  public void clear() {
  }

  @Override
  public native boolean isSupported() /*-{
    if ($wnd.console == null
        || (typeof ($wnd.console.log) != 'function' && typeof ($wnd.console.log) != 'object')) {
      return false;
    }
    if (typeof $wnd.console.error == "undefined") {
      $wnd.console.error = $wnd.console.log;
    }
    if (typeof $wnd.console.warn == "undefined") {
      $wnd.console.warn = $wnd.console.log;
    }
    if (typeof $wnd.console.info == "undefined") {
      $wnd.console.info = $wnd.console.log;
    }
    if (typeof $wnd.console.debug == "undefined") {
      $wnd.console.debug = $wnd.console.log;
    }
    return true;
  }-*/;

  @Override
  public void log(LogRecord record) {
    logMessage(record.getLevel(),
        record.getFormattedMessage() + LogUtil.stackTraceToString(record.getThrowable()));
  }

  @Override
  public void setCurrentLogLevel(int level) {
  }

  private native void logMessage(int logLevel, String message) /*-{
    if (logLevel >= @com.allen_sauer.gwt.log.client.Log::LOG_LEVEL_ERROR) {
      $wnd.console.error(message);
    } else if (logLevel >= @com.allen_sauer.gwt.log.client.Log::LOG_LEVEL_WARN) {
      $wnd.console.warn(message);
    } else if (logLevel >= @com.allen_sauer.gwt.log.client.Log::LOG_LEVEL_INFO) {
      $wnd.console.info(message);
    } else {
      $wnd.console.debug(message);
    }
  }-*/;
}
