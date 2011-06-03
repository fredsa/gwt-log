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

package com.allen_sauer.gwt.log.client.impl;

import com.allen_sauer.gwt.log.client.Logger;
import com.allen_sauer.gwt.log.client.util.DOMUtil;
import com.allen_sauer.gwt.log.shared.LogRecord;

/**
 * Temporary class used to inform the developer that the RemoteLogger has a new 
 * activation method which involves module inheritance rather than the setting
 * of a property.
 */
public class RemoteLoggerLegacyEnabled implements Logger {

  public RemoteLoggerLegacyEnabled() {
    DOMUtil.reportFatalAndThrowRuntimeException(
        "Please update your project's .gwt.xml file and replace\n"
            + "  <set-property name='log_RemoteLogger' value='ENABLED' />\n" + "with:\n"
            + "  <inherits name='com.allen_sauer.gwt.log.gwt-log-RemoteLogger' />");
  }

  @Override
  public void clear() {
  }

  @Override
  public boolean isSupported() {
    return true;
  }

  @Override
  public void log(LogRecord record) {
  }

  @Override
  public void setCurrentLogLevel(int level) {
  }

}
