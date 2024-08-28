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
package com.ericsson.oss.apps.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConflictEvaluationConstants {

  public static final String PERMIT_REASON = "No conflict identified, the change is permitted";

  public static final String DENY_REASON = "Conflict identified, the change is denied";
  public static final String INVALID_METHOD_ARG = "Mandatory Parameter is missing";
  public static final String INVALID_METHOD_PARAM = "Mandatory Parameter ";
  public static final String CONFLICT_SCHEMA = "conflict_schema";

  public static final String REASON_ALLOWED = "change is allowed";

  public static final String REASON_TIME_BASED_LOCKING = "Time based locking is active";
}
