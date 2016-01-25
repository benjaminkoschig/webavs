package ch.globaz.al.test.businessimpl.service.prestation;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Test sur les méthodes liées aux récapitulatifs
 * 
 * @author PTA
 * 
 */
public class RecapitulatifEntrepriseImpressionComplexModelServiceTest {

    private String idRecap = "182920";
    private String idRecapBis = "183533";

    private String periode = "12.2009";

    /**
     * constructeur
     * 
     * @param name
     */

    /**
     * test sur le montant total des détails d'une récap
     */
    @Ignore
    @Test
    public void testMontantTotal() {

        try {

            RecapitulatifEntrepriseImpressionComplexSearchModel recap = new RecapitulatifEntrepriseImpressionComplexSearchModel();
            // recap.setForidRecap("182920");
            recap.setForPeriodeDe(periode);
            recap.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            recap = ALImplServiceLocator.getRecapitulatifEntrepriseImpressionComplexModelService().search(recap);

            ArrayList recapi = new ArrayList();

            for (int i = 0; i < recap.getSize(); i++) {
                RecapitulatifEntrepriseImpressionComplexModel recapModel = (RecapitulatifEntrepriseImpressionComplexModel) recap
                        .getSearchResults()[i];
                recapi.add(recapModel);
            }

            String montantTotal = ALServiceLocator.getRecapitulatifEntrepriseBusinessService()
                    .calculMontantPourUneRecapEntreprise(idRecap, recapi);
            assertEquals("250.00", montantTotal);
            JadeThread.logClear();

            montantTotal = ALServiceLocator.getRecapitulatifEntrepriseBusinessService()
                    .calculMontantPourUneRecapEntreprise(idRecapBis, recapi);
            assertEquals("456160.00", montantTotal);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }

    }

}
