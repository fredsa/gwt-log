# Summary #
Library, provides easy to use logging capabilities, which works transparently on both the client and the server, while providing compile time elimination of client logging code via deferred binding for [Google Web Toolkit](http://www.gwtproject.org/) (GWT).

# Key Features #
  * Production Mode stack trace deobfuscation!
  * Client-side logging code is compiled out and introduces zero-overhead<sup>+</sup> due to GWT compiler dead code elimination when `log_level=OFF`
  * Serializable classes (domain objects, POJOs, etc.) can utilize the same logging code in client and server source code
  * Server side logging for mobile devices such as the Android, iPhone
  * Server side logging automatically detects Apache [log4j](http://logging.apache.org/log4j/), falling back to [JDK 1.4 logging](http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/overview.html)
  * Seven out of the box log levels (`TRACE`, `DEBUG`, `INFO`, `WARN`, `ERROR`, `FATAL`, `OFF`)
  * Several ways to control logging, via `*.gwt.xml` module file, a `gwt:property` in your HTML, a `log_level` URL parameter, at run time via `Log.setCurrentLogLevel(...)`
  * Ability to independently control compile time and runtime log levels
  * Both client side and server side logging work seamlessly with [Google App Engine](https://cloud.google.com/appengine)
  * Wide variety of context sensitive log destinations:

    | **Logger** | **Description** |
    |:-----------|:----------------|
    | `ConsoleLogger` | Utilizes `console.log()` for supporting browsers and installed JavaScript libraries. |
    | `FirebugLogger` | Utilizes [Firebug](http://www.getfirebug.com/) console [API](http://getfirebug.com/logging) logging. |
    | `DivLogger` | Utilizes a floating/draggable `DIV` for log messages. |
    | `GWTLogger` | Utilizes `GWT.log()`, which will appear in the development shell in Development Mode. |
    | `SystemLogger` | Utilizes `System.err` and `System.out`. |
    | `RemoteLogger` | Sends copies of client log messages to the server, with optional deobfuscation. |
    | `WindowLogger` | (Experimental) Logs messages to a separate window. |

<sup>+ Unless your logging parameters are determined by the GWT compiler to have </sup>_<sup>side effects</sup>_

# Questions? #
If you have questions, please post them on http://groups.google.com/group/gwt-log and I (or someone else) will try to answer them as best as possible. Using the forum means that others can benefit from any answers and feedback you get. It is always the fastest way to get an answer to a new question.

# Is your project using gwt-log? #
I'd like to know if you're using gwt-log on your project, and how useful (or not) this library is to you. You can send me an email at [fredsa@gmail.com](mailto:fredsa@gmail.com?subject=gwt-log).

# Working examples #
Try the [logging demo](http://allen-sauer.com/com.allen_sauer.gwt.log.demo.LogDemo/LogDemo.html):

[![](http://storage.googleapis.com/gwt-log/2008-10-28-log-panel-click-here.png)](http://allen-sauer.com/com.allen_sauer.gwt.log.demo.LogDemo/LogDemo.html)

# Getting started with your own gwt-log project #
Read the wiki here: https://github.com/fredsa/gwt-log/wiki/GettingStarted

# Feedback #
Please let me know what you think. Suggestions are always welcome.

# Other GWT projects by the same author #

| **Project** | **Description** |
|:------------|:----------------|
| [gwt-dnd](https://github.com/fredsa/gwt-dnd/) | Provides drag and drop support for your GWT applications. |
| [gwt-voices](https://github.com/fredsa/gwt-voices/) | Provides sound support for your GWT applications. |
