/*
 * Copyright 2007 Fred Sauer
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

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;

class LogUtil {
  static JavaScriptException convertJavaScriptObjectToException(
      JavaScriptObject e) {
     return new JavaScriptException(javaScriptExceptionName(e),
        javaScriptExceptionDescription(e));
  }

  private static native String javaScriptExceptionDescription(JavaScriptObject e)
  /*-{
    try {
     return e.message;
   } catch(ex) {
     return "[e has no message]";
   }
  }-*/;

  private static native String javaScriptExceptionName(JavaScriptObject e)
  /*-{
    try {
     return e.name;
   } catch(ex) {
     return "[e has no name]";
   }
  }-*/;
}
