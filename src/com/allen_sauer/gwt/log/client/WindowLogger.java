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
package com.allen_sauer.gwt.log.client;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;

import com.allen_sauer.gwt.log.client.util.DOMUtil;

/**
 * Logger which outputs to a draggable floating <code>DIV</code>.
 * 
 * TODO Fix lack of upates when Firefox is configured to force new windows to open as tabs
 */
public class WindowLogger extends AbstractLogger {
  // CHECKSTYLE_JAVADOC_OFF

  private static final String CSS_LOG_MESSAGE = "log-message";
  private static final String STACKTRACE_ELEMENT_PREFIX = "&nbsp;&nbsp;&nbsp;&nbsp;at&nbsp;";

  private String logText = "";
  private boolean ready = false;
  private JavaScriptObject window = null;

  private final WindowCloseListener windowCloseListener = new WindowCloseListener() {
    public void onWindowClosed() {
      closeWindowIfOpen();
    }

    public String onWindowClosing() {
      return null;
    }
  };

  /**
   * Default constructor.
   */
  public WindowLogger() {
    Window.addWindowCloseListener(windowCloseListener);
  }

  @Override
  public final void clear() {
    if (ready) {
      try {
        DOMUtil.windowClear(window);
      } catch (RuntimeException e) {
        // ignore
      }
    }
  }

  public final boolean isSupported() {
    return true;
  }

  private void addLogText(String text) {
    logText += text;
    if (window == null) {
      openNewWindow();
    }
    logPendingText();
  }

  private void closeWindowIfOpen() {
    if (window != null) {
      ready = false;
      DOMUtil.windowClose(window);
      window = null;
    }
  }

  private String getColor(int logLevel) {
    if (logLevel == Log.LOG_LEVEL_OFF) {
      return "#000"; // black
    }
    if (logLevel >= Log.LOG_LEVEL_FATAL) {
      return "#F00"; // bright red
    }
    if (logLevel >= Log.LOG_LEVEL_ERROR) {
      return "#C11B17"; // dark red
    }
    if (logLevel >= Log.LOG_LEVEL_WARN) {
      return "#E56717"; // dark orange
    }
    if (logLevel >= Log.LOG_LEVEL_INFO) {
      return "#2B60DE"; // blue
    }
    return "#20b000"; // green
  }

  private void logPendingText() {
    if (ready) {
      try {
        DOMUtil.windowWrapAndAppendHTML(window, logText);
        logText = "";
      } catch (JavaScriptException e) {
        openNewWindow();
      }
    }
  }

  private String makeTitle(String message, Throwable throwable) {
    if (throwable != null) {
      if (throwable.getMessage() == null) {
        message = throwable.getClass().getName();
      } else {
        message = throwable.getMessage().replaceAll(
            throwable.getClass().getName().replaceAll("^(.+\\.).+$", "$1"), "");
      }
    }
    return DOMUtil.adjustTitleLineBreaks(message).replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(
        "'", "\"");
  }

  private void openNewWindow() {
    closeWindowIfOpen();
    window = DOMUtil.windowOpen("");
    new Timer() {
      int counter = 0;

      @Override
      public void run() {
        try {
          if (counter++ > 100 || "complete".equals(DOMUtil.windowReadyState(window))) {
            DOMUtil.windowSetTitle(window, "[log] " + Window.getTitle());
            ready = true;
            cancel();
            logPendingText();
          }
        } catch (RuntimeException e) {
          window = null;
          cancel();
        }
      }
    }.scheduleRepeating(100);
  }

  @Override
  final void log(int logLevel, String message) {
    assert false;
    // Method never called since {@link #log(int, String, Throwable)} is overridden
  }

  @Override
  final void log(int logLevel, String message, Throwable throwable) {
    String text = message.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    String title = makeTitle(message, throwable);
    if (throwable != null) {
      text += "\n";
      while (throwable != null) {
        text += throwable.getClass().getName() + ":<br><b>" + throwable.getMessage() + "</b>";
        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        if (stackTraceElements.length > 0) {
          text += "<div class='log-stacktrace'>";
          for (StackTraceElement element : stackTraceElements) {
            text += STACKTRACE_ELEMENT_PREFIX + element + "<br>";
          }
          text += "</div>";
        }
        throwable = throwable.getCause();
        if (throwable != null) {
          text += "Caused by: ";
        }
      }
    }
    text = text.replaceAll("\r\n|\r|\n", "<BR>");
    addLogText("<div class='" + CSS_LOG_MESSAGE
        + "' onmouseover='className+=\" log-message-hover\"' "
        + "onmouseout='className=className.replace(/ log-message-hover/g,\"\")' style='color: "
        + getColor(logLevel) + "' title='" + title + "'>" + text + "</div>");
  }
}
