---------------------------------------------------------------
-----   APG.SQL
---------------------------------------------------------------
-- ACOR APG
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('apg.acor.utiliser.version.web','true',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

-- ENSAVS - S220215_010 - Gestion référence paiement - Reference QR
-- Ajout id Reference QR dans table Situation Professionnelle
ALTER TABLE SCHEMA.APSIPRP ADD VFIDRQR varchar(27);
reorg table SCHEMA.APSIPRP;
-- call sysproc.admin_cmd('REORG TABLE APSIPRP');

-- Ajout id Reference QR dans table Repartition de paiement
ALTER TABLE SCHEMA.APREPAP ADD VIIRQR varchar(27);
reorg table SCHEMA.APREPAP;
-- call sysproc.admin_cmd('REORG TABLE APREPAP');
