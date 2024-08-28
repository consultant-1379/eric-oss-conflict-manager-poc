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
public class ConflictError {
  public static final String DECISION_ENGINE_EXCEPTION = "Exception during executing decision ";
  public static final String CONFLICT_EVALUATION_EXCEPTION_ATTRIBUTE_CONVERTER_MESSAGE =
      "Exception occurred while converting Entity attribute to or from jsonB to object  ";
  public static final String DECISION_ENGINE_EXCEPTION_MESSAGE =
      "Exception during executing decision  ";
  public static final String DECISION_ENGINE_EXCEPTION_DETAILS =
      "Exception during executing decision  ";

  public static final String CONFLICT_MANAGER_EXCEPTION_MESSAGE =
      "Exception at Conflict Service during DB operation";

  public static final String CONFLICT_MANAGER_EXCEPTION_DETAILS =
      "Exception at Conflict Service during DB operation";
  public static final String GENERAL_CONFLICT_EXCEPTION_MESSAGE =
      "Exception occurred during conflict evaluation  ";
}
