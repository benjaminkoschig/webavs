---------------------------------------------------------------
-----   ORION.SQL
---------------------------------------------------------------

-- liste des id faillite dans affiliations à filter pour le process d'import fichier pucs
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('orion.import.motifdefin','', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
-- liste des id personnalite juridique dans affiliations à filter pour le process d'import fichier pucs
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('orion.import.personnalitejuridique','', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');