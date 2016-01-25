package ch.globaz.amal.businessimpl.services.models.revenus;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenu;
import ch.globaz.amal.business.models.revenu.SimpleRevenuSearch;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author CBU
 * 
 */
public class SimpleRevenuServiceImplTest {

    @Ignore
    @Test
    public void testCreate() {
        try {
            SimpleRevenu simpleRevenu = new SimpleRevenu();

            // Cr�ation d'un revenu avec mod�le null
            // R�sultat attendu : Exception
            boolean bCreateNull = false;
            try {
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().create(null);
            } catch (Exception e) {
                bCreateNull = true;
            } finally {
                Assert.assertTrue(bCreateNull);
                JadeThread.logClear();
            }

            // Cr�ation d'un revenu avec de mauvaises donn�es (date de traitement incorrect)
            // R�sultat attendu : Exception
            simpleRevenu = new SimpleRevenu();
            simpleRevenu.setRevDetUniqueOuiNon(false);
            simpleRevenu.setDateTraitement("12.34.5678");
            simpleRevenu.setIsSourcier(false);
            simpleRevenu.setIdContribuable("151");
            simpleRevenu.setTypeRevenu(IAMCodeSysteme.CS_TYPE_CONTRIBUABLE);
            try {
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().create(simpleRevenu);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la cr�ation d'un revenu avec donn�es incorrectes (date de traitement incorrect) ==> "
                        + e.toString());
            } finally {
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                JadeThread.logClear();
            }

            // Cr�ation d'un revenu avec de mauvaises donn�es (annee taxation aux fraises)
            // R�sultat attendu : Exception
            simpleRevenu = new SimpleRevenu();
            simpleRevenu.setAnneeTaxation("9999");
            simpleRevenu.setRevDetUniqueOuiNon(false);
            simpleRevenu.setIsSourcier(false);
            simpleRevenu.setIdContribuable("151");
            simpleRevenu.setTypeRevenu(IAMCodeSysteme.CS_TYPE_CONTRIBUABLE);
            try {
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().create(simpleRevenu);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la cr�ation d'un revenu avec donn�es incorrectes (ann�e taxation invalide) ==> "
                        + e.toString());
            } finally {
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                JadeThread.logClear();
            }

