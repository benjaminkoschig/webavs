package ch.globaz.amal.businessimpl.services.models.revenus;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuSourcier;
import ch.globaz.amal.business.models.revenu.SimpleRevenuSourcierSearch;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author CBU
 * 
 */
public class SimpleRevenuSourcierServiceImplTest {
    private String ID_CONTRIBUABLE_SOURCIER_OK = "100";

    @Test
    @Ignore
    public void testCreate() {
        try {
            SimpleRevenuSourcier simpleRevenuSourcier = new SimpleRevenuSourcier();

            // Cr�ation d'un revenu avec mod�le null
            // R�sultat attendu : Exception
            boolean bCreateNull = false;
            try {
                simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().create(null);
            } catch (Exception e) {
                bCreateNull = true;
            } finally {
                Assert.assertTrue(bCreateNull);
                JadeThread.logClear();
            }

            // Cr�ation d'un revenu avec des donn�es incorrect (FK idRevenu n'existe pas)
            // R�sultat attendu : Exception lev�e dans le checker
            simpleRevenuSourcier = new SimpleRevenuSourcier();
            String idRevenuSourcierNotExist = "999999999999999999";
            simpleRevenuSourcier.setIdRevenu(idRevenuSourcierNotExist);
            try {
                simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().create(
                        simpleRevenuSourcier);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la cr�ation d'un revenuSourcier avec donn�es incorrectes ==> "
                        + e.toString());
            } finally {
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                JadeThread.logClear();
            }

            // Cr�ation d'un revenu avec des donn�es incorrect (FK idRevenu vide)
            // R�sultat attendu : Exception lev�e dans le checker
            simpleRevenuSourcier = new SimpleRevenuSourcier();
            try {
                simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().create(
                        simpleRevenuSourcier);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la cr�ation d'un revenuSourcier avec donn�es incorrectes (fk idrevenu empty) ==> "
                        + e.toString());
            } finally {
                Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                JadeThread.logClear();
            }

