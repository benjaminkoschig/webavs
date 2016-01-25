package globaz.orion.process;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ValidationAutomatiqueTest {
    private BigDecimal montantFatcure;
    private BigDecimal masse;
    private boolean expected;
    private boolean checkMontantNegatif;

    public ValidationAutomatiqueTest(boolean expected, float montantFatcure, float masse, boolean checkMontantNegatif) {
        this.montantFatcure = new BigDecimal(montantFatcure, new MathContext(16, RoundingMode.HALF_UP));
        this.masse = new BigDecimal(masse, new MathContext(16, RoundingMode.HALF_UP));
        this.expected = expected;
        this.checkMontantNegatif = checkMontantNegatif;
    }

    @Parameters(name = "masse={2}, montantFatcure={1}, expected ={0}")
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][] { { true, 30, 1000, false }, { true, 30, 1000, true },
                { false, -30, 1000, true }, { true, 100, 1000, false }, { false, 150, 1000, false },
                { true, 120, 1500, false }, { true, 150, 2000, false }, { false, 2001, 50000, false },
                { false, -150, 1000, false }, { false, -2001, 50000, false }, { true, 0, 0, false } });
    }

    @Test
    public void testIsMontantValide() throws Exception {
        BigDecimal pourcentage = new BigDecimal(10, new MathContext(16, RoundingMode.HALF_UP));
        BigDecimal limit = new BigDecimal(2000, new MathContext(16, RoundingMode.HALF_UP));
        ValidationAutomatique validationAutomatique = new ValidationAutomatique(pourcentage, limit, montantFatcure,
                masse, checkMontantNegatif);
        assertEquals(expected, validationAutomatique.isMontantValide());
    }

    @Test
    public void testIsControleLimitValide() throws Exception {
        BigDecimal pourcentage = new BigDecimal(10, new MathContext(16, RoundingMode.HALF_UP));
        BigDecimal limit = new BigDecimal(50000, new MathContext(16, RoundingMode.HALF_UP));
        ValidationAutomatique validationAutomatique = new ValidationAutomatique(pourcentage, limit, new BigDecimal(
                -6674.30), new BigDecimal(10000), false);
        assertEquals(true, validationAutomatique.isControleLimitValide());
    }

}
