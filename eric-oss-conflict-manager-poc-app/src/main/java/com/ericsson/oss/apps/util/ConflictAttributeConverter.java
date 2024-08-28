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
package com.ericsson.oss.apps.util;

import static com.ericsson.oss.apps.util.ConflictError.CONFLICT_EVALUATION_EXCEPTION_ATTRIBUTE_CONVERTER_MESSAGE;

import com.ericsson.oss.apps.exception.ConflictManagerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Converter
@Slf4j
public class ConflictAttributeConverter implements AttributeConverter<String, String> {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(final String jsonBObject) {
    try {
      return objectMapper.writeValueAsString(jsonBObject);
    } catch (JsonProcessingException jpe) {
      log.error("Can not convert entity object into JSONB ", jpe.getMessage());
      throw new ConflictManagerException(
          CONFLICT_EVALUATION_EXCEPTION_ATTRIBUTE_CONVERTER_MESSAGE, jpe.getMessage());
    }
  }

  @Override
  public String convertToEntityAttribute(final String value) {
    try {
      return objectMapper.readValue(value, String.class);
    } catch (JsonProcessingException jpe) {
      log.error("Cannot convert JSON into String ", jpe.getMessage());
      throw new ConflictManagerException(
          CONFLICT_EVALUATION_EXCEPTION_ATTRIBUTE_CONVERTER_MESSAGE, jpe.getMessage());
    }
  }
}
