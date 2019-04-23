--INFOROM S190117_011 INFOROM  Modif. lettres prolong d'études DR3368
INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY)
SELECT
MAX(CTTEXTES2.CDITXT)+1,
'« Falls das Kind die Ausbildung vor dem auf der Ausbildungbestätigung angegebenen Zeitpunkt unterbricht oder beendet und nicht direkt anschliessend eine neue Ausbildung beginnt, muss uns dies unverzüglich mitgeteilt werden ».',
'de',
(SELECT CTELEMEN3.CCIELE FROM SCHEMA.CTELEMEN as CTELEMEN3 WHERE CTELEMEN3.CCIELE >= 107700000000 AND CTELEMEN3.CCIELE < 107800000000 AND CTELEMEN3.CCNNIV=4 AND CTELEMEN3.CCNPOS =2),
'20190401121605ccjuglo   '
FROM SCHEMA.CTTEXTES as CTTEXTES2
WHERE CTTEXTES2.CDIELE >= 107700000000
AND CTTEXTES2.CDIELE < 107800000000
AND
(
   NOT EXISTS
   (
      SELECT
      *
      FROM SCHEMA.CTTEXTES as CTTEXTES3
      WHERE CTTEXTES3.CDIELE=
      (
         SELECT
         CTELEMEN3.CCIELE
         FROM SCHEMA.CTELEMEN as CTELEMEN3
         WHERE CTELEMEN3.CCIELE >= 107700000000
         AND CTELEMEN3.CCIELE < 107800000000
         AND CTELEMEN3.CCNNIV=4
         AND CTELEMEN3.CCNPOS =2
      )
      AND CTTEXTES3.CDLCIL='de'
   )
);
INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY)
SELECT
MAX(CTTEXTES2.CDITXT)+1,
'« Si l¬enfant termine ou interrompt sa formation avant l¬échéance indiquée sur l¬attestation et s¬il n¬en reprend pas une nouvelle immédiatement après, vous êtes tenu de nous informer aussitôt ».',
'fr',
(SELECT CTELEMEN3.CCIELE FROM SCHEMA.CTELEMEN as CTELEMEN3 WHERE CTELEMEN3.CCIELE >= 107700000000 AND CTELEMEN3.CCIELE < 107800000000 AND CTELEMEN3.CCNNIV=4 AND CTELEMEN3.CCNPOS =2),
'20190401121605ccjuglo   '
FROM SCHEMA.CTTEXTES as CTTEXTES2
WHERE CTTEXTES2.CDIELE >= 107700000000
AND CTTEXTES2.CDIELE < 107800000000
AND
(
   NOT EXISTS
   (
      SELECT
      *
      FROM SCHEMA.CTTEXTES as CTTEXTES3
      WHERE CTTEXTES3.CDIELE=
      (
         SELECT
         CTELEMEN3.CCIELE
         FROM SCHEMA.CTELEMEN as CTELEMEN3
         WHERE CTELEMEN3.CCIELE >= 107700000000
         AND CTELEMEN3.CCIELE < 107800000000
         AND CTELEMEN3.CCNNIV=4
         AND CTELEMEN3.CCNPOS =2
      )
      AND CTTEXTES3.CDLCIL='fr'
   )
);
INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY)
SELECT
MAX(CTTEXTES2.CDITXT)+1,
'« Se il figlio finisce o interrompe la sua formazione prima della scadenza indicata sul certificato e se non ne riprende immediatamente un nuova, è necesario informarci immediatamente ».',
'it',
(SELECT CTELEMEN3.CCIELE FROM SCHEMA.CTELEMEN as CTELEMEN3 WHERE CTELEMEN3.CCIELE >= 107700000000 AND CTELEMEN3.CCIELE < 107800000000 AND CTELEMEN3.CCNNIV=4 AND CTELEMEN3.CCNPOS =2),
'20190401121605ccjuglo   '
FROM SCHEMA.CTTEXTES as CTTEXTES2
WHERE CTTEXTES2.CDIELE >= 107700000000
AND CTTEXTES2.CDIELE < 107800000000
AND
(
   NOT EXISTS
   (
      SELECT
      *
      FROM SCHEMA.CTTEXTES as CTTEXTES3
      WHERE CTTEXTES3.CDIELE=
      (
         SELECT
         CTELEMEN3.CCIELE
         FROM SCHEMA.CTELEMEN as CTELEMEN3
         WHERE CTELEMEN3.CCIELE >= 107700000000
         AND CTELEMEN3.CCIELE < 107800000000
         AND CTELEMEN3.CCNNIV=4
         AND CTELEMEN3.CCNPOS =2
      )
      AND CTTEXTES3.CDLCIL='it'
   )
);

