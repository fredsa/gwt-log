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
package com.allen_sauer.gwt.log.client.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Strict;

public interface LogClientBundle extends ClientBundle {

  interface LogCssResource extends CssResource {

    @ClassName("log-clear-about")
    public String logClearAbout();

    @ClassName("log-clear-button")
    public String logClearButton();

    @ClassName("log-message")
    public String logMessage();

    @ClassName("log-message-hover")
    public String logMessageHover();

    @ClassName("log-panel")
    public String logPanel();

    @ClassName("log-resize-se")
    public String logResizeSe();

    @ClassName("log-scroll-panel")
    public String logScrollPanel();

    @ClassName("log-stacktrace")
    public String logStacktrace();

    @ClassName("log-text-area")
    public String logTextArea();

    @ClassName("log-title")
    public String logTitle();
  }

  static final LogClientBundle INSTANCE = GWT.create(LogClientBundle.class);

  @Strict
  @Source("gwt-log.css")
  LogCssResource css();

}
