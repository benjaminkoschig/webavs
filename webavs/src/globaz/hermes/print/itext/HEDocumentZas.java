package globaz.hermes.print.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.gestion.HELotViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRExporterParameter;

/**
 * Insérez la description du type ici. Date de création : (02.04.2003 14:52:55)
 * 
 * @author: Administrator
 */
public class HEDocumentZas extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Lance l'application.
     * 
     * @param args
     *            un tableau d'arguments de ligne de commande -> -l lot
     */
    public static void main(String[] args) {
        HEDocumentZas process = null;
        //
        try {
            /** DEBUT BATCH PARAM * */
            if (args.length != 3) {
                throw new Exception("Usage globaz.hermes.print.itext.HEDocumentZas <idLot> <uid> <pwd>");
            }
            /** FIN BATCH PARAM * */
            BSession session = new BSession("HERMES");
            Jade.getInstance().setHomeDir(((HEApplication) session.getApplication()).getProperty("zas.home.dir"));
            session.connect(args[1], args[2]);
            process = new HEDocumentZas();
            process.setSession(session);
            HEApplication application = (HEApplication) GlobazServer.getCurrentSystem().getApplication(
                    process.getSession().getApplicationId());
            process.setCaOnly("false");
            process.setEMailAddress(application.getProperty("zas.user.email"));
            HELotViewBean lot = new HELotViewBean();
            lot.setSession(session);
            lot.setIdLot(args[0]);
            lot.retrieve();
            if (lot.isNew()) {
                throw new Exception("No such job");
            }
            if (lot.getType().equals(HELotViewBean.CS_TYPE_ENVOI)) {
                throw new Exception("This is a sent job, please specify a received job");
            }
            process.setIdLot(lot.getIdLot());
            process.executeProcess();
            System.out.print("Programme terminé ! Copier le fichier PDF (");
            System.out.println(process.getExporter().getExportNewFilePath());
            System.out.println(") avant de presser <Enter>....");
            System.in.read();
            System.out.println("Arrêt du programme lancé !");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            if ((process != null) && (process.getTransaction() != null)) {
                try {
                    process.getTransaction().closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
            System.exit(0);
        }
    }

    private String caOnly = "";
    private List<Map<String, String>> collection;
    private String forService;
    private String idLot;
    private boolean isArchivage;
    private boolean isFirst = true;
    private boolean isPrintBatch = false;

    private HELotViewBean lot;

    /**
	 * 
	 */
    public HEDocumentZas() {
        super();
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public HEDocumentZas(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    /**
     * Commentaire relatif à la méthode _headerText.
     */
    protected void _headerText() {
        if (getDocumentInfo() != null) {
            getDocumentInfo().setDocumentTypeNumber("0012CCI");
        }
        this.setParametres(
                FWIImportParametre.PARAM_COMPANYNAME,
                getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_NOM_CAISSE
                        + getSession().getIdLangueISO().toUpperCase()));
        if (getSession().getIdLangue().equals("F")) {
            this.setParametres("L_NUMERO", "Numéro de lot");
            this.setParametres("L_LIBELLE", "Date, heure");
            this.setParametres("P_TITLE", "ACCUSES DE RECEPTION DES ARC");
            this.setParametres("L_PAGE", "Page");
            if ((caOnly != null) && caOnly.equals("true")) {
                this.setParametres("P_TYPETITLE", "Liste des CA");
            } else {
                this.setParametres("P_TYPETITLE", "Liste des annonces ZAS");
            }
            super.setColumnHeader(1, "Référence interne");
            super.setColumnHeader(2, "Num ARC");
            super.setColumnHeader(3, "NSS");
            super.setColumnHeader(4, "Nom, Prénom");
            super.setColumnHeader(5, "Date\nNaissance");
            super.setColumnHeader(6, "Etat\nOrig.");
            super.setColumnHeader(7, "Mot.");
            super.setColumnHeader(8, "Numéro Assuré 1");
            super.setColumnHeader(9, "Etat");
            super.setColumnHeader(10, "User");
            super.setColumnHeader(11, "N° Affilié");
        } else if (getSession().getIdLangue().equals("D")) {
            this.setParametres("L_NUMERO", "Job-Nr.");
            this.setParametres("L_LIBELLE", "Datum, Zeit");
            this.setParametres("P_TITLE", "MZR-EMPFANGSBESTÄTIGUNGEN");
            this.setParametres("L_PAGE", "Seite");
            if ((caOnly != null) && caOnly.equals("true")) {
                this.setParametres("P_TYPETITLE", "Begleitliste zu VA");
            } else {
                this.setParametres("P_TYPETITLE", "Begleitliste für ZAS-Meldungen");
            }
            super.setColumnHeader(1, "Kasseneigener Hinweis");
            super.setColumnHeader(2, "MZR-Nr.");
            super.setColumnHeader(3, "SVN");
            super.setColumnHeader(4, "Name, Vorname");
            super.setColumnHeader(5, "Geburtsdatum");
            super.setColumnHeader(6, "Heimatstaat");
            super.setColumnHeader(7, "SZ");
            super.setColumnHeader(8, "Bisherige SVN");
            super.setColumnHeader(9, "Status");
            super.setColumnHeader(10, "Benutzer");
            super.setColumnHeader(11, "Abr.-Nr");
        }
    }

    protected void _setRefParam() {
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy ' - ' HH:mm");
        String refLabel = "";
        if (getSession().getIdLangue().equals("F")) {
            refLabel = "Référence : ";
        } else if (getSession().getIdLangue().equals("D")) {
            refLabel = "Hinweis : ";
        }
        StringBuffer refBuffer = new StringBuffer(refLabel);
        if (getSession().getIdLangue().equals("F")) {
            refBuffer.append("HEDocumentZas");
        } else if (getSession().getIdLangue().equals("D")) {
            refBuffer.append("HEDokumentZAS");
        }
        refBuffer.append(" (");
        refBuffer.append(formater.format(new Date()));
        refBuffer.append(")");
        refBuffer.append(" - ");
        refBuffer.append(getSession().getUserId().toLowerCase());
        if (JadeStringUtil.isBlank(getForService())) {
            this.setParametres("P_REFERENCE", refBuffer.toString());
        } else {
            this.setParametres("P_REFERENCE", refBuffer.toString() + " /" + getForService());
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 08:04:09)
     */
    @Override
    protected void _validate() {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("HERMES_00001"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                this._addError(getSession().getLabel("HERMES_00002"));
            }
        }
        setControleTransaction(true);
        if (!getSession().hasErrors() && !getPrintBatch()) {
            setSendCompletionMail(true);
        }
    }

    private Map<String, String> AddUserTotal(String user, int counter) {
        Map<String, String> toAdd = new HashMap<String, String>();
        toAdd.put("COL_1", "Total " + user + " : " + counter);
        toAdd.put("COL_10", user);
        return toAdd;
    }

    @Override
    public void beforeBuildReport() throws FWIException {

    }

    /**
     * Commentaire relatif à la méthode _beforeExecuteReport.
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        try {
            if ("DE".equals(getSession().getIdLangue())) {
                setDocumentTitle("HEDocument_ZAS");
            } else {
                setDocumentTitle("Confirmation_centrale");
            }
            // retrieve
            lot = new HELotViewBean();
            lot.setSession(getSession());
            lot.setIdLot(idLot);
            lot.retrieve();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "HEDocumentZas.beforeExecuteReport()");
        }
        //
        super.getExporter().setExporterOutline(JRExporterParameter.OUTLINE_NONE);
        super.setTemplateFile("HERMES_ZAS");
        super.getImporter().setDocumentTemplate("HERMES_ZAS");
    }

    @Override
    public boolean beforePrintDocument() {
        if (collection.size() == 0) {
            if (!isPrintBatch) {
                if (getMemoryLog() != null) {
                    getMemoryLog().logMessage(getSession().getLabel("HERMES_10061") + getIdLot(),
                            FWMessage.INFORMATION, "HEDocumentZas.createDataSource()");
                }
            }
            return false;
        } else {
            return true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource ()
     */
    @Override
    public void createDataSource() throws Exception {

        // Set du numéro de document INFOROM

        HEDocumentZasSource manager = null;
        // nom de la caisse
        try {
            manager = new HEDocumentZasSource();
            manager.setSession(getSession());
            super.setParametres("P_NUMERO", getIdLot());
            super.setParametres("P_LIBELLE", lot == null ? "" : lot.getDateCentraleLibelle());
            manager.setForIdLot(getIdLot());
            manager.setIsArchivage(getIsArchivage());
            manager.setLikeEnregistrement("2001");
            if ((caOnly != null) && caOnly.equals("true")) {
                manager.setForMotifLettreCA(getMotifCA());
                manager.setNotMotifPavo(true);
                // manager.setForCaOnly("true");
            } else {
                manager.setForCaOnly("false");
            }
            manager.setForService(getForService());

            manager.setOrder("RNLUTI,RNDDAN,RNTPRO,RNREFU,HEANNOP.RNIANN");
            // Make the dataSource
            Map<String, String> row;
            collection = new ArrayList<Map<String, String>>();
            int counter = 0;
            int total = 1;
            while ((row = manager.next()) != null) {
                if (!collection.isEmpty()) {
                    // reprendre le dernier élément ajouté
                    Map<String, String> dernierElement = collection.get(collection.size() - 1);
                    // traiter tous les lignes trouvées afin repérer le numéro d'affilié
                    // On teste si le numéro d'affilié a été
                    if (!JadeStringUtil.isEmpty(row.get("COL_11"))) {
                        dernierElement.put("COL_11", row.get("COL_11"));
                    }
                    String user = dernierElement.get("COL_10");
                    String userEnCours = row.get("COL_10");
                    String referenceInterne = dernierElement.get("COL_2");
                    String crt = row.get("COL_2");

                    if (!JadeStringUtil.isBlank(userEnCours) && userEnCours.equals(user)) {
                        // && !JadeStringUtil.isEmpty(crt)) {
                        if (!JadeStringUtil.isEmpty(crt) && !crt.equals(referenceInterne)) {
                            // se focaliser sur la référence unique afin de supprimer les doublons de la requête du
                            // manager
                            collection.add(row);
                            counter++;
                            total++;
                        }
                    } else {
                        collection.add(AddUserTotal(user, counter));
                        collection.add(row);
                        counter = 1;
                        total++;
                    }

                } else {
                    collection.add(row);
                    counter++;
                }
            }

            if (!collection.isEmpty()) {
                collection.add(AddUserTotal(manager.getLastUser(), counter));
                collection.add(manager.getTotalRetour(total));
            }
            _headerText();
            _setRefParam();
            if (collection.size() > 0) {
                super.setDataSource(collection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 09:47:55)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCaOnly() {
        return caOnly;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            if ("true".equals(getCaOnly())) {
                return getSession().getLabel("HERMES_10013") + " " + getIdLot() + " / "
                        + (lot == null ? "" : lot.getDateCentraleLibelle())
                        + (JadeStringUtil.isEmpty(getForService()) ? "" : "/" + getForService());
            } else {
                return getSession().getLabel("HERMES_10012") + " " + getIdLot() + " / "
                        + (lot == null ? "" : lot.getDateCentraleLibelle())
                        + (JadeStringUtil.isEmpty(getForService()) ? "" : "/" + getForService());
            }
        } else {
            if ("true".equals(getCaOnly())) {
                return getSession().getLabel("HERMES_10011") + " " + getIdLot() + " / "
                        + (lot == null ? "" : lot.getDateCentraleLibelle())
                        + (JadeStringUtil.isEmpty(getForService()) ? "" : "/" + getForService());
            } else {
                return getSession().getLabel("HERMES_10010") + " " + getIdLot() + " / "
                        + (lot == null ? "" : lot.getDateCentraleLibelle())
                        + (JadeStringUtil.isEmpty(getForService()) ? "" : "/" + getForService());
            }
        }
    }

    public String getForService() {
        return forService;
    }

    /**
     * @return
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * @return
     */
    public boolean getIsArchivage() {
        return isArchivage;
    }

    public String getMotifCA() {

        String key = "MOTCERT";
        try {
            FWFindParameterManager param = new FWFindParameterManager();
            FWFindParameter parametre;
            param.setSession(getSession());
            param.setIdApplParametre(HEApplication.DEFAULT_APPLICATION_HERMES);
            param.setIdCleDiffere(key);
            param.find();
            parametre = (FWFindParameter) param.getFirstEntity();
            return parametre.getValeurAlphaParametre();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean getPrintBatch() {
        return isPrintBatch;
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
        if (isFirst) {
            isFirst = false;
            return true;
        }
        return false;
    }

    /**
     * @param b
     */
    public void setArchivage(boolean b) {
        isArchivage = b;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 09:47:55)
     * 
     * @param newCaOnly
     *            java.lang.String
     */
    public void setCaOnly(java.lang.String newCaOnly) {
        if ((newCaOnly == null) || newCaOnly.equals("false")) {
            caOnly = "false";
        } else {
            caOnly = "true";
        }
    }

    public void setForService(String newForService) {
        forService = newForService;
    }

    /**
     * @param string
     */
    public void setIdLot(String string) {
        idLot = string;
    }

    public void setPrintBatch(boolean isPrintBatch) {
        this.isPrintBatch = isPrintBatch;
    }
}
