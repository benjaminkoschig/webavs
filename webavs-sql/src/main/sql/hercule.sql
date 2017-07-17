--D0221 : Suivi "Non certifi�s conformes"
--D�finition du code system d'un nouveau suivi "Non certifi�s conformes"
INSERT into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values ( 6700014, 'LECATJOUR', 1 ,2,0,0, 'Suivi Non certifi�s conformes', 2,2,2,2,2, 2 , 16000007 ,0,'20170613Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6700014, 'F', '1', 'Suivi Non certifi�s conformes' ,'20170613Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6700014, 'D', '1', 'Verlaufs Nicht beglaubigte' ,'20170613Globaz'); 

--D�finition du code systeme de l'�tape
INSERT INTO SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) VALUES ( 6200079, 'LEDEFFORM', 1 ,2,0,0, 'Suivi_NCC', 2,2,2,2,2, 2 , 16000002 ,0,'20170613Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6200079, 'F', '1', 'Suivi Non certifi�s conformes - D�but du suivi' ,'20170613Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6200079, 'D', '1', 'Verlaufs Nicht beglaubigte - Verfolgungsbeginn' ,'20170613Globaz'); 

--D�finition de la formule
INSERT INTO SCHEMA.ENPPDEF (PDEFID, PDEFDO, PSPY) VALUES ((SELECT MAX(PDEFID)+1 FROM SCHEMA.ENPPDEF), 6200079, '20170613Globaz   ');

--Prori�t�s de la formule
INSERT INTO SCHEMA.ENPPFO1 (PFO1ID, PDEFID, PFO1LA, PFO1RV, PFO1IG, PFO1SI, PFO1SA, PFO1TY, PFO1FC, PFO1DG, PFO1NC, PSPY, PFO1LD) VALUES ((SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200079), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200079), 6100002, 6300002,  6300002, 0, 6300002, 6400002, 6300002, 0, 0, '20170613Globaz   ', '');

--Propri�t�s de la formule
INSERT INTO SCHEMA.ENPPFO2 (PFO2ID, PFO1ID, PFO2FO, PFO2VA, PSPY) VALUES ((SELECT max(PFO2ID)+1 FROM SCHEMA.ENPPFO2), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200079), 16000013, 6300001, '20170613Globaz   ');
INSERT INTO SCHEMA.ENPPFO2 (PFO2ID, PFO1ID, PFO2FO, PFO2VA, PSPY) VALUES ((SELECT max(PFO2ID)+1 FROM SCHEMA.ENPPFO2), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200079), 16000007, 6700014, '20170613Globaz   ');

--Nouveau type de contr�le
INSERT INTO SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) VALUES ( 811014, 'VEGENRECON', 1 ,2,0,0, 'Contr�le non cert. conforme', 2,2,2,2,2, 2 , 10800011 ,0,'20170701Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 811014, 'D', 'NCC', 'Kontrolle Nicht beglaubigte' ,'20170701Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 811014, 'F', 'NCC', 'Contr�le non cert. conforme' ,'20170701Globaz'); 