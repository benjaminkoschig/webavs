----- POAVS-3768 - SPEN

--Ajout code sys SPEN
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64025010, 'PCSRVETA  ', 8, 1, 0, 0, 'SERVICE_SPEN_VD', 2, 1, 2, 2, 2, 2, 63000025, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025010, 'D', '          ', 'SPEN VD',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025010, 'F', '          ', 'SPEN VD',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- Creation Argent de poche
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64072006, 'PCTYPARG  ', 1, 1, 0, 0, 'SPEN_VD', 2, 1, 2, 2, 2, 2, 63000072, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072006, 'D', '          ',
        '[de]SPEN VD',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072006, 'F', '          ',
        'SPEN VD',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- Variable M�tier
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008095, 'PCTYPVMET ', 85, 1, 0, 0, 'SPEN', 2, 1, 2, 2, 2, 2, 63000008, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008095, 'D', '          ', '[de]SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008095, 'F', '          ', 'SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');


-- SPEN - CCVS Variable M�tier - Plafond SPEN
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008103, 'PCTYPVMET ', 93, 1, 0, 0, 'PLAFOND_ANNUEL_SPEN', 2, 1, 2, 2, 2, 2, 63000008, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008103, 'D', '          ', '[de]Plafond journalier SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008103, 'F', '          ', 'Plafond journalier SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

------------POAVS-3769 : DGEJ
-- DGEJ-SESAF / DGEJ-FOYER / DGEJ-FA
-- DGEJ-SESAF - Code Sys
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64025011, 'PCSRVETA  ', 9, 1, 0, 0, 'SERVICE_DGEJ_SESAF', 2, 1, 2, 2, 2, 2, 63000025, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025011, 'D', '          ', 'DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025011, 'F', '          ', 'DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- DGEJ-FOYER - Code Sys
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64025012, 'PCSRVETA  ', 10, 1, 0, 0, 'SERVICE_DGEJ_FOYER', 2, 1, 2, 2, 2, 2, 63000025, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025012, 'D', '          ', 'DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025012, 'F', '          ', 'DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');


-- DGEJ-FA - Code Sys
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64025013, 'PCSRVETA  ', 11, 1, 0, 0, 'SERVICE_DGEJ_FA', 2, 1, 2, 2, 2, 2, 63000025, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025013, 'D', '          ', 'DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025013, 'F', '          ', 'DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- Creation Argent de poche
--DGEJ-SESAF - Argent de poche
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64072007, 'PCTYPARG  ', 1, 1, 0, 0, 'DGEJ-SESAF', 2, 1, 2, 2, 2, 2, 63000072, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072007, 'D', '          ',
        '[de]DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072007, 'F', '          ',
        'DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

--DGEJ-FOYER - Argent de poche
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64072008, 'PCTYPARG  ', 1, 1, 0, 0, 'DGEJ-FOYER', 2, 1, 2, 2, 2, 2, 63000072, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072008, 'D', '          ',
        '[de]DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072008, 'F', '          ',
        'DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

--DGEJ-FA - Argent de poche
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64072009, 'PCTYPARG  ', 1, 1, 0, 0, 'DGEJ-FA', 2, 1, 2, 2, 2, 2, 63000072, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072009, 'D', '          ',
        '[de]DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072009, 'F', '          ',
        'DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');


-- Variable M�tier
-- DGEJ-SESAF - Variable M�tier
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008096, 'PCTYPVMET ', 86, 1, 0, 0, 'DGEJ-SESAF', 2, 1, 2, 2, 2, 2, 63000008, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008096, 'D', '          ', '[de]DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008096, 'F', '          ', 'DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- DGEJ-FOYER - Variable M�tier
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008097, 'PCTYPVMET ', 87, 1, 0, 0, 'DGEJ-FOYER', 2, 1, 2, 2, 2, 2, 63000008, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008097, 'D', '          ', '[de]DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008097, 'F', '          ', 'DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- DGEJ-FA - Variable M�tier
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008098, 'PCTYPVMET ', 88, 1, 0, 0, 'DGEJ-FA', 2, 1, 2, 2, 2, 2, 63000008, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008098, 'D', '          ', '[de]DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008098, 'F', '          ', 'DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');


