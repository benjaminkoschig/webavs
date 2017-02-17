--Personnes a ne pas poursuivre
CREATE TABLE SCHEMA.MAPERNPP (
	ID decimal(15,0) PRIMARY KEY NOT NULL,
	NSS decimal(15,0),
	ANNEE decimal(4,0),
	IDTIERSCM decimal(15,0),
	IDFAMI decimal(15,0),
	FLAG_ENVOI DECIMAL(1,0),
	FLAG_REPONSE DECIMAL(1,0),
	MT_CREANCE DECIMAL(15,2) DEFAULT 0.00,
	NOM_PRENOM varchar(100),
	NPA_LOCALITE varchar(100),
	PSPY varchar(24) NOT NULL,
   	CSPY varchar(24) NOT NULL
);

ALTER TABLE SCHEMA.MAFAMILL ADD COLUMN FLAG_ENVOI_NPP DECIMAL(1,0);
REORG TABLE SCHEMA.MAFAMILL;

--Annonces sedex
DROP TABLE SCHEMA.MASDXCO;
CREATE TABLE SCHEMA.MASDXCO (
	ID decimal(15,0) PRIMARY KEY NOT NULL,
   	MSGID varchar(40) NOT NULL,
   	BPRID varchar(40) NOT NULL,
   	XMLID decimal(15,0) NOT NULL,
	MESSAGE_TYPE decimal(8,0) NOT NULL,
	MESSAGE_SUBTYPE decimal(8,0) NOT NULL,
	EMETTEUR varchar(20) NOT NULL,
	RECEPTEUR varchar(20) NOT NULL,
	CSSTAT decimal(8,0) NOT NULL,
	IDTIERSCM decimal(15,0) NOT NULL,
	IDMEMBRE decimal(15,0),
	DATE_ANNONCE DECIMAL(8,0),
	PERIODE_DEBUT DECIMAL(8,0),
   	PERIODE_FIN DECIMAL(8,0),
   	INTERETS DECIMAL(10,2),
	FRAIS DECIMAL(10,2),
	TOTAL_CREANCE DECIMAL(10,2),
	PMT_DEBITEUR DECIMAL(10,2),
	RP_RETRO DECIMAL(10,2),
	ANNULATION DECIMAL(10,2),
	PSPY varchar(24) NOT NULL,
   	CSPY varchar(24) NOT NULL
);

--Annonces sedex personnes
DROP TABLE SCHEMA.MASDXCOP;
CREATE TABLE SCHEMA.MASDXCOP (
	ID decimal(15,0) PRIMARY KEY NOT NULL,
   	IDSEDEX decimal(15,0) NOT NULL,
	IDCONT decimal(15,0),
	IDFAMI decimal(15,0),
	PSPY varchar(24) NOT NULL,
   	CSPY varchar(24) NOT NULL
);

--XML annonces SEDEX
DROP TABLE SCHEMA.MASDXCO_XML;
CREATE TABLE SCHEMA.MASDXCO_XML(
	ID decimal(15,0) PRIMARY KEY NOT NULL,
	MSGID varchar(40),
	XML CLOB,
	PSPY varchar(24) NOT NULL,
   	CSPY varchar(24) NOT NULL
);

--Création des codes systèmes
/* Sous-types annonces SEDEX CO : */
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='AMSXCOBTY');
delete from SCHEMA.FWCOSP where pptygr ='AMSXCOBTY';

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 42003800, 'AMSXCOBTY', 0,1,3,0, 'Sous-types annonces SEDEX CO', 0,2,2,2,2,0,0,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003800, 'F', '1', 'Sous-types annonces SEDEX CO', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003800, 'D', '1', '[de]Sous-types annonces SEDEX CO', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003800, 'I', '1', '[it]Sous-types annonces SEDEX CO', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 42003801, 'AMSXCOBTY', 1 ,1,0,0, 'Liste des personnes ne devant pas être poursuivies', 2,1,2,2,2, 2 , 42003800 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003801, 'F', 'LSTPERPOU', 'Liste des personnes ne devant pas être poursuivies', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003801, 'D', 'LSTPERPOU', '[de]Liste des personnes ne devant pas être poursuivies', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003801, 'I', 'LSTPERPOU', '[it]Liste des personnes ne devant pas être poursuivies', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 42003802, 'AMSXCOBTY', 2 ,1,0,0, 'Créance avec garantie de prise en charge', 2,1,2,2,2, 2 , 42003800 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003802, 'F', 'CREPRICHA', 'Créance avec garantie de prise en charge', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003802, 'D', 'CREPRICHA', '[de]Créance avec garantie de prise en charge', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003802, 'I', 'CREPRICHA', '[it]Créance avec garantie de prise en charge', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 42003803, 'AMSXCOBTY', 3 ,1,0,0, 'Décompte trimestriel', 2,1,2,2,2, 2 , 42003800 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003803, 'F', 'DECTRIM', 'Décompte trimestriel', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003803, 'D', 'DECTRIM', '[de]Décompte trimestriel', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003803, 'I', 'DECTRIM', '[it]Décompte trimestriel', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 42003804, 'AMSXCOBTY', 4 ,1,0,0, 'Décompte final', 2,1,2,2,2, 2 , 42003800 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003804, 'F', 'DECFINAL', 'Décompte final', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003804, 'D', 'DECFINAL', '[de]Décompte final', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003804, 'I', 'DECFINAL', '[it]Décompte final', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 


--Types actes
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='AMSXCOTYA');
delete from SCHEMA.FWCOSP where pptygr ='AMSXCOTYA';

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 42003820, 'AMSXCOTYA', 0,1,3,0, 'Types actes SEDEX CO', 0,2,2,2,2,0,0,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003820, 'F', '1', 'Types actes SEDEX CO', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003820, 'D', '1', '?Types actes SEDEX CO', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003820, 'I', '1', '?Types actes SEDEX CO', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 42003821, 'AMSXCOTYA', 1 ,1,0,0, 'ACTE_DEFAUT_BIEN', 2,1,2,2,2, 2 , 42003820 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003821, 'F', 'ACTDEFBIEN', 'Acte de défaut de biens', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003821, 'D', 'ACTDEFBIEN', '?Acte de défaut de biens', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003821, 'I', 'ACTDEFBIEN', '?Acte de défaut de biens', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 42003822, 'AMSXCOTYA', 1 ,1,0,0, 'ACTE_DEF_BIEN_FAILLITE', 2,1,2,2,2, 2 , 42003820 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003822, 'F', 'ACTDEFBIFA', 'Acte de défaut de biens de faillite', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003822, 'D', 'ACTDEFBIFA', '?Acte de défaut de biens de faillite', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003822, 'I', 'ACTDEFBIFA', '?Acte de défaut de biens de faillite', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 42003823, 'AMSXCOTYA', 1 ,1,0,0, 'TITRE_EQUIVALENT', 2,1,2,2,2, 2 , 42003820 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003823, 'F', 'ACTTITREQU', 'Titre équivalent', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003823, 'D', 'ACTTITREQU', '?Titre équivalent', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 42003823, 'I', 'ACTTITREQU', '?Titre équivalent', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

