-- Cette propriete permet d'avoir la case "simule conjoint" automatiquement coche en fonction de l'etat civile (515002 = MARIE)
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('phenix.etatCivilCodeSimulConjoint','515002');

-- K180124_002 
ALTER TABLE CCJUWEB.CPDOENP ALTER COLUMN IDCOT1 SET DATA TYPE decimal(10,2); 
ALTER TABLE CCJUWEB.CPDOENP ALTER COLUMN IDCOT2 SET DATA TYPE decimal(10,2); 
CALL SYSPROC.ADMIN_CMD ('REORG TABLE CCJUWEB.CPDOENP');