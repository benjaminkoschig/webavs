---------------------------------------------------------------
-----   PEGASUS.SQL
---------------------------------------------------------------

-- PLAT2-1629
-- Creation table
create table SCHEMA.REANN61
(
    ID_ANNONCE_61      DECIMAL(15) not null
        primary key,
    NSS_ANNONCE_61     VARCHAR(64),
    NOUVEAU_MONTANT    VARCHAR(8),
    ANCIEN_MONTANT     VARCHAR(8),
    GENRE              VARCHAR(8),
    DATE_RAPPORT       VARCHAR(8),
    DATE_ANNONCE       VARCHAR(8),
    CODE_RETOUR        VARCHAR(8),
    DEGRE_INVALIDITE   VARCHAR(8),
    FRACTION           VARCHAR(8),
    OBSERVATION        VARCHAR(254),
    QUOTITE_ANNONCE_61 DECIMAL(5, 2),
    PSPY               VARCHAR(24),
    CSPY               VARCHAR(24)
);