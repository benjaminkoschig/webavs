---------------------------------------------------------------
-----   MUSCA.SQL
---------------------------------------------------------------

-- eBill propri�t�s syst�mes d'activation par module
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('musca.eBill.activer','false',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');