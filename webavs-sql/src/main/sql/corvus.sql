---------------------------------------------------------------
-----   CORVUS.SQL
---------------------------------------------------------------

-- Ajout revenu annuel déterminant pour les IS Prestations.
ALTER TABLE SCHEMA.RERETEN ADD REVENU_ANNUEL_DETERMINANT DECIMAL(15,2);
reorg table SCHEMA.RERETEN;
-- CALL sysproc.admin_cmd('REORG TABLE SCHEMA.RERETEN');

-- TRAX Adapatations de rentes
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('corvus.mail.erreur.adaptation.rentes','',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('corvus.centrale.url.adaptation.rentes','',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');