package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.tools.PRDateFormater;

public class RERentesAccordeesJoinDemRente extends BEntity {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codePrestation = "";
    private String csEtat = "";
    private String dateDebutDroit = "";
    private String dateFinDroit = "";
    private String idRenteAccordee = "";
    private String idTiersBeneficiaire = "";
    private String montantPrestation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(final BTransaction transaction) throws Exception {
        throw new Exception("Unsupported function call");
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(final BStatement statement) throws Exception {

        idRenteAccordee = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        idTiersBeneficiaire = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        codePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        montantPrestation = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
        dateDebutDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
        dateFinDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));
        csEtat = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_CS_ETAT);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(final BStatement statement) throws Exception {
        ;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(final globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), idRenteAccordee, "idRenteAccordee"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(final BStatement statement) throws Exception {
        throw new Exception("Unsupported function call");
    }

    /**
     * @return
     */
    public String getCodePrestation() {
        return codePrestation;
    }

    /**
     * @return
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * @return
     */
    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    /**
     * @return
     */
    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    /**
     * @return
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    /**
     * @return
     */
    public String getMontantPrestation() {
        return montantPrestation;
    }

    /**
     * @param string
     */
    public void setCodePrestation(final String string) {
        codePrestation = string;
    }

    /**
     * @param string
     */
    public void setCsEtat(final String string) {
        csEtat = string;
    }

    /**
     * @param string
     */
    public void setDateDebutDroit(final String string) {
        dateDebutDroit = string;
    }

    /**
     * @param string
     */
    public void setDateFinDroit(final String string) {
        dateFinDroit = string;
    }

    /**
     * @param string
     */
    public void setIdTiersBeneficiaire(final String string) {
        idTiersBeneficiaire = string;
    }

    /**
     * @param string
     */
    public void setMontantPrestation(final String string) {
        montantPrestation = string;
    }

}
