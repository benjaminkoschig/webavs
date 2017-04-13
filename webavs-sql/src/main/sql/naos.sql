-- Ajout de proprietes pour le processus REE
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.header.name','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.header.email','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.header.phone','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.header.departement','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.header.other','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.process.paquet','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.process.validation','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.message.header.recipient.id','');

-- D0196 : Ajout de propti�t�s pour changer le comportement des plan d'affilaition inactif
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ide.planAffiliation.verificationInactif','false');

-- D0197 : Ajout de propti�t�s pour changer le comportement des cotisations et prendre en compte ou non les affili�s "p�res" si le "fils" n'a pas de coti' AVS
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ide.cotisation.verificationTaxeSous','false');

--D0204 : Am�lioration controle LPP
--D�finition du code system d'un nouveau suivi annuel LPP
INSERT into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values ( 6700013, 'LECATJOUR', 1 ,2,0,0, 'Suivi annuel LPP', 2,2,2,2,2, 2 , 16000007 ,0,'20170418Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6700013, 'F', '1', 'Suivi annuel LPP' ,'20170418Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6700013, 'D', '1', '?Suivi annuel LPP' ,'20170418Globaz'); 

--D�finition du code systeme des �tapes
INSERT INTO SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) VALUES ( 6200074, 'LEDEFFORM', 1 ,2,0,0, 'Suivi_annuel_lpp', 2,2,2,2,2, 2 , 16000002 ,0,'20170418Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6200074, 'F', '1', 'Suivi annuel LPP - D�but du suivi' ,'20170418Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6200074, 'D', '1', 'Suivi annuel LPP - D�but du suivi' ,'20170418Globaz'); 

INSERT INTO SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) VALUES ( 6200075, 'LEDEFFORM', 1 ,2,0,0, 'questionnaire_annuel_lpp', 2,2,2,2,2, 2 , 16000002 ,0,'20170418Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6200075, 'F', '1', 'Suivi annuel LPP - Impression questionnaire' ,'20170418Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6200075, 'D', '1', 'Suivi annuel LPP - Impression questionnaire' ,'20170418Globaz'); 

INSERT INTO SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) VALUES ( 6200076, 'LEDEFFORM', 1 ,2,0,0, 'rappel_annuel_lpp', 2,2,2,2,2, 2 , 16000002 ,0,'20170418Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6200076, 'F', '1', 'Suivi annuel LPP - Impression rappel' ,'20170418Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6200076, 'D', '1', 'Suivi annuel LPP - Impression rappel' ,'20170418Globaz'); 

INSERT INTO SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) VALUES ( 6200077, 'LEDEFFORM', 1 ,2,0,0, 'sommation_annuel_lpp', 2,2,2,2,2, 2 , 16000002 ,0,'20170418Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6200077, 'F', '1', 'Suivi annuel LPP - Impression sommation' ,'20170418Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6200077, 'D', '1', 'Suivi annuel LPP - Impression sommation' ,'20170418Globaz'); 

