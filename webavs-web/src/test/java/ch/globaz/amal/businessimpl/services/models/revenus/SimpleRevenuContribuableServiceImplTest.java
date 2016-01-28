package ch.globaz.amal.businessimpl.services.models.revenus;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuContribuable;
import ch.globaz.amal.business.models.revenu.SimpleRevenuContribuableSearch;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author CBU
 * 
 */
public class SimpleRevenuContribuableServiceImplTest /* extends AMTestCase */{

    public SimpleRevenuContribuableServiceImplTest(String name) {
        // super(name);
    }

    public void testCreate() {
        try {
            SimpleRevenuContribuable simpleRevenuContribuable = new SimpleRevenuContribuable();

            // Création d'un revenu avec modèle null
            // Résultat attendu : Exception
            boolean bCreateNull = false;
            try {
                simpleRevenuContribuable = AmalImplServiceLocator.getSimpleRevenuContribuableService().create(null);
            } catch (Exception e) {
                bCreateNull = true;
            } finally {
                Assert.assertTrue(bCreateNull);
                JadeThread.logClear();
            }

            // Création d'un revenu avec des données incorrect (FK idRevenu n'existe pas)
            // Résultat attendu : Exception levée dans le checker
            simpleRevenuContribuable = new SimpleRevenuContribuable();
            String idRevenuContribuableNotExist = "999999999999";
            simpleRevenuContribuable.setIdRevenu(idRevenuContribuableNotExist);
            try {
                simpleRevenuContribuable = AmalImplServiceLocator.getSimpleRevenuContribuableService().create(
                        simpleRevenuContribuable);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la création d'un revenuContribuable avec données incorrectes ==> "
                        + e.toString());
            } finally {
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                JadeThread.logClear();
            }

            // Création d'un revenu avec des données incorrect (FK idRevenu vide)
            // Résultat attendu : Exception levée dans le checker
            simpleRevenuContribuable = new SimpleRevenuContribuable();
            try {
                simpleRevenuContribuable = AmalImplServiceLocator.getSimpleRevenuContribuableService().create(
                        simpleRevenuContribuable);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la création d'un revenuContribuable avec données incorrectes (fk idrevenu empty) ==> "
                        + e.toString());
            } finally {
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                JadeThread.logClear();
            }

