---------------------------------------------------------------
-----   AL.SQL
---------------------------------------------------------------

-- CCVD Activation impot source
INSERT INTO SCHEMA.ALPARAM (PPAID, PSPY, PPACDI, PPARVA, PPRADE, PPADDE, PPARPD, PCOSID, CSTYUN, PPRAVN, PPARAP, PPARIA, PPARPF, PCOITC) VALUES
       (92, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.standard.is', '', 'Allocations LFA aux petits paysans impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (93, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.h.is', '', 'Allocations familiales Siège hors canton impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (94, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.ra.is', '', 'Allocations familiales Instit enfance&jeunesse impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (95, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.rb.is', '', 'Allocations familiales Colonies de vacances impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (96, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.rc.is', '', 'Allocations familiales Homes & EMS impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (97, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.rd.is', '', 'Allocations familiales Hôpitaux impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (98, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.re.is', '', 'Allocations familiales Instit d´entraide impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (99, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.rf.is', '', 'Allocations familiales Instit lutte alcool&toxico impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (100, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.rl.is', '', 'Allocations familiales Eglises impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (101, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.s.is', '', 'Allocations familiales Siège dans le canton impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (102, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.tse.is', '', 'Allocations familiales TSE impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (103, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.vs.is', '', 'Allocations familiales VS impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (104, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.standard.independant.is', '', 'Allocations familiales aux PCI impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (105, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.standard.non.actif.is', '', 'Allocations familiales aux PSA impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (106, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.standard.salarie.is', '', 'Allocations familiales Empl. Personnel non agricole impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (107, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.standard.travailleur.agricole.is', '', 'Allocations LFA aux salariés agricoles impôt source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0);
