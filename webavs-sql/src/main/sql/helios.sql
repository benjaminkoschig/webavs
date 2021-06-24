-- Ajout des comptes pour traitement dans la table des comptes AVS
-- TYPE_SECTEUR = "1";
-- TYPE_CLASSE  = "2";
-- TYPE_COMPTE  = "3";




-- Secteur
--DELETE from SCHEMA.CGPCAVP WHERE numero in(250) and exists (select 0 from CGPCAVP WHERE numero in (250));
insert into SCHEMA.CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (1, 250, NULL, 'Compte de bilan PTra', 'Bestandesrechnung PTra', NULL);
--DELETE from CGPCAVP WHERE numero in(251) and exists (select 0 from CGPCAVP WHERE numero in (251));
insert into SCHEMA.CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (1, 251, NULL, 'Compte d''exploitation PTra', 'Betriebsrechnung PTra', NULL);
--DELETE from CGPCAVP WHERE numero in(252) and exists (select 0 from CGPCAVP WHERE numero in (252));
insert into SCHEMA.CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (1, 252, NULL, 'Compte d''exploitation PTra mal. inv.', 'Betriebsrechnung PTra mal. inv.', NULL);
--DELETE from CGPCAVP WHERE numero in(258) and exists (select 0 from CGPCAVP WHERE numero in (258));
insert into SCHEMA.CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (1, 258, NULL, 'Compte d''administration PTra', 'Verwaltungsrechnung PTra', NULL);
--DELETE from CGPCAVP WHERE numero in(259) and exists (select 0 from CGPCAVP WHERE numero in (259));
insert into SCHEMA.CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (1, 259, NULL, 'Bouclement PTra', 'Abschluss PTra', NULL);


-- Compte
--DELETE from SCHEMA.CGPCAVP WHERE numero in(1252) and exists (select 0 from CGPCAVP WHERE numero in (1252));
insert into SCHEMA.CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (3, 1252, NULL, 'Avoir envers le secteur comptable 25', '', NULL);
--DELETE from CGPCAVP WHERE numero in(2252) and exists (select 0 from CGPCAVP WHERE numero in (2252));
insert into SCHEMA.CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (3, 2252, NULL, 'Dettes envers le secteur comptable 25', '', NULL);
--DELETE from CGPCAVP WHERE numero in(1110) and exists (select 0 from CGPCAVP WHERE numero in (1110));
insert into SCHEMA.CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (3, 1110, NULL, 'Avances sur prestations (prestations transitoires provisoires)', '', NULL);
--DELETE from CGPCAVP WHERE numero in(2141) and exists (select 0 from CGPCAVP WHERE numero in (2141));
insert into SCHEMA.CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (3, 2141, NULL, 'C/C Confédération transfert Ptra, acompte/versement final', '', NULL);
--DELETE from CGPCAVP WHERE numero in(6465) and exists (select 0 from CGPCAVP WHERE numero in (6465));
insert into SCHEMA.CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (3, 6465, NULL, 'Indemnités frais d''administration prestations transitoires', '', NULL);
-- CAT SECTION
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (209016, 'OSIORDLIV ', 5, 1, 0, 0, 'PTRA', 2, 1, 2, 2, 2, 2, 10200009, 0, '202106241000000globaz   ');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (209016, 'D', 'PTRA      ', '[DE]Prestations transitoires', '202106241000000globaz   ');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (209016, 'F', 'PTRA      ', 'Prestations transitoires', '202106241000000globaz   ');
-- TYPE SECTIOn
INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('helios.prestation.transitoire', 'true', '202005041120000Globaz   ', '20200603082744ccjuglo   ');
