// créé le 24 mars 2010
package globaz.cygnus.db.conventions;

import globaz.cygnus.api.IRFTypesBeneficiairePc;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * author fha
 * 
 * @Revision jje
 */
public class RFMontantsConventionDetailManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsGenrePc = "";
    private String forCsTypeBeneficiairePc = "";
    private String forCsTypePc = "";
    private String forDateDebut = "";
    private String forDateFin = "";
    private Boolean forEnCours = Boolean.FALSE;
    // ~ Attributes
    // --------------------------------------------------------------------------------------------------------
    private String forIdConvention = "";
    private String forOrderBy = "";

    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIDossiersJointTiersManager.
     */
    public RFMontantsConventionDetailManager() {
        super();
        wantCallMethodBeforeFind(false);
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFMontantsConvention.createFromClause(_getCollection()));
            fromClause = from.toString();
        }
        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer sqlOrder = new StringBuffer();
        if (!JadeStringUtil.isEmpty(forOrderBy)) {
            sqlOrder.append(forOrderBy);
        }
        return sqlOrder.toString();
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdConvention)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFMontantsConvention.FIELDNAME_ID_CONVENTION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdConvention));
        }

        /***************************************************************************************************/
        /*
         * if (!JadeStringUtil.isEmpty(this.forDateFin) && !JadeStringUtil.isEmpty(this.forDateDebut)) { if
         * (sqlWhere.length() != 0) { sqlWhere.append(" AND "); } sqlWhere.append("NOT(");
         * sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_FIN); sqlWhere.append(" < ");
         * sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), this.forDateDebut));
         * 
         * sqlWhere.append(" OR ");
         * 
         * sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), this.forDateFin)); sqlWhere.append(" < ");
         * sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_DEBUT); sqlWhere.append(")"); }
         */
        /*******************************************************************************************************/

        if (!JadeStringUtil.isBlankOrZero(forDateDebut)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            if (!JadeStringUtil.isBlankOrZero(forDateFin)) {
                sqlWhere.append(" (( ");
            } else {
                sqlWhere.append(" ( ");
            }
            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" <= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebut));
            sqlWhere.append(" AND (");
            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_FIN);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebut));
            sqlWhere.append(" OR ");
            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_FIN);
            sqlWhere.append(" = 0 ) ) ");
        }

        if (!JadeStringUtil.isBlankOrZero(forDateFin)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" OR ");
            }

            sqlWhere.append(" ( ");
            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" <= ");
            if (RFUtils.MAX_DATE_VALUE.equals(forDateFin)) {
                sqlWhere.append(forDateFin);
            } else {
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFin));
            }
            sqlWhere.append(" AND (");
            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_FIN);
            sqlWhere.append(" >= ");
            if (RFUtils.MAX_DATE_VALUE.equals(forDateFin)) {
                sqlWhere.append(forDateFin);
            } else {
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFin));
            }
            sqlWhere.append(" OR ");
            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_FIN);
            sqlWhere.append(" = 0 ))");

            sqlWhere.append(" OR ");
            sqlWhere.append(" ( ");

            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" >= ");
            if (RFUtils.MAX_DATE_VALUE.equals(forDateDebut)) {
                sqlWhere.append(forDateDebut);
            } else {
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebut));
            }
            sqlWhere.append(" AND (");
            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_FIN);
            sqlWhere.append(" <= ");
            if (RFUtils.MAX_DATE_VALUE.equals(forDateFin)) {
                sqlWhere.append(forDateFin);
            } else {
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFin));
            }
            sqlWhere.append(" AND ");
            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_FIN);
            sqlWhere.append(" <> 0 ))");
            sqlWhere.append(" ) ");
        }

        if (!JadeStringUtil.isEmpty(forCsTypeBeneficiairePc)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append("AND ((");
            }

            sqlWhere.append(RFMontantsConvention.FIELDNAME_TYPE_BENEFICIAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypeBeneficiairePc));
            sqlWhere.append(" OR ");
            sqlWhere.append(RFMontantsConvention.FIELDNAME_TYPE_BENEFICIAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IRFTypesBeneficiairePc.POUR_TOUS));
            sqlWhere.append(" OR ");
            sqlWhere.append(forCsTypeBeneficiairePc);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IRFTypesBeneficiairePc.POUR_TOUS));
            sqlWhere.append(")");
        }

        if (!JadeStringUtil.isEmpty(forCsTypePc)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND (");
            }

            sqlWhere.append(RFMontantsConvention.FIELDNAME_TYPE_PC);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypePc));
            sqlWhere.append(" OR ");
            sqlWhere.append(RFMontantsConvention.FIELDNAME_TYPE_PC);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IRFTypesBeneficiairePc.POUR_TOUS));
            sqlWhere.append(" OR ");
            sqlWhere.append(forCsTypePc);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IRFTypesBeneficiairePc.POUR_TOUS));
            sqlWhere.append(")");
        }

        if (!JadeStringUtil.isEmpty(forCsGenrePc)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND (");
            }

            sqlWhere.append(RFMontantsConvention.FIELDNAME_GENRE_PC);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsGenrePc));
            sqlWhere.append(" OR ");
            sqlWhere.append(RFMontantsConvention.FIELDNAME_GENRE_PC);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IRFTypesBeneficiairePc.POUR_TOUS));
            sqlWhere.append(" OR ");
            sqlWhere.append(forCsGenrePc);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IRFTypesBeneficiairePc.POUR_TOUS));
            sqlWhere.append("))");

        }

        return sqlWhere.toString();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFMontantsConventionDetail();
    }

    public String getForCsGenrePc() {
        return forCsGenrePc;
    }

    public String getForCsTypeBeneficiairePc() {
        return forCsTypeBeneficiairePc;
    }

    public String getForCsTypePc() {
        return forCsTypePc;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public Boolean getForEnCours() {
        return forEnCours;
    }

    public String getForIdConvention() {
        return forIdConvention;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return RFMontantsConvention.FIELDNAME_ID_MONTANT;
    }

    public void setForCsGenrePc(String forCsGenrePc) {
        this.forCsGenrePc = forCsGenrePc;
    }

    public void setForCsTypeBeneficiairePc(String forCsTypeBeneficiairePc) {
        this.forCsTypeBeneficiairePc = forCsTypeBeneficiairePc;
    }

    public void setForCsTypePc(String forCsTypePc) {
        this.forCsTypePc = forCsTypePc;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForEnCours(Boolean forEnCours) {
        this.forEnCours = forEnCours;
    }

    public void setForIdConvention(String forIdConvention) {
        this.forIdConvention = forIdConvention;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

}