INSERT INTO SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) VALUES ( 6200078, 'LEDEFFORM', 1 ,2,0,0, 'denonciation_annuel_lpp', 2,2,2,2,2, 2 , 16000002 ,0,'20170418Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6200078, 'F', '1', 'Suivi annuel LPP - Pr�paration d�nonciation' ,'20170418Globaz'); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) VALUES ( 6200078, 'D', '1', 'Suivi annuel LPP - Pr�paration d�nonciation' ,'20170418Globaz'); 

--D�finition des formules
INSERT INTO SCHEMA.ENPPDEF (PDEFID, PDEFDO, PSPY) VALUES ((SELECT MAX(PDEFID)+1 FROM SCHEMA.ENPPDEF), 6200074, '20170418Globaz   ');
INSERT INTO SCHEMA.ENPPDEF (PDEFID, PDEFDO, PSPY) VALUES ((SELECT MAX(PDEFID)+1 FROM SCHEMA.ENPPDEF), 6200075, '20170418Globaz   ');
INSERT INTO SCHEMA.ENPPDEF (PDEFID, PDEFDO, PSPY) VALUES ((SELECT MAX(PDEFID)+1 FROM SCHEMA.ENPPDEF), 6200076, '20170418Globaz   ');
INSERT INTO SCHEMA.ENPPDEF (PDEFID, PDEFDO, PSPY) VALUES ((SELECT MAX(PDEFID)+1 FROM SCHEMA.ENPPDEF), 6200077, '20170418Globaz   ');
INSERT INTO SCHEMA.ENPPDEF (PDEFID, PDEFDO, PSPY) VALUES ((SELECT MAX(PDEFID)+1 FROM SCHEMA.ENPPDEF), 6200078, '20170418Globaz   ');

--Prori�t�s des formules
INSERT INTO SCHEMA.ENPPFO1 (PFO1ID, PDEFID, PFO1LA, PFO1RV, PFO1IG, PFO1SI, PFO1SA, PFO1TY, PFO1FC, PFO1DG, PFO1NC, PSPY, PFO1LD) VALUES ((SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200074), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200074), 6100002, 6300002,  6300002, 0, 6300002, 6400002, 6300002, 0, 0, '20170418Globaz   ', '');
INSERT INTO SCHEMA.ENPPFO1 (PFO1ID, PDEFID, PFO1LA, PFO1RV, PFO1IG, PFO1SI, PFO1SA, PFO1TY, PFO1FC, PFO1DG, PFO1NC, PSPY, PFO1LD) VALUES ((SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200075), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200075), 6100002, 6300002,  6300002, 0, 6300002, 6400002, 6300002, 0, 0, '20170418Globaz   ', '');
INSERT INTO SCHEMA.ENPPFO1 (PFO1ID, PDEFID, PFO1LA, PFO1RV, PFO1IG, PFO1SI, PFO1SA, PFO1TY, PFO1FC, PFO1DG, PFO1NC, PSPY, PFO1LD) VALUES ((SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200076), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200076), 6100002, 6300002,  6300002, 0, 6300002, 6400002, 6300002, 0, 0, '20170418Globaz   ', '');
INSERT INTO SCHEMA.ENPPFO1 (PFO1ID, PDEFID, PFO1LA, PFO1RV, PFO1IG, PFO1SI, PFO1SA, PFO1TY, PFO1FC, PFO1DG, PFO1NC, PSPY, PFO1LD) VALUES ((SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200077), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200077), 6100002, 6300002,  6300002, 0, 6300002, 6400002, 6300002, 0, 0, '20170418Globaz   ', '');
INSERT INTO SCHEMA.ENPPFO1 (PFO1ID, PDEFID, PFO1LA, PFO1RV, PFO1IG, PFO1SI, PFO1SA, PFO1TY, PFO1FC, PFO1DG, PFO1NC, PSPY, PFO1LD) VALUES ((SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200078), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200078), 6100002, 6300002,  6300002, 0, 6300002, 6400002, 6300002, 0, 0, '20170418Globaz   ', '');

