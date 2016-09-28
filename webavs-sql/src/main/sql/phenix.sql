--InfoRom472 / WEBAVS-2731
delete from SCHEMA.JADEPROP where PROPNAME in('phenix.wirr.webservice.uri.wsdl','phenix.wirr.webservice.namespace','phenix.wirr.webservice.name','phenix.wirr.webservice.sedex.sender.id');
insert into SCHEMA.JADEPROP (propname,propval) values ('phenix.wirr.webservice.uri.wsdl','https://webservice.nrr.zas.admin.ch/wirr_cc/NRRQueryService?wsdl'); 
insert into SCHEMA.JADEPROP (propname,propval) values ('phenix.wirr.webservice.namespace','http://ws.admin.ch/zas/regcent/NRR/0'); 
insert into SCHEMA.JADEPROP (propname,propval) values ('phenix.wirr.webservice.name','NRRQueryService'); 
insert into SCHEMA.JADEPROP (propname,propval) values ('phenix.wirr.webservice.sedex.sender.id',''); 

