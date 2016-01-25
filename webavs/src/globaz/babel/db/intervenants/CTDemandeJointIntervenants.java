/*
 * Créé le 26 sept. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.db.intervenants;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CTDemandeJointIntervenants extends BEntity {

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
    /** L'id du tiers principal */
    public static final String FIELDNAME_ID_DEMANDE = "WAIDEM";
    /** L'id d'un intervenant */
    public static final String FIELDNAME_ID_INTERVENANT = "WQIINT";
    /** L'id du meta dossier */
    public static final String FIELDNAME_ID_META_DOSSIER = "WQIMDO";
    /** L'id du meta dossier */
    public static final String FIELDNAME_ID_META_DOSSIER_TABLE_DEMANDE = "WAIMDO";

    /** L'id du meta dossier */
    public static final String FIELDNAME_ID_META_DOSSIER_TABLE_META_DOSSIER = "WRIMDO";
    /** L'id du tiers en relation avec l'intervenant */
    public static final String FIELDNAME_ID_TIERS_INTERVENANT = "WQITIE";

    /** L'id du tiers principal */
    public static final String FIELDNAME_ID_TIERS_PRINCIPAL = "WAITIE";
    /** Le type de demande */
    public static final String FIELDNAME_TYPE_DEMANDE = "WATTDE";
    /** Table des demandes */
    public static final String TABLE_DEMANDE = "PRDEMAP";
    /** Table des intervenants */
    public static final String TABLE_INTRVENANTS = "DOINTERV";
    /** Table des meta dossier */
    public static final String TABLE_META_DOSSIER = "DOMETADO";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDebut = "";

    private String dateFin = "";
    private transient String fromClause = null;
    private String idTiersIntervenant = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
        idTiersIntervenant = statement.dbReadNumeric(FIELDNAME_ID_TIERS_INTERVENANT);
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
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String createFromClause(String schema) {
        StringBuffer fromClause = new StringBuffer();

        // la table des demandes
        fromClause.append(schema);
        fromClause.append(TABLE_DEMANDE);

        // jointure avec la table des meta dossiers
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(TABLE_META_DOSSIER);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_ID_META_DOSSIER_TABLE_DEMANDE);
        fromClause.append("=");
        fromClause.append(FIELDNAME_ID_META_DOSSIER_TABLE_META_DOSSIER);

        // jointure avec la table des intervenants
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(TABLE_INTRVENANTS);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_ID_META_DOSSIER_TABLE_META_DOSSIER);
        fromClause.append("=");
        fromClause.append(FIELDNAME_ID_META_DOSSIER);

        return fromClause.toString();
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
    public String getIdTiersIntervenant() {
        return idTiersIntervenant;
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
    public void setIdTiersIntervenant(String string) {
        idTiersIntervenant = string;
    }
}
