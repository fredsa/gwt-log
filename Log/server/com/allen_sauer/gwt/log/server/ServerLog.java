package com.allen_sauer.gwt.log.server;

import com.allen_sauer.gwt.log.client.LogRecord;

public interface ServerLog {

  void log(LogRecord record);
}
