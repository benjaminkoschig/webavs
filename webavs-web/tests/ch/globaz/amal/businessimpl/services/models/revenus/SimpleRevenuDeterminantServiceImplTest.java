package ch.globaz.amal.businessimpl.services.models.revenus;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuDeterminant;
import ch.globaz.amal.business.models.revenu.SimpleRevenuDeterminantSearch;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

public class SimpleRevenuDeterminantServiceImplTest {

    @Test
    @Ignore
    public void testCreate() {
        try {
            SimpleRevenuDeterminant simpleRevenuDeterminant = new SimpleRevenuDeterminant();

            // Création d'un revenu avec modèle null
            // Résultat attendu : Exception
            boolean bCreateNull = false;
            try {
                simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuDeterminantService().create(null);
            } catch (Exception e) {
                bCreateNull = true;
            } finally {
                Assert.assertTrue(bCreateNull);
                JadeThread.logClear();
            }

            // // Création d'un revenu avec des données incorrect (FK idRevenuHistorique n'existe pas)
            // // Résultat attendu : Exception levée dans le checker
            // simpleRevenuDeterminant = new SimpleRevenuDeterminant();
            // String idRevenuHistoriqueNotExist = "999999999999";
            // simpleRevenuDeterminant.setIdRevenuHistorique(idRevenuHistoriqueNotExist);
            // try {
            // simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuDeterminantService().create(
            // simpleRevenuDeterminant);
            // } catch (Exception e) {
            // Assert.fail("Erreur lors de la création d'un revenuDeterminant avec données incorrectes ==> "
            // + e.toString());
            // } finally {
            // Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            // JadeThread.logClear();
            // }

            // Création d'un revenu avec des données incorrect (Données manquantes)
            // Résultat attendu : Exception levée dans le checker
            simpleRevenuDeterminant = new SimpleRevenuDeterminant();
            try {
                simpleRevenuDeterminant.setDeductionContribAvecEnfantChargeCalcul("0");
                simpleRevenuDeterminant.setDeductionContribNonCelibSansEnfantChargeCalcul("0");
                simpleRevenuDeterminant.setDeductionSelonNbreEnfantCalcul("0");
                simpleRevenuDeterminant.setExcedentDepensesPropImmoCalcul("0");
                simpleRevenuDeterminant.setExcedentDepensesSuccNonPartageesCalcul("0");
                simpleRevenuDeterminant.setFortuneImposableCalcul("0");
                simpleRevenuDeterminant.setFortuneImposablePercentCalcul("0");
                simpleRevenuDeterminant.setInteretsPassifsCalcul("0");
                simpleRevenuDeterminant.setPartRendementImmobExedantIntPassifsCalcul("0");
                simpleRevenuDeterminant.setPerteLiquidationCalcul("0");
                simpleRevenuDeterminant.setPerteReporteeExercicesCommerciauxCalcul("0");
                simpleRevenuDeterminant.setRendementFortuneImmoCalcul("0");
                simpleRevenuDeterminant.setRevenuImposableCalcul("0");

                simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuDeterminantService().create(
                        simpleRevenuDeterminant);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la création d'un revenuDeterminant avec données incorrectes (fk idrevenu empty) ==> "
                        + e.toString());
            } finally {
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                JadeThread.logClear();
            }

            // Création d'un revenu avec des données correctes
            // Résultat attendu : isNew == false; ==> RevenuDeterminant OK
            simpleRevenuDeterminant = new SimpleRevenuDeterminant();
            try {
                simpleRevenuDeterminant.setIdRevenuHistorique("1706");
                simpleRevenuDeterminant.setDeductionContribAvecEnfantChargeCalcul("0");
                simpleRevenuDeterminant.setDeductionContribNonCelibSansEnfantChargeCalcul("0");
                simpleRevenuDeterminant.setDeductionSelonNbreEnfantCalcul("0");
                simpleRevenuDeterminant.setExcedentDepensesPropImmoCalcul("0");
                simpleRevenuDeterminant.setExcedentDepensesSuccNonPartageesCalcul("0");
                simpleRevenuDeterminant.setFortuneImposableCalcul("0");
                simpleRevenuDeterminant.setFortuneImposablePercentCalcul("0");
                simpleRevenuDeterminant.setInteretsPassifsCalcul("0");
                simpleRevenuDeterminant.setPartRendementImmobExedantIntPassifsCalcul("0");
                simpleRevenuDeterminant.setPerteLiquidationCalcul("0");
                simpleRevenuDeterminant.setPerteReporteeExercicesCommerciauxCalcul("0");
                simpleRevenuDeterminant.setRendementFortuneImmoCalcul("0");
                simpleRevenuDeterminant.setRevenuImposableCalcul("0");
                simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuDeterminantService().create(
                        simpleRevenuDeterminant);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la création d'un revenuDeterminant avec données correctes ==> "
                        + e.toString());
            } finally {
                Assert.assertFalse(simpleRevenuDeterminant.isNew());
                JadeThread.logClear();
                Assert.assertNotNull(simpleRevenuDeterminant.getIdRevenuDeterminant());
                Assert.assertNotNull(simpleRevenuDeterminant.getId());
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
    public void testdelete() {
        try {
            SimpleRevenuDeterminant simpleRevenuDeterminant = new SimpleRevenuDeterminant();

            // Effacement d'un modèle null
            // Résultat attendu : Exception
            boolean bDeleteNull = false;
            try {
                simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuDeterminantService().delete(null);
            } catch (RevenuException re) {
                bDeleteNull = true;
            } catch (Exception e) {
                Assert.fail("Exception levée lors du delete NULL d'un revenuDeterminant ==> " + e.toString());
            } finally {
                Assert.assertTrue(bDeleteNull);
                JadeThread.logClear();
            }

            // Effacement d'un revenu (isNew == true)
            // Résultat attendu : Exception
            boolean bDeleteNew = false;
            simpleRevenuDeterminant = new SimpleRevenuDeterminant();
            try {
                simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuDeterminantService().delete(
                        simpleRevenuDeterminant);
            } catch (JadePersistenceException re) {
                bDeleteNew = true;
            } catch (Exception e) {
                Assert.fail("Exception levée lors du delete NEW d'un revenuDeterminant ==> " + e.toString());
            } finally {
                Assert.assertTrue(bDeleteNew);
                JadeThread.logClear();
            }

            // Effacement d'un revenu existant
            // Résultat attendu : Effacement ok (codeActif == false)
            simpleRevenuDeterminant = new SimpleRevenuDeterminant();
            simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuDeterminantService().read("100");
            boolean bDeleteOk = true;
            try {
                simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuDeterminantService().delete(
                        simpleRevenuDeterminant);
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

    @Test
    @Ignore
    public void testread() {
        try {
            SimpleRevenuDeterminant simpleRevenuDeterminant = new SimpleRevenuDeterminant();
            String idRevenuDeterminantKo = "-1";
            // Lecture d'un revenu qui n'existe pas
            // Résultat attendu : isNew == true et tout les champs à NULL
            try {
                simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuDeterminantService().read(
                        idRevenuDeterminantKo);
            } catch (Exception e) {
                Assert.fail("Erreur lecture Revenu qui n'existe pas ==> " + e.toString());
            } finally {
                Assert.assertTrue(simpleRevenuDeterminant.isNew());
                Assert.assertEquals(idRevenuDeterminantKo, simpleRevenuDeterminant.getIdRevenuDeterminant());
                Assert.assertEquals(idRevenuDeterminantKo, simpleRevenuDeterminant.getId());
                Assert.assertNull(simpleRevenuDeterminant.getDeductionContribAvecEnfantChargeCalcul());
                Assert.assertNull(simpleRevenuDeterminant.getDeductionContribNonCelibSansEnfantChargeCalcul());
                Assert.assertNull(simpleRevenuDeterminant.getDeductionSelonNbreEnfantCalcul());
                Assert.assertNull(simpleRevenuDeterminant.getExcedentDepensesPropImmoCalcul());
                Assert.assertNull(simpleRevenuDeterminant.getExcedentDepensesSuccNonPartageesCalcul());
                Assert.assertNull(simpleRevenuDeterminant.getFortuneImposableCalcul());
                Assert.assertNull(simpleRevenuDeterminant.getFortuneImposablePercentCalcul());
                Assert.assertNull(simpleRevenuDeterminant.getInteretsPassifsCalcul());
                Assert.assertNull(simpleRevenuDeterminant.getPartRendementImmobExedantIntPassifsCalcul());
                Assert.assertNull(simpleRevenuDeterminant.getPerteLiquidationCalcul());
                Assert.assertNull(simpleRevenuDeterminant.getPerteReporteeExercicesCommerciauxCalcul());
                Assert.assertNull(simpleRevenuDeterminant.getRendementFortuneImmoCalcul());
                Assert.assertNull(simpleRevenuDeterminant.getRevenuImposableCalcul());
            }

            // Lecture d'un revenu qui existe
            // Résultat attendu : isNew == true et tout les champs à NULL
            simpleRevenuDeterminant = new SimpleRevenuDeterminant();
            String idRevenuDeterminantOk = "100";
            try {
                simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuDeterminantService().read(
                        idRevenuDeterminantOk);
            } catch (Exception e) {
                Assert.fail("Erreur lecture Revenu existant ==> " + e.toString());
            } finally {
                Assert.assertFalse(simpleRevenuDeterminant.isNew());
                Assert.assertEquals(idRevenuDeterminantOk, simpleRevenuDeterminant.getIdRevenuDeterminant());
                Assert.assertEquals(idRevenuDeterminantOk, simpleRevenuDeterminant.getId());
                Assert.assertNotNull(simpleRevenuDeterminant.getDeductionContribAvecEnfantChargeCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getDeductionContribNonCelibSansEnfantChargeCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getDeductionSelonNbreEnfantCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getExcedentDepensesPropImmoCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getExcedentDepensesSuccNonPartageesCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getFortuneImposableCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getFortuneImposablePercentCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getInteretsPassifsCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getPartRendementImmobExedantIntPassifsCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getPerteLiquidationCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getPerteReporteeExercicesCommerciauxCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getRendementFortuneImmoCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getRevenuImposableCalcul());
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
    public void testsearch() {
        try {
            SimpleRevenuDeterminantSearch simpleRevenuDeterminantSearch = new SimpleRevenuDeterminantSearch();
            // Recherche d'un revenu sans critères de recherche
            // Résultat attendu : Nombre de résultat >0
            simpleRevenuDeterminantSearch = AmalImplServiceLocator.getSimpleRevenuDeterminantService().search(
                    simpleRevenuDeterminantSearch);
            int nbResult = simpleRevenuDeterminantSearch.getSize();
            Assert.assertTrue(nbResult >= 1);

            // Recherche d'un revenu avec critères de recherche qui n'existe pas
            // Résultat attendu : Nombre de résultat == 0
            simpleRevenuDeterminantSearch = new SimpleRevenuDeterminantSearch();
            simpleRevenuDeterminantSearch.setForIdRevenuDeterminant("-1");
            simpleRevenuDeterminantSearch = AmalImplServiceLocator.getSimpleRevenuDeterminantService().search(
                    simpleRevenuDeterminantSearch);
            nbResult = simpleRevenuDeterminantSearch.getSize();
            Assert.assertTrue(nbResult <= 0);

            // Recherche d'un revenu avec critères de recherche qui existe
            // Résultat attendu : Nombre de résultat == 1
            simpleRevenuDeterminantSearch = new SimpleRevenuDeterminantSearch();
            String idRevenuDeterminantToSearch = "100";
            simpleRevenuDeterminantSearch.setForIdRevenuDeterminant(idRevenuDeterminantToSearch);
            simpleRevenuDeterminantSearch = AmalImplServiceLocator.getSimpleRevenuDeterminantService().search(
                    simpleRevenuDeterminantSearch);
            nbResult = simpleRevenuDeterminantSearch.getSize();
            Assert.assertTrue(nbResult == 1);
            try {
                SimpleRevenuDeterminant simpleRevenuDeterminant = (SimpleRevenuDeterminant) simpleRevenuDeterminantSearch
                        .getSearchResults()[0];
                Assert.assertFalse(simpleRevenuDeterminant.isNew());
                Assert.assertEquals(idRevenuDeterminantToSearch, simpleRevenuDeterminant.getIdRevenuDeterminant());
                Assert.assertEquals(idRevenuDeterminantToSearch, simpleRevenuDeterminant.getId());
                Assert.assertNotNull(simpleRevenuDeterminant.getDeductionContribAvecEnfantChargeCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getDeductionContribNonCelibSansEnfantChargeCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getDeductionSelonNbreEnfantCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getExcedentDepensesPropImmoCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getExcedentDepensesSuccNonPartageesCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getFortuneImposableCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getFortuneImposablePercentCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getInteretsPassifsCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getPartRendementImmobExedantIntPassifsCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getPerteLiquidationCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getPerteReporteeExercicesCommerciauxCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getRendementFortuneImmoCalcul());
                Assert.assertNotNull(simpleRevenuDeterminant.getRevenuImposableCalcul());
            } catch (Exception e) {
                Assert.fail("Erreur lors de la récupération du résultat de recherche de simpleRevenuDeterminant ! ==> "
                        + e.toString());
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
            SimpleRevenuDeterminant simpleRevenuDeterminant = new SimpleRevenuDeterminant();

            // Update d'un revenu qui n'existe pas (isNew == true)
            // Résultat attendu : Exception
            boolean bUpdateNew = false;
            try {
                simpleRevenuDeterminant.setRevenuDeterminantCalcul("1234567890");
                simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuDeterminantService().update(
                        simpleRevenuDeterminant);
            } catch (Exception e) {
                bUpdateNew = true;
            } finally {
                Assert.assertTrue(bUpdateNew);
                JadeThread.logClear();
            }

            // Update d'un revenu qui existe avec des données correctes
            // Résultat attendu : Modèle mis à jour correctement
            String idRevenuDeterminantToUpdate = "100";
            simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuDeterminantService().read(
                    idRevenuDeterminantToUpdate);
            String spy = simpleRevenuDeterminant.getSpy();
            try {
                simpleRevenuDeterminant.setRevenuDeterminantCalcul("1234567890");
                simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuDeterminantService().update(
                        simpleRevenuDeterminant);
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors de l'update d'un revenu avec données correctes==> " + e.toString());
            } finally {
                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertEquals(idRevenuDeterminantToUpdate, simpleRevenuDeterminant.getIdRevenuDeterminant());
                Assert.assertEquals("1234567890", simpleRevenuDeterminant.getRevenuDeterminantCalcul());
                Assert.assertFalse(spy.equalsIgnoreCase(simpleRevenuDeterminant.getSpy()));
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
