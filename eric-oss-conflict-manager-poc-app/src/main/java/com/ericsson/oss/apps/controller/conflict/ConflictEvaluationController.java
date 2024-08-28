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
package com.ericsson.oss.apps.controller.conflict;

import com.ericsson.oss.apps.api.ConflictEvaluationApi;
import com.ericsson.oss.apps.api.model.ConflictManagerEvaluatedResponse;
import com.ericsson.oss.apps.api.model.ConflictManagerEvaluationRequest;
import com.ericsson.oss.apps.service.ConflictEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ConflictEvaluationController implements ConflictEvaluationApi {
  @Autowired private final ConflictEvaluationService conflictEvaluationService;

  @Override
  public ResponseEntity<ConflictManagerEvaluatedResponse> evaluateRequest(
      final ConflictManagerEvaluationRequest conflictManagerEvaluationRequest) {
    log.info(
        "Received a Post request to evaluate change request = {}",
        conflictManagerEvaluationRequest);
    return new ResponseEntity<>(
        conflictEvaluationService.evaluateRequest(conflictManagerEvaluationRequest), HttpStatus.OK);
  }
}
