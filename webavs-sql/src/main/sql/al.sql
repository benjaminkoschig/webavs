INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('al.motifE411','false','20190301120000Globaz    ','20190301120000Globaz    ');

--WEBAVS-6147 - Ajout d'un paramètre pour ajouter un caractère au NSS lors d'édition CSV
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('al.recapFormatNss','false','2019032620000Globaz    ','2019032620000Globaz    ');

INSERT INTO SCHEMA.FWCOSP (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY) VALUES (61360121,'ALRAFAMCOE',1,1,0,0,'Allocation de naissance sans base légale cantonale',2,1,2,2,2,2,60360000,0,'20190408Globaz          ');
INSERT INTO SCHEMA.FWCOSP (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY) VALUES (61360122,'ALRAFAMCOE',1,1,0,0,'Allocation d''adoption sans base légale cantonale',2,1,2,2,2,2,60360000,0,'20190408Globaz          ');
INSERT INTO SCHEMA.FWCOSP (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY) VALUES (61360131,'ALRAFAMCOE',1,1,0,0,'Le code annoncé pour le pays de résidence de l''enfant n''est pas valide',2,1,2,2,2,2,60360000,0,'20190408Globaz          ');
INSERT INTO SCHEMA.FWCOSP (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY) VALUES (61360132,'ALRAFAMCOE',1,1,0,0,'Le type d''allocation et/ou la base légale ne sont pas cohérents avec le pays de résidence de l''enfant',2,1,2,2,2,2,60360000,0,'20190408Globaz          ');
INSERT INTO SCHEMA.FWCOSP (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY) VALUES (61360208,'ALRAFAMCOE',1,1,0,0,'Tentative de modifier ou annuler une allocation expirée',2,1,2,2,2,2,60360000,0,'20190408Globaz          ');
INSERT INTO SCHEMA.FWCOSP (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY) VALUES (61360209,'ALRAFAMCOE',1,1,0,0,'Tentative de créer une allocation expirée ou archivée',2,1,2,2,2,2,60360000,0,'20190408Globaz          ');

INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (61360121,'F','121       ','Allocation de naissance sans base légale cantonale','20190408Globaz          ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (61360121,'D','121       ','Allocation de naissance sans base légale cantonale','20190408Globaz          ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (61360122,'F','122       ','Allocation d''adoption sans base légale cantonale','20190408Globaz          ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (61360122,'D','122       ','Allocation d''adoption sans base légale cantonale','20190408Globaz          ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (61360131,'F','131       ','Le code annoncé pour le pays de résidence de l''enfant n''est pas valide','20190408Globaz          ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (61360131,'D','131       ','Le code annoncé pour le pays de résidence de l''enfant n''est pas valide','20190408Globaz          ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (61360132,'F','132       ','Le type d''allocation et/ou la base légale ne sont pas cohérents avec le pays de résidence de l''enfant','20190408Globaz          ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (61360132,'D','132       ','Le type d''allocation et/ou la base légale ne sont pas cohérents avec le pays de résidence de l''enfant','20190408Globaz          ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (61360141,'F','141       ','IDE incorrect','20190408Globaz          ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (61360141,'D','141       ','IDE incorrect','20190408Globaz          ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (61360208,'F','208       ','Tentative de modifier ou annuler une allocation expirée','20190408Globaz          ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (61360208,'D','208       ','Tentative de modifier ou annuler une allocation expirée','20190408Globaz          ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (61360209,'F','209       ','Tentative de créer une allocation expirée ou archivée','20190408Globaz          ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (61360209,'D','209       ','Tentative de créer une allocation expirée ou archivée','20190408Globaz          ');