
--PUCS ajout des mois dans les DS pour les AF seul
ALTER TABLE SCHEMA.DSINDP ADD TENMOD numeric(2);

ALTER TABLE SCHEMA.DSINDP ADD TENMOF numeric(2);

reorg table SCHEMA.DSINDP allow read access;
