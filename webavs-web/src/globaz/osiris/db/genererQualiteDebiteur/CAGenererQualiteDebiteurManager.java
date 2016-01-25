/**
 *
 */
package globaz.osiris.db.genererQualiteDebiteur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;

/**
 * @author sch
 */
public class CAGenererQualiteDebiteurManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ALL_CATEGORIE = "all";
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

    private String forIdCategorie = new String();
    private String forIdGenreCompte = new String();
    private String forIdSection = new String();
    private String forSelectionRole = new String();
    private String forSoldeGreater = new String();
    private String forSoldeNot = new String();
    private String fromDate = new String();
    private String lastEtatAquilaNotIn = new String();

    @Override
    protected String _getFields(BStatement statement) {
        return "co." + CAGenererQualiteDebiteur.FIELD_IDCONTENTIEUX + ", co."
                + CAGenererQualiteDebiteur.FIELD_IDCOMPTEANNEXE + ", co." + CAGenererQualiteDebiteur.FIELD_IDSECTION
                + ", " + "co." + CAGenererQualiteDebiteur.FIELD_NOMBRE_DELAI_MUTE + ", " + "co."
                + CAGenererQualiteDebiteur.FIELD_IDSEQUENCE + ", " + "se." + CASection.FIELD_CATEGORIESECTION;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CACompteAnnexe.TABLE_CACPTAP + " ca" + CAGenererQualiteDebiteurManager.INNER_JOIN
                + _getCollection() + CASection.TABLE_CASECTP + " se" + CAGenererQualiteDebiteurManager.ON_DB_OPERAND
                + "ca." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + CAGenererQualiteDebiteurManager.EQUAL_DB_OPERAND
                + "se." + CASection.FIELD_IDCOMPTEANNEXE + CAGenererQualiteDebiteurManager.INNER_JOIN
                + _getCollection() + CAGenererQualiteDebiteur.TABLE_COCAVSP + " co"
                + CAGenererQualiteDebiteurManager.ON_DB_OPERAND + "se." + CASection.FIELD_IDSECTION
                + CAGenererQualiteDebiteurManager.EQUAL_DB_OPERAND + "co." + CAGenererQualiteDebiteur.FIELD_IDSECTION;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "ca.idcompteannexe, se.idsection";
    }

    /**
     * Méthode surcharchée pour effectuer un select qui encadre le select de base afin d'effectuer un tri supplémentaire
     */
    @Override
    protected String _getSql(BStatement statement) {
        try {
            StringBuffer sqlBuffer = new StringBuffer(CAGenererQualiteDebiteurManager.SELECT);
            String sqlFields = _getFields(statement);
            if ((sqlFields != null) && (sqlFields.trim().length() != 0)) {
                sqlBuffer.append(sqlFields);
            } else {
                sqlBuffer.append("*");
            }

            sqlBuffer.append(CAGenererQualiteDebiteurManager.FROM);

            sqlBuffer.append(_getFrom(statement));
            //
            String sqlWhere = _getWhere(statement);
            if ((sqlWhere != null) && (sqlWhere.trim().length() != 0)) {
                sqlBuffer.append(CAGenererQualiteDebiteurManager.WHERE);
                sqlBuffer.append(sqlWhere);
            }
            String sqlOrder = _getOrder(statement);
            if ((sqlOrder != null) && (sqlOrder.trim().length() != 0)) {
                sqlBuffer.append(CAGenererQualiteDebiteurManager.ORDER_BY);
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
        String sqlWhere = "";

        // Traitement du positionnement pour une sélection du rôle
        if ((!JadeStringUtil.isBlank(getForSelectionRole())) && !getForSelectionRole().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getForSelectionRole().indexOf(',') != -1) {
                String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);

                sqlWhere += "ca.IDROLE IN (";

                for (int idRole = 0; idRole < roles.length; ++idRole) {
                    if (idRole > 0) {
                        sqlWhere += ",";
                    }
                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), roles[idRole]);
                }

                sqlWhere += ")";
            } else {
                sqlWhere += "ca.IDROLE=" + this._dbWriteNumeric(statement.getTransaction(), getForSelectionRole());
            }
        }
        // Traitement de la catégorie du compte
        if ((!JadeStringUtil.isBlank(getForIdCategorie()))
                && (!getForIdCategorie().equals(CAGenererQualiteDebiteurManager.ALL_CATEGORIE))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (getForIdCategorie().indexOf(',') != -1) {
                String[] categories = JadeStringUtil.split(getForIdCategorie(), ',', Integer.MAX_VALUE);

                sqlWhere += "ca.IDCATEGORIE IN (";

                for (int id = 0; id < categories.length; ++id) {
                    if (id > 0) {
                        sqlWhere += ",";
                    }

                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), categories[id]);
                }

                sqlWhere += ")";
            } else {
                sqlWhere += "ca.IDCATEGORIE = " + this._dbWriteNumeric(statement.getTransaction(), getForIdCategorie());
            }
        }
        // Traitement du genre de compte
        if (!JadeStringUtil.isBlank(getForIdGenreCompte())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ca.IDGENRECOMPTE = " + this._dbWriteNumeric(statement.getTransaction(), getForIdGenreCompte());
        }
        if (getFromDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "se." + CASection.FIELD_DATESECTION + ">="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDate());
        }
        // Traitement du positionnement lastEtatAquilaNotIn
        if (getLastEtatAquilaNotIn().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "se." + CASection.FIELD_IDLASTETATAQUILA + " IS NOT NULL AND " + "se."
                    + CASection.FIELD_IDLASTETATAQUILA + " NOT IN (" + getLastEtatAquilaNotIn() + ")";
        }
        // Traitement du positionnement pour un solde spécifique
        if (getForSoldeNot().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "se." + CASection.FIELD_SOLDE + "<>"
                    + this._dbWriteNumeric(statement.getTransaction(), getForSoldeNot());
        }
        // Traitement du positionnement pour un solde spécifique
        if (getForSoldeGreater().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "se." + CASection.FIELD_SOLDE + ">"
                    + this._dbWriteNumeric(statement.getTransaction(), getForSoldeGreater());
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
        return new CAGenererQualiteDebiteur();
    }

    /**
     * @return the forIdCategorie
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * @return the forIdGenreCompte
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * @return the forIdSection
     */
    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * @return the forSelectionRole
     */
    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * @return the forSoldeGreater
     */
    public String getForSoldeGreater() {
        return forSoldeGreater;
    }

    /**
     * @return the forSoldeNot
     */
    public String getForSoldeNot() {
        return forSoldeNot;
    }

    /**
     * @return the fromDate
     */
    public String getFromDate() {
        return fromDate;
    }

    private String getGroupBy(BStatement statement) {
        return "";
    }

    /**
     * @return the lastEtatAquilaNotIn
     */
    public String getLastEtatAquilaNotIn() {
        return lastEtatAquilaNotIn;
    }

    /**
     * @param forIdCategorie
     *            the forIdCategorie to set
     */
    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    /**
     * @param forIdGenreCompte
     *            the forIdGenreCompte to set
     */
    public void setForIdGenreCompte(String forIdGenreCompte) {
        this.forIdGenreCompte = forIdGenreCompte;
    }

    /**
     * @param forIdSection
     *            the forIdSection to set
     */
    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    /**
     * @param forSelectionRole
     *            the forSelectionRole to set
     */
    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

    /**
     * @param forSoldeGreater
     *            the forSoldeGreater to set
     */
    public void setForSoldeGreater(String forSoldeGreater) {
        this.forSoldeGreater = forSoldeGreater;
    }

    /**
     * @param forSoldeNot
     *            the forSoldeNot to set
     */
    public void setForSoldeNot(String forSoldeNot) {
        this.forSoldeNot = forSoldeNot;
    }

    /**
     * @param fromDate
     *            the fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @param lastEtatAquilaNotIn
     *            the lastEtatAquilaNotIn to set
     */
    public void setLastEtatAquilaNotIn(String lastEtatAquilaNotIn) {
        this.lastEtatAquilaNotIn = lastEtatAquilaNotIn;
    }

}
