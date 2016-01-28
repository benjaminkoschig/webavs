package ch.globaz.al.businessimpl.rafam.sedex.handler;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Date;
import java.util.Map;
import ch.ech.xmlns.ech_0104_69._3.NoticeType;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamReturnCode;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;

/**
 * Gère le traitement des annonces de type notice (69d).
 * 
 * L'annonce enregistrée en base corespondant à la notice est mise à jour avec les données de cette dernière.
 * 
 * Si la notice ne comporte pas d'erreur (returnCode = 0), toutes les annonces correspondant au record number sont
 * validée. Si plusieurs enregistrement de type 68b ou 69d existe pour ce record number, les annonces intermédiaires
 * sont supprimées.
 * 
 * @author jts
 * 
 */
public class MessageNoticeHandler implements MessageHandler {

    /** Le message à traiter */
    private NoticeType message;

    /**
     * Constructeur
     * 
     * @param message
     *            Le message à traiter
     */
    public MessageNoticeHandler(NoticeType message) {
        this.message = message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.rafam.sedex.handler.MessageHandler#execute()
     */
    @Override
    public AnnonceRafamModel traiterMessage(Map<String, Object> params) throws JadeApplicationException,
            JadePersistenceException {

        AnnonceRafamModel annonce = ALImplServiceLocator.getAnnoncesRafamSearchService().getAnnonceForNotice(message);

        annonce = updateAnnonce(annonce);

        // pas d'erreur => validation des annonces
        if (message.getReturnCode() == 0) {

            ALImplServiceLocator.getAnnonceRafamBusinessService().validateForRecordNumber(
                    message.getRecordNumber().toString(), false);

            // Cette suppression n'est plus nécessaire, étant donné que les notices ne sont plus enregistrées depuis la
            // v1-11-00. Elle est laissée pour les cas de traitement d'anciennes annonces
            ALImplServiceLocator.getAnnonceRafamBusinessService().deleteAnnoncesForRecordNumber(
                    message.getRecordNumber().toString(), RafamTypeAnnonce._69D_NOTICE, false);

            ALImplServiceLocator.getAnnonceRafamBusinessService().deleteAnnoncesForRecordNumber(
                    message.getRecordNumber().toString(), RafamTypeAnnonce._68B_MUTATION, true);
        }

        return annonce;
    }

    /**
     * Mets à jour l'annonce passée en paramètre avec les données contenues dans la notice
     * 
     * @param annonce
     *            l'annonce à mettre à jour
     * 
     * @return l'annonce mise à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private AnnonceRafamModel updateAnnonce(AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException {

        annonce.setEtat(RafamEtatAnnonce.RECU.getCS());

        // date creation
        if (message.getCreationDate() != null) {
            annonce.setDateCreation(ALDateUtils.XMLGregorianCalendarToGlobazDate(message.getCreationDate()));
        }

        // date mutation
        if (message.getMutationDate() != null) {
            annonce.setDateMutation(ALDateUtils.XMLGregorianCalendarToGlobazDate(message.getMutationDate()));
        }
        // return code
        if ((message.getReturnCode() == 0)
                && RafamTypeAnnonce._68C_ANNULATION.equals(RafamTypeAnnonce.getRafamTypeAnnonce(annonce
                        .getTypeAnnonce()))) {
            annonce.setCodeRetour(RafamReturnCode.ANNULEE.getCode());
        } else {
            annonce.setCodeRetour(RafamReturnCode.getRafamReturnCode(String.valueOf(message.getReturnCode())).getCode());
        }

        // date de réception
        annonce.setDateReception(JadeDateUtil.getGlobazFormattedDate(new Date()));

        // erreur
        ALServiceLocator.getErreurAnnonceRafamModelService().deleteForIdAnnonce(annonce.getIdAnnonce());
        ALServiceLocator.getAnnoncesRafamErrorBusinessService().addErrors(message.getError(), annonce);

        return ALServiceLocator.getAnnonceRafamModelService().update(annonce);
    }
}
