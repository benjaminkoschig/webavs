package globaz.aquila.db.access.poursuite;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.api.ICOContentieux;
import globaz.aquila.api.ICOContentieuxConstante;
import globaz.aquila.api.ICOEtape;
import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.aquila.api.ICOSequence;
import globaz.aquila.api.ICOSequenceConstante;
import globaz.aquila.application.COApplication;
import globaz.aquila.common.COBEntity;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.batch.COEtapeManager;
import globaz.aquila.db.access.batch.COSequence;
import globaz.aquila.db.access.batch.COSequenceManager;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.COTransitionManager;
import globaz.aquila.db.access.batch.transition.COAbstractEnvoyerDocument;
import globaz.aquila.db.access.batch.transition.COTransitionAction;
import globaz.aquila.db.access.batch.transition.COTransitionException;
import globaz.aquila.db.poursuite.COContentieuxViewBean;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.config.COConfigurationKey;
import globaz.aquila.service.config.COConfigurationService;
import globaz.aquila.service.historique.COHistoriqueService;
import globaz.aquila.util.COActionUtils;
import globaz.aquila.util.CODateUtils;
import globaz.aquila.util.COEtapeBlocage;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAEcritureManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.db.contentieux.CAMotifContentieuxManager;
import globaz.osiris.external.IntRole;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Représente une entité de type Contentieux.
 * 
 * @author Arnaud Dostes, 04-oct-2004
 * @author SCH
 */
public class COContentieux extends COBEntity implements ICOContentieuxConstante {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int CHECK_COMPTE_ANNEXE = 1;
    public static final int CHECK_SECTION = 2;

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final COConfigurationService CONFIG_SERVICE = COServiceLocator.getConfigService();
    public static final String ID_CONTENTIEUX_BIDON = "-1";

    /** (compteAnnexe). */
    protected CACompteAnnexe compteAnnexe = new CACompteAnnexe();

    private CACompteAnnexe compteAnnexePrincipal = new CACompteAnnexe();
    private String dateDeclenchement = "";
    private String dateExecution = "";
    private String dateOuverture = "";
    /** (etape). */
    protected COEtape etape = new COEtape();
    private String idCompteAnnexe = "";
    private String idCompteAnnexePrincipal = "";
    private String idContentieux = "";
    private String idEtape = "";
    private String idSection = "";
    private String idSequence = "";
    private String montantInitial = "";
    private String montantTotalFrais = "";
    private String montantTotalInterets = "";
    private String montantTotalTaxes = "";
    private String nbDelaiMute = "";
    private String oldIdEtape = "";
    private Boolean previsionnel = Boolean.FALSE;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String prochaineDateDeclenchement = "";
    private boolean prochaineDateDeclenchementChangee = false;

    private boolean refreshLinks = true;

    private String remarque = "";

    /** (section). */
    protected CASection section = new CASection();

    /** (sequence). */
    protected COSequence sequence = new COSequence();

    private String user = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        if (refreshLinks) {
            refreshLinks(transaction);
        }

        oldIdEtape = idEtape;

