/*
 * Créé le 26 sept. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.db.intervenant;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DOIntervenant extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** La description de l'intervenant */
    public static final String FIELDNAME_CS_DESCRIPTION = "WQTDES";

    /** La date de debut de la relation */
    public static final String FIELDNAME_DATE_DEBUT = "WQDDDE";
    /** La date de fin de la relation */
    public static final String FIELDNAME_DATE_FIN = "WQDDFI";
    /** L'id d'un intervenant */
    public static final String FIELDNAME_ID_INTERVENANT = "WQIINT";
    /** L'id du meta dossier */
    public static final String FIELDNAME_ID_META_DOSSIER = "WQIMDO";
    /** L'id du tiers en relation avec l'intervenant */
    public static final String FIELDNAME_ID_TIERS = "WQITIE";
    /** Table des intervenants */
    public static final String TABLE_INTRVENANTS = "DOINTERV";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csDescription = "";

    private String dateDebut = "";
    private String dateFin = "";
    private transient String fromClause = null;
    private String idIntervenant = "";
    private String idMetaDossier = "";
    private String idTiersIntervenant = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdIntervenant(_incCounter(transaction, "0"));
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
        return TABLE_INTRVENANTS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idIntervenant = statement.dbReadNumeric(FIELDNAME_ID_INTERVENANT);
        idMetaDossier = statement.dbReadNumeric(FIELDNAME_ID_META_DOSSIER);
        csDescription = statement.dbReadNumeric(FIELDNAME_CS_DESCRIPTION);
        idTiersIntervenant = statement.dbReadNumeric(FIELDNAME_ID_TIERS);
        dateDebut = statement.dbReadDateAMJ(FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(FIELDNAME_DATE_FIN);
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
        statement.writeKey(FIELDNAME_ID_INTERVENANT,
                _dbWriteNumeric(statement.getTransaction(), idIntervenant, "idIntervenant"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_INTERVENANT,
                _dbWriteNumeric(statement.getTransaction(), idIntervenant, "idIntervenant"));
        statement.writeField(FIELDNAME_ID_META_DOSSIER,
                _dbWriteNumeric(statement.getTransaction(), idMetaDossier, "idMetaDossier"));
        statement.writeField(FIELDNAME_CS_DESCRIPTION,
                _dbWriteNumeric(statement.getTransaction(), csDescription, "csDescription"));
        statement.writeField(FIELDNAME_ID_TIERS,
                _dbWriteNumeric(statement.getTransaction(), idTiersIntervenant, "idTiersIntervenant"));
        statement.writeField(FIELDNAME_DATE_DEBUT, _dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));
        statement.writeField(FIELDNAME_DATE_FIN, _dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));

    }

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_INTRVENANTS);

        return fromClauseBuffer.toString();
    }

    /**
     * @return
     */
    public String getCsDescription() {
        return csDescription;
    }

    /**
     * @return
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return
     */
    public String getIdIntervenant() {
        return idIntervenant;
    }

    /**
     * @return
     */
    public String getIdMetaDossier() {
        return idMetaDossier;
    }

    /**
     * @return
     */
    public String getIdTiersIntervenant() {
        return idTiersIntervenant;
    }

    /**
     * @param string
     */
    public void setCsDescription(String string) {
        csDescription = string;
    }

    /**
     * @param string
     */
    public void setDateDebut(String string) {
        dateDebut = string;
    }

    /**
     * @param string
     */
    public void setDateFin(String string) {
        dateFin = string;
    }

    /**
     * @param string
     */
    public void setIdIntervenant(String string) {
        idIntervenant = string;
    }

    /**
     * @param string
     */
    public void setIdMetaDossier(String string) {
        idMetaDossier = string;
    }

    /**
     * @param string
     */
    public void setIdTiersIntervenant(String string) {
        idTiersIntervenant = string;
    }

}
