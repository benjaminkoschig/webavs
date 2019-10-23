package ch.globaz.al.businessimpl.rafam.handlers.afdelegue;

import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyStatus;
import ch.globaz.al.business.constantes.enumerations.RafamLegalBasis;
import ch.globaz.al.business.constantes.enumerations.RafamOccupationStatus;
import ch.globaz.al.business.constantes.enumerations.RafamReturnCode;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.ComplementDelegueModel;
import ch.globaz.al.business.models.rafam.ComplementDelegueSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rafam.InitAnnoncesRafamService;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafamDelegue;
import ch.globaz.al.businessimpl.rafam.handlers.AnnonceHandlerAbstract;
import ch.globaz.al.businessimpl.rafam.handlers.AnnoncesChangeChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

/**
 * Classe de base pour la gestion de l'enregistrement des annonces RAFam provenant d'un employeur délégué. Elle contient
 * les méthodes communes auxdifférents cas possibles (Enfant, Formation, Naissance, ...)
 *
 * @author gmo
 */
public class AnnonceDelegueHandler extends AnnonceHandlerAbstract {

    private ContextAnnonceRafamDelegue context = null;

    public AnnonceDelegueHandler(ContextAnnonceRafamDelegue context) {
        super.context = context;
        this.context = context;

    }

    private String contextRefNumberToRecordNumber() {
        return context.getIdEmployeur().concat(context.getAllowance().getAllowanceRefNumber().substring(2));
    }

    private boolean dateHasChanged(AnnonceRafamModel last, AnnonceRafamModel current) throws JadeApplicationException {

        try {
            if (

                    RafamFamilyAllowanceType.getFamilyAllowanceType(last.getGenrePrestation())
                            .equals(RafamFamilyAllowanceType.ADOPTION)
                            || RafamFamilyAllowanceType.getFamilyAllowanceType(last.getGenrePrestation())
                            .equals(RafamFamilyAllowanceType.NAISSANCE)
                            || RafamFamilyAllowanceType.getFamilyAllowanceType(last.getGenrePrestation())
                            .equals(RafamFamilyAllowanceType.DIFFERENCE_ADOPTION)
                            || RafamFamilyAllowanceType.getFamilyAllowanceType(last.getGenrePrestation())
                            .equals(RafamFamilyAllowanceType.DIFFERENCE_NAISSANCE)) {
                // pas de mutation pour les naissances...
                return false;
            } else if (!last.getDebutDroit()
                    .equals((current == null ? last.getDebutDroit() : current.getDebutDroit()))) {
                return true;
            } else if (!last.getEcheanceDroit()
                    .equals((current == null ? last.getEcheanceDroit() : current.getEcheanceDroit()))) {

                return true;
            } else {
                return false;
            }

        } catch (JadeApplicationException e) {
            throw new ALRafamException(
                    "AnnonceDelegfueHandler#dateHasChanged: erreur lors de la comparaison des dates");
        }
    }

    @Override
    protected void doAnnulation() throws JadeApplicationException, JadePersistenceException {
        if (!AnnoncesChangeChecker.isDateFinDroitExpire(context.getDroit().getDroitModel().getFinDroitForcee())) {
            if (!getLastAnnonce().isNew() && !RafamTypeAnnonce._68C_ANNULATION
                    .equals(RafamTypeAnnonce.getRafamTypeAnnonce(getLastAnnonce().getTypeAnnonce()))) {
                AnnonceRafamModel annonce = ALServiceLocator.getAnnonceRafamModelService().create(
                        ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonceDelegue68c(context.getBeneficiary(),
                                context.getChild(), context.getAllowance(), getLastAnnonce()));
                // TODOv2: gérer l'insertion par le modèle complexe AnnonceRafamDelegueComplexModel
                try {
                    manageComplementAnnonceDelegue(annonce);
                } catch (Exception e) {
                    JadeLogger.warn(this,
                            "Impossible de générer les compléments af-délégué pour l'annonce n°" + annonce.getIdAnnonce());
                }

            } else {

                if (isFichierDelegueDelta()) {
                    if (!getLastAnnonce().isNew() && RafamTypeAnnonce._68C_ANNULATION
                            .equals(RafamTypeAnnonce.getRafamTypeAnnonce(getLastAnnonce().getTypeAnnonce()))) {

                        if (RafamReturnCode.ANNULEE
                                .equals(RafamReturnCode.getRafamReturnCode(getLastAnnonce().getCodeRetour()))) {
                            throw new ALRafamException(
                                    "AnnonceDelegueHandler#doAnnulation: l'annonce est déjà annulée, la demande d'annulation est ignorée");
                        } else {
                            throw new ALRafamException(
                                    "AnnonceDelegueHandler#doAnnulation: une annulation a déjà été demandée, celle-ci est ignorée");
                        }

                    }
                    throw new ALRafamException(
                            "AnnonceDelegueHandler#doAnnulation: l'annonce n'a jamais été enregistrée, la demande d'annulation est ignorée");
                }
            }
        }
    }

