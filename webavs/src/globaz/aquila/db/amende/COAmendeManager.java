/**
 *
 */
package globaz.aquila.db.amende;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;

/**
 * @author sch
 */
public class COAmendeManager extends BManager {
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
    private String forIdSection = null;
    private String forSelectionRole = null;
    private String forSelectionTriCompteAnnexe = null;
    private String fromDate = null;
    private String fromNoCompteAnnexe = null;
    private String idTypeOperation = null;
    private String untilDate = null;
    private String untilNoCompteAnnexe = null;

    @Override
    protected String _getFields(BStatement statement) {
        return "ru." + CARubrique.FIELD_IDRUBRIQUE + ", ru." + CARubrique.FIELD_IDEXTERNE + ", SUM("
                + CAOperation.FIELD_MONTANT + ") as somme ";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAOperation.TABLE_CAOPERP + " op" + COAmendeManager.INNER_JOIN + _getCollection()
                + CARubrique.TABLE_CARUBRP + " ru" + COAmendeManager.ON_DB_OPERAND + "op." + CAOperation.FIELD_IDCOMPTE
                + COAmendeManager.EQUAL_DB_OPERAND + "ru." + CARubrique.FIELD_IDRUBRIQUE;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // récupère l'id de la section
        sqlWhere += "op." + CAOperation.FIELD_IDSECTION + COAmendeManager.EQUAL_DB_OPERAND
                + this._dbWriteNumeric(statement.getTransaction(), getForIdSection());

        // Solde état = comptabilisé
        sqlWhere += COAmendeManager.AND_DB_OPERAND;
        sqlWhere += "op." + CAOperation.FIELD_ETAT + COAmendeManager.EQUAL_DB_OPERAND + APIOperation.ETAT_COMPTABILISE;

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
        return new COAmende();
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public String getForIdSection() {
        return forIdSection;
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
        return COAmendeManager.GROUP_BY + "ru." + CARubrique.FIELD_IDRUBRIQUE + ", ru." + CARubrique.FIELD_IDEXTERNE;
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

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
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
