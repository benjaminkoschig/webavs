---------------------------------------------------------------
-----   AQUILA.SQL
---------------------------------------------------------------

-- eBill propriétés systèmes d'activation par module
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('aquila.eBill.activer','false',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- eBill contentieux eBillPrinted & eBillTransactionID
ALTER TABLE COHISTP ADD COLUMN EBILLPRINTED VARCHAR(1);
ALTER TABLE COHISTP ADD COLUMN EBILLTRANSACTIONID VARCHAR(50);
reorg table SCHEMA.COHISTP;
-- call sysproc.admin_cmd('REORG TABLE COHISTP');