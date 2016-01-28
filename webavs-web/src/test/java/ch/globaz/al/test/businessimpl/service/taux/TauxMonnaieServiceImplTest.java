package ch.globaz.al.test.businessimpl.service.taux;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.services.ALServiceLocator;

public class TauxMonnaieServiceImplTest {

    @Ignore
    @Test
    public void testTauxForPeriode() {
        try {
            String taux1 = ALServiceLocator.getCalculAdiBusinessService().getTauxAFForPeriode(ALCSTiers.MONNAIE_EURO,
                    "01.2011");
            String taux2 = ALServiceLocator.getCalculAdiBusinessService().getTauxAFForPeriode(ALCSTiers.MONNAIE_EURO,
                    "02.2011");
            String taux3 = ALServiceLocator.getCalculAdiBusinessService().getTauxAFForPeriode(ALCSTiers.MONNAIE_EURO,
                    "03.2011");
            String taux4 = ALServiceLocator.getCalculAdiBusinessService().getTauxAFForPeriode(ALCSTiers.MONNAIE_EURO,
                    "04.2011");
            String taux5 = ALServiceLocator.getCalculAdiBusinessService().getTauxAFForPeriode(ALCSTiers.MONNAIE_EURO,
                    "05.2011");
            String taux6 = ALServiceLocator.getCalculAdiBusinessService().getTauxAFForPeriode(ALCSTiers.MONNAIE_EURO,
                    "06.2011");
            String taux7 = ALServiceLocator.getCalculAdiBusinessService().getTauxAFForPeriode(ALCSTiers.MONNAIE_EURO,
                    "07.2011");
            String taux8 = ALServiceLocator.getCalculAdiBusinessService().getTauxAFForPeriode(ALCSTiers.MONNAIE_EURO,
                    "08.2011");
            String taux9 = ALServiceLocator.getCalculAdiBusinessService().getTauxAFForPeriode(ALCSTiers.MONNAIE_EURO,
                    "09.2011");
            String taux10 = ALServiceLocator.getCalculAdiBusinessService().getTauxAFForPeriode(ALCSTiers.MONNAIE_EURO,
                    "10.2011");
            String taux11 = ALServiceLocator.getCalculAdiBusinessService().getTauxAFForPeriode(ALCSTiers.MONNAIE_EURO,
                    "11.2011");
            String taux12 = ALServiceLocator.getCalculAdiBusinessService().getTauxAFForPeriode(ALCSTiers.MONNAIE_EURO,
                    "12.2011");

            assertEquals("1.345240", taux1);
            assertEquals("1.345240", taux2);
            assertEquals("1.345240", taux3);
            assertEquals("1.277940", taux4);
            assertEquals("1.277940", taux5);
            assertEquals("1.277940", taux6);
            assertEquals("1.297740", taux7);
            assertEquals("1.297740", taux8);
            assertEquals("1.297740", taux9);
            assertEquals("1.176590", taux10);
            assertEquals("1.176590", taux11);
            assertEquals("1.176590", taux12);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}
