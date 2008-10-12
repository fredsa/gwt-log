/**
 * Copyright 2008 Fred Sauer.
 */
package com.allen_sauer.gwt.log.client;

/**
 * Interface for deferred binding generator.
 */
public interface LogMessageFormatter {
  /**
   * Format a log message.
   * 
   * @param logLevelText String representing the level at which this message was logged
   * @param message the applications message to log
   * @return a formatted message using the 'log_pattern' pattern
   */
  String format(String logLevelText, String message);
}
