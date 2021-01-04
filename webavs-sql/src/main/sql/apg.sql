-- [WEBAVS-8389] FERCIAB - S191121_002 - ESVE MATERNITE
-- [WEBAVS-8389] FERCIAB - S191121_002 - ESVE MATERNITE

-- propri�t� applicative
INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('apg.FERCIAB.maternite', '', '20201016103000globaz', '20201016103000globaz');
/* SCRIPT SPECIFIQUE
-- code syst�mes
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237560, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_JU_PARITAIRE_PARTICIPATION', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237560, 'D', 'STD', '[de]Maternit� CIAB JU paritaire - Participation aux cotisations paritaires', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237560, 'F', 'STD', 'Maternit� CIAB JU paritaire - Participation aux cotisations paritaires', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237561, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_JU_PARITAIRE_MONTANT_BRUT', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237561, 'D', 'STD', '[de]Maternit� CIAB JU paritaire - Montant brut', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237561, 'F', 'STD', 'Maternit� CIAB JU paritaire - Montant brut', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237562, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_JU_PARITAIRE_MONTANT_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237562, 'D', 'STD', '[de]Maternit� CIAB JU paritaire - Montant restitution', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237562, 'F', 'STD', 'Maternit� CIAB JU paritaire - Montant restitution', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237563, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_JU_PERSONNEL_COTISATIONS_AC', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237563, 'D', 'STD', '[de]Maternit� CIAB JU personnel - Cotisations AC', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237563, 'F', 'STD', 'Maternit� CIAB JU personnel - Cotisations AC', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237564, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_JU_PERSONNEL_COTISATIONS_AVS', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237564, 'D', 'STD', '[de]Maternit� CIAB JU personnel - Cotisations AVS', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237564, 'F', 'STD', 'Maternit� CIAB JU personnel - Cotisations AVS', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237565, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_JU_PERSONNEL_PARTICIPATION', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237565, 'D', 'STD', '[de]Maternit� CIAB JU personnel - Participation aux cotisations personnelles', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237565, 'F', 'STD', 'Maternit� CIAB JU personnel - Participation aux cotisations personnelles', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237566, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_JU_PERSONNEL_MONTANT_BRUT', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237566, 'D', 'STD', '[de]Maternit� CIAB JU personnel - Montant brut', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237566, 'F', 'STD', 'Maternit� CIAB JU personnel - Montant brut', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237567, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_JU_PERSONNEL_MONTANT_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237567, 'D', 'STD', '[de]Maternit� CIAB JU personnel - Montant restitution', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237567, 'F', 'STD', 'Maternit� CIAB JU personnel - Montant restitution', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237568, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_BE_PARITAIRE_PARTICIPATION', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237568, 'D', 'STD', '[de]Maternit� CIAB BE paritaire - Participation aux cotisations paritaires', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237568, 'F', 'STD', 'Maternit� CIAB BE paritaire - Participation aux cotisations paritaires', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237569, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_BE_PARITAIRE_MONTANT_BRUT', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237569, 'D', 'STD', '[de]Maternit� CIAB BE paritaire - Montant brut', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237569, 'F', 'STD', 'Maternit� CIAB BE paritaire - Montant brut', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237570, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_BE_PARITAIRE_MONTANT_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237570, 'D', 'STD', '[de]Maternit� CIAB BE paritaire - Montant restitution', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237570, 'F', 'STD', 'Maternit� CIAB BE paritaire - Montant restitutions', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237571, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_BE_PERSONNEL_COTISATIONS_AC', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237571, 'D', 'STD', '[de]Maternit� CIAB BE personnel - Cotisations AC', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237571, 'F', 'STD', 'Maternit� CIAB BE personnel - Cotisations AC', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237572, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_BE_PERSONNEL_COTISATIONS_AVS', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237572, 'D', 'STD', '[de]Maternit� CIAB BE personnel - Cotisations AVS', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237572, 'F', 'STD', 'Maternit� CIAB BE personnel - Cotisations AVS', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237573, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_BE_PERSONNEL_PARTICIPATION', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237573, 'D', 'STD', '[de]Maternit� CIAB BE personnel - Participation aux cotisations personnelles', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237573, 'F', 'STD', 'Maternit� CIAB BE personnel - Participation aux cotisations personnelles', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237574, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_BE_PERSONNEL_MONTANT_BRUT', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237574, 'D', 'STD', '[de]Maternit� CIAB BE personnel - Montant brut', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237574, 'F', 'STD', 'Maternit� CIAB BE personnel - Montant brut', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (237575, 'OSIREFRUB ', 1, 1, 0, 0, 'MATCIAB_BE_PERSONNEL_MONTANT_RESTITUTION', 2, 2, 2, 2, 2, 2, 10200037, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237575, 'D', 'STD', '[de]Maternit� CIAB BE personnel - Montant restitution', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (237575, 'F', 'STD', 'Maternit� CIAB BE personnel - Montant restitution', '20201019103000globaz');

-- code syst�mes
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52015008, 'APGENRPRES', 2, 1, 0, 0, 'MATCIAB1PE', 2, 1, 2, 2, 2, 2, 51000015, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52015008, 'D', 'STD', '[de]Maternit� CIAB1', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52015008, 'F', 'STD', 'Maternit� CIAB1', '20201019103000globaz');

INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52015009, 'APGENRPRES', 2, 1, 0, 0, 'MATCIAB1PA', 2, 1, 2, 2, 2, 2, 51000015, 0, '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52015009, 'D', 'STD', '[de]Maternit� CIAB2', '20201019103000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52015009, 'F', 'STD', 'Maternit� CIAB2', '20201019103000globaz');

-- ged
INSERT INTO SCHEMA.FWDDOC (IDDOC,IDCONF,NUMREF,DESCFR,DESCDE,DESCIT,DOCTYP,PSPY,OUTXML,OUTPDF) VALUES ((SELECT MAX(IDDOC)+1 FROM SCHEMA.FWDDOC),1,'5052PAP','D�comptes Maternit� MATCIAB2',null,null,null,null,'2','1');

-- plage valeur
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY)
VALUES ('APG','MATCIABBEM',0,20100101,0.000000,1,0.000000,'',400.000000,'Montant maximum pour maternit� CIAB BE',0,0,'20201019103000globaz');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY)
VALUES ('APG','MATCIABJUM',0,20100101,0.000000,1,0.000000,'',400.000000,'Montant maximum pour maternit� CIAB JU',0,0,'20201019103000globaz');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY)
VALUES ('APG','MATCIABBEJ',0,20100101,0.000000,1,0.000000,'',14.000000,'Nombre de jours suppl. pour maternit� CIAB BE',0,0,'20201019103000globaz');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY)
VALUES ('APG','MATCIABJUJ',0,20100101,0.000000,1,0.000000,'',14.000000,'Nombre de jours suppl. pour maternit� CIAB JU',0,0,'20201019103000globaz');
FIN SCRIPT SPECIFIQUE*/