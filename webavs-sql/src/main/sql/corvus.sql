--WEBAVS-6846 - S190612_001 - Rente pour enfant - Ajouter remarque
INSERT INTO SCHEMA.CTELEMEN (CCBDES,CCBEDI,CCBSBD,CCBSEL,CCIDOC,CCIELE,CCNNIV,CCNPOS,PSPY) SELECT '','2','2','2',107600000001,MAX(CCIELE)+1,4,9,'20190911160418ccjuglo' FROM SCHEMA.CTELEMEN WHERE CCIDOC=107600000001;
INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) SELECT (SELECT MAX(SCHEMA.CTELEMEN.CCIELE) FROM SCHEMA.CTELEMEN WHERE CCIDOC=107600000001), MAX(CDITXT)+1, 'de', 'Eine Zusatzrente wird separat ausgezahlt.', '20190911160418ccjuglo' FROM SCHEMA.CTTEXTES WHERE SCHEMA.CTTEXTES.CDIELE like '107600000%';
INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) SELECT (SELECT MAX(SCHEMA.CTELEMEN.CCIELE) FROM SCHEMA.CTELEMEN WHERE CCIDOC=107600000001), MAX(CDITXT)+1, 'fr', 'Une rente complémentaire est versée séparément.', '20190911160418ccjuglo' FROM SCHEMA.CTTEXTES WHERE SCHEMA.CTTEXTES.CDIELE like '107600000%';
INSERT INTO SCHEMA.CTTEXTES (CDIELE,CDITXT,CDLCIL,CDLDES,PSPY) SELECT (SELECT MAX(SCHEMA.CTELEMEN.CCIELE) FROM SCHEMA.CTELEMEN WHERE CCIDOC=107600000001), MAX(CDITXT)+1, 'it', 'Una rendita aggiuntiva viene versata separatamente.', '20190911160418ccjuglo' FROM SCHEMA.CTTEXTES WHERE SCHEMA.CTTEXTES.CDIELE like '107600000%';

--WEBAVS-6845 - S190612_001 - Amélioration attestations Fiscales - Cas des femmes veuves
UPDATE SCHEMA.rebacal SET yiitbc = (SELECT pre.ztitbe
                                    FROM SCHEMA.repracc AS pre
                                        INNER JOIN SCHEMA.rereacc ON ztipra = ylirac
                                        INNER JOIN SCHEMA.rebacal AS bac2 ON ylibac = yiibca AND ztitbe <> bac2.yiitbc
                                    WHERE ztlcpr IN ('13', '23')
                                      AND (ztdfdr = 0 OR ztdfdr > 201900)
                                      AND SCHEMA.rebacal.yiibca = bac2.yiibca)
WHERE yiibca IN (SELECT bac.yiibca FROM SCHEMA.repracc AS pre
                                            INNER JOIN SCHEMA.rereacc ON ztipra = ylirac
                                            INNER JOIN SCHEMA.rebacal AS bac ON ylibac = yiibca AND ztitbe <> bac.yiitbc
                 WHERE ztlcpr IN ('13', '23') AND (ztdfdr = 0 or ztdfdr > 201900)
                   AND yiibca NOT IN (SELECT yiibca FROM SCHEMA.rebacal
                                                             INNER JOIN SCHEMA.rereacc ON yiibca = ylibac
                                                             INNER JOIN SCHEMA.repracc ON ylirac = ztipra
                                      WHERE ztlcpr IN ('13', '23') AND (ztdfdr = 0 OR ztdfdr > 201900)
                                      GROUP BY yiibca
                                      HAVING count(*)> 1));