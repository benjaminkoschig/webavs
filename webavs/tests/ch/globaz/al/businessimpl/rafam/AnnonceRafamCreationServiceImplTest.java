package ch.globaz.al.businessimpl.rafam;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.utils.ALTestCaseJU4;

public class AnnonceRafamCreationServiceImplTest extends ALTestCaseJU4 {

    @Test
    @Ignore
    public void testCreate68cForAnnonce() {
        Assert.fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testCreerAnnonceModificationsTiers() {
        Assert.fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testCreerAnnoncesRafamEvDeclencheurDossierComplexModel() {
        Assert.fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testCreerAnnoncesRafamEvDeclencheurDossierComplexModelDroitComplexModel() {
        Assert.fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testCreerAnnoncesRafamEvDeclencheurDroitComplexModel() {
        Assert.fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testCreerAnnoncesRafamEvDeclencheurRafamEtatAnnonceDossierComplexModelDroitComplexModel() {
        Assert.fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testCreerAnnoncesRafamEvDeclencheurRafamEtatAnnonceDroitComplexModel() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testSuspendreAnnonce() {
        try {
            AnnonceRafamModel annonce = ALServiceLocator.getAnnonceRafamModelService().read("34601");
            annonce = ALServiceLocator.getAnnonceRafamCreationService().suspendreAnnonce(annonce);
            Assert.assertEquals(RafamEtatAnnonce.SUSPENDU.getCS(), annonce.getEtat());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            doFinally();
        }
    }

    @Ignore
    @Test
    public void testTransmettreAnnoncesTemporaires() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testValiderAnnonce() {
        try {
            AnnonceRafamModel annonce = ALServiceLocator.getAnnonceRafamModelService().read("34601");
            annonce = ALServiceLocator.getAnnonceRafamCreationService().validerAnnonce(annonce);
            Assert.assertEquals(RafamEtatAnnonce.VALIDE.getCS(), annonce.getEtat());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            doFinally();
        }
    }

}
