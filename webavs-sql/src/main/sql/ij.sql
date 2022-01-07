-- ACOR FPI
alter table SCHEMA.IJBASIND
    add XKNJNC DECIMAL(9);
REORG TABLE SCHEMA.IJBASIND;

alter table SCHEMA.IJFPI
    add DATE_FORMATION DECIMAL(8);
REORG TABLE SCHEMA.IJFPI;

-- ajout genre r�adaption pour l'annonce renvoy� par ACOR
alter table SCHEMA.IJCALCUL
    add XNGREA DECIMAL(8);
REORG TABLE SCHEMA.IJCALCUL;

-- Modification de la taille de la colonne XXAGRE dans la table IJANNONC
ALTER TABLE SCHEMA.IJANNONC ALTER COLUMN XXAGRE SET DATA TYPE VARCHAR(2);
reorg table SCHEMA.IJANNONC;

-- Examen ou instruction
update schema.IJCALCUL as cal0 set cal0.xngrea = 1
where cal0.xniijc in (SELECT xniijc FROM schema.ijpronai
                                             inner join schema.IJCALCUL as cal1 on xbipai = cal1.xnipai
                      where xbttij in (52402001,52402002) and (xbdfdr = 0 or xbdfdr > 20211231) and cal1.xngrea is null and xbtgen = 52410001);
-- D�lai d'attente
update schema.IJCALCUL as cal0 set cal0.xngrea = 2
where cal0.xniijc in (SELECT xniijc FROM schema.ijpronai
                                             inner join schema.IJCALCUL as cal1 on xbipai = cal1.xnipai
                      where xbttij in (52402001,52402002) and (xbdfdr = 0 or xbdfdr > 20211231) and cal1.xngrea is null and xbtgen = 52410002);
-- Mesure m�dicale
update schema.IJCALCUL as cal0 set cal0.xngrea = 6
where cal0.xniijc in (SELECT xniijc FROM schema.ijpronai
                                             inner join schema.IJCALCUL as cal1 on xbipai = cal1.xnipai
                      where xbttij in (52402001,52402002) and (xbdfdr = 0 or xbdfdr > 20211231) and cal1.xngrea is null and xbtgen = 52410003);
-- Formation scolaire sp�ciale
update schema.IJCALCUL as cal0 set cal0.xngrea = 4
where cal0.xniijc in (SELECT xniijc FROM schema.ijpronai
                                             inner join schema.IJCALCUL as cal1 on xbipai = cal1.xnipai
                      where xbttij in (52402001,52402002) and (xbdfdr = 0 or xbdfdr > 20211231) and cal1.xngrea is null and xbtgen = 52410004);
-- Formation professionnelle initiale
update schema.IJCALCUL as cal0 set cal0.xngrea = 5
where cal0.xniijc in (SELECT xniijc FROM schema.ijpronai
                                             inner join schema.IJCALCUL as cal1 on xbipai = cal1.xnipai
                      where xbttij in (52402001,52402002) and (xbdfdr = 0 or xbdfdr > 20211231) and cal1.xngrea is null and xbtgen = 52410005);
-- Reclassement professionnel
update schema.IJCALCUL as cal0 set cal0.xngrea = 4
where cal0.xniijc in (SELECT xniijc FROM schema.ijpronai
                                             inner join schema.IJCALCUL as cal1 on xbipai = cal1.xnipai
                      where xbttij in (52402001,52402002) and (xbdfdr = 0 or xbdfdr > 20211231) and cal1.xngrea is null and xbtgen = 52410006);
-- Attente emploi apr�s reclassement professionnel
update schema.IJCALCUL as cal0 set cal0.xngrea = 3
where cal0.xniijc in (SELECT xniijc FROM schema.ijpronai
                                             inner join schema.IJCALCUL as cal1 on xbipai = cal1.xnipai
                      where xbttij in (52402001,52402002) and (xbdfdr = 0 or xbdfdr > 20211231) and cal1.xngrea is null and xbtgen = 52410007);
