package globaz.phenix.documentsItext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.CTDocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.ServicesFacturation;
import globaz.musca.util.FAUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.naos.util.AFUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPCotisationManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAgenceCommunale;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.db.principale.CPRemarqueDecision;
import globaz.phenix.db.principale.CPRemarqueDecisionManager;
import globaz.phenix.db.principale.CPRemarqueType;
import globaz.phenix.listes.itext.CPIListeDecisionParam;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.Constante;
import globaz.phenix.util.DocumentInfoPhenix;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TIRoleManager;
import globaz.pyxis.db.tiers.TITiers;
import java.util.Iterator;
import java.util.Vector;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Insérez la description du type ici. Date de création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class CPIDecision_Doc extends FWIDocumentManager implements Constante {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int CS_ALLEMAND = 503002;

    // initialisation pour CS langue tiers (TITIERS)
    public static final int CS_FRANCAIS = 503001;

    public static final int CS_ITALIEN = 503004;

    private boolean acompteDetailCalcul = false;

    // Acompte indépendant
    public ICTDocument acompteInd = null;

    // Acompte pour non acitf
    public ICTDocument acompteNac = null;

    public ICTDocument[] acomptesInd = null;

    public ICTDocument[] acomptesNac = null;

    private Boolean affichageEcran = Boolean.FALSE;

    private globaz.naos.db.affiliation.AFAffiliation affiliation = null;
    private boolean bloquerEnvoi = false;
    private String codeVersoAcoInd = "";
    // Variable pour stocker les différents versos utilisés
    private String codeVersoAcoNac = "";
    private String codeVersoGen = "";
    private String codeVersoGenAco = "";
    private String codeVersoGenDef = "";
    private String codeVersoGenInd = "";
    private String codeVersoGenNac = "";

    private String codeVersoGenOpp = "";
    private String codeVersoGenPro = "";
    private String codeVersoOppInd = "";
    private String codeVersoOppNac = "";
    private globaz.osiris.db.comptes.CACompteAnnexe compteAnnexe;
    private java.lang.String dateImpression = "";
    public CPDecisionAgenceCommunale decision;
    public CPDecisionAgenceCommunale decisionAdresse;
    // Babel poour décision générique
    public ICTDocument decisionGenerique = null;
    // Babel pour décision non actif
    public ICTDocument decisionInd = null;
    // Babel pour décision non actif
    public ICTDocument decisionNonActif = null;
    private String decisionRetroactiveAvecMontantFacture = "";
    public ICTDocument[] decisionsGeneriqueDE = null;
    public ICTDocument[] decisionsGeneriqueFR = null;
    public ICTDocument[] decisionsGeneriqueIT = null;
    public ICTDocument[] decisionsGeneriqueTemp = null;
    public ICTDocument[] decisionsInd = null;
    public ICTDocument[] decisionsNonActifDE = null;
    public ICTDocument[] decisionsNonActifFR = null;
    public ICTDocument[] decisionsNonActifIT = null;
    public ICTDocument[] decisionsNonActifTemp = null;
    public ICTDocument[] decisionsTableauCotiDE = null;
    public ICTDocument[] decisionsTableauCotiFR = null;
    public ICTDocument[] decisionsTableauCotiIT = null;
    public ICTDocument[] decisionsTableauCotiTemp = null;
    // Babel pour tableau de cotisation
    public ICTDocument decisionTableauCoti = null;
    private CPDonneesBase donneeBase = null;
    private CPDonneesCalcul donneeCalcul = null;
    private Boolean duplicata = Boolean.FALSE;
    private Boolean envoiGed = null;
    private boolean impressionLettreCouple = false;
    public String langueDoc = "";
    protected String libFranc = "";
    private Iterator m_container;
    private Iterator m_containerAdresse;
    private int nbDoc = 0;
    public BProcess processAppelant = null;
    public ICTDocument resAcompteInd[] = null;
    public ICTDocument resAcompteNac[] = null;
    public ICTDocument resGenerique[] = null;
    public ICTDocument resInd[] = null;
    public ICTDocument resNonActif[] = null;
    public ICTDocument resTableauCoti[] = null;
    public int typeDocument = 0;
    private JadeUser user = null;
    private boolean wantLettreCouple = true;

    private boolean isEbusiness = false;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public CPIDecision_Doc() throws Exception {
        this(new BSession(globaz.phenix.application.CPApplication.DEFAULT_APPLICATION_PHENIX));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 17:00:08)
     * 
     * @param parent
     *            BProcess
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public CPIDecision_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, globaz.phenix.application.CPApplication.APPLICATION_PHENIX_REP, "Decision");
        super.setFileTitle(getSession().getLabel("CP_DECISION"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public CPIDecision_Doc(BSession session) throws java.lang.Exception {
        super(session, globaz.phenix.application.CPApplication.APPLICATION_PHENIX_REP, "Decision");
        super.setFileTitle(getSession().getLabel("CP_DECISION"));
    }

    /**
     * Commentaire relatif à la méthode _columnHeader.
     */
    protected void _columnHeader() {
        try {
            // Période
            String periodeDecision = getDecision().getDebutDecision() + " - " + getDecision().getFinDecision();
            // Décision provisoire.... 01.01.2007 - 31.12.2007
            // Annuler le if suivant à cause de client qui encodent des acomptes
            // périodiques - PO 4242
            // if (Integer.parseInt(getDecision().getAnneeDecision()) >=
            // JACalendar.getYear(JACalendar.todayJJsMMsAAAA())) {
            // Par trimestre, mois etc...
            super.setParametres(CPIListeDecisionParam.PARAM_PARPERIODE, _getParPeriode());
            // }
            // Cotisation dues 01.01.2007 - 31.12.2007
            super.setParametres(CPIListeDecisionParam.LABEL_COTISATION_DUE,
                    this.getTexteDocument(decisionsTableauCotiTemp, decisionTableauCoti, 1, 1, periodeDecision)
                            .toString());
            // Montant déjà facturé
            super.setParametres(CPIListeDecisionParam.LABEL_MONTANT,
                    this.getTexteDocument(decisionsTableauCotiTemp, decisionTableauCoti, 1, 7, " ").toString());
            // Pour la période
            super.setParametres(CPIListeDecisionParam.LABEL_PERIODE,
                    this.getTexteDocument(decisionsTableauCotiTemp, decisionTableauCoti, 1, 6, " ").toString());
            // Total
            super.setParametres(CPIListeDecisionParam.LABEL_TOTAL,
                    this.getTexteDocument(decisionsTableauCotiTemp, decisionTableauCoti, 3, 1, " ").toString());
            // Différence
            super.setParametres(CPIListeDecisionParam.LABEL_DIFFERENCE,
                    this.getTexteDocument(decisionsTableauCotiTemp, decisionTableauCoti, 3, 2, " ").toString());
            // Libellés CHF (4) soit CHF pour colonne 1, pour colonne2 pour
            // total colonne 1 et total colonne 2
            super.setParametres("L_FRANC_COL1",
                    this.getTexteDocument(decisionsTableauCotiTemp, decisionTableauCoti, 2, 2, libFranc).toString());
            super.setParametres("L_FRANC_COL2",
                    this.getTexteDocument(decisionsTableauCotiTemp, decisionTableauCoti, 2, 4, libFranc).toString());
            super.setParametres("L_FRANC_TOT1",
                    this.getTexteDocument(decisionsTableauCotiTemp, decisionTableauCoti, 3, 3, libFranc).toString());
            super.setParametres("L_FRANC_TOT2",
                    this.getTexteDocument(decisionsTableauCotiTemp, decisionTableauCoti, 3, 5, libFranc).toString());
            super.setParametres("L_FRANC_DIFFERENCE",
                    this.getTexteDocument(decisionsTableauCotiTemp, decisionTableauCoti, 3, 7, libFranc).toString());
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    getSession().getErrors().toString() + " - " + affiliation.getAffilieNumero() + " - "
                            + getDecision().getAnneeDecision(), FWMessage.ERREUR, "");
        }
    }

    /**
     * Recherche l'adresse du tiers suivant la date de début de décision. <br>
     * Si cette dernière est plus gande que la date du jour, rechercher l'adresse du tiers <br>
     * avec la date de décision<br>
     * Date de création : (02.05.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String _getAdresse() {
        String adresse = "";
        try {
            if (decision != null) {
                TIAdresseDataSource dataSource = new TIAdresseDataSource();
                dataSource.setSession(getSession());
                dataSource.load(decisionAdresse, JACalendar.todayJJsMMsAAAA());
                ITIAdresseFormater formater = new TIAdresseFormater();
                adresse = formater.format(dataSource);
            }
            return adresse;
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return "";
        }
    }

    /**
     * Retourne l'adresse de la caisse dans la langue du tiers Date de création : (02.05.2003 11:59:50)
     * 
     * @parm java.lang.String dateImpression
     * @return java.lang.String
     */
    public java.lang.String _getAdresseCaisse() {
        try {
            return getSession().getApplication().getProperty("ADRESSE_CAISSE_COURRIER_" + langueDoc);
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return "";
        }
    }

    /**
     * Retourne les commentaire d'une décision Date de création : (02.05.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String _getCommentaire() {
        String commentaire = "";
        try {
            if (decision != null) {
                CPRemarqueDecisionManager manager = new CPRemarqueDecisionManager();
                manager.setSession(getSession());
                manager.setForIdDecision(decision.getIdDecision());
                manager.setForEmplacement(CPRemarqueType.CS_COMMENTAIRE);
                manager.setOrderBy("INIREM ASC");
                // trier par liste remarque croissante
                manager.find();
                for (int i = 0; i < manager.size(); i++) { // si la dernière
                    // décision n'existe
                    // pas, ne pas
                    // reprendre le
                    // commentaire de
                    // substitution
                    if (JadeStringUtil.isBlank(commentaire)) {
                        commentaire = ((CPRemarqueDecision) manager.getEntity(i)).getTexteRemarqueDecision();
                    } else {
                        commentaire = commentaire + " "
                                + ((CPRemarqueDecision) manager.getEntity(i)).getTexteRemarqueDecision();
                    }
                }
            } else {
                return "";
            }
            return commentaire;
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return "";
        }
    }

    /**
     * Retourne la date de début du revenu Date de création : (02.05.2003 11:59:50)
     * 
     * @parm java.lang.String dateImpression
     * @return java.lang.String
     */
    public java.lang.String _getDebutRevenu(CPPeriodeFiscale perfis) {
        try {
            String typeRevenu = ((CPApplication) getSession().getApplication()).getTypeRevenu();
            if (!perfis.isNew()) {
                int anneeDec = Integer.parseInt(decision.getAnneeDecision());
                int anneeRev1 = Integer.parseInt(perfis.getAnneeRevenuDebut());
                int anneeRev2 = Integer.parseInt(perfis.getAnneeRevenuFin());
                if ("2".equalsIgnoreCase(decision.getAnneePrise())) {
                    if ("Mensuel".equalsIgnoreCase(typeRevenu)) {
                        if (anneeDec == anneeRev2) {
                            return decision.getDebutDecision();
                        }
                    }
                    if (BSessionUtil.compareDateFirstLower(getSession(), decision.getDebutDecision(),
                            perfis.getDebutRevenu2())) {
                        return decision.getDebutDecision();
                    } else {
                        return perfis.getDebutRevenu2();
                    }
                } else {
                    if ("Mensuel".equalsIgnoreCase(typeRevenu)) {
                        if (anneeDec == anneeRev1) {
                            return decision.getDebutDecision();
                        }
                    }
                    if (BSessionUtil.compareDateFirstLower(getSession(), decision.getDebutDecision(),
                            perfis.getDebutRevenu1())) {
                        return decision.getDebutDecision();
                    } else {
                        return perfis.getDebutRevenu1();
                    }
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return "";
        }
    }

    /**
     * Retourne l'email du responable Date de création : (02.05.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String _getEmailResponsable() {
        try {
            if (decision != null) {
                if (getUser() == null) {
                    JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
                    JadeUser user = service.load(decision.getResponsable());
                    setUser(user);
                }
                // si le user n'est pas vide, rapatrier les données de détails
                // du user
                if (user != null) {
                    return user.getEmail();
                }

            }
            return "";
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return "";
        }
    }

    /**
     * Retourne la date de fin du revenu Date de création : (02.05.2003 11:59:50)
     * 
     * @parm java.lang.String dateImpression
     * @return java.lang.String
     */
    public java.lang.String _getFinRevenu(CPPeriodeFiscale perfis) {
        try {
            String typeRevenu = ((CPApplication) getSession().getApplication()).getTypeRevenu();
            if (!perfis.isNew()) {
                int anneeDec = Integer.parseInt(decision.getAnneeDecision());
                int anneeRev1 = Integer.parseInt(perfis.getAnneeRevenuDebut());
                int anneeRev2 = Integer.parseInt(perfis.getAnneeRevenuFin());
                if ("1".equalsIgnoreCase(decision.getAnneePrise())) {
                    if ("Mensuel".equalsIgnoreCase(typeRevenu)) {
                        if (anneeDec == anneeRev1) {
                            return decision.getFinDecision();
                        }
                    }
                    if (BSessionUtil.compareDateFirstLower(getSession(), decision.getFinDecision(),
                            perfis.getFinRevenu1())) {
                        return decision.getFinDecision();
                    } else {
                        return perfis.getFinRevenu1();
                    }
                } else {
                    if ("Mensuel".equalsIgnoreCase(typeRevenu)) {
                        if (anneeDec == anneeRev2) {
                            return decision.getFinDecision();
                        }
                    }
                    if (BSessionUtil.compareDateFirstLower(getSession(), decision.getFinDecision(),
                            perfis.getFinRevenu2())) {
                        return decision.getFinDecision();
                    } else {
                        return perfis.getFinRevenu2();
                    }
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return "";
        }
    }

    /**
     * Returns the idLangue.
     * 
     * @return int
     */
    public int _getIdLangue() {
        return JadeStringUtil.toIntMIN(decision.getLangue());
    }

    /**
     * Si la date d'impression est vide => impression direct, on prend la date du prochain passage Si il n'y a pas de
     * passage, on prend la date du jour Sinon on prend la date d'impression qui est renseignée (via l'écran
     * d'impression des décisions d'un passage) Date de création : (02.05.2003 11:59:50)
     * 
     * @parm java.lang.String dateImpression
     * @return java.lang.String
     */
    public java.lang.String _getLieuDate(String dateImpression) {
        String date = "";
        try {
            if (JadeStringUtil.isBlank(dateImpression)) {
                if (getDuplicata().equals(Boolean.TRUE)) {
                    boolean affichageDateFacturation = ((CPApplication) GlobazSystem.getApplication("PHENIX"))
                            .isAffichageDateFacturation();
                    if (affichageDateFacturation) {
                        date = decision.getDateFacturation();
                    } else {
                        date = decision.getDateInformation();
                    }
                }
                if (JAUtil.isDateEmpty(date)) {
                    // Impression direct (vient automatiquement à l'écran lors
                    // de la saisie de la décision)
                    globaz.musca.api.IFAPassage passage = findNextPassageFacturation();
                    if (passage != null) {
                        date = passage.getDateFacturation();
                    }
                }
            } else {
                date = dateImpression;
            }
            if (JAUtil.isDateEmpty(date)) {
                date = JACalendar.todayJJsMMsAAAA();
            }
            return date = JACalendar.format(date, langueDoc);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Libelle de la périodicité Date de création : (02.05.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String _getParPeriode() {
        try {
            if (getAffiliation() != null) {
                CPCotisationManager cotiManager = new CPCotisationManager();
                cotiManager.setSession(getSession());
                cotiManager.setForIdDecision(decision.getIdDecision());
                cotiManager.setNotInGenreCotisation(CodeSystem.TYPE_ASS_CPS_GENERAL + ", "
                        + CodeSystem.TYPE_ASS_CPS_AUTRE);
                cotiManager.find(1);
                if (cotiManager.getSize() > 0) {
                    CPCotisation coti = (CPCotisation) cotiManager.getFirstEntity();
                    if (!coti.isNew()) {
                        AFCotisation afCoti = new AFCotisation();
                        afCoti.setCotisationId(coti.getIdCotiAffiliation());
                        afCoti.setSession(getSession());
                        afCoti.retrieve();
                        if (!afCoti.isNew()) {
                            if (afCoti.getPeriodicite().equalsIgnoreCase(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                                return this.getTexteDocument(decisionsTableauCotiTemp, decisionTableauCoti, 1, 3, " ")
                                        .toString();
                            } else if (afCoti.getPeriodicite().equalsIgnoreCase("802003")) {
                                return this.getTexteDocument(decisionsTableauCotiTemp, decisionTableauCoti, 1, 4, " ")
                                        .toString();
                            } else if (afCoti.getPeriodicite().equalsIgnoreCase(CodeSystem.PERIODICITE_ANNUELLE)) {
                                return this.getTexteDocument(decisionsTableauCotiTemp, decisionTableauCoti, 1, 5, " ")
                                        .toString();
                            } else {
                                return this.getTexteDocument(decisionsTableauCotiTemp, decisionTableauCoti, 1, 2, " ")
                                        .toString();
                            }
                        }
                    }
                }
            }
            return "";
        } catch (Exception e) {
            JadeLogger.info(this, e);
            return "";
        }
    }

    protected CPPeriodeFiscale _getPers() throws Exception {
        CPPeriodeFiscale perfis = new CPPeriodeFiscale();
        perfis.setSession(getSession());
        perfis.setIdIfd(decision.getIdIfdProvisoire());
        perfis.retrieve();
        return perfis;
    }

    /**
     * Retourne les commentaire d'une décision Date de création : (02.05.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String _getRemarque() {
        try {
            String remarque = "";
            if (decision != null) {
                CPRemarqueDecisionManager manager = new CPRemarqueDecisionManager();
                manager.setSession(getSession());
                manager.setForIdDecision(decision.getIdDecision());
                manager.setForEmplacement(CPRemarqueType.CS_REMARQUE);
                manager.find();
                for (int i = 0; i < manager.size(); i++) {
                    if (JadeStringUtil.isBlank(remarque)) {
                        remarque = ((CPRemarqueDecision) manager.getEntity(i)).getTexteRemarqueDecision();
                    } else {
                        remarque = remarque + " "
                                + ((CPRemarqueDecision) manager.getEntity(i)).getTexteRemarqueDecision();
                    }
                }
            }
            return remarque;
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return "";
        }
    }

    /**
     * Retourne l'alias du responable Date de création : (02.05.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String _getResponsable() {
        try {
            if (decision != null) {
                if ((getUser() == null) && !JadeStringUtil.isEmpty(decision.getResponsable())) {

                    JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
                    JadeUser user = service.load(decision.getResponsable());
                    setUser(user);
                }
                // si le user n'est pas vide, rapatrier les données de détails
                // du user
                if (user != null) {
                    return user.getFirstname() + " " + user.getLastname();
                }
            }
            return "";
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return "";
        }
    }

    /**
     * Source, selon la langue, de la base de calcul selon le type de décision Date de création : (02.05.2003 11:59:50)
     * 
     * @parm java.lang.String dateImpression
     * @return java.lang.String
     */
    public java.lang.String _getSource(CPDonneesBase donnee) {
        String source = "";
        try {
            if (decision != null) {
                String varTemp = " ";
                if (decision.isNonActif()) {
                    if (CPDonneesBase.CS_NOTRE_ESTIMATION.equalsIgnoreCase(donnee.getSourceInformation())) {
                        source = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 2, 8, varTemp)
                                .toString();
                    } else if (CPDonneesBase.CS_VOTRE_ESTIMATION.equalsIgnoreCase(donnee.getSourceInformation())) {
                        source = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 2, 6, varTemp)
                                .toString();
                    } else if (CPDonneesBase.CS_FISC.equalsIgnoreCase(donnee.getSourceInformation())
                            || CPDonneesBase.CS_REPRISE_IMPOT.equalsIgnoreCase(donnee.getSourceInformation())) {
                        source = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 2, 7, varTemp)
                                .toString();
                    } else if (CPDonneesBase.CS_VOS_COMPTE.equalsIgnoreCase(donnee.getSourceInformation())) {
                        source = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 2, 9, varTemp)
                                .toString();
                    } else if (CPDonneesBase.CS_TAX_OFFICE.equalsIgnoreCase(donnee.getSourceInformation())) {
                        source = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 2, 10, varTemp)
                                .toString();
                    } else if (CPDonneesBase.CS_BENEFICE_CAP.equalsIgnoreCase(donnee.getSourceInformation())) {
                        source = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 2, 11, varTemp)
                                .toString();
                    } else {
                        source = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 2, 6, varTemp)
                                .toString();
                    }
                } else {
                    if (CPDonneesBase.CS_NOTRE_ESTIMATION.equalsIgnoreCase(donnee.getSourceInformation())) {
                        source = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 2, 3, varTemp)
                                .toString();
                    } else if (CPDonneesBase.CS_VOTRE_ESTIMATION.equalsIgnoreCase(donnee.getSourceInformation())) {
                        source = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 2, 1, varTemp)
                                .toString();
                    } else if (CPDonneesBase.CS_FISC.equalsIgnoreCase(donnee.getSourceInformation())
                            || CPDonneesBase.CS_REPRISE_IMPOT.equalsIgnoreCase(donnee.getSourceInformation())) {
                        source = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 2, 2, varTemp)
                                .toString();
                    } else if (CPDonneesBase.CS_VOS_COMPTE.equalsIgnoreCase(donnee.getSourceInformation())) {
                        source = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 2, 4, varTemp)
                                .toString();
                    } else if (CPDonneesBase.CS_TAX_OFFICE.equalsIgnoreCase(donnee.getSourceInformation())) {
                        source = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 2, 5, varTemp)
                                .toString();
                    } else if (CPDonneesBase.CS_BENEFICE_CAP.equalsIgnoreCase(donnee.getSourceInformation())) {
                        source = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 2, 12, varTemp)
                                .toString();
                    } else {
                        source = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 2, 1, varTemp)
                                .toString();
                    }
                }
            } else {
                return "";
            }
            return source;
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return "";
        }
    }

    /**
     * Retourne le n° d téléphone du responable Date de création : (02.05.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String _getTelResponsable() {
        try {
            if (decision != null) {
                if (getUser() == null) {
                    JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
                    JadeUser user = service.load(decision.getResponsable());
                    setUser(user);
                }
                // si le user n'est pas vide, rapatrier les données de détails
                // du user
                if (user != null) {
                    return user.getPhone();
                }

            }
            return "";
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return "";
        }
    }

    /**
     * Type de décision dans la langue de l'utilisateur Date de création : (02.05.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String _getTitre(String periodeDecision) {
        try {
            if (decision != null) {
                if (decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_PROVISOIRE)) {
                    return this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 1, 1, periodeDecision)
                            .toString();
                } else if (decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_DEFINITIVE)) {
                    return this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 1, 2, periodeDecision)
                            .toString();
                } else if (decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_RECTIFICATION)
                        || decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_CORRECTION)) {
                    return this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 1, 4, periodeDecision)
                            .toString();
                } else if (decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_ACOMPTE)) {
                    return this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 1, 3, periodeDecision)
                            .toString();
                } else if (decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_REMISE)) {
                    return this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 1, 6, periodeDecision)
                            .toString();
                } else if (decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_REDUCTION)) {
                    return this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 1, 5, periodeDecision)
                            .toString();
                } else {
                    return "";
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return "";
        }
    }

    /**
     * Commentaire relatif à la méthode _headerText.
     */
    public void _headerCommun() {
        try {
            // Période
            String periodeDecision = getDecision().getDebutDecision() + " - " + getDecision().getFinDecision();
            // Décision provioire.... 01.01.2007 - 31.12.2007
            super.setParametres(CPIListeDecisionParam.PARAM_TYPEETPERIODE, _getTitre(periodeDecision));
            // Source des données
            super.setParametres(CPIListeDecisionParam.PARAM_SOURCE, _getSource(getDonneeBase()));
            // Commentaires et remarques
            super.setParametres(CPIListeDecisionParam.PARAM_PARA1,
                    this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 1, 7, _getCommentaire())
                            .toString());
            super.setParametres(CPIListeDecisionParam.PARAM_REMARQUE,
                    this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 1, 8, _getRemarque()).toString());
            // Libellé franc (pour pas aller le rechercher dans les labels à
            // chaque fois
            libFranc = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 3, 1, " ").toString();
            // super.setParametres("P_INFO_CAISSE",
            // getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 4, 1,
            // " ").toString());
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
        }
    }

    /**
     * Commentaire relatif à la méthode _headerText.
     */
    protected void _headerText(CaisseHeaderReportBean headerBean) {
        try {
            String varTemp = "";
            // Chargement headerbean
            setHeaderBean(headerBean);
            // Fortune totale
            if (CPDecision.CS_ACOMPTE.equalsIgnoreCase(decision.getTypeDecision()) && !isAcompteDetailCalcul()) {
                String texteSource = " ";
                String periodeDecision = getDecision().getDebutDecision() + " - " + getDecision().getFinDecision();
                // Décision provioire.... 01.01.2007 - 31.12.2007
                texteSource = this.getTexteDocument(acomptesNac, acompteNac, 1, 1, _getTitre(periodeDecision))
                        .toString();
                // Commentaires
                texteSource = texteSource
                        + this.getTexteDocument(acomptesNac, acompteNac, 2, 1, _getCommentaire()).toString();
                // Source des données
                texteSource = texteSource
                        + this.getTexteDocument(acomptesNac, acompteNac, 3, 1, _getSource(getDonneeBase())).toString();
                super.setParametres(CPIListeDecisionParam.PARAM_PARA1, texteSource);
                // Détail du calcul
                super.setParametres(CPIListeDecisionParam.LABEL_REVENU,
                        this.getTexteDocument(acomptesNac, acompteNac, 4, 1, " ").toString());
                super.setParametres("L_SIGNE_PLUS", this.getTexteDocument(acomptesNac, acompteNac, 4, 2, " ")
                        .toString());
                super.setParametres(CPIListeDecisionParam.LABEL_FORTUNE,
                        this.getTexteDocument(acomptesNac, acompteNac, 4, 3, " ").toString());
                ;
                super.setParametres("L_SIGNE_EGAL", this.getTexteDocument(acomptesNac, acompteNac, 4, 4, " ")
                        .toString());
                super.setParametres("L_ITEXT_ACOMPTENAC_FORTUNE_DET",
                        this.getTexteDocument(acomptesNac, acompteNac, 4, 5, " ").toString());
                super.setParametres(CPIListeDecisionParam.LABEL_TOTAL_FORTUNE,
                        this.getTexteDocument(acomptesNac, acompteNac, 4, 6, " ").toString());
                super.setParametres(
                        CPIListeDecisionParam.PARAM_FORTUNE_DET,
                        this.getTexteDocument(
                                acomptesNac,
                                acompteNac,
                                4,
                                7,
                                getDonneeCalcul().getMontant(decision.getIdDecision(),
                                        CPDonneesCalcul.CS_FORTUNE_TOTALE)).toString());
                super.setParametres(CPIListeDecisionParam.LABEL_ARRONDI,
                        this.getTexteDocument(acomptesNac, acompteNac, 4, 8, " ").toString());
                super.setParametres(
                        CPIListeDecisionParam.PARAM_FORTUNE_ARRONDIE,
                        this.getTexteDocument(acomptesNac, acompteNac, 4, 9,
                                getDonneeCalcul().getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_50000INF))
                                .toString());
                super.setParametres("L_ITEXT_ACOMPTENAC_REMARQUE",
                        this.getTexteDocument(acomptesNac, acompteNac, 4, 10, " ").toString());
                super.setParametres(CPIListeDecisionParam.PARAM_REMARQUE,
                        this.getTexteDocument(acomptesNac, acompteNac, 5, 1, _getRemarque()).toString());
            } else {
                // Décision de non actif
                // Zones communes aux décisions
                _headerCommun();
                CPPeriodeFiscale perfis = _getPers();
                super.setParametres(CPIListeDecisionParam.PARAM_DEBUT_REVENU,
                        this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 1, 3, _getDebutRevenu(perfis))
                                .toString());
                super.setParametres(CPIListeDecisionParam.PARAM_FIN_REVENU,
                        this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 1, 5, _getFinRevenu(perfis))
                                .toString());
                // Si revenu vide, imprimer 0
                String revenuMoyen = getDonneeCalcul().getMontant(decision.getIdDecision(),
                        CPDonneesCalcul.CS_REV_MOYEN);
                if (!JadeStringUtil.isBlank(revenuMoyen)) {
                    super.setParametres(CPIListeDecisionParam.PARAM_REVENU_BRUT,
                            this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 1, 7, revenuMoyen)
                                    .toString());
                } else {
                    varTemp = getDonneeCalcul().getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_BRUT);
                    if (JadeStringUtil.isBlank(varTemp)) {
                        varTemp = "0";
                    }
                    super.setParametres(CPIListeDecisionParam.PARAM_REVENU_BRUT,
                            this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 1, 7, varTemp).toString());
                }
                // Si revenu * 20 vide, imprimer 0
                String varRevenu = "0";
                if (!JadeStringUtil.isBlank(getDonneeCalcul().getMontant(decision.getIdDecision(),
                        CPDonneesCalcul.CS_REV_20))) {
                    varRevenu = getDonneeCalcul().getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_20);
                }

                super.setParametres(CPIListeDecisionParam.PARAM_REVENU_X20,
                        this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 1, 9, varRevenu).toString());
                //
                super.setParametres("P_FRANC", libFranc);
                // Fortune
                super.setParametres(
                        CPIListeDecisionParam.PARAM_DATE_FORTUNE,
                        this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 2, 3,
                                donneeBase.getDateFortune()).toString());
                // Afficher la fortune si elle n'est pas négative et vide
                String fortune = "0";
                // PO 4070 if
                // (!JadeStringUtil.isBlank(donneeBase.getFortuneTotale()) &&
                // !"-".equalsIgnoreCase(donneeBase.getFortuneTotale().substring(0,
                // 1))) {
                if (!JadeStringUtil.isBlankOrZero(donneeBase.getFortuneTotale(1))) {
                    // Prendre la fortune au prorata si elle existe (cas < 12
                    // mois)
                    fortune = getDonneeCalcul()
                            .getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_FORTUNE_PRORATA);
                    if (JadeStringUtil.isEmpty(fortune)) {
                        fortune = donneeBase.getFortuneTotale(1);
                    }
                }
                super.setParametres(CPIListeDecisionParam.PARAM_FORTUNE,
                        this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 2, 5, fortune).toString());
                super.setParametres(CPIListeDecisionParam.LABEL_REVENU,
                        this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 1, 1, " ").toString());
                super.setParametres(CPIListeDecisionParam.LABEL_FORTUNE,
                        this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 2, 1, " ").toString());
                super.setParametres(CPIListeDecisionParam.LABEL_TOTAL_FORTUNE,
                        this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 3, 1, " ").toString());
                super.setParametres(CPIListeDecisionParam.LABEL_DU,
                        this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 1, 2, " ").toString());
                super.setParametres(CPIListeDecisionParam.LABEL_AU,
                        this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 1, 4, " ").toString());
                super.setParametres(CPIListeDecisionParam.LABEL_CAPITALISE,
                        this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 1, 8, " ").toString());
                super.setParametres(CPIListeDecisionParam.LABEL_ARRONDI,
                        this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 3, 3, " ").toString());
                super.setParametres("L_FRANC_REVENU",
                        this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 1, 6, libFranc).toString());
                super.setParametres("L_FRANC_FORTUNE",
                        this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 2, 4, libFranc).toString());
                super.setParametres(
                        CPIListeDecisionParam.PARAM_FORTUNE_DET,
                        this.getTexteDocument(
                                decisionsNonActifTemp,
                                decisionNonActif,
                                3,
                                2,
                                getDonneeCalcul().getMontant(decision.getIdDecision(),
                                        CPDonneesCalcul.CS_FORTUNE_TOTALE)).toString());
                super.setParametres(
                        CPIListeDecisionParam.PARAM_FORTUNE_ARRONDIE,
                        this.getTexteDocument(decisionsNonActifTemp, decisionNonActif, 3, 4,
                                getDonneeCalcul().getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_50000INF))
                                .toString());
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(
                    getSession().getErrors().toString() + " - " + affiliation.getAffilieNumero() + " - "
                            + getDecision().getAnneeDecision(), FWMessage.ERREUR, "");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#afterBuildReport ()
     */
    @Override
    public void afterBuildReport() {
        try {
            JasperPrint verso = null;
            if (!decision.getSpecification().equalsIgnoreCase(CPDecision.CS_SALARIE_DISPENSE)) {
                verso = getVerso();
                if (verso != null) {
                    verso.setName("VERSO");
                    super.getDocumentList().add(verso);
                }
            }

            if (getWantLettreCouple() && isImpressionLettreCouple() && (verso != null)) {
                CPLettre_Couple_Minimum_Doc lettre = new CPLettre_Couple_Minimum_Doc();
                lettre.setSession(getSession());
                lettre.setEnvoiGed(envoiGed);
                lettre.setDecision(decision);
                lettre.setDecisionAdresse(decisionAdresse);
                lettre.setSendCompletionMail(false);
                lettre.setFileTitle("Lettre_Couple");
                lettre.setProcessAppelant(processAppelant);
                lettre.setDuplicata(getDuplicata());
                // PO 8717
                lettre.setDateImpression(getDateImpression());
                lettre.executeProcess();

                if (getAffichageEcran().equals(Boolean.FALSE)) {
                    java.util.List<JasperPrint> tmp = lettre.getDocumentList();
                    if (!tmp.isEmpty()) {
                        JadePublishDocument pubDoc = (JadePublishDocument) lettre.getAttachedDocuments().get(0);
                        this.registerAttachedDocument(pubDoc);
                    }
                } else {
                    java.util.List tmp = lettre.getDocumentList();
                    if (!tmp.isEmpty()) {
                        super.getDocumentList().add(tmp.get(0));
                        JasperPrint pageBlanche = super.getImporter().importReport("PHENIX_BLANK",
                                super.getImporter().getImportPath());
                        if (pageBlanche != null) {
                            super.getDocumentList().add(pageBlanche);
                        }
                    }
                }

            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            getMemoryLog().logMessage(
                    getSession().getErrors().toString() + " - " + affiliation.getAffilieNumero() + " - "
                            + getDecision().getAnneeDecision(), FWMessage.ERREUR, "");
            getTransaction().clearErrorBuffer();
        }
    }

    @Override
    public void afterExecuteReport() {
        // try {
        // // on remplace les fichiers sauf si on fait un envoit ged auqeul cas
        // on a besoin des fichiers unitaire.
        // //boolean replaceFiles = getEnvoiGed().equals(Boolean.FALSE);
        // //mergePDF(docInfo, replaceFiles, 500, true, null, null);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        super.afterExecuteReport();
    }

    /**
     * Après l'impression d'un document Par defaut ne fait rien
     */
    @Override
    public void afterPrintDocument() {
        // JadePublishDocumentInfo documentInfoRecap = createDocumentInfo();
        // try {
        // if(getEnvoiGed().equals(Boolean.TRUE)){
        // mergePDFAndAdd(documentInfoRecap);
        // } else {
        // mergePDFAndReplace(documentInfoRecap);
        // }
        // } catch (Exception e) {
        // getMemoryLog().logMessage("Erreur lors de la fusion des documents",
        // e.toString(), this.getClass().getName());
        // }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeBuildReport ()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        setDocumentTitle(decision.getNumeroAffilie() + " - " + decision.getAnneeDecision());
    }

    /**
     * Récupère les informations du décompte avant impression.
     */
    @Override
    public void beforeExecuteReport() {
        try {
            decisionGenerique = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
            decisionTableauCoti = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
            decisionNonActif = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
            decisionsGeneriqueFR = getICTDecisionGenerique("FR");
            decisionsGeneriqueDE = getICTDecisionGenerique("DE");
            decisionsGeneriqueIT = getICTDecisionGenerique("IT");
            decisionsTableauCotiFR = getICTTableauCotisation("FR");
            decisionsTableauCotiDE = getICTTableauCotisation("DE");
            decisionsTableauCotiIT = getICTTableauCotisation("IT");
            decisionsNonActifFR = getICTDecisionNonActif("FR");
            decisionsNonActifDE = getICTDecisionNonActif("DE");
            decisionsNonActifIT = getICTDecisionNonActif("IT");
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
        }
        super.setImpressionParLot(true);
        super.setTailleLot(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforePrintDocument ()
     */
    @Override
    public boolean beforePrintDocument() {
        try {
            if (getImporter().size() < 3) {
                getExporter().setExporterOutline(JRExporterParameter.OUTLINE_NONE);
            } else {
                getExporter().setExporterOutline(JRExporterParameter.OUTLINE_1SUR2);
            }
        } catch (FWIException e) {
            // CAST OMMIT -> on laisse par defaut si prb !!!
        }
        return super.beforePrintDocument();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 14:07:16)
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            if (!JadeStringUtil.isIntegerEmpty(getDecision().getIdPassage())) {
                FAPassage thePassage = FAUtil.loadPassage(getDecision().getIdPassage(), getSession());
                FAUtil.fillDocInfoWithPassageInfo(getDocumentInfo(), thePassage);
            }

            CPIDecision_DS manager = null;
            // Sous contrôle d'exceptions
            // On récupère les documents du catalogue de textes nécessaires
            langueDoc = AFUtil.toLangueIso(decision.getLangue());
            String codeLangue = decision.getLangue();
            // decisionsGenerique
            if (IConstantes.CS_TIERS_LANGUE_ALLEMAND.equals(codeLangue)) {
                decisionsGeneriqueTemp = decisionsGeneriqueDE;
                decisionsTableauCotiTemp = decisionsTableauCotiDE;
                decisionsNonActifTemp = decisionsNonActifDE;
            } else if (IConstantes.CS_TIERS_LANGUE_ITALIEN.equals(codeLangue)) {
                decisionsGeneriqueTemp = decisionsGeneriqueIT;
                decisionsTableauCotiTemp = decisionsTableauCotiIT;
                decisionsNonActifTemp = decisionsNonActifIT;
            } else {
                decisionsGeneriqueTemp = decisionsGeneriqueFR;
                decisionsTableauCotiTemp = decisionsTableauCotiFR;
                decisionsNonActifTemp = decisionsNonActifFR;
            }

            // Affiliation
            AFAffiliation affi = new AFAffiliation();
            affi.setSession(getSession());
            affi.setAffiliationId(decision.getIdAffiliation());
            affi.retrieve();
            setAffiliation(affi);
            // Données encodées
            donneeBase = new CPDonneesBase();
            donneeBase.setSession(getSession());
            donneeBase.setIdDecision(getDecision().getIdDecision());
            donneeBase.retrieve();
            setDonneeBase(donneeBase);
            // Données calculées
            donneeCalcul = new CPDonneesCalcul();
            donneeCalcul.setSession(getSession());
            // Compte annexe
            recCompteAnnexe();
            // Role bloquer envoi
            TIRoleManager roleMng = new TIRoleManager();
            roleMng.setSession(getSession());
            roleMng.setForIdTiers(decision.getIdTiers());
            roleMng.setForRole(TIRole.CS_BLOQUER_ENVOI);
            if (getDateImpression() == null) {
                setDateImpression(JACalendar.todayJJsMMsAAAA());
                if (!JadeStringUtil.isBlankOrZero(decision.getIdPassage())) {
                    FAPassage passage = new FAPassage();
                    passage.setSession(getSession());
                    passage.setIdPassage(decision.getIdPassage());
                    passage.retrieve();
                    if (!passage.isNew()) {
                        setDateImpression(passage.getDateFacturation());
                    }
                } else {
                    globaz.musca.api.IFAPassage prochPassage = findNextPassageFacturation();
                    if (prochPassage != null) {
                        setDateImpression(prochPassage.getDateFacturation());
                    }
                }
            }
            roleMng.setForDateEntreDebutEtFin(getDateImpression());
            if (roleMng.getCount() > 0) {
                bloquerEnvoi = true;
            } else {
                bloquerEnvoi = false;
            }
            // dernier état
            manager = new CPIDecision_DS();
            manager.setLangue(langueDoc);
            manager.setSession(getSession());
            manager.setCompteAnnexe(getCompteAnnexe());
            manager.setEtat(decision.getEtat());
            manager.setNotInGenreCotisation(CodeSystem.TYPE_ASS_CPS_GENERAL + ", " + CodeSystem.TYPE_ASS_CPS_AUTRE);
            manager.setDecision(getDecision());
            manager.setIdLangue(_getIdLangue());
            // Vérifier l'id de l'entête
            if (JadeStringUtil.isIntegerEmpty(decision.getIdDecision())) {
                return;
            }
            // Where clause
            manager.setForIdDecision(decision.getIdDecision());
            manager.setDocument(decisionTableauCoti);
            manager.setDocuments(decisionsTableauCotiTemp);
            manager.setRes(resTableauCoti);
            super.setDataSource(manager);

            if (getDecision().isNonActif()) {
                // On récupère les documents du catalogue de textes nécessaires
                // pour les non actifs
                if (decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_ACOMPTE) && !isAcompteDetailCalcul()) {
                    acompteNac = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
                    acomptesNac = getICTAcompteNac();
                    super.setTemplateFile("PHENIX_ACOMPTE_NAC");
                } else if ((decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_ACOMPTE))
                        || ((Integer.parseInt(decision.getAnneeDecision()) >= JACalendar.getYear(JACalendar
                                .todayJJsMMsAAAA())) && !decision.getPeriodicite().equalsIgnoreCase(
                                CodeSystem.PERIODICITE_ANNUELLE))) {
                    super.setTemplateFile("PHENIX_DECISION");
                } else {
                    super.setTemplateFile("PHENIX_DECISION2");
                }
            } else {
                // On récupère les documents du catalogue de textes nécessaires
                // pour les non actifs
                decisionsInd = getICTDecisionInd();
                if (decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_ACOMPTE) && !isAcompteDetailCalcul()) {
                    acomptesInd = getICTAcompteIndependant();
                    if (isEbusiness) {
                        super.setTemplateFile("PHENIX_ACOMPTE_IND_EBU");
                    } else {
                        super.setTemplateFile("PHENIX_ACOMPTE_IND");
                    }

                } else if ((decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_ACOMPTE))
                        || ((Integer.parseInt(decision.getAnneeDecision()) >= JACalendar.getYear(JACalendar
                                .todayJJsMMsAAAA())) && !decision.getPeriodicite().equalsIgnoreCase(
                                CodeSystem.PERIODICITE_ANNUELLE))) {

                    if (isEbusiness) {
                        super.setTemplateFile("PHENIX_DECISION_IND_EBU");
                    } else {
                        super.setTemplateFile("PHENIX_DECISION_IND");
                    }
                } else {
                    if ("false".equalsIgnoreCase(getDecisionRetroactiveAvecMontantFacture())) {
                        if (isEbusiness) {
                            super.setTemplateFile("PHENIX_DECISION_IND_EBU");
                        } else {
                            super.setTemplateFile("PHENIX_DECISION_IND");
                        }
                    } else {
                        if (isEbusiness) {
                            super.setTemplateFile("PHENIX_DECISION_IND2_EBU");
                        } else {
                            super.setTemplateFile("PHENIX_DECISION_IND2");
                        }
                    }
                }
            }
            // Info
            setDocumentInfo();
            getDocumentInfo().setPublishDocument(false);
            getDocumentInfo().setArchiveDocument(true);

            // nom du template
            ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                    getDocumentInfo(), getSession().getApplication(), langueDoc);
            CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

            // this.setParameter(CPIListeDecisionParam.PARAM_DUPLICATA_FR, "test");
            _headerText(headerBean);
            _columnHeader();
            caisseReportHelper.addHeaderParameters(this, headerBean);

            // Agence communale
            if (!JadeStringUtil.isBlank(decision.getDesignation1Admin())) {
                caisseReportHelper.addSignatureParameters(this,
                        decision.getDesignation1Admin() + " " + decision.getDesignation2Admin()); // agence
            } else {
                caisseReportHelper.addSignatureParameters(this, "");
            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            getMemoryLog().logMessage(
                    getSession().getErrors().toString() + " - " + affiliation.getAffilieNumero() + " - "
                            + getDecision().getAnneeDecision(), FWMessage.ERREUR, "");
            getTransaction().clearErrorBuffer();
        }
    }

    private globaz.musca.api.IFAPassage findNextPassageFacturation() {
        globaz.musca.api.IFAPassage passage = null;
        // Recherche si séparation indépendant et non-actif - Inforom 314s
        Boolean isSeprationIndNac = false;
        try {
            isSeprationIndNac = new Boolean(GlobazSystem.getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA)
                    .getProperty(FAApplication.SEPARATION_IND_NA));
        } catch (Exception e) {
            isSeprationIndNac = Boolean.FALSE;
        }
        if (isSeprationIndNac) {
            if (getDecision().isNonActif()) {
                passage = ServicesFacturation.getProchainPassageFacturation(getSession(), null,
                        FAModuleFacturation.CS_MODULE_COT_PERS_NAC);
            } else {
                passage = ServicesFacturation.getProchainPassageFacturation(getSession(), null,
                        FAModuleFacturation.CS_MODULE_COT_PERS_IND);
            }
        } else {
            passage = ServicesFacturation.getProchainPassageFacturation(getSession(), null,
                    FAModuleFacturation.CS_MODULE_COT_PERS);
        }
        return passage;
    }

    /**
     * Formate le texte. Remplace un {0} par la variable passée en paramètre
     * 
     * @param paragraphe
     * @return
     * @throws Exception
     */
    public StringBuffer format(StringBuffer paragraphe, String varTemp) {
        StringBuffer res = new StringBuffer("");
        String chaineModifiee = paragraphe.toString();
        ;
        int index1 = chaineModifiee.indexOf("{");
        int index2 = chaineModifiee.indexOf("}");
        if ((index1 != -1) && (index2 != -1)) {
            String chaineARemplacer = chaineModifiee.substring(index1, index2 + 1);
            // remplacement de la variable par sa valeur (varTemp)
            if (varTemp == "") {
                varTemp = " ";
            }
            res.append(CPToolBox.replaceString(paragraphe.toString(), chaineARemplacer, varTemp));
        } else {
            res.append(paragraphe.toString());
        }
        return res;
    }

    public Boolean getAffichageEcran() {
        return affichageEcran;
    };

    /**
     * Insérez la description de la méthode ici. Date de création : (19.05.2003 17:04:26)
     * 
     * @return globaz.naos.db.affiliation.AFAffiliation
     */
    public globaz.naos.db.affiliation.AFAffiliation getAffiliation() {
        return affiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.07.2003 13:42:06)
     * 
     * @return globaz.osiris.db.comptes.CACompteAnnexe
     */
    public globaz.osiris.db.comptes.CACompteAnnexe getCompteAnnexe() {
        return compteAnnexe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.06.2002 07:42:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateImpression() {
        return dateImpression;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.05.2003 12:51:41)
     * 
     * @return globaz.phenix.db.principale.CPDecision
     */
    public CPDecisionAgenceCommunale getDecision() {
        return decision;
    }

    /**
     * @return
     */
    public CPDecisionAgenceCommunale getDecisionAdresse() {
        return decisionAdresse;
    }

    public String getDecisionRetroactiveAvecMontantFacture() {
        return decisionRetroactiveAvecMontantFacture;
    }

    /**
     * Returns the donneeBase.
     * 
     * @return CPDonneesBase
     */
    public CPDonneesBase getDonneeBase() {
        return donneeBase;
    }

    public CPDonneesCalcul getDonneeCalcul() {
        return donneeCalcul;
    }

    public Boolean getDuplicata() {
        return duplicata;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        // Déterminer l'objet du message en fonction du code erreur
        String obj = "";
        if (getMemoryLog().hasErrors()) {
            if (1 == getTypeDocument()) {
                obj = getSession().getLabel("SUJET_EMAIL_PASOK_MISEENCOMPTE") + " " + decision.getIdPassage();
            } else if (2 == getTypeDocument()) {
                obj = getSession().getLabel("SUJET_EMAIL_PASOK_DECISIONNAC") + " " + decision.getIdPassage();
            } else if (3 == getTypeDocument()) {
                obj = getSession().getLabel("SUJET_EMAIL_PASOK_LETTREDISPENSE") + " " + decision.getIdPassage();
            } else if (4 == getTypeDocument()) {
                obj = getSession().getLabel("SUJET_EMAIL_PASOK_DECISIONIND") + " " + decision.getIdPassage();
            } else {
                obj = getSession().getLabel("SUJET_EMAIL_PASOK_DECISION") + " " + decision.getIdPassage();
            }
        } else {
            if (1 == getTypeDocument()) {
                obj = getSession().getLabel("SUJET_EMAIL_OK_MISEENCOMPTE") + " " + decision.getIdPassage();
            } else if (2 == getTypeDocument()) {
                obj = getSession().getLabel("SUJET_EMAIL_OK_DECISIONNAC") + " " + decision.getIdPassage();
            } else if (3 == getTypeDocument()) {
                obj = getSession().getLabel("SUJET_EMAIL_OK_LETTREDISPENSE") + " " + decision.getIdPassage();
            } else if (4 == getTypeDocument()) {
                obj = getSession().getLabel("SUJET_EMAIL_OK_DECISIONIND") + " " + decision.getIdPassage();
            } else {
                obj = getSession().getLabel("SUJET_EMAIL_OK_DECISION") + " " + decision.getIdPassage();
            }
        }

        // Restituer l'objet
        return obj;
    }

    /**
     * @return
     */
    public Boolean getEnvoiGed() {
        return envoiGed;
    }

    /**
     * Récupère le document permettant d'ajouter les textes du catalogue de texte au document pour les aomptes pour
     * indépendant
     * 
     * @author: hna Créé le : 05.07.2007
     * @return
     */
    public ICTDocument[] getICTAcompteIndependant() {
        // if (resAcompteInd == null) {
        acompteInd.setISession(getSession());
        acompteInd.setCsDomaine(globaz.phenix.translation.CodeSystem.CS_DOMAINE_CP);
        acompteInd.setCsTypeDocument(globaz.phenix.translation.CodeSystem.CS_TYPE_ACOMPTE_IND);
        acompteInd.setDefault(new Boolean(true));
        acompteInd.setCodeIsoLangue(langueDoc); // "FR"
        acompteInd.setActif(new Boolean(true));
        try {
            resAcompteInd = acompteInd.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, this.getClass().getName());
        }
        // }
        return resAcompteInd;
    }

    /**
     * Récupère le document permettant d'ajouter les textes du catalogue de texte au document pour les aomptes pour
     * indépendant
     * 
     * @author: hna Créé le : 05.07.2007
     * @return
     */
    public ICTDocument[] getICTAcompteNac() {
        // if (resAcompteNac == null) {
        acompteNac.setISession(getSession());
        acompteNac.setCsDomaine(globaz.phenix.translation.CodeSystem.CS_DOMAINE_CP);
        acompteNac.setCsTypeDocument(globaz.phenix.translation.CodeSystem.CS_TYPE_ACOMPTE_NAC);
        acompteNac.setDefault(new Boolean(true));
        acompteNac.setCodeIsoLangue(langueDoc); // "FR"
        acompteNac.setActif(new Boolean(true));
        try {
            resAcompteNac = acompteNac.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, this.getClass().getName());
        }
        // }
        return resAcompteNac;
    }

    /**
     * Récupère le document permettant d'ajouter les textes du catalogue de texte au document
     * 
     * @author: hna Créé le : 28.06.2007
     * @return
     */
    public ICTDocument[] getICTDecisionGenerique(String langue) {
        decisionGenerique.setISession(getSession());
        decisionGenerique.setCsDomaine(globaz.phenix.translation.CodeSystem.CS_DOMAINE_CP);
        decisionGenerique.setCsTypeDocument(globaz.phenix.translation.CodeSystem.CS_TYPE_DECISION);
        decisionGenerique.setDefault(new Boolean(true));
        decisionGenerique.setCodeIsoLangue(langue);
        decisionGenerique.setActif(new Boolean(true));
        try {
            resGenerique = decisionGenerique.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, this.getClass().getName());
        }
        return resGenerique;
    }

    /**
     * Récupère le document permettant d'ajouter les textes du catalogue de texte au document
     * 
     * @author: sel Créé le : 13 déc. 06
     * @return
     */
    public ICTDocument[] getICTDecisionInd() {
        // if (resInd == null) {
        decisionInd.setISession(getSession());
        decisionInd.setCsDomaine(globaz.phenix.translation.CodeSystem.CS_DOMAINE_CP);
        if (CPDecision.CS_ACOMPTE.equalsIgnoreCase(decision.getTypeDecision()) && !isAcompteDetailCalcul()) {
            decisionInd.setCsTypeDocument(globaz.phenix.translation.CodeSystem.CS_TYPE_ACOMPTE_IND);
        } else {
            decisionInd.setCsTypeDocument(globaz.phenix.translation.CodeSystem.CS_TYPE_DECISION_IND);
        }
        decisionInd.setDefault(new Boolean(true));
        decisionInd.setCodeIsoLangue(langueDoc); // "FR"
        decisionInd.setActif(new Boolean(true));
        try {
            resInd = decisionInd.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, this.getClass().getName());
        }
        // }
        return resInd;
    }

    /**
     * Récupère le document permettant d'ajouter les textes du catalogue de texte au document pour les décision de non
     * actif
     * 
     * @author: hna Créé le : 03.07.2007
     * @return
     */
    public ICTDocument[] getICTDecisionNonActif(String langue) {
        decisionNonActif.setISession(getSession());
        decisionNonActif.setCsDomaine(globaz.phenix.translation.CodeSystem.CS_DOMAINE_CP);
        decisionNonActif.setCsTypeDocument(globaz.phenix.translation.CodeSystem.CS_TYPE_DECISION_NAC);
        decisionNonActif.setDefault(new Boolean(true));
        decisionNonActif.setCodeIsoLangue(langue);
        decisionTableauCoti.setActif(new Boolean(true));
        try {
            resNonActif = decisionNonActif.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, this.getClass().getName());
        }
        return resNonActif;
    }

    /**
     * Récupère le document permettant d'ajouter les textes du catalogue de texte au document
     * 
     * @author: hna Créé le : 28.06.2007
     * @return
     */
    public ICTDocument[] getICTTableauCotisation(String langue) {
        decisionTableauCoti.setISession(getSession());
        decisionTableauCoti.setCsDomaine(globaz.phenix.translation.CodeSystem.CS_DOMAINE_CP);
        decisionTableauCoti.setCsTypeDocument(globaz.phenix.translation.CodeSystem.CS_TYPE_TABLEAU_COTISATION);
        decisionTableauCoti.setDefault(new Boolean(true));
        decisionTableauCoti.setCodeIsoLangue(langue);
        decisionTableauCoti.setActif(new Boolean(true));
        try {
            resTableauCoti = decisionTableauCoti.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, this.getClass().getName());
        }
        return resTableauCoti;
    }

    private String getNumInforom(String typeDecision, String genreAffiliation) {

        String numInforom = "";

        if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(genreAffiliation)) {
            if (CPDecision.CS_PROVISOIRE.equalsIgnoreCase(typeDecision)
                    || CPDecision.CS_CORRECTION.equalsIgnoreCase(typeDecision)) {
                numInforom = "0072CCP";
            } else if (CPDecision.CS_DEFINITIVE.equalsIgnoreCase(typeDecision)) {
                numInforom = "0073CCP";
            } else if (CPDecision.CS_RECTIFICATION.equalsIgnoreCase(typeDecision)) {
                numInforom = "0127CCP";
            } else if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(typeDecision)) {
                numInforom = "0119CCP";
            } else if (CPDecision.CS_ACOMPTE.equalsIgnoreCase(typeDecision)) {
                numInforom = "0071CCP";
            } else {
                numInforom = "0073CCP";
            }
        } else {
            if (CPDecision.CS_PROVISOIRE.equalsIgnoreCase(typeDecision)
                    || CPDecision.CS_CORRECTION.equalsIgnoreCase(typeDecision)) {
                numInforom = "0067CCP";
            } else if (CPDecision.CS_DEFINITIVE.equalsIgnoreCase(typeDecision)) {
                numInforom = "0069CCP";
            } else if (CPDecision.CS_RECTIFICATION.equalsIgnoreCase(typeDecision)) {
                numInforom = "0070CCP";
            } else if (CPDecision.CS_ACOMPTE.equalsIgnoreCase(typeDecision)) {
                numInforom = "0066CCP";
            } else {
                numInforom = "0069CCP";
            }
        }
        if (CPDecision.CS_REMISE.equalsIgnoreCase(typeDecision)) {
            numInforom = "0191CCP";
        } else if (CPDecision.CS_REDUCTION.equalsIgnoreCase(typeDecision)) {
            numInforom = "0284CCP";
        }

        return numInforom;
    }

    public String getNumInforomForDecision() {
        String numInforom = "";

        if (getDecision() != null) {
            String typeDecision = getDecision().getTypeDecision();
            String genreAffiliation = getDecision().getGenreAffilie();

            numInforom = getNumInforom(typeDecision, genreAffiliation);
        }

        return numInforom;
    }

    /**
     * Récupère les textes du catalogue de texte Créé le : 02.07.2007
     * 
     * @return
     * @throws Exception
     */
    public StringBuffer getTexteDocument(ICTDocument[] documents, ICTDocument document, int niveau, int position) {
        StringBuffer resString = new StringBuffer("");
        try {
            if (document == null) {
                getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
            } else {
                ICTListeTextes listeTextes = documents[0].getTextes(niveau);
                resString.append(listeTextes.getTexte(position));
            }
        } catch (Exception e3) {
            new StringBuffer(" ");
            // getMemoryLog().logMessage(e3.toString(), FWMessage.ERREUR,
            // getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
        }
        return resString;
    }

    /**
     * Récupère les textes du catalogue de texte et remplace les paramètres Créé le : 02.07.2007
     * 
     * @return
     * @throws Exception
     */
    public StringBuffer getTexteDocument(ICTDocument[] documents, ICTDocument document, int niveau, int position,
            String vartTemp) {
        StringBuffer resString = new StringBuffer("");
        try {
            if (document == null) {
                getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
            } else {
                ICTListeTextes listeTextes = documents[0].getTextes(niveau);
                resString.append(listeTextes.getTexte(position));
            }
        } catch (Exception e3) {
            new StringBuffer(" ");
            // getMemoryLog().logMessage(e3.toString(), FWMessage.ERREUR,
            // getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
        }
        return format(resString, vartTemp);
    }

    /**
     * @return
     */
    public int getTypeDocument() {
        return typeDocument;
    }

    public JadeUser getUser() {
        return user;
    }

    /**
     * Recupère le document verso dans le cache sinon sur le disque si pas présent
     * 
     * @return JasperPrint
     */
    protected JasperPrint getVerso() throws Exception {
        /*
         * Il faut tenir compte de la définition des versos qui est différente selon les caisses. Il y en a qui ont un
         * verso différent selon le genre (non actif ou indépendant) ou selon le type (provisoire ou définitive) et il y
         * a des caisses qui n'ont qu'un seul verso pour tous les genres de décision
         */
        // Test existance document
        ICTDocument docVerso = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        ICTDocument[] catalogue = null;
        docVerso.setISession(getSession());
        docVerso.setCsDomaine(globaz.phenix.translation.CodeSystem.CS_DOMAINE_CP);
        docVerso.setDefault(new Boolean(true));
        docVerso.setCodeIsoLangue(langueDoc); // "FR"
        docVerso.setActif(new Boolean(true));
        java.util.List versoGenere = null;
        // Recherche verso pour opposition
        catalogue = getVersoOpposition(docVerso, catalogue);
        // Recherche pour verso générique acompte
        if (catalogue == null) {
            catalogue = getVersoAcompte(docVerso, catalogue);
        }
        // Recherche du catalogue avec verso générique (cas le plus fréquent)
        if (catalogue == null) {
            catalogue = getVersoGenerique(docVerso, catalogue);
            if ((catalogue == null) || (catalogue.length == 0)) {
                if (decision.isNonActif()) {
                    catalogue = getVersoNonActif(docVerso, catalogue);
                } else { // Indépendant
                    catalogue = getVersoIndependant(docVerso, catalogue);
                }
            }
        }
        if ((catalogue != null) && (catalogue.length != 0)) {
            CPVerso_DOC verso = new CPVerso_DOC();
            verso.setSession(getSession());
            verso.setAffiliation(getAffiliation());
            verso.setSendCompletionMail(false);
            verso.setDocument(catalogue[0]);
            verso.setDecision(decision);
            verso.setFileTitle("Verso");
            verso.setProcessAppelant(processAppelant);
            verso.executeProcess();
            versoGenere = verso.getDocumentList();
            if (versoGenere.isEmpty()) {
                return null;
            } else {
                return (JasperPrint) versoGenere.get(0);
            }
        } else {
            return null;
        }
    }

    private ICTDocument[] getVersoAcompte(ICTDocument docVerso, ICTDocument[] catalogue) throws Exception {
        if ((catalogue == null) && CPDecision.CS_ACOMPTE.equalsIgnoreCase(decision.getTypeDecision())) {
            codeVersoGenAco = globaz.phenix.translation.CodeSystem.CS_VERSO_GEN_ACO;
            docVerso.setIdDocument(codeVersoGenAco);
            catalogue = docVerso.load();
        }
        return catalogue;
    }

    private ICTDocument[] getVersoGenerique(ICTDocument docVerso, ICTDocument[] catalogue) throws Exception {
        codeVersoGen = globaz.phenix.translation.CodeSystem.CS_VERSO_GEN;
        docVerso.setIdDocument(codeVersoGen);
        catalogue = docVerso.load();
        if ((catalogue == null) || (catalogue.length == 0)) {
            // Recherche pour verso générique provisoire
            if (CPToolBox.isProvisoireMetier(decision)) {
                codeVersoGenPro = globaz.phenix.translation.CodeSystem.CS_VERSO_GEN_PRO;
                docVerso.setIdDocument(codeVersoGenPro);
                catalogue = docVerso.load();
            } else {
                // Recherche pour générique définitif
                codeVersoGenDef = globaz.phenix.translation.CodeSystem.CS_VERSO_GEN_DEF;
                docVerso.setIdDocument(codeVersoGenDef);
                catalogue = docVerso.load();
            }
        }
        return catalogue;
    }

    private ICTDocument[] getVersoIndependant(ICTDocument docVerso, ICTDocument[] catalogue) throws Exception {
        // Test si acompte
        if (CPDecision.CS_ACOMPTE.equalsIgnoreCase(decision.getTypeDecision())) {
            // Test si recherche déjà effectuée
            codeVersoAcoInd = globaz.phenix.translation.CodeSystem.CS_VERSO_ACO_IND;
            docVerso.setIdDocument(codeVersoAcoInd);
            catalogue = docVerso.load();
        }
        if ((catalogue == null) || (catalogue.length == 0)) {
            // Recherche par genre de décision
            codeVersoGenInd = globaz.phenix.translation.CodeSystem.CS_VERSO_GEN_IND;
            docVerso.setIdDocument(codeVersoGenInd);
            catalogue = docVerso.load();
            if ((catalogue == null) || (catalogue.length == 0)) {
                // Recherche par type de décision
                if (CPToolBox.isProvisoireMetier(decision)) {
                    docVerso.setIdDocument(globaz.phenix.translation.CodeSystem.CS_VERSO_PRO_IND);
                    catalogue = docVerso.load();
                } else { // Verso pour définitif indépendant
                    docVerso.setIdDocument(globaz.phenix.translation.CodeSystem.CS_VERSO_DEF_IND);
                    catalogue = docVerso.load();
                }
            }
        }
        return catalogue;
    }

    private ICTDocument[] getVersoNonActif(ICTDocument docVerso, ICTDocument[] catalogue) throws Exception {
        // Test si acompte
        if (CPDecision.CS_ACOMPTE.equalsIgnoreCase(decision.getTypeDecision())) {
            // Test si recherche déjà effectuée
            codeVersoAcoNac = globaz.phenix.translation.CodeSystem.CS_VERSO_ACO_NAC;
            docVerso.setIdDocument(codeVersoAcoNac);
            catalogue = docVerso.load();
        }
        if ((catalogue == null) || (catalogue.length == 0)) {
            // Recherche par genre de décision
            codeVersoGenNac = globaz.phenix.translation.CodeSystem.CS_VERSO_GEN_NAC;
            docVerso.setIdDocument(codeVersoGenNac);
            catalogue = docVerso.load();
            if ((catalogue == null) || (catalogue.length == 0)) {
                // Recherche par type de décision
                if (CPToolBox.isProvisoireMetier(decision)) {
                    docVerso.setIdDocument(globaz.phenix.translation.CodeSystem.CS_VERSO_PRO_NAC);
                    catalogue = docVerso.load();
                } else {
                    docVerso.setIdDocument(globaz.phenix.translation.CodeSystem.CS_VERSO_DEF_NAC);
                    catalogue = docVerso.load();
                }
            }
        }
        return catalogue;
    }

    private ICTDocument[] getVersoOpposition(ICTDocument docVerso, ICTDocument[] catalogue) throws Exception {
        if (decision.getOpposition().equals(new Boolean(true))) {
            codeVersoGenOpp = globaz.phenix.translation.CodeSystem.CS_VERSO_GEN_OPP;
            docVerso.setIdDocument(codeVersoGenOpp);
            catalogue = docVerso.load();
            if ((catalogue == null) || (catalogue.length == 0)) {
                // Test si verso opposition pour non actif
                if (decision.isNonActif()) {
                    codeVersoOppNac = globaz.phenix.translation.CodeSystem.CS_VERSO_OPP_NAC;
                    docVerso.setIdDocument(codeVersoOppNac);
                    catalogue = docVerso.load();
                } else {
                    codeVersoOppInd = globaz.phenix.translation.CodeSystem.CS_VERSO_OPP_IND;
                    docVerso.setIdDocument(codeVersoOppInd);
                    catalogue = docVerso.load();
                }
            }
        }
        return catalogue;
    }

    public boolean getWantLettreCouple() {
        return wantLettreCouple;
    }

    public boolean isAcompteDetailCalcul() {
        return acompteDetailCalcul;
    }

    /**
     * @return
     */
    public boolean isImpressionLettreCouple() {
        impressionLettreCouple = false;
        // et que la spécificité "Lettre couple" (déterminée lors du calcul) est
        // présente
        if (decision.getSpecification().equalsIgnoreCase(CPDecision.CS_LETTRE_COUPLE)) {
            impressionLettreCouple = true;
        }
        return impressionLettreCouple;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        if (getParent() != null) {
            return getParent().jobQueue();
        } else {
            return GlobazJobQueue.READ_SHORT;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() {
        processAppelant.incProgressCounter();
        boolean hasNext = false;
        if (getFileTitle().equalsIgnoreCase("Lettre_Couple") || getFileTitle().equalsIgnoreCase("Verso")) {
            nbDoc++;
            setFileTitle("");
            return true;
        }
        if ((m_container != null) && (hasNext = m_container.hasNext()) && !isAborted()) {
            decision = (CPDecisionAgenceCommunale) m_container.next();
            decisionAdresse = (CPDecisionAgenceCommunale) m_containerAdresse.next();
            // On suprime le dernier élément lu pour liberer la memoire
            m_container.remove();
            m_containerAdresse.remove();
            // Contrôle si la décision contient des cotisations
            if (!CPDecision.CS_IMPUTATION.equalsIgnoreCase(decision.getTypeDecision())
                    && !JadeStringUtil.isBlankOrZero(decision.getIdDecision())) {
                CPCotisationManager cotiMng = new CPCotisationManager();
                cotiMng.setSession(getSession());
                cotiMng.setForIdDecision(decision.getIdDecision());
                try {
                    if (cotiMng.getCount() == 0) {
                        getMemoryLog().logMessage(
                                getSession().getLabel("CP_MSG_0207") + " - " + decision.getNumeroAffilie() + " - "
                                        + getDecision().getAnneeDecision(), FWMessage.AVERTISSEMENT, "");
                        getTransaction().clearErrorBuffer();
                        return next();
                    }
                } catch (Exception e) {
                    getMemoryLog().logMessage(
                            getSession().getLabel("CP_MSG_0207") + " - " + decision.getNumeroAffilie() + " - "
                                    + getDecision().getAnneeDecision(), FWMessage.AVERTISSEMENT, "");
                    getTransaction().clearErrorBuffer();
                    return next();
                }
            }
            nbDoc++;
            if (getSession().hasErrors() || getTransaction().hasErrors()) {
                getMemoryLog().logMessage(
                        getSession().getErrors().toString() + " - " + decision.getNumeroAffilie() + " - "
                                + getDecision().getAnneeDecision(), FWMessage.ERREUR, "");
                getTransaction().clearErrorBuffer();
            }
            if (getSession().hasWarnings() || getTransaction().hasWarnings()) {
                getMemoryLog().logMessage(
                        getSession().getWarnings().toString() + " - " + decision.getNumeroAffilie() + " - "
                                + getDecision().getAnneeDecision(), FWMessage.AVERTISSEMENT, "");
                getTransaction().clearWarningBuffer();
            }
        }
        if (getSession().hasWarnings() || getTransaction().hasWarnings()) {
            getMemoryLog().logMessage(
                    getSession().getWarnings().toString() + " - " + decision.getNumeroAffilie() + " - "
                            + getDecision().getAnneeDecision(), FWMessage.AVERTISSEMENT, "");
            getTransaction().clearWarningBuffer();
        }
        return hasNext;
    }

    /**
     * Compte annexe d'un tiers affilié Date de création : (26.07.2003 16:18:35)
     */
    public void recCompteAnnexe() {
        try { // Recherche du n° d'affilié (idExterneRole)
            if (decision.getIdTiers() != null) {
                String role = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                        getSession().getApplication());
                // Extraction du compte annexe
                CACompteAnnexe compte = new CACompteAnnexe();
                compte.setSession(getSession());
                compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                compte.setIdRole(role);
                compte.setIdExterneRole(decision.getNumeroAffilie());
                compte.wantCallMethodBefore(false);
                compte.retrieve();
                setCompteAnnexe(compte);
            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            setCompteAnnexe(null);
        }
    }

    /*
     * replace les paramètres (variables) défini dans les textes entre {}
     */
    public String remplaceParametres(String texte) throws Exception {
        String chaineModifiee = texte;
        int index1 = chaineModifiee.indexOf("{");
        int index2 = chaineModifiee.indexOf("}");
        while ((index1 != -1) && (index2 != -1)) {
            String chaineARemplacer = chaineModifiee.substring(index1, index2 + 1);
            // Pour le genre d'affilié --> avec/sans activité lucrative
            if (CPSignet.CS_DATE_DECISION.equals(chaineARemplacer)) {
                chaineModifiee = CPToolBox.replaceString(chaineModifiee, CPSignet.CS_DATE_DECISION, getDecision()
                        .getDateInformation());
            } else if (CPSignet.CS_ANNEE_DECISION.equals(chaineARemplacer)) {
                chaineModifiee = CPToolBox.replaceString(chaineModifiee, CPSignet.CS_ANNEE_DECISION, getDecision()
                        .getAnneeDecision());
            } else if (CPSignet.CS_CIVILITE.equals(chaineARemplacer)) {
                chaineModifiee = CPToolBox.replaceString(chaineModifiee, CPSignet.CS_CIVILITE,
                        CodeSystem.getLibelleIso(getSession(), getDecision().getTitre_tiers(), langueDoc));
            } else if (CPSignet.CS_MONTANT_ANNUEL_AVS.equals(chaineARemplacer)) {
                String montantCoti = "";
                CPCotisation coti = CPCotisation._returnCotisation(getSession(), getDecision().getIdDecision(),
                        CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
                if (coti != null) {
                    montantCoti = coti.getMontantAnnuel();
                }
                chaineModifiee = CPToolBox.replaceString(chaineModifiee, CPSignet.CS_MONTANT_ANNUEL_AVS, montantCoti);
            } else if (CPSignet.CS_MONTANT_ANNUEL_FAD.equals(chaineARemplacer)) {
                String montantCoti = "";
                CPCotisation coti = CPCotisation._returnCotisation(getSession(), getDecision().getIdDecision(),
                        CodeSystem.TYPE_ASS_FRAIS_ADMIN);
                if (coti != null) {
                    montantCoti = coti.getMontantAnnuel();
                }
                chaineModifiee = CPToolBox.replaceString(chaineModifiee, CPSignet.CS_MONTANT_ANNUEL_FAD, montantCoti);
            } else if (CPSignet.CS_MONTANT_TOTAL_COTISATION.equals(chaineARemplacer)) {
                String coti = CPCotisation.getTotalCotisation(getSession(), getDecision().getIdDecision(),
                        CodeSystem.PERIODICITE_ANNUELLE);
                chaineModifiee = CPToolBox.replaceString(chaineModifiee, CPSignet.CS_MONTANT_TOTAL_COTISATION, coti);
            }
            // on recherche les prochaines {}
            index1 = chaineModifiee.indexOf("{", index2);
            index2 = chaineModifiee.indexOf("}", index2 + 1);
        }
        return chaineModifiee;
    }

    public void setAcompteDetailCalcul(boolean acompteDetailCalcul) {
        this.acompteDetailCalcul = acompteDetailCalcul;
    }

    public void setAffichageEcran(Boolean affichageEcran) {
        this.affichageEcran = affichageEcran;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.05.2003 17:04:26)
     * 
     * @param newAffiliation
     *            globaz.naos.db.affiliation.AFAffiliation
     */
    public void setAffiliation(globaz.naos.db.affiliation.AFAffiliation newAffiliation) {
        affiliation = newAffiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.07.2003 13:42:06)
     * 
     * @param newCompteAnnexe
     *            globaz.osiris.db.comptes.CACompteAnnexe
     */
    public void setCompteAnnexe(globaz.osiris.db.comptes.CACompteAnnexe newCompteAnnexe) {
        compteAnnexe = newCompteAnnexe;
    }

    /**
     * Sets the container.
     * 
     * @param container
     *            The container to set
     */
    public void setContainer(Vector container) {
        m_container = container.iterator();
    }

    /**
     * Sets the container.
     * 
     * @param container
     *            The container to set
     */
    public void setContainerAdresse(Vector container) {
        m_containerAdresse = container.iterator();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.06.2002 07:42:07)
     * 
     * @param newDateImpression
     *            java.lang.String
     */
    public void setDateImpression(java.lang.String newDateImpression) {
        dateImpression = newDateImpression;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.05.2003 12:51:41)
     * 
     * @param newDecision
     *            globaz.phenix.db.principale.CPDecision
     */
    public void setDecision(CPDecisionAgenceCommunale newDecision) {
        decision = newDecision;
    }

    /**
     * @param communale
     */
    public void setDecisionAdresse(CPDecisionAgenceCommunale communale) {
        decisionAdresse = communale;
    }

    public void setDecisionRetroactiveAvecMontantFacture(String newDecisionRetroactiveAvecMontantFacture) {
        decisionRetroactiveAvecMontantFacture = newDecisionRetroactiveAvecMontantFacture;
    }

    /*
     * Insertion des infos pour la publication (GED)
     */
    public void setDocumentInfo() {
        try {
            getDocumentInfo().setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID,
                    ((CPApplication) getSession().getApplication()).getGedTypeDossier());
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID, "");
        }
        try {
            getDocumentInfo().setDocumentProperty(JadeGedTargetProperties.SERVICE,
                    ((CPApplication) getSession().getApplication()).getGedService());
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty(JadeGedTargetProperties.SERVICE, "");
        }

        getDocumentInfo().setDocumentTypeNumber(getNumInforom(decision.getTypeDecision(), decision.getGenreAffilie()));

        getDocumentInfo().setDocumentType(decision.getTypeDecision());
        getDocumentInfo().setDocumentDate(getDateImpression());
        getDocumentInfo().setDocumentProperty(CADocumentInfoHelper.SECTION_ID_EXTERNE, decision.getAnneeDecision());
        getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_DEBUT, decision.getDebutDecision());
        getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_FIN, decision.getFinDecision());
        getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_TYPE, decision.getTypeDecision());
        getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_GENRE, decision.getGenreAffilie());
        getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_PERIODE,
                decision.getDebutDecision() + "-" + decision.getFinDecision());
        try {
            getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_LIB_TYPE,
                    CodeSystem.getLibelleIso(getSession(), getDecision().getTypeDecision(), langueDoc));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_LIB_TYPE, "");
        }
        try {
            getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_LIB_COURT_TYPE,
                    CodeSystem.getCode(getSession(), getDecision().getTypeDecision()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_LIB_COURT_TYPE, "");
        }
        try {
            getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_LIB_GENRE,
                    CodeSystem.getLibelleIso(getSession(), getDecision().getGenreAffilie(), langueDoc));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_LIB_GENRE, "");
        }
        try {
            getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_LIB_COURT_GENRE,
                    CodeSystem.getCode(getSession(), getDecision().getGenreAffilie()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_LIB_COURT_GENRE, "");
        }
        try {
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE,
                    CPToolBox.unFormat(decision.getNumeroAffilie()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE, "");
        }
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE, decision.getNumeroAffilie());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_ID, decision.getIdTiers());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_LANGUE_ISO,
                TITiers.toLangueIso(decision.getLangue()));
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NOM, decision.getDesignation1_tiers());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_PRENOM, decision.getDesignation2_tiers());
        // on prend les noms et prénoms du tiers et on les concatène -> on ne peut pas se fier à l'affiliation car elle
        // est parfois nulle.
        String nomPrenom = TITiers.getNom(decision.getDesignation1_tiers(), decision.getDesignation2_tiers(), " ");
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NOM_PRENOM, nomPrenom);
        /* Personne Avs */
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_SEXE, decision.getSexe());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_DATE_NAISSANCE, decision.getDateNaissance());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE,
                decision.getNumAvsActuel());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE,
                JadeStringUtil.removeChar(decision.getNumAvsActuel(), '.'));
        /* Adresse */
        TIAdresseDataSource dataSource = new TIAdresseDataSource();
        dataSource.setSession(getSession());
        dataSource.load(decision, JACalendar.todayJJsMMsAAAA());
        if (dataSource != null) {
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.ADRESSE_RUE, dataSource.rue);
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.ADRESSE_NUMERO_POSTAL, dataSource.localiteNpa);
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.ADRESSE_LOCALITE_NOM, dataSource.localiteNom);
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.ADRESSE_CANTON_ID, dataSource.canton_id);
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.ADRESSE_PAYS_ISO, dataSource.paysIso);
        }
        // Bloquer envoi
        if (bloquerEnvoi) {
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.ROLE_BLOQUER_ENVOI, "true");
            getDocumentInfo().setRejectDocument(true);
            // getDocumentInfo().setPreventFromPublish(true);
        } else {
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.ROLE_BLOQUER_ENVOI, "false");
            getDocumentInfo().setRejectDocument(false);
            // getDocumentInfo().setPreventFromPublish(false);
        }
        // Document duplex
        getDocumentInfo().setDuplex(true);
        getDocumentInfo().setDuplexRule(JadePublishDocumentInfo.DUPLEX_ON_LAST);
    }

    /**
     * Sets the donneeBase.
     * 
     * @param donneeBase
     *            The donneeBase to set
     */
    public void setDonneeBase(CPDonneesBase donneeBase) {
        this.donneeBase = donneeBase;
    }

    public void setDonneeCalcul(CPDonneesCalcul donneeCalcul) {
        this.donneeCalcul = donneeCalcul;
    }

    public void setDuplicata(Boolean duplicata) {
        this.duplicata = duplicata;
    }

    /**
     * @param boolean1
     */
    public void setEnvoiGed(Boolean boolean1) {
        envoiGed = boolean1;
    }

    protected void setHeaderBean(CaisseHeaderReportBean headerBean) throws Exception {
        // On renseigne le paramètre duplicata
        if (getDuplicata()) {
            if (IConstantes.CS_TIERS_LANGUE_ALLEMAND.equals(decision.getLangue())) {
                super.setParametres(CPIListeDecisionParam.PARAM_DUPLICATA_DE, Boolean.TRUE);
                super.setParametres(CPIListeDecisionParam.PARAM_DUPLICATA_FR, Boolean.FALSE);
            } else {
                super.setParametres(CPIListeDecisionParam.PARAM_DUPLICATA_DE, Boolean.FALSE);
                super.setParametres(CPIListeDecisionParam.PARAM_DUPLICATA_FR, Boolean.TRUE);
            }
        } else {
            super.setParametres(CPIListeDecisionParam.PARAM_DUPLICATA_DE, Boolean.FALSE);
            super.setParametres(CPIListeDecisionParam.PARAM_DUPLICATA_FR, Boolean.FALSE);
        }
        headerBean.setDate(_getLieuDate(getDateImpression()));
        headerBean.setNoAffilie(decision.getNumeroAffilie());

        // Insertion du numéro IDE
        AFIDEUtil.addNumeroIDEInDoc(getSession(), headerBean, decision.getIdAffiliation());

        headerBean.setNoAvs(decision.getNumAvsActuel());
        JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
        if (CPApplication.getCPApplication(CPApplication.DEFAULT_APPLICATION_PHENIX).isUseSessionUserForHeader()
                && !isEbusiness) {
            // dans ce cas, on utilise le user en session pour les infos (phone,
            // nom, services...) dans le header des docuements CP.

            JadeUser user = getSession().getUserInfo();
            setUser(user);
            headerBean.setNomCollaborateur(getSession().getUserFullName());
            headerBean.setTelCollaborateur(user.getPhone());
            headerBean.setUser(user);

        } else {
            if (!JadeStringUtil.isEmpty(decision.getResponsable())) {

                JadeUser user = service.load(decision.getResponsable());
                setUser(user);
                headerBean.setNomCollaborateur(_getResponsable());
                headerBean.setTelCollaborateur(_getTelResponsable());
                headerBean.setUser(user);
            }
        }

        headerBean.setAdresse(_getAdresse());
        headerBean.setConfidentiel(true);
        if (Boolean.TRUE.equals(decision.getLettreSignature())) {
            headerBean.setRecommandee(true);
        }
    }

    public void setProcessAppelant(BProcess processAppelant) {
        this.processAppelant = processAppelant;
    }

    /**
     * @param i
     */
    public void setTypeDocument(int i) {
        typeDocument = i;
    }

    public void setUser(JadeUser user) {
        this.user = user;
    }

    public void setWantLettreCouple(boolean wantLettreCouple) {
        this.wantLettreCouple = wantLettreCouple;
    }

    public boolean isEbusiness() {
        return isEbusiness;
    }

    public void setEbusiness(boolean isEbusiness) {
        this.isEbusiness = isEbusiness;
    }

}
