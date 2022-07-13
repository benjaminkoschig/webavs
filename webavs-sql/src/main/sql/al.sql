---------------------------------------------------------------
-----   AL.SQL
---------------------------------------------------------------

-- CCVD Activation impot source : todo : mettre dans un SQL sp�cifique
/* INSERT INTO SCHEMA.ALPARAM (PPAID, PSPY, PPACDI, PPARVA, PPRADE, PPADDE, PPARPD, PCOSID, CSTYUN, PPRAVN, PPARAP, PPARIA, PPARPF, PCOITC) VALUES
       (92, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.standard.is', '', 'Allocations LFA aux petits paysans imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (93, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.h.is', '', 'Allocations familiales Si�ge hors canton imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (94, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.ra.is', '', 'Allocations familiales Instit enfance&jeunesse imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (95, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.rb.is', '', 'Allocations familiales Colonies de vacances imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (96, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.rc.is', '', 'Allocations familiales Homes & EMS imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (97, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.rd.is', '', 'Allocations familiales H�pitaux imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (98, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.re.is', '', 'Allocations familiales Instit d�entraide imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (99, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.rf.is', '', 'Allocations familiales Instit lutte alcool&toxico imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (100, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.rl.is', '', 'Allocations familiales Eglises imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (101, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.s.is', '', 'Allocations familiales Si�ge dans le canton imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (102, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.tse.is', '', 'Allocations familiales TSE imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (103, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.caisse.salaire.vs.is', '', 'Allocations familiales VS imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (104, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.standard.independant.is', '', 'Allocations familiales aux PCI imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (105, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.standard.non.actif.is', '', 'Allocations familiales aux PSA imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (106, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.standard.salarie.is', '', 'Allocations familiales Empl. Personnel non agricole imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0),
       (107, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz', 'rubrique.standard.travailleur.agricole.is', '', 'Allocations LFA aux salari�s agricoles imp�t source', '19000101', '0.000000', 0, 0, '0.000000', 'WEBAF', 0, '0.000000', 0);
*/
-- CCVD Activation impot source : ajout des codes syst�me liste
INSERT INTO SCHEMA.FWCOSP
(PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (68025001, 'PTLISTAF', 1, 1, 0, 0, 'D�tail-par-CAF-et-travailleur', 2, 1, 2, 2, 2, 2, 67000025, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP
(PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (68025001, 'D', 'DCAFT', '?D�tail par CAF et travailleur', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP
(PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (68025001, 'F', 'DCAFT', 'D�tail par CAF et travailleur', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP
(PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (68025001, 'I', 'DCAFT', '?D�tail par CAF et travailleur', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOSP
(PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY)
VALUES (68025002, 'PTLISTAF', 1, 1, 0, 0, 'Retenue-et-commission-par-CAF', 2, 1, 2, 2, 2, 2, 67000025, 0, to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP
(PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (68025002, 'D', 'RCCAF', '?Retenue et commission par CAF', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP
(PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (68025002, 'F', 'RCCAF', 'Retenue et commission par CAF', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');

INSERT INTO SCHEMA.FWCOUP
(PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY)
VALUES (68025002, 'I', 'RCCAF', '?Retenue et commission par CAF', to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz');