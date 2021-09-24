--POAVS-3768 Ajout code sys SPEN

-- Référence Rubrique
-- 1 seule référence de créer pour le moment.
-- INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
--                     PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
-- VALUES (237317, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_HOME_SPEN', 2, 2, 2, 2, 2, 2, 10200038, 0,
--         VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
--
-- INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
-- VALUES (237317, 'D', '          ', '[de]PC en home SPEN à restituer',
--         VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
--
-- INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
-- VALUES (237317, 'F', '          ', 'PC en home SPEN à restituer',
--         VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);


----- POAVS-3768 - SPEN

--Ajout code sys SPEN
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64025010, 'PCSRVETA  ', 8, 1, 0, 0, 'SERVICE_SPEN_VD', 2, 1, 2, 2, 2, 2, 63000025, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025010, 'D', '          ', 'SPEN VD',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025010, 'F', '          ', 'SPEN VD',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

-- Creation Argent de poche
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64072006, 'PCTYPARG  ', 1, 1, 0, 0, 'SPEN_VD', 2, 1, 2, 2, 2, 2, 63000072, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072006, 'D', '          ',
        '[de]SPEN VD',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072006, 'F', '          ',
        'SPEN VD',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

-- Variable Métier
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008095, 'PCTYPVMET ', 85, 1, 0, 0, 'SPEN', 2, 1, 2, 2, 2, 2, 63000008, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008095, 'D', '          ', '[de]SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008095, 'F', '          ', 'SPEN',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

------------POAVS-3769 : DGEJ
-- DGEJ-SESAF / DGEJ-FOYER / DGEJ-FA
-- DGEJ-SESAF - Code Sys
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64025011, 'PCSRVETA  ', 9, 1, 0, 0, 'SERVICE_DGEJ_SESAF', 2, 1, 2, 2, 2, 2, 63000025, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025011, 'D', '          ', 'DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025011, 'F', '          ', 'DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

-- DGEJ-FOYER - Code Sys
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64025012, 'PCSRVETA  ', 10, 1, 0, 0, 'SERVICE_DGEJ_FOYER', 2, 1, 2, 2, 2, 2, 63000025, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025012, 'D', '          ', 'DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025012, 'F', '          ', 'DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);


-- DGEJ-FA - Code Sys
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64025013, 'PCSRVETA  ', 11, 1, 0, 0, 'SERVICE_DGEJ_FA', 2, 1, 2, 2, 2, 2, 63000025, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025013, 'D', '          ', 'DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64025013, 'F', '          ', 'DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

-- Creation Argent de poche
--DGEJ-SESAF - Argent de poche
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64072007, 'PCTYPARG  ', 1, 1, 0, 0, 'DGEJ-SESAF', 2, 1, 2, 2, 2, 2, 63000072, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072007, 'D', '          ',
        '[de]DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072007, 'F', '          ',
        'DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

--DGEJ-FOYER - Argent de poche
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64072008, 'PCTYPARG  ', 1, 1, 0, 0, 'DGEJ-FOYER', 2, 1, 2, 2, 2, 2, 63000072, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072008, 'D', '          ',
        '[de]DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072008, 'F', '          ',
        'DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

--DGEJ-FA - Argent de poche
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64072009, 'PCTYPARG  ', 1, 1, 0, 0, 'DGEJ-FA', 2, 1, 2, 2, 2, 2, 63000072, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072009, 'D', '          ',
        '[de]DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64072009, 'F', '          ',
        'DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);


-- Variable Métier
-- DGEJ-SESAF - Variable Métier
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008096, 'PCTYPVMET ', 86, 1, 0, 0, 'DGEJ-SESAF', 2, 1, 2, 2, 2, 2, 63000008, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008096, 'D', '          ', '[de]DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008096, 'F', '          ', 'DGEJ-SESAF',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

-- DGEJ-FOYER - Variable Métier
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008097, 'PCTYPVMET ', 87, 1, 0, 0, 'DGEJ-FOYER', 2, 1, 2, 2, 2, 2, 63000008, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008097, 'D', '          ', '[de]DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008097, 'F', '          ', 'DGEJ-FOYER',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

-- DGEJ-FA - Variable Métier
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008098, 'PCTYPVMET ', 88, 1, 0, 0, 'DGEJ-FA', 2, 1, 2, 2, 2, 2, 63000008, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008098, 'D', '          ', '[de]DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008098, 'F', '          ', 'DGEJ-FA',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);


------ POAVS-3766 - Frais entretien plus de 20 ans
-- Creation code sys Immobilier Principale
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008099, 'PCTYPVMET  ', 89, 1, 0, 0, 'FRAIS_ENTRETIEN_IMMEUBLE_PLUS_20_ANS_PRINCIPALE', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008099, 'D', '          ', '[de]Frais entretien immeubles habitation principale de plus de 20 ans',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008099, 'F', '          ', 'Frais entretien immeubles habitation principale de plus de 20 ans',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

-- Creation code sys Immobilier Annexe plus de 20 ans
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008100, 'PCTYPVMET  ', 89, 1, 0, 0, 'FRAIS_ENTRETIEN_IMMEUBLE_PLUS_20_ANS_ANNEXE', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008100, 'D', '          ', '[de]Frais entretien immeubles habitation annexe de plus de 20 ans',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008100, 'F', '          ', 'Frais entretien immeubles habitation annexe de plus de 20 ans',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

-- Creation code sys Immobilier Annexe moins de 20 ans
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (64008101, 'PCTYPVMET  ', 89, 1, 0, 0, 'FRAIS_ENTRETIEN_IMMEUBLE_MOINS_20_ANS_ANNEXE', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008101, 'D', '          ', '[de]Frais entretien immeubles habitation annexe de moins de 20 ans',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (64008101, 'F', '          ', 'Frais entretien immeubles habitation annexe de moins de 20 ans',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

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