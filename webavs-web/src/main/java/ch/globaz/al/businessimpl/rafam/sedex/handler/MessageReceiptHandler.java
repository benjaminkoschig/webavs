package ch.globaz.al.businessimpl.rafam.sedex.handler;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Date;
import java.util.Map;
import ch.ech.xmlns.ech_0104_69._3.ReceiptType;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamReturnCode;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;

/**
 * G�re le traitement des annonces de type re�u (69a).
 * 
 * Si le re�u comporte une erreur (returnCode = 1 ou 2) la derni�re annonce correspondant au record number est mise �
 * jour avec les donn�es contenues dans le re�u. Les annonces correspondant au record number sont valid�e, sauf la
 * derni�re afin qu'elle apparaisse sur le protocole d'annonces en erreur.
 * 
 * Si aucune erreur n'est pr�sente (returnCode = 0 ou 4) la derni�re annonce correspondant au record number est mise �
 * jour avec les donn�es contenues dans le re�u. Les annonces correspondant au record number sont valid�es et finalement
 * les annonce 68b interm�diaires sont supprim�es.
 * 
 * @author jts
 * 
 */
public class MessageReceiptHandler implements MessageHandler {

    /** annonce mise � jour par le traitement */
    private AnnonceRafamModel annonceUpdated = null;
    /** Le message � traiter */
    private ReceiptType message;

    /**
     * Constructeur
     * 
     * @param message
     *            Le message � traiter
     */
    public MessageReceiptHandler(ReceiptType message) {
        this.message = message;
    }

    /**
     * Effectue le traitement d'une annonce revenue en attente.
     * 
     * @param annonce
     *            l'annonce � traiter
     * 
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private void processAttente(AnnonceRafamModel annonce) throws JadeApplicationException, JadePersistenceException {

        if (RafamTypeAnnonce._68C_ANNULATION.equals(RafamTypeAnnonce.getRafamTypeAnnonce(annonce.getTypeAnnonce()))) {

            AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
            search.setForRecordNumber(annonce.getRecordNumber());
            JadePersistenceManager.delete(search);
        } else {
            processMessage(annonce);
        }
    }

    /**
     * Mets � jour l'annonce pass�e en param�tre avec les donn�es contenues dans le re�u puis valide les annonces
     * correspondant au record number sauf la derni�re afin qu'elle apparaisse sur le protocole d'annonces en erreur
     * 
     * @param annonce
     *            l'annonce � mettre � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private void processErreur(AnnonceRafamModel annonce) throws JadeApplicationException, JadePersistenceException {

        annonce = updateAnnonce(annonce);

        ALImplServiceLocator.getAnnonceRafamBusinessService().validateForRecordNumber(
                message.getRecordNumber().toString(), true);
    }

    /**
     * Mets � jour l'annonce pass�e en param�tre avec les donn�es contenues, valide les annonces correspondant au record
     * number et finalement supprime les annonces 68b interm�diaires.
     * 
     * @param annonce
     *            l'annonce � mettre � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private void processMessage(AnnonceRafamModel annonce) throws JadeApplicationException, JadePersistenceException {

        annonce = updateAnnonce(annonce);

        ALImplServiceLocator.getAnnonceRafamBusinessService().validateForRecordNumber(
                message.getRecordNumber().toString(), false);

        ALImplServiceLocator.getAnnonceRafamBusinessService().deleteAnnoncesForRecordNumber(
                message.getRecordNumber().toString(), RafamTypeAnnonce._68B_MUTATION, true);

        ALImplServiceLocator.getAnnonceRafamBusinessService().deleteAnnoncesForRecordNumber(annonce.getRecordNumber(),
                RafamTypeAnnonce._69D_NOTICE, false);
    }

    private void processTraiteOuAnnule(AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {

        processMessage(annonce);

        ALImplServiceLocator.getAnnonceRafamBusinessService().validateEnAttenteForRecordNumber(
                message.getRecordNumber().toString(), true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.rafam.sedex.handler.MessageHandler#execute()
     */
    @Override
    public AnnonceRafamModel traiterMessage(Map<String, Object> params) throws JadeApplicationException,
            JadePersistenceException {

        AnnonceRafamModel annonce = ALImplServiceLocator.getAnnoncesRafamSearchService().getAnnonceForReceipt(message);

        switch (RafamReturnCode.getRafamReturnCode(String.valueOf(message.getReturnCode()))) {

            case EN_ERREUR:
            case REJETEE:
            case RAPPEL:
                processErreur(annonce);
                break;

            case TRAITE:
            case ANNULEE:
                processTraiteOuAnnule(annonce);
                break;

            case EN_ATTENTE:
                processAttente(annonce);
                break;

            default:
                throw new ALRafamException("MessageReceiptHandler#execute : this return code is not supported");
        }

        return annonceUpdated;
    }

