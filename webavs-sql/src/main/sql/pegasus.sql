-- SCRIPT de RPC tiré de la 1.19.0-4

--RPC
--DROP TABLE SCHEMA.PC_RPC_LOT_ANNONCE;
--DROP TABLE SCHEMA.PC_RPC_ANNONCE;
--DROP TABLE SCHEMA.PC_RPC_LIEN_AN_DE;
--DROP TABLE SCHEMA.PC_RPC_RETOUR_ANNONCE;


insert into SCHEMA.JADEPROP (PROPNAME,PROPVAL) values ('pegasus.rpc.message.header.eloffice', '');
insert into SCHEMA.JADEPROP (PROPNAME,PROPVAL) values ('pegasus.rpc.message.header.recipient.id', '');
insert into SCHEMA.JADEPROP (PROPNAME,PROPVAL) values ('pegasus.rpc.load.partion.size', 'all:2000,blob:350');

--========================================================================================================================
-- SQL Script for create table (PC_RPC_LOT_ANNONCE)
-- Simple model's class name : (ch.globaz.pegasus.business.models.rpc.SimpleLotAnnonce)
--========================================================================================================================
CREATE TABLE SCHEMA.PC_RPC_LOT_ANNONCE
(
ID       	NUMERIC(15,0)  	NOT NULL,
ID_JOB  	NUMERIC(15,0)  	NOT NULL,
CS_ETAT  	NUMERIC(15,0)  	NOT NULL,
CS_TYPE  	NUMERIC(15,0)  	,
DATE_ENVOI  NUMERIC(8,0)  	,
PSPY  		VARCHAR(24)  	NOT NULL,
CSPY  		VARCHAR(24)  	NOT NULL,
PRIMARY KEY(ID)
);
COMMENT ON TABLE SCHEMA.PC_RPC_LOT_ANNONCE is 'Lot des annonces RPC';
COMMENT ON COLUMN SCHEMA.PC_RPC_LOT_ANNONCE.ID is 'ID';
COMMENT ON COLUMN SCHEMA.PC_RPC_LOT_ANNONCE.ID_JOB is 'ID_JOB';
COMMENT ON COLUMN SCHEMA.PC_RPC_LOT_ANNONCE.CS_TYPE is 'CS_TYPE';
COMMENT ON COLUMN SCHEMA.PC_RPC_LOT_ANNONCE.PSPY is 'spy - Champ espion, défini quand et qui a effectué la dernière modification de l''entité';
COMMENT ON COLUMN SCHEMA.PC_RPC_LOT_ANNONCE.CS_ETAT is 'CS_ETAT';
COMMENT ON COLUMN SCHEMA.PC_RPC_LOT_ANNONCE.DATE_ENVOI is 'DATE_ENVOI';
COMMENT ON COLUMN SCHEMA.PC_RPC_LOT_ANNONCE.CSPY is 'spy - Champ espion, défini quand et qui a créé l''entité';

--========================================================================================================================
-- SQL Script for create table (PC_RPC_ANNONCE)
-- Simple model's class name : (ch.globaz.pegasus.business.models.rpc.SimpleAnnonce)
CREATE TABLE SCHEMA.PC_RPC_ANNONCE
(
ID  	            NUMERIC(15,0)  	NOT NULL,
ID_LOT  	        NUMERIC(15,0)  	NOT NULL,
ID_DOSSIER  	    NUMERIC(15,0)  	NOT NULL,
ID_DEMANDE			NUMERIC(15,0)  	NOT NULL,
ID_VERSION_DR       NUMERIC(15,0)  	,
CS_ETAT  	        NUMERIC(15,0)  	NOT NULL,
CS_TYPE             NUMERIC(15,0)  	NOT NULL,
CS_CODE_TRAIT       NUMERIC(15,0)  	,
PSPY  	            VARCHAR(24)  	NOT NULL,
CSPY  	            VARCHAR(24)  	NOT NULL,
PRIMARY KEY(ID),
FOREIGN KEY (ID_LOT) REFERENCES SCHEMA.PC_RPC_LOT_ANNONCE (ID) ON DELETE CASCADE,
FOREIGN KEY (ID_DOSSIER) REFERENCES SCHEMA.PCDOSSI (BAIDOS),
FOREIGN KEY (ID_DEMANDE) REFERENCES SCHEMA.PCDEMPC (BBIDPC)
--FOREIGN KEY (ID_VERSION_DROIT) REFERENCES SCHEMA.PCVERDRO (BDIVDR) On ne peut pas mettre cette contraint à cause du FW qui remplace null par 0 
);
COMMENT ON TABLE SCHEMA.PC_RPC_ANNONCE is 'Annonces RPC';
COMMENT ON COLUMN SCHEMA.PC_RPC_ANNONCE.ID is 'ID';
COMMENT ON COLUMN SCHEMA.PC_RPC_ANNONCE.CS_CODE_TRAIT is 'CS_CODE_TRAIEMENT';
COMMENT ON COLUMN SCHEMA.PC_RPC_ANNONCE.PSPY is 'spy - Champ espion, défini quand et qui a effectué la dernière modification de l''entité';
COMMENT ON COLUMN SCHEMA.PC_RPC_ANNONCE.CS_ETAT is 'CS_ETAT';
COMMENT ON COLUMN SCHEMA.PC_RPC_ANNONCE.CS_TYPE is 'CS_TYPE';
COMMENT ON COLUMN SCHEMA.PC_RPC_ANNONCE.ID_LOT is 'ID_LOT';
COMMENT ON COLUMN SCHEMA.PC_RPC_ANNONCE.ID_DOSSIER is 'ID_DOSSIER';
COMMENT ON COLUMN SCHEMA.PC_RPC_ANNONCE.ID_DEMANDE is 'ID_DEMANDE';
COMMENT ON COLUMN SCHEMA.PC_RPC_ANNONCE.ID_VERSION_DR is 'ID_VERSION_DROIT';
COMMENT ON COLUMN SCHEMA.PC_RPC_ANNONCE.CSPY is 'spy - Champ espion, défini quand et qui a créé l''entité';

--========================================================================================================================
-- SQL Script for create table (PC_RPC_LIEN_ANNONCE_DECISION)
-- Simple model's class name : (ch.globaz.pegasus.business.models.rpc.SimpleLienAnnonceDecision)
--========================================================================================================================
CREATE TABLE SCHEMA.PC_RPC_LIEN_AN_DE
(
ID  	        NUMERIC(15,0)  	NOT NULL,
ID_ANNONCE  	NUMERIC(15,0)  	NOT NULL,
ID_DECISION  	NUMERIC(15,0)  	NOT NULL,
ID_PLAN_CAL     NUMERIC(15,0)  	,
CS_ROLE         NUMERIC(15,0)  	,
PSPY  	        VARCHAR(24)  	NOT NULL,
CSPY  	        VARCHAR(24)  	NOT NULL,
PRIMARY KEY(ID),
FOREIGN KEY (ID_ANNONCE) REFERENCES SCHEMA.PC_RPC_ANNONCE (ID) ON DELETE CASCADE
);
COMMENT ON TABLE SCHEMA.PC_RPC_LIEN_AN_DE is 'Lien entre les annonces et les décisions';
COMMENT ON COLUMN SCHEMA.PC_RPC_LIEN_AN_DE.ID is 'ID';
COMMENT ON COLUMN SCHEMA.PC_RPC_LIEN_AN_DE.ID_DECISION is 'ID_DECISION';
COMMENT ON COLUMN SCHEMA.PC_RPC_LIEN_AN_DE.ID_ANNONCE is 'ID_ANNONCE';
COMMENT ON COLUMN SCHEMA.PC_RPC_LIEN_AN_DE.CS_ROLE is 'CS_ROLE';
COMMENT ON COLUMN SCHEMA.PC_RPC_LIEN_AN_DE.ID_PLAN_CAL is 'ID_PLAN_CALCUL';
COMMENT ON COLUMN SCHEMA.PC_RPC_LIEN_AN_DE.PSPY is 'spy - Champ espion, défini quand et qui a effectué la dernière modification de l''entité';
COMMENT ON COLUMN SCHEMA.PC_RPC_LIEN_AN_DE.CSPY is 'spy - Champ espion, défini quand et qui a créé l''entité';

