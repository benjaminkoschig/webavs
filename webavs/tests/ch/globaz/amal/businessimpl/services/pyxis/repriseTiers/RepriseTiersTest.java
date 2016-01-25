/**
 * 
 */
package ch.globaz.amal.businessimpl.services.pyxis.repriseTiers;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.log.business.renderer.JadeBusinessMessageRendererDefaultStringAdapter;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.PersonneEtendueService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.pyxis.exception.PyxisException;

/**
 * @author dhi
 * 
 */
public class RepriseTiersTest {

    /**
     * Génération d'une erreur, pour le fun
     */
    @Ignore
    @Test
    public void testGenerateError() {
        int result = 12 / 0;
        Assert.assertEquals(4, result);
    }

    /**
     * Test la lecture(recherche) d'un tiers par date de naissance et nom prénom
     */
    @Ignore
    @Test
    public void testSearchDateNaissanceNomPrenom() {
        try {
            // Lecture de A Marca Alain
            // Designation 1 A Marca
            // Designation 2 Alain
            // Date de naissance 01.01.1955
            PersonneEtendueSearchComplexModel personneEtendueSearch = new PersonneEtendueSearchComplexModel();
            personneEtendueSearch.setForDateNaissance("01.01.1955");
            personneEtendueSearch.setForDesignation1("A MARCA");
            personneEtendueSearch.setForDesignation2("ALAIN");
            personneEtendueSearch.setFor_isInactif("2");
            try {
                personneEtendueSearch = TIBusinessServiceLocator.getPersonneEtendueService()
                        .find(personneEtendueSearch);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la recherche du tiers A Marca Alain" + ex.toString());
            }
            // Test réussi si unique
            if (personneEtendueSearch.getSize() == 1) {
                // Test des valeurs du résultat
                PersonneEtendueComplexModel personneEtendue = (PersonneEtendueComplexModel) personneEtendueSearch
                        .getSearchResults()[0];
                Assert.assertEquals("A MARCA", personneEtendue.getTiers().getDesignationUpper1());
                Assert.assertEquals("ALAIN", personneEtendue.getTiers().getDesignationUpper2());
                Assert.assertEquals("01.01.1955", personneEtendue.getPersonne().getDateNaissance());
            } else {
                Assert.fail("Echec : recherche A Marca Alain >> " + personneEtendueSearch.getSize() + " résultats.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }
    }

    /**
     * Test la lecture(recherche) d'un tiers par NSS
     */
    @Ignore
    @Test
    public void testSearchNSS() {
        try {

            // Lecture de A Marca Clémentine
            // NNSS 756.4979.9457.60
            PersonneEtendueSearchComplexModel personneEtendueSearch = new PersonneEtendueSearchComplexModel();
            personneEtendueSearch.setForNumeroAvsActuel("756.4979.9457.60");
            personneEtendueSearch.setFor_isInactif("2");
            try {
                personneEtendueSearch = TIBusinessServiceLocator.getPersonneEtendueService()
                        .find(personneEtendueSearch);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la recherche du tiers A Marca Clementine" + ex.toString());
            }
            // Test réussi si unique
            if (personneEtendueSearch.getSize() == 1) {
                // Test des valeurs du résultat
                PersonneEtendueComplexModel personneEtendue = (PersonneEtendueComplexModel) personneEtendueSearch
                        .getSearchResults()[0];
                Assert.assertEquals("A MARCA", personneEtendue.getTiers().getDesignationUpper1());
                Assert.assertEquals("CLEMENTINE", personneEtendue.getTiers().getDesignationUpper2());
                Assert.assertEquals("756.4979.9457.60", personneEtendue.getPersonneEtendue().getNumAvsActuel());
            } else {
                Assert.fail("Echec : recherche A Marca Clémentine >> " + personneEtendueSearch.getSize()
                        + " résultats.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }
    }

    /**
     * Test mise à jour d'un tiers, date de naissance modifiée
     * */
    @Ignore
    @Test
    public void testUpdateDateNaissanceAffilie() {
        try {

            // Lecture de A Marca Boris
            // NNSS 756.5138.9074.14
            PersonneEtendueSearchComplexModel personneEtendueSearch = new PersonneEtendueSearchComplexModel();
            personneEtendueSearch.setForNumeroAvsActuel("756.5138.9074.14");
            personneEtendueSearch.setFor_isInactif("2");
            try {
                personneEtendueSearch = TIBusinessServiceLocator.getPersonneEtendueService()
                        .find(personneEtendueSearch);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la recherche du tiers A Marca Boris" + ex.toString());
            }
            // Test réussi si unique
            if (personneEtendueSearch.getSize() == 1) {
                // Mise à jour de la date de naissance
                PersonneEtendueComplexModel personneEtendue = (PersonneEtendueComplexModel) personneEtendueSearch
                        .getSearchResults()[0];
                personneEtendue.getPersonne().setDateNaissance("10.10.2011");
                try {
                    personneEtendue = TIBusinessServiceLocator.getPersonneEtendueService().update(personneEtendue);
                } catch (Exception ex) {
                    Assert.fail("Exception soulevée lors de la mise à jour du tiers A Marca Boris" + ex.toString());
                }
                // Test pas de messages error ou warning
                String messages = new JadeBusinessMessageRendererDefaultStringAdapter().render(
                        JadeThread.logMessages(), JadeThread.currentLanguage());
                Assert.assertEquals(messages, false,
                        JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertEquals(messages, false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN));
                // Test des données de la mise à jour
                Assert.assertEquals("A MARCA", personneEtendue.getTiers().getDesignationUpper1());
                Assert.assertEquals("BORIS", personneEtendue.getTiers().getDesignationUpper2());
                Assert.assertEquals("756.5138.9074.14", personneEtendue.getPersonneEtendue().getNumAvsActuel());
                Assert.assertEquals("10.10.2011", personneEtendue.getPersonne().getDateNaissance());
            } else {
                Assert.fail("Echec : recherche A Marca Boris >> " + personneEtendueSearch.getSize() + " résultats.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }
    }

    /**
     * Test mise à jour d'un tiers, date de naissance modifiée
     * */
    @Ignore
    @Test
    public void testUpdateDateNaissanceAssure() {
        try {

            // Lecture de A Marca Clémentine
            // NNSS 756.4979.9457.60
            PersonneEtendueSearchComplexModel personneEtendueSearch = new PersonneEtendueSearchComplexModel();
            personneEtendueSearch.setForNumeroAvsActuel("756.4979.9457.60");
            personneEtendueSearch.setFor_isInactif("2");
            try {
                personneEtendueSearch = TIBusinessServiceLocator.getPersonneEtendueService()
                        .find(personneEtendueSearch);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la recherche du tiers A Marca Clementine" + ex.toString());
            }
            // Test réussi si unique
            if (personneEtendueSearch.getSize() == 1) {
                // Mise à jour de la date de naissance
                PersonneEtendueComplexModel personneEtendue = (PersonneEtendueComplexModel) personneEtendueSearch
                        .getSearchResults()[0];
                personneEtendue.getPersonne().setDateNaissance("10.10.2011");
                try {
                    personneEtendue = TIBusinessServiceLocator.getPersonneEtendueService().update(personneEtendue);
                } catch (Exception ex) {
                    Assert.fail("Exception soulevée lors de la mise à jour du tiers A Marca Clementine" + ex.toString());
                }
                // Test pas de messages error ou warning
                String messages = new JadeBusinessMessageRendererDefaultStringAdapter().render(
                        JadeThread.logMessages(), JadeThread.currentLanguage());
                Assert.assertEquals(messages, false,
                        JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertEquals(messages, false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN));
                // Test des données de la mise à jour
                Assert.assertEquals("A MARCA", personneEtendue.getTiers().getDesignationUpper1());
                Assert.assertEquals("CLEMENTINE", personneEtendue.getTiers().getDesignationUpper2());
                Assert.assertEquals("756.4979.9457.60", personneEtendue.getPersonneEtendue().getNumAvsActuel());
                Assert.assertEquals("10.10.2011", personneEtendue.getPersonne().getDateNaissance());
            } else {
                Assert.fail("Echec : recherche A Marca Clémentine >> " + personneEtendueSearch.getSize()
                        + " résultats.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }
    }

    /**
     * Test mise à jour d'un tiers, numero de contribuable, date et motif modif non renseignés
     * */
    @Ignore
    @Test
    public void testUpdateNoContribuableDateMotifEmpty() {
        try {
            // Lecture de A Marca Alain
            // NNSS 756.7506.9252.11
            PersonneEtendueSearchComplexModel personneEtendueSearch = new PersonneEtendueSearchComplexModel();
            personneEtendueSearch.setForNumeroAvsActuel("756.7506.9252.11");
            personneEtendueSearch.setFor_isInactif("2");
            try {
                personneEtendueSearch = TIBusinessServiceLocator.getPersonneEtendueService()
                        .find(personneEtendueSearch);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la recherche du tiers A Marca Alain" + ex.toString());
            }
            // Test réussi si unique
            if (personneEtendueSearch.getSize() == 1) {
                // Test des valeurs du résultat
                PersonneEtendueComplexModel personneEtendue = (PersonneEtendueComplexModel) personneEtendueSearch
                        .getSearchResults()[0];
                // Mise à jour du numéro de contribuable actuel
                personneEtendue.getPersonneEtendue().setNumContribuableActuel("200.600.853.60");
                // Nécessaire : date et raison modification
                try {
                    personneEtendue = TIBusinessServiceLocator.getPersonneEtendueService().update(personneEtendue);
                    Assert.fail("Exception non soulevée lors de la mise à jour du tiers A Marca Alain, motif et date modif no contribuable vide !!!!");
                } catch (PyxisException ex) {
                    // Exception renseignée
                    Assert.assertNotNull(ex.getMessage());
                    // Test pas de messages error ou warning
                    String messages = new JadeBusinessMessageRendererDefaultStringAdapter().render(
                            JadeThread.logMessages(), JadeThread.currentLanguage());
                    Assert.assertEquals(messages, false,
                            JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                    Assert.assertEquals(messages, false,
                            JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN));
                    // Test des données de la mise à jour
                    Assert.assertEquals("A MARCA", personneEtendue.getTiers().getDesignationUpper1());
                    Assert.assertEquals("ALAIN", personneEtendue.getTiers().getDesignationUpper2());
                    Assert.assertEquals("756.7506.9252.11", personneEtendue.getPersonneEtendue().getNumAvsActuel());
                    Assert.assertEquals("200.600.853.60", personneEtendue.getPersonneEtendue()
                            .getNumContribuableActuel());
                } catch (Exception e) {
                    Assert.fail("Exception non Pyxis soulevée : " + e.toString());
                }
            } else {
                Assert.fail("Echec : recherche A Marca Alain >> " + personneEtendueSearch.getSize() + " résultats.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }
    }

    /**
     * Test mise à jour d'un tiers, numero de contribuable, date et motif modif non renseigné
     * */
    @Ignore
    @Test
    public void testUpdateNoContribuableDateMotifFilled() {
        try {
            // Lecture de A Marca Alain
            // NNSS 756.7506.9252.11
            PersonneEtendueSearchComplexModel personneEtendueSearch = new PersonneEtendueSearchComplexModel();
            personneEtendueSearch.setForNumeroAvsActuel("756.7506.9252.11");
            personneEtendueSearch.setFor_isInactif("2");
            try {
                personneEtendueSearch = TIBusinessServiceLocator.getPersonneEtendueService()
                        .find(personneEtendueSearch);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la recherche du tiers A Marca Alain" + ex.toString());
            }
            // Test réussi si unique
            if (personneEtendueSearch.getSize() == 1) {
                // Test des valeurs du résultat
                PersonneEtendueComplexModel personneEtendue = (PersonneEtendueComplexModel) personneEtendueSearch
                        .getSearchResults()[0];
                // Mise à jour du numéro de contribuable actuel
                personneEtendue.getPersonneEtendue().setNumContribuableActuel("200.600.853.60");
                // Nécessaire : date et raison modification
                personneEtendue.setDateModifContribuable("01.09.2011");
                personneEtendue.setMotifModifContribuable(PersonneEtendueService.motifsModification.MOTIF_CREATION);
                try {
                    personneEtendue = TIBusinessServiceLocator.getPersonneEtendueService().update(personneEtendue);
                } catch (Exception ex) {
                    Assert.fail("Exception soulevée lors de la mise à jour du tiers A Marca Alain" + ex.toString());
                }
                // Test pas de messages error ou warning
                String messages = new JadeBusinessMessageRendererDefaultStringAdapter().render(
                        JadeThread.logMessages(), JadeThread.currentLanguage());
                Assert.assertEquals(messages, false,
                        JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertEquals(messages, false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN));
                // Test des données de la mise à jour
                Assert.assertEquals("A MARCA", personneEtendue.getTiers().getDesignationUpper1());
                Assert.assertEquals("ALAIN", personneEtendue.getTiers().getDesignationUpper2());
                Assert.assertEquals("756.7506.9252.11", personneEtendue.getPersonneEtendue().getNumAvsActuel());
                Assert.assertEquals("200.600.853.60", personneEtendue.getPersonneEtendue().getNumContribuableActuel());
            } else {
                Assert.fail("Echec : recherche A Marca Alain >> " + personneEtendueSearch.getSize() + " résultats.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }
    }

}
