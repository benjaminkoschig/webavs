-- ENSAVS-S210924_008-SPE-APG-uniformisation statuts dossiers
-- Suppression des code syst�me pour l'�tat de droit APG Enregistr�
-- A ex�cuter pour toutes les caisses.
delete from schema.FWCOUP where PCOSID = (select PCOSID from schema.FWCOSP where PPTYGR like 'APETADROIT' AND PCONCS = 9);
delete from schema.FWCOSP where PPTYGR like 'APETADROIT' AND PCONCS = 9;