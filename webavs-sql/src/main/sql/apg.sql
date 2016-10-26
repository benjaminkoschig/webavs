
ALTER TABLE SCHEMA.APSIPRP ADD COLUMN VFBACM2 VARCHAR(1) DEFAULT '2';

reorg table SCHEMA.APSIPRP allow read access;

UPDATE SCHEMA.APSIPRP SET VFBACM2 = '2';

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52015005, 'APGENRPRES', 2 ,1,0,0, 'ACM2', 2,1,2,2,2, 2 , 51000015 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52015005, 'F', 'ACMALPHA2', 'ACM 2', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52015005, 'D', 'ACMALPHA2', 'ACM 2', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('apg.droits.acm2.maternite.dureejours','14');