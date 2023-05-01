-- The script meant to be run bt the VSHOP schema owner

-- JPA
CREATE TABLE PRODUCT_JPA (
    id          VARCHAR2(255),
    code        VARCHAR2(100),
    name        VARCHAR2(255),
    description VARCHAR2(1000),
    price       NUMBER(10,4),
    quantity    NUMBER(10,0),
    revision    NUMBER(10,0),
    PRIMARY KEY (id)
)
/
CREATE INDEX PRODUCT_JPA_ID_IDX on VSHOP.PRODUCT_JPA(ID)
/
-- Oracle JSON
CREATE TABLE PRODUCT_JSON (
    DOC        JSON,
    ID AS (json_value(DOC, '$.id' RETURNING VARCHAR2(255)))
)
/
CREATE INDEX PRODUCT_JSON_ID_IDX on VSHOP.PRODUCT_JSON(ID)
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

