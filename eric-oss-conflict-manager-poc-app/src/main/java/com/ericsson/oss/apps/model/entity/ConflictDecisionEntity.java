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
package com.ericsson.oss.apps.model.entity;

import com.ericsson.oss.apps.util.ConflictAttributeConverter;
import com.ericsson.oss.apps.util.ConflictEvaluationConstants;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conflict_decision", schema = ConflictEvaluationConstants.CONFLICT_SCHEMA)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConflictDecisionEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(unique = true, nullable = false)
  private UUID id;

  @Column(name = "reason", columnDefinition = "jsonb")
  @Convert(converter = ConflictAttributeConverter.class)
  private String reason;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "request_id", referencedColumnName = "id")
  private ConflictEvaluationRequestEntity conflictEvaluationRequestEntity;
}
