/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.parametres;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import ch.globaz.amal.business.exceptions.models.subsideannee.SubsideAnneeException;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnnee;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnneeSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author dhi
 * 
 */
public class SimpleSubsideAnneeServiceImplTest {

    /**
     * @param name
     */
    public SimpleSubsideAnneeServiceImplTest(String name) {
        // super(name);
    }

    /**
     * Test la cr�ation
     */
    public void testCreate() {
        try {
            // Cr�ation avec des param�tres erron�s dans le mod�le
            // Attente de r�sultat >> JadeThread.hasMessage � true
            SimpleSubsideAnnee currentSubside = new SimpleSubsideAnnee();
            currentSubside.setAnneeSubside(null);
            try {
                currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().create(currentSubside);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la cr�ation avec param�tres erron�s " + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Cr�ation avec des param�tres corrects et boolean renseign�
            // Attente de r�sultat >> mod�le renseign� avec isNew � false
            currentSubside = new SimpleSubsideAnnee();
            currentSubside.setAnneeSubside("2020");
            currentSubside.setLimiteRevenu("10000");
            currentSubside.setSubsideAdo("30");
            currentSubside.setSubsideAdulte("25");
            currentSubside.setSubsideEnfant("55");
            currentSubside.setSubsideSalarie1618("68");
            currentSubside.setSubsideSalarie1925("122");
            try {
                currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().create(currentSubside);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la cr�ation avec param�tres corrects " + ex.toString());
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            Assert.assertEquals(false, currentSubside.isNew());
            Assert.assertNotNull(currentSubside.getId());
            Assert.assertNotNull(currentSubside.getIdSubsideAnnee());
            Assert.assertEquals("2020", currentSubside.getAnneeSubside());
            Assert.assertEquals("10000", currentSubside.getLimiteRevenu());
            Assert.assertEquals("30", currentSubside.getSubsideAdo());
            Assert.assertEquals("25", currentSubside.getSubsideAdulte());
            Assert.assertEquals("55", currentSubside.getSubsideEnfant());
            Assert.assertEquals("68", currentSubside.getSubsideSalarie1618());
            Assert.assertEquals("122", currentSubside.getSubsideSalarie1925());
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
    public void testDelete() {
        try {
            // Effacement d'un model null
            // Attente de r�sultat >> exception typ�e
            SimpleSubsideAnnee currentSubside = null;
            boolean bModelException = false;
            try {
                currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().delete(currentSubside);
            } catch (SubsideAnneeException ex) {
                bModelException = true;
            }
            Assert.assertEquals(true, bModelException);

            // Effacement d'un model nouveau >> isNew = true
            // Attente de r�sultat >> exception du framework
            boolean bJadePersistenceException = false;
            currentSubside = new SimpleSubsideAnnee();
            currentSubside.setAnneeSubside("2020");
            try {
                currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().delete(currentSubside);
            } catch (JadePersistenceException ex) {
                bJadePersistenceException = true;
            }
            Assert.assertEquals(true, bJadePersistenceException);

            // Effacement d'un model existant
            // Attente de r�sultat >> effacement OK et is new � true
            currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().read("10");
            boolean bDeleteOK = true;
            try {
                currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().delete(currentSubside);
            } catch (Exception ex) {
                bDeleteOK = false;
            }
            Assert.assertEquals(true, bDeleteOK);
            Assert.assertEquals(true, currentSubside.isNew());

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
    public void testRead() {
        try {
            // Lecture d'un mod�le avec un id bidon
            // Attente de r�sultat >> model id=-1, tous les autres champs sont null
            SimpleSubsideAnnee currentSubside = null;
            currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().read("-1");
            Assert.assertEquals("-1", currentSubside.getId());
            Assert.assertEquals("-1", currentSubside.getIdSubsideAnnee());
            Assert.assertNull(currentSubside.getAnneeSubside());
            Assert.assertNull(currentSubside.getLimiteRevenu());
            Assert.assertNull(currentSubside.getSubsideAdo());
            Assert.assertNull(currentSubside.getSubsideAdulte());
            Assert.assertNull(currentSubside.getSubsideEnfant());
            Assert.assertNull(currentSubside.getSubsideSalarie1618());
            Assert.assertNull(currentSubside.getSubsideSalarie1925());
            // Lecture d'un mod�le avec un id OK
            // Attente de r�sultat >> un mod�le renseign� et exploitable
            currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().read("10");
            Assert.assertEquals("10", currentSubside.getId());
            Assert.assertEquals("10", currentSubside.getIdSubsideAnnee());
            Assert.assertNotNull(currentSubside.getAnneeSubside());
            Assert.assertNotNull(currentSubside.getLimiteRevenu());
            Assert.assertNotNull(currentSubside.getSubsideAdo());
            Assert.assertNotNull(currentSubside.getSubsideAdulte());
            Assert.assertNotNull(currentSubside.getSubsideEnfant());
            Assert.assertNotNull(currentSubside.getSubsideSalarie1618());
            Assert.assertNotNull(currentSubside.getSubsideSalarie1925());
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
    public void testSearch() {
        try {
            int iNbResults = 0;
            // Recherche d'un mod�le sans crit�res de recherche
            // Attente de r�sultat >> nombre de r�sultat >= 1
            SimpleSubsideAnneeSearch searched = new SimpleSubsideAnneeSearch();
            searched = AmalServiceLocator.getSimpleSubsideAnneeService().search(searched);
            iNbResults = searched.getSize();
            Assert.assertEquals(true, (iNbResults >= 1));

            // Recherche avec des crit�res de recherche erron�s
            // Attente de r�sultat >> nombre de r�sultat = 0
            searched = new SimpleSubsideAnneeSearch();
            searched.setForIdSubsideAnnee("-40");
            searched = AmalServiceLocator.getSimpleSubsideAnneeService().search(searched);
            iNbResults = searched.getSize();
            Assert.assertEquals(true, (iNbResults <= 0));

            // Recherche avec des crit�res de recherche cibl�s
            // Attente de r�sultat >> nombre de r�sultat = 1
            // Attente de r�sultat >> r�sultat exploitable (mod�le)
            searched = new SimpleSubsideAnneeSearch();
            searched.setForIdSubsideAnnee("10");
            searched = AmalServiceLocator.getSimpleSubsideAnneeService().search(searched);
            iNbResults = searched.getSize();
            Assert.assertEquals(true, (iNbResults == 1));
            // Attente de r�sultat >> un mod�le renseign� et exploitable
            SimpleSubsideAnnee currentSubside = (SimpleSubsideAnnee) searched.getSearchResults()[0];
            Assert.assertEquals("10", currentSubside.getId());
            Assert.assertEquals("10", currentSubside.getIdSubsideAnnee());
            Assert.assertNotNull(currentSubside.getAnneeSubside());
            Assert.assertNotNull(currentSubside.getLimiteRevenu());
            Assert.assertNotNull(currentSubside.getSubsideAdo());
            Assert.assertNotNull(currentSubside.getSubsideAdulte());
            Assert.assertNotNull(currentSubside.getSubsideEnfant());
            Assert.assertNotNull(currentSubside.getSubsideSalarie1618());
            Assert.assertNotNull(currentSubside.getSubsideSalarie1925());
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
    public void testUpdate() {
        try {
            // Mise � jour d'un mod�le nouveau >> isNew = true
            // Attente de r�sultat >> persistence r�agit
            SimpleSubsideAnnee currentSubside = new SimpleSubsideAnnee();
            currentSubside.setAnneeSubside("2020");
            try {
                currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().update(currentSubside);
                Assert.fail("Exception non soulev�e lors de la mise � jour d'un mod�le isNew");
            } catch (JadePersistenceException ex) {
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            // Mise � jour d'un mod�le existant avec des param�tres erron�s
            // Attente de r�sultat >> Message dans JadeThread lev� par le checker
            currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().read("10");
            currentSubside.setAnneeSubside("");
            try {
                currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().update(currentSubside);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la mise � jour avec param�tres erron�s");
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Mise � jour d'un model existant avec des param�tres corrects
            // Attente de r�sultat >> mod�le renseign� en retour = mod�le de base updat�
            currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().read("10");
            currentSubside.setAnneeSubside("2020");
            currentSubside.setLimiteRevenu("10000");
            currentSubside.setSubsideAdo("30");
            currentSubside.setSubsideAdulte("25");
            currentSubside.setSubsideEnfant("55");
            currentSubside.setSubsideSalarie1618("68");
            currentSubside.setSubsideSalarie1925("122");
            try {
                currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().update(currentSubside);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la mise � jour d'une annonce avec param�tres corrects");
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            Assert.assertEquals(false, currentSubside.isNew());
            Assert.assertEquals("10", currentSubside.getId());
            Assert.assertEquals("10", currentSubside.getIdSubsideAnnee());
            Assert.assertEquals("2020", currentSubside.getAnneeSubside());
            Assert.assertEquals("10000", currentSubside.getLimiteRevenu());
            Assert.assertEquals("30", currentSubside.getSubsideAdo());
            Assert.assertEquals("25", currentSubside.getSubsideAdulte());
            Assert.assertEquals("55", currentSubside.getSubsideEnfant());
            Assert.assertEquals("68", currentSubside.getSubsideSalarie1618());
            Assert.assertEquals("122", currentSubside.getSubsideSalarie1925());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }
    }

}
