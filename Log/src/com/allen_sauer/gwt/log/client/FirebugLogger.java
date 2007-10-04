package com.allen_sauer.gwt.log.client;

public class FirebugLogger extends AbstractLogger {
  public native boolean isSupported()
  /*-{
    return !!($wnd.console && $wnd.console.firebug);
  }-*/;

  public native void log(int logLevel, String message)
  /*-{
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
