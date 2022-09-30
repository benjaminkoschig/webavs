---------------------------------------------------------------
-----   EFORM.SQL
---------------------------------------------------------------
INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (82000000, 'GFDATYPE', 0, 1, 0, 2, 'Type DA-Dossier', 2, 2, 2, 2, 2, 1, 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000001, 'GFDATYPE', 1, 1, 0, 2, 'Sollicitation', 1, 2, 2, 2, 2, 2, 82000000, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000002, 'GFDATYPE', 2, 1, 0, 2, 'Envoi', 1, 2, 2, 2, 2, 2, 82000000, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000003, 'GFDATYPE', 3, 1, 0, 2, 'Réception', 1, 2, 2, 2, 2, 2, 82000000, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (82000000, 'F', '', 'Type DA-Dossier', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000000, 'D', '', 'Typ DA-Ordner', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000001, 'F', 'SOLICITAT', 'Sollicitation', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000001, 'D', 'SOLICITAT', 'Aufforderung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000002, 'F', 'SEND_TYPE', 'Envoi', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000002, 'D', 'SEND_TYPE', 'Post', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000003, 'F', 'RECEPTION', 'Réception', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000003, 'D', 'RECEPTION', 'Rezeption', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (82000010, 'GFDASTATUS', 0, 1, 0, 2, 'Status DA-Dossier', 2, 2, 2, 2, 2, 1, 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000011, 'GFDASTATUS', 1, 1, 0, 2, 'A envoyer', 1, 2, 2, 2, 2, 2, 82000010, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000012, 'GFDASTATUS', 2, 1, 0, 2, 'Envoyé', 1, 2, 2, 2, 2, 2, 82000010, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000013, 'GFDASTATUS', 3, 1, 0, 2, 'Attente', 1, 2, 2, 2, 2, 2, 82000010, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000014, 'GFDASTATUS', 4, 1, 0, 2, 'A traiter', 1, 2, 2, 2, 2, 2, 82000010, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000015, 'GFDASTATUS', 5, 1, 0, 2, 'Terminé', 1, 2, 2, 2, 2, 2, 82000010, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000016, 'GFDASTATUS', 5, 1, 0, 2, 'Rejeté', 1, 2, 2, 2, 2, 2, 82000010, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');


INSERT INTO SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (82000010, 'F', '', 'Status DA-Dossier', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000010, 'D', '', 'Status DA-Ordner', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000011, 'F', 'TO_SEND', 'A envoyer', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000011, 'D', 'TO_SEND', 'Senden', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000012, 'F', 'SEND', 'Envoyé', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000012, 'D', 'SEND', 'Gesendet', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000013, 'F', 'WAITING', 'Attente', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000013, 'D', 'WAITING', 'Warten', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000014, 'F', 'TREAT', 'A traiter', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000014, 'D', 'TREAT', 'Zu bearbeite', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000015, 'F', 'ENDED', 'Terminé', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000015, 'D', 'ENDED', 'Beendet', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000016, 'F', 'REJECTED', 'Rejeté', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000016, 'D', 'REJECTED', 'Bedauert', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

CREATE TABLE SCHEMA.GF_DA_DOSSIER
(
    ID                          NUMERIC(15,0) 	NOT NULL,
    MESSAGE_ID                  VARCHAR(255) 	NOT NULL UNIQUE,
    NSS_AFFILIER 	            VARCHAR(255) 	NOT NULL,
    CODE_CAISSE                 NUMERIC(8,0) 	NOT NULL,
    ORIGINAL_TYPE 	            VARCHAR(255) 	NOT NULL,
    TYPE 	                    VARCHAR(255) 	NOT NULL,
    STATUS 	                    VARCHAR(255) 	NOT NULL,
    USER_GESTIONNAIRE 	        VARCHAR(255),
    YOUR_BUSINESS_REFERENCE_ID 	VARCHAR(255),
    OUR_BUSINESS_REFERENCE_ID 	VARCHAR(255) 	NOT NULL,
    CSPY 	                    VARCHAR(24) 	NOT NULL,
    PSPY 	                    VARCHAR(24) 	NOT NULL,
    PRIMARY KEY(ID)
);

INSERT INTO SCHEMA.FWSGRPP (KGROUP, FRIGHT, FCOMMENT, PSPY) VALUES ('gDaDossierUser', 'ADMINISTRATOR', 'Gestionnaires da-dossier', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('eform.dadossier.envoi.departement','',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('eform.dadossier.envoi.telephone','',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('eform.dadossier.mode.test','true',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('eform.sedex.sender.id','',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('eform.groupe.dadossier.gestionnaire','gDaDossierUser',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('eform.dadossier.email.sedex.retour.erreur','',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

-- ajout des droit DaDossier
INSERT INTO SCHEMA.FWSROLP (FCOMMENT,KROLE,PSPY) VALUES ('Da Dossier','rGFDaDossier',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWSREP (FISADD,FISREAD,FISREMOVE,FISUPDATE,KELEMENT,KROLE,PSPY) VALUES ('Y','Y','N','Y','eform.envoi.envoi','rGFDaDossier',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWSREP (FISADD,FISREAD,FISREMOVE,FISUPDATE,KELEMENT,KROLE,PSPY) VALUES ('Y','Y','N','Y','eform.demande.demande','rGFDaDossier',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWSREP (FISADD,FISREAD,FISREMOVE,FISUPDATE,KELEMENT,KROLE,PSPY) VALUES ('N','Y','N','N','eform.suivi.suivi','rGFDaDossier',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');