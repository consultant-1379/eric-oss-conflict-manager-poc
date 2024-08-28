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

import static com.ericsson.oss.apps.TestUtils.DENY;
import static com.ericsson.oss.apps.TestUtils.PERMIT;
import static com.ericsson.oss.apps.TestUtils.populateConflictDecisionExclusion;
import static com.ericsson.oss.apps.TestUtils.populateConflictDecisionInclusion;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ericsson.oss.apps.decision.engine.DefaultDecisionEngine;
import com.ericsson.oss.apps.exception.DecisionEngineException;
import com.ericsson.oss.apps.model.dto.ConflictDecision;
import com.ericsson.oss.apps.model.dto.ConflictDecisionRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class DecisionEngineTest {
  @InjectMocks private DefaultDecisionEngine decisionEngine;

  /*@Mock
  private int timeInMinutes;*/

  @Test
  public void testDecisionEngine_Permit_Change() {
    ReflectionTestUtils.setField(decisionEngine, "changeTimeInMinutes", "0");
    ConflictDecisionRequest conflictDecisionRequest = populateConflictDecisionInclusion();
    ConflictDecision conflictDecision = decisionEngine.execute(conflictDecisionRequest);
    Assertions.assertNotNull(conflictDecision);
    Assertions.assertEquals(PERMIT, conflictDecision.getDecision().getValue());
  }

  @Test
  public void testDecisionEngine_Deny_Change() {
    ReflectionTestUtils.setField(decisionEngine, "changeTimeInMinutes", "2");
    ConflictDecision conflictDecision = decisionEngine.execute(populateConflictDecisionExclusion());
    Assertions.assertNotNull(conflictDecision);
    Assertions.assertEquals(DENY, conflictDecision.getDecision().getValue());
  }

  @Test
  public void testDecisionEngine_exception() {
    ReflectionTestUtils.setField(decisionEngine, "changeTimeInMinutes", "2");
    ConflictDecisionRequest conflictDecisionRequest = new ConflictDecisionRequest();
    assertThrows(
        DecisionEngineException.class,
        () -> {
          decisionEngine.execute(conflictDecisionRequest);
        });
  }
}
