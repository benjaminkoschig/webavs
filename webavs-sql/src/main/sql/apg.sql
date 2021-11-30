-- ENSAVS-S210924_008-SPE-APG-uniformisation statuts dossiers
-- Suppression des code système pour l'état de droit APG Enregistré
-- A exécuter pour toutes les caisses.
delete from schema.FWCOUP where PCOSID = (select PCOSID from schema.FWCOSP where PPTYGR like 'APETADROIT' AND PCONCS = 9);
delete from schema.FWCOSP where PPTYGR like 'APETADROIT' AND PCONCS = 9;

/* REMOVE STATUS "ENREGISTRE" (=> "EN ATTENTE") */
/*List the cases*/
SELECT * FROM SCHEMA.APDROIP WHERE VATETA = 52003009;
/*Update the status*/
UPDATE SCHEMA.APDROIP SET VATETA = 52003001 WHERE VATETA = 52003009;

/* CORRECT "EN ATTENTE" -> "VALIDE" if has been calculated */
/*List the cases*/
SELECT * FROM SCHEMA.APDROIP WHERE VATETA = 52003001 AND VAIDRO IN (SELECT VHIDRO FROM SCHEMA.APPRESP) AND (
            VATGSE = 52001012 OR
            VATGSE = 52001017 OR
            VATGSE = 52001001 OR
            VATGSE = 52001013 OR
            VATGSE = 52001014 OR
            VATGSE = 52001006 OR
            VATGSE = 52001004 OR
            VATGSE = 52001010 OR
            VATGSE = 52001015 OR
            VATGSE = 52001016 OR
            VATGSE = 52001009 OR
            VATGSE = 52001002 OR
            VATGSE = 52001005 OR
            VATGSE = 52001056
    );
/*Update the status*/
UPDATE SCHEMA.APDROIP SET VATETA = 52003007 WHERE VATETA = 52003001 AND VAIDRO IN (SELECT VHIDRO FROM SCHEMA.APPRESP) AND (
            VATGSE = 52001012 OR
            VATGSE = 52001017 OR
            VATGSE = 52001001 OR
            VATGSE = 52001013 OR
            VATGSE = 52001014 OR
            VATGSE = 52001006 OR
            VATGSE = 52001004 OR
            VATGSE = 52001010 OR
            VATGSE = 52001015 OR
            VATGSE = 52001016 OR
            VATGSE = 52001009 OR
            VATGSE = 52001002 OR
            VATGSE = 52001005 OR
            VATGSE = 52001056
    );