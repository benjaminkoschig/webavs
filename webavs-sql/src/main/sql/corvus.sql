--  POAVS-3499 ENSAVS - S210108_001 ENSAVS - Nouv. l�gislation sur l'imp�t � la source
--  Ajout nouvelle colonnes pour nouvelle l�gislation imp�t source
ALTER TABLE SCHEMA.RECREAN
    ADD COLUMN YSRAND DECIMAL(15,2)
    ADD COLUMN YSRTIM DECIMAL(8,2);
REORG table SCHEMA.RECREAN;


-- POAVS-3405 ENSAVS - S201028_026 - ACOR V4.1 - Gestion de la fraction de rente
-- Nouveau cas sp�cial 33
insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy)
                values (52822033,'RECASSPEC',33,1,0,0,'CAS_SPECIAUX_RENTE_33',2, 1,2,2,2,2,51800022,0,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user));
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (52822033,'D','33','Code 33',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user));
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (52822033,'F','33','Code 33',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user));
-- Nouveau cas sp�cial 85
insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy)
values (52822085,'RECASSPEC',85,1,0,0,'CAS_SPECIAUX_RENTE_85',2, 1,2,2,2,2,51800022,0,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user));
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (52822085,'D','85','Code 85',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user));
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (52822085,'F','85','Code 85',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user));

-- Nouvelle colonne dans table Prestation Accord�e REPRACC
ALTER TABLE schema.REPRACC
    ADD COLUMN QUOTITE_RENTE NUMERIC (5, 2);
REORG table SCHEMA.REPRACC;
-- call SYSPROC.ADMIN_CMD('reorg table REPRACC');

-- Nouvelle colonne dans la table Base de Calcul REBACAL
ALTER TABLE schema.REBACAL
    ADD COLUMN QUOTITE_RENTE NUMERIC (5, 2);
REORG table SCHEMA.REBACAL;
--call SYSPROC.ADMIN_CMD('reorg table REBACAL');

-- Nouvelle colonne dans la table Historique rente REHISTR
ALTER TABLE schema.REHISTR
    ADD COLUMN QUOTITE_RENTE NUMERIC (5, 2);
REORG table SCHEMA.REHISTR;
--call SYSPROC.ADMIN_CMD('reorg table REHISTR');