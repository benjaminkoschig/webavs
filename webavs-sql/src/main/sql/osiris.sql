INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.es.token.duration','1',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('osiris.eBill.formulaire.bascule','true',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- Incident I220309_015
ALTER TABLE SCHEMA.CAEI ALTER NOM SET DATA TYPE varchar(70);
ALTER TABLE SCHEMA.CAEI ALTER PRENOM SET DATA TYPE varchar(70);
ALTER TABLE SCHEMA.CAEI ALTER ENTREPRISE SET DATA TYPE varchar(70);
reorg table SCHEMA.CAEI;
-- call sysproc.admin_cmd('CAEI');

ALTER TABLE SCHEMA.CAET ALTER PRENOM SET DATA TYPE varchar(70);
ALTER TABLE SCHEMA.CAET ALTER ENTREPRISE SET DATA TYPE varchar(70);
ALTER TABLE SCHEMA.CAET ALTER NOM SET DATA TYPE varchar(70);
reorg table SCHEMA.CAET;
-- call sysproc.admin_cmd('CAET');

INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (517046 , 'PYROLE    ', 1, 1, 0, 0, 'PTRA', 2, 1, 2, 2, 2, 2, 10500017, 0,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (517046, 'D', '          ', 'Ptra', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (517046, 'F', '          ', 'Ptra', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');


--eBill
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('osiris.eBill.email.confirmation','', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
ALTER TABLE SCHEMA.CACPTAP ADD COLUMN EBILLDATEINSCRIPTION DECIMAL(8);
reorg table SCHEMA.CACPTAP;
-- call sysproc.admin_cmd('CACPTAP');

--QR Factures ordre de versement
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (210003, 'OSITYPOVE', 1, 1, 0, 0, 'QR', 2, 1, 2, 2, 2, 2, 10200010, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (210003, 'D', 'QR', 'QR', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (210003, 'F', 'QR', 'QR', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');