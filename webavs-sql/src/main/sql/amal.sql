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

-----------

--========================================================================================================================
-- SQL Script for create table (MAPERNPP)
-- Simple model's class name : (ch.globaz.amal.business.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivre)
--========================================================================================================================
CREATE TABLE SCHEMA.MAPERNPP
(
ID  NUMERIC(15,0)  NOT NULL, 
IDDETFAM  NUMERIC(15,0)  , 
IDFAMI  NUMERIC(15,0)  , 
IDCONTRI  NUMERIC(15,0)  , 
IDTIERSCM  NUMERIC(15,0)  NOT NULL, 
IDSEDEX  NUMERIC(15,0)  , 
NSS  NUMERIC(13,0)  NOT NULL, 
NOM_PRENOM  VARCHAR(100)  , 
NPA_LOCALITE  VARCHAR(100)  , 
ANNEE  NUMERIC(4,0)  NOT NULL, 
MT_CREANCE  NUMERIC(15,2)  , 
FLAG_ENVOI  NUMERIC(1,0)  , 
FLAG_REPONSE  NUMERIC(1,0)  , 
CSPY  VARCHAR(24)  NOT NULL, 
PSPY  VARCHAR(24)  NOT NULL, 
PRIMARY KEY(ID) 

);
COMMENT ON TABLE SCHEMA.MAPERNPP is 'Personnes à ne pas poursuivre';
COMMENT ON COLUMN SCHEMA.MAPERNPP.ID is 'clé primaire';
COMMENT ON COLUMN SCHEMA.MAPERNPP.IDDETFAM is 'detail famille';
COMMENT ON COLUMN SCHEMA.MAPERNPP.PSPY is 'spy - Champ espion, défini quand et qui a effectué la dernière modification de l''entité';
COMMENT ON COLUMN SCHEMA.MAPERNPP.IDFAMI is 'ID_MEMBRE_FAMILLE';
COMMENT ON COLUMN SCHEMA.MAPERNPP.FLAG_ENVOI is 'Flag envoi';
COMMENT ON COLUMN SCHEMA.MAPERNPP.IDTIERSCM is 'ID_TIERS_CAISSE_MALADIE';
COMMENT ON COLUMN SCHEMA.MAPERNPP.CSPY is 'spy - Champ espion, défini quand et qui a créé l''entité';
COMMENT ON COLUMN SCHEMA.MAPERNPP.IDSEDEX is 'Id sedex';
COMMENT ON COLUMN SCHEMA.MAPERNPP.ANNEE is 'Année';
COMMENT ON COLUMN SCHEMA.MAPERNPP.MT_CREANCE is 'Montant créance';
COMMENT ON COLUMN SCHEMA.MAPERNPP.NSS is 'id famille';
COMMENT ON COLUMN SCHEMA.MAPERNPP.IDCONTRI is 'ID_CONTRIBUABLE';
COMMENT ON COLUMN SCHEMA.MAPERNPP.NPA_LOCALITE is 'NPA localité';
COMMENT ON COLUMN SCHEMA.MAPERNPP.NOM_PRENOM is 'Nom prenom';
COMMENT ON COLUMN SCHEMA.MAPERNPP.FLAG_REPONSE is 'Flag réponse';


