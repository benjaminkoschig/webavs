package ch.globaz.al.businessimpl.generation.prestations.context;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.constantes.ALConstPrestations.TypeGeneration;
import ch.globaz.al.business.constantes.enumerations.generation.prestations.Bonification;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationPrestationsContextException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;

/**
 * Contexte affilié utilisé pour la génération de prestations
 * 
 * @author jts
 * 
 */
public class ContextAffilie {

    /**
     * Retourne une instance du contexte affilié.
     * 
     * @param debutRecap début de la période de la récap
     * @param finRecap fin de la période de la récap
     * @param typeGeneration Type de génération (
     *            {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_GENERATION_TYPE} )
     * @param numAffilie Numéro de l'affilié à traiter
     * @param periodicite Numéro de la facture
     * @param numGeneration Numéro de génération. Facultatif, utilisé uniquement pour les générations globales
     * @param logger Instance du logger devant stocker les messages de la génération
     * @param jadeContextImpl Instance du context Jade
     * 
     * @return Une instance de <code>ContextAffilie</code>
     * 
     * @throws JadeApplicationException Exception levée si l'un des paramètre n'est pas correct ou si le code du type de
     *             génération n'a pas pu être contrôlé
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de
     *             persistence n'a pu se faire
     */
    public static ContextAffilie getContextAffilie(String debutRecap, String finRecap, String typeGeneration,
            String numAffilie, String periodicite, String numGeneration, ProtocoleLogger logger,
            JadeContextImplementation jadeContextImpl) throws JadeApplicationException, JadePersistenceException {

        // début récap
        if (!JadeDateUtil.isGlobazDateMonthYear(debutRecap)) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#getContextAffilie : " + debutRecap
                    + "is not a valid period");
        }

        // fin récap
        if (!JadeDateUtil.isGlobazDateMonthYear(finRecap)) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#getContextAffilie : " + finRecap
                    + "is not a valid period");
        }

        // type de génération
        if (JadeStringUtil.isEmpty(typeGeneration)) {
            throw new ALGenerationPrestationsContextException(
                    "ContextAffilie#getContextAffilie : typeGeneration is null or empty");
        }

        // numéro d'affilié
        if (JadeStringUtil.isEmpty(numAffilie)) {
            throw new ALGenerationPrestationsContextException(
                    "ContextAffilie#getContextAffilie : numAffilie is null or empty");
            // TODO (lot 2) recherche = à la place de LIKE
        } else if (AFBusinessServiceLocator.getAffiliationService().findAllForNumeroAffilieLike(numAffilie).length == 0) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#getContextAffilie : " + numAffilie
                    + " is not valid");
        }

        // chronologie récap
        if (JadeDateUtil.isDateMonthYearBefore(finRecap, debutRecap)) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#getContextAffilie : " + finRecap
                    + " must be after (or equal) " + debutRecap);
        }

        if (JadeDateUtil.isDateMonthYearBefore(finRecap, debutRecap)) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#getContextAffilie : " + finRecap
                    + " must be after (or equal) " + debutRecap);
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_GENERATION_TYPE, typeGeneration)) {
                throw new ALGenerationPrestationsContextException("ContextAffilie#getContextAffilie : "
                        + typeGeneration + "is not a valid generation type");
            }
        } catch (Exception e) {
            throw new ALGenerationPrestationsContextException(
                    "ContextAffilie#getContextAffilie : unable to check the generation type", e);
        }

        ContextAffilie context = new ContextAffilie();
        context.typeGeneration = typeGeneration;
        context.debutRecap = debutRecap;
        context.finRecap = finRecap;
        context.numAffilie = numAffilie;
        context.periodicite = periodicite;
        context.numGeneration = numGeneration;
        context.logger = logger;
        context.jadeContextImpl = jadeContextImpl;

        return context;
    }

    /**
     * contexte du dossier
     */
    private ContextDossier contextDossier;

    /**
     * Début de la période de la récap (format MM.AAAA)
     */
    private String debutRecap = null;

    /**
     * Fin de la période de la récap (format MM.AAAA)
     */
    private String finRecap = null;

    /**
     * Context Jade
     */
    private JadeContextImplementation jadeContextImpl;
    /**
     * Logger devant stocker les messages de la génération
     */
    private ProtocoleLogger logger;
    /**
     * Numéro de l'affilié
     */
    private String numAffilie = null;
    /**
     * Numéro de facture à utiliser pour les affilié de type annuel
     */
    private String numFactureAnn = null;
    /**
     * Numéro de facture à utiliser s'il a été forcé (génération depuis un dossier)
     */
    private String numFactureForcee = null;
    /**
     * Numéro de facture à utiliser pour les affilié de type mensuel ou de paiement direct
     */
    private String numFactureMen = null;
    /**
     * Numéro de facture à utiliser pour les affilié de type trimestriel
     */
    private String numFactureTri = null;

    /**
     * Numéro de génération Facultatif, il est utilisé uniquement pour les générations globales
     */
    private String numGeneration = null;
    /**
     * Le numéro du processus lié à la récap
     */
    private String numProcessus = null;
    /**
     * Périodicité
     */
    private String periodicite = null;

    /**
     * Récap devant contenir les prestations directe ainsi que les restitutions (assurance genre indépendant)
     */
    private RecapitulatifEntrepriseModel recapDirectIndep = null;

    /**
     * Récap devant contenir les prestations directe ainsi que les restitutions (assurance genre paritaire)
     */
    private RecapitulatifEntrepriseModel recapDirectPar = null;
    /**
     * Récap devant contenir les prestations indirecte (assurance genre indépendant)
     */
    private RecapitulatifEntrepriseModel recapIndirectIndep = null;
    /**
     * Récap devant contenir les prestations indirecte (assurance genre paritaire)
     */
    private RecapitulatifEntrepriseModel recapIndirectPar = null;
    /**
     * Type de génération
     * 
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_GENERATION_TYPE
     */
    private String typeGeneration = null;

    /**
     * Constructeur privé. Utiliser la méthode <code>getContextAffilie</code> pour récupérer une instance de context
     * 
     * @see ContextAffilie#getContextAffilie(String, String, String, String, String, String, ProtocoleLogger,
     *      JadeContextImplementation)
     */
    private ContextAffilie() {
        // Utiliser la méthode getContextAffilie
    }

    /**
     * Retourne le context du dossier
     * 
     * @return le contexte du dossier
     * @see ch.globaz.al.businessimpl.generation.prestations.context.ContextDossier
     */
    public ContextDossier getContextDossier() {
        return contextDossier;
    }

    /**
     * @return the debutRecap
     */
    public String getDebutRecap() {
        return debutRecap;
    }

    /**
     * @return the finRecap
     */
    public String getFinRecap() {
        return finRecap;
    }

    /**
     * Retourne l'id de la récap à laquelle l'en-tête doit être liée
     * 
     * @param entete En-tête pour laquelle récupérer la récap
     * 
     * @return id de la récap correspondant à l'en-tête <code>entete</code>
     * 
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de
     *             persistence n'a pu se faire
     */
    public String getIdRecap(EntetePrestationModel entete) throws JadeApplicationException, JadePersistenceException {

        if (entete == null) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#getIdRecap : entete is null");
        }

        // pas de récap à utiliser dans le cas d'une prestation ADI temporaire
        if (getContextDossier().getTypeGenPrestation().equals(ALConstPrestations.TypeGeneration.ADI_TEMPORAIRE)) {
            return ALConstPrestations.ID_RECAP_ADI_TMP;
        }

        String genre = ALImplServiceLocator.getDossierBusinessService().getGenreAssurance(
                contextDossier.getDossier().getDossierModel().getActiviteAllocataire());

        if (ALCSAffilie.GENRE_ASSURANCE_PARITAIRE.equals(genre)) {
            // direct ou restitution
            if (ALCSPrestation.BONI_DIRECT.equals(entete.getBonification())
                    || ALCSPrestation.BONI_RESTITUTION.equals(entete.getBonification())) {
                recapDirectPar = initRecap(recapDirectPar, ALCSPrestation.BONI_DIRECT, genre);
                return recapDirectPar.getId();
                // indirect
            } else if (ALCSPrestation.BONI_INDIRECT.equals(entete.getBonification())) {
                recapIndirectPar = initRecap(recapIndirectPar, ALCSPrestation.BONI_INDIRECT, genre);
                return recapIndirectPar.getId();
                // non supporté
            } else {
                throw new ALGenerationPrestationsContextException(
                        "ContextAffilie#getIdRecap : ce type de bonnification n'est pas supporté");
            }
        } else if (ALCSAffilie.GENRE_ASSURANCE_INDEP.equals(genre)) {
            // direct ou restitution
            if (ALCSPrestation.BONI_DIRECT.equals(entete.getBonification())
                    || ALCSPrestation.BONI_RESTITUTION.equals(entete.getBonification())) {
                recapDirectIndep = initRecap(recapDirectIndep, ALCSPrestation.BONI_DIRECT, genre);
                return recapDirectIndep.getId();
                // indirect
            } else if (ALCSPrestation.BONI_INDIRECT.equals(entete.getBonification())) {
                recapIndirectIndep = initRecap(recapIndirectIndep, ALCSPrestation.BONI_INDIRECT, genre);
                return recapIndirectIndep.getId();
                // non supporté
            } else {
                throw new ALGenerationPrestationsContextException(
                        "ContextAffilie#getIdRecap : ce type de bonnification n'est pas supporté");
            }
        } else {
            throw new ALGenerationPrestationsContextException(
                    "ContextAffilie#getIdRecap : ce genre d'assurance n'est pas supporté");
        }
    }

    /**
     * @return the jadeContextImpl
     */
    public JadeContextImplementation getJadeContextImpl() {
        return jadeContextImpl;
    }

    /**
     * @return the logger
     */
    public ProtocoleLogger getLogger() {
        return logger;
    }

    /**
     * @return le numéro de l'affilié
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * Retourne le numéro de facture correspondant aux paramètres
     * 
     * @param periodicite périodicité de l'affilié
     * @param bonification bonification du dossier
     * 
     * @return le numéro de facture
     * 
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de
     *             persistence n'a pu se faire
     */
    private String getNumeroFacture(String periodicite, String bonification) throws JadeApplicationException,
            JadePersistenceException {

        if (!JadeStringUtil.isEmpty(numFactureForcee)) {

            return numFactureForcee;

        } else if (ALCSPrestation.BONI_DIRECT.equals(bonification) || ALCSAffilie.PERIODICITE_MEN.equals(periodicite)) {
            if (JadeStringUtil.isEmpty(numFactureMen)) {
                numFactureMen = ALServiceLocator.getNumeroFactureService().getNumFacture(finRecap, periodicite,
                        bonification, numAffilie);
            }

            return numFactureMen;

        } else if (ALCSAffilie.PERIODICITE_TRI.equals(periodicite)) {
            if (JadeStringUtil.isEmpty(numFactureTri)) {
                numFactureTri = ALServiceLocator.getNumeroFactureService().getNumFacture(finRecap, periodicite,
                        bonification, numAffilie);
            }

            return numFactureTri;

        } else if (ALCSAffilie.PERIODICITE_ANN.equals(periodicite)) {
            if (JadeStringUtil.isEmpty(numFactureAnn)) {
                numFactureAnn = ALServiceLocator.getNumeroFactureService().getNumFacture(finRecap, periodicite,
                        bonification, numAffilie);
            }

            return numFactureAnn;

        } else {
            throw new ALGenerationPrestationsContextException("ContextAffilie#getNumeroFacture : this bonification ("
                    + bonification + ") type is not supported");
        }
    }

    /**
     * @return le numéro de génération
     */
    public String getNumGeneration() {
        return numGeneration;
    }

    public String getNumProcessus() {
        return numProcessus;
    }

    /**
     * Retourne le type de génération
     * 
     * @return the typeGeneration
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_GENERATION_TYPE
     */
    public String getTypeGeneration() {
        return typeGeneration;
    }

    /**
     * Initialise le contexte de dossier. Il doit avoir été libéré avant initialisation si le contexte est utilisé dans
     * le cas d'une génération globale ou affiliée
     * 
     * @param dossier Dossier en cours de génération
     * @param debutPeriode début de la période de génération de prestations
     * @param finPeriode fin de la période de génération de prestations
     * @param montantForce Montant forcé
     * @param bonification Indique s'il s'agit d'une demande de restitution
     * @param nbUnites nombre d'unité. Utilisé pour les dossiers à l'heure ou au jour
     * @param typeGen Type de génération exécuté pour le dossier
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     * @see ContextAffilie#releaseDossier()
     */
    public void initContextDossier(DossierComplexModel dossier, String debutPeriode, String finPeriode,
            String montantForce, Bonification bonification, String nbUnites, TypeGeneration typeGen)
            throws JadeApplicationException {

        // le dossier doit avoir été libéré avant initialisation
        if (contextDossier != null) {
            throw new ALGenerationPrestationsContextException(
                    "ContextAffilie#initContextDossier : contextDossier has not been released");
        }

        if (dossier == null) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#initContextDossier : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(debutPeriode)) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#initContextDossier : " + debutPeriode
                    + "is not a valid period");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(finPeriode)) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#initContextDossier : " + finPeriode
                    + "is not a valid period");
        }

        if (JadeDateUtil.isDateMonthYearBefore(finPeriode, debutPeriode)) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#initContextDossier : " + finPeriode
                    + " must be after (or equal) " + debutPeriode);
        }

        // exception levée si le numéro d'affilié du dossier ne correspond pas à
        // celui du context
        if (!dossier.getDossierModel().getNumeroAffilie().equals(numAffilie)) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#initContextDossier : "
                    + dossier.getDossierModel().getNumeroAffilie() + " does not match the context's affilie number");
        }

        if (!JadeStringUtil.isEmpty(montantForce) && !JadeNumericUtil.isNumeric(montantForce)) {
            throw new ALGenerationException("ContextAffilie#initContextDossier : " + montantForce
                    + " is not a numeric value");
        }

        if (JadeStringUtil.isEmpty(nbUnites)
                || (!JadeNumericUtil.isIntegerPositif(nbUnites) && !JadeNumericUtil.isZeroValue(nbUnites))) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#initContextDossier : " + nbUnites
                    + " is not an unsigned integer");
        }

        contextDossier = ContextDossier.getContextDossier(dossier, debutPeriode, finPeriode, montantForce, this,
                bonification, nbUnites, typeGen);
    }

    /**
     * Initialise la récap passée en paramètre. L'existance d'une récap ouverte ayant les mêmes paramètres et d'abord
     * contrôlée. Si c'est le cas, cette récap est récupérée pour être utilisée. Sinon une nouvelle récap est créée.
     * 
     * @param recap La récap à initialiser
     * @param bonification le type de bonification
     * @param genreAssurance Genre d'assurance de l'affilié. Dépend du type d'activité de l'allocataire
     * @return La récap initialisée
     * 
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de
     *             persistence n'a pu se faire
     */
    private RecapitulatifEntrepriseModel initRecap(RecapitulatifEntrepriseModel recap, String bonification,
            String genreAssurance) throws JadeApplicationException, JadePersistenceException {

        // recap peut être null

        if (JadeStringUtil.isEmpty(bonification)) {
            throw new ALGenerationPrestationsContextException(
                    "ContextAffilie#initRecap : bonification is null or empty");
        }

        if (periodicite == null) {
            periodicite = getContextDossier().getAssuranceInfo().getPeriodicitieAffiliation();
        }

        if (recap == null) {
            // FIXME appeler le service RecapitulatifEntrepriseBusinessServiceImpl#initRecap pour avoir une recap
            // recherche d'une récap existante
            RecapitulatifEntrepriseSearchModel s = new RecapitulatifEntrepriseSearchModel();
            s.setForEtatRecap(ALCSPrestation.ETAT_SA);
            s.setForNumeroAffilie(contextDossier.getDossier().getDossierModel().getNumeroAffilie());
            s.setForBonification(bonification);
            s.setForNumeroFacture(getNumeroFacture(periodicite, bonification));
            s.setForPeriodeDe(debutRecap);
            s.setForPeriodeA(finRecap);
            s.setForGenreAssurance(genreAssurance);
            ALServiceLocator.getRecapitulatifEntrepriseModelService().search(s);

            if (s.getSize() == 1) {
                recap = (RecapitulatifEntrepriseModel) s.getSearchResults()[0];
                if ((getNumProcessus() != null) && !recap.getIdProcessusPeriodique().equals(getNumProcessus())) {
                    throw new ALGenerationPrestationsContextException(
                            "ContextAffilie#initRecap : la récap associée (période/n° facture) ne peut pas être liée à un autre processus, veuillez passer par l'écran détail de la récap (AL0017)");
                }
            } else if (s.getSize() > 1) {
                throw new ALGenerationPrestationsContextException(
                        "ContextAffilie#initRecap : More than one recap were found");
            } else {
                recap = new RecapitulatifEntrepriseModel();
                recap.setEtatRecap(ALCSPrestation.ETAT_SA);
                recap.setNumeroAffilie(numAffilie);
                recap.setBonification(bonification);
                recap.setNumeroFacture(getNumeroFacture(periodicite, bonification));
                recap.setPeriodeDe(debutRecap);
                recap.setPeriodeA(finRecap);
                recap.setGenreAssurance(genreAssurance);
                recap.setIdProcessusPeriodique(getNumProcessus());

                recap = ALServiceLocator.getRecapitulatifEntrepriseModelService().create(recap);
            }
        }

        return recap;
    }

    /**
     * Libère le contexte du dossier après avoir libéré les contextes de prestation (après les avoir enregistrés si
     * nécessaire)
     * 
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de
     *             persistence n'a pu se faire
     */
    public void releaseDossier() throws JadeApplicationException, JadePersistenceException {

        saveContextAffilie();
        contextDossier.releaseDossier();
        contextDossier = null;
    }

    /**
     * Effectue un rollback des prestations enregistrée dans le contexte.
     * 
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     */
    public void rollbackPrestations() throws JadeApplicationException {
        contextDossier.rollbackDossier();
        contextDossier = null;
    }

    /**
     * Enregistre le contenu du contexte de l'affilié en persistance (Récap)
     * 
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     */
    public void saveContextAffilie() throws JadeApplicationException {
        if (!contextDossier.hasError() && contextDossier.hasPrestations()) {

            if (JadeThread.logMaxLevel() == JadeBusinessMessageLevels.ERROR) {
                try {
                    JadeThread.rollbackSession();
                } catch (Exception e) {
                    throw new ALGenerationPrestationsContextException(
                            "ContextAffilie#saveContextAffilie : unable to rollback", e);
                }
            } else {
                try {
                    JadeThread.commitSession();
                } catch (Exception e) {
                    throw new ALGenerationPrestationsContextException(
                            "ContextAffilie#saveContextAffilie : unable to commit", e);
                }
            }
        }
    }

    /**
     * Enregistre les prestations après avoir enregistré le contexte de l'affilié
     * 
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de
     *             persistence n'a pu se faire
     * @see ContextAffilie#saveContextAffilie()
     */
    public void savePrestations() throws JadeApplicationException, JadePersistenceException {
        saveContextAffilie();
        contextDossier.savePrestations();
    }

    /**
     * @param numFactureForcee the numFactureForcee to set
     */
    public void setNumFactureForcee(String numFactureForcee) {
        this.numFactureForcee = numFactureForcee;
    }

    public void setNumProcessus(String numProcessus) {
        this.numProcessus = numProcessus;
    }
}
