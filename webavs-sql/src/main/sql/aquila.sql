---------------------------------------------------------------
-----   AQUILA.SQL
---------------------------------------------------------------

-- eBill propriétés systèmes d'activation par module
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('aquila.eBill.activer','false',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
reorg table SCHEMA.JADEPROP;
-- call sysproc.admin_cmd('REORG TABLE JADEPROP');