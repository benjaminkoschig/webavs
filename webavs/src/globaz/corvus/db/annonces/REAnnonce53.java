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
public class REAnnonce53 extends REAnnoncesAbstractLevel3A {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int ALTERNATE_KEY_ID_LIEN_ANNONCE = 1;

    public static final String FIELDNAME_ANCIEN_CODE_CAS_SPEC_1 = "ZDLAC1";
    public static final String FIELDNAME_ANCIEN_CODE_CAS_SPEC_2 = "ZDLAC2";
    public static final String FIELDNAME_ANCIEN_CODE_CAS_SPEC_3 = "ZDLAC3";
    public static final String FIELDNAME_ANCIEN_CODE_CAS_SPEC_4 = "ZDLAC4";
    public static final String FIELDNAME_ANCIEN_CODE_CAS_SPEC_5 = "ZDLAC5";
    public static final String FIELDNAME_ANCIEN_MONTANT_MENSUEL = "ZDMAMM";
    public static final String FIELDNAME_ANCIEN_RAM = "ZDMANR";
    public static final String FIELDNAME_ANCIEN_RED_ANTICIPATION = "ZDMARA";
    public static final String FIELDNAME_ANCIEN_SUPP_AJOURNEMENT = "ZDMASA";
    public static final String FIELDNAME_ETAT_NOMINATIF = "ZDLENO";
    public static final String FIELDNAME_ETAT_ORIGINE = "ZDLEOR";
    public static final String FIELDNAME_FRACTION_RENTE = "ZDTFRE";
    public static final String FIELDNAME_ID_ANNONCE_53 = "ZDIANN";
    public static final String FIELDNAME_OBSERVATION_CENTRALE = "ZDLOCE";

    public static final String TABLE_NAME_ANNONCE_53 = "REANN53";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String ancienCodeCasSpecial1 = "";
    private String ancienCodeCasSpecial2 = "";
    private String ancienCodeCasSpecial3 = "";
    private String ancienCodeCasSpecial4 = "";
    private String ancienCodeCasSpecial5 = "";
    private String ancienMontantMensuel = "";
    private String ancienRAM = "";
    private String ancienRedAnticipation = "";
    private String ancienSupplementAjourn = "";
    private String etatNominatif = "";
    private String etatOrigine = "";
    private String fractionRente = "";
    private String observationCentrale = "";

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
        getFrom += TABLE_NAME_ANNONCE_53;
        getFrom += " ON ";
        getFrom += REAnnonceHeader.FIELDNAME_ID_ANNONCE;
        getFrom += "=";
        getFrom += FIELDNAME_ID_ANNONCE_53;

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
        return TABLE_NAME_ANNONCE_53;
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
        super._readProperties(statement);

        etatNominatif = statement.dbReadString(FIELDNAME_ETAT_NOMINATIF);
        etatOrigine = statement.dbReadString(FIELDNAME_ETAT_ORIGINE);
        fractionRente = statement.dbReadString(FIELDNAME_FRACTION_RENTE);
        ancienRAM = statement.dbReadString(FIELDNAME_ANCIEN_RAM);
        ancienMontantMensuel = statement.dbReadString(FIELDNAME_ANCIEN_MONTANT_MENSUEL);
        ancienCodeCasSpecial1 = statement.dbReadString(FIELDNAME_ANCIEN_CODE_CAS_SPEC_1);
        ancienCodeCasSpecial2 = statement.dbReadString(FIELDNAME_ANCIEN_CODE_CAS_SPEC_2);
        ancienCodeCasSpecial3 = statement.dbReadString(FIELDNAME_ANCIEN_CODE_CAS_SPEC_3);
        ancienCodeCasSpecial4 = statement.dbReadString(FIELDNAME_ANCIEN_CODE_CAS_SPEC_4);
        ancienCodeCasSpecial5 = statement.dbReadString(FIELDNAME_ANCIEN_CODE_CAS_SPEC_5);
        observationCentrale = statement.dbReadString(FIELDNAME_OBSERVATION_CENTRALE);
        ancienSupplementAjourn = statement.dbReadString(FIELDNAME_ANCIEN_SUPP_AJOURNEMENT);
        ancienRedAnticipation = statement.dbReadString(FIELDNAME_ANCIEN_RED_ANTICIPATION);

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
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_ANNONCE_53,
                _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
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

