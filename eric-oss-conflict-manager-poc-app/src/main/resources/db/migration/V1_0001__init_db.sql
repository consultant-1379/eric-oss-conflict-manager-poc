--------------------------------------------------------------------------------------------------------------------------------------------------------------
-- COPYRIGHT Ericsson 2024
--
--
--
-- The copyright to the computer program(s) herein is the property of
--
-- Ericsson Inc. The programs may be used and/or copied only with written
--
-- permission from Ericsson Inc. or in accordance with the terms and
--
-- conditions stipulated in the agreement/contract under which the
--
-- program(s) have been supplied.
------------------------------------------------------------------------------------------------------------------------------------------------------------/

BEGIN TRANSACTION;

    CREATE SCHEMA IF NOT EXISTS conflict_schema;

    CREATE TABLE IF NOT EXISTS conflict_schema.conflict_evaluation_request(
        id UUID PRIMARY KEY,
        cm_handle_id VARCHAR(50) NOT NULL,
        target_node_fdn VARCHAR(255) NOT NULL,
        resource_id VARCHAR(255) NOT NULL,
        change_request JSONB NOT NULL,
        decision int2,
        received_timestamp TIMESTAMP NOT NULL DEFAULT NOW()
    );


    CREATE TABLE conflict_schema.conflict_decision(
        id UUID PRIMARY KEY,
        reason JSONB NOT NULL,
        request_id UUID,
        CONSTRAINT fk_request FOREIGN KEY (request_id) REFERENCES conflict_schema.conflict_evaluation_request(id)
    );


COMMIT;