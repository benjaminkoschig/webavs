package globaz.osiris.print.itext;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIScriptDocument;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.print.itext.list.CADocumentManager;
import globaz.osiris.translation.CACodeSystem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * Cette Classe permet d'imprimer une décision de tatxation
 * 
 * @author sel
 */
public class CATaxation_Doc extends FWIScriptDocument {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NUMERO_REFERENCE_INFOROM = "0164GCA";
    private static final String TEMPLATE_NAME = "CATaxation";
    private BigDecimal sousTotal;
    private CATaxation taxation = null;
    private Iterator taxationIt = null;
    private BigDecimal totalInter;

    /**
     * Commentaire relatif au constructeur
     */
    public CATaxation_Doc() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Commentaire relatif au constructeur
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CATaxation_Doc(BSession session) throws FWIException {
        super(session, CAApplication.DEFAULT_OSIRIS_ROOT, "DecisionTaxation");
        super.setDocumentTitle(getSession().getLabel("FILENAME_CTX_TAXATION"));
    }

    /**
     * @return java.lang.String
     */
    private String _getAdresseCourrier() {
        return taxation.getLigneAdresse1() + "\n" + taxation.getLigneAdresse2() + "\n" + taxation.getLigneAdresse3()
                + "\n" + taxation.getLigneAdresse4() + "\n" + taxation.getLigneAdresse5() + "\n"
                + taxation.getLigneAdresse6() + "\n" + taxation.getLigneAdresse7();
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la première page Utiliser super.setParametres(Key, Value)
     */
    protected void _headerText() {
        try {
            super.setParametres(FWIImportParametre.PARAM_COMPANYNAME,
                    getSession().getApplication().getLabel("ADR_CAISSE", taxation.getTiers().getLangueISO()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Après la création de tous les documents Par defaut ne fait rien
     */
    @Override
    public void afterExecuteReport() {
        if (!taxation.isModePrevisionnel().booleanValue()) {
            try {
                JadePublishDocumentInfo info = createDocumentInfo();
                // Envoie un e-mail avec les pdf fusionnés
                info.setPublishDocument(true);
                info.setArchiveDocument(false);
                this.mergePDF(info, false, 500, false, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        getDocumentInfo().setDocumentTypeNumber(CATaxation_Doc.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     */
    @Override
    public void beforeExecuteReport() {
        setTemplateFile(CATaxation_Doc.TEMPLATE_NAME);
        setImpressionParLot(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforePrintDocument ()
     */
    @Override
    public boolean beforePrintDocument() {
        if (!taxation.isModePrevisionnel().booleanValue()) {
            // On sette la date du document
            getDocumentInfo().setDocumentDate(taxation.getDateDocument());
            // On dit qu'au début on ne veut pas envoyer un mail avec le
            // document
            getDocumentInfo().setPublishDocument(false);
            try {
                getSession().setApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            } catch (Exception e2) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("ERROR_SETTING_APPLICATION"));
            }

            try {
                CAApplication app = (CAApplication) GlobazServer.getCurrentSystem().getApplication(
                        CAApplication.DEFAULT_APPLICATION_OSIRIS);
                IFormatData affilieFormater = app.getAffileFormater();
                // On rempli le documentInfo avec les infos du document
                TIDocumentInfoHelper.fill(getDocumentInfo(), taxation.getSection().getCompteAnnexe().getIdTiers(),
                        getSession(), taxation.getSection().getCompteAnnexe().getIdRole(), taxation.getSection()
                                .getCompteAnnexe().getIdExterneRole(),
                        affilieFormater.unformat(taxation.getSection().getCompteAnnexe().getIdExterneRole()));
                CADocumentInfoHelper.fill(getDocumentInfo(), taxation.getSection());
            } catch (Exception e1) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("ERROR_UNFORMATING_NUM_AFFILIE"));
            }
            try {
                getDocumentInfo().setDocumentProperty("babel.type.id", "CTX");
                getDocumentInfo().setDocumentType(CACodeSystem.CS_TYPE_TAXATION);
                // On dit qu'on veut archiver le document dans la GED
                getDocumentInfo().setArchiveDocument(true);
                getDocumentInfo().setPublishDocument(false);
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                        getSession().getLabel("GED_ERROR_GETTING_TIERS_INFO"));
            }
        }
        return ((super.size() > 0) && !super.isAborted());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIDocument#bindData(String)
     */
    @Override
    public void bindData(String arg0) throws Exception {
    }

    /**
     * @param data
     *            java.lang.Object
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    public void bindObject(Object data) throws java.lang.Exception {
        taxationIt = ((Vector) data).iterator();
    }

    /**
     * Methode appelé pour la création des valeurs pour le document 1) addRow (si nécessaire) 2) Appèle des méthodes
     * pour la création des paramètres
     */
    @Override
    public void createDataSource() {
        // Initialisation des totaux
        totalInter = new BigDecimal(0);
        sousTotal = new BigDecimal(0);

        try {
            taxation = (CATaxation) taxationIt.next();
            if (taxation.isModePrevisionnel().booleanValue()) {
                setTailleLot(500);
            } else {
                setTailleLot(1);
            }

            initHeader();
            // Labels
            super.setParametres(CATaxationParam.P_CONCERNE, taxation.getConcerne());
            super.setParametres(CATaxationParam.P_TEXTE_DEBUT, taxation.getTexteDebut());
            super.setParametres(CATaxationParam.L_PARITAIRE_AVS, taxation.getTexteCorps(4, 1));
            super.setParametres(CATaxationParam.L_PARITAIRE_AC, taxation.getTexteCorps(4, 2));
            super.setParametres(CATaxationParam.L_PARITAIRE_MATERNITE, taxation.getTexteCorps(4, 3));
            super.setParametres(CATaxationParam.L_TAXE_SOMMATION, taxation.getTexteCorps(4, 4));
            super.setParametres(CATaxationParam.L_MONTANT_INTER, taxation.getTexteCorps(4, 5));
            super.setParametres(CATaxationParam.L_ACOMPTE_VERSE, taxation.getTexteCorps(4, 6));
            super.setParametres(CATaxationParam.L_COMPENSATION_APG, taxation.getTexteCorps(4, 7));
            super.setParametres(CATaxationParam.L_MONTANT_TOTAL, taxation.getTexteCorps(4, 8));

            super.setParametres(CATaxationParam.P_DEVISE, taxation.getTexteCorps(9, 90));
            super.setParametres(CATaxationParam.P_PARITAIRE_AVS,
                    CADocumentManager.formatMontant(getMontantAvs().toString()));
            super.setParametres(CATaxationParam.P_PARITAIRE_AC,
                    CADocumentManager.formatMontant(getMontantAc().toString()));
            super.setParametres(CATaxationParam.P_PARITAIRE_MATERNITE,
                    CADocumentManager.formatMontant(getMontantMaternite().toString()));
            super.setParametres(CATaxationParam.P_TAXE_SOMMATION, CADocumentManager.formatMontant(taxation.getTaxe()));
            super.setParametres(CATaxationParam.P_ACOMPTE_VERSE,
                    CADocumentManager.formatMontant(getAcompteVerse().toString()));
            super.setParametres(CATaxationParam.P_COMPENSATION_APG,
                    CADocumentManager.formatMontant(getCompensation().toString()));
            totalInter = totalInter.add(new BigDecimal(taxation.getTaxe()));
            super.setParametres(CATaxationParam.P_MONTANT_INTER, CADocumentManager.formatMontant(totalInter.toString()));
            super.setParametres(CATaxationParam.P_SOUS_TOTAL, CADocumentManager.formatMontant(sousTotal.toString()));
            super.setParametres(CATaxationParam.P_MONTANT_TOTAL,
                    CADocumentManager.formatMontant(totalInter.add(sousTotal).toString()));

            super.setParametres(CATaxationParam.P_TEXTE_FIN, taxation.getTexteFin());
            super.setParametres(CATaxationParam.P_SIGNATURE, taxation.getSignature());

        } catch (Exception e) {
            e.printStackTrace();
        }
        _headerText();
    }

    /**
     * Donne le montant totale des acomptes versés (xxxx.10xx.xxxx)
     * 
     * @return
     * @throws Exception
     */
    private BigDecimal getAcompteVerse() throws Exception {
        BigDecimal montant = new BigDecimal(0);
        CAOperationManager operationM = new CAOperationManager();
        operationM.setSession(getSession());
        operationM.setForEtat(APIOperation.ETAT_COMPTABILISE);
        operationM.setForIdSection(taxation.getSection().getIdSection());
        ArrayList forRubriqueIn = new ArrayList();
        forRubriqueIn.add("____.10");
        operationM.setForRubriqueIn(forRubriqueIn);
        montant = montant.add(operationM.getSum(CAOperation.FIELD_MONTANT));
        sousTotal = sousTotal.add(montant);
        return montant;

    }

    /**
     * Donne le montant totale des compensations/apg (xxxx.11xx.xxxx, xxxx.9996.xxxx, xxxx.9997.xxxx et xxxx.2112.xxxx)
     * 
     * @return
     * @throws Exception
     */
    private BigDecimal getCompensation() throws Exception {
        BigDecimal montant = new BigDecimal(0);
        CAOperationManager operationM = new CAOperationManager();
        operationM.setSession(getSession());
        operationM.setForEtat(APIOperation.ETAT_COMPTABILISE);
        operationM.setForIdSection(taxation.getSection().getIdSection());
        ArrayList forRubriqueIn = new ArrayList();
        forRubriqueIn.add("____.11");
        forRubriqueIn.add("____.9996");
        forRubriqueIn.add("____.9997");
        forRubriqueIn.add("____.2112");
        operationM.setForRubriqueIn(forRubriqueIn);
        montant = montant.add(operationM.getSum(CAOperation.FIELD_MONTANT));
        sousTotal = sousTotal.add(montant);
        return montant;
    }

    /**
     * Donne le montant totale des rubriques de l'AC (2160.4030.xxxx)
     * 
     * @return
     * @throws Exception
     */
    private BigDecimal getMontantAc() throws Exception {
        BigDecimal montant = new BigDecimal(0);
        CAOperationManager operationM = new CAOperationManager();
        operationM.setSession(getSession());
        operationM.setForEtat(APIOperation.ETAT_COMPTABILISE);
        operationM.setForIdSection(taxation.getSection().getIdSection());
        ArrayList forRubriqueIn = new ArrayList();
        forRubriqueIn
                .add((new StringBuffer(APIRubrique.ID_EXTERNE_BEGIN_WITH_2160_4030).replace(3, 4, "_")).toString());
        operationM.setForRubriqueIn(forRubriqueIn);
        montant = montant.add(operationM.getSum(CAOperation.FIELD_MONTANT));
        totalInter = totalInter.add(montant);
        return montant;
    }

    /**
     * Donne le montant totale des rubriques de l'AVS (2110.4000.xxxx et 2110.4010.xxxx)
     * 
     * @return
     * @throws Exception
     */
    private BigDecimal getMontantAvs() throws Exception {
        BigDecimal montant = new BigDecimal(0);
        CAOperationManager operationM = new CAOperationManager();
        operationM.setSession(getSession());
        operationM.setForEtat(APIOperation.ETAT_COMPTABILISE);
        operationM.setForIdSection(taxation.getSection().getIdSection());
        ArrayList forRubriqueIn = new ArrayList();
        forRubriqueIn
                .add((new StringBuffer(APIRubrique.ID_EXTERNE_BEGIN_WITH_2110_4000).replace(3, 4, "_")).toString());
        forRubriqueIn
                .add((new StringBuffer(APIRubrique.ID_EXTERNE_BEGIN_WITH_2110_4010).replace(3, 4, "_")).toString());
        operationM.setForRubriqueIn(forRubriqueIn);
        montant = montant.add(operationM.getSum(CAOperation.FIELD_MONTANT));
        totalInter = totalInter.add(montant);
        return montant;
    }

    /**
     * Donne le montant totale des rubriques de la maternite (7750.4030.xxxx)
     * 
     * @return
     * @throws Exception
     */
    private BigDecimal getMontantMaternite() throws Exception {
        BigDecimal montant = new BigDecimal(0);
        CAOperationManager operationM = new CAOperationManager();
        operationM.setSession(getSession());
        operationM.setForEtat(APIOperation.ETAT_COMPTABILISE);
        operationM.setForIdSection(taxation.getSection().getIdSection());
        ArrayList forRubriqueIn = new ArrayList();
        forRubriqueIn
                .add((new StringBuffer(APIRubrique.ID_EXTERNE_BEGIN_WITH_7750_4030).replace(3, 4, "_")).toString());
        operationM.setForRubriqueIn(forRubriqueIn);
        montant = montant.add(operationM.getSum(CAOperation.FIELD_MONTANT));
        totalInter = totalInter.add(montant);
        return montant;
    }

    /**
     * Initialise l'entete du document
     * 
     * @throws Exception
     */
    private void initHeader() throws Exception {
        // Header
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), taxation.getTiers().getLangueISO());
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        String adresse = "";
        if (!JadeStringUtil.isBlank(taxation.getSection().getTypeAdresse())
                && !JadeStringUtil.isBlank(taxation.getSection().getDomaine())) {
            adresse = taxation.getTiers().getAdresseAsString(getDocumentInfo(), taxation.getSection().getTypeAdresse(),
                    taxation.getSection().getDomaine(), taxation.getSection().getCompteAnnexe().getIdExterneRole(),
                    taxation.getDate());
        } else {
            adresse = _getAdresseCourrier();
        }
        headerBean.setAdresse(adresse);
        headerBean.setDate(JACalendar.format(taxation.getDate(), taxation.getTiers().getLangueISO()));

        if (CAApplication.getApplicationOsiris().getCAParametres().isConfidentiel()) {
            headerBean.setConfidentiel(true);
        }
        headerBean.setRecommandee(true);
        // headerBean.setNomCollaborateur(_getResponsable());
        caisseReportHelper.addHeaderParameters(this, headerBean);
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
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
        return taxationIt.hasNext();
    }

    /**
     * Méthode appelé pour lancer l'exportation du document Par défaut ne pas utiliser car déjà implémenté par la
     * superClass Utile si on ne veut pas exporter en fichier temporaire
     */
    @Override
    public void returnDocument() throws FWIException {
        super.imprimerListDocument();
    }
}
