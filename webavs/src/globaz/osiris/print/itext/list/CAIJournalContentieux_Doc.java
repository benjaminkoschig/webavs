package globaz.osiris.print.itext.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.api.FWIImporterInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import globaz.osiris.application.CAApplication;
import globaz.osiris.utils.quicksort.QuickSort;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Date de création : (03.06.2003 15:46:19)
 * 
 * @author: Administrator
 */
public class CAIJournalContentieux_Doc extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String NUMERO_REFERENCE_INFOROM = "0152GCA";
    String currTemplate = "";
    private CAIJournalContentieux_DS dataSource = new CAIJournalContentieux_DS();
    private java.lang.String date = new String();
    private java.lang.String dateReference = new String();
    Map globalMap = null;
    private FWIImporterInterface importDoc = null;
    private boolean modePrevisionnel = false;
    private boolean next = true;

    /**
     * Constructeur CAProcessImprJournalContentieuxCSC.
     */
    public CAIJournalContentieux_Doc() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAIJournalContentieux_Doc(BSession session) throws FWIException {
        this(session, CAApplication.DEFAULT_OSIRIS_ROOT, "CAIJournalContentieux", "Journal du contentieux");
    }

    /**
     * Constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @param filenameRoot
     *            java.lang.String
     * @param companyName
     *            java.lang.String
     * @param documentTitle
     *            java.lang.String
     */
    public CAIJournalContentieux_Doc(globaz.globall.db.BSession session, String filenameRoot, String companyName,
            String documentTitle) throws FWIException {
        super(session, filenameRoot, companyName);
        super.setDocumentTitle(documentTitle);
    }

    // Création du paramètre de référence pour les documents de type liste
    private String _getRefParam() {
        try {
            SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy ' - ' HH:mm");
            StringBuffer refBuffer = new StringBuffer(getSession().getLabel("REFERENCE") + " ");
            refBuffer.append(this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1));
            refBuffer.append(" (");
            refBuffer.append(formater.format(new Date()));
            refBuffer.append(")");
            refBuffer.append(" - ");
            refBuffer.append(getSession().getUserId());
            return refBuffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la première page Utiliser super.setParametres(Key, Value)
     */
    protected void _headerText() {
        try {
            this.setParametres(
                    FWIImportParametre.PARAM_COMPANYNAME,
                    FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                            ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

            // Exercice (date)
            super.setParametres(FWIImportParametre.PARAM_EXERCICE, globaz.globall.util.JACalendar.todayJJsMMsAAAA());
            // Titre du document
            super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("6009"));
            // Date de référence
            super.setParametres(CAIJournalContentieuxParam.LABEL_DATEREF, getSession().getLabel("DATEREFERENCE"));
            super.setParametres(CAIJournalContentieuxParam.PARAM_DATEREF, getDateReference());
            // Date sur document
            super.setParametres(CAIJournalContentieuxParam.LABEL_DATEDOC, getSession().getLabel("DATEDOCUMENT"));
            super.setParametres(CAIJournalContentieuxParam.PARAM_DATEDOC, getDate());
            // Mode d'édition prévisionnel
            super.setParametres(CAIJournalContentieuxParam.LABEL_MODEEDITION, getSession().getLabel("MODEEDITION"));
            if (isModePrevisionnel()) {
                super.setParametres(CAIJournalContentieuxParam.PARAM_MODEEDITION,
                        getSession().getLabel("MODEEDITION_PREVISIONNEL"));
            } else {
                super.setParametres(CAIJournalContentieuxParam.PARAM_MODEEDITION,
                        getSession().getLabel("MODEEDITION_DEFINITIF"));
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return;
        }

        return;
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la dernière page Utiliser super.setParametres(Key, Value)
     */
    protected void _summaryText() {
        super.setParametres(CAIJournalContentieuxParam.LABEL_TOTAUX, getSession().getLabel("TOTAL"));
        super.setParametres(CAIJournalContentieuxParam.LABEL_ELEMENT, getSession().getLabel("ELEMENT"));
    }

    /**
     * Methode pour insérer les constantes qui s'affiche le nom des colonnes Utiliser super.setParametres(Key, Value)
     */
    protected void _tableHeader() {

        this.setParametres(FWIImportParametre.getCol(1), getSession().getLabel("COMPTEANNEXE"));
        this.setParametres(FWIImportParametre.getCol(2), getSession().getLabel("SECTION"));
        this.setParametres(FWIImportParametre.getCol(3), getSession().getLabel("DATE"));
        this.setParametres(FWIImportParametre.getCol(4), getSession().getLabel("ETAPE"));
        this.setParametres(FWIImportParametre.getCol(5), getSession().getLabel("DATEDECLENCHEMENT"));
        this.setParametres(FWIImportParametre.getCol(6), getSession().getLabel("TAXES_FRAIS"));
        this.setParametres(FWIImportParametre.getCol(7), getSession().getLabel("MONTANT"));
        this.setParametres(FWIImportParametre.getCol(8), getSession().getLabel("REMARQUE"));

        this.setParametres(FWIImportParametre.getCol(10), getSession().getLabel("RECAP_PAR_ETAPE"));
        this.setParametres(FWIImportParametre.getCol(11), getSession().getLabel("NOMBRE"));
        this.setParametres(FWIImportParametre.getCol(12), getSession().getLabel("TAXES_FRAIS"));
        this.setParametres(FWIImportParametre.getCol(13), getSession().getLabel("MONTANT"));
    }

    /**
     * Cette Méthode est exécutée une fois le rapport créé. On prépare ici les sous-rapports qui viendront également sur
     * la liste. On procède de cette manière suite à un problème de saut de page dans Itext2 (Saut de page sur un
     * sous-rapport)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#afterBuildReport()
     */
    @Override
    public void afterBuildReport() {
        importDoc.setDocumentTemplate("CAIJournalContentieux_sub");
        int numeroPage = importDoc.getDocument().getPages().size();
        // Premiere recap
        // Definir subsource 1 --> récapitulation par type
        try {
            // Clonage du datasource pour qu'il reprenne les infos depuis le
            // début
            // dataSource = (CAIJournalContentieux_DS)ds.clone();
            // super.setDataSource(ds);
            // super.execute();
            // Tri de la liste de récapitulation afin de l'ordonner par étape
            CAIJournalContentieux_DS source = (CAIJournalContentieux_DS) dataSource.clone();
            QuickSort.sort(source.getTableRecap(), 0);
            importDoc.setParametre(CAIJournalContentieuxParam.PARAM_PAGENUMERO, new Integer(numeroPage));
            importDoc.setDataSource(source);
            importDoc.createDocument();
        } catch (Exception e) {
            JadeLogger.fatal(this, e);
            e.printStackTrace();
        }

        // On redonne les infos d'origine
        importDoc.setParametre(globalMap);
        importDoc.setDocumentTemplate(currTemplate);
    }

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        importDoc = super.getImporter();
        globalMap = importDoc.getParametre();
        currTemplate = importDoc.getDocumentTemplate();
        getDocumentInfo().setTemplateName(currTemplate);
        // Ajout des références en bas de liste
        super.setParametres(FWIImportParametre.PARAM_REFERENCE, _getRefParam());
        _headerText();
        _tableHeader();
        _summaryText();
    }

    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        // Attribution du nom du document
        super.setTemplateFile("CAIJournalContentieux");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 13:59:41)
     * 
     * @param ds
     *            osiris.print.itext.list CAIJournalContentieux_DS
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public void bindData(CAIJournalContentieux_DS ds) throws Exception {
        // Clonage du datasource pour qu'il reprenne les infos depuis le début
        dataSource = (CAIJournalContentieux_DS) ds.clone();
        super.setDataSource(ds);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 13:59:41)
     * 
     * @param id
     *            java.lang.String
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public void bindData(String id) throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource ()
     */
    @Override
    public void createDataSource() throws Exception {
        // Un seul document doit être générer
        setNext(false);
    }

    /**
     * Cette méthode permet de récupérer la date sur document saisie par l'utilisateur. Date de création : (07.07.2003
     * 13:56:48)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDate() {
        return date;
    }

    /**
     * Cette méthode permet de récupérer la date de référence saisie par l'utilisateur. Date de création : (07.07.2003
     * 13:57:38)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateReference() {
        return dateReference;
    }

    /**
     * Commentaire relatif à la méthode getModelClass.
     */
    public String getModelClass() {
        return null;
    }

    /**
     * Commentaire relatif à la méthode getScriptFunction.
     */
    public String getScriptFunction() {
        return null;
    }

    /**
     * Commentaire relatif à la méthode getStartPreview.
     */
    public String getStartPreview() throws Exception {
        return null;
    }

    /**
     * Cette méthode permet de récupérer le mode prévisionnel saisi par l'utilisateur. si isModePrevisionnel == true -->
     * pas de comptabilisation des rappels (lancement provisioire) si isModePrevisionnel == false --> comptabilisation
     * des rappels (lancement définitif) Date de création : (07.07.2003 13:58:28)
     * 
     * @return boolean
     */
    public boolean isModePrevisionnel() {
        return modePrevisionnel;
    }

    /**
     * Returns the next.
     * 
     * @return boolean
     */
    public boolean isNext() {
        return next;
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
        return isNext();
    }

    /**
     * Définit la date sur document. Date de création : (07.07.2003 13:56:48)
     * 
     * @param newDate
     *            java.lang.String
     */
    public void setDate(java.lang.String newDate) {
        date = newDate;
    }

    /**
     * Définit la date de référence. Date de création : (07.07.2003 13:57:38)
     * 
     * @param newDateReference
     *            java.lang.String
     */
    public void setDateReference(java.lang.String newDateReference) {
        dateReference = newDateReference;
    }

    /**
     * Définit le mode prévisionnel (lancement provisoire ou définitif des rappels). Date de création : (07.07.2003
     * 13:58:28)
     * 
     * @param newModePrevisionnel
     *            boolean
     */
    public void setModePrevisionnel(boolean newModePrevisionnel) {
        modePrevisionnel = newModePrevisionnel;
    }

    /**
     * Sets the next.
     * 
     * @param next
     *            The next to set
     */
    public void setNext(boolean next) {
        this.next = next;
    }

    /**
     * Commentaire relatif à la méthode setSession.
     */
    public void setSession(globaz.globall.api.BISession newSession) {
    }

}
