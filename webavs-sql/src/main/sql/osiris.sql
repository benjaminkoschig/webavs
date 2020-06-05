--Creation taux interets zero
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('OSIRIS','TAUXPAND20',0,20010101,0.000000,10200030,0.000000,'',5.000000,'Taux d''intérêts pour les cas en sursis/prorogation',0,0,'20200328101910globaz');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('OSIRIS','TAUXPAND20',0,20200321,0.000000,10200030,0.000000,'',0.000000,'Taux d''intérêts pour les cas en sursis/prorogation',0,0,'20200328101910globaz');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('OSIRIS','TAUXPAND20',0,20200921,0.000000,10200030,0.000000,'',5.000000,'Taux d''intérêts pour les cas en sursis/prorogation',0,0,'20200328101910globaz');

UPDATE SCHEMA.FWPARP SET PPADDE=20200701 WHERE PPACDI='TAUX' AND PPARAP='OSIRIS' AND PPADDE='20200921';
insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237600,'OSIREFRUB',1,1,0,0,'CONTENTIEUX_INTERET_MORATOIRE_PARITAIRE',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237600,'D','','[de] Contentieux - Intérêts moratoires paritaires','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237600,'F','','Contentieux - Intérêts moratoires paritaires','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237601,'OSIREFRUB',1,1,0,0,'CONTENTIEUX_INTERET_MORATOIRE_PERSONNEL',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237601,'D','','[de] Contentieux - Intérêts moratoires personnels','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237601,'F','','Contentieux - Intérêts moratoires personnels','spy');
--Ajout d'une propriété pour la période COVID spécial pour les contentieux
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('aquila.tauxInteret.pandemieSursisProrogation.periodes','21.03.2020:20.09.2020','20200602120000ccjuglo   ','20200602120000ccjuglo   ');