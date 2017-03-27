package globaz.aquila.db.access.batch.transition;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.api.ICOEtape;
import globaz.aquila.application.COApplication;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.historique.COHistoriqueService;
import globaz.aquila.service.tiers.COTiersService;
import globaz.aquila.service.transition.COCancelTransitionService;
import globaz.aquila.service.transition.COExecuteTransitionService;
import globaz.aquila.service.transition.COTransitionActionService;
import globaz.aquila.util.COActionUtils;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.external.IntTiers;
import globaz.osiris.process.interetmanuel.CAProcessInteretMoratoireManuel;
import globaz.osiris.process.interetmanuel.visualcomponent.CAInteretManuelVisualComponent;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>Description</h1>
 * <p>
 * Classe abstraite parente de toutes les actions de transition.
 * </p>
 * <p>
 * Les transitions modifient l'état du contentieux et envoyent des documents à l'utilisateur. Les traitements annexes
 * (création de l'historique, comptabilisation des taxes, ...) n'est pas fait ici mais par les services de transition
 * {@link COExecuteTransitionService} et {@link COCancelTransitionService}.
 * </p>
 * 
 * @author Pascal Lovy, 27-oct-2004
 * @see COExecuteTransitionService
 * @see COCancelTransitionService
 */
public abstract class COTransitionAction {
    /** Constante du mode automatique. */
    public static final String MODE_AUTO = "MODE_AUTO";

    /** Clé pour stocker le mode courant. */
    public static final String MODE_EXECUTION_KEY = "MODE_EXECUTION_KEY";

    /** Constante du mode manuel. */
    public static final String MODE_MANUEL = "MODE_MANUEL";

    private boolean annulerEcritures = true;
    private JACalendar calendar = new JACalendarGregorian();
    private String dateExecution = "";
    protected Map<String, String> etapeInfos;
    protected Map<String, String> etapeInfosParLibelle;
    private List frais;
    protected boolean ignoreCarMontantMinimal;

    private List<CAInteretManuelVisualComponent> interetCalcule;
    private String motif = "";

    private BProcess parent;

    private List taxes;
    private COTransition transition;
    private COTiersService tiersService = COServiceLocator.getTiersService();

    /**
     * Méthode abstraite à implémemter dans les classes enfants afin d'exécute les actions spécifiques à la transition.
     * 
     * @param contentieux Le contentieux sur lequel s'effectue la transition
     * @param taxes
     * @param transaction La transaction à utiliser
     * @throws COTransitionException En cas de problème pendant la transition
     */
    protected abstract void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException;

    /**
     * Permet d'effectuer un traitement systématiquement avant et après l’appel de la méthode.
     * {@link #_validate(COContentieux, BTransaction)}
     * 
     * @param contentieux
     * @param transaction
     * @param checkMotifBlocageCtx ne tient pas compte du motif de blocage.
     * @throws COTransitionException
     */
    private void _processValidate(COContentieux contentieux, BTransaction transaction, boolean checkMotifBlocageCtx)
            throws COTransitionException {
        // vérifier ici si suspendu
        boolean suspendu = false;

        if (checkMotifBlocageCtx) {
            try {
                suspendu = contentieux.suspendreContentieuxEnFonctionDuMotif(
                        JadeStringUtil.isEmpty(dateExecution) ? JACalendar.todayJJsMMsAAAA() : dateExecution,
                        getTransition().getEtapeSuivante().getLibEtape());
            } catch (Exception e) {
                throw new COTransitionException("AQUILA_ERREUR_CA_SUSPENDU", COActionUtils.getMessage(contentieux
                        .getSession(), "AQUILA_ERREUR_CA_SUSPENDU", new String[] { contentieux.getSection()
                        .getIdExterne() }));
            }

            if (suspendu) {
                throw new COTransitionException("AQUILA_ERR_CONTENTIEUX_SUSPENDU", COActionUtils.getMessage(contentieux
                        .getSession(), "AQUILA_ERR_CONTENTIEUX_SUSPENDU", new String[] { contentieux.getSection()
                        .getIdExterne() }));
            }
        }

        // la date d'exécution doit être ultérieure à celle de la dernière étape
        boolean executionAnterieure = false;

        try {
            COHistorique historique = contentieux.loadHistoriqueIgnorerDateExecution();

            if (historique != null) {
                // INFOROM 448 - Evaluer la date du RP avec le CDP (en ignorant les étapes des rappels)
                if (isEtapeSuivanteCDP()) {
                    // La date anterieure se calcul différemment avec le CDP (ne pas prendre en compte les rappels CDP)
                    executionAnterieure = isCDPAnterieureRP(contentieux);
                } else {
                    // INFOROM 448 - Ne pas évaluer les dates quand la dernière étape est le PV de saisie saisi
                    if (!isDerniereEtapePVSaisieSaisi(historique)) {
                        executionAnterieure = calendar.compare(getDateExecution(), historique.getDateExecution()) == JACalendar.COMPARE_FIRSTLOWER;
                    }
                }
            }
        } catch (Exception e) {
            throw new COTransitionException("AQUILA_ERREUR", COActionUtils.getMessage(contentieux.getSession(),
                    "AQUILA_ERREUR", new Object[] { this.getClass(), e.toString() }));
        }

        if (executionAnterieure) {
            if (isEtapeSuivanteCDP()) {
                throw new COTransitionException("AQUILA_JAVA_CDP_DATE_EXEC_ANTERIEURE_DATE_RP",
                        COActionUtils.getMessage(contentieux.getSession(),
                                "AQUILA_JAVA_CDP_DATE_EXEC_ANTERIEURE_DATE_RP"));
            } else {
                throw new COTransitionException("AQUILA_DATE_EXEC_ANTERIEURE_HIST", COActionUtils.getMessage(
                        contentieux.getSession(), "AQUILA_DATE_EXEC_ANTERIEURE_HIST"));
            }
        }

        _validate(contentieux, transaction);
    }

    /**
     * Méthode abstraite à implémemter dans les classes enfants afin de valider les pré conditions. Le system considère
     * que si cette méthode lève une exception, la transition n'est pas possible.
     * 
     * @param contentieux Le contentieux sur lequel s'effectue la transition
     * @param transaction La transaction à utiliser
     * @throws COTransitionException En cas de problème pendant la transition
     */
    protected abstract void _validate(COContentieux contentieux, BTransaction transaction) throws COTransitionException;

    /**
     * Valide que l'échéance soit dépassée.
     * <p>
     * Cette méthode n'est pas appellée systématiquement mais doit l'être dans la méthode
     * {@link #_validate(COContentieux, BTransaction)} des actions qui le souhaitent.
     * </p>
     * 
     * @param contentieux Le contentieux
     * @throws COTransitionException Si l'échéance n'est pas dépassée ou si le format de date est invalide
     */
    protected void _validerEcheance(COContentieux contentieux) throws COTransitionException {
        try {
            // Valide l'échéance au besoin
            COApplication application = (COApplication) GlobazSystem
                    .getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA);

            if (application.validerEcheance() && !contentieux.echeanceDepassee()) {
                throw new COTransitionException("AQUILA_ERR_ECHEANCE_PAS_DEPASSEE", COActionUtils.getMessage(
                        contentieux.getSession(), "AQUILA_ERR_ECHEANCE_PAS_DEPASSEE"));
            }
        } catch (Exception e) {
            if (e instanceof COTransitionException) {
                throw (COTransitionException) e;
            } else {
                throw new COTransitionException(e);
            }
        }
    }

    /**
     * Valide que le solde soit supérieur à zéro.
     * <p>
     * Cette méthode n'est pas appellée systématiquement mais doit l'être dans la méthode
     * {@link #_validate(COContentieux, BTransaction)} des actions qui le souhaitent.
     * </p>
     * 
     * @param contentieux Le contentieux
     * @throws COTransitionException Si le solde n'est pas supérieur à zéro
     */
    protected void _validerSolde(COContentieux contentieux) throws COTransitionException {
        FWCurrency solde = new FWCurrency(contentieux.getSolde());

        if (((contentieux.getSection() != null) && !APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(contentieux
                .getSection().getIdTypeSection()))) {
            if (solde.compareTo(new FWCurrency(0)) <= 0) {
                throw new COTransitionException("AQUILA_ERR_SOLDE_INF_ZERO", COActionUtils.getMessage(
                        contentieux.getSession(), "AQUILA_ERR_SOLDE_INF_ZERO"));
            }

            if (solde.compareTo(new FWCurrency(getTransition().getEtapeSuivante().getMontantMinimal())) < 0) {
                // mettre à jour le motif de l'historique
                throw new COTransitionException("SEUIL_MINIMAL_INFERIEUR", COActionUtils.getMessage(
                        contentieux.getSession(), "SEUIL_MINIMAL_INFERIEUR"));
            }
        }
    }

    protected void _validerCasPourEnvoyerPoursuite(COContentieux contentieux) throws COTransitionException {
        CASection section = contentieux.getSection();

        FWCurrency base = new FWCurrency(section.getBase());
        FWCurrency paiementCompensation = new FWCurrency(section.getPmtCmp());
        FWCurrency solde = new FWCurrency(section.getSolde());
        FWCurrency montantNette = new FWCurrency("0");

        // Si le solde est positif
        if (solde.isPositive() == true) {
            // Base + paiement/compensation = montant nette
            montantNette.add(base);
            montantNette.add(paiementCompensation);

            // Si base <> 0 et que la base a été payé par les paiements mais qu'il reste des taxes à payer
            if (base.isZero() == false && isZeroOrNegative(montantNette) == true) {
                // Si la caisse ne désire en aucun cas mettre les taxes aux poursuite avec un montant de créance payé.
                if (CORequisitionPoursuiteUtil.wantPutOnlyTaxesPoursuite(contentieux.getSession()) == false) {
                    throw new COTransitionException("RDP_TAXESRESTANTES_NON_ACCEPTER", COActionUtils.getMessage(
                            contentieux.getSession(), "RDP_TAXESRESTANTES_NON_ACCEPTER"));
                }
            }
        }
    }

    private boolean isZeroOrNegative(FWCurrency montant) {
        return montant.isZero() || montant.isNegative();
    }

    /**
     * Recherche ou/et calcul des IM
     * 
     * @param session
     * @param contentieux
     * @param transition
     * @return la liste des interets manuel ou null
     * @throws Exception
     */
    public List<CAInteretManuelVisualComponent> giveDecisionIM(BTransaction transaction, COContentieux contentieux,
            String date) throws Exception {

        if (contentieux == null) {
            return null;
        }

        // POAVS-223
        if (!isNouveauRegime(transaction.getSession(), date)) {
            return null;
        }

        List<CAInteretManuelVisualComponent> liste = new ArrayList<CAInteretManuelVisualComponent>();
        CAInteretMoratoireManager managerIM = findIMExistant(transaction.getSession(), contentieux);

        if (managerIM.isEmpty()) {
            // Simuler IM
            CAProcessInteretMoratoireManuel process = new CAProcessInteretMoratoireManuel();
            process.setSession(transaction.getSession());
            process.setDateFin(date);
            process.setIdSection(contentieux.getIdSection());
            process.setSimulationMode(true);
            process.setTransaction(transaction);
            process.setIsRDPProcess(true);
            process.executeProcess();

            liste = process.getVisualComponents();
        }

        return liste;
    }

    public Boolean isNouveauRegime(BSession session, String dateExecution) {
        try {
            String dateProduction = session.getApplication().getProperty("dateProductionNouveauCDP");
            if (dateProduction == null) {
                return false;
            }

            // retourne true si la date d'execution de la RP est supérieure ou égale à la date de production du nouveau
            // regime
            return BSessionUtil.compareDateFirstGreaterOrEqual(session, dateExecution, dateProduction);
        } catch (Exception e) {
            JadeLogger.error(e, "La propriété n'existe pas.");
            return false;
        }
    }

    /**
     * @param session
     * @param contentieux
     * @return
     */
    private CAInteretMoratoireManager findIMExistant(BSession session, COContentieux contentieux) {
        CAInteretMoratoireManager managerIM = new CAInteretMoratoireManager();
        managerIM.setSession(session);
        managerIM.setForIdCompteAnnexe(contentieux.getIdCompteAnnexe());
        managerIM.setForIdSection(contentieux.getIdSection());
        managerIM.setForIdJournalFacturation("0");
        try {
            managerIM.find();
        } catch (Exception e) {
            // nope
        }
        return managerIM;
    }

    /**
     * ajoute une valeur pour une information par étape pour cette transition.
     * 
     * @param etapeInfoConfig
     * @param valeur
     */
    public void addEtapeInfo(COEtapeInfoConfig etapeInfoConfig, String valeur) {
        // indexer par identifiant
        if (etapeInfos == null) {
            etapeInfos = new HashMap<String, String>();
        }

        etapeInfos.put(etapeInfoConfig.getIdEtapeInfoConfig(), valeur);

        /*
         * indexer par code système libellé Note: les libellés ne sont pas forcément uniques pour les infos ajoutées par
         * l'utilisateur mais celles qui sont utilisées par le système le sont forcément, et ce sont celles qui nous
         * intéressent
         */
        if (etapeInfosParLibelle == null) {
            etapeInfosParLibelle = new HashMap<String, String>();
        }

        etapeInfosParLibelle.put(etapeInfoConfig.getCsLibelle(), valeur);
    }

    /**
     * Annule les modifications qui avaient ete apportées a ce contentieux par cette action de transition lorsqu'elle
     * avait ete appellée.
     * <p>
     * Cette méthode ne doit pas être appellée directement, le service d'annulation {@link COCancelTransitionService}
     * s'en charge.
     * </p>
     * 
     * @param contentieux le contentieux qu'il faut retablir.
     * @param historiqueAnnule
     * @param historiqueRetabli
     * @param transaction -
     * @throws COTransitionException
     */
    public final void annuler(COContentieux contentieux, COHistorique historiqueAnnule, COHistorique historiqueRetabli,
            BTransaction transaction) throws COTransitionException {
        // remet le contentieux dans son état antérieur
        contentieux.setIdEtape(historiqueRetabli.getIdEtape());
        contentieux.setProchaineDateDeclenchement(contentieux.getDateDeclenchement());
        contentieux.setDateDeclenchement(historiqueRetabli.getDateDeclenchement());
        contentieux.setDateExecution(historiqueRetabli.getDateExecution());

        // annule les modifications du contentieux apportées par cette
        // transition
        // _annuler(contentieux, historiqueRetabli, transaction);
    }

    /**
     * Détermine si la transition peut être effectuée. (si la méthode _validate ne lève pas d'exeption)
     * 
     * @param contentieux Le contentieux sur lequel s'effectue la transition
     * @param transaction La transaction à utiliser
     * @throws COTransitionException
     */
    public void canExecute(COContentieux contentieux, BTransaction transaction) throws COTransitionException {
        this.canExecute(contentieux, transaction, true);
    }

    /**
     * Détermine si la transition peut être effectuée. (si la méthode _validate ne lève pas d'exeption)
     * 
     * @param contentieux Le contentieux sur lequel s'effectue la transition
     * @param transaction La transaction à utiliser
     * @param checkMotifBlocageCtx ne tient pas compte du motif de blocage, si provient de l'action annulé un plan de
     *            paiement.
     * @throws COTransitionException
     */
    public void canExecute(COContentieux contentieux, BTransaction transaction, boolean checkMotifBlocageCtx)
            throws COTransitionException {
        // Détermine le mode d'exécution courant
        String modeExecution = (String) transaction.getSession().getAttribute(COTransitionAction.MODE_EXECUTION_KEY);

        if (!COTransitionAction.MODE_AUTO.equals(modeExecution)) {
            modeExecution = COTransitionAction.MODE_MANUEL;
        }

        // Contrôle que le mode d'exécution soit autorisé
        if (getTransition() == null) {
            throw new COTransitionException("AQUILA_TRANSITION_NULLE", COActionUtils.getMessage(
                    contentieux.getSession(), "AQUILA_TRANSITION_NULLE"));
        }

        if ((COTransitionAction.MODE_AUTO.equals(modeExecution) && !getTransition().isAuto())
                || (COTransitionAction.MODE_MANUEL.equals(modeExecution) && !getTransition().isManuel())) {
            throw new COTransitionException("AQUILA_TRANSITION_MODE_INTERDIT", COActionUtils.getMessage(
                    contentieux.getSession(), "AQUILA_TRANSITION_MODE_INTERDIT"));
        }

        // Valide les pré conditions
        _processValidate(contentieux, transaction, checkMotifBlocageCtx);
    }

    /**
     * effectue la transition c'est-a-dire modifie le contentieux pour refleter le passage a l'etape suivante.
     * 
     * @param contentieux
     * @param transition
     * @throws COTransitionException
     */
    protected void effectuerTransition(COContentieux contentieux, COTransition transition) throws COTransitionException {
        contentieux.setIdEtape(getTransition().getIdEtapeSuivante());
        contentieux.setDateDeclenchement(contentieux.getProchaineDateDeclenchement());
        contentieux.setDateExecution(getDateExecution());
        contentieux.setProchaineDateDeclenchement(transition.calculerDateProchainDeclenchement(contentieux));
    }

    /**
     * Valide les propriétés et exécute les actions spécifiques à la transition.
     * <p>
     * Cette méthode ne doit pas être appellée directement, le service d'exécution {@link COExecuteTransitionService}
     * s'en charge.
     * </p>
     * 
     * @param contentieux Le contentieux sur lequel s'effectue la transition
     * @param transaction La transaction à utiliser
     * @throws COTransitionException En cas de problème pendant la transition
     */
    public final void execute(COContentieux contentieux, BTransaction transaction) throws COTransitionException {
        // Exécute l'action (envoie les documents, etc.)
        _execute(contentieux, taxes, transaction);

        // Post-traitement de mise à jour du contentieux
        effectuerTransition(contentieux, transition);
    }

    /**
     * retourne la date d'exécution de l'action.
     * 
     * @return La valeur courante de la propriété
     */
    public String getDateExecution() {
        if (JadeStringUtil.isEmpty(dateExecution)) {
            dateExecution = JACalendar.todayJJsMMsAAAA();
        }

        return dateExecution;
    }

    /**
     * Retourne une map {@link String clé étape info config} -> {@link String valeur} ou null.
     * 
     * @return DOCUMENT ME!
     */
    public Map<String, String> getEtapeInfos() {
        return etapeInfos;
    }

    /**
     * @return the frais
     */
    public List getFrais() {
        return frais;
    }

    /**
     * @return the interetCalcule
     */
    public List<CAInteretManuelVisualComponent> getInteretCalcule() {
        return interetCalcule;
    }

    /**
     * Retourne le motif (la remarque) de la transition.
     * 
     * @return La valeur courante de la propriété
     */
    public String getMotif() {
        return motif;
    }

    /**
     * Retourne le processus parent s'il existe.
     * 
     * @return DOCUMENT ME!
     */
    public BProcess getParent() {
        return parent;
    }

    /**
     * Retourne la liste des taxes pour l'étape suivante.
     * 
     * @return DOCUMENT ME!
     */
    public List getTaxes() {
        return taxes;
    }

    /**
     * Retourne la transition qui a servi a retrouver cette action, cette propriété a été renseignée lors de la création
     * de l'instance de transition.
     * 
     * @return la transition
     * @see COTransitionActionService#getTransitionAction(COTransition)
     */
    public COTransition getTransition() {
        return transition;
    }

    /**
     * Retourne vrai s'il faut annuler les écritures dans le cas d'une annulation de la transition.
     * 
     * @return DOCUMENT ME!
     */
    public boolean isAnnulerEcritures() {
        return annulerEcritures;
    }

    /**
     * Evalue la date entre le RP et le CDP
     * 
     * @param contentieux Le contentieux actuel
     * @return Vrai si la date d'exécution du CDP est anterieure à la date du RP
     */
    private Boolean isCDPAnterieureRP(COContentieux contentieux) {
        COHistoriqueService historiqueService = new COHistoriqueService();
        COHistorique historiqueRP;

        if (isEtapeSuivanteCDP()) {
            try {
                historiqueRP = historiqueService.getHistoriqueForLibEtape(getTransition().getSession(), contentieux,
                        ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE);

                if (historiqueRP != null) {
                    return calendar.compare(getDateExecution(), historiqueRP.getDateExecution()) == JACalendar.COMPARE_FIRSTLOWER;
                }
            } catch (Exception e) {
                JadeLogger.warn(e, e.getMessage());
            }
        }
        return false;
    }

    /**
     * Regarde si la dernière étape a été le PV de SAISIE saisi
     * 
     * @param historique L'historique du contentieux
     * @return Vrai si la dernière étape est la PV de SAISIE saisi
     */
    private Boolean isDerniereEtapePVSaisieSaisi(COHistorique historique) {
        return historique.getEtape().getLibEtape().equals(ICOEtape.CS_PV_DE_SAISIE_SAISI);
    }

    public Boolean isEtapeSuivanteCDP() {

        if (transition.getEtapeSuivante().getLibEtape().equals(ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_AVEC_OPPOSITION)) {
            return true;
        }
        if (transition.getEtapeSuivante().getLibEtape().equals(ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_SANS_OPPOSITION)) {
            return true;
        }
        return false;
    }

    public String giveCantonDestinataire(BSession session, COContentieux contentieux) {
        try {
            // destinataire est l'OP
            IntTiers destinataireDocument = tiersService.getOfficePoursuite(session, contentieux.getCompteAnnexe()
                    .getTiers(), contentieux.getCompteAnnexe().getIdExterneRole());

            if (destinataireDocument == null) {
                JadeLogger.error(FWMessage.AVERTISSEMENT, session.getLabel("AQUILA_ERR_OP_INTROUVABLE"));
            } else {
                TIAdresseDataSource adresseOP = getAdresseDataSourcePrincipal(destinataireDocument,
                        destinataireDocument.getLangueISO(), session, contentieux);
                return adresseOP.canton_court;
            }
        } catch (Exception e) {
            JadeLogger.error(FWMessage.AVERTISSEMENT, session.getLabel("AQUILA_ERR_OP_INTROUVABLE") + e.getMessage());
        }
        return null;
    }

    protected TIAdresseDataSource getAdresseDataSourcePrincipal(IntTiers tiers, String langue, BSession session,
            COContentieux contentieux) throws Exception {
        if (langue == null) {
            langue = session.getIdLangueISO();
        }

        TIAdresseDataSource result = getAdresseCourrierData(tiers, langue, session, contentieux);

        if (result != null) {
            return result;
        } else {
            try {
                return getAdresseDomicileData(tiers, langue, session, contentieux);
            } catch (Exception e) {
                JadeLogger.error(e, "Impossible de trouver l'adresse de l'assuré: " + e.getMessage());
                return new TIAdresseDataSource();
            }
        }
    }

    private TIAdresseDataSource getAdresseCourrierData(IntTiers tiers, String langue, BSession session,
            COContentieux contentieux) throws Exception {
        // Récupérer le tiers
        TITiers pyTiers = tiersService.loadTiers(session, tiers);
        if (tiers == null) {
            return null;
        } else {
            return pyTiers.getAdresseAsDataSource(getTypeAdresseCourrier(contentieux), getDomaineDefaut(contentieux),
                    contentieux.getCompteAnnexe().getIdExterneRole(), getDateExecution(), true, langue);
        }
    }

    private TIAdresseDataSource getAdresseDomicileData(IntTiers tiers, String langue, BSession session,
            COContentieux contentieux) throws Exception {
        // Récupérer le tiers
        TITiers pyTiers = tiersService.loadTiers(session, tiers);
        if (tiers == null) {
            return null;
        } else {
            return pyTiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, contentieux.getCompteAnnexe().getIdExterneRole(),
                    getDateExecution(), true, langue);
        }
    }

    private String getTypeAdresseCourrier(COContentieux contentieux) {
        String type = "";

        if (!JadeStringUtil.isIntegerEmpty(contentieux.getSection().getTypeAdresse())) {
            type = contentieux.getSection().getTypeAdresse();
        } else {
            type = IConstantes.CS_AVOIR_ADRESSE_COURRIER;
        }
        return type;
    }

    private String getDomaineDefaut(COContentieux contentieux) {
        String domaine;

        if (!JadeStringUtil.isIntegerEmpty(contentieux.getSection().getDomaine())) {
            domaine = contentieux.getSection().getDomaine();
        } else {
            domaine = contentieux.getCompteAnnexe()._getDefaultDomainFromRole();
        }
        return domaine;
    }

    /**
     * retourne vrai si l'action est ignorée car le montant minimal d'exécution de l'étape n'est pas atteint.
     * 
     * @return DOCUMENT ME!
     */
    public boolean isIgnoreCarMontantMinimal() {
        return ignoreCarMontantMinimal;
    }

    /**
     * @see #isAnnulerEcritures()
     */
    public void setAnnulerEcritures(boolean b) {
        annulerEcritures = b;
    }

    /**
     * Méthode non utilisée. methode a redefinir si les implementations d'actions effectuent des modifications sur le
     * contentieux qui doivent etre annules.
     * 
     * @param contentieux
     * @param historique
     * @param transaction
     * @throws COTransitionException
     */
    // @Deprecated
    // protected void _annuler(COContentieux contentieux, COHistorique
    // historique, BTransaction transaction) throws COTransitionException {
    // /*RIEN*/ };

    /**
     * @see #getDateExecution()
     */
    public void setDateExecution(String string) {
        dateExecution = string;
    }

    /**
     * @param frais the frais to set
     */
    public void setFrais(List frais) {
        this.frais = frais;
    }

    /**
     * @see #isIgnoreCarMontantMinimal()
     */
    public void setIgnoreCarMontantMinimal(boolean ignoreCarMontantMinimal) {
        this.ignoreCarMontantMinimal = ignoreCarMontantMinimal;
    }

    /**
     * @param interetCalcule the interetCalcule to set
     */
    public void setInteretCalcule(List<CAInteretManuelVisualComponent> interetCalcule) {
        this.interetCalcule = interetCalcule;
    }

    /**
     * @see #getMotif()
     */
    public void setMotif(String string) {
        motif = string;
    }

    /**
     * @see #getParent()
     */
    public void setParent(BProcess process) {
        parent = process;
    }

    /**
     * @see #getTaxes()
     */
    public void setTaxes(List list) {
        taxes = list;
    }

    /**
     * @see #getTransition()
     */
    public void setTransition(COTransition value) {
        transition = value;
    }
}
