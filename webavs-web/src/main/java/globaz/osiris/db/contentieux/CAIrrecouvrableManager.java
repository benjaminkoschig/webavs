package globaz.osiris.db.contentieux;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIEtape;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.translation.CACodeSystem;
import java.io.Serializable;

/**
 * Manager pour la liste des rentiers et irrécouvrables
 * 
 * @author SEL
 * 
 */
public class CAIrrecouvrableManager extends BManager implements Serializable {

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
    protected static final String TRUE_VALUE = "1";

    protected final static String WHERE = " WHERE ";
    protected final static String ZERO_VALUE = " 0 ";

    private String dateValue = null;

    @Override
    protected String _getFields(BStatement statement) {
        // return "se." + CASection.FIELD_IDCOMPTEANNEXE + ", ca." +
        // CACompteAnnexe.FIELD_IDROLE + ", ca." +
        // CACompteAnnexe.FIELD_IDEXTERNEROLE + ", ca." +
        // CACompteAnnexe.FIELD_DESCRIPTION +", se." + CASection.FIELD_IDEXTERNE
        // + ", se." + CASection.FIELD_DATE + ", se." + CASection.FIELD_SOLDE +
        // ", ca." + CACompteAnnexe.FIELD_IDCONTMOTIFBLOQUE;
        return "*";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer("");
        from.append(_getCollection() + CASection.TABLE_CASECTP + " se");
        from.append(CAIrrecouvrableManager.INNER_JOIN + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + " ca"
                + CAIrrecouvrableManager.ON_DB_OPERAND + "ca." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE
                + CAIrrecouvrableManager.EQUAL_DB_OPERAND + "se." + CASection.FIELD_IDCOMPTEANNEXE);
        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            from.append(CAIrrecouvrableManager.LEFT_OUTER_JOIN + _getCollection()
                    + CAEvenementContentieux.TABLE_CAEVCTP + " ev" + CAIrrecouvrableManager.ON_DB_OPERAND + "se."
                    + CASection.FIELD_IDLASTETAPECTX + CAIrrecouvrableManager.EQUAL_DB_OPERAND + "ev.idevecon");
            from.append(CAIrrecouvrableManager.LEFT_OUTER_JOIN + _getCollection() + "CAPECTP pe"
                    + CAIrrecouvrableManager.ON_DB_OPERAND + "pe.idParametreEtape"
                    + CAIrrecouvrableManager.EQUAL_DB_OPERAND + "ev.idParametreEtape");
            from.append(CAIrrecouvrableManager.LEFT_OUTER_JOIN + _getCollection() + "CAETCTP ec"
                    + CAIrrecouvrableManager.ON_DB_OPERAND + "pe.idEtape" + CAIrrecouvrableManager.EQUAL_DB_OPERAND
                    + "ec.idEtape");
        } else {
            from.append(CAIrrecouvrableManager.LEFT_OUTER_JOIN + _getCollection() + CAMotifContentieux.TABLE_CAMOCOP
                    + " mo" + CAIrrecouvrableManager.ON_DB_OPERAND + "mo." + CAMotifContentieux.FIELD_IDCOMPTEANNEXE
                    + CAIrrecouvrableManager.EQUAL_DB_OPERAND + "ca." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
        }
        return from.toString();
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
            StringBuffer sqlBuffer = new StringBuffer(CAIrrecouvrableManager.SELECT);
            String sqlFields = _getFields(statement);
            if ((sqlFields != null) && (sqlFields.trim().length() != 0)) {
                sqlBuffer.append(sqlFields);
            } else {
                sqlBuffer.append("*");
            }
            sqlBuffer.append(CAIrrecouvrableManager.FROM);

            sqlBuffer.append(_getFrom(statement));
            //
            String sqlWhere = _getWhere(statement);
            if ((sqlWhere != null) && (sqlWhere.trim().length() != 0)) {
                sqlBuffer.append(CAIrrecouvrableManager.WHERE);
                sqlBuffer.append(sqlWhere);
            }
            String sqlOrder = _getOrder(statement);
            if ((sqlOrder != null) && (sqlOrder.trim().length() != 0)) {
                sqlBuffer.append(CAIrrecouvrableManager.ORDER_BY);
                sqlBuffer.append(sqlOrder);
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
        StringBuffer sqlWhere = new StringBuffer("");
        // Irr ou rentier ancien ctx
        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            sqlWhere.append("(ca." + CACompteAnnexe.FIELD_IDCONTMOTIFBLOQUE + CAIrrecouvrableManager.EQUAL_DB_OPERAND
                    + CACodeSystem.CS_IRRECOUVRABLE);
            sqlWhere.append(CAIrrecouvrableManager.OR_DB_OPERAND);
            sqlWhere.append("ca." + CACompteAnnexe.FIELD_IDCONTMOTIFBLOQUE + CAIrrecouvrableManager.EQUAL_DB_OPERAND
                    + CACodeSystem.CS_RENTIER + ")");
        } else {
            sqlWhere.append("(mo." + CAMotifContentieux.FIELD_IDMOTIFBLOCAGE + CAIrrecouvrableManager.EQUAL_DB_OPERAND
                    + CACodeSystem.CS_IRRECOUVRABLE);
            sqlWhere.append(CAIrrecouvrableManager.OR_DB_OPERAND);
            sqlWhere.append("mo." + CAMotifContentieux.FIELD_IDMOTIFBLOCAGE + CAIrrecouvrableManager.EQUAL_DB_OPERAND
                    + CACodeSystem.CS_RENTIER + ")");
        }
        // CA blocage ctx
        sqlWhere.append(CAIrrecouvrableManager.AND_DB_OPERAND);
        sqlWhere.append("ca." + CACompteAnnexe.FIELD_CONTESTBLOQUE + CAIrrecouvrableManager.EQUAL_DB_OPERAND + "'1'");

        // section non-bloquée
        sqlWhere.append(CAIrrecouvrableManager.AND_DB_OPERAND);
        sqlWhere.append("se." + CASection.FIELD_CONTENTIEUXESTSUS + CAIrrecouvrableManager.EQUAL_DB_OPERAND + "'2'");

        // Section non-soldée
        sqlWhere.append(CAIrrecouvrableManager.AND_DB_OPERAND);
        sqlWhere.append("se." + CASection.FIELD_SOLDE + CAIrrecouvrableManager.GREATER_DB_OPERAND
                + CAIrrecouvrableManager.ZERO_VALUE);

        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            // Section sommation ancien ctx
            sqlWhere.append(CAIrrecouvrableManager.AND_DB_OPERAND);
            sqlWhere.append("ec.typeetape" + CAIrrecouvrableManager.EQUAL_DB_OPERAND + APIEtape.SOMMATION);

            // Date de valeur compris dans la période de blocage
            sqlWhere.append(CAIrrecouvrableManager.AND_DB_OPERAND);
            sqlWhere.append(getDateValue() + CAIrrecouvrableManager.GREATER_DB_OPERAND + "ca."
                    + CACompteAnnexe.FIELD_CONTDATEDEBBLOQUE);
            sqlWhere.append(CAIrrecouvrableManager.AND_DB_OPERAND);
            sqlWhere.append(getDateValue() + CAIrrecouvrableManager.SMALLER_DB_OPERAND + "ca."
                    + CACompteAnnexe.FIELD_CONTDATEFINBLOQUE);
        } else {
            // Section sommation AQUILA
            sqlWhere.append(CAIrrecouvrableManager.AND_DB_OPERAND);
            sqlWhere.append("se." + CASection.FIELD_IDLASTETATAQUILA + CAIrrecouvrableManager.EQUAL_DB_OPERAND
                    + APIEtape.SOMMATION_AQUILA);

            // Date de valeur compris dans la période de blocage
            sqlWhere.append(CAIrrecouvrableManager.AND_DB_OPERAND);
            sqlWhere.append(getDateValue() + CAIrrecouvrableManager.GREATER_DB_OPERAND + "mo."
                    + CAMotifContentieux.FIELD_DATEDEBUT);
            sqlWhere.append(CAIrrecouvrableManager.AND_DB_OPERAND);
            sqlWhere.append(getDateValue() + CAIrrecouvrableManager.SMALLER_DB_OPERAND + "mo."
                    + CAMotifContentieux.FIELD_DATEFIN);
        }

        sqlWhere.append(getGroupBy(statement));

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASection();
    }

    /**
     * @return the dateValue
     */
    public String getDateValue() {
        return dateValue;
    }

    private String getGroupBy(BStatement statement) {
        return "";
    }

    /**
     * @param dateValue
     *            the dateValue to set
     */
    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }
}
