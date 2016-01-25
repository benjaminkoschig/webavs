package globaz.naos.db.taxeCo2;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.io.Serializable;

public class AFLettreTaxeCo2Manager extends AFTaxeCo2Manager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdModuleEnteteExiste = new String();
    private String forIdModuleTout = new String();
    private String order = new String();

    @Override
    protected String _getFields(BStatement statement) {
        return super._getFields(statement) + ", FAAFACP.IDMODFAC, FAAFACP.IDRUBRIQUE, FAAFACP.MONTANTFACTURE ";
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return super._getFrom(statement)
                + " INNER JOIN "
                + _getCollection()
                + "FAAFACP AS FAAFACP ON (AFTACOP.MWIENT=FAAFACP.IDENTETEFACTURE AND AFTACOP.MWMMAS=FAAFACP.MASSEFACTURE) ";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = super._getWhere(statement);

        if ((getForIdModuleTout().length() != 0) && (getForIdModuleEnteteExiste().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(FAAFACP.IDMODFAC=" + this._dbWriteNumeric(statement.getTransaction(), getForIdModuleTout())
                    + " OR FAAFACP.IDMODFAC="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdModuleEnteteExiste()) + ")";
        }
        if ((getForIdModuleTout().length() == 0) && (getForIdModuleEnteteExiste().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDMODFAC="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdModuleEnteteExiste());
        }
        if ((getForIdModuleTout().length() != 0) && (getForIdModuleEnteteExiste().length() == 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDMODFAC=" + this._dbWriteNumeric(statement.getTransaction(), getForIdModuleTout());
        }

        return sqlWhere;
    }

    // ***********************************************
    // Getter
    // ***********************************************

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFLettreTaxeCo2();
    }

    public String getForIdModuleEnteteExiste() {
        return forIdModuleEnteteExiste;
    }

    public String getForIdModuleTout() {
        return forIdModuleTout;
    }

    @Override
    public String getOrder() {
        return order;
    }

    public void setForIdModuleEnteteExiste(String forIdModuleEnteteExiste) {
        this.forIdModuleEnteteExiste = forIdModuleEnteteExiste;
    }

    public void setForIdModuleTout(String forIdModuleTout) {
        this.forIdModuleTout = forIdModuleTout;
    }

    @Override
    public void setOrder(String order) {
        this.order = order;
    }

}
