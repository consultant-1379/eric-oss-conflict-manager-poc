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
package com.ericsson.oss.apps;

import com.ericsson.oss.apps.api.model.ConflictManagerEvaluatedResponse;
import com.ericsson.oss.apps.api.model.ConflictManagerEvaluationRequest;
import com.ericsson.oss.apps.api.model.ConflictManagerResponseDecisionEnum;
import com.ericsson.oss.apps.model.dto.ConflictDecision;
import com.ericsson.oss.apps.model.dto.ConflictDecisionRequest;
import com.ericsson.oss.apps.model.entity.ConflictDecisionEntity;
import com.ericsson.oss.apps.model.entity.ConflictEvaluationRequestEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {

  public static final String DECISION_ENGINE_EXCEPTION_MESSAGE =
      "Exception during executing decision  ";
  public static final String DECISION_ENGINE_EXCEPTION_DETAILS =
      "Exception during executing decision  ";

  public static final String CONFLICT_MANAGER_EXCEPTION_MESSAGE =
      "Exception at Conflict Service during DB operation";

  public static final String CONFLICT_MANAGER_EXCEPTION_DETAILS =
      "Exception at Conflict Service during DB operation";

  public static final String PERMIT = "PERMIT";
  public static final String PERMIT_REASON = "change is allowed";

  public static final String DENY = "DENY";
  public static final String DENY_REASON = "Conflict Detected";

  public static final String CM_HANDLE_ID = "F811AF64F5146DFC545EC60B73DE948E";

  public static final String CHANGE_ID = "19787929-586b-497f-89f7-998eaccf9761";

  public static ConflictEvaluationRequestEntity populateConflictEvaluateEntity() {

    return ConflictEvaluationRequestEntity.builder()
        .changeRequest(
            "{\n"
                + "   \"NRCellDU\":[\n"
                + "      {\n"
                + "         \"id\":\"NRCellDU-id\",\n"
                + "         \"attributes\":{\n"
                + "            \"administrativeState\": \"UNLOCKED\"\n"
                + "         }\n"
                + "      }")
        .cmHandleId(CM_HANDLE_ID)
        .managedElement(
            "\"SubNetwork=Europe,SubNetwork=Ireland,SubNetwork=NETSimW,ManagedElement=LTE20dg2ERBS00011\"")
        .decisionId(0)
        .requestReceivedTime(LocalDateTime.now())
        .resourceIdentifier("ericsson-enm-gnbdu:GNBDUFunction=1")
        .build();
  }

  public static ConflictManagerEvaluationRequest createValidRequest() {
    String changeRequest =
        """
                        {
                           "NRCellDU":[
                              {
                                 "id":"NRCellDU-id",
                                 "attributes":{
                                    "administrativeState": "UNLOCKED"
                                 }
                              }""";

    return new ConflictManagerEvaluationRequest(
        CM_HANDLE_ID,
        "ericsson-enm-gnbdu:GNBDUFunction=1",
        "SubNetwork=Europe,SubNetwork=Ireland,SubNetwork=NETSimW,ManagedElement=LTE20dg2ERBS00011",
        changeRequest);
  }

  public static ConflictDecisionRequest populateConflictDecisionExclusion() {

    ConflictDecisionRequest conflictDecisionRequest = new ConflictDecisionRequest();
    conflictDecisionRequest.setChangeId(UUID.randomUUID().toString());
    conflictDecisionRequest.setReceivedTimestamp(LocalDateTime.now());
    conflictDecisionRequest.setCmHandle(CM_HANDLE_ID);
    conflictDecisionRequest.setResourceIdentifier("ericsson-enm-gnbdu:GNBDUFunction=1");
    return conflictDecisionRequest;
  }

  public static ConflictDecisionRequest populateConflictDecisionInclusion() {

    ConflictDecisionRequest conflictDecisionRequest = new ConflictDecisionRequest();
    conflictDecisionRequest.setChangeId(UUID.randomUUID().toString());
    conflictDecisionRequest.setReceivedTimestamp(LocalDateTime.now().minusMinutes(1));
    conflictDecisionRequest.setCmHandle("F811AF64F5146DFC545EC60B73DE948EF");
    conflictDecisionRequest.setResourceIdentifier("ericsson-enm-gnbdu:GNBDUFunction=1");
    return conflictDecisionRequest;
  }

  public static ConflictEvaluationRequestEntity populateEntityObject(
      final ConflictManagerEvaluationRequest managerEvaluationRequest) {

    return ConflictEvaluationRequestEntity.builder()
        .id(UUID.randomUUID())
        .cmHandleId(managerEvaluationRequest.getCmHandleId())
        .resourceIdentifier(managerEvaluationRequest.getResourceIdentifier())
        .managedElement(managerEvaluationRequest.getTargetFdn())
        .changeRequest(managerEvaluationRequest.getChangeRequest().toString())
        .decisionId(0)
        .build();
  }

  public static ConflictDecisionEntity populateConflictDecisionEntity(
      final ConflictEvaluationRequestEntity conflictEvaluationRequestEntity) {

    return ConflictDecisionEntity.builder()
        .conflictEvaluationRequestEntity(conflictEvaluationRequestEntity)
        .reason(PERMIT_REASON)
        .id(UUID.randomUUID())
        .build();
  }

  public static ConflictDecision populateConflictDecisionResponse_DENY(
      final ConflictEvaluationRequestEntity conflictEvaluationRequestEntity) {
    ConflictDecision conflictDecision = new ConflictDecision();
    conflictDecision.setReason(TestUtils.DENY_REASON);
    conflictDecision.setChangeId(conflictEvaluationRequestEntity.getId().toString());
    conflictDecision.setDecision(ConflictManagerResponseDecisionEnum.DENY);
    return conflictDecision;
  }

  public static ConflictDecision populateConflictDecisionResponse_PERMIT(
      final ConflictEvaluationRequestEntity conflictEvaluationRequestEntity) {
    ConflictDecision conflictDecision = new ConflictDecision();
    conflictDecision.setReason(TestUtils.PERMIT_REASON);
    conflictDecision.setChangeId(conflictEvaluationRequestEntity.getId().toString());
    conflictDecision.setDecision(ConflictManagerResponseDecisionEnum.PERMIT);
    return conflictDecision;
  }

  public ConflictManagerEvaluatedResponse createConflictResponse() {
    ConflictManagerEvaluatedResponse conflictManagerEvaluatedResponse =
        new ConflictManagerEvaluatedResponse();
    conflictManagerEvaluatedResponse.setReason(TestUtils.PERMIT_REASON);
    conflictManagerEvaluatedResponse.setDecision(ConflictManagerResponseDecisionEnum.PERMIT);
    conflictManagerEvaluatedResponse.setChangeId(CHANGE_ID);
    conflictManagerEvaluatedResponse.setCmHandleId(CM_HANDLE_ID);
    return conflictManagerEvaluatedResponse;
  }
}
