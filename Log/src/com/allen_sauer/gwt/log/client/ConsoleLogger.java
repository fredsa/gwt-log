package com.allen_sauer.gwt.log.client;

public class ConsoleLogger extends AbstractLogger {
  public native boolean isSupported()
  /*-{
    return $wnd.console != null && typeof($wnd.console.log) == 'function';
  }-*/;

  public native void log(int logLevel, String message)
  /*-{
    $wnd.console.log(message);
  }-*/;
}
