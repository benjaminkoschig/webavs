alter table SCHEMA.APSIPRP add column VFMAAT NUMERIC(4,0);
alter table SCHEMA.APSIPRP add column VFBPEC CHAR(1);
reorg table SCHEMA.APSIPRP;

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237473,'OSIREFRUB',1,1,0,0,'MATERNITE_INDEPENDANT_AVEC_AC',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237473,'D','','Mutterschaft Selbst�ndigerwerb. mit ALV','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237473,'F','','Maternit� Ind�pendant avec AC','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237474,'OSIREFRUB',1,1,0,0,'MATERNITE_INDEPENDANT_SANS_AC',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237474,'D','','Mutterschaft Selbst�ndigerwerb. ohne ALV','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237474,'F','','Maternit� Ind�pendant sans AC','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237476,'OSIREFRUB',1,1,0,0,'MATERNITE_PRESTATIONS_RESTITUER_EMPLOYEUR',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237476,'D','','Mutterschaft Zur�ckzuerstattende Leistungen Arbeitgeber','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237476,'F','','Maternit� Prestations � restituer Employeur','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237486,'OSIREFRUB',1,1,0,0,'MATERNITE_PRESTATIONS_RESTITUER_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237486,'D','','Mutterschaft Zur�ckzuerstattende Leistungen Selbst�ndigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237486,'F','','Maternit� Prestations � restituer Ind�pendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237477,'OSIREFRUB',1,1,0,0,'MATERNITE_COTISATIONS_AVS_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237477,'D','','Mutterschaft AHV Beitr�ge Selbst�ndigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237477,'F','','Maternit� Cotisations AVS Ind�pendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237478,'OSIREFRUB',1,1,0,0,'MATERNITE_COTISATIONS_AC_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237478,'D','','Mutterschaft ALV Beitr�ge Selbst�ndigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237478,'F','','Maternit� Cotisations AC Ind�pendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237479,'OSIREFRUB',1,1,0,0,'MATERNITE_FONDS_COMPENSATIONS_EMPLOYEUR',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237479,'D','','Mutterschaft Verrechungsfonds Arbeitgeber','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237479,'F','','Maternit� Fonds de compensation Employeur','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237489,'OSIREFRUB',1,1,0,0,'MATERNITE_FONDS_COMPENSATIONS_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237489,'D','','Mutterschaft Verrechungsfonds Selbst�ndigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237489,'F','','Maternit� Fonds de compensation Ind�pendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237490,'OSIREFRUB',1,1,0,0,'MATERNITE_IMPOT_SOURCE_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237490,'D','','Mutterschaft Quellensteuer Selbst�ndigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237490,'F','','Maternit� Imp�t � la source Ind�pendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237495,'OSIREFRUB',1,1,0,0,'APG_INDEPENDANT_AVEC_AC',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237495,'D','','EO Selbst�ndigerwerb. mit ALV','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237495,'F','','APG Ind�pendant avec AC','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237496,'OSIREFRUB',1,1,0,0,'APG_INDEPENDANT_SANS_AC',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237496,'D','','EO Selbst�ndigerwerb. ohne ALV','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237496,'F','','APG Ind�pendant sans AC','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237498,'OSIREFRUB',1,1,0,0,'APG_PRESTATIONS_RESTITUER_EMPLOYEUR',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237498,'D','','EO Zur�ckzuerstattende Leistungen Arbeitgeber','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237498,'F','','APG Prestations � restituer Employeur','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237508,'OSIREFRUB',1,1,0,0,'APG_PRESTATIONS_RESTITUER_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237508,'D','','EO Zur�ckzuerstattende Leistungen Selbst�ndigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237508,'F','','APG Prestations � restituer Ind�pendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237499,'OSIREFRUB',1,1,0,0,'APG_COTISATIONS_AVS_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237499,'D','','EO AHV Beitr�ge Selbst�ndigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237499,'F','','APG Cotisations AVS Ind�pendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237500,'OSIREFRUB',1,1,0,0,'APG_COTISATIONS_AC_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237500,'D','','EO ALV Beitr�ge Selbst�ndigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237500,'F','','APG Cotisations AC Ind�pendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237501,'OSIREFRUB',1,1,0,0,'APG_FONDS_COMPENSATION_EMPLOYEUR',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237501,'D','','EO Verrechungsfonds Arbeitgeber','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237501,'F','','APG Fonds de compensation Employeur','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237511,'OSIREFRUB',1,1,0,0,'APG_FONDS_COMPENSATION_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237511,'D','','EO Verrechungsfonds Selbst�ndigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237511,'F','','APG Fonds de compensation Ind�pendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237540,'OSIREFRUB',1,1,0,0,'ALLOCATION_NAISSANCE_LAMATGE_EMPLOYEUR',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237540,'D','','Muterschaftentsch�digung GE Arbeitgeber','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237540,'F','','Allocation de naissance LAMat Gen�ve Employeur','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237550,'OSIREFRUB',1,1,0,0,'ALLOCATION_NAISSANCE_LAMATGE_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237550,'D','','Muterschaftentsch�digung GE Selbst�ndigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237550,'F','','Allocation de naissance LAMat Gen�ve Ind�pendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237541,'OSIREFRUB',1,1,0,0,'ALLOCATION_ADOPTION_LAMATGE_EMPLOYEUR',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237541,'D','','Adoptionszulage Muterschaftent. GE Arbeitgeber','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237541,'F','','Allocation d''adoption LAMat Gen�ve Employeur','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237551,'OSIREFRUB',1,1,0,0,'ALLOCATION_ADOPTION_LAMATGE_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237551,'D','','Adoptionszulage Muterschaftent. GE Selbst�ndigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237551,'F','','Allocation d''adoption LAMat Gen�ve Ind�pendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237542,'OSIREFRUB',1,1,0,0,'PRESTATIONS_RESTITUER_LAMATGE_EMPLOYEUR',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237542,'D','','R�ckforderungen Muterschaftent. GE Arbeitgeber','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237542,'F','','Prestations � restituer LAMat Gen�ve Employeur','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237552,'OSIREFRUB',1,1,0,0,'PRESTATIONS_RESTITUER_LAMATGE_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237552,'D','','R�ckforderungen Muterschaftent. GE Selbst�ndigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237552,'F','','Prestations � restituer LAMat Gen�ve Ind�pendant','spy');

