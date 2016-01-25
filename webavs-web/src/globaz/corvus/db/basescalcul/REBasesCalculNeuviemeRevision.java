/*
 * Créé le 13 fev. 07
 */
package globaz.corvus.db.basescalcul;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class REBasesCalculNeuviemeRevision extends REBasesCalcul {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_BONIFICATION_TACHE_EDUCATIVE = "YJMBTE";

    /**
	 */
    public static final String FIELDNAME_CLE_INFIRMITE_EPOUSE = "YJLCIE";

    /**
	 */
    public static final String FIELDNAME_CODE_OFFICE_AI_EPOUSE = "YJNOAI";

    /**
	 */
    public static final String FIELDNAME_DEGRE_INVALIDITE_EPOUSE = "YJNDIN";

    /**
	 */
    public static final String FIELDNAME_ID_BASES_CALCUL_NEUVIEME_REVISION = "YJIBCA";

    /**
	 */
    public static final String FIELDNAME_IS_INVALIDE_PRECOCE_EPOUSE = "YJBIPR";

    /**
	 */
    public static final String FIELDNAME_NBR_ANNEE_EDUCATION = "YJDABE";

    /**
	 */
    public static final String FIELDNAME_SURVENANCE_EVENEMENT_ASSURE_EPOUSE = "YJDSEA";

    /**
     */
    public static final String TABLE_NAME_BASES_CALCUL_NEUVIEME_REVISION = "REBACNR";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append(TABLE_NAME_BASES_CALCUL_NEUVIEME_REVISION);

        // jointure avec la table des bases de calcul
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(TABLE_NAME_BASES_CALCUL);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_ID_BASES_CALCUL_NEUVIEME_REVISION);
        fromClause.append("=");
        fromClause.append(FIELDNAME_ID_BASES_DE_CALCUL);

        return fromClause.toString();
    }

    private String bonificationTacheEducative = "";
    private String cleInfirmiteEpouse = "";
    private String codeOfficeAiEpouse = "";
    private String degreInvaliditeEpouse = "";
    private Boolean isInvaliditePrecoceEpouse = Boolean.FALSE;
    private String nbrAnneeEducation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String survenanceEvenementAssureEpouse = "";

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
        // setCsTypeDemandeRente();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_BASES_CALCUL_NEUVIEME_REVISION;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idBasesCalcul = statement.dbReadNumeric(FIELDNAME_ID_BASES_CALCUL_NEUVIEME_REVISION);
        bonificationTacheEducative = statement.dbReadNumeric(FIELDNAME_BONIFICATION_TACHE_EDUCATIVE);
        nbrAnneeEducation = statement.dbReadString(FIELDNAME_NBR_ANNEE_EDUCATION);
        codeOfficeAiEpouse = statement.dbReadNumeric(FIELDNAME_CODE_OFFICE_AI_EPOUSE);
        degreInvaliditeEpouse = statement.dbReadNumeric(FIELDNAME_DEGRE_INVALIDITE_EPOUSE);
        cleInfirmiteEpouse = statement.dbReadString(FIELDNAME_CLE_INFIRMITE_EPOUSE);
        survenanceEvenementAssureEpouse = statement.dbReadNumeric(FIELDNAME_SURVENANCE_EVENEMENT_ASSURE_EPOUSE);
        isInvaliditePrecoceEpouse = statement.dbReadBoolean(FIELDNAME_IS_INVALIDE_PRECOCE_EPOUSE);

    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_BASES_CALCUL_NEUVIEME_REVISION,
                _dbWriteNumeric(statement.getTransaction(), getIdBasesCalcul()));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        if (_getAction() == ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(FIELDNAME_ID_BASES_CALCUL_NEUVIEME_REVISION,
                    _dbWriteNumeric(statement.getTransaction(), getIdBasesCalcul(), "idBasesCalcul"));
        }

        statement.writeField(FIELDNAME_BONIFICATION_TACHE_EDUCATIVE,
                _dbWriteNumeric(statement.getTransaction(), bonificationTacheEducative, "bonificationTacheEducative"));
        statement.writeField(FIELDNAME_NBR_ANNEE_EDUCATION,
                _dbWriteString(statement.getTransaction(), nbrAnneeEducation, "nbrAnneeEducation"));
        statement.writeField(FIELDNAME_CODE_OFFICE_AI_EPOUSE,
                _dbWriteNumeric(statement.getTransaction(), codeOfficeAiEpouse, "codeOfficeAiEpouse"));
        statement.writeField(FIELDNAME_DEGRE_INVALIDITE_EPOUSE,
                _dbWriteNumeric(statement.getTransaction(), degreInvaliditeEpouse, "degreInvaliditeEpouse"));
        statement.writeField(FIELDNAME_CLE_INFIRMITE_EPOUSE,
                _dbWriteString(statement.getTransaction(), cleInfirmiteEpouse, "cleInfirmiteEpouse"));
        statement.writeField(
                FIELDNAME_SURVENANCE_EVENEMENT_ASSURE_EPOUSE,
                _dbWriteNumeric(statement.getTransaction(), survenanceEvenementAssureEpouse,
                        "survenanceEvenementAssureEpouse"));
        statement.writeField(
                FIELDNAME_IS_INVALIDE_PRECOCE_EPOUSE,
                _dbWriteBoolean(statement.getTransaction(), isInvaliditePrecoceEpouse, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "isInvaliditePrecoceEpouse"));

    }

    /**
     * @return
     */
    public String getBonificationTacheEducative() {
        return bonificationTacheEducative;
    }

    /**
     * @return
     */
    public String getCleInfirmiteEpouse() {
        return cleInfirmiteEpouse;
    }

    /**
     * @return
     */
    public String getCodeOfficeAiEpouse() {
        return codeOfficeAiEpouse;
    }

    /**
     * @return
     */
    public String getDegreInvaliditeEpouse() {
        return degreInvaliditeEpouse;
    }

    /**
     * @return
     */
    public String getNbrAnneeEducation() {
        return nbrAnneeEducation;
    }

    /**
     * @return
     */
    public String getSurvenanceEvenementAssureEpouse() {
        return survenanceEvenementAssureEpouse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return true;
    }

    /**
     * @return
     */
    public Boolean isInvaliditePrecoceEpouse() {
        return isInvaliditePrecoceEpouse;
    }

    /**
     * @param string
     */
    public void setBonificationTacheEducative(String string) {
        bonificationTacheEducative = string;
    }

    /**
     * @param string
     */
    public void setCleInfirmiteEpouse(String string) {
        cleInfirmiteEpouse = string;
    }

    /**
     * @param string
     */
    public void setCodeOfficeAiEpouse(String string) {
        codeOfficeAiEpouse = string;
    }

    /**
     * @param string
     */
    public void setDegreInvaliditeEpouse(String string) {
        degreInvaliditeEpouse = string;
    }

    /**
     * @param boolean1
     */
    public void setInvaliditePrecoceEpouse(Boolean boolean1) {
        isInvaliditePrecoceEpouse = boolean1;
    }

    /**
     * @param string
     */
    public void setNbrAnneeEducation(String string) {
        nbrAnneeEducation = string;
    }

    /**
     * @param string
     */
    public void setSurvenanceEvenementAssureEpouse(String string) {
        survenanceEvenementAssureEpouse = string;
    }

}
