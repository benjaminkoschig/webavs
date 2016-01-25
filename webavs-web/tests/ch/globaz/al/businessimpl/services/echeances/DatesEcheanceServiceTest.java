package ch.globaz.al.businessimpl.services.echeances;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.utils.ALTestCaseJU4;

/**
 * @author jts
 * 
 */
public class DatesEcheanceServiceTest extends ALTestCaseJU4 {

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.echeances.DatesEcheanceServiceImpl#getDateDebutValiditeDroit(ch.globaz.al.business.models.droit.DroitComplexModel, java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testGetDateDebutValiditeDroit() {
        Assert.fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.echeances.DatesEcheanceServiceImpl#getDateFinValiditeDroitCalculee(ch.globaz.al.business.models.droit.DroitComplexModel)}
     * .
     */
    @Ignore
    @Test
    public void testGetDateFinValiditeDroitCalculee() {
        try {
            DroitComplexModel droit = ALServiceLocator.getDroitComplexModelService().read("59506");

            Assert.assertEquals("31.03.2016", ALServiceLocator.getDatesEcheanceService()
                    .getDateFinValiditeDroitCalculee(droit));

            droit.getEnfantComplexModel().getEnfantModel().setCapableExercer(new Boolean(false));
            Assert.assertEquals("31.03.2020", ALServiceLocator.getDatesEcheanceService()
                    .getDateFinValiditeDroitCalculee(droit));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            doFinally();
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.echeances.DatesEcheanceServiceImpl#getDateFinValiditeDroitCalculeeFormAnticipe(ch.globaz.al.business.models.droit.DroitComplexModel)}
     * .
     */

    @Ignore
    @Test
    public void testGetDateFinValiditeDroitCalculeeFormAnticipe() {
        Assert.fail("Not yet implemented");
    }

}
