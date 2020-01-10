-- Specifique CCVD - S180713_009 RFM - Importation Secutel-AVASAD
-- A deplacer dans version 1.23.?-CCVD ?
insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (66000507,'RFLIBSTS',175,1,0,0,'3_secutel',2, 1,2,2,2,2,65000007,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (66000507,'D','SECUTEL','Secutel','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (66000507,'F','SECUTEL','Secutel','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (66000507,'I','SECUTEL','Secutel','spy');

insert into SCHEMA.RFSTSOI (eeists, eencod, eeitso) values (66000507, '08', 66000052);