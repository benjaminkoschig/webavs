package ch.globaz.al.businessimpl.rafam.sedex.handler;

import java.util.Map;
import al.ch.ech.xmlns.ech_0104_69._4.UPISynchronizationRecordType;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

/**
 * Gère le traitement des annonces de synchronisation UPI (69b). Ce type d'annonce doit seulement être enregistré dans
 * liste des annonces, aucun autre traitement n'est effectué
 *
 * @author jts
 *
 */
public class MessageUPISynchronizationNewXSDVersionRecordHandler implements MessageHandler {

    /** Le message à traiter */
    private UPISynchronizationRecordType message;

    /**
     * Constructeur
     *
     * @param message
     *            Le message à traiter
     */
    public MessageUPISynchronizationNewXSDVersionRecordHandler(UPISynchronizationRecordType message) {
        this.message = message;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.businessimpl.rafam.sedex.handler.MessageHandler#execute()
     */
    @Override
    public AnnonceRafamModel traiterMessage(Map<String, Object> params)
            throws JadeApplicationException, JadePersistenceException {

        AnnonceRafamModel annonce = ALServiceLocator.getAnnonceRafamModelService()
                .create(ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce69b(message));
        ALServiceLocator.getAnnoncesRafamNewXSDVersionErrorBusinessService().addErrors(message.getError(), annonce);
        return annonce;
    }

}