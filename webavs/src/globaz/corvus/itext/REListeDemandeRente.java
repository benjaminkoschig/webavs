package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.db.demandes.REDemandeRenteJointPrestationAccordee;
import globaz.corvus.vb.demandes.REDemandeRenteJointPrestationAccordeeListViewBean;
import globaz.corvus.vb.demandes.REDemandeRenteJointPrestationAccordeeViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.dynamique.FWIDocumentTable;
import globaz.framework.printing.itext.dynamique.FWITableModel;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BEntity;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;

/**
 * Générateur de liste pour les {@link REDemandeRenteJointPrestationAccordeeViewBean}
 * 
 * @author PBA
 * 
 */
public class REListeDemandeRente extends FWIAbstractManagerDocumentList {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtatDemande = "";
    private String forCsSexe = "";
    private String forCsType = "";
    private String forCsTypeCalcul = "";
    private String forDateDebut = "";
    private String forDateNaissance = "";
    private String forDroitAu = "";
    private String forDroitDu = "";
    private String forIdGestionnaire;
    private boolean isEnCours = false;
    private boolean isRechercheFamille = false;
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNSS;
    private String likePrenom = "";
    private String orderBy = "";

    public REListeDemandeRente() {
        // session, prefix, Compagnie, Titre, manager, application
        super(null, "Rentes", "GLOBAZ", "Liste des demandes de rente",
                new REDemandeRenteJointPrestationAccordeeListViewBean(), "Corvus");
    }

    @Override
    public void _beforeExecuteReport() {
        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.LISTE_PRESTATIONS_APG);

        REDemandeRenteJointPrestationAccordeeListViewBean manager = (REDemandeRenteJointPrestationAccordeeListViewBean) _getManager();

        manager.setSession(getSession());
        manager.setForCsEtatDemande(forCsEtatDemande);
        manager.setForCsSexe(forCsSexe);
        manager.setForCsTypeDemande(forCsType);
        manager.setForCsTypeCalcul(forCsTypeCalcul);
        manager.setForDateDebut(forDateDebut);
        manager.setForDateNaissance(forDateNaissance);
        manager.setForDroitAu(forDroitAu);
        manager.setForDroitDu(forDroitDu);
        manager.setForIdGestionnaire(forIdGestionnaire);
        manager.setEnCours(isEnCours);
        manager.setIsRechercheFamille(isRechercheFamille);
        manager.setLikeNom(likeNom);
        manager.setLikePrenom(likePrenom);
        manager.setLikeNumeroAVS(likeNumeroAVS);
        manager.setLikeNumeroAVSNNSS(likeNumeroAVSNSS);

        if (JadeStringUtil.isBlank(orderBy)) {
            orderBy = manager.getOrderByDefaut();
        }
        manager.setOrderBy(orderBy);

