insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64001010, 'PCETATDEM', 7 ,1,0,0, 'ANNULE', 2,1,2,2,2, 2 , 63000001 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64001010, 'F', '', 'Annul�', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64001010, 'D', '', 'Annulliert', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) );

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 64003007, 'PCETATDRO', 7 ,1,0,0, 'ANNULE', 2,1,2,2,2, 2 , 63000003 ,0, (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64003007, 'F', '', 'Annul�', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 64003007, 'D', '', 'Annulliert', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 