-- Ajout nouveau code syst�me pour nouveau module CS_MODULE_COT_PERS_PORTAIL
INSERT INTO SCHEMA.FWCOSP (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY) 
VALUES (905045,'MUTYPMODUL',2,1,0,0,'Module_CotPers_Portail',2,2,2,2,2,2,10900005,0,'20170501120000globaz');

INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (905045,'D','          ','Pers�nliche Beitr�ge-Ebusiness','20170501120000globaz');
INSERT INTO SCHEMA.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (905045,'F','          ','Cotisations personnelles-Portail','20170501120000globaz');

-- Nouveau module de facturation
INSERT INTO SCHEMA.FAMODUP (IDMODFAC,LIBELLEFR,LIBELLEDE,LIBELLEIT,NOMCLASSE,IDTYPEMODULE,NIVEAUAPPEL,MODIFIERAFACT,PSPY) 
VALUES ((SELECT COALESCE(MAX(IDMODFAC)+1,0) FROM SCHEMA.FAMODUP),'D�cision issue du portail','D�cision issue du portail' ,'D�cision issue du portail','globaz.phenix.api.musca.CPFacturationDecisionImpl',905045,1,'2','20170501120000globaz');
UPDATE SCHEMA.fwincp SET PINCVA = (SELECT COALESCE(MAX(IDMODFAC),0) FROM SCHEMA.FAMODUP) WHERE PINCID = 'FAMODUP';
