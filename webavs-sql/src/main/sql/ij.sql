---------------------------------------------------------------
-----   IJ.SQL
---------------------------------------------------------------

-- ENSAVS - S220215_010 - Gestion r�f�rence paiement - Reference QR
-- Ajout id Reference QR dans table d�cisions
ALTER TABLE SCHEMA.IJDECIS ADD XRIRQR varchar(27);
reorg table SCHEMA.IJDECIS;
-- call sysproc.admin_cmd('REORG TABLE IJDECIS');

-- Ajout id Reference QR dans table Repartition de paiement
ALTER TABLE SCHEMA.IJREPARP ADD XRIRQR varchar(27);
reorg table SCHEMA.IJREPARP;
-- call sysproc.admin_cmd('REORG TABLE IJREPARP');