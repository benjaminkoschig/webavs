-- Création du code système 251004
delete from schema.fwcoup where pcosid = 251004;
INSERT INTO schema.fwcoup (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (251004,'D','          ','Annuliert','20160606000000globrje');
INSERT INTO schema.fwcoup (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (251004,'F','          ','Annulé','20160606000000globrje');
INSERT INTO schema.fwcoup (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (251004,'I','          ','Annulé','20160606000000globrje');
delete from schema.fwcosp where pcosid = 251004;
INSERT INTO schema.fwcosp (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY) 
	VALUES (251004,'OSIRETOURS',1,1,0,0,'ETAT_RETOUR_ANNULE',2,2,2,2,2,2,10200051,0,'20160606000000globrje');
	
	
--IR395
--Création de la table des lignes de déblocage
CREATE TABLE ccjuweb.RE_LIGNE_DEBLOCAGE
(
   ID decimal(15,0) PRIMARY KEY NOT NULL,
   ID_TIERS_CREANCIER decimal(15,0),
   ID_ROLE_DETTE_EN_COMPTA decimal(15,0),
   ID_TIERS_ADRESSE_PAIEMENT decimal(15,0),
   ID_APPLICATION_ADRESSE_PAIEMENT decimal(15,0),
   ID_SECTION_DETTE_EN_COMPTA decimal(15,0),
   ID_RENTE_PRESTATION decimal(15,0),
   ID_LOT decimal(15,0),
   CS_TYPE_DEBLOCAGE decimal(15,0),
   CS_ETAT decimal(15,0),
   MONTANT decimal(15,2) NOT NULL,
   REFERENCE_PAIEMENT varchar(255),
   PSPY varchar(24) NOT NULL,
   CSPY varchar(24) NOT NULL
);

/* Type d''etat pour les lignes de déblocage : */
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='REDEBETA');
delete from SCHEMA.FWCOSP where pptygr ='REDEBETA';
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 51800062, 'REDEBETA', 0,1,3,0, 'Type d''etat pour les lignes de déblocage', 0,2,2,2,2,0,0,0, '20170313091700spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 51800062, 'F', '1', 'Type d''etat pour les lignes de déblocage', '20170313091700spy' ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 51800062, 'D', '1', 'Type d''etat pour les lignes de déblocage', '20170313091700spy' ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52862001, 'REDEBETA', 1 ,1,0,0, 'ETAT_ENREGISTRE', 2,1,2,2,2, 2 , 51800062 ,0, '20170313091700spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52862001, 'F', '1', 'Enregistré', '20170313091700spy' ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52862001, 'D', '1', 'Gespeichert', '20170313091700spy' ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52862002, 'REDEBETA', 1 ,1,0,0, 'ETAT_VALIDE', 2,1,2,2,2, 2 , 51800062 ,0, '20170313091700spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52862002, 'F', '2', 'Validé', '20170313091700spy' ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52862002, 'D', '2', 'Bestätigt', '20170313091700spy' ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52862003, 'REDEBETA', 1 ,1,0,0, 'ETAT_COMPTABILISE', 2,1,2,2,2, 2 , 51800062 ,0, '20170313091700spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52862003, 'F', '3', 'Comptabilisé', '20170313091700spy' ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52862003, 'D', '3', 'Verbucht', '20170313091700spy' ); 

/* Type de déblocage de rentes : */
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='REDEBLOC');
delete from SCHEMA.FWCOSP where pptygr ='REDEBLOC';
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 51800063, 'REDEBLOC', 0,1,3,0, 'Type de déblocage de rentes', 0,2,2,2,2,0,0,0, '20170313091700spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 51800063, 'F', '1', 'Type de déblocage de rentes', '20170313091700spy' ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 51800063, 'D', '1', 'Type de déblocage de rentes', '20170313091700spy' ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52863001, 'REDEBLOC', 1 ,1,0,0, 'TYPE_DEBLOCAGE_CREANCIER', 2,1,2,2,2, 2 , 51800063 ,0, '20170313091700spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52863001, 'F', '1', 'Créancier', '20170313091700spy' ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52863001, 'D', '1', 'Gläubiger', '20170313091700spy' ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52863002, 'REDEBLOC', 1 ,1,0,0, 'TYPE_DEBLOCAGE_DETTE_EN_COMPTA', 2,1,2,2,2, 2 , 51800063 ,0, '20170313091700spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52863002, 'F', '2', 'Dette en compta', '20170313091700spy' ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52863002, 'D', '2', 'Schulden in Buchhaltung', '20170313091700spy' ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52863003, 'REDEBLOC', 1 ,1,0,0, 'TYPE_DEBLOCAGE_VERSEMENT_BENEFICIAIRE', 2,1,2,2,2, 2 , 51800063 ,0, '20170313091700spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52863003, 'F', '3', 'Versement bénéficiare', '20170313091700spy' ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52863003, 'D', '3', 'Überweisung Begünstigte(-r)', '20170313091700spy' ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52863004, 'REDEBLOC', 1 ,1,0,0, 'TYPE_DEBLOCAGE_IMPOTS_SOURCE', 2,1,2,2,2, 2 , 51800063 ,0, '20170313091700spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52863004, 'F', '4', 'Impots à la source', '20170313091700spy' ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52863004, 'D', '4', 'Quellensteuer', '20170313091700spy' ); 
