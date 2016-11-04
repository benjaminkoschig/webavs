--- **** Création d'une nouvelle catégorie de home EPS S160614_001
-- ajout nouveau service de l'état eps
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64025009, 'PCSRVETA', 6 ,1,0,0, 'SERVICE_ETAT_EPS', 2,1,2,2,2, 2 , 63000025 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64025009, 'F', '', 'EPS', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64025009, 'D', '', 'EPS', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

--- nouvelle catégorie argent de poche
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64072005, 'PCTYPARG', 1 ,1,0,0, 'EPS', 2,1,2,2,2, 2 , 63000072 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64072005, 'F', '', 'EMS ou home dans un service EPS', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64072005, 'D', '', '[de]EMS ou home dans un service EPS', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
 
--- nouvelle variable métier montant type de chambre EPS
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008057, 'PCTYPVMET', 15 ,1,0,0, 'EPS_ETABLISSEMENT_MEDSOC', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008057, 'F', '', 'EPS (établissement psycho-sociaux)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008057, 'D', '', '[de]EPS (établissement psycho-sociaux)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) );

-- DEBUT DU REPORT DES SCRIPTS 1.16.0-CCVS-RC2

--- ajout variables métier ccvd, d0173, avenant 2
delete from SCHEMA.fwcosp where pcosid in (64008052,64008053,64008054);
delete from SCHEMA.fwcoup where pcosid in (64008052,64008053,64008054);

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008052, 'PCTYPVMET', 16 ,1,0,0, 'TAUX_IMPUTATION_VALEUR_LOCATIVE_BRUT', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008052, 'F', '', 'Calcul de la valeur locative brute dès 10 ans', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008052, 'D', '', 'Berechnung des brutto Mietwertes ab 10 Jahren', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008053, 'PCTYPVMET', 17 ,1,0,0, 'TAUX_IMPUTATION_VALEUR_LOCATIVE_BRUT_M10', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008053, 'F', '', 'Calcul de la valeur locative brute moins de 10 ans', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008053, 'D', '', 'Berechnung des brutto Mietwertes weniger als 10 Jahre', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008054, 'PCTYPVMET', 18 ,1,0,0, 'TAUX_IMPUTATION_LOYER EFFECTIF', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008054, 'F', '', 'Calcul du loyer effectif', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008054, 'D', '', 'Berechnung der effektiven Miete', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

--- adaptation du libellé du code systèmes de la cle du calculateur frais entretien
update SCHEMA.fwcoup set pcolut = 'Frais entretien immeubles ({fraction} {libelle})' where plaide = 'F' and pcosid = 64039086;
update SCHEMA.fwcoup set pcolut = 'Kosten Gebäudeunterhalt ({fraction} {libelle})' where plaide = 'D' and pcosid = 64039086;

--- adaptation du libellé du code systèmes de la cle du calculateur valeur locative
update SCHEMA.fwcoup set pcolut = 'Valeur locative {libelle}' where plaide = 'F' and pcosid = 64039031;
update SCHEMA.fwcoup set pcolut = 'Mietwert {libelle}' where plaide = 'D' and pcosid = 64039031;

--- ajout colone isMoinsDe10ans biens immo annexe
alter table SCHEMA.pcbisph add column CHBHMA  NUMERIC(1,0)  NOT NULL DEFAULT 2; 
reorg table SCHEMA.pcbisph ALLOW READ ACCESS;

--- suppression de la propriété moins de 10 ans, obolète
delete from SCHEMA.jadeprop where propname = 'pegasus.bien.immobilier.moins.10.ans.actif'

-- FIN DU REPORT DES SCRIPTS 1.16.0-CCVS-RC2