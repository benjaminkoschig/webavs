DELETE FROM SCHEMA.FWPARP WHERE SCHEMA.FWPARP.PPARAP = 'APG' and SCHEMA.FWPARP.PPACDI = 'MAXTXJO_0';
INSERT INTO SCHEMA.fwparp (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG','MAXTXJO_0',0,20090101,0,0,0.000000,'',196,'Taux journalier maximum avec droit acquis et sans enfant',0,0,'20160401globazf ');

DELETE FROM SCHEMA.FWPARP WHERE SCHEMA.FWPARP.PPARAP = 'APG' and SCHEMA.FWPARP.PPACDI = 'MAXTXJO_1';
INSERT INTO SCHEMA.fwparp (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG','MAXTXJO_1',0,20090101,0,0,0.000000,'',216,'Taux journalier maximum avec droit acquis et un enfant',0,0,'20160401globazf ');

DELETE FROM SCHEMA.FWPARP WHERE SCHEMA.FWPARP.PPARAP = 'APG' and SCHEMA.FWPARP.PPACDI = 'MAXTXJO_2';
INSERT INTO SCHEMA.fwparp (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG','MAXTXJO_2',0,20090101,0,0,0.000000,'',236,'Taux journalier maximum avec droit acquis et deux enfants',0,0,'20160401globazf ');

DELETE FROM SCHEMA.FWPARP WHERE SCHEMA.FWPARP.PPARAP = 'APG' and SCHEMA.FWPARP.PPACDI = 'MAXTXJO_3';
INSERT INTO SCHEMA.fwparp (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG','MAXTXJO_3',0,20090101,0,0,0.000000,'',245,'Taux journalier maximum avec droit acquis et plus de deux enfants',0,0,'20160401globazf ');

REORG TABLE SCHEMA.fwparp;