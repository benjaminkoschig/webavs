
-- K160208_003
delete from SCHEMA.FWCOUP where PCOSID = 203009 ;
delete from SCHEMA.FWCOSP where PCOSID = 203009 ;

insert into SCHEMA.FWCOSP (PCOSID, PPTYGR, PCONCS, PPTYCN, PPTYCL, PPTYSA, PCOSLI, PCOSDF, PCOSDM, PCOSDP, PCOIAN, PCOIDE, PCODFI, PCOITC, PCOISE, PSPY) values (203009, 'OSITYPJOU', 1, 1, 0, 0, 'Type_facturation', 2, 1, 2, 2, 2, 2, 10200003, 0, '20160412120000globazf');

insert into SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) values (203009, 'F', 'FAC', 'Facturation', '20160412120000globazf');
insert into SCHEMA.FWCOUP (PCOSID, PLAIDE, PCOUID, PCOLUT, PSPY) values (203009, 'D', 'FAC', 'Berechnung', '20160412120000globazf');

--D0101 - Extourne d'une section en comptabilité auxiliaire - Taxe Co2
INSERT INTO schema.jadeprop (PROPNAME,PROPVAL) VALUES ('osiris.rubrique.sansExtourne','');