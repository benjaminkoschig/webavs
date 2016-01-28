package ch.globaz.al.businessimpl.rafam.sedex.handler;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Map;
import ch.globaz.al.business.models.rafam.AnnonceRafamDelegueComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.rafam.sedex.ImportAnnoncesRafam;

/**
 * G�re le traitement des annonces d'employeurs d�l�gu�s. Ce type d'annonce doit �tre g�r� par un programme ind�pendant.
 * Dans ce but, les annonces sont plac�es dans un conteneur qui sera trait� par la suite (
 * {@link ImportAnnoncesRafam#createFilesEmployeursDelegues}).
 * 
 * @author jts
 * 
 */

public class MessageEmployeurDelegueHandler implements MessageHandler {

    /**
	 * 
	 */
    protected AnnonceRafamModel annonceImported = null;

    public MessageEmployeurDelegueHandler() {

    }

    /**
     * Constructeur
     * 
     * @param message
     *            Le message � traiter
     */
    public MessageEmployeurDelegueHandler(AnnonceRafamModel annonce) {
        annonceImported = annonce;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.rafam.sedex.handler.MessageHandler#execute()
     */
    @Override
    public AnnonceRafamModel traiterMessage(Map<String, Object> params) throws JadeApplicationException,
            JadePersistenceException {

        MessagesEmployeursDeleguesContainer medc = MessagesEmployeursDeleguesContainer.getInstance();

        AnnonceRafamDelegueComplexModel annonce = ALServiceLocator.getAnnonceRafamDelegueComplexModelService().read(
                annonceImported.getIdAnnonce());
        medc.addAnnonce(annonce, medc.getNoEmployeurDelegue(annonceImported.getRecordNumber()));

        return annonceImported;
    }

}
