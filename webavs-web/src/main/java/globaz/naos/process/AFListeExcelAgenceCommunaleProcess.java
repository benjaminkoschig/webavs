package globaz.naos.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.listeAgenceCommunale.AFListeAgenceCommunale;
import globaz.naos.db.listeAgenceCommunale.AFListeAgenceCommunaleCSVFile;
import globaz.naos.db.listeAgenceCommunale.AFListeAgenceCommunaleManager;
import globaz.naos.listes.excel.AFXmlmlMappingAgenceCommunale;
import globaz.naos.listes.excel.util.AFExcelmlUtils;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.DocumentInfoNaos;
import globaz.webavs.common.CommonExcelmlContainer;

public class AFListeExcelAgenceCommunaleProcess extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Constantes
    public final static String MODEL_NAME = "agenceCommunale.xml";
    public final static String NUMERO_INFOROM = "0262CAF";

    private AFListeAgenceCommunale entityAgence = null;
    private String forDate = "";
    // Champs
    private String forIdTiersAgence = "";

    private Boolean forIdTiersAgenceVide = new Boolean(false);
    private int nCaisse = 0;

    private String nomAgenceCommunale = "";
    private String nomDoc = "";
    private String nomFileCCCVS = "";
    private String numCaisse = "";
    private Boolean wantCsv = Boolean.FALSE;

    // Constructeur
    public AFListeExcelAgenceCommunaleProcess() {
        super();
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception, Exception {

        AFListeAgenceCommunaleManager manager = new AFListeAgenceCommunaleManager();

        initManager(manager);

        if (manager.size() >= 1) {
            setProgressScaleValue(manager.size());
            numCaisse = getSession().getApplication().getProperty("noCaisseFormate");
            entityAgence = (AFListeAgenceCommunale) manager.getFirstEntity();
            nomAgenceCommunale = entityAgence.getNomAdministration1() + " " + entityAgence.getNomAdministration2();
            nCaisse = Integer.parseInt(getSession().getApplication().getProperty("noCaisse"));
            if (nCaisse != 23) {
                if (JadeStringUtil.isBlank(nomAgenceCommunale)) {
                    nomDoc = (getSession().getLabel("LISTE") + " " + getSession().getLabel("EMAIL_AFFILIE_SANS_AGENCE"));
                } else {
                    nomDoc = (getSession().getLabel("LISTE") + " " + nomAgenceCommunale);
                }
            } else {
                nomFileCCCVS = entityAgence.getCodeAdministration() + " - " + "COTCPTEV";
                nomDoc = nomFileCCCVS;
            }
            if (Boolean.FALSE.equals(getWantCsv())) {
                return createDocument(manager);
            } else {
                return createDocumentCsv(manager);
            }
        } else {
            setSendCompletionMail(false);
        }

        return false;

    }

    // Méthode
    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        // if (JadeStringUtil.isEmpty(this.getFromDateRadiation())
        // || JadeStringUtil.isEmpty(this.getToDateRadiation())
        // || (new JADate(this.getFromDateRadiation()).getYear() != new JADate(this.getToDateRadiation())
        // .getYear())) {
        // this.getSession().addError(this.getSession().getLabel("LISTE_EMPLOYEUR_RADIE_ERREUR_DATE_RADIATION"));
        // }

        /**
         * sécurité supplémentaire ------------------------ mais on ne devrait pas se retrouver avec une adresse email
         * vide en effet, si l'email n'est pas renseigné getEMailAddress() prend l'email du parent ou à défaut, celui du
         * user connecté
         * 
         */
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("LISTE_EMPLOYEUR_RADIE_ERREUR_EMAIL_OBLIGATOIRE"));
            setSendCompletionMail(false);
            setSendMailOnError(false);
        }
    }

    private boolean createDocument(AFListeAgenceCommunaleManager manager) throws Exception, Exception {
        CommonExcelmlContainer container = AFXmlmlMappingAgenceCommunale.loadResults(manager, this, numCaisse,
                nomAgenceCommunale);

        if (isAborted()) {
            return false;
        }

        String docPath = "";
        if (CodeSystem.LANGUE_ALLEMAND.equals(entityAgence.getLangueAdministration())) {
            docPath = AFExcelmlUtils.createDocumentExcel("DE" + "/" + AFListeExcelAgenceCommunaleProcess.MODEL_NAME,
                    nomDoc, container);
        } else {
            docPath = AFExcelmlUtils.createDocumentExcel("FR" + "/" + AFListeExcelAgenceCommunaleProcess.MODEL_NAME,
                    nomDoc, container);
        }
        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(AFListeExcelAgenceCommunaleProcess.NUMERO_INFOROM);
        docInfo.setDocumentProperty(DocumentInfoNaos.AGENCE_CODE, entityAgence.getCodeAdministration());
        docInfo.setDocumentProperty(DocumentInfoNaos.NOM_FICHIER_CCVS, nomFileCCCVS);
        docInfo.setDocumentProperty(DocumentInfoNaos.AGENCE_LIBELLE, entityAgence.getNomAdministration1() + " "
                + entityAgence.getNomAdministration2());
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    private boolean createDocumentCsv(AFListeAgenceCommunaleManager manager) throws Exception, Exception {
        AFListeAgenceCommunaleCSVFile doc = new AFListeAgenceCommunaleCSVFile();
        doc.populateSheet(this, manager, numCaisse);
        doc.setFilename(nomDoc);
        if (isAborted()) {
            return false;
        }
        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(AFListeExcelAgenceCommunaleProcess.NUMERO_INFOROM);
        docInfo.setDocumentProperty(DocumentInfoNaos.AGENCE_CODE, entityAgence.getCodeAdministration());
        docInfo.setDocumentProperty(DocumentInfoNaos.NOM_FICHIER_CCVS, nomFileCCCVS);
        docInfo.setDocumentProperty(DocumentInfoNaos.AGENCE_LIBELLE, entityAgence.getNomAdministration1() + " "
                + entityAgence.getNomAdministration2());
        this.registerAttachedDocument(docInfo, doc.getOutputFile());

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_OBJECT_LISTE_AGENCE_ERROR");
        } else {
            if (JadeStringUtil.isBlank(nomAgenceCommunale) && JadeStringUtil.isBlank(forIdTiersAgence)) {
                return (getSession().getLabel("LISTE_PAR_AGENCE_COMMUNALE") + " - " + getSession().getLabel(
                        "EMAIL_AFFILIE_SANS_AGENCE"));
            } else {
                return (getSession().getLabel("LISTE_PAR_AGENCE_COMMUNALE") + " - " + nomAgenceCommunale);
            }
        }
    }

    public String getForDate() {
        return forDate;
    }

    public String getForIdTiersAgence() {
        return forIdTiersAgence;
    }

    public Boolean getForIdTiersAgenceVide() {
        return forIdTiersAgenceVide;
    }

    public String getNomAgenceCommunale() {
        return nomAgenceCommunale;
    }

    public Boolean getWantCsv() {
        return wantCsv;
    }

    private void initManager(AFListeAgenceCommunaleManager manager) throws Exception {
        manager.setSession(getSession());
        manager.setForDateValeur(getForDate());
        manager.setForIdTiersAgence(getForIdTiersAgence());
        manager.setNotInTypeAffiliation(CodeSystem.TYPE_AFFILI_NON_SOUMIS);
        if (getForIdTiersAgenceVide().booleanValue()) {
            manager.setForIdTiersAgenceVide(getForIdTiersAgenceVide());
        }
        manager.find(BManager.SIZE_NOLIMIT);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setForIdTiersAgence(String forIdTiersAgence) {
        this.forIdTiersAgence = forIdTiersAgence;
    }

    public void setForIdTiersAgenceVide(Boolean forIdTiersAgenceVide) {
        this.forIdTiersAgenceVide = forIdTiersAgenceVide;
    }

    public void setNomAgenceCommunale(String nomAgenceCommunale) {
        this.nomAgenceCommunale = nomAgenceCommunale;
    }

    public void setWantCsv(Boolean wantCsv) {
        this.wantCsv = wantCsv;
    }

    /**
     * setter
     */

}
