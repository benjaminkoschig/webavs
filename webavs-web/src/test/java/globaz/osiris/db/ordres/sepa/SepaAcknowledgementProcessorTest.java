package globaz.osiris.db.ordres.sepa;

import java.io.File;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SepaAcknowledgementProcessorTest {
    private SepaAcknowledgementProcessor test;

    @Before
    public void setUp() {
        test = new SepaAcknowledgementProcessor();

        System.setProperty("globaz.path", new File("src/main/properties").getPath());
    }

    @Ignore("nothing works really well yet...")
    @Test
    public void testProcessAcknowledgementOk() {
        test.processAcknowledgement(
                getClass().getResourceAsStream("/globaz/osiris/db/ordres/sepa/pain_002_CT_Beispiel_OK.xml"));
    }

    @Ignore("nothing works really well yet...")
    @Test
    public void testProcessAcknowledgementNok() {
        test.processAcknowledgement(
                getClass().getResourceAsStream("/globaz/osiris/db/ordres/sepa/pain_002_CT_Beispiel_NOK.xml"));
    }

}
