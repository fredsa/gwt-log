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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import com.allen_sauer.gwt.log.client.util.DOMUtil;

public class DivLogger extends AbstractLogger {
  private static final String STACKTRACE_ELEMENT_PREFIX = "&nbsp;&nbsp;&nbsp;&nbsp;at&nbsp;";
  private static final String STYLE_LOG_HEADER = "log-header";
  private static final String STYLE_LOG_PANEL = "log-panel";
  private static final String STYLE_LOG_SCROLL_PANEL = "log-scroll-panel";
  private static final String STYLE_LOG_TEXT_AREA = "log-text-area";
  private static final int UPDATE_INTERVAL_MILLIS = 500;

  private FlexTable debugTable = new FlexTable() {
    private WindowResizeListener windowResizeListener = new WindowResizeListener() {
      public void onWindowResized(int width, int height) {
        scrollPanel.setPixelSize(Math.max(300, (int) (Window.getClientWidth() * .8)), Math.max(100,
            (int) (Window.getClientHeight() * .2)));
      }
    };

    protected void onLoad() {
      super.onLoad();
      windowResizeListener.onWindowResized(Window.getClientWidth(), Window.getClientHeight());
      Window.addWindowResizeListener(windowResizeListener);
    }

    protected void onUnload() {
      super.onUnload();
      Window.removeWindowResizeListener(windowResizeListener);
    }
  };

  private boolean dirty = false;
  private String logText = "";
  private HTML logTextArea = new HTML();
  private ScrollPanel scrollPanel = new ScrollPanel();
  private Timer timer;

  public DivLogger() {
    debugTable.addStyleName(STYLE_LOG_PANEL);
    logTextArea.addStyleName(STYLE_LOG_TEXT_AREA);
    scrollPanel.addStyleName(STYLE_LOG_SCROLL_PANEL);

    final Label header = new Label("LOG PANEL");
    header.addStyleName(STYLE_LOG_HEADER);
    debugTable.setWidget(0, 0, header);
    debugTable.setWidget(1, 0, scrollPanel);
    debugTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

    scrollPanel.setWidget(logTextArea);

    header.addMouseListener(new MouseListenerAdapter() {
      private boolean dragging = false;
      private int dragStartX;
      private int dragStartY;

      public void onMouseDown(Widget sender, int x, int y) {
        dragging = true;
        DOM.setCapture(header.getElement());
        dragStartX = x;
        dragStartY = y;
      }

      public void onMouseMove(Widget sender, int x, int y) {
        if (dragging) {
          int absX = x + debugTable.getAbsoluteLeft();
          int absY = y + debugTable.getAbsoluteTop();
          RootPanel.get().setWidgetPosition(debugTable, absX - dragStartX, absY - dragStartY);
        }
      }

      public void onMouseUp(Widget sender, int x, int y) {
        dragging = false;
        DOM.releaseCapture(header.getElement());
      }
    });

    debugTable.setVisible(false);
    RootPanel.get().add(debugTable, 0, 0);

    timer = new Timer() {
      public void run() {
        dirty = false;
        logTextArea.setHTML(logTextArea.getHTML() + logText);
        logText = "";
        DeferredCommand.addCommand(new Command() {
          public void execute() {
            scrollPanel.setScrollPosition(Integer.MAX_VALUE);
          }
        });
      }
    };
  }

  public void clear() {
    logTextArea.setHTML("");
  }

  public Widget getWidget() {
    return debugTable;
  }

  public boolean isSupported() {
    return true;
  }

  public boolean isVisible() {
    return debugTable.isAttached() && debugTable.isVisible();
  }

  public void log(int logLevel, String message) {
    message = message.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
    message = message.replaceAll(" ", "&nbsp;");
    message = message.replaceAll("<", "&lt;");
    message = message.replaceAll(">", "&gt;");
    log(logLevel, message);
  }

  public void log(int logLevel, String message, Throwable throwable) {
    String text = message;
    String title = makeTitle(message, throwable);
    if (throwable != null) {
      text += "\n";
      while (throwable != null) {
        text += GWT.getTypeName(throwable) + ":<br><b>" + throwable.getMessage() + "</b>";
        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        if (stackTraceElements.length > 0) {
          text += "<div class='log-stacktrace'>";
          for (int i = 0; i < stackTraceElements.length; i++) {
            text += STACKTRACE_ELEMENT_PREFIX + stackTraceElements[i] + "<br>";
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
    addLogText("<div class='log-message' onmouseover='className+=\" log-message-hover\"' "
        + "onmouseout='className=className.replace(/ log-message-hover/g,\"\")' style='color: "
        + getColor(logLevel) + "' title='" + title + "'>" + text + "</div>");
    debugTable.setVisible(true);
  }

  public void moveTo(int x, int y) {
    RootPanel.get().add(debugTable, x, y);
  }

  public void setPixelSize(int width, int height) {
    logTextArea.setPixelSize(width, height);
  }

  public void setSize(String width, String height) {
    logTextArea.setSize(width, height);
  }

  private void addLogText(String debugText) {
    logText += debugText;
    if (!dirty) {
      dirty = true;
      timer.schedule(UPDATE_INTERVAL_MILLIS);
    }
  }

  private String getColor(int logLevel) {
    if (logLevel >= Log.LOG_LEVEL_FATAL) {
      return "#FF0000"; // bright red
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
    return "green";
  }

  private String makeTitle(String message, Throwable throwable) {
    if (throwable != null) {
      message = throwable.getMessage().replaceAll(
          GWT.getTypeName(throwable).replaceAll("^(.+\\.).+$", "$1"), "");
    }
    return DOMUtil.adjustTitleLineBreaks(message).replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(
        "'", "\"");
  }
}