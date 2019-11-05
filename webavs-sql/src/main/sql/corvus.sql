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