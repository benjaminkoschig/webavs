-- Cr�ation de la table du param�trage des priorit�s des jobs
CREATE TABLE SCHEMA.FWJOPRI
(
   KKEY varchar(255) PRIMARY KEY NOT NULL,
   DESCRIPTION varchar(1024),
   JOB varchar(1024),
   PRIORITY varchar(24),
   PSPY char(24)
);
CREATE UNIQUE INDEX FWJOPRI ON SCHEMA.FWJOPRI(KKEY);