-- Ajout code système du groupe domaine catalogue de texte pour le domaine proche aidant
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (52000041, 'PAIDOMAINE', 0, 1, 3, 0, 'Groupe Domaine Catalogue Texte PAI', 0, 2, 2, 2, 2, 0, 0, 0,
        'spy                     ');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (52000041, 'F', '1         ', 'Groupe Domaine Catalogue Texte PAI', 'spy                     ');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (52000041, 'D', '1         ', 'Groupe Domaine Catalogue Texte PAI', 'spy                     ');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (53041001, 'PAIDOMAINE', 1, 1, 0, 0, 'PROCHE AIDANT', 2, 1, 2, 2, 2, 2, 52000041, 0, 'spy                     ');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (53041001, 'D', '          ', '[de]Proche aidant', 'spy                     ');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (53041001, 'F', '          ', 'Proche aidant', 'spy                     ');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (53041001, 'I', '          ', '[it]Proche aidant', 'spy                     ');

-- Ajout code système du groupe Type Doc catalogue de texte pour le domaine proche aidant
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (52000042, 'PAITYPES  ', 0, 1, 3, 0, 'Groupe Type Doc Catalogue Texte PAI', 0, 2, 2, 2, 2, 0, 0, 0,
        'spy                     ');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (52000042, 'F', '1         ', 'Groupe Type Doc Catalogue Texte PAI', 'spy                     ');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (52000042, 'D', '1         ', 'Groupe Type Doc Catalogue Texte PAI', 'spy                     ');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (53042001, 'PAITYPES  ', 1, 1, 0, 0, 'DECISION', 2, 1, 2, 2, 2, 2, 52000042, 0, 'spy                     ');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (53042001, 'D', '1         ', '[de]D�cision', 'spy                     ');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (53042001, 'F', '1         ', 'D�cision', 'spy                     ');
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN,
                           PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (53042007, 'PAITYPES  ', 7, 1, 0, 0, 'ATTESTATION_FISCALE', 2, 1, 2, 2, 2, 2, 52000042, 0,
        'spy                     ');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (53042007, 'F', '7         ', 'Attestation fiscale', 'spy                     ');
INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (53042007, 'D', '7         ', '[de]Attestation fiscale', 'spy                     ');