--========================================================================================================================
-- SQL Script for create table (MASDXCO)
-- Simple model's class name : (ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO)
--========================================================================================================================
CREATE TABLE SCHEMA.MASDXCO
(
ID NUMERIC(15,0) NOT NULL,
IDTIERSCM NUMERIC(15,0) NOT NULL,
DATE_ANNONCE NUMERIC(8,0) ,
MSGID VARCHAR(40) NOT NULL,
BPRID VARCHAR(40) NOT NULL,
CSSTAT NUMERIC(8,0) NOT NULL,
MESSAGE_SUBTYPE NUMERIC(8,0) NOT NULL,
MESSAGE_TYPE NUMERIC(8,0) NOT NULL,
RECEPTEUR VARCHAR(20) NOT NULL,
EMETTEUR VARCHAR(20) NOT NULL,
STATEMENT_DATE NUMERIC(8,0) ,
STATEMENT_YEAR  NUMERIC(4,0)  , 
STATEMENT_START_DATE NUMERIC(8,0) ,
STATEMENT_END_DATE NUMERIC(8,0) ,
TOTAL_ANNULATION  NUMERIC(15,2)  , 
TOTAL_ARRIVAL_DEBTOR  NUMERIC(15,2)  , 
TOTAL_CLAIME  NUMERIC(15,2)  , 
TOTAL_ARRIVAL_PV  NUMERIC(15,2)  , 
CSPY VARCHAR(24) NOT NULL,
PSPY VARCHAR(24) NOT NULL,
PRIMARY KEY(ID)

);
COMMENT ON TABLE SCHEMA.MASDXCO is 'Enregistrement Annonce Sedex contentieux';
COMMENT ON COLUMN SCHEMA.MASDXCO.ID is 'clé primaire';
COMMENT ON COLUMN SCHEMA.MASDXCO.DATE_ANNONCE is 'DATE_ANNONCE';
COMMENT ON COLUMN SCHEMA.MASDXCO.CSSTAT is 'STATUS_MESSAGE';
COMMENT ON COLUMN SCHEMA.MASDXCO.PSPY is 'spy - Champ espion, défini quand et qui a effectué la dernière modification de l''entité';
COMMENT ON COLUMN SCHEMA.MASDXCO.STATEMENT_DATE is 'STATEMENT_DATE';
COMMENT ON COLUMN SCHEMA.MASDXCO.MESSAGE_SUBTYPE is 'SUBTYPE_MESSAGE_SEDEX';
COMMENT ON COLUMN SCHEMA.MASDXCO.MESSAGE_TYPE is 'TYPE_MESSAGE_SEDEX_RIP';
COMMENT ON COLUMN SCHEMA.MASDXCO.RECEPTEUR is 'ID_SEDEX_RECEPTEUR';
COMMENT ON COLUMN SCHEMA.MASDXCO.IDTIERSCM is 'ID_TIERS_CAISSE_MALADIE';
COMMENT ON COLUMN SCHEMA.MASDXCO.CSPY is 'spy - Champ espion, défini quand et qui a créé l''entité';
COMMENT ON COLUMN SCHEMA.MASDXCO.STATEMENT_START_DATE is 'STATEMENT_START_DATE';
COMMENT ON COLUMN SCHEMA.MASDXCO.BPRID is 'Business process id';
COMMENT ON COLUMN SCHEMA.MASDXCO.EMETTEUR is 'ID_SEDEX_EMETTEUR';
COMMENT ON COLUMN SCHEMA.MASDXCO.STATEMENT_END_DATE is 'STATEMENT_END_DATE';
COMMENT ON COLUMN SCHEMA.MASDXCO.TOTAL_ARRIVAL_PV is 'Montant total PV retro';
COMMENT ON COLUMN SCHEMA.MASDXCO.TOTAL_ANNULATION is 'Montant total des remboursements suiste a annulation';
COMMENT ON COLUMN SCHEMA.MASDXCO.TOTAL_ARRIVAL_DEBTOR is 'Montant total des paiements débiteurs';
COMMENT ON COLUMN SCHEMA.MASDXCO.TOTAL_CLAIME is 'Montant total des cates de défauts de bien';
COMMENT ON COLUMN SCHEMA.MASDXCO.MSGID is 'Message ID';
COMMENT ON COLUMN SCHEMA.MASDXCO.STATEMENT_YEAR is 'Année du décompte';
--========================================================================================================================
-- SQL Script for create table (MASDXCOPA)
-- Simple model's class name : (ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOAssure)
--========================================================================================================================
CREATE TABLE SCHEMA.MASDXCOPA
(
ID NUMERIC(15,0) NOT NULL,
ID_CONTRIBUABLE NUMERIC(15) NOT NULL,
ID_FAMILLE NUMERIC(15) NOT NULL,
ID_DETAIL_FAMILLE NUMERIC(15) NOT NULL,
ID_DEBITEUR NUMERIC(15) NOT NULL,
COST_SHARING_MONTANT NUMERIC(15,2) ,
NSS_ASSURE VARCHAR(16) ,
NOM_PRENOM_ASSURE VARCHAR(100) ,
NPA_LOCALITE_ASSURE VARCHAR(100) ,
RUE_NUMERO_ASSURE VARCHAR(100) ,
PRIME_PERIODE_DEBUT NUMERIC(8,0) ,
PRIME_MONTANT NUMERIC(15,2) ,
PRIME_PERIODE_FIN NUMERIC(8,0) ,
TYPE_SUBSIDE VARCHAR(1) ,
COST_SHARING_PRIME_DEBUT NUMERIC(8,0) ,
COST_SHARING_PRIME_FIN NUMERIC(8,0) ,
MESSAGE_ASSURE VARCHAR(255) ,
CSPY VARCHAR(24) NOT NULL,
PSPY VARCHAR(24) NOT NULL,
PRIMARY KEY(ID)

);
COMMENT ON TABLE SCHEMA.MASDXCOPA is 'Enregistrement Annonce Sedex contentieux personne assurée';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.ID is 'clé primaire';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.COST_SHARING_MONTANT is 'COST_SHARING_MONTANT';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.ID_DETAIL_FAMILLE is 'id detail famille';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.NOM_PRENOM_ASSURE is 'NOM_PRENOM_ASSURE';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.PSPY is 'spy - Champ espion, défini quand et qui a effectué la dernière modification de l''entité';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.ID_FAMILLE is 'IDFAMILLE';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.PRIME_PERIODE_DEBUT is 'PRIME_PERIODE_DEBUT';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.NPA_LOCALITE_ASSURE is 'NPA localité assuré';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.CSPY is 'spy - Champ espion, défini quand et qui a créé l''entité';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.ID_DEBITEUR is 'IDSEDEX';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.PRIME_MONTANT is 'PRIME_MONTANT';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.MESSAGE_ASSURE is 'MESSAGE_ASSURE';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.RUE_NUMERO_ASSURE is 'Rue numéro assuré';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.ID_CONTRIBUABLE is 'id contribuable';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.PRIME_PERIODE_FIN is 'PRIME_PERIODE_FIN';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.NSS_ASSURE is 'NSS ASSURE';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.TYPE_SUBSIDE is 'Type de subside';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.COST_SHARING_PRIME_DEBUT is 'COST_SHARING_PRIME_DEBUT';
COMMENT ON COLUMN SCHEMA.MASDXCOPA.COST_SHARING_PRIME_FIN is 'COST_SHARING_PRIME_FIN';
--========================================================================================================================
-- SQL Script for create table (MASDXCODEB)
-- Simple model's class name : (ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCODebiteur)
--========================================================================================================================
CREATE TABLE SCHEMA.MASDXCODEB
(
ID NUMERIC(15,0) NOT NULL,
ID_CONTRIBUABLE NUMERIC(15) NOT NULL,
ID_FAMILLE NUMERIC(15) NOT NULL,
ID_SEDEX NUMERIC(15) NOT NULL,
NSS_DEBITEUR VARCHAR(16) ,
NOM_PRENOM_DEBITEUR VARCHAR(100) ,
NPA_LOCALITE_DEBITEUR VARCHAR(100) ,
RUE_NUMERO_DEBITEUR VARCHAR(100) ,
CS_ACTE NUMERIC(8,0) ,
INTERETS NUMERIC(15,2) ,
TOTAL NUMERIC(15,2) ,
FRAIS NUMERIC(15,2) ,
MESSAGE_DEBITEUR VARCHAR(255) ,
CSPY VARCHAR(24) NOT NULL,
PSPY VARCHAR(24) NOT NULL,
PRIMARY KEY(ID)

);
COMMENT ON TABLE SCHEMA.MASDXCODEB is 'Enregistrement Annonce Sedex contentieux débiteur';
COMMENT ON COLUMN SCHEMA.MASDXCODEB.ID is 'clé primaire';
COMMENT ON COLUMN SCHEMA.MASDXCODEB.TOTAL is 'total';
COMMENT ON COLUMN SCHEMA.MASDXCODEB.PSPY is 'spy - Champ espion, défini quand et qui a effectué la dernière modification de l''entité';
COMMENT ON COLUMN SCHEMA.MASDXCODEB.ID_FAMILLE is 'IDFAMILLE';
COMMENT ON COLUMN SCHEMA.MASDXCODEB.ID_SEDEX is 'IDSEDEX';
COMMENT ON COLUMN SCHEMA.MASDXCODEB.NPA_LOCALITE_DEBITEUR is 'NPA localité débiteur';
COMMENT ON COLUMN SCHEMA.MASDXCODEB.CSPY is 'spy - Champ espion, défini quand et qui a créé l''entité';
COMMENT ON COLUMN SCHEMA.MASDXCODEB.MESSAGE_DEBITEUR is 'NSS DEBITEUR';
COMMENT ON COLUMN SCHEMA.MASDXCODEB.RUE_NUMERO_DEBITEUR is 'Rue numéro débiteur';
COMMENT ON COLUMN SCHEMA.MASDXCODEB.INTERETS is 'Interets';
COMMENT ON COLUMN SCHEMA.MASDXCODEB.CS_ACTE is 'TYPE_ACTE';
COMMENT ON COLUMN SCHEMA.MASDXCODEB.NOM_PRENOM_DEBITEUR is 'NOM_PRENOM_DEBITEUR';
COMMENT ON COLUMN SCHEMA.MASDXCODEB.ID_CONTRIBUABLE is 'id contribuable';
COMMENT ON COLUMN SCHEMA.MASDXCODEB.FRAIS is 'frais';
COMMENT ON COLUMN SCHEMA.MASDXCODEB.NSS_DEBITEUR is 'NSS DEBITEUR';
--========================================================================================================================
-- SQL Script for create table (MASDXCOP)
-- Simple model's class name : (ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOPersonne)
--========================================================================================================================
CREATE TABLE SCHEMA.MASDXCOP
(
ID NUMERIC(15,0) NOT NULL,
IDCONT NUMERIC(15,0) NOT NULL,
IDFAMI NUMERIC(15,0) NOT NULL,
IDSEDEX NUMERIC(15,0) ,
CSPY VARCHAR(24) NOT NULL,
PSPY VARCHAR(24) NOT NULL,
PRIMARY KEY(ID)

);
COMMENT ON TABLE SCHEMA.MASDXCOP is 'Enregistrement Annonce Sedex contentieux (personnes)';
COMMENT ON COLUMN SCHEMA.MASDXCOP.ID is 'clé primaire';
COMMENT ON COLUMN SCHEMA.MASDXCOP.PSPY is 'spy - Champ espion, défini quand et qui a effectué la dernière modification de l''entité';
COMMENT ON COLUMN SCHEMA.MASDXCOP.IDFAMI is 'ID_MEMBRE_FAMILLE';
COMMENT ON COLUMN SCHEMA.MASDXCOP.IDSEDEX is 'Id sedex';
COMMENT ON COLUMN SCHEMA.MASDXCOP.IDCONT is 'ID_CONTRIBUABLE_DOSSIER';
COMMENT ON COLUMN SCHEMA.MASDXCOP.CSPY is 'spy - Champ espion, défini quand et qui a créé l''entité';
--========================================================================================================================
-- SQL Script for create table (MASDXCO_XML)
-- Simple model's class name : (ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOXML)
--========================================================================================================================
CREATE TABLE SCHEMA.MASDXCO_XML
(
ID NUMERIC(15,0) NOT NULL,
IDSEDEX NUMERIC(15,0) NOT NULL,
MSGID VARCHAR(40) ,
XML CLOB ,
CSPY VARCHAR(24) NOT NULL,
PSPY VARCHAR(24) NOT NULL,
PRIMARY KEY(ID)

);
COMMENT ON TABLE SCHEMA.MASDXCO_XML is 'Contenus XML Annonce Sedex contentieux';
COMMENT ON COLUMN SCHEMA.MASDXCO_XML.ID is 'clé primaire';
COMMENT ON COLUMN SCHEMA.MASDXCO_XML.PSPY is 'spy - Champ espion, défini quand et qui a effectué la dernière modification de l''entité';
COMMENT ON COLUMN SCHEMA.MASDXCO_XML.XML is 'XML';
COMMENT ON COLUMN SCHEMA.MASDXCO_XML.MSGID is 'Message ID';
COMMENT ON COLUMN SCHEMA.MASDXCO_XML.IDSEDEX is 'ID annonce SEDEX';
COMMENT ON COLUMN SCHEMA.MASDXCO_XML.CSPY is 'spy - Champ espion, défini quand et qui a créé l''entité';

