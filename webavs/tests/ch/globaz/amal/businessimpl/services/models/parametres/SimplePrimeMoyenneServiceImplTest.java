/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.parametres;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.amal.business.exceptions.models.primemoyenne.PrimeMoyenneException;
import ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenne;
import ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenneSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author dhi
 * 
 */
public class SimplePrimeMoyenneServiceImplTest {

    /**
     * Test la création
     */
    @Ignore
    @Test
    public void testCreate() {
        try {
            // Création avec des paramètres erronés dans le modèle
            // Attente de résultat >> JadeThread.hasMessage à true
            SimplePrimeMoyenne prime = new SimplePrimeMoyenne();
            prime.setAnneeSubside("");
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().create(prime);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la création avec paramètres erronés " + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

            // Création avec des paramètres corrects mais année déjà occupée
            // checker doit réagir
            prime = new SimplePrimeMoyenne();
            prime.setAnneeSubside("2010");
            prime.setMontantPrimeAdulte("400");
            prime.setMontantPrimeEnfant("200");
            prime.setMontantPrimeFormation("300");
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().create(prime);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la création avec année existante " + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

            // Création avec des paramètres corrects et boolean renseigné
            // Attente de résultat >> modèle renseigné avec isNew à false
            prime = new SimplePrimeMoyenne();
            prime.setAnneeSubside("2020");
            prime.setMontantPrimeAdulte("400");
            prime.setMontantPrimeEnfant("200");
            prime.setMontantPrimeFormation("300");
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().create(prime);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la création avec paramètres corrects " + ex.toString());
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            Assert.assertEquals(false, prime.isNew());
            Assert.assertNotNull(prime.getId());
            Assert.assertNotNull(prime.getIdPrimeMoyenne());
            Assert.assertEquals("2020", prime.getAnneeSubside());
            Assert.assertEquals("400", prime.getMontantPrimeAdulte());
            Assert.assertEquals("200", prime.getMontantPrimeEnfant());
            Assert.assertEquals("300", prime.getMontantPrimeFormation());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }
    }

    /**
     * Test de suppression d'un modèle
     */
    @Ignore
    @Test
    public void testDelete() {
        try {
            // Effacement d'un model null
            // Attente de résultat >> exception typée
            SimplePrimeMoyenne prime = null;
            boolean bModelException = false;
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().delete(prime);
            } catch (PrimeMoyenneException ex) {
                bModelException = true;
            }
            Assert.assertEquals(true, bModelException);

            // Effacement d'un model nouveau >> isNew = true
            // Attente de résultat >> exception du framework
            prime = new SimplePrimeMoyenne();
            prime.setAnneeSubside("2020");
            prime.setMontantPrimeAdulte("400");
            prime.setMontantPrimeEnfant("200");
            prime.setMontantPrimeFormation("300");
            boolean bJadePersistenceException = false;
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().delete(prime);
            } catch (JadePersistenceException ex) {
                bJadePersistenceException = true;
            }
            Assert.assertEquals(true, bJadePersistenceException);

