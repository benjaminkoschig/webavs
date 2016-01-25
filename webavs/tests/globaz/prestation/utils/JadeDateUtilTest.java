package globaz.prestation.utils;

import globaz.jade.client.util.JadeDateUtil;
import globaz.prestation.beans.PRPeriode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.Test;

public class JadeDateUtilTest {

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
    public void test() {
        assertTrue(new PeriodeTest("01.01.2013", "31.01.2013", 30));
        assertTrue(new PeriodeTest("01.01.2013", "01.02.2013", 31));
        assertTrue(new PeriodeTest("01.03.2013", "31.03.2013", 30));
        assertTrue(new PeriodeTest("01.03.2013", "01.04.2013", 31));

    }

    private void assertTrue(PeriodeTest periode) {
        int nombreJoursCalcule = getNbDayBetweenCompense(periode.getDateDeDebut(), periode.getDateDeFin());
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
        } else {
            System.out.println(sb.toString());
        }
    }

    private int getNbDayBetweenCompense(String date1, String date2) {
        if (JadeDateUtil.areDatesEquals(date1, date2)) {
            return 0;
        }
        int ctr = 0;
        String[] array1 = date1.split("\\.");
        GregorianCalendar dateBefore = convertStringToCalendar(date1);
        GregorianCalendar nextDate = dateBefore;

        GregorianCalendar stopDate = convertStringToCalendar(date2);

        while (!stopDate.equals(nextDate) && (ctr < 1000)) {
            ctr++;
            System.out.print(calendarToString(nextDate));
            System.out.print(" -> ");
            nextDate.add(Calendar.DAY_OF_MONTH, 1);
            System.out.println(calendarToString(nextDate));
            if (ctr > 200) {
                throw new RuntimeException("Out of Bounds");
            }
        }
        return ctr;
    }

    private String calendarToString(GregorianCalendar calendar) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(calendar.get(Calendar.DAY_OF_MONTH));
        sb.append(".");
        sb.append(calendar.get(Calendar.MONTH));
        sb.append(".");
        sb.append(calendar.get(Calendar.YEAR));
        sb.append(".");
        sb.append("]");
        return sb.toString();
    }

    private GregorianCalendar convertStringToCalendar(String date) {
        String[] array = date.split("\\.");
        if (array.length != 3) {
            throw new RuntimeException("Error on splitting date : " + date);
        }
        return new GregorianCalendar(Integer.valueOf(array[2]), Integer.valueOf(array[1]), Integer.valueOf(array[0]));
    }

    private int getNbDayBetween(String beforeDate, String afterDate) {
        long numberOfMilli_InDay = (1000 * 60 * 60 * 24);

        if (JadeDateUtil.areDatesEquals(beforeDate, afterDate)) {
            return 0;
        }

        // parse dates
        Date dateBefore = JadeDateUtil.getGlobazDate(beforeDate);
        Date dateAfter = JadeDateUtil.getGlobazDate(afterDate);
        int offsetAMinutes = dateBefore.getTimezoneOffset();
        int offsetBMinutes = dateAfter.getTimezoneOffset();
        System.out.println(dateBefore.toString());
        System.out.println(dateAfter.toString());
        int ctr = 0;
        if ((dateBefore != null) && (dateAfter != null) && (dateAfter.getTime() >= dateBefore.getTime())) {
            long numberOfMillis = (dateAfter.getTime() - dateBefore.getTime());
            while (numberOfMillis >= numberOfMilli_InDay) {
                numberOfMillis = numberOfMillis - numberOfMilli_InDay;
                ctr++;
            }
            // compensation heure d'été
            if (numberOfMillis >= (numberOfMilli_InDay / 2)) {
                ctr++;
            }
            System.out.println("Millis in a day = " + numberOfMilli_InDay + ", reste = " + numberOfMillis);
        }
        return ctr;
    }
}
