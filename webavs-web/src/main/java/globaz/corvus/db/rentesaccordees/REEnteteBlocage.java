/*
 * Créé le 07 nov. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public class REEnteteBlocage extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_ENTETE_BLOCAGE = "ZZIEBK";
    public static final String FIELDNAME_MONTANT_BLOQUE = "ZZMBLK";
    public static final String FIELDNAME_MONTANT_DEBLOQUE = "ZZMDBK";
    public static final String TABLE_NAME_ENTETE_BLOCAGE = "REENTBLK";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idEnteteBlocage = "";
    private String montantBloque = "";
    private String montantDebloque = "";

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
        setIdEnteteBlocage(_incCounter(transaction, "0"));
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
        return TABLE_NAME_ENTETE_BLOCAGE;
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
        idEnteteBlocage = statement.dbReadNumeric(FIELDNAME_ID_ENTETE_BLOCAGE);
        montantBloque = statement.dbReadNumeric(FIELDNAME_MONTANT_BLOQUE);
        montantDebloque = statement.dbReadNumeric(FIELDNAME_MONTANT_DEBLOQUE);
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
        statement.writeKey(FIELDNAME_ID_ENTETE_BLOCAGE,
                _dbWriteNumeric(statement.getTransaction(), idEnteteBlocage, "idEnteteBlocage"));
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
        statement.writeField(FIELDNAME_ID_ENTETE_BLOCAGE,
                _dbWriteNumeric(statement.getTransaction(), idEnteteBlocage, "idEnteteBlocage"));
        statement.writeField(FIELDNAME_MONTANT_BLOQUE,
                _dbWriteNumeric(statement.getTransaction(), montantBloque, "montantBloque"));
        statement.writeField(FIELDNAME_MONTANT_DEBLOQUE,
                _dbWriteNumeric(statement.getTransaction(), montantDebloque, "montantDebloque"));
    }

    public String getIdEnteteBlocage() {
        return idEnteteBlocage;
    }

    public String getMontantBloque() {
        return montantBloque;
    }

    public String getMontantDebloque() {
        return montantDebloque;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setIdEnteteBlocage(String idEnteteBlocage) {
        this.idEnteteBlocage = idEnteteBlocage;
    }

    public void setMontantBloque(String montantBloque) {
        this.montantBloque = montantBloque;
    }

    public void setMontantDebloque(String montantDebloque) {
        this.montantDebloque = montantDebloque;
    }

}
