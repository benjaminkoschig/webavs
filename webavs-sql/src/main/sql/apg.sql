-- S210108_001 ENSAVS - Nouv. l�gislation sur l'imp�t � la source
ALTER TABLE SCHEMA.APPRESP
    ADD COLUMN VHMRMF DECIMAL(15,6);
REORG TABLE SCHEMA.APPRESP;

UPDATE SCHEMA.APPRESP SET VHMRMF = VHMRMD;