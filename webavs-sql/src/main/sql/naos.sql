-- FVE - S210312_009 - Cotisations nouvelles assurances
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (833004, 'VETYPCALCL', 1, 1, 0, 0, 'MONTANT_FIXE', 2, 1, 2, 2, 2, 2, 10800033, 0, VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (833004, 'D', 'COT       ', '[de]Montant fixe', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (833004, 'F', 'COT       ', 'Montant fixe', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (812044, 'VETYPEASSU', 1, 2, 0, 0, 'CRP_BASIC', 2, 2, 2, 2, 2, 2, 10800012, 0, VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (812044, 'D', '          ', '[de]CRP Basic', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (812044, 'F', '          ', 'CRP Basic', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');