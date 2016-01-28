package ch.globaz.amal.businessimpl.services.models.contribuable;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.models.contribuable.SimpleContribuable;
import ch.globaz.amal.business.models.contribuable.SimpleContribuableSearch;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

public class SimpleContribuableServiceImplTest {

    @Ignore
    @Test
    public void testCreate() {

        try {
            SimpleContribuable simpleContribuable = new SimpleContribuable();

            // Cr�ation d'un contribuable avec mod�le null
            // R�sultat attendu : Exception
            boolean bCreateNullException = false;
            try {
                simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().create(null);
            } catch (Exception e) {
                bCreateNullException = true;
            }
            Assert.assertTrue(bCreateNullException);

            // Cr�ation d'un contribuable avec des donn�es correctes
            // R�sultat attendu : isNew == false;
            boolean bCreateOk = true;
            try {
                simpleContribuable = new SimpleContribuable();
                simpleContribuable.setIdTier("1111111111");
                simpleContribuable.setIsContribuableActif(true);
                simpleContribuable.setNoContribuable("2002002002");
                simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().create(simpleContribuable);
            } catch (Exception e) {
                bCreateOk = false;
            }
            Assert.assertTrue(bCreateOk);
            Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
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
            SimpleContribuable simpleContribuable = new SimpleContribuable();
            // Effacement d'un mod�le null
            // R�sultat attendu : Exception
            boolean bContribuableNullException = false;
            try {
                simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().delete(null);
            } catch (ContribuableException e) {
                bContribuableNullException = true;
            }
            Assert.assertTrue(bContribuableNullException);

            // Effacement d'un contribuable nouveau (isNew == true)
            // R�sultat attendu : Exception
            boolean bContribuableIsNewException = false;
            try {
                simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().delete(simpleContribuable);
            } catch (Exception e) {
                bContribuableIsNewException = true;
            }
            Assert.assertTrue(bContribuableIsNewException);

            // Effacement d'un contribuable qui poss�de des membres famille
            // R�sultat attendu : Erreur checker
            simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().read("151");
            simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().delete(simpleContribuable);
            Assert.assertTrue(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

            // Effacement d'un contribuable existant (isNew == false)
            // R�sultat attendu : Delete ok
            boolean bDeleteOk = true;
            try {
                simpleContribuable = new SimpleContribuable();
                simpleContribuable.setIdTier("1111111111");
                simpleContribuable.setIsContribuableActif(true);
                simpleContribuable.setNoContribuable("2002002002");
                simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().create(simpleContribuable);
                simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().delete(simpleContribuable);
                Assert.assertTrue(simpleContribuable.isNew());
            } catch (Exception e) {
                bDeleteOk = false;
            }
            Assert.assertTrue(bDeleteOk);
            Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
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
            SimpleContribuable simpleContribuable = new SimpleContribuable();
            // Lecture d'un contribuable qui n'existe pas
            // R�sultat attendu : isNew == true
            simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().read("-1");
            Assert.assertTrue(simpleContribuable.isNew());

            // Lecture d'un contribuable qui existe
            // R�sultat attendu : isNew == false
            simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().read("151");
            Assert.assertFalse(simpleContribuable.isNew());
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
            SimpleContribuableSearch simpleContribuableSearch = new SimpleContribuableSearch();
            // Recherche d'un contribuable sans crit�res de recherche
            // R�sultat attendu : Nombre de r�sultat >0
            simpleContribuableSearch = AmalImplServiceLocator.getSimpleContribuableService().search(
                    simpleContribuableSearch);
            int nbResult = simpleContribuableSearch.getSize();
            Assert.assertEquals(true, nbResult >= 1);

            // Recherche d'un contribuable avec crit�res de recherche qui n'existe pas
            // R�sultat attendu : Nombre de r�sultat == 0
            simpleContribuableSearch = new SimpleContribuableSearch();
            simpleContribuableSearch.setForIdContribuable("-1");
            simpleContribuableSearch = AmalImplServiceLocator.getSimpleContribuableService().search(
                    simpleContribuableSearch);
            nbResult = simpleContribuableSearch.getSize();
            Assert.assertEquals(true, nbResult == 0);

            // Recherche d'un contribuable avec crit�res de recherche qui existe
            // R�sultat attendu : Nombre de r�sultat == 1
            simpleContribuableSearch = new SimpleContribuableSearch();
            simpleContribuableSearch.setForIdContribuable("151");
            simpleContribuableSearch = AmalImplServiceLocator.getSimpleContribuableService().search(
                    simpleContribuableSearch);
            nbResult = simpleContribuableSearch.getSize();
            Assert.assertEquals(true, nbResult == 1);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testUpdate() {
        try {
            SimpleContribuable simpleContribuable = new SimpleContribuable();
            // Update d'un contribuable qui n'existe pas (isNew == true)
            // R�sultat attendu : Exception
            boolean bUpdateIsNew = false;
            try {
                simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().update(simpleContribuable);
            } catch (Exception e) {
                bUpdateIsNew = true;
            }
            Assert.assertTrue(bUpdateIsNew);

            // PAS DE CHECKER SUR SIMPLECONTRIBUABLE, TEST PAS FAISABLE
            // Update d'un contribuable existant avec de mauvaises donn�es
            // R�sultat attendu : JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == true
            // boolean bUpdateWrongDatas = false;
            // try {
            // simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().read("151");
            // simpleContribuable.setNoContribuable("aaa");
            // simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().update(simpleContribuable);
            // } catch (Exception e) {
            // bUpdateWrongDatas = true;
            // }
            // Assert.assertTrue(bUpdateWrongDatas);

            // Update d'un contribuable qui existe avec des donn�es correctes
            // R�sultat attendu : Mod�le mis � jour correctement
            boolean bUpdateOk = true;
            try {
                simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().read("151");
                simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().update(simpleContribuable);
            } catch (Exception e) {
                bUpdateOk = false;
            }
            Assert.assertTrue(bUpdateOk);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }
}