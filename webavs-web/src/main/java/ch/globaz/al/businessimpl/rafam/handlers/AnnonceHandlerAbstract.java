package ch.globaz.al.businessimpl.rafam.handlers;

import ch.globaz.al.business.constantes.enumerations.*;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Classe de base pour la gestion de l'enregistrement des annonces RAFam. Elle contient les méthodes communes aux
 * différents cas possibles (Enfant, Formation, Naissance, ...)
 * 
 * @author jts
 * 
 */
public abstract class AnnonceHandlerAbstract {

    /** Context contenant toutes les informations nécessaire à la création d'annonces */
    protected ContextAnnonceRafam context = null;

    /** dernière annonce correspondant aux informations contenues dans le context */
    protected AnnonceRafamModel lastAnnonce = null;

    protected void doRadiation(AnnonceRafamModel annonce) throws JadeApplicationException, JadePersistenceException {

        // si la date de début du droit est au-delà de la date de radiation
        if (JadeDateUtil.isDateBefore(context.getDossier().getDossierModel().getFinValidite(), annonce.getDebutDroit())) {

            // si on est dans le cas d'une modification
            if (RafamTypeAnnonce._68B_MUTATION.equals(RafamTypeAnnonce.getRafamTypeAnnonce(annonce.getTypeAnnonce()))) {
                doAnnulation();
            }

        } else {

            boolean finValiditeDossierAvantFinDroit = JadeDateUtil.isDateBefore(context.getDossier().getDossierModel()
                    .getFinValidite(), annonce.getEcheanceDroit());
            String nouvelleEcheance = finValiditeDossierAvantFinDroit ? context.getDossier().getDossierModel()
                    .getFinValidite() : annonce.getEcheanceDroit();

            annonce.setEcheanceDroit(nouvelleEcheance);

            // dans le cas d'une modification on vérifie qu'il y ait eu un changement dans le droit
            if (RafamTypeAnnonce._68B_MUTATION.equals(RafamTypeAnnonce.getRafamTypeAnnonce(annonce.getTypeAnnonce()))) {
                if (AnnoncesChangeChecker.hasChanged(annonce, getLastAnnonce(), context.getDossier(),
                        context.getDroit())) {
                    enregistrerAnnonce(annonce);
                }
            } else if (!JadeDateUtil.isDateBefore(annonce.getDebutDroit(), ALConstRafam.DATE_DEBUT_MINIMUM)
                    && JadeDateUtil.isDateBefore(ALConstRafam.DATE_FIN_MINIMUM, annonce.getEcheanceDroit())) {
                enregistrerAnnonce(annonce);
            }
        }
    }

    /**
     * Crée et enregistre une annonce d'annulation (68c) en se basant sur la dernière annonce correspondant aux
     * informations du context. Si aucune annonce n'existe, rien n'est fait.
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected void doAnnulation() throws JadeApplicationException, JadePersistenceException {
        if (!getLastAnnonce().isNew()
                && !RafamTypeAnnonce._68C_ANNULATION.equals(RafamTypeAnnonce.getRafamTypeAnnonce(getLastAnnonce()
                        .getTypeAnnonce()))) {
            ALServiceLocator.getAnnonceRafamModelService().create(
                    ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce68c(getLastAnnonce(),
                            context.getEtat()));
        }
    }

    /**
     * Crée une annonce de création (68a). Si aucun cas de radiation n'a été traité elle est enregistrée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see AnnonceHandlerAbstract#isRadiation(AnnonceRafamModel)
     */
    protected void doCreation() throws JadeApplicationException, JadePersistenceException {

        if (isCurrentAllowanceTypeActive()) {

            AnnonceRafamModel annonce = ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce68a(
                    context.getDossier(), context.getDroit(), getType(), context.getEtat());

            if (isRadiation()) {
                doRadiation(annonce);
            } else {
                enregistrerAnnonce(annonce);
            }
        }
    }

    /**
     * Crée une annonce de modification (68b). Si aucun cas de radiation n'a été traité et qu'il y a eu des
     * modifications dans le droit ou le dossier depuis l'envoi de la dernière annonce elle est enregistrée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see AnnonceHandlerAbstract#isRadiation(AnnonceRafamModel)
     */
    protected void doModification() throws JadeApplicationException, JadePersistenceException {

        ModificationAction action = getActionModification();

        switch (action) {
            case ANNULATION_ET_CREATION:
                doAnnulation();
                doCreation();
                break;

            case UPDATE_NSS_ET_STANDARD:
                updateNSSAnnonces();
            case STANDARD:
                doModificationStandard();
                break;

            default:
                throw new ALRafamException("AnnonceHandlerAbstract#doModification : unsupported action");
        }
    }

    protected void doModificationStandard() throws JadeApplicationException, JadePersistenceException {

        AnnonceRafamModel annonce = ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce68b(
                context.getDossier(), context.getDroit(), getType(), context.getEtat(),
                context.getEvenementDeclencheur(), getLastAnnonce());

        if (isRadiation()) {
            doRadiation(annonce);
        } else if (AnnoncesChangeChecker
                .hasChanged(annonce, getLastAnnonce(), context.getDossier(), context.getDroit())) {
            enregistrerAnnonce(annonce);
        }
    }

