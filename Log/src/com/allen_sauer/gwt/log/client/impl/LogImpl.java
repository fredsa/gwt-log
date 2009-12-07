package com.allen_sauer.gwt.log.client.impl;

import com.allen_sauer.gwt.log.client.LogUtil;

public abstract class LogImpl implements LogImplInterface {
  public final String getCurrentLogLevelString() {
    return LogUtil.levelToString(getCurrentLogLevel());
  }

  public final String getLowestLogLevelString() {
    return LogUtil.levelToString(getLowestLogLevel());
  }
}
