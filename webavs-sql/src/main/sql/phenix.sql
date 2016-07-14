--WEBAVS-2738 InfoRom 472
ALTER TABLE SCHEMA.CPDOENP ADD COLUMN IDMAVS numeric(15,0) default 0;
reorg table SCHEMA.CPDOENP allow read access;