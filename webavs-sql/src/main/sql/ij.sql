--Ajout d'une propri�t� pour choisir le type d'annonce � envoyer 
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('ij.isAnnoncesXML','false');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('ij.centrale.test','true');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('ij.centrale.url','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('ij.racine.nom.fichier.centrale','');

-- Modification de la taille du champ remarque pour les ind�mnisation IJ S180612_002 D0233
ALTER TABLE SCHEMA.IJBASIND ALTER COLUMN XKLREM SET DATA TYPE VARCHAR(512);