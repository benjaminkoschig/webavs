--Ajout d'une propriété pour choisir le type d'annonce à envoyer 
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('corvus.recapRentes.isAnnoncesXML','false');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('corvus.recapRentes.centrale.test','true');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('corvus.recapRentes.centrale.url','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('corvus.recapRentes.racine.nom.fichier.centrale','');

--Ajout d'une propriété du type de caisse (cantonale ou professionel) qui permet de sélectionner le bon tribunal pour les décisions dans les rentes

INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('corvus.TYPE_de_CAISSE','');
