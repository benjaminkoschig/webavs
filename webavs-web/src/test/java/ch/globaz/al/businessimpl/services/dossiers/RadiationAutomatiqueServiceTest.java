package ch.globaz.al.businessimpl.services.dossiers;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.models.prestation.radiation.PrestationRadiationDossierComplexModel;
import ch.globaz.al.business.models.prestation.radiation.PrestationRadiationDossierComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.utils.ALTestCaseJU4;

public class RadiationAutomatiqueServiceTest extends ALTestCaseJU4 {

    @Ignore
    @Test
    public void testLoadLastPrestations() {

        try {
            PrestationRadiationDossierComplexSearchModel prestations = ALServiceLocator
                    .getRadiationAutomatiqueService().loadLastPrestations("12.2008");
            Assert.assertEquals(25, prestations.getSize());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            doFinally();
        }
    }

    /**
     * Cas d'une prestation pour un droit formation
     */
    @Ignore
    @Test
    public void testRadierDossier_DroitFormation() {
        try {
            RadiationAutomatiqueServiceImpl s = (RadiationAutomatiqueServiceImpl) ALServiceLocator
                    .getRadiationAutomatiqueService();
            PrestationRadiationDossierComplexSearchModel prestations = s.loadLastPrestations("09.2009", "33767");

            RadiationAutomatiqueResult res = ALServiceLocator.getRadiationAutomatiqueService().radierDossier(
                    (PrestationRadiationDossierComplexModel) prestations.getSearchResults()[0]);

            Assert.assertEquals("30.09.2009", res.getDossier().getDossierModel().getFinValidite());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            doFinally();
        }
    }

    /**
     * Cas d'une prestation pour un droit formation mais avec un droit enfant inactif depuis moins d'un an
     */
    @Ignore
    @Test
    public void testRadierDossier_DroitFormationEtPrestEnfantMoinsUnAn() {
        try {
            RadiationAutomatiqueServiceImpl s = (RadiationAutomatiqueServiceImpl) ALServiceLocator
                    .getRadiationAutomatiqueService();
            PrestationRadiationDossierComplexSearchModel prestations = s.loadLastPrestations("09.2009", "33766");

            RadiationAutomatiqueResult res = ALServiceLocator.getRadiationAutomatiqueService().radierDossier(
                    (PrestationRadiationDossierComplexModel) prestations.getSearchResults()[0]);

            Assert.assertEquals("", res.getDossier().getDossierModel().getFinValidite());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            doFinally();
        }
    }

    /**
     * Cas d'une prestation datant de plus d'un an
     */
    @Ignore
    @Test
    public void testRadierDossier_PrestDatantPlusUnAn() {
        try {

            RadiationAutomatiqueServiceImpl s = (RadiationAutomatiqueServiceImpl) ALServiceLocator
                    .getRadiationAutomatiqueService();
            PrestationRadiationDossierComplexSearchModel prestations = s.loadLastPrestations("01.2012", "6505");

            RadiationAutomatiqueResult res = ALServiceLocator.getRadiationAutomatiqueService().radierDossier(
                    (PrestationRadiationDossierComplexModel) prestations.getSearchResults()[0]);

            Assert.assertEquals("31.01.2012", res.getDossier().getDossierModel().getFinValidite());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            doFinally();
        }
    }
    // TODO Cas d'une prestation pour un enfant incapable d'exercer

    // TODO Cas ne devant pas être radier (prestation pour un droit FORM > 25 ans mais avec d'autres droit actif
    // idDossier 12974
}
