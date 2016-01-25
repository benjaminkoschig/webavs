/*
 * Créé le 9 juil. 07
 */

package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;

/**
 * @author HPE
 * 
 */

public class REInformationsComptabilite extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_ID_AFFILIE_ADRESSE_PMT = "YNIAAP";

    public static final String FIELDNAME_ID_COMPTE_ANNEXE = "YNICOA";

    public static final String FIELDNAME_ID_DOMAINE_APPLICATION = "YNIDOA";

    public static final String FIELDNAME_ID_INFO_COMPTA = "YNIIIC";
    public static final String FIELDNAME_ID_TIERS_ADRESSE_PMT = "YNITAP";
    public static final String TABLE_NAME_INFO_COMPTA = "REINCOM";
    private String idCompteAnnexe = "";
    private String idInfoCompta = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idTiersAdressePmt = "";

    /**
     * initialise la valeur de Id
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdInfoCompta(_incCounter(transaction, "0"));
    }

    /**
     * 
     * Traitement : Selon description dans fichier :
     * 
     * @link X:\Clients\Webprestations\analyse\Traitement des adresses de paiements.doc
     * 
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        super._beforeUpdate(transaction);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_INFO_COMPTA;
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

        idInfoCompta = statement.dbReadNumeric(FIELDNAME_ID_INFO_COMPTA);
        idTiersAdressePmt = statement.dbReadNumeric(FIELDNAME_ID_TIERS_ADRESSE_PMT);
        idCompteAnnexe = statement.dbReadNumeric(FIELDNAME_ID_COMPTE_ANNEXE);

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
        statement.writeKey(FIELDNAME_ID_INFO_COMPTA,
                _dbWriteNumeric(statement.getTransaction(), idInfoCompta, "idInfoCompta"));
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

        statement.writeField(FIELDNAME_ID_INFO_COMPTA,
                _dbWriteNumeric(statement.getTransaction(), idInfoCompta, "idInfoCompta"));
        statement.writeField(FIELDNAME_ID_TIERS_ADRESSE_PMT,
                _dbWriteNumeric(statement.getTransaction(), idTiersAdressePmt, "idTiersAdressePmt"));
        statement.writeField(FIELDNAME_ID_COMPTE_ANNEXE,
                _dbWriteNumeric(statement.getTransaction(), idCompteAnnexe, "idCompteAnnexe"));

    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @return
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return
     */
    public String getIdInfoCompta() {
        return idInfoCompta;
    }

    /**
     * @return
     */
    public String getIdTiersAdressePmt() {
        return idTiersAdressePmt;
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
     * setter pour l'attribut adresse paiement.
     * 
     * @param adressePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setAdressePaiement(TIAdressePaiementData adressePaiement) {
        if (adressePaiement != null) {
            idTiersAdressePmt = adressePaiement.getIdTiers();
        } else {
            idTiersAdressePmt = "";

        }
    }

    /**
     * @param string
     */
    public void setIdCompteAnnexe(String string) {
        idCompteAnnexe = string;
    }

    /**
     * @param string
     */
    public void setIdInfoCompta(String string) {
        idInfoCompta = string;
    }

    /**
     * @param string
     */
    public void setIdTiersAdressePmt(String string) {
        idTiersAdressePmt = string;
    }

}
