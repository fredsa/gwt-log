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

// CHECKSTYLE_JAVADOC_OFF
public abstract class ServerSideAbstractLogger implements ServerSideLogger {
  public void clear() {
    throw new UnsupportedOperationException();
  }

  public void debug(String message, Throwable throwable) {
    throw new UnsupportedOperationException();
  }

  public void error(String message, Throwable throwable) {
    throw new UnsupportedOperationException();
  }

  public void fatal(String message, Throwable throwable) {
    throw new UnsupportedOperationException();
  }

  public void info(String message, Throwable throwable) {
    throw new UnsupportedOperationException();
  }

  public boolean isSupported() {
    throw new UnsupportedOperationException();
  }

  public void setCurrentLogLevel(int level) {
    throw new UnsupportedOperationException();
  }

  public void trace(String message, Throwable throwable) {
    throw new UnsupportedOperationException();
  }

  public void warn(String message) {
    throw new UnsupportedOperationException();
  }

  public void warn(String message, Throwable throwable) {
    throw new UnsupportedOperationException();
  }

  void log(int logLevel, String message) {
    throw new UnsupportedOperationException();
  }

  void log(int logLevel, String message, Throwable throwable) {
    throw new UnsupportedOperationException();
  }
}
