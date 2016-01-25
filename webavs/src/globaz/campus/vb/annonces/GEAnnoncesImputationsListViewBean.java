package globaz.campus.vb.annonces;

import globaz.campus.db.annonces.GEAnnonces;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

public class GEAnnoncesImputationsListViewBean extends BManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // etatAnnonce
    private static final String FIELD_ETAT_ANNONCE = GEAnnoncesImputationsViewBean.FIELDNAME_ETAT_ANNONCE + " "
            + GEAnnoncesImputationsViewBean.ALIAS_FIELDNAME_ETAT_ANNONCE;
    // idAnnonce
    private static final String FIELD_ID_ANNONCE = GEAnnoncesImputationsViewBean.FIELDNAME_ID_ANNONCE + " "
            + GEAnnoncesImputationsViewBean.ALIAS_FIELDNAME_ID_ANNONCE;
    // idAnnonceParent
    private static final String FIELD_ID_ANNONCE_PARENT = GEAnnoncesImputationsViewBean.FIELDNAME_ID_ANNONCE_PARENT
            + " " + GEAnnoncesImputationsViewBean.ALIAS_FIELDNAME_ID_ANNONCE_PARENT;
    // idAnnonce de l'imputation
    private static final String FIELD_ID_IMPUTATION = "MAX(" + GEAnnoncesImputationsViewBean.FIELDNAME_ID_IMPUTATION
            + ") " + GEAnnoncesImputationsViewBean.ALIAS_FIELDNAME_ID_IMPUTATION;
    // idLot
    private static final String FIELD_ID_LOT = GEAnnoncesImputationsViewBean.FIELDNAME_ID_LOT + " "
            + GEAnnoncesImputationsViewBean.ALIAS_FIELDNAME_ID_LOT;
    // isImputation
    private static final String FIELD_IS_IMPUTATION = GEAnnoncesImputationsViewBean.FIELDNAME_IS_IMPUTATION + " "
            + GEAnnoncesImputationsViewBean.ALIAS_FIELDNAME_IS_IMPUTATION;
    // nom
    private static final String FIELD_NOM = GEAnnoncesImputationsViewBean.FIELDNAME_NOM + " "
            + GEAnnoncesImputationsViewBean.ALIAS_FIELDNAME_NOM;
    // numAvs
    private static final String FIELD_NUM_AVS = GEAnnoncesImputationsViewBean.FIELDNAME_NUM_AVS + " "
            + GEAnnoncesImputationsViewBean.ALIAS_FIELDNAME_NUM_AVS;
    // numImatriculation
    private static final String FIELD_NUM_IMMATRICULATION = GEAnnoncesImputationsViewBean.FIELDNAME_NUM_IMMATRICULATION_TRANSMIS
            + " " + GEAnnoncesImputationsViewBean.ALIAS_FIELDNAME_NUM_IMMATRICULATION_TRANSMIS;
    // prenom
    private static final String FIELD_PRENOM = GEAnnoncesImputationsViewBean.FIELDNAME_PRENOM + " "
            + GEAnnoncesImputationsViewBean.ALIAS_FIELDNAME_PRENOM;
    private static final String GROUP_BY_PAR_DEFAUT = GEAnnoncesImputationsViewBean.FIELDNAME_ID_ANNONCE + ", "
            + GEAnnoncesImputationsViewBean.FIELDNAME_ID_LOT + ", "
            + GEAnnoncesImputationsViewBean.FIELDNAME_ETAT_ANNONCE + ", "
            + GEAnnoncesImputationsViewBean.FIELDNAME_NUM_AVS + ", " + GEAnnoncesImputationsViewBean.FIELDNAME_NOM
            + ", " + GEAnnoncesImputationsViewBean.FIELDNAME_PRENOM + ", "
            + GEAnnoncesImputationsViewBean.FIELDNAME_NUM_IMMATRICULATION_TRANSMIS + ", "
            + GEAnnoncesImputationsViewBean.FIELDNAME_ID_ANNONCE_PARENT + ", "
            + GEAnnoncesImputationsViewBean.FIELDNAME_IS_IMPUTATION + ", " + "decisions.maiaff, aff.malnaf";
    private static final String ORDER_BY_PAR_DEFAUT = GEAnnoncesImputationsViewBean.ALIAS_FIELDNAME_ID_LOT + ", "
            + GEAnnoncesImputationsViewBean.ALIAS_FIELDNAME_NUM_AVS + ", "
            + GEAnnoncesImputationsViewBean.ALIAS_FIELDNAME_NUM_IMMATRICULATION_TRANSMIS + ", "
            + GEAnnoncesImputationsViewBean.ALIAS_FIELDNAME_NOM + ", "
            + GEAnnoncesImputationsViewBean.ALIAS_FIELDNAME_PRENOM;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean forAnnoncesUniquement = new Boolean(false);
    private String forCsEtatAnnonce = null;
    private String forIdLot = null;
    private Boolean forImputationsUniquement = new Boolean(false);
    private String forNomLike = null;
    private String forNumAffilie = null;
    private String forNumAvs = null;
    private String forNumAvsLike = null;
    private String forNumImmatriculationTransmis = null;
    private String forPrenomLike = null;

    public GEAnnoncesImputationsListViewBean() {
        super();
    }

    @Override
    protected String _getFields(BStatement statement) {
        String sqlFields = FIELD_ID_ANNONCE + ", " + FIELD_ID_IMPUTATION + ", " + FIELD_ID_LOT + ", "
                + FIELD_ETAT_ANNONCE + ", " + FIELD_NUM_AVS + ", " + FIELD_NOM + ", " + FIELD_PRENOM + ", "
                + FIELD_NUM_IMMATRICULATION + ", " + FIELD_ID_ANNONCE_PARENT + ", " + FIELD_IS_IMPUTATION
                + ", decisions.maiaff, aff.malnaf";

        return sqlFields;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        String sqlFrom = _getCollection() + GEAnnonces.TABLE_NAME_ANNONCE + " "
                + GEAnnoncesImputationsViewBean.ALIAS_TABLE_NAME_ANNONCE + " LEFT OUTER JOIN " + _getCollection()
                + GEAnnonces.TABLE_NAME_ANNONCE + " " + GEAnnoncesImputationsViewBean.ALIAS_TABLE_NAME_IMPUTATION
                + " ON  " + GEAnnoncesImputationsViewBean.ALIAS_TABLE_NAME_ANNONCE + "."
                + GEAnnonces.FIELDNAME_ID_ANNONCE + "=" + GEAnnoncesImputationsViewBean.ALIAS_TABLE_NAME_IMPUTATION
                + "." + GEAnnonces.FIELDNAME_ID_ANNONCE_PARENT + " left join " + _getCollection()
                + "cpdecip decisions on decisions.iaidec=ANNONCE.YCIDEC" + " left join " + _getCollection()
                + "AFAFFIP aff on aff.MAIAFF = decisions.maiaff";
        return sqlFrom;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        String sqlOrder = ORDER_BY_PAR_DEFAUT;
        return sqlOrder;
    }

    @Override
    protected java.lang.String _getSql(BStatement statement) {
        try {
            StringBuffer sqlBuffer = new StringBuffer("SELECT ");
            String sqlFields = _getFields(statement);
            if ((sqlFields != null) && (sqlFields.trim().length() != 0)) {
                sqlBuffer.append(sqlFields);
            } else {
                sqlBuffer.append("*");
            }
            sqlBuffer.append(" FROM ");
            sqlBuffer.append(_getFrom(statement));
            //
            String sqlWhere = _getWhere(statement);
            if ((sqlWhere != null) && (sqlWhere.trim().length() != 0)) {
                sqlBuffer.append(" WHERE ");
                sqlBuffer.append(sqlWhere);
            }
            String sqlGroup = GROUP_BY_PAR_DEFAUT;
            if ((sqlGroup != null) && (sqlGroup.trim().length() != 0)) {
                sqlBuffer.append(" GROUP BY ");
                sqlBuffer.append(sqlGroup);
            }
            String sqlOrder = _getOrder(statement);
            if ((sqlOrder != null) && (sqlOrder.trim().length() != 0)) {
                sqlBuffer.append(" ORDER BY ");
                sqlBuffer.append(sqlOrder);
            }
            return sqlBuffer.toString();
        } catch (Exception e) {
            JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSql() (" + e.toString() + ")");
            return "";
        }
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (!JadeStringUtil.isBlank(getForIdLot())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnoncesImputationsViewBean.FIELDNAME_ID_LOT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdLot());
        }
        if (!JadeStringUtil.isBlank(getForCsEtatAnnonce())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnoncesImputationsViewBean.FIELDNAME_ETAT_ANNONCE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForCsEtatAnnonce());
        }
        if (!JadeStringUtil.isBlank(getForNumAvs())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnoncesImputationsViewBean.FIELDNAME_NUM_AVS + "="
                    + _dbWriteString(statement.getTransaction(), getForNumAvs());
        }
        if (!JadeStringUtil.isBlank(getForNumImmatriculationTransmis())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnoncesImputationsViewBean.FIELDNAME_NUM_IMMATRICULATION_TRANSMIS + "="
                    + _dbWriteString(statement.getTransaction(), getForNumImmatriculationTransmis());
        }
        if (!JadeStringUtil.isBlank(getForNomLike())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += "UPPER (" + GEAnnoncesImputationsViewBean.FIELDNAME_NOM + ") like UPPER("
                    + _dbWriteString(statement.getTransaction(), "%" + getForNomLike() + "%") + ")";
        }
        if (!JadeStringUtil.isBlank(getForPrenomLike())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += "UPPER (" + GEAnnoncesImputationsViewBean.FIELDNAME_PRENOM + ") like UPPER("
                    + _dbWriteString(statement.getTransaction(), "%" + getForPrenomLike() + "%") + ")";
        }
        // Que les annonces
        if (getForAnnoncesUniquement().booleanValue()) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnoncesImputationsViewBean.FIELDNAME_IS_IMPUTATION + "="
                    + _dbWriteBoolean(statement.getTransaction(), new Boolean(false), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // Que les imputations
        if (getForImputationsUniquement().booleanValue()) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnoncesImputationsViewBean.FIELDNAME_IS_IMPUTATION + "="
                    + _dbWriteBoolean(statement.getTransaction(), new Boolean(true), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // Pour les affiliés
        if (!JadeStringUtil.isEmpty(getForNumAffilie())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += " aff.malnaf like '" + getForNumAffilie() + "%'  ";
        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new GEAnnoncesImputationsViewBean();
    }

    public Boolean getForAnnoncesUniquement() {
        return forAnnoncesUniquement;
    }

    public String getForCsEtatAnnonce() {
        return forCsEtatAnnonce;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public Boolean getForImputationsUniquement() {
        return forImputationsUniquement;
    }

    public String getForNomLike() {
        return forNomLike;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public String getForNumAvs() {
        return forNumAvs;
    }

    public String getForNumAvsLike() {
        return forNumAvsLike;
    }

    public String getForNumImmatriculationTransmis() {
        return forNumImmatriculationTransmis;
    }

    public String getForPrenomLike() {
        return forPrenomLike;
    }

    public void setForAnnoncesUniquement(Boolean forAnnoncesUniquement) {
        this.forAnnoncesUniquement = forAnnoncesUniquement;
    }

    public void setForCsEtatAnnonce(String forCsEtatAnnonce) {
        this.forCsEtatAnnonce = forCsEtatAnnonce;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForImputationsUniquement(Boolean forImputationsUniquement) {
        this.forImputationsUniquement = forImputationsUniquement;
    }

    public void setForNomLike(String forNomLike) {
        this.forNomLike = forNomLike;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    public void setForNumAvs(String forNumAvs) {
        this.forNumAvs = forNumAvs;
    }

    public void setForNumAvsLike(String forNumAvsLike) {
        this.forNumAvsLike = forNumAvsLike;
    }

    public void setForNumImmatriculationTransmis(String forNumImmatriculationTransmis) {
        this.forNumImmatriculationTransmis = forNumImmatriculationTransmis;
    }

    public void setForPrenomLike(String forPrenomLike) {
        this.forPrenomLike = forPrenomLike;
    }

}