    /**
     * Mets � jour l'annonce pass�e en param�tre avec les donn�es contenues dans le re�u
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

        // date de r�ception
        annonce.setDateReception(JadeDateUtil.getGlobazFormattedDate(new Date()));

        // return code
        annonce.setCodeRetour(RafamReturnCode.getRafamReturnCode(String.valueOf(message.getReturnCode())).getCode());

        if (RafamReturnCode.ANNULEE.equals(RafamReturnCode.getRafamReturnCode(String.valueOf(message.getReturnCode()))
                .getCode())) {
            annonce.setCanceled(true);
        } else {
            annonce.setCanceled(false);
        }

        // erreurs
        ALServiceLocator.getAnnoncesRafamErrorBusinessService().addErrors(message.getError(), annonce);

        // enfant
        if (!JadeStringUtil.isBlank(message.getChild().getOfficialName())) {
            annonce.setNomEnfant(message.getChild().getOfficialName());
        }
        if (!JadeStringUtil.isBlank(message.getChild().getFirstName())) {
            annonce.setPrenomEnfant(message.getChild().getFirstName());
        }
        if (!JadeStringUtil.isBlank(message.getChild().getSex())) {
            annonce.setSexeEnfant(ALImplServiceLocator.getAnnonceRafamBusinessService().getSexeCS(
                    message.getChild().getSex()));
        }
        if (message.getChild().getDateOfBirth() != null) {
            annonce.setDateNaissanceEnfant(JadeDateUtil.getGlobazFormattedDate(message.getChild().getDateOfBirth()
                    .getYearMonthDay().toGregorianCalendar().getTime()));
        }
        if (message.getChild().getDateOfDeath() != null) {
            annonce.setDateMortEnfant(JadeDateUtil.getGlobazFormattedDate(message.getChild().getDateOfDeath()
                    .getYearMonthDay().toGregorianCalendar().getTime()));
        }

        // allocataire
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getOfficialName())) {
            annonce.setNomAllocataire(message.getBeneficiary().getOfficialName());
        }
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getFirstName())) {
            annonce.setPrenomAllocataire(message.getBeneficiary().getFirstName());
        }
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getSex())) {
            annonce.setSexeAllocataire(ALImplServiceLocator.getAnnonceRafamBusinessService().getSexeCS(
                    message.getBeneficiary().getSex()));
        }
        if ((message.getBeneficiary().getDateOfBirth() != null)
                && (message.getBeneficiary().getDateOfBirth().getYearMonthDay() != null)) {
            annonce.setDateNaissanceAllocataire(JadeDateUtil.getGlobazFormattedDate(message.getBeneficiary()
                    .getDateOfBirth().getYearMonthDay().toGregorianCalendar().getTime()));
        }
        if (message.getBeneficiary().getDateOfDeath() != null) {
            annonce.setDateMortAllocataire(JadeDateUtil.getGlobazFormattedDate(message.getBeneficiary()
                    .getDateOfDeath().getYearMonthDay().toGregorianCalendar().getTime()));
        }

        annonceUpdated = ALServiceLocator.getAnnonceRafamModelService().update(annonce);

        return annonceUpdated;
    }
}
