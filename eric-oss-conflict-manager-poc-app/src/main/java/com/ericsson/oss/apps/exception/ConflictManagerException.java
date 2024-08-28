/*******************************************************************************
 * COPYRIGHT Ericsson 2024
 *
 *
 *
 * The copyright to the computer program(s) herein is the property of
 *
 * Ericsson Inc. The programs may be used and/or copied only with written
 *
 * permission from Ericsson Inc. or in accordance with the terms and
 *
 * conditions stipulated in the agreement/contract under which the
 *
 * program(s) have been supplied.
 ******************************************************************************/
package com.ericsson.oss.apps.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConflictManagerException extends RuntimeException {

  private final String message;
  private final String details;

  public ConflictManagerException(final String message, final String details) {
    super(message);
    this.message = message;
    this.details = details;
  }

  public static String getStackTraceInfo(final Exception exception) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    exception.printStackTrace(pw);
    return sw.toString();
  }
}
