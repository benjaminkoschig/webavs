
--PUCS ajout des mois dans les DS pour les AF seul
ALTER TABLE SCHEMA.DSINDP ADD TENMOD numeric(2);
ALTER TABLE SCHEMA.DSINDP ADD TENMOF numeric(2);
reorg TABLE SCHEMA.DSINDP allow read access;

-- Augmentation du nombre de caractere pour la concatenation du nom et prenom de l utilisateur dans la DS
ALTER TABLE SCHEMA.DSDECLP ALTER COLUMN TALSPY SET DATA TYPE VARCHAR(150);
reorg TABLE SCHEMA.DSDECLP allow read access;