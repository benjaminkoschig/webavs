-- Cr�ation d'un nouveau motif dans l'historique des tiers
INSERT INTO schema.FWCOSP (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY)
VALUES (506008,'PYMOTIFHIS',1,1,0,0,'SEXE',2,1,2,2,2,2,10500006,0,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (506008,'F','SEX','Changement de sexe',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (506008,'D','SEX','[de]Changement de sexe',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (506008,'I','SEX','[it]Changement de sexe',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');