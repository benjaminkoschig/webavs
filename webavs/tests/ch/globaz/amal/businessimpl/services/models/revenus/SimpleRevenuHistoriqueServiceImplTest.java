package ch.globaz.amal.businessimpl.services.models.revenus;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistorique;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistoriqueSearch;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

public class SimpleRevenuHistoriqueServiceImplTest {

    @Test
    @Ignore
    public void testCreate() {
        try {
            SimpleRevenuHistorique simpleRevenuHistorique = new SimpleRevenuHistorique();

            // Cr�ation d'un revenu avec mod�le null
            // R�sultat attendu : Exception
            boolean bCreateNull = false;
            try {
                simpleRevenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().create(null);
            } catch (Exception e) {
                bCreateNull = true;
            } finally {
                Assert.assertTrue(bCreateNull);
                JadeThread.logClear();
            }

            // Cr�ation d'un revenu avec des donn�es incorrect (FK idRevenu n'existe pas)
            // R�sultat attendu : Exception lev�e dans le checker
            simpleRevenuHistorique = new SimpleRevenuHistorique();
            String idRevenuNotExist = "999999999999";
            simpleRevenuHistorique.setIdRevenu(idRevenuNotExist);
            try {
                simpleRevenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().create(
                        simpleRevenuHistorique);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la cr�ation d'un revenuHistorique avec donn�es incorrectes ==> "
                        + e.toString());
            } finally {
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                JadeThread.logClear();
            }

            // Cr�ation d'un revenu avec des donn�es incorrect (Donn�es manquantes ==> anneeHistorique==null)
            // R�sultat attendu : Exception lev�e dans le checker
            simpleRevenuHistorique = new SimpleRevenuHistorique();
            try {
                simpleRevenuHistorique.setAnneeHistorique(null);
                simpleRevenuHistorique.setCodeActif(true);
                simpleRevenuHistorique.setDateCreation("20111013");
                simpleRevenuHistorique.setIdContribuable("151");
                simpleRevenuHistorique.setIdRevenu("1706");
                simpleRevenuHistorique.setIdRevenuDeterminant("1706");

                simpleRevenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().create(
                        simpleRevenuHistorique);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la cr�ation d'un revenuDeterminant avec donn�es incorrectes (fk idrevenu empty) ==> "
                        + e.toString());
            } finally {
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                JadeThread.logClear();
            }

