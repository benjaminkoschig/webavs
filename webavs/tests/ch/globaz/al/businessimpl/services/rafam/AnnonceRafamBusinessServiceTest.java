package ch.globaz.al.businessimpl.services.rafam;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * @author jts
 * 
 */
public class AnnonceRafamBusinessServiceTest {

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.rafam.AnnonceRafamBusinessServiceImpl#canValidate(ch.globaz.al.business.models.rafam.AnnonceRafamModel)}
     * .
     */
    @Ignore
    @Test
    public void testCanValidate() {
        try {

            AnnonceRafamModel annonce = new AnnonceRafamModel();

            // cas d'une annonce validée
            annonce.setEtat(RafamEtatAnnonce.VALIDE.getCS());
            Assert.assertFalse(ALImplServiceLocator.getAnnonceRafamBusinessService().canValidate(annonce));

            // cas d'une annonce UPI
            annonce.setEtat(RafamEtatAnnonce.RECU.getCS());
            annonce.setTypeAnnonce(RafamTypeAnnonce._69B_SYNCHRO_UPI.getCode());
            Assert.assertTrue(ALImplServiceLocator.getAnnonceRafamBusinessService().canValidate(annonce));

            // TODO cas d'une erreur 212 + autres cas

            // annonce.setEtat(RafamEtatAnnonce.RECU.getCS());
            // annonce.setTypeAnnonce(RafamTypeAnnonce._68A_CREATION.getCode());
            // Assert.assertFalse(ALImplServiceLocator.getAnnonceRafamBusinessService().canValidate(annonce));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }
}
