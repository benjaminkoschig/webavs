delete from SCHEMA.FWCOSP where pcosid in(818030);
delete from SCHEMA.FWCOUP where pcosid in(818030);
insert into SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy)values (818030,'VEPARTICUL',1,1,0,0,'CLOTURE_RECAP_MANUELLE',2,1,2,2,2,2,10800018,0, 'spy');
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (818030,'F','CLOTRECAP','Clôture récap manuelle','spy');
insert into SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (818030,'D','CLOTRECAP','[DE]Clôture récap manuelle','spy');