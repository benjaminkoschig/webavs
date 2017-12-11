package globaz.cygnus.db.qds;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class RFTierDateNaissance extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // tables
    public static final String T_PERSONNE = "TIPERSP";
    public static final String TIER_DATE_DE_NAISSANCE = "HPDNAI";
    public static final String TIER_IDTIER = "HTITIE";

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTierDateNaissance.T_PERSONNE);

        return fromClauseBuffer.toString();
    }

    private String datenaissance = "";
    private transient String fromClause = null;

    private String idTiers = "";

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

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFTierDateNaissance.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        datenaissance = statement.dbReadDateAMJ(RFTierDateNaisanceManager.F_TIER_NAISSANCE);
        idTiers = statement.dbReadNumeric(RFTierDateNaissance.TIER_IDTIER);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    public String getDatenaissance() {
        return datenaissance;
    }

    public String getFromClause() {
        return fromClause;
    }

    /**
     * getter
     */

    public String getIdTiers() {
        return idTiers;
    }

    public void setDatenaissance(String datenaissance) {
        this.datenaissance = datenaissance;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    /**
     * setter
     */

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

}