---------------------------------------------------------------
-----   FRAMEWORK.SQL
---------------------------------------------------------------
-- S211222_008 Sécurisation des enpoints
-- Ajout de paramètres pour les droits d'accès aux WS des caisses
INSERT INTO SCHEMA.FWPARP (PPARAP, PPACDI, PPARIA, PPADDE, PPARPD, PCOSID, PPARPF, PPARVA, PPRAVN, PPRADE, CSTYUN, PCOITC, PSPY)
VALUES ('FRAMEWORK', 'EWSACNT', 1, 20220101, 0.000000, 0, 0.000000, 'yyInrFd/GnxAtYNNLz/ssQ==', 0.000000, 'Account WS : CCB', 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWPARP (PPARAP, PPACDI, PPARIA, PPADDE, PPARPD, PCOSID, PPARPF, PPARVA, PPRAVN, PPRADE, CSTYUN, PCOITC, PSPY)
VALUES ('FRAMEWORK', 'EWSACNT', 2, 20220101, 0.000000, 0, 0.000000, 'HThu1+nro1IrbFfUzfp1rA==', 0.000000, 'Account WS : CCVS', 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWPARP (PPARAP, PPACDI, PPARIA, PPADDE, PPARPD, PCOSID, PPARPF, PPARVA, PPRAVN, PPRADE, CSTYUN, PCOITC, PSPY)
VALUES ('FRAMEWORK', 'EWSACNT', 3, 20220101, 0.000000, 0, 0.000000, '65NXSd2aUq33Z/RZkX6yHQ==', 0.000000, 'Account WS : FVE', 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');