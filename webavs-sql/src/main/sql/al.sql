--Attention CCVS et FER: A ne pas passer les insert de la table ALPARAM
-- S200131_001 ajout parametre dans ALPARAM pour l age de d�but du droit de formation
INSERT INTO SCHEMA.ALPARAM (PPAID,PPARPF,PPARIA,PPACDI,PPADDE,PCOSID,PPRADE,PPARPD,PSPY,PCOITC,PPARAP,PPRAVN,CSTYUN,PPARVA)
VALUES ((SELECT MAX(PPAID)+1 FROM SCHEMA.ALPARAM),0.000000,0,'DEBUT_DROIT_FORMATION',19000101,0,'Age et date du d�but du droit de formation',0.000000,'20200317120000globazf',0,'WEBAF',0.000000,0,'16');
INSERT INTO SCHEMA.ALPARAM (PPAID,PPARPF,PPARIA,PPACDI,PPADDE,PCOSID,PPRADE,PPARPD,PSPY,PCOITC,PPARAP,PPRAVN,CSTYUN,PPARVA)
VALUES ((SELECT MAX(PPAID)+1 FROM SCHEMA.ALPARAM),0.000000,0,'DEBUT_DROIT_FORMATION',20200801,0,'Age et date du d�but du droit de formation',0.000000,'20200317120000globazf',0,'WEBAF',0.000000,0,'15');
--

-- S200224_002 ajout d'une colonne CSCANIMP (canton d'imposition) dans la table ALDOS (DossierModel)
ALTER TABLE SCHEMA.ALDOS
ADD COLUMN CSCANIMP decimal(8,0);
REORG TABLE SCHEMA.ALDOS;

-- S200224_002 ajout d'une propri�t� jade pour la valeur "bar�me" dans l'excel d'imp�ts � la source
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('al.impotsource.bareme','D','20200525120000Globaz    ','20200525120000Globaz    ');

-- S200224_002 ajout d'une colonne CSCANIS (canton d'imposition) dans la table ALENTPRE (EntetePrestationModel)
ALTER TABLE SCHEMA.ALENTPRE
ADD COLUMN CSCANIS decimal(8,0);
REORG TABLE SCHEMA.ALENTPRE;



