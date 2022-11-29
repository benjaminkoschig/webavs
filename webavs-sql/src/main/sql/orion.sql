---------------------------------------------------------------
-----   ORION.SQL
---------------------------------------------------------------

-- ENSAVS - S220322_006 ENSAVS - ORION - Activer/Désactiver la validation automatique des Déclarations après l'import batch
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('orion.pucs.validation.declaration.validationDs.batch','false',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');