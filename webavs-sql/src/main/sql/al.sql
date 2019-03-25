INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('al.motifE411','false','20190301120000Globaz    ','20190301120000Globaz    ');

--WEBAVS-6147 - Ajout d'un paramètre pour ajouter un caractère au NSS lors d'édition CSV
INSERT INTO SCHEMA.ALPARAM (PPAID,PPARPF,PPARIA,PPACDI,PPADDE,PCOSID,PPRADE,PPARPD,PSPY,PCOITC,PPARAP,PPRAVN,CSTYUN,PPARVA)
VALUES ((SELECT MAX(PPAID)+1 FROM SCHEMA.ALPARAM),0.000000,0,'RECAP_FORMAT_NSS',19000101,0,'Paramétrage du NSS sur récapitulatif au format .csv',0.000000,'20190311120000globazf',0,'WEBAF',0.000000,0,'true');