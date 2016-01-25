package ch.globaz.al.test.businessimpl.service.models.allocataire;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.models.allocataire.RevenuModel;
import ch.globaz.al.business.services.models.allocataire.RevenuModelService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * tests sur les revenus
 * 
 * @author PTA
 * 
 */
public class RevenuModelServiceImplTest {

    /**
     * id d'allocataire présent en DB
     */
    private String idAllocataire = "15974";
    /**
     * id de revenu présent en DB
     */
    private String idRevenu = "2957";

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.allocataire.RevenuModelServiceImpl#create(ch.globaz.al.business.models.allocataire.RevenuModel)}
     * .
     */
    @Ignore
    @Test
    public void testCreate() {
        try {
            RevenuModel revenuModel = new RevenuModel();
            RevenuModelService service = ALImplServiceLocator.getRevenuModelService();

            /*
             * Mandatory
             */
            service.create(revenuModel);
            assertEquals(5, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            /*
             * DatabaseIntegrity
             */
            revenuModel.setDate("20.13.2009");
            revenuModel.setIdAllocataire("aaa");
            revenuModel.setRevenuConjoint(Boolean.TRUE);
            revenuModel.setRevenuIFD(Boolean.TRUE);
            revenuModel.setMontant("aaa");

            service.create(revenuModel);
            assertEquals(3, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            /*
             * BusinessIntegrity
             */
            revenuModel.setIdAllocataire("100000000");
            revenuModel.setDate("20.12.2008");
            revenuModel.setMontant("3456.67");
            ALImplServiceLocator.getRevenuModelService().create(revenuModel);

            // 1 message d'erreur sur l'intégrité métier
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            // toutes les données sont correctes, donc aucun message d'erreur
            revenuModel.setIdAllocataire(idAllocataire);
            ALImplServiceLocator.getRevenuModelService().create(revenuModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.allocataire.RevenuModelServiceImpl#delete(ch.globaz.al.business.models.allocataire.RevenuModel)}
     * .
     */
    @Ignore
    @Test
    public void testDelete() {
        try {
            RevenuModel revenuModel = ALImplServiceLocator.getRevenuModelService().read(idRevenu);
            ALImplServiceLocator.getRevenuModelService().delete(revenuModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.allocataire.RevenuModelServiceImpl#read(java.lang.String)} .
     */
    @Ignore
    @Test
    public void testRead() {
        try {
            RevenuModel model = ALImplServiceLocator.getRevenuModelService().read(idRevenu);
            assertEquals(false, model.isNew());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }

    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.allocataire.RevenuModelServiceImpl#update(ch.globaz.al.business.models.allocataire.RevenuModel)}
     * .
     */
    @Ignore
    @Test
    public void testUpdate() {

        try {
            // recherche de l'enregistrement à modifier par l'identifiant
            RevenuModel revenuModel = ALImplServiceLocator.getRevenuModelService().read(idRevenu);
            ALImplServiceLocator.getRevenuModelService().update(revenuModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
