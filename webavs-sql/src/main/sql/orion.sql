---------------------------------------------------------------
-----   ORION.SQL
---------------------------------------------------------------

-- liste des d�claration de salaire dans affiliations � filter pour le process d'import fichier pucs
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('orion.import.declarationsalaire','', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

-- ENSAVS - S220322_006 ENSAVS - ORION - Activer/D�sactiver la validation automatique des D�clarations apr�s l'import batch
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('orion.pucs.validation.declaration.validationDs.batch','false',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');