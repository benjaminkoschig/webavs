-- Création du nouveau type de périodes "Enfant recueilli gratuitement par le conjoint"
INSERT INTO schema.FWCOSP (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY)
VALUES (36002014,'SFTYPPER  ',1,1,0,0,'ENFANT_CONJOINT',2,1,2,2,2,2,35000002,0,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (36002014,'F','rgc','Enfant recueilli gratuitement par le conjoint',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (36002014,'D','rgc','Kind, das unentgeltlich vom Ehepartner aufgenommen wurde',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (36002014,'I','rgc','Bambino accolto gratuitamente dal coniuge',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');

ALTER TABLE SCHEMA.SFPERIOD
    ADD COLUMN ID_RECUEILLANT decimal(15);
-- reorg table SCHEMA.SFPERIOD;
call sysproc.admin_cmd('REORG TABLE SFPERIOD');


-- Création des nouveaux codes systèmes pour l'état civil dans la situation familiale
INSERT INTO schema.FWCOSP (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY)
VALUES (36001007,'SFRELAT   ',7,1,0,0,'LPART',2,1,2,2,2,2,35000001,0,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (36001007,'F','          ','LPart',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (36001007,'D','          ','[de]LPart',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOSP (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY)
VALUES (36001008,'SFRELAT   ',8,1,0,0,'LPART_DISSOUS',2,1,2,2,2,2,35000001,0,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (36001008,'F','          ','LPart dissous',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (36001008,'D','          ','[de]LPart dissous',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOSP (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY)
VALUES (36001009,'SFRELAT   ',9,1,0,0,'LPART_SEPARE_DE_FAIT',2,1,2,2,2,2,35000001,0,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (36001009,'F','          ','LPart séparé de fait',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (36001009,'D','          ','[de]LPart séparé de fait',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOSP (PCOSID,PPTYGR,PCONCS,PPTYCN,PPTYCL,PPTYSA,PCOSLI,PCOSDF,PCOSDM,PCOSDP,PCOIAN,PCOIDE,PCODFI,PCOITC,PCOISE,PSPY)
VALUES (36001010,'SFRELAT   ',10,1,0,0,'LPART_SEPARE_JUDICIAIREMENT',2,1,2,2,2,2,35000001,0,VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (36001010,'F','          ','LPart séparé judiciairement',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
INSERT INTO schema.FWCOUP (PCOSID,PLAIDE,PCOUID,PCOLUT,PSPY) VALUES (36001010,'D','          ','[de]LPart séparé judiciairement',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');
-- Mise à jour des codes systèmes existants
update schema.fwcoup set pcolut = 'Marié' where pcosid = 36001001 and plaide = 'F';
update schema.fwcoup set pcolut = 'Verheiratet' where pcosid = 36001001 and plaide = 'D';
update schema.fwcoup set pcolut = 'Divorcé' where pcosid = 36001002 and plaide = 'F';
update schema.fwcoup set pcolut = 'Geschieden' where pcosid = 36001002 and plaide = 'D';
-- Mise à jour de la relation pour chaque couple ayant le même sexe


-- Modifier la relation en "LPart" pour 2 conjoints de même sexe s'il sont "Marié/LPart"
update schema.sfrelcon as rel1 set wkttyp = 36001007 where rel1.wkicon in (
    SELECT rel2.wkicon FROM schema.sfconjoi
                                inner join schema.sfrelcon as rel2 on wjicon = rel2.wkicon
                                inner join schema.sfmbrfam as mbr1 on wjico1 = mbr1.wgimef
                                inner join schema.sfmbrfam as mbr2 on wjico2 = mbr2.wgimef
                                left outer join schema.tipersp as per1 on mbr1.wgitie = per1.htitie
                                left outer join schema.tipersp as per2 on mbr2.wgitie = per2.htitie
    where rel1.wkttyp = 36001001 and (per1.hptsex = per2.hptsex or ((mbr1.wgitie = 0 and mbr2.wgitie = 0 and mbr1.wgtsex = mbr2.wgtsex)
        or (mbr1.wgitie = 0 and mbr1.wgtsex = per2.hptsex) or (mbr2.wgitie = 0 and per1.hptsex = mbr2.wgtsex)))
);

-- Modifier la relation en "LPart dissous" pour 2 conjoints de même sexe s'il sont "Divorcé/LPart dissous"
update schema.sfrelcon as rel1 set wkttyp = 36001008 where rel1.wkicon in (
    SELECT rel2.wkicon FROM schema.sfconjoi
                                inner join schema.sfrelcon as rel2 on wjicon = rel2.wkicon
                                inner join schema.sfmbrfam as mbr1 on wjico1 = mbr1.wgimef
                                inner join schema.sfmbrfam as mbr2 on wjico2 = mbr2.wgimef
                                left outer join schema.tipersp as per1 on mbr1.wgitie = per1.htitie
                                left outer join schema.tipersp as per2 on mbr2.wgitie = per2.htitie
    where rel1.wkttyp = 36001002 and (per1.hptsex = per2.hptsex or ((mbr1.wgitie = 0 and mbr2.wgitie = 0 and mbr1.wgtsex = mbr2.wgtsex)
        or (mbr1.wgitie = 0 and mbr1.wgtsex = per2.hptsex) or (mbr2.wgitie = 0 and per1.hptsex = mbr2.wgtsex)))
);

-- Modifier la relation en "LPart séparé judiciairement" pour 2 conjoints de même sexe s'il sont "séparé judiciairement"
update schema.sfrelcon as rel1 set wkttyp = 36001009 where rel1.wkicon in (
    SELECT rel2.wkicon FROM schema.sfconjoi
                                inner join schema.sfrelcon as rel2 on wjicon = rel2.wkicon
                                inner join schema.sfmbrfam as mbr1 on wjico1 = mbr1.wgimef
                                inner join schema.sfmbrfam as mbr2 on wjico2 = mbr2.wgimef
                                left outer join schema.tipersp as per1 on mbr1.wgitie = per1.htitie
                                left outer join schema.tipersp as per2 on mbr2.wgitie = per2.htitie
    where rel1.wkttyp = 36001003 and (per1.hptsex = per2.hptsex or ((mbr1.wgitie = 0 and mbr2.wgitie = 0 and mbr1.wgtsex = mbr2.wgtsex)
        or (mbr1.wgitie = 0 and mbr1.wgtsex = per2.hptsex) or (mbr2.wgitie = 0 and per1.hptsex = mbr2.wgtsex)))
);

-- Modifier la relation en "LPart séparé de fait" pour 2 conjoints de même sexe s'il sont "séparé de fait"
update schema.sfrelcon as rel1 set wkttyp = 36001010 where rel1.wkicon in (
    SELECT rel2.wkicon FROM schema.sfconjoi
                                inner join schema.sfrelcon as rel2 on wjicon = rel2.wkicon
                                inner join schema.sfmbrfam as mbr1 on wjico1 = mbr1.wgimef
                                inner join schema.sfmbrfam as mbr2 on wjico2 = mbr2.wgimef
                                left outer join schema.tipersp as per1 on mbr1.wgitie = per1.htitie
                                left outer join schema.tipersp as per2 on mbr2.wgitie = per2.htitie
    where rel1.wkttyp = 36001004 and (per1.hptsex = per2.hptsex or ((mbr1.wgitie = 0 and mbr2.wgitie = 0 and mbr1.wgtsex = mbr2.wgtsex)
        or (mbr1.wgitie = 0 and mbr1.wgtsex = per2.hptsex) or (mbr2.wgitie = 0 and per1.hptsex = mbr2.wgtsex)))
);