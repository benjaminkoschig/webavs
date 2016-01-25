package globaz.naos.itext.affiliation;

import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliationJSection;
import globaz.naos.db.affiliation.AFAffiliationJSectionManager;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Crée la liste des affiliés radiés dont une ou plusieurs sections sont non soldées.
 * </p>
 * 
 * @author vre
 */
public class AFSoldesAffiliesRadies_DocListe extends FWIAbstractManagerDocumentList {

    private static final String DOC_NO = "0117CAF";

    private static final long serialVersionUID = 392319823704483825L;
    private boolean debutGroupe;
    private String fromDate = "";

    /**
     * Crée une nouvelle instance de la classe AFSoldesAffiliesRadies_DocListe.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public AFSoldesAffiliesRadies_DocListe() throws Exception {
        super(new BSession(AFApplication.DEFAULT_APPLICATION_NAOS), "", "GLOBAZ", "",
                new AFAffiliationJSectionManager(), AFApplication.DEFAULT_APPLICATION_NAOS);

        _setFilenameRoot(getSession().getLabel("NAOS_AFFILIES_SOLDE_AFF_RAD_TITRE"));
        _setDocumentTitle(getSession().getLabel("NAOS_AFFILIES_SOLDE_AFF_RAD_TITRE"));
    }

    /**
     * Crée une nouvelle instance de la classe AFAffiliationNAIndSansCI_DocListe.
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public AFSoldesAffiliesRadies_DocListe(BSession session) throws Exception {
        super(session, "", "GLOBAZ", "", new AFAffiliationJSectionManager(), AFApplication.DEFAULT_APPLICATION_NAOS);

        _setFilenameRoot(getSession().getLabel("NAOS_AFFILIES_SOLDE_AFF_RAD_TITRE"));
        _setDocumentTitle(getSession().getLabel("NAOS_AFFILIES_SOLDE_AFF_RAD_TITRE"));
    }

    /** 
     */
    @Override
    public void _beforeExecuteReport() {
        AFAffiliationJSectionManager mgr = (AFAffiliationJSectionManager) _getManager();

        mgr.setSession(getSession());
        mgr.setForSectionsOuvertes(Boolean.TRUE);
        mgr.setFromDateFinNonVide(fromDate);
        mgr.forIsTraitement(false);
        getDocumentInfo().setDocumentTypeNumber(AFSoldesAffiliesRadies_DocListe.DOC_NO);
    }

    @Override
    protected void addRow(BEntity arg0) throws FWIException {
        AFAffiliationJSection affJSection = (AFAffiliationJSection) arg0;

        if (debutGroupe) {
            _addCell(affJSection.getAffilieNumero());
            _addCell(affJSection.getTiers().getPrenomNom());
            _addCell(affJSection.getDateFin());

            if (!JadeStringUtil.isEmpty(affJSection.getDateDebutParticularite())
                    || !JadeStringUtil.isEmpty(affJSection.getDateFinParticularite())) {
                _addCell(affJSection.getDateDebutParticularite() + " - " + affJSection.getDateFinParticularite());
            } else {
                _addCell("");
            }

            debutGroupe = false;
        } else {
            _addCell("");
            _addCell("");
            _addCell("");
            _addCell("");
        }

        _addCell(affJSection.getSection().getIdExterne());
        _addCell(affJSection.getSection().getDateSection());
        _addCell(affJSection.getSection().getDescription());
        _addCell(affJSection.getSection().getSolde());
    }

    @Override
    protected void beginGroup(int arg0, BEntity arg1, BEntity arg2) throws FWIException {
        debutGroupe = true;
    }

    /**
     * getter pour l'attribut from date.
     * 
     * @return la valeur courante de l'attribut from date
     */
    public String getFromDate() {
        return fromDate;
    }

    @Override
    protected void initializeTable() {
        this._addColumnCenter(getSession().getLabel("NAOS_AFFILIES_SOLDE_AFF_RAD_COL_NUMAFF"), 1);
        this._addColumnLeft(getSession().getLabel("NAOS_AFFILIES_SOLDE_AFF_RAD_COL_NOM"), 2);
        this._addColumnCenter(getSession().getLabel("NAOS_AFFILIES_SOLDE_AFF_RAD_COL_DATE_RADIATION"), 1);
        this._addColumnLeft(getSession().getLabel("NAOS_AFFILIES_SOLDE_AFF_RAD_COL_PER_SANS_PER"), 1);
        this._addColumnCenter(getSession().getLabel("NAOS_AFFILIES_SOLDE_AFF_RAD_COL_NO_SECTION"), 1);
        this._addColumnCenter(getSession().getLabel("NAOS_AFFILIES_SOLDE_AFF_RAD_COL_DATE_SECTION"), 1);
        this._addColumnLeft(getSession().getLabel("NAOS_AFFILIES_SOLDE_AFF_RAD_COL_DESCRIPTION_SECTION"), 2);
        this._addColumnRight(getSession().getLabel("NAOS_AFFILIES_SOLDE_AFF_RAD_COL_SOLDE_SECTION"), 1);

        _groupOnMethod("getAffilieNumero");
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * setter pour l'attribut from date.
     * 
     * @param fromDate
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    @Override
    protected void summary() throws FWIException {
        if (_getManager().size() == 0) {
            this._addLine("", "-", "");
        }
    }
}
