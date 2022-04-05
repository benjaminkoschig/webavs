INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.es.token.duration','1',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('osiris.eBill.formulaire.bascule','true',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- Incident I220309_015
ALTER TABLE SCHEMA.CAEI ALTER NOM SET DATA TYPE varchar(70);
ALTER TABLE SCHEMA.CAEI ALTER PRENOM SET DATA TYPE varchar(70);
ALTER TABLE SCHEMA.CAEI ALTER ENTREPRISE SET DATA TYPE varchar(70);
-- reorg table SCHEMA.CAEI;

ALTER TABLE SCHEMA.CAET ALTER PRENOM SET DATA TYPE varchar(70);
ALTER TABLE SCHEMA.CAET ALTER ENTREPRISE SET DATA TYPE varchar(70);
ALTER TABLE SCHEMA.CAET ALTER NOM SET DATA TYPE varchar(70);
-- reorg table SCHEMA.CAET;

--eBill
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('osiris.eBill.email.confirmation','', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');