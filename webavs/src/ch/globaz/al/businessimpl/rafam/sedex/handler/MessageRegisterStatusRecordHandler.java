package ch.globaz.al.businessimpl.rafam.sedex.handler;

import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Map;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.ech.xmlns.ech_0104_69._3.RegisterStatusRecordType;
import ch.globaz.al.business.constantes.enumerations.RafamError;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyStatus;
import ch.globaz.al.business.constantes.enumerations.RafamLegalBasis;
import ch.globaz.al.business.constantes.enumerations.RafamOccupationStatus;
import ch.globaz.al.business.constantes.enumerations.RafamReturnCode;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;

/**
 * Gère le traitement des annonces d'état du registre (69c). Pour ce type la dernière annonce enregistrée est comparée à
 * l'annonce d'état du registre. Si une différence est constatée l'annonce est mise à jour. Dans le cas contraire rien
 * n'est fait.
 * 
 * @author jts
 * 
 */
public class MessageRegisterStatusRecordHandler implements MessageHandler {

    /** Le message à traiter */
    private RegisterStatusRecordType message;

    /**
     * Constructeur
     * 
     * @param message
     *            Le message à traiter
     */
    public MessageRegisterStatusRecordHandler(RegisterStatusRecordType message) {
        this.message = message;
    }

    /**
     * Crée une annonce d'annulation contenant les informations de l'état du registre
     * 
     * @param annonceEtatReg
     *            Annonce d'état du registre
     * @param annonce
     *            Annonce provenant de la base de données
     * 
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private void createMissing68c(RegisterStatusRecordType annonceEtatReg, AnnonceRafamModel annonce)
            throws JadeApplicationException, JadePersistenceException {
        AnnonceRafamModel annonce68c = ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce68c(annonce);
        annonce68c.setInternalErrorMessage("etatReg_missing_68c");
        annonce68c.setEtat(RafamEtatAnnonce.RECU.getCS());
        annonce68c = ALServiceLocator.getAnnonceRafamModelService().create(annonce68c);
        annonce68c = updateAnnonce(annonceEtatReg, annonce68c);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.rafam.sedex.handler.MessageHandler#execute()
     */
    @Override
    public AnnonceRafamModel traiterMessage(Map<String, Object> params) throws JadeApplicationException,
            JadePersistenceException {

        AnnonceRafamModel annonce = ALImplServiceLocator.getAnnoncesRafamSearchService().getLastAnnonceForRecordNumber(
                message.getRecordNumber().toString());

        if ((annonce != null) && !annonce.isNew()) {

            XMLGregorianCalendar messageDate = ((XMLGregorianCalendar) params.get("messageDate"));
            GregorianCalendar annonceDate = ALDateUtils.spyToGregorianCalendar(new BSpy(annonce.getSpy()));

            if (messageDate == null) {
                JadeThread
                        .logError(MessageRegisterStatusRecordHandler.class.getName(),
                                "al.rafam.import.messageDate.notDefined", new String[] { message.getRecordNumber()
                                        .toString() });
                return annonce;
                // si une annonce plus récente existe => pas de traitement
            } else if (RafamEtatAnnonce.A_TRANSMETTRE.equals(RafamEtatAnnonce.getRafamEtatAnnonceCS(annonce.getEtat()))
                    || (messageDate.toGregorianCalendar().compareTo(annonceDate) < 0)) {
                JadeThread.logError(MessageRegisterStatusRecordHandler.class.getName(), "al.rafam.import.hasNewer",
                        new String[] { message.getRecordNumber().toString() });
                return annonce;
            } else {

                // si annonce annulée à la centrale
                if ((message.getCanceled() != null) && (0 == message.getCanceled())) {

                    // on vérifie que la dernière annonce est bien une annulation
                    if (RafamTypeAnnonce._68C_ANNULATION.equals(RafamTypeAnnonce.getRafamTypeAnnonce(annonce
                            .getTypeAnnonce()))) {
                        annonce = updateAnnonce(message, annonce);
                        // si l'annonce WebAF n'est pas une annulation, création de l'annonce manquante
                    } else {
                        createMissing68c(message, annonce);
                    }
                } else {
                    // si la dernière annonce est de type annulation alors que l'annonce n'a pas été annulée à la
                    // centrale
                    if (RafamTypeAnnonce._68C_ANNULATION.equals(RafamTypeAnnonce.getRafamTypeAnnonce(annonce
                            .getTypeAnnonce()))) {
                        ALServiceLocator.getAnnonceRafamModelService().delete(annonce);
                        traiterMessage(params);
                    } else {
                        annonce = updateAnnonce(message, annonce);
                    }
                }

                validate(annonce);
                return annonce;
            }
        } else {
            // si l'annonce n'est pas annulée à la centrale log d'un message indiquant que l'annonce n'existe pas
            // Les annonce annulée sont ignorées
            if ((message.getCanceled() == null) || (1 == message.getCanceled())) {
                JadeThread.logError(MessageRegisterStatusRecordHandler.class.getName(),
                        "al.rafam.import.unknownRecordNumber", new String[] { message.getRecordNumber().toString() });
            }
            return annonce;
        }

    }

