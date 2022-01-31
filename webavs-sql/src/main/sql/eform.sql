-- ajout des propriété jade pour module eFormulaire P14
-- Application class name
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('eform.applicationClassName', 'ch.globaz.eform.web.application.GFApplication', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

-- Répertoires pour l'importation des eFormulaires P14
-- Répertoire d'archivage des fichiers sedex P14 importés
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) values ('eform.path.storage.form', '/job/jobadm1/batch/eform/', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

-- Création table de gestion des eFormulaires P14
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
    FICHIER_ZIP BLOB(2147483647),
    PSPY VARCHAR(25)
)