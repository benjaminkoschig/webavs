-- ajout des propriété jade pour module eFormulaire P14
-- Application class name
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('eform.applicationClassName', 'ch.globaz.eform.web.application.GFApplication', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('eform.email.sedex.validation', '', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('eform.user.gestionnaire.default', '', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

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
    USER_GESTIONNAIRE varchar(50),
    ATTACHEMENT_ID varchar(100) not null,
    PSPY VARCHAR(25),
    PRIMARY KEY (FORMULAIRE_ID)
);