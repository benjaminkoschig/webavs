package ch.globaz.al.test.businessimpl.service.models.dossier;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.models.dossier.CopieModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * @author jts
 * 
 */
public class CopieModelServiceImplTest {

    /**
     * Id de copie existant en DB
     */
    private String idCopie = "10";

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.CopieModelServiceImpl#create(ch.globaz.al.business.models.dossier.CopieModel)}
     * .
     */
    @Test
    @Ignore
    public void testCreate() {

        try {

            CopieModel model = new CopieModel();

            /*
             * Mandatory
             */
            ALServiceLocator.getCopieModelService().create(model);
            assertEquals(4, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            /*
             * DB integrity
             */
            model.setIdDossier("abc");
            model.setIdTiersDestinataire("abc");
            model.setOrdreCopie("abc");
            model.setTypeCopie("abc");
            ALServiceLocator.getCopieModelService().create(model);
            assertEquals(4, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            /*
             * CS integrity
             */
            model.setOrdreCopie("1");
            model.setIdDossier("1000000");
            model.setIdTiersDestinataire("1000000");
            model.setTypeCopie(ALCSDossier.ACTIVITE_AGRICULTEUR);
            ALServiceLocator.getCopieModelService().create(model);
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            /*
             * Business integrity
             */
            model.setTypeCopie(ALCSCopie.TYPE_DECISION);
            ALServiceLocator.getCopieModelService().create(model);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            /*
             * données valides
             */
            model.setIdDossier("165");
            model.setIdTiersDestinataire("163424");
            ALServiceLocator.getCopieModelService().create(model);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.CopieModelServiceImpl#delete(ch.globaz.al.business.models.dossier.CopieModel)}
     * .
     */
    @Test
    @Ignore
    public void testDelete() {

        try {
            CopieModel model = ALServiceLocator.getCopieModelService().read(idCopie);
            ALServiceLocator.getCopieModelService().delete(model);

            // Permet de contrôler qu'il n'y a pas de message d'erreur
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.CopieModelServiceImpl#read(java.lang.String)} .
     */
    @Test
    @Ignore
    public void testRead() {

        try {
            CopieModel model = ALServiceLocator.getCopieModelService().read(idCopie);
            assertEquals(false, model.isNew());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.CopieModelServiceImpl#update(ch.globaz.al.business.models.dossier.CopieModel)}
     * .
     */
    @Test
    @Ignore
    public void testUpdate() {
        try {
            CopieModel model = ALServiceLocator.getCopieModelService().read(idCopie);
            ALServiceLocator.getCopieModelService().update(model);

            // Permet de contrôler qu'il n'y a pas de message d'erreur
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}
