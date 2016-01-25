package globaz.apg.db.prestation;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.lots.APLot;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * @author DVH
 */
public class APPrestationJointLotTiersDroitManager extends APPrestationManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsSexe = "";
    private String forDateNaissance = "";
    private String forNoLot = "";
    private String forTypeDroit = "";
    private String fromClause = null;
    private String fromDateDebutDroit = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer from = new StringBuffer(new APPrestationJointLotTiersDroit()._getFrom(statement));

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
            from.append(" LEFT JOIN " + _getCollection() + IPRTiers.TABLE_AVS_HIST + " AS " + IPRTiers.TABLE_AVS_HIST
                    + " ON (" + _getCollection() + IPRTiers.TABLE_AVS + "." + IPRTiers.FIELD_TI_IDTIERS + " = "
                    + IPRTiers.TABLE_AVS_HIST + "." + IPRTiers.FIELD_TI_IDTIERS + ")");
        }

        return from.toString();

    }

    @Override
    protected String _getOrder(BStatement statement) {
        return super._getOrder(statement) + ", " + APPrestation.FIELDNAME_IDPRESTATIONAPG;
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String superWhere = super._getWhere(statement);
        if (superWhere != null) {
            sql.append(superWhere);
        }

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {

            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS()));
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(".")
                    .append(ITITiersDefTable.DESIGNATION_1_MAJ);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%"));
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(".")
                    .append(ITITiersDefTable.DESIGNATION_2_MAJ);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%"));
        }

        if (!JAUtil.isDateEmpty(forDateNaissance)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(_getCollection()).append(ITIPersonneDefTable.TABLE_NAME).append(".")
                    .append(ITIPersonneDefTable.DATE_NAISSANCE);
            sql.append("=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance));
        }

        if (!JAUtil.isDateEmpty(forCsSexe)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(_getCollection()).append(ITIPersonneDefTable.TABLE_NAME).append(".")
                    .append(ITIPersonneDefTable.CS_SEXE);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forCsSexe));
        }

        if (!JAUtil.isDateEmpty(getFromDateDebutDroit())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(_getCollection()).append(APDroitLAPG.TABLE_NAME_LAPG).append(".")
                    .append(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT);
            sql.append(">=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getFromDateDebutDroit()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForTypeDroit())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(_getCollection()).append(PRDemande.TABLE_NAME).append(".")
                    .append(PRDemande.FIELDNAME_TYPE_DEMANDE);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForTypeDroit()));
        }

        if (!JadeStringUtil.isIntegerEmpty(forNoLot)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(_getCollection()).append(APLot.TABLE_NAME).append(".").append(APLot.FIELDNAME_NOLOT);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forNoLot));
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new APPrestationJointLotTiersDroit();
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForNoLot() {
        return forNoLot;
    }

    public String getForTypeDroit() {
        return forTypeDroit;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getFromDateDebutDroit() {
        return fromDateDebutDroit;
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
        return ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL + " , " + APPrestation.FIELDNAME_GENRE_PRESTATION + " , "
                + APPrestation.FIELDNAME_DATEDEBUT;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForNoLot(String forNoLot) {
        this.forNoLot = forNoLot;
    }

    public void setForTypeDroit(String forTypeDroit) {
        this.forTypeDroit = forTypeDroit;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setFromDateDebutDroit(String fromDateDebutDroit) {
        this.fromDateDebutDroit = fromDateDebutDroit;
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
