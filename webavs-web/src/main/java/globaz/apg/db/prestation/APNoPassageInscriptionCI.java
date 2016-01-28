/*
 * Créé le 17 nov. 06
 */
package globaz.apg.db.prestation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author hpe
 * 
 *         BEntity pour le no de passage des inscriptions CI
 * 
 */
public class APNoPassageInscriptionCI extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private transient String fields = null;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String noPassage = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    @Override
    protected String _getFields(BStatement statement) {
        return _getCollection() + APInscriptionCI.TABLE_NAME + "." + APInscriptionCI.FIELDNAME_NOPASSAGE;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + APInscriptionCI.TABLE_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return APInscriptionCI.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        noPassage = statement.dbReadNumeric(APInscriptionCI.FIELDNAME_NOPASSAGE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * @return
     */
    public String getNoPassage() {
        return noPassage;
    }

    /**
     * @param string
     */
    public void setNoPassage(String string) {
        noPassage = string;
    }

}
