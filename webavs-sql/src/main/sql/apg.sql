-- Cr�ation du nouveau code syst�me
INSERT INTO SCHEMA.fwcosp (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY) VALUES (52001046,'APGENSERVI',1,1,0,0,'SALARIE_EVENEMENTIEL',2,1,2,2,2,2,51000001,0,'20200708000000globrje');
INSERT INTO SCHEMA.fwcoup (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (52001046,'D','406       ','Event-Angestellter','20200708000000globrje');
INSERT INTO SCHEMA.fwcoup (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (52001046,'F','406       ','Salari� �v�nementiel','20200708000000globrje');

-- Ajout nouvel plage de valeur
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG','PANEVENDAT',0,20200601,0.000000,1,0.000000,'',0,'Salari� revenu date d�but',0,0,'20200708000000globrje');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG','PANEVENEMI',0,20200601,0.000000,1,0.000000,'',10000,'Salari� revenu minimal',0,0,'20200708000000globrje');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('APG','PANEVENEMA',0,20200601,0.000000,1,0.000000,'',90000,'Salari� revenu maximal',0,0,'20200708000000globrje');

-- Ajout des nouvelles r�f�rences rubriques
INSERT INTO SCHEMA.fwcosp (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY) VALUES (237432,'OSIREFRUB ',1,1,0,0,'PANDEMIE_SALARIE_EVENEMENTIEL',2,2,2,2,2,2,10200037,0,'20200708000000globrje');
INSERT INTO SCHEMA.fwcoup (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (237432,'D','          ','Event-Angestellter','20200708000000globrje');
INSERT INTO SCHEMA.fwcoup (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (237432,'F','          ','Salari� �v�nementiel','20200708000000globrje');

INSERT INTO SCHEMA.fwcosp (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY) VALUES (237433,'OSIREFRUB ',1,1,0,0,'RESTIT_INDEPENDANT_MANIFESTATION_ANNULEE',2,2,2,2,2,2,10200037,0,'20200708000000globrje');
INSERT INTO SCHEMA.fwcoup (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (237433,'D','          ','Zur�ckerstattung Event-Angestellter','20200708000000globrje');
INSERT INTO SCHEMA.fwcoup (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (237433,'F','          ','Restit. salari� �v�nementiel','20200708000000globrje');

-- Ajout des secteurs et des comptes pour la comptabilisation
DELETE from SCHEMA.CGPCAVP WHERE numero in(3056) and exists (select 0 from SCHEMA.CGPCAVP WHERE numero in (3056));
insert into SCHEMA.CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (3, 3056, NULL, 'Indemnit�s pour salari� �v�nementiel', 'Entsch�digung f�r Event-Angestellter', NULL);

DELETE from SCHEMA.CGPCAVP WHERE numero in(4676) and exists (select 0 from SCHEMA.CGPCAVP WHERE numero in (4676));
insert into SCHEMA.CGPCAVP (TYPE, NUMERO, ESTCLASSE, LIBELLEFR, LIBELLEDE, LIBELLEIT) values (3, 4676, NULL, 'Indemnit�s pour salari� �v�nementiel � restituer', 'Angestellter f�r Veranstaltungen zur�ckzuerstatten', NULL);
