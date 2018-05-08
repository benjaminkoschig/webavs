-- Ajout de propriétés
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('common.ebusiness.connected','false');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('orion.adi.messageRefusGenerique','Ceci est un message d''erreur générique');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('orion.email.mutation.adresse.caf','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('orion.email.mutation.adresse.avs','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('orion.generic.user','userwebavs');

--Gestion des DAN complément
ALTER TABLE SCHEMA.EBPUCS_FILE ADD ANNEE_VERSEMENT NUMERIC(4,0);
ALTER TABLE SCHEMA.EBPUCS_FILE ADD TYPE_DECLARATION NUMERIC(2,0);
UPDATE SCHEMA.EBPUCS_FILE SET TYPE_DECLARATION=0;

--Ajout des tables pour les demandes ADI
DROP TABLE SCHEMA.EBDEM_MODIF_ACO;
DROP TABLE SCHEMA.EBDEM_MODIF_ACO_MESSAGE;

CREATE TABLE SCHEMA.EBDEM_MODIF_ACO
(ID DECIMAL(12,0) PRIMARY KEY NOT NULL,
IDDEMANDE_PORTAIL DECIMAL(12,0) NOT NULL,
IDAFFILIATION DECIMAL(12,0) NOT NULL,
NUMAFFILIE CHAR(25),
ANNEE DECIMAL(4,0),
REVENU DECIMAL(15,2),
CAPITAL DECIMAL(15,2),
CS_STATUT DECIMAL(8,0),
REMARQUE VARCHAR(255),
DATERECEPTION DECIMAL(8,0),
IDDECISION_REF DECIMAL(12,0) NOT NULL,
CSPY CHAR(24),
PSPY CHAR(24)
);

CREATE UNIQUE INDEX SCHEMA.EBDEM_MODIF_ACO ON SCHEMA.EBDEM_MODIF_ACO(ID);


CREATE TABLE SCHEMA.EBDEM_MODIF_ACO_MESSAGE
(ID DECIMAL(12,0) PRIMARY KEY NOT NULL,
IDDEMANDE DECIMAL(12,0) NOT NULL,
MESSAGE VARCHAR(255),
PSPY CHAR(24),
CSPY CHAR(24)
);

--Ajout d'une colonne pour stocker la remarque CP
ALTER TABLE SCHEMA.EBDEM_MODIF_ACO ADD COLUMN REMARQUE_CP VARCHAR(255);
REORG table SCHEMA.EBDEM_MODIF_ACO;

-- création des CS pour les motifs de radiations des dossiers AF
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='EBMOTRADAF');
delete from SCHEMA.FWCOSP where pptygr ='EBMOTRADAF';

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 11000003, 'EBMOTRADAF', 0,1,3,0, 'Motifs de radiation des dossiers AF', 0,2,2,2,2,0,0,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user)); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11000003, 'F', '1', 'Motifs de radiation des dossiers AF', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11000003, 'D', '1', 'Streichungsgründe der FZ Dossiers', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 11030001, 'EBMOTRADAF', 1 ,1,0,0, 'ACCIDENT', 2,1,2,2,2, 2 , 11000003 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11030001, 'F', '', 'Motif : fin du droit aux allocations familiales suite à l''incapacité de travail', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11030001, 'D', '', 'Grund: Ende des Anspruchs auf Kinderzulagen infolge Arbeitsunfähigkeit', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 11030002, 'EBMOTRADAF', 2 ,1,0,0, 'MALADIE', 2,1,2,2,2, 2 , 11000003 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11030002, 'F', '', 'Motif : fin du droit aux allocations familiales suite à l''incapacité de travail', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11030002, 'D', '', 'Grund: Ende des Anspruchs auf Kinderzulagen infolge Arbeitsunfähigkeit', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 11030003, 'EBMOTRADAF', 3 ,1,0,0, 'CONGE_MATERNITE', 2,1,2,2,2, 2 , 11000003 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11030003, 'F', '', 'Motif : fin du droit aux allocations familiales', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11030003, 'D', '', 'Grund: Ende des Anspruchs auf Kinderzulagen', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 11030004, 'EBMOTRADAF', 4 ,1,0,0, 'CONGE_NON_PAYE', 2,1,2,2,2, 2 , 11000003 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11030004, 'F', '', 'Motif : fin du droit aux allocations familiales (congé non payé)', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11030004, 'D', '', 'Grund: Ende des Anspruchs auf Kinderzulagen infolge eines unbezahlten Urlaubs', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 11030005, 'EBMOTRADAF', 5 ,1,0,0, 'VACANCES', 2,1,2,2,2, 2 , 11000003 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11030005, 'F', '', '', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11030005, 'D', '', '', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 11030006, 'EBMOTRADAF', 6 ,1,0,0, 'FIN_ACTIVITE', 2,1,2,2,2, 2 , 11000003 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11030006, 'F', '', 'Motif : fin de l''activité salariée', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11030006, 'D', '', 'Grund: Beendigung des Arbeitsverhältnisses', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 11030007, 'EBMOTRADAF', 7 ,1,0,0, 'DECES', 2,1,2,2,2, 2 , 11000003 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11030007, 'F', '', 'Motif : fin du droit aux allocations familiales', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 11030007, 'D', '', 'Grund: Ende des Anspruchs auf Kinderzulagen', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 