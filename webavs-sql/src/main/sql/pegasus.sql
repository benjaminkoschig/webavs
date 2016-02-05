--D0168 Ajout d'un champ remarque sur le décompte
ALTER TABLE SCHEMA.PCVERDRO ADD BDREMD VARCHAR(1024);
reorg table SCHEMA.PCVERDRO;

INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('pegasus.commune.politique.code.reference.rubrique.pc','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('pegasus.commune.politique.code.reference.rubrique.rfm','');
reorg table SCHEMA.JADEPROP;