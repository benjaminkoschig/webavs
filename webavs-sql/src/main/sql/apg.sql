-- ENSAVS-S210924_008-SPE-APG-uniformisation statuts dossiers
-- Suppression des code système pour l'état de droit APG Enregistré
-- A exécuter pour toutes les caisses.
delete from schema.FWCOUP where PCOSID = (select PCOSID from schema.FWCOSP where PPTYGR like 'APETADROIT' AND PCONCS = 9);
delete from schema.FWCOSP where PPTYGR like 'APETADROIT' AND PCONCS = 9;