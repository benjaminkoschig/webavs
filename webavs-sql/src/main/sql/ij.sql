--Ajout d'une propriété pour choisir le type d'annonce à envoyer 
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('ij.isAnnoncesXML','false');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('ij.centrale.test','true');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('ij.centrale.url','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('ij.racine.nom.fichier.centrale','');

-- Modification de la taille du champ remarque pour les indémnisation IJ S180612_002 D0233
ALTER TABLE SCHEMA.IJBASIND ALTER COLUMN XKLREM SET DATA TYPE VARCHAR(512);