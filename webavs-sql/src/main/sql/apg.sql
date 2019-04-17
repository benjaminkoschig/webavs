INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('apg.assurance.complementCIAB.ju.paritaire.id','0','20190417120000Globaz    ','20190417120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('apg.assurance.complementCIAB.ju.personnel.id','0','20190417120000Globaz    ','20190417120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('apg.assurance.complementCIAB.be.paritaire.id','0','20190417120000Globaz    ','20190417120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('apg.assurance.complementCIAB.be.personnel.id','0','20190417120000Globaz    ','20190417120000Globaz    ');

INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('apg.isFERCIAB','false','20190417120000Globaz    ','20190417120000Globaz    ');

------------BEGIN SCRIPTS SPECIFIQUE FERCIAB QUI SONT DANS UN FICHIER SQL A PART MAIS QUI SONT EN COMMENTAIRES ICI AFIN DE GARDER UNE TRACE -----------

--S170313_002-FERCIAB-Implémentation du module APG
--delete from SCHEMA.FWCOSP where pcosid in(52001050, 52001051, 52001052, 52001053, 52001054, 52001055, 52001056);
--delete from SCHEMA.FWCOUP where pcosid in(52001050, 52001051, 52001052, 52001053, 52001054, 52001055, 52001056);
--insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52001050,'APGENSERVI',1,1,0,0,'DEMENAGEMENT',2,1,2,2,2,2,51000001,0, 'spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001050,'F','501','Déménagement','spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001050,'D','501','Déménagement','spy'); 
--insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52001051,'APGENSERVI',1,1,0,0,'NAISSANCE',2,1,2,2,2,2,51000001,0, 'spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001051,'F','502','Naissance','spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001051,'D','502','Naissance','spy'); 
--insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52001052,'APGENSERVI',1,1,0,0,'MARIAGE_LPART',2,1,2,2,2,2,51000001,0, 'spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001052,'F','503','Mariage/LPart','spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001052,'D','503','Mariage/LPart','spy'); 
--insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52001053,'APGENSERVI',1,1,0,0,'DECES',2,1,2,2,2,2,51000001,0, 'spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001053,'F','504','Décès','spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001053,'D','504','Décès','spy'); 
--insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52001054,'APGENSERVI',1,1,0,0,'JOURNEES_DIVERSES',2,1,2,2,2,2,51000001,0, 'spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001054,'F','505','Inspection Recrutement Libération','spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001054,'D','505','Inspection Recrutement Libération','spy'); 
--insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52001055,'APGENSERVI',1,1,0,0,'CONGE_JEUNESSE',2,1,2,2,2,2,51000001,0, 'spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001055,'F','506','Congé jeunesse','spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001055,'D','506','Congé jeunesse','spy'); 
--insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52001056,'APGENSERVI',1,1,0,0,'SERVICE_ETRANGER',2,1,2,2,2,2,51000001,0, 'spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001056,'F','507','Service à  l''étranger','spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001056,'D','507','Service à  l''étranger','spy'); 
--insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52001057,'APGENSERVI',1,1,0,0,'DECES_DEMI_JOUR',2,1,2,2,2,2,51000001,0, 'spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001057,'F','508','Décès (demi-journée)','spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52001057,'D','508','Décès (demi-journée)','spy'); 

--delete from SCHEMA.FWCOSP where pcosid in(52015006, 52015007);
--delete from SCHEMA.FWCOUP where pcosid in(52015006, 52015007);
--insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52015006,'APGENRPRES',2,1,0,0,'Prestation complémentaire',2,1,2,2,2,2,51000015,0, 'spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52015006,'F','COMPCIAB','Prestation complémentaire','spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52015006,'D','COMPCIAB','Prestation complémentaire','spy'); 
--insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (52015007,'APGENRPRES',2,1,0,0,'Jours isolés',2,1,2,2,2,2,51000015,0, 'spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52015007,'F','ISOLES','Jours isolés','spy'); 
--insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (52015007,'D','ISOLES','Jours isolés','spy'); 

--INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','COMCIABJUR',0,20100101,0.000000,1,0.000000,'',80.000000,'Montant maximum pour complément CIAB JU recrue',0,0,'                        ');
--INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','COMCIABJUA',0,20100101,0.000000,1,0.000000,'',400.000000,'Montant maximum pour complément CIAB JU autres',0,0,'                        ');
--INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','COMCIABBEA',0,20100101,0.000000,1,0.000000,'',400.000000,'Montant maximum pour complément CIAB BE autres',0,0,'                        ');
--INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','COMCIABBER',0,20100101,0.000000,1,0.000000,'',80.000000,'Montant maximum pour complément CIAB BE recrue',0,0,'                        ');

--INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','ISOLEDEMEN',0,20100101,0.000000,1,0.000000,'',1.000000,'Jours isolés, déménagement',0,0,'                        ');
--INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','ISOLENAISS',0,20100101,0.000000,1,0.000000,'',1.000000,'Jours isolés, naissance',0,0,'                        ');
--INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','ISOLEMARIA',0,20100101,0.000000,1,0.000000,'',2.000000,'Jours isolés, mariage/LPart',0,0,'                        ');
--INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','ISOLEDECES',0,20100101,0.000000,1,0.000000,'',3.000000,'Jours isolés, décès',0,0,'                        ');
--INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','ISOLEDIVER',0,20100101,0.000000,1,0.000000,'',1.000000,'Jours isolés, Inspection Recrutement Libération',0,0,'                        ');
--INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','ISOLEJEUNE',0,20100101,0.000000,1,0.000000,'',5.000000,'Jours isolés, congé jeunesse',0,0,'                        ');
--INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG       ','ISOLEDECED',0,20100101,0.000000,1,0.000000,'',1.000000,'Jours isolés, décès (demi-journée)',0,0,'                        ');

--INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY) VALUES (2000000583,'Le montant des lignes comportant une * n''est pas soumis à la LAA','de',2000000196,'20190211161010globaz    ');
--INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY) VALUES (2000000584,'Le montant des lignes comportant une * n''est pas soumis à la LAA','fr',2000000196,'20190211161010globaz    ');
--INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY) VALUES (2000000585,'Le montant des lignes comportant une * n''est pas soumis à la LAA','it',2000000196,'20190211161010globaz    ');
--INSERT INTO SCHEMA.CTELEMEN (CCIELE,CCIDOC,CCNPOS,CCNNIV,PSPY,CCBSBD,CCBDES,CCBSEL,CCBEDI) VALUES (2000000195,2000000003,49,3,'20190211155208globaz    ','2','                    ','2','2');

--INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY) VALUES (2000000580,'Le montant des lignes comportant une * n''est pas soumis à la LAA','de',2000000195,'20190211161243globaz    ');
--INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY) VALUES (2000000581,'Le montant des lignes comportant une * n''est pas soumis à la LAA','fr',2000000195,'20190211161243globaz    ');
--INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY) VALUES (2000000582,'Le montant des lignes comportant une * n''est pas soumis à la LAA','it',2000000195,'20190211161243globaz    ');
--INSERT INTO SCHEMA.CTELEMEN (CCIELE,CCIDOC,CCNPOS,CCNNIV,PSPY,CCBSBD,CCBDES,CCBSEL,CCBEDI) VALUES (2000000196,2000000002,49,3,'20190211161010globaz    ','2','                    ','2','2');

-- Ajout texte pour titre document jours isolés
--INSERT INTO SCHEMA.CTELEMEN (CCBDES,CCBEDI,CCBSBD,CCBSEL,CCIDOC,CCIELE,CCNNIV,CCNPOS,PSPY) VALUES ('','2','2','2',2000000002,9000000197,1,2,'20190307143831');
--INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (9000000197,9000001000,'de','[DE]D&eacute;compte de prestations - indemnisation de jours isol&eacute;s','20190307143831');
--INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (9000000197,9000001001,'fr','D&eacute;compte de prestations - indemnisation de jours isol&eacute;s','20190307143831');
--INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (9000000197,9000001002,'it','[IT]D&eacute;compte de prestations - indemnisation de jours isol&eacute;s','20190307143831');

--INSERT INTO SCHEMA.CTELEMEN (CCBDES,CCBEDI,CCBSBD,CCBSEL,CCIDOC,CCIELE,CCNNIV,CCNPOS,PSPY) VALUES ('','2','2','2',2000000003,9000000198,1,2,'20190307150845');
--INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (9000000198,9000001003,'de','[DE]D&eacute;compte de prestations - indemnisation de jours isol&eacute;s','20190307150845');
--INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (9000000198,9000001004,'fr','D&eacute;compte de prestations - indemnisation de jours isol&eacute;s','20190307150845');
--INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (9000000198,9000001005,'it','[IT]D&eacute;compte de prestations - indemnisation de jours isol&eacute;s','20190307150845');

--INSERT INTO SCHEMA.CTELEMEN (CCBDES,CCBEDI,CCBSBD,CCBSEL,CCIDOC,CCIELE,CCNNIV,CCNPOS,PSPY) VALUES ('','2','2','2',2000000002,9000000199,2,201,'20190307151704');
--INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (9000000199,9000001006,'de','[DE]Vous trouverez ci-apr&egrave;s le d&eacute;compte de prestations - indemnisation de jours isol&eacute;s&nbsp;conform&eacute;ment aux demandes que vous nous avez fait parvenir.','20190307151704');
--INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (9000000199,9000001007,'fr','Vous trouverez ci-apr&egrave;s le&nbsp;d&eacute;compte de prestations - indemnisation de jours isol&eacute;s conform&eacute;ment aux demandes que vous nous avez fait parvenir.','20190307151704');
--INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (9000000199,9000001008,'it','[IT]Vous trouverez ci-apr&egrave;s le d&eacute;compte de prestations - indemnisation de jours isol&eacute;s&nbsp;conform&eacute;ment aux demandes que vous nous avez fait parvenir.','20190307151704');

--INSERT INTO SCHEMA.CTELEMEN (CCBDES,CCBEDI,CCBSBD,CCBSEL,CCIDOC,CCIELE,CCNNIV,CCNPOS,PSPY) VALUES ('','2','2','2',2000000003,9000000200,2,201,'20190307151704');
--INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (9000000200,9000001009,'de','[DE]Vous trouverez ci-apr&egrave;s le d&eacute;compte de prestations - indemnisation de jours isol&eacute;s&nbsp;conform&eacute;ment aux demandes que vous nous avez fait parvenir.','20190307151704');
--INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (9000000200,9000001010,'fr','Vous trouverez ci-apr&egrave;s le&nbsp;d&eacute;compte de prestations - indemnisation de jours isol&eacute;s conform&eacute;ment aux demandes que vous nous avez fait parvenir.','20190307151704');
--INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) VALUES (9000000200,9000001011,'it','[IT]Vous trouverez ci-apr&egrave;s le d&eacute;compte de prestations - indemnisation de jours isol&eacute;s&nbsp;conform&eacute;ment aux demandes que vous nous avez fait parvenir.','20190307151704');

------------END SCRIPTS SPECIFIQUE FERCIAB QUI SONT DANS UN FICHIER SQL A PART MAIS QUI SONT EN COMMENTAIRES ICI AFIN DE GARDER UNE TRACE -----------