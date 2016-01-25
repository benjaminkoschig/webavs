/*
 * Créé le 11 janvier 2009
 */
package globaz.cygnus.db.typeDeSoins;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jje
 */
public class RFSousTypeDeSoinJointAssPeriodeJointPotAssureManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forCodeSousTypeDeSoin = "";

    private String forCodeTypeDeSoin = "";
    private String forDateBetweenPeriode = "";

    private String forDateDebut = "";
    private String forDateFin = "";

    private String forIdSoinPot = "";
    private String forIdSousTypeSoin = "";

    private String forOrderBy = "";

    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFSousTypeDeSoinJointAssPeriodeJointPotAssureManager
     */
    public RFSousTypeDeSoinJointAssPeriodeJointPotAssureManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFSousTypeDeSoinJointAssPeriodeJointPotAssure.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer sqlOrder = new StringBuffer();
        sqlOrder.append("FHDDEP");
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

        if (!JadeStringUtil.isEmpty(forIdSoinPot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssTypeDeSoinPotAssure.FIELDNAME_ID_SOIN_POT);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdSoinPot));
        }

        if (!JadeStringUtil.isEmpty(forIdSousTypeSoin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssTypeDeSoinPotAssure.FIELDNAME_ID_SOUS_TYPE_SOIN);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdSousTypeSoin));
        }

        if (!JadeStringUtil.isEmpty(forDateDebut)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssTypeDeSoinPotAssure.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" >= ");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), forDateDebut));
        }

        if (!JadeStringUtil.isEmpty(forDateFin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssTypeDeSoinPotAssure.FIELDNAME_DATE_FIN);
            sqlWhere.append(" <= ");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), forDateFin));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCodeTypeDeSoin)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteString(statement.getTransaction(), forCodeTypeDeSoin));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCodeSousTypeDeSoin)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFSousTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteString(statement.getTransaction(), forCodeSousTypeDeSoin));
        }

        if (!JadeStringUtil.isBlankOrZero(forDateBetweenPeriode)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssTypeDeSoinPotAssure.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" <= ");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), forDateBetweenPeriode));
            sqlWhere.append(" AND ( ");
            sqlWhere.append(RFAssTypeDeSoinPotAssure.FIELDNAME_DATE_FIN);
            sqlWhere.append(" >= ");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), forDateBetweenPeriode));
            sqlWhere.append(" OR ");
            sqlWhere.append(RFAssTypeDeSoinPotAssure.FIELDNAME_DATE_FIN);
            sqlWhere.append(" = 0 )");
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFSousTypeDeSoinJointAssPeriodeJointPotAssure)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFSousTypeDeSoinJointAssPeriodeJointPotAssure();
    }

    public String getForCodeSousTypeDeSoin() {
        return forCodeSousTypeDeSoin;
    }

    public String getForCodeTypeDeSoin() {
        return forCodeTypeDeSoin;
    }

    public String getForDateBetweenPeriode() {
        return forDateBetweenPeriode;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdSoinPot() {
        return forIdSoinPot;
    }

    public String getForIdSousTypeSoin() {
        return forIdSousTypeSoin;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getFromClause() {
        return fromClause;
    }

    public void setForCodeSousTypeDeSoin(String forCodeSousTypeDeSoin) {
        this.forCodeSousTypeDeSoin = forCodeSousTypeDeSoin;
    }

    public void setForCodeTypeDeSoin(String forCodeTypeDeSoin) {
        this.forCodeTypeDeSoin = forCodeTypeDeSoin;
    }

    public void setForDateBetweenPeriode(String forDateBetweenPeriode) {
        this.forDateBetweenPeriode = forDateBetweenPeriode;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdSoinPot(String forIdSoinPot) {
        this.forIdSoinPot = forIdSoinPot;
    }

    public void setForIdSousTypeSoin(String forIdSousTypeSoin) {
        this.forIdSousTypeSoin = forIdSousTypeSoin;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
