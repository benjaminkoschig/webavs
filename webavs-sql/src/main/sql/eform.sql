-- ajout des propriété jade pour module eFormulaire P14
-- Application class name
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('eform.applicationClassName', 'ch.globaz.eform.web.application.GFApplication', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('eform.email.sedex.validation', '', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

-- Création table de gestion des eFormulaires P14
CREATE TABLE SCHEMA.GF_FORMULAIRE (
    FORMULAIRE_ID varchar(50) NOT NULL,
    FORMULAIRE_SUBJECT varchar(8),
    FORMULAIRE_DATE varchar(10),
    FORMULAIRE_TYPE varchar(8),
    FORMULAIRE_NOM varchar(50),
    BENEFICIAIRE_NSS varchar(16),
    BENEFICIAIRE_NOM varchar(40),
    BENEFICIAIRE_PRENOM varchar(40),
    BENEFICIAIRE_DATE_NAISSANCE varchar(10),
    FICHIER_ZIP BLOB(2147483647),
    PSPY VARCHAR(25),
    PRIMARY KEY (FORMULAIRE_ID)
);

-- Création table de gestion des attachements des eFormulaires P14
CREATE TABLE SCHEMA.GF_ATTACHEMENT (
    ATTACHEMENT_ID DECIMAL(15)  NOT NULL,
    FORMULAIRE_ID varchar(50),
    NOM varchar(50),
    FICHIER BLOB(2147483647),
    PSPY VARCHAR(25),
    PRIMARY KEY (ATTACHEMENT_ID)
);