insert into SCHEMA.fwcosp (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values (237591,'OSIREFRUB',1,1,0,0,'MATERNITE_IMPOT_SOURCE_LAMATGE_INDEPENDANT',2, 2,2,2,2,2,10200037,0,'spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237591,'D','','Mutterschaft Quellensteuer LAMAT Kantonal Selbst�ndigerwerb.','spy');
insert into SCHEMA.fwcoup (pcosid,plaide,pcouid,pcolut,pspy) values (237591,'F','','Maternit� imp�t � la source LAMAT cantonale Ind�pendant','spy');

update SCHEMA.fwcosp set pcosli = 'MATERNITE_ASSURE_AVEC_AC' where pcosid = 237003;
update SCHEMA.fwcoup set pcolut = 'Maternit� Assur� avec AC' where pcosid = 237003 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft Versicherte mit ALV' where pcosid = 237003 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'MATERNITE_ASSURE_SANS_AC' where pcosid = 237004;
update SCHEMA.fwcoup set pcolut = 'Maternit� Assur� sans AC' where pcosid = 237004 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft Versicherte ohne ALV' where pcosid = 237004 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'MATERNITE_PRESTATIONS_RESTITUER_ASSURE' where pcosid = 237006;
update SCHEMA.fwcoup set pcolut = 'Maternit� Prestations � restituer Assur�' where pcosid = 237006 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft Zur�ckzuerstattende Leistungen Versicherte' where pcosid = 237006 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'MATERNITE_COTISATIONS_AVS_ASSURE' where pcosid = 237007;
update SCHEMA.fwcoup set pcolut = 'Maternit� Cotisations AVS Assur�' where pcosid = 237007 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft AHV Beitr�ge Versicherte' where pcosid = 237007 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'MATERNITE_COTISATIONS_AC_ASSURE' where pcosid = 237008;
update SCHEMA.fwcoup set pcolut = 'Maternit� Cotisations AC Assur�' where pcosid = 237008 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft ALV Beitr�ge Versicherte' where pcosid = 237008 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'MATERNITE_FONDS_COMPENSATIONS_ASSURE' where pcosid = 237009;
update SCHEMA.fwcoup set pcolut = 'Maternit� Fonds de compensation Assur�' where pcosid = 237009 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft Verrechungsfonds Versicherte' where pcosid = 237009 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'MATERNITE_IMPOT_SOURCE_ASSURE' where pcosid = 237010;
update SCHEMA.fwcoup set pcolut = 'Maternit� Imp�t � la source Assur�' where pcosid = 237010 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft Quellensteuer Versicherte' where pcosid = 237010 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'APG_ASSURE_AVEC_AC' where pcosid = 237015;
update SCHEMA.fwcoup set pcolut = 'APG Assur� avec AC' where pcosid = 237015 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'EO Versicherte mit ALV' where pcosid = 237015 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'APG_ASSURE_SANS_AC' where pcosid = 237016;
update SCHEMA.fwcoup set pcolut = 'APG Assur� sans AC' where pcosid = 237016 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'EO Versicherte ohne ALV' where pcosid = 237016 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'APG_PRESTATIONS_RESTITUER_ASSURE' where pcosid = 237018;
update SCHEMA.fwcoup set pcolut = 'APG Prestations � restituer Assur�' where pcosid = 237018 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'EO Zur�ckzuerstattende Leistungen Versicherte' where pcosid = 237018 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'APG_COTISATIONS_AVS_ASSURE' where pcosid = 237019;
update SCHEMA.fwcoup set pcolut = 'APG Cotisations AVS Assur�' where pcosid = 237019 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'EO AHV Beitr�ge Versicherte' where pcosid = 237019 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'APG_COTISATIONS_AC_ASSURE' where pcosid = 237020;
update SCHEMA.fwcoup set pcolut = 'APG Cotisations AC Assur�' where pcosid = 237020 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'EO ALV Beitr�ge Versicherte' where pcosid = 237020 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'APG_FONDS_COMPENSATION_ASSURE' where pcosid = 237021;
update SCHEMA.fwcoup set pcolut = 'APG Fonds de compensation Assur�' where pcosid = 237021 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'EO Verrechungsfonds Versicherte' where pcosid = 237021 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'ALLOCATION_NAISSANCE_LAMATGE_ASSURE' where pcosid = 237060;
update SCHEMA.fwcoup set pcolut = 'Allocation de naissance LAMat Gen�ve Assur�' where pcosid = 237060 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Muterschaftentsch�digung GE Versicherte' where pcosid = 237060 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'ALLOCATION_ADOPTION_LAMATGE_ASSURE' where pcosid = 237061;
update SCHEMA.fwcoup set pcolut = 'Allocation d''adoption LAMat Gen�ve Assur�' where pcosid = 237061 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Adoptionszulage Muterschaftent. GE Versicherte' where pcosid = 237061 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'PRESTATIONS_RESTITUER_LAMATGE_ASSURE' where pcosid = 237062;
update SCHEMA.fwcoup set pcolut = 'Prestations � restituer LAMat Gen�ve Assur�' where pcosid = 237062 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'R�ckforderungen Muterschaftent. GE Versicherte' where pcosid = 237062 and plaide = 'D';

update SCHEMA.fwcosp set pcosli = 'MATERNITE_IMPOT_SOURCE_LAMATGE_ASSURE' where pcosid = 237111;
update SCHEMA.fwcoup set pcolut = 'Maternit� imp�t � la source LAMAT cantonale Assur�' where pcosid = 237111 and plaide = 'F';
update SCHEMA.fwcoup set pcolut = 'Mutterschaft Quellensteuer LAMAT Kantonal Versicherte' where pcosid = 237111 and plaide = 'D';