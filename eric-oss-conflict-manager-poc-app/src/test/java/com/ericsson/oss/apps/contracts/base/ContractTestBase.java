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
package com.ericsson.oss.apps.contracts.base;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ericsson.coverage.filter.ContractCoverageFilterV3;
import com.ericsson.oss.apps.TestUtils;
import com.ericsson.oss.apps.api.model.ConflictManagerEvaluatedResponse;
import com.ericsson.oss.apps.api.model.ConflictManagerResponseDecisionEnum;
import com.ericsson.oss.apps.controller.conflict.ConflictEvaluationController;
import com.ericsson.oss.apps.exception.ConflictEvaluationControllerAdvice;
import com.ericsson.oss.apps.service.ConflictEvaluationService;
import com.ericsson.oss.apps.util.ConflictEvaluationConstants;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@ActiveProfiles("test")
public class ContractTestBase {
  @Autowired private ConflictEvaluationController conflictEvaluationController;
  @Autowired private ConflictEvaluationControllerAdvice conflictEvaluationControllerAdvice;
  @MockBean protected ConflictEvaluationService conflictEvaluationService;

  @BeforeEach
  public void setup() {
    applyMocks();
  }

  void applyMocks() {
    mockBaseSystem();
    mockEvaluateRequestPermitResponse();
  }

  void mockBaseSystem() {
    MockMvc mockMvc =
        MockMvcBuilders.standaloneSetup(conflictEvaluationController)
            .setControllerAdvice(conflictEvaluationControllerAdvice)
            .setMessageConverters(new MappingJackson2HttpMessageConverter())
            .addFilters(new ContractCoverageFilterV3())
            .build();
    RestAssuredMockMvc.mockMvc(mockMvc);
  }

  void mockEvaluateRequestPermitResponse() {
    ConflictManagerEvaluatedResponse conflictManagerEvaluatedResponse =
        new ConflictManagerEvaluatedResponse();
    conflictManagerEvaluatedResponse.setChangeId(TestUtils.CHANGE_ID);
    conflictManagerEvaluatedResponse.setCmHandleId(TestUtils.CM_HANDLE_ID);
    conflictManagerEvaluatedResponse.setDecision(ConflictManagerResponseDecisionEnum.PERMIT);
    conflictManagerEvaluatedResponse.setReason(ConflictEvaluationConstants.PERMIT_REASON);
    when(conflictEvaluationService.evaluateRequest(any()))
        .thenReturn(conflictManagerEvaluatedResponse);
  }
}
