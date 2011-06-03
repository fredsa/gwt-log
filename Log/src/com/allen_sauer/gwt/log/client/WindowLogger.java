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

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

import com.allen_sauer.gwt.log.client.impl.LogClientBundle;
import com.allen_sauer.gwt.log.client.util.DOMUtil;
import com.allen_sauer.gwt.log.shared.LogRecord;

/**
 * Logger which outputs to a draggable floating <code>DIV</code>.
 * 
 * TODO Fix lack of updates when Firefox is configured to force new windows to open as tabs
 */
public class WindowLogger implements Logger {
  // CHECKSTYLE_JAVADOC_OFF

  private static final String STACKTRACE_ELEMENT_PREFIX = "&nbsp;&nbsp;&nbsp;&nbsp;at&nbsp;";

  private String logText = "";
  private boolean ready = false;
  private JavaScriptObject window = null;

  private final CloseHandler<Window> windowCloseListener = new CloseHandler<Window>() {
    @Override
    public void onClose(CloseEvent<Window> event) {
      closeWindowIfOpen();
    }
  };

  /**
   * Default constructor.
   */
  public WindowLogger() {
    Window.addCloseHandler(windowCloseListener);
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

  @Override
  public final boolean isSupported() {
    return true;
  }

  @Override
  public void log(LogRecord record) {
    String message = record.getFormattedMessage();
    Throwable throwable = record.getThrowable();
    String text = message.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    String title = makeTitle(message, throwable);
    if (throwable != null) {
      text += "\n";
      while (throwable != null) {
        /* Use throwable.toString() and not throwable.getClass().getName() and
         * throwable.getMessage(), so that instances of UnwrappedClientThrowable, when stack trace
         * deobfuscation is enabled) display properly
         */
        text += "<b>" + throwable.toString() + "</b>";
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
    addLogText(
        "<div class='" + LogClientBundle.INSTANCE.css().logMessage()
            + "' onmouseover='className+=\" log-message-hover\"' "
            + "onmouseout='className=className.replace(/ log-message-hover/g,\"\")' style='color: "
            + getColor(record.getLevel()) + "' title='" + title + "'>" + text + "</div>");
  }

  @Override
  public void setCurrentLogLevel(int level) {
    // do no
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
    return DOMUtil.adjustTitleLineBreaks(message).replaceAll("<", "&lt;").replaceAll(
        ">", "&gt;").replaceAll("'", "\"");
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
}
