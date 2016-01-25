/**
 * 
 */
package ch.globaz.al.test.businessimpl.service.models.tarif;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.models.tarif.CategorieTarifModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * @author PTA
 * 
 */
public class CategorieTarifModelServiceImplTest {

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.tarif.CategorieTarifModelServiceImpl#create(ch.globaz.al.business.models.tarif.CategorieTarifModel)}
     * .
     */
    @Ignore
    @Test
    public void testCreate() {
        try {
            CategorieTarifModel catTarifModel = new CategorieTarifModel();

            // TODO revoir les tests les checkers ont �t� modifi� 23.04.2009 PTA
            // aucune donn�e n'a �t� ajout�, on doit avoir 4 messages d'erreur
            // sur l'obligation de donn�es
            ALImplServiceLocator.getCategorieTarifModelService().create(catTarifModel);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            catTarifModel.setCategorieTarif("FDSA");

            // 1 messages d'erreur sur l'int�grit� des valeurs de la base

            ALImplServiceLocator.getCategorieTarifModelService().create(catTarifModel);
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            // TODO insertion de donn�es respectant l'obligation et l'int�grit�
            // des
            // donn�es de la base, mais ni les codes syst�mes et ni l'int�grit�
            // business

            ALImplServiceLocator.getCategorieTarifModelService().create(catTarifModel);
            // 2messages d'erreurs ne respectant pas le code system
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            // //TODO insertion de donn�es respectant �galement les codes
            // syst�mes,
            // mais pas l'int�grit� business

            // en principe 3 messages d'erreur sur le business integrity
            ALImplServiceLocator.getCategorieTarifModelService().create(catTarifModel);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();
            // TODO respect �galement des contraintes d'int�grit� m�tier

            ALImplServiceLocator.getCategorieTarifModelService().create(catTarifModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.tarif.CategorieTarifModelServiceImpl#read(java.lang.String)} .
     */
    @Ignore
    @Test
    public void testRead() {
        try {
            ALImplServiceLocator.getCategorieTarifModelService().read("1");
            // Permet de contr�ler qu'il n'y a pas de message d'erreur
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }

    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.tarif.CategorieTarifModelServiceImpl#update(ch.globaz.al.business.models.tarif.CategorieTarifModel)}
     * .
     */
    @Ignore
    @Test
    public void testUpdate() {

        try {
            // recherche de l'enregistrement � modifier par l'identifiant
            CategorieTarifModel catTarifModel = ALImplServiceLocator.getCategorieTarifModelService().read("1");
            // TODO modification � faire

            // mise � jour de l'enregistrement
            ALImplServiceLocator.getCategorieTarifModelService().update(catTarifModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