INSERT INTO SCHEMA.CTELEMEN (CCIELE,CCIDOC,CCNPOS,CCNNIV,PSPY,CCBSBD,CCBDES,CCBSEL,CCBEDI) SELECT MAX(CTELEMEN2.CCIELE)+1 ,107700000001,2,4,'20190401121812ccjuglo   ','2','                    ','2','2' FROM SCHEMA.CTELEMEN as CTELEMEN2 WHERE CTELEMEN2.CCIELE >= 107700000000 AND CTELEMEN2.CCIELE < 107800000000 AND  NOT EXISTS(SELECT * FROM SCHEMA.CTELEMEN as CTELEMEN2 WHERE CTELEMEN2.CCIELE >= 107700000000 AND CTELEMEN2.CCIELE < 107800000000 AND  CTELEMEN2.CCNNIV=4 AND CTELEMEN2.CCNPOS =2);

UPDATE SCHEMA.CTTEXTES SET CDLDES='Deshalb möchten wir Sie darauf aufmerksam machen, dass, gemäss den gesetzlichen Bestimmungen, alle Änderungen der persönlichen Situation der rentenberechtigten Person der Ausgleichskasse unverzüglich zu melden sind. Die Meldepflicht ist insbesondere erforderlich bei allen Unterbrechungen des Studiums oder der Lehre, die vor dem vorgesehenen Abschlussdatum erfolgen. Zu Unrecht bezogene Versicherungsleistungen sind grundsätzlich zurückzuerstatten.'
WHERE CDITXT=
(
   SELECT
   CTTEXTES3.CDITXT
   FROM SCHEMA.CTTEXTES as CTTEXTES3
   WHERE CTTEXTES3.CDIELE=
   (
      SELECT
      CTELEMEN3.CCIELE
      FROM SCHEMA.CTELEMEN as CTELEMEN3
      WHERE CTELEMEN3.CCIELE >= 107700000000
      AND CTELEMEN3.CCIELE < 107800000000
      AND CTELEMEN3.CCNNIV=5
      AND CTELEMEN3.CCNPOS =1
   )
   AND CTTEXTES3.CDLCIL='de'
)
;
UPDATE SCHEMA.CTTEXTES SET CDLDES='En effet, nous vous signalons que tout changement dans la situation personnelle du bénéficiaire de rente doit, conformément aux dispositions légales, être communiqué à la caisse de compensation. L¬obligation de renseigner s¬applique en particulier à toute interruption des études ou de l¬apprentissage survenant avant la date prévue, sous peine de devoir restituer les rentes versées indûment.'
WHERE CDITXT=
(
   SELECT
   CTTEXTES3.CDITXT
   FROM SCHEMA.CTTEXTES as CTTEXTES3
   WHERE CTTEXTES3.CDIELE=
   (
      SELECT
      CTELEMEN3.CCIELE
      FROM SCHEMA.CTELEMEN as CTELEMEN3
      WHERE CTELEMEN3.CCIELE >= 107700000000
      AND CTELEMEN3.CCIELE < 107800000000
      AND CTELEMEN3.CCNNIV=5
      AND CTELEMEN3.CCNPOS =1
   )
   AND CTTEXTES3.CDLCIL='fr'
)
;

