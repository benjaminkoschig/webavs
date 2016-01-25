package globaz.phenix.documentsItext;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.CTDocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.jade.log.JadeLogger;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichage;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.listes.itext.CPIListeDecisionParam;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.CPUtil;
import globaz.phenix.util.Constante;
import globaz.phenix.util.DocumentInfoPhenix;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TIRoleManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.CommonProperties;

/**
 * Insérez la description du type ici. Date de création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class CPImpressionCommunication_Doc extends FWIDocumentManager {

    private static final long serialVersionUID = 1L;
    public static final int CS_ALLEMAND = 503002;
    // initialisation pour CS langue tiers (TITIERS)
    public static final int CS_FRANCAIS = 503001;
    public static final int CS_ITALIEN = 503004;
    public static final String NUMERO_CAISSE = "NUMEROCAISSE";
    private String anneeDecision = "";
    private String dateEdition = "";
    private String dateEnvoi = "";
    private Boolean dateEnvoiVide = new Boolean(false);
    private String debutActivite = "";
    private String eMailObject = "";
    private CPCommunicationFiscaleAffichage entity = null;
    private String forCanton = "";
    private String forGenreAffilie = "";
    private String idAffiliation = "";
    private java.lang.String idCommunication = "";
    private java.lang.String idDecision = "";
    private String idIfd = "";
    private CPCommunicationFiscaleAffichageManager manager = null;
    private int nbCommunication = 0;
    private String nomPrenom = "";
    private CPPeriodeFiscale periodeFiscale = null;
    private long progressCounter = -1;
    private BStatement statement = null;
    private TITiersViewBean tiers;
    private Boolean withAnneeEnCours = new Boolean(false);

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public CPImpressionCommunication_Doc() throws Exception {
        this(new BSession(CPApplication.DEFAULT_APPLICATION_PHENIX));
    }

    public CPImpressionCommunication_Doc(BProcess parent) throws FWIException {
        super(parent, CPApplication.APPLICATION_PHENIX_REP, "COMMUNICATION");
        super.setFileTitle(getSession().getLabel("CP_MSG_0194"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public CPImpressionCommunication_Doc(BSession session) throws FWIException {
        super(session, CPApplication.APPLICATION_PHENIX_REP, "COMMUNICATION");
        super.setFileTitle(getSession().getLabel("CP_MSG_0194"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
        if (hasAttachedDocuments()) {
            getMemoryLog().logMessage(
                    "\n" + getSession().getLabel("CP_MSG_0149") + " " + nbCommunication + " "
                            + getSession().getLabel("CP_MSG_0180"), globaz.framework.util.FWMessage.INFORMATION,
                    this.getClass().getName());
        } else {
            getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0150"),
                    globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
        }
        if ((getTransaction() != null) && (getTransaction().isOpened())) {
            try {
                getTransaction().closeTransaction();
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        // Permet l'affichage des données du processus
        setState(Constante.FWPROCESS_MGS_220);
        super._executeCleanUp();
    }

    /**
     * Retourne l'adresse de la caisse dans la langue du tiers Date de création : (02.05.2003 11:59:50)
     * 
     * @parm java.lang.String dateImpression
     * @return java.lang.String
     */
    public java.lang.String _getAdresseCaisse() {
        try {
            return getSession().getApplication().getProperty(
                    "ADR_CAISSE_"
                            + globaz.phenix.translation.CodeSystem.getCode(getSession(), getTiers(getTransaction())
                                    .getLangue()));
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), e.toString());
            return "";
        }
    }

    /**
     * Retourne les années de référence de la période IFD Si ancien mode de taxation (2 année) sinon 1 seule Date de
     * création : (02.05.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String _getAnneeRef() {
        if (entity.getAnneeRevenuDebut().equalsIgnoreCase(entity.getAnneeRevenuFin())) {
            return entity.getAnneeRevenuDebut();
            // Taxation postnumerando (nouveau)
        } else {
            // Taxation praetnumerando (ancien)
            return entity.getAnneeRevenuDebut() + " " + entity.getAnneeRevenuFin();
        }
    }

    /**
     * Retourne le numéro de caisse et les lettres du canton Selon la langue du canton Date de création : (02.05.2003
     * 11:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String _getCanton() {
        try {
            return getSession().getApplication().getLabel(CPImpressionCommunication_Doc.NUMERO_CAISSE, _getLangue())
                    + (getSession().getApplication()).getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE) + " / "
                    + globaz.phenix.translation.CodeSystem.getCode(getSession(), entity.getCanton());
        } catch (Exception e) {
            getTransaction().addErrors(e.getMessage());
            return "";
        }
    }

    /**
     * Réalise le mapping des langue du canton en CODE SYSTEM
     * 
     * @author:BTC
     * @return int CS_FRANCAIS = 503001; CS_ALLEMAND = 503002; CS_ITALIEN = 503004;
     **/
    public int _getIdLangue() {
        try {
            String canton = globaz.phenix.translation.CodeSystem.getCode(getSession(), entity.getCanton());
            return Integer.parseInt(((CPApplication) getSession().getApplication()).getLangueCantonISO(canton));
        } catch (Exception e) {
            this._addError(getTransaction(), e.toString());
        }
        return CPImpressionCommunication_Doc.CS_ALLEMAND;
    }

    protected String _getLangue() {
        switch (_getIdLangue()) {
            case CS_FRANCAIS:
                return "FR";
            case CS_ITALIEN:
                return "IT";
            default:
                return "DE";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:44:29)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _validate() throws java.lang.Exception {
        // Contrôle du mail
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }
        // Si impression par lot
        if (JadeStringUtil.isIntegerEmpty(getIdCommunication())) {
            setAnneeDecision(getAnneeDecision().trim());
            if (JadeStringUtil.isEmpty(getAnneeDecision())) {
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(getSession().getLabel("DECISION_INVALIDE"));
            } else {
                try {
                    Integer.parseInt(getAnneeDecision());
                } catch (NumberFormatException ex) {
                    JadeLogger.error(this, ex);
                    setMsgType(FWViewBeanInterface.ERROR);
                    setMessage(getSession().getLabel("DECISION_INVALIDE"));
                }
            }
            // Contrôle de la date d'envoi
            setDateEnvoi(getDateEnvoi().trim());
            boolean erreurDateEnvoi = false;
            if (!JadeStringUtil.isEmpty(getDateEnvoi())) {
                try {
                    JADate dDateEnvoi = new JADate(getDateEnvoi());
                    BSessionUtil.checkDateGregorian(getSession(), dDateEnvoi);
                } catch (Exception ex) {
                    JadeLogger.error(this, ex);
                    erreurDateEnvoi = true;
                }
            }
            if (erreurDateEnvoi) {
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(getSession().getLabel("DATE_ENVOI_INVALIDE"));
            }
            // Contrôle de la d'édition
            if (!JadeStringUtil.isEmpty(getDateEdition())) {
                try {
                    JADate dDateEdition = new JADate(getDateEdition());
                    BSessionUtil.checkDateGregorian(getSession(), dDateEdition);
                } catch (Exception ex) {
                    setMsgType(FWViewBeanInterface.ERROR);
                    setMessage(getSession().getLabel("DATE_EDITION_INVALIDE"));
                }
            }
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        // Les erreurs sont ajoutées à la session,
        // abort permet l'arrêt du process
        if (!JadeStringUtil.isEmpty(getMessage())) {
            abort();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        try {
            super.setParametres(CPIListeDecisionParam.PARAM_ADRESSE,
                    entity.getAdresse(getTransaction(), Constante.FORMAT_ADRESSE_COURRIER));
            super.setParametres(CPIListeDecisionParam.PARAM_CANTON, _getCanton());
            super.setParametres(CPIListeDecisionParam.PARAM_NUM_AFFILIE, entity.getNumAffilie());
            String numero = "";
            if (entity.getNumAvs() != null) {
                int tailleMin = 11;
                if (NSUtil.unFormatAVS(entity.getNumAvs()).length() < tailleMin) {
                    int difference = tailleMin - entity.getNumAvs().length();
                    numero = entity.getNumAvs();
                    for (int i = 0; i < difference; i++) {
                        numero += "0";
                    }
                } else {
                    numero = NSUtil.unFormatAVS(entity.getNumAvs());
                }
                if (numero.length() == 13) {
                    // Recherche de l'ancien n° avs
                    String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, getSession()));
                    if (!JadeStringUtil.isEmpty(varNumAvs)) {
                        numero = varNumAvs;
                    }
                }
            }
            super.setParametres(CPIListeDecisionParam.PARAM_NUM_AVS, numero);
            super.setParametres(CPIListeDecisionParam.PARAM_NUM_CONTRIBUABLE,
                    entity.getNumContri(entity.getAnneeDecision()));
            super.setParametres(CPIListeDecisionParam.PARAM_ADR_CAISSE, _getAdresseCaisse());
            super.setParametres(CPIListeDecisionParam.PARAM_DATE_EDITION, getDateEdition());
            super.setParametres(CPIListeDecisionParam.PARAM_DATE_INDEPENDANT, getDebutActivite());
            entity.setConjoint(null);
            TITiersViewBean cjt = entity.getConjoint();
            if (cjt != null) {
                numero = "";
                if (cjt.getNumAvsActuel() != null) {
                    int tailleMin = 11;
                    if (NSUtil.unFormatAVS(cjt.getNumAvsActuel()).length() < tailleMin) {
                        int difference = tailleMin - cjt.getNumAvsActuel().length();
                        numero = cjt.getNumAvsActuel();
                        for (int i = 0; i < difference; i++) {
                            numero += "0";
                        }
                    } else {
                        numero = NSUtil.unFormatAVS(cjt.getNumAvsActuel());
                    }
                    if (numero.length() == 13) {
                        // Recherche de l'ancien n° avs
                        String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, getSession()));
                        if (!JadeStringUtil.isEmpty(varNumAvs)) {
                            numero = varNumAvs;
                        }
                    }
                }
                super.setParametres(CPIListeDecisionParam.PARAM_NUM_AVS_CONJOINT, numero);
                super.setParametres(CPIListeDecisionParam.PARAM_PRENOM_CONJOINT, cjt.getDesignation2());
            } else {
                super.setParametres(CPIListeDecisionParam.PARAM_NUM_AVS_CONJOINT, "");
                super.setParametres(CPIListeDecisionParam.PARAM_PRENOM_CONJOINT, "");
            }
            String canton = globaz.phenix.translation.CodeSystem.getCode(getSession(), entity.getCanton());
            CPApplication phenixApplication = (CPApplication) getSession().getApplication();
            setDocumentInfo();
            ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                    getDocumentInfo(), getSession().getApplication(),
                    phenixApplication.getLangue2ISO(phenixApplication.getLangueCantonISO(canton)));
            CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
            headerBean.setNoAffilie(" ");
            headerBean.setNoAvs(" ");
            headerBean.setAdresse(" ");
            headerBean.setDate(" ");
            headerBean.setRecommandee(false);
            headerBean.setConfidentiel(false);
            int anneeCritere2 = Integer.parseInt(entity.getAnneeRevenuDebut()) + 2;
            super.setParametres(CPIListeDecisionParam.PARAM_ANNEE_TAXATION, entity.getAnneeDecision());
            super.setParametres(CPIListeDecisionParam.PARAM_ANNEE_REF, _getAnneeRef());
            // jour critère au 01.01 quand année < 2001
            int anneeChgt = ((CPApplication) getSession().getApplication()).getAnneeChangement();
            if (Integer.parseInt(entity.getAnneeRevenuDebut()) < anneeChgt) {
                super.setParametres(CPIListeDecisionParam.PARAM_JOUR_CRITERE1, "01.01." + entity.getAnneeRevenuDebut());
            } else {
                super.setParametres(CPIListeDecisionParam.PARAM_JOUR_CRITERE1, "31.12." + entity.getAnneeRevenuDebut());
            }
            super.setParametres(CPIListeDecisionParam.PARAM_JOUR_CRITERE2, "01.01." + Integer.toString(anneeCritere2));
            super.setParametres(CPIListeDecisionParam.PARAM_ANNEE_REVENU1, entity.getAnneeRevenuDebut());
            super.setParametres(CPIListeDecisionParam.PARAM_ANNEE_REVENU2, entity.getAnneeRevenuFin());
            CPCommunicationFiscale comfis = new CPCommunicationFiscale();
            comfis.setSession(getSession());
            comfis.setIdCommunication(entity.getIdCommunication());
            comfis.retrieve(getTransaction());
            if (!comfis.isNew()) {
                comfis.setDateEnvoi(getDateEdition());
                comfis.update(getTransaction());
            }
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                getTransaction().rollback();
            }
            // Definir le nom du document :
            super.setDocumentTitle(entity.getNumAffilie() + " - " + entity.getNom() + " " + entity.getPrenom());
            caisseReportHelper.addHeaderParameters(this, headerBean);
        } catch (Exception e) {
            getTransaction().addErrors(e.getMessage());
            super.setParametres(CPIListeDecisionParam.PARAM_ANNEE_TAXATION, "");
            super.setParametres(CPIListeDecisionParam.PARAM_ANNEE_REF, "");
            super.setParametres(CPIListeDecisionParam.PARAM_JOUR_CRITERE1, "");
            super.setParametres(CPIListeDecisionParam.PARAM_JOUR_CRITERE2, "");
            super.setParametres(CPIListeDecisionParam.PARAM_ANNEE_REVENU1, "");
            super.setParametres(CPIListeDecisionParam.PARAM_ANNEE_REVENU2, "");
        }
    }

    /**
     * Récupère les informations du décompte avant impression.
     */
    @Override
    public void beforeExecuteReport() {
        // Variable pour le comptage
        try {
            super.setTailleLot(500);
            super.setImpressionParLot(true);
            // sujet de l'email à envoyer
            String emailSubject = getSession().getLabel("OBJEMAIL_FAPRINT_IMPRESSSIONCOMMUNICATION");
            setEMailObject(emailSubject);
            // Traitement des communications suivant la selection
            manager = new CPCommunicationFiscaleAffichageManager();
            manager.setSession(getSession());
            manager.setDateEnvoiVide(getDateEnvoiVide());
            manager.setForDateEnvoi(getDateEnvoi());
            manager.setForCanton(getForCanton());
            manager.setExceptRetour(Boolean.TRUE);
            // Zone d'écran
            // SI Indépendant de sélectionné => IND, TSE, AGR, REN donc
            // différtent de NON ACTIF...
            if (!JadeStringUtil.isBlankOrZero(getForGenreAffilie())) {
                if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(getForGenreAffilie())) {
                    manager.setNotInGenreAffilie(CPDecision.CS_NON_ACTIF + ", " + CPDecision.CS_ETUDIANT);
                } else {
                    manager.setInGenreAffilie(CPDecision.CS_NON_ACTIF + ", " + CPDecision.CS_ETUDIANT);
                }
            }
            manager.setForAnneeDecision(getAnneeDecision());
            manager.setForIdCommunication(getIdCommunication());
            if (JadeStringUtil.isBlankOrZero(manager.getForIdCommunication())) {
                if (JadeStringUtil.isBlankOrZero(getAnneeDecision())) {
                    manager.setWithAnneeEnCours(getWithAnneeEnCours());
                } else {
                    manager.setWithAnneeEnCours(Boolean.FALSE);
                }
            } else {
                manager.setWithAnneeEnCours(Boolean.TRUE);
            }
            manager.setDemandeAnnulee(Boolean.FALSE);
            manager.setDateEdition(getDateEdition());
            manager.orderByCanton();
            manager.orderByNumContribuable();
            manager.orderByAnnee();
            /*---------------------------------------------------------------------
             Initiliser la progression du progress
             ---------------------------------------------------------------------
             */
            nbCommunication = manager.getCount(getTransaction());
            // nombre de communication fiscale à traiter
            // Entrer les informations pour l' état du process
            setState(getSession().getLabel("OBJEMAIL_FAPRINT_IMPRESSSIONCOMMUNICATION"));
            if (nbCommunication > 0) {
                setProgressScaleValue(nbCommunication);
            } else {
                setProgressScaleValue(1);
            }
            statement = manager.cursorOpen(getTransaction());
            if (nbCommunication == 0) {
                // le manager ne contient aucune police
                getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0150"),
                        globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
            }
        } catch (Exception e) {
            this._addError("false");
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            try {
                getTransaction().rollback();
            } catch (Exception f) {
                getMemoryLog().logMessage(f.getMessage(), FWMessage.FATAL, this.getClass().getName());

            } finally {
                try {
                    if (statement != null) {
                        manager.cursorClose(statement);
                    }
                } catch (Exception g) {
                    getMemoryLog().logMessage(g.getMessage(), FWMessage.FATAL, this.getClass().getName());
                }
            }
        } finally {
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforePrintDocument ()
     */
    @Override
    public boolean beforePrintDocument() {
        if ((size() == 0) || isAborted()) {
            getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0150"),
                    globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
            // Permet l'affichage des données du processus
            setState(Constante.FWPROCESS_MGS_220);
            return false;
        } else { // On met la liste de document dans l'ordre
            super.DocumentSort();
            return true;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 14:07:16)
     */
    @Override
    public void createDataSource() {
        try {
            CPApplication phenixApplication = (globaz.phenix.application.CPApplication) getSession().getApplication();
            // Vérifier l'identifiant de l'ordre groupé
            // if (JadeStringUtil.isIntegerEmpty(entity.getIdDecision())) {
            // getMemoryLog().logMessage("a remplir", FWMessage.FATAL,
            // getClass().getName());
            // return;
            // }
            // Sauvegarde de données
            if (!entity.isNew()) {
                // Test si ancienne ou nouvelle communication
                int anneeDecision = Integer.parseInt(entity.getAnneeDecision());
                int anneeChgt = phenixApplication.getAnneeChangement();
                if (anneeDecision < anneeChgt) {
                    // definir le template a utiliser selon la langue du canton
                    // et le genre de l'affilié
                    if (entity.isNonActif()) {
                        // non actif
                        super.setTemplateFile("PHENIX_OLDCOMFIS_NA_"
                                + phenixApplication.getLangue2ISO(phenixApplication
                                        .getLangueCantonISO(globaz.phenix.translation.CodeSystem.getCode(getSession(),
                                                entity.getCanton()))));
                        getDocumentInfo().setDocumentTypeNumber("0075CCP");
                    } else {
                        if (entity.getCanton() == IConstantes.CS_LOCALITE_CANTON_VALAIS) {
                            // Indépendant valaisan
                            super.setTemplateFile("PHENIX_OLDCOMFIS_IND_VS_"
                                    + phenixApplication.getLangue2ISO(phenixApplication
                                            .getLangueCantonISO(globaz.phenix.translation.CodeSystem.getCode(
                                                    getSession(), entity.getCanton()))));
                        } else {
                            getDocumentInfo().setDocumentTypeNumber("0077CCP");
                            // Indépendant
                            super.setTemplateFile("PHENIX_OLDCOMFIS_IND_"
                                    + phenixApplication.getLangue2ISO(phenixApplication
                                            .getLangueCantonISO(globaz.phenix.translation.CodeSystem.getCode(
                                                    getSession(), entity.getCanton()))));
                        }
                    }
                } else {
                    // definir le template a utiliser selon la langue du canton
                    // et le genre de l'affilié
                    if (entity.isNonActif()) {
                        // non actif
                        super.setTemplateFile("PHENIX_NEWCOMFIS_NA_"
                                + phenixApplication.getLangue2ISO(phenixApplication
                                        .getLangueCantonISO(globaz.phenix.translation.CodeSystem.getCode(getSession(),
                                                entity.getCanton()))));
                        getDocumentInfo().setDocumentTypeNumber("0074CCP");
                    } else {
                        getDocumentInfo().setDocumentTypeNumber("0076CCP");
                        if (entity.getCanton() == IConstantes.CS_LOCALITE_CANTON_VALAIS) {
                            // Independant, TSE valaisan
                            super.setTemplateFile("PHENIX_NEWCOMFIS_IND_VS_"
                                    + phenixApplication.getLangue2ISO(phenixApplication
                                            .getLangueCantonISO(globaz.phenix.translation.CodeSystem.getCode(
                                                    getSession(), entity.getCanton()))));

                        } else {
                            // Indépendant
                            super.setTemplateFile("PHENIX_NEWCOMFIS_IND_"
                                    + phenixApplication.getLangue2ISO(phenixApplication
                                            .getLangueCantonISO(globaz.phenix.translation.CodeSystem.getCode(
                                                    getSession(), entity.getCanton()))));
                        }
                    }
                }
            }

            // Fournir le nom du document
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return;
        }
        return;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.03.2003 11:59:56)
     * 
     * @return java.lang.String
     */
    public java.lang.String getAnneeDecision() {
        return anneeDecision;
    }

    /**
     * @return
     */
    public String getDateEdition() {
        return dateEdition;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.03.2003 12:01:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateEnvoi() {
        return dateEnvoi;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.03.2003 12:02:42)
     * 
     * @return boolean
     */
    public Boolean getDateEnvoiVide() {
        return dateEnvoiVide;
    }

    /**
     * @return
     */
    public java.lang.String getDebutActivite() {
        return debutActivite;
    }

    @Override
    public java.lang.String getEMailObject() {
        return eMailObject;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 11:48:54)
     * 
     * @return globaz.phenix.db.principale.CPDecision
     */
    public CPCommunicationFiscaleAffichage getEntity() {
        return entity;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.03.2003 12:00:20)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForCanton() {
        return forCanton;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.03.2003 11:59:22)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForGenreAffilie() {
        return forGenreAffilie;
    }

    /**
     * @return
     */
    public java.lang.String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 10:56:13)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCommunication() {
        return idCommunication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 10:57:31)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdDecision() {
        return idDecision;
    }

    /**
     * @return
     */
    public String getIdIfd() {
        return idIfd;
    }

    /**
     * @return
     */
    public java.lang.String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * Returns the periodeFiscale.
     * 
     * @return CPPeriodeFiscale
     */
    public CPPeriodeFiscale getPeriodeFiscale() {
        return periodeFiscale;
    }

    /**
     * Retourne le tiers en fonction de son id. Ne peut pas retourner null
     * 
     * @param transaction
     * @return TITiersViewBean
     * @throws Exception
     */
    public TITiersViewBean getTiers(BTransaction transaction) throws Exception {
        if (JadeStringUtil.isEmpty(entity.getIdTiers())) {
            throw new Exception("La décision " + getIdDecision() + " n'a pas d'id tiers.");
        }
        tiers = new TITiersViewBean();
        tiers.setSession(getSession());
        tiers.setIdTiers(entity.getIdTiers());
        tiers.retrieve(transaction);
        if (tiers.isNew()) {
            throw new Exception("Le tiers " + entity.getIdTiers() + " n'existe pas.");
        }
        return tiers;
    }

    public Boolean getWithAnneeEnCours() {
        return withAnneeEnCours;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        try {
            if (((entity = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                    && (!entity.isNew()) && !super.isAborted()) {
                // ---------------------------------------------------------------------
                setProgressCounter(progressCounter++);
                // ---------------------------------------------------------------------
                // construit le document pour la prochaine communication
                return true;
            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.toString());
            throw new FWIException(e);
        }
        return false;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.03.2003 11:59:56)
     * 
     * @param newAnneeDecision
     *            java.lang.String
     */
    public void setAnneeDecision(String newAnneeDecision) {
        anneeDecision = newAnneeDecision;
    }

    /**
     * @param string
     */
    public void setDateEdition(String date) {
        dateEdition = date;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.03.2003 12:01:03)
     * 
     * @param newDateEnvoi
     *            java.lang.String
     */
    public void setDateEnvoi(String newDateEnvoi) {
        dateEnvoi = newDateEnvoi;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.03.2003 12:02:42)
     * 
     * @param newDateEnvoiVide
     *            boolean
     */
    public void setDateEnvoiVide(Boolean newDateEnvoiVide) {
        dateEnvoiVide = newDateEnvoiVide;
    }

    /**
     * @param string
     */
    public void setDebutActivite(java.lang.String string) {
        debutActivite = string;
    }

    /*
     * Insertion des infos pour la publication (GED)
     */
    public void setDocumentInfo() throws FWIException {
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

        getDocumentInfo().setDocumentProperty(CADocumentInfoHelper.SECTION_ID_EXTERNE, entity.getAnneeDecision());
        getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_GENRE, entity.getGenreAffilie());
        try {
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE,
                    CPToolBox.unFormat(entity.getNumAffilie()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE, "");
        }
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE, entity.getNumAffilie());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_ID, entity.getIdTiers());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NOM, entity.getNom());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_PRENOM, entity.getPrenom());
        /* Personne Avs */
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_SEXE, entity.getSexe());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_DATE_NAISSANCE, entity.getDateNaissance());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE, entity.getNumAvs());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE,
                JadeStringUtil.removeChar(entity.getNumAvs(), '.'));
        // Bloquer envoi
        // Role bloquer envoi
        boolean bloquerEnvoi = false;
        TIRoleManager roleMng = new TIRoleManager();
        roleMng.setSession(getSession());
        roleMng.setForIdTiers(entity.getIdTiers());
        roleMng.setForRole(TIRole.CS_BLOQUER_ENVOI);
        roleMng.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
        try {
            if (roleMng.getCount() > 0) {
                bloquerEnvoi = true;
            }
            if (bloquerEnvoi) {
                getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.ROLE_BLOQUER_ENVOI, "true");
                getDocumentInfo().setRejectDocument(true);
                // getDocumentInfo().setPreventFromPublish(true);
            } else {
                getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.ROLE_BLOQUER_ENVOI, "false");
                getDocumentInfo().setRejectDocument(false);
                // getDocumentInfo().setPreventFromPublish(false);
            }
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.ROLE_BLOQUER_ENVOI, "false");
            getDocumentInfo().setRejectDocument(false);
        }
    }

    public void setEMailObject(java.lang.String newEMailObject) {
        eMailObject = newEMailObject;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 11:48:54)
     * 
     * @param newEntity
     *            globaz.phenix.db.principale.CPDecision
     */
    public void setEntity(CPCommunicationFiscaleAffichage newEntity) {
        entity = newEntity;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.03.2003 12:00:20)
     * 
     * @param newForCanton
     *            java.lang.String
     */
    public void setForCanton(java.lang.String newForCanton) {
        forCanton = newForCanton;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.03.2003 11:59:22)
     * 
     * @param newForGenreAffilie
     *            java.lang.String
     */
    public void setForGenreAffilie(java.lang.String newForGenreAffilie) {
        forGenreAffilie = newForGenreAffilie;
    }

    /**
     * @param string
     */
    public void setIdAffiliation(java.lang.String string) {
        idAffiliation = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 10:56:13)
     * 
     * @param newIdCommunication
     *            java.lang.String
     */
    public void setIdCommunication(java.lang.String newIdCommunication) {
        idCommunication = newIdCommunication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 10:57:31)
     * 
     * @param newIdDecision
     *            java.lang.String
     */
    public void setIdDecision(java.lang.String newIdDecision) {
        idDecision = newIdDecision;
    }

    /**
     * @param string
     */
    public void setIdIfd(String string) {
        idIfd = string;
    }

    /**
     * @param string
     */
    public void setNomPrenom(java.lang.String string) {
        nomPrenom = string;
    }

    /**
     * Sets the periodeFiscale.
     * 
     * @param periodeFiscale
     *            The periodeFiscale to set
     */
    public void setPeriodeFiscale(CPPeriodeFiscale periodeFiscale) {
        this.periodeFiscale = periodeFiscale;
    }

    public void setWithAnneeEnCours(Boolean withAnneeEnCours) {
        this.withAnneeEnCours = withAnneeEnCours;
    }

}
