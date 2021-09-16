-- FVE - S210312_009 - Cotisations nouvelles assurances
-- Ajout column pour gestion cotisation fixe dans les lignes de déclaration de salaire
ALTER TABLE SCHEMA.DSLIDEP
    ADD COLUMN COTISATION_FIXE NUMERIC(15, 2);

REORG table SCHEMA.DSLIDEP;