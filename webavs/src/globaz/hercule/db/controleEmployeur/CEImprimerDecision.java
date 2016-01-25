package globaz.hercule.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CEImprimerDecision extends BEntity implements FWViewBeanInterface {

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
    private String referenceFacture = "";
    private String role = "";

    private String totalFacture = "";

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
     * @return faux
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return CEImprimerDecision.createFromClause(_getCollection());
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
        idControle = statement.dbReadNumeric("IDCONTROLE");
        totalFacture = statement.dbReadNumeric("TOTALFACTURE");
        numeroFacture = statement.dbReadString("IDEXTERNEFACTURE");
        nonImprimable = statement.dbReadBoolean("NONIMPRIMABLE");
        role = statement.dbReadNumeric("IDROLE");
        referenceFacture = statement.dbReadString("REFERENCEFACTURE");
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
    }

    public String getIdControle() {
        return idControle;
    }

    public Boolean getNonImprimable() {
        return nonImprimable;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public String getReferenceFacture() {
        return referenceFacture;
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

    // *******************************************************
    // Setter
    // *******************************************************

    public void setNonImprimable(Boolean nonImprimable) {
        this.nonImprimable = nonImprimable;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public void setReferenceFacture(String referenceFacture) {
        this.referenceFacture = referenceFacture;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setTotalFacture(String totalFacture) {
        this.totalFacture = totalFacture;
    }
}
