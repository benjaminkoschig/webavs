/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.annonce;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.models.annonce.SimpleAnnonce;
import ch.globaz.amal.business.models.annonce.SimpleAnnonceSearch;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * Tests des services déclarés pour les annonces
 * 
 * @author DHI
 * 
 */
public class AnnonceServiceImplTest {

    /**
     * Test la création d'une annonce
     */

    @Ignore
    @Test
    public void testCreate() {
        try {
            // Création d'une annonce avec des paramètres erronés dans le modèle
            // Attente de résultat >> JadeThread.hasMessage à true
            SimpleAnnonce currentAnnonce = new SimpleAnnonce();
            currentAnnonce.setAnneeHistorique("0");
            currentAnnonce.setIdDetailFamille("-1");
            try {
                currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().create(currentAnnonce);
            } catch (Exception ex) {
                fail("Exception soulevée lors de la création d'une annonce avec paramètres erronés " + ex.toString());
            }
            assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Création d'une annonce avec des paramètres corrects et boolean renseigné
            // Attente de résultat >> modèle renseigné avec isNew à false
            currentAnnonce = new SimpleAnnonce();
            currentAnnonce.setAnneeHistorique("2011");
            currentAnnonce.setIdDetailFamille("1");
            currentAnnonce.setNoCaisseMaladie("191010");
            currentAnnonce.setCodeActif(true);
            currentAnnonce.setCodeForcer(false);
            currentAnnonce.setRefuse(false);
            currentAnnonce.setAnnonceCaisseMaladie(true);
            try {
                currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().create(currentAnnonce);
            } catch (Exception ex) {
                fail("Exception soulevée lors de la création d'une annonce avec paramètres corrects " + ex.toString());
            }
            assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            assertEquals(false, currentAnnonce.isNew());
            assertEquals("2011", currentAnnonce.getAnneeHistorique());
            assertEquals("1", currentAnnonce.getIdDetailFamille());
            assertEquals("191010", currentAnnonce.getNoCaisseMaladie());
            assertEquals(true, (boolean) currentAnnonce.getCodeActif());
            assertEquals(false, (boolean) currentAnnonce.getCodeForcer());
            assertEquals(false, (boolean) currentAnnonce.getRefuse());
            assertEquals(true, (boolean) currentAnnonce.getAnnonceCaisseMaladie());
            assertNotNull(currentAnnonce.getId());
            assertNotNull(currentAnnonce.getIdDetailAnnonce());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        } finally {

            JadeThread.logClear();
        }
    }