        if (ICOSequence.CS_SEQUENCE_ARD.equals(getLibSequence())) {
            BSession remoteSession = (BSession) ((COApplication) GlobazServer.getCurrentSystem().getApplication(
                    ICOApplication.DEFAULT_APPLICATION_AQUILA)).getSessionOsiris(getSession());

            compteAnnexePrincipal.setSession(remoteSession);
            compteAnnexePrincipal.setIdCompteAnnexe(idCompteAnnexePrincipal);
            compteAnnexePrincipal.retrieve();
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        refreshLinks(transaction);

        /*
         * L'id doit être unique pour tous les types de contentieux afin de garantir la cohérence des liens depuis
         * l'historique
         */
        idContentieux = this._incCounter(transaction, "0", ICOContentieuxConstante.TABLE_NAME_AVS, "", "");

        // Ajoute la première ligne de l'historique
        try {
            COServiceLocator.getTransitionService().effectuerActionCreerContentieux(getSession(), transaction, this);

            if (isProchaineDateDeclenchementChangee()) {
                nbDelaiMute = Integer.toString(Integer.parseInt(nbDelaiMute.equals("") ? "0" : nbDelaiMute) + 1);
            }

        } catch (Exception e) {
            if (e instanceof COTransitionException) {
                transaction.addErrors(COActionUtils.getMessage(getSession(), (COTransitionException) e));
            } else {
                transaction.addErrors(e.getMessage());
            }

            // on remet le contentieux à bidon pour éviter des updates alors
            // qu'on veut faire un add
            setIdContentieux(COContentieux.ID_CONTENTIEUX_BIDON);
            _setSpy(null);
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        super._beforeUpdate(transaction);

        if (isProchaineDateDeclenchementChangee()) {
            nbDelaiMute = Integer.toString(Integer.parseInt(nbDelaiMute.equals("") ? "0" : nbDelaiMute) + 1);
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return ICOContentieuxConstante.TABLE_NAME_AVS;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idContentieux = statement.dbReadNumeric(ICOContentieuxConstante.FNAME_ID_CONTENTIEUX);
        idCompteAnnexe = statement.dbReadNumeric(ICOContentieuxConstante.FNAME_ID_COMPTE_ANNEXE);
        dateOuverture = statement.dbReadDateAMJ(ICOContentieuxConstante.FNAME_DATE_OUVERTURE);
        dateDeclenchement = statement.dbReadDateAMJ(ICOContentieuxConstante.FNAME_DATE_DECLENCHEMENT);
        dateExecution = statement.dbReadDateAMJ(ICOContentieuxConstante.FNAME_DATE_EXECUTION);
        prochaineDateDeclenchement = statement.dbReadDateAMJ(ICOContentieuxConstante.FNAME_PROCHAINE_DATE_DECL);
        montantInitial = statement.dbReadNumeric(ICOContentieuxConstante.FNAME_MONTANT_INITIAL, 2);
        user = statement.dbReadString(ICOContentieuxConstante.FNAME_USERNAME);
        idEtape = statement.dbReadNumeric(ICOContentieuxConstante.FNAME_ID_ETAPE);
        idSequence = statement.dbReadNumeric(ICOContentieuxConstante.FNAME_ID_SEQUENCE);
        remarque = statement.dbReadString(ICOContentieuxConstante.FNAME_REMARQUE);
        idSection = statement.dbReadNumeric(ICOContentieuxConstante.FNAME_ID_SECTION);
        nbDelaiMute = statement.dbReadNumeric(ICOContentieuxConstante.FNAME_NB_DELAI_MUTE);
        idCompteAnnexePrincipal = statement.dbReadNumeric(ICOContentieuxConstante.FNAME_ID_COMPTE_ANNEXE_PRINCIPAL);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // on ne peut pas créer un contentieux ARD pour un compte d'affilié,
        // seulement un administrateur
        if (ICOSequence.CS_SEQUENCE_ARD.equals(getLibSequence())
                && !IntRole.ROLE_ADMINISTRATEUR.equals(getCompteAnnexe().getIdRole())) {
            _addError(statement.getTransaction(), getSession().getLabel("AQUILA_ARD_AFFILIE_INTERDIT"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(ICOContentieuxConstante.FNAME_ID_CONTENTIEUX,
                this._dbWriteNumeric(statement.getTransaction(), idContentieux, ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(ICOContentieuxConstante.FNAME_ID_CONTENTIEUX,
                this._dbWriteNumeric(statement.getTransaction(), idContentieux, "idContentieux"));
        statement.writeField(ICOContentieuxConstante.FNAME_ID_COMPTE_ANNEXE,
                this._dbWriteNumeric(statement.getTransaction(), idCompteAnnexe, "idCompteAnnexe"));
        statement.writeField(ICOContentieuxConstante.FNAME_DATE_OUVERTURE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateOuverture, "dateOuverture"));
        statement.writeField(ICOContentieuxConstante.FNAME_DATE_DECLENCHEMENT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDeclenchement, "dateDeclenchement"));
        statement.writeField(ICOContentieuxConstante.FNAME_DATE_EXECUTION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateExecution, "dateExecution"));
        statement.writeField(ICOContentieuxConstante.FNAME_PROCHAINE_DATE_DECL, this._dbWriteDateAMJ(
                statement.getTransaction(), prochaineDateDeclenchement, "prochaineDateDeclenchement"));
        statement.writeField(ICOContentieuxConstante.FNAME_MONTANT_INITIAL,
                this._dbWriteNumeric(statement.getTransaction(), montantInitial, "montantInitial"));
        statement.writeField(ICOContentieuxConstante.FNAME_USERNAME,
                this._dbWriteString(statement.getTransaction(), user, ICOContentieuxConstante.FNAME_USERNAME));
        statement.writeField(ICOContentieuxConstante.FNAME_ID_ETAPE,
                this._dbWriteNumeric(statement.getTransaction(), idEtape, "etape"));
        statement.writeField(ICOContentieuxConstante.FNAME_ID_SEQUENCE,
                this._dbWriteNumeric(statement.getTransaction(), idSequence, "typeSequence"));
        statement.writeField(ICOContentieuxConstante.FNAME_REMARQUE,
                this._dbWriteString(statement.getTransaction(), remarque, ICOContentieuxConstante.FNAME_REMARQUE));
        statement.writeField(ICOContentieuxConstante.FNAME_ID_SECTION,
                this._dbWriteNumeric(statement.getTransaction(), idSection, "idSection"));
        statement.writeField(ICOContentieuxConstante.FNAME_NB_DELAI_MUTE,
                this._dbWriteNumeric(statement.getTransaction(), nbDelaiMute, "nbDelaiMute"));
        statement.writeField(ICOContentieuxConstante.FNAME_ID_COMPTE_ANNEXE_PRINCIPAL,
                this._dbWriteNumeric(statement.getTransaction(), idCompteAnnexePrincipal, "idCompteAnnexePrincipal"));
    }

    /**
     * implementation de la methode annulerDerniereEtape de ICOContentieux.
     * 
     * @param criteres
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void annulerDerniereEtape(HashMap criteres, BTransaction transaction) throws Exception {
        // rechercher le contentieux
        COContentieuxManager contentieuxMgr = creerManager(criteres);
        contentieuxMgr.find();

        if (contentieuxMgr.isEmpty() || (contentieuxMgr.size() > 1)) {
            // contentieux non trouvé ou plusieus contentieux trouvés, on ne
            // sait lequel traiter
            throw new Exception("Impossible de trouver un contentieux correspondants aux critéres");
        }

        // annuler la derniere etape du contentieux
        COContentieux contentieux = (COContentieux) contentieuxMgr.get(0);
        COHistorique historiqueAnnule = contentieux.loadHistorique();

        if ((historiqueAnnule == null) || (historiqueAnnule.getEtape() == null)) {
            throw new Exception("Il n'y a pas d'historique pour ce contentieux");
        } else if (!ICOEtape.CS_CONTENTIEUX_CREE.equals(historiqueAnnule.getEtape().getLibEtape())) {
            // HACK: sinon on pourra pas faire d'update
            // TODO sel : est-ce utile ???
            contentieux = COContentieuxFactory.loadContentieux(getISession(), contentieux.getIdContentieux());
            COServiceLocator.getCancelTransitionService().annulerDerniereTransition(getSession(), transaction,
                    contentieux, historiqueAnnule, false, true);
            // Met le dernier historique à Annule.
            historiqueAnnule.setAnnule(new Boolean(true));
            historiqueAnnule.update(transaction);
            while ((historiqueAnnule == null) || historiqueAnnule.isImpute().booleanValue()) {
                historiqueAnnule = contentieux.loadLastHistorique(Boolean.FALSE, Boolean.FALSE);
                COServiceLocator.getCancelTransitionService().annulerDerniereTransition(contentieux.getSession(),
                        transaction, contentieux, historiqueAnnule, true, true);
                historiqueAnnule.setAnnule(new Boolean(true));
                historiqueAnnule.update(transaction);
            }
        }
    }

    /**
     * @param contentieux
     */
    public void copyFrom(COContentieux contentieux) {
        compteAnnexe = contentieux.compteAnnexe;
        dateDeclenchement = contentieux.dateDeclenchement;
        dateExecution = contentieux.dateExecution;
        dateOuverture = contentieux.dateOuverture;
        etape = contentieux.etape;
        idCompteAnnexe = contentieux.idCompteAnnexe;
        idContentieux = contentieux.idContentieux;
        idEtape = contentieux.idEtape;
        idSection = contentieux.idSection;
        idSequence = contentieux.idSequence;
        montantInitial = contentieux.montantInitial;
        prochaineDateDeclenchement = contentieux.prochaineDateDeclenchement;
        remarque = contentieux.remarque;
        section = contentieux.section;
        sequence = contentieux.sequence;
        user = contentieux.user;
        montantTotalFrais = contentieux.montantTotalFrais;
        montantTotalInterets = contentieux.montantTotalInterets;
        montantTotalTaxes = contentieux.montantTotalTaxes;
    }

    /**
     * implementation de la methode creerContentieux de ICOContentieux.
     * 
     * @param idSection
     *            DOCUMENT ME!
     * @param csSequence
     *            DOCUMENT ME!
     * @param dateProchainDeclenchement
     *            DOCUMENT ME!
     * @param remarque
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public COContentieux creerContentieux(String idSection, String csSequence, String dateProchainDeclenchement,
            String remarque) throws Exception {
        COContentieux contentieux = new COContentieux();

        contentieux.setIdSection(idSection);
        contentieux.setDateDeclenchement(dateProchainDeclenchement);
        contentieux.setRemarque(remarque);

        return contentieux;
    }

    /**
     * @param criteres
     * @return
     * @throws Exception
     */
    private COContentieuxManager creerManager(Map criteres) throws Exception {
        COContentieuxManager contentieux = new COContentieuxManager();

        // renseigne les criteres dans le manager
        if (criteres.containsKey(ICOContentieux.FOR_ID_SECTION)) {
            contentieux.setForIdSection((String) criteres.get(ICOContentieux.FOR_ID_SECTION));
        }

        if (criteres.containsKey(ICOContentieux.FOR_ID_SEQUENCE_CONTENTIEUX)) {
            contentieux.setForIdSequence((String) criteres.get(ICOContentieux.FOR_ID_SEQUENCE_CONTENTIEUX));
        }

        contentieux.setSession(getSession());

        return contentieux;
    }

    /**
     * implementation de la methode creerSommationSursisPaiement de ICOContentieux.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param idSection
     *            DOCUMENT ME!
     * @param dateExecution
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public COContentieux creerSommationSursisPaiement(BSession session, BTransaction transaction, String idSection,
            String dateExecution) throws Exception {
        // créer des sessions pour les différentes applis
        BSession sessionAquila = (BSession) GlobazSystem.getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA)
                .newSession();
        session.connectSession(sessionAquila);

        // session osiris
        BSession sessionOsiris = null;
        if (session.getApplicationId().equals(CAApplication.DEFAULT_APPLICATION_OSIRIS)) {
            sessionOsiris = session;
        } else {
            sessionOsiris = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                    .newSession();
            session.connectSession(sessionOsiris);
        }

        // créer
        COContentieux contentieux = null;
        COTransition transition = null;

        // charger la section
        CASection section = new CASection();
        section.setSession(sessionOsiris);
        section.setIdSection(idSection);
        section.retrieve();

        // charger le compte annexe
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(sessionOsiris);
        compteAnnexe.setIdCompteAnnexe(section.getIdCompteAnnexe());
        compteAnnexe.retrieve();

        // charger l'étape de sommation
        COEtapeManager etapeManager = new COEtapeManager();
        COEtape etapeSommation = null;
        etapeManager.setSession(sessionAquila);
        etapeManager.setForLibEtape(ICOEtape.CS_SOMMATION_ENVOYEE);
        etapeManager.setForIdSequence(section.getIdSequenceContentieux());
        etapeManager.find();

        if (!etapeManager.isEmpty()) {
            etapeSommation = (COEtape) etapeManager.getFirstEntity();
        } else {
            throw new Exception("Impossible de trouver l'étape de sommation pour cette séquence contentieux");
        }

        // préparer le chargement de la transition
        COTransitionManager transitionManager = new COTransitionManager();
        transitionManager.setSession(sessionAquila);
        transitionManager.setForIdEtapeSuivante(etapeSommation.getIdEtape());

        COContentieuxManager contentieuxManager = new COContentieuxManager();
        contentieuxManager.setSession(sessionAquila);
        contentieuxManager.setForIdSection(idSection);
        contentieuxManager.setForIdSequence(COSequence.loadSequence(session, section, compteAnnexe).getIdSequence());
        contentieuxManager.find();
        // vérifier qu'il n'existe pas déjà un contentieux
        if (!contentieuxManager.isEmpty()) {
            // il existe un contentieux pour cette section, regardons si l'on
            // peut atteindre la sommation
            contentieux = (COContentieux) contentieuxManager.getFirstEntity();

            // charger les transitions
            transitionManager.setForIdEtape(contentieux.getIdEtape());
            transitionManager.find();

            if (!transitionManager.isEmpty()) {
                // on peut passer à la sommation, on va donc le faire
                transition = (COTransition) transitionManager.getFirstEntity();
            } else {
                // on ne peut pas passer à la sommation, il est certain que le
                // contentieux est déjà passé par cette étape
                return contentieux;
            }
        } else {
            // créer le contentieux
            contentieux = COContentieuxFactory.creerNouveauContentieux(sessionAquila, section, compteAnnexe);
            contentieux.add(transaction);

            // charger les transitions
            transitionManager.setForIdEtape(contentieux.getIdEtape());
            transitionManager.find();

            if (!transitionManager.isEmpty()) {
                // on peut passer à la sommation, on va donc le faire
                transition = (COTransition) transitionManager.getFirstEntity();
            } else {
                throw new Exception("Impossible de trouver la transition de sommation pour cette séquence contentieux");
            }
        }

        // effectuer la transition de sommation
        COTransitionAction action = COServiceLocator.getActionService().getTransitionAction(transition);

        // ne pas envoyer les documents
        ((COAbstractEnvoyerDocument) action).wantEnvoyerDocument(false);
        action.setDateExecution(dateExecution);
        action.setMotif(sessionAquila.getLabel("AQUILA_SURSIS_AU_PAIEMENT"));

        // retrouve les infos par étapes
        List etapeInfos = transition.getEtapeSuivante().loadEtapeInfoConfigs();

        for (Iterator infoIter = etapeInfos.iterator(); infoIter.hasNext();) {
            COEtapeInfoConfig infoConfig = (COEtapeInfoConfig) infoIter.next();

            // renseigner les valeurs avec vide
            action.addEtapeInfo(infoConfig, "");

            // remplacer la date d'exécution si nécessaire
            if (infoConfig.getRemplaceDateExecution().booleanValue()) {
                contentieux.setDateExecution(dateExecution);
                action.setDateExecution(contentieux.getDateExecution());
            }
        }

        // Effectue la transition
        try {
            action.canExecute(contentieux, transaction, false);
        } catch (COTransitionException e) {
            transaction.addErrors(e.getMessage());
            throw new Exception(e);
        }
        COServiceLocator.getTransitionService()
                .executerTransition(getSession(), transaction, contentieux, action, null);

        // remonter les erreurs dans la transaction
        if (contentieux.getSession().hasErrors()) {
            transaction.addErrors(contentieux.getSession().getErrors().toString());
        }

        if (contentieux.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            transaction.addErrors(contentieux.getMessage());
        }

        return contentieux;
    }

    /**
     * Détermine si l'échéance est dépassée.
     * 
     * @return true Si l'échéance est dépassée
     * @throws JAException
     *             Si le format de date est invalide
     */
    public boolean echeanceDepassee() throws JAException {
        JACalendarGregorian calendar = new JACalendarGregorian();
        JADate echeance = new JADate(getProchaineDateDeclenchement());

        return calendar.compare(JACalendar.today(), echeance) == JACalendar.COMPARE_FIRSTUPPER;
    }

    /**
     * @return <tt>true</tt> si le contentieux est suspendu à la date du jour.
     * @throws Exception
     */
    public boolean estSuspendu() throws Exception {
        return this.estSuspendu(JACalendar.todayJJsMMsAAAA());
    }

    /**
     * Détermine si le contentieux est suspendu.
     * 
     * @param dateReference
     * @return true Si le contentieux est suspendu
     * @throws Exception
     */
    public boolean estSuspendu(String dateReference) throws Exception {
        return (getOrigineSuspendu(dateReference) != null)
                || APISection.MODE_REPORT.equals(getSection().getIdModeCompensation());
    }

    /**
     * Cette méthode permet de voir si un motif de blocage de contentieux doit être traité
     * 
     * @param codeSysteme
     * @param etapeEnCours
     * @return true -> Bloque ; false -> ne bloque pas.
     * @throws Exception
     */
    private boolean etapeSuspendueEnFonctionDuMotifBlocage(String codeSysteme, String etapeEnCours, int selection)
            throws Exception {
        // Charge la table du paramétrage des motifs de blocage du contentieux
        HashMap mo = COContentieux.CONFIG_SERVICE.getGestionMotifContentieux(getSession(),
                COConfigurationService.GESTION_MOTIF_BLOCAGE);

        /**
         * <pre>
         *      Type
         *      ----
         *      0 = Aucun blocage
         *      1 = Blocage compte annexe
         *      2 = Blocage section
         *      3 = Blocage compte annexe + section
         * </pre>
         */
        if (etapeEnCours.equals(ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_SANS_OPPOSITION)
                || etapeEnCours.equals(ICOEtape.CS_JUGEMENT_DE_MAINLEVEE_SAISI)
                || etapeEnCours.equals(ICOEtape.CS_PV_SAISIE_VALANT_ADB_SAISI)
                || etapeEnCours.equals(ICOEtape.CS_AVIS_VENTE_AUX_ENCHERES_SAISI)
                || etapeEnCours.equals(ICOEtape.CS_PV_DE_SAISIE_SAISI)
                || etapeEnCours.equals(ICOEtape.CS_SURSIS_DE_L_ARTICLE_123LP_SAISI)
                || etapeEnCours.equals(ICOEtape.CS_ACTE_DE_DEFAUT_DE_BIEN_SAISI)
                || etapeEnCours.equals(ICOEtape.CS_POURSUITE_RADIEE)
                || etapeEnCours.equals(ICOEtape.CS_POURSUITE_CLASSEE)
                || etapeEnCours.equals(ICOEtape.CS_IRRECOUVRABLE)
                || etapeEnCours.equals(ICOEtape.CS_VERSEMENT_IMPUTE)
                || etapeEnCours.equals(ICOEtape.CS_FRAIS_ET_INTERETS_RECLAMES)
                || etapeEnCours.equals(ICOEtape.CS_RAPPEL_DE_RECLAMATION_DE_FRAIS_ET_INTERETS_ENVOYE)
                || etapeEnCours
                        .equals(ICOEtape.CS_DEMANDE_DE_RETRAIT_DE_LA_REQUISITION_DE_CONTINUER_LA_POURSUITE_ENVOYE)
                || etapeEnCours.equals(ICOEtape.CS_RETRAIT_REQUISITION_VENTE)) {

            return false;
        }
        if ((COEtapeBlocage) mo.get(codeSysteme) == null) {
            return true;
        }
        int type = ((COEtapeBlocage) mo.get(codeSysteme)).typeBlocage;
        // Si le type de blocage vaut 0, on effectue aucun blocage sur le motif
        if (type == 0) {
            return false;
        }
        if (selection == COContentieux.CHECK_COMPTE_ANNEXE) {
            if ((mo.containsKey(codeSysteme) && (((COEtapeBlocage) mo.get(codeSysteme)).codeSystemeEtape
                    .indexOf(etapeEnCours) > -1)) && ((type == 1) || (type == 3))) {
                return false;
            }
        } else if (selection == COContentieux.CHECK_SECTION) {
            if ((mo.containsKey(codeSysteme) && (((COEtapeBlocage) mo.get(codeSysteme)).codeSystemeEtape
                    .indexOf(etapeEnCours) > -1)) && ((type == 2) || (type == 3))) {
                return false;
            }
        }

        return true;
    }

    /**
     * @autor : oca Utile pour les recherches en ged pour un contentieux lorsque l'on ne connait que la section
     */
    public String findIdContentieuxForIdSection(String idSection) throws Exception {
        String idCtx = "";
        try {
            COContentieuxManager mgr = new COContentieuxManager();
            mgr.setSession(getSession());
            mgr.setForIdSection(idSection);
            mgr.find();

            if (mgr.size() > 1) {
                throw new Exception("Plusieurs contentieux trouvés pour la section id=" + idSection);
            } else if (mgr.size() == 1) {
                COContentieux ctx = (COContentieux) mgr.getFirstEntity();
                idCtx = ctx.getIdContentieux();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idCtx;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public CACompteAnnexe getCompteAnnexe() {
        return compteAnnexe;
    }

    /**
     * @return Le numéro de poursuite
     */
    public String getCompteAnnexeInformation() {
        if (getCompteAnnexe() != null) {
            return getCompteAnnexe().getInformation();
        } else {
            return "0";
        }
    }

    /**
     * getter pour l'attribut compte annexe principal.
     * 
     * @return la valeur courante de l'attribut compte annexe principal
     */
    public CACompteAnnexe getCompteAnnexePrincipal() {
        return compteAnnexePrincipal;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDateDeclenchement() {
        return dateDeclenchement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDateExecution() {
        return dateExecution;
    }

    /**
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String getDateFinSuspendu() throws Exception {
        return this.getDateFinSuspendu(JACalendar.todayJJsMMsAAAA());
    }

    /**
     * @param dateReference
     * @return La date de fin de suspension
     * @throws Exception
     */
    public String getDateFinSuspendu(String dateReference) throws Exception {
        CAMotifContentieux motifContentieux = getOrigineSuspendu(dateReference);

        return (motifContentieux != null) ? motifContentieux.getDateFin() : "";
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDateOuverture() {
        return dateOuverture;
    }

    /**
     * Retourne les écritures des versements de la section.
     * 
     * @return Collection Ecritures des versements de la section
     * @throws Exception
     *             En cas de problème
     */
    public Collection getEcrituresVersementsSection() throws Exception {
        Collection ecritures = new Vector();
        BSession remoteSession = (BSession) ((COApplication) GlobazServer.getCurrentSystem().getApplication(
                ICOApplication.DEFAULT_APPLICATION_AQUILA)).getSessionOsiris(getSession());

        // Recherche des écritures de versement
        CAEcritureManager ecritureManager = new CAEcritureManager();
        ecritureManager.setSession(remoteSession);
        ecritureManager.setForIdCompteAnnexe(getIdCompteAnnexe());
        ecritureManager.setForIdSection(getIdSection());

        // @TODO Mettre à jour le type d'opération (versements)
        ecritureManager.setForIdTypeOperation(APIOperation.CAPAIEMENT);

        /*
         * on ne veut pas les versements effectués avant la date d'ouverture car ils sont déjà comptabilisés dans le
         * solde de la section au moment de la création du contentieux.
         */
        ecritureManager.setFromDate(getDateOuverture());
        ecritureManager.find();

        for (int i = 0; i < ecritureManager.size(); i++) {
            ecritures.add(ecritureManager.getEntity(i));
        }

        return ecritures;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public COEtape getEtape() {
        return etape;
    }

    protected HashMap getGestionMotifContentieux(BSession session, COConfigurationKey key) throws COTransitionException {
        try {
            return COContentieux.CONFIG_SERVICE.getGestionMotifContentieux(session, key);
        } catch (Exception e) {
            throw new COTransitionException("AQUILA_OPTION_CONFIG_ERREUR", COActionUtils.getMessage(session,
                    "AQUILA_OPTION_CONFIG_ERREUR"));
        }
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * getter pour l'attribut id compte annexe principal.
     * 
     * @return la valeur courante de l'attribut id compte annexe principal
     */
    public String getIdCompteAnnexePrincipal() {
        return idCompteAnnexePrincipal;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdContentieux() {
        return idContentieux;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdEtape() {
        return idEtape;
    }

    /**
     * @return l'id étape de l'étape précédente qui n'a pas été annulé et qui n'est pas Imputer versement.
     * @throws Exception
     * @deprecated n'est pas utilisé
     */
    @Deprecated
    public String getIdEtapePrecedente() throws Exception {
        String retour = "";
        COHistoriqueService historiqueService = COServiceLocator.getHistoriqueService();
        COHistorique historique;

        if (!JadeStringUtil.isBlank(getIdEtape())) {
            historique = historiqueService.loadHistorique(getSession(), this, getIdEtape(), new Boolean(false),
                    new Boolean(true));

            if ((historique != null) && !JadeStringUtil.isBlank(historique.getIdHistoriquePrecedant())) {
                historique.setIdHistorique(historique.getIdHistoriquePrecedant());
                historique.retrieve(getSession().getCurrentThreadTransaction());
                retour = historique.getIdEtape();
            }
        }

        return retour;
    }

    /**
     * @return l'id étape de l'étape précédente qui n'a pas été annulé et qui n'est pas Imputer versement.
     * @throws Exception
     */
    public String getIdEtapePrecedenteNonSpecifique() throws Exception {
        String retour = "";
        COHistoriqueService historiqueService = COServiceLocator.getHistoriqueService();
        COHistorique historique = null;

        if (getEtape().getTypeEtape().equals(ICOEtape.CS_TYPE_SPECIFIQUE)) {
            if (!JadeStringUtil.isBlank(getIdEtape())) {
                historique = historiqueService.loadHistoriqueNotForEtapeSpecifique(getSession(), this, Boolean.FALSE,
                        Boolean.TRUE);
                if (historique != null) {
                    retour = historique.getIdEtape();
                }
            }
        }

        return retour;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdSequence() {
        return idSequence;
    }

    /**
     * @return un cs qui indique le libellé à afficher lorsque l'utilisateur a atteint cette étape pour un contentieux.
     */
    public String getLibEtape() {
        return getEtape().getLibEtape();
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getLibSequence() {
        return getSequence().getLibSequence();
    }

    /**
     * Calcule et retourne le total des frais et des amendes.
     * 
     * @return Le montant des frais
     */
    public String getMontantFrais() {
        BigDecimal frais = new BigDecimal(getSection().getFrais());
        frais = frais.add(new BigDecimal(getSection().getAmende()));
        return frais.toString();
    }

    /**
     * @return Le montant des frais et amendes, formatés
     */
    public String getMontantFraisFormate() {
        return JANumberFormatter.formatNoRound(getMontantFrais(), 2);
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getMontantInitial() {
        return montantInitial;
    }

    /**
     * Calcule et retourne le montant des intérêts.
     * 
     * @return Le montant des intérêts
     */
    public String getMontantInterets() {
        return getSection().getInterets();
    }

    /**
     * @return Le montant des intérêts, formaté
     */
    public String getMontantInteretsFormate() {
        return JANumberFormatter.formatNoRound(getMontantInterets(), 2);
    }

    /**
     * Calcule et retourne le montant des taxes.
     * 
     * @return Le montant des taxes
     */
    public String getMontantTaxes() {
        return getSection().getTaxes();
    }

    /**
     * @return Le montant des taxes, formaté
     */
    public String getMontantTaxesFormate() {
        return JANumberFormatter.formatNoRound(getMontantTaxes(), 2);
    }

    /**
     * Retourne le montant total des frais en se basant sur l'historique (pas la compta).
     * 
     * @return DOCUMENT ME!
     */
    public String getMontantTotalFrais() {
        return JANumberFormatter.formatNoRound(montantTotalFrais, 2);
    }

    /**
     * Retourne le montant total des frais formatté en se basant sur l'historique (pas la compta).
     * 
     * @return DOCUMENT ME!
     */
    public String getMontantTotalFraisFormatte() {
        return JANumberFormatter.formatNoRound(montantTotalFrais, 2);
    }

    /**
     * Retourne le montant total des intérêts en se basant sur l'historique (pas la compta).
     * 
     * @return DOCUMENT ME!
     */
    public String getMontantTotalInterets() {
        return JANumberFormatter.formatNoRound(montantTotalInterets, 2);
    }

    /**
     * Retourne le montant total des intérêts formatté en se basant sur l'historique (pas la compta).
     * 
     * @return DOCUMENT ME!
     */
    public String getMontantTotalInteretsFormatte() {
        return JANumberFormatter.formatNoRound(montantTotalInterets, 2);
    }

    /**
     * Retourne le montant total des taxes en se basant sur l'historique (pas la compta).
     * 
     * @return DOCUMENT ME!
     */
    public String getMontantTotalTaxes() {
        return JANumberFormatter.formatNoRound(montantTotalTaxes, 2);
    }

    /**
     * Retourne le montant total des taxes formatté en se basant sur l'historique (pas la compta).
     * 
     * @return DOCUMENT ME!
     */
    public String getMontantTotalTaxesFormatte() {
        return JANumberFormatter.formatNoRound(montantTotalTaxes, 2);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWParametersUserCode getMotifSuspendu() throws Exception {
        return this.getMotifSuspendu(JACalendar.todayJJsMMsAAAA());
    }

    /**
     * @param dateReference
     * @return Le motif de suspension
     * @throws Exception
     */
    public FWParametersUserCode getMotifSuspendu(String dateReference) throws Exception {
        CAMotifContentieux motifContentieux = getOrigineSuspendu(dateReference);

        return (motifContentieux != null) ? motifContentieux.getUcMotifContentieuxSuspendu()
                : new FWParametersUserCode();
    }

    /**
     * @return DOCUMENT ME!
     */
    public String getNbDelaiMute() {
        return nbDelaiMute;
    }

    /**
     * @return Le numéro de poursuite
     */
    public String getNumPoursuite() {
        return getSection().getNumeroPoursuite();
    }

    /**
     * @return Le numéro de section
     */
    public String getNumSection() {
        return getSection().getIdExterne();
    }

    /**
     * Détermine l'origine de la suspension du contentieux.
     * 
     * @param dateReference
     * @return PAS_SUSPENDU, SUSPENDU_COMPTE_ANNEXE ou SUSPENDU_SECTION
     * @throws Exception
     */
    private CAMotifContentieux getOrigineSuspendu(String dateReference) throws Exception {
        CAMotifContentieuxManager motifContentieuxManager = null;

        // verification de la section
        if (getSection().getContentieuxEstSuspendu().booleanValue()) {
            motifContentieuxManager = new CAMotifContentieuxManager();
            motifContentieuxManager.setSession(getSession());
            motifContentieuxManager.setForIdSection(getIdSection());
            motifContentieuxManager.setFromDateBetweenDebutFin(dateReference);
            motifContentieuxManager.find();

            if (!motifContentieuxManager.isEmpty()) {
                return (CAMotifContentieux) motifContentieuxManager.get(0);
            }
        }

        // verification du compte annexe
        if (getCompteAnnexe().getContEstBloque().booleanValue()) {
            if (motifContentieuxManager == null) {
                motifContentieuxManager = new CAMotifContentieuxManager();
                motifContentieuxManager.setSession(getSession());
                motifContentieuxManager.setFromDateBetweenDebutFin(dateReference);
            } else {
                motifContentieuxManager.setForIdSection("");
            }

            motifContentieuxManager.setForIdCompteAnnexe(getIdCompteAnnexe());
            motifContentieuxManager.find();

            if (!motifContentieuxManager.isEmpty()) {
                return (CAMotifContentieux) motifContentieuxManager.get(0);
            }
        }

        return null;
    }

    /**
     * Retourne la période de la section.
     * 
     * @return La période de la section
     */
    public String getPeriodeSection() {
        return CODateUtils.getDateMM(getSection().getDateDebutPeriode()) + "-"
                + CODateUtils.getDateMM(getSection().getDateFinPeriode()) + "."
                + CODateUtils.getDateYYYY(getSection().getDateDebutPeriode());
    }

    /**
     * @return the previsionnel
     */
    public Boolean getPrevisionnel() {
        return previsionnel;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getProchaineDateDeclenchement() {
        return prochaineDateDeclenchement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getRemarque() {
        return remarque;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public CASection getSection() {
        return section;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public COSequence getSequence() {
        return sequence;
    }

    /**
     * Calcule et retourne le solde.
     * 
     * @return Le solde
     */
    public String getSolde() {
        return getSection().getSolde();
    }

    /**
     * @return Le solde, formaté
     */
    public String getSoldeFormate() {
        return JANumberFormatter.formatNoRound(getSolde(), 2);
    }

    public String getTableName() {
        return _getTableName();
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getUser() {
        return user;
    }

    /**
     * implementation de la methode hasContentieux de ICOContentieux.
     * 
     * @param criteres
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public Boolean hasContentieux(HashMap criteres) throws Exception {
        COContentieuxManager contentieux = creerManager(criteres);

        return contentieux.getCount() > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * implementation de la methode isEtapeExecutee de ICOContentieux.
     * 
     * @param csEtape
     *            DOCUMENT ME!
     * @param dateDe
     *            DOCUMENT ME!
     * @param dateA
     *            DOCUMENT ME!
     * @param forIdContentieux
     *            DOCUMENT ME!
     * @param forIdSequence
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public Boolean isEtapeExecutee(String csEtape, String dateDe, String dateA, String forIdContentieux,
            String forIdSequence) throws Exception {
        boolean retValue = false;
        String dateExecution = null;

        // Charge l'etape
        COEtapeManager etapeManager = new COEtapeManager();
        etapeManager.setForIdSequence(forIdSequence);
        etapeManager.setForLibAction(csEtape);
        etapeManager.setSession(getSession());
        etapeManager.find();

        if (etapeManager.size() != 1) {
            throw new Exception("Impossible de retrouver l'étape");
        }

        String idEtape = ((COEtape) etapeManager.getEntity(0)).getIdEtape();

        // Charge l'historique du contentieux
        COHistoriqueManager historiqueManager = new COHistoriqueManager();

        historiqueManager.setSession(getSession());
        historiqueManager.setForIdContentieux(forIdContentieux);
        historiqueManager.setAfficheEtapesAnnulees(Boolean.FALSE);
        historiqueManager.setOrderBy(ICOHistoriqueConstante.FNAME_ID_HISTORIQUE + " DESC ");
        historiqueManager.find(BManager.SIZE_NOLIMIT);

        COHistorique historique = null;

        for (int i = 0; (i < historiqueManager.size()) && !retValue; i++) {
            historique = (COHistorique) historiqueManager.getEntity(i);

            if (historique.getIdEtape().equals(idEtape)) {
                dateExecution = historique.getDateExecution();
                retValue = true;
            }
        }

        // Retourne vrai si l'étape a été executée et si sa date d'execution est
        // entre les 2 dates qu'on veut. Si les
        // dates sont nulles, on ne regarde que le retValue
        if (JadeStringUtil.isBlank(dateDe) || JadeStringUtil.isBlank(dateA)) {
            return new Boolean(retValue);
        } else {
            return new Boolean((retValue && BSessionUtil.compareDateBetweenOrEqual(getSession(), dateDe, dateA,
                    dateExecution)));
        }
    }

    /**
     * Retourne vrai si l'identifiant de l'étape n'a pas changé mais que la date de prochain déclenchement a changé.
     * 
     * @return true si l'identifiant de l'étape n'a pas changé mais que la date de prochain déclenchement a changé.
     */
    public boolean isProchaineDateDeclenchementChangee() {
        return idEtape.equals(oldIdEtape) && prochaineDateDeclenchementChangee;
    }

    /**
     * @return DOCUMENT ME!
     */
    public boolean isRefreshLinks() {
        return refreshLinks;
    }

    /**
     * implementation de la methode load de ICOContentieux.
     * 
     * @param criteres
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public BManager load(HashMap criteres) throws Exception {
        COContentieuxManager contentieux = creerManager(criteres);
        contentieux.find();

        return contentieux;
    }

    /**
     * @param session
     * @throws Exception
     */
    public void loadAffilieSection(BSession session) throws Exception {
        // TODO Faut-il conserver cette méthode ?
        // Actualisation du compte annexe
        compteAnnexe.setSession(session);
        compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());
        compteAnnexe.retrieve();
        // Actualisation de la section
        section.setSession(session);
        section.setIdSection(getIdSection());
        section.retrieve();
        // L'ETAPE
        COSequenceManager sequenceManager = new COSequenceManager();

        sequenceManager.setSession(session);
        sequenceManager.setForLibSequence(ICOSequenceConstante.CS_SEQUENCE_AVS);
        sequenceManager.find();

        sequence = (COSequence) sequenceManager.get(0);
        setIdSequence(sequence.getIdSequence());
        etape.setLibEtape(ICOEtape.CS_PREMIER_RAPPEL_ENVOYE);
    }

    /**
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface loadContentieuxViewBean() throws Exception {
        if (isNew()) {
            COContentieux contentieux = new COContentieuxViewBean();

            contentieux.copyFrom(this);

            return contentieux;
        } else {
            return COContentieuxFactory.loadContentieuxViewBean(getISession(), getIdContentieux());
        }
    }

    /**
     * charge la dernière ligne de l'historique pour ce contentieux.
     * 
     * @return la dernière ligne de l'historique ou null s'il n'y en a pas.
     * @throws Exception
     */
    public COHistorique loadHistorique() throws Exception {
        return COServiceLocator.getHistoriqueService().loadHistorique(getSession(), this, getIdEtape());
    }

    /**
     * charge la dernière ligne de l'historique pour ce contentieux.
     * 
     * @param afficheEtapesAnnulees
     * @param cacheImputerVersement
     * @return la dernière ligne de l'historique ou null s'il n'y en a pas.
     * @throws Exception
     */
    public COHistorique loadHistorique(Boolean afficheEtapesAnnulees, Boolean cacheImputerVersement) throws Exception {
        return COServiceLocator.getHistoriqueService().loadHistorique(getSession(), this, getIdEtape(),
                afficheEtapesAnnulees, cacheImputerVersement);
    }

    /**
     * @return la dernière ligne de l'historique sans tenir compte des étapes annulée, imputer versement et dont
     *         IgnorerDateExecution est coché
     * @throws Exception
     */
    public COHistorique loadHistoriqueIgnorerDateExecution() throws Exception {
        return COServiceLocator.getHistoriqueService().loadHistoriqueIgnorerDateExecution(getSession(), this);
    }

    /**
     * @param afficheEtapesAnnulees
     * @param cacheImputerVersement
     * @return la dernière ligne de l'historique quelque soit l'étape ou null s'il n'y en a pas.
     * @throws Exception
     */
    public COHistorique loadLastHistorique(Boolean afficheEtapesAnnulees, Boolean cacheImputerVersement)
            throws Exception {
        return COServiceLocator.getHistoriqueService().loadHistorique(getSession(), this, "", afficheEtapesAnnulees,
                cacheImputerVersement);
    }

    /**
     * Actualise les objets liés.
     * 
     * @param transaction
     *            La transaction à utiliser
     * @exception Exception
     *                Si un problème est rencontré
     */
    public void refreshLinks(BTransaction transaction) throws Exception {
        BSession remoteSession = (BSession) ((COApplication) GlobazServer.getCurrentSystem().getApplication(
                ICOApplication.DEFAULT_APPLICATION_AQUILA)).getSessionOsiris(getSession());

        // Actualisation de l'étape
        etape = new COEtape();
        etape.setSession(getSession());
        etape.setIdEtape(idEtape);
        etape.retrieve(transaction);

        // Actualisation de la séquence
        sequence = new COSequence();
        sequence.setSession(getSession());
        sequence.setIdSequence(idSequence);
        sequence.retrieve(transaction);

        // Actualisation du compte annexe
        compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(remoteSession);
        compteAnnexe.setIdCompteAnnexe(idCompteAnnexe);
        compteAnnexe.retrieve(transaction);

        // Actualisation de la section
        section = new CASection();
        section.setSession(remoteSession);
        section.setIdSection(idSection);
        section.retrieve(transaction);

    }

    /**
     * @param annexe
     */
    public void setCompteAnnexe(CACompteAnnexe annexe) {
        compteAnnexe = annexe;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDateDeclenchement(String string) {
        dateDeclenchement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDateExecution(String string) {
        dateExecution = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDateOuverture(String string) {
        dateOuverture = string;
    }

    /**
     * @param etape
     */
    public void setEtape(COEtape etape) {
        this.etape = etape;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdCompteAnnexe(String string) {
        idCompteAnnexe = string;
    }

    /**
     * setter pour l'attribut id compte annexe principal.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdCompteAnnexePrincipal(String string) {
        idCompteAnnexePrincipal = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdContentieux(String string) {
        idContentieux = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdEtape(String string) {
        oldIdEtape = idEtape;
        idEtape = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdSection(String string) {
        idSection = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdSequence(String string) {
        idSequence = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setMontantInitial(String string) {
        montantInitial = string;
    }

    /**
     * @param montantTotalFrais
     *            DOCUMENT ME!
     */
    public void setMontantTotalFrais(String montantTotalFrais) {
        this.montantTotalFrais = montantTotalFrais;
    }

    /**
     * @param montantTotalInterets
     *            DOCUMENT ME!
     */
    public void setMontantTotalInterets(String montantTotalInterets) {
        this.montantTotalInterets = montantTotalInterets;
    }

    /**
     * @param montantTotalTaxes
     *            DOCUMENT ME!
     */
    public void setMontantTotalTaxes(String montantTotalTaxes) {
        this.montantTotalTaxes = montantTotalTaxes;
    }

    /**
     * @param string
     *            DOCUMENT ME!
     */
    public void setNbDelaiMute(String string) {
        nbDelaiMute = string;
    }

    /**
     * @param string
     *            Le numéro de poursuite
     * @throws Exception
     *             Si la sauvegarde de la section a échoué
     */
    public void setNumPoursuite(String string) throws Exception {
        getSection().setNumeroPoursuite(string);
        getSection().save();
    }

    /**
     * @param previsionnel
     *            the previsionnel to set
     */
    public void setPrevisionnel(Boolean previsionnel) {
        this.previsionnel = previsionnel;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setProchaineDateDeclenchement(String string) {
        if (!JadeStringUtil.isBlank(prochaineDateDeclenchement) && !prochaineDateDeclenchement.equals(string)) {
            prochaineDateDeclenchementChangee = true;
        }

        prochaineDateDeclenchement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setRemarque(String string) {
        remarque = string;
    }

    /**
     * @param section
     */
    public void setSection(CASection section) {
        this.section = section;
    }

    /**
     * @param sequence
     */
    public void setSequence(COSequence sequence) {
        this.sequence = sequence;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setUser(String string) {
        user = string;
    }

    /**
     * Détermine l'origine de la suspension du contentieux.
     * 
     * @param dateReference
     * @return PAS_SUSPENDU, SUSPENDU_COMPTE_ANNEXE ou SUSPENDU_SECTION
     * @throws Exception
     */
    public boolean suspendreContentieuxEnFonctionDuMotif(String dateReference, String etape) throws Exception {
        // On ne tient pas compte de l'étape "Création du contentieux"
        if (etape.equalsIgnoreCase(ICOEtape.CS_AUCUNE)) {
            return false;
        }
        boolean suspendreContentieux = false;
        CAMotifContentieuxManager motifContentieuxManager = null;

        // Chargement des motifs
        motifContentieuxManager = new CAMotifContentieuxManager();
        motifContentieuxManager.setSession(getSession());
        motifContentieuxManager.setForIdSection(getIdSection());
        motifContentieuxManager.setForIdCompteAnnexe(getIdCompteAnnexe());
        motifContentieuxManager.setFromDateBetweenDebutFin(dateReference);
        motifContentieuxManager.find();

        if (!motifContentieuxManager.isEmpty()) {
            for (int i = 0; i < motifContentieuxManager.size(); i++) {
                CAMotifContentieux motif = (CAMotifContentieux) motifContentieuxManager.getEntity(i);
                int selection;
                if (!JadeStringUtil.isIntegerEmpty(motif.getIdSection())) {
                    selection = COContentieux.CHECK_SECTION;
                } else {
                    selection = COContentieux.CHECK_COMPTE_ANNEXE;
                }
                // Si on la méthode suivante retourne true, c'est qu'il ne faut
                // pas bloquer le contentieux
                if (etapeSuspendueEnFonctionDuMotifBlocage(motif.getIdMotifBlocage(), etape, selection)) {
                    suspendreContentieux = true;
                }
            }
            return suspendreContentieux;
        }
        return suspendreContentieux;
    }

    /**
     * @param refreshLinks
     */
    public void wantRefreshLinks(boolean refreshLinks) {
        this.refreshLinks = refreshLinks;
    }
}
