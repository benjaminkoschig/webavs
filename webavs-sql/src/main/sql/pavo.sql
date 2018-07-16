--Ajout d'une propriété pour choisir le type d'annonce à utiliser (Fichier plat ou XML)
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('pavo.entete.isAnnoncesXML','false');
--Ajout d'un nouveau codeSystem pour utiliser les fichier XML pour l'importation du XML
INSERT INTO SCHEMA.FWCOSP (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY) VALUES (327019,'CITYDECL  ',1,2,0,0,'CS_AC_XML',2,2,2,2,2,2,10300027,0,'20180307Globaz          ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (327019,'F','19         ','AC-APG-IJAI-AMAT (XML XSD)','20180307Globaz          ');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (327019,'D','19         ','ALV (XML XSD)','20051231Globaz          ');
--Ajout de propriétés des incriptions CI pour les fichiers XML
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('pavo.inscriptions.isAnnoncesXML','false');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('pavo.centrale.test','true');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('pavo.centrale.url','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('pavo.racine.fichier.centrale','');