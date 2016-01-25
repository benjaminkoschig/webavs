/*
 * Créé le 07 nov. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.tools.PRDateFormater;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public class REPrestationAccordeeBloquee extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_BLOCAGE = "ZODBLO";
    public static final String FIELDNAME_ID_ENTETE_BLOCAGE = "ZOIEBK";
    public static final String FIELDNAME_ID_PA_BLOQUEE = "ZOIPAB";
    public static final String FIELDNAME_MONTANT = "ZOMBLO";
    public static final String TABLE_NAME_PRESTATION_ACCORDEE_BLOQUEE = "REPABLCK";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // Format : mm.aaaa
    private String dateBlocage = "";

    private String idEnteteBlocage = "";

    private String idPrestationAccordeeBloquee = "";
    private String montant = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * initialise la valeur de Id période api
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdPrestationAccordeeBloquee(_incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_PRESTATION_ACCORDEE_BLOQUEE;
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
    protected void _readProperties(BStatement statement) throws Exception {

        idPrestationAccordeeBloquee = statement.dbReadNumeric(FIELDNAME_ID_PA_BLOQUEE);
        dateBlocage = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement.dbReadNumeric(FIELDNAME_DATE_BLOCAGE));
        idEnteteBlocage = statement.dbReadNumeric(FIELDNAME_ID_ENTETE_BLOCAGE);
        montant = statement.dbReadNumeric(FIELDNAME_MONTANT);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement
                .writeKey(
                        FIELDNAME_ID_PA_BLOQUEE,
                        _dbWriteNumeric(statement.getTransaction(), idPrestationAccordeeBloquee,
                                "idPrestationAccordeeBloquee"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement
                .writeField(
                        FIELDNAME_ID_PA_BLOQUEE,
                        _dbWriteNumeric(statement.getTransaction(), idPrestationAccordeeBloquee,
                                "idPrestationAccordeeBloquee"));
        statement.writeField(FIELDNAME_ID_ENTETE_BLOCAGE,
                _dbWriteNumeric(statement.getTransaction(), idEnteteBlocage, "idEnteteBlocage"));

        statement.writeField(FIELDNAME_MONTANT, _dbWriteNumeric(statement.getTransaction(), montant, "montant"));
        statement.writeField(
                FIELDNAME_DATE_BLOCAGE,
                _dbWriteNumeric(statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateBlocage),
                        "dateBlocage"));

    }

    public String getDateBlocage() {
        return dateBlocage;
    }

    public String getIdEnteteBlocage() {
        return idEnteteBlocage;
    }

    public String getIdPrestationAccordeeBloquee() {
        return idPrestationAccordeeBloquee;
    }

    public String getMontant() {
        return montant;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setDateBlocage(String dateBlocage) {
        this.dateBlocage = dateBlocage;
    }

    public void setIdEnteteBlocage(String idEnteteBlocage) {
        this.idEnteteBlocage = idEnteteBlocage;
    }

    public void setIdPrestationAccordeeBloquee(String idPrestationAccordeeBloquee) {
        this.idPrestationAccordeeBloquee = idPrestationAccordeeBloquee;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