-- Mise au courant
update schema.IJCALCUL as cal0 set cal0.xngrea = 8
where cal0.xniijc in (SELECT xniijc FROM schema.ijpronai
                                             inner join schema.IJCALCUL as cal1 on xbipai = cal1.xnipai
                      where xbttij in (52402001,52402002) and (xbdfdr = 0 or xbdfdr > 20211231) and cal1.xngrea is null and xbtgen = 52410008);
-- Mesure de r�insertion
update schema.IJCALCUL as cal0 set cal0.xngrea = 4
where cal0.xniijc in (SELECT xniijc FROM schema.ijpronai
                                             inner join schema.IJCALCUL as cal1 on xbipai = cal1.xnipai
                      where xbttij in (52402001,52402002) and (xbdfdr = 0 or xbdfdr > 20211231) and cal1.xngrea is null and xbtgen = 52410009);

-- Ajout mensuel 13
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52406007, 'IJPERSALAI', 7, 1, 0, 0, 'Mensuel_13', 2, 1, 2, 2, 2, 2, 51400006, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52406007, 'D', '7', 'Monatslohn 13', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52406007, 'F', '7', 'Mensuel 13', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (51400035, 'IJGENRREA2', 0, 1, 3, 0, 'Mode_calcul_fpi', 0, 2, 2, 2, 2, 0, 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52435001, 'IJGENRREA2', 1, 1, 0, 0, 'Examen_instruction', 2, 1, 2, 2, 2, 2, 51400035, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52435002, 'IJGENRREA2', 2, 1, 0, 0, 'Delai_attente_avant_reclassement', 2, 1, 2, 2, 2, 2, 51400035, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52435003, 'IJGENRREA2', 3, 1, 0, 0, 'Delai_attente_recherche_emploi', 2, 1, 2, 2, 2, 2, 51400035, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52435004, 'IJGENRREA2', 4, 1, 0, 0, 'IJ_art_14a_15_17_18a', 2, 1, 2, 2, 2, 2, 51400035, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52435005, 'IJGENRREA2', 5, 1, 0, 0, 'FPI', 2, 1, 2, 2, 2, 2, 51400035, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52435006, 'IJGENRREA2', 6, 1, 0, 0, 'Mesure_medicale', 2, 1, 2, 2, 2, 2, 51400035, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52435007, 'IJGENRREA2', 7, 1, 0, 0, 'Formation_professionnelle', 2, 1, 2, 2, 2, 2, 51400035, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52435008, 'IJGENRREA2', 8, 1, 0, 0, 'Formation_preparatoire', 2, 1, 2, 2, 2, 2, 51400035, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52435009, 'IJGENRREA2', 9, 1, 0, 0, 'IJ_art_22_12', 2, 1, 2, 2, 2, 2, 51400035, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52435010, 'IJGENRREA2', 10, 1, 0, 0, 'IJ_art_22_14a', 2, 1, 2, 2, 2, 2, 51400035, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (51400035, 'F', '', 'Mode_calcul', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435001, 'D', '1', 'Untersuchung bzw. Abkl�rung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435001, 'F', '1', 'Examen ou instruction', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435002, 'D', '2', 'Wartezeit vor Umschulung (Art. 18 IVV)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435002, 'F', '2', 'D�lai d''attente avant reclassement (Art. 18 IVV)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435003, 'D', '3', 'Warten vor Arbeitsantritt (Art. 19 IVV)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435003, 'F', '3', 'D�lai d''attente pendant la recherche d''emploi (Art. 19 IVV)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435004, 'D', '4', 'Taggeld w�hrend Massnahme nach Art. 14a, 15, 17, 18a IVG', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435004, 'F', '4', 'Indemnit� journali�re pendant les mesures pr�vues aux art. 14a, 15, 17, 18a LAI', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435005, 'D', '5', 'EbA nach BBG mit Lehrvertrag', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435005, 'F', '5', 'FPI selon LFPr avec contrat d''apprentissage', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435006, 'D', '6', 'Medizinische Eingliederung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435006, 'F', '6', 'Mesure m�dicale de r�adaptation', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435007, 'D', '7', 'H�here Berufsbildung oder Besuch Hochschule (Terti�re Ausbildung)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435007, 'F', '7', 'Formation professionnelle sup�rieure et Haute �cole (Formation tertiaire)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435008, 'D', '8', 'Vorbereitung auf eine Hilfst�tigkeit oder auf eine T�tigkeit in einer gesch�tzen Werkst�tte nach Art. 16 Abs. 3 IVG oder gezielte Vorbereitung nach Art. 5 Abs. 2 IVV', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435008, 'F', '8', 'Formation pr�paratoire � une activit� auxiliaire ou un atelier prot�g� selon art. 16, al. 3, LAI ou pr�paration cibl�e selon art. 5, al. 2 IRAI', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435009, 'D', '9', 'Taggeld gem�ss Art. 22 Abs. 2 Bst. b nach Art. 12 IVG', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435009, 'F', '9', 'Indemnit� journali�re selon art. 22 al. 2 let. b selon art. 12 LAI', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435010, 'D', '10', 'Taggeld gem�ss Art. 22 Abs. 2 Bst. b nach Art. 14a IVG', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52435010, 'F', '10', 'Indemnit� journali�re selon art. 22 al. 2 let. b selon art. 14a LAI', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (51400036, 'IJMOTIFIN2', 0, 1, 3, 0, 'Motifs_interruption', 0, 2, 2, 2, 2, 0, 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52436001, 'IJMOTIFIN2', 1, 1, 0, 0, 'Maladie', 2, 1, 2, 2, 2, 2, 51400035, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52436002, 'IJMOTIFIN2', 2, 1, 0, 0, 'Accident', 2, 1, 2, 2, 2, 2, 51400035, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52436003, 'IJMOTIFIN2', 3, 1, 0, 0, 'Maternite', 2, 1, 2, 2, 2, 2, 51400035, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52436004, 'IJMOTIFIN2', 4, 1, 0, 0, 'Proche_aidant', 2, 1, 2, 2, 2, 2, 51400035, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52436005, 'IJMOTIFIN2', 5, 1, 0, 0, 'Autres', 2, 1, 2, 2, 2, 2, 51400035, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (51400036, 'D', '', 'Grund der Unterbrechung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (51400036, 'F', '', 'Motifs de l�interruption', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52436001, 'D', '1', 'Krankheit', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52436001, 'F', '1', 'Maladie', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52436002, 'D', '2', 'Unfall', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52436002, 'F', '2', 'Accident', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52436003, 'D', '3', 'Mutterschaft/Vaterschaft', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52436003, 'F', '3', 'Maternit�/Paternit�', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52436004, 'D', '4', 'Betreuungsurlaub', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52436004, 'F', '4', 'Cong� proche aidant', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52436005, 'D', '5', 'Andere Gr�nde', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52436005, 'F', '5', 'Autres motifs', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (51400037, 'IJGENRREAI', 0, 1, 3, 0, 'genre_readaptation', 0, 2, 2, 2, 2, 0, 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437001, 'IJGENRREAI', 1, 1, 0, 0, 'Observation_instruction', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437002, 'IJGENRREAI', 2, 1, 0, 0, 'Attente_readaptation', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437003, 'IJGENRREAI', 3, 1, 0, 0, 'Readaptation_medicale', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437004, 'IJGENRREAI', 4, 1, 0, 0, 'Placement_essai', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437005, 'IJGENRREAI', 5, 1, 0, 0, 'Formation_prof_initiale', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437006, 'IJGENRREAI', 6, 1, 0, 0, 'Reclassement', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437007, 'IJGENRREAI', 7, 1, 0, 0, 'Attente_emploi', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437008, 'IJGENRREAI', 8, 1, 0, 0, 'Mise_au_courant', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437009, 'IJGENRREAI', 9, 1, 0, 0, 'Mesures_reinsertion', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437010, 'IJGENRREAI', 100, 1, 0, 0, 'Mesures_instruction', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437011, 'IJGENRREAI', 101, 1, 0, 0, 'Travail_transition', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437012, 'IJGENRREAI', 102, 1, 0, 0, 'Mesure_reinsertion', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437013, 'IJGENRREAI', 103, 1, 0, 0, 'Entrainement_progressif', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437014, 'IJGENRREAI', 104, 1, 0, 0, 'Entrainement_au_travail', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437015, 'IJGENRREAI', 105, 1, 0, 0, 'Mesures_preparatoires', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437016, 'IJGENRREAI', 106, 1, 0, 0, 'Examen_appronfondi', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437017, 'IJGENRREAI', 107, 1, 0, 0, 'Formations_niveau_tertiaire', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437018, 'IJGENRREAI', 108, 1, 0, 0, 'Formations_niveau_tertiaire_art12', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437019, 'IJGENRREAI', 109, 1, 0, 0, 'Formations_niveau_tertiaire_art14a', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437020, 'IJGENRREAI', 110, 1, 0, 0, 'CFC', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437021, 'IJGENRREAI', 111, 1, 0, 0, 'CFC_art12', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437022, 'IJGENRREAI', 112, 1, 0, 0, 'CFC_art14a', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437023, 'IJGENRREAI', 113, 1, 0, 0, 'AFP', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437024, 'IJGENRREAI', 114, 1, 0, 0, 'AFP_art12', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437025, 'IJGENRREAI', 115, 1, 0, 0, 'AFP_art14a', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437026, 'IJGENRREAI', 116, 1, 0, 0, 'Preparation_travail_auxiliaire', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437027, 'IJGENRREAI', 117, 1, 0, 0, 'Preparation_travail_auxiliaire_art12', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437028, 'IJGENRREAI', 118, 1, 0, 0, 'Preparation_travail_auxiliaire_art14a', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437029, 'IJGENRREAI', 119, 1, 0, 0, 'Autres_formations', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437030, 'IJGENRREAI', 120, 1, 0, 0, 'Autres formations_art12', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437031, 'IJGENRREAI', 121, 1, 0, 0, 'Autres formations_art14a', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437032, 'IJGENRREAI', 122, 1, 0, 0, 'Preparation_ciblee', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437033, 'IJGENRREAI', 123, 1, 0, 0, 'Preparation_ciblee_art12', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437034, 'IJGENRREAI', 124, 1, 0, 0, 'Preparation_ciblee_art14a', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437035, 'IJGENRREAI', 125, 1, 0, 0, 'Formations_niveau_tertiaire', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437036, 'IJGENRREAI', 126, 1, 0, 0, 'Formations_niveau_tertiaire_attente', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437037, 'IJGENRREAI', 127, 1, 0, 0, 'Ecole_enseignement_general', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437038, 'IJGENRREAI', 128, 1, 0, 0, 'Ecole_enseignement_general_attente', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437039, 'IJGENRREAI', 129, 1, 0, 0, 'CFC', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437040, 'IJGENRREAI', 130, 1, 0, 0, 'CFC_attente', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437041, 'IJGENRREAI', 131, 1, 0, 0, 'AFP', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437042, 'IJGENRREAI', 132, 1, 0, 0, 'AFP_attente', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437043, 'IJGENRREAI', 133, 1, 0, 0, 'Preparation_travail_auxiliaire', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437044, 'IJGENRREAI', 134, 1, 0, 0, 'Preparation_travail_auxiliaire_attente', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437045, 'IJGENRREAI', 135, 1, 0, 0, 'Autres_formations', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437046, 'IJGENRREAI', 136, 1, 0, 0, 'Autres_formations_attente', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437047, 'IJGENRREAI', 137, 1, 0, 0, 'Preparation_ciblee', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437048, 'IJGENRREAI', 138, 1, 0, 0, 'Preparation_ciblee_attente', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437049, 'IJGENRREAI', 139, 1, 0, 0, 'Reentra�nement_travail_meme_profession', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437050, 'IJGENRREAI', 140, 1, 0, 0, 'Reentra�nement_travail_meme_profession_attente', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437051, 'IJGENRREAI', 141, 1, 0, 0, 'Recherche_emploi', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437052, 'IJGENRREAI', 142, 1, 0, 0, 'Placement_essai', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437053, 'IJGENRREAI', 143, 1, 0, 0, 'Mesures_medicales_art12', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52437054, 'IJGENRREAI', 144, 1, 0, 0, 'Mesures_medicales_art13', 2, 1, 2, 2, 2, 2, 51400037, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (51400037, 'F', '', 'genre_readaptation', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437001, 'D', '1', 'Abkl�rung/Untersuchung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437001, 'F', '1', 'Observation/Instruction', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437002, 'D', '2', 'Wartezeit vor Eingliederungsmassnahmen', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437002, 'F', '2', 'Attente de r�adaptation', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437003, 'D', '3', 'Mediz. Eingliederung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437003, 'F', '3', 'R�adaptation m�dicale', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437004, 'D', '4', 'Arbeitsversuch', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437004, 'F', '4', 'Placement � l''essai', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437005, 'D', '5', 'Erstmalige berufliche Ausbildung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437005, 'F', '5', 'Formation prof. initiale', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437006, 'D', '6', 'Umschulung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437006, 'F', '6', 'Reclassement', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437007, 'D', '7', 'Wartezeit nach Umschulung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437007, 'F', '7', 'Attente d''emploi', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437008, 'D', '8', 'Anlernzeit', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437008, 'F', '8', 'Mise au courant', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437009, 'D', '9', 'Integrationsmassnahmen', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437009, 'F', '9', 'Mesures de r�insertion', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437010, 'D', '296', '296 Berufliche und medizinische Abkl�rungen zur Eingliederungsf�higkeit', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437010, 'F', '296', '296 Mesures d''instruction m�dico-professionnelle concernant l''aptitude � la r�adaptation', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437011, 'D', '584', '584 Arbeit zur Zeit�berbr�ckung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437011, 'F', '584', '584 Travail de transition', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437012, 'D', '590', '590 Integrationsmassnahmen f�r Jugendliche', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437012, 'F', '590', '590 Mesure de r�insertion destin�es au jeunes', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437013, 'D', '591', '591 Aufbautraining', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437013, 'F', '591', '591 Entra�nement progressif', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437014, 'D', '592', '592 Arbeitstraining', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437014, 'F', '592', '592 Entra�nement au travail', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437015, 'D', '532', '532 Massnahmen zur Vorbereitung auf eine Ausbildung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437015, 'F', '532', '532 Mesures pr�paratoires durant l''orientation professionnelle', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437016, 'D', '533', '533 Vertiefte Kl�rung m�glicher Berufsrichtungen', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437016, 'F', '533', '533 Examen appronfondi de professions possibles', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437017, 'D', '401', '401 Ausbildungen auf Terti�rstufe', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437017, 'F', '401', '401 Formations de niveau tertiaire', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437018, 'D', '1401', '401 Ausbildungen auf Terti�rstufe (i.V.m. Art. 12)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437018, 'F', '1401', '401 Formations de niveau tertiaire (i.V.m. Art. 12)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437019, 'D', '2401', '401 Ausbildungen auf Terti�rstufe (i.V.m. Art. 14a)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437019, 'F', '2401', '401 Formations de niveau tertiaire (i.V.m. Art. 14a)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437020, 'D', '410', '410 Berufliche Grundbildung EFZ', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437020, 'F', '410', '410 Certificat f�d�rale de capacit� CFC', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437021, 'D', '1410', '410 Berufliche Grundbildung EFZ (i.V.m. Art. 12)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437021, 'F', '1410', '410 Certificat f�d�rale de capacit� CFC (i.V.m. Art. 12)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437022, 'D', '2410', '410 Berufliche Grundbildung EFZ (i.V.m. Art. 14a)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437022, 'F', '2410', '410 Certificat f�d�rale de capacit� CFC (i.V.m. Art. 14a)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437023, 'D', '420', '420 Berufliche Grundbildung EBA', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437023, 'F', '420', '420 Attestation f�d�rale de formation professionnelle AFP', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437024, 'D', '1420', '420 Berufliche Grundbildung EBA (i.V.m. Art. 12)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437024, 'F', '1420', '420 Attestation f�d�rale de formation professionnelle AFP (i.V.m. Art. 12)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437025, 'D', '2420', '420 Berufliche Grundbildung EBA  (i.V.m. Art. 14a)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437025, 'F', '2420', '420 Attestation f�d�rale de formation professionnelle AFP (i.V.m. Art. 14a)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437026, 'D', '425', '425 Vorbereitung auf Hilfst�tigkeit oder T�tigkeit in gesch�tzter Werkst�tte', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437026, 'F', '425', '425 Pr�paration � un travail auxiliaire ou � une activit� en atelier prot�g�', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437027, 'D', '1425', '425 Vorbereitung auf Hilfst�tigkeit oder T�tigkeit in gesch�tzter Werkst�tte (i.V.m. Art. 12)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437027, 'F', '1425', '425 Pr�paration � un travail auxiliaire ou � une activit� en atelier prot�g� (i.V.m. Art. 12)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437028, 'D', '2425', '425 Vorbereitung auf Hilfst�tigkeit oder T�tigkeit in gesch�tzter Werkst�tte (i.V.m. Art. 14a)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437028, 'F', '2425', '425 Pr�paration � un travail auxiliaire ou � une activit� en atelier prot�g� (i.V.m. Art. 14a)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437029, 'D', '426', '426 Andere Ausbildungen zur beruflichen Eingliederung (non formale Ausbildungen)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437029, 'F', '426', '426 Autres formations en vue de la r�adaptation professionnelle', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437030, 'D', '1426', '426 Andere Ausbildungen zur beruflichen Eingliederung (i.V.m. Art. 12)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437030, 'F', '1426', '426 Autres formations en vue de la r�adaptation professionnelle (i.V.m. Art. 12)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437031, 'D', '2426', '426 Andere Ausbildungen zur beruflichen Eingliederung (i.V.m. Art. 14a)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437031, 'F', '2426', '426 Autres formations en vue de la r�adaptation professionnelle(i.V.m. Art. 14a)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437032, 'D', '427', '427 Gezielte Vorbereitung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437032, 'F', '427', '427 Pr�paration cibl�e', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437033, 'D', '1427', '427 Gezielte Vorbereitung (i.V.m. Art. 12)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437033, 'F', '1427', '427 Pr�paration cibl�e (i.V.m. Art. 12)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437034, 'D', '2427', '427 Gezielte Vorbereitung  (i.V.m. Art. 14a)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437034, 'F', '2427', '427 Pr�paration cibl�e (i.V.m. Art. 14a)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437035, 'D', '451', '451 Ausbildungen auf Terti�rstufe', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437035, 'F', '451', '451 Formations de niveau tertiaire', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437036, 'D', '1451', '451 Ausbildungen auf Terti�rstufe (Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437036, 'F', '1451', '451 Formations de niveau tertiaire (Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437037, 'D', '452', '452 Allgemeinbildende Schulen', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437037, 'F', '452', '452 �cole d''enseignement g�n�ral', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437038, 'D', '1452', '452 Allgemeinbildende Schulen (Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437038, 'F', '1452', '452 �cole d''enseignement g�n�ral (Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437039, 'D', '460', '460 Berufliche Grundbildung EFZ', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437039, 'F', '460', '460 Certificat f�d�rale de capacit� CFC', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437040, 'D', '1460', '460 Berufliche Grundbildung EFZ (Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437040, 'F', '1460', '460 Certificat f�d�rale de capacit� CFC (Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437041, 'D', '470', '470 Berufliche Grundbildung EBA', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437041, 'F', '470', '470 Attestation f�d�rale de formation professionnelle AFP', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437042, 'D', '1470', '470 Berufliche Grundbildung EBA (Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437042, 'F', '1470', '470 Attestation f�d�rale de formation professionnelle AFP (Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437043, 'D', '475', '475 Vorbereitung auf Hilfst�tigkeit oder T�tigkeit in gesch�tzter Werkst�tte', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437043, 'F', '475', '475 Pr�paration � un travail auxiliaire ou � une activit� en atelier prot�g�', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437044, 'D', '1475', '475 Vorbereitung auf Hilfst�tigkeit oder T�tigkeit in gesch�tzter Werkst�tte(Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437044, 'F', '1475', '475 Pr�paration � un travail auxiliaire ou � une activit� en atelier prot�g� (Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437045, 'D', '476', '476 Andere Ausbildungen zur beruflichen Eingliederung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437045, 'F', '476', '476 Autres formations en vue de la r�adaptation professionnelle', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437046, 'D', '1476', '476 Andere Ausbildungen zur beruflichen Eingliederung (Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437046, 'F', '1476', '476 Autres formations en vue de la r�adaptation professionnelle (Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437047, 'D', '477', '477 Gezielte Vorbereitung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437047, 'F', '477', '477 Pr�paration cibl�e', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437048, 'D', '1477', '477 Gezielte Vorbereitung (Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437048, 'F', '1477', '477 Pr�paration cibl�e (Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437049, 'D', '500', '500 Wiedereinschulung in den bisherigen Beruf', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437049, 'F', '500', '500 R�entra�nement au travail dans la m�me profession (art. 17 al. 2 LAI)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437050, 'D', '1500', '500 Wiedereinschulung in den bisherigen Beruf (Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437050, 'F', '1500', '500 R�entra�nement au travail dans la m�me profession (art. 17 al. 2 LAI) (Attente)', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437051, 'D', '538', '538 Stellensuche', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437051, 'F', '538', '538 Recherche d''emploi', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437052, 'D', '540', '540 Arbeitsversuch', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437052, 'F', '540', '540 Placement � l''essai', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437053, 'D', '302', '302 Medizinische Massnahmen zur Eingliederung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437053, 'F', '302', '302 Mesures m�dicales art. 12 LAI', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437054, 'D', '301', '301 Medizinische Massnahmen zur Behandlung von Geburtsgebrechen', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52437054, 'F', '301', '301 Mesures m�dicales art. 13 LAI', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52433009, 'IJCODEABSE', 9, 1, 0, 0, 'Proche_aidant', 2, 1, 2, 2, 2, 2, 51400033, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52433009, 'D', '9', '9 Betreuungsurlaub', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52433009, 'F', '9', '9 Cong� proche aidant', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

-- ACOR
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.acor.token.duration','1',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- AJOUT TABLE IJ FPI CALCULEE
CREATE TABLE IJ_FPI_CALCULEE (ID_FPI_CALCULEE decimal(8), MODE_CALCUL decimal(8), SALAIRE_MENSUEL decimal(15,2), MONTANT_ENFANTS decimal(15, 2), NB_ENFANTS decimal (8), CSPY character(24), PSPY character(24));