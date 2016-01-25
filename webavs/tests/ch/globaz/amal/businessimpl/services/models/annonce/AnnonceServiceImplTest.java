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
 * Tests des services d�clar�s pour les annonces
 * 
 * @author DHI
 * 
 */
public class AnnonceServiceImplTest {

    /**
     * Test la cr�ation d'une annonce
     */

    @Ignore
    @Test
    public void testCreate() {
        try {
            // Cr�ation d'une annonce avec des param�tres erron�s dans le mod�le
            // Attente de r�sultat >> JadeThread.hasMessage � true
            SimpleAnnonce currentAnnonce = new SimpleAnnonce();
            currentAnnonce.setAnneeHistorique("0");
            currentAnnonce.setIdDetailFamille("-1");
            try {
                currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().create(currentAnnonce);
            } catch (Exception ex) {
                fail("Exception soulev�e lors de la cr�ation d'une annonce avec param�tres erron�s " + ex.toString());
            }
            assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Cr�ation d'une annonce avec des param�tres corrects et boolean renseign�
            // Attente de r�sultat >> mod�le renseign� avec isNew � false
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
                fail("Exception soulev�e lors de la cr�ation d'une annonce avec param�tres corrects " + ex.toString());
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
            // Attente de r�sultat >> exception de type AnnonceException
            boolean bAnnonceException = false;
            try {
                currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().delete(null);
            } catch (AnnonceException ex) {
                bAnnonceException = true;
            }
            assertEquals(true, bAnnonceException);

            // Effacement d'une annonce qui est nouvelle >> isNew = true
            // Attente de r�sultat >> exception !
            boolean bJadePersistenceException = false;
            currentAnnonce = new SimpleAnnonce();
            try {
                currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().delete(currentAnnonce);
            } catch (JadePersistenceException ex) {
                bJadePersistenceException = true;
            }
            assertEquals(true, bJadePersistenceException);

            // Effacement d'une annonce existante
            // Attente de r�sultat >> effacement OK
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
            // Attente de r�sultat >> currentAnnonce id=-1, tous les autres champs sont null
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
            // Attente de r�sultat >> un mod�le renseign� et exploitable
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
            // Recherche d'une annonce sans crit�res de recherche
            // Attente de r�sultat >> nombre de r�sultat >= 1
            SimpleAnnonceSearch annonceSearch = new SimpleAnnonceSearch();
            annonceSearch = AmalImplServiceLocator.getSimpleAnnonceService().search(annonceSearch);
            int iNbResults = annonceSearch.getSize();
            assertEquals(true, (iNbResults >= 1));

            // Recherche d'une annonce avec des crit�res de recherche erron�s
            // Attente de r�sultat >> nombre de r�sultat = 0
            annonceSearch = new SimpleAnnonceSearch();
            annonceSearch.setForAnneeHistorique("99999999");
            annonceSearch = AmalImplServiceLocator.getSimpleAnnonceService().search(annonceSearch);
            iNbResults = annonceSearch.getSize();
            assertEquals(true, (iNbResults <= 0));

            // Recherche d'une annonce avec des crit�res de recherche cibl�s
            // Attente de r�sultat >> nombre de r�sultat = 1
            // Attente de r�sultat >> r�sultat exploitable (mod�le)
            annonceSearch = new SimpleAnnonceSearch();
            annonceSearch.setForAnneeHistorique("2010");
            annonceSearch.setForIdDetailAnnonce("1");
            annonceSearch.setForIdDetailFamille("32");
            annonceSearch.setForNoCaisseMaladie("195606");
            annonceSearch = AmalImplServiceLocator.getSimpleAnnonceService().search(annonceSearch);
            iNbResults = annonceSearch.getSize();
            assertEquals(true, (iNbResults == 1));
            SimpleAnnonce currentAnnonce = (SimpleAnnonce) annonceSearch.getSearchResults()[0];
            // Attente de r�sultat >> un mod�le renseign� et exploitable
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
     * Test de mise � jour d'une annonce
     */
    @Ignore
    @Test
    public void testUpdate() {
        try {
            // Mise � jour d'une annonce qui est nouvelle >> isNew = true
            // Attente de r�sultat >> persistence r�agit
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
                fail("Exception non soulev�e lors de la mise � jour d'une annonce isNew");
            } catch (Exception ex) {
                JadeThread.logClear();
            }
            assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            // Mise � jour d'une annonce existante avec des param�tres erron�s
            // Attente de r�sultat >> Message dans JadeThread lev� par le checker
            currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().read("1");
            currentAnnonce.setAnneeHistorique("0");
            currentAnnonce.setIdDetailFamille("-1");
            try {
                currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().update(currentAnnonce);
            } catch (Exception ex) {
                fail("Exception soulev�e lors de la mise � jour d'une annonce avec param�tres erron�s");
            }
            assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Mise � jour d'une annonce existante avec des param�tres corrects
            // Attente de r�sultat >> mod�le renseign� en retour = mod�le de base updat�
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
                fail("Exception soulev�e lors de la mise � jour d'une annonce avec param�tres corrects");
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