            // Cr�ation d'un revenu avec des donn�es correctes
            // R�sultat attendu : isNew == false; ==> RevenuSourcier OK
            simpleRevenuSourcier = new SimpleRevenuSourcier();
            simpleRevenuSourcier.setIdRevenu("1");
            try {
                simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().create(
                        simpleRevenuSourcier);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la cr�ation d'un revenuSourcier avec donn�es correctes ==> " + e.toString());
            } finally {
                Assert.assertFalse(simpleRevenuSourcier.isNew());
                JadeThread.logClear();
                Assert.assertNotNull(simpleRevenuSourcier.getIdRevenuSourcier());
                Assert.assertNotNull(simpleRevenuSourcier.getId());
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
    public void testDelete() {
        try {
            SimpleRevenuSourcier simpleRevenuSourcier = new SimpleRevenuSourcier();

            // Effacement d'un mod�le null
            // R�sultat attendu : Exception
            boolean bDeleteNull = false;
            try {
                simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().delete(null);
            } catch (RevenuException re) {
                bDeleteNull = true;
            } catch (Exception e) {
                Assert.fail("Exception lev�e lors du delete NULL d'un revenuSourcier ==> " + e.toString());
            } finally {
                Assert.assertTrue(bDeleteNull);
                JadeThread.logClear();
            }

            // Effacement d'un revenu (isNew == true)
            // R�sultat attendu : Exception
            boolean bDeleteNew = false;
            simpleRevenuSourcier = new SimpleRevenuSourcier();
            try {
                simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().delete(
                        simpleRevenuSourcier);
            } catch (JadePersistenceException re) {
                bDeleteNew = true;
            } catch (Exception e) {
                Assert.fail("Exception lev�e lors du delete NEW d'un revenuSourcier ==> " + e.toString());
            } finally {
                Assert.assertTrue(bDeleteNew);
                JadeThread.logClear();
            }

            // Effacement d'un revenu existant
            // R�sultat attendu : Effacement ok (codeActif == false)
            simpleRevenuSourcier = new SimpleRevenuSourcier();
            simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().read(
                    ID_CONTRIBUABLE_SOURCIER_OK);
            boolean bDeleteOk = true;
            try {
                simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().delete(
                        simpleRevenuSourcier);
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
    public void testRead() {
        try {
            SimpleRevenuSourcier simpleRevenuSourcier = new SimpleRevenuSourcier();
            String idRevenuSourcierDontExist = "-1";
            // Lecture d'un revenu qui n'existe pas
            // R�sultat attendu : isNew == true et tout les champs � NULL
            try {
                simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().read(
                        idRevenuSourcierDontExist);
            } catch (Exception e) {
                Assert.fail("Erreur lecture Revenu qui n'existe pas ==> " + e.toString());
            } finally {
                Assert.assertTrue(simpleRevenuSourcier.isNew());
                Assert.assertEquals(idRevenuSourcierDontExist, simpleRevenuSourcier.getIdRevenuSourcier());
                Assert.assertEquals(idRevenuSourcierDontExist, simpleRevenuSourcier.getId());
                Assert.assertNull(simpleRevenuSourcier.getCotisationAc());
                Assert.assertNull(simpleRevenuSourcier.getCotisationAcSupplementaires());
                Assert.assertNull(simpleRevenuSourcier.getCotisationAvsAiApg());
                Assert.assertNull(simpleRevenuSourcier.getDeductionAssurances());
                Assert.assertNull(simpleRevenuSourcier.getDeductionAssurancesEnfant());
                Assert.assertNull(simpleRevenuSourcier.getDeductionAssurancesJeunes());
                Assert.assertNull(simpleRevenuSourcier.getDeductionDoubleGain());
                Assert.assertNull(simpleRevenuSourcier.getDeductionEnfants());
                Assert.assertNull(simpleRevenuSourcier.getDeductionFraisObtention());
                Assert.assertNull(simpleRevenuSourcier.getNombreMois());
                Assert.assertNull(simpleRevenuSourcier.getPrimesAANP());
                Assert.assertNull(simpleRevenuSourcier.getPrimesLPP());
                Assert.assertNull(simpleRevenuSourcier.getRevenuEpouseAnnuel());
                Assert.assertNull(simpleRevenuSourcier.getRevenuEpouseMensuel());
                Assert.assertNull(simpleRevenuSourcier.getRevenuEpouxAnnuel());
                Assert.assertNull(simpleRevenuSourcier.getRevenuEpouxMensuel());
                Assert.assertNull(simpleRevenuSourcier.getRevenuPrisEnCompte());
            }

            // Lecture d'un revenu qui existe
            // R�sultat attendu : isNew == true et tout les champs � NULL
            simpleRevenuSourcier = new SimpleRevenuSourcier();
            String idRevenuSourcierOk = ID_CONTRIBUABLE_SOURCIER_OK;
            try {
                simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().read(idRevenuSourcierOk);
            } catch (Exception e) {
                Assert.fail("Erreur lecture Revenu existant ==> " + e.toString());
            } finally {
                Assert.assertFalse(simpleRevenuSourcier.isNew());
                Assert.assertEquals(idRevenuSourcierOk, simpleRevenuSourcier.getIdRevenuSourcier());
                Assert.assertEquals(idRevenuSourcierOk, simpleRevenuSourcier.getId());
                Assert.assertNotNull(simpleRevenuSourcier.getCotisationAc());
                Assert.assertNotNull(simpleRevenuSourcier.getCotisationAcSupplementaires());
                Assert.assertNotNull(simpleRevenuSourcier.getCotisationAvsAiApg());
                Assert.assertNotNull(simpleRevenuSourcier.getDeductionAssurances());
                Assert.assertNotNull(simpleRevenuSourcier.getDeductionAssurancesEnfant());
                Assert.assertNotNull(simpleRevenuSourcier.getDeductionAssurancesJeunes());
                Assert.assertNotNull(simpleRevenuSourcier.getDeductionDoubleGain());
                Assert.assertNotNull(simpleRevenuSourcier.getDeductionEnfants());
                Assert.assertNotNull(simpleRevenuSourcier.getDeductionFraisObtention());
                Assert.assertNotNull(simpleRevenuSourcier.getNombreMois());
                Assert.assertNotNull(simpleRevenuSourcier.getPrimesAANP());
                Assert.assertNotNull(simpleRevenuSourcier.getPrimesLPP());
                Assert.assertNotNull(simpleRevenuSourcier.getRevenuEpouseAnnuel());
                Assert.assertNotNull(simpleRevenuSourcier.getRevenuEpouseMensuel());
                Assert.assertNotNull(simpleRevenuSourcier.getRevenuEpouxAnnuel());
                Assert.assertNotNull(simpleRevenuSourcier.getRevenuEpouxMensuel());
                Assert.assertNotNull(simpleRevenuSourcier.getRevenuPrisEnCompte());
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
    public void testSearch() {
        try {
            SimpleRevenuSourcierSearch simpleRevenuSourcierSearch = new SimpleRevenuSourcierSearch();
            // Recherche d'un revenu sans crit�res de recherche
            // R�sultat attendu : Nombre de r�sultat >0
            simpleRevenuSourcierSearch = AmalImplServiceLocator.getSimpleRevenuSourcierService().search(
                    simpleRevenuSourcierSearch);
            int nbResult = simpleRevenuSourcierSearch.getSize();
            Assert.assertTrue(nbResult >= 1);

            // Recherche d'un revenu avec crit�res de recherche qui n'existe pas
            // R�sultat attendu : Nombre de r�sultat == 0
            simpleRevenuSourcierSearch = new SimpleRevenuSourcierSearch();
            simpleRevenuSourcierSearch.setForIdRevenuSourcier("-1");
            simpleRevenuSourcierSearch = AmalImplServiceLocator.getSimpleRevenuSourcierService().search(
                    simpleRevenuSourcierSearch);
            nbResult = simpleRevenuSourcierSearch.getSize();
            Assert.assertTrue(nbResult <= 0);

            // Recherche d'un revenu avec crit�res de recherche qui existe
            // R�sultat attendu : Nombre de r�sultat == 1
            simpleRevenuSourcierSearch = new SimpleRevenuSourcierSearch();
            simpleRevenuSourcierSearch.setForIdRevenuSourcier(ID_CONTRIBUABLE_SOURCIER_OK);
            simpleRevenuSourcierSearch = AmalImplServiceLocator.getSimpleRevenuSourcierService().search(
                    simpleRevenuSourcierSearch);
            nbResult = simpleRevenuSourcierSearch.getSize();
            Assert.assertTrue(nbResult == 1);
            try {
                SimpleRevenuSourcier simpleRevenuSourcier = (SimpleRevenuSourcier) simpleRevenuSourcierSearch
                        .getSearchResults()[0];
                Assert.assertFalse(simpleRevenuSourcier.isNew());
                Assert.assertNotNull(simpleRevenuSourcier.getCotisationAc());
                Assert.assertNotNull(simpleRevenuSourcier.getCotisationAcSupplementaires());
                Assert.assertNotNull(simpleRevenuSourcier.getCotisationAvsAiApg());
                Assert.assertNotNull(simpleRevenuSourcier.getDeductionAssurances());
                Assert.assertNotNull(simpleRevenuSourcier.getDeductionAssurancesEnfant());
                Assert.assertNotNull(simpleRevenuSourcier.getDeductionAssurancesJeunes());
                Assert.assertNotNull(simpleRevenuSourcier.getDeductionDoubleGain());
                Assert.assertNotNull(simpleRevenuSourcier.getDeductionEnfants());
                Assert.assertNotNull(simpleRevenuSourcier.getDeductionFraisObtention());
                Assert.assertNotNull(simpleRevenuSourcier.getNombreMois());
                Assert.assertNotNull(simpleRevenuSourcier.getPrimesAANP());
                Assert.assertNotNull(simpleRevenuSourcier.getPrimesLPP());
                Assert.assertNotNull(simpleRevenuSourcier.getRevenuEpouseAnnuel());
                Assert.assertNotNull(simpleRevenuSourcier.getRevenuEpouseMensuel());
                Assert.assertNotNull(simpleRevenuSourcier.getRevenuEpouxAnnuel());
                Assert.assertNotNull(simpleRevenuSourcier.getRevenuEpouxMensuel());
                Assert.assertNotNull(simpleRevenuSourcier.getRevenuPrisEnCompte());
            } catch (Exception e) {
                Assert.fail("Erreur lors de la r�cup�ration du r�sultat de recherche de SimpleRevenuSourcier ! ==> "
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
    public void testUpdate() {
        try {
            SimpleRevenuSourcier simpleRevenuSourcier = new SimpleRevenuSourcier();

            // Update d'un revenu qui n'existe pas (isNew == true)
            // R�sultat attendu : Exception
            boolean bUpdateNew = false;
            try {
                simpleRevenuSourcier.setRevenuPrisEnCompte("999999");
                simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().update(
                        simpleRevenuSourcier);
            } catch (Exception e) {
                bUpdateNew = true;
            } finally {
                Assert.assertTrue(bUpdateNew);
                JadeThread.logClear();
            }

            // Update d'un revenu qui existe avec des donn�es correctes
            // R�sultat attendu : Mod�le mis � jour correctement
            String idRevenuSourcierToUpdate = ID_CONTRIBUABLE_SOURCIER_OK;
            simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().read(
                    idRevenuSourcierToUpdate);
            String spy = simpleRevenuSourcier.getSpy();
            try {
                simpleRevenuSourcier.setRevenuPrisEnCompte("99999");
                simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().update(
                        simpleRevenuSourcier);
            } catch (Exception e) {
                Assert.fail("Erreur non g�r�e lors de l'update d'un revenu avec donn�es correctes==> " + e.toString());
            } finally {
                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertEquals(idRevenuSourcierToUpdate, simpleRevenuSourcier.getIdRevenuSourcier());
                Assert.assertEquals("99999", simpleRevenuSourcier.getRevenuPrisEnCompte());
                Assert.assertFalse(spy.equalsIgnoreCase(simpleRevenuSourcier.getSpy()));
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
