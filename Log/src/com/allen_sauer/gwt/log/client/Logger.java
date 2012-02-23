/*
 * Copyright 2009 Fred Sauer
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

import com.allen_sauer.gwt.log.shared.LogRecord;

/**
 * Interface implemented by all loggers.
 */
public interface Logger {
  /**
   * Clear the output, if possible.
   */
  void clear();

  /**
   * Determine whether this logger is supported in the current configuration and environment.
   * 
   * @return true if this logger is supported
   */
  boolean isSupported();

  /**
   * Log an event.
   * 
   * @param record the event to log
   */
  void log(LogRecord record);

  /**
   * Set the current runtime log level. Setting the runtime log level to a lower level than the
   * current compile time log level will not result in any additional information being logged.
   * 
   * @param level new runtime log level.
   */
  void setCurrentLogLevel(int level);

}