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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import com.allen_sauer.gwt.log.client.util.DOMUtil;
import com.allen_sauer.gwt.log.client.util.LogUtil;

/**
 * Logger which outputs to a draggable floating <code>DIV</code>.
 */
public class DivLogger extends AbstractLogger {
  // CHECKSTYLE_JAVADOC_OFF

  private static class ScrollPanelImpl extends ScrollPanel {
    private int minScrollPanelHeight = -1;
    private int minScrollPanelWidth = -1;
    private int scrollPanelHeight;
    private int scrollPanelWidth;

    public void checkMinSize() {
      if (minScrollPanelWidth == -1) {
        minScrollPanelWidth = getOffsetWidth();
        minScrollPanelHeight = getOffsetHeight();
      }
    }

    public void incrementPixelSize(int width, int height) {
      setPixelSize(scrollPanelWidth + width, scrollPanelHeight + height);
    }

    @Override
    public void setPixelSize(int width, int height) {
      super.setPixelSize(scrollPanelWidth = Math.max(width, minScrollPanelWidth),
          scrollPanelHeight = Math.max(height, minScrollPanelHeight));
    }
  }

  private static final String CSS_LOG_MESSAGE = "log-message";
  private static final int[] levels = {
      Log.LOG_LEVEL_TRACE, Log.LOG_LEVEL_DEBUG, Log.LOG_LEVEL_INFO, Log.LOG_LEVEL_WARN,
      Log.LOG_LEVEL_ERROR, Log.LOG_LEVEL_FATAL, Log.LOG_LEVEL_OFF,};
  private static final String STACKTRACE_ELEMENT_PREFIX = "&nbsp;&nbsp;&nbsp;&nbsp;at&nbsp;";
  private static final String STYLE_LOG_HEADER = "log-header";
  private static final String STYLE_LOG_PANEL = "log-panel";
  private static final String STYLE_LOG_SCROLL_PANEL = "log-scroll-panel";
  private static final String STYLE_LOG_TEXT_AREA = "log-text-area";

  private static final int UPDATE_INTERVAL_MILLIS = 500;

  private boolean dirty = false;
  private Button[] levelButtons;
  private final DockPanel logDockPanel = new DockPanel() {
    private final WindowResizeListener windowResizeListener = new WindowResizeListener() {
      private int lastDocumentClientHeight = -1;
      private int lastDocumentClientWidth = -1;

      public void onWindowResized(int width, int height) {
        // Workaround for issue 1934
        // IE fires Window onresize events when the size of the body changes
        if (width != lastDocumentClientWidth || height != lastDocumentClientHeight) {
          lastDocumentClientWidth = width;
          lastDocumentClientHeight = height;

          scrollPanel.setPixelSize(Math.max(300, (int) (Window.getClientWidth() * .8)), Math.max(
              100, (int) (Window.getClientHeight() * .3)));
        }
      }
    };

    @Override
    public void setVisible(boolean visible) {
      super.setVisible(visible);
      if (visible) {
        scrollPanel.checkMinSize();
        windowResizeListener.onWindowResized(Window.getClientWidth(), Window.getClientHeight());
      }
    }

    @Override
    protected void onLoad() {
      super.onLoad();
      Window.addWindowResizeListener(windowResizeListener);
    }

    @Override
    protected void onUnload() {
      super.onUnload();
      Window.removeWindowResizeListener(windowResizeListener);
    }
  };
  private String logText = "";
  private final HTML logTextArea = new HTML();

  private final ScrollPanelImpl scrollPanel = new ScrollPanelImpl();

  private final Timer timer;

  /**
   * Default constructor.
   */
  public DivLogger() {
    logDockPanel.addStyleName(STYLE_LOG_PANEL);
    logTextArea.addStyleName(STYLE_LOG_TEXT_AREA);
    scrollPanel.addStyleName(STYLE_LOG_SCROLL_PANEL);

    //    scrollPanel.setAlwaysShowScrollBars(true);

    final FocusPanel headerPanel = makeHeader();

    Widget resizePanel;
    resizePanel = makeResizePanel();

    logDockPanel.add(headerPanel, DockPanel.NORTH);
    logDockPanel.add(scrollPanel, DockPanel.CENTER);
    logDockPanel.add(resizePanel, DockPanel.SOUTH);
    DOM.setStyleAttribute(DOM.getParent(resizePanel.getElement()), "lineHeight", "1px");
    logDockPanel.setCellHorizontalAlignment(resizePanel, HasHorizontalAlignment.ALIGN_RIGHT);

    scrollPanel.setWidget(logTextArea);

    logDockPanel.setVisible(false);
    RootPanel.get().add(logDockPanel, 0, 0);

    timer = new Timer() {
      @Override
      public void run() {
        dirty = false;
        logTextArea.setHTML(logTextArea.getHTML() + logText);
        logText = "";
        DeferredCommand.addCommand(new Command() {
          public void execute() {
            scrollPanel.setScrollPosition(0xB1111111);
          }
        });
      }
    };
  }

  @Override
  public final void clear() {
    logTextArea.setHTML("");
  }

  public final Widget getWidget() {
    return logDockPanel;
  }

  public final boolean isSupported() {
    return true;
  }

  public final boolean isVisible() {
    return logDockPanel.isAttached() && logDockPanel.isVisible();
  }

  public final void moveTo(int x, int y) {
    RootPanel.get().add(logDockPanel, x, y);
  }

