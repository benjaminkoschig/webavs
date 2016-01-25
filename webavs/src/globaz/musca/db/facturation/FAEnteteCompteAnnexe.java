package globaz.musca.db.facturation;

/*
 * ATTENTION: cette classe ne lit pas depuis la DB, c'est une classe temporaire utilisée pour une aggrégation de table
 * composée de jointures (JOIN). A utiliser uniquement depuis FAEnteteFactureMamager.getEntity(i) après avoir appeler sa
 * méthode find().
 */

public class FAEnteteCompteAnnexe extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idExterneRole = "";
    private java.lang.String idRoles = "";
    private java.lang.String idTiers = "";
    private java.lang.String totalDecomptes;

    // code systeme

    /**
     * Commentaire relatif au constructeur FARemarque
     */
    public FAEnteteCompteAnnexe() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return null;// unused
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idTiers = statement.dbReadString("IDTIERS");
        idExterneRole = statement.dbReadString("IDEXTERNEROLE");
        totalDecomptes = statement.dbReadNumeric("TOTALDECOMPTES");
        idRoles = statement.dbReadNumeric("IDROLE");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // unused, voir description de la classe
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        // unused, voir description de la classe
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        // unused, voir description de la classe
    }

    /**
     * Getter
     */
    public java.lang.String getIdExterneRole() {
        return idExterneRole;
    }

    /**
     * @return
     */
    public java.lang.String getIdRoles() {
        return idRoles;
    }

    /**
     * Insert the method's description here. Creation date: (20.06.2003 17:03:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTiers() {
        return idTiers;
    }

    /**
     * Insert the method's description here. Creation date: (20.06.2003 12:16:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTotalDecomptes() {
        return totalDecomptes;
    }

    /**
     * Insert the method's description here. Creation date: (20.06.2003 17:03:30)
     * 
     * @param newIdTiers
     *            java.lang.String
     */
    public void setIdTiers(java.lang.String newIdTiers) {
        idTiers = newIdTiers;
    }

}
