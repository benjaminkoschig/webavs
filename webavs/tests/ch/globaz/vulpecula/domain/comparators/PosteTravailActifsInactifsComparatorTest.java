package ch.globaz.vulpecula.domain.comparators;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public class PosteTravailActifsInactifsComparatorTest {
    private PosteTravail p1;
    private PosteTravail p2;
    private final PosteTravailActifsInactifsComparator posteTravail = new PosteTravailActifsInactifsComparator();

    @Before
    public void setUp() {
        p1 = new PosteTravail();
        p2 = new PosteTravail();
    }

    @Test
    public void givenTwoEmptyPostesWhenCompareThenReturn0() {
        int actual = posteTravail.compare(p1, p2);
        assertEquals(0, actual);
    }

    @Test
    public void givenTwoActifsPostesWithSamePeriodesWhenCompareThenReturn0() {
        Periode periode1 = new Periode(new Date("01.01.2013"), null);
        Periode periode2 = new Periode(new Date("01.01.2013"), null);

        p1.setPeriodeActivite(periode1);
        p2.setPeriodeActivite(periode2);

        int actual = posteTravail.compare(p1, p2);

        assertEquals(0, actual);
    }

    @Test
    public void givenOneActifAndOneInactifPosteWhenCompareThenReturn1() {
        Periode periode1 = new Periode(new Date("01.01.2013"), null);
        Periode periode2 = new Periode(new Date("01.01.2013"), new Date("01.03.2013"));

        p1.setPeriodeActivite(periode1);
        p2.setPeriodeActivite(periode2);

        int actual = posteTravail.compare(p1, p2);

        assertEquals(-1, actual);
    }

    @Test
    public void givenOneInactifAndOneActifPosteWhenCompareThenReturnMinus1() {
        Periode periode1 = new Periode(new Date("01.01.2013"), new Date("01.03.2013"));
        Periode periode2 = new Periode(new Date("01.01.2013"), null);

        p1.setPeriodeActivite(periode1);
        p2.setPeriodeActivite(periode2);

        int actual = posteTravail.compare(p1, p2);

        assertEquals(1, actual);
    }

    @Test
    public void givenTwoActifsWithDifferentesPeriodesThenReturnMinus1() {
        Periode periode1 = new Periode(new Date("03.01.2013"), null);
        Periode periode2 = new Periode(new Date("01.01.2013"), null);

        p1.setPeriodeActivite(periode1);
        p2.setPeriodeActivite(periode2);

        int actual = posteTravail.compare(p1, p2);

        assertEquals(-1, actual);
    }
}
