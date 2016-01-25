package globaz.cygnus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class RFAdministrations extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String F_CANTON = "HBTCAN";
    public static final String F_CODE_ADMIN = "HBCADM";
    public static final String F_DESIGNATION1 = "HTLDE1";
    private static final String F_DESIGNATION2 = "HTLDE2";
    public static final String F_IDTIER = "HTITIE";
    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String LIKE = " LIKE ";
    public static final String ON = " ON ";
    // tables
    public static final String T_ADMIN = "TIADMIP";
    public static final String T_TIER = "TITIERP";

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
        fromClauseBuffer.append(RFAdministrations.T_ADMIN);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAdministrations.T_TIER);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAdministrations.T_TIER);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAdministrations.F_IDTIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAdministrations.T_ADMIN);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAdministrations.F_IDTIER);

        return fromClauseBuffer.toString();
    }

    private String canton = "";
    private String codeAdministration = "";

    private String designation1 = "";
    private String designation2;
    private transient String fromClause = null;
    private String idTiers;

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
            StringBuffer from = new StringBuffer(RFAdministrations.createFromClause(_getCollection()));
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
        canton = statement.dbReadNumeric(RFAdministrations.F_CANTON);
        codeAdministration = statement.dbReadString(RFAdministrations.F_CODE_ADMIN);
        designation1 = statement.dbReadString(RFAdministrations.F_DESIGNATION1);
        designation2 = statement.dbReadString(RFAdministrations.F_DESIGNATION2);
        setIdTiers(statement.dbReadString(RFAdministrations.F_IDTIER));
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

    public String getCanton() {
        return canton;
    }

    public String getCodeAdministration() {
        return codeAdministration;
    }

    public String getDesignation1() {
        return designation1;
    }

    public String getDesignation2() {
        return designation2;
    }

    public String getFromClause() {
        return fromClause;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setCodeAdministration(String codeAdministration) {
        this.codeAdministration = codeAdministration;
    }

    public void setDesignation1(String designation1) {
        this.designation1 = designation1;
    }

    public void setDesignation2(String designation2) {
        this.designation2 = designation2;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public String getIdTiers() {
        return idTiers;
    }

}