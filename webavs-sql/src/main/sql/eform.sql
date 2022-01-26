-- ajout des propri�t� jade pour module eFormulaire P14
-- Application class name
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL) VALUES ('eform.applicationClassName', 'ch.globaz.eform.web.application.GFApplication');

-- R�pertoires pour l'importation des eFormulaires P14
-- R�pertoire d'importation du SM client
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL) values ('eform.path.sm.client.form', '/job/jobadm1/SedexBackup/inbox/ok/');
-- R�pertoire d'archivage des fichiers sedex P14 import�s
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL) values ('eform.path.storage.form', '/job/jobadm1/batch/eform/');

-- Cr�ation table de gestion des eFormulaires P14
CREATE TABLE SCHEMA.GF_FORMULAIRE (
    FORMULAIRE_ID varchar(50),
    FORMULAIRE_SUBJECT varchar(8),
    FORMULAIRE_DATE varchar(10),
    FORMULAIRE_TYPE varchar(8),
    FORMULAIRE_NOM varchar(50),
    BENEFICIAIRE_NSS varchar(16),
    BENEFICIAIRE_NOM varchar(40),
    BENEFICIAIRE_PRENOM varchar(40),
    BENEFICIAIRE_DATE_NAISSANCE varchar(10),
    FICHIER_ZIP BLOB(512000)
)