            // Création d'un revenu avec des données correctes
            // Résultat attendu : isNew == false; ==> RevenuContribuable OK
            simpleRevenuContribuable = new SimpleRevenuContribuable();
            simpleRevenuContribuable.setIdRevenu("1");
            try {
                simpleRevenuContribuable = AmalImplServiceLocator.getSimpleRevenuContribuableService().create(
                        simpleRevenuContribuable);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la création d'un revenuContribuable avec données correctes ==> "
                        + e.toString());
            } finally {
                Assert.assertFalse(simpleRevenuContribuable.isNew());
                JadeThread.logClear();
                Assert.assertNotNull(simpleRevenuContribuable.getIdRevenuContribuable());
                Assert.assertNotNull(simpleRevenuContribuable.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testDelete() {
        try {
            SimpleRevenuContribuable simpleRevenuContribuable = new SimpleRevenuContribuable();

            // Effacement d'un modèle null
            // Résultat attendu : Exception
            boolean bDeleteNull = false;
            try {
                simpleRevenuContribuable = AmalImplServiceLocator.getSimpleRevenuContribuableService().delete(null);
            } catch (RevenuException re) {
                bDeleteNull = true;
            } catch (Exception e) {
                Assert.fail("Exception levée lors du delete NULL d'un revenuContribuable ==> " + e.toString());
            } finally {
                Assert.assertTrue(bDeleteNull);
                JadeThread.logClear();
            }

            // Effacement d'un revenu (isNew == true)
            // Résultat attendu : Exception
            boolean bDeleteNew = false;
            simpleRevenuContribuable = new SimpleRevenuContribuable();
            try {
                simpleRevenuContribuable = AmalImplServiceLocator.getSimpleRevenuContribuableService().delete(
                        simpleRevenuContribuable);
            } catch (JadePersistenceException re) {
                bDeleteNew = true;
            } catch (Exception e) {
                Assert.fail("Exception levée lors du delete NEW d'un revenuContribuable ==> " + e.toString());
            } finally {
                Assert.assertTrue(bDeleteNew);
                JadeThread.logClear();
            }

            // Effacement d'un revenu existant
            // Résultat attendu : Effacement ok (codeActif == false)
            simpleRevenuContribuable = new SimpleRevenuContribuable();
            simpleRevenuContribuable = AmalImplServiceLocator.getSimpleRevenuContribuableService().read("1");
            boolean bDeleteOk = true;
            try {
                simpleRevenuContribuable = AmalImplServiceLocator.getSimpleRevenuContribuableService().delete(
                        simpleRevenuContribuable);
            } catch (Exception e) {
                bDeleteOk = false;
            }
            Assert.assertTrue(bDeleteOk);
            Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testRead() {
        try {
            SimpleRevenuContribuable simpleRevenuContribuable = new SimpleRevenuContribuable();
            String idRevenuContribuableKo = "-1";
            // Lecture d'un revenu qui n'existe pas
            // Résultat attendu : isNew == true et tout les champs à NULL
            try {
                simpleRevenuContribuable = AmalImplServiceLocator.getSimpleRevenuContribuableService().read(
                        idRevenuContribuableKo);
            } catch (Exception e) {
                Assert.fail("Erreur lecture Revenu qui n'existe pas ==> " + e.toString());
            } finally {
                Assert.assertTrue(simpleRevenuContribuable.isNew());
                Assert.assertEquals(idRevenuContribuableKo, simpleRevenuContribuable.getIdRevenuContribuable());
                Assert.assertEquals(idRevenuContribuableKo, simpleRevenuContribuable.getId());
                Assert.assertNull(simpleRevenuContribuable.getAllocationFamiliale());
                Assert.assertNull(simpleRevenuContribuable.getDeducAppEtu());
                Assert.assertNull(simpleRevenuContribuable.getExcDepSuccNp());
                Assert.assertNull(simpleRevenuContribuable.getExcedDepPropImmoComm());
                Assert.assertNull(simpleRevenuContribuable.getExcedDepPropImmoPriv());
                Assert.assertNull(simpleRevenuContribuable.getFortuneImposable());
                Assert.assertNull(simpleRevenuContribuable.getFortuneTaux());
                Assert.assertNull(simpleRevenuContribuable.getIndemniteImposable());
                Assert.assertNull(simpleRevenuContribuable.getInteretsPassifsCalcul());
                Assert.assertNull(simpleRevenuContribuable.getInteretsPassifsComm());
                Assert.assertNull(simpleRevenuContribuable.getInteretsPassifsPrive());
                Assert.assertNull(simpleRevenuContribuable.getPersChargeEnf());
                Assert.assertNull(simpleRevenuContribuable.getPerteActAccInd());
                Assert.assertNull(simpleRevenuContribuable.getPerteActAgricole());
                Assert.assertNull(simpleRevenuContribuable.getPerteActIndep());
                Assert.assertNull(simpleRevenuContribuable.getPerteCommercial());
                Assert.assertNull(simpleRevenuContribuable.getPerteCommercial());
                Assert.assertNull(simpleRevenuContribuable.getPerteExercicesComm());
                Assert.assertNull(simpleRevenuContribuable.getPerteLiquidation());
                Assert.assertNull(simpleRevenuContribuable.getPerteSociete());
                Assert.assertNull(simpleRevenuContribuable.getRendFortImmobComm());
                Assert.assertNull(simpleRevenuContribuable.getRendFortImmobPrive());
                Assert.assertNull(simpleRevenuContribuable.getRevenuImposable());
                Assert.assertNull(simpleRevenuContribuable.getRevenuNetEmploi());
                Assert.assertNull(simpleRevenuContribuable.getRevenuNetEpouse());
                Assert.assertNull(simpleRevenuContribuable.getRevenuTaux());
                Assert.assertNull(simpleRevenuContribuable.getTotalRevenusNets());
            }

            // Lecture d'un revenu qui existe
            // Résultat attendu : isNew == true et tout les champs à NULL
            simpleRevenuContribuable = new SimpleRevenuContribuable();
            String idRevenuContribuableOk = "1";
            try {
                simpleRevenuContribuable = AmalImplServiceLocator.getSimpleRevenuContribuableService().read(
                        idRevenuContribuableOk);
            } catch (Exception e) {
                Assert.fail("Erreur lecture Revenu existant ==> " + e.toString());
            } finally {
                Assert.assertFalse(simpleRevenuContribuable.isNew());
                Assert.assertEquals(idRevenuContribuableOk, simpleRevenuContribuable.getIdRevenuContribuable());
                Assert.assertEquals(idRevenuContribuableOk, simpleRevenuContribuable.getId());
                Assert.assertNotNull(simpleRevenuContribuable.getAllocationFamiliale());
                Assert.assertNotNull(simpleRevenuContribuable.getDeducAppEtu());
                Assert.assertNotNull(simpleRevenuContribuable.getExcDepSuccNp());
                Assert.assertNotNull(simpleRevenuContribuable.getExcedDepPropImmoComm());
                Assert.assertNotNull(simpleRevenuContribuable.getExcedDepPropImmoPriv());
                Assert.assertNotNull(simpleRevenuContribuable.getFortuneImposable());
                Assert.assertNotNull(simpleRevenuContribuable.getFortuneTaux());
                Assert.assertNotNull(simpleRevenuContribuable.getIndemniteImposable());
                Assert.assertNotNull(simpleRevenuContribuable.getInteretsPassifsComm());
                Assert.assertNotNull(simpleRevenuContribuable.getInteretsPassifsPrive());
                Assert.assertNotNull(simpleRevenuContribuable.getPersChargeEnf());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteActAccInd());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteActAgricole());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteActIndep());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteCommercial());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteCommercial());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteExercicesComm());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteLiquidation());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteSociete());
                Assert.assertNotNull(simpleRevenuContribuable.getRendFortImmobComm());
                Assert.assertNotNull(simpleRevenuContribuable.getRendFortImmobPrive());
                Assert.assertNotNull(simpleRevenuContribuable.getRevenuImposable());
                Assert.assertNotNull(simpleRevenuContribuable.getRevenuNetEmploi());
                Assert.assertNotNull(simpleRevenuContribuable.getRevenuNetEpouse());
                Assert.assertNotNull(simpleRevenuContribuable.getRevenuTaux());
                Assert.assertNotNull(simpleRevenuContribuable.getTotalRevenusNets());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testSearch() {
        try {
            SimpleRevenuContribuableSearch simpleRevenuContribuableSearch = new SimpleRevenuContribuableSearch();
            // Recherche d'un revenu sans critères de recherche
            // Résultat attendu : Nombre de résultat >0
            simpleRevenuContribuableSearch = AmalImplServiceLocator.getSimpleRevenuContribuableService().search(
                    simpleRevenuContribuableSearch);
            int nbResult = simpleRevenuContribuableSearch.getSize();
            Assert.assertTrue(nbResult >= 1);

            // Recherche d'un revenu avec critères de recherche qui n'existe pas
            // Résultat attendu : Nombre de résultat == 0
            simpleRevenuContribuableSearch = new SimpleRevenuContribuableSearch();
            simpleRevenuContribuableSearch.setForIdRevenuContribuable("-1");
            simpleRevenuContribuableSearch = AmalImplServiceLocator.getSimpleRevenuContribuableService().search(
                    simpleRevenuContribuableSearch);
            nbResult = simpleRevenuContribuableSearch.getSize();
            Assert.assertTrue(nbResult <= 0);

            // Recherche d'un revenu avec critères de recherche qui existe
            // Résultat attendu : Nombre de résultat == 1
            simpleRevenuContribuableSearch = new SimpleRevenuContribuableSearch();
            String idRevenuContribuableToSearch = "1";
            simpleRevenuContribuableSearch.setForIdRevenuContribuable(idRevenuContribuableToSearch);
            simpleRevenuContribuableSearch = AmalImplServiceLocator.getSimpleRevenuContribuableService().search(
                    simpleRevenuContribuableSearch);
            nbResult = simpleRevenuContribuableSearch.getSize();
            Assert.assertTrue(nbResult == 1);
            try {
                SimpleRevenuContribuable simpleRevenuContribuable = (SimpleRevenuContribuable) simpleRevenuContribuableSearch
                        .getSearchResults()[0];
                Assert.assertFalse(simpleRevenuContribuable.isNew());
                Assert.assertEquals(idRevenuContribuableToSearch, simpleRevenuContribuable.getIdRevenuContribuable());
                Assert.assertEquals(idRevenuContribuableToSearch, simpleRevenuContribuable.getId());
                Assert.assertNotNull(simpleRevenuContribuable.getAllocationFamiliale());
                Assert.assertNotNull(simpleRevenuContribuable.getDeducAppEtu());
                Assert.assertNotNull(simpleRevenuContribuable.getExcDepSuccNp());
                Assert.assertNotNull(simpleRevenuContribuable.getExcedDepPropImmoComm());
                Assert.assertNotNull(simpleRevenuContribuable.getExcedDepPropImmoPriv());
                Assert.assertNotNull(simpleRevenuContribuable.getFortuneImposable());
                Assert.assertNotNull(simpleRevenuContribuable.getFortuneTaux());
                Assert.assertNotNull(simpleRevenuContribuable.getIndemniteImposable());
                Assert.assertNotNull(simpleRevenuContribuable.getInteretsPassifsComm());
                Assert.assertNotNull(simpleRevenuContribuable.getInteretsPassifsPrive());
                Assert.assertNotNull(simpleRevenuContribuable.getPersChargeEnf());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteActAccInd());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteActAgricole());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteActIndep());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteCommercial());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteCommercial());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteExercicesComm());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteLiquidation());
                Assert.assertNotNull(simpleRevenuContribuable.getPerteSociete());
                Assert.assertNotNull(simpleRevenuContribuable.getRendFortImmobComm());
                Assert.assertNotNull(simpleRevenuContribuable.getRendFortImmobPrive());
                Assert.assertNotNull(simpleRevenuContribuable.getRevenuImposable());
                Assert.assertNotNull(simpleRevenuContribuable.getRevenuNetEmploi());
                Assert.assertNotNull(simpleRevenuContribuable.getRevenuNetEpouse());
                Assert.assertNotNull(simpleRevenuContribuable.getRevenuTaux());
                Assert.assertNotNull(simpleRevenuContribuable.getTotalRevenusNets());
            } catch (Exception e) {
                Assert.fail("Erreur lors de la récupération du résultat de recherche de SimpleRevenuContribuable ! ==> "
                        + e.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testUpdate() {
        try {
            SimpleRevenuContribuable simpleRevenuContribuable = new SimpleRevenuContribuable();

            // Update d'un revenu qui n'existe pas (isNew == true)
            // Résultat attendu : Exception
            boolean bUpdateNew = false;
            try {
                simpleRevenuContribuable.setRevenuTaux("999999");
                simpleRevenuContribuable = AmalImplServiceLocator.getSimpleRevenuContribuableService().update(
                        simpleRevenuContribuable);
            } catch (Exception e) {
                bUpdateNew = true;
            } finally {
                Assert.assertTrue(bUpdateNew);
                JadeThread.logClear();
            }

            // Update d'un revenu qui existe avec des données correctes
            // Résultat attendu : Modèle mis à jour correctement
            String idRevenuContribuableToUpdate = "1";
            simpleRevenuContribuable = AmalImplServiceLocator.getSimpleRevenuContribuableService().read(
                    idRevenuContribuableToUpdate);
            String spy = simpleRevenuContribuable.getSpy();
            try {
                simpleRevenuContribuable.setTotalRevenusNets("99999");
                simpleRevenuContribuable = AmalImplServiceLocator.getSimpleRevenuContribuableService().update(
                        simpleRevenuContribuable);
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors de l'update d'un revenu avec données correctes==> " + e.toString());
            } finally {
                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertEquals(idRevenuContribuableToUpdate, simpleRevenuContribuable.getIdRevenuContribuable());
                Assert.assertEquals("99999", simpleRevenuContribuable.getTotalRevenusNets());
                Assert.assertFalse(spy.equalsIgnoreCase(simpleRevenuContribuable.getSpy()));
                JadeThread.logClear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }
}