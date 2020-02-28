package ch.globaz.al.businessimpl.services.calcul;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.utils.ALTestCaseJU4;

/**
 * @author jts
 * 
 */
@RunWith(Parameterized.class)
public class CalculHorlogerTest extends ALTestCaseJU4 {

    @Parameters
    public static List<Object[]> getParametres() {
        return Arrays
                .asList(new Object[][] { { "01.03.2025", "32884", 1, "30.00" }, { "01.04.2025", "32884", 1, "0" } });
    }

    private String pDateCalcul;
    private String pIdDossier;
    private int pIndexRes;
    private String resultatAttendu;

    public CalculHorlogerTest(String pDateCalcul, String pIdDossier, int pIndexRes, String resultatAttendu) {
        this.pDateCalcul = pDateCalcul;
        this.pIdDossier = pIdDossier;
        this.pIndexRes = pIndexRes;
        this.resultatAttendu = resultatAttendu;
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.calcul.CalculServiceImpl#getCalcul(ch.globaz.al.business.models.dossier.DossierComplexModelRoot, java.lang.String, ch.globaz.naos.business.data.AssuranceInfo)}
     * .
     */
    @Ignore
    @Test
    public void testGetCalcul() {

        try {
            updateParamater("TUCANA_IS_ENABLED", "1", "01.01.2009");

            DossierComplexModel dossier = ALServiceLocator.getDossierComplexModelService().read(pIdDossier);
            List<CalculBusinessModel> resCalc = ALServiceLocator.getCalculBusinessService().getCalcul(dossier,
                    pDateCalcul);
            Assert.assertEquals(resultatAttendu, resCalc.get(pIndexRes).getCalculResultMontantBase());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            doFinally();
        }
    }

}
