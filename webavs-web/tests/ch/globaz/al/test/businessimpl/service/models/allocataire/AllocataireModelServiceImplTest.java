package ch.globaz.al.test.businessimpl.service.models.allocataire;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSAllocataire;
import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.al.business.constantes.ALCSPays;
import ch.globaz.al.business.models.allocataire.AllocataireModel;
import ch.globaz.al.business.services.models.allocataire.AllocataireModelService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.Constantes;

/**
 * test sur les allocataires
 * 
 * @author PTA
 * 
 */
public class AllocataireModelServiceImplTest {

    /**
     * id d'allocataire présent en DB
     */
    private String idAllocataire = "16248";
    /**
     * id de tiers présent en DB
     */
    private String idTiers = "161556";
    /**
     * id de tiers étranger présent en DB
     */
    private String idTiersEtranger = "161525";

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.allocataire.AllocataireModelServiceImpl#create(ch.globaz.al.business.models.allocataire.AllocataireModel)}
     */
    @Ignore
    @Test
    public void testCreate() {
        try {
            AllocataireModelService service = ALImplServiceLocator.getAllocataireModelService();

            /*
             * Mandatory
             */
            AllocataireModel allocModel = new AllocataireModel();
            service.create(allocModel);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            /*
             * Business Integrity
             */
            allocModel.setCantonResidence("aaa");
            allocModel.setIdTiersAllocataire("aaa");
            allocModel.setPermis("aaa");
            allocModel.setIdPaysResidence("aaa");
            service.create(allocModel);

            assertEquals(4, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            /*
             * Code system Integrity
             */
            allocModel.setCantonResidence(Constantes.WRONG_CS);
            allocModel.setIdTiersAllocataire("100000000");
            allocModel.setPermis(Constantes.WRONG_CS);
            allocModel.setIdPaysResidence(Constantes.WRONG_ID_PAYS);

            service.create(allocModel);

            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            /*
             * business integrity (id du pays, id tiers)
             */
            allocModel.setIdPaysResidence(Constantes.WRONG_ID_PAYS);
            allocModel.setCantonResidence(ALCSCantons.JU);
            allocModel.setPermis(null);

            service.create(allocModel);

            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            /*
             * business integrity (permis, )
             */
            allocModel.setIdTiersAllocataire(idTiers);
            allocModel.setIdPaysResidence(ALCSPays.PAYS_SUISSE);
            allocModel.setIdTiersAllocataire(idTiersEtranger);
            allocModel.setCantonResidence(null);
            service.create(allocModel);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            allocModel.setIdTiersAllocataire(idTiers);
            allocModel.setPermis(ALCSAllocataire.PERMIS_B);
            allocModel.setIdPaysResidence(ALCSPays.PAYS_SUISSE);
            allocModel.setIdTiersAllocataire(idTiersEtranger);
            allocModel.setCantonResidence(ALCSCantons.JU);
            service.create(allocModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }

    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.allocataire.AllocataireModelServiceImpl#delete(ch.globaz.al.business.models.allocataire.AllocataireModel)}
     */
    @Ignore
    @Test
    public void testDelete() {
        try {
            AllocataireModelService service = ALImplServiceLocator.getAllocataireModelService();
            AllocataireModel allocModel = service.read(idAllocataire);
            service.delete(allocModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.allocataire.AllocataireModelServiceImpl#read(String)} .
     */
    @Ignore
    @Test
    public void testRead() {
        try {
            AllocataireModel model = ALImplServiceLocator.getAllocataireModelService().read(idAllocataire);
            assertEquals(false, model.isNew());

        } catch (Exception e) {

            e.printStackTrace();
            fail(e.toString());

        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.allocataire.AllocataireModelServiceImpl#update(ch.globaz.al.business.models.allocataire.AllocataireModel)}
     * .
     */
    @Ignore
    @Test
    public void testUpdate() {

        try {
            AllocataireModelService service = ALImplServiceLocator.getAllocataireModelService();

            AllocataireModel allocModel = service.read(idAllocataire);

            service.update(allocModel);

            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
