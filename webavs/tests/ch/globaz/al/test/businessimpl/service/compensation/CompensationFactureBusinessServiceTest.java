package ch.globaz.al.test.businessimpl.service.compensation;

import static org.junit.Assert.*;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Test des services de compensation
 * 
 * @author jts
 * 
 */
public class CompensationFactureBusinessServiceTest {

    /**
     * Constructeur
     * 
     * @param name
     *            Nom de la classe
     */

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.compensation.CompensationFactureBusinessServiceImpl#restoreEtatPrestations(java.lang.String)}
     * .
     */
    @Ignore
    @Test
    public void testRestoreEtatPrestations() {
        try {
            // TODO : à terminer
            String idPassage = "1";

            ALServiceLocator.getCompensationFactureBusinessService().restoreEtatPrestations("1");

            EntetePrestationSearchModel search = new EntetePrestationSearchModel();
            search.setForIdPassage(idPassage);
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            search = ALImplServiceLocator.getEntetePrestationModelService().search(search);

            assertEquals(0, search.getSearchResults().length);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