------ POAVS-3766 - Frais entretien plus de 20 ans
-- Creation code sys Immobilier Principale
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008099, 'PCTYPVMET  ', 89, 1, 0, 0, 'FRAIS_ENTRETIEN_IMMEUBLE_PLUS_20_ANS_PRINCIPALE', 2, 2, 2, 2, 2, 2,
        10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- UPDATE SCHEMA.FWCOSP
-- SET PCOSLI = 'FRAIS_ENTRETIEN_IMMEUBLE_PLUS_20_ANS_PRINCIPALE'
-- WHERE PCOSID = 64008099;

-- UPDATE SCHEMA.FWCOUP
--     SET PCOLUT = '[de]Frais entretien immeubles habitation principale de plus de 20 ans'
-- WHERE PCOSID = 64008099
-- AND PLAIDE = 'D';
--
-- UPDATE SCHEMA.FWCOUP
-- SET PCOLUT = 'Frais entretien immeubles habitation principale de plus de 20 ans'
-- WHERE PCOSID = 64008099
--   AND PLAIDE = 'F';

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008099, 'D', '          ', '[de]Frais entretien immeubles habitation principale de plus de 20 ans',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008099, 'F', '          ', 'Frais entretien immeubles habitation principale de plus de 20 ans',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- Creation code sys Immobilier Annexe plus de 20 ans
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008100, 'PCTYPVMET  ', 90, 1, 0, 0, 'FRAIS_ENTRETIEN_IMMEUBLE_PLUS_20_ANS_ANNEXE', 2, 2, 2, 2, 2, 2, 10200038,
        0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008100, 'D', '          ', '[de]Frais entretien immeubles habitation annexe de plus de 20 ans',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008100, 'F', '          ', 'Frais entretien immeubles habitation annexe de plus de 20 ans',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- Creation code sys Immobilier Annexe moins de 20 ans
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008101, 'PCTYPVMET  ', 91, 1, 0, 0, 'FRAIS_ENTRETIEN_IMMEUBLE_MOINS_20_ANS_ANNEXE', 2, 2, 2, 2, 2, 2,
        10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008101, 'D', '          ', '[de]Frais entretien immeubles habitation annexe de moins de 20 ans',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008101, 'F', '          ', 'Frais entretien immeubles habitation annexe de moins de 20 ans',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- Creation code sys Plafond loyer encaiss�
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008102, 'PCTYPVMET  ', 92, 1, 0, 0, 'PLAFOND_LOYERS_ENCAISSES', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008102, 'D', '          ', '[de]Plafond loyers encaiss�s',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008102, 'F', '          ', 'Plafond loyers encaiss�s',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- Update variable actuelle


-- POAVS-3766 - Entretien batiment plus de 20 ans
-- Ajout nouvelles colonnes
ALTER TABLE SCHEMA.PCBISHP
    ADD COLUMN CIBHPA NUMERIC(1) DEFAULT 0;

REORG TABLE SCHEMA.PCBISHP;
-- call SYSPROC.ADMIN_CMD('reorg TABLE SCHEMA.PCBISHP');

ALTER TABLE SCHEMA.PCBISPH
    ADD COLUMN CHBHPA NUMERIC(1) DEFAULT 0;

REORG TABLE SCHEMA.PCBISPH;
-- call SYSPROC.ADMIN_CMD('reorg TABLE SCHEMA.PCBISPH');

-- Colonne pour Immeuble commerciale
ALTER TABLE SCHEMA.PCBISHP
    ADD COLUMN CIBHIC NUMERIC(1) DEFAULT 0;

