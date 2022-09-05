---------------------------------------------------------------
-----   ORION.SQL
---------------------------------------------------------------

-- activation des contr�les additionels sur les PUCS lorsque la validation est lanc� par le biais du batch
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('orion.pucs.batch.validations.activer','false',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- liste d'assurances � contr�ler lors de la validation des PUCS par le biais du batch
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('orion.pucs.batch.validations.assurances','',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');