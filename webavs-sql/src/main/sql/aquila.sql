insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values ( 5200058, 'COETAPP', 321 ,1,0,0, 'ADB Radié', 2,2,2,2,2, 2 , 15000002 ,0,'spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) values ( 5200058, 'F', '', 'ADB Radié', 'spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) values ( 5200058, 'D', '', 'VS gestrichen', 'spy'); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values ( 5300058, 'COETAEP', 311 ,1,0,0, 'Radiation ADB', 2,2,2,2,2, 2 , 15000003 ,0,'spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) values ( 5300058, 'F', '', 'Radiation ADB', 'spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) values ( 5300058, 'D', '', 'VS Streichung', 'spy'); 

INSERT INTO SCHEMA.COETAPP (ODIETA,OFISEQ,ODTACT,ODTETA,ODMMIN,PSPY,TYPEETAPE,ODBIDE) VALUES (53,1,5300058,5200058,0.00,'                        ',0,'2');
INSERT INTO SCHEMA.COETAPP (ODIETA,OFISEQ,ODTACT,ODTETA,ODMMIN,PSPY,TYPEETAPE,ODBIDE) VALUES (100053,2,5300058,5200058,0.00,'                        ',0,'2');

reorg table SCHEMA.COETAPP;
reorg table SCHEMA.FWCOSP;
reorg table SCHEMA.FWCOUP;