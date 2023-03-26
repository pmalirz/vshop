-- The script meant to be run bt the VSHOP schema owner
-- Oracle JPA
CREATE TABLE PRODUCT_JPA (
    id          VARCHAR2(255),
    code        VARCHAR2(100),
    name        VARCHAR2(255),
    description VARCHAR2(1000),
    price       NUMBER(10,2),
    quantity       NUMBER(10,0),
    PRIMARY KEY (id)
)
/
-- Oracle JSON
CREATE TABLE PRODUCT_JSON (
    DOC        JSON
)
/
ALTER TABLE PRODUCT_JSON ADD (id VARCHAR2(255)
    GENERATED ALWAYS AS (json_value(DOC, '$.id' RETURNING VARCHAR2(255))))
/
CREATE INDEX PRODUCT_JSON_ID_IDX on VSHOP.PRODUCT_JSON(id)
/

-- Oracle SODA
DECLARE
    collection  SODA_COLLECTION_T;
BEGIN
    collection := DBMS_SODA.open_collection('PRODUCT_SODA');
    IF collection IS NULL THEN
        collection := DBMS_SODA.create_collection('PRODUCT_SODA');
    END IF;
END;
/

