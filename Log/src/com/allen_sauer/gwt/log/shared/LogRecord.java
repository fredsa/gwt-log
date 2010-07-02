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

  public String getFormattedMessage() {
    assert GWT.isClient() : "Method should only be called in Client Code";
    return level == Log.LOG_LEVEL_OFF ? message : FORMATTER.format(
        LogUtil.levelToString(level), category, message);
  }

  public int getLevel() {
    return level;
  }

  public Set<Entry<String, String>> getMapEntrySet() {
    return getHashMap().entrySet();
  }

  public String getMessage() {
    return message;
  }

  public int getRecordSequence() {
    return recordSequence;
  }

  public Throwable getThrowable() {
    return throwable != null ? throwable : UnwrappedClientThrowable.getInstanceOrNull(
        wrappedClientThrowable);
  }

  public WrappedClientThrowable getWrappedClientThrowable() {
    return wrappedClientThrowable;
  }

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
