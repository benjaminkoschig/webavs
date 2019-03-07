--S170313_002-FERCIAB-Impl�mentation du module APG
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('apg.assurance.complementCIAB.ju.id','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('apg.assurance.complementCIAB.be.id','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('apg.isFERCIAB','true');

delete from SCHEMA.FWCOSP where pcosid in(52001050, 52001051, 52001052, 52001053, 52001054, 52001055, 52001056);
delete from SCHEMA.FWCOUP where pcosid in(52001050, 52001051, 52001052, 52001053, 52001054, 52001055, 52001056);
insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52001050,'APGENSERVI',1,1,0,0,'DEMENAGEMENT',2,1,2,2,2,2,51000001,0, 'spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001050,'F','501','D�m�nagement','spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001050,'D','501','D�m�nagement','spy'); 
insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52001051,'APGENSERVI',1,1,0,0,'NAISSANCE',2,1,2,2,2,2,51000001,0, 'spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001051,'F','502','Naissance','spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001051,'D','502','Naissance','spy'); 
insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52001052,'APGENSERVI',1,1,0,0,'MARIAGE_LPART',2,1,2,2,2,2,51000001,0, 'spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001052,'F','503','Mariage/LPart','spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001052,'D','503','Mariage/LPart','spy'); 
insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52001053,'APGENSERVI',1,1,0,0,'DECES',2,1,2,2,2,2,51000001,0, 'spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001053,'F','504','D�c�s','spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001053,'D','504','D�c�s','spy'); 
insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52001054,'APGENSERVI',1,1,0,0,'JOURNEES_DIVERSES',2,1,2,2,2,2,51000001,0, 'spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001054,'F','505','Inspection Recrutement Lib�ration','spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001054,'D','505','Inspection Recrutement Lib�ration','spy'); 
insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52001055,'APGENSERVI',1,1,0,0,'CONGE_JEUNESSE',2,1,2,2,2,2,51000001,0, 'spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001055,'F','506','Cong� jeunesse','spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001055,'D','506','Cong� jeunesse','spy'); 
insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52001056,'APGENSERVI',1,1,0,0,'SERVICE_ETRANGER',2,1,2,2,2,2,51000001,0, 'spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001056,'F','507','Service �  l''�tranger','spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001056,'D','507','Service �  l''�tranger','spy'); 

delete from SCHEMA.FWCOSP where pcosid in(52015006, 52015007);
delete from SCHEMA.FWCOUP where pcosid in(52015006, 52015007);
insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52015006,'APGENRPRES',2,1,0,0,'Prestation compl�mentaire',2,1,2,2,2,2,51000015,0, 'spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52015006,'F','COMPCIAB','Prestation compl�mentaire','spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52015006,'D','COMPCIAB','Prestation compl�mentaire','spy'); 
insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52015007,'APGENRPRES',2,1,0,0,'Jours isol�s',2,1,2,2,2,2,51000015,0, 'spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52015007,'F','ISOLES','Jours isol�s','spy'); 
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52015007,'D','ISOLES','Jours isol�s','spy'); 

INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','COMCIABJUR',0,20100101,0.000000,1,0.000000,'',80.000000,'Montant maximum pour compl�ment CIAB JU recrue',0,0,'                        ');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','COMCIABJUA',0,20100101,0.000000,1,0.000000,'',400.000000,'Montant maximum pour compl�ment CIAB JU autres',0,0,'                        ');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','COMCIABBEA',0,20100101,0.000000,1,0.000000,'',400.000000,'Montant maximum pour compl�ment CIAB BE autres',0,0,'                        ');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','COMCIABBER',0,20100101,0.000000,1,0.000000,'',80.000000,'Montant maximum pour compl�ment CIAB BE recrue',0,0,'                        ');

INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','ISOLEDEMEN',0,20100101,0.000000,1,0.000000,'',1.000000,'Jours isol�s, d�m�nagement',0,0,'                        ');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','ISOLENAISS',0,20100101,0.000000,1,0.000000,'',1.000000,'Jours isol�s, naissance',0,0,'                        ');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','ISOLEMARIA',0,20100101,0.000000,1,0.000000,'',2.000000,'Jours isol�s, mariage/LPart',0,0,'                        ');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','ISOLEDECES',0,20100101,0.000000,1,0.000000,'',3.000000,'Jours isol�s, d�c�s',0,0,'                        ');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','ISOLEDIVER',0,20100101,0.000000,1,0.000000,'',1.000000,'Jours isol�s, Inspection Recrutement Lib�ration',0,0,'                        ');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','ISOLEJEUNE',0,20100101,0.000000,1,0.000000,'',5.000000,'Jours isol�s, cong� jeunesse',0,0,'                        ');

INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY) VALUES (2000000583,'Le montant des lignes comportant une * n''est pas soumis � la LAA','de',2000000196,'20190211161010globaz    ');
INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY) VALUES (2000000584,'Le montant des lignes comportant une * n''est pas soumis � la LAA','fr',2000000196,'20190211161010globaz    ');
INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY) VALUES (2000000585,'Le montant des lignes comportant une * n''est pas soumis � la LAA','it',2000000196,'20190211161010globaz    ');
INSERT INTO SCHEMA.CTELEMEN (CCIELE,CCIDOC,CCNPOS,CCNNIV,PSPY,CCBSBD,CCBDES,CCBSEL,CCBEDI) VALUES (2000000195,2000000003,49,3,'20190211155208globaz    ','2','                    ','2','2');

INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY) VALUES (2000000580,'Le montant des lignes comportant une * n''est pas soumis � la LAA','de',2000000195,'20190211161243globaz    ');
INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY) VALUES (2000000581,'Le montant des lignes comportant une * n''est pas soumis � la LAA','fr',2000000195,'20190211161243globaz    ');
INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY) VALUES (2000000582,'Le montant des lignes comportant une * n''est pas soumis � la LAA','it',2000000195,'20190211161243globaz    ');
INSERT INTO SCHEMA.CTELEMEN (CCIELE,CCIDOC,CCNPOS,CCNNIV,PSPY,CCBSBD,CCBDES,CCBSEL,CCBEDI) VALUES (2000000196,2000000002,49,3,'20190211161010globaz    ','2','                    ','2','2');

-- Ajout texte pour titre document jours isol�s

INSERT INTO WEBAVSCIAM.CTELEMEN (CCBDES,CCBEDI,CCBSBD,CCBSEL,CCIDOC,CCIELE,CCNNIV,CCNPOS,PSPY) VALUES ('','2','2','2',2000000002,2000000197,1,2,'20190307143831');
INSERT INTO WEBAVSCIAM.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (2000000197,2000000586,'de','[DE]D&eacute;compte de prestations - indemnisation de jours isol&eacute;s','20190307143831');
INSERT INTO WEBAVSCIAM.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (2000000197,2000000587,'fr','D&eacute;compte de prestations - indemnisation de jours isol&eacute;s','20190307143831');
INSERT INTO WEBAVSCIAM.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (2000000197,2000000588,'it','[IT]D&eacute;compte de prestations - indemnisation de jours isol&eacute;s','20190307143831');

INSERT INTO WEBAVSCIAM.CTELEMEN (CCBDES,CCBEDI,CCBSBD,CCBSEL,CCIDOC,CCIELE,CCNNIV,CCNPOS,PSPY) VALUES ('','2','2','2',2000000003,2000000198,1,2,'20190307150845');
INSERT INTO WEBAVSCIAM.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (2000000198,2000000589,'de','[DE]D&eacute;compte de prestations - indemnisation de jours isol&eacute;s','20190307150845');
INSERT INTO WEBAVSCIAM.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (2000000198,2000000590,'fr','D&eacute;compte de prestations - indemnisation de jours isol&eacute;s','20190307150845');
INSERT INTO WEBAVSCIAM.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (2000000198,2000000591,'it','[IT]D&eacute;compte de prestations - indemnisation de jours isol&eacute;s','20190307150845');

INSERT INTO WEBAVSCIAM.CTELEMEN (CCBDES,CCBEDI,CCBSBD,CCBSEL,CCIDOC,CCIELE,CCNNIV,CCNPOS,PSPY) VALUES ('','2','2','2',2000000002,2000000199,2,201,'20190307151704');
INSERT INTO WEBAVSCIAM.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (2000000199,2000000592,'de','[DE]Vous trouverez ci-apr&egrave;s le d&eacute;compte de prestations - indemnisation de jours isol&eacute;s&nbsp;conform&eacute;ment aux demandes que vous nous avez fait parvenir.','20190307151704');
INSERT INTO WEBAVSCIAM.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (2000000199,2000000593,'fr','Vous trouverez ci-apr&egrave;s le&nbsp;d&eacute;compte de prestations - indemnisation de jours isol&eacute;s conform&eacute;ment aux demandes que vous nous avez fait parvenir.','20190307151704');
INSERT INTO WEBAVSCIAM.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (2000000199,2000000594,'it','[IT]Vous trouverez ci-apr&egrave;s le d&eacute;compte de prestations - indemnisation de jours isol&eacute;s&nbsp;conform&eacute;ment aux demandes que vous nous avez fait parvenir.','20190307151704');

INSERT INTO WEBAVSCIAM.CTELEMEN (CCBDES,CCBEDI,CCBSBD,CCBSEL,CCIDOC,CCIELE,CCNNIV,CCNPOS,PSPY) VALUES ('','2','2','2',2000000003,2000000200,2,201,'20190307151704');
INSERT INTO WEBAVSCIAM.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (2000000200,2000000595,'de','[DE]Vous trouverez ci-apr&egrave;s le d&eacute;compte de prestations - indemnisation de jours isol&eacute;s&nbsp;conform&eacute;ment aux demandes que vous nous avez fait parvenir.','20190307151704');
INSERT INTO WEBAVSCIAM.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (2000000200,2000000596,'fr','Vous trouverez ci-apr&egrave;s le&nbsp;d&eacute;compte de prestations - indemnisation de jours isol&eacute;s conform&eacute;ment aux demandes que vous nous avez fait parvenir.','20190307151704');
INSERT INTO WEBAVSCIAM.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (2000000200,2000000597,'it','[IT]Vous trouverez ci-apr&egrave;s le d&eacute;compte de prestations - indemnisation de jours isol&eacute;s&nbsp;conform&eacute;ment aux demandes que vous nous avez fait parvenir.','20190307151704');