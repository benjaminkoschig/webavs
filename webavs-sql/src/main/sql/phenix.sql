--WEBAVS-2738 InfoRom 472
ALTER TABLE SCHEMA.CPDOENP ADD COLUMN IDMAVS numeric(15,0) default 0;

--add column MontantTotalRenteavs
ALTER TABLE SCHEMA.CPCRETP ADD COLUMN IKMTRA NUMERIC(12,2);

--add column MessageRenteAvs
ALTER TABLE SCHEMA.CPCRETP ADD COLUMN IKMREA VARCHAR(255);

--add column MontantTotalRenteavs
ALTER TABLE SCHEMA.CPCRETB ADD COLUMN IKMTRA NUMERIC(12,2);

--add column MessageRenteAvs
ALTER TABLE SCHEMA.CPCRETB ADD COLUMN IKMREA VARCHAR(255);

reorg table SCHEMA.CPCRETP allow read access;
reorg table SCHEMA.CPCRETB allow read access;
reorg table SCHEMA.CPDOENP allow read access;