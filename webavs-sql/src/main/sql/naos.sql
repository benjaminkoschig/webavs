-- Ajout de proprietes pour le processus REE
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.header.name','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.header.email','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.header.phone','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.header.departement','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.header.other','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.process.paquet','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.process.validation','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ree.message.header.recipient.id','');

-- D0196 : Ajout de proptiétés pour changer le comportement des plan d'affilaition inactif
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ide.planAffiliation.verificationInactif','false');

-- D0197 : Ajout de proptiétés pour changer le comportement des cotisations et prendre en compte ou non les affiliés "pères" si le "fils" n'a pas de coti' AVS
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.ide.cotisation.verificationTaxeSous','false');