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

            // TODO revoir les tests les checkers ont été modifié 23.04.2009 PTA
            // aucune donnée n'a été ajouté, on doit avoir 4 messages d'erreur
            // sur l'obligation de données
            ALImplServiceLocator.getCategorieTarifModelService().create(catTarifModel);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            catTarifModel.setCategorieTarif("FDSA");

            // 1 messages d'erreur sur l'intégrité des valeurs de la base

            ALImplServiceLocator.getCategorieTarifModelService().create(catTarifModel);
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            // TODO insertion de données respectant l'obligation et l'intégrité
            // des
            // données de la base, mais ni les codes systèmes et ni l'intégrité
            // business

            ALImplServiceLocator.getCategorieTarifModelService().create(catTarifModel);
            // 2messages d'erreurs ne respectant pas le code system
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            // //TODO insertion de données respectant également les codes
            // systèmes,
            // mais pas l'intégrité business

            // en principe 3 messages d'erreur sur le business integrity
            ALImplServiceLocator.getCategorieTarifModelService().create(catTarifModel);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();
            // TODO respect également des contraintes d'intégrité métier

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
            // Permet de contrôler qu'il n'y a pas de message d'erreur
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
            // recherche de l'enregistrement à modifier par l'identifiant
            CategorieTarifModel catTarifModel = ALImplServiceLocator.getCategorieTarifModelService().read("1");
            // TODO modification à faire

            // mise à jour de l'enregistrement
            ALImplServiceLocator.getCategorieTarifModelService().update(catTarifModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
