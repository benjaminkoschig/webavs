package globaz.osiris.utils;

import java.math.BigDecimal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CAOsirisUtilsTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void montantDiffPlus5avecMontantNeg10CHFTest() {
        Assert.assertFalse(CAUtil.montantDiffPlusOuMoins5(new BigDecimal("-10")));
    }

    @Test
    public void montantDiffPlus5avecMontantNeg3CHFTest() {
        Assert.assertTrue(CAUtil.montantDiffPlusOuMoins5(new BigDecimal("-3")));
    }

    @Test
    public void montantDiffPlus5avecMontantNeg4CHF99Test() {
        Assert.assertTrue(CAUtil.montantDiffPlusOuMoins5(new BigDecimal("-4.99")));
    }

    @Test
    public void montantDiffPlus5avecMontantNeg5CHFTest() {
        Assert.assertFalse(CAUtil.montantDiffPlusOuMoins5(new BigDecimal("-5")));
    }

    @Test
    public void montantDiffPlus5avecMontantPos10CHFTest() {
        Assert.assertFalse(CAUtil.montantDiffPlusOuMoins5(new BigDecimal("10")));
    }

    @Test
    public void montantDiffPlus5avecMontantPos3CHFTest() {
        Assert.assertTrue(CAUtil.montantDiffPlusOuMoins5(new BigDecimal("3")));
    }

    @Test
    public void montantDiffPlus5avecMontantPos4CHF99Test() {
        Assert.assertTrue(CAUtil.montantDiffPlusOuMoins5(new BigDecimal("4.99")));
    }

    @Test
    public void montantDiffPlus5avecMontantPos5CHFTest() {
        Assert.assertFalse(CAUtil.montantDiffPlusOuMoins5(new BigDecimal("5")));
    }

    @Test
    public void montantDiffPlus5avecMontantVideTest() {
        try {
            CAUtil.montantDiffPlusOuMoins5(new BigDecimal(""));
            Assert.assertTrue(false);
        } catch (Exception e) {
            Assert.assertTrue(e.getClass().equals(NumberFormatException.class));
        }

    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

}
