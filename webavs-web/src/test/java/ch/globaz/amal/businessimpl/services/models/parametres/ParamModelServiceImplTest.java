/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.parametres;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;

/**
 * @author dhi
 * 
 */
public class ParamModelServiceImplTest {

    /**
     * @param name
     */
    public ParamModelServiceImplTest(String name) {
        // super(name);
    }

    /**
     * Test la création
     */
    public void testCreate() {
        try {
            // Création avec des paramètres erronés dans le modèle
            // Attente de résultat >> JadeThread.hasMessage à true
            try {
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la création avec paramètres erronés " + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Création avec des paramètres corrects et boolean renseigné
            // Attente de résultat >> modèle renseigné avec isNew à false
            try {
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la création avec paramètres corrects " + ex.toString());
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
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
            boolean bModelException = false;
            try {
            } catch (Exception ex) {
                bModelException = true;
            }
            Assert.assertEquals(true, bModelException);

            // Effacement d'un model nouveau >> isNew = true
            // Attente de résultat >> exception du framework
            boolean bJadePersistenceException = false;
            try {
            } catch (Exception ex) {
                bJadePersistenceException = true;
            }
            Assert.assertEquals(true, bJadePersistenceException);

            // Effacement d'un model existant
            // Attente de résultat >> effacement OK et is new à true
            boolean bDeleteOK = true;
            try {
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
    public void testRead() {
        try {
            // Lecture d'un modèle avec un id bidon
            // Attente de résultat >> model id=-1, tous les autres champs sont null

            // Lecture d'un modèle avec un id OK
            // Attente de résultat >> un modèle renseigné et exploitable
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
            // Recherche d'un modèle sans critères de recherche
            // Attente de résultat >> nombre de résultat >= 1
            int iNbResults = 0;
            Assert.assertEquals(true, (iNbResults >= 1));

            // Recherche avec des critères de recherche erronés
            // Attente de résultat >> nombre de résultat = 0
            Assert.assertEquals(true, (iNbResults <= 0));

            // Recherche avec des critères de recherche ciblés
            // Attente de résultat >> nombre de résultat = 1
            // Attente de résultat >> résultat exploitable (modèle)
            Assert.assertEquals(true, (iNbResults == 1));
            // Attente de résultat >> un modèle renseigné et exploitable
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
            try {
                Assert.fail("Exception non soulevée lors de la mise à jour d'un modèle isNew");
            } catch (Exception ex) {
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            // Mise à jour d'un modèle existant avec des paramètres erronés
            // Attente de résultat >> Message dans JadeThread levé par le checker
            try {
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la mise à jour avec paramètres erronés");
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Mise à jour d'un model existant avec des paramètres corrects
            // Attente de résultat >> modèle renseigné en retour = modèle de base updaté
            try {
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la mise à jour d'une annonce avec paramètres corrects");
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }
    }

}
