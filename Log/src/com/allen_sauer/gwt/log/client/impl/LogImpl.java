package com.allen_sauer.gwt.log.client.impl;

import com.allen_sauer.gwt.log.client.LogUtil;

/**
 * Abstract base class implementing {@link LogImplInterface}.
 */
public abstract class LogImpl implements LogImplInterface {
  @Override
  public final String getCurrentLogLevelString() {
    return LogUtil.levelToString(getCurrentLogLevel());
  }

  @Override
  public final String getLowestLogLevelString() {
    return LogUtil.levelToString(getLowestLogLevel());
  }
}
