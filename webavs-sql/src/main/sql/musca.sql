---------------------------------------------------------------
-----   MUSCA.SQL
---------------------------------------------------------------

-- FIELD_EBILL_PRINTED = "EBILLPRINTED";
ALTER TABLE SCHEMA.FAENTFP ADD COLUMN EBILLPRINTED VARCHAR(1);
reorg table SCHEMA.FAENTFP;
-- call sysproc.admin_cmd('REORG TABLE FAENTFP);