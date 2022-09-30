---------------------------------------------------------------
-----   OSIRIS.SQL
---------------------------------------------------------------

-- eBill sursis plan de recouvrement eBillPrintable
ALTER TABLE SCHEMA.CAPLARP ADD COLUMN EBILLPRINTABLE VARCHAR(1);
reorg table SCHEMA.CAPLARP;
-- call sysproc.admin_cmd('REORG TABLE CAPLARP');
