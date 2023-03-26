-- The script meant to be run bt the VSHOP schema owner

-- JPA
DROP TABLE PRODUCT_JPA CASCADE CONSTRAINTS PURGE
/

-- Oracle JSON
DROP TABLE PRODUCT_JSON CASCADE CONSTRAINTS PURGE
/

-- Oracle SODA
DECLARE
    retVal NUMBER;
    collection  SODA_COLLECTION_T;
BEGIN
    collection := DBMS_SODA.open_collection('PRODUCT_SODA');
    IF collection IS NOT NULL THEN
        retVal := DBMS_SODA.drop_collection('PRODUCT_SODA');
    END IF;
END;
/

