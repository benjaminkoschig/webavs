-- D0136 - Ajout du module permettant de creer la liste des ind�pendants AF
UPDATE SCHEMA.FWINCP SET PINCVA = (SELECT MAX(IDMODFAC)+1 FROM CCVDQUA.FAMODUP) WHERE FWINCP.PINCID = 'FAMODUP';

INSERT INTO SCHEMA.FAMODUP (IDMODFAC,LIBELLEFR,LIBELLEDE,LIBELLEIT,NOMCLASSE,IDTYPEMODULE,NIVEAUAPPEL,MODIFIERAFACT,PSPY) 
VALUES ((SELECT MAX(IDMODFAC)+1 FROM SCHEMA.FAMODUP),'Liste des ind�pendants avec allocations familliales - Revenu minimal non atteint','Liste der Selbstst�ndigerwerbenden mit Familienzulagen - Nicht erreichtes Mindesteinkommen','[IT]Liste des ind�pendants avec allocations familliales - Revenu minimal non atteint','ch.globaz.al.liste.ALIndeRevenuMinNonAtteintImpl',905002,90,'2','20170101201604globazf');