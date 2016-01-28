package globaz.cygnus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.pyxis.db.tiers.ITIAliasDefTable;

public class RFTiers extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String AVS_NSS = "HXNAVS";
    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String LIKE = " LIKE ";
    public static final String ON = " ON ";
    // tables
    public static final String T_PERSONNE = "TIPERSP";
    public static final String T_PERSONNE_AVS = "TIPAVSP";
    public static final String T_TIER = "TITIERP";
    public static final String TIER_COMPLEMENT_NOM = "HTLDE2";
    public static final String TIER_DATE_DE_NAISSANCE = "HPDNAI";
    public static final String TIER_IDTIER = "HTITIE";
    public static final String TIER_NOM = "HTLDE1";

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTiers.T_PERSONNE_AVS);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTiers.T_PERSONNE);
        fromClauseBuffer.append(" a ");
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(" a");
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFTiers.TIER_IDTIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTiers.T_PERSONNE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFTiers.TIER_IDTIER);

        // jointure entre la table des personnes et la table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTiers.T_TIER);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(" a");
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFTiers.TIER_IDTIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTiers.T_TIER);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFTiers.TIER_IDTIER);

        // jointure entre la table des tiers et des alias

        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(ITIAliasDefTable.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(" a");
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFTiers.TIER_IDTIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(ITIAliasDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(ITIAliasDefTable.ID_TIERS);

        return fromClauseBuffer.toString();
    }

    private String datenaissance = "";
    private transient String fromClause = null;

    private String idTiers = "";
    private String nom = "";
    private String numero_nss = "";
    private String prenom = "";
    private String alias = "";

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
            StringBuffer from = new StringBuffer(RFTiers.createFromClause(_getCollection()));
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
        numero_nss = statement.dbReadString(RFTiersManager.F_NSS);
        nom = statement.dbReadString(RFTiersManager.F_TIER_NOM);
        prenom = statement.dbReadString(RFTiersManager.F_TIER_COMPLEMENTNOM);
        datenaissance = statement.dbReadDateAMJ(RFTiersManager.F_TIER_NAISSANCE);
        idTiers = statement.dbReadNumeric(RFTiers.TIER_IDTIER);
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

    public String getNom() {
        return nom;
    }

    public String getNumero_nss() {
        return numero_nss;
    }

    public String getPrenom() {
        return prenom;
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

    public void setNom(String newNom) {
        nom = newNom;
    }

    public void setNumero_nss(String numero_nss) {
        this.numero_nss = numero_nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}