        if (_getAction() == ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(FIELDNAME_ID_ANNONCE_53,
                    _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        }

        statement.writeField(FIELDNAME_ETAT_NOMINATIF,
                _dbWriteString(statement.getTransaction(), etatNominatif, "etatNominatif"));
        statement.writeField(FIELDNAME_ETAT_ORIGINE,
                _dbWriteString(statement.getTransaction(), etatOrigine, "etatOrigine"));
        statement.writeField(FIELDNAME_FRACTION_RENTE,
                _dbWriteString(statement.getTransaction(), fractionRente, "fractionRente"));
        statement.writeField(FIELDNAME_ANCIEN_RAM, _dbWriteString(statement.getTransaction(), ancienRAM, "ancienRAM"));
        statement.writeField(FIELDNAME_ANCIEN_MONTANT_MENSUEL,
                _dbWriteString(statement.getTransaction(), ancienMontantMensuel, "ancienMontantMensuel"));
        statement.writeField(FIELDNAME_ANCIEN_CODE_CAS_SPEC_1,
                _dbWriteString(statement.getTransaction(), ancienCodeCasSpecial1, "ancienCodeCasSpecial1"));
        statement.writeField(FIELDNAME_ANCIEN_CODE_CAS_SPEC_2,
                _dbWriteString(statement.getTransaction(), ancienCodeCasSpecial2, "ancienCodeCasSpecial2"));
        statement.writeField(FIELDNAME_ANCIEN_CODE_CAS_SPEC_3,
                _dbWriteString(statement.getTransaction(), ancienCodeCasSpecial3, "ancienCodeCasSpecial3"));
        statement.writeField(FIELDNAME_ANCIEN_CODE_CAS_SPEC_4,
                _dbWriteString(statement.getTransaction(), ancienCodeCasSpecial4, "ancienCodeCasSpecial4"));
        statement.writeField(FIELDNAME_ANCIEN_CODE_CAS_SPEC_5,
                _dbWriteString(statement.getTransaction(), ancienCodeCasSpecial5, "ancienCodeCasSpecial5"));
        statement.writeField(FIELDNAME_OBSERVATION_CENTRALE,
                _dbWriteString(statement.getTransaction(), observationCentrale, "observationCentrale"));
        statement.writeField(FIELDNAME_ANCIEN_SUPP_AJOURNEMENT,
                _dbWriteString(statement.getTransaction(), ancienSupplementAjourn, "ancienSupplementAjourn"));
        statement.writeField(FIELDNAME_ANCIEN_RED_ANTICIPATION,
                _dbWriteString(statement.getTransaction(), ancienRedAnticipation, "ancienRedAnticipation"));

    }

    public String getAncienCodeCasSpecial1() {
        return ancienCodeCasSpecial1;
    }

    public String getAncienCodeCasSpecial2() {
        return ancienCodeCasSpecial2;
    }

    public String getAncienCodeCasSpecial3() {
        return ancienCodeCasSpecial3;
    }

    public String getAncienCodeCasSpecial4() {
        return ancienCodeCasSpecial4;
    }

    public String getAncienCodeCasSpecial5() {
        return ancienCodeCasSpecial5;
    }

    public String getAncienMontantMensuel() {
        return ancienMontantMensuel;
    }

    public String getAncienRAM() {
        return ancienRAM;
    }

    public String getAncienRedAnticipation() {
        return ancienRedAnticipation;
    }

    public String getAncienSupplementAjourn() {
        return ancienSupplementAjourn;
    }

    public String getEtatNominatif() {
        return etatNominatif;
    }

    public String getEtatOrigine() {
        return etatOrigine;
    }

    public String getFractionRente() {
        return fractionRente;
    }

    public String getObservationCentrale() {
        return observationCentrale;
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

    public void setAncienCodeCasSpecial1(String ancienCodeCasSpecial1) {
        this.ancienCodeCasSpecial1 = ancienCodeCasSpecial1;
    }

    public void setAncienCodeCasSpecial2(String ancienCodeCasSpecial2) {
        this.ancienCodeCasSpecial2 = ancienCodeCasSpecial2;
    }

    public void setAncienCodeCasSpecial3(String ancienCodeCasSpecial3) {
        this.ancienCodeCasSpecial3 = ancienCodeCasSpecial3;
    }

    public void setAncienCodeCasSpecial4(String ancienCodeCasSpecial4) {
        this.ancienCodeCasSpecial4 = ancienCodeCasSpecial4;
    }

    public void setAncienCodeCasSpecial5(String ancienCodeCasSpecial5) {
        this.ancienCodeCasSpecial5 = ancienCodeCasSpecial5;
    }

    public void setAncienMontantMensuel(String ancienMontantMensuel) {
        this.ancienMontantMensuel = ancienMontantMensuel;
    }

    public void setAncienRAM(String ancienRAM) {
        this.ancienRAM = ancienRAM;
    }

    public void setAncienRedAnticipation(String ancienRedAnticipation) {
        this.ancienRedAnticipation = ancienRedAnticipation;
    }

    public void setAncienSupplementAjourn(String ancienSupplementAjourn) {
        this.ancienSupplementAjourn = ancienSupplementAjourn;
    }

    public void setEtatNominatif(String etatNominatif) {
        this.etatNominatif = etatNominatif;
    }

    public void setEtatOrigine(String etatOrigine) {
        this.etatOrigine = etatOrigine;
    }

    public void setFractionRente(String fractionRente) {
        this.fractionRente = fractionRente;
    }

    public void setObservationCentrale(String observationCentrale) {
        this.observationCentrale = observationCentrale;
    }

}
