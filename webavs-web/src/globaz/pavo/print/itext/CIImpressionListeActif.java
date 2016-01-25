package globaz.pavo.print.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.pavo.db.inscriptions.CIListeInscriptionActif;
import globaz.pavo.db.inscriptions.CIListeInscriptionActifManager;

public class CIImpressionListeActif extends FWIAbstractManagerDocumentList {

    private static final long serialVersionUID = -1276575605478673071L;
    protected BSession bsession;
    private String companyName = "";
    private String documentTitle = "";

    private String forAnnee = "";

    private String untilAnnee = "";

    public CIImpressionListeActif() {
    }

    public CIImpressionListeActif(BSession session) throws Exception {
        super(session, "Liste_Inscription_Actif", "GLOBAZ", session.getLabel("TITRE_LISTE_71"),
                new CIListeInscriptionActifManager(), "PAVO");
        setBsession(session);
        _loadProperties("PAVO");

    }

    @Override
    public void _beforeExecuteReport() {
        try {
            _loadProperties("PAVO");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getDocumentInfo().setDocumentTypeNumber("0185CCI");
        } catch (Exception e) {
            e.printStackTrace();
        }
        CIListeInscriptionActifManager mgr = (CIListeInscriptionActifManager) _getManager();
        setDocumentTitle("impression liste après décès");
        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        _setDocumentTitle(getSession().getLabel("TITRE_INSC_71"));
        setDocumentTitle(getSession().getLabel("TITRE_INSC_71"));
        mgr.setSession(getBsession());
        mgr.setForAnnee(getForAnnee());
        mgr.setUntilAnnee(getUntilAnnee());
        setSendCompletionMail(true);
        setSendMailOnError(true);
        super._beforeExecuteReport();
    }

    @Override
    protected void _createDocumentInfo() {
        super._createDocumentInfo();

        try {
            _loadProperties("PAVO");
        } catch (Exception e) {
            e.printStackTrace();
        }
        super._setFilenameRoot("inscriptionsDeces");
    }

    @Override
    protected void addRow(BEntity entity) throws FWIException {

        CIListeInscriptionActif affiliation = (CIListeInscriptionActif) entity;
        _addCell(globaz.commons.nss.NSUtil.formatAVSUnknown(affiliation.getNumAvs()));
        String nom = affiliation.getNom();
        _addCell(nom);

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

        _addCell(affiliation.getAnnee());
        _addCell(affiliation.getMontant());
    }

    public BSession getBsession() {
        return bsession;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("LISTE_71_EMAIL_ECHEC");
        } else {
            return getSession().getLabel("LISTE_71_EMAIL");
        }

    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getUntilAnnee() {
        return untilAnnee;
    }

    @Override
    protected void initializeTable() {

        this._addColumnLeft(getSession().getLabel("LISTE_NOAVS_AC"), 8);
        this._addColumnLeft(getSession().getLabel("LISTE_NOM_AC"), 32);
        this._addColumnLeft(getSession().getLabel("LISTE_SEXE_AC"), 8);
        this._addColumnLeft(getSession().getLabel("LISTE_DATENAISSANCE_AC"), 8);
        this._addColumnLeft(getSession().getLabel("LISTE_PAYS_AC"), 16);
        this._addColumnLeft(getSession().getLabel("LISTE_ANNEE_AC"), 5);
        this._addColumnRight(getSession().getLabel("LISTE_MONTANT_AC"), 10);

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setBsession(BSession bsession) {
        this.bsession = bsession;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
        super._setCompanyName(companyName);
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
        super._setDocumentTitle(documentTitle);
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setUntilAnnee(String untilAnnee) {
        this.untilAnnee = untilAnnee;
    }

}
