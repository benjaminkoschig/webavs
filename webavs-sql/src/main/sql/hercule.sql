--Rendre inactive les couvertures des affiliés ayant une masse en 50'000 et 150'000
UPDATE SCHEMA.CECOUVP SET CEBCAV='2', PSPY='20170717133700Globaz' WHERE ceicou in (
    select CEICOU from (
        SELECT
            couv.CEICOU,
            (select sum(MASSE) from
                    (SELECT SUM(CUMULMASSE) AS MASSE, compteur.IDRUBRIQUE ,compteur.IDCOMPTEANNEXE, (SELECT propval FROM SCHEMA.JADEPROP where propname = 'pavo.idRubrique') test
                    FROM SCHEMA.CACPTRP AS compteur
                    WHERE
                        ANNEE = 2016
                    group by compteur.IDRUBRIQUE, compteur.IDCOMPTEANNEXE) AS SOUS
                    where test like '%'||IDRUBRIQUE||'%' AND CA.IDCOMPTEANNEXE = SOUS.IDCOMPTEANNEXE ) as compteur
        FROM SCHEMA.CECONTP cont
            left outer join SCHEMA.afaffip as aff on aff.maiaff = cont.maiaff
            left outer join SCHEMA.CACPTAP AS CA ON (aff.MALNAF = CA.IDEXTERNEROLE AND CA.IDTIERS = aff.HTITIE  AND CA.IDROLE = 517002)
            inner join SCHEMA.CECOUVP couv on (couv.MAIAFF = aff.maiaff and couv.CEBCAV = '1')
            inner join SCHEMA.CEATTPTS att on (att.MALNAF = cont.MALNAF and cont.MDDCDE = att.MPPEDE and cont.MDDCFI = att.MPPEFI and att.CEBAAV = '1')
        where
            att.MPNBPT < 11 or att.MPNBPT IS NULL
        ) 
    where compteur > 50000 and compteur < 150000
)