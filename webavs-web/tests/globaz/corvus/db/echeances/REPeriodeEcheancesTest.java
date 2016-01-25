package globaz.corvus.db.echeances;

import java.util.SortedSet;
import java.util.TreeSet;
import junit.framework.Assert;
import org.junit.Test;
import ch.globaz.corvus.business.models.echeances.IREPeriodeEcheances;

public class REPeriodeEcheancesTest {

    @Test
    public void testCompareTo() {
        REPeriodeEcheances periode1 = new REPeriodeEcheances();
        REPeriodeEcheances periode2 = new REPeriodeEcheances();

        periode1.setDateFin("01.01.2000");
        periode2.setDateFin("02.01.2000");
        Assert.assertEquals(-1, periode1.compareTo(periode2));

        periode2.setDateFin("31.12.1999");
        Assert.assertEquals(1, periode1.compareTo(periode2));

        periode1.setDateDebut("01.02.2000");
        periode2.setDateFin("01.01.2000");
        periode2.setDateDebut("31.01.2000");
        Assert.assertEquals(1, periode1.compareTo(periode2));

        periode2.setDateDebut("02.02.2000");
        Assert.assertEquals(-1, periode1.compareTo(periode2));

        periode2.setDateDebut("01.02.2000");
        Assert.assertEquals(0, periode1.compareTo(periode2));
    }

    @Test
    public void testSort() {
        SortedSet<REPeriodeEcheances> sortedSet = new TreeSet<REPeriodeEcheances>();

        REPeriodeEcheances periode1 = new REPeriodeEcheances();
        periode1.setDateDebut("01.01.2000");
        periode1.setDateFin("31.01.2000");
        sortedSet.add(periode1);

        REPeriodeEcheances periode2 = new REPeriodeEcheances();
        periode2.setDateDebut("02.01.2000");
        periode2.setDateFin("30.01.2000");
        sortedSet.add(periode2);

        REPeriodeEcheances periode3 = new REPeriodeEcheances();
        periode3.setDateDebut("01.03.2000");
        periode3.setDateFin("");
        sortedSet.add(periode3);

        REPeriodeEcheances periode4 = new REPeriodeEcheances();
        periode4.setDateDebut("01.01.2000");
        periode4.setDateFin("");
        sortedSet.add(periode4);

        REPeriodeEcheances periode5 = new REPeriodeEcheances();
        periode5.setDateDebut("01.01.2000");
        periode5.setDateFin("15.01.2000");
        sortedSet.add(periode5);

        REPeriodeEcheances periode6 = new REPeriodeEcheances();
        periode6.setDateDebut("02.01.2000");
        periode6.setDateFin("15.01.2000");
        sortedSet.add(periode6);

        for (IREPeriodeEcheances periode : sortedSet) {
            System.out.println(periode.getDateDebut() + " - " + periode.getDateFin());
        }
    }
}
