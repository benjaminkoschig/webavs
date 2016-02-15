--D0168 Ajout d'un champ remarque sur le décompte
ALTER TABLE SCHEMA.PCVERDRO ADD BDREMD VARCHAR(1024);
reorg table SCHEMA.PCVERDRO;

--D0118 - Commune politique
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('pegasus.commune.politique.code.reference.rubrique.pc','');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('pegasus.commune.politique.code.reference.rubrique.rfm','');
reorg table SCHEMA.JADEPROP;

--D0118 - Commune politique - Ajout code systeme type de prestastion
 delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='PRTYPPREST');
 delete from SCHEMA.FWCOSP where pptygr ='PRTYPPREST';

 insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 51200007, 'PRTYPPREST', 0,1,3,0, 'Type_prestation', 0,2,2,2,2,0,0,0, 'spy'); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 51200007, 'F', '1', 'Type_prestation', 'spy' ); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 51200007, 'D', '1', 'Type_prestation', 'spy' ); 

 insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52207001, 'PRTYPPREST', 1 ,1,0,0, 'AI', 2,1,2,2,2, 2 , 51200007 ,0, 'spy'); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52207001, 'F', '', 'Invalidité', 'spy' ); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52207001, 'D', '', 'Invalidität', 'spy' ); 
 insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52207002, 'PRTYPPREST', 2 ,1,0,0, 'API', 2,1,2,2,2, 2 , 51200007 ,0, 'spy'); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52207002, 'F', '', 'API', 'spy' ); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52207002, 'D', '', 'HE', 'spy' ); 
 insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52207003, 'PRTYPPREST', 3 ,1,0,0, 'NON_DEFINI', 2,1,2,2,2, 2 , 51200007 ,0, 'spy'); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52207003, 'F', '', 'Non défini', 'spy' ); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52207003, 'D', '', 'Undefiniert', 'spy' ); 
 insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52207004, 'PRTYPPREST', 4 ,1,0,0, 'SURVIVANT', 2,1,2,2,2, 2 , 51200007 ,0, 'spy'); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52207004, 'F', '', 'Survivant', 'spy' ); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52207004, 'D', '', 'Hinterlassene', 'spy' ); 
 insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52207005, 'PRTYPPREST', 5 ,1,0,0, 'VIEILLESSE', 2,1,2,2,2, 2 , 51200007 ,0, 'spy'); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52207005, 'F', '', 'Vieillesse', 'spy' ); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52207005, 'D', '', 'Alter', 'spy' ); 
 insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52207006, 'PRTYPPREST', 6 ,1,0,0, 'PRESTATION_COMPLEMENTAIRE', 2,1,2,2,2, 2 , 51200007 ,0, 'spy'); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52207006, 'F', '', 'PC', 'spy' ); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52207006, 'D', '', 'EL', 'spy' ); 
 insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52207007, 'PRTYPPREST', 7 ,1,0,0, 'REMBOURSEMENT_FRAIS_MEDICAUX', 2,1,2,2,2, 2 , 51200007 ,0, 'spy'); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52207007, 'F', '', 'RFM', 'spy' ); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52207007, 'D', '', 'ELKK', 'spy' ); 
