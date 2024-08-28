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
package com.ericsson.oss.apps.exception;

import static com.ericsson.oss.apps.util.ConflictError.GENERAL_CONFLICT_EXCEPTION_MESSAGE;
import static com.ericsson.oss.apps.util.ConflictEvaluationConstants.INVALID_METHOD_ARG;
import static com.ericsson.oss.apps.util.ConflictEvaluationConstants.INVALID_METHOD_PARAM;

import com.ericsson.oss.apps.api.model.ConflictManagerErrorMessage;
import com.ericsson.oss.apps.api.model.ConflictManagerStatusError;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ConflictEvaluationControllerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ConflictManagerErrorMessage> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException ex) {
    log.error("Handling MethodArgumentNotValid exception.");
    ConflictManagerErrorMessage conflictManagerErrorMessage = new ConflictManagerErrorMessage();
    conflictManagerErrorMessage.setMessage(INVALID_METHOD_ARG);
    conflictManagerErrorMessage.setDetails(getErrorMessage(ex));
    conflictManagerErrorMessage.setStatus(ConflictManagerStatusError.BAD_REQUEST);
    return new ResponseEntity<>(
        conflictManagerErrorMessage,
        HttpStatus.valueOf(conflictManagerErrorMessage.getStatus().getValue()));
  }

  @ExceptionHandler(DecisionEngineException.class)
  public ResponseEntity<ConflictManagerErrorMessage> handleDecisionEngineException(
      final DecisionEngineException ex) {
    log.error("Handling DecisionEngine exception.");
    ConflictManagerErrorMessage conflictManagerErrorMessage = new ConflictManagerErrorMessage();
    conflictManagerErrorMessage.setMessage(ex.getMessage());
    conflictManagerErrorMessage.setDetails(ex.getDetails());
    conflictManagerErrorMessage.setStatus(ConflictManagerStatusError.INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(
        conflictManagerErrorMessage,
        HttpStatus.valueOf(conflictManagerErrorMessage.getStatus().getValue()));
  }

  @ExceptionHandler(ConflictManagerException.class)
  public ResponseEntity<ConflictManagerErrorMessage> handleConflictManagerException(
      final ConflictManagerException cme) {
    log.error("Handling ConflictManager exception.");
    ConflictManagerErrorMessage conflictManagerErrorMessage = new ConflictManagerErrorMessage();
    conflictManagerErrorMessage.setMessage(cme.getMessage());
    conflictManagerErrorMessage.setDetails(cme.getDetails());
    conflictManagerErrorMessage.setStatus(ConflictManagerStatusError.INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(
        conflictManagerErrorMessage,
        HttpStatus.valueOf(conflictManagerErrorMessage.getStatus().getValue()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ConflictManagerErrorMessage> handleGeneralException(final Exception ex) {
    log.error("Handling general exception.", ex);
    ConflictManagerErrorMessage conflictManagerErrorMessage = new ConflictManagerErrorMessage();
    conflictManagerErrorMessage.setMessage(GENERAL_CONFLICT_EXCEPTION_MESSAGE);
    conflictManagerErrorMessage.setDetails(ex.getMessage());
    conflictManagerErrorMessage.setStatus(ConflictManagerStatusError.INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(
        conflictManagerErrorMessage,
        HttpStatus.valueOf(conflictManagerErrorMessage.getStatus().getValue()));
  }

  private String getErrorMessage(MethodArgumentNotValidException ex) {
    List<String> fieldErrors = new ArrayList<>();
    List<String> objectErrors = new ArrayList<>();
    for (ObjectError objectError : ex.getBindingResult().getAllErrors()) {
      if (objectError instanceof FieldError) {
        FieldError fieldError = (FieldError) objectError;
        fieldErrors.add(String.join(" ", fieldError.getField(), fieldError.getDefaultMessage()));
      } else {
        objectErrors.add(objectError.getDefaultMessage());
      }
    }

    List<String> result =
        fieldErrors.stream().map(m -> INVALID_METHOD_PARAM + m).collect(Collectors.toList());
    Collections.sort(result);
    result.addAll(objectErrors.stream().distinct().collect(Collectors.toList()));
    return String.join("; ", result);
  }
}
