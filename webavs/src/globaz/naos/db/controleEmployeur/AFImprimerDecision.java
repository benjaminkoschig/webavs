package globaz.naos.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class AFImprimerDecision extends BEntity implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append("FAENTFP");
        return fromClauseBuffer.toString();
    }

    private String idControle = "";
    private Boolean nonImprimable = Boolean.FALSE;
    private String numeroFacture = "";
    private String role = "";

    private String totalFacture = "";

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
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return AFImprimerDecision.createFromClause(_getCollection());
    }

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idControle = statement.dbReadNumeric("IDCONTROLE");
        totalFacture = statement.dbReadNumeric("TOTALFACTURE");
        numeroFacture = statement.dbReadString("IDEXTERNEFACTURE");
        nonImprimable = statement.dbReadBoolean("NONIMPRIMABLE");
        role = statement.dbReadNumeric("IDROLE");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    public String getIdControle() {
        return idControle;
    }

    public Boolean getNonImprimable() {
        return nonImprimable;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public String getRole() {
        return role;
    }

    public String getTotalFacture() {
        return totalFacture;
    }

    public void setIdControle(String idControle) {
        this.idControle = idControle;
    }

    public void setNonImprimable(Boolean nonImprimable) {
        this.nonImprimable = nonImprimable;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setTotalFacture(String totalFacture) {
        this.totalFacture = totalFacture;
    }

}
