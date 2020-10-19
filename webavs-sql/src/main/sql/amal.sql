-- [POAVS-2893] - CCJU - S200303_008 Mise en oeuvre processus 8
-- Ajout de 2 nouveaux "type d'annonce"
insert into SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) values (42003612, 'F', 13        , 'Demande prime tarifaire', '20201019globaz');
insert into SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) values (42003613, 'F', 14        , 'Réponse prime tarifaire', '20201019globaz');
insert into SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) values (42003612, 'AMSXSBTY', 13, 1, 0, 0, 'DEMANDE_PRIME_TARIFAIRE', 2, 1, 2, 2, 2, 2, 41000026, 0, '20201019globaz');
insert into SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) values (42003613, 'AMSXSBTY', 14, 1, 0, 0, 'REPONSE_PRIME_TARIFAIRE', 2, 1, 2, 2, 2, 2, 41000026, 0, '20201019globaz');

-- Ajout colonne dans l'annonce sedex permettant de stocker la valeur de la prime tarifaire
ALTER TABLE SCHEMA.MAMSGSDX ADD MTPRTA NUMERIC(10,2);
REORG TABLE SCHEMA.MAMSGSDX;