--========================================================================================================================
-- SQL Script for create table (PC_RPC_RETOUR_ANNONCE)
-- Simple model's class name : (ch.globaz.pegasus.business.models.rpc.SimpleRetourAnnonce)
--========================================================================================================================
CREATE TABLE SCHEMA.PC_RPC_RETOUR_ANNONCE
(
ID  				NUMERIC(15,0)  	NOT NULL,
ID_LIEN_AN_DE		NUMERIC(15,0)  	,
CS_CATE_PLAUSI		NUMERIC(8,0)  	,
CS_TYPE_PLAUSI		NUMERIC(8,0)  	,
CS_TYPE_VIOL_PLAU	NUMERIC(8,0)  	,
CODE_PLAUSI 		VARCHAR(10)  	,
OFFICE_PC	 		VARCHAR(10)  	,
OFFICE_PC_CONFLIT	VARCHAR(10)  	,
NSS_ANNONCE 		VARCHAR(15)  	,
NSS_PERSON	 		VARCHAR(15)  	,
CASE_ID_RPC_CONFLIT	VARCHAR(100)  	,
ID_DECISION_CONFLIT	VARCHAR(100)  	,
VALID_FROM_CONFLIT	NUMERIC(8,0)  	,
VALID_TO_CONFLIT	NUMERIC(8,0)  	,
CS_STATUS_RETOUR	NUMERIC(8,0)  	,
REMARQUE_RETOUR		VARCHAR(255)  	,
PSPY  				VARCHAR(24)  	NOT NULL,
CSPY  				VARCHAR(24)  	NOT NULL, 
PRIMARY KEY(ID)
);
COMMENT ON TABLE SCHEMA.PC_RPC_RETOUR_ANNONCE is 'Retour des annonces';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.ID is 'ID';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.ID_LIEN_AN_DE is 'ID dans la table de lien';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.CS_TYPE_PLAUSI is 'Type de plausi';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.CS_TYPE_VIOL_PLAU is 'Type de violation de plausi';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.CODE_PLAUSI is 'Code de la plausi selon le registre central';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.OFFICE_PC is 'Office PC annoncante';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.OFFICE_PC_CONFLIT is 'Valeur de Office PC en conflit';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.NSS_ANNONCE is 'Nss annconcé';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.NSS_PERSON is 'Nss de la personne concernée par le retour';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.CASE_ID_RPC_CONFLIT is 'Valeur ID du cas RPC en conflit';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.ID_DECISION_CONFLIT is 'Valeur ID de decision en conflit';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.VALID_FROM_CONFLIT is 'Valeur de VALID FROM en conflit';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.VALID_TO_CONFLIT is 'Valeur de VALID TO en conflit';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.CS_STATUS_RETOUR is 'Status de traitement du retour';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.REMARQUE_RETOUR is 'champ de remarque en cas de traitement manuel';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.PSPY is 'spy - Champ espion, défini quand et qui a effectué la dernière modification de l''entité';
COMMENT ON COLUMN SCHEMA.PC_RPC_RETOUR_ANNONCE.CSPY is 'spy - Champ espion, défini quand et qui a créé l''entité';


CREATE INDEX SCHEMA.PC_RPC_ANNONCEI1
       ON  SCHEMA.PC_RPC_ANNONCE(ID_LOT,ID_DOSSIER);
       
-- Modification du code de décès qui est faux
update SCHEMA.PCDECSUP 
set DSTMOT = 64045015
where DSTMOT = 64063013;


/* Etat des annonces RPC : */
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='PCRPCETA');
delete from SCHEMA.FWCOSP where pptygr ='PCRPCETA';

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 63000074, 'PCRPCETA', 0,1,3,0, 'Etat des annonces RPC', 0,2,2,2,2,0,0,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user)); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000074, 'F', '1', 'Etat des annonces RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000074, 'D', '1', 'Etat des annonces RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64074001, 'PCRPCETA', 1 ,1,0,0, 'ERROR', 2,1,2,2,2, 2 , 63000074 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64074001, 'F', '', 'Erreur génération', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64074001, 'D', '', 'Fehler', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64074002, 'PCRPCETA', 1 ,1,0,0, 'OUVERT', 2,1,2,2,2, 2 , 63000074 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64074002, 'F', '', 'Ouvert', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64074002, 'D', '', 'Offen', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64074004, 'PCRPCETA', 1 ,1,0,0, 'CORRECTION', 2,1,2,2,2, 2 , 63000074 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64074004, 'F', '', 'Correction', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64074004, 'D', '', 'Korrektur', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64074005, 'PCRPCETA', 1 ,1,0,0, 'POUR_ENVOI', 2,1,2,2,2, 2 , 63000074 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64074005, 'F', '', 'Pour envoi', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64074005, 'D', '', 'Zum Versand', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64074006, 'PCRPCETA', 1 ,1,0,0, 'PLAUSI_KO', 2,1,2,2,2, 2 , 63000074 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64074006, 'F', '', 'Plausibilités amont', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64074006, 'D', '', 'KO-Plausibilität', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 


/* Code de traitement pour les annonces RPC : */
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='PCRPCCT');
delete from SCHEMA.FWCOSP where pptygr ='PCRPCCT';

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 63000075, 'PCRPCCT', 0,1,3,0, 'Code de traitement pour les annonces RPC', 0,2,2,2,2,0,0,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user)); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000075, 'F', '1', 'Code de traitement pour les annonces RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000075, 'D', '1', 'Code de traitement pour les annonces RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64075003, 'PCRPCCT', 1 ,1,0,0, 'RETOUR_A_TRAITER', 2,1,2,2,2, 2 , 63000075 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64075003, 'F', '', 'Retour à traiter', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64075003, 'D', '', 'Retour zum behandeln', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64075004, 'PCRPCCT', 1 ,1,0,0, 'RETOUR_ACCEPTE', 2,1,2,2,2, 2 , 63000075 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64075004, 'F', '', 'Retour accepté', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64075004, 'D', '', 'Retour akzeptiert', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64075005, 'PCRPCCT', 1 ,1,0,0, 'RETOUR_CORRIGE', 2,1,2,2,2, 2 , 63000075 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64075005, 'F', '', 'Retour corrigé', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64075005, 'D', '', 'Retour korrigiert', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64075006, 'PCRPCCT', 1 ,1,0,0, 'RETOUR_AVERTISSEMENT', 2,1,2,2,2, 2 , 63000075 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64075006, 'F', '', 'Retour avertissement', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64075006, 'D', '', 'Retour warnung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

/* Etat du lot pour les annonces RPC : */
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='PCRELT');
delete from SCHEMA.FWCOSP where pptygr ='PCRELT';

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 63000076, 'PCRELT', 0,1,3,0, 'Etat du lot pour les annonces RPC', 0,2,2,2,2,0,0,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user)); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000076, 'F', '1', 'Etat du lot pour les annonces RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000076, 'D', '1', 'Etat du lot pour les annonces RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64076001, 'PCRELT', 1 ,1,0,0, 'ERROR', 2,1,2,2,2, 2 , 63000076 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64076001, 'F', '', 'En Erreur', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64076001, 'D', '', 'Im Fehler', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64076002, 'PCRELT', 1 ,1,0,0, 'EN_GENERATION', 2,1,2,2,2, 2 , 63000076 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64076002, 'F', '', 'En Génération', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64076002, 'D', '', 'Im Erstellung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64076003, 'PCRELT', 1 ,1,0,0, 'ENVOYE', 2,1,2,2,2, 2 , 63000076 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64076003, 'F', '', 'Envoyé', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64076003, 'D', '', 'Gesendet', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64076004, 'PCRELT', 1 ,1,0,0, 'GENERATION_TEMINE', 2,1,2,2,2, 2 , 63000076 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64076004, 'F', '', 'Génération terminé', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64076004, 'D', '', 'Fertigstellung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 


