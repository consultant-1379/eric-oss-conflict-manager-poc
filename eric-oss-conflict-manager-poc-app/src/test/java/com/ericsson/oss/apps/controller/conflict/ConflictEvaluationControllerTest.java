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

import static com.ericsson.oss.apps.TestUtils.createValidRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.ericsson.oss.apps.service.ConflictEvaluationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WebMvcTest
@ContextConfiguration(classes = {ConflictEvaluationController.class})
@ActiveProfiles("test")
class ConflictEvaluationControllerTest {

  @Autowired ObjectMapper mapper;

  @Autowired private MockMvc mockMvc;

  @MockBean private ConflictEvaluationService conflictEvaluationService;

  @Test
  void positiveTestEvaluateRequest() throws Exception {

    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    final MockHttpServletResponse response =
        mockMvc
            .perform(
                post("/v1/evaluate")
                    .content(mapper.writeValueAsString(createValidRequest()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();
    Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
  }
}
