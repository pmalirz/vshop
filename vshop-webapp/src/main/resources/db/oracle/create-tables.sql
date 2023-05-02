-- The script meant to be run bt the VSHOP schema owner

-- JPA
CREATE TABLE PRODUCT_JPA (
    ID          VARCHAR2(255),
    CODE        VARCHAR2(100),
    NAME        VARCHAR2(255),
    DESCRIPTION VARCHAR2(1000),
    PRICE       NUMBER(10,4),
    QUANTITY    NUMBER(10,0),
    REVISION    NUMBER(10,0),
    PRIMARY KEY (ID)
)
/
-- Oracle JSON
CREATE TABLE PRODUCT_JSON (
    DOC        JSON,
    ID AS (json_value(DOC, '$.id' RETURNING VARCHAR2(255))),
    PRIMARY KEY (ID)
)
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
CREATE SEARCH INDEX PRODUCT_SODA_DOC_TXT_IDX ON PRODUCT_SODA(JSON_DOCUMENT) FOR JSON
/


