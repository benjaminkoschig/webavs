-- PCA-620 - WEBAVS-6638
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('phenix.nomUserPortail','','20190807083544TESTFPV   ','20190807083544TESTFPV   ');

delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='CPPROVENAN');
delete from SCHEMA.FWCOSP where pptygr ='CPPROVENAN';

insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (10600023,'CPPROVENAN',0,1,3,0,'Provenance cot pers',0,2,2,2,2,0,0,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user));
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (10600023,'F','1','Provenance cot pers',(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user));
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (10600023,'D','1','Provenance cot pers',(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user));

insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (623001,'CPPROVENAN',1,1,0,0,'TOUTES',2,1,2,2,2,2,10600023,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user));
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (623001,'D','TO','Toutes',(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user));
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (623001,'F','TO','Toutes',(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user));
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (623001,'I','TO','Toutes',(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user));
insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (623002,'CPPROVENAN',1,1,0,0,'PORTAIL',3,1,2,2,2,2,10600023,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user));
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (623002,'D','PO','Portail',(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user));
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (623002,'F','PO','Portail',(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user));
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (623002,'I','PO','Portail',(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user));
insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (623003,'CPPROVENAN',1,1,0,0,'NON_PORTAIL',4,1,2,2,2,2,10600023,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user));
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (623003,'D','NP','Non portail',(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user));
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (623003,'F','NP','Non portail',(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user));
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (623003,'I','NP','Non portail',(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user));

INSERT INTO SCHEMA.CPCANTOP (SPINDE,SPTPAR,SPCCAN,SPCPAR,SPDDEB,PSPY,SPTGAF) VALUES ((SELECT COALESCE(MAX(SPINDE)+1,0) FROM SCHEMA.CPCANTOP),650001,505021,640001,0,'20191105120000globazf   ',602001);
UPDATE SCHEMA.FWINCP SET PINCVA = (SELECT COALESCE(MAX(SPINDE),0) FROM SCHEMA.CPCANTOP) WHERE PINCID = 'CPCANTOP';

-- PCA-584 - WEBAVS
delete from SCHEMA.FWCOSP where pcosid in(818030);
delete from SCHEMA.FWCOUP where pcosid in(818030);
insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (818030,'VEPARTICUL',1,1,0,0,'CLOTURE_RECAP_MANUELLE',2,1,2,2,2,2,10800018,0, 'spy');
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (818030,'F','CLOTRECAP','Clôture récap manuelle','spy');
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (818030,'D','CLOTRECAP','[DE]Clôture récap manuelle','spy');