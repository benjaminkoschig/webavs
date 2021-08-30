--  POAVS-3499 ENSAVS - S210108_001 ENSAVS - Nouv. législation sur l'impôt à la source
--  Ajout nouvelle colonnes pour nouvelle législation impôt source
ALTER TABLE SCHEMA.RECREAN
    ADD COLUMN YSRAND DECIMAL(15,2)
    ADD COLUMN YSRTIM DECIMAL(8,2);
REORG table SCHEMA.RECREAN;