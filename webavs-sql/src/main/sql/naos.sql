delete from SCHEMA.FWCOSP where pcosid in(818030);
delete from SCHEMA.FWCOUP where pcosid in(818030);
insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (818030,'VEPARTICUL',1,1,0,0,'CLOTURE_RECAP_MANUELLE',2,1,2,2,2,2,10800018,0, 'spy');
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (818030,'F','CLOTRECAP','Clôture récap manuelle','spy');
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (818030,'D','CLOTRECAP','[DE]Clôture récap manuelle','spy');

ALTER TABLE SCHEMA.AFANOIDE ADD AIDNSS CHAR(20);
REORG TABLE SCHEMA.AFANOIDE allow read access;


-- Création de la table du paramétrage des priorités des jobs
CREATE TABLE SCHEMA.AF_CONTACT_FPV
(
   KEY varchar(255) PRIMARY KEY NOT NULL,
   AFFNUM char(15)  UNIQUE NOT NULL,
   NOM varchar(50) NOT NULL,
   PRENOM varchar(50) NOT NULL,
   SEXE varchar(15) NOT NULL,
   EMAIL varchar(100) NOT NULL,
   PROSPECTION varchar(1) NOT NULL,
   PSPY char(24),
   CSPY char(24)
);
CREATE UNIQUE INDEX AF_CONTACT_FPV ON SCHEMA.AF_CONTACT_FPV(KEY);

INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('naos.contactFpv','false','20190731120000Globaz    ','20190731120000Globaz    ');

