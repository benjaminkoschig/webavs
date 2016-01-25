/*
 * Créé le 15 fevr. 07
 */
package globaz.corvus.db.historiques;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * <H1>Description</H1>
 * 
 * Entête de toutes les annonces de rentes.
 * 
 * @author scr
 */

public class REHistoriqueHeader extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_HISTORIQUE = "WHIAHI";
    public static final String TABLE_NAME_HISTORIQUE_HEADER = "REAHISTR";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    protected String idHistorique = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idHistorique = this._incCounter(transaction, idHistorique, REHistoriqueHeader.TABLE_NAME_HISTORIQUE_HEADER);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + REHistoriqueHeader.TABLE_NAME_HISTORIQUE_HEADER;
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
        return REHistoriqueHeader.TABLE_NAME_HISTORIQUE_HEADER;
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
        idHistorique = statement.dbReadNumeric(REHistoriqueHeader.FIELDNAME_ID_HISTORIQUE);
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
        statement.writeKey(REHistoriqueHeader.FIELDNAME_ID_HISTORIQUE,
                this._dbWriteNumeric(statement.getTransaction(), idHistorique, "idHistorique"));
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
        statement.writeField(REHistoriqueHeader.FIELDNAME_ID_HISTORIQUE,
                this._dbWriteNumeric(statement.getTransaction(), idHistorique, "idHistorique"));
    }

    public String getIdHistorique() {
        return idHistorique;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setIdHistorique(String idHistorique) {
        this.idHistorique = idHistorique;
    }

}
