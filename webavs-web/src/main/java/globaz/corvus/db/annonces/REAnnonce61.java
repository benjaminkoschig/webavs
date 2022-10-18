/*
 * Créé le 2 août 07
 */
package globaz.corvus.db.annonces;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author HPE
 * 
 */
public class REAnnonce61 extends REAnnoncesAbstractLevel1A {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int ALTERNATE_KEY_ID_LIEN_ANNONCE = 1;

    public static final String FIELDNAME_ID_ANNONCE_61 = "ID_ANNONCE_61";
    public static final String FIELDNAME_NSS_ANNONCE_61 = "NSS_ANNONCE_61";
    public static final String FIELDNAME_NOUVEAU_MONTANT = "NOUVEAU_MONTANT";
    public static final String FIELDNAME_ANCIEN_MONTANT = "ANCIEN_MONTANT";
    public static final String FIELDNAME_GENRE = "GENRE";
    public static final String FIELDNAME_DATE_RAPPORT = "DATE_RAPPORT";
    public static final String FIELDNAME_DATE_ANNONCE = "DATE_ANNONCE";
    public static final String FIELDNAME_CODE_RETOUR = "CODE_RETOUR";
    public static final String FIELDNAME_DEGRE_INVALIDITE = "DEGRE_INVALIDITE";
    public static final String FIELDNAME_FRACTION_RENTE = "FRACTION";
    public static final String FIELDNAME_OBSERVATION = "OBSERVATION";

    public static final String FIELDNAME_QUOTITE_ANNONCE_61 = "QUOTITE_ANNONCE_61";

    public static final String TABLE_NAME_ANNONCE_61 = "REANN61";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String nss = "";
    private String nouveauMontant = "";
    private String ancienMontant = "";
    private String genre = "";
    private String dateAnnonce = "";
    private String dateRapport = "";
    private String codeRetour = "";
    private String degreInvalidite = "";
    private String fractionRente = "";
    private String observation = "";
    private String quotite = "";


    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * initialise la valeur de Id annonce header
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += super._getFrom(statement);

        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += TABLE_NAME_ANNONCE_61;
        getFrom += " ON ";
        getFrom += REAnnonceHeader.FIELDNAME_ID_ANNONCE;
        getFrom += "=";
        getFrom += FIELDNAME_ID_ANNONCE_61;

        return getFrom;
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
        return TABLE_NAME_ANNONCE_61;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        nss = statement.dbReadString(FIELDNAME_NSS_ANNONCE_61);
        nouveauMontant = statement.dbReadString(FIELDNAME_NOUVEAU_MONTANT);
        ancienMontant = statement.dbReadString(FIELDNAME_ANCIEN_MONTANT);
        genre = statement.dbReadString(FIELDNAME_GENRE);
        dateAnnonce = statement.dbReadString(FIELDNAME_DATE_ANNONCE);
        dateRapport = statement.dbReadString(FIELDNAME_DATE_RAPPORT);
        codeRetour = statement.dbReadString(FIELDNAME_CODE_RETOUR);
        degreInvalidite = statement.dbReadString(FIELDNAME_DEGRE_INVALIDITE);
        fractionRente = statement.dbReadString(FIELDNAME_FRACTION_RENTE);
        observation = statement.dbReadString(FIELDNAME_OBSERVATION);
        quotite = statement.dbReadNumeric(FIELDNAME_QUOTITE_ANNONCE_61);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(BStatement)
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

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case ALTERNATE_KEY_ID_LIEN_ANNONCE:
                statement.writeKey(FIELDNAME_ID_LIEN_ANNONCE,
                        _dbWriteNumeric(statement.getTransaction(), getIdLienAnnonce(), "idLienAnnonce"));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_ANNONCE_61,
                _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        if (_getAction() == ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(FIELDNAME_ID_ANNONCE_61,
                    _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        }

        statement.writeField(FIELDNAME_FRACTION_RENTE, _dbWriteString(statement.getTransaction(), fractionRente, "fractionRente"));
        statement.writeField(FIELDNAME_OBSERVATION, _dbWriteString(statement.getTransaction(), observation, "observation"));
        statement.writeField(FIELDNAME_NSS_ANNONCE_61, _dbWriteString(statement.getTransaction(), nss, "nss"));
        statement.writeField(FIELDNAME_DEGRE_INVALIDITE, _dbWriteString(statement.getTransaction(), degreInvalidite, "degreInvalidite"));
        statement.writeField(FIELDNAME_CODE_RETOUR, _dbWriteString(statement.getTransaction(), codeRetour, "codeRetour"));
        statement.writeField(FIELDNAME_DATE_RAPPORT, _dbWriteString(statement.getTransaction(), dateRapport, "dateRapport"));
        statement.writeField(FIELDNAME_DATE_ANNONCE, _dbWriteString(statement.getTransaction(), dateAnnonce, "dateAnnonce"));
        statement.writeField(FIELDNAME_ANCIEN_MONTANT, _dbWriteString(statement.getTransaction(), ancienMontant, "ancienMontant"));
        statement.writeField(FIELDNAME_NOUVEAU_MONTANT, _dbWriteString(statement.getTransaction(), nouveauMontant, "nouveauMontant"));
        statement.writeField(FIELDNAME_GENRE, _dbWriteString(statement.getTransaction(), genre, "genre"));


        statement.writeField(FIELDNAME_QUOTITE_ANNONCE_61, _dbWriteNumeric(statement.getTransaction(), quotite, "quotite"));
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
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return true;
    }

    public String getQuotite() {
        return quotite;
    }

    public void setQuotite(String quotite) {
        this.quotite = quotite;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getNouveauMontant() {
        return nouveauMontant;
    }

    public void setNouveauMontant(String nouveauMontant) {
        this.nouveauMontant = nouveauMontant;
    }

    public String getAncienMontant() {
        return ancienMontant;
    }

    public void setAncienMontant(String ancienMontant) {
        this.ancienMontant = ancienMontant;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDateRapport() {
        return dateRapport;
    }

    public void setDateRapport(String dateRapport) {
        this.dateRapport = dateRapport;
    }

    public String getCodeRetour() {
        return codeRetour;
    }

    public void setCodeRetour(String codeRetour) {
        this.codeRetour = codeRetour;
    }

    public String getDegreInvalidite() {
        return degreInvalidite;
    }

    public void setDegreInvalidite(String degreInvalidite) {
        this.degreInvalidite = degreInvalidite;
    }

    public String getFractionRente() {
        return fractionRente;
    }

    public void setFractionRente(String fractionRente) {
        this.fractionRente = fractionRente;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getDateAnnonce() {
        return dateAnnonce;
    }

    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }
}