            // Cr�ation d'un revenu avec de mauvaises donn�es (nbjours > 365)
            // R�sultat attendu : Exception
            simpleRevenu = new SimpleRevenu();
            simpleRevenu.setAnneeTaxation("2011");
            simpleRevenu.setNbJours("800");
            simpleRevenu.setRevDetUniqueOuiNon(false);
            simpleRevenu.setIsSourcier(false);
            simpleRevenu.setIdContribuable("151");
            simpleRevenu.setTypeRevenu(IAMCodeSysteme.CS_TYPE_CONTRIBUABLE);
            try {
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().create(simpleRevenu);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la cr�ation d'un revenu avec donn�es incorrectes (nbJours invalide) ==> "
                        + e.toString());
            } finally {
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                JadeThread.logClear();
            }

            // Cr�ation d'un revenu avec de mauvaises donn�es (nbenfant = -1)
            // R�sultat attendu : Exception
            simpleRevenu = new SimpleRevenu();
            simpleRevenu.setAnneeTaxation("2011");
            simpleRevenu.setNbEnfants("-1");
            simpleRevenu.setRevDetUniqueOuiNon(false);
            simpleRevenu.setIsSourcier(false);
            simpleRevenu.setIdContribuable("151");
            simpleRevenu.setTypeRevenu(IAMCodeSysteme.CS_TYPE_CONTRIBUABLE);
            try {
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().create(simpleRevenu);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la cr�ation d'un revenu avec donn�es incorrectes (nbEnfants invalide) ==> "
                        + e.toString());
            } finally {
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                JadeThread.logClear();
            }

            // Cr�ation d'un revenu avec des donn�es correctes
            // R�sultat attendu : isNew == false; ==> Revenu OK
            simpleRevenu = new SimpleRevenu();
            simpleRevenu.setAnneeTaxation("2011");
            simpleRevenu.setRevDetUniqueOuiNon(false);
            simpleRevenu.setIsSourcier(false);
            simpleRevenu.setIdContribuable("151");
            simpleRevenu.setTypeRevenu(IAMCodeSysteme.CS_TYPE_CONTRIBUABLE);
            try {
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().create(simpleRevenu);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la cr�ation d'un revenu avec donn�es correctes ==> " + e.toString());
            } finally {
                Assert.assertFalse(simpleRevenu.isNew());
                JadeThread.logClear();
                Assert.assertNotNull(simpleRevenu.getIdRevenu());
                Assert.assertNotNull(simpleRevenu.getId());
                Assert.assertEquals(simpleRevenu.getIdRevenu(), simpleRevenu.getId());
                Assert.assertEquals(simpleRevenu.getIdContribuable(), "151");
                Assert.assertEquals(simpleRevenu.getAnneeTaxation(), "2011");
                Assert.assertEquals(simpleRevenu.getRevDetUniqueOuiNon().booleanValue(), false);
                Assert.assertEquals(simpleRevenu.getIsSourcier().booleanValue(), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    @Ignore
    @Test
    public void testDelete() {
        try {
            SimpleRevenu simpleRevenu = new SimpleRevenu();

            // Effacement d'un mod�le null
            // R�sultat attendu : Exception
            boolean bDeleteNull = false;
            try {
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().delete(null);
            } catch (RevenuException re) {
                bDeleteNull = true;
            } catch (Exception e) {
                Assert.fail("Exception lev�e lors du delete NULL d'un revenu ==> " + e.toString());
            } finally {
                Assert.assertTrue(bDeleteNull);
                JadeThread.logClear();
            }

            // Effacement d'un revenu (isNew == true)
            // R�sultat attendu : Exception
            boolean bDeleteNew = false;
            simpleRevenu = new SimpleRevenu();
            try {
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().delete(simpleRevenu);
            } catch (JadePersistenceException jpe) {
                bDeleteNew = true;
            } catch (Exception e) {
                Assert.fail("Exception lev�e lors du delete NEW d'un revenu ==> " + e.toString());
            } finally {
                Assert.assertTrue(bDeleteNew);
                JadeThread.logClear();
            }

            // Effacement d'un revenu existant
            // R�sultat attendu : Effacement ok (codeActif == false)
            simpleRevenu = new SimpleRevenu();
            simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().read("1");
            boolean bDeleteOk = true;
            try {
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().delete(simpleRevenu);
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

    @Ignore
    @Test
    public void testRead() {
        try {
            SimpleRevenu simpleRevenu = new SimpleRevenu();
            // Lecture d'un revenu qui n'existe pas
            // R�sultat attendu : isNew == true et tout les champs � NULL
            try {
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().read("-1");
            } catch (Exception e) {
                Assert.fail("Erreur lecture Revenu qui n'existe pas ==> " + e.toString());
            } finally {
                Assert.assertTrue(simpleRevenu.isNew());
                Assert.assertEquals("-1", simpleRevenu.getIdRevenu());
                Assert.assertEquals("-1", simpleRevenu.getId());
                Assert.assertNull(simpleRevenu.getAnneeTaxation());
                Assert.assertNull(simpleRevenu.getCodeSuspendu());
                Assert.assertNull(simpleRevenu.getDateAvisTaxation());
                Assert.assertNull(simpleRevenu.getDateSaisie());
                Assert.assertNull(simpleRevenu.getDateTraitement());
                Assert.assertNull(simpleRevenu.getEtatCivil());
                Assert.assertNull(simpleRevenu.getIdContribuable());
                Assert.assertNull(simpleRevenu.getNbEnfants());
                Assert.assertNull(simpleRevenu.getNbEnfantSuspens());
                Assert.assertNull(simpleRevenu.getNbJours());
                Assert.assertNull(simpleRevenu.getNoLotAvisTaxation());
                Assert.assertNull(simpleRevenu.getNumeroContribuable());
                Assert.assertNull(simpleRevenu.getProfession());
                Assert.assertNull(simpleRevenu.getRevDetUnique());
                Assert.assertNull(simpleRevenu.getTypeRevenu());
                Assert.assertNull(simpleRevenu.getTypeTaxation());
                Assert.assertNull(simpleRevenu.getSpy());
                Assert.assertNull(simpleRevenu.getRevDetUniqueOuiNon());
                Assert.assertFalse(simpleRevenu.isSourcier());
            }

            // Lecture d'un revenu qui existe
            // R�sultat attendu : isNew == true et tout les champs � NULL
            simpleRevenu = new SimpleRevenu();
            try {
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().read("1");
            } catch (Exception e) {
                Assert.fail("Erreur lecture Revenu existant ==> " + e.toString());
            } finally {
                Assert.assertFalse(simpleRevenu.isNew());
                Assert.assertEquals("1", simpleRevenu.getIdRevenu());
                Assert.assertEquals("1", simpleRevenu.getId());
                Assert.assertNotNull(simpleRevenu.getAnneeTaxation());
                Assert.assertNotNull(simpleRevenu.getCodeSuspendu());
                Assert.assertNotNull(simpleRevenu.getDateAvisTaxation());
                Assert.assertNotNull(simpleRevenu.getDateSaisie());
                Assert.assertNotNull(simpleRevenu.getDateTraitement());
                Assert.assertNotNull(simpleRevenu.getEtatCivil());
                Assert.assertNotNull(simpleRevenu.getIdContribuable());
                Assert.assertNotNull(simpleRevenu.getNbEnfants());
                Assert.assertNotNull(simpleRevenu.getNbEnfantSuspens());
                Assert.assertNotNull(simpleRevenu.getNbJours());
                Assert.assertNotNull(simpleRevenu.getNoLotAvisTaxation());
                Assert.assertNotNull(simpleRevenu.getNumeroContribuable());
                Assert.assertNotNull(simpleRevenu.getProfession());
                Assert.assertNotNull(simpleRevenu.getRevDetUnique());
                Assert.assertNotNull(simpleRevenu.getTypeRevenu());
                Assert.assertNotNull(simpleRevenu.getTypeTaxation());
                Assert.assertNotNull(simpleRevenu.getSpy());
                Assert.assertNotNull(simpleRevenu.getRevDetUniqueOuiNon());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    @Ignore
    @Test
    public void testSearch() {
        try {
            SimpleRevenuSearch simpleRevenuSearch = new SimpleRevenuSearch();
            // Recherche d'un revenu sans crit�res de recherche
            // R�sultat attendu : Nombre de r�sultat >0
            simpleRevenuSearch = AmalImplServiceLocator.getSimpleRevenuService().search(simpleRevenuSearch);
            int nbResult = simpleRevenuSearch.getSize();
            Assert.assertTrue(nbResult >= 1);

            // Recherche d'un revenu avec crit�res de recherche qui n'existe pas
            // R�sultat attendu : Nombre de r�sultat == 0
            simpleRevenuSearch = new SimpleRevenuSearch();
            simpleRevenuSearch.setForIdContribuable("-1");
            simpleRevenuSearch = AmalImplServiceLocator.getSimpleRevenuService().search(simpleRevenuSearch);
            nbResult = simpleRevenuSearch.getSize();
            Assert.assertTrue(nbResult <= 0);

            // Recherche d'un revenu avec crit�res de recherche qui existe
            // R�sultat attendu : Nombre de r�sultat == 1
            simpleRevenuSearch = new SimpleRevenuSearch();
            simpleRevenuSearch.setForIdRevenu("151");
            simpleRevenuSearch = AmalImplServiceLocator.getSimpleRevenuService().search(simpleRevenuSearch);
            nbResult = simpleRevenuSearch.getSize();
            Assert.assertTrue(nbResult == 1);
            try {
                SimpleRevenu simpleRevenu = (SimpleRevenu) simpleRevenuSearch.getSearchResults()[0];
                Assert.assertFalse(simpleRevenu.isNew());
                Assert.assertNotNull(simpleRevenu.getAnneeTaxation());
                Assert.assertNotNull(simpleRevenu.getCodeSuspendu());
                Assert.assertNotNull(simpleRevenu.getDateAvisTaxation());
                Assert.assertNotNull(simpleRevenu.getDateSaisie());
                Assert.assertNotNull(simpleRevenu.getDateTraitement());
                Assert.assertNotNull(simpleRevenu.getEtatCivil());
                Assert.assertNotNull(simpleRevenu.getIdContribuable());
                Assert.assertNotNull(simpleRevenu.getNbEnfants());
                Assert.assertNotNull(simpleRevenu.getNbEnfantSuspens());
                Assert.assertNotNull(simpleRevenu.getNbJours());
                Assert.assertNotNull(simpleRevenu.getNoLotAvisTaxation());
                Assert.assertNotNull(simpleRevenu.getNumeroContribuable());
                Assert.assertNotNull(simpleRevenu.getProfession());
                Assert.assertNotNull(simpleRevenu.getRevDetUnique());
                Assert.assertNotNull(simpleRevenu.getTypeRevenu());
                Assert.assertNotNull(simpleRevenu.getTypeTaxation());
                Assert.assertNotNull(simpleRevenu.getSpy());
                Assert.assertNotNull(simpleRevenu.getRevDetUniqueOuiNon());
            } catch (Exception e) {
                Assert.fail("Erreur lors de la r�cup�ration du r�sultat de rechercher de SimpleRevenu ! ==> "
                        + e.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    @Ignore
    @Test
    public void testUpdate() {
        try {
            SimpleRevenu simpleRevenu = new SimpleRevenu();

            // Update d'un revenu qui n'existe pas (isNew == true)
            // R�sultat attendu : Exception
            boolean bUpdateNew = false;
            try {
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().update(simpleRevenu);
            } catch (RevenuException re) {
                bUpdateNew = true;
            } catch (Exception e) {
                Assert.fail("Erreur non g�r�e lors de l'update d'un revenu qui n'existe pas ==> " + e.toString());
            } finally {
                Assert.assertTrue(bUpdateNew);
                JadeThread.logClear();
            }

            // Update d'un revenuexistant avec de mauvaises donn�es (date traitement incorrecte)
            // R�sultat attendu : JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == true
            simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().read("1");
            try {
                simpleRevenu.setDateTraitement("12.34.5678");
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().update(simpleRevenu);
            } catch (Exception e) {
                Assert.fail("Erreur non g�r�e lors de l'update d'un revenu avec mauvaises donn�es ==> " + e.toString());
            } finally {
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                JadeThread.logClear();
            }

            // Update d'un revenu qui existe avec des donn�es correctes
            // R�sultat attendu : Mod�le mis � jour correctement
            simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().read("1");
            String spy = simpleRevenu.getSpy();
            try {
                simpleRevenu.setAnneeTaxation("2017");
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().update(simpleRevenu);
            } catch (Exception e) {
                Assert.fail("Erreur non g�r�e lors de l'update d'un revenu avec donn�es correctes==> " + e.toString());
            } finally {
                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertEquals("2017", simpleRevenu.getAnneeTaxation());
                Assert.assertFalse(spy.equalsIgnoreCase(simpleRevenu.getSpy()));
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
