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
package com.allen_sauer.gwt.log.rebind;

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.util.collect.HashSet;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import com.allen_sauer.gwt.log.client.LogUtil;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generator for LogMessageFormatter interface.
 */
public class LogMessageFormatterGenerator extends Generator {
  // CHECKSTYLE_JAVADOC_OFF

  private static final HashMap<String, String> CONVERSION_MAP = new HashMap<String, String>();

  private static final HashMap<String, String> DATE_FORMAT_MAP = new HashMap<String, String>();

  private static final String ISO8601 = "ISO8601";

  private static final String PROPERTY_LOG_PATTERN = "log_pattern";

  private static Set<String> STACKTRACE_SET = new HashSet<String>();
  static {
    DATE_FORMAT_MAP.put("ABSOLUTE", "HH:mm:ss,SSS");
    DATE_FORMAT_MAP.put("DATE", "dd MMM yyyy HH:mm:ss,SSS");
    DATE_FORMAT_MAP.put(ISO8601, "yyyy-MM-dd HH:mm:ss,SSS");
  }

  static {
    // %c - category
    CONVERSION_MAP.put("c", "category");

    // %C - fully qualified class name of caller
    CONVERSION_MAP.put("C", "ste == null ? \"-\" : ste.getClassName()");
    STACKTRACE_SET.add("C");

    // %d - date of logging event (default ISO8601)
    // e.g. %d{dd MMM yyyy HH:mm:ss,SSS}, %d{ISO8601} or %d{ABSOLUTE}
    CONVERSION_MAP.put("d", "new Date()");

    // %F - filename
    CONVERSION_MAP.put("F", "ste == null ? \"-\" : ste.getFileName()");
    STACKTRACE_SET.add("F");

    // %l - location information of caller
    CONVERSION_MAP.put("l", "ste == null ? \"-\" : ste.toString()");
    STACKTRACE_SET.add("l");

    // %L - line number of caller
    CONVERSION_MAP.put("L", "ste == null ? \"-\" : ste.getLineNumber()");
    STACKTRACE_SET.add("L");

    // %m - application supplied message
    CONVERSION_MAP.put("m", "message");

    // %M - method name of caller
    CONVERSION_MAP.put("M", "ste == null ? \"-\" : ste.getMethodName()");
    STACKTRACE_SET.add("M");

    // %n - platform dependent line separator
    CONVERSION_MAP.put("n", "\"\\\\n\"");

    // %p - priority of logging event
    CONVERSION_MAP.put("p", "logLevelText");

    // %r - number of elapsed milliseconds
    CONVERSION_MAP.put("r", "Math.round(Duration.currentTimeMillis() - BIG_BANG)");

    // %t - name of caller thread
    CONVERSION_MAP.put("t", "\"-\"");

    // %x - nested diagnostic context
    CONVERSION_MAP.put("x", "\"-\"");

    // %X - mapped diagnostic context, e.g. %X{someKey}
    CONVERSION_MAP.put("X", "\"-\"");

    // %% - percent sign
    CONVERSION_MAP.put("%", "\"%\"");
  }

