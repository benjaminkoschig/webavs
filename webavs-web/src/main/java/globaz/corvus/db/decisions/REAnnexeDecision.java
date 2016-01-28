/*
 * Créé le 20 août. 07
 */
package globaz.corvus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author BSC
 * 
 */
public class REAnnexeDecision extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_DECISION = "ZKIDEC";
    public static final String FIELDNAME_ID_DECISION_ANNEXE = "ZKIDAN";
    public static final String FIELDNAME_VALEUR = "ZKLVAL";
    public static final String TABLE_NAME_ANNEXE_DECISION = "REDECAN";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idDecision = "";
    private String idDecisionAnnexe = "";
    private String libelle = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdDecisionAnnexe(_incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_ANNEXE_DECISION;
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

        idDecisionAnnexe = statement.dbReadNumeric(FIELDNAME_ID_DECISION_ANNEXE);
        idDecision = statement.dbReadNumeric(FIELDNAME_ID_DECISION);
        libelle = statement.dbReadString(FIELDNAME_VALEUR);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
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
        statement.writeKey(FIELDNAME_ID_DECISION_ANNEXE,
                _dbWriteNumeric(statement.getTransaction(), idDecisionAnnexe, "idDecisionAnnexe"));
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

        statement.writeField(FIELDNAME_ID_DECISION_ANNEXE,
                _dbWriteNumeric(statement.getTransaction(), idDecisionAnnexe, "idDecisionAnnexe"));
        statement.writeField(FIELDNAME_ID_DECISION,
                _dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(FIELDNAME_VALEUR, _dbWriteString(statement.getTransaction(), libelle, "libelle"));
    }

    /**
     * @return the idDecision
     */
    public String getIdDecision() {
        return idDecision;
    }

    /**
     * @return
     */
    public String getIdDecisionAnnexe() {
        return idDecisionAnnexe;
    }

    /**
     * @return
     */
    public String getLibelle() {
        return libelle;
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
     * @param idDecision
     *            the idDecision to set
     */
    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    /**
     * @param string
     */
    public void setIdDecisionAnnexe(String string) {
        idDecisionAnnexe = string;
    }

    /**
     * @param string
     */
    public void setLibelle(String string) {
        libelle = string;
    }

}
