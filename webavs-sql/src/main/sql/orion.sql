
delete from SCHEMA.FWCOUP where pcosid in(select pcosid from SCHEMA.FWCOSP where pptygr ='GEBETSWD');
 delete from  SCHEMA.FWCOSP where pptygr ='GEBETSWD';

 insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values ( 11000002, 'GEBETSWD', 0,1,3,0, 'Etat pour les fichiers SwissDec', 0,2,2,2,2,0,0,0,'200512231Globaz'); 

 insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values ( 11020001, 'GEBETSWD', 1 ,2,0,0, 'A_VALIDE', 2,2,2,2,2, 2 , 11000002 ,0,'20051231Globaz'); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) values ( 11020001, 'F', '', 'A valider' ,'20051231Globaz'); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) values ( 11020001, 'D', '', '[de]A valider' ,'20051231Globaz'); 
 insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values ( 11020002, 'GEBETSWD', 2 ,2,0,0, 'REJETE', 2,2,2,2,2, 2 , 11000002 ,0,'20051231Globaz'); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) values ( 11020002, 'F', '', 'Rejet�' ,'20051231Globaz'); 
 insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut,pspy) values ( 11020002, 'D', '', '[de]Rejet�' ,'20051231Globaz'); 
 
 
CREATE TABLE SCHEMA.EBPUCS_FILE (
   ID decimal(12,0) NOT NULL,
   ID_PROCESS_INFO decimal(12,0) NOT NULL,
   ID_FILE_NAME varchar(256) NOT NULL,
   ID_AFFILIATION ID decimal(12,0),
   ANNEE_DECLARATION decimal(12,0) NOT NULL,
   STATUS decimal(12,0) NOT NULL,
   DATE_RECEPTION decimal(12,0) NOT NULL,
   HANDLING_USER varchar(64) NOT NULL,
   NOM_AFFILIE varchar(255) NOT NULL,
   NUMERO_AFFILIE varchar(64) NOT NULL,
   NB_SALAIRES decimal(12,0) NOT NULL,
   PROVENANCE varchar(64) NOT NULL,
   TOTAL_CONTROLE decimal(10,2) NOT NULL,
   SIZE_FILE_IN_KO decimal(12,2),
   NIVEAU_SECURITE decimal(3,0),
   IS_AF_SEUL decimal(1,0),
   DUPLICATE  decimal(1,0),
   SAL_INF_LIMIT  decimal(1,0),
   PSPY char(24),
   PRIMARY KEY(ID)
);


CREATE TABLE SCHEMA.PROCESS_INFO (
   ID decimal(12,0) NOT NULL,
   START_DATE decimal(20,0),
   END_DATE decimal(20,0),
   ETAT varchar(64) NOT NULL,
   TIME_ITEM decimal(12,0),
   TIME_AFTER decimal(12,0),
   TIME_BEFORE decimal(12,0),
   NB_ITEM_TOTAL decimal(12,0),
   NB_ITEM_IN_ERROR decimal(12,0),
   KEY_PROCES varchar(16),
   USER char(32),
   PSPY char(24),
   PRIMARY KEY(ID)
);

