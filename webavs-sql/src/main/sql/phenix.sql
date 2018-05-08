-- Cette propriete permet d'avoir la case "simule conjoint" automatiquement coche en fonction de l'etat civile (515002 = MARIE)
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('phenix.etatCivilCodeSimulConjoint','515002');

-- K180124_002 
ALTER TABLE SCHEMA.CPDOENP ALTER COLUMN IDCOT1 SET DATA TYPE decimal(10,2); 
ALTER TABLE SCHEMA.CPDOENP ALTER COLUMN IDCOT2 SET DATA TYPE decimal(10,2); 
REORG TABLE SCHEMA.CPDOENP;

-- Ajout ID demande portail dans la table des d�cisions
ALTER TABLE SCHEMA.CPDECIP ADD COLUMN EBIDDP NUMERIC(15,0);

-- Insertion libell� libellePassageCotPersAutoPortail dans les propri�t�s PHENIX
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('phenix.libellePassageCotPersAutoPortail','D�cisions personnelles issues du portail');

-- Ajout de propri�t� : Indique si les d�cisions de cotisations personnelles doivent �tre cr��es dans l'EBusiness
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('phenix.adi.manage.decision.in.ebusiness','false');