/*
 * Copyright 2010 Fred Sauer
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
package com.allen_sauer.gwt.log.shared;

import com.google.gwt.core.client.GWT;

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.LogMessageFormatter;
import com.allen_sauer.gwt.log.client.LogUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Wrapper class to capture a single log message and optional stack trace, used
 * primarily for transfer between client and server.
 */
@SuppressWarnings("serial")
public class LogRecord implements Serializable {

  static final LogMessageFormatter FORMATTER = (LogMessageFormatter) (GWT.isClient() ? GWT.create(
      LogMessageFormatter.class) : null);
  private static int gloablRecordSequence;
  private String category;
  private int level;
  private HashMap<String, String> map;
  private String message;
  private int recordSequence;
  private transient Throwable throwable;
  private WrappedClientThrowable wrappedClientThrowable;

  /**
   * Constructor.
   * 
   * @param category The category in which this message should be logged
   * @param level the level at which this message should be logged
   * @param message the message to be logged
   * @param throwable the stack trace associated with this message or null
   */
  public LogRecord(String category, int level, String message, Throwable throwable) {
    this.category = category;
    this.throwable = throwable;
    recordSequence = ++gloablRecordSequence;
    this.level = level;
    this.message = message;
    wrappedClientThrowable = WrappedClientThrowable.getInstanceOrNull(throwable);
  }

  // For GWT serialization
  @SuppressWarnings("unused")
  private LogRecord() {
  }

  /**
   * Retrieve a formatted message for this log record.
   * 
   * @return the formatted message
   */
  public String getFormattedMessage() {
    assert GWT.isClient() : "Method should only be called in Client Code";
    return level == Log.LOG_LEVEL_OFF ? message : FORMATTER.format(
        LogUtil.levelToString(level), category, message);
  }

  /**
   * Retrieve the log level for this log record.
   * 
   * @return the log level
   */
  public int getLevel() {
    return level;
  }

  /**
   * Retrieve the Set of key/value pairs for this log record, used for logging arbitrary data.
   * 
   * @return the Set of key/value pairs
   */
  public Set<Entry<String, String>> getMapEntrySet() {
    return getHashMap().entrySet();
  }

  /**
   * Retrieve this raw log record.
   * 
   * @return the raw log message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Retrieve the global client-side or server-side sequence number for this log record.
   * 
   * @return the global client-side or server-side sequence number
   */
  public int getRecordSequence() {
    return recordSequence;
  }

  /**
   * Retrieves either the original (server side) throwable or a reconstituted client-side throwable.
   * 
   * @return the original or reconstituted throwable
   */
  public Throwable getThrowable() {
    return throwable != null ? throwable : UnwrappedClientThrowable.getInstanceOrNull(
        wrappedClientThrowable);
  }

  /**
   * Get the wrapped client throwable, suitable for serialization with RPC serialization code
   * penalty.
   * 
   * @return the wrapped client throwable
   */
  public WrappedClientThrowable getModifiableWrappedClientThrowable() {
    return wrappedClientThrowable;
  }

  /**
   * Set a key/value pair associated with this log record.
   * 
   * @param key the unique key under which to store the supplied value
   * @param value the value to be stored under the provided key
   */
  public void set(String key, String value) {
    getHashMap().put(key, value);
  }

  private HashMap<String, String> getHashMap() {
    if (map == null) {
      map = new HashMap<String, String>();
      map.put("logSequence", "" + getRecordSequence());
    }
    return map;
  }
}
