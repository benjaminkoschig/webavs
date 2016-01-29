--D0107 Augmentation de la taille du champ remarque
ALTER TABLE SCHEMA.APPRESP ALTER COLUMN VHLREM SET DATA TYPE VARCHAR(1024);
reorg table SCHEMA.APPRESP;

--D0178 Montant minimum pour l'allocation d'exploitation
INSERT INTO SCHEMA.fwparp (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG','APGMINALEX',0,0,0.000000,1,0.000000,'false',67,'Montant minimum pour allocation d''exploitation',0,0,'20160101globazf ');
REORG TABLE SCHEMA.fwparp;