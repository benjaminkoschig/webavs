-- Propriété permettant de switcher entre l'utilisation de la version 4.1 des xsd ou l'ancienne (3.0)
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('al.generation.annonces.xsd.4.1','false');

-- Ajout de la colonne du pays de domicile de l'enfant dans la table des annonces
ALTER TABLE SCHEMA.ALANNO ADD COLUMN ARPAYE DECIMAL(3) DEFAULT '0' NOT NULL;
COMMENT ON COLUMN SCHEMA.ALANNO.ARPAYE IS 'Pays de domicile de l''enfant';
-- Ajout de la colonne du numéro IDE dans la table des annonces
ALTER TABLE SCHEMA.ALANNO ADD COLUMN ARNIDE VARCHAR(50);
COMMENT ON COLUMN SCHEMA.ALANNO.ARNIDE IS 'Numéro IDE';
REORG TABLE SCHEMA.ALANNO;