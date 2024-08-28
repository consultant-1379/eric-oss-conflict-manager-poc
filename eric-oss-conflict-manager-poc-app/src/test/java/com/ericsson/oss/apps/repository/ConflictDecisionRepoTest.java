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
package com.ericsson.oss.apps.repository;

import com.ericsson.oss.apps.TestUtils;
import com.ericsson.oss.apps.model.entity.ConflictDecisionEntity;
import com.ericsson.oss.apps.model.entity.ConflictEvaluationRequestEntity;
import com.ericsson.oss.apps.repositories.ConflictDecisionRepo;
import com.ericsson.oss.apps.repositories.ConflictEvaluationRequestRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ConflictDecisionRepoTest {

  @Autowired private ConflictEvaluationRequestRepo conflictEvaluationRepoTest;

  @Autowired private ConflictDecisionRepo conflictDecisionRepoTest;

  @Test
  @Transactional
  public void testConflictDecisionIsPersisted() {
    final ConflictEvaluationRequestEntity entity =
        conflictEvaluationRepoTest.save(TestUtils.populateConflictEvaluateEntity());
    ConflictDecisionEntity conflictDecisionEntity =
        ConflictDecisionEntity.builder()
            .reason(
                "{\n"
                    + "   \"NRCellDU\":[\n"
                    + "      {\n"
                    + "         \"id\":\"NRCellDU-id\",\n"
                    + "         \"attributes\":{\n"
                    + "            \"administrativeState\": \"UNLOCKED\"\n"
                    + "         }\n"
                    + "      }")
            .conflictEvaluationRequestEntity(entity)
            .build();

    final ConflictDecisionEntity conflictDecision =
        conflictDecisionRepoTest.save(conflictDecisionEntity);

    Assertions.assertThat(conflictDecision.getId()).isNotNull();
  }
}
