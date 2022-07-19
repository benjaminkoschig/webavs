---------------------------------------------------------------
-----   AL.SQL
---------------------------------------------------------------

-- CCVD Activation impot source : ajout des codes système liste (pour toutes les caisses)
INSERT INTO SCHEMA.FWCOSP
(PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (68025001, 'PTLISTAF', 1, 1, 0, 0, 'Détail-par-CAF-et-travailleur', 2, 1, 2, 2, 2, 2, 67000025, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP
(PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (68025001, 'D', 'DCAFT', '?Détail par CAF et travailleur', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP
(PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (68025001, 'F', 'DCAFT', 'Détail par CAF et travailleur', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP
(PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (68025001, 'I', 'DCAFT', '?Détail par CAF et travailleur', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOSP
(PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (68025002, 'PTLISTAF', 1, 1, 0, 0, 'Retenue-et-commission-par-CAF', 2, 1, 2, 2, 2, 2, 67000025, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP
(PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (68025002, 'D', 'RCCAF', '?Retenue et commission par CAF', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP
(PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (68025002, 'F', 'RCCAF', 'Retenue et commission par CAF', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP
(PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (68025002, 'I', 'RCCAF', '?Retenue et commission par CAF', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');