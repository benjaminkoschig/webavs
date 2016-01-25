package ch.globaz.pegasus.businessimpl.services.models.calcul;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class CalculMontantTest {

    class BigDecimalResult {

        BigDecimal expected;
        BigDecimal value;

        public BigDecimalResult(BigDecimal value, BigDecimal expected) {
            this.expected = expected;
            this.value = value;
        }

    }

    @Test
    public void BigDecimalRound() {
        BigDecimal valeur = (new BigDecimal(2.54635645)).setScale(2, BigDecimal.ROUND_CEILING); // 2.55
        BigDecimal valeur2 = (new BigDecimal(2.000001)).setScale(2, BigDecimal.ROUND_DOWN); // 2.55
        BigDecimal valeur3 = (new BigDecimal(0.745)).setScale(2, BigDecimal.ROUND_CEILING); // 2.55
        System.out.println(valeur);
        System.out.println(valeur2);
        System.out.println(valeur3);

    }

    @Test
    public void BigDecimalRoundingTest() {

        List<BigDecimalResult> listesMontant = new ArrayList<BigDecimalResult>();

        listesMontant.add(new BigDecimalResult(new BigDecimal(2.000001).setScale(0, BigDecimal.ROUND_DOWN),
                new BigDecimal(2.00).setScale(0)));
        listesMontant.add(new BigDecimalResult(new BigDecimal(2.01).setScale(0, BigDecimal.ROUND_DOWN), new BigDecimal(
                2.00).setScale(0)));
        listesMontant.add(new BigDecimalResult(new BigDecimal(2.44).setScale(0, BigDecimal.ROUND_DOWN), new BigDecimal(
                2.00).setScale(0)));
        listesMontant.add(new BigDecimalResult(new BigDecimal(2.4999999).setScale(0, BigDecimal.ROUND_DOWN),
                new BigDecimal(2.00)));
        listesMontant.add(new BigDecimalResult(new BigDecimal(2.5).setScale(0, BigDecimal.ROUND_CEILING),
                new BigDecimal(3.00).setScale(0)));
        listesMontant.add(new BigDecimalResult(new BigDecimal(2.99000001).setScale(0, BigDecimal.ROUND_CEILING),
                new BigDecimal(3.00).setScale(0)));
        listesMontant.add(new BigDecimalResult(new BigDecimal(2.09).setScale(0, BigDecimal.ROUND_DOWN), new BigDecimal(
                2.00).setScale(0)));
        listesMontant.add(new BigDecimalResult(new BigDecimal(2.549999).setScale(0, BigDecimal.ROUND_CEILING),
                new BigDecimal(3.00).setScale(0)));
        listesMontant.add(new BigDecimalResult(new BigDecimal(2.0000009).setScale(0, BigDecimal.ROUND_DOWN),
                new BigDecimal(2.00).setScale(0)));

        for (BigDecimalResult result : listesMontant) {
            Assert.assertTrue("Value: " + result.value + ", expected: " + result.expected,
                    result.value.equals(result.expected));
        }

    }

    @Ignore
    @Test
    public void testArondiBigDecimal() {
        float montant1 = 3.456656f; // considere 3.45 --> arrondi 3
        float montant2 = 2.012121f; // considere 2.01 --> arrondi 2
        float montant3 = 1.502131f; // considere 1.50 --> arrondi 2

        BigDecimal trois = new BigDecimal(3);
        BigDecimal deux = new BigDecimal(2);

        BigDecimal montant1F = new BigDecimal(montant1);

        BigDecimal montant2F = new BigDecimal(montant2);
        BigDecimal montant3F = new BigDecimal(montant3);

        Assert.assertTrue("montant1F: " + montant1F + ", montant1:" + montant1, montant1F.equals(trois));
        Assert.assertTrue(montant2F.equals(deux));
        Assert.assertTrue(montant3F.equals(deux));
    }

    @Test
    public void testArrodiStandard() {

        float montant1 = 3.456656f; // considere 3.45 --> arrondi 3
        float montant2 = 2.012121f; // considere 2.01 --> arrondi 2
        float montant3 = 1.502131f; // considere 1.50 --> arrondi 2

        int montant1F = Math.round(montant1);
        int montant2F = Math.round(montant2);
        int montant3F = Math.round(montant3);

        Assert.assertTrue("montant1F: " + montant1F + ", montant1:" + montant1, montant1F == 3);
        Assert.assertTrue(montant2F == 2);
        Assert.assertTrue(montant3F == 2);
    }

}
