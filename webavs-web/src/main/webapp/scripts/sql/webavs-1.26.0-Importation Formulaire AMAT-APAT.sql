// Creation table Historique importation APG
CREATE TABLE SCHEMA.HISTORIQUE_IMPORTATION_APG
(
    ID          INTEGER     not null    primary key,
    NSS         VARCHAR(24) not null,
    FICHIER_XML BLOB(512000),
    STATE       INTEGER,
    TYPE_APG    VARCHAR(25),
    PSPY        VARCHAR(25)
);

// Copie des données de la table PAndémie vers la nouvelle table
insert into SCHEMA.HISTORIQUE_IMPORTATION_APG (ID, NSS, FICHIER_XML, STATE, PSPY)
select ID, NSS, FICHIER_XML, STATE, PSPY from HISTORIQUE_APG_PANDEMIE;

// Set TYPE_APG des lignes ajoutées en type PANDEMIE
update SCHEMA.HISTORIQUE_IMPORTATION_APG set TYPE_APG = 'PANDEMIE';