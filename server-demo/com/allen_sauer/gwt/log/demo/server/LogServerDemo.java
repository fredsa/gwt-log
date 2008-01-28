/**
 * Copyright 2008 Fred Sauer.
 */
package com.allen_sauer.gwt.log.demo.server;

import com.allen_sauer.gwt.log.client.ServerSideLog;

public class LogServerDemo {
  public static void main(String[] args) {
    ServerSideLog.debug("Debug test message");
    ServerSideLog.info("Informational test message");
    ServerSideLog.warn("Warning test message");
    ServerSideLog.error("Error test message");
    ServerSideLog.fatal("Fatal test message");
  }
}
