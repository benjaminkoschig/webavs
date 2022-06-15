---------------------------------------------------------------
-----   NAOS.SQL
---------------------------------------------------------------

ALTER TABLE SCHEMA.AFSUAFP
    ADD COLUMN DATE_ENVOIE_CAF DECIMAL(8)
    ADD COLUMN REMARQUE VARCHAR(5000)
    ADD COLUMN EXCEPTION_DATE CHARACTER(1) DEFAULT '2';

reorg table SCHEMA.AFSUAFP;
-- call sysproc.admin_cmd('REORG TABLE AFSUAFP');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('naos.gestion.dossier.caf','false',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');