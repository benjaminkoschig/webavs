-- Cr�ation du code syst�me 251004
delete from schema.fwcoup where pcosid = 251004;
INSERT INTO schema.fwcoup (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (251004,'D','          ','Annuliert','20160606000000globrje');
INSERT INTO schema.fwcoup (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (251004,'F','          ','Annul�','20160606000000globrje');
INSERT INTO schema.fwcoup (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (251004,'I','          ','Annul�','20160606000000globrje');
delete from schema.fwcosp where pcosid = 251004;
INSERT INTO schema.fwcosp (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY) 
	VALUES (251004,'OSIRETOURS',1,1,0,0,'ETAT_RETOUR_ANNULE',2,2,2,2,2,2,10200051,0,'20160606000000globrje');