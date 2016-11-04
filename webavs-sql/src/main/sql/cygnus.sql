--Mandat s160913_006 --
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 66000075, 'RFLIBTSO', 25 ,1,0,0, '25_SOINS_A_DOMICILE', 2,1,2,2,2, 2 , 65000006 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 66000075, 'F', '', 'Soins à domicile', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 66000075, 'D', '', '[de]Soins à domicile', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 


insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 66000454, 'RFLIBSTS', 180 ,1,0,0, 'SOINS_A_DOMICILE', 2,1,2,2,2, 2 , 65000007 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 66000454, 'F', '', 'Soins à domicile', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 66000454, 'D', '', '[de]Soins à domicile', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 


INSERT INTO SCHEMA.RFTSOIN (EFITSO,EFNCOD,PSPY) VALUES (66000071,'21',null);
INSERT INTO SCHEMA.RFSTSOI (EEISTS,EENCOD,EEITSO,EEIRUA,EEIRUV,EEIRUI) VALUES (66000450,'01',66000071,null,null,null);

INSERT INTO SCHEMA.RFTSOIN (EFITSO,EFNCOD,PSPY) VALUES (66000072,'22',null);
INSERT INTO SCHEMA.RFSTSOI (EEISTS,EENCOD,EEITSO,EEIRUA,EEIRUV,EEIRUI) VALUES (66000451,'01',66000072,null,null,null);

INSERT INTO SCHEMA.RFTSOIN (EFITSO,EFNCOD,PSPY) VALUES (66000073,'23',null);
INSERT INTO SCHEMA.RFSTSOI (EEISTS,EENCOD,EEITSO,EEIRUA,EEIRUV,EEIRUI) VALUES (66000452,'01',66000073,null,null,null);

INSERT INTO SCHEMA.RFTSOIN (EFITSO,EFNCOD,PSPY) VALUES (66000074,'24',null);
INSERT INTO SCHEMA.RFSTSOI (EEISTS,EENCOD,EEITSO,EEIRUA,EEIRUV,EEIRUI) VALUES (66000453,'01',66000074,null,null,null);

insert into SCHEMA.RFTSOIN (EFITSO,EFNCOD,PSPY) VALUES (66000075,'25',null);
insert into SCHEMA.RFSTSOI (EEISTS,EENCOD,EEITSO,EEIRUA,EEIRUV,EEIRUI) VALUES (66000454,'01',66000075,null,null,null);
--Fin Mandat s160913_006 ---

-- DEBUT DU REPORT DES SCRIPTS 1.16.0-CCVS-RC3

--- insertion nouveau type de type remboursement RFTYREM
DELETE from SCHEMA.fwcosp where pcosid = 66002304;
DELETE from SCHEMA.fwcoup where pcosid = 66002304;

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 66002304, 'RFTYREM', 4 ,1,0,0, 'HOME', 2,1,2,2,2, 2 , 65000029 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 66002304, 'F', '', 'Home', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 66002304, 'D', '', 'Heim', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

--- nouvelles propriétés cantons VS
delete from SCHEMA.jadeprop where propname = 'cygnus.canton.loi.rfm';
insert into SCHEMA.jadeprop (propname, propval) values ('cygnus.canton.loi.rfm', 'VS');

-- nouvelle colone table rfprest
 alter table SCHEMA.rfprest add column FOTGEN DECIMAL(8,0);
 reorg table SCHEMA.rfprest;
 
 -- supression periodes de service de l'état erroné
delete from SCHEMA.fwcoup where pcosid in (64025006,64025007,64025008,64008055,64008056);
delete from SCHEMA.fwcosp where pcosid in (64025006,64025007,64025008,64008055,64008056);
  --insertion nouveau cs
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64025006, 'PCSRVETA', 6 ,1,0,0, 'SERVICE_ETAT_RSV', 2,1,2,2,2, 2 , 63000025 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64025006, 'F', '', 'EMS', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64025006, 'D', '', 'Alters- und Pflegeheim', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64025007, 'PCSRVETA', 7 ,1,0,0, 'SERVICE_ETAT_NON_RSV', 2,1,2,2,2, 2 , 63000025 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64025007, 'F', '', 'Lits d''attente', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64025007, 'D', '', 'Warteliste (der freien Betten)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64025008, 'PCSRVETA', 7 ,1,0,0, 'SERVICE_ETAT_INSTITUTION', 2,1,2,2,2, 2 , 63000025 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64025008, 'F', '', 'Institution', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64025008, 'D', '', 'Institution (Einrichtung)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008055, 'PCTYPVMET', 48 ,1,0,0, 'PLAFOND_ANNUEL_INSTITUTION', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008055, 'F', '', 'Plafond journalier Institution', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008055, 'D', '', 'Tägliche Höchstgrenze der Einrichtungen (Institutionen)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008056, 'PCTYPVMET', 48 ,1,0,0, 'PLAFOND_ANNUEL_LITS_ATTENTE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008056, 'F', '', 'Plafond journalier lits d''attente', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008056, 'D', '', 'Tägliche Höchstgrenze der Warteliste der freien Betten', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

update SCHEMA.fwcoup set pcolut = 'Tägliche Höchstgrenze der Alters- und Pflegeheime' where pcosid = 64008048 and PLAIDE = 'F';
update SCHEMA.fwcoup set pcolut = 'Plafond journalier EMS' where pcosid = 64008048 and PLAIDE = 'D';

-- FIN DU REPORT DES SCRIPTS 1.16.0-CCVS-RC3
