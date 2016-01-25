package globaz.naos.itext.affiliation;

import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.naos.db.affiliation.AFAffiliationJTiersCI;
import globaz.naos.db.affiliation.AFAffiliationJTiersCIManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.alternate.TIPAvsAdrLienAdmin;
import globaz.pyxis.db.alternate.TIPAvsAdrLienAdminManager;

public class AFAffiliationNAIndSansCI_DocListe extends FWIAbstractManagerDocumentList {

    private static final long serialVersionUID = -1164150744712608634L;
    public static final String AGENCE_COMMUNALE = "509031";

    // Numéro du document
    private static final String DOC_NO = "0116CAF";

    public AFAffiliationNAIndSansCI_DocListe() {
    }

    public AFAffiliationNAIndSansCI_DocListe(BSession session) throws Exception {
        super(session, "IND_NA_SANS_CI", "GLOBAZ", session.getLabel("NAOS_AFFILIES_IND_NA_SANS_CI_TITRE"),
                new AFAffiliationJTiersCIManager(), "NAOS");

        //
        AFAffiliationJTiersCIManager mgr = (AFAffiliationJTiersCIManager) _getManager();

        mgr.setSession(session);
        mgr.forAucunCIOuvert(Boolean.TRUE);
        mgr.setForTypeAffiliation(new String[] { CodeSystem.TYPE_AFFILI_INDEP, CodeSystem.TYPE_AFFILI_INDEP_EMPLOY,
                CodeSystem.TYPE_AFFILI_NON_ACTIF, CodeSystem.TYPE_AFFILI_SELON_ART_1A, CodeSystem.TYPE_AFFILI_TSE,
                CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE });

        setSendCompletionMail(true);
        setSendMailOnError(true);

    }

    /**
     * Méthode appelée avant l'exécution du rapport
     */
    @Override
    public void _beforeExecuteReport() {
        getDocumentInfo().setDocumentTypeNumber(AFAffiliationNAIndSansCI_DocListe.DOC_NO);
    }

    @Override
    protected void addRow(BEntity entity) throws FWIException {
        AFAffiliationJTiersCI affiliation = (AFAffiliationJTiersCI) entity;

        _addCell(affiliation.getAffilieNumero());
        _addCell(affiliation.getNumAVS());
        _addCell(affiliation.getPrenomNom());
        _addCell("");
        _addCell(getSession().getCodeLibelle(affiliation.getTypeAffiliation()));
        _addCell(affiliation.getDateDebut());

        try {
            TIPAvsAdrLienAdminManager lienAgence = new TIPAvsAdrLienAdminManager();
            lienAgence.setSession(entity.getSession());
            lienAgence.setForIdTiers(affiliation.getIdTiers());
            lienAgence.setForGenreAdministration(AFAffiliationNAIndSansCI_DocListe.AGENCE_COMMUNALE);

            lienAgence.find();
            if (lienAgence.size() != 0) {
                TIPAvsAdrLienAdmin agence = (TIPAvsAdrLienAdmin) lienAgence.getFirstEntity();
                _addCell(agence.getDesignation2Admin());
            } else {
                _addCell("vide");
            }
        } catch (Exception exception) {
            _addCell("exeption");
        }
    }

    @Override
    protected void initializeTable() {
        this._addColumnLeft(getSession().getLabel("NAOS_AFFILIES_IND_NA_SANS_CI_COL_NUMAFF"));
        this._addColumnLeft(getSession().getLabel("NAOS_AFFILIES_IND_NA_SANS_CI_COL_NUMAVS"));
        this._addColumnLeft(getSession().getLabel("NAOS_AFFILIES_IND_NA_SANS_CI_COL_NOM"));
        this._addColumnLeft("");
        this._addColumnLeft(getSession().getLabel("NAOS_AFFILIES_IND_NA_SANS_CI_COL_TYPE"));
        this._addColumnLeft(getSession().getLabel("NAOS_AFFILIES_IND_NA_SANS_CI_COL_DATE_DEBUT"));
        this._addColumnLeft(getSession().getLabel("NAOS_AFFILIES_IND_NA_SANS_CI_COL_AGENCE"));
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }
}
