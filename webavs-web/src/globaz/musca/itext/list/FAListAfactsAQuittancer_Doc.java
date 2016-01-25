package globaz.musca.itext.list;

// ITEXT
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.itext.FAiTextParameterList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Insérez la description du type ici. Date de création : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
public class FAListAfactsAQuittancer_Doc extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String NUM_REF_INFOROM_LISTE_AFACT_QUIT = "0182CFA";
    private globaz.musca.db.facturation.FAAfact afact;
    private java.lang.String idAfact = new String();
    private java.lang.String idModuleFacturation = new String();
    private java.lang.String idPassage = new String();
    private java.lang.String idTri = new String();
    private java.lang.String idTypeModule = new String(FAModuleFacturation.CS_MODULE_AFACT);
    private Iterator<String> itPassage;
    private java.lang.String libelle = new String();
    private List<String> listPassage = new ArrayList<String>();
    private globaz.musca.db.facturation.FAPassage passage;
    // Nombre total de documents que le manager devrait contenir
    private long shouldNbImprimer = 0;

    public final String TEMPLATE_FILENAME = new String("MUSCA_AFACT_LST");

    public FAListAfactsAQuittancer_Doc() throws Exception {
        this(new BSession(globaz.musca.application.FAApplication.DEFAULT_APPLICATION_MUSCA));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 11:28:36)
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public FAListAfactsAQuittancer_Doc(BProcess parent) throws FWIException {
        this(parent, globaz.musca.application.FAApplication.APPLICATION_MUSCA_REP, "listAFactAQuittancer");
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public FAListAfactsAQuittancer_Doc(BProcess arg0, String arg1, String arg2) throws FWIException {
        super(arg0, arg1, arg2);
        super.setFileTitle("Impression des listes des afacts à quittancer");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 11:28:36)
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public FAListAfactsAQuittancer_Doc(BSession session) throws FWIException {
        this(session, globaz.musca.application.FAApplication.APPLICATION_MUSCA_REP, "listAFactAQuittancer");
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public FAListAfactsAQuittancer_Doc(BSession arg0, String arg1, String arg2) throws FWIException {
        super(arg0, arg1, arg2);
        super.setFileTitle("Impression des listes des afacts à quittancer");
    }

    protected void _headerText() {

        try {
            super.setParametres(
                    FWIImportParametre.PARAM_COMPANYNAME,
                    getTemplateProperty(
                            getDocumentInfo(),
                            ACaisseReportHelper.JASP_PROP_NOM_CAISSE
                                    + JadeStringUtil.toUpperCase(getSession().getIdLangueISO())));
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR,
                    "Erreur lors de la recherche du nom de la companie");
        }
        super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("LISAFACTTITTLE"));

        super.setParametres(FAiTextParameterList.LABEL_NUMERO, getSession().getLabel("NUMERO"));
        super.setParametres(FAiTextParameterList.PARAM_NUMERO, getPassage().getIdPassage());

        super.setParametres(FAiTextParameterList.LABEL_LIBELLE, getSession().getLabel("LIBELLE"));
        super.setParametres(FAiTextParameterList.PARAM_LIBELLE, getPassage().getLibelle());

        super.setParametres(FAiTextParameterList.LABEL_DATE_FACT, getSession().getLabel("DATEFACT"));
        super.setParametres(FAiTextParameterList.PARAM_DATE_FACT, getPassage().getDateFacturation());

        super.setParametres(FAiTextParameterList.LABEL_MODULE, getSession().getLabel("MODULE"));
        super.setParametres(FAiTextParameterList.PARAM_MODULE, getModuleLibelle(getIdModuleFacturation()));

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.03.2003 14:31:37)
     */
    protected void _summaryText() {
        super.setParametres(FAiTextParameterList.LABEL_TOTAUX, getSession().getLabel("TOTAUX"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.03.2003 14:33:40)
     */
    protected void _tableHeader() {
        super.setColumnHeader(1, getSession().getLabel("DEBITEUR"));
        super.setColumnHeader(2, getSession().getLabel("DECOMPTE"));
        super.setColumnHeader(3, getSession().getLabel("RUBRIQUE"));
        super.setColumnHeader(4, getSession().getLabel("LIBELLE"));
        super.setColumnHeader(5, getSession().getLabel("ANNEE"));
        super.setColumnHeader(6, getSession().getLabel("MASSE"));
        super.setColumnHeader(7, getSession().getLabel("TAUX"));
        super.setColumnHeader(8, getSession().getLabel("MONTANT"));
        super.setColumnHeader(9, getSession().getLabel("AQUITTANCER"));
        super.setColumnHeader(10, getSession().getLabel("VISA"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:44:29)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _validate() throws java.lang.Exception {
        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
            this._addError("Le champ email doit être renseigné.");
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        getDocumentInfo().setDocumentTypeNumber(FAListAfactsAQuittancer_Doc.NUM_REF_INFOROM_LISTE_AFACT_QUIT);
    }

    /**
     * Récupère les informations du décompte avant impression.
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        itPassage = listPassage.iterator();
        super.setTemplateFile(TEMPLATE_FILENAME);
        itPassage = listPassage.iterator();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 11:53:46)
     * 
     * @param id
     *            java.lang.String
     */
    public void bindData(String id) throws java.lang.Exception {
        setIdPassage(id);
        super._executeProcess();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource ()
     */
    @Override
    public void createDataSource() throws Exception {
        FAListAfactsAQuittancer_DS manager = null;
        List<Map<?, ?>> source = new ArrayList<Map<?, ?>>();
        // Sous contrôle d'exceptions
        try {
            manager = new FAListAfactsAQuittancer_DS();
            manager.setSession(getSession());

            // Vérifier l'id du passage
            if (JadeStringUtil.isIntegerEmpty(getIdPassage())) {
                getMemoryLog().logMessage("à remplir", FWMessage.FATAL, this.getClass().getName());
                return;
            }

            // instancier le passage en cours
            passage = new FAPassage();
            passage.setSession(getSession());
            passage.setIdPassage(getIdPassage());
            passage.retrieve(getTransaction());
            if (passage.hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), passage.getClass().getName());
                getMemoryLog().logMessage("a remplir", FWMessage.FATAL, this.getClass().getName());
                return;
            }

            // S'il n'y a pas d'erreur lors du rapatriement des données, passer
            // aux traitements
            if (!isAborted()) {

                // Where clause
                manager.setForIdPassage(getIdPassage());
                manager.setForIdModuleFacturation(getIdModuleFacturation());
                manager.setForAQuittancer(new Boolean(true));
                // Order by
                manager.setOrderBy(getIdTri());

                // Charger les valeurs du rapport
                if (isAborted()) {
                    return;
                }
                while (manager.next()) {
                    source.add(manager.getFieldValues());
                }

            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return;
        }

        _headerText();
        _tableHeader();
        _summaryText();
        super.setDataSource(source);

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.04.2003 14:09:33)
     * 
     * @return globaz.musca.db.facturation.FAAfact
     */
    public globaz.musca.db.facturation.FAAfact getAfact() {
        return afact;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.04.2003 14:10:20)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdAfact() {
        return idAfact;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.04.2003 14:58:11)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:47:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdPassage() {
        return idPassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.03.2003 11:20:43)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTri() {
        return idTri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.04.2003 12:15:36)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTypeModule() {
        return idTypeModule;
    }

    public java.lang.String getLibelle() {
        return libelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.04.2003 11:16:47)
     * 
     * @return java.lang.String
     * @param idModuleFacturation
     *            java.lang.String
     */
    public String getModuleLibelle(String idModuleFacturation) {
        FAModuleFacturation entity = new FAModuleFacturation();
        entity.setSession(getSession());
        entity.setIdModuleFacturation(getIdModuleFacturation());
        try {
            entity.retrieve(getTransaction());
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }
        if (entity.isNew()) {
            return "Module empty...";
        } else {
            return entity.getLibelle();
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.03.2003 14:13:38)
     * 
     * @return globaz.musca.db.facturation.FAPassage
     */
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
        if (itPassage.hasNext()) {
            idPassage = itPassage.next();
            return true;
        }
        return false;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.04.2003 14:09:33)
     * 
     * @param newAfact
     *            globaz.musca.db.facturation.FAAfact
     */
    public void setAfact(globaz.musca.db.facturation.FAAfact newAfact) {
        afact = newAfact;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.04.2003 14:10:20)
     * 
     * @param newIdAfact
     *            java.lang.String
     */
    public void setIdAfact(java.lang.String newIdAfact) {
        idAfact = newIdAfact;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.04.2003 14:58:11)
     * 
     * @param newIdModuleFacturation
     *            java.lang.String
     */
    public void setIdModuleFacturation(java.lang.String newIdModuleFacturation) {
        idModuleFacturation = newIdModuleFacturation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:47:34)
     * 
     * @param newIdPassage
     *            java.lang.String
     */
    public void setIdPassage(java.lang.String idPassage) {
        if (!listPassage.contains(idPassage)) {
            listPassage.add(idPassage);
        }
        this.idPassage = idPassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.03.2003 11:20:43)
     * 
     * @param newIdTri
     *            java.lang.String
     */
    public void setIdTri(java.lang.String newIdTri) {
        idTri = newIdTri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.04.2003 12:15:36)
     * 
     * @param newIdTypeModule
     *            java.lang.String
     */
    public void setIdTypeModule(java.lang.String newIdTypeModule) {
        idTypeModule = newIdTypeModule;
    }

    public void setLibelle(java.lang.String libelle) {
        this.libelle = libelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.03.2003 14:13:38)
     * 
     * @param newPassage
     *            globaz.musca.db.facturation.FAPassage
     */
    public void setPassage(globaz.musca.db.facturation.FAPassage newPassage) {
        passage = newPassage;
    }

    /**
     * Sets the shouldNbImprimer.
     * 
     * @param shouldNbImprimer
     *            The shouldNbImprimer to set
     */
    public void setShouldNbImprimer(long shouldNbImprimer) {
        this.shouldNbImprimer = shouldNbImprimer;
    }

}
