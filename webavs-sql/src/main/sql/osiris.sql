--DEBUT DU REPORT DES SCRIPTS DE LA BRANCHE 1.17.1-4-ISO20022
--consolidation RC01 et RC02

--retrait du choix des type d'og 
delete from SCHEMA.fwcoup where pcosid in(select pcosid from SCHEMA.fwcosp where pptygr ='OSIOGTYA');
delete from SCHEMA.fwcosp where pptygr ='OSIOGTYA';

--fin de consolidation RC01 et RC02

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (211003,'OSIOGRBVR',1,1,0,0,'BVR_camt054',2, 2,2,2,2,2,10200011,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (211003,'D','ES054','ESR camt054','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (211003,'F','BV054','BVR camt054','spy');

create table schema.CAYRFI(
	YRFIID NUMERIC(15,0) NOT NULL PRIMARY KEY,
	YRFIFN VARCHAR(255) NOT NULL,
	YRFITY VARCHAR(255) NOT NULL,
	YRFIST VARCHAR(50) NOT NULL,
	YRFIRE VARCHAR(255),
	YRFIDC NUMERIC(15,0) NOT NULL,
	YRFIIB VARCHAR(255),
	PSPY CHAR(24) NOT NULL
);
INSERT INTO SCHEMA.FWINCP (PINCID, PCOSID, PINCAN, PINCLI, PINCVA, PSPY) VALUES ('CAYRFI', 0, 0, '', 1, '20170116100000globazf');

create table schema.CAYRIF(
	YRIFID NUMERIC(15,0) NOT NULL PRIMARY KEY,
	YRIFFN VARCHAR(255) NOT NULL,
	YRIFDC NUMERIC(15,0) NOT NULL,
	PSPY CHAR(24) NOT NULL
);
INSERT INTO SCHEMA.FWINCP (PINCID, PCOSID, PINCAN, PINCLI, PINCVA, PSPY) VALUES ('CAYRIF', 0, 0, '', 1, '20170116100000globazf');

create table schema.CAJOISO(
	JOISOID	NUMERIC(15,0) NOT NULL PRIMARY KEY,
	JOISOIJ NUMERIC(15,0) NOT NULL,
	JOISOME VARCHAR(255) NOT NULL,
	JOISODT VARCHAR(255) NOT NULL,
	JOISONT VARCHAR(255) NOT NULL,
	JOISOFN VARCHAR(255) NOT NULL,
	PSPY CHAR(24) NOT NULL
);
INSERT INTO SCHEMA.FWINCP (PINCID, PCOSID, PINCAN, PINCLI, PINCVA, PSPY) VALUES ('CAJOISO', 0, 0, '', 1, '20170116100000globazf');

alter table SCHEMA.CAOPBVP add column ACCTSVCRREF VARCHAR(255);
alter table SCHEMA.CAOPBVP add column BKTXCD VARCHAR(255);
alter table SCHEMA.CAOPBVP add column DBTR VARCHAR(255);
reorg table SCHEMA.CAOPBVP;


--Drop de la colonne (ISOTYPEAVIS) impossible avec AS400, donc manipulation sp�cifique � faire
CREATE TABLE SCHEMA.CAORGRP_NEW
(
   IDORDREGROUPE NUMERIC(9,0) PRIMARY KEY NOT NULL,
   IDJOURNAL NUMERIC(9,0),
   IDORGANEEXECUTION NUMERIC(9,0) NOT NULL,
   IDPOSTEJOURNAL NUMERIC(9,0),
   MOTIF char(50),
   DATECREATION NUMERIC(8,0),
   DATEECHEANCE NUMERIC(8,0),
   DATETRANSMISSION NUMERIC(8,0),
   NOMBRETRANSACTIONS NUMERIC(7,0),
   TOTAL NUMERIC(15,2),
   NUMEROOG NUMERIC(3,0),
   NOMSUPPORT char(10),
   TYPEORDREGROUPE NUMERIC(9,0),
   NATUREORDRESLIVRES NUMERIC(9,0),
   ESTCONFIDENTIEL char(1),
   IDLOG NUMERIC(9,0),
   PSPY char(24),
   ETAT NUMERIC(9,0),
   ISOTRANSACSTAT NUMERIC(9,0),
   ISOORDRESTAT NUMERIC(9,0),
   ISOGEST varchar(24),
   ISOHAUTEPRIO varchar(1),
   ISONUMLIVR varchar(35)
);

CREATE INDEX CAORGRL1_NEW ON SCHEMA.CAORGRP_NEW(IDJOURNAL);
CREATE INDEX CAORGRL3_NEW ON SCHEMA.CAORGRP_NEW(IDPOSTEJOURNAL);
CREATE INDEX CAORGRL2_NEW ON SCHEMA.CAORGRP_NEW(IDORGANEEXECUTION);
CREATE INDEX CAORGRL4_NEW ON SCHEMA.CAORGRP_NEW(IDLOG);

insert into SCHEMA.CAORGRP_NEW ( select 
   IDORDREGROUPE,
   IDJOURNAL,
   IDORGANEEXECUTION,
   IDPOSTEJOURNAL,
   MOTIF,
   DATECREATION,
   DATEECHEANCE,
   DATETRANSMISSION,
   NOMBRETRANSACTIONS,
   TOTAL,
   NUMEROOG,
   NOMSUPPORT,
   TYPEORDREGROUPE,
   NATUREORDRESLIVRES,
   ESTCONFIDENTIEL,
   IDLOG,
   PSPY,
   ETAT,
   ISOTRANSACSTAT,
   ISOORDRESTAT,
   ISOGEST,
   ISOHAUTEPRIO,
   ISONUMLIVR from SCHEMA.CAORGRP);

RENAME TABLE SCHEMA.CAORGRP  TO CAORGRP_OLD;
rename index SCHEMA.CAORGRL1 to CAORGRL1_OLD;
rename index SCHEMA.CAORGRL2 to CAORGRL2_OLD;
rename index SCHEMA.CAORGRL3 to CAORGRL3_OLD;
rename index SCHEMA.CAORGRL4 to CAORGRL4_OLD;
rename index SCHEMA.CAORGRP to CAORGRP_OLD;

RENAME TABLE SCHEMA.CAORGRP_NEW  TO CAORGRP;
rename index SCHEMA.CAORGRL1_NEW to CAORGRL1;
rename index SCHEMA.CAORGRL2_NEW to CAORGRL2;
rename index SCHEMA.CAORGRL3_NEW to CAORGRL3;
rename index SCHEMA.CAORGRL4_NEW to CAORGRL4;
rename index SCHEMA.CAORGRP_NEW to CAORGRP;
--FIN de l'ex�cution du drop de la colonnne (ISOTYPEAVIS)


--recr�ation de la table des rejet OSIRIS
DROP TABLE SCHEMA.CAORREJ;
CREATE TABLE SCHEMA.CAORREJ (
	IDORRE NUMERIC(9,0) primary key not null,
	IDORDR NUMERIC(9,0) not null,
	IDORGR NUMERIC(9,0) NOT NULL,
	CODE VARCHAR(32),
	PROPRI VARCHAR(255),
	ADDITI VARCHAR(255),
	PSPY CHAR(24)
	);
CREATE INDEX SCHEMA.CAORREJ1 ON SCHEMA.CAORREJ ( IDORGR DESC, IDORDR);
	
UPDATE SCHEMA.FWCOUP SET PCOLUT = 'g�n�r�' WHERE PCOSID = 208005 AND PLAIDE = 'F';
UPDATE SCHEMA.FWCOUP SET PCOLUT = 'zu �bermitteln' WHERE PCOSID = 262001 AND PLAIDE = 'D';
UPDATE SCHEMA.FWCOUP SET PCOLUT = '�bermittelt' WHERE PCOSID = 262002 AND PLAIDE = 'D';
UPDATE SCHEMA.FWCOUP SET PCOLUT = 'best�tigt' WHERE PCOSID = 262003 AND PLAIDE = 'D';

--FIN DU REPORT DES SCRIPTS DE LA BRANCHE 1.17.1-4-ISO20022

INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL) VALUES ('osiris.recalcul.soldes.compteAnnexe.section', 'true');

