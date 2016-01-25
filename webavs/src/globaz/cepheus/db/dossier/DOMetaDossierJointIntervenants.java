/*
 * Créé le 26 sept. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.db.dossier;

import globaz.cepheus.db.intervenant.DOIntervenant;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DOMetaDossierJointIntervenants extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String csDescription = "";

    private String dateDebut = "";
    private String dateFin = "";
    private transient String fromClause = null;
    private String idIntervenant = "";
    private String idMetaDossier = "";
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
        idIntervenant = statement.dbReadNumeric(DOIntervenant.FIELDNAME_ID_INTERVENANT);
        idMetaDossier = statement.dbReadNumeric(DOIntervenant.FIELDNAME_ID_META_DOSSIER);
        csDescription = statement.dbReadNumeric(DOIntervenant.FIELDNAME_CS_DESCRIPTION);
        idTiersIntervenant = statement.dbReadNumeric(DOIntervenant.FIELDNAME_ID_TIERS);
        dateDebut = statement.dbReadDateAMJ(DOIntervenant.FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(DOIntervenant.FIELDNAME_DATE_FIN);
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

        fromClause.append(schema);
        fromClause.append(DOMetaDossier.TABLE_META_DOSSIER);

        // jointure avec la table des intervenants
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(DOIntervenant.TABLE_INTRVENANTS);
        fromClause.append(" ON ");
        fromClause.append(DOIntervenant.FIELDNAME_ID_META_DOSSIER);
        fromClause.append("=");
        fromClause.append(DOMetaDossier.FIELDNAME_ID_META_DOSSIER);

        return fromClause.toString();
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
