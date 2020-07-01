-- spécifique CCVD - S200417_003 RFM - Importation TMR
insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (66000508,'RFLIBSTS',176,1,0,0,'TMR',2, 2,2,2,2,2,65000007,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (66000508,'D','TMR','Importierte TMR','20200701120000Globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (66000508,'F','TMR','TMR importé','20200701120000Globaz');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (66000508,'I','TMR','[I]TMR importé','20200701120000Globaz');

insert into SCHEMA.RFSTSOI (eeists, eencod, eeitso) values (66000508, '16', 66000065);