/* Type des annonces RPC : */
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='PCRPCAT');
delete from SCHEMA.FWCOSP where pptygr ='PCRPCAT';

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 63000077, 'PCRPCAT', 0,1,3,0, 'Type des annonces RPC', 0,2,2,2,2,0,0,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user)); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000077, 'F', '1', 'Type des annonces RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000077, 'D', '1', 'Type des annonces RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64077001, 'PCRPCAT', 1 ,1,0,0, 'PARTIEL', 2,1,2,2,2, 2 , 63000077 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64077001, 'F', '', 'Partielle', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64077001, 'D', '', 'Teilweise', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64077002, 'PCRPCAT', 1 ,1,0,0, 'COMPLET', 2,1,2,2,2, 2 , 63000077 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64077002, 'F', '', 'Complete', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64077002, 'D', '', 'Vollständig', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64077003, 'PCRPCAT', 1 ,1,0,0, 'ANNULATION', 2,1,2,2,2, 2 , 63000077 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64077003, 'F', '', 'Annulation', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64077003, 'D', '', 'Annulierung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

/* Type de violation des annonces retour RPC : */
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='PCRPCVT');
delete from SCHEMA.FWCOSP where pptygr ='PCRPCVT';

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 63000078, 'PCRPCVT', 0,1,3,0, 'Type de violation des annonces retour RPC', 0,2,2,2,2,0,0,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user)); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000078, 'F', '1', 'Type de violation des annonces retour RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000078, 'D', '1', 'Type de violation des annonces retour RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64078001, 'PCRPCVT', 1 ,1,0,0, 'GENERAL', 2,1,2,2,2, 2 , 63000078 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64078001, 'F', '', 'Générale', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64078001, 'D', '', 'Allgemein', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64078002, 'PCRPCVT', 1 ,1,0,0, 'PERSON', 2,1,2,2,2, 2 , 63000078 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64078002, 'F', '', 'Personne', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64078002, 'D', '', 'Person', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64078003, 'PCRPCVT', 1 ,1,0,0, 'OVERLAP', 2,1,2,2,2, 2 , 63000078 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64078003, 'F', '', 'Overlap', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64078003, 'D', '', 'Überlappen', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

/* Type des annonces retour RPC : */
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='PCRPCRT');
delete from SCHEMA.FWCOSP where pptygr ='PCRPCRT';

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 63000079, 'PCRPCRT', 0,1,3,0, 'Type des annonces retour RPC', 0,2,2,2,2,0,0,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user)); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000079, 'F', '1', 'Type des annonces retour RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000079, 'D', '1', 'Type des annonces retour RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64079001, 'PCRPCRT', 1 ,1,0,0, 'INTRA', 2,1,2,2,2, 2 , 63000079 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64079001, 'F', '', 'Intra', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64079001, 'D', '', 'Intra', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64079002, 'PCRPCRT', 1 ,1,0,0, 'CALCUL', 2,1,2,2,2, 2 , 63000079 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64079002, 'F', '', 'Calcul', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64079002, 'D', '', 'Berechnung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64079003, 'PCRPCRT', 1 ,1,0,0, 'INFO', 2,1,2,2,2, 2 , 63000079 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64079003, 'F', '', 'Info', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64079003, 'D', '', 'Info', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64079004, 'PCRPCRT', 1 ,1,0,0, 'INTER', 2,1,2,2,2, 2 , 63000079 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64079004, 'F', '', 'Inter', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64079004, 'D', '', 'Inter', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64079005, 'PCRPCRT', 1 ,1,0,0, 'SIMPLE', 2,1,2,2,2, 2 , 63000079 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64079005, 'F', '', 'Simple', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64079005, 'D', '', 'Einfach', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64079006, 'PCRPCRT', 1 ,1,0,0, 'INTERNAL', 2,1,2,2,2, 2 , 63000079 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64079006, 'F', '', 'Internal', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64079006, 'D', '', 'Intern', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

/* Catégorie des plausis des annonces retour RPC : */
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='PCRPCPC');
delete from SCHEMA.FWCOSP where pptygr ='PCRPCPC';

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 63000080, 'PCRPCPC', 0,1,3,0, 'Catégorie des plausis des annonces retour RPC', 0,2,2,2,2,0,0,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user)); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000080, 'F', '1', 'Catégorie des plausis des annonces retour RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000080, 'D', '1', 'Catégorie des plausis des annonces retour RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64080001, 'PCRPCPC', 1 ,1,0,0, 'BLOCKING', 2,1,2,2,2, 2 , 63000080 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080001, 'F', '', 'Blocking', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080001, 'D', '', 'Blockierend', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64080002, 'PCRPCPC', 1 ,1,0,0, 'AUTO', 2,1,2,2,2, 2 , 63000080 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080002, 'F', '', 'Auto', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080002, 'D', '', 'Auto', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64080003, 'PCRPCPC', 1 ,1,0,0, 'DATA_INTEGRITY', 2,1,2,2,2, 2 , 63000080 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080003, 'F', '', 'Data integrity', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080003, 'D', '', 'Datenintegrität', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64080004, 'PCRPCPC', 1 ,1,0,0, 'ERROR', 2,1,2,2,2, 2 , 63000080 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080004, 'F', '', 'Error', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080004, 'D', '', 'Fehler', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64080005, 'PCRPCPC', 1 ,1,0,0, 'INACTIVE', 2,1,2,2,2, 2 , 63000080 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080005, 'F', '', 'Inactive', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080005, 'D', '', 'Inaktiv', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64080006, 'PCRPCPC', 1 ,1,0,0, 'INFO', 2,1,2,2,2, 2 , 63000080 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080006, 'F', '', 'Info', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080006, 'D', '', 'Info', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64080007, 'PCRPCPC', 1 ,1,0,0, 'MANUAL', 2,1,2,2,2, 2 , 63000080 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080007, 'F', '', 'Manual', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080007, 'D', '', 'Handbuch', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64080008, 'PCRPCPC', 1 ,1,0,0, 'NONE', 2,1,2,2,2, 2 , 63000080 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080008, 'F', '', 'None', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080008, 'D', '', 'Keiner', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64080009, 'PCRPCPC', 1 ,1,0,0, 'WARNING', 2,1,2,2,2, 2 , 63000080 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080009, 'F', '', 'Warning', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64080009, 'D', '', 'Warnung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

/* Statut des retours d''annonces RPC : */
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='PCRPCST');
delete from SCHEMA.FWCOSP where pptygr ='PCRPCST';

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 63000081, 'PCRPCST', 0,1,3,0, 'Statut des retours d''annonces RPC', 0,2,2,2,2,0,0,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user)); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000081, 'F', '1', 'Statut des retours d''annonces RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000081, 'D', '1', 'Statut des retours d''annonces RPC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64081001, 'PCRPCST', 1 ,1,0,0, 'A_TRAITE', 2,1,2,2,2, 2 , 63000081 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64081001, 'F', '', 'Non traité', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64081001, 'D', '', 'Zu erledigen', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64081002, 'PCRPCST', 1 ,1,0,0, 'ACCEPTE', 2,1,2,2,2, 2 , 63000081 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64081002, 'F', '', 'Accepté', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64081002, 'D', '', 'Akzeptiert', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64081003, 'PCRPCST', 1 ,1,0,0, 'TRAITE', 2,1,2,2,2, 2 , 63000081 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64081003, 'F', '', 'Traité', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64081003, 'D', '', 'Behandelt', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64081004, 'PCRPCST', 1 ,1,0,0, 'AUTO', 2,1,2,2,2, 2 , 63000081 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64081004, 'F', '', 'Automatique', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64081004, 'D', '', 'Automatisch', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 


