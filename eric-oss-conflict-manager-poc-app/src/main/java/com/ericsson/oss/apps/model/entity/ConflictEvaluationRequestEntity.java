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
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "conflict_evaluation_request", schema = ConflictEvaluationConstants.CONFLICT_SCHEMA)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConflictEvaluationRequestEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(unique = true, nullable = false)
  private UUID id;

  @Column(name = "cm_handle_id")
  private String cmHandleId;

  @Column(name = "resource_id")
  private String resourceIdentifier;

  @Column(name = "change_request", columnDefinition = "jsonb")
  @Convert(converter = ConflictAttributeConverter.class)
  private String changeRequest;

  @Column(name = "target_node_fdn")
  private String managedElement;

  @CreationTimestamp
  @Column(name = "received_timestamp")
  private LocalDateTime requestReceivedTime;

  @Column(name = "decision")
  private Integer decisionId;

  @OneToOne(
      mappedBy = "conflictEvaluationRequestEntity",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY)
  private ConflictDecisionEntity conflictDecisionEntity;
}
