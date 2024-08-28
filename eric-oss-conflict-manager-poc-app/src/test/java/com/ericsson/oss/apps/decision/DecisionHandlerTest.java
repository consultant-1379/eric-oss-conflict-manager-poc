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
package com.ericsson.oss.apps.decision;

import static org.mockito.Mockito.when;

import com.ericsson.oss.apps.decision.engine.DecisionEngine;
import com.ericsson.oss.apps.decision.service.DecisionHandlerImpl;
import com.ericsson.oss.apps.model.dto.ConflictDecision;
import com.ericsson.oss.apps.model.dto.ConflictDecisionRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DecisionHandlerTest {
  @InjectMocks private DecisionHandlerImpl decisionHandler;

  @Mock private DecisionEngine decisionEngine;

  @Test
  public void testDecisionHandler() {

    ConflictDecisionRequest conflictDecisionRequest = new ConflictDecisionRequest();
    when(decisionEngine.execute(conflictDecisionRequest)).thenReturn(new ConflictDecision());
    Assertions.assertNotNull(decisionHandler.executeDecision(conflictDecisionRequest));
  }
}
