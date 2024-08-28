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
package com.ericsson.oss.apps.decision.engine;

import static com.ericsson.oss.apps.util.ConflictError.DECISION_ENGINE_EXCEPTION;

import com.ericsson.oss.apps.api.model.ConflictManagerResponseDecisionEnum;
import com.ericsson.oss.apps.exception.DecisionEngineException;
import com.ericsson.oss.apps.model.dto.ConflictDecision;
import com.ericsson.oss.apps.model.dto.ConflictDecisionRequest;
import com.ericsson.oss.apps.util.ConflictEvaluationConstants;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component("defaultDecisionEngine")
@RequiredArgsConstructor
public class DefaultDecisionEngine implements DecisionEngine {

  @Value("${decision.changeTimeInMinutes}")
  private String changeTimeInMinutes;

  @Override
  public ConflictDecision execute(final ConflictDecisionRequest conflictDecisionRequest) {
    log.info("Decision Engine executing the logic = {}", conflictDecisionRequest);
    final Duration changeWindow = Duration.ofMinutes(Integer.valueOf(changeTimeInMinutes));
    final List<String> cmHandleList = new ArrayList<>();
    cmHandleList.add("F811AF64F5146DFC545EC60B73DE948E");
    final ConflictDecision conflictDecision = new ConflictDecision();
    try {
      log.info("change window configured = {}", changeWindow);
      if (!cmHandleList.contains(conflictDecisionRequest.getCmHandle())
          && Duration.between(conflictDecisionRequest.getReceivedTimestamp(), LocalDateTime.now())
                  .compareTo(changeWindow)
              > 0) {
        conflictDecision.setDecision(ConflictManagerResponseDecisionEnum.PERMIT);
        conflictDecision.setReason(ConflictEvaluationConstants.REASON_ALLOWED);
        conflictDecision.setChangeId(conflictDecisionRequest.getChangeId());
      } else {
        conflictDecision.setDecision(ConflictManagerResponseDecisionEnum.DENY);
        conflictDecision.setReason(ConflictEvaluationConstants.REASON_TIME_BASED_LOCKING);
        conflictDecision.setChangeId(conflictDecisionRequest.getChangeId());
      }
      log.info("Received a Post request to execute policy = {}", conflictDecisionRequest);
    } catch (Exception ex) {
      log.error("Exception during decision making = {}", ex.getMessage());
      throw new DecisionEngineException(ex.getMessage(), DECISION_ENGINE_EXCEPTION);
    }
    return conflictDecision;
  }
}