            // Effacement d'un model existant
            // Attente de résultat >> effacement OK et is new à true
            prime = AmalServiceLocator.getSimplePrimeMoyenneService().read("1");
            boolean bDeleteOK = true;
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().delete(prime);
            } catch (Exception ex) {
                bDeleteOK = false;
            }
            Assert.assertEquals(true, bDeleteOK);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    /**
     * Test de lecture d'un modèle
     */
    @Ignore
    @Test
    public void testRead() {
        try {
            // Lecture d'un modèle avec un id bidon
            // Attente de résultat >> model id=-1, tous les autres champs sont null
            SimplePrimeMoyenne prime = AmalServiceLocator.getSimplePrimeMoyenneService().read("-1");
            Assert.assertEquals("-1", prime.getId());
            Assert.assertNull(prime.getAnneeSubside());
            Assert.assertNull(prime.getMontantPrimeAdulte());
            Assert.assertNull(prime.getMontantPrimeEnfant());
            Assert.assertNull(prime.getMontantPrimeFormation());
            // Lecture d'un modèle avec un id OK
            // Attente de résultat >> un modèle renseigné et exploitable
            prime = AmalServiceLocator.getSimplePrimeMoyenneService().read("1");
            Assert.assertEquals("1", prime.getId());
            Assert.assertEquals("1", prime.getIdPrimeMoyenne());
            Assert.assertNotNull(prime.getAnneeSubside());
            Assert.assertNotNull(prime.getMontantPrimeAdulte());
            Assert.assertNotNull(prime.getMontantPrimeEnfant());
            Assert.assertNotNull(prime.getMontantPrimeFormation());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    /**
     * Test de recherche
     */
    @Ignore
    @Test
    public void testSearch() {
        try {
            // Recherche d'un modèle sans critères de recherche
            // Attente de résultat >> nombre de résultat >= 1
            int iNbResults = 0;
            SimplePrimeMoyenneSearch primeSearch = new SimplePrimeMoyenneSearch();
            primeSearch = AmalServiceLocator.getSimplePrimeMoyenneService().search(primeSearch);
            iNbResults = primeSearch.getSize();
            Assert.assertEquals(true, (iNbResults >= 1));

            // Recherche avec des critères de recherche erronés
            // Attente de résultat >> nombre de résultat = 0
            primeSearch = new SimplePrimeMoyenneSearch();
            primeSearch.setForAnneeSubside("-20");
            primeSearch = AmalServiceLocator.getSimplePrimeMoyenneService().search(primeSearch);
            iNbResults = primeSearch.getSize();
            Assert.assertEquals(true, (iNbResults <= 0));

            // Recherche avec des critères de recherche ciblés
            // Attente de résultat >> nombre de résultat = 1
            // Attente de résultat >> résultat exploitable (modèle)
            primeSearch = new SimplePrimeMoyenneSearch();
            primeSearch.setForAnneeSubside("2010");
            primeSearch = AmalServiceLocator.getSimplePrimeMoyenneService().search(primeSearch);
            iNbResults = primeSearch.getSize();
            Assert.assertEquals(true, (iNbResults == 1));
            // Attente de résultat >> un modèle renseigné et exploitable
            SimplePrimeMoyenne prime = (SimplePrimeMoyenne) primeSearch.getSearchResults()[0];
            Assert.assertEquals("2010", prime.getAnneeSubside());
            Assert.assertNotNull(prime.getId());
            Assert.assertNotNull(prime.getIdPrimeMoyenne());
            Assert.assertNotNull(prime.getMontantPrimeAdulte());
            Assert.assertNotNull(prime.getMontantPrimeEnfant());
            Assert.assertNotNull(prime.getMontantPrimeFormation());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    /**
     * Test de mise à jour
     */
    @Ignore
    @Test
    public void testUpdate() {
        try {
            // Mise à jour d'un modèle nouveau >> isNew = true
            // Attente de résultat >> persistence réagit
            SimplePrimeMoyenne prime = new SimplePrimeMoyenne();
            prime.setAnneeSubside("2020");
            prime.setMontantPrimeAdulte("400");
            prime.setMontantPrimeEnfant("200");
            prime.setMontantPrimeFormation("300");
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().update(prime);
                Assert.fail("Exception non soulevée lors de la mise à jour d'un modèle isNew");
            } catch (Exception ex) {
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            // Mise à jour d'un modèle existant avec des paramètres erronés
            // Attente de résultat >> Message dans JadeThread levé par le checker
            prime = AmalServiceLocator.getSimplePrimeMoyenneService().read("1");
            prime.setAnneeSubside("");
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().update(prime);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la mise à jour avec paramètres erronés " + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Mise à jour d'un model existant avec des paramètres corrects
            // Attente de résultat >> modèle renseigné en retour = modèle de base updaté
            prime = AmalServiceLocator.getSimplePrimeMoyenneService().read("1");
            prime.setAnneeSubside("2020");
            prime.setMontantPrimeAdulte("400");
            prime.setMontantPrimeEnfant("200");
            prime.setMontantPrimeFormation("300");
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().update(prime);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la mise à jour d'une annonce avec paramètres corrects "
                        + ex.toString());
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            Assert.assertEquals(false, prime.isNew());
            Assert.assertEquals("1", prime.getId());
            Assert.assertEquals("1", prime.getIdPrimeMoyenne());
            Assert.assertEquals("2020", prime.getAnneeSubside());
            Assert.assertEquals("400", prime.getMontantPrimeAdulte());
            Assert.assertEquals("200", prime.getMontantPrimeEnfant());
            Assert.assertEquals("300", prime.getMontantPrimeFormation());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }
    }

}
