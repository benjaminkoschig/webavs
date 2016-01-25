/*
 * Créé le 2 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.db.annexes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CTAnnexes extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** L'id d'un annexes */
    public static final String FIELDNAME_ANNEXES_ID = "CEIGAN";

    /**
     * Le CS du groupes des ennexes possible pour ce domaine et ce type de document
     */
    public static final String FIELDNAME_CS_GROUPE = "CETGAN";

    /** Table des annexes */
    public static final String TABLE_ANNEXES = "CTGRPANX";

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
        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_ANNEXES);

        return fromClauseBuffer.toString();
    }

    private String csGroupe = "";
    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String idAnnexes = "";

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
        return TABLE_ANNEXES;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAnnexes = statement.dbReadNumeric(FIELDNAME_ANNEXES_ID);
        csGroupe = statement.dbReadString(FIELDNAME_CS_GROUPE);
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
        statement.writeKey(FIELDNAME_ANNEXES_ID, _dbWriteNumeric(statement.getTransaction(), idAnnexes, "idAnnexes"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ANNEXES_ID, _dbWriteNumeric(statement.getTransaction(), idAnnexes, "idAnnexes"));
        statement.writeField(FIELDNAME_CS_GROUPE, _dbWriteString(statement.getTransaction(), csGroupe, "csGroupe"));
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

}