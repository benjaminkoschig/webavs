--Ajout d'une propri�t� pour choisir le type d'annonce � envoyer 
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('corvus.recapRentes.isAnnoncesXML','false');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('corvus.recapRentes.centrale.test','true');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('corvus.recapRentes.centrale.url','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('corvus.recapRentes.racine.nom.fichier.centrale','');

--Ajout d'une propri�t� du type de caisse (cantonale ou professionel) qui permet de s�lectionner le bon tribunal pour les d�cisions dans les rentes

INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('corvus.TYPE_de_CAISSE','');
