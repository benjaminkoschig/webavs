package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteListViewBean;
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

public class REListeRenteAccordee extends FWIAbstractManagerDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCodeCasSpecial = "";
    private String forCsEtatDemande = "";
    private String forCsSexe = "";
    private String forCsTypeDemande = "";
    private String forDateNaissance = "";
    private String forDroitAu = "";
    private String forDroitDu = "";
    private Boolean forEnCours = false;
    private String forGenrePrestation = "";
    private String forNoBaseCalcul = "";
    private String forNoDemandeRente = "";
    private String forNoRenteAccordee = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    public REListeRenteAccordee() {
        // session, prefix, Compagnie, Titre, manager, application
        super(null, "Rentes", "GLOBAZ", "Liste des rentes accordées",
                new RERenteAccordeeJointDemandeRenteListViewBean(), "Corvus");
    }

    @Override
    public void _beforeExecuteReport() {
        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.LISTE_PRESTATIONS_APG);

        RERenteAccordeeJointDemandeRenteListViewBean manager = (RERenteAccordeeJointDemandeRenteListViewBean) _getManager();

        manager.setSession(getSession());
        manager.setForCodeCasSpecial(forCodeCasSpecial);
        manager.setForCsEtat(forCsEtatDemande);
        manager.setForCsSexe(forCsSexe);
        manager.setForCsTypeDemande(forCsTypeDemande);
        manager.setForDateNaissance(forDateNaissance);
        manager.setForDroitAu(forDroitAu);
        manager.setForDroitDu(forDroitDu);
        manager.setForNoRenteAccordee(forNoRenteAccordee);
        manager.setForNoBaseCalcul(forNoBaseCalcul);
        manager.setForNoDemandeRente(forNoDemandeRente);
        manager.setForGenrePrestation(forGenrePrestation);
        manager.setLikeNom(likeNom);
        manager.setLikeNumeroAVS(likeNumeroAVS);
        manager.setLikeNumeroAVSNNSS(likeNumeroAVSNNSS);
        manager.setLikePrenom(likePrenom);
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

        RERenteAccJoinTblTiersJoinDemandeRente rente = (RERenteAccJoinTblTiersJoinDemandeRente) entity;

        // Détail du requérant
        StringBuilder detailRequerantBuilder = new StringBuilder();
        detailRequerantBuilder.append(rente.getNumeroAvsBenef());
        if (!JadeStringUtil.isBlank(rente.getDateDecesBenef())) {
            detailRequerantBuilder.append(" ( † ").append(rente.getDateDecesBenef()).append(")");
        }
        detailRequerantBuilder.append("\n");
        detailRequerantBuilder.append(rente.getNomBenef()).append(" ").append(rente.getPrenomBenef()).append(" / ");
        detailRequerantBuilder.append(rente.getDateNaissanceBenef()).append(" / ");
        detailRequerantBuilder.append(rente.getSexeBenef()).append(" / ");
        detailRequerantBuilder.append(rente.getNationaliteBenef());
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

    public String getForCodeCasSpecial() {
        return forCodeCasSpecial;
    }

    public String getForCsEtatDemande() {
        return forCsEtatDemande;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForCsTypeDemande() {
        return forCsTypeDemande;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForDroitAu() {
        return forDroitAu;
    }

    public String getForDroitDu() {
        return forDroitDu;
    }

    public Boolean getForEnCours() {
        return forEnCours;
    }

    public String getForGenrePrestation() {
        return forGenrePrestation;
    }

    public String getForNoBaseCalcul() {
        return forNoBaseCalcul;
    }

    public String getForNoDemandeRente() {
        return forNoDemandeRente;
    }

    public String getForNoRenteAccordee() {
        return forNoRenteAccordee;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    private String getRetenueBlocageLibelle(RERenteAccJoinTblTiersJoinDemandeRente rente) {
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
            tblTotauxGenre.addColumn(getSession().getLabel("LISTE_RENTE_ACCORDEE_CRITERES_SELECTION"),
                    FWITableModel.LEFT, 10);
            tblTotauxGenre.endTableDefinition();
        } catch (FWIException ex) {
            getMemoryLog().logMessage(ex.getMessage(), ex.getClass().getName(), REListeDemandeRente.class.getName());
        }

        StringBuilder criteres = new StringBuilder();

        if (!JadeStringUtil.isEmpty(likeNumeroAVS)) {
            criteres.append(getSession().getLabel("LISTE_RENTE_ACCORDEE_CRITERES_NNSS"));
            criteres.append(":");
            criteres.append(likeNumeroAVS);
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_RENTE_ACCORDEE_CRITERES_NOM"));
            criteres.append(":");
            criteres.append(likeNom);
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_RENTE_ACCORDEE_CRITERES_PRENOM"));
            criteres.append(":");
            criteres.append(likePrenom);
        }

        if (!JadeStringUtil.isEmpty(forCsSexe)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_RENTE_ACCORDEE_CRITERES_SEXE"));
            criteres.append(":");
            criteres.append(getSession().getCodeLibelle(forCsSexe));
        }

        if (!JadeStringUtil.isEmpty(forCsEtatDemande)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_RENTE_ACCORDEE_CRITERES_ETAT_DEMANDE"));
            criteres.append(":");
            criteres.append(forCsEtatDemande);
        }

        if (!JadeStringUtil.isEmpty(forCsTypeDemande)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_RENTE_ACCORDEE_CRITERES_TYPE_DEMANDE"));
            criteres.append(":");
            criteres.append(forCsTypeDemande);
        }

        if (!JadeStringUtil.isEmpty(forDroitAu)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_RENTE_ACCORDEE_CRITERES_DROIT_AU"));
            criteres.append(":");
            criteres.append(forDroitAu);
        }

        if (!JadeStringUtil.isEmpty(forDroitDu)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_RENTE_ACCORDEE_CRITERES_DROIT_DU"));
            criteres.append(":");
            criteres.append(forDroitDu);
        }

        if (!JadeStringUtil.isEmpty(forDateNaissance)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_RENTE_ACCORDEE_CRITERES_DATE_NAISSANCE"));
            criteres.append(":");
            criteres.append(forDateNaissance);
        }

        if (!JadeStringUtil.isEmpty(forNoRenteAccordee)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_RENTE_ACCORDEE_CRITERES_NO_RENTE"));
            criteres.append(":");
            criteres.append(forNoRenteAccordee);
        }

        if (!JadeStringUtil.isEmpty(forNoBaseCalcul)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_RENTE_ACCORDEE_CRITERES_NO_BASE_CALCUL"));
            criteres.append(":");
            criteres.append(forNoBaseCalcul);
        }

        if (!JadeStringUtil.isEmpty(forNoDemandeRente)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_RENTE_ACCORDEE_CRITERES_NO_DEMANDE_RENTE"));
            criteres.append(":");
            criteres.append(forNoDemandeRente);
        }

        if (!JadeStringUtil.isEmpty(forGenrePrestation)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_RENTE_ACCORDEE_CRITERES_GENRE_PRESTATION"));
            criteres.append(":");
            criteres.append(forGenrePrestation);
        }

        if (!JadeStringUtil.isBlankOrZero(forCodeCasSpecial)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_RENTE_ACCORDEE_CRITERES_CODE_CAS_SPECIAL"));
            criteres.append(":");
            criteres.append(forCodeCasSpecial);
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
        this._addColumnLeft(getSession().getLabel("LISTE_RENTE_ACCORDEE_DETAIL_REQUERANT"), 25);
        this._addColumnCenter(getSession().getLabel("LISTE_RENTE_ACCORDEE_GENRE"), 4);
        this._addColumnLeft(getSession().getLabel("LISTE_RENTE_ACCORDEE_PERIODE"), 7);
        this._addColumnLeft(getSession().getLabel("LISTE_RENTE_ACCORDEE_RETENUES_BLOCAGE"), 7);
        this._addColumnLeft(getSession().getLabel("LISTE_RENTE_ACCORDEE_MONTANT"), 5);
        this._addColumnCenter(getSession().getLabel("LISTE_RENTE_ACCORDEE_ETAT_DEMANDE"), 5);
        this._addColumnCenter(getSession().getLabel("LISTE_RENTE_ACCORDEE_DATE_ECHEANCE"), 5);
        this._addColumnRight(getSession().getLabel("LISTE_RENTE_ACCORDEE_NO_PRESTATION_ACCORDEE"), 5);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setForCodeCasSpecial(String forCodeCasSpecial) {
        this.forCodeCasSpecial = forCodeCasSpecial;
    }

    public void setForCsEtatDemande(String forCsEtatDemande) {
        this.forCsEtatDemande = forCsEtatDemande;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForCsTypeDemande(String forCsTypeDemande) {
        this.forCsTypeDemande = forCsTypeDemande;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForDroitAu(String forDroitAu) {
        this.forDroitAu = forDroitAu;
    }

    public void setForDroitDu(String forDroitDu) {
        this.forDroitDu = forDroitDu;
    }

    public void setForEnCours(Boolean forEnCours) {
        this.forEnCours = forEnCours;
    }

    public void setForGenrePrestation(String forGenrePrestation) {
        this.forGenrePrestation = forGenrePrestation;
    }

    public void setForNoBaseCalcul(String forNoBaseCalcul) {
        this.forNoBaseCalcul = forNoBaseCalcul;
    }

    public void setForNoDemandeRente(String forNoDemandeRente) {
        this.forNoDemandeRente = forNoDemandeRente;
    }

    public void setForNoRenteAccordee(String forNoRenteAccordee) {
        this.forNoRenteAccordee = forNoRenteAccordee;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setLikeNumeroAVSNNSS(String likeNumeroAVSNSS) {
        likeNumeroAVSNNSS = likeNumeroAVSNSS;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }
}