            // Cr�ation d'un revenu avec des donn�es incorrect (revenu existe d�j� pour cette ann�e l�)
            // R�sultat attendu : Exception lev�e dans le checker
            simpleRevenuHistorique = new SimpleRevenuHistorique();
            try {
                simpleRevenuHistorique.setAnneeHistorique("2011");
                simpleRevenuHistorique.setCodeActif(true);
                simpleRevenuHistorique.setDateCreation("20111013");
                simpleRevenuHistorique.setIdContribuable("151");
                simpleRevenuHistorique.setIdRevenu("1706");
                simpleRevenuHistorique.setIdRevenuDeterminant("1706");

                simpleRevenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().create(
                        simpleRevenuHistorique);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la cr�ation d'un revenuDeterminant avec donn�es incorrectes (revenu already exist) ==> "
                        + e.toString());
            } finally {
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                JadeThread.logClear();
            }

            // Cr�ation d'un revenu avec des donn�es correctes
            // R�sultat attendu : isNew == false; ==> RevenuDeterminant OK
            simpleRevenuHistorique = new SimpleRevenuHistorique();
            try {
                simpleRevenuHistorique.setAnneeHistorique("2019");
                simpleRevenuHistorique.setCodeActif(true);
                simpleRevenuHistorique.setDateCreation("20111013");
                simpleRevenuHistorique.setIdContribuable("151");
                simpleRevenuHistorique.setIdRevenu("1706");
                simpleRevenuHistorique.setIdRevenuDeterminant("1706");
                simpleRevenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().create(
                        simpleRevenuHistorique);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la cr�ation d'un revenuDeterminant avec donn�es correctes ==> "
                        + e.toString());
            } finally {
                Assert.assertFalse(simpleRevenuHistorique.isNew());
                JadeThread.logClear();
                Assert.assertNotNull(simpleRevenuHistorique.getIdRevenuDeterminant());
                Assert.assertNotNull(simpleRevenuHistorique.getId());
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
            SimpleRevenuHistorique simpleRevenuDeterminant = new SimpleRevenuHistorique();

            // Effacement d'un mod�le null
            // R�sultat attendu : Exception
            boolean bDeleteNull = false;
            try {
                simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().delete(null);
            } catch (RevenuException re) {
                bDeleteNull = true;
            } catch (Exception e) {
                Assert.fail("Exception lev�e lors du delete NULL d'un revenuDeterminant ==> " + e.toString());
            } finally {
                Assert.assertTrue(bDeleteNull);
                JadeThread.logClear();
            }

            // Effacement d'un revenu correct (La suppression d'un revenu historique est interdite)
            // R�sultat attendu : Exception
            simpleRevenuDeterminant = new SimpleRevenuHistorique();
            simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().read("100");
            try {
                simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().delete(
                        simpleRevenuDeterminant);
            } catch (Exception e) {
                Assert.fail("Exception non g�r�e lors du delete d'un revenuHistorique ==> " + e.toString());
            }
            Assert.assertFalse(simpleRevenuDeterminant.isNew());
            Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
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
            SimpleRevenuHistorique simpleRevenuHistorique = new SimpleRevenuHistorique();
            String idRevenuHistoriqueKo = "-1";

            // Lecture d'un revenu qui n'existe pas
            // R�sultat attendu : isNew == true et tout les champs � NULL
            try {
                simpleRevenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().read(
                        idRevenuHistoriqueKo);
            } catch (Exception e) {
                Assert.fail("Erreur lecture RevenuHistorique qui n'existe pas ==> " + e.toString());
            } finally {
                Assert.assertTrue(simpleRevenuHistorique.isNew());
                Assert.assertEquals(idRevenuHistoriqueKo, simpleRevenuHistorique.getIdRevenuHistorique());
                Assert.assertEquals(idRevenuHistoriqueKo, simpleRevenuHistorique.getId());
                Assert.assertNull(simpleRevenuHistorique.getAnneeHistorique());
                Assert.assertNull(simpleRevenuHistorique.getDateCreation());
                Assert.assertNull(simpleRevenuHistorique.getIdContribuable());
                Assert.assertNull(simpleRevenuHistorique.getIdRevenu());
                Assert.assertNull(simpleRevenuHistorique.getIdRevenuDeterminant());
                Assert.assertNull(simpleRevenuHistorique.getCodeActif());
            }

            // Lecture d'un revenu qui existe
            // R�sultat attendu : isNew == true et tout les champs � NULL
            simpleRevenuHistorique = new SimpleRevenuHistorique();
            String idRevenuHistoriqueOk = "100";
            try {
                simpleRevenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().read(
                        idRevenuHistoriqueOk);
            } catch (Exception e) {
                Assert.fail("Erreur lecture RevenuHistorique existant ==> " + e.toString());
            } finally {
                Assert.assertFalse(simpleRevenuHistorique.isNew());
                Assert.assertEquals(idRevenuHistoriqueOk, simpleRevenuHistorique.getIdRevenuHistorique());
                Assert.assertEquals(idRevenuHistoriqueOk, simpleRevenuHistorique.getId());
                Assert.assertNotNull(simpleRevenuHistorique.getAnneeHistorique());
                Assert.assertNotNull(simpleRevenuHistorique.getDateCreation());
                Assert.assertNotNull(simpleRevenuHistorique.getIdContribuable());
                Assert.assertNotNull(simpleRevenuHistorique.getIdRevenu());
                Assert.assertNotNull(simpleRevenuHistorique.getIdRevenuDeterminant());
                Assert.assertNotNull(simpleRevenuHistorique.getCodeActif());
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
            SimpleRevenuHistoriqueSearch simpleRevenuHistoriqueSearch = new SimpleRevenuHistoriqueSearch();
            // Recherche d'un revenu sans crit�res de recherche
            // R�sultat attendu : Nombre de r�sultat >0
            simpleRevenuHistoriqueSearch = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().search(
                    simpleRevenuHistoriqueSearch);
            int nbResult = simpleRevenuHistoriqueSearch.getSize();
            Assert.assertTrue(nbResult >= 1);

            // Recherche d'un revenu avec crit�res de recherche qui n'existe pas
            // R�sultat attendu : Nombre de r�sultat == 0
            simpleRevenuHistoriqueSearch = new SimpleRevenuHistoriqueSearch();
            simpleRevenuHistoriqueSearch.setForIdRevenuHistorique("-1");
            simpleRevenuHistoriqueSearch = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().search(
                    simpleRevenuHistoriqueSearch);
            nbResult = simpleRevenuHistoriqueSearch.getSize();
            Assert.assertTrue(nbResult <= 0);

            // Recherche d'un revenu avec crit�res de recherche qui existe
            // R�sultat attendu : Nombre de r�sultat == 1
            simpleRevenuHistoriqueSearch = new SimpleRevenuHistoriqueSearch();
            String idRevenuDeterminantToSearch = "100";
            simpleRevenuHistoriqueSearch.setForIdRevenuHistorique(idRevenuDeterminantToSearch);
            simpleRevenuHistoriqueSearch = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().search(
                    simpleRevenuHistoriqueSearch);
            nbResult = simpleRevenuHistoriqueSearch.getSize();
            Assert.assertTrue(nbResult == 1);
            try {
                SimpleRevenuHistorique simpleRevenuHistorique = (SimpleRevenuHistorique) simpleRevenuHistoriqueSearch
                        .getSearchResults()[0];
                Assert.assertFalse(simpleRevenuHistorique.isNew());
                Assert.assertEquals(idRevenuDeterminantToSearch, simpleRevenuHistorique.getIdRevenuHistorique());
                Assert.assertEquals(idRevenuDeterminantToSearch, simpleRevenuHistorique.getId());
                Assert.assertNotNull(simpleRevenuHistorique.getAnneeHistorique());
                Assert.assertNotNull(simpleRevenuHistorique.getDateCreation());
                Assert.assertNotNull(simpleRevenuHistorique.getIdContribuable());
                Assert.assertNotNull(simpleRevenuHistorique.getIdRevenu());
                Assert.assertNotNull(simpleRevenuHistorique.getIdRevenuDeterminant());
                Assert.assertNotNull(simpleRevenuHistorique.getCodeActif());
            } catch (Exception e) {
                Assert.fail("Erreur lors de la r�cup�ration du r�sultat de recherche de simpleRevenuHistorique ! ==> "
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
            SimpleRevenuHistorique simpleRevenuHistorique = new SimpleRevenuHistorique();

            // Update d'un revenu qui n'existe pas (isNew == true)
            // R�sultat attendu : Exception
            boolean bUpdateNew = false;
            try {
                simpleRevenuHistorique.setDateCreation("20150911");
                simpleRevenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().update(
                        simpleRevenuHistorique);
            } catch (Exception e) {
                bUpdateNew = true;
            } finally {
                Assert.assertTrue(bUpdateNew);
                JadeThread.logClear();
            }

            // Update d'un revenu qui existe avec des donn�es incorrectes (date invalide)
            // R�sultat attendu : Mod�le mis � jour correctement
            String idRevenuHistoriqueToUpdate = "1706";
            simpleRevenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().read(
                    idRevenuHistoriqueToUpdate);
            String spy = simpleRevenuHistorique.getSpy();
            try {
                simpleRevenuHistorique.setDateCreation("20150911");
                simpleRevenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().update(
                        simpleRevenuHistorique);
            } catch (Exception e) {
                Assert.fail("Erreur non g�r�e lors de l'update d'un revenu avec donn�es correctes==> " + e.toString());
            } finally {
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertEquals(idRevenuHistoriqueToUpdate, simpleRevenuHistorique.getIdRevenuHistorique());
                JadeThread.logClear();
            }

            // Update d'un revenu qui existe avec des donn�es correctes
            // R�sultat attendu : Mod�le mis � jour correctement
            idRevenuHistoriqueToUpdate = "1706";
            simpleRevenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().read(
                    idRevenuHistoriqueToUpdate);
            spy = simpleRevenuHistorique.getSpy();
            try {
                simpleRevenuHistorique.setDateCreation("11.09.2015");
                simpleRevenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().update(
                        simpleRevenuHistorique);
            } catch (Exception e) {
                Assert.fail("Erreur non g�r�e lors de l'update d'un revenu avec donn�es correctes==> " + e.toString());
            } finally {
                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertEquals(idRevenuHistoriqueToUpdate, simpleRevenuHistorique.getIdRevenuHistorique());
                Assert.assertEquals("11.09.2015", simpleRevenuHistorique.getDateCreation());
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
