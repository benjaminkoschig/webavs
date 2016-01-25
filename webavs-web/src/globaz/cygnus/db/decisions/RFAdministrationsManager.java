package globaz.cygnus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRStringUtils;

public class RFAdministrationsManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // fields
    public static final String F_CANTON = "HBTCAN";
    public static final String F_CODE_ADMIN = "HBCADM";
    public static final String F_DESIGNATION1 = "HTLDE1";
    public static final String F_DESIGNATION2 = "HTLDE2";
    public static final String F_DESIGNATION1_UPPER = "HTLDU1";
    public static final String F_DESIGNATION2_UPPER = "HTLDU2";
    public static final String F_IDTIER = "HTITIE";
    // sql
    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String LIKE = " LIKE ";
    public static final String ON = " ON ";
    // tables
    public static final String T_ADMIN = "TIADMIP";
    public static final String T_TIER = "TITIERP";

    private String forCanton;
    private String forCodeAdministrationLike;
    // Attributs
    private String forDesignation1Like;
    private String forDesignation2Like;
    private transient String fromClause = null;

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {

        String schema = _getCollection();

        StringBuffer fields = new StringBuffer();

        if (!JadeStringUtil.isBlank(fields.toString())) {
            fields.append(",");
        }
        fields.append(schema + RFAdministrationsManager.T_ADMIN + "." + RFAdministrationsManager.F_CANTON);

        if (!JadeStringUtil.isBlank(fields.toString())) {
            fields.append(",");
        }
        fields.append(schema + RFAdministrationsManager.T_ADMIN + "." + RFAdministrationsManager.F_CODE_ADMIN);

        if (!JadeStringUtil.isBlank(fields.toString())) {
            fields.append(",");
        }
        fields.append(schema + RFAdministrationsManager.T_TIER + "." + RFAdministrationsManager.F_DESIGNATION1);

        if (!JadeStringUtil.isBlank(fields.toString())) {
            fields.append(",");
        }
        fields.append(schema + RFAdministrationsManager.T_TIER + "." + RFAdministrationsManager.F_DESIGNATION2);

        if (!JadeStringUtil.isBlank(fields.toString())) {
            fields.append(",");
        }
        fields.append(schema + RFAdministrationsManager.T_TIER + "." + RFAdministrationsManager.F_IDTIER);

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFAdministrations.createFromClause(_getCollection()));

            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return RFAdministrations.F_DESIGNATION1;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();
        String schema = _getCollection();

        if (!JadeStringUtil.isEmpty(forCanton)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(schema + RFAdministrationsManager.T_ADMIN + "." + RFAdministrationsManager.F_CANTON);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCanton));
        }

        if (!JadeStringUtil.isEmpty(forCodeAdministrationLike)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(schema + RFAdministrationsManager.T_ADMIN + "." + RFAdministrationsManager.F_CODE_ADMIN);
            sqlWhere.append(" LIKE ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(forCodeAdministrationLike) + "%"));
        }

        if (!JadeStringUtil.isEmpty(forDesignation1Like)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(schema + RFAdministrationsManager.T_TIER + "."
                    + RFAdministrationsManager.F_DESIGNATION1_UPPER);
            sqlWhere.append(" LIKE ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(forDesignation1Like) + "%"));
        }

        if (!JadeStringUtil.isEmpty(forDesignation2Like)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(schema + RFAdministrationsManager.T_TIER + "."
                    + RFAdministrationsManager.F_DESIGNATION2_UPPER);
            sqlWhere.append(" LIKE ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(forDesignation2Like) + "%"));
        }
        return sqlWhere.toString();
    }

    // Méthodes
    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFAdministrations();
    }

    public String getForCanton() {
        return forCanton;
    }

    public String getForCodeAdministrationLike() {
        return forCodeAdministrationLike;
    }

    public String getForDesignation1Like() {
        return forDesignation1Like;
    }

    public String getForDesignation2Like() {
        return forDesignation2Like;
    }

    public void setForCanton(String forCanton) {
        this.forCanton = forCanton;
    }

    public void setForCodeAdministrationLike(String forCodeAdministrationLike) {
        this.forCodeAdministrationLike = forCodeAdministrationLike;
    }

    public void setForDesignation1Like(String forDesignation1Like) {
        this.forDesignation1Like = forDesignation1Like;
    }

    public void setForDesignation2Like(String forDesignation2Like) {
        this.forDesignation2Like = forDesignation2Like;
    }
}
