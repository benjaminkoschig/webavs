--===============================================================================================================
------------------------------------------ UNIQUEMENT POUR LA ***FPV*** ----------------------------------------
--===============================================================================================================

--S161216_003 - FPV - IJ - Uniformisation des ent�tes des documents
UPDATE SCHEMA.CTTEXTES SET CDIELE=14000000016,CDITXT=14000000046,CDLCIL='de',CDLDES='Route du Lac 2, 1094 Paudex',PSPY='' WHERE CDITXT=14000000046;
UPDATE SCHEMA.CTTEXTES SET CDIELE=14000000016,CDITXT=14000000047,CDLCIL='fr',CDLDES='Route du Lac 2, 1094 Paudex',PSPY='' WHERE CDITXT=14000000047;
UPDATE SCHEMA.CTTEXTES SET CDIELE=14000000016,CDITXT=14000000048,CDLCIL='it',CDLDES='Route du Lac 2, 1094 Paudex',PSPY='' WHERE CDITXT=14000000048;

UPDATE SCHEMA.CTTEXTES SET CDIELE=14000000017,CDITXT=14000000049,CDLCIL='de',CDLDES='&nbsp;Kontakt-Person: {Titre} {NomPrenom} Tel. {NoTelephone}',PSPY='' WHERE CDITXT=14000000049;
UPDATE SCHEMA.CTTEXTES SET CDIELE=14000000017,CDITXT=14000000050,CDLCIL='fr',CDLDES='&nbsp;Personne de r&eacute;f&eacute;rence : {Titre} {NomPrenom} T&eacute;l. {NoTelephone}',PSPY='' WHERE CDITXT=14000000050;
UPDATE SCHEMA.CTTEXTES SET CDIELE=14000000017,CDITXT=14000000051,CDLCIL='it',CDLDES='&nbsp;[IT]Personne de r&eacute;f&eacute;rence : {Titre} {NomPrenom}&nbsp;T&eacute;l. {NoTelephone}',PSPY='' WHERE CDITXT=14000000051;

INSERT INTO SCHEMA.CTELEMEN (CCBDES,CCBEDI,CCBSBD,CCBSEL,CCIDOC,CCIELE,CCNNIV,CCNPOS,PSPY) VALUES ('','2','2','2',83000000001,83000000019,1,2,'');
INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (83000000019,83000000054,'it','Incarto trattato da :&nbsp;','');
INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (83000000019,83000000053,'fr','Dossier trait&eacute; par :&nbsp;','');
INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (83000000019,83000000052,'de','Dossier bearbeitet von :&nbsp;','');

--===============================================================================================================
--===============================================================================================================
--===============================================================================================================
