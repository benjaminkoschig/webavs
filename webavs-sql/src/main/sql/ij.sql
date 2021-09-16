
-- ACOR
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('ij.acor.utiliser.version.web','false','20210729120000Globaz    ','20210729120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.acor.adresse.web','','20210729120000Globaz    ','20210729120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.acor.navigateur','','20210729120000Globaz    ','20210729120000Globaz    ');
DELETE FROM SCHEMA.JADEPROP where PROPNAME = 'corvus.acor.adresse.web';
DELETE FROM SCHEMA.JADEPROP where PROPNAME = 'corvus.acor.navigateur';

-- FPI
create table SCHEMA.IJFPI
(
    ID_PRONONCE_FPI DECIMAL(15) not null
        constraint PK_FPI
            primary key,
    SITUATION_ASSURE DECIMAL(8),
    ID_DERNIER_REVENU DECIMAL(15),
--     XGBCAV CHARACTER(1),
--     XGBCAC CHARACTER(1),
    PSPY   CHARACTER(24)
);

alter table SCHEMA.IJPRONAI
    add XBBM8A CHAR(1);
REORG TABLE SCHEMA.IJPRONAI;

INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
    VALUES (52402005, 'IJTYPE', 5, 1, 0, 0, 'FPI', 2, 1, 2, 2, 2, 2, 51400002, 0, 'spy                     ');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52402005, 'F', '5', 'FPI', 'spy                     ');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52402005, 'D', '5', '[de]FPI', 'spy                     ');

INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (51400034, 'IJMODECFPI', 0, 1, 3, 0, 'Mode_calcul_fpi', 0, 2, 2, 2, 2, 0, 0, 0, VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52434001, 'IJMODECFPI', 1, 1, 0, 0, 'FPI_avec_contrat_apprentissage', 2, 1, 2, 2, 2, 2, 51400034, 0, VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52434002, 'IJMODECFPI', 2, 1, 0, 0, 'FPI_sans_contrat_apprentissage', 2, 1, 2, 2, 2, 2, 51400034, 0, VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52434003, 'IJMODECFPI', 3, 1, 0, 0, 'FPI_sans_stage', 2, 1, 2, 2, 2, 2, 51400034, 0, VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52434004, 'IJMODECFPI', 4, 1, 0, 0, 'FPI_avec_stage', 2, 1, 2, 2, 2, 2, 51400034, 0, VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52434005, 'IJMODECFPI', 5, 1, 0, 0, 'Autre_formation', 2, 1, 2, 2, 2, 2, 51400034, 0, VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52434006, 'IJMODECFPI', 7, 1, 0, 0, 'Cas_special_1', 2, 1, 2, 2, 2, 2, 51400034, 0, VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52434007, 'IJMODECFPI', 8, 1, 0, 0, 'Cas_special_2', 2, 1, 2, 2, 2, 2, 51400034, 0, VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (51400034, 'F', '', 'Mode_calcul', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52434001, 'D', '1', '[de]FPI avec contrat d''apprentissage (AFC ou CFC)', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52434001, 'F', '1', 'FPI avec contrat d''apprentissage (AFC ou CFC)', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52434002, 'D', '2', '[de]FPI sans contrat d''apprentissage (activité auxiliaire/atelier protégé)', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52434002, 'F', '2', 'FPI sans contrat d''apprentissage (activité auxiliaire/atelier protégé)', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52434003, 'D', '3', '[de]FPI sans stage rémunéré', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52434003, 'F', '3', 'FPI sans stage rémunéré', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52434004, 'D', '4', '[de]FPI avec stage rémunéré', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52434004, 'F', '4', 'FPI avec stage rémunéré', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52434005, 'D', '5', '[de]Autre formation (formation AI, PRAINSOS, préparation ciblée en vue d’une FPI)', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52434005, 'F', '5', 'Autre formation (formation AI, PRAINSOS, préparation ciblée en vue d’une FPI)', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52434006, 'D', '6', '[de]Cas spécial 1 : selon l''art. 22 al. 2 let. b (assurés au bénéfice de mesures médicales de réadaptation au sens de l''art. 12 LAI)', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52434006, 'F', '6 ', 'Cas spécial 1 : selon l''art. 22 al. 2 let. b (assurés au bénéfice de mesures médicales de réadaptation au sens de l''art. 12 LAI)', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52434007, 'D', '7', '[de]Cas spécial 2 : selon l''art. 22 al. 2 let. b (assurés au bénéfice de mesures de réinsertion au sens de l''art. 14a LAI)', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52434007, 'F', '7', 'Cas spécial 2 : selon l''art. 22 al. 2 let. b (assurés au bénéfice de mesures de réinsertion au sens de l''art. 14a LAI)', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
