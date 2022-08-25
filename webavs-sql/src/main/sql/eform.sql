---------------------------------------------------------------
-----   EFORM.SQL
---------------------------------------------------------------
INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (82000000, 'F', '', 'Type DA-Dossier', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000000, 'D', '', 'Typ DA-Ordner', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000001, 'F', 'SOLICITATION', 'Sollicitation', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000001, 'D', 'SOLICITATION', 'Aufforderung', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000002, 'F', 'SEND_TYPE', 'Envoi', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000002, 'D', 'SEND_TYPE', 'Post', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000003, 'F', 'RECEPTION', 'Réception', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz'),
       (82000003, 'D', 'RECEPTION', 'Rezeption', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
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
       (82000015, 'D', 'ENDED', 'Beendet', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');