/* Type de MOTIF de la création/modification d''un droit : */
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='PCMOTDRO');
delete from SCHEMA.FWCOSP where pptygr ='PCMOTDRO';

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 63000063, 'PCMOTDRO', 0,1,3,0, 'Type de MOTIF de la création/modification d''un droit', 0,2,2,2,2,0,0,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user)); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000063, 'F', '1', 'Type de MOTIF de la création/modification d''un droit', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000063, 'D', '1', 'Type de MOTIF de la création/modification d''un droit', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063001, 'PCMOTDRO', 1 ,1,0,0, 'NOUVEAU_DROIT', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063001, 'F', '', 'Nouveau droit', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063001, 'D', '', 'Neues Recht', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063002, 'PCMOTDRO', 1 ,1,0,0, 'MODIFICATIONS_DIVERSES', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063002, 'F', '', 'Modifications diverses', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063002, 'D', '', 'Diverse Änderungen', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063003, 'PCMOTDRO', 1 ,1,0,0, 'TRANSFERT_CLARENS', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063003, 'F', '', 'Transfert Clarens', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063003, 'D', '', 'Übermittlung Clarens', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063004, 'PCMOTDRO', 1 ,1,0,0, 'TRANSFERT_HORS_CANTON', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063004, 'F', '', 'Transfert hors canton', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063004, 'D', '', 'Übermittlung ausser Kanton', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063005, 'PCMOTDRO', 1 ,1,0,0, 'REVISION_QUADRIENNALE', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063005, 'F', '', 'Révision quadriennale', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063005, 'D', '', 'Vier-Jahres-Revisionen', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063006, 'PCMOTDRO', 1 ,1,0,0, 'MARRIAGE', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063006, 'F', '', 'Marriage', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063006, 'D', '', 'Hochzeit', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063007, 'PCMOTDRO', 1 ,1,0,0, 'VEUVAGE', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063007, 'F', '', 'Veuvage', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063007, 'D', '', 'Verwitwung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063008, 'PCMOTDRO', 1 ,1,0,0, 'ARRIVEE_ETRANGER', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063008, 'F', '', 'Arrivée de l''étranger', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063008, 'D', '', 'Ankunft im Ausland', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063009, 'PCMOTDRO', 1 ,1,0,0, 'DIVORCE', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063009, 'F', '', 'Divorce/séparation', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063009, 'D', '', 'Scheidung/Trennung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063010, 'PCMOTDRO', 1 ,1,0,0, 'ADAPTATION', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063010, 'F', '', 'Adaptation', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063010, 'D', '', 'Anpassung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063011, 'PCMOTDRO', 1 ,1,0,0, 'REPRISE', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063011, 'F', '', 'Reprise', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063011, 'D', '', 'Wiederaufnahme', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063012, 'PCMOTDRO', 1 ,1,0,0, 'ADAPTATION_HOME', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063012, 'F', '', 'Adaptation home', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063012, 'D', '', 'Anpassung Heim', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063013, 'PCMOTDRO', 1 ,1,0,0, 'DECES', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063013, 'F', '', 'Décès', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063013, 'D', '', 'Tod', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063014, 'PCMOTDRO', 1 ,1,0,0, 'REPRISE_ADAPTATION_ERRONE', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063014, 'F', '', 'Reprise - adaptation erroné', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063014, 'D', '', 'Wiederaufnahme - fehlerhafte Anpassung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063015, 'PCMOTDRO', 1 ,1,0,0, 'CORRECTION_REPRISE', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063015, 'F', '', 'Correction reprise', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063015, 'D', '', 'Korrekturmassnahme', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063016, 'PCMOTDRO', 1 ,1,0,0, 'DEPART_ETRANGER', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063016, 'F', '', 'Départ à l''étranger', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063016, 'D', '', 'Wegzug ins Ausland', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64063017, 'PCMOTDRO', 1 ,1,0,0, 'MODIFICATION_RETOUR_REGISTRE', 2,1,2,2,2, 2 , 63000063 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063017, 'F', '', 'Modification retour registre', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64063017, 'D', '', 'Änderung zurück ans Register', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

