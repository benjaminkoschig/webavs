insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (52201010,'PRTYPDEMAN',2,1,0,0,'TYPE_PROCHE_AIDANT',2, 1,2,2,2,2,51200001,0,'20210401120000spy');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (52201010,'D','PAI','Betreuenden Angehörigen','20210401120000spy');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (52201010,'F','PAI','Proche aidant','20210401120000spy');

INSERT INTO schema.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) VALUES (52001018, 'APGENSERVI', 1, 1, 0, 0, 'Allocation_proche_aidant', 2, 1, 2, 2, 2, 2, 51000001, 0, '20210401120000spy');
INSERT INTO schema.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52001018, 'D', '92', 'Betreuenden Angehörigen', '20210401120000spy');
INSERT INTO schema.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) VALUES (52001018, 'F', '92', 'Proche aidant', '20210401120000spy');

-- Création de la table droit proche aidant
CREATE TABLE APDROITPROCHEAIDANT
(
    ID_DROIT DECIMAL(15)  NOT NULL,
    PSPY VARCHAR(24),
    VBBDUP DECIMAL(1),
    VBISIF DECIMAL(15),
    VBNNCP DECIMAL(9),
    VBNNOC DECIMAL(9),
    VBTREV DECIMAL(8),
    REMARQUE VARCHAR (8000),
    PRIMARY KEY (ID_DROIT)
);
