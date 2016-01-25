/*
 * Créé le 2 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.db.annexes;

import globaz.babel.db.cat.CTTypeDocument;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CTTypeDocumentJointAnnexes extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

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

        // la table des types de documents
        fromClause.append(schema);
        fromClause.append(CTTypeDocument.TABLE_NAME_TYPE_DOCUMENT);

        // jointure avec la table des annexes
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(CTAnnexes.TABLE_ANNEXES);
        fromClause.append(" ON ");
        fromClause.append("CAIGAN");
        fromClause.append("=");
        fromClause.append(CTAnnexes.FIELDNAME_ANNEXES_ID);

        return fromClause.toString();
    }

    private String csGroupe = "";
    private transient String fromClause = null;
    private String idAnnexes = "";
    private String typeDocumentCsDomaine = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String typeDocumentCsType = "";

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
        idAnnexes = statement.dbReadNumeric(CTAnnexes.FIELDNAME_ANNEXES_ID);
        csGroupe = statement.dbReadString(CTAnnexes.FIELDNAME_CS_GROUPE);
        typeDocumentCsDomaine = statement.dbReadNumeric(CTTypeDocument.FIELDNAME_CS_DOMAINE);
        typeDocumentCsType = statement.dbReadNumeric(CTTypeDocument.FIELDNAME_CS_TYPE_DOCUMENT);
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
    public String getCsGroupe() {
        return csGroupe;
    }

    /**
     * @return
     */
    public String getIdAnnexes() {
        return idAnnexes;
    }

    /**
     * @return
     */
    public String getTypeDocumentCsDomaine() {
        return typeDocumentCsDomaine;
    }

    /**
     * @return
     */
    public String getTypeDocumentCsType() {
        return typeDocumentCsType;
    }

    /**
     * @param string
     */
    public void setCsGroupe(String string) {
        csGroupe = string;
    }

    /**
     * @param string
     */
    public void setIdAnnexes(String string) {
        idAnnexes = string;
    }

    /**
     * @param string
     */
    public void setTypeDocumentCsDomaine(String string) {
        typeDocumentCsDomaine = string;
    }

    /**
     * @param string
     */
    public void setTypeDocumentCsType(String string) {
        typeDocumentCsType = string;
    }

}