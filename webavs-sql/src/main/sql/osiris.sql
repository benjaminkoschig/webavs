-- POAVS-2955 - NE PAS EXECUTER SUR CCJU
ALTER TABLE SCHEMA.CGJOURP ADD COLUMN ISEXTRACT CHAR(1) DEFAULT 1;

REORG TABLE SCHEMA.CGJOURP;

-- FIN NON EXECUTION SUR CCJU
insert into SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) values (223009, 'F', 'AVNPTRA   ', 'Avance PTRA                                                 ', '                        ');
insert into SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) values (223009, 'OSIPLRMOD ', 1, 1, 0, 0, 'Avance PTRA                             ', 2, 1, 2, 2, 2, 2, 10200023, 0, '                        ');
