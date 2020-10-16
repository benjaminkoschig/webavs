-- [WEBAVS-8330] FERCIAB - S190902_006
INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('draco.majoration.declaration.manuelle.active', '', '20200915103000globaz', '20200915103000globaz');
INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY) VALUES ('draco.majoration.declaration.manuelle.assurance', '', '20200915103000globaz', '20200915103000globaz');

INSERT INTO SCHEMA.FWCOSP(pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values (812028,'VETYPEASSU',1,1,0,0,'Frais_d_administration_maj',2,1,2,2,2,2,10800012,0, '20200915103000globaz');
INSERT INTO SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (812028,'F','E','Frais d¬administration majoration','spy');
INSERT INTO SCHEMA.FWCOUP(pcosid,plaide,pcouid,pcolut, pspy)values (812028,'D','FAD','[DE]Verwaltungskosten Zuschlag','spy');
