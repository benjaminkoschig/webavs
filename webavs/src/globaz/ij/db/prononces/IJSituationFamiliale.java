/*
 * Créé le 7 sept. 05
 */
package globaz.ij.db.prononces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJSituationFamiliale extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_IDREQUERANT = "XJIREQ";

    /**
     */
    public static final String FIELDNAME_IDSITUATIONFAMILIALE = "XJISIF";

    /**
     */
    public static final String TABLE_NAME = "IJSITFAM";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idRequerant = "";
    private String idSituationFamiliale = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
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
        idSituationFamiliale = statement.dbReadNumeric(FIELDNAME_IDSITUATIONFAMILIALE);
        idRequerant = statement.dbReadNumeric(FIELDNAME_IDREQUERANT);
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
        // TODO Raccord de méthode auto-généré
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_IDSITUATIONFAMILIALE,
                _dbWriteNumeric(statement.getTransaction(), "idSituationFamiliale"));
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
        statement.writeField(FIELDNAME_IDSITUATIONFAMILIALE,
                _dbWriteNumeric(statement.getTransaction(), idSituationFamiliale, "idSituationFamiliale"));
        statement.writeField(FIELDNAME_IDREQUERANT,
                _dbWriteNumeric(statement.getTransaction(), idRequerant, "idRequerant"));
    }

    /**
     * getter pour l'attribut id requerant
     * 
     * @return la valeur courante de l'attribut id requerant
     */
    public String getIdRequerant() {
        return idRequerant;
    }

    /**
     * getter pour l'attribut id situation familiale
     * 
     * @return la valeur courante de l'attribut id situation familiale
     */
    public String getIdSituationFamiliale() {
        return idSituationFamiliale;
    }

    /**
     * setter pour l'attribut id requerant
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRequerant(String string) {
        idRequerant = string;
    }

    /**
     * setter pour l'attribut id situation familiale
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSituationFamiliale(String string) {
        idSituationFamiliale = string;
    }
}
