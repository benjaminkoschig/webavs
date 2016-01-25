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
 * G�re le traitement des annonces de type notice (69d).
 * 
 * L'annonce enregistr�e en base corespondant � la notice est mise � jour avec les donn�es de cette derni�re.
 * 
 * Si la notice ne comporte pas d'erreur (returnCode = 0), toutes les annonces correspondant au record number sont
 * valid�e. Si plusieurs enregistrement de type 68b ou 69d existe pour ce record number, les annonces interm�diaires
 * sont supprim�es.
 * 
 * @author jts
 * 
 */
public class MessageNoticeHandler implements MessageHandler {

    /** Le message � traiter */
    private NoticeType message;

    /**
     * Constructeur
     * 
     * @param message
     *            Le message � traiter
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

            // Cette suppression n'est plus n�cessaire, �tant donn� que les notices ne sont plus enregistr�es depuis la
            // v1-11-00. Elle est laiss�e pour les cas de traitement d'anciennes annonces
            ALImplServiceLocator.getAnnonceRafamBusinessService().deleteAnnoncesForRecordNumber(
                    message.getRecordNumber().toString(), RafamTypeAnnonce._69D_NOTICE, false);

            ALImplServiceLocator.getAnnonceRafamBusinessService().deleteAnnoncesForRecordNumber(
                    message.getRecordNumber().toString(), RafamTypeAnnonce._68B_MUTATION, true);
        }

        return annonce;
    }

    /**
     * Mets � jour l'annonce pass�e en param�tre avec les donn�es contenues dans la notice
     * 
     * @param annonce
     *            l'annonce � mettre � jour
     * 
     * @return l'annonce mise � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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

        // date de r�ception
        annonce.setDateReception(JadeDateUtil.getGlobazFormattedDate(new Date()));

        // erreur
        ALServiceLocator.getErreurAnnonceRafamModelService().deleteForIdAnnonce(annonce.getIdAnnonce());
        ALServiceLocator.getAnnoncesRafamErrorBusinessService().addErrors(message.getError(), annonce);

        return ALServiceLocator.getAnnonceRafamModelService().update(annonce);
    }
}
