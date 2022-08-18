---------------------------------------------------------------
-----   EFORM.SQL
---------------------------------------------------------------

-- eBill contentieux eBillPrintable
ALTER TABLE SCHEMA.GF_FORMULAIRE ADD COLUMN BUSINESS_PROCESS_ID VARCHAR(255);

-- call sysproc.admin_cmd('REORG TABLE GF_FORMULAIRE');
reorg table SCHEMA.GF_FORMULAIRE;