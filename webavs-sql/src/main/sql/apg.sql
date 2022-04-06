-- Mise à jour des propriétés SEODOR
UPDATE SCHEMA.JADEPROP SET PROPVAL = 'ServicePeriodsService-2-0', PSPY = to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz' WHERE PROPNAME = 'common.seodor.webservice.name';
UPDATE SCHEMA.JADEPROP SET PROPVAL = 'http://www.zas.admin.ch/seodor/ws/service-periods/2', PSPY = to_char(current timestamp, 'YYYYMMDDHH24MISS') concat 'globaz' WHERE PROPNAME = 'common.seodor.webservice.namespace';
