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
package com.ericsson.oss.apps.config;

import com.ericsson.oss.apps.decision.engine.DecisionEngine;
import com.ericsson.oss.apps.decision.service.DecisionHandler;
import com.ericsson.oss.apps.decision.service.DecisionHandlerImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConflictManagerConfiguration {

  @Bean
  public DecisionHandler decisionHandler(DecisionEngine decisionEngine) {

    return new DecisionHandlerImpl(decisionEngine);
  }

  @Bean
  public ModelMapper modelMapper() {

    return new ModelMapper();
  }
}
