package ch.globaz.al.test.businessimpl.service.models.dossier;

import static org.junit.Assert.*;
import globaz.jade.persistence.model.JadeAbstractModel;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl;

/**
 * @author jts
 * 
 */
public class DossierBusinessServiceImplTest {

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl#copierDossier(ch.globaz.al.business.models.dossier.DossierComplexModel)}
     * .
     */
    @Test
    @Ignore
    public void testCopierDossier() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl#createDossierEtEnvoyeAnnoncesRafam(ch.globaz.al.business.models.dossier.DossierComplexModel)}
     * .
     */
    @Test
    @Ignore
    public void testCreateDroitEtEnvoyeAnnoncesRafam() {
        Assert.fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl#getActivitesCategorieCotPers()}
     * .
     */
    @Test
    @Ignore
    public void testGetActivitesCategorieCotPers() {
        Assert.fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl#getActivitesToProcess(java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testGetActivitesToProcess() {
        Assert.fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl#getDossierComplexForDroit(ch.globaz.al.business.models.droit.DroitComplexModel)}
     * .
     */
    @Test
    @Ignore
    public void testGetDossierComplexForDroit() {
        Assert.fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl#getGenreAssurance(java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testGetGenreAssurance() {
        Assert.fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl#getIdDossiersActifs(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testGetIdDossiersActifs() {
        try {
            JadeAbstractModel[] idsDossier = ALImplServiceLocator.getDossierBusinessService().getIdDossiersActifs(
                    "756.0514.0076.41", "524.1001");

            assertEquals(2, idsDossier.length);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl#isAffilie(ch.globaz.al.business.models.dossier.DossierComplexModel, java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testIsAffilie() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl#isAgenceCommunale(java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testIsAgenceCommunale() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl#isAgricole(java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testIsAgricole() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl#isAllocataire(ch.globaz.al.business.models.dossier.DossierComplexModel, java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testIsAllocataire() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl#isModePaiementDirect(ch.globaz.al.business.models.dossier.DossierModel)}
     * .
     */
    @Test
    @Ignore
    public void testIsModePaiementDirect() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl#isParitaire(java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testIsParitaire() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl#radierDossier(ch.globaz.al.business.models.dossier.DossierModel)}
     * .
     */
    @Test
    @Ignore
    public void testRadierDossier() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierBusinessServiceImpl#retirerBeneficiaire(ch.globaz.al.business.models.dossier.DossierComplexModel, boolean)}
     * .
     */
    @Test
    @Ignore
    public void testRetirerBeneficiaire() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link DossierBusinessServiceImpl#updateDossierEtEnvoiAnnoncesRafam(ch.globaz.al.business.models.dossier.DossierComplexModel, String)}
     */
    @Test
    @Ignore
    public void testUpdateDossierEtEnvoyeAnnoncesRafam() {
        fail("Not yet implemented");
    }

}
