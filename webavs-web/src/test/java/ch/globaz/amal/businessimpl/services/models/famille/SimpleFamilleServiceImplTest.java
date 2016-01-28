package ch.globaz.amal.businessimpl.services.models.famille;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

public class SimpleFamilleServiceImplTest {
    private static final String ID_FAMILLE_TO_READ = "151100309";

    public void showContent(SimpleFamille simpleFamille) {
        System.out.println("Id contribuable 	: " + simpleFamille.getIdContribuable());
        System.out.println("Id famille 		: " + simpleFamille.getIdFamille());
        System.out.println("Nom, Prénom 		: " + simpleFamille.getNomPrenom());
        System.out.println("Date de naissance 	: " + simpleFamille.getDateNaissance());
        System.out.println("IsContribuable		: " + Boolean.toString(simpleFamille.getIsContribuable()));
        System.out.println("\n");
    }

    @Test
    @Ignore
    public void testCreate() {
        try {
            SimpleFamille simpleFamille = new SimpleFamille();

            // Création d'un membre famille avec modèle null
            // Résultat attendu : Exception
            boolean bCreateNull = false;
            try {
                simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().create(null);
            } catch (Exception e) {
                bCreateNull = true;
            }
            Assert.assertTrue(bCreateNull);

            // Création d'un membre famille avec de mauvaises données (sans idContribuable)
            // Résultat attendu : Exception
            simpleFamille = new SimpleFamille();
            simpleFamille.setDateNaissance("03.01.1982");
            simpleFamille.setIsContribuable(true);
            simpleFamille.setNomPrenom("Alain Deloin");
            simpleFamille.setPereMereEnfant(IAMCodeSysteme.CS_TYPE_PERE);
            simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().create(simpleFamille);
            Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            Assert.assertTrue(simpleFamille.isNew());
            JadeThread.logClear();

            // Création d'un membre famille avec de mauvaises données (sans nom et prénom)
            // Résultat attendu : Exception
            simpleFamille = new SimpleFamille();
            simpleFamille.setIdContribuable("151");
            simpleFamille.setDateNaissance("03.01.1982");
            simpleFamille.setIsContribuable(true);
            simpleFamille.setPereMereEnfant(IAMCodeSysteme.CS_TYPE_PERE);
            simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().create(simpleFamille);
            Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            Assert.assertTrue(simpleFamille.isNew());
            JadeThread.logClear();

            // Création d'un membre famille avec de mauvaises données (sans isContribuable)
            // Résultat attendu : Exception
            simpleFamille = new SimpleFamille();
            simpleFamille.setIdContribuable("151");
            simpleFamille.setNomPrenom("Alain Deloin");
            simpleFamille.setDateNaissance("03.01.1982");
            simpleFamille.setPereMereEnfant(IAMCodeSysteme.CS_TYPE_PERE);
            simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().create(simpleFamille);
            Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            Assert.assertTrue(simpleFamille.isNew());
            JadeThread.logClear();

            // Création d'un membre famille avec de mauvaises données (sans date de naissance)
            // Résultat attendu : Exception
            simpleFamille = new SimpleFamille();
            simpleFamille.setIdContribuable("151");
            simpleFamille.setNomPrenom("Alain Deloin");
            simpleFamille.setIsContribuable(true);
            simpleFamille.setPereMereEnfant(IAMCodeSysteme.CS_TYPE_PERE);
            simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().create(simpleFamille);
            Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            Assert.assertTrue(simpleFamille.isNew());
            JadeThread.logClear();

            // Création d'un membre famille avec de mauvaises données (sans pereMereEnfant)
            // Résultat attendu : Exception
            simpleFamille = new SimpleFamille();
            simpleFamille.setIdContribuable("151");
            simpleFamille.setNomPrenom("Alain Deloin");
            simpleFamille.setIsContribuable(true);
            simpleFamille.setDateNaissance("19450301");
            simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().create(simpleFamille);
            Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            Assert.assertTrue(simpleFamille.isNew());
            JadeThread.logClear();

            // Création d'un membre famille avec de mauvaises données (date de naissance incorrecte)
            // Résultat attendu : Exception
            simpleFamille = new SimpleFamille();
            simpleFamille.setIdContribuable("151");
            simpleFamille.setNomPrenom("Alain Deloin");
            simpleFamille.setIsContribuable(true);
            simpleFamille.setDateNaissance("19450301");
            simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().create(simpleFamille);
            Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            Assert.assertTrue(simpleFamille.isNew());
            JadeThread.logClear();

            // Création d'un membre famille avec des données correctes
            // Résultat attendu : isNew == false;
            simpleFamille = new SimpleFamille();
            simpleFamille.setIdContribuable("151");
            simpleFamille.setNomPrenom("Alain Deloin");
            simpleFamille.setDateNaissance("03.04.1945");
            simpleFamille.setIsContribuable(true);
            simpleFamille.setPereMereEnfant(IAMCodeSysteme.CS_TYPE_PERE);
            simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().create(simpleFamille);
            Assert.assertFalse(simpleFamille.isNew());
            JadeThread.logClear();

            System.out.println("********* Create SimpleFamille ***********");
            showContent(simpleFamille);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    @Test
    @Ignore
    public void testDelete() {
        try {
            SimpleFamille simpleFamille = new SimpleFamille();
            // Effacement d'un modèle null
            // Résultat attendu : Exception
            boolean bDeleteNull = false;
            try {
                simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().delete(null);
            } catch (Exception e) {
                bDeleteNull = true;
            }
            Assert.assertTrue(bDeleteNull);
            JadeThread.logClear();

            // Effacement d'un membre famille nouveau (isNew == true)
            // Résultat attendu : Exception
            boolean bDeleteNew = false;
            try {
                simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().delete(simpleFamille);
            } catch (Exception e) {
                bDeleteNew = true;
            }
            Assert.assertTrue(bDeleteNew);
            JadeThread.logClear();

            // Effacement d'un membre qui possède un/des subside(s)
            // Résultat attendu : JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == true
            simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
                    SimpleFamilleServiceImplTest.ID_FAMILLE_TO_READ);
            try {
                AmalImplServiceLocator.getSimpleFamilleService().delete(simpleFamille);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée avant checker : " + ex.toString());
            }
            Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

            // Effacement d'un membre famille existant qui ne possède pas de subside (isNew == false)
            // Résultat attendu : Delete ok
            boolean bDeleteOk = true;
            try {
                simpleFamille = new SimpleFamille();
                simpleFamille.setIdContribuable("151");
                simpleFamille.setNomPrenom("Alain Deloin");
                simpleFamille.setDateNaissance("03.04.1945");
                simpleFamille.setPereMereEnfant(IAMCodeSysteme.CS_TYPE_PERE);
                simpleFamille.setIsContribuable(true);
                simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().create(simpleFamille);
                simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().delete(simpleFamille);
                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertTrue(simpleFamille.isNew());
            } catch (Exception e) {
                bDeleteOk = false;
            }
            Assert.assertTrue(bDeleteOk);
            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    @Test
    @Ignore
    public void testRead() {
        try {
            SimpleFamille simpleFamille = new SimpleFamille();
            // Lecture d'un membre famille qui n'existe pas
            // Résultat attendu : isNew == true
            simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().read("-1");
            Assert.assertTrue(simpleFamille.isNew());
            JadeThread.logClear();

            // Lecture d'un membre famille qui existe
            // Résultat attendu : isNew == false
            simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
                    SimpleFamilleServiceImplTest.ID_FAMILLE_TO_READ);
            Assert.assertFalse(simpleFamille.isNew());
            JadeThread.logClear();
            System.out.println("********* Read SimpleFamille idFamille : 32894 ***********");
            showContent(simpleFamille);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    @Test
    @Ignore
    public void testSearch() {
        try {
            SimpleFamilleSearch simpleFamilleSearch = new SimpleFamilleSearch();
            // Recherche d'un membre famille sans critères de recherche
            // Résultat attendu : Nombre de résultat >0
            simpleFamilleSearch = AmalImplServiceLocator.getSimpleFamilleService().search(simpleFamilleSearch);
            int nbResult = simpleFamilleSearch.getSize();
            Assert.assertEquals(true, nbResult >= 1);
            JadeThread.logClear();

            // Recherche d'un membre famille avec critères de recherche qui n'existe pas
            // Résultat attendu : Nombre de résultat == 0
            simpleFamilleSearch = new SimpleFamilleSearch();
            simpleFamilleSearch.setForIdContribuable("-1");
            simpleFamilleSearch.setForIdFamille("-1");
            simpleFamilleSearch = AmalImplServiceLocator.getSimpleFamilleService().search(simpleFamilleSearch);
            nbResult = simpleFamilleSearch.getSize();
            Assert.assertEquals(true, nbResult == 0);
            JadeThread.logClear();

            // Recherche d'un membre famille avec critères de recherche qui existe
            // Résultat attendu : Nombre de résultat == 1
            simpleFamilleSearch = new SimpleFamilleSearch();
            simpleFamilleSearch.setForIdContribuable("151");
            simpleFamilleSearch.setForIdFamille(SimpleFamilleServiceImplTest.ID_FAMILLE_TO_READ);
            simpleFamilleSearch = AmalImplServiceLocator.getSimpleFamilleService().search(simpleFamilleSearch);
            nbResult = simpleFamilleSearch.getSize();
            Assert.assertEquals(true, nbResult == 1);
            JadeThread.logClear();

            System.out.println("********* Search SimpleFamille idContribuable : 151 & idFamille : 32894 ***********");
            try {
                showContent((SimpleFamille) simpleFamilleSearch.getSearchResults()[0]);
            } catch (Exception e) {
                Assert.fail("Erreur ! Search ok mais lecture item 0 impossible ! ==> " + e.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    @Test
    @Ignore
    public void testupdate() {
        try {
            SimpleFamille simpleFamille = new SimpleFamille();
            // Update d'un membre famille qui n'existe pas (isNew == true)
            // Résultat attendu : Exception
            boolean bUpdateNew = false;
            try {
                simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().update(simpleFamille);
            } catch (Exception e) {
                bUpdateNew = true;
            }
            Assert.assertTrue(bUpdateNew);
            JadeThread.logClear();

            // Update d'un membre famille existant avec de mauvaises données
            // Résultat attendu : JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == true
            try {
                simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
                        SimpleFamilleServiceImplTest.ID_FAMILLE_TO_READ);
                simpleFamille.setNomPrenom("");
                simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().update(simpleFamille);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée avant/dans le checker : " + ex.toString());
            }
            Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

            // Update d'un membre famille qui existe avec des données correctes
            // Résultat attendu : Modèle mis à jour correctement
            try {
                simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
                        SimpleFamilleServiceImplTest.ID_FAMILLE_TO_READ);
                simpleFamille.setNomPrenom("NOUVEAU NOM PRENOM");
                simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().update(simpleFamille);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée avant/dans le checker : " + ex.toString());
            }
            Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

            System.out.println("********* Updated SimpleFamille idFamille : 32894 ***********");
            showContent(simpleFamille);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }
}
