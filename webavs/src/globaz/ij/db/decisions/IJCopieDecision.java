package globaz.ij.db.decisions;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * 
 * @author SCR
 * 
 */
public class IJCopieDecision extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_CS_TYPE_COPIE = "XJTTYP";

    public static final String FIELDNAME_ID_COPIES = "XJICOP";
    public static final String FIELDNAME_ID_DECISION = "XJIDEC";
    public static final String FIELDNAME_ID_TIERS = "XJITIE";
    public static final String FIELDNAME_IS_COPIE_OAI = "XJBCAI";
    public static final String FIELDNAME_PRENOM_NOM = "XJLPRN";
    public static final String TABLE_NAME = "IJCOPIES";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csTypeCopie = "";
    private String idCopies = "";
    private String idDecision = "";
    private String idTiers = "";
    private Boolean isCopieOAI = null;
    private String prenomNom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdCopies(_incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCopies = statement.dbReadNumeric(FIELDNAME_ID_COPIES);
        idTiers = statement.dbReadNumeric(FIELDNAME_ID_TIERS);
        prenomNom = statement.dbReadString(FIELDNAME_PRENOM_NOM);
        isCopieOAI = statement.dbReadBoolean(FIELDNAME_IS_COPIE_OAI);
        idDecision = statement.dbReadNumeric(FIELDNAME_ID_DECISION);
        csTypeCopie = statement.dbReadNumeric(FIELDNAME_CS_TYPE_COPIE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        ;
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_COPIES, _dbWriteNumeric(statement.getTransaction(), idCopies, "idCopies"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_DECISION,
                _dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(FIELDNAME_ID_COPIES, _dbWriteNumeric(statement.getTransaction(), idCopies, "idCopies"));
        statement.writeField(FIELDNAME_ID_TIERS, _dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(FIELDNAME_PRENOM_NOM, _dbWriteString(statement.getTransaction(), prenomNom, "prenomNom"));
        statement.writeField(FIELDNAME_IS_COPIE_OAI,
                _dbWriteBoolean(statement.getTransaction(), isCopieOAI, BConstants.DB_TYPE_BOOLEAN_CHAR, "isCopieOAI"));
        statement.writeField(FIELDNAME_CS_TYPE_COPIE,
                _dbWriteNumeric(statement.getTransaction(), csTypeCopie, "csTypeCopie"));

    }

    public String getCsTypeCopie() {
        return csTypeCopie;
    }

    public String getIdCopies() {
        return idCopies;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsCopieOAI() {
        return isCopieOAI;
    }

    public String getPrenomNom() {
        return prenomNom;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setCsTypeCopie(String csTypeCopie) {
        this.csTypeCopie = csTypeCopie;
    }

    public void setIdCopies(String idCopies) {
        this.idCopies = idCopies;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsCopieOAI(Boolean isCopieOAI) {
        this.isCopieOAI = isCopieOAI;
    }

    public void setPrenomNom(String prenomNom) {
        this.prenomNom = prenomNom;
    }
}
