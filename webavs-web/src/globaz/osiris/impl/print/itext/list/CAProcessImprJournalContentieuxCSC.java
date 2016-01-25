package globaz.osiris.impl.print.itext.list;

import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.application.CAApplication;
import globaz.osiris.print.itext.list.CAIListOrdreGroupeParam;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Insérez la description du type ici. Date de création : (03.06.2003 15:46:19)
 * 
 * @author: Administrator
 */
public class CAProcessImprJournalContentieuxCSC extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    class ComparableVector extends java.util.Vector {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public int compareTo(Object obj2, int index) {
            ComparableVector obj3 = (ComparableVector) obj2;
            if (obj3 == null) {
                return 0;
            }
            String str1 = elementAt(index).toString();
            String str2 = obj3.elementAt(index).toString();
            return (str1.compareTo(str2));
        }
    }

    private int compteur = 0;
    private java.lang.String date = new String();

    private java.lang.String dateReference = new String();
    private Map m_container = new java.util.Hashtable();

    private Map m_containerRecap = new TreeMap();
    private boolean modePrevisionnel = false;

    private boolean next = true;

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param parent
     *            BProcess
     */
    public CAProcessImprJournalContentieuxCSC(BProcess parent) throws FWIException {
        this(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "CAJournalContentieuxCSC");
        super.setDocumentTitle("Journal du contentieux CSC");
    }

    /**
     * Constructor for CAProcessImprJournalContentieuxCSC.
     * 
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CAProcessImprJournalContentieuxCSC(BProcess parent, String rootApplication, String fileName)
            throws FWIException {
        super(parent, rootApplication, fileName);
    }

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAProcessImprJournalContentieuxCSC(BSession session) throws FWIException {
        this(session, CAApplication.DEFAULT_OSIRIS_ROOT, "CAJournalContentieuxCSC");
        super.setDocumentTitle("Journal du contentieux CSC");
    }

    /**
     * Constructor for CAProcessImprJournalContentieuxCSC.
     * 
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CAProcessImprJournalContentieuxCSC(BSession session, String rootApplication, String fileName)
            throws FWIException {
        super(session, rootApplication, fileName);
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

            super.setParametres(FWIImportParametre.PARAM_COMPANYNAME, "");
            super.setParametres(FWIImportParametre.PARAM_EXERCICE, globaz.globall.util.JACalendar.todayJJsMMsAAAA());
            // Titre du document
            super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("6009"));
            // Date de référence
            super.setParametres(CAImprJournalContentieuxCSCParam.LABEL_DATEREF, getSession().getLabel("DATEREFERENCE"));
            super.setParametres(CAImprJournalContentieuxCSCParam.PARAM_DATEREF, getDateReference());
            // Date sur document
            super.setParametres(CAImprJournalContentieuxCSCParam.LABEL_DATEDOC, getSession().getLabel("DATEDOCUMENT"));
            super.setParametres(CAImprJournalContentieuxCSCParam.PARAM_DATEDOC, getDate());
            // Mode d'édition prévisionnel
            super.setParametres(CAImprJournalContentieuxCSCParam.LABEL_MODEEDITION, getSession()
                    .getLabel("MODEEDITION"));
            if (isModePrevisionnel()) {
                super.setParametres(CAImprJournalContentieuxCSCParam.PARAM_MODEEDITION,
                        getSession().getLabel("MODEEDITION_PREVISIONNEL"));
            } else {
                super.setParametres(CAImprJournalContentieuxCSCParam.PARAM_MODEEDITION,
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
        super.setParametres(CAImprJournalContentieuxCSCParam.LABEL_TOTAUX, getSession().getLabel("TOTAL"));
        super.setParametres(CAImprJournalContentieuxCSCParam.LABEL_ELEMENT, getSession().getLabel("ELEMENT"));
    }

    /**
     * Methode pour insérer les constantes qui s'affiche le nom des colonnes Utiliser super.setParametres(Key, Value)
     */
    protected void _tableHeader() {

        this.setParametres(FWIImportParametre.getCol(1), getSession().getLabel("COMPTEANNEXE"));
        this.setParametres(FWIImportParametre.getCol(2), getSession().getLabel("SECTION"));
        this.setParametres(FWIImportParametre.getCol(3), getSession().getLabel("DATE"));
        this.setParametres(FWIImportParametre.getCol(4), getSession().getLabel("ETAPE"));
        this.setParametres(FWIImportParametre.getCol(5), getSession().getLabel("DATEECHEANCE"));
        this.setParametres(FWIImportParametre.getCol(6), getSession().getLabel("TAXES_FRAIS"));
        this.setParametres(FWIImportParametre.getCol(7), getSession().getLabel("MONTANT"));
        this.setParametres(FWIImportParametre.getCol(8), getSession().getLabel("REMARQUE"));

        this.setParametres(FWIImportParametre.getCol(10), getSession().getLabel("RECAP_PAR_ETAPE"));
        this.setParametres(FWIImportParametre.getCol(11), getSession().getLabel("NOMBRE"));
        this.setParametres(FWIImportParametre.getCol(12), getSession().getLabel("TAXES_FRAIS"));
        this.setParametres(FWIImportParametre.getCol(13), getSession().getLabel("MONTANT"));
    }

    @Override
    protected void _validate() throws Exception {

        if (getParent() == null) {
            setSendCompletionMail(true);
            setControleTransaction(true);
        }

        if (getSession().hasErrors()) {
            abort();
        }
    }

    public void addDataObject(CAImprJournalContentieuxCSCBean bean) {
        try {
            compteur++;
            m_container.put(new Integer(compteur), bean);
            // Récapitulation
            // On rajoute le no de compteur afin d'avoir des clés différentes,
            // sans quoi
            // elles seraient remplacées.
            m_containerRecap.put(bean.getCOL_10() + "#" + String.valueOf(compteur), bean);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

    }

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        // Ajout des références en bas de liste
        super.setParametres(CAImprJournalContentieuxCSCParam.PARAM_REFERENCE, _getRefParam());
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
    public void beforeExecuteReport() {

        // Attribution du nom du document
        try {
            setTemplateFile("CAIJournalContentieux");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 13:59:41)
     * 
     * @param ds
     *            osiris.print.itext.list CAIJournalContentieux_DS
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    // public void bindData(CAIJournalContentieux_DS ds) throws Exception {
    // // Clonage du datasource pour qu'il reprenne les infos depuis le début
    // dataSource = (CAIJournalContentieux_DS)ds.clone();
    // super.setDataSource(ds);
    // //super.execute();
    // }
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

        List l = new ArrayList((new TreeMap(m_container)).values());
        super.setDataSource(l);

        JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(m_containerRecap.values());
        super.setParametres(CAIListOrdreGroupeParam.PARAM_SOURCE_SUB1, source);
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

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("IMPRESSION_ERROR");
        } else {
            return getSession().getLabel("IMPRESSION_OK");
        }
    }

    // /**
    // * Commentaire relatif à la méthode getModelClass.
    // */
    // public String getModelClass() {
    // return null;
    // }
    // /**
    // * Commentaire relatif à la méthode getScriptFunction.
    // */
    // public String getScriptFunction() {
    // return null;
    // }
    // /**
    // * Commentaire relatif à la méthode getStartPreview.
    // */
    // public String getStartPreview() throws Exception {
    // return null;
    // }
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
     * Méthode appelé pour lancer l'exportation du document Par défaut ne pas utiliser car déjà implémenté par la
     * superClass Utile si on ne veut pas exporter en fichier temporaire Date de création : (17.02.2003 14:44:15)
     */
    @Override
    public void returnDocument() throws FWIException {
        super.imprimerLastDocument();
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