-- Related to -- S160704_002 - PC - Déplafonnement de loyer - 1.19.1
/* type_variables_metier : */
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='PCTYPVMET');
delete from SCHEMA.FWCOSP where pptygr ='PCTYPVMET';

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 63000008, 'PCTYPVMET', 0,1,3,0, 'type_variables_metier', 0,2,2,2,2,0,0,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user)); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000008, 'F', '1', 'type_variables_metier', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000008, 'D', '1', 'type_variables_metier', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008001, 'PCTYPVMET', 1 ,1,0,0, 'ARGENT_POCHE_MEDICALISE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008001, 'F', '', 'Argent de poche en home médicalisé par mois', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008001, 'D', '', 'Monatliches Taschengeld im medizinischen Pflegeheim', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008002, 'PCTYPVMET', 2 ,1,0,0, 'ARGENT_POCHE_NON_MEDICALISE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008002, 'F', '', 'Argent de poche en home non-médicalisé par mois', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008002, 'D', '', 'Monatliches Taschengeld im nicht-medizinischen Heim', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008003, 'PCTYPVMET', 3 ,1,0,0, 'BESOINS_VITAUX_2_ENFANTS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008003, 'F', '', 'Besoins vitaux pour 1er et 2ème enfant', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008003, 'D', '', 'Lebensbedarf für 1. und 2. Kind', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008004, 'PCTYPVMET', 4 ,1,0,0, 'BESOINS_VITAUX_4_ENFANTS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008004, 'F', '', 'Besoins vitaux pour 3ème et 4ème enfant', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008004, 'D', '', 'Lebensbedarf für 3. und 4. Kind', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008005, 'PCTYPVMET', 5 ,1,0,0, 'BESOINS_VITAUX_5_ENFANTS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008005, 'F', '', 'Besoins vitaux pour 5ème enfant et suivants', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008005, 'D', '', 'Lebensbedarf für 5. Kind und weitere Kinder', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008006, 'PCTYPVMET', 6 ,1,0,0, 'BESOINS_VITAUX_COUPLES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008006, 'F', '', 'Besoins vitaux pour les couples', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008006, 'D', '', 'Lebensbedarf für Paare', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008007, 'PCTYPVMET', 7 ,1,0,0, 'BESOINS_VITAUX_CELIBATAIRES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008007, 'F', '', 'Besoins vitaux pour les personnes seules', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008007, 'D', '', 'Lebensbedarf für Alleinstehende', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008008, 'PCTYPVMET', 8 ,1,0,0, 'DEDUCTION_FORFAITAIRE_FORTUNE_COUPLES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008008, 'F', '', 'Déduction forfaitaire sur fortune (couples)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008008, 'D', '', 'Pauschalabzug auf Vermögen (Paare)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008009, 'PCTYPVMET', 9 ,1,0,0, 'DEDUCTION_FORFAITAIRE_FORTUNE_CELIBATAIRES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008009, 'F', '', 'Déduction forfaitaire sur fortune (personnes seules)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008009, 'D', '', 'Pauschalabzug auf Vermögen (Alleinstehende)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008010, 'PCTYPVMET', 10 ,1,0,0, 'DEDUCTION_FORFAITAIRE_FORTUNE_ENFANTS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008010, 'F', '', 'Déduction forfaitaire sur fortune (pour chaque enfant)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008010, 'D', '', 'Pauschalabzug auf Vermögen (für jedes Kind)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008011, 'PCTYPVMET', 11 ,1,0,0, 'DEDUCTION_FORFAITAIRE_IMMOBILIER_ASSURE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008011, 'F', '', 'Déduction sur propriété immobilière habitée par assuré/famille', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008011, 'D', '', 'Abzug auf durch versicherte Person/Familie bewohntes Immobilieneigentum', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008012, 'PCTYPVMET', 12 ,1,0,0, 'FORFAIT_CHARGES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008012, 'F', '', 'Forfait charges', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008012, 'D', '', 'Kostenpauschale', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008013, 'PCTYPVMET', 13 ,1,0,0, 'FORFAIT_FRAIS_CHAUFFAGE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008013, 'F', '', 'Forfait pour frais de chauffage', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008013, 'D', '', 'Pauschale für Heizkosten', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008014, 'PCTYPVMET', 14 ,1,0,0, 'FRAIS_ENTRETIEN_IMMEUBLE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008014, 'F', '', 'Frais entretien immeubles', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008014, 'D', '', 'Kosten Gebäudeunterhalt', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008015, 'PCTYPVMET', 16 ,1,0,0, 'FRANCHISE_CAISSE_MALADIE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008015, 'F', '', 'Franchise et quote-part de la caisse-maladie', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008015, 'D', '', 'Franchise und Selbstbehalt der Krankenkasse', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008016, 'PCTYPVMET', 17 ,1,0,0, 'FRANCHISE_REVENUS_PRIVILEGIERS_FAMILLE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008016, 'F', '', 'Franchise revenus privilégiés (famille)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008016, 'D', '', 'Franchise obere Einkommensklasse (Familie)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008017, 'PCTYPVMET', 18 ,1,0,0, 'FRANCHISE_REVENUS_PRIVILEGIERS_CELIBATAIRES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008017, 'F', '', 'Franchise revenus privilégiés (personnes seules)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008017, 'D', '', 'Franchise obere Einkommensklasse (Alleinstehende)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008018, 'PCTYPVMET', 19 ,1,0,0, 'MONTANT_MAXIMUM_PC_HOME', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008018, 'F', '', 'Montant maximum PC par année (home)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008018, 'D', '', 'EL-Maximalbetrag pro Jahr (Heim)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008019, 'PCTYPVMET', 20 ,1,0,0, 'MONTANT_MAXIMUM_PC_MAISON', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008019, 'F', '', 'Montant maximum PC par année (maison)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008019, 'D', '', 'EL-Maximalbetrag pro Jahr (Daheim)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008020, 'PCTYPVMET', 21 ,1,0,0, 'MONTANT_MINIMAL_PC', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008020, 'F', '', 'Montant minimal d''une PC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008020, 'D', '', 'Minimalbetrag einer EL', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008021, 'PCTYPVMET', 22 ,1,0,0, 'FRACTION_REVENUS_PRIVILEGIES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008021, 'F', '', 'Fraction revenus privilégiés', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008021, 'D', '', 'Bruchteil obere Einkommensklasse', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008022, 'PCTYPVMET', 23 ,1,0,0, 'FRACTIONS_FORTUNE_NON_VIEILLESSE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008022, 'F', '', 'Fraction sur la fortune (calcul sur base rente non-vieillesse)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008022, 'D', '', 'Bruchteil auf Vermögen (Berechnung auf Basis Nichtaltersrente)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008023, 'PCTYPVMET', 24 ,1,0,0, 'FRACTIONS_FORTUNE_VIEILLESSE_ENFANT', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008023, 'F', '', 'Fraction sur la fortune (calcul sur base rente vieillesse + enfant)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008023, 'D', '', 'Bruchteil auf Vermögen (Berechnung auf Basis Altersrente + Kind)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008024, 'PCTYPVMET', 25 ,1,0,0, 'FRACTIONS_FORTUNE_VIEILLESSE_HOME', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008024, 'F', '', 'Fraction sur la fortune (calcul sur base rente vieillesse + home)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008024, 'D', '', 'Bruchteil auf Vermögen (Berechnung auf Basis Altersrente + Heim)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008025, 'PCTYPVMET', 26 ,1,0,0, 'FRACTIONS_FORTUNE_VIEILLESSE_MAISON', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008025, 'F', '', 'Fraction sur la fortune (calcul sur base rente vieillesse + maison)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008025, 'D', '', 'Bruchteil auf Vermögen (Berechnung auf Basis Altersrente + Daheim)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008029, 'PCTYPVMET', 27 ,1,0,0, 'REGIME_ALIMENTAIRE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008029, 'F', '', 'Régime alimentaire', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008029, 'D', '', 'Diät', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008030, 'PCTYPVMET', 28 ,1,0,0, 'TAUX_OFAS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008030, 'F', '', 'Taux OFAS intérêt fortune', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008030, 'D', '', 'BSV-Grad Zinsen Vermögen', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008031, 'PCTYPVMET', 29 ,1,0,0, 'LOYER_MAXIMUM_PERSONNES_SEULES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008031, 'F', '', 'Loyer maximum pris en compte (personnes seules)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008031, 'D', '', 'Maximal berücksichtigte Miete (Alleinstehende)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008032, 'PCTYPVMET', 30 ,1,0,0, 'LOYER_MAXIMUM_AUTRES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008032, 'F', '', 'Loyer maximum pris en compte (autres)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008032, 'D', '', 'Maximal berücksichtigte Miete (Andere)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008033, 'PCTYPVMET', 31 ,1,0,0, 'MONTANT_MINIMAL_PC', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008033, 'F', '', 'Montant minimal d''une PC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008033, 'D', '', 'Minimalbetrag einer EL', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008034, 'PCTYPVMET', 32 ,1,0,0, 'AMORTISSEMENT_ANNUEL', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008034, 'F', '', 'Amortissement annuel', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008034, 'D', '', 'Jährliche Abschreibung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008035, 'PCTYPVMET', 33 ,1,0,0, 'TAUX_PENSION_NON_RECONNUE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008035, 'F', '', 'Taux pension non reconnue', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008035, 'D', '', 'Grad nicht anerkannte Pension', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008036, 'PCTYPVMET', 34 ,1,0,0, 'PLAFOND_DEPENSE_FAUTEUIL_ROULANT', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008036, 'F', '', 'Plafond de dépense fauteuil roulant', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008036, 'D', '', 'Ausgabenobergrenze Rollstuhl', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008037, 'PCTYPVMET', 35 ,1,0,0, 'FORFAIT_REVENU_NATURE_TENUE_MENAGE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008037, 'F', '', 'Montant annuel forfaitaire de revenus en nature pour tenue de ménage', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008037, 'D', '', 'Jahrespauschalbetrag für Naturalbezüge für Haushaltsführung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008038, 'PCTYPVMET', 36 ,1,0,0, 'MONTANT_ALLOCATIONS_NOEL', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008038, 'F', '', 'Montant de l''allocations de Noël', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008038, 'D', '', 'Betrag der Weihnachtszuschläge', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008039, 'PCTYPVMET', 37 ,1,0,0, 'DEDUCTION_FORFAITAIRE_IMMOBILIER_ASSURE_API_HOME', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008039, 'F', '', 'Déduction sur propriété immobilière habitée par assuré/famille avec Home/API', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008039, 'D', '', 'Abzug auf durch versicherte Person/Familie bewohntes Immobilieneigentum mit Heim/HE', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008040, 'PCTYPVMET', 38 ,1,0,0, 'MENSUALISATION_IJ_CHOMAGE_NBRE_JOURS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008040, 'F', '', 'Nbre jours mensuel pour la journalisation IJ Chômage', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008040, 'D', '', 'Anzahl monatliche Tage für Journalisierung TG Arbeitslose', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008041, 'PCTYPVMET', 39 ,1,0,0, 'TAUX_SOUS_LOCATIONS_LOYER_FRAIS_ACQUISITION', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008041, 'F', '', 'Taux imputation loyer pour frais d''acquisition sous-locations', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008041, 'D', '', 'Anrechnungsgrad Mietzins für Abschlussaufwendungen Untermieten', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008042, 'PCTYPVMET', 40 ,1,0,0, 'TAUX_IMPUTATION_REVENUS_SOUS_LOCATION_FRAIS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008042, 'F', '', 'Taux imputation revenu des sous locations pour les frais d''acquisition', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008042, 'D', '', 'Anrechnungsgrad Einkommen aus Untermieten für Abschlussaufwendungen', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008043, 'PCTYPVMET', 41 ,1,0,0, 'Montant ESE handicap physique', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008043, 'F', '', 'Montant ESE handicap physique', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008043, 'D', '', 'Betrag ESE körperliche Behinderung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008044, 'PCTYPVMET', 42 ,1,0,0, 'Montant EMS non médicalisé psychiatrique', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008044, 'F', '', 'Montant EMS non médicalisé psychiatrique', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008044, 'D', '', 'Betrag Altersheim mit psychiatrischer Betreuung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008045, 'PCTYPVMET', 43 ,1,0,0, 'Montant EMS non médicalisé âge avancées', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008045, 'F', '', 'Montant EMS non médicalisé âge avancées', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008045, 'D', '', 'Betrag Altersheim mit medizinischer Betreuung in fortgeschrittenem Alter', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008046, 'PCTYPVMET', 44 ,1,0,0, 'Argent de poche home AVS par année', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008046, 'F', '', 'Argent de poche home AVS par année', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008046, 'D', '', 'Taschengeld AHV Heim pro Jahr', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008047, 'PCTYPVMET', 45 ,1,0,0, 'Argent de poche home AI par année', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008047, 'F', '', 'Argent de poche home AI par année', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008047, 'D', '', 'Taschengeld IV Heim pro Jahr', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008048, 'PCTYPVMET', 48 ,1,0,0, 'PLAFOND_ANNUEL_HOME', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008048, 'F', '', 'Plafond journalier EMS', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008048, 'D', '', 'Tägliche Obergrenze Pflegeheim ', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008049, 'PCTYPVMET', 46 ,1,0,0, 'FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008049, 'F', '', 'Fraction sur la fortune (calcul sur base rente non-vieillesse + home)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008049, 'D', '', 'Bruchteil auf Vermögen (Berechnung auf Basis Nichtaltersrente + Heim)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008050, 'PCTYPVMET', 47 ,1,0,0, 'FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008050, 'F', '', 'Fraction sur la fortune (calcul sur base rente non-vieillesse + maison)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008050, 'D', '', 'Bruchteil auf Vermögen (Berechnung auf Basis Nichtaltersrente + Daheim)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008051, 'PCTYPVMET', 15 ,1,0,0, 'FRAIS_ENTRETIEN_IMMEUBLE_MOINS_10_ANS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008051, 'F', '', 'Frais entretien immeubles de moins de 10 ans', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008051, 'D', '', 'Gebäudewartungskosten von weniger als 10 Jahren', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008052, 'PCTYPVMET', 16 ,1,0,0, 'TAUX_IMPUTATION_VALEUR_LOCATIVE_BRUT', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008052, 'F', '', 'Calcul de la valeur locative brute dès 10 ans', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008052, 'D', '', 'Berechnung des brutto Mietwertes ab 10 Jahren', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008053, 'PCTYPVMET', 17 ,1,0,0, 'TAUX_IMPUTATION_VALEUR_LOCATIVE_BRUT_M10', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008053, 'F', '', 'Calcul de la valeur locative brute moins de 10 ans', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008053, 'D', '', 'Berechnung des brutto Mietwertes weniger als 10 Jahre', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008054, 'PCTYPVMET', 18 ,1,0,0, 'TAUX_IMPUTATION_LOYER EFFECTIF', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008054, 'F', '', 'Calcul du loyer effectif', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008054, 'D', '', 'Berechnung der effektiven Miete', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008055, 'PCTYPVMET', 48 ,1,0,0, 'PLAFOND_ANNUEL_INSTITUTION', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008055, 'F', '', 'Plafond journalier Institution', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008055, 'D', '', 'Plafond Tägliche Institution', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008056, 'PCTYPVMET', 48 ,1,0,0, 'PLAFOND_ANNUEL_LITS_ATTENTE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008056, 'F', '', 'Plafond journalier lits d''attente', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008056, 'D', '', 'Tägliche Höchstgrenze der Warteliste der freien Betten', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008057, 'PCTYPVMET', 15 ,1,0,0, 'EPS_ETABLISSEMENT_MEDSOC', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008057, 'F', '', 'EPS (établissement psycho-sociaux)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008057, 'D', '', '[de]EPS (établissement psycho-sociaux)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
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

