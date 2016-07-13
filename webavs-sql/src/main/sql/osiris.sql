-- DEBUT DU REPORT DES SCRIPTS 1.16.0-CCVS

--Suppression des anciens CS
delete from SCHEMA.fwcosp where pcosid=237303;
delete from SCHEMA.fwcoup where pcosid=237303;
delete from SCHEMA.fwcosp where pcosid=237304;
delete from SCHEMA.fwcoup where pcosid=237304;
delete from SCHEMA.fwcosp where pcosid=237305;
delete from SCHEMA.fwcoup where pcosid=237305;
delete from SCHEMA.fwcosp where pcosid=237306;
delete from SCHEMA.fwcoup where pcosid=237306;

--PC AVS EN HOME
insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237303,'OSIREFRUB',1,1,0,0,'PC_AVS_EN_HOME',2, 2,2,2,2,2,10200037,0,'20160621120000globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237303,'F','','PC AVS en home','20160621120000globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237303,'D','','EL AHV im Heim','20160621120000globaz');

--PC AI EN HOME
insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237304,'OSIREFRUB',1,1,0,0,'PC_AI_EN_HOME',2, 2,2,2,2,2,10200037,0,'20160621120000globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237304,'F','','PC AI en home','20160621120000globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237304,'D','','EL IV im Heim','20160621120000globaz');

--PC RESTITUTION AVS EN HOME 
insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237305,'OSIREFRUB',1,1,0,0,'PC_AVS_RESTITUTION_EN_HOME',2, 2,2,2,2,2,10200037,0,'20160621120000globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237305,'F','','PC AVS restitution en home','20160621120000globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237305,'D','','EL AHV Rückgabe im Heim','20160621120000globaz');

--PC RESTITUTION AI EN HOME
insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237306,'OSIREFRUB',1,1,0,0,'PC_AI_RESTITUTION_EN_HOME',2, 2,2,2,2,2,10200037,0,'20160621120000globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237306,'F','','PC AI restitution en home','20160621120000globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237306,'D','','EL IV Rückgabe im Heim','20160621120000globaz');

--RFM AVS EN HOME
insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237307,'OSIREFRUB',1,1,0,0,'RFM_AVS_EN_HOME',2, 2,2,2,2,2,10200037,0,'20160621120000globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237307,'F','','RFM AVS en home','20160621120000globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237307,'D','','ELKK AHV im Heim','20160621120000globaz');

--RFM AI EN HOME
insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237308,'OSIREFRUB',1,1,0,0,'RFM_AI_EN_HOME',2, 2,2,2,2,2,10200037,0,'20160621120000globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237308,'F','','RFM AI en home','20160621120000globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237308,'D','','ELKK IV im Heim','20160621120000globaz');

-- FIN DU REPORT DES SCRIPTS 1.16.0-CCVS