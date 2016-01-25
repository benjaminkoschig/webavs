/*
 * Créé le 19 juil. 07
 */

package globaz.corvus.db.creances;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author BSC
 * 
 */
public class RECreanceAccordee extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_ID_CREANCE_ACCORDEE = "YRICAC";

    public static final String FIELDNAME_ID_CREANCIER = "YRICRE";
    public static final String FIELDNAME_ID_ORDRE_VERSEMENT = "YRIOVE";
    public static final String FIELDNAME_ID_RENTE_ACCORDEE = "YRIRAC";
    public static final String FIELDNAME_MONTANT = "YRMONT";
    public static final String TABLE_NAME_CREANCES_ACCORDEES = "RECRACC";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idCreanceAccordee = "";
    private String idCreancier = "";
    private String idOrdreVersement = "";
    private String idRenteAccordee = "";
    private String montant = "";

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
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdCreanceAccordee(_incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_CREANCES_ACCORDEES;
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

        idCreanceAccordee = statement.dbReadNumeric(FIELDNAME_ID_CREANCE_ACCORDEE);
        idCreancier = statement.dbReadNumeric(FIELDNAME_ID_CREANCIER);
        idRenteAccordee = statement.dbReadNumeric(FIELDNAME_ID_RENTE_ACCORDEE);
        montant = statement.dbReadNumeric(FIELDNAME_MONTANT);
        idOrdreVersement = statement.dbReadNumeric(FIELDNAME_ID_ORDRE_VERSEMENT);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
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
        statement.writeKey(FIELDNAME_ID_CREANCE_ACCORDEE,
                _dbWriteNumeric(statement.getTransaction(), idCreanceAccordee, "idCreanceAccordee"));
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

        statement.writeField(FIELDNAME_ID_CREANCE_ACCORDEE,
                _dbWriteNumeric(statement.getTransaction(), idCreanceAccordee, "idCreancier"));
        statement.writeField(FIELDNAME_ID_CREANCIER,
                _dbWriteNumeric(statement.getTransaction(), idCreancier, "idDemandeRente"));
        statement.writeField(FIELDNAME_ID_RENTE_ACCORDEE,
                _dbWriteNumeric(statement.getTransaction(), idRenteAccordee, "idTiersAdressePmt"));
        statement.writeField(FIELDNAME_MONTANT, _dbWriteNumeric(statement.getTransaction(), montant, "montant"));
        statement.writeField(FIELDNAME_ID_ORDRE_VERSEMENT,
                _dbWriteNumeric(statement.getTransaction(), idOrdreVersement, "idOrdreVersement"));
    }

    /**
     * @return
     */
    public String getIdCreanceAccordee() {
        return idCreanceAccordee;
    }

    /**
     * @return
     */
    public String getIdCreancier() {
        return idCreancier;
    }

    /**
     * @return
     */
    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    /**
     * @return
     */
    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    /**
     * @return
     */
    public String getMontant() {
        return montant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    /**
     * @param string
     */
    public void setIdCreanceAccordee(String string) {
        idCreanceAccordee = string;
    }

    /**
     * @param string
     */
    public void setIdCreancier(String string) {
        idCreancier = string;
    }

    /**
     * @param string
     */
    public void setIdOrdreVersement(String string) {
        idOrdreVersement = string;
    }

    /**
     * @param string
     */
    public void setIdRenteAccordee(String string) {
        idRenteAccordee = string;
    }

    /**
     * @param string
     */
    public void setMontant(String string) {
        montant = string;
    }

}
