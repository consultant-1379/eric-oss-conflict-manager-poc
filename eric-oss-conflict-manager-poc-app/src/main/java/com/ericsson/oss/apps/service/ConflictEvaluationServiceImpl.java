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

import com.ericsson.oss.apps.api.model.ConflictManagerEvaluatedResponse;
import com.ericsson.oss.apps.api.model.ConflictManagerEvaluationRequest;
import com.ericsson.oss.apps.api.model.ConflictManagerResponseDecisionEnum;
import com.ericsson.oss.apps.decision.service.DecisionHandler;
import com.ericsson.oss.apps.exception.ConflictManagerException;
import com.ericsson.oss.apps.exception.DecisionEngineException;
import com.ericsson.oss.apps.model.dto.ConflictDecision;
import com.ericsson.oss.apps.model.dto.ConflictDecisionRequest;
import com.ericsson.oss.apps.model.entity.ConflictDecisionEntity;
import com.ericsson.oss.apps.model.entity.ConflictEvaluationRequestEntity;
import com.ericsson.oss.apps.repositories.ConflictDecisionRepo;
import com.ericsson.oss.apps.repositories.ConflictEvaluationRequestRepo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConflictEvaluationServiceImpl implements ConflictEvaluationService {

  @Autowired private DecisionHandler decisionHandler;

  @Autowired private ConflictEvaluationRequestRepo conflictEvaluationRequestRepo;

  @Autowired private ConflictDecisionRepo conflictDecisionRepo;

  public ConflictManagerEvaluatedResponse evaluateRequest(
      final ConflictManagerEvaluationRequest conflictManagerEvaluationRequest)
      throws ConflictManagerException, DecisionEngineException {
    log.info(
        "Received a Post request to evaluate change request = {}",
        conflictManagerEvaluationRequest);
    try {
      final ConflictEvaluationRequestEntity conflictEvaluationRequestEntity =
          conflictEvaluationRequestRepo.save(
              populateEvalReqEntity(conflictManagerEvaluationRequest));
      log.info("Conflict evaluation request saved in the db = {}", conflictEvaluationRequestEntity);

      final ConflictDecisionRequest conflictDecisionRequest =
          populateDecisionReq(conflictEvaluationRequestEntity);
      final ConflictDecision conflictDecision =
          decisionHandler.executeDecision(conflictDecisionRequest);
      final ConflictDecisionEntity conflictDecisionEntity =
          populateDecisionReqEntity(conflictDecision, conflictEvaluationRequestEntity);

      if (ObjectUtils.nullSafeEquals(
          ConflictManagerResponseDecisionEnum.DENY, conflictDecision.getDecision())) {
        conflictDecisionRepo.save(conflictDecisionEntity);
        log.info("ConflictDecision  is saved to db = {}", conflictDecisionEntity.getId());
      }

      return populateConflictEvalResponse(conflictDecision, conflictDecisionRequest);

    } catch (DecisionEngineException decisionException) {

      log.error("Error during executing decision", decisionException);
      throw new DecisionEngineException(
          decisionException.getMessage(), decisionException.getDetails());
    } catch (Exception exception) {

      log.error("Error during saving conflict request in the db", exception);
      final String details = ConflictManagerException.getStackTraceInfo(exception);
      throw new ConflictManagerException(exception.getMessage(), details);
    }
  }

  private ConflictEvaluationRequestEntity populateEvalReqEntity(
      final ConflictManagerEvaluationRequest conflictManagerEvaluationRequest) {

    return ConflictEvaluationRequestEntity.builder()
        .cmHandleId(conflictManagerEvaluationRequest.getCmHandleId())
        .resourceIdentifier(conflictManagerEvaluationRequest.getResourceIdentifier())
        .managedElement(conflictManagerEvaluationRequest.getResourceIdentifier())
        .changeRequest(conflictManagerEvaluationRequest.getChangeRequest().toString())
        .build();
  }

  private ConflictDecisionEntity populateDecisionReqEntity(
      final ConflictDecision conflictDecision,
      final ConflictEvaluationRequestEntity conflictEvaluationRequestEntity) {
    return ConflictDecisionEntity.builder()
        .reason(conflictDecision.getReason())
        .conflictEvaluationRequestEntity(conflictEvaluationRequestEntity)
        .build();
  }

  private ConflictDecisionRequest populateDecisionReq(
      final ConflictEvaluationRequestEntity conflictEvaluationRequestEntity) {
    final ConflictDecisionRequest conflictDecisionRequest = new ConflictDecisionRequest();
    log.info("ConflictDecisionRequest dto object is created = {}", conflictDecisionRequest);
    conflictDecisionRequest.setCmHandle(conflictEvaluationRequestEntity.getCmHandleId());
    conflictDecisionRequest.setResourceIdentifier(
        conflictEvaluationRequestEntity.getResourceIdentifier());
    conflictDecisionRequest.setChangeRequest(conflictEvaluationRequestEntity.getChangeRequest());
    conflictDecisionRequest.setReceivedTimestamp(
        conflictEvaluationRequestEntity.getRequestReceivedTime());
    if (Optional.of(conflictEvaluationRequestEntity.getId()).isPresent()) {
      conflictDecisionRequest.setChangeId(conflictEvaluationRequestEntity.getId().toString());
    }
    return conflictDecisionRequest;
  }

  private ConflictManagerEvaluatedResponse populateConflictEvalResponse(
      final ConflictDecision conflictDecision,
      final ConflictDecisionRequest conflictDecisionRequest) {
    final ConflictManagerEvaluatedResponse conflictEvalResponse =
        new ConflictManagerEvaluatedResponse();
    conflictEvalResponse.setCmHandleId(conflictDecisionRequest.getCmHandle());
    conflictEvalResponse.setDecision(conflictDecision.getDecision());
    conflictEvalResponse.setReason(conflictDecision.getReason());
    conflictEvalResponse.setChangeId(conflictDecision.getChangeId());
    return conflictEvalResponse;
  }
}
