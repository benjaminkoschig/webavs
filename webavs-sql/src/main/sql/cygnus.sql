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