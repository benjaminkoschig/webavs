---------------------------------------------------------------
-----   EFORM.SQL
---------------------------------------------------------------
-- P14 : Gestion du sous type du message Sedex
ALTER TABLE SCHEMA.GF_FORMULAIRE ADD COLUMN SUB_TYPE VARCHAR(255);

-- call sysproc.admin_cmd('REORG TABLE GF_FORMULAIRE');
reorg table SCHEMA.GF_FORMULAIRE;