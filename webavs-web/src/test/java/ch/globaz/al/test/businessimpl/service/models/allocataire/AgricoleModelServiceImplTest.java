package ch.globaz.al.test.businessimpl.service.models.allocataire;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.models.allocataire.AgricoleModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * tests sur les agricoles
 * 
 * @author PTA
 * 
 */
public class AgricoleModelServiceImplTest {

    // TODO : pas de données actuellement dans la base, enregistrement ajouté
    // "à la main"
    /**
     * id agricole existant
     */
    private String idAgricole = "1";
    /**
     * id d'un allocataire existant
     */
    private String idAllocataire = "4557";

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.allocataire.AgricoleModelServiceImpl#create(ch.globaz.al.business.models.allocataire.AgricoleModel)}
     */
    @Ignore
    @Test
    public void testCreate() {
        // context de test de création
        try {
            AgricoleModel agriModel = new AgricoleModel();

            /*
             * Mandatory
             */
            ALImplServiceLocator.getAgricoleModelService().create(agriModel);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            /*
             * DatabaseIntegrity
             */
            agriModel.setDomaineMontagne(new Boolean(false));
            agriModel.setIdAllocataire("aaa");

            ALImplServiceLocator.getAgricoleModelService().create(agriModel);
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            /*
             * BusinessIntegrity
             */
            // id allocataire inexistant
            agriModel.setIdAllocataire("1000000");

            ALImplServiceLocator.getAgricoleModelService().create(agriModel);
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            // toutes les règles sont respectées, renvoie 0 messages d'erreur
            agriModel.setIdAllocataire(idAllocataire);
            ALImplServiceLocator.getAgricoleModelService().create(agriModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.allocataire.AgricoleModelServiceImpl#delete(ch.globaz.al.business.models.allocataire.AgricoleModel)}
     */
    @Ignore
    @Test
    public void testDelete() {
        try {
            // lecture de l'id Agricole
            AgricoleModel agriModel = ALImplServiceLocator.getAgricoleModelService().read(idAgricole);
            ALImplServiceLocator.getAgricoleModelService().delete(agriModel);
            AgricoleModel agriModel2 = ALImplServiceLocator.getAgricoleModelService().read(idAgricole);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            assertEquals(true, agriModel2.isNew());

            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.allocataire.AgricoleModelServiceImpl #read(java.lang.String)}
     */
    @Ignore
    @Test
    public void testRead() {
        try {
            AgricoleModel agriModel = ALImplServiceLocator.getAgricoleModelService().read(idAgricole);
            assertEquals(false, agriModel.isNew());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.allocataire.AgricoleModelServiceImpl#update(ch.globaz.al.business.models.allocataire.AgricoleModel)}
     */
    @Ignore
    @Test
    public void testUpdate() {
        // recherche de l'enregistrement à modifier par l'identifiant

        try {
            AgricoleModel agriModel = ALImplServiceLocator.getAgricoleModelService().read(idAgricole);
            // modification à faire
            agriModel.setDomaineMontagne(new Boolean(true));
            // mise à jour de l'enregistrement
            ALImplServiceLocator.getAgricoleModelService().update(agriModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}
