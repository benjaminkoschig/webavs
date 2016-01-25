package globaz.hermes.db.gestion;

import globaz.globall.db.BStatement;

public class HEOutputAnnonceJointHEInfos extends HEOutputAnnonceViewBean {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_AVS = "HEINCOP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append("HEANNOP AS HEANNOP");

        // jointure entre table des demandes et table des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append("HEINCOP AS HEINCOP");
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(" CAST(");
        // fromClauseBuffer.append(schema);
        fromClauseBuffer.append("HEANNOP");
        fromClauseBuffer.append(point);
        fromClauseBuffer.append("RNREFU");
        fromClauseBuffer.append(" AS INTEGER)");
        fromClauseBuffer.append(egal);
        // fromClauseBuffer.append(schema);
        fromClauseBuffer.append("HEINCOP");
        fromClauseBuffer.append(point);
        fromClauseBuffer.append("RNIANN");

        return fromClauseBuffer.toString();
    }

    // Autres champs nécessaires
    private String libInfo = "";

    private String typeInfo = "";

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
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
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return faux
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        libInfo = statement.dbReadString("LIBINCO");
        typeInfo = statement.dbReadNumeric("TYPEINCO");
    }

    public String getLibInfo() {
        return libInfo;
    }

    public String getTypeInfo() {
        return typeInfo;
    }

    public void setLibInfo(String libInfo) {
        this.libInfo = libInfo;
    }

    public void setTypeInfo(String typeInfo) {
        this.typeInfo = typeInfo;
    }

}
