package ch.globaz.al.test.businessimpl.service.models.prestation;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.models.prestation.TransfertTucanaModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * @author PTA
 * 
 */
public class TransfertTucanaModelServiceImplTest {

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.TransfertTucanaModelServiceImpl#create(ch.globaz.al.business.models.prestation.TransfertTucanaModel)}
     * .
     */
    @Ignore
    @Test
    public void testCreate() {
        // D�fini l'utilisation du thread context
        try {

            TransfertTucanaModel transTucanModel = new TransfertTucanaModel();

            // aucune donn�e n'a �t� ajout�e au mod�le. On doit, si la
            // v�rification s'est pass�e correctement, avoir 2 messages d'erreur
            // dans le log pour l'obligation des donn�es
            ALImplServiceLocator.getTransfertTucanaModelService().create(transTucanModel);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();
            // saisie de donn�es obligatoires sans tenir compte de l'int�grit�
            // des donn�es de la base
            transTucanModel.setIdDetailPrestation("un");
            transTucanModel.setNumBouclement("janv2000");
            ALImplServiceLocator.getTransfertTucanaModelService().create(transTucanModel);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();
            // saisie de donn�es en respectant l'obligation et l'int�grit� mais
            // IdDetailPrestation non valable
            transTucanModel.setIdDetailPrestation("1");
            transTucanModel.setNumBouclement("2000.01");
            ALImplServiceLocator.getTransfertTucanaModelService().create(transTucanModel);
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.TransfertTucanaModelServiceImpl#delete(ch.globaz.al.business.models.prestation.TransfertTucanaModel)}
     * .
     */
    @Ignore
    @Test
    public void testDelete() {
        try {
            // lecture de l'identifiant de Transfert Tucana
            TransfertTucanaModel transTucanModel = ALImplServiceLocator.getTransfertTucanaModelService().read("1");
            ALImplServiceLocator.getTransfertTucanaModelService().delete(transTucanModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.TransfertTucanaModelServiceImpl#read(java.lang.String)}
     * .
     */
    @Ignore
    @Test
    public void testRead() {
        try {
            ALImplServiceLocator.getTransfertTucanaModelService().read("1");
            // Permet de contr�ler qu'il n'y a pas de message d'erreur
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }

    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.TransfertTucanaModelServiceImpl#update(ch.globaz.al.business.models.prestation.TransfertTucanaModel)}
     * .
     */
    @Ignore
    @Test
    public void testUpdate() {

        try {
            // recherche de l'enregistrement � modifier par l'identifiant
            TransfertTucanaModel transTucanModel = ALImplServiceLocator.getTransfertTucanaModelService().read("1");
            // modification � faire
            transTucanModel.setNumBouclement("10.2009");

            // mise � jour de l'enregistrement
            ALImplServiceLocator.getTransfertTucanaModelService().update(transTucanModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
