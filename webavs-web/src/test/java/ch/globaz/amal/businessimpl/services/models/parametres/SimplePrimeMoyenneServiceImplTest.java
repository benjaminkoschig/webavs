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
     * Test la cr�ation
     */
    @Ignore
    @Test
    public void testCreate() {
        try {
            // Cr�ation avec des param�tres erron�s dans le mod�le
            // Attente de r�sultat >> JadeThread.hasMessage � true
            SimplePrimeMoyenne prime = new SimplePrimeMoyenne();
            prime.setAnneeSubside("");
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().create(prime);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la cr�ation avec param�tres erron�s " + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

            // Cr�ation avec des param�tres corrects mais ann�e d�j� occup�e
            // checker doit r�agir
            prime = new SimplePrimeMoyenne();
            prime.setAnneeSubside("2010");
            prime.setMontantPrimeAdulte("400");
            prime.setMontantPrimeEnfant("200");
            prime.setMontantPrimeFormation("300");
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().create(prime);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la cr�ation avec ann�e existante " + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

            // Cr�ation avec des param�tres corrects et boolean renseign�
            // Attente de r�sultat >> mod�le renseign� avec isNew � false
            prime = new SimplePrimeMoyenne();
            prime.setAnneeSubside("2020");
            prime.setMontantPrimeAdulte("400");
            prime.setMontantPrimeEnfant("200");
            prime.setMontantPrimeFormation("300");
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().create(prime);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la cr�ation avec param�tres corrects " + ex.toString());
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
     * Test de suppression d'un mod�le
     */
    @Ignore
    @Test
    public void testDelete() {
        try {
            // Effacement d'un model null
            // Attente de r�sultat >> exception typ�e
            SimplePrimeMoyenne prime = null;
            boolean bModelException = false;
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().delete(prime);
            } catch (PrimeMoyenneException ex) {
                bModelException = true;
            }
            Assert.assertEquals(true, bModelException);

            // Effacement d'un model nouveau >> isNew = true
            // Attente de r�sultat >> exception du framework
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
            // Attente de r�sultat >> effacement OK et is new � true
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
     * Test de lecture d'un mod�le
     */
    @Ignore
    @Test
    public void testRead() {
        try {
            // Lecture d'un mod�le avec un id bidon
            // Attente de r�sultat >> model id=-1, tous les autres champs sont null
            SimplePrimeMoyenne prime = AmalServiceLocator.getSimplePrimeMoyenneService().read("-1");
            Assert.assertEquals("-1", prime.getId());
            Assert.assertNull(prime.getAnneeSubside());
            Assert.assertNull(prime.getMontantPrimeAdulte());
            Assert.assertNull(prime.getMontantPrimeEnfant());
            Assert.assertNull(prime.getMontantPrimeFormation());
            // Lecture d'un mod�le avec un id OK
            // Attente de r�sultat >> un mod�le renseign� et exploitable
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
            // Recherche d'un mod�le sans crit�res de recherche
            // Attente de r�sultat >> nombre de r�sultat >= 1
            int iNbResults = 0;
            SimplePrimeMoyenneSearch primeSearch = new SimplePrimeMoyenneSearch();
            primeSearch = AmalServiceLocator.getSimplePrimeMoyenneService().search(primeSearch);
            iNbResults = primeSearch.getSize();
            Assert.assertEquals(true, (iNbResults >= 1));

            // Recherche avec des crit�res de recherche erron�s
            // Attente de r�sultat >> nombre de r�sultat = 0
            primeSearch = new SimplePrimeMoyenneSearch();
            primeSearch.setForAnneeSubside("-20");
            primeSearch = AmalServiceLocator.getSimplePrimeMoyenneService().search(primeSearch);
            iNbResults = primeSearch.getSize();
            Assert.assertEquals(true, (iNbResults <= 0));

            // Recherche avec des crit�res de recherche cibl�s
            // Attente de r�sultat >> nombre de r�sultat = 1
            // Attente de r�sultat >> r�sultat exploitable (mod�le)
            primeSearch = new SimplePrimeMoyenneSearch();
            primeSearch.setForAnneeSubside("2010");
            primeSearch = AmalServiceLocator.getSimplePrimeMoyenneService().search(primeSearch);
            iNbResults = primeSearch.getSize();
            Assert.assertEquals(true, (iNbResults == 1));
            // Attente de r�sultat >> un mod�le renseign� et exploitable
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
     * Test de mise � jour
     */
    @Ignore
    @Test
    public void testUpdate() {
        try {
            // Mise � jour d'un mod�le nouveau >> isNew = true
            // Attente de r�sultat >> persistence r�agit
            SimplePrimeMoyenne prime = new SimplePrimeMoyenne();
            prime.setAnneeSubside("2020");
            prime.setMontantPrimeAdulte("400");
            prime.setMontantPrimeEnfant("200");
            prime.setMontantPrimeFormation("300");
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().update(prime);
                Assert.fail("Exception non soulev�e lors de la mise � jour d'un mod�le isNew");
            } catch (Exception ex) {
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            // Mise � jour d'un mod�le existant avec des param�tres erron�s
            // Attente de r�sultat >> Message dans JadeThread lev� par le checker
            prime = AmalServiceLocator.getSimplePrimeMoyenneService().read("1");
            prime.setAnneeSubside("");
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().update(prime);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la mise � jour avec param�tres erron�s " + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Mise � jour d'un model existant avec des param�tres corrects
            // Attente de r�sultat >> mod�le renseign� en retour = mod�le de base updat�
            prime = AmalServiceLocator.getSimplePrimeMoyenneService().read("1");
            prime.setAnneeSubside("2020");
            prime.setMontantPrimeAdulte("400");
            prime.setMontantPrimeEnfant("200");
            prime.setMontantPrimeFormation("300");
            try {
                prime = AmalServiceLocator.getSimplePrimeMoyenneService().update(prime);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la mise � jour d'une annonce avec param�tres corrects "
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
