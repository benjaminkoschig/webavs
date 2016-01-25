package ch.globaz.al.test.businessimpl.service.models.dossier;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSAllocataire;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.models.dossier.CommentaireModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.Constantes;

/**
 * @author jts
 * 
 */
public class CommentaireModelServiceImplTest {

    /**
     * Commentaire existant en DB
     */
    private String idCommentaire = "2";

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.CommentaireModelServiceImpl#create(ch.globaz.al.business.models.dossier.CommentaireModel)}
     * .
     */
    @Test
    @Ignore
    public void testCreate() {

        try {

            CommentaireModel model = new CommentaireModel();

            /*
             * Mandatory
             */
            ALImplServiceLocator.getCommentaireModelService().create(model);
            assertEquals(3, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            /*
             * DB integrity
             */
            model.setIdDossier("abc");
            model.setTexte(Constantes.STRING_1000);
            model.setType("def");
            ALImplServiceLocator.getCommentaireModelService().create(model);
            assertEquals(3, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            /*
             * CS integrity
             */
            model.setIdDossier("10000000");
            model.setTexte("Commentaire valide");
            model.setType(ALCSAllocataire.PERMIS_B);
            ALImplServiceLocator.getCommentaireModelService().create(model);
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            /*
             * Business integrity
             */
            model.setType(ALCSDossier.COMMENTAIRE_TYPE_DECISION);
            ALImplServiceLocator.getCommentaireModelService().create(model);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            /*
             * Données valides
             */
            model.setIdDossier("151");
            ALImplServiceLocator.getCommentaireModelService().create(model);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.CommentaireModelServiceImpl#delete(ch.globaz.al.business.models.dossier.CommentaireModel)}
     * .
     */
    @Test
    @Ignore
    public void testDelete() {

        try {
            CommentaireModel model = ALImplServiceLocator.getCommentaireModelService().read(idCommentaire);
            ALImplServiceLocator.getCommentaireModelService().delete(model);

            // Permet de contrôler qu'il n'y a pas de message d'erreur
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.CommentaireModelServiceImpl#read(java.lang.String)} .
     */
    @Test
    @Ignore
    public void testRead() {

        try {
            CommentaireModel model = ALImplServiceLocator.getCommentaireModelService().read("1");

            // Permet de contrôler qu'il n'y a pas de message d'erreur
            assertEquals(false, model.isNew());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.CommentaireModelServiceImpl#update(ch.globaz.al.business.models.dossier.CommentaireModel)}
     * .
     */
    @Test
    @Ignore
    public void testUpdate() {
        try {
            CommentaireModel model = ALImplServiceLocator.getCommentaireModelService().read("1");
            model.setTexte("Texte modifié");
            ALImplServiceLocator.getCommentaireModelService().update(model);

            // Permet de contrôler qu'il n'y a pas de message d'erreur
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}
