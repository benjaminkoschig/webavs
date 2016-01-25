package globaz.hermes.print.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.gestion.HEIrrecouvrableCI;
import globaz.hermes.db.gestion.HEIrrecouvrableCIManager;
import globaz.jade.client.util.JadeStringUtil;

public class HEListIrrecouvrable extends FWIAbstractManagerDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param args
     *            DOCUMENT ME!
     */
    public static final void main(String[] args) {
        try {
            // session
            BSession session = new BSession(HEApplication.DEFAULT_APPLICATION_HERMES);

            session.connect("cicicam", "cicicam");

            // doc
            HEListIrrecouvrable doc = new HEListIrrecouvrable(session, "328");

            doc.setSession(session);

            doc.executeProcess();
            doc.setEMailAddress("usrext02@globaz.ch");

            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected BSession bsession;
    private String companyName = "";
    private String documentTitle = "";

    private String filenameRoot = "";

    private String service;

    /**
     * Crée une nouvelle instance de la classe AFAffiliationNAIndSansCI_DocListe.
     */
    public HEListIrrecouvrable() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAffiliationNAIndSansCI_DocListe.
     * 
     * @param session
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public HEListIrrecouvrable(BSession session, String idlot) throws Exception {

        super(session, "Liste_Irrecouvrable", "GLOBAZ", session.getLabel("TITRE_LISTE"),
                new HEIrrecouvrableCIManager(), "HERMES");
        HEIrrecouvrableCIManager mgr = (HEIrrecouvrableCIManager) _getManager();

        mgr.setSession(session);
        mgr.setLikeEnregistrement("2201");
        mgr.setForMotifIn("'71','81','75','85','79'");
        mgr.setForIdLot(idlot);
        mgr.setIrrecouvrable(true);

        setSendMailOnError(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.printing.itext.dynamique.FWIAbstractDocumentList# _beforeExecuteReport()
     */
    @Override
    public void _beforeExecuteReport() {
        getDocumentInfo().setDocumentTypeNumber("0180CCI");
        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
    }

    /**
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList#addRow(globaz.globall.db.BEntity)
     */
    @Override
    protected void addRow(BEntity entity) throws FWIException {
        String nom = "";

        HEIrrecouvrableCI affiliation = (HEIrrecouvrableCI) entity;

        nom = affiliation.getNom();

        _addCell(globaz.commons.nss.NSUtil.formatAVSUnknown(affiliation.getNumAvs()));
        _addCell(nom);
        _addCell("");
        _addCell("");
        FWParametersSystemCode csSexe = new FWParametersSystemCode();
        csSexe.setSession(getSession());
        csSexe.getCode(affiliation.getSexe());
        csSexe.getCurrentCodeUtilisateur().getLibelle();

        _addCell(csSexe.getCurrentCodeUtilisateur().getLibelle());

        _addCell(affiliation.getDateNaissance());

        FWParametersSystemCode csPays = new FWParametersSystemCode();
        csPays.setSession(getSession());
        csPays.getCode(affiliation.getPays());
        csPays.getCurrentCodeUtilisateur().getLibelle();

        _addCell(csPays.getCurrentCodeUtilisateur().getLibelle());
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            if (!JadeStringUtil.isBlank(getService())) {
                StringBuffer buffer = new StringBuffer("L'impression du document '");
                buffer.append(_getDocumentTitle());
                buffer.append("' s'est terminée en erreur");
                buffer.append("/" + getService());
                return buffer.toString();
            } else {
                StringBuffer buffer = new StringBuffer("L'impression du document '");
                buffer.append(_getDocumentTitle());
                buffer.append("' s'est terminée en erreur");
                return buffer.toString();
            }

        } else {
            if (!JadeStringUtil.isBlank(getService())) {
                StringBuffer buffer = new StringBuffer("L'impression du document '");
                buffer.append(_getDocumentTitle());
                buffer.append("' s'est terminée avec succès");
                buffer.append("/" + getService());
                return buffer.toString();
            } else {
                StringBuffer buffer = new StringBuffer("L'impression du document '");
                buffer.append(_getDocumentTitle());
                buffer.append("' s'est terminée avec succès");
                return buffer.toString();
            }

        }
    }

    public String getFilenameRoot() {
        return filenameRoot;
    }

    public String getService() {
        return service;
    }

    /**
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList#initializeTable()
     */
    @Override
    protected void initializeTable() {
        _addColumnLeft(getSession().getLabel("LISTE_NOAVS"));
        _addColumnLeft(getSession().getLabel("LISTE_NOM"));
        _addColumnLeft("");
        _addColumnLeft("");
        _addColumnLeft(getSession().getLabel("LISTE_SEXE"));
        _addColumnLeft(getSession().getLabel("LISTE_DATENAISSANCE"));
        _addColumnLeft(getSession().getLabel("LISTE_PAYS"));

    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
        super._setDocumentTitle(documentTitle);
    }

    public void setFilenameRoot(String filenameRoot) {
        this.filenameRoot = filenameRoot;
        super._setFilenameRoot(filenameRoot);
    }

    public void setService(String service) {
        this.service = service;
    }

}
