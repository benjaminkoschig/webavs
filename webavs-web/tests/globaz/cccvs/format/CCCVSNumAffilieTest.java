package globaz.cccvs.format;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class CCCVSNumAffilieTest {

    @Test
    /**
     * On test le cas qui ne doivent pas passer sur la méthode CHECK.
     * @throws Exception
     */
    public void testCheckErrorsAVS() throws Exception {
        CCCVSNumAffilie ccvs = new CCCVSNumAffilie();
        List<String> listErrors = new ArrayList<String>();

        listErrors.add("123.343.1234567");
        listErrors.add("1233.343.123457");
        listErrors.add("123.3436.123457");
        listErrors.add("1233436123457");
        listErrors.add(null);

        for (int i = 0; i < listErrors.size(); i++) {
            try {
                ccvs.check(listErrors.get(i));
                Assert.fail();
            } catch (Exception e) {

            }
        }
    }

    @Test
    /**
     * On test les cas qui doivent passer sur la méthode CHECK.
     * @throws Exception
     */
    public void testCheckValidesAVS() throws Exception {
        CCCVSNumAffilie ccvs = new CCCVSNumAffilie();
        List<String> listValides = new ArrayList<String>();

        listValides.add("100.002.123456");
        listValides.add("100.002.12345");
        listValides.add("100.002.123");
        listValides.add("100.002.12");
        listValides.add("100.002.1");
        listValides.add("100.002");
        listValides.add("100.00");
        listValides.add("100.0");
        listValides.add("100");
        listValides.add("10");
        listValides.add("1");
        listValides.add("");

        for (int i = 0; i < listValides.size(); i++) {
            try {
                ccvs.check(listValides.get(i));
            } catch (Exception e) {
                Assert.fail();
            }
        }
    }

    @Test
    /**
     * On test les cas en sachant ce que l'on attend.
     */
    public void testFormatAVS() {
        CCCVSNumAffilie ccvs = new CCCVSNumAffilie();

        // Il doit être tronquer à 12 chiffres
        Assert.assertEquals("100.002.123456", ccvs.format("100.002.1234567"));

        // Cas normal
        Assert.assertEquals("100.002.123456", ccvs.format("100.002.123456"));
        Assert.assertEquals("100.002.123456", ccvs.format("10.0002123.456"));
        Assert.assertEquals("100.002.12345", ccvs.format("10000212345"));
        Assert.assertEquals("456.789", ccvs.format("456789"));
        Assert.assertEquals("456.78", ccvs.format("45678"));
        Assert.assertEquals("456", ccvs.format("456"));

        // Retourne vide
        Assert.assertEquals("", ccvs.format(""));
        // Retourne null
        Assert.assertNull(ccvs.format(null));
    }
}
