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

--ISO20022 - traitement des OG
insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (258003,'OSIOGROG',3,1,0,0,'ISO 20022 pain.001',2, 2,2,2,2,2,10200058,0,'20160727120000globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (258003,'D','','ISO 20022 pain.001','20160727120000globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (258003,'F','','ISO 20022 pain.001','20160727120000globaz');

--nouveaux status et types
delete from schema.fwcoup where pcosid in(select pcosid from schema.fwcosp where pptygr ='OSIOGSET');
delete from schema.fwcosp where pptygr ='OSIOGSET';
insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (10200060,'OSIOGSET',0,1,3,0,'Statut d''execution des transactions',0,1,2,2,2,0,0,0,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (10200060,'F','','Statut d''execution des transactions','20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (10200060,'D','','Statut d''execution des transactions' ,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (10200060,'I','','Statut d''execution des transactions' ,'20160727120000globaz');


insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (260001,'OSIOGSET',1,1,0,0,'aucune',2, 2,2,2,2,2,10200060,0,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (260001,'D','','keine','20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (260001,'F','','aucune','20160727120000globaz');

insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (260002,'OSIOGSET',2,1,0,0,'complet',2, 2,2,2,2,2,10200060,0,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (260002,'D','','vollständig','20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (260002,'F','','complet','20160727120000globaz');

insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (260003,'OSIOGSET',3,1,0,0,'partiel',2, 2,2,2,2,2,10200060,0,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (260003,'D','','teilweise','20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (260003,'F','','partiel','20160727120000globaz');

insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (260004,'OSIOGSET',4,1,0,0,'rejeté',2, 2,2,2,2,2,10200060,0,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (260004,'D','','abgelehnt','20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (260004,'F','','rejeté','20160727120000globaz');






delete from schema.fwcoup where pcosid in(select pcosid from schema.fwcosp where pptygr ='OSIOGTYA');
delete from schema.fwcosp where pptygr ='OSIOGTYA';
insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (10200061,'OSIOGTYA',0,1,3,0,'Type d''avis de l''OG pour ISO20022',0,1,2,2,2,0,0,0,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (10200061,'F','','Type d''avis de l''OG pour ISO20022','20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (10200061,'D','','Type d''avis de l''OG pour ISO20022' ,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (10200061,'I','','Type d''avis de l''OG pour ISO20022' ,'20160727120000globaz');


insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (261001,'OSIOGTYA',1,1,0,0,'aucun',2, 2,2,2,2,2,10200061,0,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (261001,'D','','kein','20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (261001,'F','','aucun','20160727120000globaz');

insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (261002,'OSIOGTYA',2,1,0,0,'detaillé',2, 2,2,2,2,2,10200061,0,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (261002,'D','','detailliert','20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (261002,'F','','detaillé','20160727120000globaz');

insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (261003,'OSIOGTYA',3,1,0,0,'collectif sans detail',2, 2,2,2,2,2,10200061,0,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (261003,'D','','kollektiv ohne Detail','20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (261003,'F','','collectif sans detail','20160727120000globaz');

insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (261004,'OSIOGTYA',4,1,0,0,'collectif avec detail',2, 2,2,2,2,2,10200061,0,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (261004,'D','','kollektiv mit Detail','20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (261004,'F','','collectif avec detail','20160727120000globaz');






delete from schema.fwcoup where pcosid in(select pcosid from schema.fwcosp where pptygr ='OSIOGSEO');
delete from schema.fwcosp where pptygr ='OSIOGSEO';
insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (10200062,'OSIOGSEO',0,1,3,0,'Statut d''execution de l''ordre',0,1,2,2,2,0,0,0,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (10200062,'F','','Statut d''execution de l''ordre','20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (10200062,'D','','Statut d''execution de l''ordre' ,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (10200062,'I','','Statut d''execution de l''ordre' ,'20160727120000globaz');


insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (262001,'OSIOGSEO',1,1,0,0,'à transmettre',2, 2,2,2,2,2,10200062,0,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (262001,'D','','[de]à transmettre','20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (262001,'F','','à transmettre','20160727120000globaz');

insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (262002,'OSIOGSEO',2,1,0,0,'transmis',2, 2,2,2,2,2,10200062,0,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (262002,'D','','[de]transmis','20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (262002,'F','','transmis','20160727120000globaz');

insert into schema.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (262003,'OSIOGSEO',3,1,0,0,'confirmé',2, 2,2,2,2,2,10200062,0,'20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (262003,'D','','[de]confirmé','20160727120000globaz');
insert into schema.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (262003,'F','','confirmé','20160727120000globaz');

--nouveau champ de l'ordre groupé
ALTER TABLE SCHEMA.CAORGRP ADD ISOTYPEAVIS numeric(9);
ALTER TABLE SCHEMA.CAORGRP ADD ISOTRANSACSTAT numeric(9);
ALTER TABLE SCHEMA.CAORGRP ADD ISOORDRESTAT numeric(9);

ALTER TABLE SCHEMA.CAORGRP ADD ISOGEST VARCHAR(24);
ALTER TABLE SCHEMA.CAORGRP ADD ISOHAUTEPRIO VARCHAR(1);
ALTER TABLE SCHEMA.CAORGRP ADD ISONUMLIVR VARCHAR(35);

reorg table SCHEMA.CAORGRP allow read access;

--prop pour ISO/SEPA
insert into SCHEMA.jadeprop (propname, propval) values ('osiris.iso.sepa.nbmax.multiog','2');
insert into SCHEMA.jadeprop (propname, propval) values ('osiris.iso.sepa.nbmax.ovparog','99000');
insert into SCHEMA.jadeprop (propname, propval) values ('osiris.iso.sepa.ftp.host','isotest.postfinance.ch');
insert into SCHEMA.jadeprop (propname, propval) values ('osiris.iso.sepa.ftp.port','10022');
insert into SCHEMA.jadeprop (propname, propval) values ('osiris.iso.sepa.ftp.user','');
insert into SCHEMA.jadeprop (propname, propval) values ('osiris.iso.sepa.ftp.pass','');
insert into SCHEMA.jadeprop (propname, propval) values ('osiris.iso.sepa.ftp.ack.folder','');
insert into SCHEMA.jadeprop (propname, propval) values ('osiris.iso.sepa.ftp.post.folder','');


--table des rejet OSIRIS
DROP TABLE SCHEMA.CAORREJ;
CREATE TABLE SCHEMA.CAORREJ (
	IDORRE NUMERIC(9,0) primary key not null,
	IDORDR NUMERIC(9,0) not null,
	CODE VARCHAR(32),
	PROPRI VARCHAR(255),
	ADDITI VARCHAR(255),
	PSPY CHAR(24)
	);

--trad de CS pour D0048
update SCHEMA.FWCOUP set PCOLUT = 'EL' where PCOSID = 64055001 and PLAIDE = 'D';
update SCHEMA.FWCOUP set PCOLUT = 'EL Invalidität' where PCOSID = 64027003 and PLAIDE = 'D';
update SCHEMA.FWCOUP set PCOLUT = 'EL Hinterlassene' where PCOSID = 64027002 and PLAIDE = 'D';
update SCHEMA.FWCOUP set PCOLUT = 'EL Alter' where PCOSID = 64027001 and PLAIDE = 'D';


