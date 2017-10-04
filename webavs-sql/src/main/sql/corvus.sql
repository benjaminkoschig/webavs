CREATE TABLE schema.TEMP_LIGNE_DEBLO AS (
    SELECT *
    FROM schema.RE_LIGNE_DEBLOCAGE
) WITH NO DATA

INSERT INTO schema.TEMP_LIGNE_DEBLO (SELECT * FROM schema.RE_LIGNE_DEBLOCAGE)

DROP TABLE schema.RE_LIGNE_DEBLOCAGE

CREATE TABLE schema.RE_LIGNE_DEBLOCAGE
(
   ID decimal(15,0) PRIMARY KEY NOT NULL,
   ID_TIERS_CREANCIER decimal(15,0),
   ID_ROLE_SECTION decimal(15,0),
   ID_TIERS_ADRESSE_PAIEMENT decimal(15,0),
   ID_APP_ADRESSE_PAIEMENT decimal(15,0),
   ID_SECTION_COMPENSEE decimal(15,0),
   ID_RENTE_ACCORDEE decimal(15,0),
   ID_LOT decimal(15,0),
   CS_TYPE_DEBLOCAGE decimal(15,0),
   CS_ETAT decimal(15,0),
   MONTANT decimal(15,2) NOT NULL,
   REFERENCE_PAIEMENT varchar(255),
   PSPY varchar(24) NOT NULL,
   CSPY varchar(24) NOT NULL
);

INSERT INTO schema.RE_LIGNE_DEBLOCAGE (SELECT * FROM schema.TEMP_LIGNE_DEBLO)

DROP TABLE schema.TEMP_LIGNE_DEBLO

-- S160704_002 Creation table de ventilation dans les rentes (prestations accordees)
CREATE TABLE schema.RE_VENTILATION
(
	ID_VENTILATION DECIMAL(15) NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
	ID_REPRACC DECIMAL(15) NOT NULL,
	MONTANT_VENTILE DECIMAL(15) NOT NULL,
	CS_TYPE_VENTILATION DECIMAL(15) NOT NULL,
	PSPY VARCHAR(24),
	CSPY VARCHAR(24)
);

reorg table schema.RE_VENTILATION;

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 51800065, 'REVENTIL', 0,1,3,0, 'Type de ventilation de rentes', 0,2,2,2,2,0,0,0, 'spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 51800065, 'F', '1', 'Type de ventilation de rentes', 'spy' ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 51800065, 'D', '1', 'Type de ventilation de rentes', 'spy' ); 

insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 52865001, 'REVENTIL', 1 ,1,0,0, 'TYPE_VENTILATION_PART_CANTONALE', 2,1,2,2,2, 2 , 51800065 ,0, 'spy'); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52865001, 'F', '1', 'Part cantonale', 'spy' ); 
insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 52865001, 'D', '1', 'Monatlicher kantonaler', 'spy' ); 