REORG TABLE SCHEMA.PCBISHP;
-- call SYSPROC.ADMIN_CMD('reorg TABLE SCHEMA.PCBISHP');

ALTER TABLE SCHEMA.PCBISPH
    ADD COLUMN CHBHIC NUMERIC(1) DEFAULT 0;

REORG TABLE SCHEMA.PCBISPH;
-- call SYSPROC.ADMIN_CMD('reorg TABLE SCHEMA.PCBISPH');


--POAVS-3768 Ajout code sys SPEN pour rubrique comptable

-- R�f�rence Rubrique
-- 1 seule r�f�rence de cr�er pour le moment.
-- AVS HOME SPEN
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237622, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_SPEN', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237622, 'D', '          ', '[de]PC AVS en home SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237622, 'F', '          ', 'PC AVS en home SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AVS HOME SPEN HC
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237623, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_HC_SPEN', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237623, 'D', '          ', '[de]PC AVS en home Hors Canton SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237623, 'F', '          ', 'PC AVS en home Hors Canton SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME SPEN
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237624, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_SPEN', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237624, 'D', '          ', '[de]PC AI en home SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237624, 'F', '          ', 'PC AI en home SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME SPEN HC
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237625, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_HC_SPEN', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237625, 'D', '          ', '[de]PC AI en home Hors Canton SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237625, 'F', '          ', 'PC AI en home Hors Canton SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

--

-- AVS HOME SPEN RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237626, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_SPEN_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237626, 'D', '          ', '[de]PC AVS Restitution en home SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237626, 'F', '          ', 'PC AVS Restitution en home SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AVS HOME SPEN HC RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237627, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_HC_SPEN_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237627, 'D', '          ', '[de]PC AVS Restitution en home Hors Canton SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237627, 'F', '          ', 'PC AVS Restitution en home Hors Canton SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME SPEN RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237628, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_SPEN_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237628, 'D', '          ', '[de]PC AI Restitution en home SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237628, 'F', '          ', 'PC AI Restitution en home SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME SPEN HC RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237629, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_HC_SPEN_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237629, 'D', '          ', '[de]PC AI Restitution en home Hors Canton SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237629, 'F', '          ', 'PC AI Restitution en home Hors Canton SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');


------------POAVS-3769 : DGEJ
-- SESAF

-- AVS HOME DGEJ-SESAF
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237630, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_DGEJ_SESAF', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237630, 'D', '          ', '[de]PC AVS en home DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237630, 'F', '          ', 'PC AVS en home DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AVS HOME SPEN HC
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237631, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_HC_DGEJ_SESAF', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237631, 'D', '          ', '[de]PC AVS en home Hors Canton DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237631, 'F', '          ', 'PC AVS en home Hors Canton DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME DGEJ-SESAF
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237632, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_DGEJ_SESAF', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237632, 'D', '          ', '[de]PC AI en home DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237632, 'F', '          ', 'PC AI en home DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME DGEJ-SESAF HC
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237633, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_HC_DGEJ_SESAF', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237633, 'D', '          ', '[de]PC AI en home Hors Canton DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237633, 'F', '          ', 'PC AI en home Hors Canton DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

--

-- AVS HOME DGEJ-SESAF RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237634, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_DGEJ_SESAF_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237634, 'D', '          ', '[de]PC AVS Restitution en home DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237634, 'F', '          ', 'PC AVS Restitution en home DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AVS HOME DGEJ-SESAF HC RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237635, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_HC_DGEJ_SESAF_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237635, 'D', '          ', '[de]PC AVS Restitution en home Hors Canton DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237635, 'F', '          ', 'PC AVS Restitution en home Hors Canton DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME DGEJ-SESAF RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237636, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_DGEJ_SESAF_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237636, 'D', '          ', '[de]PC AI Restitution en home DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237636, 'F', '          ', 'PC AI Restitution en home DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME DGEJ-SESAF HC RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237637, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_HC_DGEJ_SESAF_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237637, 'D', '          ', '[de]PC AI Restitution en home Hors Canton DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237637, 'F', '          ', 'PC AI Restitution en home Hors Canton DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');


