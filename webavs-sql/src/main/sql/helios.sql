-- Ajout des comptes pour traitement dans la table des comptes AVS
-- TYPE_SECTEUR = "1";
-- TYPE_CLASSE  = "2";
-- TYPE_COMPTE  = "3";




-- Secteur


--DELETE from CGPCAVP WHERE numero in(25) and exists (select 0 from CGPCAVP WHERE numero in (25));
INSERT INTO CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) VALUES (1, 25, null, 'Prestations transitoires', 'Provisorische �berbr�ckungsleistung', null);
--DELETE from CGPCAVP WHERE numero in(250) and exists (select 0 from CGPCAVP WHERE numero in (250));
insert into CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (1, 250, NULL, 'Compte de bilan PTra', 'Bestandesrechnung PTra', NULL);
--DELETE from CGPCAVP WHERE numero in(251) and exists (select 0 from CGPCAVP WHERE numero in (251));
insert into CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (1, 251, NULL, 'Compte d''exploitation PTra', 'Betriebsrechnung PTra', NULL);
--DELETE from CGPCAVP WHERE numero in(252) and exists (select 0 from CGPCAVP WHERE numero in (252));
insert into CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (1, 252, NULL, 'Compte d''exploitation PTra mal. inv.', 'Betriebsrechnung PTra FM', NULL);
--DELETE from CGPCAVP WHERE numero in(258) and exists (select 0 from CGPCAVP WHERE numero in (258));
insert into CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (1, 258, NULL, 'Compte d''administration PTra', 'Verwaltungsrechnung PTra', NULL);
--DELETE from CGPCAVP WHERE numero in(259) and exists (select 0 from CGPCAVP WHERE numero in (259));
insert into CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (1, 259, NULL, 'Bouclement PTra', 'Abschluss PTra', NULL);


-- Compte
--DELETE from CGPCAVP WHERE numero in(1252) and exists (select 0 from CGPCAVP WHERE numero in (1252));
insert into CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (3, 1252, NULL, 'Avoir envers le secteur comptable 25', '', NULL);
--DELETE from CGPCAVP WHERE numero in(2252) and exists (select 0 from CGPCAVP WHERE numero in (2252));
insert into CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (3, 2252, NULL, 'Dettes envers le secteur comptable 25', '', NULL);
--DELETE from CGPCAVP WHERE numero in(1110) and exists (select 0 from CGPCAVP WHERE numero in (1110));
insert into CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (3, 1110, NULL, 'Avances sur prestations (prestations transitoires provisoires)', '', NULL);
--DELETE from CGPCAVP WHERE numero in(2141) and exists (select 0 from CGPCAVP WHERE numero in (2141));
insert into CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (3, 2141, NULL, 'C/C Conf�d�ration transfert Ptra, acompte/versement final', '', NULL);
--DELETE from CGPCAVP WHERE numero in(6465) and exists (select 0 from CGPCAVP WHERE numero in (6465));
insert into CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (3, 6465, NULL, 'Indemnit�s frais d''administration prestations transitoires', '', NULL);

-- Cat�gories de sections
INSERT INTO FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (227065, 'OSICATSEC ', 65, 1, 0, 0, 'Paiement_principal_PTRA', 2, 2, 2, 2, 2, 2, 10200027, 0, '202106291000globazf');
INSERT INTO FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (227066, 'OSICATSEC ', 66, 1, 0, 0, 'Decision_PTRA', 2, 2, 2, 2, 2, 2, 10200027, 0, '202106291000globazf');
INSERT INTO FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (227067, 'OSICATSEC ', 67, 1, 0, 0, 'Decision_PTRA_FM', 2, 2, 2, 2, 2, 2, 10200027, 0, '202106291000globazf');
INSERT INTO FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (227068, 'OSICATSEC ', 68, 1, 0, 0, 'Avance_PTRA', 2, 2, 2, 2, 2, 2, 10200027, 0, '202106291000globazf');

INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (227065, 'F', '65', 'Paiement principal PTra', '202106291000globazf');
INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (227065, 'D', '65', 'Hauptzahlung P�L', '202106291000globazf');
INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (227065, 'I', '65', 'Pagamento principale PTra', '202106291000globazf');
INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (227066, 'F', '66', 'D�cision PTra', '202106291000globazf');
INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (227066, 'D', '66', 'Verf�gung P�L', '202106291000globazf');
INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (227066, 'I', '66', 'Decisione PTra', '202106291000globazf');
INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (227067, 'F', '67', 'D�cision PTra FM', '202106291000globazf');
INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (227067, 'D', '67', 'Verf�gung P�L KK', '202106291000globazf');
INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (227067, 'I', '67', 'Decisione PTra CM', '202106291000globazf');
INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (227068, 'F', '68', 'Avance PTra', '202106291000globazf');
INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (227068, 'D', '68', 'Vorauszahlung �L', '202106291000globazf');
INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (227068, 'I', '68', 'Anticipo PTra', '202106291000globazf');

-- Nature des ordres � livrer
INSERT INTO FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (209016, 'OSIORDLIV ', 5, 1, 0, 0, 'PTRA', 2, 1, 2, 2, 2, 2, 10200009, 0, '202106241000000globaz');

INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (209016, 'D', 'PTRA      ', 'Provisorische �berbr�ckungsleistung', '202106241000000globaz   ');
INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (209016, 'F', 'PTRA      ', 'Prestations transitoires', '202106241000000globaz   ');
INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (209016, 'I', 'PTRA      ', 'Prestazioni transitorie', '202106241000000globaz   ');