  /**
   * Convert a log pattern to source code to be used in generated code.
   * 
   * @param logPattern the log pattern to convert
   * @return source code which will format the supplied message at runtime
   */
  private static String logPatternToCode(String logPattern) {
    StringBuffer buf = new StringBuffer("\"\"");
    // Regex breakdown
    // 1. (.*?) - Non pattern characters
    // -. % - Escape character ("%")
    // 2. (-?) - Optional leading dash ("-")
    // 3. (\\d*) - Zero or more digits
    // -. \\.? - Optional period (".")
    // 4. (\\d*) - Zero or more digits
    // 5. ([cCdFlLmMnprtxX%]) - Conversion character
    // 6. (\\{ . . . \\})? - "{" + . . . + "}"
    // 7. ([^\\}]+) - Format specifier: one or more characters, but not "}"
    Pattern pattern = Pattern.compile(
        "(.*?)%(-?)(\\d*)\\.?(\\d*)([cCdFlLmMnprtxX%])(\\{([^\\}]+)\\})?");
    Matcher matcher = pattern.matcher(logPattern);
    boolean stackTraceToggle = false;
    while (matcher.find()) {
      buf.append("\n + \"").append(matcher.group(1)).append("\"");
      int minFieldWidth = Integer.parseInt(matcher.group(2) + "0" + matcher.group(3));
      int maxFieldWidth = Integer.parseInt("0" + matcher.group(4));
      String conversionSpecifier = matcher.group(5);
      String formatSpecifier = matcher.group(7);

      String convertedExpression = CONVERSION_MAP.get(conversionSpecifier);
      if (STACKTRACE_SET.contains(conversionSpecifier)) {
        stackTraceToggle = true;
      }
      String group2ToEnd = matcher.group(0).substring(matcher.group(1).length());
      if (convertedExpression == null) {
        buf.append("\n + \"");
        matcher.appendReplacement(buf, group2ToEnd);
        buf.append("\" // \"").append(group2ToEnd).append("\"");
      } else {
        if (minFieldWidth > 0) {
          // right justify
          convertedExpression = "LogUtil.padLeft(" + convertedExpression + ", " + minFieldWidth
              + ")";
        } else if (minFieldWidth < 0) {
          // left justify
          convertedExpression = "LogUtil.padRight(" + convertedExpression + ", " + -minFieldWidth
              + ")";
        }
        if (maxFieldWidth > 0) {
          convertedExpression = "LogUtil.trim(" + convertedExpression + ", " + maxFieldWidth + ")";
        }

        // Handle date formating
        if (conversionSpecifier.equals("d")) {
          if (formatSpecifier == null) {
            formatSpecifier = ISO8601;
          }
          String newFormatSpecifier = DATE_FORMAT_MAP.get(formatSpecifier);
          if (newFormatSpecifier != null) {
            formatSpecifier = newFormatSpecifier;
          }
          convertedExpression = "LogUtil.formatDate(" + convertedExpression + ", \""
              + formatSpecifier + "\")";
        } else if (conversionSpecifier.equals("c") || conversionSpecifier.equals("C")) {
          if (formatSpecifier != null) {
            int precision = Integer.parseInt("0" + formatSpecifier);
            convertedExpression = "LogUtil.formatCategory(" + convertedExpression + ", " + precision
                + ")";
          }
        }

        buf.append("\n + (");
        matcher.appendReplacement(buf, convertedExpression);
        buf.append(") // \"").append(group2ToEnd).append("\"");
      }
    }
    buf.append("\n + \"");
    matcher.appendTail(buf);
    buf.append("\"");
    String ste = "GWT.isScript() ? null : LogUtil.getCallingStackTraceElement()";
    return (stackTraceToggle ? "StackTraceElement ste = " + ste + ";\n" : "") + "return "
        + buf.toString() + ";";
  }

  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName)
      throws UnableToCompleteException {
    TypeOracle typeOracle = context.getTypeOracle();

    JClassType userType;
    try {
      userType = typeOracle.getType(typeName);
    } catch (NotFoundException e) {
      logger.log(TreeLogger.ERROR, "OOPS", e);
      throw new UnableToCompleteException();
    }
    String packageName = userType.getPackage().getName();
    String className = userType.getName();

    JClassType remoteService = typeOracle.findType(typeName);
    if (remoteService == null) {
      logger.log(TreeLogger.ERROR, "Unable to find metadata for type '" + typeName + "'", null);
      throw new UnableToCompleteException();
    }

    if (remoteService.isInterface() == null) {
      logger.log(
          TreeLogger.ERROR, remoteService.getQualifiedSourceName() + " is not an interface", null);
      throw new UnableToCompleteException();
    }
    ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(
        packageName, className + "Impl");
    composerFactory.addImplementedInterface(remoteService.getQualifiedSourceName());

    composerFactory.addImport(Date.class.getName());
    composerFactory.addImport(GWT.class.getName());
    composerFactory.addImport(LogUtil.class.getName());
    composerFactory.addImport(Duration.class.getName());

    PrintWriter pw = context.tryCreate(logger, packageName, className + "Impl");
    if (pw != null) {
      SourceWriter sw = composerFactory.createSourceWriter(context, pw);

      PropertyOracle propertyOracle = context.getPropertyOracle();
      String logPattern;
      try {
        ConfigurationProperty logPatternProperty = propertyOracle.getConfigurationProperty(
            PROPERTY_LOG_PATTERN);
        List<String> values = logPatternProperty.getValues();
        logPattern = values.get(0);
      } catch (BadPropertyValueException e) {
        logger.log(TreeLogger.ERROR, "Unable to find value for '" + PROPERTY_LOG_PATTERN + "'", e);
        throw new UnableToCompleteException();
      }

      sw.println();
      sw.println("private double BIG_BANG = Duration.currentTimeMillis();");

      sw.println();
      sw.println("public String format(String logLevelText, String category, String message) {");
      sw.indent();
      sw.println("if (category == null) {");
      sw.indent();
      sw.println("category = \"<null category>\";");
      sw.outdent();
      sw.println("}");
      sw.println("if (message == null) {");
      sw.indent();
      sw.println("message = \"<null message>\";");
      sw.outdent();
      sw.println("}");
      sw.println(logPatternToCode(logPattern));
      sw.outdent();
      sw.println("}\n");

      sw.commit(logger);
    }
    return composerFactory.getCreatedClassName();
  }
}
