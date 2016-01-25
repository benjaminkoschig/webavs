/**
 *
 */
package globaz.osiris.db.bulletinSolde;

import globaz.aquila.api.helper.ICOEtapeHelper;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIEtape;
import globaz.osiris.api.APIOperation;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

/**
 * @author sch
 */
public class CABulletinSoldeManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final String AND_DB_OPERAND = " AND ";
    protected static final String BETWEEN_DB_OPERAND = " BETWEEN ";
    protected static final String DECIMAL_DB_OPERAND = " DECIMAL ";
    protected static final String DIFFERENT_DB_OPERAND = " <> ";
    protected static final String EQUAL_DB_OPERAND = " = ";
    protected static final String FALSE_VALUE = "2";

    protected final static String FROM = " FROM ";

    protected static final String GREATER_DB_OPERAND = " > ";

    protected static final String GREATER_EQUAL_DB_OPERAND = " >= ";
    protected static final String GROUP_BY = " GROUP BY ";
    protected static final String IN_DB_OPERAND = " IN ";
    protected final static String INNER_JOIN = " INNER JOIN ";
    protected final static String IS_NULL = " IS NULL ";
    protected final static String LEFT_OUTER_JOIN = " LEFT OUTER JOIN ";
    protected static final String LIKE_DB_OPERAND = " LIKE ";
    protected static final String ON_DB_OPERAND = " ON ";
    protected static final String OR_DB_OPERAND = " OR ";
    protected static final String ORDER_BY = " ORDER BY ";
    protected final static String SELECT = "SELECT ";
    protected static final String SMALLER_DB_OPERAND = " < ";
    protected static final String SMALLER_EQUAL_DB_OPERAND = " <= ";
    protected static final String SUBSTRING_DB_OPERAND = " SUBSTR ";

    protected final static String TRI_PAR_NOM = "2";
    protected static final String TRUE_VALUE = "1";

    protected final static String WHERE = " WHERE ";
    protected final static String ZERO_VALUE = " 0 ";

    private String documentDate = null;
    private String forIdJournal = null;
    private String forSelectionRole = null;
    private String forSelectionTriCompteAnnexe = null;
    private String fromDate = null;
    private String fromNoCompteAnnexe = null;
    private String idTypeOperation = null;
    private String untilDate = null;
    private String untilNoCompteAnnexe = null;

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sqlField = new StringBuilder();
        sqlField.append("t." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ", t." + CASection.FIELD_IDSECTION);
        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            sqlField.append(", " + "t." + "typeEtape");
        }

        return sqlField.toString();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sqlFrom = new StringBuilder();
        sqlFrom.append(_getCollection() + CACompteAnnexe.TABLE_CACPTAP + " ca" + CABulletinSoldeManager.INNER_JOIN
                + _getCollection() + CASection.TABLE_CASECTP + " se" + CABulletinSoldeManager.ON_DB_OPERAND + "ca."
                + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + CABulletinSoldeManager.EQUAL_DB_OPERAND + "se."
                + CASection.FIELD_IDCOMPTEANNEXE + CABulletinSoldeManager.INNER_JOIN + _getCollection()
                + CAOperation.TABLE_CAOPERP + " op" + CABulletinSoldeManager.ON_DB_OPERAND + "se."
                + CASection.FIELD_IDSECTION + CABulletinSoldeManager.EQUAL_DB_OPERAND + "op."
                + CAOperation.FIELD_IDSECTION);

        if (CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            sqlFrom.append("");
        } else {
            sqlFrom.append(CABulletinSoldeManager.LEFT_OUTER_JOIN + _getCollection() + "CAEVCTP ev"
                    + CABulletinSoldeManager.ON_DB_OPERAND + "se." + CASection.FIELD_IDLASTETAPECTX
                    + CABulletinSoldeManager.EQUAL_DB_OPERAND + "ev.idevecon" + CABulletinSoldeManager.LEFT_OUTER_JOIN
                    + _getCollection() + "CAPECTP pe" + CABulletinSoldeManager.ON_DB_OPERAND + "pe.idParametreEtape"
                    + CABulletinSoldeManager.EQUAL_DB_OPERAND + "ev.idParametreEtape"
                    + CABulletinSoldeManager.LEFT_OUTER_JOIN + _getCollection() + "CAETCTP ec"
                    + CABulletinSoldeManager.ON_DB_OPERAND + "pe.idEtape" + CABulletinSoldeManager.EQUAL_DB_OPERAND
                    + "ec.idEtape");
        }

        return sqlFrom.toString();
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "";
    }

    /**
     * Méthode surcharchée pour effectuer un select qui encadre le select de base afin d'effectuer un tri supplémentaire
     */
    @Override
    protected String _getSql(BStatement statement) {
        try {
            StringBuffer sqlBuffer = new StringBuffer(CABulletinSoldeManager.SELECT);
            String sqlFields = _getFields(statement);
            if ((sqlFields != null) && (sqlFields.trim().length() != 0)) {
                sqlBuffer.append(sqlFields);
            } else {
                sqlBuffer.append("*");
            }
            sqlBuffer.append(CABulletinSoldeManager.FROM);

            sqlBuffer.append("(" + CABulletinSoldeManager.SELECT + "ca." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
            sqlBuffer.append(", ");
            sqlBuffer.append("se." + CASection.FIELD_IDSECTION);
            if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
                sqlBuffer.append(", typeEtape");
            }

            sqlBuffer.append(CABulletinSoldeManager.FROM);

            sqlBuffer.append(_getFrom(statement));
            //
            String sqlWhere = _getWhere(statement);
            if ((sqlWhere != null) && (sqlWhere.trim().length() != 0)) {
                sqlBuffer.append(CABulletinSoldeManager.WHERE);
                sqlBuffer.append(sqlWhere);
            }
            String sqlOrder = _getOrder(statement);
            if ((sqlOrder != null) && (sqlOrder.trim().length() != 0)) {
                sqlBuffer.append(CABulletinSoldeManager.ORDER_BY);
                sqlBuffer.append(sqlOrder);
            }
            sqlBuffer.append(") t");
            sqlBuffer.append(CABulletinSoldeManager.INNER_JOIN + _getCollection() + CACompteAnnexe.TABLE_CACPTAP
                    + " c ");
            sqlBuffer.append(CABulletinSoldeManager.ON_DB_OPERAND + "t." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE
                    + CABulletinSoldeManager.EQUAL_DB_OPERAND + "c." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
            sqlBuffer.append(CABulletinSoldeManager.INNER_JOIN + _getCollection() + CASection.TABLE_CASECTP + " s ");
            sqlBuffer.append(CABulletinSoldeManager.ON_DB_OPERAND + "t." + CASection.FIELD_IDSECTION
                    + CABulletinSoldeManager.EQUAL_DB_OPERAND + "s." + CASection.FIELD_IDSECTION);
            // Id Externe role
            if (!JadeStringUtil.isBlank(getFromNoCompteAnnexe()) && !JadeStringUtil.isBlank(getUntilNoCompteAnnexe())) {
                sqlBuffer.append(CABulletinSoldeManager.WHERE + "c." + CACompteAnnexe.FIELD_IDEXTERNEROLE
                        + CABulletinSoldeManager.GREATER_EQUAL_DB_OPERAND
                        + this._dbWriteString(statement.getTransaction(), getFromNoCompteAnnexe()));
                sqlBuffer.append(CABulletinSoldeManager.AND_DB_OPERAND);
                sqlBuffer.append("c." + CACompteAnnexe.FIELD_IDEXTERNEROLE
                        + CABulletinSoldeManager.SMALLER_EQUAL_DB_OPERAND
                        + this._dbWriteString(statement.getTransaction(), getUntilNoCompteAnnexe()));
            }
            sqlBuffer.append(CABulletinSoldeManager.ORDER_BY);

            if (CABulletinSoldeManager.TRI_PAR_NOM.equals(getForSelectionTriCompteAnnexe())) {
                sqlBuffer.append("c." + CACompteAnnexe.FIELD_DESCRIPTION);
                sqlBuffer.append(", ");
                sqlBuffer.append("s." + CASection.FIELD_IDEXTERNE);
            } else {
                sqlBuffer.append("c." + CACompteAnnexe.FIELD_IDEXTERNEROLE);
                sqlBuffer.append(", ");
                sqlBuffer.append("s." + CASection.FIELD_IDEXTERNE);
            }

            return sqlBuffer.toString();
        } catch (Exception e) {
            JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSql() (" + e.toString() + ")");
            return "";
        }
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // Montant < 0
        sqlWhere += "op." + CAOperation.FIELD_MONTANT + CABulletinSoldeManager.SMALLER_DB_OPERAND
                + CABulletinSoldeManager.ZERO_VALUE;

        // Solde section > 0
        sqlWhere += CABulletinSoldeManager.AND_DB_OPERAND;
        sqlWhere += "se." + CASection.FIELD_SOLDE + CABulletinSoldeManager.GREATER_DB_OPERAND
                + CABulletinSoldeManager.ZERO_VALUE;

        // Paiement compensation <> 0
        sqlWhere += CABulletinSoldeManager.AND_DB_OPERAND;
        sqlWhere += "se." + CASection.FIELD_PMTCMP + CABulletinSoldeManager.DIFFERENT_DB_OPERAND
                + CABulletinSoldeManager.ZERO_VALUE;

        // Section contentieux est suspendu = false
        sqlWhere += CABulletinSoldeManager.AND_DB_OPERAND;
        sqlWhere += "se." + CASection.FIELD_CONTENTIEUXESTSUS + CABulletinSoldeManager.EQUAL_DB_OPERAND
                + this._dbWriteString(statement.getTransaction(), CABulletinSoldeManager.FALSE_VALUE);

        // Tenir compte de la date
        if (!JadeStringUtil.isBlank(getFromDate()) && !JadeStringUtil.isBlank(getUntilDate())) {
            sqlWhere += CABulletinSoldeManager.AND_DB_OPERAND;
            sqlWhere += CABulletinSoldeManager.DECIMAL_DB_OPERAND + "(" + CABulletinSoldeManager.SUBSTRING_DB_OPERAND
                    + "(" + "op." + "PSPY, 1, 8))" + CABulletinSoldeManager.BETWEEN_DB_OPERAND
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDate());
            sqlWhere += CABulletinSoldeManager.AND_DB_OPERAND;
            sqlWhere += this._dbWriteDateAMJ(statement.getTransaction(), getUntilDate());
        }

        // Type etape
        sqlWhere += CABulletinSoldeManager.AND_DB_OPERAND;
        // Test que la section n'est pas en poursuite (pas au contentieux ou étape inférieur à Poursuite)
        if (CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            sqlWhere += "se." + CASection.FIELD_IDLASTETATAQUILA + CABulletinSoldeManager.IN_DB_OPERAND + "("
                    + ICOEtapeHelper.ETAPE_POURSUITE_SQL_NOT_IN_FORMAT + ")";
        } else {
            sqlWhere += "( ec.typeEtape" + CABulletinSoldeManager.IS_NULL + CABulletinSoldeManager.OR_DB_OPERAND
                    + "ec.typeEtape" + CABulletinSoldeManager.IN_DB_OPERAND + "(" + APIEtape.RAPPEL + ","
                    + APIEtape.SOMMATION + "))";
        }

        // Etat
        sqlWhere += CABulletinSoldeManager.AND_DB_OPERAND;
        sqlWhere += "op." + CAOperation.FIELD_ETAT + CABulletinSoldeManager.IN_DB_OPERAND + "("
                + APIOperation.ETAT_COMPTABILISE + "," + APIOperation.ETAT_PROVISOIRE + ")";

        // Id role
        if (!JadeStringUtil.isBlank(getForSelectionRole())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CABulletinSoldeManager.AND_DB_OPERAND;
            }
            // sqlWhere += "ca." + CACompteAnnexe.FIELD_IDROLE + IN_DB_OPERAND +
            // "(" + _dbWriteString(statement.getTransaction(),
            // getForSelectionRole()) + ")";
            if (getForSelectionRole().indexOf(',') != -1) {
                String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);

                sqlWhere += "ca." + CACompteAnnexe.FIELD_IDROLE + CABulletinSoldeManager.IN_DB_OPERAND + "(";

                for (int idRole = 0; idRole < roles.length; ++idRole) {
                    if (idRole > 0) {
                        sqlWhere += ",";
                    }
                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), roles[idRole]);
                }

                sqlWhere += ")";
            } else {
                sqlWhere += "ca." + CACompteAnnexe.FIELD_IDROLE + CABulletinSoldeManager.EQUAL_DB_OPERAND
                        + this._dbWriteNumeric(statement.getTransaction(), getForSelectionRole());
            }
        }

        // Traitement du positionnement pour un id type operation
        if (!JadeStringUtil.isBlank(getIdTypeOperation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CABulletinSoldeManager.AND_DB_OPERAND;
            }
            sqlWhere += "op." + CAOperation.FIELD_IDTYPEOPERATION + CABulletinSoldeManager.LIKE_DB_OPERAND
                    + this._dbWriteString(statement.getTransaction(), getIdTypeOperation());
        } else {
            if (sqlWhere.length() != 0) {
                sqlWhere += CABulletinSoldeManager.AND_DB_OPERAND;
            }
            sqlWhere += "(op." + CAOperation.FIELD_IDTYPEOPERATION + CABulletinSoldeManager.LIKE_DB_OPERAND
                    + this._dbWriteString(statement.getTransaction(), "EP%");

            sqlWhere += CABulletinSoldeManager.OR_DB_OPERAND + "op." + CAOperation.FIELD_IDTYPEOPERATION
                    + CABulletinSoldeManager.LIKE_DB_OPERAND + this._dbWriteString(statement.getTransaction(), "AP%")
                    + ")";
        }

        // Traitement du positionnement pour un idJournal
        if (!JadeStringUtil.isBlank(getForIdJournal())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CABulletinSoldeManager.AND_DB_OPERAND;
            }
            sqlWhere += "op." + CAOperation.FIELD_IDJOURNAL + CABulletinSoldeManager.EQUAL_DB_OPERAND
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }

        sqlWhere += getGroupBy(statement);

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CABulletinSolde();
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    public String getForSelectionTriCompteAnnexe() {
        return forSelectionTriCompteAnnexe;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getFromNoCompteAnnexe() {
        return fromNoCompteAnnexe;
    }

    private String getGroupBy(BStatement statement) {
        StringBuilder sqlGroup = new StringBuilder();
        sqlGroup.append(CABulletinSoldeManager.GROUP_BY + "ca." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ", se."
                + CASection.FIELD_IDSECTION);

        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            sqlGroup.append(", " + "typeEtape");
        }

        return sqlGroup.toString();
    }

    public String getIdTypeOperation() {
        return idTypeOperation;
    }

    public String getUntilDate() {
        return untilDate;
    }

    public String getUntilNoCompteAnnexe() {
        return untilNoCompteAnnexe;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

    public void setForSelectionTriCompteAnnexe(String forSelectionTriCompteAnnexe) {
        this.forSelectionTriCompteAnnexe = forSelectionTriCompteAnnexe;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setFromNoCompteAnnexe(String fromNoCompteAnnexe) {
        this.fromNoCompteAnnexe = fromNoCompteAnnexe;
    }

    public void setIdTypeOperation(String idTypeOperation) {
        this.idTypeOperation = idTypeOperation;
    }

    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }

    public void setUntilNoCompteAnnexe(String untilNoCompteAnnexe) {
        this.untilNoCompteAnnexe = untilNoCompteAnnexe;
    }

}
