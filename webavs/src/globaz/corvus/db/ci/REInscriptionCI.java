/*
 * Créé le 15 fevr. 07
 */
package globaz.corvus.db.ci;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */

public class REInscriptionCI extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int ALTERNATE_KEY_ID_TIERS = 1;
    public static final String FIELDNAME_ACTIF = "YPBACT";
    public static final String FIELDNAME_ATTENTE_CI_ADD = "YPBACI";
    public static final String FIELDNAME_ID_ARC = "YPIARC";
    public static final String FIELDNAME_ID_INSCRIPTION = "YPIICI";
    public static final String FIELDNAME_ID_RCI = "YPIRCI";

    public static final String TABLE_NAME_INS_CI = "REINSCI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean actif = Boolean.FALSE;
    private Boolean attenteCiAdd = Boolean.FALSE;
    private String idArc = "";
    private String idInscription = "";
    private String idRCI = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * initialise la valeur de Id période api
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdInscription(_incCounter(transaction, idInscription, TABLE_NAME_INS_CI));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_INS_CI;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idInscription = statement.dbReadNumeric(FIELDNAME_ID_INSCRIPTION);
        idRCI = statement.dbReadNumeric(FIELDNAME_ID_RCI);
        idArc = statement.dbReadNumeric(FIELDNAME_ID_ARC);
        actif = statement.dbReadBoolean(FIELDNAME_ACTIF);
        attenteCiAdd = statement.dbReadBoolean(FIELDNAME_ATTENTE_CI_ADD);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * 
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
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_INSCRIPTION,
                _dbWriteNumeric(statement.getTransaction(), idInscription, "idInscription"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_INSCRIPTION,
                _dbWriteNumeric(statement.getTransaction(), idInscription, "idInscription"));
        statement.writeField(FIELDNAME_ID_RCI, _dbWriteNumeric(statement.getTransaction(), idRCI, "idRCI"));
        statement.writeField(FIELDNAME_ID_ARC, _dbWriteNumeric(statement.getTransaction(), idArc, "idARC"));
        statement.writeField(FIELDNAME_ACTIF,
                _dbWriteBoolean(statement.getTransaction(), actif, BConstants.DB_TYPE_BOOLEAN_CHAR, "actif"));
        statement.writeField(
                FIELDNAME_ATTENTE_CI_ADD,
                _dbWriteBoolean(statement.getTransaction(), attenteCiAdd, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "attenteCiAdd"));
    }

    public Boolean getActif() {
        return actif;
    }

    public Boolean getAttenteCiAdd() {
        return attenteCiAdd;
    }

    public String getIdArc() {
        return idArc;
    }

    public String getIdInscription() {
        return idInscription;
    }

    public String getIdRCI() {
        return idRCI;
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

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public void setAttenteCiAdd(Boolean attenteCiAdd) {
        this.attenteCiAdd = attenteCiAdd;
    }

    public void setIdArc(String idArc) {
        this.idArc = idArc;
    }

    public void setIdInscription(String idInscription) {
        this.idInscription = idInscription;
    }

    public void setIdRCI(String idRCI) {
        this.idRCI = idRCI;
    }

}