-- FOYER

-- AVS HOME DGEJ-FOYER
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237638, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_DGEJ_FOYER', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237638, 'D', '          ', '[de]PC AVS en home DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237638, 'F', '          ', 'PC AVS en home DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AVS HOME DGEJ-FOYER HC
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237639, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_HC_DGEJ_FOYER', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237639, 'D', '          ', '[de]PC AVS en home Hors Canton DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237639, 'F', '          ', 'PC AVS en home Hors Canton DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME DGEJ-FOYER
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237640, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_DGEJ_FOYER', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237640, 'D', '          ', '[de]PC AI en home DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237640, 'F', '          ', 'PC AI en home DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME DGEJ-FOYER HC
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237641, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_HC_DGEJ_FOYER', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237641, 'D', '          ', '[de]PC AI en home Hors Canton DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237641, 'F', '          ', 'PC AI en home Hors Canton DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

--

-- AVS HOME DGEJ-FOYER RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237642, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_DGEJ_FOYER_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237642, 'D', '          ', '[de]PC AVS Restitution en home DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237642, 'F', '          ', 'PC AVS Restitution en home DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AVS HOME DGEJ-FOYER HC RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237643, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_HC_DGEJ_FOYER_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237643, 'D', '          ', '[de]PC AVS Restitution en home Hors Canton DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237643, 'F', '          ', 'PC AVS Restitution en home Hors Canton DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME DGEJ-FOYER RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237644, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_DGEJ_FOYER_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237644, 'D', '          ', '[de]PC AI Restitution en home DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237644, 'F', '          ', 'PC AI Restitution en home DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME DGEJ-FOYER HC RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237645, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_HC_DGEJ_FOYER_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237645, 'D', '          ', '[de]PC AI Restitution en home Hors Canton DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237645, 'F', '          ', 'PC AI Restitution en home Hors Canton DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- FA

-- AVS HOME DGEJ-FA
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237646, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_DGEJ_FA', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237646, 'D', '          ', '[de]PC AVS en home DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237646, 'F', '          ', 'PC AVS en home DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AVS HOME DGEJ-FA HC
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237647, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_HC_DGEJ_FA', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237647, 'D', '          ', '[de]PC AVS en home Hors Canton DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237647, 'F', '          ', 'PC AVS en home Hors Canton DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME DGEJ-FA
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237648, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_DGEJ_FA', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237648, 'D', '          ', '[de]PC AI en home DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237648, 'F', '          ', 'PC AI en home DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME DGEJ-FA HC
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237649, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_HC_DGEJ_FA', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237649, 'D', '          ', '[de]PC AI en home Hors Canton DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237649, 'F', '          ', 'PC AI en home Hors Canton DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

--

-- AVS HOME DGEJ-FA RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237650, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_DGEJ_FA_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237650, 'D', '          ', '[de]PC AVS Restitution en home DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237650, 'F', '          ', 'PC AVS Restitution en home DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AVS HOME DGEJ-FA HC RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237651, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AVS_HOME_HC_DGEJ_FA_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237651, 'D', '          ', '[de]PC AVS Restitution en home Hors Canton DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237651, 'F', '          ', 'PC AVS Restitution en home Hors Canton DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME DGEJ-FA RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237652, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_DGEJ_FA_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237652, 'D', '          ', '[de]PC AI Restitution en home DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237652, 'F', '          ', 'PC AI Restitution en home DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AI HOME DGEJ-FA HC RESTITUTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237653, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_AI_HOME_HC_DGEJ_FA_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237653, 'D', '          ', '[de]PC AI Restitution en home Hors Canton DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237653, 'F', '          ', 'PC AI Restitution en home Hors Canton DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');