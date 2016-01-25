package globaz.helios.db.comptes.exportEcritures;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.helios.db.comptes.CGPlanComptableViewBean;

public class CGExportEcritures extends CGPlanComptableViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String ccAgenceSiege = new String();
    private java.lang.Boolean exportComptaSiege = new Boolean(false);

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
        super._readProperties(statement);
        exportComptaSiege = statement.dbReadBoolean("EXPORTCOMPTASIEGE");
        ccAgenceSiege = statement.dbReadString("CCAGENCESIEGE");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
        super._validate(statement);
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
        super._writeProperties(statement);
        statement.writeField("EXPORTCOMPTASIEGE", this._dbWriteBoolean(statement.getTransaction(),
                isExportationComptabiliteAuSiege(), BConstants.DB_TYPE_BOOLEAN_CHAR, "exportComptaSiege"));
        statement.writeField("CCAGENCESIEGE",
                this._dbWriteString(statement.getTransaction(), getCcAgenceSiege(), "ccAgenceSiege"));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    public java.lang.String getCcAgenceSiege() {
        return ccAgenceSiege;
    }

    public java.lang.Boolean getExportComptaSiege() {
        return exportComptaSiege;
    }

    public java.lang.Boolean isExportationComptabiliteAuSiege() {
        return exportComptaSiege;
    }

    public void setCcAgenceSiege(java.lang.String ccAgenceSiege) {
        this.ccAgenceSiege = ccAgenceSiege;
    }

    public void setExportComptaSiege(java.lang.Boolean exportComptaSiege) {
        this.exportComptaSiege = exportComptaSiege;
    }
}
