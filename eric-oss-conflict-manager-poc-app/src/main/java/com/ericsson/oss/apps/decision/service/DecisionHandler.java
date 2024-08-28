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
package com.ericsson.oss.apps.decision.service;

import com.ericsson.oss.apps.model.dto.ConflictDecision;
import com.ericsson.oss.apps.model.dto.ConflictDecisionRequest;

public interface DecisionHandler {
  ConflictDecision executeDecision(final ConflictDecisionRequest conflictDecisionRequest);
}
