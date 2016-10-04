--- **** Création d'une nouvelle catégorie de home EPS S160614_001
-- ajout nouveau service de l'état eps
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64025006, 'PCSRVETA', 6 ,1,0,0, 'SERVICE_ETAT_EPS', 2,1,2,2,2, 2 , 63000025 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64025006, 'F', '', 'EPS', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64025006, 'D', '', 'EPS', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

--- nouvelle catégorie argent de poche
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64072005, 'PCTYPARG', 1 ,1,0,0, 'EPS', 2,1,2,2,2, 2 , 63000072 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64072005, 'F', '', 'EMS ou home dans un service EPS', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64072005, 'D', '', '[de]EMS ou home dans un service EPS', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
 
--- nouvelle variable métier montant type de chambre EPS
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008052, 'PCTYPVMET', 15 ,1,0,0, 'EPS_ETABLISSEMENT_MEDSOC', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008052, 'F', '', 'EPS (établissement psycho-sociaux)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008052, 'D', '', '[de]EPS (établissement psycho-sociaux)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) );  