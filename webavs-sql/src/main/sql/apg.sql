alter table SCHEMA.APSIPRP add column VFMAAT NUMERIC(4,0);
alter table SCHEMA.APSIPRP add column VFBPEC CHAR(1);
reorg table SCHEMA.APSIPRP;

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237473,'OSIREFRUB',1,1,0,0,'MATERNITE_INDEPENDANT_AVEC_AC',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237473,'D','','Mutterschaft Selbständigerwerb. mit ALV','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237473,'F','','Maternité Indépendant avec AC','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237474,'OSIREFRUB',1,1,0,0,'MATERNITE_INDEPENDANT_SANS_AC',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237474,'D','','Mutterschaft Selbständigerwerb. ohne ALV','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237474,'F','','Maternité Indépendant sans AC','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237476,'OSIREFRUB',1,1,0,0,'MATERNITE_PRESTATIONS_RESTITUER_EMPLOYEUR',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237476,'D','','Mutterschaft Zurückzuerstattende Leistungen Arbeitgeber','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237476,'F','','Maternité Prestations à restituer Employeur','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237486,'OSIREFRUB',1,1,0,0,'MATERNITE_PRESTATIONS_RESTITUER_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237486,'D','','Mutterschaft Zurückzuerstattende Leistungen Selbständigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237486,'F','','Maternité Prestations à restituer Indépendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237477,'OSIREFRUB',1,1,0,0,'MATERNITE_COTISATIONS_AVS_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237477,'D','','Mutterschaft AHV Beiträge Selbständigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237477,'F','','Maternité Cotisations AVS Indépendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237478,'OSIREFRUB',1,1,0,0,'MATERNITE_COTISATIONS_AC_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237478,'D','','Mutterschaft ALV Beiträge Selbständigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237478,'F','','Maternité Cotisations AC Indépendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237479,'OSIREFRUB',1,1,0,0,'MATERNITE_FONDS_COMPENSATIONS_EMPLOYEUR',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237479,'D','','Mutterschaft Verrechungsfonds Arbeitgeber','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237479,'F','','Maternité Fonds de compensation Employeur','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237489,'OSIREFRUB',1,1,0,0,'MATERNITE_FONDS_COMPENSATIONS_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237489,'D','','Mutterschaft Verrechungsfonds Selbständigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237489,'F','','Maternité Fonds de compensation Indépendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237490,'OSIREFRUB',1,1,0,0,'MATERNITE_IMPOT_SOURCE_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237490,'D','','Mutterschaft Quellensteuer Selbständigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237490,'F','','Maternité Impôt à la source Indépendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237495,'OSIREFRUB',1,1,0,0,'APG_INDEPENDANT_AVEC_AC',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237495,'D','','EO Selbständigerwerb. mit ALV','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237495,'F','','APG Indépendant avec AC','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237496,'OSIREFRUB',1,1,0,0,'APG_INDEPENDANT_SANS_AC',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237496,'D','','EO Selbständigerwerb. ohne ALV','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237496,'F','','APG Indépendant sans AC','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237498,'OSIREFRUB',1,1,0,0,'APG_PRESTATIONS_RESTITUER_EMPLOYEUR',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237498,'D','','EO Zurückzuerstattende Leistungen Arbeitgeber','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237498,'F','','APG Prestations à restituer Employeur','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237508,'OSIREFRUB',1,1,0,0,'APG_PRESTATIONS_RESTITUER_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237508,'D','','EO Zurückzuerstattende Leistungen Selbständigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237508,'F','','APG Prestations à restituer Indépendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237499,'OSIREFRUB',1,1,0,0,'APG_COTISATIONS_AVS_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237499,'D','','EO AHV Beiträge Selbständigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237499,'F','','APG Cotisations AVS Indépendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237500,'OSIREFRUB',1,1,0,0,'APG_COTISATIONS_AC_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237500,'D','','EO ALV Beiträge Selbständigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237500,'F','','APG Cotisations AC Indépendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237501,'OSIREFRUB',1,1,0,0,'APG_FONDS_COMPENSATION_EMPLOYEUR',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237501,'D','','EO Verrechungsfonds Arbeitgeber','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237501,'F','','APG Fonds de compensation Employeur','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237511,'OSIREFRUB',1,1,0,0,'APG_FONDS_COMPENSATION_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237511,'D','','EO Verrechungsfonds Selbständigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237511,'F','','APG Fonds de compensation Indépendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237540,'OSIREFRUB',1,1,0,0,'ALLOCATION_NAISSANCE_LAMATGE_EMPLOYEUR',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237540,'D','','Muterschaftentschädigung GE Arbeitgeber','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237540,'F','','Allocation de naissance LAMat Genève Employeur','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237550,'OSIREFRUB',1,1,0,0,'ALLOCATION_NAISSANCE_LAMATGE_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237550,'D','','Muterschaftentschädigung GE Selbständigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237550,'F','','Allocation de naissance LAMat Genève Indépendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237541,'OSIREFRUB',1,1,0,0,'ALLOCATION_ADOPTION_LAMATGE_EMPLOYEUR',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237541,'D','','Adoptionszulage Muterschaftent. GE Arbeitgeber','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237541,'F','','Allocation d''adoption LAMat Genève Employeur','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237551,'OSIREFRUB',1,1,0,0,'ALLOCATION_ADOPTION_LAMATGE_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237551,'D','','Adoptionszulage Muterschaftent. GE Selbständigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237551,'F','','Allocation d''adoption LAMat Genève Indépendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237542,'OSIREFRUB',1,1,0,0,'PRESTATIONS_RESTITUER_LAMATGE_EMPLOYEUR',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237542,'D','','Rückforderungen Muterschaftent. GE Arbeitgeber','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237542,'F','','Prestations à restituer LAMat Genève Employeur','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237552,'OSIREFRUB',1,1,0,0,'PRESTATIONS_RESTITUER_LAMATGE_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237552,'D','','Rückforderungen Muterschaftent. GE Selbständigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237552,'F','','Prestations à restituer LAMat Genève Indépendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237591,'OSIREFRUB',1,1,0,0,'MATERNITE_IMPOT_SOURCE_LAMATGE_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237591,'D','','Mutterschaft Quellensteuer LAMAT Kantonal Selbständigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237591,'F','','Maternité impôt à la source LAMAT cantonale Indépendant','spy');