/* Ajout des nouvelle clés de calculateur pour le BLOB */
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64039152, 'PCKYVPC', 1 ,1,0,0, 'CLE_TOTAL_CC_MENSUEL_CALCULE', 2,1,2,2,2, 2 , 63000039 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64039152, 'F', '', 'PC mensuelle calculée avant montant minimum', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64039152, 'D', '', 'Berechnete monatliche EL vor Minimalbetrag', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64039153, 'PCKYVPC', 1 ,1,0,0, 'CLE_TOTAL_CC_MONTANT_MINIMAL_APPLIQUE', 2,1,2,2,2, 2 , 63000039 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64039153, 'F', '', 'Montant minimum mensuel appliqué', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64039153, 'D', '', 'Angewendeter monatlicher Minimalbetrag', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64039154, 'PCKYVPC', 1 ,1,0,0, 'KEY_AUTRES_RENTES_RENTE_ETRANGERE_TAUX_CHANGE', 2,1,2,2,2, 2 , 63000039 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64039154, 'F', '', 'Taux de change', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64039154, 'D', '', 'Wechselkurs', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) );

insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL) values ('pegasus.rpc.groupresponsable', '');
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL) values ('pegasus.rpc.limit.day.generation', '10');


-- fin de SCRIPT de RPC tiré de la 1.19.0-4

-- K170622_002 : Ajout param : statut de la part fédérale

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64039156, 'PCKYVPC', 1 ,1,0,0, 'CLE_TOTAL_CC_STATUS_FEDERAL', 2,1,2,2,2, 2 , 63000039 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64039156, 'F', '', 'STATUS TOTAL CC FEDERALE', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64039156, 'D', '', 'STATUS GESAMT CC AUSGLEICHKASSE', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

