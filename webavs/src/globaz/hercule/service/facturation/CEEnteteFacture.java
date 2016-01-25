package globaz.hercule.service.facturation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Entité représentant une facture et une date de facturation
 * 
 * @author Sullivann Corneille
 * @since 21 févr. 2014
 */
public class CEEnteteFacture extends BEntity {

    private static final long serialVersionUID = 6611665241813946504L;
    private String totalFacture = "";
    private String dateFacturation = "";

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        totalFacture = statement.dbReadNumeric("TOTALFACTURE");
        dateFacturation = statement.dbReadDateAMJ("DATEFACTURATION");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    @Override
    protected boolean _allowAdd() {
        return false;
    }

    @Override
    protected boolean _allowDelete() {
        return false;
    }

    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * Getter de totalFacture
     * 
     * @return the totalFacture
     */
    public String getTotalFacture() {
        return totalFacture;
    }

    /**
     * Setter de totalFacture
     * 
     * @param totalFacture the totalFacture to set
     */
    public void setTotalFacture(String totalFacture) {
        this.totalFacture = totalFacture;
    }

    /**
     * Getter de dateFacturation
     * 
     * @return the dateFacturation
     */
    public String getDateFacturation() {
        return dateFacturation;
    }

    /**
     * Setter de dateFacturation
     * 
     * @param dateFacturation the dateFacturation to set
     */
    public void setDateFacturation(String dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

}
