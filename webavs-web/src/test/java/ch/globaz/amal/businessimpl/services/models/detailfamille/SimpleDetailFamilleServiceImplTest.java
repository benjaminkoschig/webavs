/**
 *
 */
package ch.globaz.amal.businessimpl.services.models.detailfamille;

import org.junit.Assert;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;

/**
 * @author DHI
 *
 */
public class SimpleDetailFamilleServiceImplTest /* extends AMTestCase */ {

    /**
     * @param name
     */
    public SimpleDetailFamilleServiceImplTest(String name) {
        // super(name);
    }

    /**
     * Test de création d'un detailFamille
     *
     */
    public void testCreate() {
        try {
            // Création d'un subside avec des paramètres erronés dans le modèle
            // Attente de résultat >> JadeThread.hasMessage à true
            SimpleDetailFamille currentDetailFamille = new SimpleDetailFamille();
            currentDetailFamille.setAnneeHistorique("-1");
            try {
                currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService()
                        .create(currentDetailFamille);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la création d'un détail famille avec paramètres erronés "
                        + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Création d'une annonce avec des paramètres corrects
            // Attente de résultat >> modèle renseigné avec isNew à false
            currentDetailFamille = new SimpleDetailFamille();
            currentDetailFamille.setAnneeHistorique("2011");
            currentDetailFamille.setDebutDroit("02.2011");
            currentDetailFamille.setIdFamille("1");
            currentDetailFamille.setRefus(false);
            try {
                currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService()
                        .create(currentDetailFamille);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la création d'un détail famille avec paramètres corrects "
                        + ex.toString());
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            Assert.assertEquals(false, currentDetailFamille.isNew());
            Assert.assertEquals("2011", currentDetailFamille.getAnneeHistorique());
            Assert.assertEquals("1", currentDetailFamille.getIdFamille());
            Assert.assertNotNull(currentDetailFamille.getId());
            Assert.assertNotNull(currentDetailFamille.getIdDetailFamille());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }

    }

    /**
     * Test de suppression d'un detailFamille
     *
     */
    public void testDelete() {
        try {
            SimpleDetailFamille currentDetailFamille = new SimpleDetailFamille();

            // Effacement d'un detailFamille null
            // Attente de résultat >> exception de type DetailFamilleException
            boolean bDetailFamilleException = false;
            try {
                currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().delete(null);
            } catch (DetailFamilleException ex) {
                bDetailFamilleException = true;
            } catch (Exception ex) {
                bDetailFamilleException = false;
            }
            Assert.assertEquals(true, bDetailFamilleException);

            // Effacement d'un detailFamille qui est nouveau >> isNew = true
            // Attente de résultat >> exception !
            boolean bJadePersistenceException = false;
            currentDetailFamille = new SimpleDetailFamille();
            currentDetailFamille.setId("-10"); // id sans historique
            try {
                currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService()
                        .delete(currentDetailFamille);
            } catch (JadePersistenceException ex) {
                bJadePersistenceException = true;
            } catch (Exception ex) {
                bJadePersistenceException = false;
            }
            Assert.assertEquals(true, bJadePersistenceException);

            // Effacement d'une detailFamille existante, 35 = id avec historique envoi
            // Attente de résultat >> JadeThread.hasError
            currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().read("35");
            currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().delete(currentDetailFamille);
            Assert.assertTrue(JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

            // Effacement d'une detailFamille existante, 36 = id sans historique
            // Attente de résultat >> effacement OK
            currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().read("36");
            boolean bDeleteOK = true;
            try {
                currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService()
                        .delete(currentDetailFamille);
            } catch (Exception ex) {
                bDeleteOK = false;
            }
            Assert.assertTrue(bDeleteOK);
            Assert.assertTrue(currentDetailFamille.isNew());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    /**
     * Test de lecture d'un detailFamille
     *
     */
    public void testRead() {
        try {
            // Lecture d'un detail famille avec un id bidon
            // Attente de résultat >> currentDetailFamille id=-1, tous les autres champs sont null
            SimpleDetailFamille currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService()
                    .read("-1");
            Assert.assertEquals("-1", currentDetailFamille.getId());
            Assert.assertEquals("-1", currentDetailFamille.getIdDetailFamille());
            Assert.assertNull(currentDetailFamille.getAnneeHistorique());
            Assert.assertNull(currentDetailFamille.getAnneeRecalcul());
            Assert.assertNull(currentDetailFamille.getCodeTraitement());
            Assert.assertNull(currentDetailFamille.getCodeTraitementDossier());
            Assert.assertNull(currentDetailFamille.getDateAnnonceCaisseMaladie());
            Assert.assertNull(currentDetailFamille.getDateAvisRIP());
            Assert.assertNull(currentDetailFamille.getDateEnvoi());
            Assert.assertNull(currentDetailFamille.getDateModification());
            Assert.assertNull(currentDetailFamille.getDateRecepDemande());
            Assert.assertNull(currentDetailFamille.getDebutDroit());
            Assert.assertNull(currentDetailFamille.getFinDroit());
            Assert.assertNull(currentDetailFamille.getIdContribuable());
            Assert.assertNull(currentDetailFamille.getIdFamille());
            Assert.assertNull(currentDetailFamille.getMontantContribAnnuelle());
            Assert.assertNull(currentDetailFamille.getMontantContribSansSuppl());
            Assert.assertNull(currentDetailFamille.getMontantContribution());
            Assert.assertNull(currentDetailFamille.getMontantContributionAssiste());
            Assert.assertNull(currentDetailFamille.getMontantExact());
            Assert.assertNull(currentDetailFamille.getMontantPrimeAssurance());
            Assert.assertNull(currentDetailFamille.getMontantSupplement());
            Assert.assertNull(currentDetailFamille.getNoAssure());
            Assert.assertNull(currentDetailFamille.getNoCaisseMaladie());
            Assert.assertNull(currentDetailFamille.getNoLot());
            Assert.assertNull(currentDetailFamille.getNoModeles());
            Assert.assertNull(currentDetailFamille.getOldMontantContribAnnuelle());
            Assert.assertNull(currentDetailFamille.getSupplExtra());
            Assert.assertNull(currentDetailFamille.getTauxEnfantCharge());
            Assert.assertNull(currentDetailFamille.getTypeAvisRIP());
            Assert.assertNull(currentDetailFamille.getTypeDemande());
            Assert.assertNull(currentDetailFamille.getUser());
            Assert.assertNull(currentDetailFamille.getAnnonceCaisseMaladie());
            Assert.assertNull(currentDetailFamille.getCodeActif());
            Assert.assertNull(currentDetailFamille.getCodeForcer());
            Assert.assertNull(currentDetailFamille.getRefus());
            Assert.assertEquals(true, currentDetailFamille.isNew());
            // Lecture d'un detailFamille avec un id OK
            // Attente de résultat >> un modèle renseigné et exploitable
            currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().read("1");
            Assert.assertEquals("1", currentDetailFamille.getId());
            Assert.assertEquals("1", currentDetailFamille.getIdDetailFamille());
            Assert.assertNotNull(currentDetailFamille.getAnneeHistorique());
            Assert.assertNotNull(currentDetailFamille.getAnneeRecalcul());
            Assert.assertNotNull(currentDetailFamille.getCodeTraitement());
            Assert.assertNotNull(currentDetailFamille.getCodeTraitementDossier());
            Assert.assertNotNull(currentDetailFamille.getDateAnnonceCaisseMaladie());
            Assert.assertNotNull(currentDetailFamille.getDateAvisRIP());
            Assert.assertNotNull(currentDetailFamille.getDateEnvoi());
            Assert.assertNotNull(currentDetailFamille.getDateModification());
            Assert.assertNotNull(currentDetailFamille.getDateRecepDemande());
            Assert.assertNotNull(currentDetailFamille.getDebutDroit());
            Assert.assertNotNull(currentDetailFamille.getFinDroit());
            Assert.assertNotNull(currentDetailFamille.getIdContribuable());
            Assert.assertNotNull(currentDetailFamille.getIdFamille());
            Assert.assertNotNull(currentDetailFamille.getMontantContribAnnuelle());
            Assert.assertNull(currentDetailFamille.getMontantContribSansSuppl());
            Assert.assertNotNull(currentDetailFamille.getMontantContribution());
            Assert.assertNull(currentDetailFamille.getMontantContributionAssiste());
            Assert.assertNotNull(currentDetailFamille.getMontantExact());
            Assert.assertNotNull(currentDetailFamille.getMontantPrimeAssurance());
            Assert.assertNull(currentDetailFamille.getMontantSupplement());
            Assert.assertNotNull(currentDetailFamille.getNoAssure());
            Assert.assertNotNull(currentDetailFamille.getNoCaisseMaladie());
            Assert.assertNotNull(currentDetailFamille.getNoLot());
            Assert.assertNotNull(currentDetailFamille.getNoModeles());
            Assert.assertNotNull(currentDetailFamille.getOldMontantContribAnnuelle());
            Assert.assertNotNull(currentDetailFamille.getSupplExtra());
            Assert.assertNotNull(currentDetailFamille.getTauxEnfantCharge());
            Assert.assertNotNull(currentDetailFamille.getTypeAvisRIP());
            Assert.assertNotNull(currentDetailFamille.getTypeDemande());
            Assert.assertNotNull(currentDetailFamille.getUser());
            Assert.assertNotNull(currentDetailFamille.getAnnonceCaisseMaladie());
            Assert.assertNotNull(currentDetailFamille.getCodeActif());
            Assert.assertNotNull(currentDetailFamille.getCodeForcer());
            Assert.assertNotNull(currentDetailFamille.getRefus());
            Assert.assertEquals(false, currentDetailFamille.isNew());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    /**
     *
     * Test de recherche d'un détail Famille
     *
     */
    public void testSearch() {
        try {
            // Recherche d'un detail Famille sans critères de recherche
            // Attente de résultat >> nombre de résultat >= 1
            SimpleDetailFamilleSearch detailSearch = new SimpleDetailFamilleSearch();
            detailSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(detailSearch);
            int iNbResults = detailSearch.getSize();
            Assert.assertEquals(true, (iNbResults >= 1));

            // Recherche d'un detail Famille avec des critères de recherche erronés
            // Attente de résultat >> nombre de résultat = 0
            detailSearch = new SimpleDetailFamilleSearch();
            detailSearch.setForAnneeHistorique("99999999");
            detailSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(detailSearch);
            iNbResults = detailSearch.getSize();
            Assert.assertEquals(true, (iNbResults <= 0));

            // Recherche d'un detail Famille avec des critères de recherche ciblés
            // Attente de résultat >> nombre de résultat = 1
            // Attente de résultat >> résultat exploitable (modèle)
            detailSearch = new SimpleDetailFamilleSearch();
            detailSearch.setForIdDetailFamille("1");
            detailSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(detailSearch);
            iNbResults = detailSearch.getSize();
            Assert.assertEquals(true, (iNbResults == 1));
            SimpleDetailFamille currentDetailFamille = (SimpleDetailFamille) detailSearch.getSearchResults()[0];
            // Attente de résultat >> un modèle renseigné et exploitable
            Assert.assertEquals("1", currentDetailFamille.getId());
            Assert.assertEquals("1", currentDetailFamille.getIdDetailFamille());
            Assert.assertNotNull(currentDetailFamille.getAnneeHistorique());
            Assert.assertNotNull(currentDetailFamille.getAnneeRecalcul());
            Assert.assertNotNull(currentDetailFamille.getCodeTraitement());
            Assert.assertNotNull(currentDetailFamille.getCodeTraitementDossier());
            Assert.assertNotNull(currentDetailFamille.getDateAnnonceCaisseMaladie());
            Assert.assertNotNull(currentDetailFamille.getDateAvisRIP());
            Assert.assertNotNull(currentDetailFamille.getDateEnvoi());
            Assert.assertNotNull(currentDetailFamille.getDateModification());
            Assert.assertNotNull(currentDetailFamille.getDateRecepDemande());
            Assert.assertNotNull(currentDetailFamille.getDebutDroit());
            Assert.assertNotNull(currentDetailFamille.getFinDroit());
            Assert.assertNotNull(currentDetailFamille.getIdContribuable());
            Assert.assertNotNull(currentDetailFamille.getIdFamille());
            Assert.assertNotNull(currentDetailFamille.getMontantContribAnnuelle());
            Assert.assertNull(currentDetailFamille.getMontantContribSansSuppl());
            Assert.assertNotNull(currentDetailFamille.getMontantContribution());
            Assert.assertNull(currentDetailFamille.getMontantContributionAssiste());
            Assert.assertNotNull(currentDetailFamille.getMontantExact());
            Assert.assertNotNull(currentDetailFamille.getMontantPrimeAssurance());
            Assert.assertNull(currentDetailFamille.getMontantSupplement());
            Assert.assertNotNull(currentDetailFamille.getNoAssure());
            Assert.assertNotNull(currentDetailFamille.getNoCaisseMaladie());
            Assert.assertNotNull(currentDetailFamille.getNoLot());
            Assert.assertNotNull(currentDetailFamille.getNoModeles());
            Assert.assertNotNull(currentDetailFamille.getOldMontantContribAnnuelle());
            Assert.assertNotNull(currentDetailFamille.getSupplExtra());
            Assert.assertNotNull(currentDetailFamille.getTauxEnfantCharge());
            Assert.assertNotNull(currentDetailFamille.getTypeAvisRIP());
            Assert.assertNotNull(currentDetailFamille.getTypeDemande());
            Assert.assertNotNull(currentDetailFamille.getUser());
            Assert.assertNotNull(currentDetailFamille.getAnnonceCaisseMaladie());
            Assert.assertNotNull(currentDetailFamille.getCodeActif());
            Assert.assertNotNull(currentDetailFamille.getCodeForcer());
            Assert.assertNotNull(currentDetailFamille.getRefus());
            Assert.assertEquals(false, currentDetailFamille.isNew());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    /**
     * Test de mise à jour d'une entité detailFamille
     *
     */
    public void testUpdate() {

        try {
            // Mise à jour d'un detail Famille qui est nouveau >> isNew = true
            // Attente de résultat >> Exception du framework
            SimpleDetailFamille currentDetailFamille = new SimpleDetailFamille();
            currentDetailFamille.setAnneeHistorique("2011");
            currentDetailFamille.setDebutDroit("02.2011");
            currentDetailFamille.setIdFamille("1");
            currentDetailFamille.setRefus(false);
            try {
                currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService()
                        .update(currentDetailFamille);
                Assert.fail("Exception non soulevée lors de la mise à jour d'une detail Famille isNew");
            } catch (Exception ex) {
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            // Mise à jour d'un detail Famille existante avec des paramètres erronés
            // Attente de résultat >> Message dans JadeThread levé par le checker
            currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().read("1");
            currentDetailFamille.setAnneeHistorique("-1");
            try {
                currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService()
                        .update(currentDetailFamille);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la mise à jour d'une detail Famille avec paramètres erronés "
                        + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Mise à jour d'un detail Famille existant avec des paramètres corrects
            // Attente de résultat >> modèle renseigné en retour = modèle de base updaté
            currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().read("1");
            currentDetailFamille.setAnneeHistorique("1996");
            currentDetailFamille.setAnneeRecalcul("1995");
            try {
                currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService()
                        .update(currentDetailFamille);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la mise à jour d'un detail Famille avec paramètres corrects "
                        + ex.toString());
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            Assert.assertEquals("1996", currentDetailFamille.getAnneeHistorique());
            Assert.assertEquals("1995", currentDetailFamille.getAnneeRecalcul());
            Assert.assertEquals(false, currentDetailFamille.isNew());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }
    }

}
