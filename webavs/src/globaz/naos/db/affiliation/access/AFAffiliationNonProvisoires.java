package globaz.naos.db.affiliation.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * Entité pour les Affiliation Non-Provisoires.
 * 
 * @author ado 21 avr. 04
 */
public class AFAffiliationNonProvisoires extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idAffiliation = "";
    private String idTiers = "";
    private String numAffilie = "";

    private TITiersViewBean tiers = null;

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
    }

    @Override
    protected String _getFields(BStatement statement) {
        return "distinct " + _getCollection() + "afaffip.htitie," + _getCollection() + "afaffip.maiaff,"
                + _getCollection() + "afaffip.malnaf ";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "afaffip inner join " + _getCollection() + "afcotip on " + _getCollection()
                + "afaffip.maiaff=" + _getCollection() + "afcotip.maiaff and mabtra=2 and memmen=0";
    }

    @Override
    protected String _getTableName() {
        return "";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idTiers = statement.dbReadNumeric("HTITIE");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        numAffilie = statement.dbReadString("MALNAF");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + "AFAFFIP.MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getIdAffiliation()));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * Returns the idAffiliation.
     * 
     * @return String
     */
    public String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * Returns the idTiers.
     * 
     * @return String
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Returns the numAffilie.
     * 
     * @return String
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * Returns the tiers.
     * 
     * @return TITiers (null if error)
     */
    public TITiersViewBean getTiers() {

        if ((tiers == null) && (getSession() != null)) {

            tiers = new TITiersViewBean();
            tiers.setSession(getSession());
            tiers.setIdTiers(getIdTiers());
            try {
                tiers.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                tiers = null;
            }
        }
        return tiers;
    }

    /**
     * Sets the idAffiliation.
     * 
     * @param idAffiliation
     *            The idAffiliation to set
     */
    public void setIdAffiliation(String newIdAffiliation) {
        idAffiliation = newIdAffiliation;
    }

    /**
     * Sets the idTiers.
     * 
     * @param idTiers
     *            The idTiers to set
     */
    public void setIdTiers(String newIdTiers) {
        idTiers = newIdTiers;
    }

    /**
     * Sets the numAffilie.
     * 
     * @param numAffilie
     *            The numAffilie to set
     */
    public void setNumAffilie(String newNumAffilie) {
        numAffilie = newNumAffilie;
    }

}
