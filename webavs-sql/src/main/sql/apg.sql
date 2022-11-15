---------------------------------------------------------------
-----   APG.SQL
---------------------------------------------------------------
-- ACOR APG
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('apg.acor.utiliser.version.web','true',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz',to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

-- ENSAVS - S220215_010 - Gestion r�f�rence paiement - Reference QR
-- Ajout id Reference QR dans table Situation Professionnelle
--ALTER TABLE SCHEMA.APSIPRP DROP COLUMN VFIDRQR;
ALTER TABLE SCHEMA.APSIPRP ADD VFIDRQR numeric(15,0);
reorg table SCHEMA.APSIPRP;
-- call sysproc.admin_cmd('REORG TABLE APSIPRP');

-- Ajout id Reference QR dans table Repartition de paiement
--ALTER TABLE SCHEMA.APREPAP DROP COLUMN VIIRQR;
ALTER TABLE SCHEMA.APREPAP ADD VIIRQR numeric(15,0);
reorg table SCHEMA.APREPAP;
-- call sysproc.admin_cmd('REORG TABLE APREPAP');


------------------------------------------------------------
-- ENSAVS-S221021_006-Augment. APG AMat 2023 - annonces RAPG
------------------------------------------------------------

-- breakrule 307 (update APGMINALEX plage valeur existante) Adaptation de l'allocation d'exploitation de CHF 67 � CHF 75.-
INSERT INTO SCHEMA.FWPARP (PPARAP, PPACDI, PPARIA, PPADDE, PPARPD, PCOSID, PPARPF, PPARVA, PPRAVN, PPRADE, CSTYUN, PCOITC, PSPY)
VALUES ('APG', 'APGMINALEX', 0, 20230101, 0.000000, 1, 0.000000, '', 75.000000, 'Montant minimum pour allocation d''exploitation', 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

-- breakrule 321/424 (update MAXTXJO_0 plage valeur existante) Indemnit� journali�re APG sans enfant de CHF 196 � CHF 220.-
INSERT INTO SCHEMA.FWPARP (PPARAP, PPACDI, PPARIA, PPADDE, PPARPD, PCOSID, PPARPF, PPARVA, PPRAVN, PPRADE, CSTYUN, PCOITC, PSPY)
VALUES ('APG', 'MAXTXJO_0', 0, 20230101, 0.000000, 0, 0.000000, '', 220.000000, 'Taux journalier maximum avec droit acquis et sans enfant', 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
-- annonce sedex (update MAXTXJO_1 plage valeur existante) Indemnit� journali�re APG avec enfants de CHF 216 � CHF 242.-
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY)
VALUES ('APG', 'MAXTXJO_1', 0, 20230101, 0.000000, 0, 0.000000, '', 242.000000, 'Taux journalier maximum avec droit acquis et un enfant', 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
-- annonce sedex (update MAXTXJO_2 plage valeur existante) Indemnit� journali�re APG avec enfants de CHF 236 � CHF 264.-
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY)
VALUES ('APG', 'MAXTXJO_2', 0, 20230101, 0.000000, 0, 0.000000, '', 264.000000, 'Taux journalier maximum avec droit acquis et deux enfants', 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
-- annonce sedex (update MAXTXJO_3 plage valeur existante) Indemnit� journali�re APG avec enfants de CHF 245 � CHF 275.-
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY)
VALUES ('APG', 'MAXTXJO_3', 0, 20230101, 0.000000, 0, 0.000000, '', 275.000000, 'Taux journalier maximum avec droit acquis et plus de deux enfants', 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

-- breakrule 321 (cr�ation MAXTXUNI_0 plage valeur) Indemnit� unitaire sans enfant de CHF 62 � CHF 69.-
INSERT INTO SCHEMA.FWPARP (PPARAP, PPACDI, PPARIA, PPADDE, PPARPD, PCOSID, PPARPF, PPARVA, PPRAVN, PPRADE, CSTYUN, PCOITC, PSPY)
VALUES ('APG', 'MAXTXUNI_0', 0, 20090101, 0.000000, 1, 0.000000, '', 62.000000, 'Taux indemnit� unitaire sans enfant', 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWPARP (PPARAP, PPACDI, PPARIA, PPADDE, PPARPD, PCOSID, PPARPF, PPARVA, PPRAVN, PPRADE, CSTYUN, PCOITC, PSPY)
VALUES ('APG', 'MAXTXUNI_0', 0, 20230101, 0.000000, 1, 0.000000, '', 69.000000, 'Taux indemnit� unitaire sans enfant', 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

--- calculerMontantLAMat
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY)
VALUES ('APG', 'M_MINGROS', 0, 20230101, 0.000000, 0, 0.000000, '', 69.000000, 'Montant minimum pour prestation maternite genevoise', 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY)
VALUES ('APG','M_MAXGROS', 0, 20230101, 0.000000, 0, 0.000000, '', 329.600000, 'Montant maximum pour prestation maternite genevoise', 0, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');
