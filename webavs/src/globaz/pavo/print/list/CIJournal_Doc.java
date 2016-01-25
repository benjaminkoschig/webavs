package globaz.pavo.print.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.process.FWProcess;
import globaz.framework.translation.FWTranslation;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIEcritureSommeJournal;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.translation.CodeSystem;
import globaz.pavo.util.CIGeneric_Bean;
import globaz.pavo.util.CIGeneric_BeanComparator;
import globaz.pavo.util.CIPavoContainer;
import globaz.pavo.util.CIUtil;
import globaz.webavs.common.CommonExcelmlUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Insert the type's description here. Creation date: (09.07.2003 17:00:18)
 * 
 * @author: Administrator
 */
public class CIJournal_Doc extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String XLS_DOC_NAME = "Journal";
    private Boolean avecEcrituresNegatives = new Boolean("false");
    private Boolean avecSousTotal = new Boolean(false);
    private java.lang.String detail;
    private CIJournal_DS ds = null;
    private Boolean ecrituresSalariees = new Boolean(false);
    private boolean forAffilieParitaire = false;
    private boolean forAffiliePersonnel = false;
    private String forCode;
    private String forGenreEcrituresAParser = "";
    private java.lang.String forIdTypeCompte;
    private java.lang.String forIdTypeJournal;
    private String forIdTypeJournalMultiple = "";
    private java.lang.String forNomEspion;
    private java.lang.String forNumeroAffilie;
    private java.lang.String fromAnnee;
    private java.lang.String fromDateInscription;
    private java.lang.String fromIdJournal;
    private java.lang.String fromNumeroAvs;
    private boolean idJournalInit = false;
    private Boolean imprimerSousTotal = new Boolean(false);
    private Boolean imprimerTitre = new Boolean(false);
    private String infoAffilie = "";
    private boolean isFirst = true;
    private List listBeanDocument = new ArrayList();
    private java.lang.Boolean listeDetaillee;
    private String mailAddress = "";
    private java.lang.String recapitulation;
    private java.lang.String tri;
    private String typeImpression = "pdf";
    private Boolean UnMailPasAnnee = new Boolean(false);
    private java.lang.String untilAnnee;
    private java.lang.String untilDateInscription;
    private java.lang.String untilIdJournal;
    private java.lang.String untilNumeroAvs;
    private String userCompta = "";
    private String userVisa = "";

    private CIPavoContainer xlsContainer = new CIPavoContainer();

    public CIJournal_Doc() throws Exception {
        this(new BSession(CIApplication.DEFAULT_APPLICATION_PAVO));
    }

    /**
     * CIJournal_Doc constructor comment.
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @param filenameRoot
     *            java.lang.String
     * @param companyName
     *            java.lang.String
     * @param documentTitle
     *            java.lang.String
     * @param source
     *            net.sf.jasperreports.engine.JRDataSource
     */
    public CIJournal_Doc(FWProcess parent) throws Exception {
        super(parent, CIApplication.APPLICATION_PAVO_REP, parent.getSession().getLabel("IMPRESSION_IK_JOURNAL"));
        init();
    }

    /**
     * CIJournal_Doc constructor comment.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */

    public CIJournal_Doc(globaz.globall.db.BSession session) throws Exception {
        super(session, CIApplication.APPLICATION_PAVO_REP, session.getLabel("IMPRESSION_IK_JOURNAL"));
        init();
    }

    @Override
    protected void _validate() throws Exception {

        /*
         * FWSecureUsers users = new FWSecureUsers(); users.setSession(getSession());
         * users.setForUser(this.getForNomEspion()); users.find(getTransaction()); if (users==null || users.size()<=0) {
         * _addError(getTransaction(), getSession().getLabel("MSG_IMPRIMER_JOURNAL_USER_INEXISTANT")); abort(); }
         */
        if (JadeStringUtil.isEmpty(getMailAddress())) {
            this._addError(getTransaction(), getSession().getLabel("MSG_COMPTA_JOURNAL_EMAIL"));
            // abort();
        } else {
            setEMailAddress(getMailAddress());
        }

        // Recherche d'un seul numéro de journal. -> contrôle de son existance
        if ((getFromIdJournal() != null) && (getFromIdJournal().trim().length() > 0) && (getUntilIdJournal() != null)
                && (getUntilIdJournal().trim().length() > 0) && getFromIdJournal().equals(getUntilIdJournal())) {

            CIJournal journal = new CIJournal();
            journal.setSession(getSession());
            journal.setIdJournal(getFromIdJournal());
            journal.retrieve(getTransaction());
            if ((journal == null) || journal.isNew()) {
                this._addError(getTransaction(), getSession().getLabel("MSG_IMPRIMER_JOURNAL_INEXISTANT"));
                // abort();
            }
        }
        if (!JadeStringUtil.isEmpty(getForGenreEcrituresAParser())) {
            if (getForGenreEcrituresAParser().length() == 1) {
                this._addError(getTransaction(), getSession().getLabel("MSG_ECRITURE_GRE"));
                // abort();

            }
        }
        // Vérifie que tous les champs de-à ne soient pas contradictoires (
        // valeur de début supérieure à la valeur de fin)
        // Champ journal
        if ((getFromIdJournal().length() != 0) && (getUntilIdJournal().length() != 0)
                && (Long.parseLong(getFromIdJournal()) > Long.parseLong(getUntilIdJournal()))) {
            this._addError(getTransaction(), getSession().getLabel("MSG_CRITERES_DE-A_NON_VALIDE"));
            // abort();
        }
        // Champ Année de cotisation
        if ((getFromAnnee().length() != 0) && (getUntilAnnee().length() != 0)
                && (Integer.parseInt(getFromAnnee()) > Integer.parseInt(getUntilAnnee()))) {
            this._addError(getTransaction(), getSession().getLabel("MSG_CRITERES_DE-A_NON_VALIDE"));
            // abort();
        }
        // Champ date d'inscription
        if ((getFromDateInscription().length() != 0)
                && (getUntilDateInscription().length() != 0)
                && BSessionUtil.compareDateFirstGreater(getSession(), getFromDateInscription(),
                        getUntilDateInscription())) {
            this._addError(getTransaction(), getSession().getLabel("MSG_CRITERES_DE-A_NON_VALIDE"));
            // abort();
        }
        // Champ numéro AVS
        if ((getFromNumeroAvs().length() != 0)
                && (getUntilNumeroAvs().length() != 0)
                && (Long.parseLong(CIUtil.unFormatAVS(getFromNumeroAvs())) > Long.parseLong(CIUtil
                        .unFormatAVS(getUntilNumeroAvs())))) {
            this._addError(getTransaction(), getSession().getLabel("MSG_CRITERES_DE-A_NON_VALIDE"));
            // abort();
        }
        if (!getSession().hasErrors()) {
            setControleTransaction(true);
            setSendCompletionMail(true);
            setSendMailOnError(true);
        }
    }

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        try {
            // Assignation template
            getDocumentInfo().setDocumentTypeNumber("0061CCI");
            setTemplateFile("PAVO_Journal");
            getExporter().setExportFileName(getSession().getLabel("IMPRESSION_IK_JOURNAL"));
            // Assignation des paramètres
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_TITRE,
                    getSession().getLabel("IMPRESSION_JOURNAL_TITRE"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_JOURNAL,
                    getSession().getLabel("IMPRESSION_JOURNAL_JOURNAL"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_COMPTE_INDIVIDUEL,
                    getSession().getLabel("IMPRESSION_JOURNAL_COMPTE_INDIVIDUEL"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_DATE_INSCRIPTION,
                    getSession().getLabel("IMPRESSION_JOURNAL_DATE_INSCRIPTION"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_ANNEE_COTISATION,
                    getSession().getLabel("IMPRESSION_JOURNAL_ANNEE_COTISATION"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_COMPTE,
                    getSession().getLabel("IMPRESSION_JOURNAL_COMPTE"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COL1,
                    getSession().getLabel("IMPRESSION_JOURNAL_COL1"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COL2,
                    getSession().getLabel("IMPRESSION_JOURNAL_COL2"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COL3,
                    getSession().getLabel("IMPRESSION_JOURNAL_COL3"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COL4,
                    getSession().getLabel("IMPRESSION_JOURNAL_COL4"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COL5,
                    getSession().getLabel("IMPRESSION_JOURNAL_COL5"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COL6,
                    getSession().getLabel("IMPRESSION_JOURNAL_COL6"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COL7,
                    getSession().getLabel("IMPRESSION_JOURNAL_COL7"));
            // super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COL8,
            // getSession().getLabel("IMPRESSION_JOURNAL_COL8"));
            // super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COL9,
            // getSession().getLabel("IMPRESSION_JOURNAL_COL9"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COL10,
                    getSession().getLabel("IMPRESSION_JOURNAL_COL10"));
            // super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COL11,
            // getSession().getLabel("IMPRESSION_JOURNAL_COL11"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COL12,
                    getSession().getLabel("IMPRESSION_JOURNAL_COL12"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COL13,
                    getSession().getLabel("IMPRESSION_JOURNAL_COL13"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COL14,
                    getSession().getLabel("IMPRESSION_JOURNAL_USER"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_RECAP_COL1,
                    getSession().getLabel("IMPRESSION_JOURNAL_RECAP_COL1"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_RECAP_COL2,
                    getSession().getLabel("IMPRESSION_JOURNAL_RECAP_COL2"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_RECAP_COL3,
                    getSession().getLabel("IMPRESSION_JOURNAL_RECAP_COL3"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_RECAP_COL4,
                    getSession().getLabel("IMPRESSION_JOURNAL_RECAP_COL4"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_RECAP_TOTAL,
                    getSession().getLabel("IMPRESSION_JOURNAL_RECAP_TOTAL"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_TOTAL,
                    getSession().getLabel("IMPRESSION_JOURNAL_TOTAL"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_TOTAL_INSCRIT,
                    getSession().getLabel("IMPRESSION_JOURNAL_TOTAL_INSCRIT"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_DIFFERENCE,
                    getSession().getLabel("IMPRESSION_JOURNAL_DIFFERENCE"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_USER,
                    getSession().getLabel("IMPRESSION_JOURNAL_USER"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_NUMERO_AFFILIE,
                    getSession().getLabel("IMPRESSION_JOURNAL_NUMERO_AFFILIE"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_TYPE_JOURNAL,
                    getSession().getLabel("IMPRESSION_JOURNAL_TYPE"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_CODE,
                    getSession().getLabel("IMPRESSION_JOURNAL_CODE"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_ECRITURES_NEGATIVES,
                    getSession().getLabel("IMPRESSION_JOURNAL_ECRITURES_NEGATIVES"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_CHIFFRE_CLE,
                    getSession().getLabel("IMPRESSION_JOURNAL_CHIFFRE_CLE"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_TRI,
                    getSession().getLabel("IMPRESSION_JOURNAL_TRI"));
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_UTILISATEUR_VISA, getUserVisa());
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_LABEL_PAGE,
                    getSession().getLabel("IMPRESSION_JOURNAL_PAGE"));
            super.setParametres(
                    CIJournal_ParameterList.PARAM_JOURNAL_LABEL_COMPANYNAME,
                    getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_NOM_CAISSE
                            + getSession().getIdLangueISO().toUpperCase()));
            super.setParametres(CIJournal_ParameterList.PARAM_INFO_AFFILIE, getInfoAffilie());
            // Impression titres
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_IMPRIMER_TITRES, imprimerTitre);

            if ("on".equals(getRecapitulation())) {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_RECAPITULATION, new Boolean(true));
            } else {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_RECAPITULATION, new Boolean(false));
            }
            if ("on".equals(getDetail())) {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_DETAIL, new Boolean(true));
            } else {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_DETAIL, new Boolean(false));
            }

            // Info numéro d'affilié
            if (!JadeStringUtil.isEmpty(getForNumeroAffilie())) {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_NUMERO_AFFILIE, getForNumeroAffilie());
            } else {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_NUMERO_AFFILIE, "*");
            }

            // Info Type du journal
            if (!JadeStringUtil.isEmpty(getForIdTypeJournalMultiple())) {
                // Recherche de tous les codes systeme
                FWParametersSystemCodeManager csTypeJournalManager = new FWParametersSystemCodeManager();
                csTypeJournalManager.setForIdGroupe("CITYPINS");
                csTypeJournalManager.setForIdTypeCode("10300001");
                csTypeJournalManager.setSession(getSession());
                csTypeJournalManager.find();

                StringTokenizer token = new StringTokenizer(getForIdTypeJournalMultiple(), ",");
                String libelles = "";

                while (token.hasMoreTokens()) {
                    FWParametersSystemCode cs = new FWParametersSystemCode();
                    cs = csTypeJournalManager.getCodeSysteme(token.nextToken());
                    libelles += cs.getCurrentCodeUtilisateur().getLibelle() + "\n";
                }
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_TYPE_JOURNAL, libelles);

            } else {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_TYPE_JOURNAL, "*");
            }

            // Info user
            if (!JadeStringUtil.isEmpty(getForNomEspion())) {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_USER, getForNomEspion());
            } else {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_USER, "*");
            }
            // Info Code
            if (!JadeStringUtil.isEmpty(getForCode())) {
                FWParametersSystemCodeManager csCodeManager = new FWParametersSystemCodeManager();
                csCodeManager.setForIdGroupe("CICODAMO");
                csCodeManager.setForIdTypeCode("10300013");
                csCodeManager.setSession(getSession());
                csCodeManager.find();
                FWParametersSystemCode cs = csCodeManager.getCodeSysteme(forCode);
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_CODE, cs.getCurrentCodeUtilisateur()
                        .getLibelle());
            } else {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_CODE, "*");
            }

            // Info Ecritures négatives
            if (avecEcrituresNegatives.booleanValue()) {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_ECRITURES_NEGATIVES,
                        getSession().getLabel("OUI"));
            } else {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_ECRITURES_NEGATIVES,
                        getSession().getLabel("NON"));
            }

            // Info Chiffre clé
            if (!JadeStringUtil.isEmpty(getForGenreEcrituresAParser())) {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_CHIFFRE_CLE, getForGenreEcrituresAParser());
            } else {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_CHIFFRE_CLE, "*");
            }
            // Info Tri
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_TRI, getTri());
            if ("nomPrenom".equals(getTri())) {
                if (getAvecSousTotal().booleanValue()) {
                    setImprimerSousTotal(new Boolean(true));
                }
            }
            if ("avs".equals(getTri())) {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_TRI, getSession().getLabel("DT_DETAIL_COL1"));
            }
            if ("date".equals(getTri())) {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_TRI,
                        getSession().getLabel("IMPRESSION_JOURNAL_DATE_INSCRIPTION"));
            }
            if ("numAff".equals(getTri())) {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_TRI,
                        getSession().getLabel("IMPRESSION_JOURNAL_NUMERO_AFFILIE"));
            }
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_IMPRIMER_SOUS_TOTAUX, getImprimerSousTotal());
            // Infos de caisse
            globaz.pavo.application.CIApplication applic = (globaz.pavo.application.CIApplication) getSession()
                    .getApplication();
            globaz.pyxis.api.ITIAdministration adm = applic.getAdministrationLocale(getSession());

            // Description année de cotisation
            if ((!JadeStringUtil.isEmpty(fromAnnee)) && (!JadeStringUtil.isEmpty(untilAnnee))) {
                // Si égaux
                if (fromAnnee.equals(untilAnnee)) {
                    super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_ANNEE_COTISATION, fromAnnee);
                } else {
                    super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_ANNEE_COTISATION, fromAnnee + " - "
                            + untilAnnee);
                }
            }
            // Seule from
            else if (!JadeStringUtil.isEmpty(fromAnnee)) {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_ANNEE_COTISATION, fromAnnee);
            } else if (!JadeStringUtil.isEmpty(untilAnnee)) {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_ANNEE_COTISATION, untilAnnee);
            } else {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_ANNEE_COTISATION, "*");
            }

            // Description date d'inscription
            if ((!JadeStringUtil.isEmpty(fromDateInscription)) && (!JadeStringUtil.isEmpty(untilDateInscription))) {
                // Si égaux
                if (fromDateInscription.equals(untilDateInscription)) {
                    super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_DATE_INSCRIPTION, fromDateInscription);
                } else {
                    super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_DATE_INSCRIPTION, fromDateInscription
                            + " - " + untilDateInscription);
                }
            }
            // Seule from
            else if (!JadeStringUtil.isEmpty(fromDateInscription)) {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_DATE_INSCRIPTION, fromDateInscription);
            } else if (!JadeStringUtil.isEmpty(untilDateInscription)) {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_DATE_INSCRIPTION, untilDateInscription);
            } else {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_DATE_INSCRIPTION, "*");
            }

            // Description numéro AVS
            if ((!JadeStringUtil.isEmpty(fromNumeroAvs)) && (!JadeStringUtil.isEmpty(untilNumeroAvs))) {
                // Récupération compte individuel
                globaz.pavo.db.compte.CICompteIndividuelManager compteIndivManager = new globaz.pavo.db.compte.CICompteIndividuelManager();
                compteIndivManager.setSession(getSession());
                compteIndivManager.setForNumeroAvs(fromNumeroAvs);
                compteIndivManager.find();
                if (compteIndivManager.size() > 0) {
                    globaz.pavo.db.compte.CICompteIndividuel compteIndiv = (globaz.pavo.db.compte.CICompteIndividuel) compteIndivManager
                            .getEntity(0);
                    // Si égaux
                    if (fromNumeroAvs.equals(untilNumeroAvs)) {
                        super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COMPTE_INDIVIDUEL,
                                NSUtil.formatAVSUnknown(fromNumeroAvs) + " " + compteIndiv.getNomPrenom());
                    } else {
                        super.setParametres(
                                CIJournal_ParameterList.PARAM_JOURNAL_COMPTE_INDIVIDUEL,
                                NSUtil.formatAVSUnknown(fromNumeroAvs) + " - "
                                        + NSUtil.formatAVSUnknown(untilNumeroAvs));
                    }
                } else
                // Pas de compte trouvé
                // Si égaux
                if (fromNumeroAvs.equals(untilNumeroAvs)) {
                    super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COMPTE_INDIVIDUEL,
                            NSUtil.formatAVSUnknown(fromNumeroAvs));
                } else {
                    super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COMPTE_INDIVIDUEL,
                            NSUtil.formatAVSUnknown(fromNumeroAvs) + " - " + NSUtil.formatAVSUnknown(untilNumeroAvs));
                }
            }
            // Seule from
            else if (!JadeStringUtil.isEmpty(fromNumeroAvs)) {
                // Récupération compte individuel
                globaz.pavo.db.compte.CICompteIndividuelManager compteIndivManager = new globaz.pavo.db.compte.CICompteIndividuelManager();
                compteIndivManager.setSession(getSession());
                compteIndivManager.setForNumeroAvs(fromNumeroAvs);
                compteIndivManager.find();
                if (compteIndivManager.size() > 0) {
                    globaz.pavo.db.compte.CICompteIndividuel compteIndiv = (globaz.pavo.db.compte.CICompteIndividuel) compteIndivManager
                            .getEntity(0);
                    super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COMPTE_INDIVIDUEL,
                            NSUtil.formatAVSUnknown(fromNumeroAvs) + " " + compteIndiv.getNomPrenom());
                } else {
                    // Pas de compte
                    super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COMPTE_INDIVIDUEL,
                            NSUtil.formatAVSUnknown(fromNumeroAvs));
                }
            }
            // Seule to
            else if (!JadeStringUtil.isEmpty(untilNumeroAvs)) {
                // Récupération compte individuel
                globaz.pavo.db.compte.CICompteIndividuelManager compteIndivManager = new globaz.pavo.db.compte.CICompteIndividuelManager();
                compteIndivManager.setSession(getSession());
                compteIndivManager.setForNumeroAvs(fromNumeroAvs);
                compteIndivManager.find();
                if (compteIndivManager.size() > 0) {
                    globaz.pavo.db.compte.CICompteIndividuel compteIndiv = (globaz.pavo.db.compte.CICompteIndividuel) compteIndivManager
                            .getEntity(0);
                    super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COMPTE_INDIVIDUEL,
                            NSUtil.formatAVSUnknown(untilNumeroAvs) + " " + compteIndiv.getNomPrenom());
                } else {
                    // Pas de compte
                    super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COMPTE_INDIVIDUEL,
                            NSUtil.formatAVSUnknown(untilNumeroAvs));
                }
            } else {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COMPTE_INDIVIDUEL, "*");
            }

            // Description journeaux
            String libelleJournal = "";
            if ((!JadeStringUtil.isEmpty(fromIdJournal)) && (!JadeStringUtil.isEmpty(untilIdJournal))) {
                // Récupéraion du journal
                globaz.pavo.db.inscriptions.CIJournal journalFrom = new globaz.pavo.db.inscriptions.CIJournal();
                journalFrom.setSession(getSession());
                journalFrom.setIdJournal(fromIdJournal);
                journalFrom.retrieve();
                if (!journalFrom.isNew()) {
                    // Si égaux
                    if (fromIdJournal.equals(untilIdJournal)) {
                        libelleJournal = fromIdJournal + " " + journalFrom.getDescription();
                    } else {
                        libelleJournal = fromIdJournal + " - " + untilIdJournal;
                    }
                } else
                // Pas de journal trouvé
                // Si égaux
                if (fromIdJournal.equals(untilIdJournal)) {
                    libelleJournal = fromIdJournal;
                } else {
                    libelleJournal = fromIdJournal + " - " + untilIdJournal;
                }
            }
            // Seule from
            else if (!JadeStringUtil.isEmpty(fromIdJournal)) {
                // Récupéraion du journal
                globaz.pavo.db.inscriptions.CIJournal journalFrom = new globaz.pavo.db.inscriptions.CIJournal();
                journalFrom.setSession(getSession());
                journalFrom.setIdJournal(fromIdJournal);
                journalFrom.retrieve();
                if (!journalFrom.isNew()) {
                    libelleJournal = fromIdJournal + " " + journalFrom.getDescription();
                } else {
                    // Pas de journal trouvé
                    libelleJournal = fromIdJournal;
                }
            }
            // Seule to
            else if (!JadeStringUtil.isEmpty(untilIdJournal)) {
                // Récupéraion du journal
                globaz.pavo.db.inscriptions.CIJournal journalTo = new globaz.pavo.db.inscriptions.CIJournal();
                journalTo.setSession(getSession());
                journalTo.setIdJournal(untilIdJournal);
                journalTo.retrieve();
                if (!journalTo.isNew()) {
                    libelleJournal = untilIdJournal + " " + journalTo.getDescription();
                } else {
                    // Pas de journal trouvé
                    libelleJournal = untilIdJournal;
                }
            } else {
                libelleJournal = "*";
            }
            if (!JadeStringUtil.isEmpty(forIdTypeJournal)) {
                if (libelleJournal.length() != 0) {
                    libelleJournal += ", ";
                }
                libelleJournal += CodeSystem.getLibelle(forIdTypeJournal, getSession());
            }
            super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_JOURNAL, libelleJournal);
            // Description type de compte
            if (!JadeStringUtil.isEmpty(forIdTypeCompte)) {
                try {
                    FWParametersSystemCodeManager csTypeCompteManager = new FWParametersSystemCodeManager();
                    csTypeCompteManager.setForIdGroupe("CITYPCOM");
                    csTypeCompteManager.setForIdTypeCode("10300003");
                    csTypeCompteManager.setSession(getSession());
                    csTypeCompteManager.find();
                    FWParametersSystemCode cs = csTypeCompteManager.getCodeSysteme(forIdTypeCompte);
                    super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COMPTE, cs.getCurrentCodeUtilisateur()
                            .getLibelle());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_COMPTE, "*");
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            abort();
        }
    }

    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     */
    @Override
    public void beforeExecuteReport() {
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    @Override
    public boolean beforePrintDocument() {

        // Create pdf ou xls
        if ("xls".equals(getTypeImpression())) {
            createExcel();
            return false;
        }

        return super.beforePrintDocument();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 11:53:46)
     * 
     * @param id
     *            java.lang.String
     */
    public void bindData(String fromNumeroJournalVal, String toNumeroJournalVal, String fromNumeroAvsVal,
            String toNumeroAvsVal, String fromAnneeCotisationVal, String toAnneeCotisationVal,
            String fromDateInscriptionVal, String toDateInscriptionVal, String forIdTypeCompteVal,
            String recapitulationVal, String detailVal, String triVal) throws java.lang.Exception {
        // Paramètres de génération du rapport
        setFromIdJournal(fromNumeroJournalVal);
        setUntilIdJournal(toNumeroJournalVal);
        setFromNumeroAvs(fromNumeroAvsVal);
        setUntilNumeroAvs(toNumeroAvsVal);
        setFromDateInscription(fromDateInscriptionVal);
        setUntilDateInscription(toDateInscriptionVal);
        setFromAnnee(fromAnneeCotisationVal);
        setUntilAnnee(toAnneeCotisationVal);
        setForIdTypeCompte(forIdTypeCompteVal);
        setRecapitulation(recapitulationVal);
        setDetail(detailVal);
        setTri(triVal);

        super.executeProcess();
    }

    /**
     * Methode appelé pour la création des valeurs pour le document 1) addRow (si nécessaire) 2) Appèle des méthodes
     * pour la création des paramètres
     */
    @Override
    public void createDataSource() throws Exception {

        ds = new CIJournal_DS(getSession());
        ds.setCacherEcritureProtege(1);
        ds.wantCallMethodBeforeFind(true);

        // Paramétrage de la source
        ds.setFromNumeroAvs(CIUtil.unFormatAVS(fromNumeroAvs));
        ds.setToNumeroAvs(CIUtil.unFormatAVS(untilNumeroAvs));
        ds.setEcrituresSalariees(ecrituresSalariees);
        ds.setFromDateInscription(fromDateInscription);
        ds.setToDateInscription(untilDateInscription);
        ds.setFromNumeroJournal(fromIdJournal);
        ds.setToNumeroJournal(untilIdJournal);
        ds.setFromAnneeCotisation(fromAnnee);
        ds.setToAnneeCotisation(untilAnnee);
        ds.setForIdTypeCompte(forIdTypeCompte);
        ds.setForIdTypeJournal(forIdTypeJournal);
        ds.setTri(tri);
        ds.setForNumeroAffilie(forNumeroAffilie);
        ds.setForNomEspion(forNomEspion);
        ds.setForCode(forCode);
        ds.setForItTypeJournalMultiple(forIdTypeJournalMultiple);
        ds.setForGenreEcrituresAParser(getForGenreEcrituresAParser());
        ds.setAvecEcrituresNegatives(avecEcrituresNegatives);

        super.setDataSource(ds);

        // CRéation de la source de récapitulation
        CIJournal_DS recapAllDS = (CIJournal_DS) ds.clone();
        recapAllDS.setSession(getSession());
        CIJournalRecapSource recapDS = new CIJournalRecapSource();
        ArrayList table = new ArrayList(10);
        // Initialisation
        FWParametersSystemCodeManager codeSystemManager = FWTranslation.getSystemCodeList("CICODGEN", getSession());
        ArrayList buf;
        for (int i = 0; i < codeSystemManager.size(); i++) {
            FWParametersSystemCode code = (FWParametersSystemCode) codeSystemManager.getEntity(i);
            buf = new ArrayList();
            buf.add(0, code.getCurrentCodeUtilisateur().getCodeUtilisateur() + " - "
                    + code.getCurrentCodeUtilisateur().getLibelle());
            buf.add(1, new Integer(0));
            buf.add(2, new Double(0.0));
            table.add(Integer.parseInt(code.getCurrentCodeUtilisateur().getCodeUtilisateur()), buf);
        }
        // Si on imprime le journal avec son détail, la limite est fixée à 5000
        // inscriptions
        if (!JadeStringUtil.isEmpty(getDetail())) {
            if (recapAllDS.getCount(getTransaction()) > 30000) {
                abort();
                getMemoryLog().logMessage("l'impression du journal comporte plus de 30000 écritures",
                        FWMessage.INFORMATION, "traitement Journaux");
                return;
            }
            recapAllDS.find(globaz.globall.db.BManager.SIZE_NOLIMIT);
        } else {
            // Si on imprime le journal sans son détail, la limite est fixée à
            // 50000 inscriptions
            if (recapAllDS.getCount(getTransaction()) > 500000) {
                abort();
                getMemoryLog().logMessage("l'impression du journal comporte plus de 50000 écritures",
                        FWMessage.INFORMATION, "traitement Journaux");
                return;
            }
        }

        for (int ind = 0; ind <= 9; ind++) {
            recapAllDS.setForGenre(this.getIndexFromGenreEcriture(ind));
            int nombre = recapAllDS.getCount(getTransaction());
            if (nombre > 0) {
                buf = buf = (ArrayList) table.get(ind);
                buf.set(1, new Integer(nombre));
                CIEcritureSommeJournal somme = new CIEcritureSommeJournal();
                somme.setSession(getSession());
                somme.setFromNumeroAvs(CIUtil.unFormatAVS(fromNumeroAvs));
                somme.setToNumeroAvs(CIUtil.unFormatAVS(untilNumeroAvs));
                somme.setFromDateInscription(fromDateInscription);
                somme.setToDateInscription(untilDateInscription);
                somme.setEcrituresSalariees(ecrituresSalariees);
                somme.setFromNumeroJournal(fromIdJournal);
                somme.setToNumeroJournal(untilIdJournal);
                somme.setFromAnneeCotisation(fromAnnee);
                somme.setToAnneeCotisation(untilAnnee);
                somme.setForIdTypeCompte(forIdTypeCompte);
                somme.setForIdTypeJournal(forIdTypeJournal);
                somme.setTri(tri);
                somme.setForNumeroAffilie(forNumeroAffilie);
                somme.setForNomEspion(forNomEspion);
                somme.setForCode(forCode);
                somme.setForItTypeJournalMultiple(forIdTypeJournalMultiple);
                somme.setForGenreEcrituresAParser(getForGenreEcrituresAParser());
                somme.setAvecEcrituresNegatives(avecEcrituresNegatives);
                somme.setForGenre(this.getIndexFromGenreEcriture(ind));
                somme.setSecure("true");
                somme.changeManagerSize(BManager.SIZE_NOLIMIT);
                BigDecimal result = somme.getSum("KBMMON", getTransaction());

                buf.set(2, new Double(result.toString()));
            }
        }

        recapDS.setSource(table);
        super.setParametres(CIJournal_ParameterList.PARAM_JOURNAL_RECAP_DATASOURCE, recapDS);
    }

    public void createExcel() {
        try {

            String xmlModelPath = Jade.getInstance().getExternalModelDir() + CIApplication.APPLICATION_PAVO_REP
                    + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                    + CIJournal_Doc.XLS_DOC_NAME + "Modele.xml";

            String xlsDocPath = Jade.getInstance().getPersistenceDir()
                    + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(CIJournal_Doc.XLS_DOC_NAME + ".xml");

            ds.setSession(getSession());
            listBeanDocument = ds.getListBeans();

            // On applique un tri en fonction de la clé de tri des objets de la map
            Collections.sort(listBeanDocument, new CIGeneric_BeanComparator());

            prepareDataForXLSDoc();
            xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath, xlsContainer);

            JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
            docInfoExcel.setApplicationDomain(CIApplication.DEFAULT_APPLICATION_PAVO);
            docInfoExcel.setDocumentTitle(CIJournal_Doc.XLS_DOC_NAME);
            docInfoExcel.setPublishDocument(true);
            docInfoExcel.setArchiveDocument(false);
            docInfoExcel.setDocumentTypeNumber("0061CCI");
            this.registerAttachedDocument(docInfoExcel, xlsDocPath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("XLS Journal contruction processed");
        }
    }

    /**
     * Returns the avecEcrituresNegatives.
     * 
     * @return Boolean
     */
    public Boolean getAvecEcrituresNegatives() {
        return avecEcrituresNegatives;
    }

    public Boolean getAvecSousTotal() {
        return avecSousTotal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 09:35:44)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDetail() {
        return detail;
    }

    public Boolean getEcrituresSalariees() {
        return ecrituresSalariees;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        // return super.getEMailObject();
        CIJournal journalForm = new CIJournal();

        journalForm.setSession(getSession());
        journalForm.setIdJournal(fromIdJournal);
        try {
            journalForm.retrieve();
        } catch (Exception e) {
            // TODO Bloc catch auto-généré
            e.printStackTrace();
        }
        if (isOnError()) {
            StringBuffer buffer = new StringBuffer("Impression du journal No '");
            buffer.append(fromIdJournal);
            buffer.append(" a échoué");
            return buffer.toString();
        } else {
            if (getUnMailPasAnnee().booleanValue()) {
                StringBuffer buffer = new StringBuffer(getSession().getLabel("MSG_IMPRESSION_JOURNAL"));
                buffer.append(fromIdJournal);
                buffer.append("/");
                buffer.append(journalForm.getDescription());
                buffer.append(" ");
                buffer.append(getFromAnnee());
                return buffer.toString();
            } else {
                StringBuffer buffer = new StringBuffer(getSession().getLabel("MSG_IMPRESSION_JOURNAL"));
                buffer.append(fromIdJournal);
                buffer.append("/");
                buffer.append(journalForm.getDescription());
                return buffer.toString();
            }
        }
    }

    /**
     * Returns the forCode.
     * 
     * @return String
     */
    public String getForCode() {
        return forCode;
    }

    /**
     * Returns the forGenreEcrituresAParser.
     * 
     * @return String
     */
    public String getForGenreEcrituresAParser() {
        return forGenreEcrituresAParser;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 18:05:57)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeCompte() {
        return forIdTypeCompte;
    }

    /**
     * Returns the forIdTypeJournal.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeJournal() {
        return forIdTypeJournal;
    }

    /**
     * Returns the forIdTypeJournalMultiple.
     * 
     * @return String
     */
    public String getForIdTypeJournalMultiple() {
        return forIdTypeJournalMultiple;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 14:30:47)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNomEspion() {
        return forNomEspion;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 14:27:57)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 11:38:59)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromAnnee() {
        return fromAnnee;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:23:25)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromDateInscription() {
        return fromDateInscription;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 11:39:00)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdJournal() {
        return fromIdJournal;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:39:49)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromNumeroAvs() {
        return fromNumeroAvs;
    }

    public Boolean getImprimerSousTotal() {
        return imprimerSousTotal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.07.2003 08:58:43)
     * 
     * @return int
     * @param ecr
     *            globaz.pavo.print.list.CIExtendedEcriture
     */
    private int getIndexFromGenreEcriture(CIExtendedEcriture ecr) {
        if (ecr.getGenreEcriture().equals(globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_0)) {
            return 0;
        }
        if (ecr.getGenreEcriture().equals(globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_1)) {
            return 1;
        }
        if (ecr.getGenreEcriture().equals(globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_2)) {
            return 2;
        }
        if (ecr.getGenreEcriture().equals(globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_3)) {
            return 3;
        }
        if (ecr.getGenreEcriture().equals(globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_4)) {
            return 4;
        }
        if (ecr.getGenreEcriture().equals(globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_5)) {
            return 5;
        }
        if (ecr.getGenreEcriture().equals(globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_6)) {
            return 6;
        }
        if (ecr.getGenreEcriture().equals(globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_7)) {
            return 7;
        }
        if (ecr.getGenreEcriture().equals(globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_8)) {
            return 8;
        }
        if (ecr.getGenreEcriture().equals(globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_7)) {
            return 9;
        } else {
            return -1;
        }
    }

    private String getIndexFromGenreEcriture(int gre) {
        if (0 == gre) {
            return globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_0;
        }
        if (1 == gre) {
            return globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_1;
        }
        if (2 == gre) {
            return globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_2;
        }
        if (3 == gre) {
            return globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_3;
        }
        if (4 == gre) {
            return globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_4;
        }
        if (5 == gre) {
            return globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_5;
        }
        if (6 == gre) {
            return globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_6;
        }
        if (7 == gre) {
            return globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_7;
        }
        if (8 == gre) {
            return globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_8;
        }
        if (9 == gre) {
            return globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_9;
        } else {
            // Si le genre n'est pas compris entre 0 et 9, retour 9, ne devrait
            // pas arriver
            return globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_9;
        }
    }

    /**
     * Retourne le nom du tiers lié à l'affilié
     * 
     * @return
     */
    public String getInfoAffile() {
        String info = "";
        try {
            if (!JadeStringUtil.isBlankOrZero(forNumeroAffilie) && !JadeStringUtil.isBlankOrZero(fromAnnee)) {
                CIApplication app = (CIApplication) getSession().getApplication();
                AFAffiliation affilie = app.getAffilieByNo(getSession(), forNumeroAffilie, forAffilieParitaire,
                        forAffiliePersonnel, "1", "12", fromAnnee, "", "");
                info = affilie.getTiersNom();
            }
        } catch (Exception e) {

        }
        return info;

    }

    public String getInfoAffilie() {
        return infoAffilie;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:25:01)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getListeDetaillee() {
        return listeDetaillee;
    }

    /**
     * @return
     */
    public String getMailAddress() {
        return mailAddress;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 09:35:44)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRecapitulation() {
        return recapitulation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 09:38:36)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTri() {
        return tri;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    public Boolean getUnMailPasAnnee() {
        return UnMailPasAnnee;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 11:39:00)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilAnnee() {
        return untilAnnee;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 11:04:15)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilDateInscription() {
        return untilDateInscription;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 11:39:00)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilIdJournal() {
        return untilIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 11:04:15)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilNumeroAvs() {
        return untilNumeroAvs;
    }

    /**
     * Returns the userVisa.
     * 
     * @return String
     */
    public String getUserVisa() {
        return userVisa;
    }

    private void init() {
        idJournalInit = true;
    }

    public boolean isForAffilieParitaire() {
        return forAffilieParitaire;
    }

    public boolean isForAffiliePersonnel() {
        return forAffiliePersonnel;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        if (isFirst) {
            isFirst = false;
            return true;
        }
        return false;
    }

    private void prepareDataForXLSDoc() {

        int l = listBeanDocument.size();

        xlsContainer.addMap(super.getImporter().getParametre());

        for (int i = 0; i < l; i++) {
            CIGeneric_Bean gb = (CIGeneric_Bean) listBeanDocument.get(i);

            String content = "";

            for (int j = 0; j < gb.getColSize(""); j++) {
                if (gb.getCol(j) != null) {
                    content = gb.getCol(j).toString();
                }
                xlsContainer.addValue("COL_" + j + "_VALUE", content);
            }
        }
    }

    /**
     * Sets the avecEcrituresNegatives.
     * 
     * @param avecEcrituresNegatives
     *            The avecEcrituresNegatives to set
     */
    public void setAvecEcrituresNegatives(Boolean avecEcrituresNegatives) {
        this.avecEcrituresNegatives = avecEcrituresNegatives;
    }

    public void setAvecSousTotal(Boolean avecSousTotal) {
        this.avecSousTotal = avecSousTotal;
    }

    /**
     * Méthode appelé pour lancer l'exportation du document Par défaut ne pas utiliser car déjà implémenté par la
     * superClass Utile si on ne veut pas exporter en fichier temporaire Date de création : (17.02.2003 14:44:15)
     */
    /*
     * public void returnDocument() throws Exception { super.createOutputFile(); }
     */
    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 09:35:44)
     * 
     * @param newDetail
     *            java.lang.String
     */
    public void setDetail(java.lang.String newDetail) {
        detail = newDetail;
    }

    public void setEcrituresSalariees(Boolean ecrituresSalariees) {
        this.ecrituresSalariees = ecrituresSalariees;
    }

    public void setForAffilieParitaire(boolean forAffilieParitaire) {
        this.forAffilieParitaire = forAffilieParitaire;
    }

    public void setForAffiliePersonnel(boolean forAffiliePersonnel) {
        this.forAffiliePersonnel = forAffiliePersonnel;
    }

    /**
     * Sets the forCode.
     * 
     * @param forCode
     *            The forCode to set
     */
    public void setForCode(String forCode) {
        this.forCode = forCode;
    }

    /**
     * Sets the forGenreEcrituresAParser.
     * 
     * @param forGenreEcrituresAParser
     *            The forGenreEcrituresAParser to set
     */
    public void setForGenreEcrituresAParser(String forGenreEcrituresAParser) {
        this.forGenreEcrituresAParser = forGenreEcrituresAParser;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 18:05:57)
     * 
     * @param newForIdTypeCompte
     *            java.lang.String
     */
    public void setForIdTypeCompte(java.lang.String newForIdTypeCompte) {
        forIdTypeCompte = newForIdTypeCompte;
    }

    /**
     * Sets the forIdTypeJournal.
     * 
     * @param forIdTypeJournal
     *            The forIdTypeJournal to set
     */
    public void setForIdTypeJournal(java.lang.String forIdTypeJournal) {
        this.forIdTypeJournal = forIdTypeJournal;
    }

    /**
     * Sets the forIdTypeJournalMultiple.
     * 
     * @param forIdTypeJournalMultiple
     *            The forIdTypeJournalMultiple to set
     */
    public void setForIdTypeJournalMultiple(String forIdTypeJournalMultiple) {
        this.forIdTypeJournalMultiple = forIdTypeJournalMultiple;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 14:30:47)
     * 
     * @param newForNomEspion
     *            java.lang.String
     */
    public void setForNomEspion(java.lang.String newForNomEspion) {
        forNomEspion = newForNomEspion;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 14:27:57)
     * 
     * @param newForNumeroAffilie
     *            java.lang.String
     */
    public void setForNumeroAffilie(java.lang.String newForNumeroAffilie) {
        forNumeroAffilie = newForNumeroAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 11:38:59)
     * 
     * @param newFromAnnee
     *            java.lang.String
     */
    public void setFromAnnee(java.lang.String newFromAnnee) {
        fromAnnee = newFromAnnee;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:23:25)
     * 
     * @param newFromDateInscription
     *            java.lang.String
     */
    public void setFromDateInscription(java.lang.String newFromDateInscription) {
        fromDateInscription = newFromDateInscription;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 11:39:00)
     * 
     * @param newFromIdJournal
     *            java.lang.String
     */
    public void setFromIdJournal(java.lang.String newFromIdJournal) {
        fromIdJournal = newFromIdJournal;
        if (idJournalInit) {
            setUntilIdJournal(newFromIdJournal);
            idJournalInit = false;
        }
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:39:49)
     * 
     * @param newFromNumeroAvs
     *            java.lang.String
     */
    public void setFromNumeroAvs(java.lang.String newFromNumeroAvs) {
        fromNumeroAvs = newFromNumeroAvs;
    }

    public void setImprimerSousTotal(Boolean imprimerSousTotal) {
        this.imprimerSousTotal = imprimerSousTotal;
    }

    public void setInfoAffilie(String infoAffilie) {
        this.infoAffilie = infoAffilie;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:25:01)
     * 
     * @param newListeDetaillee
     *            java.lang.Boolean
     */
    public void setListeDetaillee(java.lang.Boolean newListeDetaillee) {
        listeDetaillee = newListeDetaillee;
    }

    /**
     * @param string
     */
    public void setMailAddress(String string) {
        mailAddress = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 09:35:44)
     * 
     * @param newRecapitulation
     *            java.lang.String
     */
    public void setRecapitulation(java.lang.String newRecapitulation) {
        recapitulation = newRecapitulation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 09:38:36)
     * 
     * @param newTri
     *            java.lang.String
     */
    public void setTri(java.lang.String newTri) {
        tri = newTri;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }

    public void setUnMailPasAnnee(Boolean unMailPasAnnee) {
        UnMailPasAnnee = unMailPasAnnee;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 11:39:00)
     * 
     * @param newUntilAnnee
     *            java.lang.String
     */
    public void setUntilAnnee(java.lang.String newUntilAnnee) {
        untilAnnee = newUntilAnnee;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 11:04:15)
     * 
     * @param newUntilDateInscription
     *            java.lang.String
     */
    public void setUntilDateInscription(java.lang.String newUntilDateInscription) {
        untilDateInscription = newUntilDateInscription;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 11:39:00)
     * 
     * @param newUntilIdJournal
     *            java.lang.String
     */
    public void setUntilIdJournal(java.lang.String newUntilIdJournal) {
        untilIdJournal = newUntilIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 11:04:15)
     * 
     * @param newUntilNumeroAvs
     *            java.lang.String
     */
    public void setUntilNumeroAvs(java.lang.String newUntilNumeroAvs) {
        untilNumeroAvs = newUntilNumeroAvs;
    }

    /**
     * Sets the userVisa.
     * 
     * @param userVisa
     *            The userVisa to set
     */
    public void setUserVisa(String userVisa) {
        this.userVisa = userVisa;
    }

}
