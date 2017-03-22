-- D0136 :  ajout de code système
insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 61000051, 'REMINAVS', 0,1,3,0, 'Rente minimale AVS', 0,2,2,2,2,0,0,0, 'spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 61000051, 'F', '1', 'Rente minimale AVS', 'spy' ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 61000051, 'D', '1', 'Rente minimale AVS', 'spy' ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 61051001, 'REMINAVS', 1 ,1,0,0, 'RENTE_MIN_AVS', 2,1,2,2,2, 2 , 61000051 ,0, 'spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 61051001, 'F', '0', 'Rente minimale de vieillesse', 'spy' ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 61051001, 'D', '0', 'Altersrente Minimum', 'spy' );  

-- D0136 : ajout de plage de valeur pour les revenus minimaux 
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('AL','REMINAVS',0,20130101,0.000000,61051001,0.000000,'',1170.000000,'Rente minimale de vieillesse ',0,0,'20170321114021ccjuglo');
INSERT INTO SCHEMA.FWPARP (PPARAP,PPACDI,PPARIA,PPADDE,PPARPD,PCOSID,PPARPF,PPARVA,PPRAVN,PPRADE,CSTYUN,PCOITC,PSPY) VALUES ('AL','REMINAVS',0,20150101,0.000000,61051001,0.000000,'',1170.000000,'Rente minimale de vieillesse ',0,0,'20170321135241ccjuglo');