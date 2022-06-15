---------------------------------------------------------------
-----   AL.SQL
---------------------------------------------------------------

INSERT INTO SCHEMA.FWPARP (PPARAP, PPACDI, PPARIA, PPADDE, PPARPD, PCOSID, PPARPF, PPARVA, PPRAVN, PPRADE, CSTYUN, PCOITC, PSPY) VALUES ('AL', 'SALBMINMO', 0, 20220101, 0.000000, 1, 0.000000, '', 597, 'Salaire du b�n�ficiaire minimum par mois', 0, 0, VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWPARP (PPARAP, PPACDI, PPARIA, PPADDE, PPARPD, PCOSID, PPARPF, PPARVA, PPRAVN, PPRADE, CSTYUN, PCOITC, PSPY) VALUES ('AL', 'SALBMINAN', 0, 20220101, 0.000000, 1, 0.000000, '', 7170, 'Salaire du b�n�ficiaire minimum par ann�e', 0, 0, VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWPARP (PPARAP, PPACDI, PPARIA, PPADDE, PPARPD, PCOSID, PPARPF, PPARVA, PPRAVN, PPRADE, CSTYUN, PCOITC, PSPY) VALUES ('AL', 'REVENMAXMO', 0, 20220101, 0.000000, 1, 0.000000, '', 2390, 'Revenu propre de l''enfant maximum par mois', 0, 0, VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

INSERT INTO SCHEMA.FWPARP (PPARAP, PPACDI, PPARIA, PPADDE, PPARPD, PCOSID, PPARPF, PPARVA, PPRAVN, PPRADE, CSTYUN, PCOITC, PSPY) VALUES ('AL', 'REVEFMAXAN', 0, 20220101, 0.000000, 1, 0.000000, '', 28680, 'Revenu propre de l''enfant maximum par ann�e', 0, 0, VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');