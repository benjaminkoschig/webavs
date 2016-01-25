/*
 * Créé le 8 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * DOCUMENT ME!
 * 
 * @author mmu
 * 
 *         <p>
 *         Fait la jointure entre Requerant et MembreFamille
 *         </p>
 */
public class SFApercuRequerant extends SFMembreFamille {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int ALT_KEY_IDMEMBRE = 100;
    public static final int ALT_KEY_IDTIERS = 200;
    /** DOCUMENT ME! */
    public static final String FIELD_IDDOMAINEAPPLICATION = "WDTDOA";

    /** DOCUMENT ME! */
    public static final String FIELD_IDREQUERANT = "WDIREQ";

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            : ne nom de la base de donnée (p.ex. WEBAVSP), donnée par _getCollection()
     * 
     * @return DOCUMENT ME!
     */
    public static String createFromClause(String schema) {

        return schema + SFRequerant.TABLE_NAME + " AS " + SFRequerant.TABLE_NAME + " INNER JOIN " + schema
                + SFMembreFamille.TABLE_NAME + " AS " + SFMembreFamille.TABLE_NAME + " ON ("
                + SFMembreFamille.TABLE_NAME + "." + SFMembreFamille.FIELD_IDMEMBREFAMILLE + " = "
                + SFRequerant.TABLE_NAME + "." + SFRequerant.FIELD_IDMEMBREFAMILLE + ")"
                + SFMembreFamille.createJoinClause(schema);
    }

    private String idDomaineApplication = "";

    private String idRequerant = "";

    private String provenance = "";

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = createFromClause(_getCollection());

        return from;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idRequerant = statement.dbReadNumeric(SFRequerant.FIELD_IDREQUERANT);
        idDomaineApplication = statement.dbReadNumeric(SFRequerant.FIELD_IDDOMAINEAPPLICATION);
        provenance = statement.dbReadString(SFRequerant.FIELD_PROVENANCE);
    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == ALT_KEY_IDMEMBRE) {
            statement.writeKey(FIELD_IDDOMAINEAPPLICATION,
                    _dbWriteNumeric(statement.getTransaction(), idDomaineApplication, "idDomaineApplication"));
            statement.writeKey(FIELD_IDMEMBREFAMILLE,
                    _dbWriteNumeric(statement.getTransaction(), getIdMembreFamille(), "idMembreFamille"));
        } else if (alternateKey == ALT_KEY_IDTIERS) {
            statement.writeKey(FIELD_IDDOMAINEAPPLICATION,
                    _dbWriteNumeric(statement.getTransaction(), idDomaineApplication, "idDomaineApplication"));
            statement.writeKey(FIELD_IDTIERS, _dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        } else {
            super._writeAlternateKey(statement, alternateKey);
        }

    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDREQUERANT, _dbWriteNumeric(statement.getTransaction(), idRequerant, "idRequerant"));
    }

    /**
     * @return
     */
    public String getIdDomaineApplication() {
        return idDomaineApplication;
    }

    /**
     * @return
     */
    public String getIdRequerant() {
        return idRequerant;
    }

    @Override
    public String getProvenance() {
        return provenance;
    }

    /**
     * @param string
     */
    public void setIdDomaineApplication(String string) {
        idDomaineApplication = string;
    }

    /**
     * @param string
     */
    public void setIdRequerant(String string) {
        idRequerant = string;
    }

    @Override
    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

}
