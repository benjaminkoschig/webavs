package globaz.helios.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HeliosTest {
    private static Pattern DIGIT_AND_SIZE_PATTERN = Pattern.compile("([0-9]){4,4}.([0-9]){4,4}.([0-9]){4,4}");

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void checkPatternFormatNumeroCompteAlphanumeriqueErreurTest() {
        String compte = "1111.1111.11A1";
        Matcher mat = HeliosTest.DIGIT_AND_SIZE_PATTERN.matcher(compte);
        Assert.assertFalse(mat.matches());
    }

    @Test
    public void checkPatternFormatNumeroCompteCorrectTest() {
        String compte = "1111.1111.1111";
        Matcher mat = HeliosTest.DIGIT_AND_SIZE_PATTERN.matcher(compte);
        Assert.assertTrue(mat.matches());
    }

    @Test
    public void checkPatternFormatNumeroCompteTropCourtErreurTest() {
        String compte = "1111.1111.11111";
        Matcher mat = HeliosTest.DIGIT_AND_SIZE_PATTERN.matcher(compte);
        Assert.assertFalse(mat.matches());
    }

    @Test
    public void checkPatternFormatNumeroCompteTropLongErreurTest() {
        String compte = "1111.1111.11111";
        Matcher mat = HeliosTest.DIGIT_AND_SIZE_PATTERN.matcher(compte);
        Assert.assertFalse(mat.matches());
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

}
