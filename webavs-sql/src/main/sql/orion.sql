---------------------------------------------------------------
-----   ORION.SQL
---------------------------------------------------------------

-- liste des déclaration de salaire dans affiliations à filter pour le process d'import fichier pucs
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('orion.import.declarationsalaire','', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

-- ENSAVS - S220322_006 ENSAVS - ORION - Activer/Désactiver la validation automatique des Déclarations après l'import batch
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('orion.pucs.validation.declaration.validationDs.batch','false',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');