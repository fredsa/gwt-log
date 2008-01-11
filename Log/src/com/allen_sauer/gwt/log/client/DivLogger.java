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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class DivLogger extends AbstractLogger {
  private static final String STYLE_LOG_HEADER = "log-header";
  private static final String STYLE_LOG_PANEL = "log-panel";
  private static final String STYLE_LOG_SCROLL_PANEL = "log-scroll-panel";
  private static final String STYLE_LOG_TEXT_AREA = "log-text-area";
  private static final int UPDATE_INTERVAL_MILLIS = 500;

  private FlexTable debugTable = new FlexTable();
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
        logTextArea.setHTML(logText);
        DeferredCommand.addCommand(new Command() {
          public void execute() {
            scrollPanel.setScrollPosition(Integer.MAX_VALUE);
          }
        });
      }
    };
  }

  public void clear() {
    setLogText("");
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
    message = message.replaceAll("\r\n|\r|\n", "<BR>");
    setLogText(logText + "<div style='color: " + getColor(logLevel) + ";'>" + message + "</div>");
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

  private void setLogText(String debugText) {
    logText = debugText;
    if (!dirty) {
      dirty = true;
      timer.schedule(UPDATE_INTERVAL_MILLIS);
    }
  }
}