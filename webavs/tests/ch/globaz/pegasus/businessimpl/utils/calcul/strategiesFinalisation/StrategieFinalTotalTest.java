package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.junit.Test;

public class StrategieFinalTotalTest {

    class FloatBigDecimalCorrespondance {

        private BigDecimal bdValue;
        private float floatValue;

        public FloatBigDecimalCorrespondance(float floatValue, BigDecimal bdValue) {
            this.bdValue = bdValue;
            this.floatValue = floatValue;
        }
    }

    @Test
    public void testBigDecimalArrondi() {

        MathContext ctx = new MathContext(2, RoundingMode.CEILING);

        FloatBigDecimalCorrespondance c1 = new FloatBigDecimalCorrespondance(10.01f, new BigDecimal(10.01));

        System.out.println(new BigDecimal(10.01));
        System.out.println(new BigDecimal(10.01, ctx));
        System.out.println(new BigDecimal(10.001));
        System.out.println(new BigDecimal(10.001, ctx));
        System.out.println(new BigDecimal(10.001).setScale(2, RoundingMode.CEILING));
        System.out.println(new BigDecimal(10.001).setScale(0, RoundingMode.CEILING));

        System.out.println(new BigDecimal(10.001).setScale(2, BigDecimal.ROUND_CEILING).setScale(2,
                BigDecimal.ROUND_CEILING));
        System.out.println(new BigDecimal(10.011).setScale(2, BigDecimal.ROUND_DOWN));
        System.out.println(new BigDecimal(10.001).setScale(2, BigDecimal.ROUND_DOWN).setScale(0,
                BigDecimal.ROUND_CEILING));

        System.out.println(new BigDecimal(10.011).setScale(2, BigDecimal.ROUND_DOWN).setScale(0,
                BigDecimal.ROUND_CEILING));
        System.out.println(new BigDecimal(10.99).setScale(2, BigDecimal.ROUND_DOWN).setScale(0,
                BigDecimal.ROUND_CEILING));

    }

    @Test
    public void testCeilBigDecimalwithFoat() {
        float val = 10.0121221F;
        float val2 = 10.01f;
        float val3 = 10f;

        BigDecimal bd1 = new BigDecimal(val);
        BigDecimal bd2 = new BigDecimal(val2);
        BigDecimal bd3 = new BigDecimal(val3);

        System.out.println("*********** Simple float conversion");
        System.out.println("float[10.0121221]-->BigDecimal: " + bd1);
        System.out.println("float[10.01]-->BigDecimal: " + bd2);
        System.out.println("float[10]-->BigDecimal: " + bd3);

        System.out.println("*********** Simple float conversion, BigDecimal fLoatValue()");
        System.out.println("float[10.0121221]-->BigDecimal: " + bd1.floatValue());
        System.out.println("float[10.01]-->BigDecimal: " + bd2.floatValue());
        System.out.println("float[10]-->BigDecimal: " + bd3.floatValue());

    }

    public void testCeilFunction() {

        double[] valeurs = { 2.56, 3.0001, 4.001, 5.99, 6.00 };
        double[] ceilAttendu = { 3.0, 4.0, 5.0, 6.0, 6.0 };

        for (int cpt = 0; cpt < valeurs.length; cpt++) {
            double ceilValue = Math.ceil(valeurs[cpt]);
            System.out.println(ceilValue);
            assertTrue("value:" + valeurs[cpt] + ", ceil:" + ceilValue, ceilValue == ceilAttendu[cpt]);
        }

    }

    @Test
    public void testDDDObject() {
        System.out.println("******* Custom object as float");
        System.out.println(new MontantCalcul(10.0001f).getMontant());
        System.out.println(new MontantCalcul(10.0101f).getMontant());
        System.out.println(new MontantCalcul(10.2001f).getMontant());
        System.out.println(new MontantCalcul(10.51f).getMontant());
        System.out.println(new MontantCalcul(10.000f).getMontant());

        System.out.println("******* Custom object as String");
        System.out.println(new MontantCalcul("10.0001").getMontant());
        System.out.println(new MontantCalcul("10.0101").getMontant());
        System.out.println(new MontantCalcul("10.2001").getMontant());
        System.out.println(new MontantCalcul("10.51").getMontant());
        System.out.println(new MontantCalcul("10.000").getMontant());

        String g = null;

        if ("true".equals(g)) {

        } else {
            System.out.println("tu vois");
        }
    }

    @Test
    public void testDivide() {
        System.out.println("******* Custom divide");
        System.out.println(new MontantCalcul(1200.001f).getMontant().divide(new MontantCalcul(12f).getMontant(), 2,
                BigDecimal.ROUND_CEILING));
    }
}
