---------------------------------------------------------------
-----   ORION.SQL
---------------------------------------------------------------

-- liste des id faillite dans affiliations à filter pour le process d'import fichier pucs
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('orion.batch.import.idfaillite','', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');