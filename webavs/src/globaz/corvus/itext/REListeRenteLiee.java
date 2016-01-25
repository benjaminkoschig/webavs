package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.vb.rentesaccordees.RERenteLieeJointRenteAccordeeListViewBean;
import globaz.corvus.vb.rentesaccordees.RERenteLieeJointRenteAccordeeViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.dynamique.FWIDocumentTable;
import globaz.framework.printing.itext.dynamique.FWITableModel;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

public class REListeRenteLiee extends FWIAbstractManagerDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat = "";
    private boolean forEnCours = false;
    private String forGenre;
    private String forIdTiersLiant;

    public REListeRenteLiee() {
        // session, prefix, Compagnie, Titre, manager, application
        super(null, "Rentes_liees", "GLOBAZ", "Liste des rentes liées",
                new RERenteLieeJointRenteAccordeeListViewBean(), "Corvus");
    }

    @Override
    public void _beforeExecuteReport() {
        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.LISTE_PRESTATIONS_APG);

        RERenteLieeJointRenteAccordeeListViewBean manager = (RERenteLieeJointRenteAccordeeListViewBean) _getManager();

        manager.setSession(getSession());
        manager.setForCsEtat(forCsEtat);
        manager.setForGenre(forGenre);
        manager.setForIdTiersLiant(forIdTiersLiant);
        manager.setIsRechercheRenteEnCours(forEnCours);

        try {
            if (manager.getCount(getTransaction()) == 0) {
                addRow(new RERenteAccJoinTblTiersJoinDemandeRente());
            }
        } catch (Exception ex) {
            getMemoryLog().logMessage(ex.getMessage(), ex.getClass().getName(), REListeDemandeRente.class.getName());
        }

        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
    }

    @Override
    protected void addRow(BEntity entity) throws FWIException {

        RERenteLieeJointRenteAccordeeViewBean rente = (RERenteLieeJointRenteAccordeeViewBean) entity;

        // Détail du requérant
        StringBuilder detailRequerantBuilder = new StringBuilder();
        detailRequerantBuilder.append(rente.getNssTiersBeneficiaire());
        if (!JadeStringUtil.isBlank(rente.getDateDecesTiersBeneficiaire())) {
            detailRequerantBuilder.append(" ( † ").append(rente.getDateDecesTiersBeneficiaire()).append(")");
        }
        detailRequerantBuilder.append("\n");
        detailRequerantBuilder.append(rente.getNomPrenomTiersBeneficiaire()).append(" / ");
        detailRequerantBuilder.append(rente.getDateNaissanceTiersBeneficiaire()).append(" / ");
        detailRequerantBuilder.append(rente.getSexeTiersBeneficiaire()).append(" / ");
        detailRequerantBuilder.append(rente.getNationaliteTiersBeneficiaire());
        _addCell(detailRequerantBuilder.toString());

        // Genre
        _addCell(rente.getCodePrestation());

        // Période du droit
        StringBuilder periodeDroitBuilder = new StringBuilder();
        periodeDroitBuilder.append(rente.getDateDebutDroit()).append("-").append(rente.getDateFinDroit());
        _addCell(periodeDroitBuilder.toString());

        // Retenues / Bloquage
        _addCell(getRetenueBlocageLibelle(rente));

        // Montant
        _addCell(new FWCurrency(rente.getMontantPrestation()).toStringFormat());

        // Etat de la demande
        _addCell(rente.getSession().getCodeLibelle(rente.getCsEtat()));

        // Date d'échéance
        _addCell(rente.getDateEcheance());

        // N° de la prestation
        _addCell(rente.getIdPrestationAccordee());
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForGenre() {
        return forGenre;
    }

    public String getForIdTiersLiant() {
        return forIdTiersLiant;
    }

    private String getRetenueBlocageLibelle(RERenteLieeJointRenteAccordeeViewBean rente) {
        try {
            if (rente.getIsPrestationBloquee().booleanValue() && rente.getIsRetenues().booleanValue()) {
                return rente.getSession().getApplication()
                        .getLabel("JSP_RAC_L_R_B", rente.getSession().getUserInfo().getLanguage());
            } else if (rente.getIsPrestationBloquee().booleanValue()) {
                return rente.getSession().getApplication()
                        .getLabel("JSP_RAC_L_B", rente.getSession().getUserInfo().getLanguage());
            } else if (rente.getIsRetenues().booleanValue()) {
                return rente.getSession().getApplication()
                        .getLabel("JSP_RAC_L_R", rente.getSession().getUserInfo().getLanguage());
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void initializeTable() {
        FWIDocumentTable tblTotauxGenre = new FWIDocumentTable();

        try {
            tblTotauxGenre.addColumn(getSession().getLabel("LISTE_RENTE_LIEE_CRITERES_SELECTION"), FWITableModel.LEFT,
                    10);
            tblTotauxGenre.endTableDefinition();
        } catch (FWIException ex) {
            getMemoryLog().logMessage(ex.getMessage(), ex.getClass().getName(), REListeDemandeRente.class.getName());
        }

        StringBuilder criteres = new StringBuilder();

        if (!JadeStringUtil.isEmpty(forIdTiersLiant)) {
            criteres.append(getSession().getLabel("LISTE_RENTE_LIEE_CRITERES_NNSS"));
            criteres.append(":");
            try {
                criteres.append(PRTiersHelper.getTiersParId(getSession(), forIdTiersLiant).getProperty(
                        PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
            } catch (Exception ex) {
                criteres.append("unknown");
            }
        }

        if (!JadeStringUtil.isEmpty(forCsEtat)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_RENTE_LIEE_CRITERES_ETAT_DEMANDE"));
            criteres.append(":");
            criteres.append(forCsEtat);
        }

        if (!JadeStringUtil.isEmpty(forGenre)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_RENTE_LIEE_CRITERES_GENRE"));
            criteres.append(":");
            criteres.append(forGenre);
        }

        tblTotauxGenre.addCell(criteres.toString());

        try {
            tblTotauxGenre.addRow();
            tblTotauxGenre.addRow();

            super._addTable(tblTotauxGenre);
        } catch (FWIException ex) {
            getMemoryLog().logMessage(ex.getMessage(), FWIException.class.getName(),
                    REListeDemandeRente.class.getName());
        }

        // colonnes
        this._addColumnLeft(getSession().getLabel("LISTE_RENTE_LIEE_DETAIL_REQUERANT"), 25);
        this._addColumnCenter(getSession().getLabel("LISTE_RENTE_LIEE_GENRE"), 4);
        this._addColumnLeft(getSession().getLabel("LISTE_RENTE_LIEE_PERIODE"), 7);
        this._addColumnLeft(getSession().getLabel("LISTE_RENTE_LIEE_RETENUES_BLOCAGE"), 7);
        this._addColumnLeft(getSession().getLabel("LISTE_RENTE_LIEE_MONTANT"), 5);
        this._addColumnCenter(getSession().getLabel("LISTE_RENTE_LIEE_ETAT_DEMANDE"), 5);
        this._addColumnCenter(getSession().getLabel("LISTE_RENTE_LIEE_DATE_ECHEANCE"), 5);
        this._addColumnRight(getSession().getLabel("LISTE_RENTE_LIEE_NO_PRESTATION_ACCORDEE"), 5);
    }

    public boolean isForEnCours() {
        return forEnCours;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForEnCours(boolean forEnCours) {
        this.forEnCours = forEnCours;
    }

    public void setForGenre(String forGenre) {
        this.forGenre = forGenre;
    }

    public void setForIdTiersLiant(String forIdTiersLiant) {
        this.forIdTiersLiant = forIdTiersLiant;
    }
}
