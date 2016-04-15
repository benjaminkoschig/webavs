-- ajout d'un nouveau champ au niveau du dossier 
ALTER TABLE ccvdqua.ALDOS ADD IDGEST varchar(64) DEFAULT null;
reorg table SCHEMA.ALDOS allow read access;

-- ajout d'une nouvelle propeties pour activer la génération de décisions en file d'attente
insert into SCHEMA.JADEPROP (propname,propval) values ('al.decision.file.attente','false');