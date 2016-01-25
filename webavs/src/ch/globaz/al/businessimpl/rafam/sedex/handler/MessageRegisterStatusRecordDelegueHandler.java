package ch.globaz.al.businessimpl.rafam.sedex.handler;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Map;
import ch.ech.xmlns.ech_0104_69._3.RegisterStatusRecordType;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.models.rafam.AnnonceRafamDelegueComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.models.rafam.ComplementDelegueModel;
import ch.globaz.al.business.models.rafam.ComplementDelegueSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;

public class MessageRegisterStatusRecordDelegueHandler extends MessageEmployeurDelegueHandler {

    public static boolean areAnnoncesDelegueInDb = true;
    public static boolean canSendMailReactivationModuleAfDelegue = false;
    private RegisterStatusRecordType message = null;

    public MessageRegisterStatusRecordDelegueHandler(RegisterStatusRecordType register) {
        message = register;
    }

    private void createComplementDelegue() throws JadeApplicationServiceNotAvailableException,
            JadeApplicationException, JadePersistenceException {
        ComplementDelegueModel complementAnnonce = new ComplementDelegueModel();
        complementAnnonce.setRecordNumber(message.getRecordNumber().toString());
        complementAnnonce.setAllowanceAmount("0.00");
        // complementAnnonce.setBeneficiaryEndDate("01.01.1900");
        complementAnnonce.setBeneficiaryStartDate("01.01.1900");
        complementAnnonce.setMessageCompanyName(ALConstRafam.MESSAGE_ED_DATA_BLANK);

        complementAnnonce.setMessageDate(ALDateUtils.XMLGregorianCalendarToGlobazDate(message.getCreationDate()));

        complementAnnonce.setMessageFakId("000000");
        complementAnnonce.setMessageFakName(ALConstRafam.MESSAGE_ED_DATA_BLANK);

        String email = JadePropertiesService.getInstance().getProperty(
                "al.rafam.delegue." + message.getRecordNumber().toString().substring(0, 2) + "defaultContactMail");
        complementAnnonce.setMessageMailResponsiblePerson(email);
        complementAnnonce.setMessageNameResponsiblePerson(ALConstRafam.MESSAGE_ED_DATA_BLANK);
        complementAnnonce.setMessageTelResponsiblePerson(ALConstRafam.MESSAGE_ED_DATA_BLANK);

        ALServiceLocator.getComplementDelegueModelService().create(complementAnnonce);

    }

    @Override
    public AnnonceRafamModel traiterMessage(Map<String, Object> params) throws JadeApplicationException,
            JadePersistenceException {

        if (!MessageRegisterStatusRecordDelegueHandler.areAnnoncesDelegueInDb) {

            MessageRegisterStatusRecordDelegueHandler.canSendMailReactivationModuleAfDelegue = true;
            annonceImported = ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce69cDelegue(message, true);
            // on ne crée pas les annonces annulées
            if (!annonceImported.getCanceled()) {
                annonceImported = ALServiceLocator.getAnnonceRafamModelService().create(annonceImported);

                ALServiceLocator.getAnnoncesRafamErrorBusinessService().addErrors(message.getError(), annonceImported);
                try {
                    createComplementDelegue();
                } catch (Exception e) {
                    JadeLogger.warn(this, "Impossible de crééer le complément délégué pour l'annonce n°"
                            + annonceImported.getRecordNumber());
                }
            }

        } else {

            annonceImported = ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce69cDelegue(message, false);
            // on gère les cas ou pas de date fourni dans le message sedex (naissance), on récupère la date mémorisée en
            // base
            if ((annonceImported.getDebutDroit() == null) || (annonceImported.getEcheanceDroit() == null)) {
                AnnonceRafamSearchModel searchAnnonce = new AnnonceRafamSearchModel();
                searchAnnonce.setForRecordNumber(annonceImported.getRecordNumber());
                searchAnnonce = ALServiceLocator.getAnnonceRafamModelService().search(searchAnnonce);
                if (searchAnnonce.getSize() > 0) {
                    annonceImported.setDebutDroit(((AnnonceRafamModel) searchAnnonce.getSearchResults()[0])
                            .getDebutDroit());
                    annonceImported.setEcheanceDroit(((AnnonceRafamModel) searchAnnonce.getSearchResults()[0])
                            .getEcheanceDroit());
                }

            }
            // on stocke le code erreur ici, pour pouvoir le récupérer dans le fichier de retour
            // TODO v2 mettre tous les codes erreurs
            if (message.getError().size() > 0) {
                annonceImported.setInternalErrorMessage(Long.toString(message.getError().get(0).getCode()));
            }

            AnnonceRafamDelegueComplexModel annonce = new AnnonceRafamDelegueComplexModel();
            annonce.setAnnonceRafamModel(annonceImported);

            ComplementDelegueSearchModel searchComplement = new ComplementDelegueSearchModel();
            searchComplement.setForRecordNumber(annonceImported.getRecordNumber());
            searchComplement = ALServiceLocator.getComplementDelegueModelService().search(searchComplement);

            if (searchComplement.getSize() > 0) {
                annonce.setComplementDelegueModel((ComplementDelegueModel) searchComplement.getSearchResults()[0]);

            }

            MessagesEmployeursDeleguesContainer medc = MessagesEmployeursDeleguesContainer.getInstance();

            medc.addAnnonce(annonce, medc.getNoEmployeurDelegue(annonceImported.getRecordNumber()));

        }

        return annonceImported;
    }

}
