---------------------------------------------------------------
-----   CORVUS.SQL
---------------------------------------------------------------

ALTER TABLE SCHEMA.REBACAL
    ALTER COLUMN QUOTITE_RENTE SET DATA TYPE DECIMAL(6,3);
reorg table SCHEMA.REBACAL;
-- call sysproc.admin_cmd('REORG TABLE REBACAL');

ALTER TABLE SCHEMA.REHISTR
ALTER COLUMN QUOTITE_RENTE SET DATA TYPE DECIMAL(6,3);
reorg TABLE SCHEMA.REHISTR;
-- call sysproc.admin_cmd('REORG TABLE REHISTR');


-- ajout des codes cas spéciaux
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52822065, 'RECASSPEC ', 65, 1, 0, 0, 'CAS_SPECIAUX_RENTE_65', 2, 1, 2, 2, 2, 2, 51800022, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52822065, 'D', '65', 'Code 65', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52822065, 'F', '65', 'Code 65', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

-- Ajout navsComplémentaire dans historique rente
ALTER TABLE SCHEMA.REHISTR
    ADD COLUMN ID_TIERS_COMP_1 DECIMAL(15);
ALTER TABLE SCHEMA.REHISTR
    ADD COLUMN ID_TIERS_COMP_2 DECIMAL(15);
reorg table SCHEMA.REHISTR;
-- call sysproc.admin_cmd('REORG TABLE REHISTR');