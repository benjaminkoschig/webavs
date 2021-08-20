-- **** Suppression des propri�t�s existantes
DELETE FROM SCHEMA.jadeprop where propname like 'phenix.ADR%';

-- **** R�injection par script des propri�t�s avec la nouvelle adresse de la caisse H51.03
INSERT INTO SCHEMA.jadeprop (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('phenix.ADR_CAISSE_DE','4, rue du Conseil-G�n�ral / Place de Neuve, 1204 Gen�ve
Tel. 022 807 00 97, Fax 022 807 00 99'
,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user
,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.jadeprop (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('phenix.ADR_CAISSE_FR','4, rue du Conseil-G�n�ral / Place de Neuve, 1204 Gen�ve
T�l. 022 807 00 97, Fax 022 807 00 99'
,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user
,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.jadeprop (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('phenix.ADR_CAISSE_IT','4, rue du Conseil-G�n�ral / Place de Neuve, 1204 Gen�ve
T�l. 022 807 00 97, Fax 022 807 00 99'
,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user
,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.jadeprop (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('phenix.ADRESSE_CAISSE_COURRIER_DE','Ausgleichskasse der Uhrenindustrie, Agentur 3, 4, rue du Conseil-G�n�ral / Place de Neuve, 1204 Gen�ve'
,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user
,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.jadeprop (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('phenix.ADRESSE_CAISSE_COURRIER_FR','Caisse de compensation de l''industrie horlog�re, Agence 3, 4, rue du Conseil-G�n�ral / Place de Neuve, 1204 Gen�ve'
,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user
,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
INSERT INTO SCHEMA.jadeprop (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('phenix.ADRESSE_CAISSE_COURRIER_IT','Caisse de compensation de l''industrie horlog�re, Agence 3, 4, rue du Conseil-G�n�ral / Place de Neuve, 1204 Gen�ve'
,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user
,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);