--========================================================================================================================
-- SQL Script for create table (MASDXCOPMT)
-- Simple model's class name : (ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOPaiements)
--========================================================================================================================
CREATE TABLE SCHEMA.MASDXCOPMT
(
ID  NUMERIC(15,0)  NOT NULL, 
ID_DEBITEUR  NUMERIC(15)  NOT NULL, 
PAIEMENT_TOTAL_AMOUT  NUMERIC(15,2)  , 
PAIEMENT_CATEGORY  VARCHAR(1)  , 
CSPY  VARCHAR(24)  NOT NULL, 
PSPY  VARCHAR(24)  NOT NULL, 
PRIMARY KEY(ID) 

);
COMMENT ON TABLE SCHEMA.MASDXCOPMT is 'Enregistrement Annonce Sedex paiements';
COMMENT ON COLUMN SCHEMA.MASDXCOPMT.ID is 'clé primaire';
COMMENT ON COLUMN SCHEMA.MASDXCOPMT.PAIEMENT_TOTAL_AMOUT is 'Montant total paiement';
COMMENT ON COLUMN SCHEMA.MASDXCOPMT.PSPY is 'spy - Champ espion, défini quand et qui a effectué la dernière modification de l''entité';
COMMENT ON COLUMN SCHEMA.MASDXCOPMT.PAIEMENT_CATEGORY is 'Type paiement';
COMMENT ON COLUMN SCHEMA.MASDXCOPMT.ID_DEBITEUR is 'IDSEDEX';
COMMENT ON COLUMN SCHEMA.MASDXCOPMT.CSPY is 'spy - Champ espion, défini quand et qui a créé l''entité';