    @Override
    protected void doCreation() throws JadeApplicationException, JadePersistenceException {
        if (isCurrentAllowanceTypeActive() && !AnnoncesChangeChecker.isDateFinDroitExpire(context.getDroit().getDroitModel().getFinDroitForcee())) {

            AnnonceRafamModel annonce = ALImplServiceLocator.getInitAnnoncesRafamService()
                    .initAnnonceDelegue68a(context.getBeneficiary(), context.getChild(), context.getAllowance());
            // si la date d'échéance est avant DATE_FIN_MINIMUM et que c'est pas une alloc nais/diff nais acce/diff acce
            if (!JadeNumericUtil.isEmptyOrZero(annonce.getEcheanceDroit())
                    && !JadeDateUtil.isDateBefore(ALConstRafam.DATE_FIN_MINIMUM, annonce.getEcheanceDroit())
                    && !(RafamFamilyAllowanceType.getFamilyAllowanceType(annonce.getGenrePrestation())
                    .equals(RafamFamilyAllowanceType.ADOPTION)
                    || RafamFamilyAllowanceType.getFamilyAllowanceType(annonce.getGenrePrestation())
                    .equals(RafamFamilyAllowanceType.NAISSANCE)
                    || RafamFamilyAllowanceType.getFamilyAllowanceType(annonce.getGenrePrestation())
                    .equals(RafamFamilyAllowanceType.DIFFERENCE_ADOPTION)
                    || RafamFamilyAllowanceType.getFamilyAllowanceType(annonce.getGenrePrestation())
                    .equals(RafamFamilyAllowanceType.DIFFERENCE_NAISSANCE))

            ) {
                throw new ALRafamException(
                        "AnnonceDelegueHandler#doCreation: la période annoncée ne doit pas être antérieure à 2011");

            }

            annonce = ALServiceLocator.getAnnonceRafamModelService().create(annonce);
            if (!annonce.isNew()) {
                annonce.setInternalOfficeReference(
                        "ED-" + context.getIdEmployeur() + "-" + context.getAllowance().getAllowanceRefNumber());
                annonce.setRecordNumber(contextRefNumberToRecordNumber());
                ALServiceLocator.getAnnonceRafamModelService().update(annonce);
            }
            // TODO: gérer l'insertion par le modèle complexe AnnonceRafamDelegueComplexModel
            try {
                manageComplementAnnonceDelegue(annonce);
            } catch (Exception e) {
                JadeLogger.warn(this,
                        "Impossible de générer les compléments af-délégué pour l'annonce n°" + annonce.getIdAnnonce());
            }

        }
    }

    @Override
    protected void doModification() throws JadeApplicationException, JadePersistenceException {

        AnnonceRafamModel annonce = ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonceDelegue68b(
                context.getBeneficiary(), context.getChild(), context.getAllowance(), getLastAnnonce());
        annonce.setInternalOfficeReference(
                "ED-" + context.getIdEmployeur() + "-" + context.getAllowance().getAllowanceRefNumber());

        if (!JadeNumericUtil.isEmptyOrZero(annonce.getEcheanceDroit())
                && !JadeDateUtil.isDateBefore(ALConstRafam.DATE_FIN_MINIMUM, annonce.getEcheanceDroit())) {
            throw new ALRafamException(
                    "AnnonceDelegueHandler#doModification: la période annoncée ne doit pas être antérieure à 2011");

        }

        if (hasChanged(annonce)) {
            ALServiceLocator.getAnnonceRafamModelService().create(annonce);
            // TODO: gérer l'insertion par le modèle complexe AnnonceRafamDelegueComplexModel
            try {
                manageComplementAnnonceDelegue(annonce);
            } catch (Exception e) {
                JadeLogger.warn(this,
                        "Impossible de générer les compléments af-délégué pour l'annonce n°" + annonce.getIdAnnonce());
            }
        } else {
            if (isFichierDelegueDelta()) {
                JadeLogger.warn(this, "l'annonce " + context.getAllowance().getAllowanceRefNumber()
                        + " ne présente aucune mutation et n'a pas été transmise");
                throw new ALRafamException(
                        "AnnonceDelegueHandler#doModification: l'annonce ne présente aucun changement et n'a pas été transmise");
            }

        }
    }

