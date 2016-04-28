
--Table des Affiliations
--add column Activité var255
ALTER TABLE SCHEMA.AFAFFIP ADD MATACT VARCHAR(255);

reorg table SCHEMA.AFAFFIP allow read access;

--Table des annonces IDE
--add column histNumAff 
ALTER TABLE SCHEMA.AFANOIDE ADD AIDHNA CHAR(15);
--add column histNaissance
ALTER TABLE SCHEMA.AFANOIDE ADD AIDHDN NUMERIC(8,0);
--add column histActivite
ALTER TABLE SCHEMA.AFANOIDE ADD AIDHAC VARCHAR(255);
--add column histNogaCode
ALTER TABLE SCHEMA.AFANOIDE ADD AIDHNO CHAR(6);

reorg table SCHEMA.AFANOIDE allow read access;