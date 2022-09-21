---------------------------------------------------------------
-----   OSIRIS.SQL
---------------------------------------------------------------

-- eBill sursis plan de recouvrement eBillPrintable
ALTER TABLE SCHEMA.CAPLARP ADD COLUMN EBILLPRINTABLE VARCHAR(1);
reorg table SCHEMA.CAPLARP;
-- call sysproc.admin_cmd('REORG TABLE CAPLARP');


--UNIQUEMENT CCVD - S220215_006 refacturation cotisations minimales LPTra
INSERT INTO CCVDQUA.FWPARP (PPARAP, PPACDI, PPARIA, PPADDE, PPARPD, PCOSID, PPARPF, PPARVA, PPRAVN, PPRADE, CSTYUN, PCOITC, PSPY) VALUES ('NAOS      ', 'RUBRIQUE5 ', 0, 0, 0.000000, 818031, 0.000000, '31004', 0.000000, 'ben.LPTRA - rubrique', 0, 0, '20220905000000globaz      ');
INSERT INTO CCVDQUA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (818031, 'VEPARTICUL', 1, 2, 0, 0, 'Non_actif_benef_ptra', 2, 2, 2, 2, 2, 2, 10800018, 0, '20220905000000globaz       ');
INSERT INTO CCVDQUA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (818031, 'D', '          ', '[DE]Non-actif bénéficiaire LPTRA', '20220905000000globaz');
INSERT INTO CCVDQUA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (818031, 'F', '          ', 'Non-actif bénéficiaire LPTRA', '20220905000000globaz');
INSERT INTO CCVDQUA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (818031, 'I', '          ', '[IT]Non-actif bénéficiaire LPTRA', '20220905000000globaz');
