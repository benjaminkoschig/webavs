package globaz.hermes.db.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HENbreARCDansLot extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final String HEANNOP_ARCHIVE = "HEANNOR";
    private final String HEANNOP_EN_COURS = "HEANNOP";
    private boolean isArchivage = false;
    private int nombreARC = 0;
    private String numeroLot = "";

    /**
     * Constructor for HENbreARCDansLot.
     */
    public HENbreARCDansLot() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_getFields(BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "COUNT(*) AS NBANNONCE";
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return super._getFrom(statement);
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        if (isArchivage()) {
            return HEANNOP_ARCHIVE;
        } else {
            return HEANNOP_EN_COURS;
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        try {
            setNombreARC(Integer.parseInt(statement.dbReadNumeric("NBANNONCE", 0)));
        } catch (java.lang.NumberFormatException e) {
            e.printStackTrace();
            setNombreARC(0);
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("RMILOT", getNumeroLot());
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * Returns the nombreARC.
     * 
     * @return int
     */
    public int getNombreARC() {
        return nombreARC;
    }

    /**
     * Returns the numeroLot.
     * 
     * @return String
     */
    public String getNumeroLot() {
        return numeroLot;
    }

    /**
     * Returns the isArchivage.
     * 
     * @return boolean
     */
    public boolean isArchivage() {
        return isArchivage;
    }

    /**
     * Sets the isArchivage.
     * 
     * @param isArchivage
     *            The isArchivage to set
     */
    public void setIsArchivage(boolean isArchivage) {
        this.isArchivage = isArchivage;
    }

    /**
     * Sets the nombreARC.
     * 
     * @param nombreARC
     *            The nombreARC to set
     */
    public void setNombreARC(int nombreARC) {
        this.nombreARC = nombreARC;
    }

    /**
     * Sets the numeroLot.
     * 
     * @param numeroLot
     *            The numeroLot to set
     */
    public void setNumeroLot(String numeroLot) {
        this.numeroLot = numeroLot;
    }

}