    /**
     * Enregistre l'annonce en persistance. L'évènement déclencheur est remplacé par celui contenu dans le context.
     * 
     * @param annonce
     *            l'annonce à enregistrer
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected void enregistrerAnnonce(AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException {

        annonce.setEvDeclencheur(context.getEvenementDeclencheur().getCS());
        ALServiceLocator.getAnnonceRafamModelService().create(annonce);
    }

    /**
     * Exécute le traitement du handler. L'evénement ayant déclenché l'appel du service de gestion des annonces est
     * analysé afin de déterminer l'action à exécuter
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void creerAnnonce() throws JadeApplicationException, JadePersistenceException {

        if (context.getEvenementDeclencheur().equals(RafamEvDeclencheur.CREATION)) {
            doCreation();
        } else if (context.getEvenementDeclencheur().equals(RafamEvDeclencheur.ANNULATION)) {
            doAnnulation();
        } else {
            switch (getAction()) {
                case CREATION:
                    doCreation();
                    break;

                case MODIFICATION:
                    doModification();
                    break;

                case ANNULATION:
                    doAnnulation();
                    break;

                default:
                    throw new ALRafamException("AnnonceHandlerAbstract#execute : this action is not supported");
            }
        }
    }

    protected void updateNSSAnnonces() throws JadeApplicationException, JadePersistenceException {

        String recordNumber = getLastAnnonce().getRecordNumber();

        AnnonceRafamSearchModel annonces = ALImplServiceLocator.getAnnoncesRafamSearchService()
                .getAnnoncesForRecordNumber(recordNumber);

        for (JadeAbstractModel item : annonces.getSearchResults()) {
            AnnonceRafamModel annonce = (AnnonceRafamModel) item;

            String nssEnfant = context.getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel()
                    .getPersonneEtendue().getNumAvsActuel();
            String nssAllocataire = context.getDossier().getAllocataireComplexModel().getPersonneEtendueComplexModel()
                    .getPersonneEtendue().getNumAvsActuel();

            ALImplServiceLocator.getAnnonceRafamBusinessService().setNSS(annonce, nssAllocataire, nssEnfant);
        }
    }

    /**
     * Détermine l'action à effectuer en cas de mutation.
     * 
     * En cas de modification de NSS cette procédure s'applique :
     * <a href="http://dev-confluence.ju.globaz.ch/display/APPLI/Gestion+des+changements+de+NSS">Gestion des changements
     * de NSS</a>
     * 
     * @return l'action à exécuter
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    protected ModificationAction getActionModification() throws JadeApplicationException, JadePersistenceException {

        AnnonceRafamModel lastAnnonceUPI = ALImplServiceLocator.getAnnoncesRafamSearchService()
                .getLastAnnonceUPIForRecordNumber(getLastAnnonce().getRecordNumber());

        boolean nssHasChanged = nssHasChanged();

        if (!nssHasChanged) {
            return ModificationAction.STANDARD;
        }

        if (lastAnnonceUPI == null) {
            if (nssHasChanged) {
                return ModificationAction.ANNULATION_ET_CREATION;
            } else {
                return ModificationAction.STANDARD;
            }
        } else {

            RafamReturnCode returnCode = RafamReturnCode.getRafamReturnCode(lastAnnonceUPI.getCodeRetour());

            if (returnCode.equals(RafamReturnCode.TRAITE)) {
                return ModificationAction.UPDATE_NSS_ET_STANDARD;
            } else if (returnCode.equals(RafamReturnCode.EN_ERREUR) || returnCode.equals(RafamReturnCode.RAPPEL)) {

                if (newNssIsUnknown(lastAnnonceUPI)) {
                    return ModificationAction.ANNULATION_ET_CREATION;
                } else if (nssHasChanged) {
                    return ModificationAction.UPDATE_NSS_ET_STANDARD;
                } else {
                    return ModificationAction.STANDARD;
                }
            } else {
                throw new ALRafamException("AnnonceHandlerAbstract#getActionModification : unsupported return code");
            }
        }
    }

    /**
     * Recherche la dernière annonce en fonction des informations (droit et type d'allocation) contenues dans le context
     * 
     * @return La dernière annonce. Si aucune annonce n'a été trouvée une nouvelle instance de
     *         <code>AnnonceRafamModel</code> est retournée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected AnnonceRafamModel getLastAnnonce() throws JadeApplicationException, JadePersistenceException {

        if (lastAnnonce == null) {
            lastAnnonce = ALImplServiceLocator.getAnnoncesRafamSearchService().getLastActive(
                    context.getDroit().getId(), getType());
        }

        return lastAnnonce;
    }

    /**
     * Dans le cas d'une modification de droit ou de dossier, permet d'identifier l'action à exécuter en fonction de
     * l'état actuel des annonces
     * 
     * @return L'action à exécuter
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected RafamTypeAction getAction() throws JadeApplicationException, JadePersistenceException {

        if ((getLastAnnonce() == null) || getLastAnnonce().isNew()) {
            return RafamTypeAction.CREATION;
        } else {

            RafamTypeAnnonce type = RafamTypeAnnonce.getRafamTypeAnnonce(getLastAnnonce().getTypeAnnonce());
            RafamReturnCode returnCode = RafamReturnCode.getRafamReturnCode(getLastAnnonce().getCodeRetour());

            // si l'annonce n'est pas en erreur
            if (returnCode.equals(RafamReturnCode.TRAITE) || returnCode.equals(RafamReturnCode.ANNULEE)
                    || returnCode.equals(RafamReturnCode.EN_ATTENTE)) {
                if (type.equals(RafamTypeAnnonce._68C_ANNULATION)) {
                    return RafamTypeAction.CREATION;
                } else {
                    return RafamTypeAction.MODIFICATION;
                }
                // cas en erreur (code 1 ou 2)
            } else {
                if (returnCode.equals(RafamReturnCode.REJETEE)) {
                    if(lastAnnonce.getEtat() != null &&!RafamEtatAnnonce.ARCHIVE.equals(RafamEtatAnnonce.getRafamEtatAnnonceCS(lastAnnonce.getEtat()))){
                        ALImplServiceLocator.getAnnonceRafamBusinessService().deleteRefusees(
                                getLastAnnonce().getRecordNumber());
                        lastAnnonce = null;
                    }

                    if (type.equals(RafamTypeAnnonce._68A_CREATION)) {
                        return RafamTypeAction.CREATION;
                    } else {
                        return RafamTypeAction.MODIFICATION;
                    }
                } else {
                    return RafamTypeAction.MODIFICATION;
                }
            }
        }
    }

    /**
     * Gère les cas de radiation de dossier. Si on est dans le cas d'une radiation, la date de fin de l'annonce passée
     * en paramètre est modifiée de façon à correspondre à la date de radiation du dossier et l'annonce est enregistrée.
     * Dans le cas d'une annonce de modification, si la date de radiation est antérieur à la date de début du droit, une
     * annonce d'annulation est enregistrée.
     * 
     * @param annonce
     *            l'annonce à traiter
     * 
     * @return <code>true</code> si un cas de radiation a été traité par la méthode, <code>false</code> si rien n'a été
     *         fait
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected boolean isRadiation() throws JadeApplicationException, JadePersistenceException {

        if (RafamFamilyAllowanceType.ADOPTION.equals(getType()) || RafamFamilyAllowanceType.NAISSANCE.equals(getType())
                || RafamFamilyAllowanceType.DIFFERENCE_ADOPTION.equals(getType())
                || RafamFamilyAllowanceType.DIFFERENCE_NAISSANCE.equals(getType())) {
            return false;
        }

        if (ALCSDossier.ETAT_RADIE.equals(context.getDossier().getDossierModel().getEtatDossier())) {
            return true;
        }

        return false;
    }

    protected boolean nssHasChanged() throws JadeApplicationException, JadePersistenceException {

        String nssEnfantActuel = context.getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel()
                .getPersonneEtendue().getNumAvsActuel();
        String nssEnfantAnnonce = getLastAnnonce().getNssEnfant();

        String nssAllocataireActuel = context.getDossier().getAllocataireComplexModel()
                .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel();
        String nssAllocataireAnnonce = getLastAnnonce().getNssAllocataire();

        boolean nssHasChanged = !nssEnfantActuel.equals(nssEnfantAnnonce)
                || !nssAllocataireActuel.equals(nssAllocataireAnnonce);

        return nssHasChanged;
    }

    protected boolean newNssIsUnknown(AnnonceRafamModel lastAnnonceUPI) {
        String newNssEnfant = lastAnnonceUPI.getNewNssEnfant();
        String nssAllocataire = lastAnnonceUPI.getNssAllocataire();
        return ALConstRafam.UPI_UNKNOWN_NSS.equals(newNssEnfant) || ALConstRafam.UPI_UNKNOWN_NSS.equals(nssAllocataire);
    }

    /**
     * Vérifie si l'état actuel du droit correspond bien au type d'annonce en cours de traitement. Cela permet de savoir
     * si l'appel du handler en cours est dû à une annonce existante ou à l'état du droit.
     * 
     * @return <code>true</code> si le type en cours de traitement est actif
     * 
     * @throws JadeApplicationException
     *             Exception levée si la vérification n'a pas pu être effectuée avec succès
     */
    protected abstract boolean isCurrentAllowanceTypeActive() throws JadeApplicationException;

    /**
     * Retourne le type d'allocation gérée par une classe
     * 
     * @return Le type d'allocation correspondant à la classe
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected abstract RafamFamilyAllowanceType getType() throws JadeApplicationException;
}