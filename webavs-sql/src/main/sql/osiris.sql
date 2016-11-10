-- DEBUT DU REPORT DES SCRIPTS 1.16.0-CCVS-RC3

--- insertion nouevless rubriques comptables --> rfm avenant 3 ccvs
DELETE from SCHEMA.fwcosp where pcosid in (237307,237308);
DELETE from SCHEMA.fwcoup where pcosid in (237307,237308);

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237307,'OSIREFRUB',1,1,0,0,'RFM_AVS_EN_HOME',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237307,'D','','ELKK AHV im Heim','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237307,'F','','RFM AVS en home','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237308,'OSIREFRUB',1,1,0,0,'RFM_AI_EN_HOME',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237308,'D','','ELKK IV im Heim','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237308,'F','','RFM AI en home','spy');

--Suppression du mapping rubrique <--> r�f�rence rubrique
delete from SCHEMA.CARERUP WHERE IDRUBRIQUE IN (223,224,230,232,233,236,238,239,242,245,246,249,252);

--Insertion du mapping rubrique <--> r�f�rence rubrique
INSERT INTO SCHEMA.CARERUP (IDREFRUBRIQUE,IDRUBRIQUE,IDCODEREFERENCE, PSPY) VALUES (88,230,237117,'20160912000000reprise');
INSERT INTO SCHEMA.CARERUP (IDREFRUBRIQUE,IDRUBRIQUE,IDCODEREFERENCE, PSPY) VALUES (89,242,237118,'20160912000000reprise');
INSERT INTO SCHEMA.CARERUP (IDREFRUBRIQUE,IDRUBRIQUE,IDCODEREFERENCE, PSPY) VALUES (75,236,237119,'20160912000000reprise');
INSERT INTO SCHEMA.CARERUP (IDREFRUBRIQUE,IDRUBRIQUE,IDCODEREFERENCE, PSPY) VALUES (78,249,237120,'20160912000000reprise');
INSERT INTO SCHEMA.CARERUP (IDREFRUBRIQUE,IDRUBRIQUE,IDCODEREFERENCE, PSPY) VALUES (99,233,237135,'20160912000000reprise');
INSERT INTO SCHEMA.CARERUP (IDREFRUBRIQUE,IDRUBRIQUE,IDCODEREFERENCE, PSPY) VALUES (100,246,237136,'20160912000000reprise');
INSERT INTO SCHEMA.CARERUP (IDREFRUBRIQUE,IDRUBRIQUE,IDCODEREFERENCE, PSPY) VALUES (73,239,237206,'20160912000000reprise');
INSERT INTO SCHEMA.CARERUP (IDREFRUBRIQUE,IDRUBRIQUE,IDCODEREFERENCE, PSPY) VALUES (80,224,237217,'20160912000000reprise');
INSERT INTO SCHEMA.CARERUP (IDREFRUBRIQUE,IDRUBRIQUE,IDCODEREFERENCE, PSPY) VALUES (79,223,237303,'20160912000000reprise');
INSERT INTO SCHEMA.CARERUP (IDREFRUBRIQUE,IDRUBRIQUE,IDCODEREFERENCE, PSPY) VALUES (81,238,237304,'20160912000000reprise');
INSERT INTO SCHEMA.CARERUP (IDREFRUBRIQUE,IDRUBRIQUE,IDCODEREFERENCE, PSPY) VALUES (90,230,237305,'20160912000000reprise');
INSERT INTO SCHEMA.CARERUP (IDREFRUBRIQUE,IDRUBRIQUE,IDCODEREFERENCE, PSPY) VALUES (86,232,237307,'20160912000000reprise');
INSERT INTO SCHEMA.CARERUP (IDREFRUBRIQUE,IDRUBRIQUE,IDCODEREFERENCE, PSPY) VALUES (87,245,237308,'20160912000000reprise');
INSERT INTO SCHEMA.CARERUP (IDREFRUBRIQUE,IDRUBRIQUE,IDCODEREFERENCE, PSPY) VALUES (101,242,237306,'20160912000000reprise');


UPDATE SCHEMA.FWINCP set PINCVA = (select max(IDREFRUBRIQUE) from SCHEMA.CARERUP) WHERE PINCID = 'CARERUP';

-- S160614_001 Cr�ation d'une nouvelle rubrique EPS - Paiements
insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237309,'OSIREFRUB',1,1,0,0,'PC_AVS_HOME_EPS',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237309,'D','','ELKK AHV im Heim EPS','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237309,'F','','PC AVS en home EPS','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237310,'OSIREFRUB',1,1,0,0,'PC_AVS_HOME_HC_EPS',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237310,'D','','ELKK AHV im Heim ausserhalb Kantons EPS','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237310,'F','','PC AVS en home Hors canton EPS','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237311,'OSIREFRUB',1,1,0,0,'PC_AVS_HOME_EPS',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237311,'D','','ELKK IV im Heim EPS','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237311,'F','','PC AI en home EPS','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237312,'OSIREFRUB',1,1,0,0,'PC_AVS_HOME_HC_EPS',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237312,'D','','ELKK IV im Heim ausserhalb Kantons EPS','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237312,'F','','PC AI en home Hors canton EPS','spy');

-- S160614_001 Cr�ation d'une nouvelle rubrique EPS - Restitutions
insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237313,'OSIREFRUB',1,1,0,0,'PC_AVS_HOME_EPS',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237313,'D','','ELKK AHV im Heim EPS zu situieren','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237313,'F','','PC AVS en home EPS � restituer','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237314,'OSIREFRUB',1,1,0,0,'PC_AVS_HOME_HC_EPS',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237314,'D','','ELKK AHV im Heim ausserhalb Kantons EPS zu situieren','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237314,'F','','PC AVS en home Hors canton EPS � restituer','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237315,'OSIREFRUB',1,1,0,0,'PC_AVS_HOME_EPS',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237315,'D','','ELKK IV im Heim EPS zu situieren','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237315,'F','','PC AI en home EPS � restituer','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237316,'OSIREFRUB',1,1,0,0,'PC_AVS_HOME_HC_EPS',2, 2,2,2,2,2,10200038,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237316,'D','','ELKK IV im Heim ausserhalb Kantons EPS zu situieren','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237316,'F','','PC AI en home Hors canton EPS � restituer','spy');