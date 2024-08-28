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

import static com.ericsson.oss.apps.util.ConflictError.CONFLICT_MANAGER_EXCEPTION_DETAILS;
import static com.ericsson.oss.apps.util.ConflictError.CONFLICT_MANAGER_EXCEPTION_MESSAGE;
import static com.ericsson.oss.apps.util.ConflictError.DECISION_ENGINE_EXCEPTION_DETAILS;
import static com.ericsson.oss.apps.util.ConflictError.DECISION_ENGINE_EXCEPTION_MESSAGE;
import static org.mockito.Mockito.doReturn;

import com.ericsson.oss.apps.api.model.ConflictManagerErrorMessage;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
public class ConflictEvaluationControllerAdviceTest {

  @InjectMocks private ConflictEvaluationControllerAdvice ConflictEvaluationControllerAdvice;
  @Mock private MethodParameter methodParameter;
  @Mock private BindingResult bindingResult;

  @Mock private Exception ex;

  @Test
  public void testHandleMethodArgumentNotValid_ObjectError() {
    doReturn(Collections.singletonList(new ObjectError("TestObjectError", "Test Object Error")))
        .when(bindingResult)
        .getAllErrors();
    MethodArgumentNotValidException methodArgumentNotValidException =
        new MethodArgumentNotValidException(methodParameter, bindingResult);

    ResponseEntity<ConflictManagerErrorMessage> response =
        ConflictEvaluationControllerAdvice.handleMethodArgumentNotValid(
            methodArgumentNotValidException);
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
  }

  @Test
  public void testHandleMethodArgumentNotValid_FieldError() {
    doReturn(
            Collections.singletonList(
                new FieldError("TestObjectError", "Test Object Error", "Test Object Message")))
        .when(bindingResult)
        .getAllErrors();
    MethodArgumentNotValidException methodArgumentNotValidException =
        new MethodArgumentNotValidException(methodParameter, bindingResult);

    ResponseEntity<ConflictManagerErrorMessage> response =
        ConflictEvaluationControllerAdvice.handleMethodArgumentNotValid(
            methodArgumentNotValidException);
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
  }

  @Test
  public void testHandleDecisionEngineException() {

    DecisionEngineException dce =
        new DecisionEngineException(
            DECISION_ENGINE_EXCEPTION_MESSAGE, DECISION_ENGINE_EXCEPTION_DETAILS);
    ResponseEntity<ConflictManagerErrorMessage> response =
        ConflictEvaluationControllerAdvice.handleDecisionEngineException(dce);
    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
  }

  @Test
  public void testHandleGeneralException() {

    doReturn("Exception occurred in conflict handling").when(ex).getMessage();
    ResponseEntity<ConflictManagerErrorMessage> response =
        ConflictEvaluationControllerAdvice.handleGeneralException(ex);
    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
  }

  @Test
  public void testHandleConflictManagerException() {

    ConflictManagerException cme =
        new ConflictManagerException(
            CONFLICT_MANAGER_EXCEPTION_MESSAGE, CONFLICT_MANAGER_EXCEPTION_DETAILS);

    ResponseEntity<ConflictManagerErrorMessage> response =
        ConflictEvaluationControllerAdvice.handleConflictManagerException(cme);
    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
  }
}