        try {
            if (manager.getCount(getTransaction()) == 0) {
                addRow(new REDemandeRenteJointPrestationAccordee());
            }
        } catch (Exception ex) {
            getMemoryLog().logMessage(ex.getMessage(), ex.getClass().getName(), REListeDemandeRente.class.getName());
        }

        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
    }

    @Override
    protected void addRow(BEntity entity) throws FWIException {
        REDemandeRenteJointPrestationAccordeeViewBean demande = (REDemandeRenteJointPrestationAccordeeViewBean) entity;

        // Détail de l'assuré
        StringBuilder detailAssureBuilder = new StringBuilder();
        detailAssureBuilder.append(demande.getNoAVS());
        if (!JadeStringUtil.isBlank(demande.getDateDeces())) {
            detailAssureBuilder.append(" ( † ").append(demande.getDateDeces()).append(")");
        }
        detailAssureBuilder.append("\n");
        detailAssureBuilder.append(demande.getNom()).append(" ").append(demande.getPrenom()).append(" / ");
        detailAssureBuilder.append(demande.getDateNaissance()).append(" / ");
        detailAssureBuilder.append(getLibelleCourtSexe(demande.getCsSexe())).append(" / ");
        detailAssureBuilder.append(getLibellePays(demande.getCsNationalite()));
        _addCell(detailAssureBuilder.toString());

        // Période du droit
        StringBuilder periodeDroitBuilder = new StringBuilder();
        periodeDroitBuilder.append(demande.getDateDebut());
        periodeDroitBuilder.append(" - ");
        periodeDroitBuilder.append(demande.getDateFin());
        _addCell(periodeDroitBuilder.toString());

        // Type de demande
        _addCell(getSession().getCodeLibelle(demande.getCsTypeDemande()));

        // Codes de prestation
        StringBuilder codesPrestationBuilder = new StringBuilder();
        boolean isFirst = true;
        for (int i = 0; i < demande.getCodesPrestation().size(); i++) {
            String codePrestation = demande.getCodesPrestation().get(i);

            if (!JadeStringUtil.isBlank(codePrestation)) {
                if (!isFirst) {
                    codesPrestationBuilder.append("-");
                }
                codesPrestationBuilder.append(codePrestation);

                if (isFirst) {
                    isFirst = false;
                }
            }
        }
        _addCell(codesPrestationBuilder.toString());

        // Date de réception
        _addCell(demande.getDateReception());

        // Etat de la demande
        _addCell(getSession().getCodeLibelle(demande.getCsEtatDemande()));

        // Gestionnaire
        try {
            _addCell(PRGestionnaireHelper.getNomGestionnaire(demande.getIdGestionnaire()));
        } catch (Exception ex) {
            _addCell("");
        }

        // No de demande
        _addCell(demande.getIdDemandeRente());
    }

    public String getForCsEtatDemande() {
        return forCsEtatDemande;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForCsType() {
        return forCsType;
    }

    public String getForCsTypeCalcul() {
        return forCsTypeCalcul;
    }

    public String getForDateDebut() {
        return forDateDebut;
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

    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    public String getLibelleCourtSexe(String csSexe) {
        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }
    }

    public String getLibellePays(String csNationalite) {
        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", csNationalite));
        }
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public String getOrderBy() {
        return orderBy;
    }

    @Override
    protected void initializeTable() {
        // affichage des criteres de recherche non "vides"
        FWIDocumentTable tblTotauxGenre = new FWIDocumentTable();

        try {
            tblTotauxGenre.addColumn(getSession().getLabel("LISTE_DEMANDE_RENTE_CRITERES_SELECTION"),
                    FWITableModel.LEFT, 10);
            tblTotauxGenre.endTableDefinition();
        } catch (FWIException ex) {
            getMemoryLog().logMessage(ex.getMessage(), ex.getClass().getName(), REListeDemandeRente.class.getName());
        }

        StringBuilder criteres = new StringBuilder();

        if (!JadeStringUtil.isEmpty(likeNumeroAVS)) {
            criteres.append(getSession().getLabel("LISTE_DEMANDE_RENTE_CRITERES_NNSS"));
            criteres.append(":");
            criteres.append(likeNumeroAVS);
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_DEMANDE_RENTE_CRITERES_NOM"));
            criteres.append(":");
            criteres.append(likeNom);
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_DEMANDE_RENTE_CRITERES_PRENOM"));
            criteres.append(":");
            criteres.append(likePrenom);
        }

        if (!JadeStringUtil.isEmpty(forDateNaissance)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_DEMANDE_RENTE_CRITERES_DATE_NAISSANCE"));
            criteres.append(":");
            criteres.append(forDateNaissance);
        }

        if (!JadeStringUtil.isEmpty(forCsSexe)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_DEMANDE_RENTE_CRITERES_SEXE"));
            criteres.append(":");
            criteres.append(getSession().getCodeLibelle(forCsSexe));
        }

        if (!JadeStringUtil.isEmpty(forCsEtatDemande)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_DEMANDE_RENTE_CRITERES_ETAT_DEMANDE"));
            criteres.append(":");
            criteres.append(getSession().getCodeLibelle(forCsEtatDemande));
        }

        if (!JadeStringUtil.isEmpty(forCsType)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_DEMANDE_RENTE_CRITERES_TYPE_DEMANDE"));
            criteres.append(":");
            criteres.append(getSession().getCodeLibelle(forCsType));
        }

        if (!JadeStringUtil.isEmpty(forCsTypeCalcul)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_DEMANDE_RENTE_CRITERES_TYPE_CALCUL"));
            criteres.append(":");
            criteres.append(getSession().getCodeLibelle(forCsTypeCalcul));
        }

        if (!JadeStringUtil.isEmpty(forDateDebut)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_DEMANDE_RENTE_CRITERES_DATE_DEBUT"));
            criteres.append(":");
            criteres.append(forDateDebut);
        }

        if (!JadeStringUtil.isEmpty(forDroitAu)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_DEMANDE_RENTE_CRITERES_DROIT_AU"));
            criteres.append(":");
            criteres.append(forDroitAu);
        }

        if (!JadeStringUtil.isEmpty(forDroitDu)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_DEMANDE_RENTE_CRITERES_DROIT_DU"));
            criteres.append(":");
            criteres.append(forDroitDu);
        }

        if (!JadeStringUtil.isEmpty(forIdGestionnaire)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_DEMANDE_RENTE_CRITERES_GESTIONNAIRE"));
            criteres.append(":");
            try {
                criteres.append(PRGestionnaireHelper.getNomGestionnaire(getForIdGestionnaire()));
            } catch (Exception ex) {
                criteres.append("?");
            }
        }

        if (isEnCours) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_DEMANDE_RENTE_CRITERES_EN_COURS"));
        }

        if (isRechercheFamille) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_DEMANDE_RENTE_CRITERES_RECHERCHE_FAMILLE"));
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
        this._addColumnLeft(getSession().getLabel("LISTE_DEMANDE_RENTE_DETAIL_REQUERANT"), 22);
        this._addColumnLeft(getSession().getLabel("LISTE_DEMANDE_RENTE_PERIODE"), 8);
        this._addColumnLeft(getSession().getLabel("LISTE_DEMANDE_RENTE_TYPE_CALCUL"), 6);
        this._addColumnLeft(getSession().getLabel("LISTE_DEMANDE_RENTE_CODE_PRESTATION"), 9);
        this._addColumnLeft(getSession().getLabel("LISTE_DEMANDE_RENTE_DATE_RECEPTION"), 6);
        this._addColumnLeft(getSession().getLabel("LISTE_DEMANDE_RENTE_ETAT_DEMANDE"), 5);
        this._addColumnLeft(getSession().getLabel("LISTE_DEMANDE_RENTE_GESTIONNAIRE"), 6);
        this._addColumnRight(getSession().getLabel("LISTE_DEMANDE_RENTE_NO_DEMANDE"), 5);
    }

    public boolean isEnCours() {
        return isEnCours;
    }

    public boolean isRechercheFamille() {
        return isRechercheFamille;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setForCsEtatDemande(String forCsEtatDemande) {
        this.forCsEtatDemande = forCsEtatDemande;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForCsType(String forCsType) {
        this.forCsType = forCsType;
    }

    public void setForCsTypeCalcul(String forCsTypeCalcul) {
        this.forCsTypeCalcul = forCsTypeCalcul;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
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

    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    public void setIsEnCours(boolean isEnCours) {
        this.isEnCours = isEnCours;
    }

    public void setIsRechercheFamille(boolean isRechercheFamille) {
        this.isRechercheFamille = isRechercheFamille;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setLikeNumeroAVSNSS(String likeNumeroAVSNSS) {
        this.likeNumeroAVSNSS = likeNumeroAVSNSS;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
