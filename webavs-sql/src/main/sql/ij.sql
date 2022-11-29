---------------------------------------------------------------
-----   IJ.SQL
---------------------------------------------------------------

-- Ajout de la propriété d'affichage d'acor v3
INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY)
VALUES ('ij.acor.utiliser.version.poste.utilisateur', 'false', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');