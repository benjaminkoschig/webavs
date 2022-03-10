-- ajout des propriété jade pour module eFormulaire P14
-- Application class name
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('eform.applicationClassName', 'ch.globaz.eform.web.application.GFApplication', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('eform.email.sedex.validation', '', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('eform.user.gestionnaire.default', '', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
insert into SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('eform.groupe.eform.gestionnaire', 'gEformUser', VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

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
COMMENT ON TABLE SCHEMA.GF_FORMULAIRE is 'contient les données saisies par l''utilisateur selon l''attestation reçue';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.ID is 'Id technique du formulaire fournis';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.DATE is 'Date d''émission du formulaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.BENEFICIAIRE_NSS is 'Numéro AVS du bénificiaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.BENEFICIAIRE_PRENOM is 'Prénom du bénificiaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.SUBJECT is 'Numéro du formulaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.MESSAGE_ID is 'Id du formulaire fournis par la central';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.CSPY is 'spy - Champ espion, défini quand et qui a créé l''entité';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.BENEFICIAIRE_DATE_NAISSANCE is 'Date de naissance du bénéficiaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.TYPE is 'Type du formulaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.BENEFICIAIRE_NOM is 'Nom du bénificiaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.PSPY is 'spy - Champ espion, défini quand et qui a effectué la dernière modification de l''entité';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.STATUS is 'Status du formulaire';
COMMENT ON COLUMN SCHEMA.GF_FORMULAIRE.USER_GESTIONNAIRE is 'Gestionnaire attitré';

-- création des droits et autorisations
INSERT INTO SCHEMA.FWSROLP (KROLE, FCOMMENT, PSPY) VALUES ('rGFAdmin', 'eform', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWSREP (KROLE, KELEMENT, FISREAD, FISADD, FISUPDATE, FISREMOVE, PSPY) VALUES ('rGFAdmin', 'eform', 'Y', 'Y', 'Y', 'Y', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWSROLP (KROLE, FCOMMENT, PSPY) VALUES ('rGFUser', 'eform', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWSROLP (KROLE, FCOMMENT, PSPY) VALUES ('rGFReader', 'eform', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWSROLP (KROLE, FCOMMENT, PSPY) VALUES ('rGFManager', 'eform', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWSREP (KROLE, KELEMENT, FISREAD, FISADD, FISUPDATE, FISREMOVE, PSPY) VALUES ('rGFManager', 'eform', 'Y', 'Y', 'Y', 'Y', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWSREP (KROLE, KELEMENT, FISREAD, FISADD, FISUPDATE, FISREMOVE, PSPY) VALUES ('rGFUser', 'eform', 'Y', 'Y', 'Y', 'Y', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWSREP (KROLE, KELEMENT, FISREAD, FISADD, FISUPDATE, FISREMOVE, PSPY) VALUES ('rGFReader', 'eform', 'Y', 'N', 'N', 'N', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWSGRPP (KGROUP, FRIGHT, FCOMMENT, PSPY) VALUES ('gEformUser', 'ADMINISTRATOR', 'Gestionnaires eform', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (81000000, 'GFSTATUS', 0, 1, 0, 2, 'Status Formulaire', 2, 2, 2, 2, 2, 1, 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000001, 'GFSTATUS', 1, 1, 0, 2, 'Reçu', 1, 2, 2, 2, 2, 1, 81000000, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000002, 'GFSTATUS', 2, 1, 0, 2, 'En traitement', 1, 2, 2, 2, 2, 1, 81000000, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000003, 'GFSTATUS', 3, 1, 0, 2, 'A traiter', 1, 2, 2, 2, 2, 1, 81000000, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000004, 'GFSTATUS', 4, 1, 0, 2, 'A valider', 1, 2, 2, 2, 2, 1, 81000000, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000005, 'GFSTATUS', 5, 1, 0, 2, 'En erreur', 1, 2, 2, 2, 2, 1, 81000000, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000006, 'GFSTATUS', 6, 1, 0, 2, 'Rejeter', 1, 2, 2, 2, 2, 1, 81000000, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000007, 'GFSTATUS', 7, 1, 0, 2, 'Traité', 1, 2, 2, 2, 2, 1, 81000000, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (81000000, 'F', '', 'Status Formulaire', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000000, 'D', '', 'Statusformular', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000001, 'F', 'RECEIVE', 'Reçu', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000001, 'D', 'RECEIVE', 'Empfangen', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000002, 'F', 'PROCESSING', 'En traitement', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000002, 'D', 'PROCESSING', 'wird bearbeitet', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000003, 'F', 'TREAT', 'A traiter', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000003, 'D', 'TREAT', 'Behandeln', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000004, 'F', 'TO_VALIDAT', 'A valider', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000004, 'D', 'TO_VALIDAT', 'Bestätigen', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000005, 'F', 'IN_ERROR', 'En erreur', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000005, 'D', 'IN_ERROR', 'Fehlerhaft', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000006, 'F', 'REJECTED', 'Rejeter', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000006, 'D', 'REJECTED', 'Ablehnen', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000007, 'F', 'TREATY', 'Traité', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (81000007, 'D', 'TREATY', 'Vertrag', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
