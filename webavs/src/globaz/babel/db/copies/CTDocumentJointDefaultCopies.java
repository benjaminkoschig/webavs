/*
 * Créé le 2 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.db.copies;

import globaz.babel.db.cat.CTDocument;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CTDocumentJointDefaultCopies extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClause = new StringBuffer();

        // la table des documents
        fromClause.append(schema);
        fromClause.append(CTDocument.TABLE_NAME_DOCUMENT);

        // jointure avec la table des copies par defaut
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(CTDefaultCopies.TABLE_DEFAULT_COPIES);
        fromClause.append(" ON ");
        fromClause.append(CTDocument.FIELDNAME_ID_DOCUMENT);
        fromClause.append("=");
        fromClause.append(CTDefaultCopies.FIELDNAME_ID_DOCUMENT);

        return fromClause.toString();
    }

    private String csTypeIntervenant = "";
    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String idDefaultCopie = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDefaultCopie = statement.dbReadNumeric(CTDefaultCopies.FIELDNAME_DEFAULT_COPIE_ID);
        csTypeIntervenant = statement.dbReadNumeric(CTDefaultCopies.FIELDNAME_CS_TYPE_INTERVENANT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // nope
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // nope
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // nope
    }

    /**
     * @return
     */
    public String getCsTypeIntervenant() {
        return csTypeIntervenant;
    }

    /**
     * @return
     */
    public String getIdDefaultCopie() {
        return idDefaultCopie;
    }

    /**
     * @param string
     */
    public void setCsTypeIntervenant(String string) {
        csTypeIntervenant = string;
    }

    /**
     * @param string
     */
    public void setIdDefaultCopie(String string) {
        idDefaultCopie = string;
    }

}