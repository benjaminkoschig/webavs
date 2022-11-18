---------------------------------------------------------------
-----   ORION.SQL
---------------------------------------------------------------

-- liste des id faillite dans affiliations � filter pour le process d'import fichier pucs
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('orion.import.motifdefin','', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
-- liste des id personnalite juridique dans affiliations � filter pour le process d'import fichier pucs
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('orion.import.personnalitejuridique','', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

-- ENSAVS - S220322_006 ENSAVS - ORION - V�rifier l'ann�e de cotisation
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('orion.pucs.validation.declaration.annee','',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');