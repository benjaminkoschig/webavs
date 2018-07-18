--Ajout d'une propriété pour choisir le type d'annonce à envoyer 
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('helios.isAnnoncesXML','false');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('helios.centrale.test','true');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('helios.centrale.url','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('helios.racine.nom.fichier.centrale','');
--Ajout d'une colonne contenant le nom du fichier envoyé
ALTER TABLE SCHEMA.CGPERIP ADD COLUMN NOMFICHIER CHAR(256);
REORG TABLE SCHEMA.CGPERIP;