-- ajout des propri�t� jade pour module eFormulaire P14
-- Application class name
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('eform.applicationClassName', 'ch.globaz.eform.web.application.GFApplication', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('eform.email.sedex.validation', '', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('eform.user.gestionnaire.default', '', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

--========================================================================================================================
-- SQL Script for create table (GF_FORMULAIRE)
-- Simple model's class name : (ch.globaz.eform.business.models.GFEFormModel)
--========================================================================================================================
CREATE TABLE SCHEMA.GF_FORMULAIRE
(
    ID 	NUMERIC(15,0) 	NOT NULL,
    DATE 	NUMERIC(8,0) 	NOT NULL,
    BENEFICIAIRE_NSS 	VARCHAR(255) 	NOT NULL,
    BENEFICIAIRE_PRENOM 	VARCHAR(255) 	NOT NULL,
    SUBJECT 	VARCHAR(255) 	NOT NULL,
    MESSAGE_ID 	VARCHAR(255) 	NOT NULL,
    CSPY 	VARCHAR(24) 	NOT NULL,
    BENEFICIAIRE_DATE_NAISSANCE 	NUMERIC(8,0) 	NOT NULL,
    TYPE 	VARCHAR(255) 	NOT NULL,
    BENEFICIAIRE_NOM 	VARCHAR(255) 	NOT NULL,
    PSPY 	VARCHAR(24) 	NOT NULL,
    STATUS 	VARCHAR(255) 	NOT NULL,
    USER_GESTIONNAIRE 	VARCHAR(255) 	,
    PRIMARY KEY(ID)
);
COMMENT ON TABLE SCHEMA.GF_FORMULAIRE is 'contient les donn�es saisies par l''utilisateur selon l''attestation re�ue';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.ID is 'Id technique du formulaire fournis';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.DATE is 'Date d''�mission du formulaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.BENEFICIAIRE_NSS is 'Num�ro AVS du b�nificiaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.BENEFICIAIRE_PRENOM is 'Pr�nom du b�nificiaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.SUBJECT is 'Num�ro du formulaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.MESSAGE_ID is 'Id du formulaire fournis par la central';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.CSPY is 'spy - Champ espion, d�fini quand et qui a cr�� l''entit�';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.BENEFICIAIRE_DATE_NAISSANCE is 'Date de naissance du b�n�ficiaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.TYPE is 'Type du formulaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.BENEFICIAIRE_NOM is 'Nom du b�nificiaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.PSPY is 'spy - Champ espion, d�fini quand et qui a effectu� la derni�re modification de l''entit�';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.STATUS is 'Status du formulaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.USER_GESTIONNAIRE is 'Gestionnaire attitr�';

-- cr�ation des droits et autorisations
INSERT INTO SCHEMA.FWSROLP (KROLE, FCOMMENT, PSPY) VALUES ('rGFAdmin', 'eform', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWSREP (KROLE, KELEMENT, FISREAD, FISADD, FISUPDATE, FISREMOVE, PSPY) VALUES ('rGFAdmin', 'eform', 'Y', 'Y', 'Y', 'Y', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');