DELETE schema.JADEPROP WHERE PROPNAME LIKE 'common.many.ged.actives';
DELETE schema.JADEPROP WHERE PROPNAME LIKE 'common.many.ged.adapter';
INSERT INTO schema.JADEPROP (PROPNAME,PROPVAL) VALUES ('common.many.ged.actives','false');
INSERT INTO schema.JADEPROP (PROPNAME,PROPVAL) VALUES ('common.many.ged.adapter','');