update SCHEMA.fwcosp set pcosli = 'MATERNITE_ASSURE_AVEC_AC' where pcosid = 237003;
update SCHEMA.fwcoup set pcolut = 'Maternité Assuré avec AC' where pcosid = 237003 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft Versicherte mit ALV' where pcosid = 237003 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'MATERNITE_ASSURE_SANS_AC' where pcosid = 237004;
update SCHEMA.fwcoup set pcolut = 'Maternité Assuré sans AC' where pcosid = 237004 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft Versicherte ohne ALV' where pcosid = 237004 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'MATERNITE_PRESTATIONS_RESTITUER_ASSURE' where pcosid = 237006;
update SCHEMA.fwcoup set pcolut = 'Maternité Prestations à restituer Assuré' where pcosid = 237006 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft Zurückzuerstattende Leistungen Versicherte' where pcosid = 237006 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'MATERNITE_COTISATIONS_AVS_ASSURE' where pcosid = 237007;
update SCHEMA.fwcoup set pcolut = 'Maternité Cotisations AVS Assuré' where pcosid = 237007 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft AHV Beiträge Versicherte' where pcosid = 237007 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'MATERNITE_COTISATIONS_AC_ASSURE' where pcosid = 237008;
update SCHEMA.fwcoup set pcolut = 'Maternité Cotisations AC Assuré' where pcosid = 237008 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft ALV Beiträge Versicherte' where pcosid = 237008 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'MATERNITE_FONDS_COMPENSATIONS_ASSURE' where pcosid = 237009;
update SCHEMA.fwcoup set pcolut = 'Maternité Fonds de compensation Assuré' where pcosid = 237009 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft Verrechungsfonds Versicherte' where pcosid = 237009 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'MATERNITE_IMPOT_SOURCE_ASSURE' where pcosid = 237010;
update SCHEMA.fwcoup set pcolut = 'Maternité Impôt à la source Assuré' where pcosid = 237010 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft Quellensteuer Versicherte' where pcosid = 237010 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'APG_ASSURE_AVEC_AC' where pcosid = 237015;
update SCHEMA.fwcoup set pcolut = 'APG Assuré avec AC' where pcosid = 237015 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'EO Versicherte mit ALV' where pcosid = 237015 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'APG_ASSURE_SANS_AC' where pcosid = 237016;
update SCHEMA.fwcoup set pcolut = 'APG Assuré sans AC' where pcosid = 237016 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'EO Versicherte ohne ALV' where pcosid = 237016 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'APG_PRESTATIONS_RESTITUER_ASSURE' where pcosid = 237018;
update SCHEMA.fwcoup set pcolut = 'APG Prestations à restituer Assuré' where pcosid = 237018 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'EO Zurückzuerstattende Leistungen Versicherte' where pcosid = 237018 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'APG_COTISATIONS_AVS_ASSURE' where pcosid = 237019;
update SCHEMA.fwcoup set pcolut = 'APG Cotisations AVS Assuré' where pcosid = 237019 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'EO AHV Beiträge Versicherte' where pcosid = 237019 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'APG_COTISATIONS_AC_ASSURE' where pcosid = 237020;
update SCHEMA.fwcoup set pcolut = 'APG Cotisations AC Assuré' where pcosid = 237020 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'EO ALV Beiträge Versicherte' where pcosid = 237020 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'APG_FONDS_COMPENSATION_ASSURE' where pcosid = 237021;
update SCHEMA.fwcoup set pcolut = 'APG Fonds de compensation Assuré' where pcosid = 237021 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'EO Verrechungsfonds Versicherte' where pcosid = 237021 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'ALLOCATION_NAISSANCE_LAMATGE_ASSURE' where pcosid = 237060;
update SCHEMA.fwcoup set pcolut = 'Allocation de naissance LAMat Genève Assuré' where pcosid = 237060 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Muterschaftentschädigung GE Versicherte' where pcosid = 237060 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'ALLOCATION_ADOPTION_LAMATGE_ASSURE' where pcosid = 237061;
update SCHEMA.fwcoup set pcolut = 'Allocation d''adoption LAMat Genève Assuré' where pcosid = 237061 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Adoptionszulage Muterschaftent. GE Versicherte' where pcosid = 237061 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'PRESTATIONS_RESTITUER_LAMATGE_ASSURE' where pcosid = 237062;
update SCHEMA.fwcoup set pcolut = 'Prestations à restituer LAMat Genève Assuré' where pcosid = 237062 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Rückforderungen Muterschaftent. GE Versicherte' where pcosid = 237062 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'MATERNITE_IMPOT_SOURCE_LAMATGE_ASSURE' where pcosid = 237111;
update SCHEMA.fwcoup set pcolut = 'Maternité impôt à la source LAMAT cantonale Assuré' where pcosid = 237111 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft Quellensteuer LAMAT Kantonal Versicherte' where pcosid = 237111 and plaide = 'D';