-- S160704_002 - PC - Déplafonnement de loyer - 1.19.1
/* type_variables_metier : */
--remplacé par la famille complète pour éviter les problèmes de merge de script 1.19.1 et 1.19.0 rpc
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='PCTYPVMET');
delete from SCHEMA.FWCOSP where pptygr ='PCTYPVMET';

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 63000008, 'PCTYPVMET', 0,1,3,0, 'type_variables_metier', 0,2,2,2,2,0,0,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user)); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000008, 'F', '1', 'type_variables_metier', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 63000008, 'D', '1', 'type_variables_metier', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008001, 'PCTYPVMET', 1 ,1,0,0, 'ARGENT_POCHE_MEDICALISE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008001, 'F', '', 'Argent de poche en home médicalisé par mois', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008001, 'D', '', 'Monatliches Taschengeld im medizinischen Pflegeheim', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008002, 'PCTYPVMET', 2 ,1,0,0, 'ARGENT_POCHE_NON_MEDICALISE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008002, 'F', '', 'Argent de poche en home non-médicalisé par mois', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008002, 'D', '', 'Monatliches Taschengeld im nicht-medizinischen Heim', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008003, 'PCTYPVMET', 3 ,1,0,0, 'BESOINS_VITAUX_2_ENFANTS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008003, 'F', '', 'Besoins vitaux pour 1er et 2ème enfant', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008003, 'D', '', 'Lebensbedarf für 1. und 2. Kind', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008004, 'PCTYPVMET', 4 ,1,0,0, 'BESOINS_VITAUX_4_ENFANTS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008004, 'F', '', 'Besoins vitaux pour 3ème et 4ème enfant', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008004, 'D', '', 'Lebensbedarf für 3. und 4. Kind', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008005, 'PCTYPVMET', 5 ,1,0,0, 'BESOINS_VITAUX_5_ENFANTS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008005, 'F', '', 'Besoins vitaux pour 5ème enfant et suivants', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008005, 'D', '', 'Lebensbedarf für 5. Kind und weitere Kinder', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008006, 'PCTYPVMET', 6 ,1,0,0, 'BESOINS_VITAUX_COUPLES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008006, 'F', '', 'Besoins vitaux pour les couples', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008006, 'D', '', 'Lebensbedarf für Paare', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008007, 'PCTYPVMET', 7 ,1,0,0, 'BESOINS_VITAUX_CELIBATAIRES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008007, 'F', '', 'Besoins vitaux pour les personnes seules', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008007, 'D', '', 'Lebensbedarf für Alleinstehende', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008008, 'PCTYPVMET', 8 ,1,0,0, 'DEDUCTION_FORFAITAIRE_FORTUNE_COUPLES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008008, 'F', '', 'Déduction forfaitaire sur fortune (couples)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008008, 'D', '', 'Pauschalabzug auf Vermögen (Paare)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008009, 'PCTYPVMET', 9 ,1,0,0, 'DEDUCTION_FORFAITAIRE_FORTUNE_CELIBATAIRES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008009, 'F', '', 'Déduction forfaitaire sur fortune (personnes seules)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008009, 'D', '', 'Pauschalabzug auf Vermögen (Alleinstehende)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008010, 'PCTYPVMET', 10 ,1,0,0, 'DEDUCTION_FORFAITAIRE_FORTUNE_ENFANTS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008010, 'F', '', 'Déduction forfaitaire sur fortune (pour chaque enfant)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008010, 'D', '', 'Pauschalabzug auf Vermögen (für jedes Kind)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008011, 'PCTYPVMET', 11 ,1,0,0, 'DEDUCTION_FORFAITAIRE_IMMOBILIER_ASSURE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008011, 'F', '', 'Déduction sur propriété immobilière habitée par assuré/famille', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008011, 'D', '', 'Abzug auf durch versicherte Person/Familie bewohntes Immobilieneigentum', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008012, 'PCTYPVMET', 12 ,1,0,0, 'FORFAIT_CHARGES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008012, 'F', '', 'Forfait charges', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008012, 'D', '', 'Kostenpauschale', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008013, 'PCTYPVMET', 13 ,1,0,0, 'FORFAIT_FRAIS_CHAUFFAGE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008013, 'F', '', 'Forfait pour frais de chauffage', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008013, 'D', '', 'Pauschale für Heizkosten', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008014, 'PCTYPVMET', 14 ,1,0,0, 'FRAIS_ENTRETIEN_IMMEUBLE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008014, 'F', '', 'Frais entretien immeubles', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008014, 'D', '', 'Kosten Gebäudeunterhalt', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008015, 'PCTYPVMET', 16 ,1,0,0, 'FRANCHISE_CAISSE_MALADIE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008015, 'F', '', 'Franchise et quote-part de la caisse-maladie', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008015, 'D', '', 'Franchise und Selbstbehalt der Krankenkasse', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008016, 'PCTYPVMET', 17 ,1,0,0, 'FRANCHISE_REVENUS_PRIVILEGIERS_FAMILLE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008016, 'F', '', 'Franchise revenus privilégiés (famille)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008016, 'D', '', 'Franchise obere Einkommensklasse (Familie)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008017, 'PCTYPVMET', 18 ,1,0,0, 'FRANCHISE_REVENUS_PRIVILEGIERS_CELIBATAIRES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008017, 'F', '', 'Franchise revenus privilégiés (personnes seules)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008017, 'D', '', 'Franchise obere Einkommensklasse (Alleinstehende)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008018, 'PCTYPVMET', 19 ,1,0,0, 'MONTANT_MAXIMUM_PC_HOME', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008018, 'F', '', 'Montant maximum PC par année (home)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008018, 'D', '', 'EL-Maximalbetrag pro Jahr (Heim)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008019, 'PCTYPVMET', 20 ,1,0,0, 'MONTANT_MAXIMUM_PC_MAISON', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008019, 'F', '', 'Montant maximum PC par année (maison)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008019, 'D', '', 'EL-Maximalbetrag pro Jahr (Daheim)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008020, 'PCTYPVMET', 21 ,1,0,0, 'MONTANT_MINIMAL_PC', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008020, 'F', '', 'Montant minimal d''une PC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008020, 'D', '', 'Minimalbetrag einer EL', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008021, 'PCTYPVMET', 22 ,1,0,0, 'FRACTION_REVENUS_PRIVILEGIES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008021, 'F', '', 'Fraction revenus privilégiés', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008021, 'D', '', 'Bruchteil obere Einkommensklasse', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008022, 'PCTYPVMET', 23 ,1,0,0, 'FRACTIONS_FORTUNE_NON_VIEILLESSE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008022, 'F', '', 'Fraction sur la fortune (calcul sur base rente non-vieillesse)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008022, 'D', '', 'Bruchteil auf Vermögen (Berechnung auf Basis Nichtaltersrente)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008023, 'PCTYPVMET', 24 ,1,0,0, 'FRACTIONS_FORTUNE_VIEILLESSE_ENFANT', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008023, 'F', '', 'Fraction sur la fortune (calcul sur base rente vieillesse + enfant)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008023, 'D', '', 'Bruchteil auf Vermögen (Berechnung auf Basis Altersrente + Kind)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008024, 'PCTYPVMET', 25 ,1,0,0, 'FRACTIONS_FORTUNE_VIEILLESSE_HOME', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008024, 'F', '', 'Fraction sur la fortune (calcul sur base rente vieillesse + home)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008024, 'D', '', 'Bruchteil auf Vermögen (Berechnung auf Basis Altersrente + Heim)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008025, 'PCTYPVMET', 26 ,1,0,0, 'FRACTIONS_FORTUNE_VIEILLESSE_MAISON', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008025, 'F', '', 'Fraction sur la fortune (calcul sur base rente vieillesse + maison)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008025, 'D', '', 'Bruchteil auf Vermögen (Berechnung auf Basis Altersrente + Daheim)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008029, 'PCTYPVMET', 27 ,1,0,0, 'REGIME_ALIMENTAIRE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008029, 'F', '', 'Régime alimentaire', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008029, 'D', '', 'Diät', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008030, 'PCTYPVMET', 28 ,1,0,0, 'TAUX_OFAS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008030, 'F', '', 'Taux OFAS intérêt fortune', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008030, 'D', '', 'BSV-Grad Zinsen Vermögen', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008031, 'PCTYPVMET', 29 ,1,0,0, 'LOYER_MAXIMUM_PERSONNES_SEULES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008031, 'F', '', 'Loyer maximum pris en compte (personnes seules)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008031, 'D', '', 'Maximal berücksichtigte Miete (Alleinstehende)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008032, 'PCTYPVMET', 30 ,1,0,0, 'LOYER_MAXIMUM_AUTRES', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008032, 'F', '', 'Loyer maximum pris en compte (autres)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008032, 'D', '', 'Maximal berücksichtigte Miete (Andere)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008033, 'PCTYPVMET', 31 ,1,0,0, 'MONTANT_MINIMAL_PC', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008033, 'F', '', 'Montant minimal d''une PC', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008033, 'D', '', 'Minimalbetrag einer EL', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008034, 'PCTYPVMET', 32 ,1,0,0, 'AMORTISSEMENT_ANNUEL', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008034, 'F', '', 'Amortissement annuel', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008034, 'D', '', 'Jährliche Abschreibung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008035, 'PCTYPVMET', 33 ,1,0,0, 'TAUX_PENSION_NON_RECONNUE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008035, 'F', '', 'Taux pension non reconnue', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008035, 'D', '', 'Grad nicht anerkannte Pension', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008036, 'PCTYPVMET', 34 ,1,0,0, 'PLAFOND_DEPENSE_FAUTEUIL_ROULANT', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008036, 'F', '', 'Plafond de dépense fauteuil roulant', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008036, 'D', '', 'Ausgabenobergrenze Rollstuhl', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008037, 'PCTYPVMET', 35 ,1,0,0, 'FORFAIT_REVENU_NATURE_TENUE_MENAGE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008037, 'F', '', 'Montant annuel forfaitaire de revenus en nature pour tenue de ménage', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008037, 'D', '', 'Jahrespauschalbetrag für Naturalbezüge für Haushaltsführung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008038, 'PCTYPVMET', 36 ,1,0,0, 'MONTANT_ALLOCATIONS_NOEL', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008038, 'F', '', 'Montant de l''allocations de Noël', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008038, 'D', '', 'Betrag der Weihnachtszuschläge', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008039, 'PCTYPVMET', 37 ,1,0,0, 'DEDUCTION_FORFAITAIRE_IMMOBILIER_ASSURE_API_HOME', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008039, 'F', '', 'Déduction sur propriété immobilière habitée par assuré/famille avec Home/API', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008039, 'D', '', 'Abzug auf durch versicherte Person/Familie bewohntes Immobilieneigentum mit Heim/HE', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008040, 'PCTYPVMET', 38 ,1,0,0, 'MENSUALISATION_IJ_CHOMAGE_NBRE_JOURS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008040, 'F', '', 'Nbre jours mensuel pour la journalisation IJ Chômage', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008040, 'D', '', 'Anzahl monatliche Tage für Journalisierung TG Arbeitslose', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008041, 'PCTYPVMET', 39 ,1,0,0, 'TAUX_SOUS_LOCATIONS_LOYER_FRAIS_ACQUISITION', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008041, 'F', '', 'Taux imputation loyer pour frais d''acquisition sous-locations', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008041, 'D', '', 'Anrechnungsgrad Mietzins für Abschlussaufwendungen Untermieten', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008042, 'PCTYPVMET', 40 ,1,0,0, 'TAUX_IMPUTATION_REVENUS_SOUS_LOCATION_FRAIS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008042, 'F', '', 'Taux imputation revenu des sous locations pour les frais d''acquisition', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008042, 'D', '', 'Anrechnungsgrad Einkommen aus Untermieten für Abschlussaufwendungen', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008043, 'PCTYPVMET', 41 ,1,0,0, 'Montant ESE handicap physique', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008043, 'F', '', 'Montant ESE handicap physique', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008043, 'D', '', 'Betrag ESE körperliche Behinderung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008044, 'PCTYPVMET', 42 ,1,0,0, 'Montant EMS non médicalisé psychiatrique', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008044, 'F', '', 'Montant EMS non médicalisé psychiatrique', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008044, 'D', '', 'Betrag Altersheim mit psychiatrischer Betreuung', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008045, 'PCTYPVMET', 43 ,1,0,0, 'Montant EMS non médicalisé âge avancées', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008045, 'F', '', 'Montant EMS non médicalisé âge avancées', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008045, 'D', '', 'Betrag Altersheim mit medizinischer Betreuung in fortgeschrittenem Alter', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008046, 'PCTYPVMET', 44 ,1,0,0, 'Argent de poche home AVS par année', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008046, 'F', '', 'Argent de poche home AVS par année', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008046, 'D', '', 'Taschengeld AHV Heim pro Jahr', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008047, 'PCTYPVMET', 45 ,1,0,0, 'Argent de poche home AI par année', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008047, 'F', '', 'Argent de poche home AI par année', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008047, 'D', '', 'Taschengeld IV Heim pro Jahr', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008048, 'PCTYPVMET', 48 ,1,0,0, 'PLAFOND_ANNUEL_HOME', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008048, 'F', '', 'Plafond journalier EMS', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008048, 'D', '', 'Tägliche Obergrenze Pflegeheim ', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008049, 'PCTYPVMET', 46 ,1,0,0, 'FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008049, 'F', '', 'Fraction sur la fortune (calcul sur base rente non-vieillesse + home)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008049, 'D', '', 'Bruchteil auf Vermögen (Berechnung auf Basis Nichtaltersrente + Heim)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008050, 'PCTYPVMET', 47 ,1,0,0, 'FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008050, 'F', '', 'Fraction sur la fortune (calcul sur base rente non-vieillesse + maison)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008050, 'D', '', 'Bruchteil auf Vermögen (Berechnung auf Basis Nichtaltersrente + Daheim)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008051, 'PCTYPVMET', 15 ,1,0,0, 'FRAIS_ENTRETIEN_IMMEUBLE_MOINS_10_ANS', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008051, 'F', '', 'Frais entretien immeubles de moins de 10 ans', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008051, 'D', '', 'Gebäudewartungskosten von weniger als 10 Jahren', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008052, 'PCTYPVMET', 16 ,1,0,0, 'TAUX_IMPUTATION_VALEUR_LOCATIVE_BRUT', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008052, 'F', '', 'Calcul de la valeur locative brute dès 10 ans', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008052, 'D', '', 'Berechnung des brutto Mietwertes ab 10 Jahren', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008053, 'PCTYPVMET', 17 ,1,0,0, 'TAUX_IMPUTATION_VALEUR_LOCATIVE_BRUT_M10', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008053, 'F', '', 'Calcul de la valeur locative brute moins de 10 ans', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008053, 'D', '', 'Berechnung des brutto Mietwertes weniger als 10 Jahre', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008054, 'PCTYPVMET', 18 ,1,0,0, 'TAUX_IMPUTATION_LOYER EFFECTIF', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008054, 'F', '', 'Calcul du loyer effectif', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008054, 'D', '', 'Berechnung der effektiven Miete', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008055, 'PCTYPVMET', 48 ,1,0,0, 'PLAFOND_ANNUEL_INSTITUTION', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008055, 'F', '', 'Plafond journalier Institution', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008055, 'D', '', 'Plafond Tägliche Institution', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008056, 'PCTYPVMET', 48 ,1,0,0, 'PLAFOND_ANNUEL_LITS_ATTENTE', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008056, 'F', '', 'Plafond journalier lits d''attente', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008056, 'D', '', 'Tägliche Höchstgrenze der Warteliste der freien Betten', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64008057, 'PCTYPVMET', 15 ,1,0,0, 'EPS_ETABLISSEMENT_MEDSOC', 2,1,2,2,2, 2 , 63000008 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008057, 'F', '', 'EPS (établissement psycho-sociaux)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64008057, 'D', '', '[de]EPS (établissement psycho-sociaux)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
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

