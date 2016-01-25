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
 * Tests pour les param�tres annuel
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
     * Test la cr�ation
     */
    @Test
    @Ignore
    public void testCreate() {
        try {
            // Cr�ation avec des param�tres erron�s dans le mod�le
            // Attente de r�sultat >> JadeThread.hasMessage � true
            SimpleParametreAnnuel annuel = new SimpleParametreAnnuel();
            annuel.setAnneeParametre("");
            annuel.setValeurParametre(null);
            try {
                annuel = AmalServiceLocator.getParametreAnnuelService().create(annuel);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la cr�ation avec param�tres erron�s " + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Cr�ation avec des param�tres corrects et boolean renseign�
            // Attente de r�sultat >> mod�le renseign� avec isNew � false
            annuel = new SimpleParametreAnnuel();
            annuel.setAnneeParametre("2020");
            annuel.setCodeTypeParametre(IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ASSU_COUPLE);
            annuel.setValeurParametre("110");
            annuel.setValeurParametreString("");
            try {
                annuel = AmalServiceLocator.getParametreAnnuelService().create(annuel);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la cr�ation avec param�tres corrects " + ex.toString());
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
     * Test de suppression d'un mod�le
     */
    @Test
    @Ignore
    public void testDelete() {
        try {
            SimpleParametreAnnuel annuel = null;
            // Effacement d'un model null
            // Attente de r�sultat >> exception typ�e
            boolean bModelException = false;
            try {
                annuel = AmalServiceLocator.getParametreAnnuelService().delete(annuel);
            } catch (ParametreAnnuelException ex) {
                bModelException = true;
            }
            Assert.assertEquals(true, bModelException);

            // Effacement d'un model nouveau >> isNew = true
            // Attente de r�sultat >> exception du framework
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
            // Attente de r�sultat >> effacement OK et is new � true
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
     * Test de lecture d'un mod�le
     */
    @Test
    @Ignore
    public void testRead() {
        try {
            // Lecture d'un mod�le avec un id bidon
            // Attente de r�sultat >> model id=-1, tous les autres champs sont null
            SimpleParametreAnnuel annuel = AmalServiceLocator.getParametreAnnuelService().read("-1");
            Assert.assertEquals("-1", annuel.getId());
            Assert.assertNull(annuel.getAnneeParametre());
            Assert.assertNull(annuel.getCodeTypeParametre());
            Assert.assertNull(annuel.getValeurParametre());
            Assert.assertNull(annuel.getValeurParametreString());
            // Lecture d'un mod�le avec un id OK
            // Attente de r�sultat >> un mod�le renseign� et exploitable
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
            // Recherche d'un mod�le sans crit�res de recherche
            // Attente de r�sultat >> nombre de r�sultat >= 1
            int iNbResults = 0;
            SimpleParametreAnnuelSearch searched = new SimpleParametreAnnuelSearch();
            searched = AmalServiceLocator.getParametreAnnuelService().search(searched);
            iNbResults = searched.getSize();
            Assert.assertEquals(true, (iNbResults >= 1));

            // Recherche avec des crit�res de recherche erron�s
            // Attente de r�sultat >> nombre de r�sultat = 0
            searched = new SimpleParametreAnnuelSearch();
            searched.setForAnneeParametre("-22");
            searched = AmalServiceLocator.getParametreAnnuelService().search(searched);
            iNbResults = searched.getSize();
            Assert.assertEquals(true, (iNbResults <= 0));

            // Recherche avec des crit�res de recherche cibl�s
            // Attente de r�sultat >> nombre de r�sultat = 1
            // Attente de r�sultat >> r�sultat exploitable (mod�le)
            searched = new SimpleParametreAnnuelSearch();
            searched.setForAnneeParametre("2010");
            searched.setForCodeTypeParametre(IAMParametresAnnuels.CS_MONTANT_AVEC_ENFANT_CHARGE);
            searched = AmalServiceLocator.getParametreAnnuelService().search(searched);
            iNbResults = searched.getSize();
            Assert.assertEquals(true, (iNbResults == 1));
            // Attente de r�sultat >> un mod�le renseign� et exploitable
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
     * Test de mise � jour
     */
    @Test
    @Ignore
    public void testUpdate() {
        try {
            // Mise � jour d'un mod�le nouveau >> isNew = true
            // Attente de r�sultat >> persistence r�agit
            SimpleParametreAnnuel annuel = new SimpleParametreAnnuel();
            annuel.setAnneeParametre("2020");
            annuel.setCodeTypeParametre(IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ASSU_COUPLE);
            annuel.setValeurParametre("110");
            annuel.setValeurParametreString("");
            try {
                annuel = AmalServiceLocator.getParametreAnnuelService().update(annuel);
                Assert.fail("Exception non soulev�e lors de la mise � jour d'un mod�le isNew ");
            } catch (Exception ex) {
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            // Mise � jour d'un mod�le existant avec des param�tres erron�s
            // Attente de r�sultat >> Message dans JadeThread lev� par le checker
            annuel = AmalServiceLocator.getParametreAnnuelService().read("1");
            annuel.setAnneeParametre(null);
            try {
                annuel = AmalServiceLocator.getParametreAnnuelService().update(annuel);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la mise � jour avec param�tres erron�s " + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Mise � jour d'un model existant avec des param�tres corrects
            // Attente de r�sultat >> mod�le renseign� en retour = mod�le de base updat�
            annuel = AmalServiceLocator.getParametreAnnuelService().read("1");
            annuel.setAnneeParametre("2020");
            annuel.setCodeTypeParametre(IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ASSU_COUPLE);
            annuel.setValeurParametre("110");
            annuel.setValeurParametreString("");
            try {
                annuel = AmalServiceLocator.getParametreAnnuelService().update(annuel);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la mise � jour d'une annonce avec param�tres corrects");
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
