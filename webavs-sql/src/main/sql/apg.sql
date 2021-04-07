insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (52201010,'PRTYPDEMAN',2,1,0,0,'TYPE_PROCHE_AIDANT',2, 1,2,2,2,2,51200001,0,'20210401120000spy');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (52201010,'D','PAI','Betreuenden Angehörigen','20210401120000spy');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (52201010,'F','PAI','Proche aidant','20210401120000spy');

-- Création de la table droit paternité
CREATE TABLE APDROITPROCHEAIDANT
(
    ID_DROIT DECIMAL(15)  NOT NULL,
    PSPY VARCHAR(24),
    PRIMARY KEY (ID_DROIT)
);
