-- POAVS-2816 Ajout propriété pour la webservices des breakrules
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.endpoint.address','https://www.rapg-a.zas.admin.ch/ApgCore/ws/consultation','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.keystore.password','tsz9ty9of+53aZlasRVLjw==','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.keystore.path','/home/wascnt/keys/T6-150000-1_2.p12','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.keystore.type','JKS','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.webservice.wsdl.path','wsdl/apg/wsdl/rapg-webservice-1-0.wsdl','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.ssi.context.type','TLS','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.webservice.name','RapgConsultationService-1','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.webservice.namespace','http://www.zas.admin.ch/rapg/webservice/consultation/1','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.webservice.sedex.sender.id','T6-150000-1','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('apg.rapg.activer.webservice','true','20200318120000Globaz    ','20200318120000Globaz    ');

-- [WEBAVS-7577] INFOROM SEODOR - Propriété d'appel du WebService
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('apg.rapg.genre.service.seodor','','202003241120000Globaz    ','202003241120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.seodor.wsdl.path','wsdl/apg/wsdl/rapg-webservice-1-0.wsdl','20200318120000Globaz    ','20200318120000Globaz    ');
