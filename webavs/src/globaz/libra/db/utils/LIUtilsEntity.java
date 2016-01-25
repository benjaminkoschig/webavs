/*
 * Cr�� le 24.07.2009
 */
package globaz.libra.db.utils;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * 
 * @author HPE
 * 
 */
public class LIUtilsEntity extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int ALTERNATE_KEY_ID_JOURNALISATION = 1;
    public static final String FIELDNAME_ID_JOURNALISATIONS = "ABIJOU";
    public static final String FIELDNAME_ID_UTILS = "ABIUTI";

    public static final String FIELDNAME_REMARQUE = "ABLREM";

    public static final String TABLE_NAME = "LIUTILS";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idJournalisation = new String();
    private String idUtils = new String();
    private String remarque = new String();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe LIUtilsEntity.
     */
    public LIUtilsEntity() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * M�thode avant l'ajout l'incr�mentation de la cl� primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdUtils(_incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table utils pour libra
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * Lecture des propri�t�s dans les champs de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idUtils = statement.dbReadNumeric(FIELDNAME_ID_UTILS);
        idJournalisation = statement.dbReadNumeric(FIELDNAME_ID_JOURNALISATIONS);
        remarque = statement.dbReadString(FIELDNAME_REMARQUE);

    }

    /**
     * M�thode de validation des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    /**
     * D�finition de la cl� primaire de la table des �ch�ances multiples
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {

        if (alternateKey == ALTERNATE_KEY_ID_JOURNALISATION) {
            statement.writeKey(FIELDNAME_ID_JOURNALISATIONS,
                    _dbWriteNumeric(statement.getTransaction(), idJournalisation, "idJournalisation"));
        } else {
            statement.writeKey(FIELDNAME_ID_UTILS, _dbWriteNumeric(statement.getTransaction(), idUtils, "idUtils"));
        }

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_UTILS, _dbWriteNumeric(statement.getTransaction(), idUtils, "idUtils"));
    }

    /**
     * M�thode d'�criture des champs dans la table des �ch�ances
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_ID_UTILS, _dbWriteNumeric(statement.getTransaction(), idUtils, "idUtils"));
        statement.writeField(FIELDNAME_ID_JOURNALISATIONS,
                _dbWriteNumeric(statement.getTransaction(), idJournalisation, "idJournalisation"));
        statement.writeField(FIELDNAME_REMARQUE, _dbWriteString(statement.getTransaction(), remarque, "remarque"));

    }

    public String getIdJournalisation() {
        return idJournalisation;
    }

    public String getIdUtils() {
        return idUtils;
    }

    public String getRemarque() {
        return remarque;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setIdJournalisation(String idJournalisation) {
        this.idJournalisation = idJournalisation;
    }

    public void setIdUtils(String idUtils) {
        this.idUtils = idUtils;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

}