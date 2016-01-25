package ch.globaz.common.domaine;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.junit.Test;
import ch.globaz.common.domaine.Part;

public class PartTest {

    @Test
    public void testPartStringString() throws Exception {
        String nominateur = null;
        String denominateur = null;
        Part part = new Part(nominateur, denominateur);
        Part partExpected = new Part("0", "0");
        assertEquals(partExpected.getNumerateur(), part.getNumerateur());
        assertEquals(partExpected.getDenominateur(), part.getDenominateur());

        part = new Part("1", "2");
        assertEquals(new BigDecimal(1), part.getNumerateur());
        assertEquals(new BigDecimal(2), part.getDenominateur());
    }

    @Test
    public void testPartBigDecimalBigDecimal() throws Exception {
        Part part = new Part(new BigDecimal(1), new BigDecimal(2));
        assertEquals(new BigDecimal(1), part.getNumerateur());
        assertEquals(new BigDecimal(2), part.getDenominateur());
    }

    @Test
    public void testResultat() throws Exception {
        Part part = new Part(new BigDecimal(2), new BigDecimal(2));
        assertEquals(new BigDecimal(1), part.resultat());

        part = new Part(new BigDecimal(1), new BigDecimal(2));
        assertEquals(new BigDecimal(0.5), part.resultat());
        part = new Part(new BigDecimal(1), new BigDecimal(3));
        assertEquals(new BigDecimal(0.3333, new MathContext(4, RoundingMode.HALF_UP)), part.resultat());
    }

}
