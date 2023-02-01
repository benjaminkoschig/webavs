---------------------------------------------------------------
-----   APG.SQL
---------------------------------------------------------------
-- K221221_001 - CIAM - I221216_014 - Version corrective 1.29.1-1 : LAMat GE incorrecte
-- Propriété pour définir les années LAmat avec un changement de taux
INSERT INTO JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('apg.prestation.maternite.lamat.annees.changement.taux','2008,2023',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz',VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat 'globaz');