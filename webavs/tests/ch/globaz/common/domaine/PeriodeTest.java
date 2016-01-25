package ch.globaz.common.domaine;

import static org.junit.Assert.*;
import globaz.jade.client.util.JadeDateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.common.domaine.Periode.ComparaisonDePeriode;
import ch.globaz.common.domaine.Periode.StrategieConversionDateMoisAnnee;
import ch.globaz.common.domaine.Periode.TypePeriode;

public class PeriodeTest {

    private Periode periodeJJMMAAAA_1999;
    private Periode periodeJJMMAAAA_2000;
    private Periode periodeJJMMAAAA_Fin1999Debut2000;
    private Periode periodeJJMMAAAA_Fin2000;
    private Periode periodeMMAAAA_1999;
    private Periode periodeMMAAAA_2000;
    private Periode periodeMMAAAA_Fin1999Debut2000;
    private Periode periodeMMAAAA_Fin2000;

    private void assertComparerChevauchementAvecStrategie(ComparaisonDePeriode retourAttendu, Periode unePeriode,
            Periode uneAutrePeriode, StrategieConversionDateMoisAnnee strategie) {

        ComparaisonDePeriode resultat;

        if (strategie != null) {
            resultat = unePeriode.comparerChevauchement(uneAutrePeriode, strategie);
        } else {
            resultat = unePeriode.comparerChevauchement(uneAutrePeriode);
        }

        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, strategie), retourAttendu, resultat);
    }

    private void assertCompareToAvecStrategie(int retourAttendu, Periode unePeriode, Periode uneAutrePeriode,
            StrategieConversionDateMoisAnnee strategie) {

        int resultat;

        if (strategie != null) {
            resultat = unePeriode.compareTo(uneAutrePeriode, strategie);
        } else {
            resultat = unePeriode.compareTo(uneAutrePeriode);
        }

        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, strategie), retourAttendu, resultat);
    }

    private void assertDifference(SortedSet<Periode> resultatsAttendus, Periode unePeriode, Periode uneAutrePeriode,
            StrategieConversionDateMoisAnnee strategie) {
        this.assertDifference(resultatsAttendus, unePeriode, uneAutrePeriode, strategie, false);
    }

    private void assertDifference(SortedSet<Periode> resultatsAttendus, Periode unePeriode, Periode uneAutrePeriode,
            StrategieConversionDateMoisAnnee strategie, boolean symetrique) {

        SortedSet<Periode> resultats;

        if (strategie == null) {
            if (symetrique) {
                resultats = unePeriode.differenceSymetrique(uneAutrePeriode);
            } else {
                resultats = unePeriode.difference(uneAutrePeriode);
            }
        } else {
            if (symetrique) {
                resultats = unePeriode.differenceSymetrique(uneAutrePeriode, strategie);
            } else {
                resultats = unePeriode.difference(uneAutrePeriode, strategie);
            }
        }

        Periode[] resultatsTableau = resultats.toArray(new Periode[2]);
        Periode[] resultatsAttendusTableau = resultatsAttendus.toArray(new Periode[2]);

        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, strategie)
                + "\nNombre de périodes retournées différent", resultatsAttendusTableau.length, resultatsTableau.length);
        for (int i = 0; i < resultatsTableau.length; i++) {
            Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, strategie)
                    + getMessageComparaisonResultats(resultatsAttendusTableau[i], resultatsTableau[i]),
                    resultatsAttendusTableau[i], resultatsTableau[i]);
        }

    }

    private void assertDifferenceSymetrique(SortedSet<Periode> resultatsAttendus, Periode unePeriode,
            Periode uneAutrePeriode, StrategieConversionDateMoisAnnee strategie) {
        this.assertDifference(resultatsAttendus, unePeriode, uneAutrePeriode, strategie, true);
    }

    private void assertIntersection(Periode resultatAttendu, Periode unePeriode, Periode uneAutrePeriode,
            StrategieConversionDateMoisAnnee strategie) {
        Periode resultat;

        if (strategie == null) {
            resultat = unePeriode.intersection(uneAutrePeriode);
        } else {
            resultat = unePeriode.intersection(uneAutrePeriode, strategie);
        }

        Assert.assertEquals(
                getMessageErreur(unePeriode, uneAutrePeriode, strategie)
                        + getMessageComparaisonResultats(resultatAttendu, resultat), resultatAttendu, resultat);
    }

    private void assertIntersectionMois(Periode resultatAttendu, Periode unePeriode, Periode uneAutrePeriode) {
        Periode resultat;

        resultat = unePeriode.intersectionMois(uneAutrePeriode);

        Assert.assertEquals(
                getMessageErreur(unePeriode, uneAutrePeriode, null)
                        + getMessageComparaisonResultats(resultatAttendu, resultat), resultatAttendu, resultat);
    }

    private void assertUnion(Periode expectedResult, Periode periode1, Periode periode2,
            StrategieConversionDateMoisAnnee strategie) {

        Periode result;

        if (strategie == null) {
            result = periode1.union(periode2);
        } else {
            result = periode1.union(periode2, strategie);
        }

        Assert.assertEquals(
                getMessageErreur(periode1, periode2, strategie)
                        + getMessageComparaisonResultats(expectedResult, result), expectedResult, result);
    }

    private void assertUnionMois(Periode expectedResult, Periode periode1, Periode periode2) {
        Periode result;

        result = periode1.unionMois(periode2);

        Assert.assertEquals(
                getMessageErreur(periode1, periode2, null) + getMessageComparaisonResultats(expectedResult, result),
                expectedResult, result);
    }

    private String getMessageComparaisonResultats(Periode resultatAttendu, Periode resultat) {

        StringBuilder messageComparaisonResultats = new StringBuilder();
        messageComparaisonResultats.append("expected : ");
        if (resultatAttendu == null) {
            messageComparaisonResultats.append("null");
        } else {
            messageComparaisonResultats.append("[").append(resultatAttendu.getDateDebut()).append(" - ")
                    .append(resultatAttendu.getDateFin()).append("]");
        }
        messageComparaisonResultats.append("\nresult : ");
        if (resultat == null) {
            messageComparaisonResultats.append("null");
        } else {
            messageComparaisonResultats.append("[").append(resultat.getDateDebut()).append(" - ")
                    .append(resultat.getDateFin()).append("]");
        }
        messageComparaisonResultats.append("\n");

        return messageComparaisonResultats.toString();
    }

    private String getMessageErreur(Periode unePeriode, Periode uneAutrePeriode,
            StrategieConversionDateMoisAnnee strategie) {

        StringBuilder messageErreur = new StringBuilder();
        messageErreur.append("\n[").append(unePeriode.getDateDebut()).append(" - ").append(unePeriode.getDateFin())
                .append("]");
        if (TypePeriode.MOIS_ANNEE.equals(unePeriode.getType()) && (strategie != null)) {
            messageErreur.append(" -> stratégie : ").append(getNomStrategie(strategie));
        }
        messageErreur.append("\n[").append(uneAutrePeriode.getDateDebut()).append(" - ")
                .append(uneAutrePeriode.getDateFin()).append("]");
        if (TypePeriode.MOIS_ANNEE.equals(uneAutrePeriode.getType()) && (strategie != null)) {
            messageErreur.append(" -> stratégie : ").append(getNomStrategie(strategie));
        }
        messageErreur.append("\n");

        return messageErreur.toString();
    }

    private String getNomStrategie(StrategieConversionDateMoisAnnee strategie) {
        if (strategie == null) {
            return "par défaut";
        }
        switch (strategie) {
            case DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS:
                return "Debut: dernier du mois / Fin: dernier du mois";
            case DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS:
                return "Debut: dernier du mois / Fin: premier du mois";
            case DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS:
                return "Debut: premier du mois / Fin: dernier du mois";
            case DATE_DEBUT_PERMIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS:
                return "Debut: premier du mois / Fin: premier du mois";
        }
        return "";
    }

    @Before
    public void setUp() throws Exception {
        periodeJJMMAAAA_1999 = new Periode("01.01.1999", "31.12.1999");
        periodeJJMMAAAA_2000 = new Periode("01.01.2000", "31.12.2000");
        periodeJJMMAAAA_Fin1999Debut2000 = new Periode("01.12.1999", "31.01.2000");
        periodeJJMMAAAA_Fin2000 = new Periode("01.11.2000", "31.12.2000");
        periodeMMAAAA_1999 = new Periode("01.1999", "12.1999");
        periodeMMAAAA_2000 = new Periode("01.2000", "12.2000");
        periodeMMAAAA_Fin1999Debut2000 = new Periode("11.1999", "02.2000");
        periodeMMAAAA_Fin2000 = new Periode("11.2000", "12.2000");
    }

    @Test
    public void testChevauchementAuMois() {

        Periode unePeriode;
        Periode uneAutrePeriode;

        // test avec deux périodes JJ.MM.AAAA
        unePeriode = new Periode("01.01.2000", "01.06.2000");
        uneAutrePeriode = new Periode("31.07.2000", "31.12.2000");

        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT,
                unePeriode.comparerChevauchementMois(uneAutrePeriode));
        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT,
                uneAutrePeriode.comparerChevauchementMois(unePeriode));

        unePeriode = new Periode("01.01.2000", "31.05.2000");
        uneAutrePeriode = new Periode("01.07.2000", "31.12.2000");

        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                unePeriode.comparerChevauchementMois(uneAutrePeriode));
        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                uneAutrePeriode.comparerChevauchementMois(unePeriode));

        unePeriode = new Periode("01.01.2000", "01.06.2000");
        uneAutrePeriode = new Periode("30.06.2000", "31.12.2000");

        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                unePeriode.comparerChevauchementMois(uneAutrePeriode));
        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                uneAutrePeriode.comparerChevauchementMois(unePeriode));

        // test hybride MM.AAAA - JJ.MM.AAAA
        unePeriode = new Periode("01.2000", "06.2000");
        uneAutrePeriode = new Periode("15.07.2000", "31.12.2000");

        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT,
                unePeriode.comparerChevauchementMois(uneAutrePeriode));
        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT,
                uneAutrePeriode.comparerChevauchementMois(unePeriode));

        unePeriode = new Periode("01.2000", "05.2000");
        uneAutrePeriode = new Periode("01.07.2000", "31.12.2000");

        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                unePeriode.comparerChevauchementMois(uneAutrePeriode));
        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                uneAutrePeriode.comparerChevauchementMois(unePeriode));

        unePeriode = new Periode("01.2000", "06.2000");
        uneAutrePeriode = new Periode("30.06.2000", "31.12.2000");

        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                unePeriode.comparerChevauchementMois(uneAutrePeriode));
        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                uneAutrePeriode.comparerChevauchementMois(unePeriode));

        // test hybride JJ.MM.AAAA - MM.AAAA
        unePeriode = new Periode("20.01.2000", "07.06.2000");
        uneAutrePeriode = new Periode("07.2000", "12.2000");

        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT,
                unePeriode.comparerChevauchementMois(uneAutrePeriode));
        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT,
                uneAutrePeriode.comparerChevauchementMois(unePeriode));

        unePeriode = new Periode("01.01.2000", "31.05.2000");
        uneAutrePeriode = new Periode("07.2000", "12.2000");

        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                unePeriode.comparerChevauchementMois(uneAutrePeriode));
        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                uneAutrePeriode.comparerChevauchementMois(unePeriode));

        unePeriode = new Periode("01.01.2000", "01.06.2000");
        uneAutrePeriode = new Periode("06.2000", "12.2000");

        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                unePeriode.comparerChevauchementMois(uneAutrePeriode));
        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                uneAutrePeriode.comparerChevauchementMois(unePeriode));

        // test avec deux périodes MM.AAAA
        unePeriode = new Periode("01.2000", "06.2000");
        uneAutrePeriode = new Periode("07.2000", "12.2000");

        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT,
                unePeriode.comparerChevauchementMois(uneAutrePeriode));
        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT,
                uneAutrePeriode.comparerChevauchementMois(unePeriode));

        unePeriode = new Periode("01.2000", "05.2000");
        uneAutrePeriode = new Periode("07.2000", "12.2000");

        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                unePeriode.comparerChevauchementMois(uneAutrePeriode));
        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                uneAutrePeriode.comparerChevauchementMois(unePeriode));

        unePeriode = new Periode("01.2000", "06.2000");
        uneAutrePeriode = new Periode("06.2000", "12.2000");

        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                unePeriode.comparerChevauchementMois(uneAutrePeriode));
        Assert.assertEquals(getMessageErreur(unePeriode, uneAutrePeriode, null),
                Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                uneAutrePeriode.comparerChevauchementMois(unePeriode));
    }

    @Test
    public void testComparerChevauchement() {
        this.testComparerChevauchementAvecStrategie(null);
    }

    @Test
    public void testComparerChevauchementAvecStrategie() {
        this.testComparerChevauchementAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS);
        this.testComparerChevauchementAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS);
        this.testComparerChevauchementAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS);
        this.testComparerChevauchementAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_PERMIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS);
    }

    public void testComparerChevauchementAvecStrategie(StrategieConversionDateMoisAnnee strategie) {

        // Note : les inverses des testes précédents sont intentionnellement gardés afin de vérifier
        // si la valeur retournée ne change pas selon l'ordre d'appel

        // chevauchement des périodes couvrant l'année 1999 avec les autres
        // périodes JJ.MM.AAAA avec périodes JJ.MM.AAAA
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_1999, periodeJJMMAAAA_1999, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_1999, periodeJJMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT,
                periodeJJMMAAAA_1999, periodeJJMMAAAA_2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeJJMMAAAA_1999, periodeJJMMAAAA_Fin2000, strategie);
        // périodes MM.AAAA avec périodes MM.AAAA
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_1999, periodeMMAAAA_1999, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_1999, periodeMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(
                JadeDateUtil.getNbDayBetween(Periode.convertDateFin(periodeMMAAAA_1999.getDateFin(), strategie),
                        Periode.convertDateDebut(periodeMMAAAA_2000.getDateDebut(), strategie)) <= 1 ? Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT
                        : ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES, periodeMMAAAA_1999, periodeMMAAAA_2000,
                strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeMMAAAA_1999, periodeMMAAAA_Fin2000, strategie);
        // hybride : périodes JJ.MM.AAAA avec périodes MM.AAAA
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_1999, periodeMMAAAA_1999, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_1999, periodeMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(
                JadeDateUtil.getNbDayBetween(periodeJJMMAAAA_1999.getDateFin(),
                        Periode.convertDateDebut(periodeMMAAAA_2000.getDateDebut(), strategie)) <= 1 ? Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT
                        : ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES, periodeJJMMAAAA_1999,
                periodeMMAAAA_2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeJJMMAAAA_1999, periodeMMAAAA_Fin2000, strategie);
        // hybride : périodes MM.AAAA avec périodes JJ.MM.AAAA
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_1999, periodeJJMMAAAA_1999, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_1999, periodeJJMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(
                JadeDateUtil.getNbDayBetween(Periode.convertDateFin(periodeMMAAAA_1999.getDateFin(), strategie),
                        periodeJJMMAAAA_2000.getDateDebut()) <= 1 ? Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT
                        : ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES, periodeMMAAAA_1999,
                periodeJJMMAAAA_2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeMMAAAA_1999, periodeJJMMAAAA_Fin2000, strategie);

        // chevauchement de la période couvrant la fin de l'année 1999 avec les autres
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_1999, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeJJMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_Fin2000, strategie);
        // périodes MM.AAAA avec périodes MM.AAAA
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_Fin1999Debut2000, periodeMMAAAA_1999, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_Fin1999Debut2000, periodeMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_Fin1999Debut2000, periodeMMAAAA_2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeMMAAAA_Fin1999Debut2000, periodeMMAAAA_Fin2000, strategie);
        // hybride : périodes JJ.MM.AAAA avec périodes MM.AAAA
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_Fin1999Debut2000, periodeMMAAAA_1999, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_Fin1999Debut2000, periodeMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_Fin1999Debut2000, periodeMMAAAA_2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeJJMMAAAA_Fin1999Debut2000, periodeMMAAAA_Fin2000, strategie);
        // hybride : périodes MM.AAAA avec périodes JJ.MM.AAAA
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_1999, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_Fin2000, strategie);

        // chevauchement de la période couvrant l'année 2000 avec les autres
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT,
                periodeJJMMAAAA_2000, periodeJJMMAAAA_1999, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_2000, periodeJJMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_2000, periodeJJMMAAAA_2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_2000, periodeJJMMAAAA_Fin2000, strategie);
        // périodes MM.AAAA avec périodes MM.AAAA
        assertComparerChevauchementAvecStrategie(
                JadeDateUtil.getNbDayBetween(Periode.convertDateFin(periodeMMAAAA_1999.getDateFin(), strategie),
                        Periode.convertDateDebut(periodeMMAAAA_2000.getDateDebut(), strategie)) <= 1 ? Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT
                        : ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES, periodeMMAAAA_2000, periodeMMAAAA_1999,
                strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_2000, periodeMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_2000, periodeMMAAAA_2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_2000, periodeMMAAAA_Fin2000, strategie);
        // hybride : périodes JJ.MM.AAAA avec périodes MM.AAAA
        assertComparerChevauchementAvecStrategie(
                JadeDateUtil.getNbDayBetween(Periode.convertDateFin(periodeMMAAAA_1999.getDateFin(), strategie),
                        periodeJJMMAAAA_2000.getDateDebut()) <= 1 ? Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT
                        : ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES, periodeJJMMAAAA_2000,
                periodeMMAAAA_1999, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_2000, periodeMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_2000, periodeMMAAAA_2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_2000, periodeMMAAAA_Fin2000, strategie);
        // hybride : périodes MM.AAAA avec périodes JJ.MM.AAAA
        assertComparerChevauchementAvecStrategie(
                JadeDateUtil.getNbDayBetween(periodeJJMMAAAA_1999.getDateFin(),
                        Periode.convertDateDebut(periodeMMAAAA_2000.getDateDebut(), strategie)) <= 1 ? Periode.ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT
                        : ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES, periodeMMAAAA_2000,
                periodeJJMMAAAA_1999, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_2000, periodeJJMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_2000, periodeJJMMAAAA_2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_2000, periodeJJMMAAAA_Fin2000, strategie);

        // chevauchement de la période couvrant la fin de l'année 2000 avec les autres
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeJJMMAAAA_Fin2000, periodeJJMMAAAA_1999, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeJJMMAAAA_Fin2000, periodeJJMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_Fin2000, periodeJJMMAAAA_2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_Fin2000, periodeJJMMAAAA_Fin2000, strategie);
        // périodes MM.AAAA avec périodes MM.AAAA
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeMMAAAA_Fin2000, periodeMMAAAA_1999, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeMMAAAA_Fin2000, periodeMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_Fin2000, periodeMMAAAA_2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_Fin2000, periodeMMAAAA_Fin2000, strategie);
        // hybride : périodes JJ.MM.AAAA avec périodes MM.AAAA
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeJJMMAAAA_Fin2000, periodeMMAAAA_1999, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeJJMMAAAA_Fin2000, periodeMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_Fin2000, periodeMMAAAA_2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeJJMMAAAA_Fin2000, periodeMMAAAA_Fin2000, strategie);
        // hybride : périodes MM.AAAA avec périodes JJ.MM.AAAA
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeMMAAAA_Fin2000, periodeJJMMAAAA_1999, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES,
                periodeMMAAAA_Fin2000, periodeJJMMAAAA_Fin1999Debut2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_Fin2000, periodeJJMMAAAA_2000, strategie);
        assertComparerChevauchementAvecStrategie(Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT,
                periodeMMAAAA_Fin2000, periodeJJMMAAAA_Fin2000, strategie);
    }

    @Test
    public void testCompareTo() {
        this.testCompareToAvecStrategie(null);

        // comparaison avec une date de fin vide et une avec la valeur max
        Periode periode1 = new Periode("01.2000", "");
        Periode periode2 = new Periode("01.2000", Periode.convertDateFin(periode1.getDateFin(),
                StrategieConversionDateMoisAnnee.DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS));

        Assert.assertEquals(periode1, periode2);
    }

    @Test
    public void testCompareToAvecStrategie() {
        this.testCompareToAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS);
        this.testCompareToAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS);
        this.testCompareToAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS);
        this.testCompareToAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_PERMIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS);
    }

    private void testCompareToAvecStrategie(StrategieConversionDateMoisAnnee strategie) {
        // période couvrant l'année 1999, est en tête de liste après le tri (donc -1 sauf pour son équivalence)
        assertCompareToAvecStrategie(0, periodeMMAAAA_1999, periodeMMAAAA_1999, strategie);
        assertCompareToAvecStrategie(-1, periodeMMAAAA_1999, periodeMMAAAA_Fin1999Debut2000, strategie);
        assertCompareToAvecStrategie(-1, periodeMMAAAA_1999, periodeMMAAAA_2000, strategie);
        assertCompareToAvecStrategie(-1, periodeMMAAAA_1999, periodeMMAAAA_Fin2000, strategie);

        // période couvrant la fin de l'année 1999, 2ème de la liste après le tri
        assertCompareToAvecStrategie(1, periodeMMAAAA_Fin1999Debut2000, periodeMMAAAA_1999, strategie);
        assertCompareToAvecStrategie(0, periodeMMAAAA_Fin1999Debut2000, periodeMMAAAA_Fin1999Debut2000, strategie);
        assertCompareToAvecStrategie(-1, periodeMMAAAA_Fin1999Debut2000, periodeMMAAAA_2000, strategie);
        assertCompareToAvecStrategie(-1, periodeMMAAAA_Fin1999Debut2000, periodeMMAAAA_Fin2000, strategie);

        // période couvrant l'année 2000, 3ème de la liste après le tri
        assertCompareToAvecStrategie(1, periodeMMAAAA_2000, periodeMMAAAA_1999, strategie);
        assertCompareToAvecStrategie(1, periodeMMAAAA_2000, periodeMMAAAA_Fin1999Debut2000, strategie);
        assertCompareToAvecStrategie(0, periodeMMAAAA_2000, periodeMMAAAA_2000, strategie);
        assertCompareToAvecStrategie(-1, periodeMMAAAA_2000, periodeMMAAAA_Fin2000, strategie);

        // période couvrant la fin de l'année 2000, dernière de la liste après le tri (donc 1 sauf pour son équivalence)
        assertCompareToAvecStrategie(1, periodeMMAAAA_Fin2000, periodeMMAAAA_1999, strategie);
        assertCompareToAvecStrategie(1, periodeMMAAAA_Fin2000, periodeMMAAAA_Fin1999Debut2000, strategie);
        assertCompareToAvecStrategie(1, periodeMMAAAA_Fin2000, periodeMMAAAA_2000, strategie);
        assertCompareToAvecStrategie(0, periodeMMAAAA_Fin2000, periodeMMAAAA_Fin2000, strategie);

        // ajout dans le désordre dans un conteneur implémentant le tri naturel
        SortedSet<Periode> periodes = new TreeSet<Periode>();
        periodes.add(periodeMMAAAA_Fin2000);
        periodes.add(periodeMMAAAA_Fin1999Debut2000);
        periodes.add(periodeMMAAAA_2000);
        periodes.add(periodeMMAAAA_1999);

        // vérification du tri
        Periode[] toArray = periodes.toArray(new Periode[4]);
        Assert.assertEquals(periodeMMAAAA_1999, toArray[0]);
        Assert.assertEquals(periodeMMAAAA_Fin1999Debut2000, toArray[1]);
        Assert.assertEquals(periodeMMAAAA_2000, toArray[2]);
        Assert.assertEquals(periodeMMAAAA_Fin2000, toArray[3]);
    }

    @Test
    public void testDifference() {
        this.testDifferenceAvecStrategie(null);
    }

    @Test
    public void testDifferenceAvecStrategie() {
        this.testDifferenceAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS);
        this.testDifferenceAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS);
        this.testDifferenceAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS);
        this.testDifferenceAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_PERMIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS);
    }

    private void testDifferenceAvecStrategie(StrategieConversionDateMoisAnnee strategie) {
        SortedSet<Periode> resultatsAttendusDifference = new TreeSet<Periode>();
        SortedSet<Periode> resultatsAttendusDifferenceSymetrique = new TreeSet<Periode>();

        // test entre période couvrant 1999 et une couvrant le milieu de l'année 1999
        Periode periodeJJMMAAAA_milieu1999 = new Periode("01.03.1999", "30.09.1999");
        Periode periodeMMAAAA_milieu1999 = new Periode("03.1999", "09.1999");

        // deux dates JJ.MM.AAAA
        resultatsAttendusDifference.add(new Periode(periodeJJMMAAAA_1999.getDateDebut(), periodeJJMMAAAA_milieu1999
                .getDateDebut()));
        resultatsAttendusDifference.add(new Periode(periodeJJMMAAAA_milieu1999.getDateFin(), periodeJJMMAAAA_1999
                .getDateFin()));
        resultatsAttendusDifferenceSymetrique.addAll(resultatsAttendusDifference);

        this.assertDifference(resultatsAttendusDifference, periodeJJMMAAAA_1999, periodeJJMMAAAA_milieu1999, strategie);
        this.assertDifference(new TreeSet<Periode>(), periodeJJMMAAAA_milieu1999, periodeJJMMAAAA_1999, strategie);

        assertDifferenceSymetrique(resultatsAttendusDifferenceSymetrique, periodeJJMMAAAA_1999,
                periodeJJMMAAAA_milieu1999, strategie);
        assertDifferenceSymetrique(resultatsAttendusDifferenceSymetrique, periodeJJMMAAAA_milieu1999,
                periodeJJMMAAAA_1999, strategie);

        resultatsAttendusDifference.clear();
        resultatsAttendusDifferenceSymetrique.clear();

        // comparaison hybride : JJ.MM.AAAA - MM.AAAA
        resultatsAttendusDifference.add(new Periode(periodeJJMMAAAA_1999.getDateDebut(), Periode.convertDateDebut(
                periodeMMAAAA_milieu1999.getDateDebut(), strategie)));
        resultatsAttendusDifference.add(new Periode(Periode.convertDateFin(periodeMMAAAA_milieu1999.getDateFin(),
                strategie), periodeJJMMAAAA_1999.getDateFin()));
        resultatsAttendusDifferenceSymetrique.addAll(resultatsAttendusDifference);

        this.assertDifference(resultatsAttendusDifference, periodeJJMMAAAA_1999, periodeMMAAAA_milieu1999, strategie);
        this.assertDifference(new TreeSet<Periode>(), periodeMMAAAA_milieu1999, periodeJJMMAAAA_1999, strategie);

        assertDifferenceSymetrique(resultatsAttendusDifferenceSymetrique, periodeJJMMAAAA_1999,
                periodeMMAAAA_milieu1999, strategie);
        assertDifferenceSymetrique(resultatsAttendusDifferenceSymetrique, periodeMMAAAA_milieu1999,
                periodeJJMMAAAA_1999, strategie);

        resultatsAttendusDifference.clear();
        resultatsAttendusDifferenceSymetrique.clear();

        // hybride : MM.AAAA - JJ.MM.AAAA
        resultatsAttendusDifference.add(new Periode(Periode.convertDateDebut(periodeMMAAAA_1999.getDateDebut(),
                strategie), periodeJJMMAAAA_milieu1999.getDateDebut()));
        resultatsAttendusDifference.add(new Periode(periodeJJMMAAAA_milieu1999.getDateFin(), Periode.convertDateFin(
                periodeMMAAAA_1999.getDateFin(), strategie)));
        resultatsAttendusDifferenceSymetrique.addAll(resultatsAttendusDifference);

        this.assertDifference(resultatsAttendusDifference, periodeMMAAAA_1999, periodeJJMMAAAA_milieu1999, strategie);
        this.assertDifference(new TreeSet<Periode>(), periodeJJMMAAAA_milieu1999, periodeMMAAAA_1999, strategie);

        assertDifferenceSymetrique(resultatsAttendusDifferenceSymetrique, periodeMMAAAA_1999,
                periodeJJMMAAAA_milieu1999, strategie);
        assertDifferenceSymetrique(resultatsAttendusDifferenceSymetrique, periodeJJMMAAAA_milieu1999,
                periodeMMAAAA_1999, strategie);

        resultatsAttendusDifference.clear();
        resultatsAttendusDifferenceSymetrique.clear();

        // deux dates MM.AAAA
        resultatsAttendusDifference.add(new Periode(Periode.convertDateDebut(periodeMMAAAA_1999.getDateDebut(),
                strategie), Periode.convertDateDebut(periodeMMAAAA_milieu1999.getDateDebut(), strategie)));
        resultatsAttendusDifference.add(new Periode(Periode.convertDateFin(periodeMMAAAA_milieu1999.getDateFin(),
                strategie), Periode.convertDateFin(periodeMMAAAA_1999.getDateFin(), strategie)));
        this.assertDifference(resultatsAttendusDifference, periodeMMAAAA_1999, periodeMMAAAA_milieu1999, strategie);
        resultatsAttendusDifference.clear();
        this.assertDifference(resultatsAttendusDifference, periodeMMAAAA_milieu1999, periodeJJMMAAAA_1999, strategie);

        resultatsAttendusDifference.clear();
        resultatsAttendusDifferenceSymetrique.clear();

        // test entre une période couvrant 1999 et une couvrant la moitié de 1999 jusqu'à mi-2000
        Periode periodeJJMMAAAA_milieu1999milieu2000 = new Periode("01.07.1999", "01.07.2000");
        Periode periodeMMAAAA_milieu1999milieu2000 = new Periode("07.1999", "07.2000");

        // deux dates JJ.MM.AAAA
        resultatsAttendusDifference.add(new Periode(periodeJJMMAAAA_1999.getDateDebut(),
                periodeJJMMAAAA_milieu1999milieu2000.getDateDebut()));
        resultatsAttendusDifferenceSymetrique.addAll(resultatsAttendusDifference);

        this.assertDifference(resultatsAttendusDifference, periodeJJMMAAAA_1999, periodeJJMMAAAA_milieu1999milieu2000,
                strategie);

        resultatsAttendusDifference.clear();
        resultatsAttendusDifference.add(new Periode(periodeJJMMAAAA_1999.getDateFin(),
                periodeJJMMAAAA_milieu1999milieu2000.getDateFin()));
        resultatsAttendusDifferenceSymetrique.addAll(resultatsAttendusDifference);

        this.assertDifference(resultatsAttendusDifference, periodeJJMMAAAA_milieu1999milieu2000, periodeJJMMAAAA_1999,
                strategie);

        assertDifferenceSymetrique(resultatsAttendusDifferenceSymetrique, periodeJJMMAAAA_1999,
                periodeJJMMAAAA_milieu1999milieu2000, strategie);
        assertDifferenceSymetrique(resultatsAttendusDifferenceSymetrique, periodeJJMMAAAA_milieu1999milieu2000,
                periodeJJMMAAAA_1999, strategie);

        resultatsAttendusDifference.clear();
        resultatsAttendusDifferenceSymetrique.clear();

        // comparaison hybride 1999:JJ.MM.AAAA - milieu1999milieu2000:MM.AAAA
        resultatsAttendusDifference.add(new Periode(periodeJJMMAAAA_1999.getDateDebut(), Periode.convertDateDebut(
                periodeMMAAAA_milieu1999milieu2000.getDateDebut(), strategie)));
        resultatsAttendusDifferenceSymetrique.addAll(resultatsAttendusDifference);

        this.assertDifference(resultatsAttendusDifference, periodeJJMMAAAA_1999, periodeMMAAAA_milieu1999milieu2000,
                strategie);

        resultatsAttendusDifference.clear();

        resultatsAttendusDifference.add(new Periode(periodeJJMMAAAA_1999.getDateFin(), Periode.convertDateFin(
                periodeMMAAAA_milieu1999milieu2000.getDateFin(), strategie)));
        resultatsAttendusDifferenceSymetrique.addAll(resultatsAttendusDifference);

        this.assertDifference(resultatsAttendusDifference, periodeMMAAAA_milieu1999milieu2000, periodeJJMMAAAA_1999,
                strategie);

        assertDifferenceSymetrique(resultatsAttendusDifferenceSymetrique, periodeJJMMAAAA_1999,
                periodeMMAAAA_milieu1999milieu2000, strategie);
        assertDifferenceSymetrique(resultatsAttendusDifferenceSymetrique, periodeMMAAAA_milieu1999milieu2000,
                periodeJJMMAAAA_1999, strategie);

        resultatsAttendusDifference.clear();
        resultatsAttendusDifferenceSymetrique.clear();

        // comparaison hybride 1999:MM.AAAA - milieu1999milieu2000:JJ.MM.AAAA
        resultatsAttendusDifference.add(new Periode(Periode.convertDateDebut(periodeMMAAAA_1999.getDateDebut(),
                strategie), periodeJJMMAAAA_milieu1999milieu2000.getDateDebut()));
        resultatsAttendusDifferenceSymetrique.addAll(resultatsAttendusDifference);

        this.assertDifference(resultatsAttendusDifference, periodeMMAAAA_1999, periodeJJMMAAAA_milieu1999milieu2000,
                strategie);

        resultatsAttendusDifference.clear();

        resultatsAttendusDifference.add(new Periode(Periode.convertDateFin(periodeMMAAAA_1999.getDateFin(), strategie),
                periodeJJMMAAAA_milieu1999milieu2000.getDateFin()));
        resultatsAttendusDifferenceSymetrique.addAll(resultatsAttendusDifference);

        this.assertDifference(resultatsAttendusDifference, periodeJJMMAAAA_milieu1999milieu2000, periodeMMAAAA_1999,
                strategie);

        assertDifferenceSymetrique(resultatsAttendusDifferenceSymetrique, periodeMMAAAA_1999,
                periodeJJMMAAAA_milieu1999milieu2000, strategie);
        assertDifferenceSymetrique(resultatsAttendusDifferenceSymetrique, periodeJJMMAAAA_milieu1999milieu2000,
                periodeMMAAAA_1999, strategie);

        resultatsAttendusDifference.clear();
        resultatsAttendusDifferenceSymetrique.clear();

        // dates MM.AAAA
        resultatsAttendusDifference.add(new Periode(Periode.convertDateDebut(periodeMMAAAA_1999.getDateDebut(),
                strategie), Periode.convertDateDebut(periodeMMAAAA_milieu1999milieu2000.getDateDebut(), strategie)));
        resultatsAttendusDifferenceSymetrique.addAll(resultatsAttendusDifference);

        this.assertDifference(resultatsAttendusDifference, periodeMMAAAA_1999, periodeMMAAAA_milieu1999milieu2000,
                strategie);

        resultatsAttendusDifference.clear();

        resultatsAttendusDifference.add(new Periode(Periode.convertDateFin(periodeMMAAAA_1999.getDateFin(), strategie),
                Periode.convertDateFin(periodeMMAAAA_milieu1999milieu2000.getDateFin(), strategie)));
        resultatsAttendusDifferenceSymetrique.addAll(resultatsAttendusDifference);

        this.assertDifference(resultatsAttendusDifference, periodeMMAAAA_milieu1999milieu2000, periodeMMAAAA_1999,
                strategie);

        assertDifferenceSymetrique(resultatsAttendusDifferenceSymetrique, periodeMMAAAA_1999,
                periodeMMAAAA_milieu1999milieu2000, strategie);
        assertDifferenceSymetrique(resultatsAttendusDifferenceSymetrique, periodeMMAAAA_milieu1999milieu2000,
                periodeMMAAAA_1999, strategie);

        resultatsAttendusDifference.clear();
        resultatsAttendusDifferenceSymetrique.clear();

    }

    @Test
    public void testIntersection() {
        this.testIntersectionAvecStrategie(null);
    }

    @Test
    public void testIntersectionAvecStrategie() {
        this.testIntersectionAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS);
        this.testIntersectionAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS);
        this.testIntersectionAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS);
        this.testIntersectionAvecStrategie(StrategieConversionDateMoisAnnee.DATE_DEBUT_PERMIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS);
    }

    public void testIntersectionAvecStrategie(StrategieConversionDateMoisAnnee strategie) {

        Periode expectedResult;

        // JJ.MM.AAAA couvrant 1999
        // test avec les dates JJ.MM.AAAA
        expectedResult = new Periode(periodeJJMMAAAA_1999.getDateDebut(), periodeJJMMAAAA_1999.getDateFin());
        assertIntersection(expectedResult, periodeJJMMAAAA_1999, periodeJJMMAAAA_1999, strategie);
        assertIntersection(null, periodeJJMMAAAA_1999, periodeJJMMAAAA_2000, strategie);
        expectedResult = new Periode(periodeJJMMAAAA_Fin1999Debut2000.getDateDebut(), periodeJJMMAAAA_1999.getDateFin());
        assertIntersection(expectedResult, periodeJJMMAAAA_1999, periodeJJMMAAAA_Fin1999Debut2000, strategie);
        assertIntersection(null, periodeJJMMAAAA_1999, periodeJJMMAAAA_Fin2000, strategie);
        // test avec les date MM.AAAA
        expectedResult = new Periode(Periode.convertDateDebut(periodeMMAAAA_1999.getDateDebut(), strategie),
                Periode.convertDateFin(periodeMMAAAA_1999.getDateFin(), strategie));
        assertIntersection(expectedResult, periodeJJMMAAAA_1999, periodeMMAAAA_1999, strategie);
        assertIntersection(null, periodeJJMMAAAA_1999, periodeMMAAAA_2000, strategie);
        expectedResult = new Periode(
                Periode.convertDateDebut(periodeMMAAAA_Fin1999Debut2000.getDateDebut(), strategie),
                periodeJJMMAAAA_1999.getDateFin());
        assertIntersection(expectedResult, periodeJJMMAAAA_1999, periodeMMAAAA_Fin1999Debut2000, strategie);
        assertIntersection(null, periodeJJMMAAAA_1999, periodeMMAAAA_Fin2000, strategie);

        // JJ.MM.AAAA couvrant 2000
        // test avec les dates JJ.MM.AAAA
        assertIntersection(null, periodeJJMMAAAA_2000, periodeJJMMAAAA_1999, strategie);
        expectedResult = new Periode(periodeJJMMAAAA_2000.getDateDebut(), periodeJJMMAAAA_2000.getDateFin());
        assertIntersection(expectedResult, periodeJJMMAAAA_2000, periodeJJMMAAAA_2000, strategie);
        expectedResult = new Periode(periodeJJMMAAAA_2000.getDateDebut(), periodeJJMMAAAA_Fin1999Debut2000.getDateFin());
        assertIntersection(expectedResult, periodeJJMMAAAA_2000, periodeJJMMAAAA_Fin1999Debut2000, strategie);
        expectedResult = new Periode(periodeJJMMAAAA_Fin2000.getDateDebut(), periodeJJMMAAAA_Fin2000.getDateFin());
        assertIntersection(expectedResult, periodeJJMMAAAA_2000, periodeJJMMAAAA_Fin2000, strategie);
        // test avec les dates MM.AAAA
        assertIntersection(null, periodeJJMMAAAA_2000, periodeMMAAAA_1999, strategie);
        expectedResult = new Periode(Periode.convertDateDebut(periodeMMAAAA_2000.getDateDebut(), strategie),
                Periode.convertDateFin(periodeMMAAAA_2000.getDateFin(), strategie));
        assertIntersection(expectedResult, periodeJJMMAAAA_2000, periodeMMAAAA_2000, strategie);
        expectedResult = new Periode(periodeJJMMAAAA_2000.getDateDebut(), Periode.convertDateFin(
                periodeMMAAAA_Fin1999Debut2000.getDateFin(), strategie));
        assertIntersection(expectedResult, periodeJJMMAAAA_2000, periodeMMAAAA_Fin1999Debut2000, strategie);
        expectedResult = new Periode(Periode.convertDateDebut(periodeMMAAAA_Fin2000.getDateDebut(), strategie),
                Periode.convertDateFin(periodeMMAAAA_Fin2000.getDateFin(), strategie));
        assertIntersection(expectedResult, periodeJJMMAAAA_2000, periodeMMAAAA_Fin2000, strategie);

        // JJ.MM.AAAA couvrant fin 1999 début 2000
        // test avec les dates JJ.MM.AAAA
        expectedResult = new Periode(periodeJJMMAAAA_Fin1999Debut2000.getDateDebut(), periodeJJMMAAAA_1999.getDateFin());
        assertIntersection(expectedResult, periodeJJMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_1999, strategie);
        expectedResult = new Periode(periodeJJMMAAAA_2000.getDateDebut(), periodeJJMMAAAA_Fin1999Debut2000.getDateFin());
        assertIntersection(expectedResult, periodeJJMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_2000, strategie);
        expectedResult = new Periode(periodeJJMMAAAA_Fin1999Debut2000.getDateDebut(),
                periodeJJMMAAAA_Fin1999Debut2000.getDateFin());
        assertIntersection(expectedResult, periodeJJMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_Fin1999Debut2000,
                strategie);
        assertIntersection(null, periodeJJMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_Fin2000, strategie);
        // test avec les dates MM.AAAA
        expectedResult = new Periode(periodeJJMMAAAA_Fin1999Debut2000.getDateDebut(), Periode.convertDateFin(
                periodeMMAAAA_1999.getDateFin(), strategie));
        assertIntersection(expectedResult, periodeJJMMAAAA_Fin1999Debut2000, periodeMMAAAA_1999, strategie);
        expectedResult = new Periode(Periode.convertDateDebut(periodeMMAAAA_2000.getDateDebut(), strategie),
                periodeJJMMAAAA_Fin1999Debut2000.getDateFin());
        assertIntersection(expectedResult, periodeJJMMAAAA_Fin1999Debut2000, periodeMMAAAA_2000, strategie);
        expectedResult = new Periode(periodeJJMMAAAA_Fin1999Debut2000.getDateDebut(),
                periodeJJMMAAAA_Fin1999Debut2000.getDateFin());
        assertIntersection(expectedResult, periodeJJMMAAAA_Fin1999Debut2000, periodeMMAAAA_Fin1999Debut2000, strategie);
        assertIntersection(null, periodeJJMMAAAA_Fin1999Debut2000, periodeMMAAAA_Fin2000, strategie);

        // JJ.MM.AAAA couvrant fin 2000
        // test avec les dates JJ.MM.AAAA
        assertIntersection(null, periodeJJMMAAAA_Fin2000, periodeMMAAAA_1999, strategie);
        expectedResult = new Periode(periodeJJMMAAAA_Fin2000.getDateDebut(), periodeJJMMAAAA_Fin2000.getDateFin());
        assertIntersection(expectedResult, periodeJJMMAAAA_Fin2000, periodeJJMMAAAA_2000, strategie);
        assertIntersection(null, periodeJJMMAAAA_Fin2000, periodeJJMMAAAA_Fin1999Debut2000, strategie);
        expectedResult = new Periode(periodeJJMMAAAA_Fin2000.getDateDebut(), periodeJJMMAAAA_Fin2000.getDateFin());
        assertIntersection(expectedResult, periodeJJMMAAAA_Fin2000, periodeJJMMAAAA_Fin2000, strategie);
        // test avec les date MM.AAAA
        assertIntersection(null, periodeJJMMAAAA_Fin2000, periodeMMAAAA_1999, strategie);
        expectedResult = new Periode(periodeJJMMAAAA_Fin2000.getDateDebut(), Periode.convertDateFin(
                periodeMMAAAA_2000.getDateFin(), strategie));
        assertIntersection(expectedResult, periodeJJMMAAAA_Fin2000, periodeMMAAAA_2000, strategie);
        assertIntersection(null, periodeJJMMAAAA_Fin2000, periodeMMAAAA_Fin1999Debut2000, strategie);
        expectedResult = new Periode(Periode.convertDateDebut(periodeMMAAAA_Fin2000.getDateDebut(), strategie),
                Periode.convertDateFin(periodeMMAAAA_Fin2000.getDateFin(), strategie));
        assertIntersection(expectedResult, periodeJJMMAAAA_Fin2000, periodeMMAAAA_Fin2000, strategie);
    }

    @Test
    public void testIntersectionMois() {

        Periode expectedResult;

        // MM.AAAA couvrant 1999
        // test avec les dates MM.AAAA
        expectedResult = new Periode(periodeMMAAAA_1999.getDateDebut(), periodeMMAAAA_1999.getDateFin());
        assertIntersectionMois(expectedResult, periodeMMAAAA_1999, periodeMMAAAA_1999);
        assertIntersectionMois(null, periodeJJMMAAAA_1999, periodeJJMMAAAA_2000);
        expectedResult = new Periode(
                JadeDateUtil.convertDateMonthYear(periodeJJMMAAAA_Fin1999Debut2000.getDateDebut()),
                periodeMMAAAA_1999.getDateFin());
        assertIntersectionMois(expectedResult, periodeJJMMAAAA_1999, periodeJJMMAAAA_Fin1999Debut2000);
        assertIntersectionMois(null, periodeJJMMAAAA_1999, periodeJJMMAAAA_Fin2000);
        // test avec les date MM.AAAA
        expectedResult = new Periode(periodeMMAAAA_1999.getDateDebut(), periodeMMAAAA_1999.getDateFin());
        assertIntersectionMois(expectedResult, periodeJJMMAAAA_1999, periodeMMAAAA_1999);
        assertIntersectionMois(null, periodeJJMMAAAA_1999, periodeMMAAAA_2000);
        expectedResult = new Periode(periodeMMAAAA_Fin1999Debut2000.getDateDebut(), periodeMMAAAA_1999.getDateFin());
        assertIntersectionMois(expectedResult, periodeJJMMAAAA_1999, periodeMMAAAA_Fin1999Debut2000);
        assertIntersectionMois(null, periodeJJMMAAAA_1999, periodeMMAAAA_Fin2000);

        // JJ.MM.AAAA couvrant 2000
        // test avec les dates JJ.MM.AAAA
        assertIntersectionMois(null, periodeJJMMAAAA_2000, periodeJJMMAAAA_1999);
        expectedResult = new Periode(periodeMMAAAA_2000.getDateDebut(), periodeMMAAAA_2000.getDateFin());
        assertIntersectionMois(expectedResult, periodeJJMMAAAA_2000, periodeJJMMAAAA_2000);
        expectedResult = new Periode(periodeMMAAAA_2000.getDateDebut(),
                JadeDateUtil.convertDateMonthYear(periodeJJMMAAAA_Fin1999Debut2000.getDateFin()));
        assertIntersectionMois(expectedResult, periodeJJMMAAAA_2000, periodeJJMMAAAA_Fin1999Debut2000);
        expectedResult = new Periode(periodeMMAAAA_Fin2000.getDateDebut(), periodeMMAAAA_Fin2000.getDateFin());
        assertIntersectionMois(expectedResult, periodeJJMMAAAA_2000, periodeJJMMAAAA_Fin2000);
        // test avec les dates MM.AAAA
        assertIntersectionMois(null, periodeMMAAAA_2000, periodeMMAAAA_1999);
        expectedResult = new Periode(periodeMMAAAA_2000.getDateDebut(), periodeMMAAAA_2000.getDateFin());
        assertIntersectionMois(expectedResult, periodeJJMMAAAA_2000, periodeMMAAAA_2000);
        expectedResult = new Periode(periodeMMAAAA_2000.getDateDebut(), periodeMMAAAA_Fin1999Debut2000.getDateFin());
        assertIntersectionMois(expectedResult, periodeMMAAAA_2000, periodeMMAAAA_Fin1999Debut2000);
        expectedResult = new Periode(periodeMMAAAA_Fin2000.getDateDebut(), periodeMMAAAA_Fin2000.getDateFin());
        assertIntersectionMois(expectedResult, periodeMMAAAA_2000, periodeMMAAAA_Fin2000);

        // JJ.MM.AAAA couvrant fin 1999 début 2000
        // test avec les dates JJ.MM.AAAA
        expectedResult = new Periode(
                JadeDateUtil.convertDateMonthYear(periodeJJMMAAAA_Fin1999Debut2000.getDateDebut()),
                periodeMMAAAA_1999.getDateFin());
        assertIntersectionMois(expectedResult, periodeJJMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_1999);
        expectedResult = new Periode(periodeMMAAAA_2000.getDateDebut(),
                JadeDateUtil.convertDateMonthYear(periodeJJMMAAAA_Fin1999Debut2000.getDateFin()));
        assertIntersectionMois(expectedResult, periodeJJMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_2000);
        expectedResult = new Periode(
                JadeDateUtil.convertDateMonthYear(periodeJJMMAAAA_Fin1999Debut2000.getDateDebut()),
                JadeDateUtil.convertDateMonthYear(periodeJJMMAAAA_Fin1999Debut2000.getDateFin()));
        assertIntersectionMois(expectedResult, periodeJJMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_Fin1999Debut2000);
        assertIntersectionMois(null, periodeJJMMAAAA_Fin1999Debut2000, periodeJJMMAAAA_Fin2000);
        // test avec les dates MM.AAAA
        expectedResult = new Periode(periodeMMAAAA_Fin1999Debut2000.getDateDebut(), periodeMMAAAA_1999.getDateFin());
        assertIntersectionMois(expectedResult, periodeMMAAAA_Fin1999Debut2000, periodeMMAAAA_1999);
        expectedResult = new Periode(periodeMMAAAA_2000.getDateDebut(), periodeMMAAAA_Fin1999Debut2000.getDateFin());
        assertIntersectionMois(expectedResult, periodeMMAAAA_Fin1999Debut2000, periodeMMAAAA_2000);
        expectedResult = new Periode(periodeMMAAAA_Fin1999Debut2000.getDateDebut(),
                periodeMMAAAA_Fin1999Debut2000.getDateFin());
        assertIntersectionMois(expectedResult, periodeMMAAAA_Fin1999Debut2000, periodeMMAAAA_Fin1999Debut2000);
        assertIntersectionMois(null, periodeMMAAAA_Fin1999Debut2000, periodeMMAAAA_Fin2000);

        // JJ.MM.AAAA couvrant fin 2000
        // test avec les dates JJ.MM.AAAA
        assertIntersectionMois(null, periodeJJMMAAAA_Fin2000, periodeMMAAAA_1999);
        expectedResult = new Periode(periodeMMAAAA_Fin2000.getDateDebut(), periodeMMAAAA_Fin2000.getDateFin());
        assertIntersectionMois(expectedResult, periodeJJMMAAAA_Fin2000, periodeJJMMAAAA_2000);
        assertIntersectionMois(null, periodeJJMMAAAA_Fin2000, periodeJJMMAAAA_Fin1999Debut2000);
        expectedResult = new Periode(periodeMMAAAA_Fin2000.getDateDebut(), periodeMMAAAA_Fin2000.getDateFin());
        assertIntersectionMois(expectedResult, periodeJJMMAAAA_Fin2000, periodeJJMMAAAA_Fin2000);
        // test avec les date MM.AAAA
        assertIntersectionMois(null, periodeJJMMAAAA_Fin2000, periodeMMAAAA_1999);
        expectedResult = new Periode(periodeMMAAAA_Fin2000.getDateDebut(), periodeMMAAAA_2000.getDateFin());
        assertIntersectionMois(expectedResult, periodeJJMMAAAA_Fin2000, periodeMMAAAA_2000);
        assertIntersectionMois(null, periodeJJMMAAAA_Fin2000, periodeMMAAAA_Fin1999Debut2000);
        expectedResult = new Periode(periodeMMAAAA_Fin2000.getDateDebut(), periodeMMAAAA_Fin2000.getDateFin());
        assertIntersectionMois(expectedResult, periodeJJMMAAAA_Fin2000, periodeMMAAAA_Fin2000);

    }

    @Test
    public void testIsDateDansPeriode() {
        Periode periode = new Periode("01.01.2000", "31.12.2000");

        Assert.assertFalse("La date du 31.12.1999 n'est pas dans la période couvrant l'année 2000",
                periode.isDateDansLaPeriode("31.12.1999"));
        Assert.assertTrue("La date du 01.01.2000 est dans la période couvrant l'année 2000",
                periode.isDateDansLaPeriode("01.01.2000"));
        Assert.assertTrue("La date du 02.01.2000 est dans la période couvrant l'année 2000",
                periode.isDateDansLaPeriode("02.01.2000"));

        Assert.assertTrue("La date du 15.06.2000 est dans la période couvrant l'année 2000",
                periode.isDateDansLaPeriode("15.06.2000"));

        Assert.assertTrue("La date du 30.12.2000 est dans la période couvrant l'année 2000",
                periode.isDateDansLaPeriode("30.12.2000"));
        Assert.assertTrue("La date du 31.12.2000 est dans la période couvrant l'année 2000",
                periode.isDateDansLaPeriode("31.12.2000"));
        Assert.assertFalse("La date du 01.01.2001 n'est pas dans la période couvrant l'année 2000",
                periode.isDateDansLaPeriode("01.01.2001"));

        Assert.assertFalse("La date passée en paramètre est invalide et ne doit pas être prise en compte",
                periode.isDateDansLaPeriode("1.1.2000"));
    }

    @Test
    public void testTypePeriode() {
        Assert.assertEquals(periodeJJMMAAAA_1999.getType(), TypePeriode.JOUR_MOIS_ANNEE);
        Assert.assertEquals(periodeJJMMAAAA_2000.getType(), TypePeriode.JOUR_MOIS_ANNEE);
        Assert.assertEquals(periodeJJMMAAAA_Fin1999Debut2000.getType(), TypePeriode.JOUR_MOIS_ANNEE);
        Assert.assertEquals(periodeJJMMAAAA_Fin2000.getType(), TypePeriode.JOUR_MOIS_ANNEE);

        Assert.assertEquals(periodeMMAAAA_1999.getType(), TypePeriode.MOIS_ANNEE);
        Assert.assertEquals(periodeMMAAAA_2000.getType(), TypePeriode.MOIS_ANNEE);
        Assert.assertEquals(periodeMMAAAA_Fin1999Debut2000.getType(), TypePeriode.MOIS_ANNEE);
        Assert.assertEquals(periodeMMAAAA_Fin2000.getType(), TypePeriode.MOIS_ANNEE);
    }

    @Test
    public void testUnion() {
        this.testUnion(null);
    }

    private void testUnion(StrategieConversionDateMoisAnnee strategie) {
        Periode expectedResult = new Periode("01.01.1999", "31.12.2000");
        Periode periode1 = new Periode("01.01.1999", "31.12.1999");
        Periode periode2 = new Periode("01.01.2000", "31.12.2000");

        assertUnion(expectedResult, periode1, periode2, strategie);
        assertUnion(expectedResult, periode2, periode1, strategie);

        expectedResult = new Periode("01.06.1999", "31.06.2000");
        periode1 = new Periode("01.06.1999", "01.03.2000");
        periode2 = new Periode("01.09.1999", "31.06.2000");

        assertUnion(expectedResult, periode1, periode2, strategie);
        assertUnion(expectedResult, periode2, periode1, strategie);

        expectedResult = null;
        periode1 = new Periode("01.01.1999", "30.12.1999");
        periode2 = new Periode("01.01.2000", "31.12.2000");

        assertUnion(expectedResult, periode1, periode2, strategie);
        assertUnion(expectedResult, periode2, periode1, strategie);
    }

    @Test
    public void testUnionAvecStrategie() {
        this.testUnion(StrategieConversionDateMoisAnnee.DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS);
        this.testUnion(StrategieConversionDateMoisAnnee.DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS);
        this.testUnion(StrategieConversionDateMoisAnnee.DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS);
        this.testUnion(StrategieConversionDateMoisAnnee.DATE_DEBUT_PERMIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS);
    }

    @Test
    public void testUnionMois() throws Exception {
        Periode expectedResult = new Periode("01.1999", "12.2000");
        Periode periode1 = new Periode("01.1999", "12.1999");
        Periode periode2 = new Periode("01.2000", "12.2000");

        assertUnionMois(expectedResult, periode1, periode2);
        assertUnionMois(expectedResult, periode2, periode1);

        expectedResult = new Periode("06.1999", "06.2000");
        periode1 = new Periode("06.1999", "03.2000");
        periode2 = new Periode("09.1999", "06.2000");

        assertUnionMois(expectedResult, periode1, periode2);
        assertUnionMois(expectedResult, periode2, periode1);

        expectedResult = null;
        periode1 = new Periode("01.1999", "12.1999");
        periode2 = new Periode("02.2000", "12.2000");

        assertUnionMois(expectedResult, periode1, periode2);
        assertUnionMois(expectedResult, periode2, periode1);

        expectedResult = new Periode("01.1999", "");
        periode1 = new Periode("01.1999", "12.1999");
        periode2 = new Periode("01.2000", "");

        assertUnionMois(expectedResult, periode1, periode2);
        assertUnionMois(expectedResult, periode2, periode1);
    }

    @Test
    public void testReslovePeriodeByWeek() throws Exception {

        /*
         * for (int i = 0; i < 53; i++) {
         * Periode periode = Periode.reslovePeriodeByWeek("2015", String.valueOf(i));
         * System.out.println(periode.getDateDebut() + " - " + periode.getDateFin() + ", " + i);
         * 
         * Periode periode2 = Periode.reslovePeriodeByWeek2("2015", String.valueOf(i));
         * System.out.println(periode2.getDateDebut() + " - " + periode2.getDateFin() + ", " + i);
         * }
         */

        Periode periodeNoWeek1 = new Periode("29.12.2014", "04.01.2015");
        Assert.assertEquals(periodeNoWeek1, Periode.resolvePeriodeByWeek(2015, 1));

        Periode periodeNoWeek2 = new Periode("05.01.2015", "11.01.2015");
        Assert.assertEquals(periodeNoWeek2, Periode.resolvePeriodeByWeek("2015", "2"));

        Periode periodeNoWeek52 = new Periode("21.12.2015", "27.12.2015");
        Periode periodeNoWeeResolved52 = Periode.resolvePeriodeByWeek(2015, 52);
        Assert.assertEquals(periodeNoWeek52, periodeNoWeeResolved52);

        Assert.assertEquals(52, resolveWeek(periodeNoWeeResolved52.getDateDebut()));
        Assert.assertEquals(52, resolveWeek(periodeNoWeeResolved52.getDateFin()));

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int curentWeeek = cal.get(Calendar.WEEK_OF_YEAR);

        Periode periodeNoWeek0 = Periode.resolvePeriodeByWeek(0, 0);
        Assert.assertEquals(curentWeeek, resolveWeek(periodeNoWeek0.getDateDebut()));
        Assert.assertEquals(curentWeeek, resolveWeek(periodeNoWeek0.getDateFin()));

        periodeNoWeek0 = Periode.resolvePeriodeByWeek(0, 6);
        Assert.assertEquals(6, resolveWeek(periodeNoWeek0.getDateDebut()));
        Assert.assertEquals(6, resolveWeek(periodeNoWeek0.getDateFin()));

        try {
            Periode.resolvePeriodeByWeek("2015", "53");
            Assert.fail("Valeur sup a 52");
        } catch (Exception e) {
            Assert.assertTrue("Valeur sup a 52", true);
        }
    }

    private int resolveWeek(String sdate) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Date date = df.parse(sdate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        return week;
    }

    @Test
    public void testResolvePeriodeByYear() throws Exception {
        Periode periode = new Periode("01.2014", "12.2014");
        assertEquals(periode, Periode.resolvePeriodeByYear("2014"));
    }

    @Test
    public void testResolvePeriodeByYearInteger() throws Exception {
        Periode periode = new Periode("01.2014", "12.2014");
        assertEquals(periode, Periode.resolvePeriodeByYear(2014));
    }
}
