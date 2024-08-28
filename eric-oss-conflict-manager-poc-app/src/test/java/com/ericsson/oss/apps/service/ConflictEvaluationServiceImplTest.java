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
package com.ericsson.oss.apps.service;

import static com.ericsson.oss.apps.TestUtils.createValidRequest;
import static com.ericsson.oss.apps.TestUtils.populateConflictDecisionResponse_DENY;
import static com.ericsson.oss.apps.TestUtils.populateConflictDecisionResponse_PERMIT;
import static com.ericsson.oss.apps.TestUtils.populateEntityObject;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.ericsson.oss.apps.TestUtils;
import com.ericsson.oss.apps.api.model.ConflictManagerEvaluatedResponse;
import com.ericsson.oss.apps.api.model.ConflictManagerEvaluationRequest;
import com.ericsson.oss.apps.decision.service.DecisionHandler;
import com.ericsson.oss.apps.exception.ConflictManagerException;
import com.ericsson.oss.apps.exception.DecisionEngineException;
import com.ericsson.oss.apps.model.dto.ConflictDecision;
import com.ericsson.oss.apps.model.dto.ConflictDecisionRequest;
import com.ericsson.oss.apps.model.entity.ConflictDecisionEntity;
import com.ericsson.oss.apps.model.entity.ConflictEvaluationRequestEntity;
import com.ericsson.oss.apps.repositories.ConflictDecisionRepo;
import com.ericsson.oss.apps.repositories.ConflictEvaluationRequestRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ConflictEvaluationServiceImplTest {

  @Spy private DecisionHandler decisionHandler;

  @Mock private ConflictEvaluationRequestRepo conflictEvaluationRequestRepo;

  @Mock private ConflictDecisionRepo conflictDecisionRepo;

  @InjectMocks private ConflictEvaluationServiceImpl conflictEvaluationServiceImpl;

  @Test
  public void testConflictServiceEvaluation_Success() {
    final ConflictManagerEvaluationRequest conflictManagerEvaluationRequest = createValidRequest();
    final ConflictEvaluationRequestEntity conflictEvaluationRequestEntity =
        populateEntityObject(conflictManagerEvaluationRequest);
    final ConflictDecision conflictDecision =
        populateConflictDecisionResponse_PERMIT(conflictEvaluationRequestEntity);
    when(conflictEvaluationRequestRepo.save(any(ConflictEvaluationRequestEntity.class)))
        .thenReturn(conflictEvaluationRequestEntity);
    when(decisionHandler.executeDecision(any(ConflictDecisionRequest.class)))
        .thenReturn(conflictDecision);
    final ConflictManagerEvaluatedResponse conflictManagerEvaluatedResponse =
        conflictEvaluationServiceImpl.evaluateRequest(createValidRequest());
    Assertions.assertNotNull(conflictManagerEvaluatedResponse);
    Assertions.assertEquals(
        TestUtils.PERMIT, conflictManagerEvaluatedResponse.getDecision().getValue());
  }

  @Test
  public void testConflictServiceEvaluation_Failure() {
    final ConflictManagerEvaluationRequest conflictManagerEvaluationRequest = createValidRequest();
    final ConflictEvaluationRequestEntity conflictEvaluationRequestEntity =
        populateEntityObject(conflictManagerEvaluationRequest);
    final ConflictDecisionEntity conflictDecisionEntity =
        TestUtils.populateConflictDecisionEntity(conflictEvaluationRequestEntity);
    final ConflictDecision conflictDecision =
        populateConflictDecisionResponse_DENY(conflictEvaluationRequestEntity);
    when(conflictEvaluationRequestRepo.save(any(ConflictEvaluationRequestEntity.class)))
        .thenReturn(conflictEvaluationRequestEntity);
    when(decisionHandler.executeDecision(any(ConflictDecisionRequest.class)))
        .thenReturn(conflictDecision);
    when(conflictDecisionRepo.save(any(ConflictDecisionEntity.class)))
        .thenReturn(conflictDecisionEntity);
    final ConflictManagerEvaluatedResponse conflictManagerEvaluatedResponse =
        conflictEvaluationServiceImpl.evaluateRequest(createValidRequest());
    Assertions.assertNotNull(conflictManagerEvaluatedResponse);
    Assertions.assertEquals(
        TestUtils.DENY, conflictManagerEvaluatedResponse.getDecision().getValue());
  }

  @Test
  public void testEvaluationReq_DecisionException() {
    final ConflictManagerEvaluationRequest conflictManagerEvaluationRequest = createValidRequest();
    final ConflictEvaluationRequestEntity conflictEvaluationRequestEntity =
        populateEntityObject(conflictManagerEvaluationRequest);
    when(conflictEvaluationRequestRepo.save(any(ConflictEvaluationRequestEntity.class)))
        .thenReturn(conflictEvaluationRequestEntity);
    when(decisionHandler.executeDecision(any(ConflictDecisionRequest.class)))
        .thenThrow(
            new DecisionEngineException(
                TestUtils.DECISION_ENGINE_EXCEPTION_MESSAGE,
                TestUtils.DECISION_ENGINE_EXCEPTION_DETAILS));
    assertThrows(
        DecisionEngineException.class,
        () -> {
          conflictEvaluationServiceImpl.evaluateRequest(createValidRequest());
        });
  }

  @Test
  public void testEvaluationReq_ConflictException() {
    ConflictManagerEvaluationRequest request = createValidRequest();
    ConflictEvaluationRequestEntity entity = populateEntityObject(request);
    ConflictDecisionRequest conflictDecisionRequest = new ConflictDecisionRequest();
    ConflictDecision conflictDecision = new ConflictDecision();
    lenient().when(conflictEvaluationRequestRepo.save(entity)).thenReturn(entity);
    lenient()
        .when(decisionHandler.executeDecision(conflictDecisionRequest))
        .thenReturn(conflictDecision);
    assertThrows(
        ConflictManagerException.class,
        () -> {
          conflictEvaluationServiceImpl.evaluateRequest(createValidRequest());
        });
  }
}
