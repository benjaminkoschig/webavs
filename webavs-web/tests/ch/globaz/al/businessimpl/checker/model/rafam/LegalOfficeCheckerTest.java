package ch.globaz.al.businessimpl.checker.model.rafam;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LegalOfficeCheckerTest {

    @Parameters
    public static List<Object[]> getParametres() {
        return Arrays.asList(new Object[][] { { "14", false }, { "602.201", true }, { "602.202", true },
                { "602.203", true }, { "602.205", true }, { "602.206", true }, { "602.207", true },
                { "602.208", true }, { "602.209", true }, { "602.210", true }, { "602.211", true },
                { "602.212", true }, { "602.213", true }, { "602.214", true }, { "602.216", true },
                { "602.217", true }, { "ALK.123", true }, { "ABC.123", false } });
    }

    private String pLegalOffice;
    private boolean pExpectedResult;

    public LegalOfficeCheckerTest(String pLegalOffice, boolean pExpectedResult) {
        this.pLegalOffice = pLegalOffice;
        this.pExpectedResult = pExpectedResult;
    }

    @Test
    public void testIsValid() {
        boolean result = LegalOfficeChecker.isValid(pLegalOffice);
        Assert.assertTrue(result == pExpectedResult);
    }

}
