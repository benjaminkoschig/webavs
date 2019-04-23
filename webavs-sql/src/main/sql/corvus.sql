--INFOROM S190117_011 INFOROM  Modif. lettres prolong d'�tudes DR3368
INSERT INTO SCHEMA.CTTEXTES (CDITXT,CDLDES,CDLCIL,CDIELE,PSPY)
SELECT
MAX(CTTEXTES2.CDITXT)+1,
'� Falls das Kind die Ausbildung vor dem auf der Ausbildungbest�tigung angegebenen Zeitpunkt unterbricht oder beendet und nicht direkt anschliessend eine neue Ausbildung beginnt, muss uns dies unverz�glich mitgeteilt werden �.',
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
'� Si l�enfant termine ou interrompt sa formation avant l��ch�ance indiqu�e sur l�attestation et s�il n�en reprend pas une nouvelle imm�diatement apr�s, vous �tes tenu de nous informer aussit�t �.',
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
'� Se il figlio finisce o interrompe la sua formazione prima della scadenza indicata sul certificato e se non ne riprende immediatamente un nuova, � necesario informarci immediatamente �.',
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

UPDATE SCHEMA.CTTEXTES SET CDLDES='Deshalb m�chten wir Sie darauf aufmerksam machen, dass, gem�ss den gesetzlichen Bestimmungen, alle �nderungen der pers�nlichen Situation der rentenberechtigten Person der Ausgleichskasse unverz�glich zu melden sind. Die Meldepflicht ist insbesondere erforderlich bei allen Unterbrechungen des Studiums oder der Lehre, die vor dem vorgesehenen Abschlussdatum erfolgen. Zu Unrecht bezogene Versicherungsleistungen sind grunds�tzlich zur�ckzuerstatten.'
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
UPDATE SCHEMA.CTTEXTES SET CDLDES='En effet, nous vous signalons que tout changement dans la situation personnelle du b�n�ficiaire de rente doit, conform�ment aux dispositions l�gales, �tre communiqu� � la caisse de compensation. L�obligation de renseigner s�applique en particulier � toute interruption des �tudes ou de l�apprentissage survenant avant la date pr�vue, sous peine de devoir restituer les rentes vers�es ind�ment.'
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

