/**
 * Copyright 2008 Fred Sauer.
 */
package com.allen_sauer.gwt.log.client;

import com.google.gwt.user.client.ui.Widget;

/**
 * @deprecated Use the {@link LoggerDIV} class instead.
 */
public abstract class DivLogger extends AbstractLogger {

  public abstract Widget getWidget();

  public abstract boolean isVisible();

  public abstract void moveTo(int x, int y);

  public abstract void setPixelSize(int width, int height);

  public abstract void setSize(String width, String height);
}
