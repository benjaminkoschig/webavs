package ch.globaz.al.businessimpl.services.dossiers;

import java.util.HashSet;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.models.dossier.DossierLieComplexModel;
import ch.globaz.al.business.models.dossier.DossierLieComplexSearchModel;
import ch.globaz.al.business.models.dossier.LienDossierModel;
import ch.globaz.al.business.models.dossier.LienDossierSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALTestCaseJU4;

public class DossierBusinessServiceImplTest extends ALTestCaseJU4 {

    @Ignore
    @Test
    public void testAjouterLien() {
        // new 25178 et 21381

        try {
            ALServiceLocator.getDossierBusinessService().ajouterLien("25178", "21381", "61120001");

            LienDossierSearchModel search = new LienDossierSearchModel();
            search.setForIdDossierPere("25178");
            search.setForIdDossierFils("21381");
            search = ALImplServiceLocator.getLienDossierModelService().search(search);

            Assert.assertEquals(1, search.getSize());

            // on vérifie que le service a bien ajouter un 2e lien, l'inverse
            LienDossierModel lienInverse = ALServiceLocator.getDossierBusinessService().getLienInverse(
                    (LienDossierModel) search.getSearchResults()[0]);
            Assert.assertNotNull(lienInverse);
            Assert.assertEquals("61120001", lienInverse.getTypeLien());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            doFinally();
        }

    }

    @Ignore
    @Test
    public void testChangerTypeLien() {
        // existant 28319 et 97 lien alloc
        try {

            ALServiceLocator.getDossierBusinessService().changerTypeLien("1", "61120002");

            LienDossierModel lienpere = ALImplServiceLocator.getLienDossierModelService().read("1");
            LienDossierModel lieninverse = ALImplServiceLocator.getLienDossierModelService().read("2");

            // on vérifie que les 2 liens ont bien changé de type

            Assert.assertEquals("61120002", lienpere.getTypeLien());
            Assert.assertEquals("61120002", lieninverse.getTypeLien());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            doFinally();
        }

    }

    @Ignore
    @Test
    public void testCopierDossier() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testCreateDossierEtEnvoyeAnnoncesRafam() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testDeleteDossierEtEnvoyeAnnoncesRafam() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testGetActivitesCategorieCotPers() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testGetActivitesToProcess() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testGetDossierComplexForDroit() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testGetDossiersLies() {
        try {

            // existant 28319 et 97 lien alloc
            DossierLieComplexSearchModel searchLies = ALImplServiceLocator.getDossierBusinessService().getDossiersLies(
                    "28319");

            Assert.assertEquals(1, searchLies.getSize());
            Assert.assertEquals("97", ((DossierLieComplexModel) searchLies.getSearchResults()[0]).getIdDossierLie());

            searchLies = ALImplServiceLocator.getDossierBusinessService().getDossiersLies("97");

            Assert.assertEquals(1, searchLies.getSize());
            Assert.assertEquals("28319", ((DossierLieComplexModel) searchLies.getSearchResults()[0]).getIdDossierLie());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            doFinally();
        }
    }

    @Ignore
    @Test
    public void testGetGenreAssurance() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testGetIdDossiersActifs() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testGetLienInverse() {
        try {

            LienDossierModel lienOriginal = ALImplServiceLocator.getLienDossierModelService().read("1");
            LienDossierModel lienInverse = ALServiceLocator.getDossierBusinessService().getLienInverse(lienOriginal);

            LienDossierModel lienInverseInverse = ALServiceLocator.getDossierBusinessService().getLienInverse(
                    lienInverse);

            Assert.assertEquals("2", lienInverse.getIdLien());
            Assert.assertEquals("1", lienInverseInverse.getIdLien());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            doFinally();
        }
    }

    @Ignore
    @Test
    public void testGetResultsAffilieAllocDossiers() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testGetTypeCotisation() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testHasSentAnnonces() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testIsAffilie() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testIsAgenceCommunale() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testIsAgricole() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testIsAllocataire() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testIsModePaiementDirect() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testIsParitaire() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testListerEtatsOkGenerationGlobale() {

        try {
            HashSet<String> setEtats = (HashSet<String>) ALServiceLocator.getDossierBusinessService()
                    .listerEtatsOkGenerationGlobale();

            Assert.assertEquals(1, setEtats.size());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            doFinally();
        }

    }

    @Ignore
    @Test
    public void testListerEtatsOkJournalise() {

        try {
            HashSet<String> setEtats = (HashSet<String>) ALServiceLocator.getDossierBusinessService()
                    .listerEtatsOkJournalise();

            Assert.assertEquals(6, setEtats.size());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            doFinally();
        }

    }

    @Ignore
    @Test
    public void testListerEtatsOkRafam() {

        try {
            HashSet<String> setEtats = (HashSet<String>) ALServiceLocator.getDossierBusinessService()
                    .listerEtatsOkRafam();

            Assert.assertEquals(2, setEtats.size());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            doFinally();
        }

    }

    @Ignore
    @Test
    public void testNbreJourDebutMoisAF() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testNbreJourFinMoisAF() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testRadierDossier() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testRadierDossierHorloger() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testReadWidget_nbreJourDebutMoisAF() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testReadWidget_nbreJourFinMoisAF() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testRetirerBeneficiaire() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testRetirerLien() {
        // existant 28319 et 97 lien alloc
        try {

            ALServiceLocator.getDossierBusinessService().retirerLien("1");

            LienDossierModel lienpere = ALImplServiceLocator.getLienDossierModelService().read("1");
            LienDossierModel lieninverse = ALImplServiceLocator.getLienDossierModelService().read("2");

            // on vérifie que les 2 liens ont bien été supprimés
            Assert.assertTrue(lienpere.isNew());
            Assert.assertTrue(lieninverse.isNew());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            doFinally();
        }
    }

    @Ignore
    @Test
    public void testSearchDossierAttestation() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testUpdateDossierEtEnvoyeAnnoncesRafam() {
        Assert.fail("Not yet implemented");
    }

}
