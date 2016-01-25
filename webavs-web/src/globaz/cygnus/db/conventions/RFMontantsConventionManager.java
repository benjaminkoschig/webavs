// créé le 24 mars 2010
package globaz.cygnus.db.conventions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * author fha
 */
public class RFMontantsConventionManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsTypeBeneficiairePc = "";
    private Boolean forEnCours = Boolean.FALSE;
    private String forFromDate = "";
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
    public RFMontantsConventionManager() {
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

        if (!JadeStringUtil.isEmpty(forFromDate)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forFromDate));
        }

        if (forEnCours.equals(Boolean.TRUE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" <= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(),
                    JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDMMYYYY)));

            sqlWhere.append(" AND (");

            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_FIN);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(),
                    JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDMMYYYY)));

            sqlWhere.append(" OR ");

            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_FIN);
            sqlWhere.append(" = 0)");
        }

        if (!JadeStringUtil.isEmpty(forCsTypeBeneficiairePc)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFMontantsConvention.FIELDNAME_TYPE_BENEFICIAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypeBeneficiairePc));
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
        return new RFMontantsConvention();
    }

    public String getForCsTypeBeneficiairePc() {
        return forCsTypeBeneficiairePc;
    }

    public Boolean getForEnCours() {
        return forEnCours;
    }

    public String getForFromDate() {
        return forFromDate;
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

    public void setForCsTypeBeneficiairePc(String forCsTypeBeneficiairePc) {
        this.forCsTypeBeneficiairePc = forCsTypeBeneficiairePc;
    }

    public void setForEnCours(Boolean forEnCours) {
        this.forEnCours = forEnCours;
    }

    public void setForFromDate(String forFromDate) {
        this.forFromDate = forFromDate;
    }

    public void setForIdConvention(String forIdConvention) {
        this.forIdConvention = forIdConvention;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

}
