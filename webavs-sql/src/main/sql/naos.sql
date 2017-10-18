--S170906_001 D0204-1 Avenant: Amélioration du contrôle LAA/LPP
CREATE TABLE SCHEMA.AF_SUIVI_LPP_ANN_SALARIES
(
   ID decimal(15,0) PRIMARY KEY NOT NULL,
   ID_AFFILIATION decimal(15,0) NOT NULL,
   NUMERO_AFFILIE char(15) NOT NULL,
   NSS char(20) NOT NULL,
   NOM_SALARIE char(80),
   MOIS_DEBUT decimal(2,0),
   MOIS_FIN decimal(2,0),
   ANNEE decimal(4,0) NOT NULL,
   SALAIRE decimal(15,2) NOT NULL,   
   NIVEAU_SECURITE decimal(9,0),
   PSPY varchar(24) NOT NULL,
   CSPY varchar(24) NOT NULL
);

