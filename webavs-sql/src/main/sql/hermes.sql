INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('hermes.autoriseDateNaissanceUniquementAnnee','false');
ALTER TABLE SCHEMA.HEANNOP ADD RNBARC CHAR(1) DEFAULT '2';
COMMENT ON COLUMN SCHEMA.HEANNOP.RNBARC IS 'Boolean permettant de savoir si un arc 61 a été créé en même temps que l''annonce';
REORG TABLE SCHEMA.HEANNOP;