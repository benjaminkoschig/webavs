---------------------------------------------------------------
-----   DRACO.SQL
---------------------------------------------------------------

-- activation des contr�les additionels sur la validation de salaire
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('draco.validation.controles.supplementaires','false', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

-- liste d'assurances � contr�ler lors de la validation de d�claration de salaire
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('draco.validation.assurances','', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');