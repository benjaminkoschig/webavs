-- K170622_002 : Ajout param : statut de la part fédérale

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64039156, 'PCKYVPC', 1 ,1,0,0, 'CLE_TOTAL_CC_STATUS_FEDERAL', 2,1,2,2,2, 2 , 63000039 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64039156, 'F', '', 'STATUS TOTAL CC FEDERALE', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64039156, 'D', '', 'STATUS GESAMT CC AUSGLEICHKASSE', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

-- S160704_002 - PC - Déplafonnement de loyer - 1.19.1
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008058, 'PCTYPVMET', 49 ,1,0,0, 'DEPLAFONNEMENT_SEULE_STUDIO', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008058, 'F', '', 'Personne seule - Studio à 1,5 pièce', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008058, 'D', '', 'Alleinstehende Person - Studio bis 1,5 Zimmer', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008059, 'PCTYPVMET', 50 ,1,0,0, 'DEPLAFONNEMENT_SEULE_2_25_PIECES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008059, 'F', '', 'Personne seule - 2 à 2,5 pièces', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008059, 'D', '', 'Alleinstehende Person - 2 bis 2,5 Zimmer', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008060, 'PCTYPVMET', 51 ,1,0,0, 'DEPLAFONNEMENT_SEULE_3_PLUS_PIECES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008060, 'F', '', 'Personne seule - 3 pièces et plus', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008060, 'D', '', 'Alleinstehende Person - 3 Zimmer une mehr', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008061, 'PCTYPVMET', 52 ,1,0,0, 'DEPLAFONNEMENT_COUPLE_STUDIO', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008061, 'F', '', 'Couple - Studio à 1,5 pièce', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008061, 'D', '', 'Ehepaar - Studio bis 1,5 Zimmer', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008062, 'PCTYPVMET', 53 ,1,0,0, 'DEPLAFONNEMENT_COUPLE_2_25_PIECES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008062, 'F', '', 'Couple - 2 à 2,5 pièces', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008062, 'D', '', 'Ehepaar - 2 bis 2,5 Zimmer', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008063, 'PCTYPVMET', 54 ,1,0,0, 'DEPLAFONNEMENT_COUPLE_3_PLUS_PIECES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008063, 'F', '', 'Couple - 3 pièces et plus', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008063, 'D', '', 'Ehepaar - 3 Zimmer une mehr', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) );

insert into SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('pegasus.loyer.option.deplafonnement.appartement.protege','');

ALTER TABLE SCHEMA.PCLOYER ADD CRDAPA DECIMAL(15,0) DEFAULT 0;
REORG TABLE SCHEMA.PCLOYER;