  @Override
  public void setCurrentLogLevel(int level) {
    super.setCurrentLogLevel(level);
    for (int i = 0; i < levels.length; i++) {
      if (levels[i] < Log.getLowestLogLevel()) {
        levelButtons[i].setEnabled(false);
      } else {
        String levelText = LogUtil.levelToString(levels[i]);
        boolean current = level == levels[i];
        levelButtons[i].setTitle(current ? "Current (runtime) log level is already '" + levelText
            + "'" : "Set current (runtime) log level to '" + levelText + "'");
        boolean active = level <= levels[i];
        DOM.setStyleAttribute(levelButtons[i].getElement(), "color", active ? getColor(levels[i])
            : "#ccc");
      }
    }
  }

  public final void setPixelSize(int width, int height) {
    logTextArea.setPixelSize(width, height);
  }

  public final void setSize(String width, String height) {
    logTextArea.setSize(width, height);
  }

  @Override
  final void log(int logLevel, String message) {
    assert false;
    // Method never called since {@link #log(int, String, Throwable)} is overridden
  }

  @Override
  final void log(int logLevel, String message, Throwable throwable) {
    logDockPanel.setVisible(true);
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

  private void addLogText(String logTest) {
    logText += logTest;
    if (!dirty) {
      dirty = true;
      timer.schedule(UPDATE_INTERVAL_MILLIS);
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
    if (logLevel >= Log.LOG_LEVEL_DEBUG) {
      return "#20b000"; // green
    }
    return "#F0F"; // purple
  }

  /**
   * @deprecated
   */
  @Deprecated
  private FocusPanel makeHeader() {
    FocusPanel header;
    header = new FocusPanel();
    HorizontalPanel masterPanel = new HorizontalPanel();
    masterPanel.setWidth("100%");
    header.add(masterPanel);
    header.addStyleName(STYLE_LOG_HEADER);

    final Label titleLabel = new Label("gwt-log", false);
    titleLabel.setStylePrimaryName("log-title");

    HorizontalPanel buttonPanel = new HorizontalPanel();
    levelButtons = new Button[levels.length];
    for (int i = 0; i < levels.length; i++) {
      final int level = levels[i];
      levelButtons[i] = new Button(LogUtil.levelToString(level));
      buttonPanel.add(levelButtons[i]);
      levelButtons[i].addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          ((Button) sender).setFocus(false);
          Log.setCurrentLogLevel(level);
        }
      });
    }

    Button clearButton = new Button("Clear");
    clearButton.addStyleName("log-clear-button");
    DOM.setStyleAttribute(clearButton.getElement(), "color", "#00c");
    clearButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        ((Button) sender).setFocus(false);
        Log.clear();
      }
    });
    buttonPanel.add(clearButton);

    Button aboutButton = new Button("About");
    aboutButton.addStyleName("log-clear-about");
    aboutButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        ((Button) sender).setFocus(false);

        // TODO Add GWT.getVersion() after 1.5
        Log.diagnostic("\n" //
            + "gwt-log-" + Log.getVersion() //
            + " - Runtime logging for your Google Web Toolkit projects\n" + //
            "Copyright 2007-2008 Fred Sauer\n" + //
            "The original software is available from:\n" + //
            "\u00a0\u00a0\u00a0\u00a0http://allen-sauer.com/gwt/\n", null);
      }
    });

    masterPanel.add(titleLabel);
    masterPanel.add(buttonPanel);
    masterPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    masterPanel.add(aboutButton);

    masterPanel.setCellHeight(titleLabel, "100%");
    masterPanel.setCellWidth(titleLabel, "50%");
    masterPanel.setCellWidth(aboutButton, "50%");

    titleLabel.addMouseListener(new MouseListenerAdapter() {
      private boolean dragging = false;
      private int dragStartX;
      private int dragStartY;

      @Override
      public void onMouseDown(Widget sender, int x, int y) {
        dragging = true;
        DOM.setCapture(titleLabel.getElement());
        dragStartX = x;
        dragStartY = y;
      }

      @Override
      public void onMouseMove(Widget sender, int x, int y) {
        if (dragging) {
          int absX = x + logDockPanel.getAbsoluteLeft();
          int absY = y + logDockPanel.getAbsoluteTop();
          RootPanel.get().setWidgetPosition(logDockPanel, absX - dragStartX, absY - dragStartY);
        }
      }

      @Override
      public void onMouseUp(Widget sender, int x, int y) {
        dragging = false;
        DOM.releaseCapture(titleLabel.getElement());
      }
    });

    return header;
  }

  private Widget makeResizePanel() {
    final Image handle = new Image(GWT.getModuleBaseURL() + "gwt-log-triangle-10x10.png");
    handle.addStyleName("log-resize-se");
    handle.addMouseListener(new MouseListenerAdapter() {
      private boolean dragging = false;
      private int dragStartX;
      private int dragStartY;

      @Override
      public void onMouseDown(Widget sender, int x, int y) {
        dragging = true;
        DOM.setCapture(handle.getElement());
        dragStartX = x;
        dragStartY = y;
        DOM.eventPreventDefault(DOM.eventGetCurrentEvent());
      }

      @Override
      public void onMouseMove(Widget sender, int x, int y) {
        if (dragging) {
          scrollPanel.incrementPixelSize(x - dragStartX, y - dragStartY);
          scrollPanel.setScrollPosition(Integer.MAX_VALUE);
        }
      }

      @Override
      public void onMouseUp(Widget sender, int x, int y) {
        dragging = false;
        DOM.releaseCapture(handle.getElement());
      }
    });
    return handle;
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
}