    /**
     * Met à jour l'annonce passée en paramètre avec les informations contenues dans une annonce d'état du registre
     * 
     * @param annonceEtatReg
     *            Annonce d'état du registre
     * @param annonce
     *            l'annonce à mettre à jour
     * @return L'annonce mise à jour
     * 
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private AnnonceRafamModel updateAnnonce(RegisterStatusRecordType annonceEtatReg, AnnonceRafamModel annonce)
            throws JadeApplicationException, JadePersistenceException {

        if (!annonceEtatReg.getRecordNumber().toString().equals(annonce.getRecordNumber())) {
            throw new ALAnnonceRafamException(
                    "MessageRegisterStatusRecordHandler#updateAnnonce : record numbers are diffrents");
        }

        boolean somethingHasChanged = false;

        // FamilyAllowanceType
        if (JadeStringUtil.isBlankOrZero(annonce.getGenrePrestation())
                || !RafamFamilyAllowanceType.getFamilyAllowanceType(message.getFamilyAllowanceType()).equals(
                        RafamFamilyAllowanceType.getFamilyAllowanceType(annonce.getGenrePrestation()))) {
            annonce.setGenrePrestation(RafamFamilyAllowanceType
                    .getFamilyAllowanceType(message.getFamilyAllowanceType()).getCodeCentrale());
            somethingHasChanged = true;
        }

        // child

        // // officialName
        if (!JadeStringUtil.isBlank(message.getChild().getOfficialName())
                && !message.getChild().getOfficialName().equals(annonce.getNomEnfant())) {
            annonce.setNomEnfant(message.getChild().getOfficialName());
            somethingHasChanged = true;
        }

        // // firstName
        if (!JadeStringUtil.isBlank(message.getChild().getFirstName())
                && !message.getChild().getFirstName().equals(annonce.getPrenomEnfant())) {
            annonce.setPrenomEnfant(message.getChild().getFirstName());
            somethingHasChanged = true;
        }

        // //sex
        if (!JadeStringUtil.isBlank(message.getChild().getSex())) {
            String CSSexeEtatReg = ALImplServiceLocator.getAnnonceRafamBusinessService().getSexeCS(
                    message.getChild().getSex());

            if (!CSSexeEtatReg.equals(annonce.getSexeEnfant())) {
                annonce.setSexeEnfant(CSSexeEtatReg);
                somethingHasChanged = true;
            }
        }

        // //dateOfBirth
        if ((message.getChild().getDateOfBirth() != null)
                && (message.getChild().getDateOfBirth().getYearMonthDay() != null)) {
            String dateEtatReg = JadeDateUtil.getGlobazFormattedDate(message.getChild().getDateOfBirth()
                    .getYearMonthDay().toGregorianCalendar().getTime());

            if (!dateEtatReg.equals(annonce.getDateNaissanceEnfant())) {
                annonce.setDateNaissanceEnfant(dateEtatReg);
                somethingHasChanged = true;
            }

        }

        // //dateOfDeath
        if ((message.getChild().getDateOfDeath() != null)
                && (message.getChild().getDateOfDeath().getYearMonthDay() != null)) {
            String dateEtatReg = JadeDateUtil.getGlobazFormattedDate(message.getChild().getDateOfDeath()
                    .getYearMonthDay().toGregorianCalendar().getTime());

            if (!dateEtatReg.equals(annonce.getDateMortEnfant())) {
                annonce.setDateMortEnfant(dateEtatReg);
                somethingHasChanged = true;
            }
        }

        // legalBasis
        // //law
        if (JadeStringUtil.isBlankOrZero(annonce.getBaseLegale())
                || !RafamLegalBasis.getLegalBasis(message.getLegalBasis().getLaw()).equals(
                        RafamLegalBasis.getLegalBasis(annonce.getBaseLegale()))) {
            annonce.setBaseLegale(RafamLegalBasis.getLegalBasis(message.getLegalBasis().getLaw()).getCodeCentrale());
            somethingHasChanged = true;
        }

        // //canton
        if ((message.getLegalBasis().getCanton() != null)
                && !message.getLegalBasis().getCanton().value().equals(annonce.getCanton())) {
            annonce.setCanton(message.getLegalBasis().getCanton().value());
            somethingHasChanged = true;
        }

        // validityPeriod
        if (message.getValidityPeriod() != null) {

            // //start
            if (!ALDateUtils.XMLGregorianCalendarToGlobazDate(message.getValidityPeriod().getStart()).equals(
                    annonce.getDebutDroit())) {
                annonce.setDebutDroit(ALDateUtils.XMLGregorianCalendarToGlobazDate(message.getValidityPeriod()
                        .getStart()));
                somethingHasChanged = true;
            }

            // //end
            if (!ALDateUtils.XMLGregorianCalendarToGlobazDate(message.getValidityPeriod().getEnd()).equals(
                    annonce.getEcheanceDroit())) {
                annonce.setEcheanceDroit(ALDateUtils.XMLGregorianCalendarToGlobazDate(message.getValidityPeriod()
                        .getEnd()));
                somethingHasChanged = true;
            }

        }

        // beneficiary
        // // officialName
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getOfficialName())
                && !message.getBeneficiary().getOfficialName().equals(annonce.getNomAllocataire())) {
            annonce.setNomAllocataire(message.getBeneficiary().getOfficialName());
            somethingHasChanged = true;
        }

        // // firstName
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getFirstName())
                && !message.getBeneficiary().getFirstName().equals(annonce.getPrenomAllocataire())) {
            annonce.setPrenomAllocataire(message.getBeneficiary().getFirstName());
            somethingHasChanged = true;
        }

        // //sex
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getSex())) {
            String CSSexeEtatReg = ALImplServiceLocator.getAnnonceRafamBusinessService().getSexeCS(
                    message.getBeneficiary().getSex());

            if (!CSSexeEtatReg.equals(annonce.getSexeAllocataire())) {
                annonce.setSexeAllocataire(CSSexeEtatReg);
                somethingHasChanged = true;
            }
        }

        // //dateOfBirth
        if ((message.getBeneficiary().getDateOfBirth() != null)
                && (message.getBeneficiary().getDateOfBirth().getYearMonthDay() != null)) {
            String dateEtatReg = JadeDateUtil.getGlobazFormattedDate(message.getBeneficiary().getDateOfBirth()
                    .getYearMonthDay().toGregorianCalendar().getTime());

            if (!dateEtatReg.equals(annonce.getDateNaissanceAllocataire())) {
                annonce.setDateNaissanceAllocataire(dateEtatReg);
                somethingHasChanged = true;
            }

        }

        // //dateOfDeath
        if (message.getBeneficiary().getDateOfDeath() != null) {
            String dateEtatReg = JadeDateUtil.getGlobazFormattedDate(message.getBeneficiary().getDateOfDeath()
                    .getYearMonthDay().toGregorianCalendar().getTime());

            if (!dateEtatReg.equals(annonce.getDateMortAllocataire())) {
                annonce.setDateMortAllocataire(dateEtatReg);
                somethingHasChanged = true;
            }
        }

        // //familialStatus
        if (JadeStringUtil.isBlankOrZero(annonce.getCodeStatutFamilial())
                || !RafamFamilyStatus.getFamilyStatus(message.getBeneficiary().getFamilialStatus()).equals(
                        RafamFamilyStatus.getFamilyStatus(annonce.getCodeStatutFamilial()))) {
            annonce.setCodeStatutFamilial(RafamFamilyStatus.getFamilyStatus(
                    message.getBeneficiary().getFamilialStatus()).getCodeCentrale());
            somethingHasChanged = true;
        }

        // //occupationStatus
        if (JadeStringUtil.isBlankOrZero(annonce.getCodeTypeActivite())
                || !RafamOccupationStatus.getOccupationStatus(message.getBeneficiary().getOccupationStatus()).equals(
                        RafamOccupationStatus.getOccupationStatus(annonce.getCodeTypeActivite()))) {
            annonce.setCodeTypeActivite(RafamOccupationStatus.getOccupationStatus(
                    message.getBeneficiary().getOccupationStatus()).getCodeCentrale());
            somethingHasChanged = true;
        }

        // creationDate
        if (!JadeDateUtil.getGlobazFormattedDate(message.getCreationDate().toGregorianCalendar().getTime()).equals(
                annonce.getDateCreation())) {
            annonce.setDateCreation(JadeDateUtil.getGlobazFormattedDate(message.getCreationDate().toGregorianCalendar()
                    .getTime()));
            somethingHasChanged = true;
        }

        // mutationDate
        if ((message.getMutationDate() != null)
                && !JadeDateUtil.getGlobazFormattedDate(message.getMutationDate().toGregorianCalendar().getTime())
                        .equals(annonce.getDateMutation())) {
            annonce.setDateMutation(JadeDateUtil.getGlobazFormattedDate(message.getMutationDate().toGregorianCalendar()
                    .getTime()));
            somethingHasChanged = true;
        }

        // return code
        if ((message.getReturnCode() != null)
                && !RafamReturnCode.getRafamReturnCode(message.getReturnCode().toString()).equals(
                        RafamReturnCode.getRafamReturnCode(annonce.getCodeRetour()))) {
            annonce.setCodeRetour(RafamReturnCode.getRafamReturnCode(message.getReturnCode().toString()).getCode());
            somethingHasChanged = true;
        }

        // office identifier
        if (JadeStringUtil.isBlankOrZero(annonce.getOfficeIdentifier())
                || !message.getDeliveryOffice().getOfficeIdentifier().equals(annonce.getOfficeIdentifier())) {
            annonce.setOfficeIdentifier(message.getDeliveryOffice().getOfficeIdentifier());
            somethingHasChanged = true;
        }

        // branche
        if (JadeStringUtil.isBlankOrZero(annonce.getOfficeBranch())
                || !String.valueOf(message.getDeliveryOffice().getBranch()).equals(annonce.getOfficeIdentifier())) {
            annonce.setOfficeBranch(String.valueOf(message.getDeliveryOffice().getBranch()));
            somethingHasChanged = true;
        }

        // legal office
        if (JadeStringUtil.isBlankOrZero(annonce.getLegalOffice())
                || !message.getLegalOffice().getOfficeIdentifier().equals(annonce.getLegalOffice())) {
            annonce.setLegalOffice(message.getLegalOffice().getOfficeIdentifier());
            somethingHasChanged = true;
        }

        // error
        if ((message.getError() == null) || message.getError().isEmpty()) {
            ALServiceLocator.getErreurAnnonceRafamModelService().deleteForIdAnnonce(annonce.getIdAnnonce());
            if (message.getReturnCode() == null) {
                if (!RafamReturnCode.TRAITE.equals(RafamReturnCode.getRafamReturnCode(annonce.getCodeRetour()))) {
                    annonce.setCodeRetour(RafamReturnCode.TRAITE.getCode());
                    somethingHasChanged = true;
                }
            }

        } else {

            boolean changeState = true;

            ArrayList<String> erreurs = ALServiceLocator.getAnnoncesRafamErrorBusinessService().getErrorsFromList(
                    message.getError());
            erreurs.remove(RafamError._212_ALLOCATION_DIFFERENTIELLE_SANS_ALLOCATION_DE_BASE_MEME_PERIODE.getCode());

            // l'annonce enregistrée a une erreur 212 uniquement, est validée et que le message en cours de traitement
            // n'a pas d'autre erreur qu'une 212
            if (ALServiceLocator.getAnnoncesRafamErrorBusinessService().hasError(annonce.getId(),
                    RafamError._212_ALLOCATION_DIFFERENTIELLE_SANS_ALLOCATION_DE_BASE_MEME_PERIODE.getCode())
                    && !ALServiceLocator.getAnnoncesRafamErrorBusinessService().hasErrorOfOtherType(annonce.getId(),
                            RafamError._212_ALLOCATION_DIFFERENTIELLE_SANS_ALLOCATION_DE_BASE_MEME_PERIODE.getCode())
                    && RafamEtatAnnonce.VALIDE.getCS().equals(annonce.getEtat()) && (erreurs.size() == 0)) {
                changeState = false;
            }

            if (!ALServiceLocator.getAnnoncesRafamErrorBusinessService().hasError(annonce.getId(), null)) {
                somethingHasChanged = true;
            }

            ALServiceLocator.getErreurAnnonceRafamModelService().deleteForIdAnnonce(annonce.getIdAnnonce());
            ALServiceLocator.getAnnoncesRafamErrorBusinessService().addErrors(message.getError(), annonce);

            if (!RafamEtatAnnonce.RECU.equals(RafamEtatAnnonce.getRafamEtatAnnonceCS(annonce.getEtat())) && changeState) {
                annonce.setEtat(RafamEtatAnnonce.RECU.getCS());
                somethingHasChanged = true;
            }

            if (message.getReturnCode() == null) {
                if (!RafamReturnCode.EN_ERREUR.equals(RafamReturnCode.getRafamReturnCode(annonce.getCodeRetour()))) {
                    annonce.setCodeRetour(RafamReturnCode.EN_ERREUR.getCode());
                    somethingHasChanged = true;
                }
            }
        }

        // canceled
        if ((message.getCanceled() == null) && annonce.getCanceled()) {
            annonce.setCanceled(false);
            somethingHasChanged = true;
        } else {

            boolean canceledEtatReg = (0 == message.getCanceled() ? true : false);

            if (canceledEtatReg != annonce.getCanceled()) {
                annonce.setCanceled(canceledEtatReg);
                somethingHasChanged = true;
            }

            if (canceledEtatReg) {
                annonce.setCodeRetour(RafamReturnCode.ANNULEE.getCode());
            }
        }

        if (somethingHasChanged) {
            annonce.setInternalErrorMessage("etatReg_update");
            ALServiceLocator.getAnnonceRafamModelService().update(annonce);
        }

        return annonce;
    }

    /**
     * Met à jour l'état des annonces (validation ou mise en erreur) en fonction de l'état du registre
     * 
     * @param annonceEtatReg
     *            Annonce d'état du registre
     * 
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private void validate(AnnonceRafamModel annonce) throws JadeApplicationException, JadePersistenceException {

        switch (RafamReturnCode.getRafamReturnCode(annonce.getCodeRetour())) {

            case EN_ERREUR:
            case REJETEE:
            case RAPPEL:
                ALImplServiceLocator.getAnnonceRafamBusinessService().validateForRecordNumber(
                        annonce.getRecordNumber(), true);
                break;

            case TRAITE:
            case ANNULEE:
            case EN_ATTENTE:
                ALImplServiceLocator.getAnnonceRafamBusinessService().validateForRecordNumber(
                        annonce.getRecordNumber(), false);
                ALImplServiceLocator.getAnnonceRafamBusinessService().deleteAnnoncesForRecordNumber(
                        annonce.getRecordNumber(), RafamTypeAnnonce._68B_MUTATION, true);
                ALImplServiceLocator.getAnnonceRafamBusinessService().deleteAnnoncesForRecordNumber(
                        annonce.getRecordNumber(), RafamTypeAnnonce._68C_ANNULATION, true);
                ALImplServiceLocator.getAnnonceRafamBusinessService().deleteAnnoncesForRecordNumber(
                        annonce.getRecordNumber(), RafamTypeAnnonce._69D_NOTICE, false);
                break;

            default:
                throw new ALRafamException(
                        "AnnonceRafamBusinessServiceImpl#validate : this return code is not supported");
        }
    }
}
