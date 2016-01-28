package globaz.ij.db.prononces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRAbstractManagerHierarchique;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author DVH
 */
public class IJPrononceJointDemandeManager extends PRAbstractManagerHierarchique {

    private static final long serialVersionUID = 1L;
    public static final String CLE_DROITS_NON_COMMUNIQUES = "DROITS_NON_COMMUNIQUES";
    public static final String CLE_DROITS_TOUS = "DROITS_TOUS";

    private String csMotif = "";
    private String forCsEtatDemande = "";
    private String forCsEtatPrononce = "";
    private String forCsMotifEcheance = "";
    private String forCsSexe = "";
    private String forCsTypeIJ = "";
    private String forDateEcheance = "";
    private String forDateNaissance = "";
    private String forIdGestionnaire = "";
    private String forIdPrononce;
    private String forIdTiers = "";
    private String fromDateDebutPrononce = "";
    private String fromNom = "";
    private String fromNumeroAVS = "";
    private String fromPrenom = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer from = new StringBuffer(IJPrononceJointDemande.createFromClause(_getCollection()));

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
            from.append(" LEFT JOIN " + _getCollection() + IPRTiers.TABLE_AVS_HIST + " AS " + IPRTiers.TABLE_AVS_HIST
                    + " ON (" + _getCollection() + IPRTiers.TABLE_AVS + "." + IPRTiers.FIELD_TI_IDTIERS + " = "
                    + IPRTiers.TABLE_AVS_HIST + "." + IPRTiers.FIELD_TI_IDTIERS + ")");
        }

        return from.toString();

    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        String schema = _getCollection();

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS());
        }

        if (!JadeStringUtil.isEmpty(forCsEtatDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + PRDemande.TABLE_NAME + "." + PRDemande.FIELDNAME_ETAT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtatDemande);
        }

        if (!JadeStringUtil.isBlankOrZero(forCsTypeIJ)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + IJPrononce.TABLE_NAME_PRONONCE + "." + IJPrononce.FIELDNAME_CS_TYPE_IJ + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsTypeIJ);
        }

        if (!JadeStringUtil.isEmpty(forCsEtatPrononce)
                && !IJPrononceJointDemandeManager.CLE_DROITS_TOUS.equals(forCsEtatPrononce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (IJPrononceJointDemandeManager.CLE_DROITS_NON_COMMUNIQUES.equals(forCsEtatPrononce)) {
                sqlWhere += schema + IJPrononce.TABLE_NAME_PRONONCE + "." + IJPrononce.FIELDNAME_CS_ETAT + "<>"
                        + this._dbWriteNumeric(statement.getTransaction(), IIJPrononce.CS_COMMUNIQUE);

                sqlWhere += " AND ";

                sqlWhere += schema + IJPrononce.TABLE_NAME_PRONONCE + "." + IJPrononce.FIELDNAME_CS_ETAT + "<>"
                        + this._dbWriteNumeric(statement.getTransaction(), IIJPrononce.CS_ANNULE);

            } else {
                sqlWhere += schema + IJPrononce.TABLE_NAME_PRONONCE + "." + IJPrononce.FIELDNAME_CS_ETAT + "="
                        + this._dbWriteNumeric(statement.getTransaction(), forCsEtatPrononce);
            }
        }

        if (!JadeStringUtil.isEmpty(forIdGestionnaire)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + IJPrononce.TABLE_NAME_PRONONCE + "." + IJPrononce.FIELDNAME_ID_GESTIONNAIRE + "="
                    + this._dbWriteString(statement.getTransaction(), forIdGestionnaire);
        }

        if (!JAUtil.isDateEmpty(fromDateDebutPrononce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + IJPrononce.TABLE_NAME_PRONONCE + "." + IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE + ">="
                    + this._dbWriteDateAMJ(statement.getTransaction(), fromDateDebutPrononce);
        }

        if (!JAUtil.isDateEmpty(forDateNaissance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + IJPrononceJointDemande.TABLE_TIERS_DETAIL + "."
                    + IJPrononceJointDemande.FIELDNAME_DATE_NAISSANCE + "="
                    + this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance);
        }

        if (!JAUtil.isDateEmpty(forCsSexe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + IJPrononceJointDemande.TABLE_TIERS_DETAIL + "."
                    + IJPrononceJointDemande.FIELDNAME_SEXE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsSexe);
        }
        if (!JadeStringUtil.isIntegerEmpty(fromNom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema
                    + IJPrononceJointDemande.TABLE_TIERS
                    + "."
                    + IJPrononceJointDemande.FIELDNAME_NOM_FOR_SEARCH
                    + ">="
                    + this._dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(fromNom));
        }

        if (!JadeStringUtil.isEmpty(fromPrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema
                    + IJPrononceJointDemande.TABLE_TIERS
                    + "."
                    + IJPrononceJointDemande.FIELDNAME_PRENOM_FOR_SEARCH
                    + ">="
                    + this._dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(fromPrenom));
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema
                    + IJPrononceJointDemande.TABLE_TIERS
                    + "."
                    + IJPrononceJointDemande.FIELDNAME_NOM_FOR_SEARCH
                    + " LIKE "
                    + this._dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%");
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema
                    + IJPrononceJointDemande.TABLE_TIERS
                    + "."
                    + IJPrononceJointDemande.FIELDNAME_PRENOM_FOR_SEARCH
                    + " LIKE "
                    + this._dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%");
        }

        if (!JadeStringUtil.isEmpty(forDateEcheance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + IJPrononce.TABLE_NAME_PRONONCE + "."
                    + IJPrononceJointDemande.FIELDNAME_DATEECHEANCE_FOR_SEARCH + " <= "
                    + this._dbWriteNumeric(statement.getTransaction(), (forDateEcheance + "31")) + " AND "
                    + IJPrononceJointDemande.FIELDNAME_DATEECHEANCE_FOR_SEARCH + " IS NOT NULL" + " AND "
                    + IJPrononceJointDemande.FIELDNAME_DATEECHEANCE_FOR_SEARCH + " <> 0";

        }

        if (!JadeStringUtil.isEmpty(forIdTiers)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + PRDemande.TABLE_NAME + "." + PRDemande.FIELDNAME_IDTIERS + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdTiers);
        }

        if (!JadeStringUtil.isBlank(forIdPrononce)) {
            if (sqlWhere.length() > 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += schema + IJPrononce.TABLE_NAME_PRONONCE + "." + IJPrononce.FIELDNAME_ID_PRONONCE + "="
                    + forIdPrononce;
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJPrononceJointDemande();
    }

    public String getCsMotif() {
        return csMotif;
    }

    public String getForCsEtatDemande() {
        return forCsEtatDemande;
    }

    public String getForCsEtatPrononce() {
        return forCsEtatPrononce;
    }

    public String getForCsMotifEcheance() {
        return forCsMotifEcheance;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForCsTypeIJ() {
        return forCsTypeIJ;
    }

    public String getForDateEcheance() {
        return forDateEcheance;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    public String getForIdPrononce() {
        return forIdPrononce;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getFromDateDebutPrononce() {
        return fromDateDebutPrononce;
    }

    public String getFromNom() {
        return fromNom;
    }

    public String getFromNumeroAVS() {
        return fromNumeroAVS;
    }

    public String getFromPrenom() {
        return fromPrenom;
    }

    @Override
    public String getHierarchicalOrderBy() {
        return getOrderByDefaut();
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

    @Override
    public String getOrderByDefaut() {

        if ((!JadeStringUtil.isBlankOrZero(getLikeNumeroAVSNNSS()) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
            return IJPrononce.FIELDNAME_ID_PRONONCE + PRNSSUtil.SECONDARY_ORDER_BY;
        } else {
            return IJPrononce.FIELDNAME_ID_PRONONCE;
        }

    }

    public void setCsMotif(String csMotif) {
        this.csMotif = csMotif;
    }

    public void setForCsEtatDemande(String forCsEtatDemande) {
        this.forCsEtatDemande = forCsEtatDemande;
    }

    public void setForCsEtatPrononce(String forCsEtatPrononce) {
        this.forCsEtatPrononce = forCsEtatPrononce;
    }

    public void setForCsMotifEcheance(String forCsMotifEcheance) {
        this.forCsMotifEcheance = forCsMotifEcheance;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForCsTypeIJ(String forCsTypeIJ) {
        this.forCsTypeIJ = forCsTypeIJ;
    }

    public void setForDateEcheance(String forDateEcheance) {
        this.forDateEcheance = forDateEcheance;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    public void setForIdPrononce(String forIdPrononce) {
        this.forIdPrononce = forIdPrononce;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setFromDateDebutPrononce(String fromDateDebutPrononce) {
        this.fromDateDebutPrononce = fromDateDebutPrononce;
    }

    public void setFromNom(String fromNom) {
        this.fromNom = fromNom;
    }

    public void setFromNumeroAVS(String fromNumeroAVS) {
        this.fromNumeroAVS = fromNumeroAVS;
    }

    public void setFromPrenom(String fromPrenom) {
        this.fromPrenom = fromPrenom;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setLikeNumeroAVSNNSS(String likeNumeroAVSNNSS) {
        this.likeNumeroAVSNNSS = likeNumeroAVSNNSS;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }
}
