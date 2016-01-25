package globaz.prestation.utils;

import globaz.prestation.beans.PRPeriode;
import globaz.prestation.utils.PRDateUtils.PRDateEquality;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import org.junit.Assert;
import org.junit.Test;

public class PRDateUtilsTest {

    private class PeriodeTest extends PRPeriode {
        private int nombreDeJours;

        public PeriodeTest(String datedeDebut, String dateDeFin, int nombreDeJours) {
            super(datedeDebut, dateDeFin);
            this.nombreDeJours = nombreDeJours;
        }

        public final int getNombreDeJours() {
            return nombreDeJours;
        }

        public final void setNombreDeJours(int nombreDeJours) {
            this.nombreDeJours = nombreDeJours;
        }

    }

    @Test
    public void getAge() {
        String dateDeNaissance = "10.02.2000";

        try {
            Assert.assertTrue(PRDateUtils.getAge(dateDeNaissance, "10.02.2000") == 0);
            Assert.assertTrue(PRDateUtils.getAge(dateDeNaissance, "10.02.2001") == 1);
            Assert.assertTrue(PRDateUtils.getAge(dateDeNaissance, "10.01.2013") == 12);
            Assert.assertTrue(PRDateUtils.getAge(dateDeNaissance, "09.02.2013") == 12);
            Assert.assertTrue(PRDateUtils.getAge(dateDeNaissance, "10.02.2013") == 13);
            Assert.assertTrue(PRDateUtils.getAge(dateDeNaissance, "11.02.2013") == 13);
            Assert.assertTrue(PRDateUtils.getAge(dateDeNaissance, "07.02.2068") == 67);
            Assert.assertTrue(PRDateUtils.getAge(dateDeNaissance, "11.02.2068") == 68);
            Assert.assertTrue(PRDateUtils.getAge(dateDeNaissance, "12.12.2068") == 68);
            Assert.assertTrue(PRDateUtils.getAge("28.10.1986", "27.10.2013") == 26);
            Assert.assertTrue(PRDateUtils.getAge("28.10.1986", "28.10.2013") == 27);
            Assert.assertTrue(PRDateUtils.getAge("28.11.1986", "27.11.2013") == 26);
            Assert.assertTrue(PRDateUtils.getAge("28.11.1986", "01.12.2013") == 27);

        } catch (DateException exception) {
            exception.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testPlause208ValidationXML() {
        String messageDate = "03.05.2012"; // champsAnnonce.getMessageDate();
        String accountingMonth = "06.2012"; // champsAnnonce.getAccountingMonth();
        String jourEnvoiAnnonce = messageDate.substring(0, 2);
        String accountingMonthPourTest = jourEnvoiAnnonce + "." + accountingMonth;
        switch (PRDateUtils.compare(messageDate, accountingMonthPourTest)) {
            case EQUALS:
            case BEFORE:
                // l'accountingMonth est antérieur (avant) le messageDate -> ok
                // l'accountingMonth est égal au messageDate -> ok
                Assert.assertTrue(false);
                break;
            case AFTER:
                // l'accountingMonth est postérieur (après) le messageDate -> KO
                Assert.assertTrue(true);
                break;
            case INCOMPARABLE:
                // Cas anormal.... que faire pour ne paas bloquer
                Assert.assertTrue(false);
                break;
        }

    }

    @Test
    public void compare() throws Exception {
        String refDate = "10.02.2012";
        String wrongDateFormat = "02.2012";
        String beforeDate = "09.02.2012";
        String afterDate = "11.02.2012";
        Assert.assertTrue(PRDateUtils.compare(refDate, beforeDate).equals(PRDateEquality.BEFORE));
        Assert.assertTrue(PRDateUtils.compare(refDate, refDate).equals(PRDateEquality.EQUALS));
        Assert.assertTrue(PRDateUtils.compare(refDate, afterDate).equals(PRDateEquality.AFTER));

        try {
            PRDateUtils.compare(refDate, wrongDateFormat);
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof DataFormatException);
        }

        Assert.assertTrue(PRDateUtils.compare("02.02.12", "01.02.12").equals(PRDateEquality.BEFORE));
        Assert.assertTrue(PRDateUtils.compare("02.02.12", "02.02.12").equals(PRDateEquality.EQUALS));
        Assert.assertTrue(PRDateUtils.compare("02.02.12", "03.02.12").equals(PRDateEquality.AFTER));

        try {
            PRDateUtils.compare("02.02.12", "02.02.1");
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof DataFormatException);
        }

        try {
            PRDateUtils.compare("02.02.2012", "02.02.1");
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof DataFormatException);
        }

        try {
            PRDateUtils.compare("02.02.12", "02.02.2012");
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof DataFormatException);
        }

        try {
            PRDateUtils.compare(null, "02.02.2012");
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof DataFormatException);
        }

        try {
            PRDateUtils.compare("", "02.02.2012");
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof DataFormatException);
        }

        try {
            PRDateUtils.compare("E", "02.02.2012");
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof DataFormatException);
        }

        try {
            PRDateUtils.compare("02.02.2012", null);
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof DataFormatException);
        }

        try {
            PRDateUtils.compare("02.02.2012", "");
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof DataFormatException);
        }

        try {
            PRDateUtils.compare("02.02.2012", "R");
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof DataFormatException);
        }

        Assert.assertTrue(PRDateUtils.compare("31.01.12", "30.01.12").equals(PRDateEquality.BEFORE));
        Assert.assertTrue(PRDateUtils.compare("31.01.12", "31.01.12").equals(PRDateEquality.EQUALS));
        Assert.assertTrue(PRDateUtils.compare("31.01.12", "01.02.12").equals(PRDateEquality.AFTER));
    }

    @Test
    public void isDateDansLaPeriode() {
        PRPeriode periode = new PRPeriode("01.01.2012", "10.01.2012");
        try {
            Assert.assertTrue(PRDateUtils.isDateDansLaPeriode(periode, "01.01.2012"));
            Assert.assertTrue(PRDateUtils.isDateDansLaPeriode(periode, "10.01.2012"));
            Assert.assertFalse(PRDateUtils.isDateDansLaPeriode(periode, "11.01.2012"));
            Assert.assertFalse(PRDateUtils.isDateDansLaPeriode(periode, "01.02.2012"));
            periode = new PRPeriode("25.12.2012", "10.01.2013");
            Assert.assertTrue(PRDateUtils.isDateDansLaPeriode(periode, "25.12.2012"));
            Assert.assertTrue(PRDateUtils.isDateDansLaPeriode(periode, "31.12.2012"));
            Assert.assertTrue(PRDateUtils.isDateDansLaPeriode(periode, "10.01.2013"));
            Assert.assertFalse(PRDateUtils.isDateDansLaPeriode(periode, "11.01.2013"));
            Assert.assertFalse(PRDateUtils.isDateDansLaPeriode(periode, "24.12.2013"));
        } catch (Exception e) {
            Assert.assertTrue(false);
            e.printStackTrace();
        }
    }

    @Test
    public void hasChevauchementDePeriodesTest() {
        List<PRPeriode> periodes = new ArrayList<PRPeriode>();
        periodes.add(new PRPeriode("01.01.2012", "05.01.2012"));
        periodes.add(new PRPeriode("06.01.2012", "10.01.2012"));
        periodes.add(new PRPeriode("11.01.2012", "15.01.2012"));
        periodes.add(new PRPeriode("26.01.2012", "28.01.2012"));
        Assert.assertFalse(PRDateUtils.hasChevauchementDePeriodes(periodes));

        // Premier jours qui chevauche
        periodes.add(new PRPeriode("01.01.2012", "01.01.2012"));
        Assert.assertTrue(PRDateUtils.hasChevauchementDePeriodes(periodes));
        periodes.remove(periodes.size() - 1);

        // Dernier jours qui chevauche
        periodes.add(new PRPeriode("20.01.2012", "26.01.2012"));
        Assert.assertTrue(PRDateUtils.hasChevauchementDePeriodes(periodes));
        periodes.remove(periodes.size() - 1);

        // Chevauchement total
        periodes.add(new PRPeriode("10.01.2012", "26.01.2012"));
        Assert.assertTrue(PRDateUtils.hasChevauchementDePeriodes(periodes));
        periodes.remove(periodes.size() - 1);

        periodes.add(new PRPeriode("01.02.2012", "05.02.2012"));
        Assert.assertFalse(PRDateUtils.hasChevauchementDePeriodes(periodes));
        periodes.remove(periodes.size() - 1);

    }

    @Test
    public void testGetNbDayBetween() {
        assertTrue(new PeriodeTest("01.02.2013", "01.02.2013", 0));
        assertTrue(new PeriodeTest("01.02.2013", "02.02.2013", 1));
        assertTrue(new PeriodeTest("01.02.2013", "01.03.2013", 28));
        assertTrue(new PeriodeTest("01.01.2013", "01.02.2013", 31));
        assertTrue(new PeriodeTest("01.03.2013", "31.03.2013", 30));
        assertTrue(new PeriodeTest("01.03.2013", "01.04.2013", 31));
        // Date début après date de fin -> exception
        try {
            PRDateUtils.getNbDayBetween("01.04.2013", "01.03.2013");
            Assert.assertTrue(false);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
        // Format date début incorrect -> exception
        try {
            PRDateUtils.getNbDayBetween("01.04.201", "01.03.2013");
            Assert.assertTrue(false);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
        // Format date début incorrect -> exception
        try {
            PRDateUtils.getNbDayBetween("01.04.201", "01.0.2013");
            Assert.assertTrue(false);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

        /**
         * Ces tests sont la pour valider le test dateDeDebut est avant dateDeFin
         */
        Assert.assertTrue(PRDateUtils.getNbDayBetween("01.01.2015", "31.12.2015") == 364); // Année normale
        Assert.assertTrue(PRDateUtils.getNbDayBetween("01.01.2016", "31.12.2016") == 365); // Année bisextile
        Assert.assertTrue(PRDateUtils.getNbDayBetween("01.03.2010", "31.03.2010") == 30);
        Assert.assertTrue(PRDateUtils.getNbDayBetween("10.04.2010", "01.05.2018") == 2943);
    }

    @Test
    public void testGetNbDayBetween2() {
        assertTrue(new PeriodeTest("01.02.2013", "01.02.2013", 0));
        assertTrue(new PeriodeTest("01.02.2013", "02.02.2013", 1));
        assertTrue(new PeriodeTest("01.02.2013", "01.03.2013", 28));
        assertTrue(new PeriodeTest("01.01.2013", "01.02.2013", 31));
        assertTrue(new PeriodeTest("01.03.2013", "31.03.2013", 30));
        assertTrue(new PeriodeTest("01.03.2013", "01.04.2013", 31));
        // Date début après date de fin -> exception
        try {
            PRDateUtils.getNbDayBetween2("01.04.2013", "01.03.2013");
            Assert.assertTrue(false);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
        // Format date début incorrect -> exception
        try {
            PRDateUtils.getNbDayBetween2("01.04.201", "01.03.2013");
            Assert.assertTrue(false);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
        // Format date début incorrect -> exception
        try {
            PRDateUtils.getNbDayBetween2("01.04.201", "01.0.2013");
            Assert.assertTrue(false);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

        /**
         * Ces tests sont la pour valider le test dateDeDebut est avant dateDeFin
         */
        Assert.assertTrue(PRDateUtils.getNbDayBetween("01.01.2015", "31.12.2015") == 364); // Année normale
        Assert.assertTrue(PRDateUtils.getNbDayBetween("01.01.2016", "31.12.2016") == 365); // Année bisextile
        Assert.assertTrue(PRDateUtils.getNbDayBetween("01.03.2010", "31.03.2010") == 30);
        Assert.assertTrue(PRDateUtils.getNbDayBetween("10.04.2010", "01.05.2018") == 2943);
    }

    private void assertTrue(PeriodeTest periode) {
        int nombreJoursCalcule = PRDateUtils.getNbDayBetween(periode.getDateDeDebut(), periode.getDateDeFin());
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(periode.getDateDeDebut());
        sb.append(" - ");
        sb.append(periode.getDateDeFin());
        sb.append("] attendu = ");
        sb.append(periode.getNombreDeJours());
        sb.append(", trouvé = ");
        sb.append(nombreJoursCalcule);
        sb.append(", différence = ");
        sb.append(periode.getNombreDeJours() - nombreJoursCalcule);
        if (nombreJoursCalcule != periode.getNombreDeJours()) {
            System.err.println(sb.toString());
            Assert.assertTrue(false);
        } else {
            System.out.println(sb.toString());
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testGen00001() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.01.2012"), 0);
    }

    @Test
    public void testGen00002() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.01.2012"), 1);
    }

    @Test
    public void testGen00003() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.01.2012"), 2);
    }

    @Test
    public void testGen00004() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.01.2012"), 3);
    }

    @Test
    public void testGen00005() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.01.2012"), 4);
    }

    @Test
    public void testGen00006() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.01.2012"), 5);
    }

    @Test
    public void testGen00007() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.01.2012"), 6);
    }

    @Test
    public void testGen00008() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.01.2012"), 7);
    }

    @Test
    public void testGen00009() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.01.2012"), 8);
    }

    @Test
    public void testGen00010() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.01.2012"), 9);
    }

    @Test
    public void testGen00011() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.01.2012"), 10);
    }

    @Test
    public void testGen00012() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.01.2012"), 11);
    }

    @Test
    public void testGen00013() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.01.2012"), 12);
    }

    @Test
    public void testGen00014() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.01.2012"), 13);
    }

    @Test
    public void testGen00015() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.01.2012"), 14);
    }

    @Test
    public void testGen00016() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.01.2012"), 15);
    }

    @Test
    public void testGen00017() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.01.2012"), 16);
    }

    @Test
    public void testGen00018() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.01.2012"), 17);
    }

    @Test
    public void testGen00019() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.01.2012"), 18);
    }

    @Test
    public void testGen00020() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.01.2012"), 19);
    }

    @Test
    public void testGen00021() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.01.2012"), 20);
    }

    @Test
    public void testGen00022() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.01.2012"), 21);
    }

    @Test
    public void testGen00023() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.01.2012"), 22);
    }

    @Test
    public void testGen00024() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.01.2012"), 23);
    }

    @Test
    public void testGen00025() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.01.2012"), 24);
    }

    @Test
    public void testGen00026() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.01.2012"), 25);
    }

    @Test
    public void testGen00027() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.01.2012"), 26);
    }

    @Test
    public void testGen00028() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.01.2012"), 27);
    }

    @Test
    public void testGen00029() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.01.2012"), 28);
    }

    @Test
    public void testGen00030() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.01.2012"), 29);
    }

    @Test
    public void testGen00031() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.01.2012"), 30);
    }

    @Test
    public void testGen00032() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.02.2012"), 31);
    }

    @Test
    public void testGen00033() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.02.2012"), 32);
    }

    @Test
    public void testGen00034() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.02.2012"), 33);
    }

    @Test
    public void testGen00035() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.02.2012"), 34);
    }

    @Test
    public void testGen00036() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.02.2012"), 35);
    }

    @Test
    public void testGen00037() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.02.2012"), 36);
    }

    @Test
    public void testGen00038() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.02.2012"), 37);
    }

    @Test
    public void testGen00039() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.02.2012"), 38);
    }

    @Test
    public void testGen00040() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.02.2012"), 39);
    }

    @Test
    public void testGen00041() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.02.2012"), 40);
    }

    @Test
    public void testGen00042() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.02.2012"), 41);
    }

    @Test
    public void testGen00043() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.02.2012"), 42);
    }

    @Test
    public void testGen00044() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.02.2012"), 43);
    }

    @Test
    public void testGen00045() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.02.2012"), 44);
    }

    @Test
    public void testGen00046() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.02.2012"), 45);
    }

    @Test
    public void testGen00047() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.02.2012"), 46);
    }

    @Test
    public void testGen00048() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.02.2012"), 47);
    }

    @Test
    public void testGen00049() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.02.2012"), 48);
    }

    @Test
    public void testGen00050() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.02.2012"), 49);
    }

    @Test
    public void testGen00051() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.02.2012"), 50);
    }

    @Test
    public void testGen00052() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.02.2012"), 51);
    }

    @Test
    public void testGen00053() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.02.2012"), 52);
    }

    @Test
    public void testGen00054() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.02.2012"), 53);
    }

    @Test
    public void testGen00055() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.02.2012"), 54);
    }

    @Test
    public void testGen00056() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.02.2012"), 55);
    }

    @Test
    public void testGen00057() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.02.2012"), 56);
    }

    @Test
    public void testGen00058() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.02.2012"), 57);
    }

    @Test
    public void testGen00059() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.02.2012"), 58);
    }

    @Test
    public void testGen00060() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.02.2012"), 59);
    }

    @Test
    public void testGen00061() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.03.2012"), 60);
    }

    @Test
    public void testGen00062() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.03.2012"), 61);
    }

    @Test
    public void testGen00063() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.03.2012"), 62);
    }

    @Test
    public void testGen00064() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.03.2012"), 63);
    }

    @Test
    public void testGen00065() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.03.2012"), 64);
    }

    @Test
    public void testGen00066() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.03.2012"), 65);
    }

    @Test
    public void testGen00067() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.03.2012"), 66);
    }

    @Test
    public void testGen00068() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.03.2012"), 67);
    }

    @Test
    public void testGen00069() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.03.2012"), 68);
    }

    @Test
    public void testGen00070() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.03.2012"), 69);
    }

    @Test
    public void testGen00071() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.03.2012"), 70);
    }

    @Test
    public void testGen00072() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.03.2012"), 71);
    }

    @Test
    public void testGen00073() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.03.2012"), 72);
    }

    @Test
    public void testGen00074() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.03.2012"), 73);
    }

    @Test
    public void testGen00075() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.03.2012"), 74);
    }

    @Test
    public void testGen00076() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.03.2012"), 75);
    }

    @Test
    public void testGen00077() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.03.2012"), 76);
    }

    @Test
    public void testGen00078() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.03.2012"), 77);
    }

    @Test
    public void testGen00079() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.03.2012"), 78);
    }

    @Test
    public void testGen00080() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.03.2012"), 79);
    }

    @Test
    public void testGen00081() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.03.2012"), 80);
    }

    @Test
    public void testGen00082() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.03.2012"), 81);
    }

    @Test
    public void testGen00083() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.03.2012"), 82);
    }

    @Test
    public void testGen00084() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.03.2012"), 83);
    }

    @Test
    public void testGen00085() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.03.2012"), 84);
    }

    @Test
    public void testGen00086() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.03.2012"), 85);
    }

    @Test
    public void testGen00087() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.03.2012"), 86);
    }

    @Test
    public void testGen00088() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.03.2012"), 87);
    }

    @Test
    public void testGen00089() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.03.2012"), 88);
    }

    @Test
    public void testGen00090() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.03.2012"), 89);
    }

    @Test
    public void testGen00091() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.03.2012"), 90);
    }

    @Test
    public void testGen00092() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.04.2012"), 91);
    }

    @Test
    public void testGen00093() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.04.2012"), 92);
    }

    @Test
    public void testGen00094() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.04.2012"), 93);
    }

    @Test
    public void testGen00095() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.04.2012"), 94);
    }

    @Test
    public void testGen00096() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.04.2012"), 95);
    }

    @Test
    public void testGen00097() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.04.2012"), 96);
    }

    @Test
    public void testGen00098() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.04.2012"), 97);
    }

    @Test
    public void testGen00099() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.04.2012"), 98);
    }

    @Test
    public void testGen00100() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.04.2012"), 99);
    }

    @Test
    public void testGen00101() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.04.2012"), 100);
    }

    @Test
    public void testGen00102() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.04.2012"), 101);
    }

    @Test
    public void testGen00103() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.04.2012"), 102);
    }

    @Test
    public void testGen00104() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.04.2012"), 103);
    }

    @Test
    public void testGen00105() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.04.2012"), 104);
    }

    @Test
    public void testGen00106() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.04.2012"), 105);
    }

    @Test
    public void testGen00107() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.04.2012"), 106);
    }

    @Test
    public void testGen00108() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.04.2012"), 107);
    }

    @Test
    public void testGen00109() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.04.2012"), 108);
    }

    @Test
    public void testGen00110() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.04.2012"), 109);
    }

    @Test
    public void testGen00111() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.04.2012"), 110);
    }

    @Test
    public void testGen00112() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.04.2012"), 111);
    }

    @Test
    public void testGen00113() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.04.2012"), 112);
    }

    @Test
    public void testGen00114() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.04.2012"), 113);
    }

    @Test
    public void testGen00115() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.04.2012"), 114);
    }

    @Test
    public void testGen00116() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.04.2012"), 115);
    }

    @Test
    public void testGen00117() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.04.2012"), 116);
    }

    @Test
    public void testGen00118() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.04.2012"), 117);
    }

    @Test
    public void testGen00119() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.04.2012"), 118);
    }

    @Test
    public void testGen00120() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.04.2012"), 119);
    }

    @Test
    public void testGen00121() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.04.2012"), 120);
    }

    @Test
    public void testGen00122() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.05.2012"), 121);
    }

    @Test
    public void testGen00123() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.05.2012"), 122);
    }

    @Test
    public void testGen00124() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.05.2012"), 123);
    }

    @Test
    public void testGen00125() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.05.2012"), 124);
    }

    @Test
    public void testGen00126() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.05.2012"), 125);
    }

    @Test
    public void testGen00127() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.05.2012"), 126);
    }

    @Test
    public void testGen00128() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.05.2012"), 127);
    }

    @Test
    public void testGen00129() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.05.2012"), 128);
    }

    @Test
    public void testGen00130() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.05.2012"), 129);
    }

    @Test
    public void testGen00131() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.05.2012"), 130);
    }

    @Test
    public void testGen00132() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.05.2012"), 131);
    }

    @Test
    public void testGen00133() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.05.2012"), 132);
    }

    @Test
    public void testGen00134() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.05.2012"), 133);
    }

    @Test
    public void testGen00135() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.05.2012"), 134);
    }

    @Test
    public void testGen00136() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.05.2012"), 135);
    }

    @Test
    public void testGen00137() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.05.2012"), 136);
    }

    @Test
    public void testGen00138() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.05.2012"), 137);
    }

    @Test
    public void testGen00139() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.05.2012"), 138);
    }

    @Test
    public void testGen00140() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.05.2012"), 139);
    }

    @Test
    public void testGen00141() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.05.2012"), 140);
    }

    @Test
    public void testGen00142() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.05.2012"), 141);
    }

    @Test
    public void testGen00143() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.05.2012"), 142);
    }

    @Test
    public void testGen00144() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.05.2012"), 143);
    }

    @Test
    public void testGen00145() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.05.2012"), 144);
    }

    @Test
    public void testGen00146() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.05.2012"), 145);
    }

    @Test
    public void testGen00147() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.05.2012"), 146);
    }

    @Test
    public void testGen00148() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.05.2012"), 147);
    }

    @Test
    public void testGen00149() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.05.2012"), 148);
    }

    @Test
    public void testGen00150() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.05.2012"), 149);
    }

    @Test
    public void testGen00151() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.05.2012"), 150);
    }

    @Test
    public void testGen00152() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.05.2012"), 151);
    }

    @Test
    public void testGen00153() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.06.2012"), 152);
    }

    @Test
    public void testGen00154() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.06.2012"), 153);
    }

    @Test
    public void testGen00155() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.06.2012"), 154);
    }

    @Test
    public void testGen00156() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.06.2012"), 155);
    }

    @Test
    public void testGen00157() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.06.2012"), 156);
    }

    @Test
    public void testGen00158() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.06.2012"), 157);
    }

    @Test
    public void testGen00159() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.06.2012"), 158);
    }

    @Test
    public void testGen00160() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.06.2012"), 159);
    }

    @Test
    public void testGen00161() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.06.2012"), 160);
    }

    @Test
    public void testGen00162() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.06.2012"), 161);
    }

    @Test
    public void testGen00163() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.06.2012"), 162);
    }

    @Test
    public void testGen00164() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.06.2012"), 163);
    }

    @Test
    public void testGen00165() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.06.2012"), 164);
    }

    @Test
    public void testGen00166() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.06.2012"), 165);
    }

    @Test
    public void testGen00167() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.06.2012"), 166);
    }

    @Test
    public void testGen00168() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.06.2012"), 167);
    }

    @Test
    public void testGen00169() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.06.2012"), 168);
    }

    @Test
    public void testGen00170() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.06.2012"), 169);
    }

    @Test
    public void testGen00171() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.06.2012"), 170);
    }

    @Test
    public void testGen00172() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.06.2012"), 171);
    }

    @Test
    public void testGen00173() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.06.2012"), 172);
    }

    @Test
    public void testGen00174() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.06.2012"), 173);
    }

    @Test
    public void testGen00175() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.06.2012"), 174);
    }

    @Test
    public void testGen00176() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.06.2012"), 175);
    }

    @Test
    public void testGen00177() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.06.2012"), 176);
    }

    @Test
    public void testGen00178() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.06.2012"), 177);
    }

    @Test
    public void testGen00179() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.06.2012"), 178);
    }

    @Test
    public void testGen00180() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.06.2012"), 179);
    }

    @Test
    public void testGen00181() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.06.2012"), 180);
    }

    @Test
    public void testGen00182() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.06.2012"), 181);
    }

    @Test
    public void testGen00183() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.07.2012"), 182);
    }

    @Test
    public void testGen00184() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.07.2012"), 183);
    }

    @Test
    public void testGen00185() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.07.2012"), 184);
    }

    @Test
    public void testGen00186() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.07.2012"), 185);
    }

    @Test
    public void testGen00187() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.07.2012"), 186);
    }

    @Test
    public void testGen00188() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.07.2012"), 187);
    }

    @Test
    public void testGen00189() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.07.2012"), 188);
    }

    @Test
    public void testGen00190() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.07.2012"), 189);
    }

    @Test
    public void testGen00191() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.07.2012"), 190);
    }

    @Test
    public void testGen00192() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.07.2012"), 191);
    }

    @Test
    public void testGen00193() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.07.2012"), 192);
    }

    @Test
    public void testGen00194() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.07.2012"), 193);
    }

    @Test
    public void testGen00195() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.07.2012"), 194);
    }

    @Test
    public void testGen00196() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.07.2012"), 195);
    }

    @Test
    public void testGen00197() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.07.2012"), 196);
    }

    @Test
    public void testGen00198() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.07.2012"), 197);
    }

    @Test
    public void testGen00199() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.07.2012"), 198);
    }

    @Test
    public void testGen00200() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.07.2012"), 199);
    }

    @Test
    public void testGen00201() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.07.2012"), 200);
    }

    @Test
    public void testGen00202() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.07.2012"), 201);
    }

    @Test
    public void testGen00203() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.07.2012"), 202);
    }

    @Test
    public void testGen00204() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.07.2012"), 203);
    }

    @Test
    public void testGen00205() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.07.2012"), 204);
    }

    @Test
    public void testGen00206() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.07.2012"), 205);
    }

    @Test
    public void testGen00207() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.07.2012"), 206);
    }

    @Test
    public void testGen00208() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.07.2012"), 207);
    }

    @Test
    public void testGen00209() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.07.2012"), 208);
    }

    @Test
    public void testGen00210() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.07.2012"), 209);
    }

    @Test
    public void testGen00211() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.07.2012"), 210);
    }

    @Test
    public void testGen00212() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.07.2012"), 211);
    }

    @Test
    public void testGen00213() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.07.2012"), 212);
    }

    @Test
    public void testGen00214() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.08.2012"), 213);
    }

    @Test
    public void testGen00215() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.08.2012"), 214);
    }

    @Test
    public void testGen00216() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.08.2012"), 215);
    }

    @Test
    public void testGen00217() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.08.2012"), 216);
    }

    @Test
    public void testGen00218() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.08.2012"), 217);
    }

    @Test
    public void testGen00219() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.08.2012"), 218);
    }

    @Test
    public void testGen00220() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.08.2012"), 219);
    }

    @Test
    public void testGen00221() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.08.2012"), 220);
    }

    @Test
    public void testGen00222() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.08.2012"), 221);
    }

    @Test
    public void testGen00223() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.08.2012"), 222);
    }

    @Test
    public void testGen00224() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.08.2012"), 223);
    }

    @Test
    public void testGen00225() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.08.2012"), 224);
    }

    @Test
    public void testGen00226() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.08.2012"), 225);
    }

    @Test
    public void testGen00227() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.08.2012"), 226);
    }

    @Test
    public void testGen00228() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.08.2012"), 227);
    }

    @Test
    public void testGen00229() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.08.2012"), 228);
    }

    @Test
    public void testGen00230() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.08.2012"), 229);
    }

    @Test
    public void testGen00231() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.08.2012"), 230);
    }

    @Test
    public void testGen00232() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.08.2012"), 231);
    }

    @Test
    public void testGen00233() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.08.2012"), 232);
    }

    @Test
    public void testGen00234() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.08.2012"), 233);
    }

    @Test
    public void testGen00235() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.08.2012"), 234);
    }

    @Test
    public void testGen00236() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.08.2012"), 235);
    }

    @Test
    public void testGen00237() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.08.2012"), 236);
    }

    @Test
    public void testGen00238() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.08.2012"), 237);
    }

    @Test
    public void testGen00239() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.08.2012"), 238);
    }

    @Test
    public void testGen00240() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.08.2012"), 239);
    }

    @Test
    public void testGen00241() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.08.2012"), 240);
    }

    @Test
    public void testGen00242() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.08.2012"), 241);
    }

    @Test
    public void testGen00243() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.08.2012"), 242);
    }

    @Test
    public void testGen00244() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.08.2012"), 243);
    }

    @Test
    public void testGen00245() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.09.2012"), 244);
    }

    @Test
    public void testGen00246() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.09.2012"), 245);
    }

    @Test
    public void testGen00247() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.09.2012"), 246);
    }

    @Test
    public void testGen00248() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.09.2012"), 247);
    }

    @Test
    public void testGen00249() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.09.2012"), 248);
    }

    @Test
    public void testGen00250() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.09.2012"), 249);
    }

    @Test
    public void testGen00251() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.09.2012"), 250);
    }

    @Test
    public void testGen00252() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.09.2012"), 251);
    }

    @Test
    public void testGen00253() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.09.2012"), 252);
    }

    @Test
    public void testGen00254() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.09.2012"), 253);
    }

    @Test
    public void testGen00255() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.09.2012"), 254);
    }

    @Test
    public void testGen00256() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.09.2012"), 255);
    }

    @Test
    public void testGen00257() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.09.2012"), 256);
    }

    @Test
    public void testGen00258() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.09.2012"), 257);
    }

    @Test
    public void testGen00259() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.09.2012"), 258);
    }

    @Test
    public void testGen00260() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.09.2012"), 259);
    }

    @Test
    public void testGen00261() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.09.2012"), 260);
    }

    @Test
    public void testGen00262() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.09.2012"), 261);
    }

    @Test
    public void testGen00263() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.09.2012"), 262);
    }

    @Test
    public void testGen00264() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.09.2012"), 263);
    }

    @Test
    public void testGen00265() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.09.2012"), 264);
    }

    @Test
    public void testGen00266() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.09.2012"), 265);
    }

    @Test
    public void testGen00267() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.09.2012"), 266);
    }

    @Test
    public void testGen00268() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.09.2012"), 267);
    }

    @Test
    public void testGen00269() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.09.2012"), 268);
    }

    @Test
    public void testGen00270() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.09.2012"), 269);
    }

    @Test
    public void testGen00271() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.09.2012"), 270);
    }

    @Test
    public void testGen00272() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.09.2012"), 271);
    }

    @Test
    public void testGen00273() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.09.2012"), 272);
    }

    @Test
    public void testGen00274() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.09.2012"), 273);
    }

    @Test
    public void testGen00275() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.10.2012"), 274);
    }

    @Test
    public void testGen00276() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.10.2012"), 275);
    }

    @Test
    public void testGen00277() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.10.2012"), 276);
    }

    @Test
    public void testGen00278() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.10.2012"), 277);
    }

    @Test
    public void testGen00279() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.10.2012"), 278);
    }

    @Test
    public void testGen00280() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.10.2012"), 279);
    }

    @Test
    public void testGen00281() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.10.2012"), 280);
    }

    @Test
    public void testGen00282() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.10.2012"), 281);
    }

    @Test
    public void testGen00283() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.10.2012"), 282);
    }

    @Test
    public void testGen00284() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.10.2012"), 283);
    }

    @Test
    public void testGen00285() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.10.2012"), 284);
    }

    @Test
    public void testGen00286() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.10.2012"), 285);
    }

    @Test
    public void testGen00287() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.10.2012"), 286);
    }

    @Test
    public void testGen00288() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.10.2012"), 287);
    }

    @Test
    public void testGen00289() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.10.2012"), 288);
    }

    @Test
    public void testGen00290() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.10.2012"), 289);
    }

    @Test
    public void testGen00291() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.10.2012"), 290);
    }

    @Test
    public void testGen00292() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.10.2012"), 291);
    }

    @Test
    public void testGen00293() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.10.2012"), 292);
    }

    @Test
    public void testGen00294() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.10.2012"), 293);
    }

    @Test
    public void testGen00295() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.10.2012"), 294);
    }

    @Test
    public void testGen00296() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.10.2012"), 295);
    }

    @Test
    public void testGen00297() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.10.2012"), 296);
    }

    @Test
    public void testGen00298() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.10.2012"), 297);
    }

    @Test
    public void testGen00299() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.10.2012"), 298);
    }

    @Test
    public void testGen00300() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.10.2012"), 299);
    }

    @Test
    public void testGen00301() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.10.2012"), 300);
    }

    @Test
    public void testGen00302() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.10.2012"), 301);
    }

    @Test
    public void testGen00303() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.10.2012"), 302);
    }

    @Test
    public void testGen00304() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.10.2012"), 303);
    }

    @Test
    public void testGen00305() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.10.2012"), 304);
    }

    @Test
    public void testGen00306() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.11.2012"), 305);
    }

    @Test
    public void testGen00307() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.11.2012"), 306);
    }

    @Test
    public void testGen00308() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.11.2012"), 307);
    }

    @Test
    public void testGen00309() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.11.2012"), 308);
    }

    @Test
    public void testGen00310() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.11.2012"), 309);
    }

    @Test
    public void testGen00311() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.11.2012"), 310);
    }

    @Test
    public void testGen00312() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.11.2012"), 311);
    }

    @Test
    public void testGen00313() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.11.2012"), 312);
    }

    @Test
    public void testGen00314() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.11.2012"), 313);
    }

    @Test
    public void testGen00315() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.11.2012"), 314);
    }

    @Test
    public void testGen00316() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.11.2012"), 315);
    }

    @Test
    public void testGen00317() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.11.2012"), 316);
    }

    @Test
    public void testGen00318() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.11.2012"), 317);
    }

    @Test
    public void testGen00319() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.11.2012"), 318);
    }

    @Test
    public void testGen00320() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.11.2012"), 319);
    }

    @Test
    public void testGen00321() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.11.2012"), 320);
    }

    @Test
    public void testGen00322() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.11.2012"), 321);
    }

    @Test
    public void testGen00323() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.11.2012"), 322);
    }

    @Test
    public void testGen00324() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.11.2012"), 323);
    }

    @Test
    public void testGen00325() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.11.2012"), 324);
    }

    @Test
    public void testGen00326() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.11.2012"), 325);
    }

    @Test
    public void testGen00327() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.11.2012"), 326);
    }

    @Test
    public void testGen00328() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.11.2012"), 327);
    }

    @Test
    public void testGen00329() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.11.2012"), 328);
    }

    @Test
    public void testGen00330() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.11.2012"), 329);
    }

    @Test
    public void testGen00331() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.11.2012"), 330);
    }

    @Test
    public void testGen00332() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.11.2012"), 331);
    }

    @Test
    public void testGen00333() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.11.2012"), 332);
    }

    @Test
    public void testGen00334() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.11.2012"), 333);
    }

    @Test
    public void testGen00335() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.11.2012"), 334);
    }

    @Test
    public void testGen00336() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.12.2012"), 335);
    }

    @Test
    public void testGen00337() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.12.2012"), 336);
    }

    @Test
    public void testGen00338() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.12.2012"), 337);
    }

    @Test
    public void testGen00339() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.12.2012"), 338);
    }

    @Test
    public void testGen00340() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.12.2012"), 339);
    }

    @Test
    public void testGen00341() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.12.2012"), 340);
    }

    @Test
    public void testGen00342() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.12.2012"), 341);
    }

    @Test
    public void testGen00343() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.12.2012"), 342);
    }

    @Test
    public void testGen00344() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.12.2012"), 343);
    }

    @Test
    public void testGen00345() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.12.2012"), 344);
    }

    @Test
    public void testGen00346() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.12.2012"), 345);
    }

    @Test
    public void testGen00347() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.12.2012"), 346);
    }

    @Test
    public void testGen00348() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.12.2012"), 347);
    }

    @Test
    public void testGen00349() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.12.2012"), 348);
    }

    @Test
    public void testGen00350() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.12.2012"), 349);
    }

    @Test
    public void testGen00351() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.12.2012"), 350);
    }

    @Test
    public void testGen00352() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.12.2012"), 351);
    }

    @Test
    public void testGen00353() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.12.2012"), 352);
    }

    @Test
    public void testGen00354() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.12.2012"), 353);
    }

    @Test
    public void testGen00355() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.12.2012"), 354);
    }

    @Test
    public void testGen00356() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.12.2012"), 355);
    }

    @Test
    public void testGen00357() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.12.2012"), 356);
    }

    @Test
    public void testGen00358() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.12.2012"), 357);
    }

    @Test
    public void testGen00359() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.12.2012"), 358);
    }

    @Test
    public void testGen00360() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.12.2012"), 359);
    }

    @Test
    public void testGen00361() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.12.2012"), 360);
    }

    @Test
    public void testGen00362() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.12.2012"), 361);
    }

    @Test
    public void testGen00363() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.12.2012"), 362);
    }

    @Test
    public void testGen00364() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.12.2012"), 363);
    }

    @Test
    public void testGen00365() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.12.2012"), 364);
    }

    @Test
    public void testGen00366() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.12.2012"), 365);
    }

    @Test
    public void testGen00367() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.01.2013"), 366);
    }

    @Test
    public void testGen00368() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.01.2013"), 367);
    }

    @Test
    public void testGen00369() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.01.2013"), 368);
    }

    @Test
    public void testGen00370() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.01.2013"), 369);
    }

    @Test
    public void testGen00371() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.01.2013"), 370);
    }

    @Test
    public void testGen00372() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.01.2013"), 371);
    }

    @Test
    public void testGen00373() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.01.2013"), 372);
    }

    @Test
    public void testGen00374() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.01.2013"), 373);
    }

    @Test
    public void testGen00375() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.01.2013"), 374);
    }

    @Test
    public void testGen00376() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.01.2013"), 375);
    }

    @Test
    public void testGen00377() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.01.2013"), 376);
    }

    @Test
    public void testGen00378() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.01.2013"), 377);
    }

    @Test
    public void testGen00379() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.01.2013"), 378);
    }

    @Test
    public void testGen00380() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.01.2013"), 379);
    }

    @Test
    public void testGen00381() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.01.2013"), 380);
    }

    @Test
    public void testGen00382() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.01.2013"), 381);
    }

    @Test
    public void testGen00383() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.01.2013"), 382);
    }

    @Test
    public void testGen00384() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.01.2013"), 383);
    }

    @Test
    public void testGen00385() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.01.2013"), 384);
    }

    @Test
    public void testGen00386() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.01.2013"), 385);
    }

    @Test
    public void testGen00387() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.01.2013"), 386);
    }

    @Test
    public void testGen00388() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.01.2013"), 387);
    }

    @Test
    public void testGen00389() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.01.2013"), 388);
    }

    @Test
    public void testGen00390() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.01.2013"), 389);
    }

    @Test
    public void testGen00391() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.01.2013"), 390);
    }

    @Test
    public void testGen00392() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.01.2013"), 391);
    }

    @Test
    public void testGen00393() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.01.2013"), 392);
    }

    @Test
    public void testGen00394() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.01.2013"), 393);
    }

    @Test
    public void testGen00395() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.01.2013"), 394);
    }

    @Test
    public void testGen00396() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.01.2013"), 395);
    }

    @Test
    public void testGen00397() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.01.2013"), 396);
    }

    @Test
    public void testGen00398() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.02.2013"), 397);
    }

    @Test
    public void testGen00399() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.02.2013"), 398);
    }

    @Test
    public void testGen00400() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.02.2013"), 399);
    }

    @Test
    public void testGen00401() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.02.2013"), 400);
    }

    @Test
    public void testGen00402() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.02.2013"), 401);
    }

    @Test
    public void testGen00403() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.02.2013"), 402);
    }

    @Test
    public void testGen00404() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.02.2013"), 403);
    }

    @Test
    public void testGen00405() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.02.2013"), 404);
    }

    @Test
    public void testGen00406() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.02.2013"), 405);
    }

    @Test
    public void testGen00407() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.02.2013"), 406);
    }

    @Test
    public void testGen00408() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.02.2013"), 407);
    }

    @Test
    public void testGen00409() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.02.2013"), 408);
    }

    @Test
    public void testGen00410() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.02.2013"), 409);
    }

    @Test
    public void testGen00411() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.02.2013"), 410);
    }

    @Test
    public void testGen00412() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.02.2013"), 411);
    }

    @Test
    public void testGen00413() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.02.2013"), 412);
    }

    @Test
    public void testGen00414() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.02.2013"), 413);
    }

    @Test
    public void testGen00415() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.02.2013"), 414);
    }

    @Test
    public void testGen00416() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.02.2013"), 415);
    }

    @Test
    public void testGen00417() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.02.2013"), 416);
    }

    @Test
    public void testGen00418() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.02.2013"), 417);
    }

    @Test
    public void testGen00419() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.02.2013"), 418);
    }

    @Test
    public void testGen00420() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.02.2013"), 419);
    }

    @Test
    public void testGen00421() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.02.2013"), 420);
    }

    @Test
    public void testGen00422() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.02.2013"), 421);
    }

    @Test
    public void testGen00423() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.02.2013"), 422);
    }

    @Test
    public void testGen00424() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.02.2013"), 423);
    }

    @Test
    public void testGen00425() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.02.2013"), 424);
    }

    @Test
    public void testGen00426() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.03.2013"), 425);
    }

    @Test
    public void testGen00427() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.03.2013"), 426);
    }

    @Test
    public void testGen00428() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.03.2013"), 427);
    }

    @Test
    public void testGen00429() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.03.2013"), 428);
    }

    @Test
    public void testGen00430() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.03.2013"), 429);
    }

    @Test
    public void testGen00431() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.03.2013"), 430);
    }

    @Test
    public void testGen00432() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.03.2013"), 431);
    }

    @Test
    public void testGen00433() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.03.2013"), 432);
    }

    @Test
    public void testGen00434() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.03.2013"), 433);
    }

    @Test
    public void testGen00435() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.03.2013"), 434);
    }

    @Test
    public void testGen00436() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.03.2013"), 435);
    }

    @Test
    public void testGen00437() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.03.2013"), 436);
    }

    @Test
    public void testGen00438() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.03.2013"), 437);
    }

    @Test
    public void testGen00439() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.03.2013"), 438);
    }

    @Test
    public void testGen00440() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.03.2013"), 439);
    }

    @Test
    public void testGen00441() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.03.2013"), 440);
    }

    @Test
    public void testGen00442() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.03.2013"), 441);
    }

    @Test
    public void testGen00443() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.03.2013"), 442);
    }

    @Test
    public void testGen00444() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.03.2013"), 443);
    }

    @Test
    public void testGen00445() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.03.2013"), 444);
    }

    @Test
    public void testGen00446() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.03.2013"), 445);
    }

    @Test
    public void testGen00447() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.03.2013"), 446);
    }

    @Test
    public void testGen00448() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.03.2013"), 447);
    }

    @Test
    public void testGen00449() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.03.2013"), 448);
    }

    @Test
    public void testGen00450() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.03.2013"), 449);
    }

    @Test
    public void testGen00451() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.03.2013"), 450);
    }

    @Test
    public void testGen00452() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.03.2013"), 451);
    }

    @Test
    public void testGen00453() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.03.2013"), 452);
    }

    @Test
    public void testGen00454() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.03.2013"), 453);
    }

    @Test
    public void testGen00455() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.03.2013"), 454);
    }

    @Test
    public void testGen00456() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.03.2013"), 455);
    }

    @Test
    public void testGen00457() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.04.2013"), 456);
    }

    @Test
    public void testGen00458() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.04.2013"), 457);
    }

    @Test
    public void testGen00459() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.04.2013"), 458);
    }

    @Test
    public void testGen00460() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.04.2013"), 459);
    }

    @Test
    public void testGen00461() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.04.2013"), 460);
    }

    @Test
    public void testGen00462() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.04.2013"), 461);
    }

    @Test
    public void testGen00463() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.04.2013"), 462);
    }

    @Test
    public void testGen00464() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.04.2013"), 463);
    }

    @Test
    public void testGen00465() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.04.2013"), 464);
    }

    @Test
    public void testGen00466() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.04.2013"), 465);
    }

    @Test
    public void testGen00467() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.04.2013"), 466);
    }

    @Test
    public void testGen00468() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.04.2013"), 467);
    }

    @Test
    public void testGen00469() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.04.2013"), 468);
    }

    @Test
    public void testGen00470() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.04.2013"), 469);
    }

    @Test
    public void testGen00471() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.04.2013"), 470);
    }

    @Test
    public void testGen00472() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.04.2013"), 471);
    }

    @Test
    public void testGen00473() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.04.2013"), 472);
    }

    @Test
    public void testGen00474() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.04.2013"), 473);
    }

    @Test
    public void testGen00475() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.04.2013"), 474);
    }

    @Test
    public void testGen00476() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.04.2013"), 475);
    }

    @Test
    public void testGen00477() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.04.2013"), 476);
    }

    @Test
    public void testGen00478() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.04.2013"), 477);
    }

    @Test
    public void testGen00479() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.04.2013"), 478);
    }

    @Test
    public void testGen00480() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.04.2013"), 479);
    }

    @Test
    public void testGen00481() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.04.2013"), 480);
    }

    @Test
    public void testGen00482() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.04.2013"), 481);
    }

    @Test
    public void testGen00483() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.04.2013"), 482);
    }

    @Test
    public void testGen00484() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.04.2013"), 483);
    }

    @Test
    public void testGen00485() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.04.2013"), 484);
    }

    @Test
    public void testGen00486() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.04.2013"), 485);
    }

    @Test
    public void testGen00487() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.05.2013"), 486);
    }

    @Test
    public void testGen00488() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.05.2013"), 487);
    }

    @Test
    public void testGen00489() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.05.2013"), 488);
    }

    @Test
    public void testGen00490() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.05.2013"), 489);
    }

    @Test
    public void testGen00491() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.05.2013"), 490);
    }

    @Test
    public void testGen00492() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.05.2013"), 491);
    }

    @Test
    public void testGen00493() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.05.2013"), 492);
    }

    @Test
    public void testGen00494() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.05.2013"), 493);
    }

    @Test
    public void testGen00495() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.05.2013"), 494);
    }

    @Test
    public void testGen00496() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.05.2013"), 495);
    }

    @Test
    public void testGen00497() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.05.2013"), 496);
    }

    @Test
    public void testGen00498() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.05.2013"), 497);
    }

    @Test
    public void testGen00499() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.05.2013"), 498);
    }

    @Test
    public void testGen00500() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.05.2013"), 499);
    }

    @Test
    public void testGen00501() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.05.2013"), 500);
    }

    @Test
    public void testGen00502() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.05.2013"), 501);
    }

    @Test
    public void testGen00503() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.05.2013"), 502);
    }

    @Test
    public void testGen00504() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.05.2013"), 503);
    }

    @Test
    public void testGen00505() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.05.2013"), 504);
    }

    @Test
    public void testGen00506() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.05.2013"), 505);
    }

    @Test
    public void testGen00507() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.05.2013"), 506);
    }

    @Test
    public void testGen00508() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.05.2013"), 507);
    }

    @Test
    public void testGen00509() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.05.2013"), 508);
    }

    @Test
    public void testGen00510() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.05.2013"), 509);
    }

    @Test
    public void testGen00511() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.05.2013"), 510);
    }

    @Test
    public void testGen00512() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.05.2013"), 511);
    }

    @Test
    public void testGen00513() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.05.2013"), 512);
    }

    @Test
    public void testGen00514() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.05.2013"), 513);
    }

    @Test
    public void testGen00515() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.05.2013"), 514);
    }

    @Test
    public void testGen00516() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.05.2013"), 515);
    }

    @Test
    public void testGen00517() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.05.2013"), 516);
    }

    @Test
    public void testGen00518() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.06.2013"), 517);
    }

    @Test
    public void testGen00519() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.06.2013"), 518);
    }

    @Test
    public void testGen00520() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.06.2013"), 519);
    }

    @Test
    public void testGen00521() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.06.2013"), 520);
    }

    @Test
    public void testGen00522() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.06.2013"), 521);
    }

    @Test
    public void testGen00523() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.06.2013"), 522);
    }

    @Test
    public void testGen00524() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.06.2013"), 523);
    }

    @Test
    public void testGen00525() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.06.2013"), 524);
    }

    @Test
    public void testGen00526() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.06.2013"), 525);
    }

    @Test
    public void testGen00527() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.06.2013"), 526);
    }

    @Test
    public void testGen00528() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.06.2013"), 527);
    }

    @Test
    public void testGen00529() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.06.2013"), 528);
    }

    @Test
    public void testGen00530() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.06.2013"), 529);
    }

    @Test
    public void testGen00531() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.06.2013"), 530);
    }

    @Test
    public void testGen00532() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.06.2013"), 531);
    }

    @Test
    public void testGen00533() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.06.2013"), 532);
    }

    @Test
    public void testGen00534() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.06.2013"), 533);
    }

    @Test
    public void testGen00535() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.06.2013"), 534);
    }

    @Test
    public void testGen00536() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.06.2013"), 535);
    }

    @Test
    public void testGen00537() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.06.2013"), 536);
    }

    @Test
    public void testGen00538() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.06.2013"), 537);
    }

    @Test
    public void testGen00539() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.06.2013"), 538);
    }

    @Test
    public void testGen00540() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.06.2013"), 539);
    }

    @Test
    public void testGen00541() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.06.2013"), 540);
    }

    @Test
    public void testGen00542() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.06.2013"), 541);
    }

    @Test
    public void testGen00543() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.06.2013"), 542);
    }

    @Test
    public void testGen00544() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.06.2013"), 543);
    }

    @Test
    public void testGen00545() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.06.2013"), 544);
    }

    @Test
    public void testGen00546() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.06.2013"), 545);
    }

    @Test
    public void testGen00547() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.06.2013"), 546);
    }

    @Test
    public void testGen00548() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.07.2013"), 547);
    }

    @Test
    public void testGen00549() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.07.2013"), 548);
    }

    @Test
    public void testGen00550() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.07.2013"), 549);
    }

    @Test
    public void testGen00551() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.07.2013"), 550);
    }

    @Test
    public void testGen00552() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.07.2013"), 551);
    }

    @Test
    public void testGen00553() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.07.2013"), 552);
    }

    @Test
    public void testGen00554() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.07.2013"), 553);
    }

    @Test
    public void testGen00555() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.07.2013"), 554);
    }

    @Test
    public void testGen00556() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.07.2013"), 555);
    }

    @Test
    public void testGen00557() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.07.2013"), 556);
    }

    @Test
    public void testGen00558() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.07.2013"), 557);
    }

    @Test
    public void testGen00559() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.07.2013"), 558);
    }

    @Test
    public void testGen00560() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.07.2013"), 559);
    }

    @Test
    public void testGen00561() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.07.2013"), 560);
    }

    @Test
    public void testGen00562() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.07.2013"), 561);
    }

    @Test
    public void testGen00563() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.07.2013"), 562);
    }

    @Test
    public void testGen00564() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.07.2013"), 563);
    }

    @Test
    public void testGen00565() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.07.2013"), 564);
    }

    @Test
    public void testGen00566() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.07.2013"), 565);
    }

    @Test
    public void testGen00567() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.07.2013"), 566);
    }

    @Test
    public void testGen00568() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.07.2013"), 567);
    }

    @Test
    public void testGen00569() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.07.2013"), 568);
    }

    @Test
    public void testGen00570() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.07.2013"), 569);
    }

    @Test
    public void testGen00571() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.07.2013"), 570);
    }

    @Test
    public void testGen00572() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.07.2013"), 571);
    }

    @Test
    public void testGen00573() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.07.2013"), 572);
    }

    @Test
    public void testGen00574() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.07.2013"), 573);
    }

    @Test
    public void testGen00575() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.07.2013"), 574);
    }

    @Test
    public void testGen00576() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.07.2013"), 575);
    }

    @Test
    public void testGen00577() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.07.2013"), 576);
    }

    @Test
    public void testGen00578() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.07.2013"), 577);
    }

    @Test
    public void testGen00579() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.08.2013"), 578);
    }

    @Test
    public void testGen00580() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.08.2013"), 579);
    }

    @Test
    public void testGen00581() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.08.2013"), 580);
    }

    @Test
    public void testGen00582() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.08.2013"), 581);
    }

    @Test
    public void testGen00583() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.08.2013"), 582);
    }

    @Test
    public void testGen00584() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.08.2013"), 583);
    }

    @Test
    public void testGen00585() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.08.2013"), 584);
    }

    @Test
    public void testGen00586() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.08.2013"), 585);
    }

    @Test
    public void testGen00587() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.08.2013"), 586);
    }

    @Test
    public void testGen00588() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.08.2013"), 587);
    }

    @Test
    public void testGen00589() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.08.2013"), 588);
    }

    @Test
    public void testGen00590() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.08.2013"), 589);
    }

    @Test
    public void testGen00591() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.08.2013"), 590);
    }

    @Test
    public void testGen00592() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.08.2013"), 591);
    }

    @Test
    public void testGen00593() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.08.2013"), 592);
    }

    @Test
    public void testGen00594() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.08.2013"), 593);
    }

    @Test
    public void testGen00595() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.08.2013"), 594);
    }

    @Test
    public void testGen00596() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.08.2013"), 595);
    }

    @Test
    public void testGen00597() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.08.2013"), 596);
    }

    @Test
    public void testGen00598() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.08.2013"), 597);
    }

    @Test
    public void testGen00599() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.08.2013"), 598);
    }

    @Test
    public void testGen00600() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.08.2013"), 599);
    }

    @Test
    public void testGen00601() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.08.2013"), 600);
    }

    @Test
    public void testGen00602() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.08.2013"), 601);
    }

    @Test
    public void testGen00603() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.08.2013"), 602);
    }

    @Test
    public void testGen00604() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.08.2013"), 603);
    }

    @Test
    public void testGen00605() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.08.2013"), 604);
    }

    @Test
    public void testGen00606() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.08.2013"), 605);
    }

    @Test
    public void testGen00607() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.08.2013"), 606);
    }

    @Test
    public void testGen00608() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.08.2013"), 607);
    }

    @Test
    public void testGen00609() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.08.2013"), 608);
    }

    @Test
    public void testGen00610() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.09.2013"), 609);
    }

    @Test
    public void testGen00611() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.09.2013"), 610);
    }

    @Test
    public void testGen00612() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.09.2013"), 611);
    }

    @Test
    public void testGen00613() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.09.2013"), 612);
    }

    @Test
    public void testGen00614() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.09.2013"), 613);
    }

    @Test
    public void testGen00615() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.09.2013"), 614);
    }

    @Test
    public void testGen00616() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.09.2013"), 615);
    }

    @Test
    public void testGen00617() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.09.2013"), 616);
    }

    @Test
    public void testGen00618() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.09.2013"), 617);
    }

    @Test
    public void testGen00619() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.09.2013"), 618);
    }

    @Test
    public void testGen00620() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.09.2013"), 619);
    }

    @Test
    public void testGen00621() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.09.2013"), 620);
    }

    @Test
    public void testGen00622() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.09.2013"), 621);
    }

    @Test
    public void testGen00623() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.09.2013"), 622);
    }

    @Test
    public void testGen00624() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.09.2013"), 623);
    }

    @Test
    public void testGen00625() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.09.2013"), 624);
    }

    @Test
    public void testGen00626() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.09.2013"), 625);
    }

    @Test
    public void testGen00627() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.09.2013"), 626);
    }

    @Test
    public void testGen00628() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.09.2013"), 627);
    }

    @Test
    public void testGen00629() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.09.2013"), 628);
    }

    @Test
    public void testGen00630() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.09.2013"), 629);
    }

    @Test
    public void testGen00631() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.09.2013"), 630);
    }

    @Test
    public void testGen00632() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.09.2013"), 631);
    }

    @Test
    public void testGen00633() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.09.2013"), 632);
    }

    @Test
    public void testGen00634() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.09.2013"), 633);
    }

    @Test
    public void testGen00635() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.09.2013"), 634);
    }

    @Test
    public void testGen00636() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.09.2013"), 635);
    }

    @Test
    public void testGen00637() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.09.2013"), 636);
    }

    @Test
    public void testGen00638() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.09.2013"), 637);
    }

    @Test
    public void testGen00639() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.09.2013"), 638);
    }

    @Test
    public void testGen00640() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.10.2013"), 639);
    }

    @Test
    public void testGen00641() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.10.2013"), 640);
    }

    @Test
    public void testGen00642() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.10.2013"), 641);
    }

    @Test
    public void testGen00643() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.10.2013"), 642);
    }

    @Test
    public void testGen00644() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.10.2013"), 643);
    }

    @Test
    public void testGen00645() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.10.2013"), 644);
    }

    @Test
    public void testGen00646() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.10.2013"), 645);
    }

    @Test
    public void testGen00647() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.10.2013"), 646);
    }

    @Test
    public void testGen00648() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.10.2013"), 647);
    }

    @Test
    public void testGen00649() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.10.2013"), 648);
    }

    @Test
    public void testGen00650() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.10.2013"), 649);
    }

    @Test
    public void testGen00651() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.10.2013"), 650);
    }

    @Test
    public void testGen00652() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.10.2013"), 651);
    }

    @Test
    public void testGen00653() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.10.2013"), 652);
    }

    @Test
    public void testGen00654() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.10.2013"), 653);
    }

    @Test
    public void testGen00655() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.10.2013"), 654);
    }

    @Test
    public void testGen00656() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.10.2013"), 655);
    }

    @Test
    public void testGen00657() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.10.2013"), 656);
    }

    @Test
    public void testGen00658() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.10.2013"), 657);
    }

    @Test
    public void testGen00659() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.10.2013"), 658);
    }

    @Test
    public void testGen00660() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.10.2013"), 659);
    }

    @Test
    public void testGen00661() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.10.2013"), 660);
    }

    @Test
    public void testGen00662() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.10.2013"), 661);
    }

    @Test
    public void testGen00663() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.10.2013"), 662);
    }

    @Test
    public void testGen00664() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.10.2013"), 663);
    }

    @Test
    public void testGen00665() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.10.2013"), 664);
    }

    @Test
    public void testGen00666() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.10.2013"), 665);
    }

    @Test
    public void testGen00667() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.10.2013"), 666);
    }

    @Test
    public void testGen00668() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.10.2013"), 667);
    }

    @Test
    public void testGen00669() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.10.2013"), 668);
    }

    @Test
    public void testGen00670() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.10.2013"), 669);
    }

    @Test
    public void testGen00671() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.11.2013"), 670);
    }

    @Test
    public void testGen00672() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.11.2013"), 671);
    }

    @Test
    public void testGen00673() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.11.2013"), 672);
    }

    @Test
    public void testGen00674() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.11.2013"), 673);
    }

    @Test
    public void testGen00675() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.11.2013"), 674);
    }

    @Test
    public void testGen00676() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.11.2013"), 675);
    }

    @Test
    public void testGen00677() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.11.2013"), 676);
    }

    @Test
    public void testGen00678() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.11.2013"), 677);
    }

    @Test
    public void testGen00679() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.11.2013"), 678);
    }

    @Test
    public void testGen00680() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.11.2013"), 679);
    }

    @Test
    public void testGen00681() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.11.2013"), 680);
    }

    @Test
    public void testGen00682() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.11.2013"), 681);
    }

    @Test
    public void testGen00683() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.11.2013"), 682);
    }

    @Test
    public void testGen00684() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.11.2013"), 683);
    }

    @Test
    public void testGen00685() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.11.2013"), 684);
    }

    @Test
    public void testGen00686() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.11.2013"), 685);
    }

    @Test
    public void testGen00687() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.11.2013"), 686);
    }

    @Test
    public void testGen00688() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.11.2013"), 687);
    }

    @Test
    public void testGen00689() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.11.2013"), 688);
    }

    @Test
    public void testGen00690() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.11.2013"), 689);
    }

    @Test
    public void testGen00691() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.11.2013"), 690);
    }

    @Test
    public void testGen00692() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.11.2013"), 691);
    }

    @Test
    public void testGen00693() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.11.2013"), 692);
    }

    @Test
    public void testGen00694() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.11.2013"), 693);
    }

    @Test
    public void testGen00695() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.11.2013"), 694);
    }

    @Test
    public void testGen00696() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.11.2013"), 695);
    }

    @Test
    public void testGen00697() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.11.2013"), 696);
    }

    @Test
    public void testGen00698() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.11.2013"), 697);
    }

    @Test
    public void testGen00699() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.11.2013"), 698);
    }

    @Test
    public void testGen00700() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.11.2013"), 699);
    }

    @Test
    public void testGen00701() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.12.2013"), 700);
    }

    @Test
    public void testGen00702() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.12.2013"), 701);
    }

    @Test
    public void testGen00703() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.12.2013"), 702);
    }

    @Test
    public void testGen00704() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.12.2013"), 703);
    }

    @Test
    public void testGen00705() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.12.2013"), 704);
    }

    @Test
    public void testGen00706() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.12.2013"), 705);
    }

    @Test
    public void testGen00707() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.12.2013"), 706);
    }

    @Test
    public void testGen00708() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.12.2013"), 707);
    }

    @Test
    public void testGen00709() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.12.2013"), 708);
    }

    @Test
    public void testGen00710() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.12.2013"), 709);
    }

    @Test
    public void testGen00711() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.12.2013"), 710);
    }

    @Test
    public void testGen00712() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.12.2013"), 711);
    }

    @Test
    public void testGen00713() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.12.2013"), 712);
    }

    @Test
    public void testGen00714() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.12.2013"), 713);
    }

    @Test
    public void testGen00715() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.12.2013"), 714);
    }

    @Test
    public void testGen00716() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.12.2013"), 715);
    }

    @Test
    public void testGen00717() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.12.2013"), 716);
    }

    @Test
    public void testGen00718() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.12.2013"), 717);
    }

    @Test
    public void testGen00719() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.12.2013"), 718);
    }

    @Test
    public void testGen00720() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.12.2013"), 719);
    }

    @Test
    public void testGen00721() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.12.2013"), 720);
    }

    @Test
    public void testGen00722() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.12.2013"), 721);
    }

    @Test
    public void testGen00723() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.12.2013"), 722);
    }

    @Test
    public void testGen00724() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.12.2013"), 723);
    }

    @Test
    public void testGen00725() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.12.2013"), 724);
    }

    @Test
    public void testGen00726() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.12.2013"), 725);
    }

    @Test
    public void testGen00727() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.12.2013"), 726);
    }

    @Test
    public void testGen00728() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.12.2013"), 727);
    }

    @Test
    public void testGen00729() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.12.2013"), 728);
    }

    @Test
    public void testGen00730() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.12.2013"), 729);
    }

    @Test
    public void testGen00731() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.12.2013"), 730);
    }

    @Test
    public void testGen00732() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.01.2014"), 731);
    }

    @Test
    public void testGen00733() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.01.2014"), 732);
    }

    @Test
    public void testGen00734() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.01.2014"), 733);
    }

    @Test
    public void testGen00735() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.01.2014"), 734);
    }

    @Test
    public void testGen00736() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.01.2014"), 735);
    }

    @Test
    public void testGen00737() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.01.2014"), 736);
    }

    @Test
    public void testGen00738() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.01.2014"), 737);
    }

    @Test
    public void testGen00739() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.01.2014"), 738);
    }

    @Test
    public void testGen00740() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.01.2014"), 739);
    }

    @Test
    public void testGen00741() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.01.2014"), 740);
    }

    @Test
    public void testGen00742() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.01.2014"), 741);
    }

    @Test
    public void testGen00743() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.01.2014"), 742);
    }

    @Test
    public void testGen00744() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.01.2014"), 743);
    }

    @Test
    public void testGen00745() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.01.2014"), 744);
    }

    @Test
    public void testGen00746() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.01.2014"), 745);
    }

    @Test
    public void testGen00747() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.01.2014"), 746);
    }

    @Test
    public void testGen00748() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.01.2014"), 747);
    }

    @Test
    public void testGen00749() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.01.2014"), 748);
    }

    @Test
    public void testGen00750() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.01.2014"), 749);
    }

    @Test
    public void testGen00751() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.01.2014"), 750);
    }

    @Test
    public void testGen00752() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.01.2014"), 751);
    }

    @Test
    public void testGen00753() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.01.2014"), 752);
    }

    @Test
    public void testGen00754() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.01.2014"), 753);
    }

    @Test
    public void testGen00755() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.01.2014"), 754);
    }

    @Test
    public void testGen00756() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.01.2014"), 755);
    }

    @Test
    public void testGen00757() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.01.2014"), 756);
    }

    @Test
    public void testGen00758() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.01.2014"), 757);
    }

    @Test
    public void testGen00759() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.01.2014"), 758);
    }

    @Test
    public void testGen00760() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.01.2014"), 759);
    }

    @Test
    public void testGen00761() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.01.2014"), 760);
    }

    @Test
    public void testGen00762() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.01.2014"), 761);
    }

    @Test
    public void testGen00763() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.02.2014"), 762);
    }

    @Test
    public void testGen00764() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.02.2014"), 763);
    }

    @Test
    public void testGen00765() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.02.2014"), 764);
    }

    @Test
    public void testGen00766() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.02.2014"), 765);
    }

    @Test
    public void testGen00767() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.02.2014"), 766);
    }

    @Test
    public void testGen00768() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.02.2014"), 767);
    }

    @Test
    public void testGen00769() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.02.2014"), 768);
    }

    @Test
    public void testGen00770() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.02.2014"), 769);
    }

    @Test
    public void testGen00771() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.02.2014"), 770);
    }

    @Test
    public void testGen00772() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.02.2014"), 771);
    }

    @Test
    public void testGen00773() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.02.2014"), 772);
    }

    @Test
    public void testGen00774() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.02.2014"), 773);
    }

    @Test
    public void testGen00775() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.02.2014"), 774);
    }

    @Test
    public void testGen00776() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.02.2014"), 775);
    }

    @Test
    public void testGen00777() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.02.2014"), 776);
    }

    @Test
    public void testGen00778() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.02.2014"), 777);
    }

    @Test
    public void testGen00779() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.02.2014"), 778);
    }

    @Test
    public void testGen00780() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.02.2014"), 779);
    }

    @Test
    public void testGen00781() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.02.2014"), 780);
    }

    @Test
    public void testGen00782() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.02.2014"), 781);
    }

    @Test
    public void testGen00783() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.02.2014"), 782);
    }

    @Test
    public void testGen00784() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.02.2014"), 783);
    }

    @Test
    public void testGen00785() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.02.2014"), 784);
    }

    @Test
    public void testGen00786() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.02.2014"), 785);
    }

    @Test
    public void testGen00787() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.02.2014"), 786);
    }

    @Test
    public void testGen00788() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.02.2014"), 787);
    }

    @Test
    public void testGen00789() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.02.2014"), 788);
    }

    @Test
    public void testGen00790() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.02.2014"), 789);
    }

    @Test
    public void testGen00791() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.03.2014"), 790);
    }

    @Test
    public void testGen00792() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.03.2014"), 791);
    }

    @Test
    public void testGen00793() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.03.2014"), 792);
    }

    @Test
    public void testGen00794() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.03.2014"), 793);
    }

    @Test
    public void testGen00795() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.03.2014"), 794);
    }

    @Test
    public void testGen00796() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.03.2014"), 795);
    }

    @Test
    public void testGen00797() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.03.2014"), 796);
    }

    @Test
    public void testGen00798() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.03.2014"), 797);
    }

    @Test
    public void testGen00799() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.03.2014"), 798);
    }

    @Test
    public void testGen00800() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.03.2014"), 799);
    }

    @Test
    public void testGen00801() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.03.2014"), 800);
    }

    @Test
    public void testGen00802() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.03.2014"), 801);
    }

    @Test
    public void testGen00803() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.03.2014"), 802);
    }

    @Test
    public void testGen00804() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.03.2014"), 803);
    }

    @Test
    public void testGen00805() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.03.2014"), 804);
    }

    @Test
    public void testGen00806() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.03.2014"), 805);
    }

    @Test
    public void testGen00807() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.03.2014"), 806);
    }

    @Test
    public void testGen00808() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.03.2014"), 807);
    }

    @Test
    public void testGen00809() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.03.2014"), 808);
    }

    @Test
    public void testGen00810() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.03.2014"), 809);
    }

    @Test
    public void testGen00811() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.03.2014"), 810);
    }

    @Test
    public void testGen00812() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.03.2014"), 811);
    }

    @Test
    public void testGen00813() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.03.2014"), 812);
    }

    @Test
    public void testGen00814() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.03.2014"), 813);
    }

    @Test
    public void testGen00815() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.03.2014"), 814);
    }

    @Test
    public void testGen00816() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.03.2014"), 815);
    }

    @Test
    public void testGen00817() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.03.2014"), 816);
    }

    @Test
    public void testGen00818() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.03.2014"), 817);
    }

    @Test
    public void testGen00819() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.03.2014"), 818);
    }

    @Test
    public void testGen00820() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.03.2014"), 819);
    }

    @Test
    public void testGen00821() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.03.2014"), 820);
    }

    @Test
    public void testGen00822() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.04.2014"), 821);
    }

    @Test
    public void testGen00823() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.04.2014"), 822);
    }

    @Test
    public void testGen00824() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.04.2014"), 823);
    }

    @Test
    public void testGen00825() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.04.2014"), 824);
    }

    @Test
    public void testGen00826() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.04.2014"), 825);
    }

    @Test
    public void testGen00827() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.04.2014"), 826);
    }

    @Test
    public void testGen00828() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.04.2014"), 827);
    }

    @Test
    public void testGen00829() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.04.2014"), 828);
    }

    @Test
    public void testGen00830() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.04.2014"), 829);
    }

    @Test
    public void testGen00831() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.04.2014"), 830);
    }

    @Test
    public void testGen00832() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.04.2014"), 831);
    }

    @Test
    public void testGen00833() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.04.2014"), 832);
    }

    @Test
    public void testGen00834() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.04.2014"), 833);
    }

    @Test
    public void testGen00835() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.04.2014"), 834);
    }

    @Test
    public void testGen00836() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.04.2014"), 835);
    }

    @Test
    public void testGen00837() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.04.2014"), 836);
    }

    @Test
    public void testGen00838() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.04.2014"), 837);
    }

    @Test
    public void testGen00839() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.04.2014"), 838);
    }

    @Test
    public void testGen00840() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.04.2014"), 839);
    }

    @Test
    public void testGen00841() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.04.2014"), 840);
    }

    @Test
    public void testGen00842() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.04.2014"), 841);
    }

    @Test
    public void testGen00843() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.04.2014"), 842);
    }

    @Test
    public void testGen00844() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.04.2014"), 843);
    }

    @Test
    public void testGen00845() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.04.2014"), 844);
    }

    @Test
    public void testGen00846() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.04.2014"), 845);
    }

    @Test
    public void testGen00847() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.04.2014"), 846);
    }

    @Test
    public void testGen00848() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.04.2014"), 847);
    }

    @Test
    public void testGen00849() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.04.2014"), 848);
    }

    @Test
    public void testGen00850() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.04.2014"), 849);
    }

    @Test
    public void testGen00851() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.04.2014"), 850);
    }

    @Test
    public void testGen00852() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.05.2014"), 851);
    }

    @Test
    public void testGen00853() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.05.2014"), 852);
    }

    @Test
    public void testGen00854() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.05.2014"), 853);
    }

    @Test
    public void testGen00855() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.05.2014"), 854);
    }

    @Test
    public void testGen00856() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.05.2014"), 855);
    }

    @Test
    public void testGen00857() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.05.2014"), 856);
    }

    @Test
    public void testGen00858() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.05.2014"), 857);
    }

    @Test
    public void testGen00859() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.05.2014"), 858);
    }

    @Test
    public void testGen00860() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.05.2014"), 859);
    }

    @Test
    public void testGen00861() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.05.2014"), 860);
    }

    @Test
    public void testGen00862() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.05.2014"), 861);
    }

    @Test
    public void testGen00863() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.05.2014"), 862);
    }

    @Test
    public void testGen00864() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.05.2014"), 863);
    }

    @Test
    public void testGen00865() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.05.2014"), 864);
    }

    @Test
    public void testGen00866() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.05.2014"), 865);
    }

    @Test
    public void testGen00867() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.05.2014"), 866);
    }

    @Test
    public void testGen00868() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.05.2014"), 867);
    }

    @Test
    public void testGen00869() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.05.2014"), 868);
    }

    @Test
    public void testGen00870() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.05.2014"), 869);
    }

    @Test
    public void testGen00871() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.05.2014"), 870);
    }

    @Test
    public void testGen00872() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.05.2014"), 871);
    }

    @Test
    public void testGen00873() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.05.2014"), 872);
    }

    @Test
    public void testGen00874() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.05.2014"), 873);
    }

    @Test
    public void testGen00875() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.05.2014"), 874);
    }

    @Test
    public void testGen00876() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.05.2014"), 875);
    }

    @Test
    public void testGen00877() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.05.2014"), 876);
    }

    @Test
    public void testGen00878() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.05.2014"), 877);
    }

    @Test
    public void testGen00879() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.05.2014"), 878);
    }

    @Test
    public void testGen00880() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.05.2014"), 879);
    }

    @Test
    public void testGen00881() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.05.2014"), 880);
    }

    @Test
    public void testGen00882() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.05.2014"), 881);
    }

    @Test
    public void testGen00883() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.06.2014"), 882);
    }

    @Test
    public void testGen00884() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.06.2014"), 883);
    }

    @Test
    public void testGen00885() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.06.2014"), 884);
    }

    @Test
    public void testGen00886() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.06.2014"), 885);
    }

    @Test
    public void testGen00887() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.06.2014"), 886);
    }

    @Test
    public void testGen00888() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.06.2014"), 887);
    }

    @Test
    public void testGen00889() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.06.2014"), 888);
    }

    @Test
    public void testGen00890() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.06.2014"), 889);
    }

    @Test
    public void testGen00891() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.06.2014"), 890);
    }

    @Test
    public void testGen00892() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.06.2014"), 891);
    }

    @Test
    public void testGen00893() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.06.2014"), 892);
    }

    @Test
    public void testGen00894() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.06.2014"), 893);
    }

    @Test
    public void testGen00895() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.06.2014"), 894);
    }

    @Test
    public void testGen00896() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.06.2014"), 895);
    }

    @Test
    public void testGen00897() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.06.2014"), 896);
    }

    @Test
    public void testGen00898() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.06.2014"), 897);
    }

    @Test
    public void testGen00899() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.06.2014"), 898);
    }

    @Test
    public void testGen00900() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.06.2014"), 899);
    }

    @Test
    public void testGen00901() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.06.2014"), 900);
    }

    @Test
    public void testGen00902() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.06.2014"), 901);
    }

    @Test
    public void testGen00903() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.06.2014"), 902);
    }

    @Test
    public void testGen00904() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.06.2014"), 903);
    }

    @Test
    public void testGen00905() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.06.2014"), 904);
    }

    @Test
    public void testGen00906() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.06.2014"), 905);
    }

    @Test
    public void testGen00907() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.06.2014"), 906);
    }

    @Test
    public void testGen00908() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.06.2014"), 907);
    }

    @Test
    public void testGen00909() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.06.2014"), 908);
    }

    @Test
    public void testGen00910() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.06.2014"), 909);
    }

    @Test
    public void testGen00911() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.06.2014"), 910);
    }

    @Test
    public void testGen00912() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.06.2014"), 911);
    }

    @Test
    public void testGen00913() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.07.2014"), 912);
    }

    @Test
    public void testGen00914() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.07.2014"), 913);
    }

    @Test
    public void testGen00915() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.07.2014"), 914);
    }

    @Test
    public void testGen00916() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.07.2014"), 915);
    }

    @Test
    public void testGen00917() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.07.2014"), 916);
    }

    @Test
    public void testGen00918() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.07.2014"), 917);
    }

    @Test
    public void testGen00919() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.07.2014"), 918);
    }

    @Test
    public void testGen00920() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.07.2014"), 919);
    }

    @Test
    public void testGen00921() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.07.2014"), 920);
    }

    @Test
    public void testGen00922() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.07.2014"), 921);
    }

    @Test
    public void testGen00923() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.07.2014"), 922);
    }

    @Test
    public void testGen00924() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.07.2014"), 923);
    }

    @Test
    public void testGen00925() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.07.2014"), 924);
    }

    @Test
    public void testGen00926() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.07.2014"), 925);
    }

    @Test
    public void testGen00927() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.07.2014"), 926);
    }

    @Test
    public void testGen00928() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.07.2014"), 927);
    }

    @Test
    public void testGen00929() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.07.2014"), 928);
    }

    @Test
    public void testGen00930() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.07.2014"), 929);
    }

    @Test
    public void testGen00931() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.07.2014"), 930);
    }

    @Test
    public void testGen00932() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.07.2014"), 931);
    }

    @Test
    public void testGen00933() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.07.2014"), 932);
    }

    @Test
    public void testGen00934() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.07.2014"), 933);
    }

    @Test
    public void testGen00935() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.07.2014"), 934);
    }

    @Test
    public void testGen00936() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.07.2014"), 935);
    }

    @Test
    public void testGen00937() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.07.2014"), 936);
    }

    @Test
    public void testGen00938() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.07.2014"), 937);
    }

    @Test
    public void testGen00939() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.07.2014"), 938);
    }

    @Test
    public void testGen00940() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.07.2014"), 939);
    }

    @Test
    public void testGen00941() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.07.2014"), 940);
    }

    @Test
    public void testGen00942() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.07.2014"), 941);
    }

    @Test
    public void testGen00943() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.07.2014"), 942);
    }

    @Test
    public void testGen00944() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.08.2014"), 943);
    }

    @Test
    public void testGen00945() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.08.2014"), 944);
    }

    @Test
    public void testGen00946() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.08.2014"), 945);
    }

    @Test
    public void testGen00947() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.08.2014"), 946);
    }

    @Test
    public void testGen00948() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.08.2014"), 947);
    }

    @Test
    public void testGen00949() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.08.2014"), 948);
    }

    @Test
    public void testGen00950() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.08.2014"), 949);
    }

    @Test
    public void testGen00951() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.08.2014"), 950);
    }

    @Test
    public void testGen00952() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.08.2014"), 951);
    }

    @Test
    public void testGen00953() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.08.2014"), 952);
    }

    @Test
    public void testGen00954() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.08.2014"), 953);
    }

    @Test
    public void testGen00955() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.08.2014"), 954);
    }

    @Test
    public void testGen00956() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.08.2014"), 955);
    }

    @Test
    public void testGen00957() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.08.2014"), 956);
    }

    @Test
    public void testGen00958() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.08.2014"), 957);
    }

    @Test
    public void testGen00959() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.08.2014"), 958);
    }

    @Test
    public void testGen00960() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.08.2014"), 959);
    }

    @Test
    public void testGen00961() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.08.2014"), 960);
    }

    @Test
    public void testGen00962() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.08.2014"), 961);
    }

    @Test
    public void testGen00963() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.08.2014"), 962);
    }

    @Test
    public void testGen00964() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.08.2014"), 963);
    }

    @Test
    public void testGen00965() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.08.2014"), 964);
    }

    @Test
    public void testGen00966() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.08.2014"), 965);
    }

    @Test
    public void testGen00967() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.08.2014"), 966);
    }

    @Test
    public void testGen00968() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.08.2014"), 967);
    }

    @Test
    public void testGen00969() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.08.2014"), 968);
    }

    @Test
    public void testGen00970() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.08.2014"), 969);
    }

    @Test
    public void testGen00971() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.08.2014"), 970);
    }

    @Test
    public void testGen00972() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.08.2014"), 971);
    }

    @Test
    public void testGen00973() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.08.2014"), 972);
    }

    @Test
    public void testGen00974() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.08.2014"), 973);
    }

    @Test
    public void testGen00975() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.09.2014"), 974);
    }

    @Test
    public void testGen00976() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.09.2014"), 975);
    }

    @Test
    public void testGen00977() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.09.2014"), 976);
    }

    @Test
    public void testGen00978() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.09.2014"), 977);
    }

    @Test
    public void testGen00979() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.09.2014"), 978);
    }

    @Test
    public void testGen00980() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.09.2014"), 979);
    }

    @Test
    public void testGen00981() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.09.2014"), 980);
    }

    @Test
    public void testGen00982() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.09.2014"), 981);
    }

    @Test
    public void testGen00983() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.09.2014"), 982);
    }

    @Test
    public void testGen00984() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.09.2014"), 983);
    }

    @Test
    public void testGen00985() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.09.2014"), 984);
    }

    @Test
    public void testGen00986() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.09.2014"), 985);
    }

    @Test
    public void testGen00987() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.09.2014"), 986);
    }

    @Test
    public void testGen00988() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.09.2014"), 987);
    }

    @Test
    public void testGen00989() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.09.2014"), 988);
    }

    @Test
    public void testGen00990() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.09.2014"), 989);
    }

    @Test
    public void testGen00991() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.09.2014"), 990);
    }

    @Test
    public void testGen00992() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.09.2014"), 991);
    }

    @Test
    public void testGen00993() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.09.2014"), 992);
    }

    @Test
    public void testGen00994() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.09.2014"), 993);
    }

    @Test
    public void testGen00995() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.09.2014"), 994);
    }

    @Test
    public void testGen00996() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.09.2014"), 995);
    }

    @Test
    public void testGen00997() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.09.2014"), 996);
    }

    @Test
    public void testGen00998() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.09.2014"), 997);
    }

    @Test
    public void testGen00999() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.09.2014"), 998);
    }

    @Test
    public void testGen01000() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.09.2014"), 999);
    }

    @Test
    public void testGen01001() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.09.2014"), 1000);
    }

    @Test
    public void testGen01002() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.09.2014"), 1001);
    }

    @Test
    public void testGen01003() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.09.2014"), 1002);
    }

    @Test
    public void testGen01004() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.09.2014"), 1003);
    }

    @Test
    public void testGen01005() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.10.2014"), 1004);
    }

    @Test
    public void testGen01006() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.10.2014"), 1005);
    }

    @Test
    public void testGen01007() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.10.2014"), 1006);
    }

    @Test
    public void testGen01008() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.10.2014"), 1007);
    }

    @Test
    public void testGen01009() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.10.2014"), 1008);
    }

    @Test
    public void testGen01010() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.10.2014"), 1009);
    }

    @Test
    public void testGen01011() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.10.2014"), 1010);
    }

    @Test
    public void testGen01012() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.10.2014"), 1011);
    }

    @Test
    public void testGen01013() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.10.2014"), 1012);
    }

    @Test
    public void testGen01014() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.10.2014"), 1013);
    }

    @Test
    public void testGen01015() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.10.2014"), 1014);
    }

    @Test
    public void testGen01016() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.10.2014"), 1015);
    }

    @Test
    public void testGen01017() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.10.2014"), 1016);
    }

    @Test
    public void testGen01018() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.10.2014"), 1017);
    }

    @Test
    public void testGen01019() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.10.2014"), 1018);
    }

    @Test
    public void testGen01020() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.10.2014"), 1019);
    }

    @Test
    public void testGen01021() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.10.2014"), 1020);
    }

    @Test
    public void testGen01022() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.10.2014"), 1021);
    }

    @Test
    public void testGen01023() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.10.2014"), 1022);
    }

    @Test
    public void testGen01024() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.10.2014"), 1023);
    }

    @Test
    public void testGen01025() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.10.2014"), 1024);
    }

    @Test
    public void testGen01026() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.10.2014"), 1025);
    }

    @Test
    public void testGen01027() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.10.2014"), 1026);
    }

    @Test
    public void testGen01028() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.10.2014"), 1027);
    }

    @Test
    public void testGen01029() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.10.2014"), 1028);
    }

    @Test
    public void testGen01030() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.10.2014"), 1029);
    }

    @Test
    public void testGen01031() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.10.2014"), 1030);
    }

    @Test
    public void testGen01032() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.10.2014"), 1031);
    }

    @Test
    public void testGen01033() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.10.2014"), 1032);
    }

    @Test
    public void testGen01034() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.10.2014"), 1033);
    }

    @Test
    public void testGen01035() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.10.2014"), 1034);
    }

    @Test
    public void testGen01036() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.11.2014"), 1035);
    }

    @Test
    public void testGen01037() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.11.2014"), 1036);
    }

    @Test
    public void testGen01038() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.11.2014"), 1037);
    }

    @Test
    public void testGen01039() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.11.2014"), 1038);
    }

    @Test
    public void testGen01040() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.11.2014"), 1039);
    }

    @Test
    public void testGen01041() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.11.2014"), 1040);
    }

    @Test
    public void testGen01042() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.11.2014"), 1041);
    }

    @Test
    public void testGen01043() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.11.2014"), 1042);
    }

    @Test
    public void testGen01044() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.11.2014"), 1043);
    }

    @Test
    public void testGen01045() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.11.2014"), 1044);
    }

    @Test
    public void testGen01046() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.11.2014"), 1045);
    }

    @Test
    public void testGen01047() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.11.2014"), 1046);
    }

    @Test
    public void testGen01048() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.11.2014"), 1047);
    }

    @Test
    public void testGen01049() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.11.2014"), 1048);
    }

    @Test
    public void testGen01050() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.11.2014"), 1049);
    }

    @Test
    public void testGen01051() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.11.2014"), 1050);
    }

    @Test
    public void testGen01052() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.11.2014"), 1051);
    }

    @Test
    public void testGen01053() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.11.2014"), 1052);
    }

    @Test
    public void testGen01054() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.11.2014"), 1053);
    }

    @Test
    public void testGen01055() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.11.2014"), 1054);
    }

    @Test
    public void testGen01056() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.11.2014"), 1055);
    }

    @Test
    public void testGen01057() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.11.2014"), 1056);
    }

    @Test
    public void testGen01058() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.11.2014"), 1057);
    }

    @Test
    public void testGen01059() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.11.2014"), 1058);
    }

    @Test
    public void testGen01060() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.11.2014"), 1059);
    }

    @Test
    public void testGen01061() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.11.2014"), 1060);
    }

    @Test
    public void testGen01062() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.11.2014"), 1061);
    }

    @Test
    public void testGen01063() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.11.2014"), 1062);
    }

    @Test
    public void testGen01064() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.11.2014"), 1063);
    }

    @Test
    public void testGen01065() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.11.2014"), 1064);
    }

    @Test
    public void testGen01066() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.12.2014"), 1065);
    }

    @Test
    public void testGen01067() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.12.2014"), 1066);
    }

    @Test
    public void testGen01068() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.12.2014"), 1067);
    }

    @Test
    public void testGen01069() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.12.2014"), 1068);
    }

    @Test
    public void testGen01070() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.12.2014"), 1069);
    }

    @Test
    public void testGen01071() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.12.2014"), 1070);
    }

    @Test
    public void testGen01072() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.12.2014"), 1071);
    }

    @Test
    public void testGen01073() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.12.2014"), 1072);
    }

    @Test
    public void testGen01074() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.12.2014"), 1073);
    }

    @Test
    public void testGen01075() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.12.2014"), 1074);
    }

    @Test
    public void testGen01076() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.12.2014"), 1075);
    }

    @Test
    public void testGen01077() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.12.2014"), 1076);
    }

    @Test
    public void testGen01078() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.12.2014"), 1077);
    }

    @Test
    public void testGen01079() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.12.2014"), 1078);
    }

    @Test
    public void testGen01080() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.12.2014"), 1079);
    }

    @Test
    public void testGen01081() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.12.2014"), 1080);
    }

    @Test
    public void testGen01082() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.12.2014"), 1081);
    }

    @Test
    public void testGen01083() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.12.2014"), 1082);
    }

    @Test
    public void testGen01084() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.12.2014"), 1083);
    }

    @Test
    public void testGen01085() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.12.2014"), 1084);
    }

    @Test
    public void testGen01086() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.12.2014"), 1085);
    }

    @Test
    public void testGen01087() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.12.2014"), 1086);
    }

    @Test
    public void testGen01088() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.12.2014"), 1087);
    }

    @Test
    public void testGen01089() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.12.2014"), 1088);
    }

    @Test
    public void testGen01090() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.12.2014"), 1089);
    }

    @Test
    public void testGen01091() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.12.2014"), 1090);
    }

    @Test
    public void testGen01092() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.12.2014"), 1091);
    }

    @Test
    public void testGen01093() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.12.2014"), 1092);
    }

    @Test
    public void testGen01094() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.12.2014"), 1093);
    }

    @Test
    public void testGen01095() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.12.2014"), 1094);
    }

    @Test
    public void testGen01096() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.12.2014"), 1095);
    }

    @Test
    public void testGen01097() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.01.2015"), 1096);
    }

    @Test
    public void testGen01098() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.01.2015"), 1097);
    }

    @Test
    public void testGen01099() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.01.2015"), 1098);
    }

    @Test
    public void testGen01100() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.01.2015"), 1099);
    }

    @Test
    public void testGen01101() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.01.2015"), 1100);
    }

    @Test
    public void testGen01102() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.01.2015"), 1101);
    }

    @Test
    public void testGen01103() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.01.2015"), 1102);
    }

    @Test
    public void testGen01104() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.01.2015"), 1103);
    }

    @Test
    public void testGen01105() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.01.2015"), 1104);
    }

    @Test
    public void testGen01106() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.01.2015"), 1105);
    }

    @Test
    public void testGen01107() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.01.2015"), 1106);
    }

    @Test
    public void testGen01108() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.01.2015"), 1107);
    }

    @Test
    public void testGen01109() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.01.2015"), 1108);
    }

    @Test
    public void testGen01110() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.01.2015"), 1109);
    }

    @Test
    public void testGen01111() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.01.2015"), 1110);
    }

    @Test
    public void testGen01112() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.01.2015"), 1111);
    }

    @Test
    public void testGen01113() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.01.2015"), 1112);
    }

    @Test
    public void testGen01114() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.01.2015"), 1113);
    }

    @Test
    public void testGen01115() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.01.2015"), 1114);
    }

    @Test
    public void testGen01116() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.01.2015"), 1115);
    }

    @Test
    public void testGen01117() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.01.2015"), 1116);
    }

    @Test
    public void testGen01118() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.01.2015"), 1117);
    }

    @Test
    public void testGen01119() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.01.2015"), 1118);
    }

    @Test
    public void testGen01120() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.01.2015"), 1119);
    }

    @Test
    public void testGen01121() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.01.2015"), 1120);
    }

    @Test
    public void testGen01122() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.01.2015"), 1121);
    }

    @Test
    public void testGen01123() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.01.2015"), 1122);
    }

    @Test
    public void testGen01124() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.01.2015"), 1123);
    }

    @Test
    public void testGen01125() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.01.2015"), 1124);
    }

    @Test
    public void testGen01126() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.01.2015"), 1125);
    }

    @Test
    public void testGen01127() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.01.2015"), 1126);
    }

    @Test
    public void testGen01128() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.02.2015"), 1127);
    }

    @Test
    public void testGen01129() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.02.2015"), 1128);
    }

    @Test
    public void testGen01130() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.02.2015"), 1129);
    }

    @Test
    public void testGen01131() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.02.2015"), 1130);
    }

    @Test
    public void testGen01132() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.02.2015"), 1131);
    }

    @Test
    public void testGen01133() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.02.2015"), 1132);
    }

    @Test
    public void testGen01134() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.02.2015"), 1133);
    }

    @Test
    public void testGen01135() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.02.2015"), 1134);
    }

    @Test
    public void testGen01136() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.02.2015"), 1135);
    }

    @Test
    public void testGen01137() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.02.2015"), 1136);
    }

    @Test
    public void testGen01138() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.02.2015"), 1137);
    }

    @Test
    public void testGen01139() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.02.2015"), 1138);
    }

    @Test
    public void testGen01140() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.02.2015"), 1139);
    }

    @Test
    public void testGen01141() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.02.2015"), 1140);
    }

    @Test
    public void testGen01142() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.02.2015"), 1141);
    }

    @Test
    public void testGen01143() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.02.2015"), 1142);
    }

    @Test
    public void testGen01144() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.02.2015"), 1143);
    }

    @Test
    public void testGen01145() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.02.2015"), 1144);
    }

    @Test
    public void testGen01146() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.02.2015"), 1145);
    }

    @Test
    public void testGen01147() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.02.2015"), 1146);
    }

    @Test
    public void testGen01148() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.02.2015"), 1147);
    }

    @Test
    public void testGen01149() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.02.2015"), 1148);
    }

    @Test
    public void testGen01150() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.02.2015"), 1149);
    }

    @Test
    public void testGen01151() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.02.2015"), 1150);
    }

    @Test
    public void testGen01152() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.02.2015"), 1151);
    }

    @Test
    public void testGen01153() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.02.2015"), 1152);
    }

    @Test
    public void testGen01154() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.02.2015"), 1153);
    }

    @Test
    public void testGen01155() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.02.2015"), 1154);
    }

    @Test
    public void testGen01156() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.03.2015"), 1155);
    }

    @Test
    public void testGen01157() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.03.2015"), 1156);
    }

    @Test
    public void testGen01158() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.03.2015"), 1157);
    }

    @Test
    public void testGen01159() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.03.2015"), 1158);
    }

    @Test
    public void testGen01160() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.03.2015"), 1159);
    }

    @Test
    public void testGen01161() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.03.2015"), 1160);
    }

    @Test
    public void testGen01162() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.03.2015"), 1161);
    }

    @Test
    public void testGen01163() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.03.2015"), 1162);
    }

    @Test
    public void testGen01164() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.03.2015"), 1163);
    }

    @Test
    public void testGen01165() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.03.2015"), 1164);
    }

    @Test
    public void testGen01166() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.03.2015"), 1165);
    }

    @Test
    public void testGen01167() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.03.2015"), 1166);
    }

    @Test
    public void testGen01168() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.03.2015"), 1167);
    }

    @Test
    public void testGen01169() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.03.2015"), 1168);
    }

    @Test
    public void testGen01170() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.03.2015"), 1169);
    }

    @Test
    public void testGen01171() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.03.2015"), 1170);
    }

    @Test
    public void testGen01172() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.03.2015"), 1171);
    }

    @Test
    public void testGen01173() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.03.2015"), 1172);
    }

    @Test
    public void testGen01174() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.03.2015"), 1173);
    }

    @Test
    public void testGen01175() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.03.2015"), 1174);
    }

    @Test
    public void testGen01176() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.03.2015"), 1175);
    }

    @Test
    public void testGen01177() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.03.2015"), 1176);
    }

    @Test
    public void testGen01178() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.03.2015"), 1177);
    }

    @Test
    public void testGen01179() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.03.2015"), 1178);
    }

    @Test
    public void testGen01180() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.03.2015"), 1179);
    }

    @Test
    public void testGen01181() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.03.2015"), 1180);
    }

    @Test
    public void testGen01182() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.03.2015"), 1181);
    }

    @Test
    public void testGen01183() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.03.2015"), 1182);
    }

    @Test
    public void testGen01184() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.03.2015"), 1183);
    }

    @Test
    public void testGen01185() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.03.2015"), 1184);
    }

    @Test
    public void testGen01186() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.03.2015"), 1185);
    }

    @Test
    public void testGen01187() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.04.2015"), 1186);
    }

    @Test
    public void testGen01188() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.04.2015"), 1187);
    }

    @Test
    public void testGen01189() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.04.2015"), 1188);
    }

    @Test
    public void testGen01190() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.04.2015"), 1189);
    }

    @Test
    public void testGen01191() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.04.2015"), 1190);
    }

    @Test
    public void testGen01192() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.04.2015"), 1191);
    }

    @Test
    public void testGen01193() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.04.2015"), 1192);
    }

    @Test
    public void testGen01194() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.04.2015"), 1193);
    }

    @Test
    public void testGen01195() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.04.2015"), 1194);
    }

    @Test
    public void testGen01196() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.04.2015"), 1195);
    }

    @Test
    public void testGen01197() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.04.2015"), 1196);
    }

    @Test
    public void testGen01198() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.04.2015"), 1197);
    }

    @Test
    public void testGen01199() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.04.2015"), 1198);
    }

    @Test
    public void testGen01200() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.04.2015"), 1199);
    }

    @Test
    public void testGen01201() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.04.2015"), 1200);
    }

    @Test
    public void testGen01202() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.04.2015"), 1201);
    }

    @Test
    public void testGen01203() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.04.2015"), 1202);
    }

    @Test
    public void testGen01204() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.04.2015"), 1203);
    }

    @Test
    public void testGen01205() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.04.2015"), 1204);
    }

    @Test
    public void testGen01206() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.04.2015"), 1205);
    }

    @Test
    public void testGen01207() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.04.2015"), 1206);
    }

    @Test
    public void testGen01208() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.04.2015"), 1207);
    }

    @Test
    public void testGen01209() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.04.2015"), 1208);
    }

    @Test
    public void testGen01210() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.04.2015"), 1209);
    }

    @Test
    public void testGen01211() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.04.2015"), 1210);
    }

    @Test
    public void testGen01212() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.04.2015"), 1211);
    }

    @Test
    public void testGen01213() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.04.2015"), 1212);
    }

    @Test
    public void testGen01214() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.04.2015"), 1213);
    }

    @Test
    public void testGen01215() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.04.2015"), 1214);
    }

    @Test
    public void testGen01216() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.04.2015"), 1215);
    }

    @Test
    public void testGen01217() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.05.2015"), 1216);
    }

    @Test
    public void testGen01218() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.05.2015"), 1217);
    }

    @Test
    public void testGen01219() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.05.2015"), 1218);
    }

    @Test
    public void testGen01220() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.05.2015"), 1219);
    }

    @Test
    public void testGen01221() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.05.2015"), 1220);
    }

    @Test
    public void testGen01222() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.05.2015"), 1221);
    }

    @Test
    public void testGen01223() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.05.2015"), 1222);
    }

    @Test
    public void testGen01224() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.05.2015"), 1223);
    }

    @Test
    public void testGen01225() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.05.2015"), 1224);
    }

    @Test
    public void testGen01226() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.05.2015"), 1225);
    }

    @Test
    public void testGen01227() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.05.2015"), 1226);
    }

    @Test
    public void testGen01228() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.05.2015"), 1227);
    }

    @Test
    public void testGen01229() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.05.2015"), 1228);
    }

    @Test
    public void testGen01230() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.05.2015"), 1229);
    }

    @Test
    public void testGen01231() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.05.2015"), 1230);
    }

    @Test
    public void testGen01232() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.05.2015"), 1231);
    }

    @Test
    public void testGen01233() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.05.2015"), 1232);
    }

    @Test
    public void testGen01234() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.05.2015"), 1233);
    }

    @Test
    public void testGen01235() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.05.2015"), 1234);
    }

    @Test
    public void testGen01236() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.05.2015"), 1235);
    }

    @Test
    public void testGen01237() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.05.2015"), 1236);
    }

    @Test
    public void testGen01238() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.05.2015"), 1237);
    }

    @Test
    public void testGen01239() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.05.2015"), 1238);
    }

    @Test
    public void testGen01240() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.05.2015"), 1239);
    }

    @Test
    public void testGen01241() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.05.2015"), 1240);
    }

    @Test
    public void testGen01242() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.05.2015"), 1241);
    }

    @Test
    public void testGen01243() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.05.2015"), 1242);
    }

    @Test
    public void testGen01244() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.05.2015"), 1243);
    }

    @Test
    public void testGen01245() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.05.2015"), 1244);
    }

    @Test
    public void testGen01246() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.05.2015"), 1245);
    }

    @Test
    public void testGen01247() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.05.2015"), 1246);
    }

    @Test
    public void testGen01248() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.06.2015"), 1247);
    }

    @Test
    public void testGen01249() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.06.2015"), 1248);
    }

    @Test
    public void testGen01250() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.06.2015"), 1249);
    }

    @Test
    public void testGen01251() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.06.2015"), 1250);
    }

    @Test
    public void testGen01252() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.06.2015"), 1251);
    }

    @Test
    public void testGen01253() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.06.2015"), 1252);
    }

    @Test
    public void testGen01254() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.06.2015"), 1253);
    }

    @Test
    public void testGen01255() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.06.2015"), 1254);
    }

    @Test
    public void testGen01256() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.06.2015"), 1255);
    }

    @Test
    public void testGen01257() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.06.2015"), 1256);
    }

    @Test
    public void testGen01258() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.06.2015"), 1257);
    }

    @Test
    public void testGen01259() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.06.2015"), 1258);
    }

    @Test
    public void testGen01260() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.06.2015"), 1259);
    }

    @Test
    public void testGen01261() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.06.2015"), 1260);
    }

    @Test
    public void testGen01262() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.06.2015"), 1261);
    }

    @Test
    public void testGen01263() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.06.2015"), 1262);
    }

    @Test
    public void testGen01264() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.06.2015"), 1263);
    }

    @Test
    public void testGen01265() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.06.2015"), 1264);
    }

    @Test
    public void testGen01266() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.06.2015"), 1265);
    }

    @Test
    public void testGen01267() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.06.2015"), 1266);
    }

    @Test
    public void testGen01268() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.06.2015"), 1267);
    }

    @Test
    public void testGen01269() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.06.2015"), 1268);
    }

    @Test
    public void testGen01270() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.06.2015"), 1269);
    }

    @Test
    public void testGen01271() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.06.2015"), 1270);
    }

    @Test
    public void testGen01272() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.06.2015"), 1271);
    }

    @Test
    public void testGen01273() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.06.2015"), 1272);
    }

    @Test
    public void testGen01274() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.06.2015"), 1273);
    }

    @Test
    public void testGen01275() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.06.2015"), 1274);
    }

    @Test
    public void testGen01276() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.06.2015"), 1275);
    }

    @Test
    public void testGen01277() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.06.2015"), 1276);
    }

    @Test
    public void testGen01278() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.07.2015"), 1277);
    }

    @Test
    public void testGen01279() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.07.2015"), 1278);
    }

    @Test
    public void testGen01280() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.07.2015"), 1279);
    }

    @Test
    public void testGen01281() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.07.2015"), 1280);
    }

    @Test
    public void testGen01282() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.07.2015"), 1281);
    }

    @Test
    public void testGen01283() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.07.2015"), 1282);
    }

    @Test
    public void testGen01284() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.07.2015"), 1283);
    }

    @Test
    public void testGen01285() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.07.2015"), 1284);
    }

    @Test
    public void testGen01286() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.07.2015"), 1285);
    }

    @Test
    public void testGen01287() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.07.2015"), 1286);
    }

    @Test
    public void testGen01288() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.07.2015"), 1287);
    }

    @Test
    public void testGen01289() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.07.2015"), 1288);
    }

    @Test
    public void testGen01290() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.07.2015"), 1289);
    }

    @Test
    public void testGen01291() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.07.2015"), 1290);
    }

    @Test
    public void testGen01292() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.07.2015"), 1291);
    }

    @Test
    public void testGen01293() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.07.2015"), 1292);
    }

    @Test
    public void testGen01294() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.07.2015"), 1293);
    }

    @Test
    public void testGen01295() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.07.2015"), 1294);
    }

    @Test
    public void testGen01296() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.07.2015"), 1295);
    }

    @Test
    public void testGen01297() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.07.2015"), 1296);
    }

    @Test
    public void testGen01298() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.07.2015"), 1297);
    }

    @Test
    public void testGen01299() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.07.2015"), 1298);
    }

    @Test
    public void testGen01300() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.07.2015"), 1299);
    }

    @Test
    public void testGen01301() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.07.2015"), 1300);
    }

    @Test
    public void testGen01302() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.07.2015"), 1301);
    }

    @Test
    public void testGen01303() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.07.2015"), 1302);
    }

    @Test
    public void testGen01304() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.07.2015"), 1303);
    }

    @Test
    public void testGen01305() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.07.2015"), 1304);
    }

    @Test
    public void testGen01306() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.07.2015"), 1305);
    }

    @Test
    public void testGen01307() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.07.2015"), 1306);
    }

    @Test
    public void testGen01308() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.07.2015"), 1307);
    }

    @Test
    public void testGen01309() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.08.2015"), 1308);
    }

    @Test
    public void testGen01310() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.08.2015"), 1309);
    }

    @Test
    public void testGen01311() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.08.2015"), 1310);
    }

    @Test
    public void testGen01312() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.08.2015"), 1311);
    }

    @Test
    public void testGen01313() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.08.2015"), 1312);
    }

    @Test
    public void testGen01314() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.08.2015"), 1313);
    }

    @Test
    public void testGen01315() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.08.2015"), 1314);
    }

    @Test
    public void testGen01316() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.08.2015"), 1315);
    }

    @Test
    public void testGen01317() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.08.2015"), 1316);
    }

    @Test
    public void testGen01318() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.08.2015"), 1317);
    }

    @Test
    public void testGen01319() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.08.2015"), 1318);
    }

    @Test
    public void testGen01320() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.08.2015"), 1319);
    }

    @Test
    public void testGen01321() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.08.2015"), 1320);
    }

    @Test
    public void testGen01322() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.08.2015"), 1321);
    }

    @Test
    public void testGen01323() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.08.2015"), 1322);
    }

    @Test
    public void testGen01324() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.08.2015"), 1323);
    }

    @Test
    public void testGen01325() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.08.2015"), 1324);
    }

    @Test
    public void testGen01326() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.08.2015"), 1325);
    }

    @Test
    public void testGen01327() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.08.2015"), 1326);
    }

    @Test
    public void testGen01328() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.08.2015"), 1327);
    }

    @Test
    public void testGen01329() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.08.2015"), 1328);
    }

    @Test
    public void testGen01330() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.08.2015"), 1329);
    }

    @Test
    public void testGen01331() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.08.2015"), 1330);
    }

    @Test
    public void testGen01332() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.08.2015"), 1331);
    }

    @Test
    public void testGen01333() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.08.2015"), 1332);
    }

    @Test
    public void testGen01334() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.08.2015"), 1333);
    }

    @Test
    public void testGen01335() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.08.2015"), 1334);
    }

    @Test
    public void testGen01336() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.08.2015"), 1335);
    }

    @Test
    public void testGen01337() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.08.2015"), 1336);
    }

    @Test
    public void testGen01338() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.08.2015"), 1337);
    }

    @Test
    public void testGen01339() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.08.2015"), 1338);
    }

    @Test
    public void testGen01340() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.09.2015"), 1339);
    }

    @Test
    public void testGen01341() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.09.2015"), 1340);
    }

    @Test
    public void testGen01342() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.09.2015"), 1341);
    }

    @Test
    public void testGen01343() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.09.2015"), 1342);
    }

    @Test
    public void testGen01344() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.09.2015"), 1343);
    }

    @Test
    public void testGen01345() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.09.2015"), 1344);
    }

    @Test
    public void testGen01346() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.09.2015"), 1345);
    }

    @Test
    public void testGen01347() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.09.2015"), 1346);
    }

    @Test
    public void testGen01348() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.09.2015"), 1347);
    }

    @Test
    public void testGen01349() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.09.2015"), 1348);
    }

    @Test
    public void testGen01350() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.09.2015"), 1349);
    }

    @Test
    public void testGen01351() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.09.2015"), 1350);
    }

    @Test
    public void testGen01352() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.09.2015"), 1351);
    }

    @Test
    public void testGen01353() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.09.2015"), 1352);
    }

    @Test
    public void testGen01354() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.09.2015"), 1353);
    }

    @Test
    public void testGen01355() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.09.2015"), 1354);
    }

    @Test
    public void testGen01356() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.09.2015"), 1355);
    }

    @Test
    public void testGen01357() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.09.2015"), 1356);
    }

    @Test
    public void testGen01358() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.09.2015"), 1357);
    }

    @Test
    public void testGen01359() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.09.2015"), 1358);
    }

    @Test
    public void testGen01360() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.09.2015"), 1359);
    }

    @Test
    public void testGen01361() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.09.2015"), 1360);
    }

    @Test
    public void testGen01362() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.09.2015"), 1361);
    }

    @Test
    public void testGen01363() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.09.2015"), 1362);
    }

    @Test
    public void testGen01364() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.09.2015"), 1363);
    }

    @Test
    public void testGen01365() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.09.2015"), 1364);
    }

    @Test
    public void testGen01366() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.09.2015"), 1365);
    }

    @Test
    public void testGen01367() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.09.2015"), 1366);
    }

    @Test
    public void testGen01368() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.09.2015"), 1367);
    }

    @Test
    public void testGen01369() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.09.2015"), 1368);
    }

    @Test
    public void testGen01370() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.10.2015"), 1369);
    }

    @Test
    public void testGen01371() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.10.2015"), 1370);
    }

    @Test
    public void testGen01372() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.10.2015"), 1371);
    }

    @Test
    public void testGen01373() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.10.2015"), 1372);
    }

    @Test
    public void testGen01374() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.10.2015"), 1373);
    }

    @Test
    public void testGen01375() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.10.2015"), 1374);
    }

    @Test
    public void testGen01376() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.10.2015"), 1375);
    }

    @Test
    public void testGen01377() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.10.2015"), 1376);
    }

    @Test
    public void testGen01378() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.10.2015"), 1377);
    }

    @Test
    public void testGen01379() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.10.2015"), 1378);
    }

    @Test
    public void testGen01380() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.10.2015"), 1379);
    }

    @Test
    public void testGen01381() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.10.2015"), 1380);
    }

    @Test
    public void testGen01382() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.10.2015"), 1381);
    }

    @Test
    public void testGen01383() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.10.2015"), 1382);
    }

    @Test
    public void testGen01384() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.10.2015"), 1383);
    }

    @Test
    public void testGen01385() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.10.2015"), 1384);
    }

    @Test
    public void testGen01386() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.10.2015"), 1385);
    }

    @Test
    public void testGen01387() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.10.2015"), 1386);
    }

    @Test
    public void testGen01388() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.10.2015"), 1387);
    }

    @Test
    public void testGen01389() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.10.2015"), 1388);
    }

    @Test
    public void testGen01390() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.10.2015"), 1389);
    }

    @Test
    public void testGen01391() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.10.2015"), 1390);
    }

    @Test
    public void testGen01392() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.10.2015"), 1391);
    }

    @Test
    public void testGen01393() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.10.2015"), 1392);
    }

    @Test
    public void testGen01394() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.10.2015"), 1393);
    }

    @Test
    public void testGen01395() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.10.2015"), 1394);
    }

    @Test
    public void testGen01396() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.10.2015"), 1395);
    }

    @Test
    public void testGen01397() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.10.2015"), 1396);
    }

    @Test
    public void testGen01398() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.10.2015"), 1397);
    }

    @Test
    public void testGen01399() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.10.2015"), 1398);
    }

    @Test
    public void testGen01400() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.10.2015"), 1399);
    }

    @Test
    public void testGen01401() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.11.2015"), 1400);
    }

    @Test
    public void testGen01402() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.11.2015"), 1401);
    }

    @Test
    public void testGen01403() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.11.2015"), 1402);
    }

    @Test
    public void testGen01404() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.11.2015"), 1403);
    }

    @Test
    public void testGen01405() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.11.2015"), 1404);
    }

    @Test
    public void testGen01406() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.11.2015"), 1405);
    }

    @Test
    public void testGen01407() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.11.2015"), 1406);
    }

    @Test
    public void testGen01408() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.11.2015"), 1407);
    }

    @Test
    public void testGen01409() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.11.2015"), 1408);
    }

    @Test
    public void testGen01410() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.11.2015"), 1409);
    }

    @Test
    public void testGen01411() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.11.2015"), 1410);
    }

    @Test
    public void testGen01412() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.11.2015"), 1411);
    }

    @Test
    public void testGen01413() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.11.2015"), 1412);
    }

    @Test
    public void testGen01414() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.11.2015"), 1413);
    }

    @Test
    public void testGen01415() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.11.2015"), 1414);
    }

    @Test
    public void testGen01416() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.11.2015"), 1415);
    }

    @Test
    public void testGen01417() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.11.2015"), 1416);
    }

    @Test
    public void testGen01418() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.11.2015"), 1417);
    }

    @Test
    public void testGen01419() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.11.2015"), 1418);
    }

    @Test
    public void testGen01420() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.11.2015"), 1419);
    }

    @Test
    public void testGen01421() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.11.2015"), 1420);
    }

    @Test
    public void testGen01422() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.11.2015"), 1421);
    }

    @Test
    public void testGen01423() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.11.2015"), 1422);
    }

    @Test
    public void testGen01424() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.11.2015"), 1423);
    }

    @Test
    public void testGen01425() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.11.2015"), 1424);
    }

    @Test
    public void testGen01426() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.11.2015"), 1425);
    }

    @Test
    public void testGen01427() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.11.2015"), 1426);
    }

    @Test
    public void testGen01428() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.11.2015"), 1427);
    }

    @Test
    public void testGen01429() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.11.2015"), 1428);
    }

    @Test
    public void testGen01430() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.11.2015"), 1429);
    }

    @Test
    public void testGen01431() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.12.2015"), 1430);
    }

    @Test
    public void testGen01432() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.12.2015"), 1431);
    }

    @Test
    public void testGen01433() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.12.2015"), 1432);
    }

    @Test
    public void testGen01434() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.12.2015"), 1433);
    }

    @Test
    public void testGen01435() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.12.2015"), 1434);
    }

    @Test
    public void testGen01436() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.12.2015"), 1435);
    }

    @Test
    public void testGen01437() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.12.2015"), 1436);
    }

    @Test
    public void testGen01438() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.12.2015"), 1437);
    }

    @Test
    public void testGen01439() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.12.2015"), 1438);
    }

    @Test
    public void testGen01440() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.12.2015"), 1439);
    }

    @Test
    public void testGen01441() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.12.2015"), 1440);
    }

    @Test
    public void testGen01442() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.12.2015"), 1441);
    }

    @Test
    public void testGen01443() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.12.2015"), 1442);
    }

    @Test
    public void testGen01444() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.12.2015"), 1443);
    }

    @Test
    public void testGen01445() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.12.2015"), 1444);
    }

    @Test
    public void testGen01446() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.12.2015"), 1445);
    }

    @Test
    public void testGen01447() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.12.2015"), 1446);
    }

    @Test
    public void testGen01448() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.12.2015"), 1447);
    }

    @Test
    public void testGen01449() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.12.2015"), 1448);
    }

    @Test
    public void testGen01450() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.12.2015"), 1449);
    }

    @Test
    public void testGen01451() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.12.2015"), 1450);
    }

    @Test
    public void testGen01452() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.12.2015"), 1451);
    }

    @Test
    public void testGen01453() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.12.2015"), 1452);
    }

    @Test
    public void testGen01454() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.12.2015"), 1453);
    }

    @Test
    public void testGen01455() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.12.2015"), 1454);
    }

    @Test
    public void testGen01456() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.12.2015"), 1455);
    }

    @Test
    public void testGen01457() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.12.2015"), 1456);
    }

    @Test
    public void testGen01458() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.12.2015"), 1457);
    }

    @Test
    public void testGen01459() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.12.2015"), 1458);
    }

    @Test
    public void testGen01460() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.12.2015"), 1459);
    }

    @Test
    public void testGen01461() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.12.2015"), 1460);
    }

    @Test
    public void testGen01462() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.01.2016"), 1461);
    }

    @Test
    public void testGen01463() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.01.2016"), 1462);
    }

    @Test
    public void testGen01464() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.01.2016"), 1463);
    }

    @Test
    public void testGen01465() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.01.2016"), 1464);
    }

    @Test
    public void testGen01466() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.01.2016"), 1465);
    }

    @Test
    public void testGen01467() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.01.2016"), 1466);
    }

    @Test
    public void testGen01468() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.01.2016"), 1467);
    }

    @Test
    public void testGen01469() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.01.2016"), 1468);
    }

    @Test
    public void testGen01470() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.01.2016"), 1469);
    }

    @Test
    public void testGen01471() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.01.2016"), 1470);
    }

    @Test
    public void testGen01472() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.01.2016"), 1471);
    }

    @Test
    public void testGen01473() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.01.2016"), 1472);
    }

    @Test
    public void testGen01474() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.01.2016"), 1473);
    }

    @Test
    public void testGen01475() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.01.2016"), 1474);
    }

    @Test
    public void testGen01476() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.01.2016"), 1475);
    }

    @Test
    public void testGen01477() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.01.2016"), 1476);
    }

    @Test
    public void testGen01478() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.01.2016"), 1477);
    }

    @Test
    public void testGen01479() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.01.2016"), 1478);
    }

    @Test
    public void testGen01480() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.01.2016"), 1479);
    }

    @Test
    public void testGen01481() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.01.2016"), 1480);
    }

    @Test
    public void testGen01482() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.01.2016"), 1481);
    }

    @Test
    public void testGen01483() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.01.2016"), 1482);
    }

    @Test
    public void testGen01484() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.01.2016"), 1483);
    }

    @Test
    public void testGen01485() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.01.2016"), 1484);
    }

    @Test
    public void testGen01486() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.01.2016"), 1485);
    }

    @Test
    public void testGen01487() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.01.2016"), 1486);
    }

    @Test
    public void testGen01488() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.01.2016"), 1487);
    }

    @Test
    public void testGen01489() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.01.2016"), 1488);
    }

    @Test
    public void testGen01490() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.01.2016"), 1489);
    }

    @Test
    public void testGen01491() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.01.2016"), 1490);
    }

    @Test
    public void testGen01492() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.01.2016"), 1491);
    }

    @Test
    public void testGen01493() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.02.2016"), 1492);
    }

    @Test
    public void testGen01494() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.02.2016"), 1493);
    }

    @Test
    public void testGen01495() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.02.2016"), 1494);
    }

    @Test
    public void testGen01496() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.02.2016"), 1495);
    }

    @Test
    public void testGen01497() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.02.2016"), 1496);
    }

    @Test
    public void testGen01498() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.02.2016"), 1497);
    }

    @Test
    public void testGen01499() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.02.2016"), 1498);
    }

    @Test
    public void testGen01500() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.02.2016"), 1499);
    }

    @Test
    public void testGen01501() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.02.2016"), 1500);
    }

    @Test
    public void testGen01502() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.02.2016"), 1501);
    }

    @Test
    public void testGen01503() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.02.2016"), 1502);
    }

    @Test
    public void testGen01504() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.02.2016"), 1503);
    }

    @Test
    public void testGen01505() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.02.2016"), 1504);
    }

    @Test
    public void testGen01506() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.02.2016"), 1505);
    }

    @Test
    public void testGen01507() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.02.2016"), 1506);
    }

    @Test
    public void testGen01508() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.02.2016"), 1507);
    }

    @Test
    public void testGen01509() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.02.2016"), 1508);
    }

    @Test
    public void testGen01510() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.02.2016"), 1509);
    }

    @Test
    public void testGen01511() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.02.2016"), 1510);
    }

    @Test
    public void testGen01512() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.02.2016"), 1511);
    }

    @Test
    public void testGen01513() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.02.2016"), 1512);
    }

    @Test
    public void testGen01514() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.02.2016"), 1513);
    }

    @Test
    public void testGen01515() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.02.2016"), 1514);
    }

    @Test
    public void testGen01516() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.02.2016"), 1515);
    }

    @Test
    public void testGen01517() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.02.2016"), 1516);
    }

    @Test
    public void testGen01518() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.02.2016"), 1517);
    }

    @Test
    public void testGen01519() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.02.2016"), 1518);
    }

    @Test
    public void testGen01520() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.02.2016"), 1519);
    }

    @Test
    public void testGen01521() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.02.2016"), 1520);
    }

    @Test
    public void testGen01522() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.03.2016"), 1521);
    }

    @Test
    public void testGen01523() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.03.2016"), 1522);
    }

    @Test
    public void testGen01524() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.03.2016"), 1523);
    }

    @Test
    public void testGen01525() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.03.2016"), 1524);
    }

    @Test
    public void testGen01526() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.03.2016"), 1525);
    }

    @Test
    public void testGen01527() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.03.2016"), 1526);
    }

    @Test
    public void testGen01528() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.03.2016"), 1527);
    }

    @Test
    public void testGen01529() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.03.2016"), 1528);
    }

    @Test
    public void testGen01530() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.03.2016"), 1529);
    }

    @Test
    public void testGen01531() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.03.2016"), 1530);
    }

    @Test
    public void testGen01532() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.03.2016"), 1531);
    }

    @Test
    public void testGen01533() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.03.2016"), 1532);
    }

    @Test
    public void testGen01534() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.03.2016"), 1533);
    }

    @Test
    public void testGen01535() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.03.2016"), 1534);
    }

    @Test
    public void testGen01536() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.03.2016"), 1535);
    }

    @Test
    public void testGen01537() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.03.2016"), 1536);
    }

    @Test
    public void testGen01538() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.03.2016"), 1537);
    }

    @Test
    public void testGen01539() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.03.2016"), 1538);
    }

    @Test
    public void testGen01540() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.03.2016"), 1539);
    }

    @Test
    public void testGen01541() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.03.2016"), 1540);
    }

    @Test
    public void testGen01542() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.03.2016"), 1541);
    }

    @Test
    public void testGen01543() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.03.2016"), 1542);
    }

    @Test
    public void testGen01544() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.03.2016"), 1543);
    }

    @Test
    public void testGen01545() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.03.2016"), 1544);
    }

    @Test
    public void testGen01546() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.03.2016"), 1545);
    }

    @Test
    public void testGen01547() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.03.2016"), 1546);
    }

    @Test
    public void testGen01548() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.03.2016"), 1547);
    }

    @Test
    public void testGen01549() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.03.2016"), 1548);
    }

    @Test
    public void testGen01550() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.03.2016"), 1549);
    }

    @Test
    public void testGen01551() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.03.2016"), 1550);
    }

    @Test
    public void testGen01552() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.03.2016"), 1551);
    }

    @Test
    public void testGen01553() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.04.2016"), 1552);
    }

    @Test
    public void testGen01554() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.04.2016"), 1553);
    }

    @Test
    public void testGen01555() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.04.2016"), 1554);
    }

    @Test
    public void testGen01556() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.04.2016"), 1555);
    }

    @Test
    public void testGen01557() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.04.2016"), 1556);
    }

    @Test
    public void testGen01558() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.04.2016"), 1557);
    }

    @Test
    public void testGen01559() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.04.2016"), 1558);
    }

    @Test
    public void testGen01560() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.04.2016"), 1559);
    }

    @Test
    public void testGen01561() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.04.2016"), 1560);
    }

    @Test
    public void testGen01562() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.04.2016"), 1561);
    }

    @Test
    public void testGen01563() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.04.2016"), 1562);
    }

    @Test
    public void testGen01564() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.04.2016"), 1563);
    }

    @Test
    public void testGen01565() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.04.2016"), 1564);
    }

    @Test
    public void testGen01566() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.04.2016"), 1565);
    }

    @Test
    public void testGen01567() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.04.2016"), 1566);
    }

    @Test
    public void testGen01568() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.04.2016"), 1567);
    }

    @Test
    public void testGen01569() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.04.2016"), 1568);
    }

    @Test
    public void testGen01570() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.04.2016"), 1569);
    }

    @Test
    public void testGen01571() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.04.2016"), 1570);
    }

    @Test
    public void testGen01572() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.04.2016"), 1571);
    }

    @Test
    public void testGen01573() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.04.2016"), 1572);
    }

    @Test
    public void testGen01574() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.04.2016"), 1573);
    }

    @Test
    public void testGen01575() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.04.2016"), 1574);
    }

    @Test
    public void testGen01576() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.04.2016"), 1575);
    }

    @Test
    public void testGen01577() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.04.2016"), 1576);
    }

    @Test
    public void testGen01578() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.04.2016"), 1577);
    }

    @Test
    public void testGen01579() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.04.2016"), 1578);
    }

    @Test
    public void testGen01580() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.04.2016"), 1579);
    }

    @Test
    public void testGen01581() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.04.2016"), 1580);
    }

    @Test
    public void testGen01582() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.04.2016"), 1581);
    }

    @Test
    public void testGen01583() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.05.2016"), 1582);
    }

    @Test
    public void testGen01584() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.05.2016"), 1583);
    }

    @Test
    public void testGen01585() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.05.2016"), 1584);
    }

    @Test
    public void testGen01586() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.05.2016"), 1585);
    }

    @Test
    public void testGen01587() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.05.2016"), 1586);
    }

    @Test
    public void testGen01588() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.05.2016"), 1587);
    }

    @Test
    public void testGen01589() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.05.2016"), 1588);
    }

    @Test
    public void testGen01590() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.05.2016"), 1589);
    }

    @Test
    public void testGen01591() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.05.2016"), 1590);
    }

    @Test
    public void testGen01592() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.05.2016"), 1591);
    }

    @Test
    public void testGen01593() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.05.2016"), 1592);
    }

    @Test
    public void testGen01594() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.05.2016"), 1593);
    }

    @Test
    public void testGen01595() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.05.2016"), 1594);
    }

    @Test
    public void testGen01596() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.05.2016"), 1595);
    }

    @Test
    public void testGen01597() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.05.2016"), 1596);
    }

    @Test
    public void testGen01598() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.05.2016"), 1597);
    }

    @Test
    public void testGen01599() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.05.2016"), 1598);
    }

    @Test
    public void testGen01600() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.05.2016"), 1599);
    }

    @Test
    public void testGen01601() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.05.2016"), 1600);
    }

    @Test
    public void testGen01602() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.05.2016"), 1601);
    }

    @Test
    public void testGen01603() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.05.2016"), 1602);
    }

    @Test
    public void testGen01604() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.05.2016"), 1603);
    }

    @Test
    public void testGen01605() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.05.2016"), 1604);
    }

    @Test
    public void testGen01606() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.05.2016"), 1605);
    }

    @Test
    public void testGen01607() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.05.2016"), 1606);
    }

    @Test
    public void testGen01608() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.05.2016"), 1607);
    }

    @Test
    public void testGen01609() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.05.2016"), 1608);
    }

    @Test
    public void testGen01610() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.05.2016"), 1609);
    }

    @Test
    public void testGen01611() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.05.2016"), 1610);
    }

    @Test
    public void testGen01612() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.05.2016"), 1611);
    }

    @Test
    public void testGen01613() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.05.2016"), 1612);
    }

    @Test
    public void testGen01614() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.06.2016"), 1613);
    }

    @Test
    public void testGen01615() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.06.2016"), 1614);
    }

    @Test
    public void testGen01616() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.06.2016"), 1615);
    }

    @Test
    public void testGen01617() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.06.2016"), 1616);
    }

    @Test
    public void testGen01618() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.06.2016"), 1617);
    }

    @Test
    public void testGen01619() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.06.2016"), 1618);
    }

    @Test
    public void testGen01620() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.06.2016"), 1619);
    }

    @Test
    public void testGen01621() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.06.2016"), 1620);
    }

    @Test
    public void testGen01622() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.06.2016"), 1621);
    }

    @Test
    public void testGen01623() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.06.2016"), 1622);
    }

    @Test
    public void testGen01624() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.06.2016"), 1623);
    }

    @Test
    public void testGen01625() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.06.2016"), 1624);
    }

    @Test
    public void testGen01626() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.06.2016"), 1625);
    }

    @Test
    public void testGen01627() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.06.2016"), 1626);
    }

    @Test
    public void testGen01628() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.06.2016"), 1627);
    }

    @Test
    public void testGen01629() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.06.2016"), 1628);
    }

    @Test
    public void testGen01630() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.06.2016"), 1629);
    }

    @Test
    public void testGen01631() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.06.2016"), 1630);
    }

    @Test
    public void testGen01632() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.06.2016"), 1631);
    }

    @Test
    public void testGen01633() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.06.2016"), 1632);
    }

    @Test
    public void testGen01634() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.06.2016"), 1633);
    }

    @Test
    public void testGen01635() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.06.2016"), 1634);
    }

    @Test
    public void testGen01636() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.06.2016"), 1635);
    }

    @Test
    public void testGen01637() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.06.2016"), 1636);
    }

    @Test
    public void testGen01638() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.06.2016"), 1637);
    }

    @Test
    public void testGen01639() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.06.2016"), 1638);
    }

    @Test
    public void testGen01640() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.06.2016"), 1639);
    }

    @Test
    public void testGen01641() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.06.2016"), 1640);
    }

    @Test
    public void testGen01642() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.06.2016"), 1641);
    }

    @Test
    public void testGen01643() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.06.2016"), 1642);
    }

    @Test
    public void testGen01644() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.07.2016"), 1643);
    }

    @Test
    public void testGen01645() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.07.2016"), 1644);
    }

    @Test
    public void testGen01646() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.07.2016"), 1645);
    }

    @Test
    public void testGen01647() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.07.2016"), 1646);
    }

    @Test
    public void testGen01648() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.07.2016"), 1647);
    }

    @Test
    public void testGen01649() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.07.2016"), 1648);
    }

    @Test
    public void testGen01650() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.07.2016"), 1649);
    }

    @Test
    public void testGen01651() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.07.2016"), 1650);
    }

    @Test
    public void testGen01652() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.07.2016"), 1651);
    }

    @Test
    public void testGen01653() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.07.2016"), 1652);
    }

    @Test
    public void testGen01654() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.07.2016"), 1653);
    }

    @Test
    public void testGen01655() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.07.2016"), 1654);
    }

    @Test
    public void testGen01656() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.07.2016"), 1655);
    }

    @Test
    public void testGen01657() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.07.2016"), 1656);
    }

    @Test
    public void testGen01658() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.07.2016"), 1657);
    }

    @Test
    public void testGen01659() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.07.2016"), 1658);
    }

    @Test
    public void testGen01660() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.07.2016"), 1659);
    }

    @Test
    public void testGen01661() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.07.2016"), 1660);
    }

    @Test
    public void testGen01662() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.07.2016"), 1661);
    }

    @Test
    public void testGen01663() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.07.2016"), 1662);
    }

    @Test
    public void testGen01664() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.07.2016"), 1663);
    }

    @Test
    public void testGen01665() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.07.2016"), 1664);
    }

    @Test
    public void testGen01666() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.07.2016"), 1665);
    }

    @Test
    public void testGen01667() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.07.2016"), 1666);
    }

    @Test
    public void testGen01668() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.07.2016"), 1667);
    }

    @Test
    public void testGen01669() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.07.2016"), 1668);
    }

    @Test
    public void testGen01670() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.07.2016"), 1669);
    }

    @Test
    public void testGen01671() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.07.2016"), 1670);
    }

    @Test
    public void testGen01672() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.07.2016"), 1671);
    }

    @Test
    public void testGen01673() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.07.2016"), 1672);
    }

    @Test
    public void testGen01674() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.07.2016"), 1673);
    }

    @Test
    public void testGen01675() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.08.2016"), 1674);
    }

    @Test
    public void testGen01676() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.08.2016"), 1675);
    }

    @Test
    public void testGen01677() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.08.2016"), 1676);
    }

    @Test
    public void testGen01678() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.08.2016"), 1677);
    }

    @Test
    public void testGen01679() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.08.2016"), 1678);
    }

    @Test
    public void testGen01680() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.08.2016"), 1679);
    }

    @Test
    public void testGen01681() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.08.2016"), 1680);
    }

    @Test
    public void testGen01682() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.08.2016"), 1681);
    }

    @Test
    public void testGen01683() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.08.2016"), 1682);
    }

    @Test
    public void testGen01684() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.08.2016"), 1683);
    }

    @Test
    public void testGen01685() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.08.2016"), 1684);
    }

    @Test
    public void testGen01686() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.08.2016"), 1685);
    }

    @Test
    public void testGen01687() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.08.2016"), 1686);
    }

    @Test
    public void testGen01688() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.08.2016"), 1687);
    }

    @Test
    public void testGen01689() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.08.2016"), 1688);
    }

    @Test
    public void testGen01690() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.08.2016"), 1689);
    }

    @Test
    public void testGen01691() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.08.2016"), 1690);
    }

    @Test
    public void testGen01692() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.08.2016"), 1691);
    }

    @Test
    public void testGen01693() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.08.2016"), 1692);
    }

    @Test
    public void testGen01694() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.08.2016"), 1693);
    }

    @Test
    public void testGen01695() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.08.2016"), 1694);
    }

    @Test
    public void testGen01696() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.08.2016"), 1695);
    }

    @Test
    public void testGen01697() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.08.2016"), 1696);
    }

    @Test
    public void testGen01698() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.08.2016"), 1697);
    }

    @Test
    public void testGen01699() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.08.2016"), 1698);
    }

    @Test
    public void testGen01700() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.08.2016"), 1699);
    }

    @Test
    public void testGen01701() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.08.2016"), 1700);
    }

    @Test
    public void testGen01702() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.08.2016"), 1701);
    }

    @Test
    public void testGen01703() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.08.2016"), 1702);
    }

    @Test
    public void testGen01704() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.08.2016"), 1703);
    }

    @Test
    public void testGen01705() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.08.2016"), 1704);
    }

    @Test
    public void testGen01706() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.09.2016"), 1705);
    }

    @Test
    public void testGen01707() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.09.2016"), 1706);
    }

    @Test
    public void testGen01708() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.09.2016"), 1707);
    }

    @Test
    public void testGen01709() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.09.2016"), 1708);
    }

    @Test
    public void testGen01710() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.09.2016"), 1709);
    }

    @Test
    public void testGen01711() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.09.2016"), 1710);
    }

    @Test
    public void testGen01712() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.09.2016"), 1711);
    }

    @Test
    public void testGen01713() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.09.2016"), 1712);
    }

    @Test
    public void testGen01714() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.09.2016"), 1713);
    }

    @Test
    public void testGen01715() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.09.2016"), 1714);
    }

    @Test
    public void testGen01716() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.09.2016"), 1715);
    }

    @Test
    public void testGen01717() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.09.2016"), 1716);
    }

    @Test
    public void testGen01718() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.09.2016"), 1717);
    }

    @Test
    public void testGen01719() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.09.2016"), 1718);
    }

    @Test
    public void testGen01720() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.09.2016"), 1719);
    }

    @Test
    public void testGen01721() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.09.2016"), 1720);
    }

    @Test
    public void testGen01722() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.09.2016"), 1721);
    }

    @Test
    public void testGen01723() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.09.2016"), 1722);
    }

    @Test
    public void testGen01724() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.09.2016"), 1723);
    }

    @Test
    public void testGen01725() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.09.2016"), 1724);
    }

    @Test
    public void testGen01726() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.09.2016"), 1725);
    }

    @Test
    public void testGen01727() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.09.2016"), 1726);
    }

    @Test
    public void testGen01728() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.09.2016"), 1727);
    }

    @Test
    public void testGen01729() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.09.2016"), 1728);
    }

    @Test
    public void testGen01730() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.09.2016"), 1729);
    }

    @Test
    public void testGen01731() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.09.2016"), 1730);
    }

    @Test
    public void testGen01732() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.09.2016"), 1731);
    }

    @Test
    public void testGen01733() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.09.2016"), 1732);
    }

    @Test
    public void testGen01734() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.09.2016"), 1733);
    }

    @Test
    public void testGen01735() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.09.2016"), 1734);
    }

    @Test
    public void testGen01736() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.10.2016"), 1735);
    }

    @Test
    public void testGen01737() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.10.2016"), 1736);
    }

    @Test
    public void testGen01738() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.10.2016"), 1737);
    }

    @Test
    public void testGen01739() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.10.2016"), 1738);
    }

    @Test
    public void testGen01740() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.10.2016"), 1739);
    }

    @Test
    public void testGen01741() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.10.2016"), 1740);
    }

    @Test
    public void testGen01742() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.10.2016"), 1741);
    }

    @Test
    public void testGen01743() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.10.2016"), 1742);
    }

    @Test
    public void testGen01744() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.10.2016"), 1743);
    }

    @Test
    public void testGen01745() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.10.2016"), 1744);
    }

    @Test
    public void testGen01746() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.10.2016"), 1745);
    }

    @Test
    public void testGen01747() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.10.2016"), 1746);
    }

    @Test
    public void testGen01748() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.10.2016"), 1747);
    }

    @Test
    public void testGen01749() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.10.2016"), 1748);
    }

    @Test
    public void testGen01750() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.10.2016"), 1749);
    }

    @Test
    public void testGen01751() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.10.2016"), 1750);
    }

    @Test
    public void testGen01752() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.10.2016"), 1751);
    }

    @Test
    public void testGen01753() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.10.2016"), 1752);
    }

    @Test
    public void testGen01754() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.10.2016"), 1753);
    }

    @Test
    public void testGen01755() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.10.2016"), 1754);
    }

    @Test
    public void testGen01756() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.10.2016"), 1755);
    }

    @Test
    public void testGen01757() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.10.2016"), 1756);
    }

    @Test
    public void testGen01758() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.10.2016"), 1757);
    }

    @Test
    public void testGen01759() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.10.2016"), 1758);
    }

    @Test
    public void testGen01760() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.10.2016"), 1759);
    }

    @Test
    public void testGen01761() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.10.2016"), 1760);
    }

    @Test
    public void testGen01762() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.10.2016"), 1761);
    }

    @Test
    public void testGen01763() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.10.2016"), 1762);
    }

    @Test
    public void testGen01764() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.10.2016"), 1763);
    }

    @Test
    public void testGen01765() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.10.2016"), 1764);
    }

    @Test
    public void testGen01766() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.10.2016"), 1765);
    }

    @Test
    public void testGen01767() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.11.2016"), 1766);
    }

    @Test
    public void testGen01768() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.11.2016"), 1767);
    }

    @Test
    public void testGen01769() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.11.2016"), 1768);
    }

    @Test
    public void testGen01770() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.11.2016"), 1769);
    }

    @Test
    public void testGen01771() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.11.2016"), 1770);
    }

    @Test
    public void testGen01772() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.11.2016"), 1771);
    }

    @Test
    public void testGen01773() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.11.2016"), 1772);
    }

    @Test
    public void testGen01774() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.11.2016"), 1773);
    }

    @Test
    public void testGen01775() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.11.2016"), 1774);
    }

    @Test
    public void testGen01776() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.11.2016"), 1775);
    }

    @Test
    public void testGen01777() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.11.2016"), 1776);
    }

    @Test
    public void testGen01778() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.11.2016"), 1777);
    }

    @Test
    public void testGen01779() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.11.2016"), 1778);
    }

    @Test
    public void testGen01780() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.11.2016"), 1779);
    }

    @Test
    public void testGen01781() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.11.2016"), 1780);
    }

    @Test
    public void testGen01782() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.11.2016"), 1781);
    }

    @Test
    public void testGen01783() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.11.2016"), 1782);
    }

    @Test
    public void testGen01784() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.11.2016"), 1783);
    }

    @Test
    public void testGen01785() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.11.2016"), 1784);
    }

    @Test
    public void testGen01786() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.11.2016"), 1785);
    }

    @Test
    public void testGen01787() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.11.2016"), 1786);
    }

    @Test
    public void testGen01788() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.11.2016"), 1787);
    }

    @Test
    public void testGen01789() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.11.2016"), 1788);
    }

    @Test
    public void testGen01790() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.11.2016"), 1789);
    }

    @Test
    public void testGen01791() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.11.2016"), 1790);
    }

    @Test
    public void testGen01792() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.11.2016"), 1791);
    }

    @Test
    public void testGen01793() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.11.2016"), 1792);
    }

    @Test
    public void testGen01794() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.11.2016"), 1793);
    }

    @Test
    public void testGen01795() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.11.2016"), 1794);
    }

    @Test
    public void testGen01796() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.11.2016"), 1795);
    }

    @Test
    public void testGen01797() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.12.2016"), 1796);
    }

    @Test
    public void testGen01798() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.12.2016"), 1797);
    }

    @Test
    public void testGen01799() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.12.2016"), 1798);
    }

    @Test
    public void testGen01800() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.12.2016"), 1799);
    }

    @Test
    public void testGen01801() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.12.2016"), 1800);
    }

    @Test
    public void testGen01802() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.12.2016"), 1801);
    }

    @Test
    public void testGen01803() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.12.2016"), 1802);
    }

    @Test
    public void testGen01804() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.12.2016"), 1803);
    }

    @Test
    public void testGen01805() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.12.2016"), 1804);
    }

    @Test
    public void testGen01806() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.12.2016"), 1805);
    }

    @Test
    public void testGen01807() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.12.2016"), 1806);
    }

    @Test
    public void testGen01808() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.12.2016"), 1807);
    }

    @Test
    public void testGen01809() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.12.2016"), 1808);
    }

    @Test
    public void testGen01810() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.12.2016"), 1809);
    }

    @Test
    public void testGen01811() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.12.2016"), 1810);
    }

    @Test
    public void testGen01812() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.12.2016"), 1811);
    }

    @Test
    public void testGen01813() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.12.2016"), 1812);
    }

    @Test
    public void testGen01814() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.12.2016"), 1813);
    }

    @Test
    public void testGen01815() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.12.2016"), 1814);
    }

    @Test
    public void testGen01816() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.12.2016"), 1815);
    }

    @Test
    public void testGen01817() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.12.2016"), 1816);
    }

    @Test
    public void testGen01818() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.12.2016"), 1817);
    }

    @Test
    public void testGen01819() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.12.2016"), 1818);
    }

    @Test
    public void testGen01820() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.12.2016"), 1819);
    }

    @Test
    public void testGen01821() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.12.2016"), 1820);
    }

    @Test
    public void testGen01822() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.12.2016"), 1821);
    }

    @Test
    public void testGen01823() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.12.2016"), 1822);
    }

    @Test
    public void testGen01824() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.12.2016"), 1823);
    }

    @Test
    public void testGen01825() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.12.2016"), 1824);
    }

    @Test
    public void testGen01826() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.12.2016"), 1825);
    }

    @Test
    public void testGen01827() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.12.2016"), 1826);
    }

    @Test
    public void testGen01828() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.01.2017"), 1827);
    }

    @Test
    public void testGen01829() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.01.2017"), 1828);
    }

    @Test
    public void testGen01830() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.01.2017"), 1829);
    }

    @Test
    public void testGen01831() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.01.2017"), 1830);
    }

    @Test
    public void testGen01832() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.01.2017"), 1831);
    }

    @Test
    public void testGen01833() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.01.2017"), 1832);
    }

    @Test
    public void testGen01834() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.01.2017"), 1833);
    }

    @Test
    public void testGen01835() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.01.2017"), 1834);
    }

    @Test
    public void testGen01836() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.01.2017"), 1835);
    }

    @Test
    public void testGen01837() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.01.2017"), 1836);
    }

    @Test
    public void testGen01838() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.01.2017"), 1837);
    }

    @Test
    public void testGen01839() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.01.2017"), 1838);
    }

    @Test
    public void testGen01840() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.01.2017"), 1839);
    }

    @Test
    public void testGen01841() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.01.2017"), 1840);
    }

    @Test
    public void testGen01842() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.01.2017"), 1841);
    }

    @Test
    public void testGen01843() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.01.2017"), 1842);
    }

    @Test
    public void testGen01844() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.01.2017"), 1843);
    }

    @Test
    public void testGen01845() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.01.2017"), 1844);
    }

    @Test
    public void testGen01846() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.01.2017"), 1845);
    }

    @Test
    public void testGen01847() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.01.2017"), 1846);
    }

    @Test
    public void testGen01848() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.01.2017"), 1847);
    }

    @Test
    public void testGen01849() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.01.2017"), 1848);
    }

    @Test
    public void testGen01850() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.01.2017"), 1849);
    }

    @Test
    public void testGen01851() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.01.2017"), 1850);
    }

    @Test
    public void testGen01852() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.01.2017"), 1851);
    }

    @Test
    public void testGen01853() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.01.2017"), 1852);
    }

    @Test
    public void testGen01854() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.01.2017"), 1853);
    }

    @Test
    public void testGen01855() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.01.2017"), 1854);
    }

    @Test
    public void testGen01856() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.01.2017"), 1855);
    }

    @Test
    public void testGen01857() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.01.2017"), 1856);
    }

    @Test
    public void testGen01858() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.01.2017"), 1857);
    }

    @Test
    public void testGen01859() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.02.2017"), 1858);
    }

    @Test
    public void testGen01860() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.02.2017"), 1859);
    }

    @Test
    public void testGen01861() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.02.2017"), 1860);
    }

    @Test
    public void testGen01862() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.02.2017"), 1861);
    }

    @Test
    public void testGen01863() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.02.2017"), 1862);
    }

    @Test
    public void testGen01864() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.02.2017"), 1863);
    }

    @Test
    public void testGen01865() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.02.2017"), 1864);
    }

    @Test
    public void testGen01866() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.02.2017"), 1865);
    }

    @Test
    public void testGen01867() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.02.2017"), 1866);
    }

    @Test
    public void testGen01868() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.02.2017"), 1867);
    }

    @Test
    public void testGen01869() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.02.2017"), 1868);
    }

    @Test
    public void testGen01870() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.02.2017"), 1869);
    }

    @Test
    public void testGen01871() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.02.2017"), 1870);
    }

    @Test
    public void testGen01872() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.02.2017"), 1871);
    }

    @Test
    public void testGen01873() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.02.2017"), 1872);
    }

    @Test
    public void testGen01874() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.02.2017"), 1873);
    }

    @Test
    public void testGen01875() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.02.2017"), 1874);
    }

    @Test
    public void testGen01876() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.02.2017"), 1875);
    }

    @Test
    public void testGen01877() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.02.2017"), 1876);
    }

    @Test
    public void testGen01878() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.02.2017"), 1877);
    }

    @Test
    public void testGen01879() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.02.2017"), 1878);
    }

    @Test
    public void testGen01880() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.02.2017"), 1879);
    }

    @Test
    public void testGen01881() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.02.2017"), 1880);
    }

    @Test
    public void testGen01882() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.02.2017"), 1881);
    }

    @Test
    public void testGen01883() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.02.2017"), 1882);
    }

    @Test
    public void testGen01884() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.02.2017"), 1883);
    }

    @Test
    public void testGen01885() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.02.2017"), 1884);
    }

    @Test
    public void testGen01886() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.02.2017"), 1885);
    }

    @Test
    public void testGen01887() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.03.2017"), 1886);
    }

    @Test
    public void testGen01888() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.03.2017"), 1887);
    }

    @Test
    public void testGen01889() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.03.2017"), 1888);
    }

    @Test
    public void testGen01890() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.03.2017"), 1889);
    }

    @Test
    public void testGen01891() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.03.2017"), 1890);
    }

    @Test
    public void testGen01892() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.03.2017"), 1891);
    }

    @Test
    public void testGen01893() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.03.2017"), 1892);
    }

    @Test
    public void testGen01894() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.03.2017"), 1893);
    }

    @Test
    public void testGen01895() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.03.2017"), 1894);
    }

    @Test
    public void testGen01896() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.03.2017"), 1895);
    }

    @Test
    public void testGen01897() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.03.2017"), 1896);
    }

    @Test
    public void testGen01898() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.03.2017"), 1897);
    }

    @Test
    public void testGen01899() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.03.2017"), 1898);
    }

    @Test
    public void testGen01900() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.03.2017"), 1899);
    }

    @Test
    public void testGen01901() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.03.2017"), 1900);
    }

    @Test
    public void testGen01902() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.03.2017"), 1901);
    }

    @Test
    public void testGen01903() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.03.2017"), 1902);
    }

    @Test
    public void testGen01904() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.03.2017"), 1903);
    }

    @Test
    public void testGen01905() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.03.2017"), 1904);
    }

    @Test
    public void testGen01906() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.03.2017"), 1905);
    }

    @Test
    public void testGen01907() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.03.2017"), 1906);
    }

    @Test
    public void testGen01908() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.03.2017"), 1907);
    }

    @Test
    public void testGen01909() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.03.2017"), 1908);
    }

    @Test
    public void testGen01910() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.03.2017"), 1909);
    }

    @Test
    public void testGen01911() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.03.2017"), 1910);
    }

    @Test
    public void testGen01912() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.03.2017"), 1911);
    }

    @Test
    public void testGen01913() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.03.2017"), 1912);
    }

    @Test
    public void testGen01914() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.03.2017"), 1913);
    }

    @Test
    public void testGen01915() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.03.2017"), 1914);
    }

    @Test
    public void testGen01916() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.03.2017"), 1915);
    }

    @Test
    public void testGen01917() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.03.2017"), 1916);
    }

    @Test
    public void testGen01918() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.04.2017"), 1917);
    }

    @Test
    public void testGen01919() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.04.2017"), 1918);
    }

    @Test
    public void testGen01920() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.04.2017"), 1919);
    }

    @Test
    public void testGen01921() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.04.2017"), 1920);
    }

    @Test
    public void testGen01922() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.04.2017"), 1921);
    }

    @Test
    public void testGen01923() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.04.2017"), 1922);
    }

    @Test
    public void testGen01924() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.04.2017"), 1923);
    }

    @Test
    public void testGen01925() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.04.2017"), 1924);
    }

    @Test
    public void testGen01926() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.04.2017"), 1925);
    }

    @Test
    public void testGen01927() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.04.2017"), 1926);
    }

    @Test
    public void testGen01928() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.04.2017"), 1927);
    }

    @Test
    public void testGen01929() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.04.2017"), 1928);
    }

    @Test
    public void testGen01930() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.04.2017"), 1929);
    }

    @Test
    public void testGen01931() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.04.2017"), 1930);
    }

    @Test
    public void testGen01932() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.04.2017"), 1931);
    }

    @Test
    public void testGen01933() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.04.2017"), 1932);
    }

    @Test
    public void testGen01934() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.04.2017"), 1933);
    }

    @Test
    public void testGen01935() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.04.2017"), 1934);
    }

    @Test
    public void testGen01936() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.04.2017"), 1935);
    }

    @Test
    public void testGen01937() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.04.2017"), 1936);
    }

    @Test
    public void testGen01938() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.04.2017"), 1937);
    }

    @Test
    public void testGen01939() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.04.2017"), 1938);
    }

    @Test
    public void testGen01940() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.04.2017"), 1939);
    }

    @Test
    public void testGen01941() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.04.2017"), 1940);
    }

    @Test
    public void testGen01942() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.04.2017"), 1941);
    }

    @Test
    public void testGen01943() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.04.2017"), 1942);
    }

    @Test
    public void testGen01944() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.04.2017"), 1943);
    }

    @Test
    public void testGen01945() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.04.2017"), 1944);
    }

    @Test
    public void testGen01946() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.04.2017"), 1945);
    }

    @Test
    public void testGen01947() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.04.2017"), 1946);
    }

    @Test
    public void testGen01948() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.05.2017"), 1947);
    }

    @Test
    public void testGen01949() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.05.2017"), 1948);
    }

    @Test
    public void testGen01950() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.05.2017"), 1949);
    }

    @Test
    public void testGen01951() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.05.2017"), 1950);
    }

    @Test
    public void testGen01952() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.05.2017"), 1951);
    }

    @Test
    public void testGen01953() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.05.2017"), 1952);
    }

    @Test
    public void testGen01954() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.05.2017"), 1953);
    }

    @Test
    public void testGen01955() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.05.2017"), 1954);
    }

    @Test
    public void testGen01956() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.05.2017"), 1955);
    }

    @Test
    public void testGen01957() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.05.2017"), 1956);
    }

    @Test
    public void testGen01958() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.05.2017"), 1957);
    }

    @Test
    public void testGen01959() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.05.2017"), 1958);
    }

    @Test
    public void testGen01960() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.05.2017"), 1959);
    }

    @Test
    public void testGen01961() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.05.2017"), 1960);
    }

    @Test
    public void testGen01962() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.05.2017"), 1961);
    }

    @Test
    public void testGen01963() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.05.2017"), 1962);
    }

    @Test
    public void testGen01964() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.05.2017"), 1963);
    }

    @Test
    public void testGen01965() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.05.2017"), 1964);
    }

    @Test
    public void testGen01966() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.05.2017"), 1965);
    }

    @Test
    public void testGen01967() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.05.2017"), 1966);
    }

    @Test
    public void testGen01968() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.05.2017"), 1967);
    }

    @Test
    public void testGen01969() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.05.2017"), 1968);
    }

    @Test
    public void testGen01970() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.05.2017"), 1969);
    }

    @Test
    public void testGen01971() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.05.2017"), 1970);
    }

    @Test
    public void testGen01972() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.05.2017"), 1971);
    }

    @Test
    public void testGen01973() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.05.2017"), 1972);
    }

    @Test
    public void testGen01974() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.05.2017"), 1973);
    }

    @Test
    public void testGen01975() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.05.2017"), 1974);
    }

    @Test
    public void testGen01976() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.05.2017"), 1975);
    }

    @Test
    public void testGen01977() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.05.2017"), 1976);
    }

    @Test
    public void testGen01978() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.05.2017"), 1977);
    }

    @Test
    public void testGen01979() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.06.2017"), 1978);
    }

    @Test
    public void testGen01980() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.06.2017"), 1979);
    }

    @Test
    public void testGen01981() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.06.2017"), 1980);
    }

    @Test
    public void testGen01982() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.06.2017"), 1981);
    }

    @Test
    public void testGen01983() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.06.2017"), 1982);
    }

    @Test
    public void testGen01984() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.06.2017"), 1983);
    }

    @Test
    public void testGen01985() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.06.2017"), 1984);
    }

    @Test
    public void testGen01986() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.06.2017"), 1985);
    }

    @Test
    public void testGen01987() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.06.2017"), 1986);
    }

    @Test
    public void testGen01988() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.06.2017"), 1987);
    }

    @Test
    public void testGen01989() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.06.2017"), 1988);
    }

    @Test
    public void testGen01990() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.06.2017"), 1989);
    }

    @Test
    public void testGen01991() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.06.2017"), 1990);
    }

    @Test
    public void testGen01992() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.06.2017"), 1991);
    }

    @Test
    public void testGen01993() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.06.2017"), 1992);
    }

    @Test
    public void testGen01994() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.06.2017"), 1993);
    }

    @Test
    public void testGen01995() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.06.2017"), 1994);
    }

    @Test
    public void testGen01996() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.06.2017"), 1995);
    }

    @Test
    public void testGen01997() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.06.2017"), 1996);
    }

    @Test
    public void testGen01998() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.06.2017"), 1997);
    }

    @Test
    public void testGen01999() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.06.2017"), 1998);
    }

    @Test
    public void testGen02000() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.06.2017"), 1999);
    }

    @Test
    public void testGen02001() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.06.2017"), 2000);
    }

    @Test
    public void testGen02002() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.06.2017"), 2001);
    }

    @Test
    public void testGen02003() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.06.2017"), 2002);
    }

    @Test
    public void testGen02004() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.06.2017"), 2003);
    }

    @Test
    public void testGen02005() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.06.2017"), 2004);
    }

    @Test
    public void testGen02006() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.06.2017"), 2005);
    }

    @Test
    public void testGen02007() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.06.2017"), 2006);
    }

    @Test
    public void testGen02008() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.06.2017"), 2007);
    }

    @Test
    public void testGen02009() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.07.2017"), 2008);
    }

    @Test
    public void testGen02010() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.07.2017"), 2009);
    }

    @Test
    public void testGen02011() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.07.2017"), 2010);
    }

    @Test
    public void testGen02012() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.07.2017"), 2011);
    }

    @Test
    public void testGen02013() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.07.2017"), 2012);
    }

    @Test
    public void testGen02014() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.07.2017"), 2013);
    }

    @Test
    public void testGen02015() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.07.2017"), 2014);
    }

    @Test
    public void testGen02016() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.07.2017"), 2015);
    }

    @Test
    public void testGen02017() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.07.2017"), 2016);
    }

    @Test
    public void testGen02018() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.07.2017"), 2017);
    }

    @Test
    public void testGen02019() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.07.2017"), 2018);
    }

    @Test
    public void testGen02020() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.07.2017"), 2019);
    }

    @Test
    public void testGen02021() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.07.2017"), 2020);
    }

    @Test
    public void testGen02022() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.07.2017"), 2021);
    }

    @Test
    public void testGen02023() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.07.2017"), 2022);
    }

    @Test
    public void testGen02024() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.07.2017"), 2023);
    }

    @Test
    public void testGen02025() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.07.2017"), 2024);
    }

    @Test
    public void testGen02026() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.07.2017"), 2025);
    }

    @Test
    public void testGen02027() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.07.2017"), 2026);
    }

    @Test
    public void testGen02028() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.07.2017"), 2027);
    }

    @Test
    public void testGen02029() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.07.2017"), 2028);
    }

    @Test
    public void testGen02030() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.07.2017"), 2029);
    }

    @Test
    public void testGen02031() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.07.2017"), 2030);
    }

    @Test
    public void testGen02032() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.07.2017"), 2031);
    }

    @Test
    public void testGen02033() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.07.2017"), 2032);
    }

    @Test
    public void testGen02034() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.07.2017"), 2033);
    }

    @Test
    public void testGen02035() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.07.2017"), 2034);
    }

    @Test
    public void testGen02036() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.07.2017"), 2035);
    }

    @Test
    public void testGen02037() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.07.2017"), 2036);
    }

    @Test
    public void testGen02038() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.07.2017"), 2037);
    }

    @Test
    public void testGen02039() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.07.2017"), 2038);
    }

    @Test
    public void testGen02040() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.08.2017"), 2039);
    }

    @Test
    public void testGen02041() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.08.2017"), 2040);
    }

    @Test
    public void testGen02042() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.08.2017"), 2041);
    }

    @Test
    public void testGen02043() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.08.2017"), 2042);
    }

    @Test
    public void testGen02044() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.08.2017"), 2043);
    }

    @Test
    public void testGen02045() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.08.2017"), 2044);
    }

    @Test
    public void testGen02046() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.08.2017"), 2045);
    }

    @Test
    public void testGen02047() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.08.2017"), 2046);
    }

    @Test
    public void testGen02048() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.08.2017"), 2047);
    }

    @Test
    public void testGen02049() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.08.2017"), 2048);
    }

    @Test
    public void testGen02050() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.08.2017"), 2049);
    }

    @Test
    public void testGen02051() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.08.2017"), 2050);
    }

    @Test
    public void testGen02052() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.08.2017"), 2051);
    }

    @Test
    public void testGen02053() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.08.2017"), 2052);
    }

    @Test
    public void testGen02054() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.08.2017"), 2053);
    }

    @Test
    public void testGen02055() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.08.2017"), 2054);
    }

    @Test
    public void testGen02056() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.08.2017"), 2055);
    }

    @Test
    public void testGen02057() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.08.2017"), 2056);
    }

    @Test
    public void testGen02058() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.08.2017"), 2057);
    }

    @Test
    public void testGen02059() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.08.2017"), 2058);
    }

    @Test
    public void testGen02060() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.08.2017"), 2059);
    }

    @Test
    public void testGen02061() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.08.2017"), 2060);
    }

    @Test
    public void testGen02062() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.08.2017"), 2061);
    }

    @Test
    public void testGen02063() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.08.2017"), 2062);
    }

    @Test
    public void testGen02064() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.08.2017"), 2063);
    }

    @Test
    public void testGen02065() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.08.2017"), 2064);
    }

    @Test
    public void testGen02066() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.08.2017"), 2065);
    }

    @Test
    public void testGen02067() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.08.2017"), 2066);
    }

    @Test
    public void testGen02068() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.08.2017"), 2067);
    }

    @Test
    public void testGen02069() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.08.2017"), 2068);
    }

    @Test
    public void testGen02070() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.08.2017"), 2069);
    }

    @Test
    public void testGen02071() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.09.2017"), 2070);
    }

    @Test
    public void testGen02072() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.09.2017"), 2071);
    }

    @Test
    public void testGen02073() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.09.2017"), 2072);
    }

    @Test
    public void testGen02074() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.09.2017"), 2073);
    }

    @Test
    public void testGen02075() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.09.2017"), 2074);
    }

    @Test
    public void testGen02076() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.09.2017"), 2075);
    }

    @Test
    public void testGen02077() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.09.2017"), 2076);
    }

    @Test
    public void testGen02078() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.09.2017"), 2077);
    }

    @Test
    public void testGen02079() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.09.2017"), 2078);
    }

    @Test
    public void testGen02080() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.09.2017"), 2079);
    }

    @Test
    public void testGen02081() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.09.2017"), 2080);
    }

    @Test
    public void testGen02082() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.09.2017"), 2081);
    }

    @Test
    public void testGen02083() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.09.2017"), 2082);
    }

    @Test
    public void testGen02084() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.09.2017"), 2083);
    }

    @Test
    public void testGen02085() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.09.2017"), 2084);
    }

    @Test
    public void testGen02086() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.09.2017"), 2085);
    }

    @Test
    public void testGen02087() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.09.2017"), 2086);
    }

    @Test
    public void testGen02088() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.09.2017"), 2087);
    }

    @Test
    public void testGen02089() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.09.2017"), 2088);
    }

    @Test
    public void testGen02090() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.09.2017"), 2089);
    }

    @Test
    public void testGen02091() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.09.2017"), 2090);
    }

    @Test
    public void testGen02092() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.09.2017"), 2091);
    }

    @Test
    public void testGen02093() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.09.2017"), 2092);
    }

    @Test
    public void testGen02094() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.09.2017"), 2093);
    }

    @Test
    public void testGen02095() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.09.2017"), 2094);
    }

    @Test
    public void testGen02096() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.09.2017"), 2095);
    }

    @Test
    public void testGen02097() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.09.2017"), 2096);
    }

    @Test
    public void testGen02098() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.09.2017"), 2097);
    }

    @Test
    public void testGen02099() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.09.2017"), 2098);
    }

    @Test
    public void testGen02100() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.09.2017"), 2099);
    }

    @Test
    public void testGen02101() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.10.2017"), 2100);
    }

    @Test
    public void testGen02102() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.10.2017"), 2101);
    }

    @Test
    public void testGen02103() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.10.2017"), 2102);
    }

    @Test
    public void testGen02104() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.10.2017"), 2103);
    }

    @Test
    public void testGen02105() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.10.2017"), 2104);
    }

    @Test
    public void testGen02106() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.10.2017"), 2105);
    }

    @Test
    public void testGen02107() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.10.2017"), 2106);
    }

    @Test
    public void testGen02108() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.10.2017"), 2107);
    }

    @Test
    public void testGen02109() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.10.2017"), 2108);
    }

    @Test
    public void testGen02110() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.10.2017"), 2109);
    }

    @Test
    public void testGen02111() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.10.2017"), 2110);
    }

    @Test
    public void testGen02112() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.10.2017"), 2111);
    }

    @Test
    public void testGen02113() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.10.2017"), 2112);
    }

    @Test
    public void testGen02114() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.10.2017"), 2113);
    }

    @Test
    public void testGen02115() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.10.2017"), 2114);
    }

    @Test
    public void testGen02116() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.10.2017"), 2115);
    }

    @Test
    public void testGen02117() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.10.2017"), 2116);
    }

    @Test
    public void testGen02118() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.10.2017"), 2117);
    }

    @Test
    public void testGen02119() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.10.2017"), 2118);
    }

    @Test
    public void testGen02120() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.10.2017"), 2119);
    }

    @Test
    public void testGen02121() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.10.2017"), 2120);
    }

    @Test
    public void testGen02122() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.10.2017"), 2121);
    }

    @Test
    public void testGen02123() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.10.2017"), 2122);
    }

    @Test
    public void testGen02124() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.10.2017"), 2123);
    }

    @Test
    public void testGen02125() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.10.2017"), 2124);
    }

    @Test
    public void testGen02126() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.10.2017"), 2125);
    }

    @Test
    public void testGen02127() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.10.2017"), 2126);
    }

    @Test
    public void testGen02128() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.10.2017"), 2127);
    }

    @Test
    public void testGen02129() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.10.2017"), 2128);
    }

    @Test
    public void testGen02130() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.10.2017"), 2129);
    }

    @Test
    public void testGen02131() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.10.2017"), 2130);
    }

    @Test
    public void testGen02132() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.11.2017"), 2131);
    }

    @Test
    public void testGen02133() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.11.2017"), 2132);
    }

    @Test
    public void testGen02134() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.11.2017"), 2133);
    }

    @Test
    public void testGen02135() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.11.2017"), 2134);
    }

    @Test
    public void testGen02136() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.11.2017"), 2135);
    }

    @Test
    public void testGen02137() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.11.2017"), 2136);
    }

    @Test
    public void testGen02138() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.11.2017"), 2137);
    }

    @Test
    public void testGen02139() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.11.2017"), 2138);
    }

    @Test
    public void testGen02140() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.11.2017"), 2139);
    }

    @Test
    public void testGen02141() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.11.2017"), 2140);
    }

    @Test
    public void testGen02142() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.11.2017"), 2141);
    }

    @Test
    public void testGen02143() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.11.2017"), 2142);
    }

    @Test
    public void testGen02144() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.11.2017"), 2143);
    }

    @Test
    public void testGen02145() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.11.2017"), 2144);
    }

    @Test
    public void testGen02146() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.11.2017"), 2145);
    }

    @Test
    public void testGen02147() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.11.2017"), 2146);
    }

    @Test
    public void testGen02148() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.11.2017"), 2147);
    }

    @Test
    public void testGen02149() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.11.2017"), 2148);
    }

    @Test
    public void testGen02150() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.11.2017"), 2149);
    }

    @Test
    public void testGen02151() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.11.2017"), 2150);
    }

    @Test
    public void testGen02152() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.11.2017"), 2151);
    }

    @Test
    public void testGen02153() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.11.2017"), 2152);
    }

    @Test
    public void testGen02154() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.11.2017"), 2153);
    }

    @Test
    public void testGen02155() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.11.2017"), 2154);
    }

    @Test
    public void testGen02156() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.11.2017"), 2155);
    }

    @Test
    public void testGen02157() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.11.2017"), 2156);
    }

    @Test
    public void testGen02158() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.11.2017"), 2157);
    }

    @Test
    public void testGen02159() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.11.2017"), 2158);
    }

    @Test
    public void testGen02160() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.11.2017"), 2159);
    }

    @Test
    public void testGen02161() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.11.2017"), 2160);
    }

    @Test
    public void testGen02162() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.12.2017"), 2161);
    }

    @Test
    public void testGen02163() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.12.2017"), 2162);
    }

    @Test
    public void testGen02164() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.12.2017"), 2163);
    }

    @Test
    public void testGen02165() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.12.2017"), 2164);
    }

    @Test
    public void testGen02166() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.12.2017"), 2165);
    }

    @Test
    public void testGen02167() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.12.2017"), 2166);
    }

    @Test
    public void testGen02168() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.12.2017"), 2167);
    }

    @Test
    public void testGen02169() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.12.2017"), 2168);
    }

    @Test
    public void testGen02170() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.12.2017"), 2169);
    }

    @Test
    public void testGen02171() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.12.2017"), 2170);
    }

    @Test
    public void testGen02172() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.12.2017"), 2171);
    }

    @Test
    public void testGen02173() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.12.2017"), 2172);
    }

    @Test
    public void testGen02174() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.12.2017"), 2173);
    }

    @Test
    public void testGen02175() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.12.2017"), 2174);
    }

    @Test
    public void testGen02176() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.12.2017"), 2175);
    }

    @Test
    public void testGen02177() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.12.2017"), 2176);
    }

    @Test
    public void testGen02178() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.12.2017"), 2177);
    }

    @Test
    public void testGen02179() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.12.2017"), 2178);
    }

    @Test
    public void testGen02180() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.12.2017"), 2179);
    }

    @Test
    public void testGen02181() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.12.2017"), 2180);
    }

    @Test
    public void testGen02182() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.12.2017"), 2181);
    }

    @Test
    public void testGen02183() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.12.2017"), 2182);
    }

    @Test
    public void testGen02184() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.12.2017"), 2183);
    }

    @Test
    public void testGen02185() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.12.2017"), 2184);
    }

    @Test
    public void testGen02186() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.12.2017"), 2185);
    }

    @Test
    public void testGen02187() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.12.2017"), 2186);
    }

    @Test
    public void testGen02188() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.12.2017"), 2187);
    }

    @Test
    public void testGen02189() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.12.2017"), 2188);
    }

    @Test
    public void testGen02190() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.12.2017"), 2189);
    }

    @Test
    public void testGen02191() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.12.2017"), 2190);
    }

    @Test
    public void testGen02192() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.12.2017"), 2191);
    }

    @Test
    public void testGen02193() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.01.2018"), 2192);
    }

    @Test
    public void testGen02194() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.01.2018"), 2193);
    }

    @Test
    public void testGen02195() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.01.2018"), 2194);
    }

    @Test
    public void testGen02196() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.01.2018"), 2195);
    }

    @Test
    public void testGen02197() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.01.2018"), 2196);
    }

    @Test
    public void testGen02198() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.01.2018"), 2197);
    }

    @Test
    public void testGen02199() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.01.2018"), 2198);
    }

    @Test
    public void testGen02200() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.01.2018"), 2199);
    }

    @Test
    public void testGen02201() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.01.2018"), 2200);
    }

    @Test
    public void testGen02202() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.01.2018"), 2201);
    }

    @Test
    public void testGen02203() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.01.2018"), 2202);
    }

    @Test
    public void testGen02204() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.01.2018"), 2203);
    }

    @Test
    public void testGen02205() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.01.2018"), 2204);
    }

    @Test
    public void testGen02206() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.01.2018"), 2205);
    }

    @Test
    public void testGen02207() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.01.2018"), 2206);
    }

    @Test
    public void testGen02208() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.01.2018"), 2207);
    }

    @Test
    public void testGen02209() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.01.2018"), 2208);
    }

    @Test
    public void testGen02210() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.01.2018"), 2209);
    }

    @Test
    public void testGen02211() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.01.2018"), 2210);
    }

    @Test
    public void testGen02212() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.01.2018"), 2211);
    }

    @Test
    public void testGen02213() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.01.2018"), 2212);
    }

    @Test
    public void testGen02214() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.01.2018"), 2213);
    }

    @Test
    public void testGen02215() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.01.2018"), 2214);
    }

    @Test
    public void testGen02216() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.01.2018"), 2215);
    }

    @Test
    public void testGen02217() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.01.2018"), 2216);
    }

    @Test
    public void testGen02218() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.01.2018"), 2217);
    }

    @Test
    public void testGen02219() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.01.2018"), 2218);
    }

    @Test
    public void testGen02220() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.01.2018"), 2219);
    }

    @Test
    public void testGen02221() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.01.2018"), 2220);
    }

    @Test
    public void testGen02222() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.01.2018"), 2221);
    }

    @Test
    public void testGen02223() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.01.2018"), 2222);
    }

    @Test
    public void testGen02224() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.02.2018"), 2223);
    }

    @Test
    public void testGen02225() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.02.2018"), 2224);
    }

    @Test
    public void testGen02226() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.02.2018"), 2225);
    }

    @Test
    public void testGen02227() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.02.2018"), 2226);
    }

    @Test
    public void testGen02228() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.02.2018"), 2227);
    }

    @Test
    public void testGen02229() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.02.2018"), 2228);
    }

    @Test
    public void testGen02230() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.02.2018"), 2229);
    }

    @Test
    public void testGen02231() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.02.2018"), 2230);
    }

    @Test
    public void testGen02232() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.02.2018"), 2231);
    }

    @Test
    public void testGen02233() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.02.2018"), 2232);
    }

    @Test
    public void testGen02234() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.02.2018"), 2233);
    }

    @Test
    public void testGen02235() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.02.2018"), 2234);
    }

    @Test
    public void testGen02236() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.02.2018"), 2235);
    }

    @Test
    public void testGen02237() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.02.2018"), 2236);
    }

    @Test
    public void testGen02238() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.02.2018"), 2237);
    }

    @Test
    public void testGen02239() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.02.2018"), 2238);
    }

    @Test
    public void testGen02240() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.02.2018"), 2239);
    }

    @Test
    public void testGen02241() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.02.2018"), 2240);
    }

    @Test
    public void testGen02242() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.02.2018"), 2241);
    }

    @Test
    public void testGen02243() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.02.2018"), 2242);
    }

    @Test
    public void testGen02244() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.02.2018"), 2243);
    }

    @Test
    public void testGen02245() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.02.2018"), 2244);
    }

    @Test
    public void testGen02246() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.02.2018"), 2245);
    }

    @Test
    public void testGen02247() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.02.2018"), 2246);
    }

    @Test
    public void testGen02248() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.02.2018"), 2247);
    }

    @Test
    public void testGen02249() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.02.2018"), 2248);
    }

    @Test
    public void testGen02250() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.02.2018"), 2249);
    }

    @Test
    public void testGen02251() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.02.2018"), 2250);
    }

    @Test
    public void testGen02252() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.03.2018"), 2251);
    }

    @Test
    public void testGen02253() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.03.2018"), 2252);
    }

    @Test
    public void testGen02254() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.03.2018"), 2253);
    }

    @Test
    public void testGen02255() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.03.2018"), 2254);
    }

    @Test
    public void testGen02256() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.03.2018"), 2255);
    }

    @Test
    public void testGen02257() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.03.2018"), 2256);
    }

    @Test
    public void testGen02258() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.03.2018"), 2257);
    }

    @Test
    public void testGen02259() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.03.2018"), 2258);
    }

    @Test
    public void testGen02260() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.03.2018"), 2259);
    }

    @Test
    public void testGen02261() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.03.2018"), 2260);
    }

    @Test
    public void testGen02262() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.03.2018"), 2261);
    }

    @Test
    public void testGen02263() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.03.2018"), 2262);
    }

    @Test
    public void testGen02264() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.03.2018"), 2263);
    }

    @Test
    public void testGen02265() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.03.2018"), 2264);
    }

    @Test
    public void testGen02266() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.03.2018"), 2265);
    }

    @Test
    public void testGen02267() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.03.2018"), 2266);
    }

    @Test
    public void testGen02268() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.03.2018"), 2267);
    }

    @Test
    public void testGen02269() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.03.2018"), 2268);
    }

    @Test
    public void testGen02270() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.03.2018"), 2269);
    }

    @Test
    public void testGen02271() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.03.2018"), 2270);
    }

    @Test
    public void testGen02272() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.03.2018"), 2271);
    }

    @Test
    public void testGen02273() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.03.2018"), 2272);
    }

    @Test
    public void testGen02274() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.03.2018"), 2273);
    }

    @Test
    public void testGen02275() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.03.2018"), 2274);
    }

    @Test
    public void testGen02276() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.03.2018"), 2275);
    }

    @Test
    public void testGen02277() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.03.2018"), 2276);
    }

    @Test
    public void testGen02278() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.03.2018"), 2277);
    }

    @Test
    public void testGen02279() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.03.2018"), 2278);
    }

    @Test
    public void testGen02280() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.03.2018"), 2279);
    }

    @Test
    public void testGen02281() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.03.2018"), 2280);
    }

    @Test
    public void testGen02282() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "31.03.2018"), 2281);
    }

    @Test
    public void testGen02283() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.04.2018"), 2282);
    }

    @Test
    public void testGen02284() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.04.2018"), 2283);
    }

    @Test
    public void testGen02285() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.04.2018"), 2284);
    }

    @Test
    public void testGen02286() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.04.2018"), 2285);
    }

    @Test
    public void testGen02287() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.04.2018"), 2286);
    }

    @Test
    public void testGen02288() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.04.2018"), 2287);
    }

    @Test
    public void testGen02289() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.04.2018"), 2288);
    }

    @Test
    public void testGen02290() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.04.2018"), 2289);
    }

    @Test
    public void testGen02291() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.04.2018"), 2290);
    }

    @Test
    public void testGen02292() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.04.2018"), 2291);
    }

    @Test
    public void testGen02293() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.04.2018"), 2292);
    }

    @Test
    public void testGen02294() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.04.2018"), 2293);
    }

    @Test
    public void testGen02295() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.04.2018"), 2294);
    }

    @Test
    public void testGen02296() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.04.2018"), 2295);
    }

    @Test
    public void testGen02297() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.04.2018"), 2296);
    }

    @Test
    public void testGen02298() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.04.2018"), 2297);
    }

    @Test
    public void testGen02299() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.04.2018"), 2298);
    }

    @Test
    public void testGen02300() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.04.2018"), 2299);
    }

    @Test
    public void testGen02301() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.04.2018"), 2300);
    }

    @Test
    public void testGen02302() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.04.2018"), 2301);
    }

    @Test
    public void testGen02303() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.04.2018"), 2302);
    }

    @Test
    public void testGen02304() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.04.2018"), 2303);
    }

    @Test
    public void testGen02305() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.04.2018"), 2304);
    }

    @Test
    public void testGen02306() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.04.2018"), 2305);
    }

    @Test
    public void testGen02307() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.04.2018"), 2306);
    }

    @Test
    public void testGen02308() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "26.04.2018"), 2307);
    }

    @Test
    public void testGen02309() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "27.04.2018"), 2308);
    }

    @Test
    public void testGen02310() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.04.2018"), 2309);
    }

    @Test
    public void testGen02311() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "29.04.2018"), 2310);
    }

    @Test
    public void testGen02312() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.04.2018"), 2311);
    }

    @Test
    public void testGen02313() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "01.05.2018"), 2312);
    }

    @Test
    public void testGen02314() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.05.2018"), 2313);
    }

    @Test
    public void testGen02315() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "03.05.2018"), 2314);
    }

    @Test
    public void testGen02316() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.05.2018"), 2315);
    }

    @Test
    public void testGen02317() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.05.2018"), 2316);
    }

    @Test
    public void testGen02318() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "06.05.2018"), 2317);
    }

    @Test
    public void testGen02319() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "07.05.2018"), 2318);
    }

    @Test
    public void testGen02320() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "08.05.2018"), 2319);
    }

    @Test
    public void testGen02321() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "09.05.2018"), 2320);
    }

    @Test
    public void testGen02322() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "10.05.2018"), 2321);
    }

    @Test
    public void testGen02323() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "11.05.2018"), 2322);
    }

    @Test
    public void testGen02324() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.05.2018"), 2323);
    }

    @Test
    public void testGen02325() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "13.05.2018"), 2324);
    }

    @Test
    public void testGen02326() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "14.05.2018"), 2325);
    }

    @Test
    public void testGen02327() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.05.2018"), 2326);
    }

    @Test
    public void testGen02328() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "16.05.2018"), 2327);
    }

    @Test
    public void testGen02329() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.05.2018"), 2328);
    }

    @Test
    public void testGen02330() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "18.05.2018"), 2329);
    }

    @Test
    public void testGen02331() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "19.05.2018"), 2330);
    }

    @Test
    public void testGen02332() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.05.2018"), 2331);
    }

    @Test
    public void testGen02333() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "21.05.2018"), 2332);
    }

    @Test
    public void testGen02334() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.05.2018"), 2333);
    }

    @Test
    public void testGen02335() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "23.05.2018"), 2334);
    }

    @Test
    public void testGen02336() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "24.05.2018"), 2335);
    }

    @Test
    public void testGen02337() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.05.2018"), 2336);
    }

    @Test
    public void testSitaxBug00001() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "04.12.2013"), 703);
    }

    @Test
    public void testSitaxBug00002() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "17.11.2013"), 686);
    }

    @Test
    public void testSitaxBug00003() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.10.2013"), 650);
    }

    @Test
    public void testSitaxBug00004() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "12.10.2012"), 285);
    }

    @Test
    public void testSitaxBug00005() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "22.09.2013"), 630);
    }

    @Test
    public void testSitaxBug00006() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "25.08.2013"), 602);
    }

    @Test
    public void testSitaxBug00007() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.05.2013"), 490);
    }

    @Test
    public void testSitaxBug00008() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "05.04.2013"), 460);
    }

    @Test
    public void testSitaxBug00009() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "20.03.2013"), 444);
    }

    @Test
    public void testSitaxBug00010() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.02.2013"), 424);
    }

    @Test
    public void testSitaxBug00011() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "28.01.2013"), 393);
    }

    @Test
    public void testSitaxBug00012() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.02.2013"), 411);
    }

    @Test
    public void testSitaxBug00013() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "30.01.2013"), 395);
    }

    @Test
    public void testSitaxBug00014() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "15.01.2013"), 380);
    }

    @Test
    public void testSitaxBug00015() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween("01.01.2012", "02.01.2013"), 367);
    }

    @Test
    public void test2Gen00001() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.01.2012"), 0);
    }

    @Test
    public void test2Gen00002() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.01.2012"), 1);
    }

    @Test
    public void test2Gen00003() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.01.2012"), 2);
    }

    @Test
    public void test2Gen00004() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.01.2012"), 3);
    }

    @Test
    public void test2Gen00005() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.01.2012"), 4);
    }

    @Test
    public void test2Gen00006() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.01.2012"), 5);
    }

    @Test
    public void test2Gen00007() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.01.2012"), 6);
    }

    @Test
    public void test2Gen00008() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.01.2012"), 7);
    }

    @Test
    public void test2Gen00009() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.01.2012"), 8);
    }

    @Test
    public void test2Gen00010() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.01.2012"), 9);
    }

    @Test
    public void test2Gen00011() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.01.2012"), 10);
    }

    @Test
    public void test2Gen00012() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.01.2012"), 11);
    }

    @Test
    public void test2Gen00013() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.01.2012"), 12);
    }

    @Test
    public void test2Gen00014() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.01.2012"), 13);
    }

    @Test
    public void test2Gen00015() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.01.2012"), 14);
    }

    @Test
    public void test2Gen00016() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.01.2012"), 15);
    }

    @Test
    public void test2Gen00017() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.01.2012"), 16);
    }

    @Test
    public void test2Gen00018() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.01.2012"), 17);
    }

    @Test
    public void test2Gen00019() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.01.2012"), 18);
    }

    @Test
    public void test2Gen00020() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.01.2012"), 19);
    }

    @Test
    public void test2Gen00021() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.01.2012"), 20);
    }

    @Test
    public void test2Gen00022() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.01.2012"), 21);
    }

    @Test
    public void test2Gen00023() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.01.2012"), 22);
    }

    @Test
    public void test2Gen00024() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.01.2012"), 23);
    }

    @Test
    public void test2Gen00025() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.01.2012"), 24);
    }

    @Test
    public void test2Gen00026() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.01.2012"), 25);
    }

    @Test
    public void test2Gen00027() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.01.2012"), 26);
    }

    @Test
    public void test2Gen00028() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.01.2012"), 27);
    }

    @Test
    public void test2Gen00029() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.01.2012"), 28);
    }

    @Test
    public void test2Gen00030() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.01.2012"), 29);
    }

    @Test
    public void test2Gen00031() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.01.2012"), 30);
    }

    @Test
    public void test2Gen00032() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.02.2012"), 31);
    }

    @Test
    public void test2Gen00033() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.02.2012"), 32);
    }

    @Test
    public void test2Gen00034() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.02.2012"), 33);
    }

    @Test
    public void test2Gen00035() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.02.2012"), 34);
    }

    @Test
    public void test2Gen00036() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.02.2012"), 35);
    }

    @Test
    public void test2Gen00037() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.02.2012"), 36);
    }

    @Test
    public void test2Gen00038() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.02.2012"), 37);
    }

    @Test
    public void test2Gen00039() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.02.2012"), 38);
    }

    @Test
    public void test2Gen00040() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.02.2012"), 39);
    }

    @Test
    public void test2Gen00041() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.02.2012"), 40);
    }

    @Test
    public void test2Gen00042() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.02.2012"), 41);
    }

    @Test
    public void test2Gen00043() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.02.2012"), 42);
    }

    @Test
    public void test2Gen00044() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.02.2012"), 43);
    }

    @Test
    public void test2Gen00045() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.02.2012"), 44);
    }

    @Test
    public void test2Gen00046() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.02.2012"), 45);
    }

    @Test
    public void test2Gen00047() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.02.2012"), 46);
    }

    @Test
    public void test2Gen00048() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.02.2012"), 47);
    }

    @Test
    public void test2Gen00049() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.02.2012"), 48);
    }

    @Test
    public void test2Gen00050() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.02.2012"), 49);
    }

    @Test
    public void test2Gen00051() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.02.2012"), 50);
    }

    @Test
    public void test2Gen00052() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.02.2012"), 51);
    }

    @Test
    public void test2Gen00053() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.02.2012"), 52);
    }

    @Test
    public void test2Gen00054() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.02.2012"), 53);
    }

    @Test
    public void test2Gen00055() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.02.2012"), 54);
    }

    @Test
    public void test2Gen00056() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.02.2012"), 55);
    }

    @Test
    public void test2Gen00057() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.02.2012"), 56);
    }

    @Test
    public void test2Gen00058() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.02.2012"), 57);
    }

    @Test
    public void test2Gen00059() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.02.2012"), 58);
    }

    @Test
    public void test2Gen00060() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.02.2012"), 59);
    }

    @Test
    public void test2Gen00061() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.03.2012"), 60);
    }

    @Test
    public void test2Gen00062() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.03.2012"), 61);
    }

    @Test
    public void test2Gen00063() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.03.2012"), 62);
    }

    @Test
    public void test2Gen00064() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.03.2012"), 63);
    }

    @Test
    public void test2Gen00065() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.03.2012"), 64);
    }

    @Test
    public void test2Gen00066() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.03.2012"), 65);
    }

    @Test
    public void test2Gen00067() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.03.2012"), 66);
    }

    @Test
    public void test2Gen00068() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.03.2012"), 67);
    }

    @Test
    public void test2Gen00069() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.03.2012"), 68);
    }

    @Test
    public void test2Gen00070() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.03.2012"), 69);
    }

    @Test
    public void test2Gen00071() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.03.2012"), 70);
    }

    @Test
    public void test2Gen00072() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.03.2012"), 71);
    }

    @Test
    public void test2Gen00073() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.03.2012"), 72);
    }

    @Test
    public void test2Gen00074() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.03.2012"), 73);
    }

    @Test
    public void test2Gen00075() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.03.2012"), 74);
    }

    @Test
    public void test2Gen00076() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.03.2012"), 75);
    }

    @Test
    public void test2Gen00077() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.03.2012"), 76);
    }

    @Test
    public void test2Gen00078() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.03.2012"), 77);
    }

    @Test
    public void test2Gen00079() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.03.2012"), 78);
    }

    @Test
    public void test2Gen00080() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.03.2012"), 79);
    }

    @Test
    public void test2Gen00081() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.03.2012"), 80);
    }

    @Test
    public void test2Gen00082() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.03.2012"), 81);
    }

    @Test
    public void test2Gen00083() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.03.2012"), 82);
    }

    @Test
    public void test2Gen00084() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.03.2012"), 83);
    }

    @Test
    public void test2Gen00085() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.03.2012"), 84);
    }

    @Test
    public void test2Gen00086() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.03.2012"), 85);
    }

    @Test
    public void test2Gen00087() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.03.2012"), 86);
    }

    @Test
    public void test2Gen00088() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.03.2012"), 87);
    }

    @Test
    public void test2Gen00089() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.03.2012"), 88);
    }

    @Test
    public void test2Gen00090() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.03.2012"), 89);
    }

    @Test
    public void test2Gen00091() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.03.2012"), 90);
    }

    @Test
    public void test2Gen00092() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.04.2012"), 91);
    }

    @Test
    public void test2Gen00093() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.04.2012"), 92);
    }

    @Test
    public void test2Gen00094() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.04.2012"), 93);
    }

    @Test
    public void test2Gen00095() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.04.2012"), 94);
    }

    @Test
    public void test2Gen00096() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.04.2012"), 95);
    }

    @Test
    public void test2Gen00097() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.04.2012"), 96);
    }

    @Test
    public void test2Gen00098() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.04.2012"), 97);
    }

    @Test
    public void test2Gen00099() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.04.2012"), 98);
    }

    @Test
    public void test2Gen00100() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.04.2012"), 99);
    }

    @Test
    public void test2Gen00101() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.04.2012"), 100);
    }

    @Test
    public void test2Gen00102() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.04.2012"), 101);
    }

    @Test
    public void test2Gen00103() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.04.2012"), 102);
    }

    @Test
    public void test2Gen00104() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.04.2012"), 103);
    }

    @Test
    public void test2Gen00105() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.04.2012"), 104);
    }

    @Test
    public void test2Gen00106() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.04.2012"), 105);
    }

    @Test
    public void test2Gen00107() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.04.2012"), 106);
    }

    @Test
    public void test2Gen00108() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.04.2012"), 107);
    }

    @Test
    public void test2Gen00109() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.04.2012"), 108);
    }

    @Test
    public void test2Gen00110() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.04.2012"), 109);
    }

    @Test
    public void test2Gen00111() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.04.2012"), 110);
    }

    @Test
    public void test2Gen00112() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.04.2012"), 111);
    }

    @Test
    public void test2Gen00113() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.04.2012"), 112);
    }

    @Test
    public void test2Gen00114() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.04.2012"), 113);
    }

    @Test
    public void test2Gen00115() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.04.2012"), 114);
    }

    @Test
    public void test2Gen00116() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.04.2012"), 115);
    }

    @Test
    public void test2Gen00117() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.04.2012"), 116);
    }

    @Test
    public void test2Gen00118() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.04.2012"), 117);
    }

    @Test
    public void test2Gen00119() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.04.2012"), 118);
    }

    @Test
    public void test2Gen00120() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.04.2012"), 119);
    }

    @Test
    public void test2Gen00121() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.04.2012"), 120);
    }

    @Test
    public void test2Gen00122() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.05.2012"), 121);
    }

    @Test
    public void test2Gen00123() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.05.2012"), 122);
    }

    @Test
    public void test2Gen00124() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.05.2012"), 123);
    }

    @Test
    public void test2Gen00125() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.05.2012"), 124);
    }

    @Test
    public void test2Gen00126() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.05.2012"), 125);
    }

    @Test
    public void test2Gen00127() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.05.2012"), 126);
    }

    @Test
    public void test2Gen00128() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.05.2012"), 127);
    }

    @Test
    public void test2Gen00129() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.05.2012"), 128);
    }

    @Test
    public void test2Gen00130() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.05.2012"), 129);
    }

    @Test
    public void test2Gen00131() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.05.2012"), 130);
    }

    @Test
    public void test2Gen00132() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.05.2012"), 131);
    }

    @Test
    public void test2Gen00133() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.05.2012"), 132);
    }

    @Test
    public void test2Gen00134() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.05.2012"), 133);
    }

    @Test
    public void test2Gen00135() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.05.2012"), 134);
    }

    @Test
    public void test2Gen00136() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.05.2012"), 135);
    }

    @Test
    public void test2Gen00137() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.05.2012"), 136);
    }

    @Test
    public void test2Gen00138() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.05.2012"), 137);
    }

    @Test
    public void test2Gen00139() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.05.2012"), 138);
    }

    @Test
    public void test2Gen00140() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.05.2012"), 139);
    }

    @Test
    public void test2Gen00141() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.05.2012"), 140);
    }

    @Test
    public void test2Gen00142() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.05.2012"), 141);
    }

    @Test
    public void test2Gen00143() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.05.2012"), 142);
    }

    @Test
    public void test2Gen00144() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.05.2012"), 143);
    }

    @Test
    public void test2Gen00145() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.05.2012"), 144);
    }

    @Test
    public void test2Gen00146() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.05.2012"), 145);
    }

    @Test
    public void test2Gen00147() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.05.2012"), 146);
    }

    @Test
    public void test2Gen00148() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.05.2012"), 147);
    }

    @Test
    public void test2Gen00149() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.05.2012"), 148);
    }

    @Test
    public void test2Gen00150() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.05.2012"), 149);
    }

    @Test
    public void test2Gen00151() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.05.2012"), 150);
    }

    @Test
    public void test2Gen00152() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.05.2012"), 151);
    }

    @Test
    public void test2Gen00153() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.06.2012"), 152);
    }

    @Test
    public void test2Gen00154() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.06.2012"), 153);
    }

    @Test
    public void test2Gen00155() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.06.2012"), 154);
    }

    @Test
    public void test2Gen00156() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.06.2012"), 155);
    }

    @Test
    public void test2Gen00157() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.06.2012"), 156);
    }

    @Test
    public void test2Gen00158() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.06.2012"), 157);
    }

    @Test
    public void test2Gen00159() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.06.2012"), 158);
    }

    @Test
    public void test2Gen00160() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.06.2012"), 159);
    }

    @Test
    public void test2Gen00161() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.06.2012"), 160);
    }

    @Test
    public void test2Gen00162() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.06.2012"), 161);
    }

    @Test
    public void test2Gen00163() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.06.2012"), 162);
    }

    @Test
    public void test2Gen00164() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.06.2012"), 163);
    }

    @Test
    public void test2Gen00165() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.06.2012"), 164);
    }

    @Test
    public void test2Gen00166() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.06.2012"), 165);
    }

    @Test
    public void test2Gen00167() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.06.2012"), 166);
    }

    @Test
    public void test2Gen00168() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.06.2012"), 167);
    }

    @Test
    public void test2Gen00169() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.06.2012"), 168);
    }

    @Test
    public void test2Gen00170() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.06.2012"), 169);
    }

    @Test
    public void test2Gen00171() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.06.2012"), 170);
    }

    @Test
    public void test2Gen00172() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.06.2012"), 171);
    }

    @Test
    public void test2Gen00173() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.06.2012"), 172);
    }

    @Test
    public void test2Gen00174() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.06.2012"), 173);
    }

    @Test
    public void test2Gen00175() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.06.2012"), 174);
    }

    @Test
    public void test2Gen00176() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.06.2012"), 175);
    }

    @Test
    public void test2Gen00177() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.06.2012"), 176);
    }

    @Test
    public void test2Gen00178() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.06.2012"), 177);
    }

    @Test
    public void test2Gen00179() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.06.2012"), 178);
    }

    @Test
    public void test2Gen00180() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.06.2012"), 179);
    }

    @Test
    public void test2Gen00181() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.06.2012"), 180);
    }

    @Test
    public void test2Gen00182() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.06.2012"), 181);
    }

    @Test
    public void test2Gen00183() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.07.2012"), 182);
    }

    @Test
    public void test2Gen00184() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.07.2012"), 183);
    }

    @Test
    public void test2Gen00185() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.07.2012"), 184);
    }

    @Test
    public void test2Gen00186() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.07.2012"), 185);
    }

    @Test
    public void test2Gen00187() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.07.2012"), 186);
    }

    @Test
    public void test2Gen00188() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.07.2012"), 187);
    }

    @Test
    public void test2Gen00189() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.07.2012"), 188);
    }

    @Test
    public void test2Gen00190() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.07.2012"), 189);
    }

    @Test
    public void test2Gen00191() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.07.2012"), 190);
    }

    @Test
    public void test2Gen00192() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.07.2012"), 191);
    }

    @Test
    public void test2Gen00193() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.07.2012"), 192);
    }

    @Test
    public void test2Gen00194() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.07.2012"), 193);
    }

    @Test
    public void test2Gen00195() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.07.2012"), 194);
    }

    @Test
    public void test2Gen00196() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.07.2012"), 195);
    }

    @Test
    public void test2Gen00197() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.07.2012"), 196);
    }

    @Test
    public void test2Gen00198() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.07.2012"), 197);
    }

    @Test
    public void test2Gen00199() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.07.2012"), 198);
    }

    @Test
    public void test2Gen00200() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.07.2012"), 199);
    }

    @Test
    public void test2Gen00201() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.07.2012"), 200);
    }

    @Test
    public void test2Gen00202() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.07.2012"), 201);
    }

    @Test
    public void test2Gen00203() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.07.2012"), 202);
    }

    @Test
    public void test2Gen00204() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.07.2012"), 203);
    }

    @Test
    public void test2Gen00205() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.07.2012"), 204);
    }

    @Test
    public void test2Gen00206() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.07.2012"), 205);
    }

    @Test
    public void test2Gen00207() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.07.2012"), 206);
    }

    @Test
    public void test2Gen00208() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.07.2012"), 207);
    }

    @Test
    public void test2Gen00209() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.07.2012"), 208);
    }

    @Test
    public void test2Gen00210() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.07.2012"), 209);
    }

    @Test
    public void test2Gen00211() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.07.2012"), 210);
    }

    @Test
    public void test2Gen00212() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.07.2012"), 211);
    }

    @Test
    public void test2Gen00213() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.07.2012"), 212);
    }

    @Test
    public void test2Gen00214() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.08.2012"), 213);
    }

    @Test
    public void test2Gen00215() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.08.2012"), 214);
    }

    @Test
    public void test2Gen00216() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.08.2012"), 215);
    }

    @Test
    public void test2Gen00217() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.08.2012"), 216);
    }

    @Test
    public void test2Gen00218() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.08.2012"), 217);
    }

    @Test
    public void test2Gen00219() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.08.2012"), 218);
    }

    @Test
    public void test2Gen00220() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.08.2012"), 219);
    }

    @Test
    public void test2Gen00221() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.08.2012"), 220);
    }

    @Test
    public void test2Gen00222() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.08.2012"), 221);
    }

    @Test
    public void test2Gen00223() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.08.2012"), 222);
    }

    @Test
    public void test2Gen00224() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.08.2012"), 223);
    }

    @Test
    public void test2Gen00225() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.08.2012"), 224);
    }

    @Test
    public void test2Gen00226() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.08.2012"), 225);
    }

    @Test
    public void test2Gen00227() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.08.2012"), 226);
    }

    @Test
    public void test2Gen00228() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.08.2012"), 227);
    }

    @Test
    public void test2Gen00229() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.08.2012"), 228);
    }

    @Test
    public void test2Gen00230() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.08.2012"), 229);
    }

    @Test
    public void test2Gen00231() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.08.2012"), 230);
    }

    @Test
    public void test2Gen00232() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.08.2012"), 231);
    }

    @Test
    public void test2Gen00233() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.08.2012"), 232);
    }

    @Test
    public void test2Gen00234() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.08.2012"), 233);
    }

    @Test
    public void test2Gen00235() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.08.2012"), 234);
    }

    @Test
    public void test2Gen00236() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.08.2012"), 235);
    }

    @Test
    public void test2Gen00237() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.08.2012"), 236);
    }

    @Test
    public void test2Gen00238() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.08.2012"), 237);
    }

    @Test
    public void test2Gen00239() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.08.2012"), 238);
    }

    @Test
    public void test2Gen00240() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.08.2012"), 239);
    }

    @Test
    public void test2Gen00241() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.08.2012"), 240);
    }

    @Test
    public void test2Gen00242() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.08.2012"), 241);
    }

    @Test
    public void test2Gen00243() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.08.2012"), 242);
    }

    @Test
    public void test2Gen00244() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.08.2012"), 243);
    }

    @Test
    public void test2Gen00245() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.09.2012"), 244);
    }

    @Test
    public void test2Gen00246() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.09.2012"), 245);
    }

    @Test
    public void test2Gen00247() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.09.2012"), 246);
    }

    @Test
    public void test2Gen00248() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.09.2012"), 247);
    }

    @Test
    public void test2Gen00249() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.09.2012"), 248);
    }

    @Test
    public void test2Gen00250() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.09.2012"), 249);
    }

    @Test
    public void test2Gen00251() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.09.2012"), 250);
    }

    @Test
    public void test2Gen00252() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.09.2012"), 251);
    }

    @Test
    public void test2Gen00253() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.09.2012"), 252);
    }

    @Test
    public void test2Gen00254() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.09.2012"), 253);
    }

    @Test
    public void test2Gen00255() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.09.2012"), 254);
    }

    @Test
    public void test2Gen00256() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.09.2012"), 255);
    }

    @Test
    public void test2Gen00257() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.09.2012"), 256);
    }

    @Test
    public void test2Gen00258() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.09.2012"), 257);
    }

    @Test
    public void test2Gen00259() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.09.2012"), 258);
    }

    @Test
    public void test2Gen00260() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.09.2012"), 259);
    }

    @Test
    public void test2Gen00261() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.09.2012"), 260);
    }

    @Test
    public void test2Gen00262() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.09.2012"), 261);
    }

    @Test
    public void test2Gen00263() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.09.2012"), 262);
    }

    @Test
    public void test2Gen00264() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.09.2012"), 263);
    }

    @Test
    public void test2Gen00265() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.09.2012"), 264);
    }

    @Test
    public void test2Gen00266() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.09.2012"), 265);
    }

    @Test
    public void test2Gen00267() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.09.2012"), 266);
    }

    @Test
    public void test2Gen00268() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.09.2012"), 267);
    }

    @Test
    public void test2Gen00269() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.09.2012"), 268);
    }

    @Test
    public void test2Gen00270() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.09.2012"), 269);
    }

    @Test
    public void test2Gen00271() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.09.2012"), 270);
    }

    @Test
    public void test2Gen00272() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.09.2012"), 271);
    }

    @Test
    public void test2Gen00273() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.09.2012"), 272);
    }

    @Test
    public void test2Gen00274() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.09.2012"), 273);
    }

    @Test
    public void test2Gen00275() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.10.2012"), 274);
    }

    @Test
    public void test2Gen00276() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.10.2012"), 275);
    }

    @Test
    public void test2Gen00277() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.10.2012"), 276);
    }

    @Test
    public void test2Gen00278() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.10.2012"), 277);
    }

    @Test
    public void test2Gen00279() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.10.2012"), 278);
    }

    @Test
    public void test2Gen00280() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.10.2012"), 279);
    }

    @Test
    public void test2Gen00281() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.10.2012"), 280);
    }

    @Test
    public void test2Gen00282() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.10.2012"), 281);
    }

    @Test
    public void test2Gen00283() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.10.2012"), 282);
    }

    @Test
    public void test2Gen00284() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.10.2012"), 283);
    }

    @Test
    public void test2Gen00285() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.10.2012"), 284);
    }

    @Test
    public void test2Gen00286() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.10.2012"), 285);
    }

    @Test
    public void test2Gen00287() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.10.2012"), 286);
    }

    @Test
    public void test2Gen00288() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.10.2012"), 287);
    }

    @Test
    public void test2Gen00289() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.10.2012"), 288);
    }

    @Test
    public void test2Gen00290() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.10.2012"), 289);
    }

    @Test
    public void test2Gen00291() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.10.2012"), 290);
    }

    @Test
    public void test2Gen00292() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.10.2012"), 291);
    }

    @Test
    public void test2Gen00293() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.10.2012"), 292);
    }

    @Test
    public void test2Gen00294() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.10.2012"), 293);
    }

    @Test
    public void test2Gen00295() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.10.2012"), 294);
    }

    @Test
    public void test2Gen00296() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.10.2012"), 295);
    }

    @Test
    public void test2Gen00297() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.10.2012"), 296);
    }

    @Test
    public void test2Gen00298() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.10.2012"), 297);
    }

    @Test
    public void test2Gen00299() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.10.2012"), 298);
    }

    @Test
    public void test2Gen00300() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.10.2012"), 299);
    }

    @Test
    public void test2Gen00301() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.10.2012"), 300);
    }

    @Test
    public void test2Gen00302() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.10.2012"), 301);
    }

    @Test
    public void test2Gen00303() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.10.2012"), 302);
    }

    @Test
    public void test2Gen00304() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.10.2012"), 303);
    }

    @Test
    public void test2Gen00305() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.10.2012"), 304);
    }

    @Test
    public void test2Gen00306() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.11.2012"), 305);
    }

    @Test
    public void test2Gen00307() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.11.2012"), 306);
    }

    @Test
    public void test2Gen00308() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.11.2012"), 307);
    }

    @Test
    public void test2Gen00309() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.11.2012"), 308);
    }

    @Test
    public void test2Gen00310() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.11.2012"), 309);
    }

    @Test
    public void test2Gen00311() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.11.2012"), 310);
    }

    @Test
    public void test2Gen00312() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.11.2012"), 311);
    }

    @Test
    public void test2Gen00313() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.11.2012"), 312);
    }

    @Test
    public void test2Gen00314() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.11.2012"), 313);
    }

    @Test
    public void test2Gen00315() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.11.2012"), 314);
    }

    @Test
    public void test2Gen00316() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.11.2012"), 315);
    }

    @Test
    public void test2Gen00317() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.11.2012"), 316);
    }

    @Test
    public void test2Gen00318() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.11.2012"), 317);
    }

    @Test
    public void test2Gen00319() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.11.2012"), 318);
    }

    @Test
    public void test2Gen00320() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.11.2012"), 319);
    }

    @Test
    public void test2Gen00321() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.11.2012"), 320);
    }

    @Test
    public void test2Gen00322() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.11.2012"), 321);
    }

    @Test
    public void test2Gen00323() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.11.2012"), 322);
    }

    @Test
    public void test2Gen00324() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.11.2012"), 323);
    }

    @Test
    public void test2Gen00325() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.11.2012"), 324);
    }

    @Test
    public void test2Gen00326() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.11.2012"), 325);
    }

    @Test
    public void test2Gen00327() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.11.2012"), 326);
    }

    @Test
    public void test2Gen00328() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.11.2012"), 327);
    }

    @Test
    public void test2Gen00329() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.11.2012"), 328);
    }

    @Test
    public void test2Gen00330() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.11.2012"), 329);
    }

    @Test
    public void test2Gen00331() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.11.2012"), 330);
    }

    @Test
    public void test2Gen00332() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.11.2012"), 331);
    }

    @Test
    public void test2Gen00333() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.11.2012"), 332);
    }

    @Test
    public void test2Gen00334() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.11.2012"), 333);
    }

    @Test
    public void test2Gen00335() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.11.2012"), 334);
    }

    @Test
    public void test2Gen00336() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.12.2012"), 335);
    }

    @Test
    public void test2Gen00337() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.12.2012"), 336);
    }

    @Test
    public void test2Gen00338() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.12.2012"), 337);
    }

    @Test
    public void test2Gen00339() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.12.2012"), 338);
    }

    @Test
    public void test2Gen00340() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.12.2012"), 339);
    }

    @Test
    public void test2Gen00341() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.12.2012"), 340);
    }

    @Test
    public void test2Gen00342() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.12.2012"), 341);
    }

    @Test
    public void test2Gen00343() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.12.2012"), 342);
    }

    @Test
    public void test2Gen00344() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.12.2012"), 343);
    }

    @Test
    public void test2Gen00345() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.12.2012"), 344);
    }

    @Test
    public void test2Gen00346() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.12.2012"), 345);
    }

    @Test
    public void test2Gen00347() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.12.2012"), 346);
    }

    @Test
    public void test2Gen00348() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.12.2012"), 347);
    }

    @Test
    public void test2Gen00349() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.12.2012"), 348);
    }

    @Test
    public void test2Gen00350() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.12.2012"), 349);
    }

    @Test
    public void test2Gen00351() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.12.2012"), 350);
    }

    @Test
    public void test2Gen00352() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.12.2012"), 351);
    }

    @Test
    public void test2Gen00353() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.12.2012"), 352);
    }

    @Test
    public void test2Gen00354() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.12.2012"), 353);
    }

    @Test
    public void test2Gen00355() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.12.2012"), 354);
    }

    @Test
    public void test2Gen00356() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.12.2012"), 355);
    }

    @Test
    public void test2Gen00357() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.12.2012"), 356);
    }

    @Test
    public void test2Gen00358() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.12.2012"), 357);
    }

    @Test
    public void test2Gen00359() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.12.2012"), 358);
    }

    @Test
    public void test2Gen00360() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.12.2012"), 359);
    }

    @Test
    public void test2Gen00361() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.12.2012"), 360);
    }

    @Test
    public void test2Gen00362() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.12.2012"), 361);
    }

    @Test
    public void test2Gen00363() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.12.2012"), 362);
    }

    @Test
    public void test2Gen00364() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.12.2012"), 363);
    }

    @Test
    public void test2Gen00365() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.12.2012"), 364);
    }

    @Test
    public void test2Gen00366() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.12.2012"), 365);
    }

    @Test
    public void test2Gen00367() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.01.2013"), 366);
    }

    @Test
    public void test2Gen00368() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.01.2013"), 367);
    }

    @Test
    public void test2Gen00369() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.01.2013"), 368);
    }

    @Test
    public void test2Gen00370() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.01.2013"), 369);
    }

    @Test
    public void test2Gen00371() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.01.2013"), 370);
    }

    @Test
    public void test2Gen00372() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.01.2013"), 371);
    }

    @Test
    public void test2Gen00373() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.01.2013"), 372);
    }

    @Test
    public void test2Gen00374() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.01.2013"), 373);
    }

    @Test
    public void test2Gen00375() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.01.2013"), 374);
    }

    @Test
    public void test2Gen00376() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.01.2013"), 375);
    }

    @Test
    public void test2Gen00377() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.01.2013"), 376);
    }

    @Test
    public void test2Gen00378() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.01.2013"), 377);
    }

    @Test
    public void test2Gen00379() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.01.2013"), 378);
    }

    @Test
    public void test2Gen00380() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.01.2013"), 379);
    }

    @Test
    public void test2Gen00381() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.01.2013"), 380);
    }

    @Test
    public void test2Gen00382() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.01.2013"), 381);
    }

    @Test
    public void test2Gen00383() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.01.2013"), 382);
    }

    @Test
    public void test2Gen00384() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.01.2013"), 383);
    }

    @Test
    public void test2Gen00385() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.01.2013"), 384);
    }

    @Test
    public void test2Gen00386() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.01.2013"), 385);
    }

    @Test
    public void test2Gen00387() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.01.2013"), 386);
    }

    @Test
    public void test2Gen00388() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.01.2013"), 387);
    }

    @Test
    public void test2Gen00389() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.01.2013"), 388);
    }

    @Test
    public void test2Gen00390() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.01.2013"), 389);
    }

    @Test
    public void test2Gen00391() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.01.2013"), 390);
    }

    @Test
    public void test2Gen00392() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.01.2013"), 391);
    }

    @Test
    public void test2Gen00393() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.01.2013"), 392);
    }

    @Test
    public void test2Gen00394() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.01.2013"), 393);
    }

    @Test
    public void test2Gen00395() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.01.2013"), 394);
    }

    @Test
    public void test2Gen00396() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.01.2013"), 395);
    }

    @Test
    public void test2Gen00397() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.01.2013"), 396);
    }

    @Test
    public void test2Gen00398() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.02.2013"), 397);
    }

    @Test
    public void test2Gen00399() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.02.2013"), 398);
    }

    @Test
    public void test2Gen00400() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.02.2013"), 399);
    }

    @Test
    public void test2Gen00401() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.02.2013"), 400);
    }

    @Test
    public void test2Gen00402() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.02.2013"), 401);
    }

    @Test
    public void test2Gen00403() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.02.2013"), 402);
    }

    @Test
    public void test2Gen00404() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.02.2013"), 403);
    }

    @Test
    public void test2Gen00405() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.02.2013"), 404);
    }

    @Test
    public void test2Gen00406() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.02.2013"), 405);
    }

    @Test
    public void test2Gen00407() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.02.2013"), 406);
    }

    @Test
    public void test2Gen00408() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.02.2013"), 407);
    }

    @Test
    public void test2Gen00409() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.02.2013"), 408);
    }

    @Test
    public void test2Gen00410() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.02.2013"), 409);
    }

    @Test
    public void test2Gen00411() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.02.2013"), 410);
    }

    @Test
    public void test2Gen00412() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.02.2013"), 411);
    }

    @Test
    public void test2Gen00413() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.02.2013"), 412);
    }

    @Test
    public void test2Gen00414() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.02.2013"), 413);
    }

    @Test
    public void test2Gen00415() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.02.2013"), 414);
    }

    @Test
    public void test2Gen00416() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.02.2013"), 415);
    }

    @Test
    public void test2Gen00417() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.02.2013"), 416);
    }

    @Test
    public void test2Gen00418() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.02.2013"), 417);
    }

    @Test
    public void test2Gen00419() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.02.2013"), 418);
    }

    @Test
    public void test2Gen00420() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.02.2013"), 419);
    }

    @Test
    public void test2Gen00421() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.02.2013"), 420);
    }

    @Test
    public void test2Gen00422() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.02.2013"), 421);
    }

    @Test
    public void test2Gen00423() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.02.2013"), 422);
    }

    @Test
    public void test2Gen00424() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.02.2013"), 423);
    }

    @Test
    public void test2Gen00425() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.02.2013"), 424);
    }

    @Test
    public void test2Gen00426() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.03.2013"), 425);
    }

    @Test
    public void test2Gen00427() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.03.2013"), 426);
    }

    @Test
    public void test2Gen00428() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.03.2013"), 427);
    }

    @Test
    public void test2Gen00429() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.03.2013"), 428);
    }

    @Test
    public void test2Gen00430() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.03.2013"), 429);
    }

    @Test
    public void test2Gen00431() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.03.2013"), 430);
    }

    @Test
    public void test2Gen00432() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.03.2013"), 431);
    }

    @Test
    public void test2Gen00433() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.03.2013"), 432);
    }

    @Test
    public void test2Gen00434() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.03.2013"), 433);
    }

    @Test
    public void test2Gen00435() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.03.2013"), 434);
    }

    @Test
    public void test2Gen00436() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.03.2013"), 435);
    }

    @Test
    public void test2Gen00437() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.03.2013"), 436);
    }

    @Test
    public void test2Gen00438() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.03.2013"), 437);
    }

    @Test
    public void test2Gen00439() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.03.2013"), 438);
    }

    @Test
    public void test2Gen00440() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.03.2013"), 439);
    }

    @Test
    public void test2Gen00441() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.03.2013"), 440);
    }

    @Test
    public void test2Gen00442() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.03.2013"), 441);
    }

    @Test
    public void test2Gen00443() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.03.2013"), 442);
    }

    @Test
    public void test2Gen00444() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.03.2013"), 443);
    }

    @Test
    public void test2Gen00445() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.03.2013"), 444);
    }

    @Test
    public void test2Gen00446() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.03.2013"), 445);
    }

    @Test
    public void test2Gen00447() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.03.2013"), 446);
    }

    @Test
    public void test2Gen00448() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.03.2013"), 447);
    }

    @Test
    public void test2Gen00449() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.03.2013"), 448);
    }

    @Test
    public void test2Gen00450() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.03.2013"), 449);
    }

    @Test
    public void test2Gen00451() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.03.2013"), 450);
    }

    @Test
    public void test2Gen00452() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.03.2013"), 451);
    }

    @Test
    public void test2Gen00453() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.03.2013"), 452);
    }

    @Test
    public void test2Gen00454() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.03.2013"), 453);
    }

    @Test
    public void test2Gen00455() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.03.2013"), 454);
    }

    @Test
    public void test2Gen00456() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.03.2013"), 455);
    }

    @Test
    public void test2Gen00457() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.04.2013"), 456);
    }

    @Test
    public void test2Gen00458() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.04.2013"), 457);
    }

    @Test
    public void test2Gen00459() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.04.2013"), 458);
    }

    @Test
    public void test2Gen00460() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.04.2013"), 459);
    }

    @Test
    public void test2Gen00461() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.04.2013"), 460);
    }

    @Test
    public void test2Gen00462() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.04.2013"), 461);
    }

    @Test
    public void test2Gen00463() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.04.2013"), 462);
    }

    @Test
    public void test2Gen00464() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.04.2013"), 463);
    }

    @Test
    public void test2Gen00465() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.04.2013"), 464);
    }

    @Test
    public void test2Gen00466() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.04.2013"), 465);
    }

    @Test
    public void test2Gen00467() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.04.2013"), 466);
    }

    @Test
    public void test2Gen00468() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.04.2013"), 467);
    }

    @Test
    public void test2Gen00469() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.04.2013"), 468);
    }

    @Test
    public void test2Gen00470() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.04.2013"), 469);
    }

    @Test
    public void test2Gen00471() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.04.2013"), 470);
    }

    @Test
    public void test2Gen00472() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.04.2013"), 471);
    }

    @Test
    public void test2Gen00473() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.04.2013"), 472);
    }

    @Test
    public void test2Gen00474() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.04.2013"), 473);
    }

    @Test
    public void test2Gen00475() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.04.2013"), 474);
    }

    @Test
    public void test2Gen00476() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.04.2013"), 475);
    }

    @Test
    public void test2Gen00477() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.04.2013"), 476);
    }

    @Test
    public void test2Gen00478() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.04.2013"), 477);
    }

    @Test
    public void test2Gen00479() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.04.2013"), 478);
    }

    @Test
    public void test2Gen00480() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.04.2013"), 479);
    }

    @Test
    public void test2Gen00481() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.04.2013"), 480);
    }

    @Test
    public void test2Gen00482() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.04.2013"), 481);
    }

    @Test
    public void test2Gen00483() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.04.2013"), 482);
    }

    @Test
    public void test2Gen00484() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.04.2013"), 483);
    }

    @Test
    public void test2Gen00485() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.04.2013"), 484);
    }

    @Test
    public void test2Gen00486() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.04.2013"), 485);
    }

    @Test
    public void test2Gen00487() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.05.2013"), 486);
    }

    @Test
    public void test2Gen00488() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.05.2013"), 487);
    }

    @Test
    public void test2Gen00489() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.05.2013"), 488);
    }

    @Test
    public void test2Gen00490() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.05.2013"), 489);
    }

    @Test
    public void test2Gen00491() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.05.2013"), 490);
    }

    @Test
    public void test2Gen00492() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.05.2013"), 491);
    }

    @Test
    public void test2Gen00493() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.05.2013"), 492);
    }

    @Test
    public void test2Gen00494() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.05.2013"), 493);
    }

    @Test
    public void test2Gen00495() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.05.2013"), 494);
    }

    @Test
    public void test2Gen00496() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.05.2013"), 495);
    }

    @Test
    public void test2Gen00497() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.05.2013"), 496);
    }

    @Test
    public void test2Gen00498() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.05.2013"), 497);
    }

    @Test
    public void test2Gen00499() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.05.2013"), 498);
    }

    @Test
    public void test2Gen00500() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.05.2013"), 499);
    }

    @Test
    public void test2Gen00501() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.05.2013"), 500);
    }

    @Test
    public void test2Gen00502() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.05.2013"), 501);
    }

    @Test
    public void test2Gen00503() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.05.2013"), 502);
    }

    @Test
    public void test2Gen00504() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.05.2013"), 503);
    }

    @Test
    public void test2Gen00505() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.05.2013"), 504);
    }

    @Test
    public void test2Gen00506() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.05.2013"), 505);
    }

    @Test
    public void test2Gen00507() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.05.2013"), 506);
    }

    @Test
    public void test2Gen00508() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.05.2013"), 507);
    }

    @Test
    public void test2Gen00509() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.05.2013"), 508);
    }

    @Test
    public void test2Gen00510() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.05.2013"), 509);
    }

    @Test
    public void test2Gen00511() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.05.2013"), 510);
    }

    @Test
    public void test2Gen00512() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.05.2013"), 511);
    }

    @Test
    public void test2Gen00513() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.05.2013"), 512);
    }

    @Test
    public void test2Gen00514() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.05.2013"), 513);
    }

    @Test
    public void test2Gen00515() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.05.2013"), 514);
    }

    @Test
    public void test2Gen00516() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.05.2013"), 515);
    }

    @Test
    public void test2Gen00517() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.05.2013"), 516);
    }

    @Test
    public void test2Gen00518() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.06.2013"), 517);
    }

    @Test
    public void test2Gen00519() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.06.2013"), 518);
    }

    @Test
    public void test2Gen00520() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.06.2013"), 519);
    }

    @Test
    public void test2Gen00521() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.06.2013"), 520);
    }

    @Test
    public void test2Gen00522() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.06.2013"), 521);
    }

    @Test
    public void test2Gen00523() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.06.2013"), 522);
    }

    @Test
    public void test2Gen00524() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.06.2013"), 523);
    }

    @Test
    public void test2Gen00525() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.06.2013"), 524);
    }

    @Test
    public void test2Gen00526() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.06.2013"), 525);
    }

    @Test
    public void test2Gen00527() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.06.2013"), 526);
    }

    @Test
    public void test2Gen00528() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.06.2013"), 527);
    }

    @Test
    public void test2Gen00529() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.06.2013"), 528);
    }

    @Test
    public void test2Gen00530() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.06.2013"), 529);
    }

    @Test
    public void test2Gen00531() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.06.2013"), 530);
    }

    @Test
    public void test2Gen00532() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.06.2013"), 531);
    }

    @Test
    public void test2Gen00533() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.06.2013"), 532);
    }

    @Test
    public void test2Gen00534() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.06.2013"), 533);
    }

    @Test
    public void test2Gen00535() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.06.2013"), 534);
    }

    @Test
    public void test2Gen00536() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.06.2013"), 535);
    }

    @Test
    public void test2Gen00537() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.06.2013"), 536);
    }

    @Test
    public void test2Gen00538() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.06.2013"), 537);
    }

    @Test
    public void test2Gen00539() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.06.2013"), 538);
    }

    @Test
    public void test2Gen00540() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.06.2013"), 539);
    }

    @Test
    public void test2Gen00541() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.06.2013"), 540);
    }

    @Test
    public void test2Gen00542() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.06.2013"), 541);
    }

    @Test
    public void test2Gen00543() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.06.2013"), 542);
    }

    @Test
    public void test2Gen00544() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.06.2013"), 543);
    }

    @Test
    public void test2Gen00545() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.06.2013"), 544);
    }

    @Test
    public void test2Gen00546() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.06.2013"), 545);
    }

    @Test
    public void test2Gen00547() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.06.2013"), 546);
    }

    @Test
    public void test2Gen00548() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.07.2013"), 547);
    }

    @Test
    public void test2Gen00549() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.07.2013"), 548);
    }

    @Test
    public void test2Gen00550() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.07.2013"), 549);
    }

    @Test
    public void test2Gen00551() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.07.2013"), 550);
    }

    @Test
    public void test2Gen00552() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.07.2013"), 551);
    }

    @Test
    public void test2Gen00553() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.07.2013"), 552);
    }

    @Test
    public void test2Gen00554() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.07.2013"), 553);
    }

    @Test
    public void test2Gen00555() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.07.2013"), 554);
    }

    @Test
    public void test2Gen00556() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.07.2013"), 555);
    }

    @Test
    public void test2Gen00557() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.07.2013"), 556);
    }

    @Test
    public void test2Gen00558() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.07.2013"), 557);
    }

    @Test
    public void test2Gen00559() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.07.2013"), 558);
    }

    @Test
    public void test2Gen00560() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.07.2013"), 559);
    }

    @Test
    public void test2Gen00561() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.07.2013"), 560);
    }

    @Test
    public void test2Gen00562() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.07.2013"), 561);
    }

    @Test
    public void test2Gen00563() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.07.2013"), 562);
    }

    @Test
    public void test2Gen00564() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.07.2013"), 563);
    }

    @Test
    public void test2Gen00565() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.07.2013"), 564);
    }

    @Test
    public void test2Gen00566() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.07.2013"), 565);
    }

    @Test
    public void test2Gen00567() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.07.2013"), 566);
    }

    @Test
    public void test2Gen00568() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.07.2013"), 567);
    }

    @Test
    public void test2Gen00569() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.07.2013"), 568);
    }

    @Test
    public void test2Gen00570() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.07.2013"), 569);
    }

    @Test
    public void test2Gen00571() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.07.2013"), 570);
    }

    @Test
    public void test2Gen00572() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.07.2013"), 571);
    }

    @Test
    public void test2Gen00573() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.07.2013"), 572);
    }

    @Test
    public void test2Gen00574() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.07.2013"), 573);
    }

    @Test
    public void test2Gen00575() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.07.2013"), 574);
    }

    @Test
    public void test2Gen00576() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.07.2013"), 575);
    }

    @Test
    public void test2Gen00577() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.07.2013"), 576);
    }

    @Test
    public void test2Gen00578() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.07.2013"), 577);
    }

    @Test
    public void test2Gen00579() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.08.2013"), 578);
    }

    @Test
    public void test2Gen00580() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.08.2013"), 579);
    }

    @Test
    public void test2Gen00581() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.08.2013"), 580);
    }

    @Test
    public void test2Gen00582() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.08.2013"), 581);
    }

    @Test
    public void test2Gen00583() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.08.2013"), 582);
    }

    @Test
    public void test2Gen00584() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.08.2013"), 583);
    }

    @Test
    public void test2Gen00585() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.08.2013"), 584);
    }

    @Test
    public void test2Gen00586() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.08.2013"), 585);
    }

    @Test
    public void test2Gen00587() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.08.2013"), 586);
    }

    @Test
    public void test2Gen00588() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.08.2013"), 587);
    }

    @Test
    public void test2Gen00589() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.08.2013"), 588);
    }

    @Test
    public void test2Gen00590() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.08.2013"), 589);
    }

    @Test
    public void test2Gen00591() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.08.2013"), 590);
    }

    @Test
    public void test2Gen00592() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.08.2013"), 591);
    }

    @Test
    public void test2Gen00593() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.08.2013"), 592);
    }

    @Test
    public void test2Gen00594() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.08.2013"), 593);
    }

    @Test
    public void test2Gen00595() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.08.2013"), 594);
    }

    @Test
    public void test2Gen00596() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.08.2013"), 595);
    }

    @Test
    public void test2Gen00597() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.08.2013"), 596);
    }

    @Test
    public void test2Gen00598() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.08.2013"), 597);
    }

    @Test
    public void test2Gen00599() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.08.2013"), 598);
    }

    @Test
    public void test2Gen00600() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.08.2013"), 599);
    }

    @Test
    public void test2Gen00601() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.08.2013"), 600);
    }

    @Test
    public void test2Gen00602() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.08.2013"), 601);
    }

    @Test
    public void test2Gen00603() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.08.2013"), 602);
    }

    @Test
    public void test2Gen00604() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.08.2013"), 603);
    }

    @Test
    public void test2Gen00605() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.08.2013"), 604);
    }

    @Test
    public void test2Gen00606() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.08.2013"), 605);
    }

    @Test
    public void test2Gen00607() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.08.2013"), 606);
    }

    @Test
    public void test2Gen00608() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.08.2013"), 607);
    }

    @Test
    public void test2Gen00609() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.08.2013"), 608);
    }

    @Test
    public void test2Gen00610() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.09.2013"), 609);
    }

    @Test
    public void test2Gen00611() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.09.2013"), 610);
    }

    @Test
    public void test2Gen00612() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.09.2013"), 611);
    }

    @Test
    public void test2Gen00613() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.09.2013"), 612);
    }

    @Test
    public void test2Gen00614() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.09.2013"), 613);
    }

    @Test
    public void test2Gen00615() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.09.2013"), 614);
    }

    @Test
    public void test2Gen00616() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.09.2013"), 615);
    }

    @Test
    public void test2Gen00617() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.09.2013"), 616);
    }

    @Test
    public void test2Gen00618() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.09.2013"), 617);
    }

    @Test
    public void test2Gen00619() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.09.2013"), 618);
    }

    @Test
    public void test2Gen00620() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.09.2013"), 619);
    }

    @Test
    public void test2Gen00621() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.09.2013"), 620);
    }

    @Test
    public void test2Gen00622() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.09.2013"), 621);
    }

    @Test
    public void test2Gen00623() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.09.2013"), 622);
    }

    @Test
    public void test2Gen00624() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.09.2013"), 623);
    }

    @Test
    public void test2Gen00625() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.09.2013"), 624);
    }

    @Test
    public void test2Gen00626() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.09.2013"), 625);
    }

    @Test
    public void test2Gen00627() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.09.2013"), 626);
    }

    @Test
    public void test2Gen00628() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.09.2013"), 627);
    }

    @Test
    public void test2Gen00629() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.09.2013"), 628);
    }

    @Test
    public void test2Gen00630() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.09.2013"), 629);
    }

    @Test
    public void test2Gen00631() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.09.2013"), 630);
    }

    @Test
    public void test2Gen00632() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.09.2013"), 631);
    }

    @Test
    public void test2Gen00633() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.09.2013"), 632);
    }

    @Test
    public void test2Gen00634() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.09.2013"), 633);
    }

    @Test
    public void test2Gen00635() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.09.2013"), 634);
    }

    @Test
    public void test2Gen00636() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.09.2013"), 635);
    }

    @Test
    public void test2Gen00637() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.09.2013"), 636);
    }

    @Test
    public void test2Gen00638() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.09.2013"), 637);
    }

    @Test
    public void test2Gen00639() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.09.2013"), 638);
    }

    @Test
    public void test2Gen00640() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.10.2013"), 639);
    }

    @Test
    public void test2Gen00641() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.10.2013"), 640);
    }

    @Test
    public void test2Gen00642() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.10.2013"), 641);
    }

    @Test
    public void test2Gen00643() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.10.2013"), 642);
    }

    @Test
    public void test2Gen00644() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.10.2013"), 643);
    }

    @Test
    public void test2Gen00645() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.10.2013"), 644);
    }

    @Test
    public void test2Gen00646() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.10.2013"), 645);
    }

    @Test
    public void test2Gen00647() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.10.2013"), 646);
    }

    @Test
    public void test2Gen00648() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.10.2013"), 647);
    }

    @Test
    public void test2Gen00649() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.10.2013"), 648);
    }

    @Test
    public void test2Gen00650() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.10.2013"), 649);
    }

    @Test
    public void test2Gen00651() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.10.2013"), 650);
    }

    @Test
    public void test2Gen00652() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.10.2013"), 651);
    }

    @Test
    public void test2Gen00653() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.10.2013"), 652);
    }

    @Test
    public void test2Gen00654() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.10.2013"), 653);
    }

    @Test
    public void test2Gen00655() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.10.2013"), 654);
    }

    @Test
    public void test2Gen00656() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.10.2013"), 655);
    }

    @Test
    public void test2Gen00657() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.10.2013"), 656);
    }

    @Test
    public void test2Gen00658() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.10.2013"), 657);
    }

    @Test
    public void test2Gen00659() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.10.2013"), 658);
    }

    @Test
    public void test2Gen00660() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.10.2013"), 659);
    }

    @Test
    public void test2Gen00661() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.10.2013"), 660);
    }

    @Test
    public void test2Gen00662() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.10.2013"), 661);
    }

    @Test
    public void test2Gen00663() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.10.2013"), 662);
    }

    @Test
    public void test2Gen00664() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.10.2013"), 663);
    }

    @Test
    public void test2Gen00665() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.10.2013"), 664);
    }

    @Test
    public void test2Gen00666() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.10.2013"), 665);
    }

    @Test
    public void test2Gen00667() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.10.2013"), 666);
    }

    @Test
    public void test2Gen00668() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.10.2013"), 667);
    }

    @Test
    public void test2Gen00669() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.10.2013"), 668);
    }

    @Test
    public void test2Gen00670() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.10.2013"), 669);
    }

    @Test
    public void test2Gen00671() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.11.2013"), 670);
    }

    @Test
    public void test2Gen00672() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.11.2013"), 671);
    }

    @Test
    public void test2Gen00673() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.11.2013"), 672);
    }

    @Test
    public void test2Gen00674() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.11.2013"), 673);
    }

    @Test
    public void test2Gen00675() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.11.2013"), 674);
    }

    @Test
    public void test2Gen00676() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.11.2013"), 675);
    }

    @Test
    public void test2Gen00677() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.11.2013"), 676);
    }

    @Test
    public void test2Gen00678() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.11.2013"), 677);
    }

    @Test
    public void test2Gen00679() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.11.2013"), 678);
    }

    @Test
    public void test2Gen00680() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.11.2013"), 679);
    }

    @Test
    public void test2Gen00681() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.11.2013"), 680);
    }

    @Test
    public void test2Gen00682() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.11.2013"), 681);
    }

    @Test
    public void test2Gen00683() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.11.2013"), 682);
    }

    @Test
    public void test2Gen00684() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.11.2013"), 683);
    }

    @Test
    public void test2Gen00685() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.11.2013"), 684);
    }

    @Test
    public void test2Gen00686() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.11.2013"), 685);
    }

    @Test
    public void test2Gen00687() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.11.2013"), 686);
    }

    @Test
    public void test2Gen00688() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.11.2013"), 687);
    }

    @Test
    public void test2Gen00689() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.11.2013"), 688);
    }

    @Test
    public void test2Gen00690() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.11.2013"), 689);
    }

    @Test
    public void test2Gen00691() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.11.2013"), 690);
    }

    @Test
    public void test2Gen00692() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.11.2013"), 691);
    }

    @Test
    public void test2Gen00693() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.11.2013"), 692);
    }

    @Test
    public void test2Gen00694() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.11.2013"), 693);
    }

    @Test
    public void test2Gen00695() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.11.2013"), 694);
    }

    @Test
    public void test2Gen00696() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.11.2013"), 695);
    }

    @Test
    public void test2Gen00697() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.11.2013"), 696);
    }

    @Test
    public void test2Gen00698() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.11.2013"), 697);
    }

    @Test
    public void test2Gen00699() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.11.2013"), 698);
    }

    @Test
    public void test2Gen00700() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.11.2013"), 699);
    }

    @Test
    public void test2Gen00701() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.12.2013"), 700);
    }

    @Test
    public void test2Gen00702() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.12.2013"), 701);
    }

    @Test
    public void test2Gen00703() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.12.2013"), 702);
    }

    @Test
    public void test2Gen00704() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.12.2013"), 703);
    }

    @Test
    public void test2Gen00705() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.12.2013"), 704);
    }

    @Test
    public void test2Gen00706() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.12.2013"), 705);
    }

    @Test
    public void test2Gen00707() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.12.2013"), 706);
    }

    @Test
    public void test2Gen00708() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.12.2013"), 707);
    }

    @Test
    public void test2Gen00709() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.12.2013"), 708);
    }

    @Test
    public void test2Gen00710() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.12.2013"), 709);
    }

    @Test
    public void test2Gen00711() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.12.2013"), 710);
    }

    @Test
    public void test2Gen00712() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.12.2013"), 711);
    }

    @Test
    public void test2Gen00713() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.12.2013"), 712);
    }

    @Test
    public void test2Gen00714() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.12.2013"), 713);
    }

    @Test
    public void test2Gen00715() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.12.2013"), 714);
    }

    @Test
    public void test2Gen00716() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.12.2013"), 715);
    }

    @Test
    public void test2Gen00717() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.12.2013"), 716);
    }

    @Test
    public void test2Gen00718() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.12.2013"), 717);
    }

    @Test
    public void test2Gen00719() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.12.2013"), 718);
    }

    @Test
    public void test2Gen00720() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.12.2013"), 719);
    }

    @Test
    public void test2Gen00721() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.12.2013"), 720);
    }

    @Test
    public void test2Gen00722() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.12.2013"), 721);
    }

    @Test
    public void test2Gen00723() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.12.2013"), 722);
    }

    @Test
    public void test2Gen00724() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.12.2013"), 723);
    }

    @Test
    public void test2Gen00725() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.12.2013"), 724);
    }

    @Test
    public void test2Gen00726() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.12.2013"), 725);
    }

    @Test
    public void test2Gen00727() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.12.2013"), 726);
    }

    @Test
    public void test2Gen00728() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.12.2013"), 727);
    }

    @Test
    public void test2Gen00729() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.12.2013"), 728);
    }

    @Test
    public void test2Gen00730() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.12.2013"), 729);
    }

    @Test
    public void test2Gen00731() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.12.2013"), 730);
    }

    @Test
    public void test2Gen00732() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.01.2014"), 731);
    }

    @Test
    public void test2Gen00733() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.01.2014"), 732);
    }

    @Test
    public void test2Gen00734() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.01.2014"), 733);
    }

    @Test
    public void test2Gen00735() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.01.2014"), 734);
    }

    @Test
    public void test2Gen00736() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.01.2014"), 735);
    }

    @Test
    public void test2Gen00737() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.01.2014"), 736);
    }

    @Test
    public void test2Gen00738() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.01.2014"), 737);
    }

    @Test
    public void test2Gen00739() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.01.2014"), 738);
    }

    @Test
    public void test2Gen00740() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.01.2014"), 739);
    }

    @Test
    public void test2Gen00741() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.01.2014"), 740);
    }

    @Test
    public void test2Gen00742() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.01.2014"), 741);
    }

    @Test
    public void test2Gen00743() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.01.2014"), 742);
    }

    @Test
    public void test2Gen00744() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.01.2014"), 743);
    }

    @Test
    public void test2Gen00745() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.01.2014"), 744);
    }

    @Test
    public void test2Gen00746() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.01.2014"), 745);
    }

    @Test
    public void test2Gen00747() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.01.2014"), 746);
    }

    @Test
    public void test2Gen00748() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.01.2014"), 747);
    }

    @Test
    public void test2Gen00749() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.01.2014"), 748);
    }

    @Test
    public void test2Gen00750() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.01.2014"), 749);
    }

    @Test
    public void test2Gen00751() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.01.2014"), 750);
    }

    @Test
    public void test2Gen00752() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.01.2014"), 751);
    }

    @Test
    public void test2Gen00753() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.01.2014"), 752);
    }

    @Test
    public void test2Gen00754() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.01.2014"), 753);
    }

    @Test
    public void test2Gen00755() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.01.2014"), 754);
    }

    @Test
    public void test2Gen00756() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.01.2014"), 755);
    }

    @Test
    public void test2Gen00757() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.01.2014"), 756);
    }

    @Test
    public void test2Gen00758() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.01.2014"), 757);
    }

    @Test
    public void test2Gen00759() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.01.2014"), 758);
    }

    @Test
    public void test2Gen00760() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.01.2014"), 759);
    }

    @Test
    public void test2Gen00761() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.01.2014"), 760);
    }

    @Test
    public void test2Gen00762() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.01.2014"), 761);
    }

    @Test
    public void test2Gen00763() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.02.2014"), 762);
    }

    @Test
    public void test2Gen00764() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.02.2014"), 763);
    }

    @Test
    public void test2Gen00765() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.02.2014"), 764);
    }

    @Test
    public void test2Gen00766() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.02.2014"), 765);
    }

    @Test
    public void test2Gen00767() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.02.2014"), 766);
    }

    @Test
    public void test2Gen00768() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.02.2014"), 767);
    }

    @Test
    public void test2Gen00769() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.02.2014"), 768);
    }

    @Test
    public void test2Gen00770() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.02.2014"), 769);
    }

    @Test
    public void test2Gen00771() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.02.2014"), 770);
    }

    @Test
    public void test2Gen00772() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.02.2014"), 771);
    }

    @Test
    public void test2Gen00773() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.02.2014"), 772);
    }

    @Test
    public void test2Gen00774() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.02.2014"), 773);
    }

    @Test
    public void test2Gen00775() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.02.2014"), 774);
    }

    @Test
    public void test2Gen00776() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.02.2014"), 775);
    }

    @Test
    public void test2Gen00777() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.02.2014"), 776);
    }

    @Test
    public void test2Gen00778() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.02.2014"), 777);
    }

    @Test
    public void test2Gen00779() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.02.2014"), 778);
    }

    @Test
    public void test2Gen00780() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.02.2014"), 779);
    }

    @Test
    public void test2Gen00781() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.02.2014"), 780);
    }

    @Test
    public void test2Gen00782() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.02.2014"), 781);
    }

    @Test
    public void test2Gen00783() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.02.2014"), 782);
    }

    @Test
    public void test2Gen00784() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.02.2014"), 783);
    }

    @Test
    public void test2Gen00785() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.02.2014"), 784);
    }

    @Test
    public void test2Gen00786() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.02.2014"), 785);
    }

    @Test
    public void test2Gen00787() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.02.2014"), 786);
    }

    @Test
    public void test2Gen00788() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.02.2014"), 787);
    }

    @Test
    public void test2Gen00789() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.02.2014"), 788);
    }

    @Test
    public void test2Gen00790() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.02.2014"), 789);
    }

    @Test
    public void test2Gen00791() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.03.2014"), 790);
    }

    @Test
    public void test2Gen00792() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.03.2014"), 791);
    }

    @Test
    public void test2Gen00793() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.03.2014"), 792);
    }

    @Test
    public void test2Gen00794() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.03.2014"), 793);
    }

    @Test
    public void test2Gen00795() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.03.2014"), 794);
    }

    @Test
    public void test2Gen00796() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.03.2014"), 795);
    }

    @Test
    public void test2Gen00797() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.03.2014"), 796);
    }

    @Test
    public void test2Gen00798() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.03.2014"), 797);
    }

    @Test
    public void test2Gen00799() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.03.2014"), 798);
    }

    @Test
    public void test2Gen00800() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.03.2014"), 799);
    }

    @Test
    public void test2Gen00801() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.03.2014"), 800);
    }

    @Test
    public void test2Gen00802() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.03.2014"), 801);
    }

    @Test
    public void test2Gen00803() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.03.2014"), 802);
    }

    @Test
    public void test2Gen00804() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.03.2014"), 803);
    }

    @Test
    public void test2Gen00805() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.03.2014"), 804);
    }

    @Test
    public void test2Gen00806() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.03.2014"), 805);
    }

    @Test
    public void test2Gen00807() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.03.2014"), 806);
    }

    @Test
    public void test2Gen00808() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.03.2014"), 807);
    }

    @Test
    public void test2Gen00809() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.03.2014"), 808);
    }

    @Test
    public void test2Gen00810() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.03.2014"), 809);
    }

    @Test
    public void test2Gen00811() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.03.2014"), 810);
    }

    @Test
    public void test2Gen00812() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.03.2014"), 811);
    }

    @Test
    public void test2Gen00813() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.03.2014"), 812);
    }

    @Test
    public void test2Gen00814() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.03.2014"), 813);
    }

    @Test
    public void test2Gen00815() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.03.2014"), 814);
    }

    @Test
    public void test2Gen00816() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.03.2014"), 815);
    }

    @Test
    public void test2Gen00817() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.03.2014"), 816);
    }

    @Test
    public void test2Gen00818() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.03.2014"), 817);
    }

    @Test
    public void test2Gen00819() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.03.2014"), 818);
    }

    @Test
    public void test2Gen00820() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.03.2014"), 819);
    }

    @Test
    public void test2Gen00821() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.03.2014"), 820);
    }

    @Test
    public void test2Gen00822() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.04.2014"), 821);
    }

    @Test
    public void test2Gen00823() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.04.2014"), 822);
    }

    @Test
    public void test2Gen00824() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.04.2014"), 823);
    }

    @Test
    public void test2Gen00825() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.04.2014"), 824);
    }

    @Test
    public void test2Gen00826() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.04.2014"), 825);
    }

    @Test
    public void test2Gen00827() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.04.2014"), 826);
    }

    @Test
    public void test2Gen00828() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.04.2014"), 827);
    }

    @Test
    public void test2Gen00829() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.04.2014"), 828);
    }

    @Test
    public void test2Gen00830() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.04.2014"), 829);
    }

    @Test
    public void test2Gen00831() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.04.2014"), 830);
    }

    @Test
    public void test2Gen00832() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.04.2014"), 831);
    }

    @Test
    public void test2Gen00833() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.04.2014"), 832);
    }

    @Test
    public void test2Gen00834() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.04.2014"), 833);
    }

    @Test
    public void test2Gen00835() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.04.2014"), 834);
    }

    @Test
    public void test2Gen00836() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.04.2014"), 835);
    }

    @Test
    public void test2Gen00837() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.04.2014"), 836);
    }

    @Test
    public void test2Gen00838() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.04.2014"), 837);
    }

    @Test
    public void test2Gen00839() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.04.2014"), 838);
    }

    @Test
    public void test2Gen00840() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.04.2014"), 839);
    }

    @Test
    public void test2Gen00841() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.04.2014"), 840);
    }

    @Test
    public void test2Gen00842() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.04.2014"), 841);
    }

    @Test
    public void test2Gen00843() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.04.2014"), 842);
    }

    @Test
    public void test2Gen00844() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.04.2014"), 843);
    }

    @Test
    public void test2Gen00845() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.04.2014"), 844);
    }

    @Test
    public void test2Gen00846() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.04.2014"), 845);
    }

    @Test
    public void test2Gen00847() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.04.2014"), 846);
    }

    @Test
    public void test2Gen00848() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.04.2014"), 847);
    }

    @Test
    public void test2Gen00849() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.04.2014"), 848);
    }

    @Test
    public void test2Gen00850() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.04.2014"), 849);
    }

    @Test
    public void test2Gen00851() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.04.2014"), 850);
    }

    @Test
    public void test2Gen00852() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.05.2014"), 851);
    }

    @Test
    public void test2Gen00853() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.05.2014"), 852);
    }

    @Test
    public void test2Gen00854() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.05.2014"), 853);
    }

    @Test
    public void test2Gen00855() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.05.2014"), 854);
    }

    @Test
    public void test2Gen00856() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.05.2014"), 855);
    }

    @Test
    public void test2Gen00857() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.05.2014"), 856);
    }

    @Test
    public void test2Gen00858() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.05.2014"), 857);
    }

    @Test
    public void test2Gen00859() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.05.2014"), 858);
    }

    @Test
    public void test2Gen00860() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.05.2014"), 859);
    }

    @Test
    public void test2Gen00861() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.05.2014"), 860);
    }

    @Test
    public void test2Gen00862() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.05.2014"), 861);
    }

    @Test
    public void test2Gen00863() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.05.2014"), 862);
    }

    @Test
    public void test2Gen00864() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.05.2014"), 863);
    }

    @Test
    public void test2Gen00865() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.05.2014"), 864);
    }

    @Test
    public void test2Gen00866() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.05.2014"), 865);
    }

    @Test
    public void test2Gen00867() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.05.2014"), 866);
    }

    @Test
    public void test2Gen00868() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.05.2014"), 867);
    }

    @Test
    public void test2Gen00869() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.05.2014"), 868);
    }

    @Test
    public void test2Gen00870() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.05.2014"), 869);
    }

    @Test
    public void test2Gen00871() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.05.2014"), 870);
    }

    @Test
    public void test2Gen00872() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.05.2014"), 871);
    }

    @Test
    public void test2Gen00873() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.05.2014"), 872);
    }

    @Test
    public void test2Gen00874() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.05.2014"), 873);
    }

    @Test
    public void test2Gen00875() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.05.2014"), 874);
    }

    @Test
    public void test2Gen00876() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.05.2014"), 875);
    }

    @Test
    public void test2Gen00877() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.05.2014"), 876);
    }

    @Test
    public void test2Gen00878() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.05.2014"), 877);
    }

    @Test
    public void test2Gen00879() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.05.2014"), 878);
    }

    @Test
    public void test2Gen00880() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.05.2014"), 879);
    }

    @Test
    public void test2Gen00881() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.05.2014"), 880);
    }

    @Test
    public void test2Gen00882() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.05.2014"), 881);
    }

    @Test
    public void test2Gen00883() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.06.2014"), 882);
    }

    @Test
    public void test2Gen00884() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.06.2014"), 883);
    }

    @Test
    public void test2Gen00885() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.06.2014"), 884);
    }

    @Test
    public void test2Gen00886() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.06.2014"), 885);
    }

    @Test
    public void test2Gen00887() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.06.2014"), 886);
    }

    @Test
    public void test2Gen00888() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.06.2014"), 887);
    }

    @Test
    public void test2Gen00889() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.06.2014"), 888);
    }

    @Test
    public void test2Gen00890() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.06.2014"), 889);
    }

    @Test
    public void test2Gen00891() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.06.2014"), 890);
    }

    @Test
    public void test2Gen00892() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.06.2014"), 891);
    }

    @Test
    public void test2Gen00893() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.06.2014"), 892);
    }

    @Test
    public void test2Gen00894() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.06.2014"), 893);
    }

    @Test
    public void test2Gen00895() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.06.2014"), 894);
    }

    @Test
    public void test2Gen00896() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.06.2014"), 895);
    }

    @Test
    public void test2Gen00897() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.06.2014"), 896);
    }

    @Test
    public void test2Gen00898() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.06.2014"), 897);
    }

    @Test
    public void test2Gen00899() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.06.2014"), 898);
    }

    @Test
    public void test2Gen00900() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.06.2014"), 899);
    }

    @Test
    public void test2Gen00901() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.06.2014"), 900);
    }

    @Test
    public void test2Gen00902() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.06.2014"), 901);
    }

    @Test
    public void test2Gen00903() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.06.2014"), 902);
    }

    @Test
    public void test2Gen00904() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.06.2014"), 903);
    }

    @Test
    public void test2Gen00905() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.06.2014"), 904);
    }

    @Test
    public void test2Gen00906() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.06.2014"), 905);
    }

    @Test
    public void test2Gen00907() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.06.2014"), 906);
    }

    @Test
    public void test2Gen00908() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.06.2014"), 907);
    }

    @Test
    public void test2Gen00909() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.06.2014"), 908);
    }

    @Test
    public void test2Gen00910() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.06.2014"), 909);
    }

    @Test
    public void test2Gen00911() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.06.2014"), 910);
    }

    @Test
    public void test2Gen00912() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.06.2014"), 911);
    }

    @Test
    public void test2Gen00913() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.07.2014"), 912);
    }

    @Test
    public void test2Gen00914() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.07.2014"), 913);
    }

    @Test
    public void test2Gen00915() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.07.2014"), 914);
    }

    @Test
    public void test2Gen00916() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.07.2014"), 915);
    }

    @Test
    public void test2Gen00917() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.07.2014"), 916);
    }

    @Test
    public void test2Gen00918() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.07.2014"), 917);
    }

    @Test
    public void test2Gen00919() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.07.2014"), 918);
    }

    @Test
    public void test2Gen00920() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.07.2014"), 919);
    }

    @Test
    public void test2Gen00921() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.07.2014"), 920);
    }

    @Test
    public void test2Gen00922() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.07.2014"), 921);
    }

    @Test
    public void test2Gen00923() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.07.2014"), 922);
    }

    @Test
    public void test2Gen00924() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.07.2014"), 923);
    }

    @Test
    public void test2Gen00925() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.07.2014"), 924);
    }

    @Test
    public void test2Gen00926() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.07.2014"), 925);
    }

    @Test
    public void test2Gen00927() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.07.2014"), 926);
    }

    @Test
    public void test2Gen00928() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.07.2014"), 927);
    }

    @Test
    public void test2Gen00929() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.07.2014"), 928);
    }

    @Test
    public void test2Gen00930() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.07.2014"), 929);
    }

    @Test
    public void test2Gen00931() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.07.2014"), 930);
    }

    @Test
    public void test2Gen00932() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.07.2014"), 931);
    }

    @Test
    public void test2Gen00933() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.07.2014"), 932);
    }

    @Test
    public void test2Gen00934() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.07.2014"), 933);
    }

    @Test
    public void test2Gen00935() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.07.2014"), 934);
    }

    @Test
    public void test2Gen00936() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.07.2014"), 935);
    }

    @Test
    public void test2Gen00937() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.07.2014"), 936);
    }

    @Test
    public void test2Gen00938() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.07.2014"), 937);
    }

    @Test
    public void test2Gen00939() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.07.2014"), 938);
    }

    @Test
    public void test2Gen00940() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.07.2014"), 939);
    }

    @Test
    public void test2Gen00941() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.07.2014"), 940);
    }

    @Test
    public void test2Gen00942() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.07.2014"), 941);
    }

    @Test
    public void test2Gen00943() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.07.2014"), 942);
    }

    @Test
    public void test2Gen00944() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.08.2014"), 943);
    }

    @Test
    public void test2Gen00945() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.08.2014"), 944);
    }

    @Test
    public void test2Gen00946() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.08.2014"), 945);
    }

    @Test
    public void test2Gen00947() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.08.2014"), 946);
    }

    @Test
    public void test2Gen00948() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.08.2014"), 947);
    }

    @Test
    public void test2Gen00949() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.08.2014"), 948);
    }

    @Test
    public void test2Gen00950() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.08.2014"), 949);
    }

    @Test
    public void test2Gen00951() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.08.2014"), 950);
    }

    @Test
    public void test2Gen00952() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.08.2014"), 951);
    }

    @Test
    public void test2Gen00953() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.08.2014"), 952);
    }

    @Test
    public void test2Gen00954() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.08.2014"), 953);
    }

    @Test
    public void test2Gen00955() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.08.2014"), 954);
    }

    @Test
    public void test2Gen00956() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.08.2014"), 955);
    }

    @Test
    public void test2Gen00957() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.08.2014"), 956);
    }

    @Test
    public void test2Gen00958() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.08.2014"), 957);
    }

    @Test
    public void test2Gen00959() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.08.2014"), 958);
    }

    @Test
    public void test2Gen00960() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.08.2014"), 959);
    }

    @Test
    public void test2Gen00961() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.08.2014"), 960);
    }

    @Test
    public void test2Gen00962() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.08.2014"), 961);
    }

    @Test
    public void test2Gen00963() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.08.2014"), 962);
    }

    @Test
    public void test2Gen00964() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.08.2014"), 963);
    }

    @Test
    public void test2Gen00965() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.08.2014"), 964);
    }

    @Test
    public void test2Gen00966() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.08.2014"), 965);
    }

    @Test
    public void test2Gen00967() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.08.2014"), 966);
    }

    @Test
    public void test2Gen00968() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.08.2014"), 967);
    }

    @Test
    public void test2Gen00969() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.08.2014"), 968);
    }

    @Test
    public void test2Gen00970() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.08.2014"), 969);
    }

    @Test
    public void test2Gen00971() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.08.2014"), 970);
    }

    @Test
    public void test2Gen00972() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.08.2014"), 971);
    }

    @Test
    public void test2Gen00973() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.08.2014"), 972);
    }

    @Test
    public void test2Gen00974() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.08.2014"), 973);
    }

    @Test
    public void test2Gen00975() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.09.2014"), 974);
    }

    @Test
    public void test2Gen00976() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.09.2014"), 975);
    }

    @Test
    public void test2Gen00977() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.09.2014"), 976);
    }

    @Test
    public void test2Gen00978() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.09.2014"), 977);
    }

    @Test
    public void test2Gen00979() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.09.2014"), 978);
    }

    @Test
    public void test2Gen00980() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.09.2014"), 979);
    }

    @Test
    public void test2Gen00981() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.09.2014"), 980);
    }

    @Test
    public void test2Gen00982() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.09.2014"), 981);
    }

    @Test
    public void test2Gen00983() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.09.2014"), 982);
    }

    @Test
    public void test2Gen00984() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.09.2014"), 983);
    }

    @Test
    public void test2Gen00985() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.09.2014"), 984);
    }

    @Test
    public void test2Gen00986() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.09.2014"), 985);
    }

    @Test
    public void test2Gen00987() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.09.2014"), 986);
    }

    @Test
    public void test2Gen00988() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.09.2014"), 987);
    }

    @Test
    public void test2Gen00989() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.09.2014"), 988);
    }

    @Test
    public void test2Gen00990() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.09.2014"), 989);
    }

    @Test
    public void test2Gen00991() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.09.2014"), 990);
    }

    @Test
    public void test2Gen00992() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.09.2014"), 991);
    }

    @Test
    public void test2Gen00993() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.09.2014"), 992);
    }

    @Test
    public void test2Gen00994() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.09.2014"), 993);
    }

    @Test
    public void test2Gen00995() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.09.2014"), 994);
    }

    @Test
    public void test2Gen00996() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.09.2014"), 995);
    }

    @Test
    public void test2Gen00997() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.09.2014"), 996);
    }

    @Test
    public void test2Gen00998() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.09.2014"), 997);
    }

    @Test
    public void test2Gen00999() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.09.2014"), 998);
    }

    @Test
    public void test2Gen01000() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.09.2014"), 999);
    }

    @Test
    public void test2Gen01001() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.09.2014"), 1000);
    }

    @Test
    public void test2Gen01002() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.09.2014"), 1001);
    }

    @Test
    public void test2Gen01003() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.09.2014"), 1002);
    }

    @Test
    public void test2Gen01004() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.09.2014"), 1003);
    }

    @Test
    public void test2Gen01005() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.10.2014"), 1004);
    }

    @Test
    public void test2Gen01006() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.10.2014"), 1005);
    }

    @Test
    public void test2Gen01007() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.10.2014"), 1006);
    }

    @Test
    public void test2Gen01008() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.10.2014"), 1007);
    }

    @Test
    public void test2Gen01009() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.10.2014"), 1008);
    }

    @Test
    public void test2Gen01010() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.10.2014"), 1009);
    }

    @Test
    public void test2Gen01011() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.10.2014"), 1010);
    }

    @Test
    public void test2Gen01012() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.10.2014"), 1011);
    }

    @Test
    public void test2Gen01013() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.10.2014"), 1012);
    }

    @Test
    public void test2Gen01014() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.10.2014"), 1013);
    }

    @Test
    public void test2Gen01015() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.10.2014"), 1014);
    }

    @Test
    public void test2Gen01016() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.10.2014"), 1015);
    }

    @Test
    public void test2Gen01017() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.10.2014"), 1016);
    }

    @Test
    public void test2Gen01018() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.10.2014"), 1017);
    }

    @Test
    public void test2Gen01019() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.10.2014"), 1018);
    }

    @Test
    public void test2Gen01020() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.10.2014"), 1019);
    }

    @Test
    public void test2Gen01021() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.10.2014"), 1020);
    }

    @Test
    public void test2Gen01022() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.10.2014"), 1021);
    }

    @Test
    public void test2Gen01023() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.10.2014"), 1022);
    }

    @Test
    public void test2Gen01024() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.10.2014"), 1023);
    }

    @Test
    public void test2Gen01025() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.10.2014"), 1024);
    }

    @Test
    public void test2Gen01026() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.10.2014"), 1025);
    }

    @Test
    public void test2Gen01027() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.10.2014"), 1026);
    }

    @Test
    public void test2Gen01028() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.10.2014"), 1027);
    }

    @Test
    public void test2Gen01029() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.10.2014"), 1028);
    }

    @Test
    public void test2Gen01030() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.10.2014"), 1029);
    }

    @Test
    public void test2Gen01031() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.10.2014"), 1030);
    }

    @Test
    public void test2Gen01032() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.10.2014"), 1031);
    }

    @Test
    public void test2Gen01033() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.10.2014"), 1032);
    }

    @Test
    public void test2Gen01034() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.10.2014"), 1033);
    }

    @Test
    public void test2Gen01035() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.10.2014"), 1034);
    }

    @Test
    public void test2Gen01036() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.11.2014"), 1035);
    }

    @Test
    public void test2Gen01037() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.11.2014"), 1036);
    }

    @Test
    public void test2Gen01038() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.11.2014"), 1037);
    }

    @Test
    public void test2Gen01039() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.11.2014"), 1038);
    }

    @Test
    public void test2Gen01040() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.11.2014"), 1039);
    }

    @Test
    public void test2Gen01041() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.11.2014"), 1040);
    }

    @Test
    public void test2Gen01042() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.11.2014"), 1041);
    }

    @Test
    public void test2Gen01043() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.11.2014"), 1042);
    }

    @Test
    public void test2Gen01044() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.11.2014"), 1043);
    }

    @Test
    public void test2Gen01045() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.11.2014"), 1044);
    }

    @Test
    public void test2Gen01046() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.11.2014"), 1045);
    }

    @Test
    public void test2Gen01047() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.11.2014"), 1046);
    }

    @Test
    public void test2Gen01048() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.11.2014"), 1047);
    }

    @Test
    public void test2Gen01049() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.11.2014"), 1048);
    }

    @Test
    public void test2Gen01050() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.11.2014"), 1049);
    }

    @Test
    public void test2Gen01051() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.11.2014"), 1050);
    }

    @Test
    public void test2Gen01052() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.11.2014"), 1051);
    }

    @Test
    public void test2Gen01053() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.11.2014"), 1052);
    }

    @Test
    public void test2Gen01054() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.11.2014"), 1053);
    }

    @Test
    public void test2Gen01055() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.11.2014"), 1054);
    }

    @Test
    public void test2Gen01056() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.11.2014"), 1055);
    }

    @Test
    public void test2Gen01057() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.11.2014"), 1056);
    }

    @Test
    public void test2Gen01058() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.11.2014"), 1057);
    }

    @Test
    public void test2Gen01059() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.11.2014"), 1058);
    }

    @Test
    public void test2Gen01060() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.11.2014"), 1059);
    }

    @Test
    public void test2Gen01061() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.11.2014"), 1060);
    }

    @Test
    public void test2Gen01062() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.11.2014"), 1061);
    }

    @Test
    public void test2Gen01063() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.11.2014"), 1062);
    }

    @Test
    public void test2Gen01064() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.11.2014"), 1063);
    }

    @Test
    public void test2Gen01065() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.11.2014"), 1064);
    }

    @Test
    public void test2Gen01066() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.12.2014"), 1065);
    }

    @Test
    public void test2Gen01067() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.12.2014"), 1066);
    }

    @Test
    public void test2Gen01068() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.12.2014"), 1067);
    }

    @Test
    public void test2Gen01069() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.12.2014"), 1068);
    }

    @Test
    public void test2Gen01070() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.12.2014"), 1069);
    }

    @Test
    public void test2Gen01071() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.12.2014"), 1070);
    }

    @Test
    public void test2Gen01072() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.12.2014"), 1071);
    }

    @Test
    public void test2Gen01073() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.12.2014"), 1072);
    }

    @Test
    public void test2Gen01074() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.12.2014"), 1073);
    }

    @Test
    public void test2Gen01075() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.12.2014"), 1074);
    }

    @Test
    public void test2Gen01076() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.12.2014"), 1075);
    }

    @Test
    public void test2Gen01077() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.12.2014"), 1076);
    }

    @Test
    public void test2Gen01078() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.12.2014"), 1077);
    }

    @Test
    public void test2Gen01079() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.12.2014"), 1078);
    }

    @Test
    public void test2Gen01080() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.12.2014"), 1079);
    }

    @Test
    public void test2Gen01081() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.12.2014"), 1080);
    }

    @Test
    public void test2Gen01082() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.12.2014"), 1081);
    }

    @Test
    public void test2Gen01083() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.12.2014"), 1082);
    }

    @Test
    public void test2Gen01084() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.12.2014"), 1083);
    }

    @Test
    public void test2Gen01085() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.12.2014"), 1084);
    }

    @Test
    public void test2Gen01086() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.12.2014"), 1085);
    }

    @Test
    public void test2Gen01087() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.12.2014"), 1086);
    }

    @Test
    public void test2Gen01088() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.12.2014"), 1087);
    }

    @Test
    public void test2Gen01089() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.12.2014"), 1088);
    }

    @Test
    public void test2Gen01090() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.12.2014"), 1089);
    }

    @Test
    public void test2Gen01091() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.12.2014"), 1090);
    }

    @Test
    public void test2Gen01092() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.12.2014"), 1091);
    }

    @Test
    public void test2Gen01093() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.12.2014"), 1092);
    }

    @Test
    public void test2Gen01094() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.12.2014"), 1093);
    }

    @Test
    public void test2Gen01095() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.12.2014"), 1094);
    }

    @Test
    public void test2Gen01096() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.12.2014"), 1095);
    }

    @Test
    public void test2Gen01097() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.01.2015"), 1096);
    }

    @Test
    public void test2Gen01098() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.01.2015"), 1097);
    }

    @Test
    public void test2Gen01099() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.01.2015"), 1098);
    }

    @Test
    public void test2Gen01100() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.01.2015"), 1099);
    }

    @Test
    public void test2Gen01101() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.01.2015"), 1100);
    }

    @Test
    public void test2Gen01102() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.01.2015"), 1101);
    }

    @Test
    public void test2Gen01103() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.01.2015"), 1102);
    }

    @Test
    public void test2Gen01104() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.01.2015"), 1103);
    }

    @Test
    public void test2Gen01105() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.01.2015"), 1104);
    }

    @Test
    public void test2Gen01106() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.01.2015"), 1105);
    }

    @Test
    public void test2Gen01107() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.01.2015"), 1106);
    }

    @Test
    public void test2Gen01108() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.01.2015"), 1107);
    }

    @Test
    public void test2Gen01109() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.01.2015"), 1108);
    }

    @Test
    public void test2Gen01110() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.01.2015"), 1109);
    }

    @Test
    public void test2Gen01111() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.01.2015"), 1110);
    }

    @Test
    public void test2Gen01112() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.01.2015"), 1111);
    }

    @Test
    public void test2Gen01113() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.01.2015"), 1112);
    }

    @Test
    public void test2Gen01114() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.01.2015"), 1113);
    }

    @Test
    public void test2Gen01115() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.01.2015"), 1114);
    }

    @Test
    public void test2Gen01116() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.01.2015"), 1115);
    }

    @Test
    public void test2Gen01117() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.01.2015"), 1116);
    }

    @Test
    public void test2Gen01118() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.01.2015"), 1117);
    }

    @Test
    public void test2Gen01119() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.01.2015"), 1118);
    }

    @Test
    public void test2Gen01120() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.01.2015"), 1119);
    }

    @Test
    public void test2Gen01121() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.01.2015"), 1120);
    }

    @Test
    public void test2Gen01122() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.01.2015"), 1121);
    }

    @Test
    public void test2Gen01123() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.01.2015"), 1122);
    }

    @Test
    public void test2Gen01124() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.01.2015"), 1123);
    }

    @Test
    public void test2Gen01125() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.01.2015"), 1124);
    }

    @Test
    public void test2Gen01126() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.01.2015"), 1125);
    }

    @Test
    public void test2Gen01127() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.01.2015"), 1126);
    }

    @Test
    public void test2Gen01128() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.02.2015"), 1127);
    }

    @Test
    public void test2Gen01129() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.02.2015"), 1128);
    }

    @Test
    public void test2Gen01130() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.02.2015"), 1129);
    }

    @Test
    public void test2Gen01131() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.02.2015"), 1130);
    }

    @Test
    public void test2Gen01132() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.02.2015"), 1131);
    }

    @Test
    public void test2Gen01133() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.02.2015"), 1132);
    }

    @Test
    public void test2Gen01134() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.02.2015"), 1133);
    }

    @Test
    public void test2Gen01135() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.02.2015"), 1134);
    }

    @Test
    public void test2Gen01136() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.02.2015"), 1135);
    }

    @Test
    public void test2Gen01137() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.02.2015"), 1136);
    }

    @Test
    public void test2Gen01138() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.02.2015"), 1137);
    }

    @Test
    public void test2Gen01139() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.02.2015"), 1138);
    }

    @Test
    public void test2Gen01140() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.02.2015"), 1139);
    }

    @Test
    public void test2Gen01141() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.02.2015"), 1140);
    }

    @Test
    public void test2Gen01142() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.02.2015"), 1141);
    }

    @Test
    public void test2Gen01143() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.02.2015"), 1142);
    }

    @Test
    public void test2Gen01144() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.02.2015"), 1143);
    }

    @Test
    public void test2Gen01145() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.02.2015"), 1144);
    }

    @Test
    public void test2Gen01146() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.02.2015"), 1145);
    }

    @Test
    public void test2Gen01147() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.02.2015"), 1146);
    }

    @Test
    public void test2Gen01148() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.02.2015"), 1147);
    }

    @Test
    public void test2Gen01149() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.02.2015"), 1148);
    }

    @Test
    public void test2Gen01150() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.02.2015"), 1149);
    }

    @Test
    public void test2Gen01151() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.02.2015"), 1150);
    }

    @Test
    public void test2Gen01152() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.02.2015"), 1151);
    }

    @Test
    public void test2Gen01153() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.02.2015"), 1152);
    }

    @Test
    public void test2Gen01154() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.02.2015"), 1153);
    }

    @Test
    public void test2Gen01155() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.02.2015"), 1154);
    }

    @Test
    public void test2Gen01156() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.03.2015"), 1155);
    }

    @Test
    public void test2Gen01157() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.03.2015"), 1156);
    }

    @Test
    public void test2Gen01158() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.03.2015"), 1157);
    }

    @Test
    public void test2Gen01159() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.03.2015"), 1158);
    }

    @Test
    public void test2Gen01160() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.03.2015"), 1159);
    }

    @Test
    public void test2Gen01161() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.03.2015"), 1160);
    }

    @Test
    public void test2Gen01162() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.03.2015"), 1161);
    }

    @Test
    public void test2Gen01163() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.03.2015"), 1162);
    }

    @Test
    public void test2Gen01164() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.03.2015"), 1163);
    }

    @Test
    public void test2Gen01165() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.03.2015"), 1164);
    }

    @Test
    public void test2Gen01166() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.03.2015"), 1165);
    }

    @Test
    public void test2Gen01167() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.03.2015"), 1166);
    }

    @Test
    public void test2Gen01168() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.03.2015"), 1167);
    }

    @Test
    public void test2Gen01169() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.03.2015"), 1168);
    }

    @Test
    public void test2Gen01170() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.03.2015"), 1169);
    }

    @Test
    public void test2Gen01171() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.03.2015"), 1170);
    }

    @Test
    public void test2Gen01172() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.03.2015"), 1171);
    }

    @Test
    public void test2Gen01173() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.03.2015"), 1172);
    }

    @Test
    public void test2Gen01174() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.03.2015"), 1173);
    }

    @Test
    public void test2Gen01175() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.03.2015"), 1174);
    }

    @Test
    public void test2Gen01176() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.03.2015"), 1175);
    }

    @Test
    public void test2Gen01177() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.03.2015"), 1176);
    }

    @Test
    public void test2Gen01178() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.03.2015"), 1177);
    }

    @Test
    public void test2Gen01179() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.03.2015"), 1178);
    }

    @Test
    public void test2Gen01180() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.03.2015"), 1179);
    }

    @Test
    public void test2Gen01181() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.03.2015"), 1180);
    }

    @Test
    public void test2Gen01182() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.03.2015"), 1181);
    }

    @Test
    public void test2Gen01183() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.03.2015"), 1182);
    }

    @Test
    public void test2Gen01184() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.03.2015"), 1183);
    }

    @Test
    public void test2Gen01185() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.03.2015"), 1184);
    }

    @Test
    public void test2Gen01186() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.03.2015"), 1185);
    }

    @Test
    public void test2Gen01187() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.04.2015"), 1186);
    }

    @Test
    public void test2Gen01188() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.04.2015"), 1187);
    }

    @Test
    public void test2Gen01189() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.04.2015"), 1188);
    }

    @Test
    public void test2Gen01190() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.04.2015"), 1189);
    }

    @Test
    public void test2Gen01191() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.04.2015"), 1190);
    }

    @Test
    public void test2Gen01192() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.04.2015"), 1191);
    }

    @Test
    public void test2Gen01193() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.04.2015"), 1192);
    }

    @Test
    public void test2Gen01194() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.04.2015"), 1193);
    }

    @Test
    public void test2Gen01195() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.04.2015"), 1194);
    }

    @Test
    public void test2Gen01196() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.04.2015"), 1195);
    }

    @Test
    public void test2Gen01197() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.04.2015"), 1196);
    }

    @Test
    public void test2Gen01198() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.04.2015"), 1197);
    }

    @Test
    public void test2Gen01199() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.04.2015"), 1198);
    }

    @Test
    public void test2Gen01200() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.04.2015"), 1199);
    }

    @Test
    public void test2Gen01201() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.04.2015"), 1200);
    }

    @Test
    public void test2Gen01202() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.04.2015"), 1201);
    }

    @Test
    public void test2Gen01203() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.04.2015"), 1202);
    }

    @Test
    public void test2Gen01204() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.04.2015"), 1203);
    }

    @Test
    public void test2Gen01205() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.04.2015"), 1204);
    }

    @Test
    public void test2Gen01206() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.04.2015"), 1205);
    }

    @Test
    public void test2Gen01207() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.04.2015"), 1206);
    }

    @Test
    public void test2Gen01208() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.04.2015"), 1207);
    }

    @Test
    public void test2Gen01209() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.04.2015"), 1208);
    }

    @Test
    public void test2Gen01210() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.04.2015"), 1209);
    }

    @Test
    public void test2Gen01211() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.04.2015"), 1210);
    }

    @Test
    public void test2Gen01212() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.04.2015"), 1211);
    }

    @Test
    public void test2Gen01213() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.04.2015"), 1212);
    }

    @Test
    public void test2Gen01214() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.04.2015"), 1213);
    }

    @Test
    public void test2Gen01215() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.04.2015"), 1214);
    }

    @Test
    public void test2Gen01216() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.04.2015"), 1215);
    }

    @Test
    public void test2Gen01217() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.05.2015"), 1216);
    }

    @Test
    public void test2Gen01218() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.05.2015"), 1217);
    }

    @Test
    public void test2Gen01219() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.05.2015"), 1218);
    }

    @Test
    public void test2Gen01220() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.05.2015"), 1219);
    }

    @Test
    public void test2Gen01221() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.05.2015"), 1220);
    }

    @Test
    public void test2Gen01222() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.05.2015"), 1221);
    }

    @Test
    public void test2Gen01223() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.05.2015"), 1222);
    }

    @Test
    public void test2Gen01224() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.05.2015"), 1223);
    }

    @Test
    public void test2Gen01225() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.05.2015"), 1224);
    }

    @Test
    public void test2Gen01226() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.05.2015"), 1225);
    }

    @Test
    public void test2Gen01227() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.05.2015"), 1226);
    }

    @Test
    public void test2Gen01228() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.05.2015"), 1227);
    }

    @Test
    public void test2Gen01229() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.05.2015"), 1228);
    }

    @Test
    public void test2Gen01230() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.05.2015"), 1229);
    }

    @Test
    public void test2Gen01231() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.05.2015"), 1230);
    }

    @Test
    public void test2Gen01232() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.05.2015"), 1231);
    }

    @Test
    public void test2Gen01233() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.05.2015"), 1232);
    }

    @Test
    public void test2Gen01234() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.05.2015"), 1233);
    }

    @Test
    public void test2Gen01235() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.05.2015"), 1234);
    }

    @Test
    public void test2Gen01236() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.05.2015"), 1235);
    }

    @Test
    public void test2Gen01237() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.05.2015"), 1236);
    }

    @Test
    public void test2Gen01238() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.05.2015"), 1237);
    }

    @Test
    public void test2Gen01239() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.05.2015"), 1238);
    }

    @Test
    public void test2Gen01240() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.05.2015"), 1239);
    }

    @Test
    public void test2Gen01241() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.05.2015"), 1240);
    }

    @Test
    public void test2Gen01242() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.05.2015"), 1241);
    }

    @Test
    public void test2Gen01243() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.05.2015"), 1242);
    }

    @Test
    public void test2Gen01244() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.05.2015"), 1243);
    }

    @Test
    public void test2Gen01245() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.05.2015"), 1244);
    }

    @Test
    public void test2Gen01246() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.05.2015"), 1245);
    }

    @Test
    public void test2Gen01247() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.05.2015"), 1246);
    }

    @Test
    public void test2Gen01248() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.06.2015"), 1247);
    }

    @Test
    public void test2Gen01249() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.06.2015"), 1248);
    }

    @Test
    public void test2Gen01250() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.06.2015"), 1249);
    }

    @Test
    public void test2Gen01251() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.06.2015"), 1250);
    }

    @Test
    public void test2Gen01252() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.06.2015"), 1251);
    }

    @Test
    public void test2Gen01253() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.06.2015"), 1252);
    }

    @Test
    public void test2Gen01254() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.06.2015"), 1253);
    }

    @Test
    public void test2Gen01255() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.06.2015"), 1254);
    }

    @Test
    public void test2Gen01256() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.06.2015"), 1255);
    }

    @Test
    public void test2Gen01257() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.06.2015"), 1256);
    }

    @Test
    public void test2Gen01258() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.06.2015"), 1257);
    }

    @Test
    public void test2Gen01259() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.06.2015"), 1258);
    }

    @Test
    public void test2Gen01260() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.06.2015"), 1259);
    }

    @Test
    public void test2Gen01261() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.06.2015"), 1260);
    }

    @Test
    public void test2Gen01262() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.06.2015"), 1261);
    }

    @Test
    public void test2Gen01263() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.06.2015"), 1262);
    }

    @Test
    public void test2Gen01264() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.06.2015"), 1263);
    }

    @Test
    public void test2Gen01265() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.06.2015"), 1264);
    }

    @Test
    public void test2Gen01266() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.06.2015"), 1265);
    }

    @Test
    public void test2Gen01267() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.06.2015"), 1266);
    }

    @Test
    public void test2Gen01268() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.06.2015"), 1267);
    }

    @Test
    public void test2Gen01269() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.06.2015"), 1268);
    }

    @Test
    public void test2Gen01270() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.06.2015"), 1269);
    }

    @Test
    public void test2Gen01271() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.06.2015"), 1270);
    }

    @Test
    public void test2Gen01272() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.06.2015"), 1271);
    }

    @Test
    public void test2Gen01273() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.06.2015"), 1272);
    }

    @Test
    public void test2Gen01274() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.06.2015"), 1273);
    }

    @Test
    public void test2Gen01275() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.06.2015"), 1274);
    }

    @Test
    public void test2Gen01276() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.06.2015"), 1275);
    }

    @Test
    public void test2Gen01277() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.06.2015"), 1276);
    }

    @Test
    public void test2Gen01278() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.07.2015"), 1277);
    }

    @Test
    public void test2Gen01279() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.07.2015"), 1278);
    }

    @Test
    public void test2Gen01280() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.07.2015"), 1279);
    }

    @Test
    public void test2Gen01281() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.07.2015"), 1280);
    }

    @Test
    public void test2Gen01282() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.07.2015"), 1281);
    }

    @Test
    public void test2Gen01283() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.07.2015"), 1282);
    }

    @Test
    public void test2Gen01284() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.07.2015"), 1283);
    }

    @Test
    public void test2Gen01285() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.07.2015"), 1284);
    }

    @Test
    public void test2Gen01286() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.07.2015"), 1285);
    }

    @Test
    public void test2Gen01287() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.07.2015"), 1286);
    }

    @Test
    public void test2Gen01288() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.07.2015"), 1287);
    }

    @Test
    public void test2Gen01289() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.07.2015"), 1288);
    }

    @Test
    public void test2Gen01290() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.07.2015"), 1289);
    }

    @Test
    public void test2Gen01291() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.07.2015"), 1290);
    }

    @Test
    public void test2Gen01292() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.07.2015"), 1291);
    }

    @Test
    public void test2Gen01293() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.07.2015"), 1292);
    }

    @Test
    public void test2Gen01294() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.07.2015"), 1293);
    }

    @Test
    public void test2Gen01295() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.07.2015"), 1294);
    }

    @Test
    public void test2Gen01296() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.07.2015"), 1295);
    }

    @Test
    public void test2Gen01297() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.07.2015"), 1296);
    }

    @Test
    public void test2Gen01298() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.07.2015"), 1297);
    }

    @Test
    public void test2Gen01299() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.07.2015"), 1298);
    }

    @Test
    public void test2Gen01300() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.07.2015"), 1299);
    }

    @Test
    public void test2Gen01301() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.07.2015"), 1300);
    }

    @Test
    public void test2Gen01302() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.07.2015"), 1301);
    }

    @Test
    public void test2Gen01303() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.07.2015"), 1302);
    }

    @Test
    public void test2Gen01304() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.07.2015"), 1303);
    }

    @Test
    public void test2Gen01305() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.07.2015"), 1304);
    }

    @Test
    public void test2Gen01306() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.07.2015"), 1305);
    }

    @Test
    public void test2Gen01307() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.07.2015"), 1306);
    }

    @Test
    public void test2Gen01308() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.07.2015"), 1307);
    }

    @Test
    public void test2Gen01309() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.08.2015"), 1308);
    }

    @Test
    public void test2Gen01310() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.08.2015"), 1309);
    }

    @Test
    public void test2Gen01311() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.08.2015"), 1310);
    }

    @Test
    public void test2Gen01312() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.08.2015"), 1311);
    }

    @Test
    public void test2Gen01313() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.08.2015"), 1312);
    }

    @Test
    public void test2Gen01314() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.08.2015"), 1313);
    }

    @Test
    public void test2Gen01315() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.08.2015"), 1314);
    }

    @Test
    public void test2Gen01316() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.08.2015"), 1315);
    }

    @Test
    public void test2Gen01317() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.08.2015"), 1316);
    }

    @Test
    public void test2Gen01318() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.08.2015"), 1317);
    }

    @Test
    public void test2Gen01319() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.08.2015"), 1318);
    }

    @Test
    public void test2Gen01320() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.08.2015"), 1319);
    }

    @Test
    public void test2Gen01321() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.08.2015"), 1320);
    }

    @Test
    public void test2Gen01322() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.08.2015"), 1321);
    }

    @Test
    public void test2Gen01323() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.08.2015"), 1322);
    }

    @Test
    public void test2Gen01324() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.08.2015"), 1323);
    }

    @Test
    public void test2Gen01325() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.08.2015"), 1324);
    }

    @Test
    public void test2Gen01326() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.08.2015"), 1325);
    }

    @Test
    public void test2Gen01327() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.08.2015"), 1326);
    }

    @Test
    public void test2Gen01328() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.08.2015"), 1327);
    }

    @Test
    public void test2Gen01329() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.08.2015"), 1328);
    }

    @Test
    public void test2Gen01330() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.08.2015"), 1329);
    }

    @Test
    public void test2Gen01331() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.08.2015"), 1330);
    }

    @Test
    public void test2Gen01332() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.08.2015"), 1331);
    }

    @Test
    public void test2Gen01333() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.08.2015"), 1332);
    }

    @Test
    public void test2Gen01334() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.08.2015"), 1333);
    }

    @Test
    public void test2Gen01335() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.08.2015"), 1334);
    }

    @Test
    public void test2Gen01336() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.08.2015"), 1335);
    }

    @Test
    public void test2Gen01337() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.08.2015"), 1336);
    }

    @Test
    public void test2Gen01338() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.08.2015"), 1337);
    }

    @Test
    public void test2Gen01339() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.08.2015"), 1338);
    }

    @Test
    public void test2Gen01340() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.09.2015"), 1339);
    }

    @Test
    public void test2Gen01341() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.09.2015"), 1340);
    }

    @Test
    public void test2Gen01342() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.09.2015"), 1341);
    }

    @Test
    public void test2Gen01343() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.09.2015"), 1342);
    }

    @Test
    public void test2Gen01344() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.09.2015"), 1343);
    }

    @Test
    public void test2Gen01345() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.09.2015"), 1344);
    }

    @Test
    public void test2Gen01346() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.09.2015"), 1345);
    }

    @Test
    public void test2Gen01347() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.09.2015"), 1346);
    }

    @Test
    public void test2Gen01348() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.09.2015"), 1347);
    }

    @Test
    public void test2Gen01349() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.09.2015"), 1348);
    }

    @Test
    public void test2Gen01350() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.09.2015"), 1349);
    }

    @Test
    public void test2Gen01351() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.09.2015"), 1350);
    }

    @Test
    public void test2Gen01352() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.09.2015"), 1351);
    }

    @Test
    public void test2Gen01353() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.09.2015"), 1352);
    }

    @Test
    public void test2Gen01354() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.09.2015"), 1353);
    }

    @Test
    public void test2Gen01355() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.09.2015"), 1354);
    }

    @Test
    public void test2Gen01356() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.09.2015"), 1355);
    }

    @Test
    public void test2Gen01357() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.09.2015"), 1356);
    }

    @Test
    public void test2Gen01358() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.09.2015"), 1357);
    }

    @Test
    public void test2Gen01359() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.09.2015"), 1358);
    }

    @Test
    public void test2Gen01360() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.09.2015"), 1359);
    }

    @Test
    public void test2Gen01361() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.09.2015"), 1360);
    }

    @Test
    public void test2Gen01362() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.09.2015"), 1361);
    }

    @Test
    public void test2Gen01363() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.09.2015"), 1362);
    }

    @Test
    public void test2Gen01364() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.09.2015"), 1363);
    }

    @Test
    public void test2Gen01365() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.09.2015"), 1364);
    }

    @Test
    public void test2Gen01366() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.09.2015"), 1365);
    }

    @Test
    public void test2Gen01367() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.09.2015"), 1366);
    }

    @Test
    public void test2Gen01368() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.09.2015"), 1367);
    }

    @Test
    public void test2Gen01369() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.09.2015"), 1368);
    }

    @Test
    public void test2Gen01370() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.10.2015"), 1369);
    }

    @Test
    public void test2Gen01371() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.10.2015"), 1370);
    }

    @Test
    public void test2Gen01372() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.10.2015"), 1371);
    }

    @Test
    public void test2Gen01373() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.10.2015"), 1372);
    }

    @Test
    public void test2Gen01374() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.10.2015"), 1373);
    }

    @Test
    public void test2Gen01375() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.10.2015"), 1374);
    }

    @Test
    public void test2Gen01376() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.10.2015"), 1375);
    }

    @Test
    public void test2Gen01377() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.10.2015"), 1376);
    }

    @Test
    public void test2Gen01378() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.10.2015"), 1377);
    }

    @Test
    public void test2Gen01379() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.10.2015"), 1378);
    }

    @Test
    public void test2Gen01380() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.10.2015"), 1379);
    }

    @Test
    public void test2Gen01381() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.10.2015"), 1380);
    }

    @Test
    public void test2Gen01382() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.10.2015"), 1381);
    }

    @Test
    public void test2Gen01383() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.10.2015"), 1382);
    }

    @Test
    public void test2Gen01384() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.10.2015"), 1383);
    }

    @Test
    public void test2Gen01385() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.10.2015"), 1384);
    }

    @Test
    public void test2Gen01386() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.10.2015"), 1385);
    }

    @Test
    public void test2Gen01387() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.10.2015"), 1386);
    }

    @Test
    public void test2Gen01388() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.10.2015"), 1387);
    }

    @Test
    public void test2Gen01389() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.10.2015"), 1388);
    }

    @Test
    public void test2Gen01390() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.10.2015"), 1389);
    }

    @Test
    public void test2Gen01391() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.10.2015"), 1390);
    }

    @Test
    public void test2Gen01392() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.10.2015"), 1391);
    }

    @Test
    public void test2Gen01393() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.10.2015"), 1392);
    }

    @Test
    public void test2Gen01394() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.10.2015"), 1393);
    }

    @Test
    public void test2Gen01395() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.10.2015"), 1394);
    }

    @Test
    public void test2Gen01396() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.10.2015"), 1395);
    }

    @Test
    public void test2Gen01397() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.10.2015"), 1396);
    }

    @Test
    public void test2Gen01398() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.10.2015"), 1397);
    }

    @Test
    public void test2Gen01399() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.10.2015"), 1398);
    }

    @Test
    public void test2Gen01400() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.10.2015"), 1399);
    }

    @Test
    public void test2Gen01401() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.11.2015"), 1400);
    }

    @Test
    public void test2Gen01402() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.11.2015"), 1401);
    }

    @Test
    public void test2Gen01403() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.11.2015"), 1402);
    }

    @Test
    public void test2Gen01404() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.11.2015"), 1403);
    }

    @Test
    public void test2Gen01405() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.11.2015"), 1404);
    }

    @Test
    public void test2Gen01406() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.11.2015"), 1405);
    }

    @Test
    public void test2Gen01407() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.11.2015"), 1406);
    }

    @Test
    public void test2Gen01408() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.11.2015"), 1407);
    }

    @Test
    public void test2Gen01409() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.11.2015"), 1408);
    }

    @Test
    public void test2Gen01410() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.11.2015"), 1409);
    }

    @Test
    public void test2Gen01411() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.11.2015"), 1410);
    }

    @Test
    public void test2Gen01412() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.11.2015"), 1411);
    }

    @Test
    public void test2Gen01413() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.11.2015"), 1412);
    }

    @Test
    public void test2Gen01414() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.11.2015"), 1413);
    }

    @Test
    public void test2Gen01415() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.11.2015"), 1414);
    }

    @Test
    public void test2Gen01416() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.11.2015"), 1415);
    }

    @Test
    public void test2Gen01417() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.11.2015"), 1416);
    }

    @Test
    public void test2Gen01418() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.11.2015"), 1417);
    }

    @Test
    public void test2Gen01419() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.11.2015"), 1418);
    }

    @Test
    public void test2Gen01420() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.11.2015"), 1419);
    }

    @Test
    public void test2Gen01421() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.11.2015"), 1420);
    }

    @Test
    public void test2Gen01422() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.11.2015"), 1421);
    }

    @Test
    public void test2Gen01423() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.11.2015"), 1422);
    }

    @Test
    public void test2Gen01424() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.11.2015"), 1423);
    }

    @Test
    public void test2Gen01425() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.11.2015"), 1424);
    }

    @Test
    public void test2Gen01426() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.11.2015"), 1425);
    }

    @Test
    public void test2Gen01427() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.11.2015"), 1426);
    }

    @Test
    public void test2Gen01428() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.11.2015"), 1427);
    }

    @Test
    public void test2Gen01429() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.11.2015"), 1428);
    }

    @Test
    public void test2Gen01430() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.11.2015"), 1429);
    }

    @Test
    public void test2Gen01431() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.12.2015"), 1430);
    }

    @Test
    public void test2Gen01432() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.12.2015"), 1431);
    }

    @Test
    public void test2Gen01433() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.12.2015"), 1432);
    }

    @Test
    public void test2Gen01434() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.12.2015"), 1433);
    }

    @Test
    public void test2Gen01435() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.12.2015"), 1434);
    }

    @Test
    public void test2Gen01436() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.12.2015"), 1435);
    }

    @Test
    public void test2Gen01437() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.12.2015"), 1436);
    }

    @Test
    public void test2Gen01438() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.12.2015"), 1437);
    }

    @Test
    public void test2Gen01439() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.12.2015"), 1438);
    }

    @Test
    public void test2Gen01440() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.12.2015"), 1439);
    }

    @Test
    public void test2Gen01441() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.12.2015"), 1440);
    }

    @Test
    public void test2Gen01442() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.12.2015"), 1441);
    }

    @Test
    public void test2Gen01443() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.12.2015"), 1442);
    }

    @Test
    public void test2Gen01444() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.12.2015"), 1443);
    }

    @Test
    public void test2Gen01445() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.12.2015"), 1444);
    }

    @Test
    public void test2Gen01446() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.12.2015"), 1445);
    }

    @Test
    public void test2Gen01447() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.12.2015"), 1446);
    }

    @Test
    public void test2Gen01448() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.12.2015"), 1447);
    }

    @Test
    public void test2Gen01449() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.12.2015"), 1448);
    }

    @Test
    public void test2Gen01450() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.12.2015"), 1449);
    }

    @Test
    public void test2Gen01451() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.12.2015"), 1450);
    }

    @Test
    public void test2Gen01452() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.12.2015"), 1451);
    }

    @Test
    public void test2Gen01453() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.12.2015"), 1452);
    }

    @Test
    public void test2Gen01454() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.12.2015"), 1453);
    }

    @Test
    public void test2Gen01455() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.12.2015"), 1454);
    }

    @Test
    public void test2Gen01456() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.12.2015"), 1455);
    }

    @Test
    public void test2Gen01457() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.12.2015"), 1456);
    }

    @Test
    public void test2Gen01458() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.12.2015"), 1457);
    }

    @Test
    public void test2Gen01459() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.12.2015"), 1458);
    }

    @Test
    public void test2Gen01460() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.12.2015"), 1459);
    }

    @Test
    public void test2Gen01461() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.12.2015"), 1460);
    }

    @Test
    public void test2Gen01462() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.01.2016"), 1461);
    }

    @Test
    public void test2Gen01463() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.01.2016"), 1462);
    }

    @Test
    public void test2Gen01464() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.01.2016"), 1463);
    }

    @Test
    public void test2Gen01465() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.01.2016"), 1464);
    }

    @Test
    public void test2Gen01466() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.01.2016"), 1465);
    }

    @Test
    public void test2Gen01467() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.01.2016"), 1466);
    }

    @Test
    public void test2Gen01468() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.01.2016"), 1467);
    }

    @Test
    public void test2Gen01469() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.01.2016"), 1468);
    }

    @Test
    public void test2Gen01470() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.01.2016"), 1469);
    }

    @Test
    public void test2Gen01471() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.01.2016"), 1470);
    }

    @Test
    public void test2Gen01472() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.01.2016"), 1471);
    }

    @Test
    public void test2Gen01473() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.01.2016"), 1472);
    }

    @Test
    public void test2Gen01474() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.01.2016"), 1473);
    }

    @Test
    public void test2Gen01475() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.01.2016"), 1474);
    }

    @Test
    public void test2Gen01476() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.01.2016"), 1475);
    }

    @Test
    public void test2Gen01477() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.01.2016"), 1476);
    }

    @Test
    public void test2Gen01478() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.01.2016"), 1477);
    }

    @Test
    public void test2Gen01479() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.01.2016"), 1478);
    }

    @Test
    public void test2Gen01480() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.01.2016"), 1479);
    }

    @Test
    public void test2Gen01481() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.01.2016"), 1480);
    }

    @Test
    public void test2Gen01482() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.01.2016"), 1481);
    }

    @Test
    public void test2Gen01483() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.01.2016"), 1482);
    }

    @Test
    public void test2Gen01484() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.01.2016"), 1483);
    }

    @Test
    public void test2Gen01485() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.01.2016"), 1484);
    }

    @Test
    public void test2Gen01486() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.01.2016"), 1485);
    }

    @Test
    public void test2Gen01487() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.01.2016"), 1486);
    }

    @Test
    public void test2Gen01488() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.01.2016"), 1487);
    }

    @Test
    public void test2Gen01489() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.01.2016"), 1488);
    }

    @Test
    public void test2Gen01490() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.01.2016"), 1489);
    }

    @Test
    public void test2Gen01491() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.01.2016"), 1490);
    }

    @Test
    public void test2Gen01492() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.01.2016"), 1491);
    }

    @Test
    public void test2Gen01493() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.02.2016"), 1492);
    }

    @Test
    public void test2Gen01494() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.02.2016"), 1493);
    }

    @Test
    public void test2Gen01495() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.02.2016"), 1494);
    }

    @Test
    public void test2Gen01496() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.02.2016"), 1495);
    }

    @Test
    public void test2Gen01497() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.02.2016"), 1496);
    }

    @Test
    public void test2Gen01498() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.02.2016"), 1497);
    }

    @Test
    public void test2Gen01499() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.02.2016"), 1498);
    }

    @Test
    public void test2Gen01500() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.02.2016"), 1499);
    }

    @Test
    public void test2Gen01501() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.02.2016"), 1500);
    }

    @Test
    public void test2Gen01502() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.02.2016"), 1501);
    }

    @Test
    public void test2Gen01503() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.02.2016"), 1502);
    }

    @Test
    public void test2Gen01504() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.02.2016"), 1503);
    }

    @Test
    public void test2Gen01505() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.02.2016"), 1504);
    }

    @Test
    public void test2Gen01506() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.02.2016"), 1505);
    }

    @Test
    public void test2Gen01507() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.02.2016"), 1506);
    }

    @Test
    public void test2Gen01508() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.02.2016"), 1507);
    }

    @Test
    public void test2Gen01509() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.02.2016"), 1508);
    }

    @Test
    public void test2Gen01510() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.02.2016"), 1509);
    }

    @Test
    public void test2Gen01511() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.02.2016"), 1510);
    }

    @Test
    public void test2Gen01512() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.02.2016"), 1511);
    }

    @Test
    public void test2Gen01513() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.02.2016"), 1512);
    }

    @Test
    public void test2Gen01514() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.02.2016"), 1513);
    }

    @Test
    public void test2Gen01515() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.02.2016"), 1514);
    }

    @Test
    public void test2Gen01516() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.02.2016"), 1515);
    }

    @Test
    public void test2Gen01517() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.02.2016"), 1516);
    }

    @Test
    public void test2Gen01518() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.02.2016"), 1517);
    }

    @Test
    public void test2Gen01519() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.02.2016"), 1518);
    }

    @Test
    public void test2Gen01520() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.02.2016"), 1519);
    }

    @Test
    public void test2Gen01521() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.02.2016"), 1520);
    }

    @Test
    public void test2Gen01522() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.03.2016"), 1521);
    }

    @Test
    public void test2Gen01523() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.03.2016"), 1522);
    }

    @Test
    public void test2Gen01524() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.03.2016"), 1523);
    }

    @Test
    public void test2Gen01525() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.03.2016"), 1524);
    }

    @Test
    public void test2Gen01526() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.03.2016"), 1525);
    }

    @Test
    public void test2Gen01527() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.03.2016"), 1526);
    }

    @Test
    public void test2Gen01528() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.03.2016"), 1527);
    }

    @Test
    public void test2Gen01529() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.03.2016"), 1528);
    }

    @Test
    public void test2Gen01530() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.03.2016"), 1529);
    }

    @Test
    public void test2Gen01531() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.03.2016"), 1530);
    }

    @Test
    public void test2Gen01532() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.03.2016"), 1531);
    }

    @Test
    public void test2Gen01533() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.03.2016"), 1532);
    }

    @Test
    public void test2Gen01534() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.03.2016"), 1533);
    }

    @Test
    public void test2Gen01535() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.03.2016"), 1534);
    }

    @Test
    public void test2Gen01536() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.03.2016"), 1535);
    }

    @Test
    public void test2Gen01537() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.03.2016"), 1536);
    }

    @Test
    public void test2Gen01538() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.03.2016"), 1537);
    }

    @Test
    public void test2Gen01539() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.03.2016"), 1538);
    }

    @Test
    public void test2Gen01540() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.03.2016"), 1539);
    }

    @Test
    public void test2Gen01541() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.03.2016"), 1540);
    }

    @Test
    public void test2Gen01542() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.03.2016"), 1541);
    }

    @Test
    public void test2Gen01543() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.03.2016"), 1542);
    }

    @Test
    public void test2Gen01544() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.03.2016"), 1543);
    }

    @Test
    public void test2Gen01545() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.03.2016"), 1544);
    }

    @Test
    public void test2Gen01546() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.03.2016"), 1545);
    }

    @Test
    public void test2Gen01547() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.03.2016"), 1546);
    }

    @Test
    public void test2Gen01548() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.03.2016"), 1547);
    }

    @Test
    public void test2Gen01549() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.03.2016"), 1548);
    }

    @Test
    public void test2Gen01550() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.03.2016"), 1549);
    }

    @Test
    public void test2Gen01551() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.03.2016"), 1550);
    }

    @Test
    public void test2Gen01552() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.03.2016"), 1551);
    }

    @Test
    public void test2Gen01553() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.04.2016"), 1552);
    }

    @Test
    public void test2Gen01554() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.04.2016"), 1553);
    }

    @Test
    public void test2Gen01555() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.04.2016"), 1554);
    }

    @Test
    public void test2Gen01556() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.04.2016"), 1555);
    }

    @Test
    public void test2Gen01557() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.04.2016"), 1556);
    }

    @Test
    public void test2Gen01558() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.04.2016"), 1557);
    }

    @Test
    public void test2Gen01559() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.04.2016"), 1558);
    }

    @Test
    public void test2Gen01560() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.04.2016"), 1559);
    }

    @Test
    public void test2Gen01561() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.04.2016"), 1560);
    }

    @Test
    public void test2Gen01562() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.04.2016"), 1561);
    }

    @Test
    public void test2Gen01563() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.04.2016"), 1562);
    }

    @Test
    public void test2Gen01564() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.04.2016"), 1563);
    }

    @Test
    public void test2Gen01565() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.04.2016"), 1564);
    }

    @Test
    public void test2Gen01566() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.04.2016"), 1565);
    }

    @Test
    public void test2Gen01567() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.04.2016"), 1566);
    }

    @Test
    public void test2Gen01568() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.04.2016"), 1567);
    }

    @Test
    public void test2Gen01569() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.04.2016"), 1568);
    }

    @Test
    public void test2Gen01570() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.04.2016"), 1569);
    }

    @Test
    public void test2Gen01571() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.04.2016"), 1570);
    }

    @Test
    public void test2Gen01572() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.04.2016"), 1571);
    }

    @Test
    public void test2Gen01573() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.04.2016"), 1572);
    }

    @Test
    public void test2Gen01574() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.04.2016"), 1573);
    }

    @Test
    public void test2Gen01575() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.04.2016"), 1574);
    }

    @Test
    public void test2Gen01576() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.04.2016"), 1575);
    }

    @Test
    public void test2Gen01577() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.04.2016"), 1576);
    }

    @Test
    public void test2Gen01578() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.04.2016"), 1577);
    }

    @Test
    public void test2Gen01579() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.04.2016"), 1578);
    }

    @Test
    public void test2Gen01580() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.04.2016"), 1579);
    }

    @Test
    public void test2Gen01581() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.04.2016"), 1580);
    }

    @Test
    public void test2Gen01582() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.04.2016"), 1581);
    }

    @Test
    public void test2Gen01583() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.05.2016"), 1582);
    }

    @Test
    public void test2Gen01584() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.05.2016"), 1583);
    }

    @Test
    public void test2Gen01585() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.05.2016"), 1584);
    }

    @Test
    public void test2Gen01586() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.05.2016"), 1585);
    }

    @Test
    public void test2Gen01587() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.05.2016"), 1586);
    }

    @Test
    public void test2Gen01588() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.05.2016"), 1587);
    }

    @Test
    public void test2Gen01589() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.05.2016"), 1588);
    }

    @Test
    public void test2Gen01590() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.05.2016"), 1589);
    }

    @Test
    public void test2Gen01591() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.05.2016"), 1590);
    }

    @Test
    public void test2Gen01592() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.05.2016"), 1591);
    }

    @Test
    public void test2Gen01593() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.05.2016"), 1592);
    }

    @Test
    public void test2Gen01594() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.05.2016"), 1593);
    }

    @Test
    public void test2Gen01595() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.05.2016"), 1594);
    }

    @Test
    public void test2Gen01596() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.05.2016"), 1595);
    }

    @Test
    public void test2Gen01597() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.05.2016"), 1596);
    }

    @Test
    public void test2Gen01598() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.05.2016"), 1597);
    }

    @Test
    public void test2Gen01599() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.05.2016"), 1598);
    }

    @Test
    public void test2Gen01600() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.05.2016"), 1599);
    }

    @Test
    public void test2Gen01601() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.05.2016"), 1600);
    }

    @Test
    public void test2Gen01602() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.05.2016"), 1601);
    }

    @Test
    public void test2Gen01603() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.05.2016"), 1602);
    }

    @Test
    public void test2Gen01604() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.05.2016"), 1603);
    }

    @Test
    public void test2Gen01605() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.05.2016"), 1604);
    }

    @Test
    public void test2Gen01606() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.05.2016"), 1605);
    }

    @Test
    public void test2Gen01607() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.05.2016"), 1606);
    }

    @Test
    public void test2Gen01608() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.05.2016"), 1607);
    }

    @Test
    public void test2Gen01609() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.05.2016"), 1608);
    }

    @Test
    public void test2Gen01610() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.05.2016"), 1609);
    }

    @Test
    public void test2Gen01611() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.05.2016"), 1610);
    }

    @Test
    public void test2Gen01612() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.05.2016"), 1611);
    }

    @Test
    public void test2Gen01613() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.05.2016"), 1612);
    }

    @Test
    public void test2Gen01614() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.06.2016"), 1613);
    }

    @Test
    public void test2Gen01615() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.06.2016"), 1614);
    }

    @Test
    public void test2Gen01616() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.06.2016"), 1615);
    }

    @Test
    public void test2Gen01617() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.06.2016"), 1616);
    }

    @Test
    public void test2Gen01618() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.06.2016"), 1617);
    }

    @Test
    public void test2Gen01619() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.06.2016"), 1618);
    }

    @Test
    public void test2Gen01620() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.06.2016"), 1619);
    }

    @Test
    public void test2Gen01621() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.06.2016"), 1620);
    }

    @Test
    public void test2Gen01622() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.06.2016"), 1621);
    }

    @Test
    public void test2Gen01623() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.06.2016"), 1622);
    }

    @Test
    public void test2Gen01624() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.06.2016"), 1623);
    }

    @Test
    public void test2Gen01625() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.06.2016"), 1624);
    }

    @Test
    public void test2Gen01626() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.06.2016"), 1625);
    }

    @Test
    public void test2Gen01627() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.06.2016"), 1626);
    }

    @Test
    public void test2Gen01628() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.06.2016"), 1627);
    }

    @Test
    public void test2Gen01629() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.06.2016"), 1628);
    }

    @Test
    public void test2Gen01630() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.06.2016"), 1629);
    }

    @Test
    public void test2Gen01631() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.06.2016"), 1630);
    }

    @Test
    public void test2Gen01632() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.06.2016"), 1631);
    }

    @Test
    public void test2Gen01633() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.06.2016"), 1632);
    }

    @Test
    public void test2Gen01634() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.06.2016"), 1633);
    }

    @Test
    public void test2Gen01635() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.06.2016"), 1634);
    }

    @Test
    public void test2Gen01636() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.06.2016"), 1635);
    }

    @Test
    public void test2Gen01637() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.06.2016"), 1636);
    }

    @Test
    public void test2Gen01638() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.06.2016"), 1637);
    }

    @Test
    public void test2Gen01639() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.06.2016"), 1638);
    }

    @Test
    public void test2Gen01640() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.06.2016"), 1639);
    }

    @Test
    public void test2Gen01641() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.06.2016"), 1640);
    }

    @Test
    public void test2Gen01642() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.06.2016"), 1641);
    }

    @Test
    public void test2Gen01643() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.06.2016"), 1642);
    }

    @Test
    public void test2Gen01644() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.07.2016"), 1643);
    }

    @Test
    public void test2Gen01645() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.07.2016"), 1644);
    }

    @Test
    public void test2Gen01646() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.07.2016"), 1645);
    }

    @Test
    public void test2Gen01647() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.07.2016"), 1646);
    }

    @Test
    public void test2Gen01648() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.07.2016"), 1647);
    }

    @Test
    public void test2Gen01649() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.07.2016"), 1648);
    }

    @Test
    public void test2Gen01650() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.07.2016"), 1649);
    }

    @Test
    public void test2Gen01651() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.07.2016"), 1650);
    }

    @Test
    public void test2Gen01652() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.07.2016"), 1651);
    }

    @Test
    public void test2Gen01653() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.07.2016"), 1652);
    }

    @Test
    public void test2Gen01654() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.07.2016"), 1653);
    }

    @Test
    public void test2Gen01655() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.07.2016"), 1654);
    }

    @Test
    public void test2Gen01656() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.07.2016"), 1655);
    }

    @Test
    public void test2Gen01657() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.07.2016"), 1656);
    }

    @Test
    public void test2Gen01658() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.07.2016"), 1657);
    }

    @Test
    public void test2Gen01659() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.07.2016"), 1658);
    }

    @Test
    public void test2Gen01660() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.07.2016"), 1659);
    }

    @Test
    public void test2Gen01661() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.07.2016"), 1660);
    }

    @Test
    public void test2Gen01662() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.07.2016"), 1661);
    }

    @Test
    public void test2Gen01663() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.07.2016"), 1662);
    }

    @Test
    public void test2Gen01664() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.07.2016"), 1663);
    }

    @Test
    public void test2Gen01665() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.07.2016"), 1664);
    }

    @Test
    public void test2Gen01666() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.07.2016"), 1665);
    }

    @Test
    public void test2Gen01667() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.07.2016"), 1666);
    }

    @Test
    public void test2Gen01668() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.07.2016"), 1667);
    }

    @Test
    public void test2Gen01669() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.07.2016"), 1668);
    }

    @Test
    public void test2Gen01670() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.07.2016"), 1669);
    }

    @Test
    public void test2Gen01671() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.07.2016"), 1670);
    }

    @Test
    public void test2Gen01672() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.07.2016"), 1671);
    }

    @Test
    public void test2Gen01673() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.07.2016"), 1672);
    }

    @Test
    public void test2Gen01674() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.07.2016"), 1673);
    }

    @Test
    public void test2Gen01675() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.08.2016"), 1674);
    }

    @Test
    public void test2Gen01676() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.08.2016"), 1675);
    }

    @Test
    public void test2Gen01677() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.08.2016"), 1676);
    }

    @Test
    public void test2Gen01678() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.08.2016"), 1677);
    }

    @Test
    public void test2Gen01679() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.08.2016"), 1678);
    }

    @Test
    public void test2Gen01680() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.08.2016"), 1679);
    }

    @Test
    public void test2Gen01681() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.08.2016"), 1680);
    }

    @Test
    public void test2Gen01682() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.08.2016"), 1681);
    }

    @Test
    public void test2Gen01683() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.08.2016"), 1682);
    }

    @Test
    public void test2Gen01684() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.08.2016"), 1683);
    }

    @Test
    public void test2Gen01685() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.08.2016"), 1684);
    }

    @Test
    public void test2Gen01686() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.08.2016"), 1685);
    }

    @Test
    public void test2Gen01687() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.08.2016"), 1686);
    }

    @Test
    public void test2Gen01688() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.08.2016"), 1687);
    }

    @Test
    public void test2Gen01689() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.08.2016"), 1688);
    }

    @Test
    public void test2Gen01690() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.08.2016"), 1689);
    }

    @Test
    public void test2Gen01691() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.08.2016"), 1690);
    }

    @Test
    public void test2Gen01692() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.08.2016"), 1691);
    }

    @Test
    public void test2Gen01693() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.08.2016"), 1692);
    }

    @Test
    public void test2Gen01694() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.08.2016"), 1693);
    }

    @Test
    public void test2Gen01695() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.08.2016"), 1694);
    }

    @Test
    public void test2Gen01696() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.08.2016"), 1695);
    }

    @Test
    public void test2Gen01697() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.08.2016"), 1696);
    }

    @Test
    public void test2Gen01698() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.08.2016"), 1697);
    }

    @Test
    public void test2Gen01699() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.08.2016"), 1698);
    }

    @Test
    public void test2Gen01700() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.08.2016"), 1699);
    }

    @Test
    public void test2Gen01701() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.08.2016"), 1700);
    }

    @Test
    public void test2Gen01702() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.08.2016"), 1701);
    }

    @Test
    public void test2Gen01703() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.08.2016"), 1702);
    }

    @Test
    public void test2Gen01704() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.08.2016"), 1703);
    }

    @Test
    public void test2Gen01705() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.08.2016"), 1704);
    }

    @Test
    public void test2Gen01706() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.09.2016"), 1705);
    }

    @Test
    public void test2Gen01707() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.09.2016"), 1706);
    }

    @Test
    public void test2Gen01708() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.09.2016"), 1707);
    }

    @Test
    public void test2Gen01709() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.09.2016"), 1708);
    }

    @Test
    public void test2Gen01710() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.09.2016"), 1709);
    }

    @Test
    public void test2Gen01711() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.09.2016"), 1710);
    }

    @Test
    public void test2Gen01712() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.09.2016"), 1711);
    }

    @Test
    public void test2Gen01713() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.09.2016"), 1712);
    }

    @Test
    public void test2Gen01714() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.09.2016"), 1713);
    }

    @Test
    public void test2Gen01715() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.09.2016"), 1714);
    }

    @Test
    public void test2Gen01716() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.09.2016"), 1715);
    }

    @Test
    public void test2Gen01717() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.09.2016"), 1716);
    }

    @Test
    public void test2Gen01718() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.09.2016"), 1717);
    }

    @Test
    public void test2Gen01719() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.09.2016"), 1718);
    }

    @Test
    public void test2Gen01720() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.09.2016"), 1719);
    }

    @Test
    public void test2Gen01721() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.09.2016"), 1720);
    }

    @Test
    public void test2Gen01722() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.09.2016"), 1721);
    }

    @Test
    public void test2Gen01723() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.09.2016"), 1722);
    }

    @Test
    public void test2Gen01724() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.09.2016"), 1723);
    }

    @Test
    public void test2Gen01725() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.09.2016"), 1724);
    }

    @Test
    public void test2Gen01726() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.09.2016"), 1725);
    }

    @Test
    public void test2Gen01727() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.09.2016"), 1726);
    }

    @Test
    public void test2Gen01728() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.09.2016"), 1727);
    }

    @Test
    public void test2Gen01729() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.09.2016"), 1728);
    }

    @Test
    public void test2Gen01730() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.09.2016"), 1729);
    }

    @Test
    public void test2Gen01731() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.09.2016"), 1730);
    }

    @Test
    public void test2Gen01732() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.09.2016"), 1731);
    }

    @Test
    public void test2Gen01733() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.09.2016"), 1732);
    }

    @Test
    public void test2Gen01734() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.09.2016"), 1733);
    }

    @Test
    public void test2Gen01735() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.09.2016"), 1734);
    }

    @Test
    public void test2Gen01736() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.10.2016"), 1735);
    }

    @Test
    public void test2Gen01737() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.10.2016"), 1736);
    }

    @Test
    public void test2Gen01738() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.10.2016"), 1737);
    }

    @Test
    public void test2Gen01739() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.10.2016"), 1738);
    }

    @Test
    public void test2Gen01740() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.10.2016"), 1739);
    }

    @Test
    public void test2Gen01741() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.10.2016"), 1740);
    }

    @Test
    public void test2Gen01742() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.10.2016"), 1741);
    }

    @Test
    public void test2Gen01743() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.10.2016"), 1742);
    }

    @Test
    public void test2Gen01744() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.10.2016"), 1743);
    }

    @Test
    public void test2Gen01745() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.10.2016"), 1744);
    }

    @Test
    public void test2Gen01746() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.10.2016"), 1745);
    }

    @Test
    public void test2Gen01747() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.10.2016"), 1746);
    }

    @Test
    public void test2Gen01748() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.10.2016"), 1747);
    }

    @Test
    public void test2Gen01749() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.10.2016"), 1748);
    }

    @Test
    public void test2Gen01750() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.10.2016"), 1749);
    }

    @Test
    public void test2Gen01751() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.10.2016"), 1750);
    }

    @Test
    public void test2Gen01752() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.10.2016"), 1751);
    }

    @Test
    public void test2Gen01753() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.10.2016"), 1752);
    }

    @Test
    public void test2Gen01754() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.10.2016"), 1753);
    }

    @Test
    public void test2Gen01755() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.10.2016"), 1754);
    }

    @Test
    public void test2Gen01756() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.10.2016"), 1755);
    }

    @Test
    public void test2Gen01757() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.10.2016"), 1756);
    }

    @Test
    public void test2Gen01758() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.10.2016"), 1757);
    }

    @Test
    public void test2Gen01759() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.10.2016"), 1758);
    }

    @Test
    public void test2Gen01760() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.10.2016"), 1759);
    }

    @Test
    public void test2Gen01761() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.10.2016"), 1760);
    }

    @Test
    public void test2Gen01762() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.10.2016"), 1761);
    }

    @Test
    public void test2Gen01763() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.10.2016"), 1762);
    }

    @Test
    public void test2Gen01764() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.10.2016"), 1763);
    }

    @Test
    public void test2Gen01765() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.10.2016"), 1764);
    }

    @Test
    public void test2Gen01766() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.10.2016"), 1765);
    }

    @Test
    public void test2Gen01767() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.11.2016"), 1766);
    }

    @Test
    public void test2Gen01768() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.11.2016"), 1767);
    }

    @Test
    public void test2Gen01769() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.11.2016"), 1768);
    }

    @Test
    public void test2Gen01770() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.11.2016"), 1769);
    }

    @Test
    public void test2Gen01771() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.11.2016"), 1770);
    }

    @Test
    public void test2Gen01772() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.11.2016"), 1771);
    }

    @Test
    public void test2Gen01773() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.11.2016"), 1772);
    }

    @Test
    public void test2Gen01774() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.11.2016"), 1773);
    }

    @Test
    public void test2Gen01775() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.11.2016"), 1774);
    }

    @Test
    public void test2Gen01776() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.11.2016"), 1775);
    }

    @Test
    public void test2Gen01777() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.11.2016"), 1776);
    }

    @Test
    public void test2Gen01778() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.11.2016"), 1777);
    }

    @Test
    public void test2Gen01779() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.11.2016"), 1778);
    }

    @Test
    public void test2Gen01780() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.11.2016"), 1779);
    }

    @Test
    public void test2Gen01781() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.11.2016"), 1780);
    }

    @Test
    public void test2Gen01782() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.11.2016"), 1781);
    }

    @Test
    public void test2Gen01783() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.11.2016"), 1782);
    }

    @Test
    public void test2Gen01784() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.11.2016"), 1783);
    }

    @Test
    public void test2Gen01785() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.11.2016"), 1784);
    }

    @Test
    public void test2Gen01786() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.11.2016"), 1785);
    }

    @Test
    public void test2Gen01787() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.11.2016"), 1786);
    }

    @Test
    public void test2Gen01788() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.11.2016"), 1787);
    }

    @Test
    public void test2Gen01789() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.11.2016"), 1788);
    }

    @Test
    public void test2Gen01790() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.11.2016"), 1789);
    }

    @Test
    public void test2Gen01791() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.11.2016"), 1790);
    }

    @Test
    public void test2Gen01792() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.11.2016"), 1791);
    }

    @Test
    public void test2Gen01793() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.11.2016"), 1792);
    }

    @Test
    public void test2Gen01794() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.11.2016"), 1793);
    }

    @Test
    public void test2Gen01795() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.11.2016"), 1794);
    }

    @Test
    public void test2Gen01796() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.11.2016"), 1795);
    }

    @Test
    public void test2Gen01797() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.12.2016"), 1796);
    }

    @Test
    public void test2Gen01798() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.12.2016"), 1797);
    }

    @Test
    public void test2Gen01799() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.12.2016"), 1798);
    }

    @Test
    public void test2Gen01800() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.12.2016"), 1799);
    }

    @Test
    public void test2Gen01801() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.12.2016"), 1800);
    }

    @Test
    public void test2Gen01802() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.12.2016"), 1801);
    }

    @Test
    public void test2Gen01803() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.12.2016"), 1802);
    }

    @Test
    public void test2Gen01804() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.12.2016"), 1803);
    }

    @Test
    public void test2Gen01805() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.12.2016"), 1804);
    }

    @Test
    public void test2Gen01806() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.12.2016"), 1805);
    }

    @Test
    public void test2Gen01807() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.12.2016"), 1806);
    }

    @Test
    public void test2Gen01808() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.12.2016"), 1807);
    }

    @Test
    public void test2Gen01809() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.12.2016"), 1808);
    }

    @Test
    public void test2Gen01810() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.12.2016"), 1809);
    }

    @Test
    public void test2Gen01811() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.12.2016"), 1810);
    }

    @Test
    public void test2Gen01812() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.12.2016"), 1811);
    }

    @Test
    public void test2Gen01813() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.12.2016"), 1812);
    }

    @Test
    public void test2Gen01814() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.12.2016"), 1813);
    }

    @Test
    public void test2Gen01815() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.12.2016"), 1814);
    }

    @Test
    public void test2Gen01816() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.12.2016"), 1815);
    }

    @Test
    public void test2Gen01817() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.12.2016"), 1816);
    }

    @Test
    public void test2Gen01818() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.12.2016"), 1817);
    }

    @Test
    public void test2Gen01819() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.12.2016"), 1818);
    }

    @Test
    public void test2Gen01820() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.12.2016"), 1819);
    }

    @Test
    public void test2Gen01821() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.12.2016"), 1820);
    }

    @Test
    public void test2Gen01822() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.12.2016"), 1821);
    }

    @Test
    public void test2Gen01823() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.12.2016"), 1822);
    }

    @Test
    public void test2Gen01824() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.12.2016"), 1823);
    }

    @Test
    public void test2Gen01825() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.12.2016"), 1824);
    }

    @Test
    public void test2Gen01826() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.12.2016"), 1825);
    }

    @Test
    public void test2Gen01827() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.12.2016"), 1826);
    }

    @Test
    public void test2Gen01828() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.01.2017"), 1827);
    }

    @Test
    public void test2Gen01829() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.01.2017"), 1828);
    }

    @Test
    public void test2Gen01830() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.01.2017"), 1829);
    }

    @Test
    public void test2Gen01831() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.01.2017"), 1830);
    }

    @Test
    public void test2Gen01832() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.01.2017"), 1831);
    }

    @Test
    public void test2Gen01833() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.01.2017"), 1832);
    }

    @Test
    public void test2Gen01834() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.01.2017"), 1833);
    }

    @Test
    public void test2Gen01835() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.01.2017"), 1834);
    }

    @Test
    public void test2Gen01836() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.01.2017"), 1835);
    }

    @Test
    public void test2Gen01837() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.01.2017"), 1836);
    }

    @Test
    public void test2Gen01838() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.01.2017"), 1837);
    }

    @Test
    public void test2Gen01839() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.01.2017"), 1838);
    }

    @Test
    public void test2Gen01840() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.01.2017"), 1839);
    }

    @Test
    public void test2Gen01841() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.01.2017"), 1840);
    }

    @Test
    public void test2Gen01842() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.01.2017"), 1841);
    }

    @Test
    public void test2Gen01843() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.01.2017"), 1842);
    }

    @Test
    public void test2Gen01844() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.01.2017"), 1843);
    }

    @Test
    public void test2Gen01845() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.01.2017"), 1844);
    }

    @Test
    public void test2Gen01846() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.01.2017"), 1845);
    }

    @Test
    public void test2Gen01847() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.01.2017"), 1846);
    }

    @Test
    public void test2Gen01848() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.01.2017"), 1847);
    }

    @Test
    public void test2Gen01849() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.01.2017"), 1848);
    }

    @Test
    public void test2Gen01850() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.01.2017"), 1849);
    }

    @Test
    public void test2Gen01851() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.01.2017"), 1850);
    }

    @Test
    public void test2Gen01852() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.01.2017"), 1851);
    }

    @Test
    public void test2Gen01853() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.01.2017"), 1852);
    }

    @Test
    public void test2Gen01854() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.01.2017"), 1853);
    }

    @Test
    public void test2Gen01855() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.01.2017"), 1854);
    }

    @Test
    public void test2Gen01856() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.01.2017"), 1855);
    }

    @Test
    public void test2Gen01857() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.01.2017"), 1856);
    }

    @Test
    public void test2Gen01858() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.01.2017"), 1857);
    }

    @Test
    public void test2Gen01859() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.02.2017"), 1858);
    }

    @Test
    public void test2Gen01860() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.02.2017"), 1859);
    }

    @Test
    public void test2Gen01861() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.02.2017"), 1860);
    }

    @Test
    public void test2Gen01862() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.02.2017"), 1861);
    }

    @Test
    public void test2Gen01863() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.02.2017"), 1862);
    }

    @Test
    public void test2Gen01864() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.02.2017"), 1863);
    }

    @Test
    public void test2Gen01865() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.02.2017"), 1864);
    }

    @Test
    public void test2Gen01866() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.02.2017"), 1865);
    }

    @Test
    public void test2Gen01867() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.02.2017"), 1866);
    }

    @Test
    public void test2Gen01868() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.02.2017"), 1867);
    }

    @Test
    public void test2Gen01869() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.02.2017"), 1868);
    }

    @Test
    public void test2Gen01870() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.02.2017"), 1869);
    }

    @Test
    public void test2Gen01871() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.02.2017"), 1870);
    }

    @Test
    public void test2Gen01872() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.02.2017"), 1871);
    }

    @Test
    public void test2Gen01873() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.02.2017"), 1872);
    }

    @Test
    public void test2Gen01874() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.02.2017"), 1873);
    }

    @Test
    public void test2Gen01875() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.02.2017"), 1874);
    }

    @Test
    public void test2Gen01876() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.02.2017"), 1875);
    }

    @Test
    public void test2Gen01877() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.02.2017"), 1876);
    }

    @Test
    public void test2Gen01878() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.02.2017"), 1877);
    }

    @Test
    public void test2Gen01879() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.02.2017"), 1878);
    }

    @Test
    public void test2Gen01880() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.02.2017"), 1879);
    }

    @Test
    public void test2Gen01881() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.02.2017"), 1880);
    }

    @Test
    public void test2Gen01882() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.02.2017"), 1881);
    }

    @Test
    public void test2Gen01883() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.02.2017"), 1882);
    }

    @Test
    public void test2Gen01884() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.02.2017"), 1883);
    }

    @Test
    public void test2Gen01885() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.02.2017"), 1884);
    }

    @Test
    public void test2Gen01886() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.02.2017"), 1885);
    }

    @Test
    public void test2Gen01887() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.03.2017"), 1886);
    }

    @Test
    public void test2Gen01888() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.03.2017"), 1887);
    }

    @Test
    public void test2Gen01889() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.03.2017"), 1888);
    }

    @Test
    public void test2Gen01890() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.03.2017"), 1889);
    }

    @Test
    public void test2Gen01891() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.03.2017"), 1890);
    }

    @Test
    public void test2Gen01892() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.03.2017"), 1891);
    }

    @Test
    public void test2Gen01893() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.03.2017"), 1892);
    }

    @Test
    public void test2Gen01894() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.03.2017"), 1893);
    }

    @Test
    public void test2Gen01895() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.03.2017"), 1894);
    }

    @Test
    public void test2Gen01896() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.03.2017"), 1895);
    }

    @Test
    public void test2Gen01897() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.03.2017"), 1896);
    }

    @Test
    public void test2Gen01898() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.03.2017"), 1897);
    }

    @Test
    public void test2Gen01899() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.03.2017"), 1898);
    }

    @Test
    public void test2Gen01900() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.03.2017"), 1899);
    }

    @Test
    public void test2Gen01901() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.03.2017"), 1900);
    }

    @Test
    public void test2Gen01902() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.03.2017"), 1901);
    }

    @Test
    public void test2Gen01903() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.03.2017"), 1902);
    }

    @Test
    public void test2Gen01904() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.03.2017"), 1903);
    }

    @Test
    public void test2Gen01905() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.03.2017"), 1904);
    }

    @Test
    public void test2Gen01906() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.03.2017"), 1905);
    }

    @Test
    public void test2Gen01907() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.03.2017"), 1906);
    }

    @Test
    public void test2Gen01908() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.03.2017"), 1907);
    }

    @Test
    public void test2Gen01909() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.03.2017"), 1908);
    }

    @Test
    public void test2Gen01910() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.03.2017"), 1909);
    }

    @Test
    public void test2Gen01911() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.03.2017"), 1910);
    }

    @Test
    public void test2Gen01912() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.03.2017"), 1911);
    }

    @Test
    public void test2Gen01913() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.03.2017"), 1912);
    }

    @Test
    public void test2Gen01914() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.03.2017"), 1913);
    }

    @Test
    public void test2Gen01915() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.03.2017"), 1914);
    }

    @Test
    public void test2Gen01916() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.03.2017"), 1915);
    }

    @Test
    public void test2Gen01917() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.03.2017"), 1916);
    }

    @Test
    public void test2Gen01918() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.04.2017"), 1917);
    }

    @Test
    public void test2Gen01919() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.04.2017"), 1918);
    }

    @Test
    public void test2Gen01920() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.04.2017"), 1919);
    }

    @Test
    public void test2Gen01921() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.04.2017"), 1920);
    }

    @Test
    public void test2Gen01922() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.04.2017"), 1921);
    }

    @Test
    public void test2Gen01923() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.04.2017"), 1922);
    }

    @Test
    public void test2Gen01924() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.04.2017"), 1923);
    }

    @Test
    public void test2Gen01925() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.04.2017"), 1924);
    }

    @Test
    public void test2Gen01926() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.04.2017"), 1925);
    }

    @Test
    public void test2Gen01927() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.04.2017"), 1926);
    }

    @Test
    public void test2Gen01928() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.04.2017"), 1927);
    }

    @Test
    public void test2Gen01929() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.04.2017"), 1928);
    }

    @Test
    public void test2Gen01930() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.04.2017"), 1929);
    }

    @Test
    public void test2Gen01931() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.04.2017"), 1930);
    }

    @Test
    public void test2Gen01932() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.04.2017"), 1931);
    }

    @Test
    public void test2Gen01933() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.04.2017"), 1932);
    }

    @Test
    public void test2Gen01934() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.04.2017"), 1933);
    }

    @Test
    public void test2Gen01935() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.04.2017"), 1934);
    }

    @Test
    public void test2Gen01936() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.04.2017"), 1935);
    }

    @Test
    public void test2Gen01937() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.04.2017"), 1936);
    }

    @Test
    public void test2Gen01938() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.04.2017"), 1937);
    }

    @Test
    public void test2Gen01939() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.04.2017"), 1938);
    }

    @Test
    public void test2Gen01940() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.04.2017"), 1939);
    }

    @Test
    public void test2Gen01941() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.04.2017"), 1940);
    }

    @Test
    public void test2Gen01942() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.04.2017"), 1941);
    }

    @Test
    public void test2Gen01943() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.04.2017"), 1942);
    }

    @Test
    public void test2Gen01944() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.04.2017"), 1943);
    }

    @Test
    public void test2Gen01945() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.04.2017"), 1944);
    }

    @Test
    public void test2Gen01946() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.04.2017"), 1945);
    }

    @Test
    public void test2Gen01947() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.04.2017"), 1946);
    }

    @Test
    public void test2Gen01948() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.05.2017"), 1947);
    }

    @Test
    public void test2Gen01949() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.05.2017"), 1948);
    }

    @Test
    public void test2Gen01950() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.05.2017"), 1949);
    }

    @Test
    public void test2Gen01951() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.05.2017"), 1950);
    }

    @Test
    public void test2Gen01952() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.05.2017"), 1951);
    }

    @Test
    public void test2Gen01953() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.05.2017"), 1952);
    }

    @Test
    public void test2Gen01954() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.05.2017"), 1953);
    }

    @Test
    public void test2Gen01955() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.05.2017"), 1954);
    }

    @Test
    public void test2Gen01956() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.05.2017"), 1955);
    }

    @Test
    public void test2Gen01957() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.05.2017"), 1956);
    }

    @Test
    public void test2Gen01958() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.05.2017"), 1957);
    }

    @Test
    public void test2Gen01959() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.05.2017"), 1958);
    }

    @Test
    public void test2Gen01960() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.05.2017"), 1959);
    }

    @Test
    public void test2Gen01961() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.05.2017"), 1960);
    }

    @Test
    public void test2Gen01962() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.05.2017"), 1961);
    }

    @Test
    public void test2Gen01963() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.05.2017"), 1962);
    }

    @Test
    public void test2Gen01964() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.05.2017"), 1963);
    }

    @Test
    public void test2Gen01965() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.05.2017"), 1964);
    }

    @Test
    public void test2Gen01966() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.05.2017"), 1965);
    }

    @Test
    public void test2Gen01967() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.05.2017"), 1966);
    }

    @Test
    public void test2Gen01968() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.05.2017"), 1967);
    }

    @Test
    public void test2Gen01969() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.05.2017"), 1968);
    }

    @Test
    public void test2Gen01970() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.05.2017"), 1969);
    }

    @Test
    public void test2Gen01971() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.05.2017"), 1970);
    }

    @Test
    public void test2Gen01972() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.05.2017"), 1971);
    }

    @Test
    public void test2Gen01973() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.05.2017"), 1972);
    }

    @Test
    public void test2Gen01974() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.05.2017"), 1973);
    }

    @Test
    public void test2Gen01975() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.05.2017"), 1974);
    }

    @Test
    public void test2Gen01976() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.05.2017"), 1975);
    }

    @Test
    public void test2Gen01977() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.05.2017"), 1976);
    }

    @Test
    public void test2Gen01978() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.05.2017"), 1977);
    }

    @Test
    public void test2Gen01979() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.06.2017"), 1978);
    }

    @Test
    public void test2Gen01980() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.06.2017"), 1979);
    }

    @Test
    public void test2Gen01981() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.06.2017"), 1980);
    }

    @Test
    public void test2Gen01982() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.06.2017"), 1981);
    }

    @Test
    public void test2Gen01983() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.06.2017"), 1982);
    }

    @Test
    public void test2Gen01984() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.06.2017"), 1983);
    }

    @Test
    public void test2Gen01985() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.06.2017"), 1984);
    }

    @Test
    public void test2Gen01986() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.06.2017"), 1985);
    }

    @Test
    public void test2Gen01987() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.06.2017"), 1986);
    }

    @Test
    public void test2Gen01988() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.06.2017"), 1987);
    }

    @Test
    public void test2Gen01989() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.06.2017"), 1988);
    }

    @Test
    public void test2Gen01990() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.06.2017"), 1989);
    }

    @Test
    public void test2Gen01991() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.06.2017"), 1990);
    }

    @Test
    public void test2Gen01992() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.06.2017"), 1991);
    }

    @Test
    public void test2Gen01993() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.06.2017"), 1992);
    }

    @Test
    public void test2Gen01994() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.06.2017"), 1993);
    }

    @Test
    public void test2Gen01995() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.06.2017"), 1994);
    }

    @Test
    public void test2Gen01996() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.06.2017"), 1995);
    }

    @Test
    public void test2Gen01997() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.06.2017"), 1996);
    }

    @Test
    public void test2Gen01998() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.06.2017"), 1997);
    }

    @Test
    public void test2Gen01999() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.06.2017"), 1998);
    }

    @Test
    public void test2Gen02000() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.06.2017"), 1999);
    }

    @Test
    public void test2Gen02001() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.06.2017"), 2000);
    }

    @Test
    public void test2Gen02002() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.06.2017"), 2001);
    }

    @Test
    public void test2Gen02003() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.06.2017"), 2002);
    }

    @Test
    public void test2Gen02004() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.06.2017"), 2003);
    }

    @Test
    public void test2Gen02005() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.06.2017"), 2004);
    }

    @Test
    public void test2Gen02006() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.06.2017"), 2005);
    }

    @Test
    public void test2Gen02007() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.06.2017"), 2006);
    }

    @Test
    public void test2Gen02008() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.06.2017"), 2007);
    }

    @Test
    public void test2Gen02009() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.07.2017"), 2008);
    }

    @Test
    public void test2Gen02010() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.07.2017"), 2009);
    }

    @Test
    public void test2Gen02011() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.07.2017"), 2010);
    }

    @Test
    public void test2Gen02012() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.07.2017"), 2011);
    }

    @Test
    public void test2Gen02013() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.07.2017"), 2012);
    }

    @Test
    public void test2Gen02014() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.07.2017"), 2013);
    }

    @Test
    public void test2Gen02015() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.07.2017"), 2014);
    }

    @Test
    public void test2Gen02016() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.07.2017"), 2015);
    }

    @Test
    public void test2Gen02017() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.07.2017"), 2016);
    }

    @Test
    public void test2Gen02018() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.07.2017"), 2017);
    }

    @Test
    public void test2Gen02019() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.07.2017"), 2018);
    }

    @Test
    public void test2Gen02020() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.07.2017"), 2019);
    }

    @Test
    public void test2Gen02021() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.07.2017"), 2020);
    }

    @Test
    public void test2Gen02022() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.07.2017"), 2021);
    }

    @Test
    public void test2Gen02023() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.07.2017"), 2022);
    }

    @Test
    public void test2Gen02024() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.07.2017"), 2023);
    }

    @Test
    public void test2Gen02025() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.07.2017"), 2024);
    }

    @Test
    public void test2Gen02026() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.07.2017"), 2025);
    }

    @Test
    public void test2Gen02027() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.07.2017"), 2026);
    }

    @Test
    public void test2Gen02028() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.07.2017"), 2027);
    }

    @Test
    public void test2Gen02029() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.07.2017"), 2028);
    }

    @Test
    public void test2Gen02030() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.07.2017"), 2029);
    }

    @Test
    public void test2Gen02031() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.07.2017"), 2030);
    }

    @Test
    public void test2Gen02032() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.07.2017"), 2031);
    }

    @Test
    public void test2Gen02033() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.07.2017"), 2032);
    }

    @Test
    public void test2Gen02034() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.07.2017"), 2033);
    }

    @Test
    public void test2Gen02035() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.07.2017"), 2034);
    }

    @Test
    public void test2Gen02036() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.07.2017"), 2035);
    }

    @Test
    public void test2Gen02037() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.07.2017"), 2036);
    }

    @Test
    public void test2Gen02038() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.07.2017"), 2037);
    }

    @Test
    public void test2Gen02039() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.07.2017"), 2038);
    }

    @Test
    public void test2Gen02040() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.08.2017"), 2039);
    }

    @Test
    public void test2Gen02041() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.08.2017"), 2040);
    }

    @Test
    public void test2Gen02042() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.08.2017"), 2041);
    }

    @Test
    public void test2Gen02043() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.08.2017"), 2042);
    }

    @Test
    public void test2Gen02044() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.08.2017"), 2043);
    }

    @Test
    public void test2Gen02045() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.08.2017"), 2044);
    }

    @Test
    public void test2Gen02046() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.08.2017"), 2045);
    }

    @Test
    public void test2Gen02047() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.08.2017"), 2046);
    }

    @Test
    public void test2Gen02048() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.08.2017"), 2047);
    }

    @Test
    public void test2Gen02049() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.08.2017"), 2048);
    }

    @Test
    public void test2Gen02050() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.08.2017"), 2049);
    }

    @Test
    public void test2Gen02051() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.08.2017"), 2050);
    }

    @Test
    public void test2Gen02052() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.08.2017"), 2051);
    }

    @Test
    public void test2Gen02053() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.08.2017"), 2052);
    }

    @Test
    public void test2Gen02054() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.08.2017"), 2053);
    }

    @Test
    public void test2Gen02055() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.08.2017"), 2054);
    }

    @Test
    public void test2Gen02056() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.08.2017"), 2055);
    }

    @Test
    public void test2Gen02057() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.08.2017"), 2056);
    }

    @Test
    public void test2Gen02058() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.08.2017"), 2057);
    }

    @Test
    public void test2Gen02059() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.08.2017"), 2058);
    }

    @Test
    public void test2Gen02060() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.08.2017"), 2059);
    }

    @Test
    public void test2Gen02061() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.08.2017"), 2060);
    }

    @Test
    public void test2Gen02062() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.08.2017"), 2061);
    }

    @Test
    public void test2Gen02063() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.08.2017"), 2062);
    }

    @Test
    public void test2Gen02064() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.08.2017"), 2063);
    }

    @Test
    public void test2Gen02065() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.08.2017"), 2064);
    }

    @Test
    public void test2Gen02066() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.08.2017"), 2065);
    }

    @Test
    public void test2Gen02067() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.08.2017"), 2066);
    }

    @Test
    public void test2Gen02068() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.08.2017"), 2067);
    }

    @Test
    public void test2Gen02069() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.08.2017"), 2068);
    }

    @Test
    public void test2Gen02070() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.08.2017"), 2069);
    }

    @Test
    public void test2Gen02071() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.09.2017"), 2070);
    }

    @Test
    public void test2Gen02072() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.09.2017"), 2071);
    }

    @Test
    public void test2Gen02073() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.09.2017"), 2072);
    }

    @Test
    public void test2Gen02074() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.09.2017"), 2073);
    }

    @Test
    public void test2Gen02075() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.09.2017"), 2074);
    }

    @Test
    public void test2Gen02076() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.09.2017"), 2075);
    }

    @Test
    public void test2Gen02077() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.09.2017"), 2076);
    }

    @Test
    public void test2Gen02078() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.09.2017"), 2077);
    }

    @Test
    public void test2Gen02079() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.09.2017"), 2078);
    }

    @Test
    public void test2Gen02080() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.09.2017"), 2079);
    }

    @Test
    public void test2Gen02081() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.09.2017"), 2080);
    }

    @Test
    public void test2Gen02082() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.09.2017"), 2081);
    }

    @Test
    public void test2Gen02083() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.09.2017"), 2082);
    }

    @Test
    public void test2Gen02084() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.09.2017"), 2083);
    }

    @Test
    public void test2Gen02085() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.09.2017"), 2084);
    }

    @Test
    public void test2Gen02086() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.09.2017"), 2085);
    }

    @Test
    public void test2Gen02087() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.09.2017"), 2086);
    }

    @Test
    public void test2Gen02088() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.09.2017"), 2087);
    }

    @Test
    public void test2Gen02089() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.09.2017"), 2088);
    }

    @Test
    public void test2Gen02090() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.09.2017"), 2089);
    }

    @Test
    public void test2Gen02091() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.09.2017"), 2090);
    }

    @Test
    public void test2Gen02092() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.09.2017"), 2091);
    }

    @Test
    public void test2Gen02093() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.09.2017"), 2092);
    }

    @Test
    public void test2Gen02094() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.09.2017"), 2093);
    }

    @Test
    public void test2Gen02095() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.09.2017"), 2094);
    }

    @Test
    public void test2Gen02096() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.09.2017"), 2095);
    }

    @Test
    public void test2Gen02097() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.09.2017"), 2096);
    }

    @Test
    public void test2Gen02098() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.09.2017"), 2097);
    }

    @Test
    public void test2Gen02099() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.09.2017"), 2098);
    }

    @Test
    public void test2Gen02100() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.09.2017"), 2099);
    }

    @Test
    public void test2Gen02101() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.10.2017"), 2100);
    }

    @Test
    public void test2Gen02102() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.10.2017"), 2101);
    }

    @Test
    public void test2Gen02103() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.10.2017"), 2102);
    }

    @Test
    public void test2Gen02104() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.10.2017"), 2103);
    }

    @Test
    public void test2Gen02105() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.10.2017"), 2104);
    }

    @Test
    public void test2Gen02106() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.10.2017"), 2105);
    }

    @Test
    public void test2Gen02107() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.10.2017"), 2106);
    }

    @Test
    public void test2Gen02108() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.10.2017"), 2107);
    }

    @Test
    public void test2Gen02109() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.10.2017"), 2108);
    }

    @Test
    public void test2Gen02110() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.10.2017"), 2109);
    }

    @Test
    public void test2Gen02111() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.10.2017"), 2110);
    }

    @Test
    public void test2Gen02112() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.10.2017"), 2111);
    }

    @Test
    public void test2Gen02113() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.10.2017"), 2112);
    }

    @Test
    public void test2Gen02114() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.10.2017"), 2113);
    }

    @Test
    public void test2Gen02115() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.10.2017"), 2114);
    }

    @Test
    public void test2Gen02116() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.10.2017"), 2115);
    }

    @Test
    public void test2Gen02117() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.10.2017"), 2116);
    }

    @Test
    public void test2Gen02118() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.10.2017"), 2117);
    }

    @Test
    public void test2Gen02119() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.10.2017"), 2118);
    }

    @Test
    public void test2Gen02120() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.10.2017"), 2119);
    }

    @Test
    public void test2Gen02121() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.10.2017"), 2120);
    }

    @Test
    public void test2Gen02122() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.10.2017"), 2121);
    }

    @Test
    public void test2Gen02123() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.10.2017"), 2122);
    }

    @Test
    public void test2Gen02124() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.10.2017"), 2123);
    }

    @Test
    public void test2Gen02125() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.10.2017"), 2124);
    }

    @Test
    public void test2Gen02126() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.10.2017"), 2125);
    }

    @Test
    public void test2Gen02127() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.10.2017"), 2126);
    }

    @Test
    public void test2Gen02128() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.10.2017"), 2127);
    }

    @Test
    public void test2Gen02129() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.10.2017"), 2128);
    }

    @Test
    public void test2Gen02130() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.10.2017"), 2129);
    }

    @Test
    public void test2Gen02131() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.10.2017"), 2130);
    }

    @Test
    public void test2Gen02132() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.11.2017"), 2131);
    }

    @Test
    public void test2Gen02133() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.11.2017"), 2132);
    }

    @Test
    public void test2Gen02134() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.11.2017"), 2133);
    }

    @Test
    public void test2Gen02135() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.11.2017"), 2134);
    }

    @Test
    public void test2Gen02136() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.11.2017"), 2135);
    }

    @Test
    public void test2Gen02137() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.11.2017"), 2136);
    }

    @Test
    public void test2Gen02138() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.11.2017"), 2137);
    }

    @Test
    public void test2Gen02139() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.11.2017"), 2138);
    }

    @Test
    public void test2Gen02140() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.11.2017"), 2139);
    }

    @Test
    public void test2Gen02141() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.11.2017"), 2140);
    }

    @Test
    public void test2Gen02142() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.11.2017"), 2141);
    }

    @Test
    public void test2Gen02143() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.11.2017"), 2142);
    }

    @Test
    public void test2Gen02144() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.11.2017"), 2143);
    }

    @Test
    public void test2Gen02145() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.11.2017"), 2144);
    }

    @Test
    public void test2Gen02146() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.11.2017"), 2145);
    }

    @Test
    public void test2Gen02147() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.11.2017"), 2146);
    }

    @Test
    public void test2Gen02148() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.11.2017"), 2147);
    }

    @Test
    public void test2Gen02149() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.11.2017"), 2148);
    }

    @Test
    public void test2Gen02150() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.11.2017"), 2149);
    }

    @Test
    public void test2Gen02151() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.11.2017"), 2150);
    }

    @Test
    public void test2Gen02152() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.11.2017"), 2151);
    }

    @Test
    public void test2Gen02153() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.11.2017"), 2152);
    }

    @Test
    public void test2Gen02154() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.11.2017"), 2153);
    }

    @Test
    public void test2Gen02155() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.11.2017"), 2154);
    }

    @Test
    public void test2Gen02156() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.11.2017"), 2155);
    }

    @Test
    public void test2Gen02157() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.11.2017"), 2156);
    }

    @Test
    public void test2Gen02158() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.11.2017"), 2157);
    }

    @Test
    public void test2Gen02159() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.11.2017"), 2158);
    }

    @Test
    public void test2Gen02160() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.11.2017"), 2159);
    }

    @Test
    public void test2Gen02161() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.11.2017"), 2160);
    }

    @Test
    public void test2Gen02162() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.12.2017"), 2161);
    }

    @Test
    public void test2Gen02163() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.12.2017"), 2162);
    }

    @Test
    public void test2Gen02164() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.12.2017"), 2163);
    }

    @Test
    public void test2Gen02165() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.12.2017"), 2164);
    }

    @Test
    public void test2Gen02166() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.12.2017"), 2165);
    }

    @Test
    public void test2Gen02167() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.12.2017"), 2166);
    }

    @Test
    public void test2Gen02168() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.12.2017"), 2167);
    }

    @Test
    public void test2Gen02169() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.12.2017"), 2168);
    }

    @Test
    public void test2Gen02170() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.12.2017"), 2169);
    }

    @Test
    public void test2Gen02171() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.12.2017"), 2170);
    }

    @Test
    public void test2Gen02172() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.12.2017"), 2171);
    }

    @Test
    public void test2Gen02173() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.12.2017"), 2172);
    }

    @Test
    public void test2Gen02174() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.12.2017"), 2173);
    }

    @Test
    public void test2Gen02175() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.12.2017"), 2174);
    }

    @Test
    public void test2Gen02176() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.12.2017"), 2175);
    }

    @Test
    public void test2Gen02177() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.12.2017"), 2176);
    }

    @Test
    public void test2Gen02178() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.12.2017"), 2177);
    }

    @Test
    public void test2Gen02179() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.12.2017"), 2178);
    }

    @Test
    public void test2Gen02180() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.12.2017"), 2179);
    }

    @Test
    public void test2Gen02181() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.12.2017"), 2180);
    }

    @Test
    public void test2Gen02182() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.12.2017"), 2181);
    }

    @Test
    public void test2Gen02183() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.12.2017"), 2182);
    }

    @Test
    public void test2Gen02184() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.12.2017"), 2183);
    }

    @Test
    public void test2Gen02185() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.12.2017"), 2184);
    }

    @Test
    public void test2Gen02186() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.12.2017"), 2185);
    }

    @Test
    public void test2Gen02187() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.12.2017"), 2186);
    }

    @Test
    public void test2Gen02188() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.12.2017"), 2187);
    }

    @Test
    public void test2Gen02189() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.12.2017"), 2188);
    }

    @Test
    public void test2Gen02190() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.12.2017"), 2189);
    }

    @Test
    public void test2Gen02191() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.12.2017"), 2190);
    }

    @Test
    public void test2Gen02192() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.12.2017"), 2191);
    }

    @Test
    public void test2Gen02193() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.01.2018"), 2192);
    }

    @Test
    public void test2Gen02194() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.01.2018"), 2193);
    }

    @Test
    public void test2Gen02195() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.01.2018"), 2194);
    }

    @Test
    public void test2Gen02196() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.01.2018"), 2195);
    }

    @Test
    public void test2Gen02197() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.01.2018"), 2196);
    }

    @Test
    public void test2Gen02198() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.01.2018"), 2197);
    }

    @Test
    public void test2Gen02199() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.01.2018"), 2198);
    }

    @Test
    public void test2Gen02200() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.01.2018"), 2199);
    }

    @Test
    public void test2Gen02201() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.01.2018"), 2200);
    }

    @Test
    public void test2Gen02202() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.01.2018"), 2201);
    }

    @Test
    public void test2Gen02203() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.01.2018"), 2202);
    }

    @Test
    public void test2Gen02204() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.01.2018"), 2203);
    }

    @Test
    public void test2Gen02205() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.01.2018"), 2204);
    }

    @Test
    public void test2Gen02206() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.01.2018"), 2205);
    }

    @Test
    public void test2Gen02207() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.01.2018"), 2206);
    }

    @Test
    public void test2Gen02208() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.01.2018"), 2207);
    }

    @Test
    public void test2Gen02209() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.01.2018"), 2208);
    }

    @Test
    public void test2Gen02210() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.01.2018"), 2209);
    }

    @Test
    public void test2Gen02211() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.01.2018"), 2210);
    }

    @Test
    public void test2Gen02212() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.01.2018"), 2211);
    }

    @Test
    public void test2Gen02213() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.01.2018"), 2212);
    }

    @Test
    public void test2Gen02214() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.01.2018"), 2213);
    }

    @Test
    public void test2Gen02215() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.01.2018"), 2214);
    }

    @Test
    public void test2Gen02216() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.01.2018"), 2215);
    }

    @Test
    public void test2Gen02217() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.01.2018"), 2216);
    }

    @Test
    public void test2Gen02218() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.01.2018"), 2217);
    }

    @Test
    public void test2Gen02219() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.01.2018"), 2218);
    }

    @Test
    public void test2Gen02220() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.01.2018"), 2219);
    }

    @Test
    public void test2Gen02221() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.01.2018"), 2220);
    }

    @Test
    public void test2Gen02222() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.01.2018"), 2221);
    }

    @Test
    public void test2Gen02223() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.01.2018"), 2222);
    }

    @Test
    public void test2Gen02224() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.02.2018"), 2223);
    }

    @Test
    public void test2Gen02225() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.02.2018"), 2224);
    }

    @Test
    public void test2Gen02226() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.02.2018"), 2225);
    }

    @Test
    public void test2Gen02227() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.02.2018"), 2226);
    }

    @Test
    public void test2Gen02228() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.02.2018"), 2227);
    }

    @Test
    public void test2Gen02229() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.02.2018"), 2228);
    }

    @Test
    public void test2Gen02230() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.02.2018"), 2229);
    }

    @Test
    public void test2Gen02231() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.02.2018"), 2230);
    }

    @Test
    public void test2Gen02232() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.02.2018"), 2231);
    }

    @Test
    public void test2Gen02233() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.02.2018"), 2232);
    }

    @Test
    public void test2Gen02234() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.02.2018"), 2233);
    }

    @Test
    public void test2Gen02235() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.02.2018"), 2234);
    }

    @Test
    public void test2Gen02236() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.02.2018"), 2235);
    }

    @Test
    public void test2Gen02237() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.02.2018"), 2236);
    }

    @Test
    public void test2Gen02238() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.02.2018"), 2237);
    }

    @Test
    public void test2Gen02239() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.02.2018"), 2238);
    }

    @Test
    public void test2Gen02240() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.02.2018"), 2239);
    }

    @Test
    public void test2Gen02241() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.02.2018"), 2240);
    }

    @Test
    public void test2Gen02242() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.02.2018"), 2241);
    }

    @Test
    public void test2Gen02243() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.02.2018"), 2242);
    }

    @Test
    public void test2Gen02244() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.02.2018"), 2243);
    }

    @Test
    public void test2Gen02245() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.02.2018"), 2244);
    }

    @Test
    public void test2Gen02246() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.02.2018"), 2245);
    }

    @Test
    public void test2Gen02247() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.02.2018"), 2246);
    }

    @Test
    public void test2Gen02248() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.02.2018"), 2247);
    }

    @Test
    public void test2Gen02249() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.02.2018"), 2248);
    }

    @Test
    public void test2Gen02250() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.02.2018"), 2249);
    }

    @Test
    public void test2Gen02251() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.02.2018"), 2250);
    }

    @Test
    public void test2Gen02252() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.03.2018"), 2251);
    }

    @Test
    public void test2Gen02253() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.03.2018"), 2252);
    }

    @Test
    public void test2Gen02254() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.03.2018"), 2253);
    }

    @Test
    public void test2Gen02255() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.03.2018"), 2254);
    }

    @Test
    public void test2Gen02256() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.03.2018"), 2255);
    }

    @Test
    public void test2Gen02257() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.03.2018"), 2256);
    }

    @Test
    public void test2Gen02258() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.03.2018"), 2257);
    }

    @Test
    public void test2Gen02259() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.03.2018"), 2258);
    }

    @Test
    public void test2Gen02260() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.03.2018"), 2259);
    }

    @Test
    public void test2Gen02261() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.03.2018"), 2260);
    }

    @Test
    public void test2Gen02262() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.03.2018"), 2261);
    }

    @Test
    public void test2Gen02263() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.03.2018"), 2262);
    }

    @Test
    public void test2Gen02264() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.03.2018"), 2263);
    }

    @Test
    public void test2Gen02265() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.03.2018"), 2264);
    }

    @Test
    public void test2Gen02266() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.03.2018"), 2265);
    }

    @Test
    public void test2Gen02267() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.03.2018"), 2266);
    }

    @Test
    public void test2Gen02268() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.03.2018"), 2267);
    }

    @Test
    public void test2Gen02269() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.03.2018"), 2268);
    }

    @Test
    public void test2Gen02270() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.03.2018"), 2269);
    }

    @Test
    public void test2Gen02271() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.03.2018"), 2270);
    }

    @Test
    public void test2Gen02272() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.03.2018"), 2271);
    }

    @Test
    public void test2Gen02273() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.03.2018"), 2272);
    }

    @Test
    public void test2Gen02274() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.03.2018"), 2273);
    }

    @Test
    public void test2Gen02275() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.03.2018"), 2274);
    }

    @Test
    public void test2Gen02276() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.03.2018"), 2275);
    }

    @Test
    public void test2Gen02277() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.03.2018"), 2276);
    }

    @Test
    public void test2Gen02278() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.03.2018"), 2277);
    }

    @Test
    public void test2Gen02279() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.03.2018"), 2278);
    }

    @Test
    public void test2Gen02280() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.03.2018"), 2279);
    }

    @Test
    public void test2Gen02281() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.03.2018"), 2280);
    }

    @Test
    public void test2Gen02282() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "31.03.2018"), 2281);
    }

    @Test
    public void test2Gen02283() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.04.2018"), 2282);
    }

    @Test
    public void test2Gen02284() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.04.2018"), 2283);
    }

    @Test
    public void test2Gen02285() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.04.2018"), 2284);
    }

    @Test
    public void test2Gen02286() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.04.2018"), 2285);
    }

    @Test
    public void test2Gen02287() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.04.2018"), 2286);
    }

    @Test
    public void test2Gen02288() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.04.2018"), 2287);
    }

    @Test
    public void test2Gen02289() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.04.2018"), 2288);
    }

    @Test
    public void test2Gen02290() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.04.2018"), 2289);
    }

    @Test
    public void test2Gen02291() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.04.2018"), 2290);
    }

    @Test
    public void test2Gen02292() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.04.2018"), 2291);
    }

    @Test
    public void test2Gen02293() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.04.2018"), 2292);
    }

    @Test
    public void test2Gen02294() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.04.2018"), 2293);
    }

    @Test
    public void test2Gen02295() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.04.2018"), 2294);
    }

    @Test
    public void test2Gen02296() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.04.2018"), 2295);
    }

    @Test
    public void test2Gen02297() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.04.2018"), 2296);
    }

    @Test
    public void test2Gen02298() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.04.2018"), 2297);
    }

    @Test
    public void test2Gen02299() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.04.2018"), 2298);
    }

    @Test
    public void test2Gen02300() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.04.2018"), 2299);
    }

    @Test
    public void test2Gen02301() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.04.2018"), 2300);
    }

    @Test
    public void test2Gen02302() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.04.2018"), 2301);
    }

    @Test
    public void test2Gen02303() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.04.2018"), 2302);
    }

    @Test
    public void test2Gen02304() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.04.2018"), 2303);
    }

    @Test
    public void test2Gen02305() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.04.2018"), 2304);
    }

    @Test
    public void test2Gen02306() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.04.2018"), 2305);
    }

    @Test
    public void test2Gen02307() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.04.2018"), 2306);
    }

    @Test
    public void test2Gen02308() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "26.04.2018"), 2307);
    }

    @Test
    public void test2Gen02309() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "27.04.2018"), 2308);
    }

    @Test
    public void test2Gen02310() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.04.2018"), 2309);
    }

    @Test
    public void test2Gen02311() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "29.04.2018"), 2310);
    }

    @Test
    public void test2Gen02312() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.04.2018"), 2311);
    }

    @Test
    public void test2Gen02313() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "01.05.2018"), 2312);
    }

    @Test
    public void test2Gen02314() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.05.2018"), 2313);
    }

    @Test
    public void test2Gen02315() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "03.05.2018"), 2314);
    }

    @Test
    public void test2Gen02316() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.05.2018"), 2315);
    }

    @Test
    public void test2Gen02317() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.05.2018"), 2316);
    }

    @Test
    public void test2Gen02318() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "06.05.2018"), 2317);
    }

    @Test
    public void test2Gen02319() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "07.05.2018"), 2318);
    }

    @Test
    public void test2Gen02320() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "08.05.2018"), 2319);
    }

    @Test
    public void test2Gen02321() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "09.05.2018"), 2320);
    }

    @Test
    public void test2Gen02322() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "10.05.2018"), 2321);
    }

    @Test
    public void test2Gen02323() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "11.05.2018"), 2322);
    }

    @Test
    public void test2Gen02324() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.05.2018"), 2323);
    }

    @Test
    public void test2Gen02325() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "13.05.2018"), 2324);
    }

    @Test
    public void test2Gen02326() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "14.05.2018"), 2325);
    }

    @Test
    public void test2Gen02327() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.05.2018"), 2326);
    }

    @Test
    public void test2Gen02328() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "16.05.2018"), 2327);
    }

    @Test
    public void test2Gen02329() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.05.2018"), 2328);
    }

    @Test
    public void test2Gen02330() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "18.05.2018"), 2329);
    }

    @Test
    public void test2Gen02331() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "19.05.2018"), 2330);
    }

    @Test
    public void test2Gen02332() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.05.2018"), 2331);
    }

    @Test
    public void test2Gen02333() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "21.05.2018"), 2332);
    }

    @Test
    public void test2Gen02334() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.05.2018"), 2333);
    }

    @Test
    public void test2Gen02335() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "23.05.2018"), 2334);
    }

    @Test
    public void test2Gen02336() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "24.05.2018"), 2335);
    }

    @Test
    public void test2Gen02337() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.05.2018"), 2336);
    }

    @Test
    public void test2SitaxBug00001() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "04.12.2013"), 703);
    }

    @Test
    public void test2SitaxBug00002() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "17.11.2013"), 686);
    }

    @Test
    public void test2SitaxBug00003() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.10.2013"), 650);
    }

    @Test
    public void test2SitaxBug00004() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "12.10.2012"), 285);
    }

    @Test
    public void test2SitaxBug00005() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "22.09.2013"), 630);
    }

    @Test
    public void test2SitaxBug00006() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "25.08.2013"), 602);
    }

    @Test
    public void test2SitaxBug00007() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.05.2013"), 490);
    }

    @Test
    public void test2SitaxBug00008() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "05.04.2013"), 460);
    }

    @Test
    public void test2SitaxBug00009() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "20.03.2013"), 444);
    }

    @Test
    public void test2SitaxBug00010() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.02.2013"), 424);
    }

    @Test
    public void test2SitaxBug00011() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "28.01.2013"), 393);
    }

    @Test
    public void test2SitaxBug00012() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.02.2013"), 411);
    }

    @Test
    public void test2SitaxBug00013() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "30.01.2013"), 395);
    }

    @Test
    public void test2SitaxBug00014() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "15.01.2013"), 380);
    }

    @Test
    public void test2SitaxBug00015() {
        Assert.assertEquals(PRDateUtils.getNbDayBetween2("01.01.2012", "02.01.2013"), 367);
    }
}
