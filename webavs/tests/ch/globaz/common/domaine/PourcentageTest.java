package ch.globaz.common.domaine;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class PourcentageTest {

    private static final double PRECISION_CALCUL_ATTENDUE = 0.0001;

    @Test
    public void testMultiplier() throws Exception {

        Assert.assertEquals(new BigDecimal(0.0), Pourcentage.ZERO_POURCENT.multiplier(BigDecimal.ONE));
        Assert.assertEquals(new BigDecimal(0.5), Pourcentage.CINQUANTE_POURCENT.multiplier(BigDecimal.ONE));
        Assert.assertEquals(new BigDecimal(1.0), Pourcentage.CENT_POURCENT.multiplier(BigDecimal.ONE));

        Assert.assertEquals(0.0, Pourcentage.ZERO_POURCENT.multiplier(1.0), PourcentageTest.PRECISION_CALCUL_ATTENDUE);
        Assert.assertEquals(0.5, Pourcentage.CINQUANTE_POURCENT.multiplier(1.0),
                PourcentageTest.PRECISION_CALCUL_ATTENDUE);
        Assert.assertEquals(1.0, Pourcentage.CENT_POURCENT.multiplier(1.0), PourcentageTest.PRECISION_CALCUL_ATTENDUE);

        Assert.assertEquals(0.75, new Pourcentage(75).multiplier(1.0), PourcentageTest.PRECISION_CALCUL_ATTENDUE);

        Assert.assertEquals(0.66, new Pourcentage(66).multiplier(1.0), PourcentageTest.PRECISION_CALCUL_ATTENDUE);
        Assert.assertEquals(0.33, new Pourcentage(33).multiplier(1.0), PourcentageTest.PRECISION_CALCUL_ATTENDUE);

        Assert.assertEquals(0.99, new Pourcentage(99).multiplier(1.0), PourcentageTest.PRECISION_CALCUL_ATTENDUE);
        Assert.assertEquals(0.999, new Pourcentage(99.9).multiplier(1.0), PourcentageTest.PRECISION_CALCUL_ATTENDUE);
        Assert.assertEquals(0.9999, new Pourcentage(99.99).multiplier(1.0), PourcentageTest.PRECISION_CALCUL_ATTENDUE);

        Assert.assertEquals(0, Pourcentage.ZERO_POURCENT.multiplier(1));
        Assert.assertEquals(0, Pourcentage.CINQUANTE_POURCENT.multiplier(1));
        Assert.assertEquals(1, Pourcentage.CENT_POURCENT.multiplier(1));

        Assert.assertEquals(7, new Pourcentage(75).multiplier(10));
    }

    @Test
    public void testPourcentage() throws Exception {
        int i = -1000;
        try {
            for (i = 0; i < 2000; i++) {
                new Pourcentage(i);
            }
        } catch (Exception ex) {
            Assert.fail("Erreur avec un pourcentage de " + i + "%");
        }
    }

}
