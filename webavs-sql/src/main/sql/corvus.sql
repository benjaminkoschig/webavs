--  POAVS-3499 ENSAVS - S210108_001 ENSAVS - Nouv. l�gislation sur l'imp�t � la source
--  Ajout nouvelle colonnes pour nouvelle l�gislation imp�t source
ALTER TABLE SCHEMA.RECREAN
    ADD COLUMN YSRAND DECIMAL(15,2)
    ADD COLUMN YSRTIM DECIMAL(8,2);
REORG table SCHEMA.RECREAN;