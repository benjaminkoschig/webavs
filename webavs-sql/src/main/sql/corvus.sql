-- D0200 ajout du nouveau code système 489
delete from SCHEMA.FWCOSP where pcosid = 52810489;
delete from SCHEMA.FWCOUP where pcosid = 52810489;

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52810489, 'REINFIRMIT', 489 ,1,0,0, 'INFIRMITE_489', 2,1,2,2,2, 2 , 51800010 ,0, 'spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52810489, 'F', '489', 'Trisomie 21 (Down-Syndrom)', '20161018000000globlga' ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52810489, 'D', '489', 'Trisomie 21 (Down-Syndrom)', '20161018000000globlga' );