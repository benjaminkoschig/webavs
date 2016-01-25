package ch.globaz.amal.businessimpl.services.models.famille;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.FamilleContribuableSearch;
import ch.globaz.amal.business.models.famille.FamilleContribuableViewSearch;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * Class de test pour le modèle complex FamilleContribuable
 * 
 * @author cbu
 * 
 */
public class FamilleServiceImplTest {

    private static final String ID_FAMILLECONTRIBUABLE_TO_READ = "151100309";

    @Ignore
    @Test
    public void testCount() {
        try {
            // Count avec paramètres null de simpleFamille
            // Résultat attendu : nbResultats == 0
            boolean bCountNullModelSimpleFamille = false;
            try {
                SimpleFamilleSearch simpleFamilleSearch = null;
                int nbResult = AmalServiceLocator.getFamilleContribuableService().count(simpleFamilleSearch);
            } catch (FamilleException fe) {
                bCountNullModelSimpleFamille = true;
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bCountNullModelSimpleFamille);

            // Count avec paramètres null
            // Résultat attendu : nbResultats == 0
            boolean bCountNullModel = false;
            try {
                FamilleContribuableSearch familleContribuableSearch = null;
                int nbResult = AmalServiceLocator.getFamilleContribuableService().count(familleContribuableSearch);
            } catch (FamilleException fe) {
                bCountNullModel = true;
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bCountNullModel);

            // Count avec paramètres incorrect de simpleFamille
            // Résultat attendu : nbResultats == 0
            try {
                SimpleFamilleSearch simpleFamilleSearch = new SimpleFamilleSearch();
                simpleFamilleSearch.setForIdContribuable("-1");

                int nbResult = AmalServiceLocator.getFamilleContribuableService().count(simpleFamilleSearch);

                Assert.assertTrue(nbResult == 0);
            } catch (FamilleException fe) {
                Assert.fail("Erreur non gérée lors du count sur FamilleContribuableSearch ! ==> " + fe.toString());
            } finally {
                JadeThread.logClear();
            }

            // Count avec paramètres incorrect
            // Résultat attendu : nbResultats == 0
            try {
                FamilleContribuableSearch familleContribuableSearch = new FamilleContribuableSearch();
                familleContribuableSearch.setForIdContribuable("-1");

                int nbResult = AmalServiceLocator.getFamilleContribuableService().count(familleContribuableSearch);

                Assert.assertTrue(nbResult == 0);
            } catch (FamilleException fe) {
                Assert.fail("Erreur non gérée lors du count sur FamilleContribuableSearch ! ==> " + fe.toString());
            } finally {
                JadeThread.logClear();
            }

            // Count avec paramètres corrects de simpleFamille
            // Résultat attendu : nbResultats > 0
            try {
                SimpleFamilleSearch simpleFamilleSearch = new SimpleFamilleSearch();
                simpleFamilleSearch.setForIdContribuable("151");

                int nbResult = AmalServiceLocator.getFamilleContribuableService().count(simpleFamilleSearch);

                Assert.assertTrue(nbResult > 0);

            } catch (FamilleException fe) {
                Assert.fail("Erreur non gérée lors du count sur FamilleContribuableSearch ! ==> " + fe.toString());
            } finally {
                JadeThread.logClear();
            }

            // Count avec paramètres corrects
            // Résultat attendu : nbResultats > 0
            try {
                FamilleContribuableSearch familleContribuableSearch = new FamilleContribuableSearch();
                familleContribuableSearch.setForIdContribuable("151");

                int nbResult = AmalServiceLocator.getFamilleContribuableService().count(familleContribuableSearch);

                Assert.assertTrue(nbResult > 0);
            } catch (FamilleException fe) {
                Assert.fail("Erreur non gérée lors du count sur FamilleContribuableSearch ! ==> " + fe.toString());
            } finally {
                JadeThread.logClear();
            }
        } catch (Exception e) {
            Assert.fail("Erreur générale lors du count de FamilleContribuable --> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    @Ignore
    @Test
    public void testCreate() {
        try {
            // Création d'un membre famille avec modèle null
            // Résultat attendu => FamilleException
            boolean bCreateFamilleContribuableNullmodel = false;
            try {
                FamilleContribuable familleContribuable = AmalServiceLocator.getFamilleContribuableService().create(
                        null);
            } catch (FamilleException fe) {
                bCreateFamilleContribuableNullmodel = true;
            }
            Assert.assertTrue(bCreateFamilleContribuableNullmodel);

            // Création d'un membre famille avec date de naissance manquante
            // Résultat attendu => Erreur business (JadeThread.logHasMessage.... == true)
            try {
                SimpleFamille simpleFamille = new SimpleFamille();
                simpleFamille.setNomPrenom("AcGlobaz TacimGlobaz");
                simpleFamille.setIsContribuable(true);
                simpleFamille.setIdTier("138862");
                simpleFamille.setIdContribuable("151");

                PersonneEtendueComplexModel personneEtendueComplexModel = new PersonneEtendueComplexModel();
                personneEtendueComplexModel.getTiers().setIdTiers("138862");
                personneEtendueComplexModel.getTiers().setDesignation1("Ac");
                personneEtendueComplexModel.getTiers().setDesignation2("Tacim");

                FamilleContribuable familleContribuable = new FamilleContribuable();
                familleContribuable.setSimpleFamille(simpleFamille);
                familleContribuable.setPersonneEtendue(personneEtendueComplexModel);

                familleContribuable = AmalServiceLocator.getFamilleContribuableService().create(familleContribuable);
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertTrue(familleContribuable.isNew());
            } catch (FamilleException fe) {
                Assert.fail("Erreur non gérée lors du create de FamilleContribuable ! ==> " + fe.toString());
            } finally {
                JadeThread.logClear();
            }

            // Création d'un membre famille avec idContribuable inexistant
            // Résultat attendu => Erreur business (JadeThread.logHasMessage.... == true)
            try {
                SimpleFamille simpleFamille = new SimpleFamille();
                simpleFamille.setNomPrenom("AcGlobaz TacimGlobaz");
                simpleFamille.setIsContribuable(true);
                simpleFamille.setIdTier("138862");
                simpleFamille.setDateNaissance("03.01.1982");
                simpleFamille.setIdContribuable("");

                PersonneEtendueComplexModel personneEtendueComplexModel = new PersonneEtendueComplexModel();
                personneEtendueComplexModel.getTiers().setIdTiers("138862");
                personneEtendueComplexModel.getTiers().setDesignation1("Ac");
                personneEtendueComplexModel.getTiers().setDesignation2("Tacim");

                FamilleContribuable familleContribuable = new FamilleContribuable();
                familleContribuable.setSimpleFamille(simpleFamille);
                familleContribuable.setPersonneEtendue(personneEtendueComplexModel);

                familleContribuable = AmalServiceLocator.getFamilleContribuableService().create(familleContribuable);
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertTrue(familleContribuable.isNew());
            } catch (FamilleException fe) {
                Assert.fail("Erreur non gérée lors du create de FamilleContribuable ! ==> " + fe.toString());
            } finally {
                JadeThread.logClear();
            }

            // Création d'un membre famille avec rattachement à un tiers déjà existant
            // Résultat attendu => Ok
            try {
                SimpleFamille simpleFamille = new SimpleFamille();
                simpleFamille.setNomPrenom("AcGlobaz TacimGlobaz");
                simpleFamille.setDateNaissance("03.04.1945");
                simpleFamille.setIsContribuable(true);
                simpleFamille.setIdContribuable("151");
                simpleFamille.setIdTier("138862");
                simpleFamille.setPereMereEnfant(IAMCodeSysteme.CS_TYPE_PERE);

                PersonneEtendueComplexModel personneEtendueComplexModel = new PersonneEtendueComplexModel();
                personneEtendueComplexModel.getTiers().setIdTiers("138862");
                personneEtendueComplexModel.getTiers().setDesignation1("Ac");
                personneEtendueComplexModel.getTiers().setDesignation2("Tacim");
                personneEtendueComplexModel.getPersonne().setDateNaissance("04.05.1962");

                FamilleContribuable familleContribuable = new FamilleContribuable();
                familleContribuable.setSimpleFamille(simpleFamille);
                familleContribuable.setPersonneEtendue(personneEtendueComplexModel);

                familleContribuable = AmalServiceLocator.getFamilleContribuableService().create(familleContribuable);
                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertFalse(familleContribuable.isNew());
            } catch (FamilleException fe) {
                Assert.fail("Erreur non gérée lors du create de FamilleContribuable ! ==> " + fe.toString());
            } finally {
                JadeThread.logClear();
            }

            // Création d'un membre famille avec création du tiers
            // Résultat attendu => Ok
            try {
                PersonneEtendueComplexModel personneEtendueComplexModel = new PersonneEtendueComplexModel();
                personneEtendueComplexModel.getTiers().setDesignation1("AcGlobaz");
                personneEtendueComplexModel.getTiers().setDesignation2("TacimGlobaz");
                personneEtendueComplexModel.getPersonne().setDateNaissance("03.01.1982");

                SimpleFamille simpleFamille = new SimpleFamille();
                simpleFamille.setIdContribuable("151");
                simpleFamille.setNomPrenom("AcGlobaz TacimGlobaz");
                simpleFamille.setDateNaissance("03.04.1945");
                simpleFamille.setIsContribuable(true);
                simpleFamille.setPereMereEnfant(IAMCodeSysteme.CS_TYPE_PERE);

                FamilleContribuable familleContribuable = new FamilleContribuable();
                familleContribuable.setSimpleFamille(simpleFamille);
                familleContribuable.setPersonneEtendue(personneEtendueComplexModel);

                familleContribuable = AmalServiceLocator.getFamilleContribuableService().create(familleContribuable);
                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertFalse(familleContribuable.isNew());
                Assert.assertFalse(familleContribuable.getPersonneEtendue().isNew());
                Assert.assertTrue(familleContribuable.getSimpleFamille().getIdTier()
                        .equals(familleContribuable.getPersonneEtendue().getTiers().getIdTiers()));
            } catch (Exception e) {
                Assert.fail("Erreur non gérée de FamilleContribuable ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
        } catch (Exception e) {
            Assert.fail("Erreur générale lors du create de FamilleContribuable --> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    @Ignore
    @Test
    public void testDelete() {
        try {
            // Delete d'un modèle NULL
            // Résultat attendu : FamilleException
            boolean bDeleteFamilleContribuable = false;
            try {
                AmalServiceLocator.getFamilleContribuableService().delete(null);
            } catch (FamilleException fe) {
                bDeleteFamilleContribuable = true;
            }
            Assert.assertTrue(bDeleteFamilleContribuable);

            // Delete d'un modèle qui possède des subsides
            // Résultat attendu : Erreur métier
            try {
                FamilleContribuable familleContribuable = AmalServiceLocator.getFamilleContribuableService().read(
                        FamilleServiceImplTest.ID_FAMILLECONTRIBUABLE_TO_READ);
                familleContribuable = AmalServiceLocator.getFamilleContribuableService().delete(familleContribuable);
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertFalse(familleContribuable.isNew());
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors du delete de FamilleContribuable --> " + e.toString());
            } finally {
                JadeThread.logClear();
            }

            // Delete d'un modèle ok (ne possède pas de subsides)
            // ID utilisé : 67458 ==> Burrus Bernadette, épouse de Burrus Charles
            // Résultat attendu : simpleFamille.isNew == true
            try {
                FamilleContribuable familleContribuable = AmalServiceLocator.getFamilleContribuableService().read(
                        "41368172902");
                familleContribuable = AmalServiceLocator.getFamilleContribuableService().delete(familleContribuable);
                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertTrue(familleContribuable.isNew());
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors du delete de FamilleContribuable --> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
        } catch (Exception e) {
            Assert.fail("Erreur générale lors du delete de FamilleContribuable --> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    @Ignore
    @Test
    public void testRead() {
        try {
            // Lecture d'un FamilleContribuable vide
            // Résultat attendu : FamilleException
            boolean bReadFamilleContribuable = false;
            try {
                AmalServiceLocator.getFamilleContribuableService().read("");
            } catch (FamilleException fe) {
                bReadFamilleContribuable = true;
            }
            Assert.assertTrue(bReadFamilleContribuable);

            // Lecture d'un FamilleContribuable qui n'existe pas
            // Résultat attendu : familleContribuable.isNew() == true
            try {
                FamilleContribuable familleContribuable = new FamilleContribuable();
                familleContribuable = AmalServiceLocator.getFamilleContribuableService().read("-1");
                Assert.assertTrue(familleContribuable.isNew());
            } catch (FamilleException fe) {
                Assert.fail("Erreur non gérée lors du delete de FamilleContribuable --> " + fe.toString());
            }

            // Lecture d'un FamilleContribuable qui existe
            // Résultat attendu : familleContribuable.isNew() == false
            try {
                FamilleContribuable familleContribuable = new FamilleContribuable();
                familleContribuable = AmalServiceLocator.getFamilleContribuableService().read(
                        FamilleServiceImplTest.ID_FAMILLECONTRIBUABLE_TO_READ);
                Assert.assertFalse(familleContribuable.isNew());
            } catch (FamilleException fe) {
                Assert.fail("Erreur non gérée lors du delete de FamilleContribuable --> " + fe.toString());
            }
        } catch (Exception e) {
            Assert.fail("Erreur générale lors du read de FamilleContribuable --> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    @Ignore
    @Test
    public void testSearch() {
        try {
            // Recherche d'un familleContribuable avec modèle null
            // Résultat attendu : FamilleException
            boolean bSearchNullModelFamilleContribuable = false;
            try {
                FamilleContribuableSearch familleContribuableSearch = null;
                familleContribuableSearch = AmalServiceLocator.getFamilleContribuableService().search(
                        familleContribuableSearch);
            } catch (FamilleException fe) {
                bSearchNullModelFamilleContribuable = true;
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bSearchNullModelFamilleContribuable);

            // Recherche d'un familleContribuableView avec modèle null
            // Résultat attendu : FamilleException
            boolean bSearchNullModelFamilleContribuableView = false;
            try {
                FamilleContribuableViewSearch familleContribuableViewSearch = null;
                familleContribuableViewSearch = AmalServiceLocator.getFamilleContribuableService().search(
                        familleContribuableViewSearch);
            } catch (FamilleException fe) {
                bSearchNullModelFamilleContribuableView = true;
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bSearchNullModelFamilleContribuableView);

            // Recherche d'un familleContribuable avec modèle null
            // Résultat attendu : FamilleException
            boolean bSearchNullModelSimpleFamille = false;
            try {
                SimpleFamilleSearch simpleFamilleSearch = null;
                simpleFamilleSearch = AmalServiceLocator.getFamilleContribuableService().search(simpleFamilleSearch);
            } catch (FamilleException fe) {
                bSearchNullModelSimpleFamille = true;
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bSearchNullModelSimpleFamille);

            // Recherche d'un familleContribuable avec paramètres ok
            // Résultat attendu : nbResult > 0
            try {
                FamilleContribuableSearch familleContribuableSearch = new FamilleContribuableSearch();
                familleContribuableSearch.setForIdContribuable("151");
                familleContribuableSearch = AmalServiceLocator.getFamilleContribuableService().search(
                        familleContribuableSearch);

                Assert.assertTrue(familleContribuableSearch.getSize() > 0);
                FamilleContribuable familleContribuable = (FamilleContribuable) familleContribuableSearch
                        .getSearchResults()[0];
                Assert.assertFalse(familleContribuable.isNew());
                Assert.assertFalse(familleContribuable.getSimpleDetailFamille().isNew());
                Assert.assertTrue("151".equals(familleContribuable.getSimpleFamille().getIdContribuable()));
            } catch (FamilleException fe) {
                Assert.fail("Erreur non gérée lors du search de FamilleContribuable --> " + fe.toString());
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bSearchNullModelFamilleContribuable);

            // // Recherche d'un familleContribuableView avec paramètres ok
            // // Résultat attendu : nbResult > 0
            // try {
            // FamilleContribuableViewSearch familleContribuableViewSearch = new FamilleContribuableViewSearch();
            // familleContribuableViewSearch.setForIdContribuable("151");
            // familleContribuableViewSearch = AmalServiceLocator.getFamilleContribuableService().search(
            // familleContribuableViewSearch);
            //
            // Assert.assertTrue(familleContribuableViewSearch.getSize() > 0);
            // FamilleContribuableView familleContribuableView = (FamilleContribuableView) familleContribuableViewSearch
            // .getSearchResults()[0];
            // Assert.assertFalse(familleContribuableView.isNew());
            // } catch (FamilleException fe) {
            // Assert.fail("Erreur non gérée lors du search de FamilleContribuable --> " + fe.toString());
            // } finally {
            // JadeThread.logClear();
            // }
            // Assert.assertTrue(bSearchNullModelFamilleContribuable);

            // Recherche d'un simpleFamille avec paramètres ok
            // Résultat attendu : nbResult > 0
            try {
                SimpleFamilleSearch simpleFamilleSearch = new SimpleFamilleSearch();
                simpleFamilleSearch.setForIdContribuable("151");
                simpleFamilleSearch = AmalServiceLocator.getFamilleContribuableService().search(simpleFamilleSearch);

                Assert.assertTrue(simpleFamilleSearch.getSize() > 0);
                SimpleFamille simpleFamille = (SimpleFamille) simpleFamilleSearch.getSearchResults()[0];
                Assert.assertFalse(simpleFamille.isNew());
                Assert.assertTrue("151".equals(simpleFamille.getIdContribuable()));
            } catch (FamilleException fe) {
                Assert.fail("Erreur non gérée lors du search de FamilleContribuable --> " + fe.toString());
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bSearchNullModelFamilleContribuable);

        } catch (Exception e) {
            Assert.fail("Erreur générale lors du search de FamilleContribuableSearch --> " + e.toString());
        } finally {
            // doFinally();
        }

    }

    @Ignore
    @Test
    public void testUpdate() {
        try {
            // update d'un modèle null
            // Résultat attendu --> FamilleException
            boolean bUpdateFamilleContribuableNullModel = false;
            try {
                AmalServiceLocator.getFamilleContribuableService().update(null);
            } catch (FamilleException fe) {
                bUpdateFamilleContribuableNullModel = true;
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bUpdateFamilleContribuableNullModel);

            // update d'un modèle incorrect (Date de naissance incorrect)
            // Résultat attendu --> Erreur business
            try {
                FamilleContribuable familleContribuable = new FamilleContribuable();
                familleContribuable = AmalServiceLocator.getFamilleContribuableService().read(
                        FamilleServiceImplTest.ID_FAMILLECONTRIBUABLE_TO_READ);
                String dateNaissanceBefore = familleContribuable.getSimpleFamille().getDateNaissance();
                familleContribuable.getSimpleFamille().setDateNaissance("03011982");
                familleContribuable = AmalServiceLocator.getFamilleContribuableService().update(familleContribuable);

                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

                FamilleContribuable familleContribuableNew = new FamilleContribuable();
                familleContribuableNew = AmalServiceLocator.getFamilleContribuableService().read(
                        FamilleServiceImplTest.ID_FAMILLECONTRIBUABLE_TO_READ);

                Assert.assertTrue(dateNaissanceBefore.equals(familleContribuableNew.getSimpleFamille()
                        .getDateNaissance()));
            } catch (FamilleException fe) {
                Assert.fail("Erreur non gérée lors de l'update de FamilleContribuable --> " + fe.toString());
            } finally {
                JadeThread.logClear();
            }
        } catch (Exception e) {
            Assert.fail("Erreur générale lors de l'update de FamilleContribuable --> " + e.toString());
        } finally {
            // doFinally();
        }
    }
}