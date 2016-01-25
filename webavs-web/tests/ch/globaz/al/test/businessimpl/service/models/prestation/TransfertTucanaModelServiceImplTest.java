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
        // Défini l'utilisation du thread context
        try {

            TransfertTucanaModel transTucanModel = new TransfertTucanaModel();

            // aucune donnée n'a été ajoutée au modèle. On doit, si la
            // vérification s'est passée correctement, avoir 2 messages d'erreur
            // dans le log pour l'obligation des données
            ALImplServiceLocator.getTransfertTucanaModelService().create(transTucanModel);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();
            // saisie de données obligatoires sans tenir compte de l'intégrité
            // des données de la base
            transTucanModel.setIdDetailPrestation("un");
            transTucanModel.setNumBouclement("janv2000");
            ALImplServiceLocator.getTransfertTucanaModelService().create(transTucanModel);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();
            // saisie de données en respectant l'obligation et l'intégrité mais
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
            // Permet de contrôler qu'il n'y a pas de message d'erreur
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
            // recherche de l'enregistrement à modifier par l'identifiant
            TransfertTucanaModel transTucanModel = ALImplServiceLocator.getTransfertTucanaModelService().read("1");
            // modification à faire
            transTucanModel.setNumBouclement("10.2009");

            // mise à jour de l'enregistrement
            ALImplServiceLocator.getTransfertTucanaModelService().update(transTucanModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
