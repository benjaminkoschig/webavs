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
     * Test la cr�ation
     */
    public void testCreate() {
        try {
            // Cr�ation avec des param�tres erron�s dans le mod�le
            // Attente de r�sultat >> JadeThread.hasMessage � true
            try {
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la cr�ation avec param�tres erron�s " + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Cr�ation avec des param�tres corrects et boolean renseign�
            // Attente de r�sultat >> mod�le renseign� avec isNew � false
            try {
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la cr�ation avec param�tres corrects " + ex.toString());
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
     * Test de suppression d'un mod�le
     */
    public void testDelete() {
        try {

            // Effacement d'un model null
            // Attente de r�sultat >> exception typ�e
            boolean bModelException = false;
            try {
            } catch (Exception ex) {
                bModelException = true;
            }
            Assert.assertEquals(true, bModelException);

            // Effacement d'un model nouveau >> isNew = true
            // Attente de r�sultat >> exception du framework
            boolean bJadePersistenceException = false;
            try {
            } catch (Exception ex) {
                bJadePersistenceException = true;
            }
            Assert.assertEquals(true, bJadePersistenceException);

            // Effacement d'un model existant
            // Attente de r�sultat >> effacement OK et is new � true
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
     * Test de lecture d'un mod�le
     */
    public void testRead() {
        try {
            // Lecture d'un mod�le avec un id bidon
            // Attente de r�sultat >> model id=-1, tous les autres champs sont null

            // Lecture d'un mod�le avec un id OK
            // Attente de r�sultat >> un mod�le renseign� et exploitable
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
            // Recherche d'un mod�le sans crit�res de recherche
            // Attente de r�sultat >> nombre de r�sultat >= 1
            int iNbResults = 0;
            Assert.assertEquals(true, (iNbResults >= 1));

            // Recherche avec des crit�res de recherche erron�s
            // Attente de r�sultat >> nombre de r�sultat = 0
            Assert.assertEquals(true, (iNbResults <= 0));

            // Recherche avec des crit�res de recherche cibl�s
            // Attente de r�sultat >> nombre de r�sultat = 1
            // Attente de r�sultat >> r�sultat exploitable (mod�le)
            Assert.assertEquals(true, (iNbResults == 1));
            // Attente de r�sultat >> un mod�le renseign� et exploitable
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
            try {
                Assert.fail("Exception non soulev�e lors de la mise � jour d'un mod�le isNew");
            } catch (Exception ex) {
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            // Mise � jour d'un mod�le existant avec des param�tres erron�s
            // Attente de r�sultat >> Message dans JadeThread lev� par le checker
            try {
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la mise � jour avec param�tres erron�s");
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Mise � jour d'un model existant avec des param�tres corrects
            // Attente de r�sultat >> mod�le renseign� en retour = mod�le de base updat�
            try {
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la mise � jour d'une annonce avec param�tres corrects");
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
