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