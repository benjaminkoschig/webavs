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

-- Définit si on est en mode test
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('corvus.centrale.test','true');
-- Définit l'url où le fichier doit être déposé
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('corvus.centrale.url','');
--Définit la racine du fichier à déposer
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('corvus.racine.nom.fichier.centrale','');