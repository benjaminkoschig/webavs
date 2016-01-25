package globaz.musca.itext.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.FWIDocumentListManager;
import globaz.framework.printing.itext.api.FWIBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.itext.FAiTextParameterList;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;

public class FAListDecomptes_Doc extends FWIDocumentListManager {

    private static final long serialVersionUID = 85443161405274176L;
    public final static String NUM_REF_INFOROM_LISTE_DECOMPTE = "0098CFA";
    private globaz.musca.db.facturation.FAEnteteFacture enteteFacture;
    private java.lang.String idPassage = new String();
    private java.lang.String idSousType = new String();
    private java.lang.String idTri = new String();
    private java.lang.String idTriDecompte = new String();
    private java.lang.String libelle = new String();
    private double montantNegatif = 0.0;

    private double montantPositif = 0.0;

    private globaz.musca.db.facturation.FAPassage passage;
    // Nombre total de documents que le manager devrait contenir
    private long shouldNbImprimer = 0;
    public final String TEMPLATE_FILENAME = new String("MUSCA_DCOMPT_LST");

    public final String TEMPLATE_RECAP = new String("MUSCA_DCOMPT_LST_RECAP");

    public FAListDecomptes_Doc() throws Exception {
        this(new BSession(FAApplication.DEFAULT_APPLICATION_MUSCA));
    }

    public FAListDecomptes_Doc(BProcess parent) throws Exception {
        super(parent, globaz.musca.application.FAApplication.APPLICATION_MUSCA_REP, "ImpressionListDecomptes");
        super.setFileTitle("Impression des listes de décomptes");
    }

    public FAListDecomptes_Doc(BSession session) throws Exception {
        super(session, globaz.musca.application.FAApplication.APPLICATION_MUSCA_REP, "ImpressionListDecomptes");
        super.setFileTitle("Impression des listes de décomptes");
    }

    protected void _headerText() {
        try {
            super.setTemplateFile(TEMPLATE_FILENAME + "_MAIN");
            super.setParametres(
                    FWIImportParametre.PARAM_COMPANYNAME,
                    getTemplateProperty(
                            getDocumentInfo(),
                            ACaisseReportHelper.JASP_PROP_NOM_CAISSE
                                    + JadeStringUtil.toUpperCase(getSession().getIdLangueISO())));
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("LISDECTITLE"));
        super.setParametres(FAiTextParameterList.LABEL_NUMERO, getSession().getLabel("NUMERO"));
        super.setParametres(FAiTextParameterList.PARAM_NUMERO, getPassage().getIdPassage());
        super.setParametres(FAiTextParameterList.LABEL_LIBELLE, getSession().getLabel("LIBELLE"));
        super.setParametres(FAiTextParameterList.PARAM_LIBELLE, getPassage().getLibelle());
        super.setParametres(FAiTextParameterList.LABEL_DATE_FACT, getSession().getLabel("DATEFACT"));
        super.setParametres(FAiTextParameterList.PARAM_DATE_FACT, getPassage().getDateFacturation());
        super.setParametres(FAiTextParameterList.LABEL_DATE_ECHEANCE, getSession().getLabel("DATEECHEANCE"));
        super.setParametres(FAiTextParameterList.PARAM_DATE_ECHEANCE, getPassage().getDateEcheance());

    }

    protected void _summaryText() {
        super.setParametres(FAiTextParameterList.LABEL_TOTAUX, getSession().getLabel("TOTAUX"));
        super.setParametres(FAiTextParameterList.PARAM_REFERENCE,
                getSession().getUserId() + " (" + JACalendar.todayJJsMMsAAAA() + ")");
    }

    protected void _tableHeader() {
        super.setColumnHeader(1, getSession().getLabel("DEBITEUR"));
        super.setColumnHeader(2, getSession().getLabel("DECOMPTE"));
        super.setColumnHeader(3, getSession().getLabel("MONTANT"));
        super.setColumnHeader(4, getSession().getLabel("ADRPAIEM"));
        super.setColumnHeader(5, getSession().getLabel("REMARQUE"));
        super.setColumnHeader(6, getSession().getLabel("IMPRESSION"));
    }

