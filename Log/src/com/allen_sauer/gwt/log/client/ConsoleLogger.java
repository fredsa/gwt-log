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
package com.allen_sauer.gwt.log.client;

import com.allen_sauer.gwt.log.shared.LogRecord;

/**
 * Logger which sends output via <code>$wnd.console.log()</code> if <code>$wnd.console.log</code> is
 * a function, unless <code>$wnd.console.firebug</code> is defined.
 */
public final class ConsoleLogger implements Logger {
  // CHECKSTYLE_JAVADOC_OFF

  public void clear() {
  }

  public native boolean isSupported()
  /*-{
    return $wnd.console != null && !$wnd.console.firebug &&
      (typeof($wnd.console.log) == 'function' || typeof($wnd.console.log) == 'object');
  }-*/;

  public void log(LogRecord record) {
    // Haven't figured out how to get line breaks to interpret inside of calls to $wnd.console.log()
    String[] lines = (record.getFormattedMessage()
        + LogUtil.stackTraceToString(record.getThrowable())).split("\n");
    for (int i = 0; i < lines.length; i++) {
      logMessage(lines[i]);
    }
  }

  public void setCurrentLogLevel(int level) {
  }

  private native void logMessage(String message)
  /*-{
    $wnd.console.log(message);
  }-*/;
}
