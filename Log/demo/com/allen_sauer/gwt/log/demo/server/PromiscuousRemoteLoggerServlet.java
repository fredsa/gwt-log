// Copyright 2011 Google Inc. All Rights Reserved.

package com.allen_sauer.gwt.log.demo.server;

import com.allen_sauer.gwt.log.server.RemoteLoggerServlet;

import javax.servlet.http.HttpServletRequest;

/**
 * Remote logger servlet which allows cross domain logging from any origin. It'll eat your logs.
 */
public class PromiscuousRemoteLoggerServlet extends RemoteLoggerServlet {
  @Override
  protected String getAccessControlAllowOriginHeader(HttpServletRequest request) {
    // Whatever the client sends us, we accept
    return request.getHeader("Origin");
  }
}
