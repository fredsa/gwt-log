package com.allen_sauer.gwt.log;

public class LogStdioTest extends LogTest {
  public static void main(String[] args) {
    System.setProperty("gwt-log.RemoteLogger", "STDIO");
    testApi();
  }
}
