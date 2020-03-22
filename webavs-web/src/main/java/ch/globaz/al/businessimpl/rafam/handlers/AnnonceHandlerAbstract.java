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
 * Classe de base pour la gestion de l'enregistrement des annonces RAFam. Elle contient les m�thodes communes aux
 * diff�rents cas possibles (Enfant, Formation, Naissance, ...)
 *
 * @author jts
 */
public abstract class AnnonceHandlerAbstract {

    /**
     * Context contenant toutes les informations n�cessaire � la cr�ation d'annonces
     */
    protected ContextAnnonceRafam context = null;

    /**
     * derni�re annonce correspondant aux informations contenues dans le context
     */
    protected AnnonceRafamModel lastAnnonce = null;

    protected void doRadiation(AnnonceRafamModel annonce) throws JadeApplicationException, JadePersistenceException {
        if (!AnnoncesChangeChecker.isDateFinDroitExpire(context.getDroit().getDroitModel().getFinDroitForcee())) {
            // si la date de d�but du droit est au-del� de la date de radiation
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

                // dans le cas d'une modification on v�rifie qu'il y ait eu un changement dans le droit
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
    }

    /**
     * Cr�e et enregistre une annonce d'annulation (68c) en se basant sur la derni�re annonce correspondant aux
     * informations du context. Si aucune annonce n'existe, rien n'est fait.
     *
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *                                  faire
     */
    protected void doAnnulation() throws JadeApplicationException, JadePersistenceException {
        if (!getLastAnnonce().isNew()
                && !RafamTypeAnnonce._68C_ANNULATION.equals(RafamTypeAnnonce.getRafamTypeAnnonce(getLastAnnonce()
                .getTypeAnnonce())) && !AnnoncesChangeChecker.isDateFinDroitExpire(context.getDroit().getDroitModel().getFinDroitForcee())) {
            ALServiceLocator.getAnnonceRafamModelService().create(
                    ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce68c(getLastAnnonce(),
                            context.getEtat()));
        }
    }

    /**
     * Cr�e une annonce de cr�ation (68a). Si aucun cas de radiation n'a �t� trait� elle est enregistr�e
     *
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *                                  faire
     * @see AnnonceHandlerAbstract#isRadiation(AnnonceRafamModel)
     */
    protected void doCreation() throws JadeApplicationException, JadePersistenceException {

        if (isCurrentAllowanceTypeActive() && !AnnoncesChangeChecker.isDateFinDroitExpire(context.getDroit().getDroitModel().getFinDroitForcee())) {

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
     * Cr�e une annonce de modification (68b). Si aucun cas de radiation n'a �t� trait� et qu'il y a eu des
     * modifications dans le droit ou le dossier depuis l'envoi de la derni�re annonce elle est enregistr�e
     *
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *                                  faire
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
     * Enregistre l'annonce en persistance. L'�v�nement d�clencheur est remplac� par celui contenu dans le context.
     *
     * @param annonce l'annonce � enregistrer
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *                                  faire
     */
    protected void enregistrerAnnonce(AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException {

        annonce.setEvDeclencheur(context.getEvenementDeclencheur().getCS());
        ALServiceLocator.getAnnonceRafamModelService().create(annonce);
    }

    /**
     * Ex�cute le traitement du handler. L'ev�nement ayant d�clench� l'appel du service de gestion des annonces est
     * analys� afin de d�terminer l'action � ex�cuter
     *
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *                                  faire
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
        if (!AnnoncesChangeChecker.isDateFinDroitExpire(context.getDroit().getDroitModel().getFinDroitForcee())){
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
    }

    /**
     * D�termine l'action � effectuer en cas de mutation.
     * <p>
     * En cas de modification de NSS cette proc�dure s'applique :
     * <a href="http://dev-confluence.ju.globaz.ch/display/APPLI/Gestion+des+changements+de+NSS">Gestion des changements
     * de NSS</a>
     *
     * @return l'action � ex�cuter
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
     * Recherche la derni�re annonce en fonction des informations (droit et type d'allocation) contenues dans le context
     *
     * @return La derni�re annonce. Si aucune annonce n'a �t� trouv�e une nouvelle instance de
     * <code>AnnonceRafamModel</code> est retourn�e
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *                                  faire
     */
    protected AnnonceRafamModel getLastAnnonce() throws JadeApplicationException, JadePersistenceException {

        if (lastAnnonce == null) {
            lastAnnonce = ALImplServiceLocator.getAnnoncesRafamSearchService().getLastActive(
                    context.getDroit().getId(), getType());
        }

        return lastAnnonce;
    }

    /**
     * Dans le cas d'une modification de droit ou de dossier, permet d'identifier l'action � ex�cuter en fonction de
     * l'�tat actuel des annonces
     *
     * @return L'action � ex�cuter
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *                                  faire
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
                    if (lastAnnonce.getEtat() != null && !RafamEtatAnnonce.ARCHIVE.equals(RafamEtatAnnonce.getRafamEtatAnnonceCS(lastAnnonce.getEtat())) && !lastAnnonce.getDelegated()) {
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
     * G�re les cas de radiation de dossier. Si on est dans le cas d'une radiation, la date de fin de l'annonce pass�e
     * en param�tre est modifi�e de fa�on � correspondre � la date de radiation du dossier et l'annonce est enregistr�e.
     * Dans le cas d'une annonce de modification, si la date de radiation est ant�rieur � la date de d�but du droit, une
     * annonce d'annulation est enregistr�e.
     *
     * @param annonce l'annonce � traiter
     * @return <code>true</code> si un cas de radiation a �t� trait� par la m�thode, <code>false</code> si rien n'a �t�
     * fait
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *                                  faire
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
     * V�rifie si l'�tat actuel du droit correspond bien au type d'annonce en cours de traitement. Cela permet de savoir
     * si l'appel du handler en cours est d� � une annonce existante ou � l'�tat du droit.
     *
     * @return <code>true</code> si le type en cours de traitement est actif
     * @throws JadeApplicationException Exception lev�e si la v�rification n'a pas pu �tre effectu�e avec succ�s
     */
    protected abstract boolean isCurrentAllowanceTypeActive() throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne le type d'allocation g�r�e par une classe
     *
     * @return Le type d'allocation correspondant � la classe
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected abstract RafamFamilyAllowanceType getType() throws JadeApplicationException;
}