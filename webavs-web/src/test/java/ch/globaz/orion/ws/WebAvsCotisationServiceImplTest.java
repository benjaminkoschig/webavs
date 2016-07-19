package ch.globaz.orion.ws;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.orion.ws.cotisation.WebAvsCotisationsServiceImpl;

public class WebAvsCotisationServiceImplTest {

    @Test
    public void convertirAnnuelToMensuelA5CentimesTest() {

        // Test si 14.64 => donne 1.22 soit donner 1.20
        BigDecimal testMontant = new BigDecimal(14.64);
        BigDecimal testResultat = new BigDecimal(1.20);
        assertTrue(WebAvsCotisationsServiceImpl.convertirAnnuelToMensuelA5Centimes(testMontant).doubleValue() == testResultat
                .doubleValue());

        // Test si 1.26 doit donner 1.25
        testMontant = new BigDecimal(15.12);
        testResultat = new BigDecimal(1.25);
        assertTrue(WebAvsCotisationsServiceImpl.convertirAnnuelToMensuelA5Centimes(testMontant).doubleValue() == testResultat
                .doubleValue());

        // Test si 1.24 doit donner 1.25
        testMontant = new BigDecimal(14.88);
        testResultat = new BigDecimal(1.25);
        assertTrue(WebAvsCotisationsServiceImpl.convertirAnnuelToMensuelA5Centimes(testMontant).doubleValue() == testResultat
                .doubleValue());

        // Test si 1.28 doit donner 1.30
        testMontant = new BigDecimal(15.36);
        testResultat = new BigDecimal(1.30);
        assertTrue(WebAvsCotisationsServiceImpl.convertirAnnuelToMensuelA5Centimes(testMontant).doubleValue() == testResultat
                .doubleValue());

        try {
            WebAvsCotisationsServiceImpl.convertirAnnuelToMensuelA5Centimes(null);
            Assert.fail("Ne prend pas en compte un montant null");
        } catch (Exception e) {
        }

    }
}
