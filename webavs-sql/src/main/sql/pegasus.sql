--POAVS-3768 Ajout code sys SPEN

-- Référence Rubrique
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                    PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (237317, 'OSIREFRUB ', 1, 1, 0, 0, 'PC_HOME_SPEN', 2, 2, 2, 2, 2, 2, 10200038, 0,
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237317, 'D', '          ', '[de]PC en home SPEN à restituer',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (237317, 'F', '          ', 'PC en home SPEN à restituer',
        VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