    @Override
    protected void _validate() throws java.lang.Exception {
        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
            this._addError("Le champ email doit être renseigné.");
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
    }

    @Override
    public void beforeBuildReport() {
        // Générer le nom du document
        List beanList = super.getBeanContainer();
        String first = (String) ((Map) super.getBeanContainer().get(0)).get("COL_1");
        String last = (String) ((Map) super.getBeanContainer().get(super.getBeanContainer().size() - 1)).get("COL_1");
        super.setDocumentTitle(first + "..." + last);
        getDocumentInfo().setDocumentTypeNumber(FAListDecomptes_Doc.NUM_REF_INFOROM_LISTE_DECOMPTE);
    }

    @Override
    public final boolean beforePrintDocument() {
        boolean isOk = super.beforePrintDocument();
        if (isOk && (super.getPageNumber() >= super.getMaxPageNumber() - 1)) {
            // On lance la construction de la recap
            FWIImportManager recapImport = super.getImporter();
            recapImport.setDataSource(new JREmptyDataSource());
            recapImport.setDocumentTemplate(TEMPLATE_RECAP);
            recapImport.setDocumentName(" Recapitulatif");
            recapImport.setParametre(FAiTextParameterList.LABEL_TYPE_DECOMPTE, getSession()
                    .getLabel("LISDEC_RECAPTYPE"));
            recapImport.setParametre(FAiTextParameterList.LABEL_CREDIT, getSession().getLabel("LISDEC_RECAPCREDIT"));
            recapImport.setParametre(FAiTextParameterList.LABEL_POSITIF, getSession().getLabel("LISDEC_RECAPPOSITIF"));
            recapImport
                    .setParametre(FAiTextParameterList.LABEL_RECAP_TITRE, getSession().getLabel("LISDEC_RECAPTITLE"));
            recapImport.setParametre(FAiTextParameterList.LABEL_NUM_DECOMPTE,
                    getSession().getLabel("LISDEC_RECAPNDECOMPTE"));
            recapImport.setParametre(FAiTextParameterList.PARAM_CREDIT, new Double(montantNegatif));
            recapImport.setParametre(FAiTextParameterList.PARAM_POSITIF, new Double(montantPositif));
            recapImport.setParametre(FAiTextParameterList.PARAM_NUM_DECOMPTE, new Double(shouldNbImprimer));
            try {
                recapImport.createDocument();
            } catch (FWIException e) {
                // Il y a eu une erreur, on informe l'utilisateur
                getMemoryLog().logMessage(
                        getSession().getLabel("La recapitulation du document a eu une erreur : " + e.getMessage()),
                        globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
            }
        }
        return isOk;
    }

    public void bindData(String id) throws java.lang.Exception {
        setIdPassage(id);
        super.executeProcess();
    }

    @Override
    public void createDataSource() throws Exception {
    }

    @Override
    protected void doReadBean(FWIBeanInterface myBean) {
        double montant = 0.0;
        if ((montant = myBean.getMontant()) > 0.0) {
            montantPositif += montant;
        } else {
            montantNegatif += montant;
        }
    }

    public globaz.musca.db.facturation.FAEnteteFacture getEnteteFacture() {
        return enteteFacture;
    }

    public java.lang.String getIdPassage() {
        return idPassage;
    }

    public java.lang.String getIdSousType() {
        return idSousType;
    }

    public java.lang.String getIdTri() {
        return idTri;
    }

    public java.lang.String getIdTriDecompte() {
        return idTriDecompte;
    }

    public java.lang.String getLibelle() {
        return libelle;
    }

    public globaz.musca.db.facturation.FAPassage getPassage() {
        return passage;
    }

    /**
     * Returns the shouldNbImprimer.
     * 
     * @return long
     */
    public long getShouldNbImprimer() {
        return shouldNbImprimer;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Récupère les informations du décompte avant impression.
     */
    @Override
    public boolean prepareDocument() {

        FAEnteteFactureManager manager = null;
        // Sous contrôle d'exceptions
        try {
            manager = new FAEnteteFactureManager();
            manager.setSession(getSession());

            // Vérifier l'id du passage
            if (JadeStringUtil.isIntegerEmpty(getIdPassage())) {
                getMemoryLog().logMessage("à remplir", FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            // instancier le passage en cours
            passage = new FAPassage();
            passage.setSession(getSession());
            passage.setIdPassage(getIdPassage());
            passage.retrieve(getTransaction());
            if (passage.hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), passage.getClass().getName());
                getMemoryLog().logMessage("a remplir", FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            // S'il n'y a pas d'erreur lors du rapatriement des données, passer
            // aux traitements
            if (!isAborted()) {

                // état du process
                setState("(" + getIdPassage() + ") " + getSession().getLabel("PROCESSSTATE_RECHERCHEDONNEE"));

                // Where clause
                manager.setForIdPassage(passage.getIdPassage());
                manager.setForTriDecompte(getIdTriDecompte());
                manager.setForIdSousType(getIdSousType());
                // Order by
                manager.setOrderBy(getIdTri());

                try {
                    shouldNbImprimer = manager.getCount(getTransaction());
                    super.setTotalNumberOfEntity((int) shouldNbImprimer);
                } catch (Exception e) {
                    getMemoryLog().logMessage("Ne peut pas obtenir le COUNT(*) du manager", FWViewBeanInterface.ERROR,
                            this.getClass().getName());
                }

                // le manager ne contient aucune entête de facture
                if (shouldNbImprimer == 0) {
                    getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_FAPRINT_NOFACTURE"),
                            globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
                }

                // Suivre la progression
                setSuivreProgression(true);

                // Entrer les informations pour l' état du process
                setState("(" + getIdPassage() + ") " + getSession().getLabel("PROCESSSTATE_CREATIONDOCUMENTS"));
                // Charger les valeurs du rapport
                if (isAborted()) {
                    return false;
                }

            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        // On set les different propriete
        super.setHauteurMinLigne(17);
        super.setHauteurOnFirstPage(300);
        super.setHauteurOnLastPage(390);
        super.setHauteurOnPage(390);
        super.setMainTemplate(TEMPLATE_FILENAME + "_MAIN");
        super.setLastTemplate(TEMPLATE_FILENAME + "_LAST");
        super.setFirstTemplate(TEMPLATE_FILENAME + "_FIRST");
        super.setTailleLot(100); // Setter la taille du lot
        super.setImpressionParLot(true);
        super.setManager(manager);

        FAListDecomptes_Bean bean = new FAListDecomptes_Bean();
        bean.setTransaction(getTransaction());
        bean.setContext(this);
        super.setBeanObject(bean);

        return true;
    }

    public void setEnteteFacture(globaz.musca.db.facturation.FAEnteteFacture newEnteteFacture) {
        enteteFacture = newEnteteFacture;
    }

    @Override
    public Map setGlobalParameter() {
        _headerText();
        _summaryText();
        _tableHeader();
        return getImporter().getParametre();
    }

    public void setIdPassage(java.lang.String idPassage) {
        this.idPassage = idPassage;
    }

    public void setIdSousType(java.lang.String newIdSousType) {
        idSousType = newIdSousType;
    }

    public void setIdTri(java.lang.String newIdTri) {
        idTri = newIdTri;
    }

    public void setIdTriDecompte(java.lang.String newIdTriDecompte) {
        idTriDecompte = newIdTriDecompte;
    }

    public void setLibelle(java.lang.String libelle) {
        this.libelle = libelle;
    }

    public void setPassage(globaz.musca.db.facturation.FAPassage newPassage) {
        passage = newPassage;
    }

}
