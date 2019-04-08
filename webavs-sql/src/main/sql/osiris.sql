--FERCIAM  S181016_002 Ajout mode de transfert organes exécutions

INSERT INTO SCHEMA.FWCOSP (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY) VALUES (246003,'OSIMODTRA ',1,2,0,0,'TRANSFERT_PAR_REPERTOIRE',2,2,2,2,2,2,10200046,0,'                        ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (246003,'D','REP       ','?Transfert dans un répertoire','                        ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (246003,'F','REP       ','Transfert dans un répertoire','                        ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (246003,'I','REP       ','?Transfert dans un répertoire','                        ');

INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('osiris.DossierRacineOrganeExe','','20190329120000Globaz    ','20180101120000Globaz    ');

ALTER TABLE SCHEMA.CAOREXP ADD COLUMN NOMREPERTOIRE VARCHAR(255);
REORG table SCHEMA.CAOREXP;

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237520,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_JU_PARITAIRE_PARTICIPATION',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237520,'D','','[DE]Complément CIAB JU paritaire - Participation aux cotisations paritaires','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237520,'F','','Complément CIAB JU paritaire - Participation aux cotisations paritaires','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237521,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_JU_PARITAIRE_PARTICIPATION',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237521,'D','','[DE]Complément CIAB JU paritaire - Montant brut','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237521,'F','','Complément CIAB JU paritaire - Montant brut','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237522,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_JU_PARITAIRE_MONTANT_BRUT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237522,'D','','[DE]Complément CIAB JU paritaire - Montant restitution','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237522,'F','','Complément CIAB JU paritaire - Montant restitution','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237523,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_JU_PARITAIRE_MONTANT_RESTITUTION',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237523,'D','','[DE]Complément CIAB JU personnel - Cotisations AC','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237523,'F','','Complément CIAB JU personnel - Cotisations AC','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237524,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_JU_PERSONNEL_COTISATIONS_AC',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237524,'D','','[DE]Complément CIAB JU personnel - Cotisations AVS','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237524,'F','','Complément CIAB JU personnel - Cotisations AVS','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237525,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_JU_PERSONNEL_COTISATIONS_AVS',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237525,'D','','[DE]Complément CIAB JU personnel - Participation aux cotisations personnelles','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237525,'F','','Complément CIAB JU personnel - Participation aux cotisations personnelles','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237526,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_JU_PERSONNEL_MONTANT_BRUT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237526,'D','','[DE]Complément CIAB JU personnel - Montant brut','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237526,'F','','Complément CIAB JU personnel - Montant brut','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237527,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_JU_PERSONNEL_MONTANT_RESTITUTION',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237527,'D','','[DE]Complément CIAB JU personnel - Montant restitution','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237527,'F','','Complément CIAB JU personnel - Montant restitution','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237528,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_BE_PARITAIRE_PARTICIPATION',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237528,'D','','[DE]Complément CIAB BE paritaire - Participation aux cotisations paritaires','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237528,'F','','Complément CIAB BE paritaire - Participation aux cotisations paritaires','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237529,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_BE_PARITAIRE_MONTANT_BRUT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237529,'D','','[DE]Complément CIAB BE paritaire - Montant brut','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237529,'F','','Complément CIAB BE paritaire - Montant brut','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237530,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_BE_PARITAIRE_MONTANT_RESTITUTION',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237530,'D','','[DE]Complément CIAB BE paritaire - Montant restitution','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237530,'F','','Complément CIAB BE paritaire - Montant restitution','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237531,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_BE_PERSONNEL_COTISATIONS_AC',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237531,'D','','[DE]Complément CIAB BE personnel - Cotisations AC','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237531,'F','','Complément CIAB BE personnel - Cotisations AC','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237532,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_BE_PERSONNEL_COTISATIONS_AVS',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237532,'D','','[DE]Complément CIAB BE personnel - Cotisations AVS','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237532,'F','','Complément CIAB BE personnel - Cotisations AVS','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237533,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_BE_PERSONNEL_PARTICIPATION',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237533,'D','','[DE]Complément CIAB BE personnel - Participation aux cotisations personnelles','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237533,'F','','Complément CIAB BE personnel - Participation aux cotisations personnelles','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237534,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_BE_PERSONNEL_MONTANT_BRUT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237534,'D','','[DE]Complément CIAB BE personnel - Montant brut','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237534,'F','','Complément CIAB BE personnel - Montant brut','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237535,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_BE_PERSONNEL_MONTANT_RESTITUTION',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237535,'D','','[DE]Complément CIAB BE personnel - Montant restitution','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237535,'F','','Complément CIAB BE personnel - Montant restitution','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237536,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_JU_PARITAIRE_IMPOT_SOURCE',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237536,'D','','[DE]Complément CIAB JU paritaire - Impôt à la source','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237536,'F','','Complément CIAB JU paritaire - Impôt à la source','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237537,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_JU_PERSONNEL_IMPOT_SOURCE',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237537,'D','','[DE]Complément CIAB JU personnel - Impôt à la source','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237537,'F','','Complément CIAB JU personnel - Impôt à la source','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237538,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_BE_PARITAIRE_IMPOT_SOURCE',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237538,'D','','[DE]Complément CIAB BE paritaire - Impôt à la source','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237538,'F','','Complément CIAB BE paritaire - Impôt à la source','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237539,'OSIREFRUB',1,1,0,0,'APG_COMPCIAB_BE_PERSONNEL_IMPOT_SOURCE',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237539,'D','','[DE]Complément CIAB BE personnel - Impôt à la source','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237539,'F','','Complément CIAB BE personnel - Impôt à la source','spy');