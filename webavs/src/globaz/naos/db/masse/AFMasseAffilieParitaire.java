package globaz.naos.db.masse;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * Entité pour la liste des affiliés paritaires actifs avec masse AVS > 200'000.- et périodicité trimestrielle.
 * 
 * @author SEL <br>
 *         Date : 19 févr. 08
 */
public class AFMasseAffilieParitaire extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = "";
    private String idAffiliation = "";
    private String idTiers = "";
    private String masseAnnuelle = "";
    private String numAffilie = "";
    private TITiersViewBean tiers = null;

    /**
     * Effectue des traitements après une lecture dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_afterRetrieve(BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idTiers = statement.dbReadNumeric("HTITIE");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        numAffilie = statement.dbReadString("MALNAF");
        dateDebut = statement.dbReadString("MADDEB");
        masseAnnuelle = statement.dbReadString("MEMMAP");
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + "AFAFFIP.MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getIdAffiliation()));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
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
     * @return the masseAnnuel
     */
    public String getMasseAnnuelle() {
        return masseAnnuelle;
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
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
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
     * @param masseAnnuel
     *            the masseAnnuel to set
     */
    public void setMasseAnnuelle(String masseAnnuel) {
        masseAnnuelle = masseAnnuel;
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
