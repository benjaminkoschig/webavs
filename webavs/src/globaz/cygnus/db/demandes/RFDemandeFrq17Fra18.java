/*
 * Créé le 06 janvier 2010
 */
package globaz.cygnus.db.demandes;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author jje
 */
public class RFDemandeFrq17Fra18 extends RFDemande {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CS_GENRE_DE_SOIN = "EITGSO";
    public static final String FIELDNAME_DATE_DECOMPTE = "EIDDEC";
    public static final String FIELDNAME_ID_DEMANDE_1718 = "EIIDEM";
    public static final String FIELDNAME_MONTANT_DECOMPTE = "EIMDEC";
    public static final String FIELDNAME_NUMERO_DECOMPTE = "EINDEC";

    public static final String TABLE_NAME = "RFD1718";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csGenreDeSoin = "";
    private String dateDecompte = "";
    private boolean hasCreationSpy = false;
    private boolean hasSpy = false;
    private String idDemande1718 = "";

    private String montantDecompte = "";
    private String numeroDecompte = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDemandeFrq17Fra18
     */
    public RFDemandeFrq17Fra18() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return faux
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // super._beforeAdd(transaction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += _getCollection();
        getFrom += RFDemande.TABLE_NAME;
        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += RFDemandeFrq17Fra18.TABLE_NAME;
        getFrom += " ON ";
        getFrom += RFDemandeFrq17Fra18.FIELDNAME_ID_DEMANDE_1718;
        getFrom += "=";
        getFrom += RFDemande.FIELDNAME_ID_DEMANDE;

        return getFrom;
    }

    /**
     * getter pour le nom de la table des demandes frqp et fra
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFDemandeFrq17Fra18.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des demandes frqp et fra
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idDemande1718 = statement.dbReadNumeric(RFDemandeFrq17Fra18.FIELDNAME_ID_DEMANDE_1718);
        dateDecompte = statement.dbReadDateAMJ(RFDemandeFrq17Fra18.FIELDNAME_DATE_DECOMPTE);
        montantDecompte = statement.dbReadNumeric(RFDemandeFrq17Fra18.FIELDNAME_MONTANT_DECOMPTE);
        numeroDecompte = statement.dbReadString(RFDemandeFrq17Fra18.FIELDNAME_NUMERO_DECOMPTE);
        csGenreDeSoin = statement.dbReadNumeric(RFDemandeFrq17Fra18.FIELDNAME_CS_GENRE_DE_SOIN);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des demandes frqp et fra
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFDemandeFrq17Fra18.FIELDNAME_ID_DEMANDE_1718,
                this._dbWriteNumeric(statement.getTransaction(), idDemande1718, "idDemande1718"));
    }

    /**
     * Méthode d'écriture des champs dans la table des demandes frqp et fra
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // super._writeProperties(statement);
        statement.writeField(RFDemandeFrq17Fra18.FIELDNAME_ID_DEMANDE_1718,
                this._dbWriteNumeric(statement.getTransaction(), idDemande1718, "idDemande1718"));
        statement.writeField(RFDemandeFrq17Fra18.FIELDNAME_DATE_DECOMPTE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDecompte, "dateDecompte"));
        statement.writeField(RFDemandeFrq17Fra18.FIELDNAME_MONTANT_DECOMPTE,
                this._dbWriteNumeric(statement.getTransaction(), montantDecompte, "montantDecompte"));
        statement.writeField(RFDemandeFrq17Fra18.FIELDNAME_NUMERO_DECOMPTE,
                this._dbWriteString(statement.getTransaction(), numeroDecompte, "numeroDecompte"));
        statement.writeField(RFDemandeFrq17Fra18.FIELDNAME_CS_GENRE_DE_SOIN,
                this._dbWriteNumeric(statement.getTransaction(), csGenreDeSoin, "csGenreDeSoin"));

    }

    public String getCsGenreDeSoin() {
        return csGenreDeSoin;
    }

    public String getDateDecompte() {
        return dateDecompte;
    }

    public String getIdDemande1718() {
        return idDemande1718;
    }

    public String getMontantDecompte() {
        return montantDecompte;
    }

    public String getNumeroDecompte() {
        return numeroDecompte;
    }

    @Override
    public boolean hasCreationSpy() {
        return hasCreationSpy;
    }

    @Override
    public boolean hasSpy() {
        return hasSpy;
    }

    public void setCsGenreDeSoin(String csGenreDeSoin) {
        this.csGenreDeSoin = csGenreDeSoin;
    }

    public void setDateDecompte(String dateDecompte) {
        this.dateDecompte = dateDecompte;
    }

    public void setHasCreationSpy(boolean hasCreationSpy) {
        this.hasCreationSpy = hasCreationSpy;
    }

    public void setHasSpy(boolean hasSpy) {
        this.hasSpy = hasSpy;
    }

    public void setIdDemande1718(String idDemande1718) {
        this.idDemande1718 = idDemande1718;
    }

    public void setMontantDecompte(String montantDecompte) {
        this.montantDecompte = montantDecompte;
    }

    public void setNumeroDecompte(String numeroDecompte) {
        this.numeroDecompte = numeroDecompte;
    }

}