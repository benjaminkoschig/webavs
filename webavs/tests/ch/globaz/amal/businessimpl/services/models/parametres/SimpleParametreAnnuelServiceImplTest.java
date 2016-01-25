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
import ch.globaz.amal.business.constantes.IAMParametresAnnuels;
import ch.globaz.amal.business.exceptions.models.parametreannuel.ParametreAnnuelException;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuel;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * Tests pour les paramètres annuel
 * 
 * @author dhi
 * 
 */
public class SimpleParametreAnnuelServiceImplTest {

    /**
     * Default constructor
     * 
     * @param name
     *            Nom du test
     */

    /**
     * Test la création
     */
    @Test
    @Ignore
    public void testCreate() {
        try {
            // Création avec des paramètres erronés dans le modèle
            // Attente de résultat >> JadeThread.hasMessage à true
            SimpleParametreAnnuel annuel = new SimpleParametreAnnuel();
            annuel.setAnneeParametre("");
            annuel.setValeurParametre(null);
            try {
                annuel = AmalServiceLocator.getParametreAnnuelService().create(annuel);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la création avec paramètres erronés " + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Création avec des paramètres corrects et boolean renseigné
            // Attente de résultat >> modèle renseigné avec isNew à false
            annuel = new SimpleParametreAnnuel();
            annuel.setAnneeParametre("2020");
            annuel.setCodeTypeParametre(IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ASSU_COUPLE);
            annuel.setValeurParametre("110");
            annuel.setValeurParametreString("");
            try {
                annuel = AmalServiceLocator.getParametreAnnuelService().create(annuel);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la création avec paramètres corrects " + ex.toString());
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            Assert.assertEquals(false, annuel.isNew());
            Assert.assertNotNull(annuel.getId());
            Assert.assertNotNull(annuel.getIdParametreAnnuel());
            Assert.assertEquals("2020", annuel.getAnneeParametre());
            Assert.assertEquals(IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ASSU_COUPLE,
                    annuel.getCodeTypeParametre());
            Assert.assertEquals("110", annuel.getValeurParametre());
            Assert.assertEquals("", annuel.getValeurParametreString());

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
    @Test
    @Ignore
    public void testDelete() {
        try {
            SimpleParametreAnnuel annuel = null;
            // Effacement d'un model null
            // Attente de résultat >> exception typée
            boolean bModelException = false;
            try {
                annuel = AmalServiceLocator.getParametreAnnuelService().delete(annuel);
            } catch (ParametreAnnuelException ex) {
                bModelException = true;
            }
            Assert.assertEquals(true, bModelException);

            // Effacement d'un model nouveau >> isNew = true
            // Attente de résultat >> exception du framework
            annuel = new SimpleParametreAnnuel();
            annuel.setAnneeParametre("2020");
            annuel.setCodeTypeParametre(IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ASSU_COUPLE);
            annuel.setValeurParametre("110");
            annuel.setValeurParametreString("");
            boolean bJadePersistenceException = false;
            try {
                annuel = AmalServiceLocator.getParametreAnnuelService().delete(annuel);
            } catch (JadePersistenceException ex) {
                bJadePersistenceException = true;
            }
            Assert.assertEquals(true, bJadePersistenceException);

            // Effacement d'un model existant
            // Attente de résultat >> effacement OK et is new à true
            annuel = AmalServiceLocator.getParametreAnnuelService().read("1");
            boolean bDeleteOK = true;
            try {
                annuel = AmalServiceLocator.getParametreAnnuelService().delete(annuel);
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
    @Test
    @Ignore
    public void testRead() {
        try {
            // Lecture d'un modèle avec un id bidon
            // Attente de résultat >> model id=-1, tous les autres champs sont null
            SimpleParametreAnnuel annuel = AmalServiceLocator.getParametreAnnuelService().read("-1");
            Assert.assertEquals("-1", annuel.getId());
            Assert.assertNull(annuel.getAnneeParametre());
            Assert.assertNull(annuel.getCodeTypeParametre());
            Assert.assertNull(annuel.getValeurParametre());
            Assert.assertNull(annuel.getValeurParametreString());
            // Lecture d'un modèle avec un id OK
            // Attente de résultat >> un modèle renseigné et exploitable
            annuel = AmalServiceLocator.getParametreAnnuelService().read("1");
            Assert.assertEquals("1", annuel.getId());
            Assert.assertEquals("1", annuel.getIdParametreAnnuel());
            Assert.assertNotNull(annuel.getAnneeParametre());
            Assert.assertNotNull(annuel.getCodeTypeParametre());
            Assert.assertNotNull(annuel.getValeurParametre());
            Assert.assertNotNull(annuel.getValeurParametreString());
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
    @Test
    @Ignore
    public void testSearch() {
        try {
            // Recherche d'un modèle sans critères de recherche
            // Attente de résultat >> nombre de résultat >= 1
            int iNbResults = 0;
            SimpleParametreAnnuelSearch searched = new SimpleParametreAnnuelSearch();
            searched = AmalServiceLocator.getParametreAnnuelService().search(searched);
            iNbResults = searched.getSize();
            Assert.assertEquals(true, (iNbResults >= 1));

            // Recherche avec des critères de recherche erronés
            // Attente de résultat >> nombre de résultat = 0
            searched = new SimpleParametreAnnuelSearch();
            searched.setForAnneeParametre("-22");
            searched = AmalServiceLocator.getParametreAnnuelService().search(searched);
            iNbResults = searched.getSize();
            Assert.assertEquals(true, (iNbResults <= 0));

            // Recherche avec des critères de recherche ciblés
            // Attente de résultat >> nombre de résultat = 1
            // Attente de résultat >> résultat exploitable (modèle)
            searched = new SimpleParametreAnnuelSearch();
            searched.setForAnneeParametre("2010");
            searched.setForCodeTypeParametre(IAMParametresAnnuels.CS_MONTANT_AVEC_ENFANT_CHARGE);
            searched = AmalServiceLocator.getParametreAnnuelService().search(searched);
            iNbResults = searched.getSize();
            Assert.assertEquals(true, (iNbResults == 1));
            // Attente de résultat >> un modèle renseigné et exploitable
            SimpleParametreAnnuel annuel = (SimpleParametreAnnuel) searched.getSearchResults()[0];
            Assert.assertNotNull(annuel.getId());
            Assert.assertNotNull(annuel.getIdParametreAnnuel());
            Assert.assertNotNull(annuel.getAnneeParametre());
            Assert.assertNotNull(annuel.getCodeTypeParametre());
            Assert.assertNotNull(annuel.getValeurParametre());
            Assert.assertNotNull(annuel.getValeurParametreString());
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
    @Test
    @Ignore
    public void testUpdate() {
        try {
            // Mise à jour d'un modèle nouveau >> isNew = true
            // Attente de résultat >> persistence réagit
            SimpleParametreAnnuel annuel = new SimpleParametreAnnuel();
            annuel.setAnneeParametre("2020");
            annuel.setCodeTypeParametre(IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ASSU_COUPLE);
            annuel.setValeurParametre("110");
            annuel.setValeurParametreString("");
            try {
                annuel = AmalServiceLocator.getParametreAnnuelService().update(annuel);
                Assert.fail("Exception non soulevée lors de la mise à jour d'un modèle isNew ");
            } catch (Exception ex) {
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            // Mise à jour d'un modèle existant avec des paramètres erronés
            // Attente de résultat >> Message dans JadeThread levé par le checker
            annuel = AmalServiceLocator.getParametreAnnuelService().read("1");
            annuel.setAnneeParametre(null);
            try {
                annuel = AmalServiceLocator.getParametreAnnuelService().update(annuel);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la mise à jour avec paramètres erronés " + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Mise à jour d'un model existant avec des paramètres corrects
            // Attente de résultat >> modèle renseigné en retour = modèle de base updaté
            annuel = AmalServiceLocator.getParametreAnnuelService().read("1");
            annuel.setAnneeParametre("2020");
            annuel.setCodeTypeParametre(IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ASSU_COUPLE);
            annuel.setValeurParametre("110");
            annuel.setValeurParametreString("");
            try {
                annuel = AmalServiceLocator.getParametreAnnuelService().update(annuel);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la mise à jour d'une annonce avec paramètres corrects");
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            Assert.assertEquals(false, annuel.isNew());
            Assert.assertEquals("1", annuel.getId());
            Assert.assertEquals("1", annuel.getIdParametreAnnuel());
            Assert.assertEquals("2020", annuel.getAnneeParametre());
            Assert.assertEquals(IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ASSU_COUPLE,
                    annuel.getCodeTypeParametre());
            Assert.assertEquals("110", annuel.getValeurParametre());
            Assert.assertEquals("", annuel.getValeurParametreString());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }
    }

}
