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
     * Test la création
     */
    public void testCreate() {
        try {
            // Création avec des paramètres erronés dans le modèle
            // Attente de résultat >> JadeThread.hasMessage à true
            SimpleSubsideAnnee currentSubside = new SimpleSubsideAnnee();
            currentSubside.setAnneeSubside(null);
            try {
                currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().create(currentSubside);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la création avec paramètres erronés " + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Création avec des paramètres corrects et boolean renseigné
            // Attente de résultat >> modèle renseigné avec isNew à false
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
                Assert.fail("Exception soulevée lors de la création avec paramètres corrects " + ex.toString());
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
     * Test de suppression d'un modèle
     */
    public void testDelete() {
        try {
            // Effacement d'un model null
            // Attente de résultat >> exception typée
            SimpleSubsideAnnee currentSubside = null;
            boolean bModelException = false;
            try {
                currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().delete(currentSubside);
            } catch (SubsideAnneeException ex) {
                bModelException = true;
            }
            Assert.assertEquals(true, bModelException);

            // Effacement d'un model nouveau >> isNew = true
            // Attente de résultat >> exception du framework
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
            // Attente de résultat >> effacement OK et is new à true
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
     * Test de lecture d'un modèle
     */
    public void testRead() {
        try {
            // Lecture d'un modèle avec un id bidon
            // Attente de résultat >> model id=-1, tous les autres champs sont null
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
            // Lecture d'un modèle avec un id OK
            // Attente de résultat >> un modèle renseigné et exploitable
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
            // Recherche d'un modèle sans critères de recherche
            // Attente de résultat >> nombre de résultat >= 1
            SimpleSubsideAnneeSearch searched = new SimpleSubsideAnneeSearch();
            searched = AmalServiceLocator.getSimpleSubsideAnneeService().search(searched);
            iNbResults = searched.getSize();
            Assert.assertEquals(true, (iNbResults >= 1));

            // Recherche avec des critères de recherche erronés
            // Attente de résultat >> nombre de résultat = 0
            searched = new SimpleSubsideAnneeSearch();
            searched.setForIdSubsideAnnee("-40");
            searched = AmalServiceLocator.getSimpleSubsideAnneeService().search(searched);
            iNbResults = searched.getSize();
            Assert.assertEquals(true, (iNbResults <= 0));

            // Recherche avec des critères de recherche ciblés
            // Attente de résultat >> nombre de résultat = 1
            // Attente de résultat >> résultat exploitable (modèle)
            searched = new SimpleSubsideAnneeSearch();
            searched.setForIdSubsideAnnee("10");
            searched = AmalServiceLocator.getSimpleSubsideAnneeService().search(searched);
            iNbResults = searched.getSize();
            Assert.assertEquals(true, (iNbResults == 1));
            // Attente de résultat >> un modèle renseigné et exploitable
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
     * Test de mise à jour
     */
    public void testUpdate() {
        try {
            // Mise à jour d'un modèle nouveau >> isNew = true
            // Attente de résultat >> persistence réagit
            SimpleSubsideAnnee currentSubside = new SimpleSubsideAnnee();
            currentSubside.setAnneeSubside("2020");
            try {
                currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().update(currentSubside);
                Assert.fail("Exception non soulevée lors de la mise à jour d'un modèle isNew");
            } catch (JadePersistenceException ex) {
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            // Mise à jour d'un modèle existant avec des paramètres erronés
            // Attente de résultat >> Message dans JadeThread levé par le checker
            currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().read("10");
            currentSubside.setAnneeSubside("");
            try {
                currentSubside = AmalServiceLocator.getSimpleSubsideAnneeService().update(currentSubside);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la mise à jour avec paramètres erronés");
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Mise à jour d'un model existant avec des paramètres corrects
            // Attente de résultat >> modèle renseigné en retour = modèle de base updaté
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
                Assert.fail("Exception soulevée lors de la mise à jour d'une annonce avec paramètres corrects");
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
