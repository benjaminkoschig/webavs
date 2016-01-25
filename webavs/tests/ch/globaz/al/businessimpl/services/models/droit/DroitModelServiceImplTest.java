package ch.globaz.al.businessimpl.services.models.droit;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALTestCaseJU4;

/**
 * @author jts
 * 
 */
public class DroitModelServiceImplTest extends ALTestCaseJU4 {

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.droit.DroitModelServiceImpl#clone(ch.globaz.al.business.models.droit.DroitModel, java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testCloneDroitModelString() {
        Assert.fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.droit.DroitModelServiceImpl#copie(ch.globaz.al.business.models.droit.DroitModel)}
     * .
     */
    @Ignore
    @Test
    public void testCopie() {

        try {

            // Liste de méthodes à ignorer
            ArrayList<String> ignore = new ArrayList<String>();
            ignore.add("getId");
            ignore.add("getIdDroit");

            // préparation de l'instance de référence puis appel du service de copie.
            DroitModel model = (DroitModel) initTestModel("ch.globaz.al.business.models.droit.DroitModel");
            DroitModel copie = ALImplServiceLocator.getDroitModelService().copie(model);

            // comparaison des deux instances
            checkModelCopie(ignore, model, copie);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            doFinally();
        }

    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.droit.DroitModelServiceImpl#count(ch.globaz.al.business.models.droit.DroitSearchModel)}
     * .
     */
    @Test
    @Ignore
    public void testCount() {
        Assert.fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.droit.DroitModelServiceImpl#create(ch.globaz.al.business.models.droit.DroitModel)}
     * .
     */
    @Test
    @Ignore
    public void testCreate() {
        Assert.fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.droit.DroitModelServiceImpl#delete(ch.globaz.al.business.models.droit.DroitModel)}
     * .
     */
    @Test
    @Ignore
    public void testDelete() {
        Assert.fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.droit.DroitModelServiceImpl#initModel(ch.globaz.al.business.models.droit.DroitModel)}
     * .
     */
    @Test
    @Ignore
    public void testInitModel() {
        Assert.fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.droit.DroitModelServiceImpl#read(java.lang.String)}.
     */
    @Test
    @Ignore
    public void testRead() {
        Assert.fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.droit.DroitModelServiceImpl#search(ch.globaz.al.business.models.droit.DroitSearchModel)}
     * .
     */
    @Test
    @Ignore
    public void testSearch() {
        Assert.fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.droit.DroitModelServiceImpl#update(ch.globaz.al.business.models.droit.DroitModel)}
     * .
     */
    @Test
    @Ignore
    public void testUpdate() {
        Assert.fail("Not yet implemented");
    }

}
