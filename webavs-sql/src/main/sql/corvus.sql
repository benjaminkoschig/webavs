---------------------------------------------------------------
-----   CORVUS.SQL
---------------------------------------------------------------

-- Ajout revenu annuel déterminant pour les IS Prestations.
ALTER TABLE SCHEMA.RERETEN ADD REVENU_ANNUEL_DETERMINANT DECIMAL(15,2);
reorg table SCHEMA.RERETEN;
-- CALL sysproc.admin_cmd('REORG TABLE SCHEMA.RERETEN');

-- Mise à jour libellé des codes system pour les quotités
update SCHEMA.FWCOUP set PCOLUT = 'Rente d''invalidité de {quotite} d''une rente entière' where PCOSID = 52821500 and PLAIDE = 'F';
update SCHEMA.FWCOUP set PCOLUT = 'Invalidenrente von {quotite} einer ganzen Rente' where PCOSID = 52821500 and PLAIDE = 'D';
update SCHEMA.FWCOUP set PCOLUT = 'Rendita d''invalidità di {quotite} di una rendita intera' where PCOSID = 52821500 and PLAIDE = 'I';

update SCHEMA.FWCOUP set PCOLUT = 'Rente extraordinaire d''invalidité de {quotite}' where PCOSID = 52821700 and PLAIDE = 'F';
update SCHEMA.FWCOUP set PCOLUT = 'Ausserordentliche Invalidenrente von {quotite}' where PCOSID = 52821700 and PLAIDE = 'D';
update SCHEMA.FWCOUP set PCOLUT = 'Rendita straordinaria d''invalidità di {quotite}' where PCOSID = 52821700 and PLAIDE = 'I';

-- TRAX Adapatations de rentes
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('corvus.mail.erreur.adaptation.rentes','',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('corvus.centrale.url.adaptation.rentes','',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

ALTER TABLE SCHEMA.REANN53
    ADD COLUMN QUOTITE_ANNONCE_53 NUMERIC (5, 2);
REORG table SCHEMA.REANN53;
-- call SYSPROC.ADMIN_CMD('reorg table REANN53');

ALTER TABLE SCHEMA.REANN51
    ADD COLUMN QUOTITE_ANNONCE_51 NUMERIC (5, 2);
REORG table SCHEMA.REANN51;
-- call SYSPROC.ADMIN_CMD('reorg table REANN51');

ALTER TABLE SCHEMA.REFICHA
    ADD COLUMN DATE_TRAITEMENT VARCHAR (20);
REORG table SCHEMA.REFICHA;
-- call SYSPROC.ADMIN_CMD('reorg table REFICHA');
