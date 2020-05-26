-- Ajout propriété pour la webservices des breakrules
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.endpoint.address','','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.keystore.password','A RENSEIGNER','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.keystore.path','A RENSEIGNER','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.keystore.type','PKCS12','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.webservice.wsdl.path','wsdl/apg/wsdl/rapg-test-query.wsdl','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.ssi.context.type','TLS','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.webservice.name','RapgConsultationService-1','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.webservice.namespace','http://www.zas.admin.ch/rapg/webservice/consultation/1','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.rapg.webservice.sedex.sender.id','A RENSEIGNER','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('apg.rapg.activer.webservice','false','20200318120000Globaz    ','20200318120000Globaz    ');

-- [WEBAVS-7577] INFOROM SEODOR - Propriété d'appel du WebService
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.seodor.endpoint.address','','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.seodor.keystore.password','A RENSEIGNER','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.seodor.keystore.path','A RENSEIGNER','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.seodor.keystore.type','PKCS12','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.seodor.wsdl.path','wsdl/seodor/wsdl/SEODOR-test-query.wsdl','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.seodor.ssi.context.type','TLS','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.seodor.webservice.name','ServicePeriodsService10','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.seodor.webservice.namespace','http://www.zas.admin.ch/seodor/ws/service-periods/1','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.seodor.webservice.sedex.sender.id','A RENSEIGNER','20200318120000Globaz    ','20200318120000Globaz    ');
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('apg.rapg.genre.service.seodor','','202003241120000Globaz    ','202003241120000Globaz    ');

-- SEODOR - Modification taille de champ pour BreakRules sur 4 decimals
ALTER TABLE SCHEMA.APBRULES
    ALTER COLUMN VRBRCO SET DATA TYPE DECIMAL(4);
-- RAPG - Ajouter une colonne pour les libelles
ALTER TABLE CCJUWEB.APBRULES
    ADD COLUMN VRBRLI VARCHAR(4000);

REORG TABLE SCHEMA.APBRULES;
