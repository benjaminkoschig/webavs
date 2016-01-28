package globaz.aquila.db.access.traitspec;

import globaz.aquila.ts.COTSExecutor;
import globaz.aquila.ts.COTSFileUtil;
import globaz.aquila.ts.COTSPredicate;
import globaz.aquila.ts.COTSValidator;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * <p>
 * Représente un traitement spécifique à exécuter lorsqu'un contentieux passe une certaine transition.
 * </p>
 * 
 * @author vre
 */
public class COTraitementSpecifique extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String FNAME_CS_TYPE = "OQTTSP";

    public static final String FNAME_EXECUTOR_CLASS_NAME = "OQLECL";

    public static final String FNAME_FILE_UTIL_CLASS_NAME = "OQLFCL";
    public static final String FNAME_ID_TRAITEMENT_SPECIFIQUE = "OQITSP";
    public static final String FNAME_PREDICATE_CLASS_NAME = "OQLPCL";
    public static final String FNAME_VALIDATOR_CLASS_NAME = "OQLVCL";
    private static final long serialVersionUID = -3384560470955911657L;
    public static final String TABLE_NAME = "COTRASP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csType = "";
    private String executorClassName = "";
    private String fileUtilClassName = "";
    private String idTraitementSpecifique = "";
    private String predicateClassName = "";
    private String validatorClassName = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return COTraitementSpecifique.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idTraitementSpecifique = statement.dbReadNumeric(COTraitementSpecifique.FNAME_ID_TRAITEMENT_SPECIFIQUE);
        csType = statement.dbReadNumeric(COTraitementSpecifique.FNAME_CS_TYPE);
        predicateClassName = statement.dbReadString(COTraitementSpecifique.FNAME_PREDICATE_CLASS_NAME);
        validatorClassName = statement.dbReadString(COTraitementSpecifique.FNAME_VALIDATOR_CLASS_NAME);
        executorClassName = statement.dbReadString(COTraitementSpecifique.FNAME_EXECUTOR_CLASS_NAME);
        fileUtilClassName = statement.dbReadString(COTraitementSpecifique.FNAME_FILE_UTIL_CLASS_NAME);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        setIdTraitementSpecifique(this._incCounter(statement.getTransaction(), "0"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(COTraitementSpecifique.FNAME_ID_TRAITEMENT_SPECIFIQUE,
                this._dbWriteNumeric(statement.getTransaction(), idTraitementSpecifique, "idTraitementSpecifique"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(COTraitementSpecifique.FNAME_ID_TRAITEMENT_SPECIFIQUE,
                this._dbWriteNumeric(statement.getTransaction(), idTraitementSpecifique, "idTraitementSpecifique"));
        statement.writeField(COTraitementSpecifique.FNAME_CS_TYPE,
                this._dbWriteNumeric(statement.getTransaction(), csType, "csType"));
        statement.writeField(COTraitementSpecifique.FNAME_PREDICATE_CLASS_NAME,
                this._dbWriteString(statement.getTransaction(), predicateClassName, "predicateClassName"));
        statement.writeField(COTraitementSpecifique.FNAME_VALIDATOR_CLASS_NAME,
                this._dbWriteString(statement.getTransaction(), validatorClassName, "validatorClassName"));
        statement.writeField(COTraitementSpecifique.FNAME_EXECUTOR_CLASS_NAME,
                this._dbWriteString(statement.getTransaction(), executorClassName, "executorClassName"));
        statement.writeField(COTraitementSpecifique.FNAME_FILE_UTIL_CLASS_NAME,
                this._dbWriteString(statement.getTransaction(), fileUtilClassName, "fileUtilClassName"));
    }

    /**
     * @return le type de traitement spécifique (COTYPETSP)
     */
    public String getCsType() {
        return csType;
    }

    public COTSExecutor getExecutor() throws Exception {
        if (!JadeStringUtil.isBlank(getExecutorClassName())) {
            return (COTSExecutor) Class.forName(getExecutorClassName()).newInstance();
        }

        return null;
    }

    public String getExecutorClassName() {
        return executorClassName;
    }

    public COTSFileUtil getFileUtil() throws Exception {
        if (!JadeStringUtil.isBlank(getFileUtilClassName())) {
            return (COTSFileUtil) Class.forName(getFileUtilClassName()).newInstance();
        }

        return null;
    }

    public String getFileUtilClassName() {
        return fileUtilClassName;
    }

    /**
     * @return l'identifiant de ce traitement spécifique
     */
    public String getIdTraitementSpecifique() {
        return idTraitementSpecifique;
    }

    public COTSPredicate getPredicate() throws Exception {
        if (!JadeStringUtil.isBlank(getPredicateClassName())) {
            return (COTSPredicate) Class.forName(getPredicateClassName()).newInstance();
        }

        return null;
    }

    /**
     * @return le nom complétement qualifié de la classe servant à déterminer si ce traitement spécifique s'applique
     *         pour un contentieux et une transition.
     */
    public String getPredicateClassName() {
        return predicateClassName;
    }

    public COTSValidator getValidator() throws Exception {
        if (!JadeStringUtil.isBlank(getValidatorClassName())) {
            return (COTSValidator) Class.forName(getValidatorClassName()).newInstance();
        }

        return null;
    }

    /**
     * @return le nom complétement qualifié de la classe servant à valider un contentieux et une transition pour
     *         lesquels ce traitement spécifique s'applique.
     */
    public String getValidatorClassName() {
        return validatorClassName;
    }

    /**
     * @see #getCsType()
     */
    public void setCsType(String csType) {
        this.csType = csType;
    }

    public void setExecutorClassName(String executorClassName) {
        this.executorClassName = executorClassName;
    }

    public void setFileUtilClassName(String fileClassName) {
        fileUtilClassName = fileClassName;
    }

    /**
     * @see #getIdTraitementSpecifique()
     */
    public void setIdTraitementSpecifique(String idTraitementSpecifique) {
        this.idTraitementSpecifique = idTraitementSpecifique;
    }

    /**
     * @see #getPredicateClassName()
     */
    public void setPredicateClassName(String predicateClassName) {
        this.predicateClassName = predicateClassName;
    }

    /**
     * @see #getValidatorClassName()
     */
    public void setValidatorClassName(String validatorClassName) {
        this.validatorClassName = validatorClassName;
    }

}
