---------------------------------------------------------------
-----   CORVUS.SQL
---------------------------------------------------------------

-- Ajout revenu annuel d�terminant pour les IS Prestations.
ALTER TABLE SCHEMA.RERETEN ADD REVENU_ANNUEL_DETERMINANT DECIMAL(15,2);
reorg table SCHEMA.RERETEN;
-- CALL sysproc.admin_cmd('REORG TABLE SCHEMA.RERETEN');

-- Mise � jour libell� des codes system pour les quotit�s
update SCHEMA.FWCOUP set PCOLUT = 'Rente d''invalidit� de {quotite} d''une rente enti�re' where PCOSID = 52821500 and PLAIDE = 'F';
update SCHEMA.FWCOUP set PCOLUT = 'Invalidenrente von {quotite} einer ganzen Rente' where PCOSID = 52821500 and PLAIDE = 'D';
update SCHEMA.FWCOUP set PCOLUT = 'Rendita d''invalidit� di {quotite} di una rendita intera' where PCOSID = 52821500 and PLAIDE = 'I';

update SCHEMA.FWCOUP set PCOLUT = 'Rente extraordinaire d''invalidit� de {quotite}' where PCOSID = 52821700 and PLAIDE = 'F';
update SCHEMA.FWCOUP set PCOLUT = 'Ausserordentliche Invalidenrente von {quotite}' where PCOSID = 52821700 and PLAIDE = 'D';
update SCHEMA.FWCOUP set PCOLUT = 'Rendita straordinaria d''invalidit� di {quotite}' where PCOSID = 52821700 and PLAIDE = 'I';