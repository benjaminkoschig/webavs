---------------------------------------------------------------
-----   APG.SQL
---------------------------------------------------------------

-- Mise � jour des propri�t�s SEODOR
UPDATE SCHEMA.JADEPROP SET PROPVAL = 'ServicePeriodsService-2-0', PSPY = to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz' WHERE PROPNAME = 'common.seodor.webservice.name';
UPDATE SCHEMA.JADEPROP SET PROPVAL = 'http://www.zas.admin.ch/seodor/ws/service-periods/2', PSPY = to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz' WHERE PROPNAME = 'common.seodor.webservice.namespace';

-- FIELDNAME_NOMBREJOURSSUPP = "VHNNJU";
ALTER TABLE SCHEMA.APPRESP ADD COLUMN VHNNJU DECIMAL(9);

-- FIELDNAME_NOMBREJOURSCONGES = "VHNNJC";
ALTER TABLE SCHEMA.APPRESP ADD COLUMN VHNNJC DECIMAL(9);

-- FIELDNAME_DATEFIN_SAISIE = "VHDFIS";
ALTER TABLE SCHEMA.APPRESP ADD COLUMN VHDFIS DECIMAL(8);
reorg table SCHEMA.APPRESP;
-- call sysproc.admin_cmd('REORG TABLE APPRESP');

-- FIELDNAME_DATEFIN_CALCULEE = "VADDFC";
ALTER TABLE SCHEMA.APDROITPATERNITE ADD COLUMN VADDFC DECIMAL(8);
reorg table SCHEMA.APDROITPATERNITE;
-- call sysproc.admin_cmd('REORG TABLE APDROITPATERNITE');