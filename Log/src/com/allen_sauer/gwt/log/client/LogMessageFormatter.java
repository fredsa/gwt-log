/*
 * Copyright 2009 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
