--WEBAVS-3170 (S160708_001 AGRIVIT)
delete from SCHEMA.JADEPROP where PROPNAME in('naos.renouvellement.cap.impression.decisions.pour.type.assurance');
insert into SCHEMA.JADEPROP (propname,propval) values ('naos.renouvellement.cap.impression.decisions.pour.type.assurance','812028,812030'); 
