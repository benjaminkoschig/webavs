-- K170622_002 : Ajout param : statut de la part f�d�rale

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64039156, 'PCKYVPC', 1 ,1,0,0, 'CLE_TOTAL_CC_STATUS_FEDERAL', 2,1,2,2,2, 2 , 63000039 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64039156, 'F', '', 'STATUS TOTAL CC FEDERALE', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64039156, 'D', '', 'STATUS GESAMT CC AUSGLEICHKASSE', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

-- K170622_002 : liste de r�partition par commune politique
DELETE FROM schema.JADEPROP WHERE PROPNAME = 'pegasus.commune.politique.code.reference.rubrique.pc';
DELETE FROM schema.JADEPROP WHERE PROPNAME = 'pegasus.commune.politique.code.reference.rubrique.rfm';
INSERT INTO schema.JADEPROP (PROPNAME,PROPVAL) VALUES ('pegasus.commune.politique.numero.rubrique.pc','4110.3080.0000,4110.3080.1000,4110.3330.0000,4110.3370.0000,4110.4609.0000,4110.4609.1000,4110.4650.0000,4210.3080.0000,4210.3080.1000,4210.3330.0000,4210.3370.0000,4210.4609.0000,4210.4609.1000,4210.4650.0000');
INSERT INTO schema.JADEPROP (PROPNAME,PROPVAL) VALUES ('pegasus.commune.politique.numero.rubrique.rfm','4120.3080.0000,4120.3080.1000,4120.3080.2000,4120.3330.0000,4120.3370.0000,4120.4609.0000,4120.4650.0000,4220.3080.0000,4220.3080.1000,4220.3080.2000,4220.3330.0000,4220.3370.0000,4220.4609.0000,4220.4609.1000,4220.4650.0000');

-- S160704_002 - PC - D�plafonnement de loyer - 1.19.1
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008058, 'PCTYPVMET', 49 ,1,0,0, 'DEPLAFONNEMENT_SEULE_STUDIO', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008058, 'F', '', 'Personne seule - Studio � 1,5 pi�ce', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008058, 'D', '', 'Alleinstehende Person - Studio bis 1,5 Zimmer', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008059, 'PCTYPVMET', 50 ,1,0,0, 'DEPLAFONNEMENT_SEULE_2_25_PIECES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008059, 'F', '', 'Personne seule - 2 � 2,5 pi�ces', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008059, 'D', '', 'Alleinstehende Person - 2 bis 2,5 Zimmer', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008060, 'PCTYPVMET', 51 ,1,0,0, 'DEPLAFONNEMENT_SEULE_3_PLUS_PIECES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008060, 'F', '', 'Personne seule - 3 pi�ces et plus', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008060, 'D', '', 'Alleinstehende Person - 3 Zimmer une mehr', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008061, 'PCTYPVMET', 52 ,1,0,0, 'DEPLAFONNEMENT_COUPLE_STUDIO', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008061, 'F', '', 'Couple - Studio � 1,5 pi�ce', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008061, 'D', '', 'Ehepaar - Studio bis 1,5 Zimmer', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008062, 'PCTYPVMET', 53 ,1,0,0, 'DEPLAFONNEMENT_COUPLE_2_25_PIECES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008062, 'F', '', 'Couple - 2 � 2,5 pi�ces', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008062, 'D', '', 'Ehepaar - 2 bis 2,5 Zimmer', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008063, 'PCTYPVMET', 54 ,1,0,0, 'DEPLAFONNEMENT_COUPLE_3_PLUS_PIECES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008063, 'F', '', 'Couple - 3 pi�ces et plus', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008063, 'D', '', 'Ehepaar - 3 Zimmer une mehr', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) );

insert into SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('pegasus.loyer.option.deplafonnement.appartement.protege','64008058,64008059,64008060,64008061,64008062,64008063');

ALTER TABLE SCHEMA.PCLOYER ADD CRDAPA DECIMAL(15,0) DEFAULT 0;
REORG TABLE SCHEMA.PCLOYER;

