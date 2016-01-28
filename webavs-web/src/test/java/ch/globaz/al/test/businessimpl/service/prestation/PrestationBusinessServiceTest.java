package ch.globaz.al.test.businessimpl.service.prestation;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.DetailPrestationSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Test sur les méthodes liées à PrestationBusinessService
 * 
 * @author PTA
 * 
 */
public class PrestationBusinessServiceTest {
    /**
     * identifiant dû détail de la prestation
     * 
     */
    String idDetail = "76";
    /**
     * identifiant du droit
     */
    String idDroit = "13";

    /**
     * Constructeur de la classe
     * 
     * @param name
     */

    /**
     * test sur la méthode
     * 
     * @see ch.globaz.al.businessimpl.services.models.prestation.PrestationBusinessServiceImpl#getAgeEnfantDetailPrestation(DroitComplexModel,
     *      DetailPrestationModel)
     */
    @Ignore
    @Test
    public void testAgeEnfantDetailPrestation() {
        try {

            DetailPrestationModel det = new DetailPrestationModel();
            DroitComplexModel droit = new DroitComplexModel();
            DroitComplexSearchModel droitSearch = new DroitComplexSearchModel();
            DetailPrestationSearchModel detSearch = new DetailPrestationSearchModel();
            droitSearch.setForIdDroit(idDroit);
            droitSearch = ALServiceLocator.getDroitComplexModelService().search(droitSearch);

            droit = (DroitComplexModel) droitSearch.getSearchResults()[0];
            detSearch.setForIdDetailPrestation(idDetail);
            detSearch = ALImplServiceLocator.getDetailPrestationModelService().search(detSearch);
            det = (DetailPrestationModel) detSearch.getSearchResults()[0];

            // recherche del'age de l'enfant à la date du détail de la
            // prestation
            String ageEnfant = ALServiceLocator.getPrestationBusinessService().getAgeEnfantDetailPrestation(droit, det);

            assertEquals("11", ageEnfant);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}