    @Override
    protected AnnonceRafamModel getLastAnnonce() throws JadeApplicationException, JadePersistenceException {
        if (lastAnnonce == null) {

            AnnonceRafamModel last = ALImplServiceLocator.getAnnoncesRafamSearchService()
                    .getLastAnnonceForRecordNumber(contextRefNumberToRecordNumber());

            if (RafamTypeAnnonce._68C_ANNULATION.getCode().equals(last.getTypeAnnonce())) {
                throw new ALRafamException(
                        "AnnonceDelegueHandlerAbstract#getLastAnnonce : 68c => aucune annonce active trouvée");
            } else if (last.isNew()) {
                // throw new ALRafamException(
                // "AnnonceDelegueHandlerAbstract#getLastAnnonce : - => aucune annonce active trouvée");
            }

            lastAnnonce = last;
        }

        return lastAnnonce;
    }

    @Override
    protected RafamFamilyAllowanceType getType() throws JadeApplicationException {

        // TODO: voir si on gère le noeud beneficiary en entier ou si une allowance
        return RafamFamilyAllowanceType.getFamilyAllowanceType(
                context.getBeneficiary().getChild().get(0).getAllowance().get(0).getAllowanceType());
    }

    protected boolean hasChanged(AnnonceRafamModel annonce) throws JadeApplicationException, JadePersistenceException {
        InitAnnoncesRafamService s = ALImplServiceLocator.getInitAnnoncesRafamService();
        AnnonceRafamModel last = getLastAnnonce();

        if (last.isNew() || AnnoncesChangeChecker.isDateFinDroitExpire(last.getEcheanceDroit())) {
            return false;
        } else {
            if (!RafamFamilyAllowanceType.getFamilyAllowanceType(last.getGenrePrestation())
                    .equals(RafamFamilyAllowanceType.getFamilyAllowanceType(annonce.getGenrePrestation()))) {
                throw new ALRafamException(
                        "AnnonceDelegueHandler#hasChanged : le type d'allocation ne peut pas être changé pour un même recordNumber");
            } else if (!RafamLegalBasis.getLegalBasis(last.getBaseLegale())
                    .equals(RafamLegalBasis.getLegalBasis(annonce.getBaseLegale()))) {
                throw new ALRafamException(
                        "AnnonceDelegueHandler#hasChanged : l'activité de l'allocataire a été modifiée après l'envoi d'une annonce RAFam. Dans ces cas de figure il est nécessaire de créer un nouveau dossier");
            } else if (dateHasChanged(last, annonce)) {
                return true;
            } else if (!RafamFamilyStatus.getFamilyStatus(last.getCodeStatutFamilial())
                    .equals(RafamFamilyStatus.getFamilyStatus(annonce.getCodeStatutFamilial()))) {
                return true;
            } else if (!RafamOccupationStatus.getOccupationStatus(last.getCodeTypeActivite())
                    .equals(RafamOccupationStatus.getOccupationStatus(annonce.getCodeTypeActivite()))) {
                return true;
            } else if ((annonce != null) && !last.getNssAllocataire().equals(annonce.getNssAllocataire())) {
                return true;

                // Vérification du pays de résidence de l'enfant seulement avec la nouvelle version des schéma xsd
            } else if ("true"
                    .equals(JadePropertiesService.getInstance().getProperty(ALConstRafam.VERSION_ANNONCES_XSD_4_1))
                    && paysResidenceEnfantChanged(annonce, last)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private static boolean paysResidenceEnfantChanged(AnnonceRafamModel annonce, AnnonceRafamModel lastAnnonce) {
        // Si les deux codes pays existent
        if (!JadeStringUtil.isBlankOrZero(annonce.getCodeCentralePaysEnfant())
                && !JadeStringUtil.isBlankOrZero(lastAnnonce.getCodeCentralePaysEnfant())) {
            // On retourne s'ils sont identiques
            return !lastAnnonce.getCodeCentralePaysEnfant().equals(annonce.getCodeCentralePaysEnfant());
            // Si le dernier est vide et que le nouveau est rempli
        } else if (JadeStringUtil.isBlankOrZero(lastAnnonce.getCodeCentralePaysEnfant())
                && !JadeStringUtil.isBlankOrZero(annonce.getCodeCentralePaysEnfant())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean isCurrentAllowanceTypeActive() throws JadeApplicationException {
        return true;
    }

    private boolean isFichierDelegueDelta() {

        String isDeltaProperty = JadePropertiesService.getInstance()
                .getProperty("al.rafam.delegue." + context.getIdEmployeur() + ".fileDelta");
        return (isDeltaProperty != null) && (isDeltaProperty.equals("false")) ? false : true;
    }

    /**
     * Remplit les infos complémentaires à l'annonce utiles pour le fichier de retour au format employeur délégué
     *
     * @param annonce
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private void manageComplementAnnonceDelegue(AnnonceRafamModel annonce)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException {

        ComplementDelegueSearchModel searchComplement = new ComplementDelegueSearchModel();
        searchComplement.setForRecordNumber(annonce.getRecordNumber());
        searchComplement = ALServiceLocator.getComplementDelegueModelService().search(searchComplement);

        if (searchComplement.getSize() > 0) {
            // ne devrait pas arriver, il faudrait logger un avertissement
            ComplementDelegueModel complementAnnonce = (ComplementDelegueModel) searchComplement.getSearchResults()[0];
            complementAnnonce.setRecordNumber(annonce.getRecordNumber());
            complementAnnonce.setAllowanceAmount(context.getAllowance().getAllowanceAmount());
            if (context.getBeneficiary().getBeneficiaryEndDateEmployment() != null) {
                complementAnnonce.setBeneficiaryEndDate(ALDateUtils
                        .XMLGregorianCalendarToGlobazDate(context.getBeneficiary().getBeneficiaryEndDateEmployment()));
            }
            if (context.getBeneficiary().getBeneficiaryStartDateEmployment() != null) {
                complementAnnonce.setBeneficiaryStartDate(ALDateUtils.XMLGregorianCalendarToGlobazDate(
                        context.getBeneficiary().getBeneficiaryStartDateEmployment()));
            }

            complementAnnonce.setMessageCompanyName(context.getHeader().getCompanyName());
            if (context.getHeader().getMessageDate() != null) {
                complementAnnonce.setMessageDate(
                        ALDateUtils.XMLGregorianCalendarToGlobazDate(context.getHeader().getMessageDate()));
            }
            complementAnnonce.setMessageFakId(context.getHeader().getFakID());
            complementAnnonce.setMessageFakName(context.getHeader().getFakName());
            complementAnnonce.setMessageMailResponsiblePerson(context.getHeader().getMailResponsiblePerson());
            complementAnnonce.setMessageNameResponsiblePerson(context.getHeader().getNameResponsiblePerson());
            complementAnnonce.setMessageTelResponsiblePerson(context.getHeader().getTelResponsiblePerson());

            ALServiceLocator.getComplementDelegueModelService().update(complementAnnonce);
        } else {
            ComplementDelegueModel complementAnnonce = new ComplementDelegueModel();
            complementAnnonce.setRecordNumber(annonce.getRecordNumber());
            complementAnnonce.setAllowanceAmount(context.getAllowance().getAllowanceAmount());
            if (context.getBeneficiary().getBeneficiaryEndDateEmployment() != null) {
                complementAnnonce.setBeneficiaryEndDate(ALDateUtils
                        .XMLGregorianCalendarToGlobazDate(context.getBeneficiary().getBeneficiaryEndDateEmployment()));
            }
            if (context.getBeneficiary().getBeneficiaryStartDateEmployment() != null) {
                complementAnnonce.setBeneficiaryStartDate(ALDateUtils.XMLGregorianCalendarToGlobazDate(
                        context.getBeneficiary().getBeneficiaryStartDateEmployment()));
            }
            complementAnnonce.setMessageCompanyName(context.getHeader().getCompanyName());
            if (context.getHeader().getMessageDate() != null) {
                complementAnnonce.setMessageDate(
                        ALDateUtils.XMLGregorianCalendarToGlobazDate(context.getHeader().getMessageDate()));
            }
            complementAnnonce.setMessageFakId(context.getHeader().getFakID());
            complementAnnonce.setMessageFakName(context.getHeader().getFakName());
            complementAnnonce.setMessageMailResponsiblePerson(context.getHeader().getMailResponsiblePerson());
            complementAnnonce.setMessageNameResponsiblePerson(context.getHeader().getNameResponsiblePerson());
            complementAnnonce.setMessageTelResponsiblePerson(context.getHeader().getTelResponsiblePerson());

            ALServiceLocator.getComplementDelegueModelService().create(complementAnnonce);
        }

    }

}
