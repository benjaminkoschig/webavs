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
 * Contexte affili� utilis� pour la g�n�ration de prestations
 * 
 * @author jts
 * 
 */
public class ContextAffilie {

    /**
     * Retourne une instance du contexte affili�.
     * 
     * @param debutRecap d�but de la p�riode de la r�cap
     * @param finRecap fin de la p�riode de la r�cap
     * @param typeGeneration Type de g�n�ration (
     *            {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_GENERATION_TYPE} )
     * @param numAffilie Num�ro de l'affili� � traiter
     * @param periodicite Num�ro de la facture
     * @param numGeneration Num�ro de g�n�ration. Facultatif, utilis� uniquement pour les g�n�rations globales
     * @param logger Instance du logger devant stocker les messages de la g�n�ration
     * @param jadeContextImpl Instance du context Jade
     * 
     * @return Une instance de <code>ContextAffilie</code>
     * 
     * @throws JadeApplicationException Exception lev�e si l'un des param�tre n'est pas correct ou si le code du type de
     *             g�n�ration n'a pas pu �tre contr�l�
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de
     *             persistence n'a pu se faire
     */
    public static ContextAffilie getContextAffilie(String debutRecap, String finRecap, String typeGeneration,
            String numAffilie, String periodicite, String numGeneration, ProtocoleLogger logger,
            JadeContextImplementation jadeContextImpl) throws JadeApplicationException, JadePersistenceException {

        // d�but r�cap
        if (!JadeDateUtil.isGlobazDateMonthYear(debutRecap)) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#getContextAffilie : " + debutRecap
                    + "is not a valid period");
        }

        // fin r�cap
        if (!JadeDateUtil.isGlobazDateMonthYear(finRecap)) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#getContextAffilie : " + finRecap
                    + "is not a valid period");
        }

        // type de g�n�ration
        if (JadeStringUtil.isEmpty(typeGeneration)) {
            throw new ALGenerationPrestationsContextException(
                    "ContextAffilie#getContextAffilie : typeGeneration is null or empty");
        }

        // num�ro d'affili�
        if (JadeStringUtil.isEmpty(numAffilie)) {
            throw new ALGenerationPrestationsContextException(
                    "ContextAffilie#getContextAffilie : numAffilie is null or empty");
            // TODO (lot 2) recherche = � la place de LIKE
        } else if (AFBusinessServiceLocator.getAffiliationService().findAllForNumeroAffilieLike(numAffilie).length == 0) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#getContextAffilie : " + numAffilie
                    + " is not valid");
        }

        // chronologie r�cap
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
     * D�but de la p�riode de la r�cap (format MM.AAAA)
     */
    private String debutRecap = null;

    /**
     * Fin de la p�riode de la r�cap (format MM.AAAA)
     */
    private String finRecap = null;

    /**
     * Context Jade
     */
    private JadeContextImplementation jadeContextImpl;
    /**
     * Logger devant stocker les messages de la g�n�ration
     */
    private ProtocoleLogger logger;
    /**
     * Num�ro de l'affili�
     */
    private String numAffilie = null;
    /**
     * Num�ro de facture � utiliser pour les affili� de type annuel
     */
    private String numFactureAnn = null;
    /**
     * Num�ro de facture � utiliser s'il a �t� forc� (g�n�ration depuis un dossier)
     */
    private String numFactureForcee = null;
    /**
     * Num�ro de facture � utiliser pour les affili� de type mensuel ou de paiement direct
     */
    private String numFactureMen = null;
    /**
     * Num�ro de facture � utiliser pour les affili� de type trimestriel
     */
    private String numFactureTri = null;

    /**
     * Num�ro de g�n�ration Facultatif, il est utilis� uniquement pour les g�n�rations globales
     */
    private String numGeneration = null;
    /**
     * Le num�ro du processus li� � la r�cap
     */
    private String numProcessus = null;
    /**
     * P�riodicit�
     */
    private String periodicite = null;

    /**
     * R�cap devant contenir les prestations directe ainsi que les restitutions (assurance genre ind�pendant)
     */
    private RecapitulatifEntrepriseModel recapDirectIndep = null;

    /**
     * R�cap devant contenir les prestations directe ainsi que les restitutions (assurance genre paritaire)
     */
    private RecapitulatifEntrepriseModel recapDirectPar = null;
    /**
     * R�cap devant contenir les prestations indirecte (assurance genre ind�pendant)
     */
    private RecapitulatifEntrepriseModel recapIndirectIndep = null;
    /**
     * R�cap devant contenir les prestations indirecte (assurance genre paritaire)
     */
    private RecapitulatifEntrepriseModel recapIndirectPar = null;
    /**
     * Type de g�n�ration
     * 
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_GENERATION_TYPE
     */
    private String typeGeneration = null;

    /**
     * Constructeur priv�. Utiliser la m�thode <code>getContextAffilie</code> pour r�cup�rer une instance de context
     * 
     * @see ContextAffilie#getContextAffilie(String, String, String, String, String, String, ProtocoleLogger,
     *      JadeContextImplementation)
     */
    private ContextAffilie() {
        // Utiliser la m�thode getContextAffilie
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
     * Retourne l'id de la r�cap � laquelle l'en-t�te doit �tre li�e
     * 
     * @param entete En-t�te pour laquelle r�cup�rer la r�cap
     * 
     * @return id de la r�cap correspondant � l'en-t�te <code>entete</code>
     * 
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de
     *             persistence n'a pu se faire
     */
    public String getIdRecap(EntetePrestationModel entete) throws JadeApplicationException, JadePersistenceException {

        if (entete == null) {
            throw new ALGenerationPrestationsContextException("ContextAffilie#getIdRecap : entete is null");
        }

        // pas de r�cap � utiliser dans le cas d'une prestation ADI temporaire
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
                // non support�
            } else {
                throw new ALGenerationPrestationsContextException(
                        "ContextAffilie#getIdRecap : ce type de bonnification n'est pas support�");
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
                // non support�
            } else {
                throw new ALGenerationPrestationsContextException(
                        "ContextAffilie#getIdRecap : ce type de bonnification n'est pas support�");
            }
        } else {
            throw new ALGenerationPrestationsContextException(
                    "ContextAffilie#getIdRecap : ce genre d'assurance n'est pas support�");
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
     * @return le num�ro de l'affili�
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * Retourne le num�ro de facture correspondant aux param�tres
     * 
     * @param periodicite p�riodicit� de l'affili�
     * @param bonification bonification du dossier
     * 
     * @return le num�ro de facture
     * 
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de
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
     * @return le num�ro de g�n�ration
     */
    public String getNumGeneration() {
        return numGeneration;
    }

    public String getNumProcessus() {
        return numProcessus;
    }

    /**
     * Retourne le type de g�n�ration
     * 
     * @return the typeGeneration
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_GENERATION_TYPE
     */
    public String getTypeGeneration() {
        return typeGeneration;
    }

    /**
     * Initialise le contexte de dossier. Il doit avoir �t� lib�r� avant initialisation si le contexte est utilis� dans
     * le cas d'une g�n�ration globale ou affili�e
     * 
     * @param dossier Dossier en cours de g�n�ration
     * @param debutPeriode d�but de la p�riode de g�n�ration de prestations
     * @param finPeriode fin de la p�riode de g�n�ration de prestations
     * @param montantForce Montant forc�
     * @param bonification Indique s'il s'agit d'une demande de restitution
     * @param nbUnites nombre d'unit�. Utilis� pour les dossiers � l'heure ou au jour
     * @param typeGen Type de g�n�ration ex�cut� pour le dossier
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
     * @see ContextAffilie#releaseDossier()
     */
    public void initContextDossier(DossierComplexModel dossier, String debutPeriode, String finPeriode,
            String montantForce, Bonification bonification, String nbUnites, TypeGeneration typeGen)
            throws JadeApplicationException {

        // le dossier doit avoir �t� lib�r� avant initialisation
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

        // exception lev�e si le num�ro d'affili� du dossier ne correspond pas �
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
     * Initialise la r�cap pass�e en param�tre. L'existance d'une r�cap ouverte ayant les m�mes param�tres et d'abord
     * contr�l�e. Si c'est le cas, cette r�cap est r�cup�r�e pour �tre utilis�e. Sinon une nouvelle r�cap est cr��e.
     * 
     * @param recap La r�cap � initialiser
     * @param bonification le type de bonification
     * @param genreAssurance Genre d'assurance de l'affili�. D�pend du type d'activit� de l'allocataire
     * @return La r�cap initialis�e
     * 
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de
     *             persistence n'a pu se faire
     */
    private RecapitulatifEntrepriseModel initRecap(RecapitulatifEntrepriseModel recap, String bonification,
            String genreAssurance) throws JadeApplicationException, JadePersistenceException {

        // recap peut �tre null

        if (JadeStringUtil.isEmpty(bonification)) {
            throw new ALGenerationPrestationsContextException(
                    "ContextAffilie#initRecap : bonification is null or empty");
        }

        if (periodicite == null) {
            periodicite = getContextDossier().getAssuranceInfo().getPeriodicitieAffiliation();
        }

        if (recap == null) {
            // FIXME appeler le service RecapitulatifEntrepriseBusinessServiceImpl#initRecap pour avoir une recap
            // recherche d'une r�cap existante
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
                            "ContextAffilie#initRecap : la r�cap associ�e (p�riode/n� facture) ne peut pas �tre li�e � un autre processus, veuillez passer par l'�cran d�tail de la r�cap (AL0017)");
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
     * Lib�re le contexte du dossier apr�s avoir lib�r� les contextes de prestation (apr�s les avoir enregistr�s si
     * n�cessaire)
     * 
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de
     *             persistence n'a pu se faire
     */
    public void releaseDossier() throws JadeApplicationException, JadePersistenceException {

        saveContextAffilie();
        contextDossier.releaseDossier();
        contextDossier = null;
    }

    /**
     * Effectue un rollback des prestations enregistr�e dans le contexte.
     * 
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
     */
    public void rollbackPrestations() throws JadeApplicationException {
        contextDossier.rollbackDossier();
        contextDossier = null;
    }

    /**
     * Enregistre le contenu du contexte de l'affili� en persistance (R�cap)
     * 
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
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
     * Enregistre les prestations apr�s avoir enregistr� le contexte de l'affili�
     * 
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de
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