    /**
     * Test de suppression d'une annonce
     */
    @Ignore
    @Test
    public void testDelete() {
        try {
            SimpleAnnonce currentAnnonce = new SimpleAnnonce();

            // Effacement d'une annonce null
            // Attente de résultat >> exception de type AnnonceException
            boolean bAnnonceException = false;
            try {
                currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().delete(null);
            } catch (AnnonceException ex) {
                bAnnonceException = true;
            }
            assertEquals(true, bAnnonceException);

            // Effacement d'une annonce qui est nouvelle >> isNew = true
            // Attente de résultat >> exception !
            boolean bJadePersistenceException = false;
            currentAnnonce = new SimpleAnnonce();
            try {
                currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().delete(currentAnnonce);
            } catch (JadePersistenceException ex) {
                bJadePersistenceException = true;
            }
            assertEquals(true, bJadePersistenceException);

            // Effacement d'une annonce existante
            // Attente de résultat >> effacement OK
            currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().read("1");
            boolean bDeleteOK = true;
            try {
                currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().delete(currentAnnonce);
            } catch (Exception ex) {
                bDeleteOK = false;
            }
            assertEquals(true, bDeleteOK);
            assertEquals(true, currentAnnonce.isNew());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test de lecture d'une annonce
     */
    @Ignore
    @Test
    public void testRead() {
        try {
            // Lecture d'une annonce avec un id bidon
            // Attente de résultat >> currentAnnonce id=-1, tous les autres champs sont null
            SimpleAnnonce currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().read("-1");
            assertEquals("-1", currentAnnonce.getId());
            assertEquals("-1", currentAnnonce.getIdDetailAnnonce());
            assertNull(currentAnnonce.getAncienMontantContributionAnnuelle());
            assertNull(currentAnnonce.getAnneeHistorique());
            assertNull(currentAnnonce.getAnneeRecalcul());
            assertNull(currentAnnonce.getCodeTraitement());
            assertNull(currentAnnonce.getCodeTraitementDossier());
            assertNull(currentAnnonce.getCreationSpy());
            assertNull(currentAnnonce.getDateAvisRIP());
            assertNull(currentAnnonce.getDateEnvoiAnnonce());
            assertNull(currentAnnonce.getDateModification());
            assertNull(currentAnnonce.getDateReceptionDemande());
            assertNull(currentAnnonce.getDebutDroit());
            assertNull(currentAnnonce.getFinDroit());
            assertNull(currentAnnonce.getIdDetailFamille());
            assertNull(currentAnnonce.getMontantContribution());
            assertNull(currentAnnonce.getMontantContributionAnnuelle());
            assertNull(currentAnnonce.getMontantExact());
            assertNull(currentAnnonce.getMontantPrime());
            assertNull(currentAnnonce.getNoAssure());
            assertNull(currentAnnonce.getNoCaisseMaladie());
            assertNull(currentAnnonce.getNoModele());
            assertNull(currentAnnonce.getNumeroLot());
            assertNull(currentAnnonce.getSpy());
            assertNull(currentAnnonce.getSupplementExtraordinaire());
            assertNull(currentAnnonce.getTauxEnfantCharge());
            assertNull(currentAnnonce.getTypeAvisRIP());
            assertNull(currentAnnonce.getTypeDemande());
            assertNull(currentAnnonce.getAnnonceCaisseMaladie());
            assertNull(currentAnnonce.getCodeActif());
            assertNull(currentAnnonce.getCodeForcer());
            assertNull(currentAnnonce.getRefuse());
            assertEquals(true, currentAnnonce.isNew());
            // Lecture d'une annonce avec un id OK
            // Attente de résultat >> un modèle renseigné et exploitable
            currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().read("1");
            assertEquals("1", currentAnnonce.getId());
            assertEquals("1", currentAnnonce.getIdDetailAnnonce());
            assertNotNull(currentAnnonce.getAncienMontantContributionAnnuelle());
            assertNotNull(currentAnnonce.getAnneeHistorique());
            assertNotNull(currentAnnonce.getAnneeRecalcul());
            assertNotNull(currentAnnonce.getCodeTraitement());
            assertNotNull(currentAnnonce.getCodeTraitementDossier());
            assertNotNull(currentAnnonce.getCreationSpy());
            assertNotNull(currentAnnonce.getDateAvisRIP());
            assertNotNull(currentAnnonce.getDateEnvoiAnnonce());
            assertNotNull(currentAnnonce.getDateModification());
            assertNotNull(currentAnnonce.getDateReceptionDemande());
            assertNotNull(currentAnnonce.getDebutDroit());
            assertNotNull(currentAnnonce.getFinDroit());
            assertNotNull(currentAnnonce.getIdDetailFamille());
            assertNotNull(currentAnnonce.getMontantContribution());
            assertNotNull(currentAnnonce.getMontantContributionAnnuelle());
            assertNotNull(currentAnnonce.getMontantExact());
            assertNotNull(currentAnnonce.getMontantPrime());
            assertNotNull(currentAnnonce.getNoAssure());
            assertNotNull(currentAnnonce.getNoCaisseMaladie());
            assertNotNull(currentAnnonce.getNoModele());
            assertNotNull(currentAnnonce.getNumeroLot());
            assertNotNull(currentAnnonce.getSpy());
            assertNotNull(currentAnnonce.getSupplementExtraordinaire());
            assertNotNull(currentAnnonce.getTauxEnfantCharge());
            assertNotNull(currentAnnonce.getTypeAvisRIP());
            assertNotNull(currentAnnonce.getTypeDemande());
            assertNotNull(currentAnnonce.getAnnonceCaisseMaladie());
            assertNotNull(currentAnnonce.getCodeActif());
            assertNotNull(currentAnnonce.getCodeForcer());
            assertNotNull(currentAnnonce.getRefuse());
            assertEquals(false, currentAnnonce.isNew());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test de recherche d'une annonce
     */
    @Ignore
    @Test
    public void testSearch() {
        try {
            // Recherche d'une annonce sans critères de recherche
            // Attente de résultat >> nombre de résultat >= 1
            SimpleAnnonceSearch annonceSearch = new SimpleAnnonceSearch();
            annonceSearch = AmalImplServiceLocator.getSimpleAnnonceService().search(annonceSearch);
            int iNbResults = annonceSearch.getSize();
            assertEquals(true, (iNbResults >= 1));

            // Recherche d'une annonce avec des critères de recherche erronés
            // Attente de résultat >> nombre de résultat = 0
            annonceSearch = new SimpleAnnonceSearch();
            annonceSearch.setForAnneeHistorique("99999999");
            annonceSearch = AmalImplServiceLocator.getSimpleAnnonceService().search(annonceSearch);
            iNbResults = annonceSearch.getSize();
            assertEquals(true, (iNbResults <= 0));

            // Recherche d'une annonce avec des critères de recherche ciblés
            // Attente de résultat >> nombre de résultat = 1
            // Attente de résultat >> résultat exploitable (modèle)
            annonceSearch = new SimpleAnnonceSearch();
            annonceSearch.setForAnneeHistorique("2010");
            annonceSearch.setForIdDetailAnnonce("1");
            annonceSearch.setForIdDetailFamille("32");
            annonceSearch.setForNoCaisseMaladie("195606");
            annonceSearch = AmalImplServiceLocator.getSimpleAnnonceService().search(annonceSearch);
            iNbResults = annonceSearch.getSize();
            assertEquals(true, (iNbResults == 1));
            SimpleAnnonce currentAnnonce = (SimpleAnnonce) annonceSearch.getSearchResults()[0];
            // Attente de résultat >> un modèle renseigné et exploitable
            assertEquals("1", currentAnnonce.getId());
            assertEquals("1", currentAnnonce.getIdDetailAnnonce());
            assertNotNull(currentAnnonce.getAncienMontantContributionAnnuelle());
            assertNotNull(currentAnnonce.getAnneeHistorique());
            assertNotNull(currentAnnonce.getAnneeRecalcul());
            assertNotNull(currentAnnonce.getCodeTraitement());
            assertNotNull(currentAnnonce.getCodeTraitementDossier());
            assertNotNull(currentAnnonce.getCreationSpy());
            assertNotNull(currentAnnonce.getDateAvisRIP());
            assertNotNull(currentAnnonce.getDateEnvoiAnnonce());
            assertNotNull(currentAnnonce.getDateModification());
            assertNotNull(currentAnnonce.getDateReceptionDemande());
            assertNotNull(currentAnnonce.getDebutDroit());
            assertNotNull(currentAnnonce.getFinDroit());
            assertNotNull(currentAnnonce.getIdDetailFamille());
            assertNotNull(currentAnnonce.getMontantContribution());
            assertNotNull(currentAnnonce.getMontantContributionAnnuelle());
            assertNotNull(currentAnnonce.getMontantExact());
            assertNotNull(currentAnnonce.getMontantPrime());
            assertNotNull(currentAnnonce.getNoAssure());
            assertNotNull(currentAnnonce.getNoCaisseMaladie());
            assertNotNull(currentAnnonce.getNoModele());
            assertNotNull(currentAnnonce.getNumeroLot());
            assertNotNull(currentAnnonce.getSpy());
            assertNotNull(currentAnnonce.getSupplementExtraordinaire());
            assertNotNull(currentAnnonce.getTauxEnfantCharge());
            assertNotNull(currentAnnonce.getTypeAvisRIP());
            assertNotNull(currentAnnonce.getTypeDemande());
            assertNotNull(currentAnnonce.getAnnonceCaisseMaladie());
            assertNotNull(currentAnnonce.getCodeActif());
            assertNotNull(currentAnnonce.getCodeForcer());
            assertNotNull(currentAnnonce.getRefuse());
            assertEquals(false, currentAnnonce.isNew());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test de mise à jour d'une annonce
     */
    @Ignore
    @Test
    public void testUpdate() {
        try {
            // Mise à jour d'une annonce qui est nouvelle >> isNew = true
            // Attente de résultat >> persistence réagit
            SimpleAnnonce currentAnnonce = new SimpleAnnonce();
            currentAnnonce.setAnneeHistorique("2011");
            currentAnnonce.setIdDetailFamille("1");
            currentAnnonce.setNoCaisseMaladie("191010");
            currentAnnonce.setMontantContribution("40");
            currentAnnonce.setCodeActif(true);
            currentAnnonce.setCodeForcer(false);
            currentAnnonce.setRefuse(false);
            currentAnnonce.setAnnonceCaisseMaladie(true);
            try {
                currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().update(currentAnnonce);
                fail("Exception non soulevée lors de la mise à jour d'une annonce isNew");
            } catch (Exception ex) {
                JadeThread.logClear();
            }
            assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            // Mise à jour d'une annonce existante avec des paramètres erronés
            // Attente de résultat >> Message dans JadeThread levé par le checker
            currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().read("1");
            currentAnnonce.setAnneeHistorique("0");
            currentAnnonce.setIdDetailFamille("-1");
            try {
                currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().update(currentAnnonce);
            } catch (Exception ex) {
                fail("Exception soulevée lors de la mise à jour d'une annonce avec paramètres erronés");
            }
            assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Mise à jour d'une annonce existante avec des paramètres corrects
            // Attente de résultat >> modèle renseigné en retour = modèle de base updaté
            currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().read("1");
            currentAnnonce.setAnneeHistorique("2011");
            currentAnnonce.setAnneeRecalcul("2010");
            currentAnnonce.setCodeActif(true);
            currentAnnonce.setCodeForcer(false);
            currentAnnonce.setRefuse(false);
            currentAnnonce.setAnnonceCaisseMaladie(true);
            try {
                currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().update(currentAnnonce);
            } catch (Exception ex) {
                fail("Exception soulevée lors de la mise à jour d'une annonce avec paramètres corrects");
            }
            assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            assertEquals("2011", currentAnnonce.getAnneeHistorique());
            assertEquals("2010", currentAnnonce.getAnneeRecalcul());
            assertEquals(false, currentAnnonce.isNew());
            assertEquals(true, (boolean) currentAnnonce.getCodeActif());
            assertEquals(false, (boolean) currentAnnonce.getCodeForcer());
            assertEquals(false, (boolean) currentAnnonce.getRefuse());
            assertEquals(true, (boolean) currentAnnonce.getAnnonceCaisseMaladie());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        } finally {

            JadeThread.logClear();
        }
    }

}
