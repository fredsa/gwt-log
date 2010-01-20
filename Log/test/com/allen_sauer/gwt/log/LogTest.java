package com.allen_sauer.gwt.log;

import com.allen_sauer.gwt.log.client.Log;

public class LogTest {
  protected static void testApi() {
    Log.isTraceEnabled();
    Log.isDebugEnabled();
    Log.isInfoEnabled();
    Log.isWarnEnabled();
    Log.isErrorEnabled();
    Log.isFatalEnabled();

    Log.trace("trace");
    Log.trace("trace", new NullPointerException());

    Log.debug("debug");
    Log.debug("debug", new NullPointerException());

    Log.info("info");
    Log.info("info", new NullPointerException());

    Log.warn("warn");
    Log.warn("warn", new NullPointerException());

    Log.error("error");
    Log.error("error", new NullPointerException());

    Log.fatal("fatal");
    Log.fatal("fatal", new NullPointerException());
  }
}