--Propri�t�s des formules
INSERT INTO SCHEMA.ENPPFO2 (PFO2ID, PFO1ID, PFO2FO, PFO2VA, PSPY) VALUES ((SELECT max(PFO2ID)+1 FROM SCHEMA.ENPPFO2), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200074), 16000013, 6300001, '20170418Globaz   ');
INSERT INTO SCHEMA.ENPPFO2 (PFO2ID, PFO1ID, PFO2FO, PFO2VA, PSPY) VALUES ((SELECT max(PFO2ID)+1 FROM SCHEMA.ENPPFO2), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200074), 16000007, 6700013, '20170418Globaz   ');
INSERT INTO SCHEMA.ENPPFO2 (PFO2ID, PFO1ID, PFO2FO, PFO2VA, PSPY) VALUES ((SELECT max(PFO2ID)+1 FROM SCHEMA.ENPPFO2), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200075), 16000013, 6300002, '20170418Globaz   ');
INSERT INTO SCHEMA.ENPPFO2 (PFO2ID, PFO1ID, PFO2FO, PFO2VA, PSPY) VALUES ((SELECT max(PFO2ID)+1 FROM SCHEMA.ENPPFO2), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200075), 16000007, 6700013, '20170418Globaz   ');
INSERT INTO SCHEMA.ENPPFO2 (PFO2ID, PFO1ID, PFO2FO, PFO2VA, PSPY) VALUES ((SELECT max(PFO2ID)+1 FROM SCHEMA.ENPPFO2), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200076), 16000013, 6300002, '20170418Globaz   ');
INSERT INTO SCHEMA.ENPPFO2 (PFO2ID, PFO1ID, PFO2FO, PFO2VA, PSPY) VALUES ((SELECT max(PFO2ID)+1 FROM SCHEMA.ENPPFO2), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200076), 16000007, 6700013, '20170418Globaz   ');
INSERT INTO SCHEMA.ENPPFO2 (PFO2ID, PFO1ID, PFO2FO, PFO2VA, PSPY) VALUES ((SELECT max(PFO2ID)+1 FROM SCHEMA.ENPPFO2), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200077), 16000013, 6300002, '20170418Globaz   ');
INSERT INTO SCHEMA.ENPPFO2 (PFO2ID, PFO1ID, PFO2FO, PFO2VA, PSPY) VALUES ((SELECT max(PFO2ID)+1 FROM SCHEMA.ENPPFO2), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200077), 16000007, 6700013, '20170418Globaz   ');
INSERT INTO SCHEMA.ENPPFO2 (PFO2ID, PFO1ID, PFO2FO, PFO2VA, PSPY) VALUES ((SELECT max(PFO2ID)+1 FROM SCHEMA.ENPPFO2), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200078), 16000013, 6300002, '20170418Globaz   ');
INSERT INTO SCHEMA.ENPPFO2 (PFO2ID, PFO1ID, PFO2FO, PFO2VA, PSPY) VALUES ((SELECT max(PFO2ID)+1 FROM SCHEMA.ENPPFO2), (SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200078), 16000007, 6700013, '20170418Globaz   ');

--Implementation de la class
INSERT INTO SCHEMA.ENPPPDF (PFO1ID,PPDFNC,PSPY) VALUES ((SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200075),'globaz.naos.itext.suiviLPP.AFQuestionnaireLPP_Doc','20170418Globaz');
INSERT INTO SCHEMA.ENPPPDF (PFO1ID,PPDFNC,PSPY) VALUES ((SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200076),'globaz.naos.itext.suiviLPP.AFRappelLPP_Doc','20170418Globaz');
INSERT INTO SCHEMA.ENPPPDF (PFO1ID,PPDFNC,PSPY) VALUES ((SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200077),'globaz.naos.itext.suiviLPP.AFSommationLPP_Doc','20170418Globaz');

--Lien entre �tapes
INSERT INTO SCHEMA.ENPPRAP (PRAPID,PFO1ID,PDEFID,PRAPTA,PRAPUN,PSPY) VALUES ((SELECT max(PRAPID)+1 FROM SCHEMA.ENPPRAP),(SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200074),(SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200075),0,32000043,'20170418Globaz');
INSERT INTO SCHEMA.ENPPRAP (PRAPID,PFO1ID,PDEFID,PRAPTA,PRAPUN,PSPY) VALUES ((SELECT max(PRAPID)+1 FROM SCHEMA.ENPPRAP),(SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200075),(SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200076),60,32000043,'20170418Globaz');
INSERT INTO SCHEMA.ENPPRAP (PRAPID,PFO1ID,PDEFID,PRAPTA,PRAPUN,PSPY) VALUES ((SELECT max(PRAPID)+1 FROM SCHEMA.ENPPRAP),(SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200076),(SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200077),20,32000043,'20170418Globaz');
INSERT INTO SCHEMA.ENPPRAP (PRAPID,PFO1ID,PDEFID,PRAPTA,PRAPUN,PSPY) VALUES ((SELECT max(PRAPID)+1 FROM SCHEMA.ENPPRAP),(SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200077),(SELECT PDEFID FROM SCHEMA.ENPPDEF WHERE PDEFDO = 6200078),20,32000043,'20170418Globaz');
