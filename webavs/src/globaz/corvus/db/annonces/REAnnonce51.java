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
public class REAnnonce51 extends REAnnoncesAbstractLevel3B {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int ALTERNATE_KEY_ID_LIEN_ANNONCE = 1;

    public static final String FIELDNAME_ANCIEN_CAS_SPEC_1_TRANSFERT = "ZFLC1T";
    public static final String FIELDNAME_ANCIEN_CAS_SPEC_2_TRANSFERT = "ZFLC2T";
    public static final String FIELDNAME_ANCIEN_CAS_SPEC_3_TRANSFERT = "ZFLC3T";
    public static final String FIELDNAME_ANCIEN_CODE_CAS_SPEC_1 = "ZFLAC1";
    public static final String FIELDNAME_ANCIEN_CODE_CAS_SPEC_2 = "ZFLAC2";
    public static final String FIELDNAME_ANCIEN_CODE_CAS_SPEC_3 = "ZFLAC3";
    public static final String FIELDNAME_ANCIEN_CODE_CAS_SPEC_4 = "ZFLAC4";
    public static final String FIELDNAME_ANCIEN_CODE_CAS_SPEC_5 = "ZFLAC5";
    public static final String FIELDNAME_ANCIEN_GENRE_PRESTATION = "ZFLAGE";
    public static final String FIELDNAME_ANCIEN_MONTANT_MENSUEL = "ZFMAMM";
    public static final String FIELDNAME_ANCIEN_MONTANT_PREST_AV_TRANSFERT = "ZFMAMA";
    public static final String FIELDNAME_ANCIEN_RAM = "ZFMARA";
    public static final String FIELDNAME_ANCIEN_RAM_AVANT_TRANSFERT = "ZFMRAV";

    // nouveaux champs pour la reprise des annonces biennales
    public static final String FIELDNAME_ANCIEN_RAM_SANS_BTE = "ZFMARS";
    public static final String FIELDNAME_ANCIEN_REVENU_PRIS_COMPTE = "ZFBREV";
    public static final String FIELDNAME_ANCIEN_SUPP_AJOURN_TRANSFERT = "ZFMAST";
    public static final String FIELDNAME_ANCIEN_SUPPL_AJOURN = "ZFMASA";
    public static final String FIELDNAME_ANCIENNE_BTE_MOYENNE_PRIS_COMPTE = "ZFMABT";
    public static final String FIELDNAME_ANCIENNE_ECHELLE_RENTE = "ZFLAEC";
    public static final String FIELDNAME_ETAT_NOMINATIF = "ZFLENO";
    public static final String FIELDNAME_ETAT_ORIGINE = "ZFLEOR";
    public static final String FIELDNAME_FRACTION_RENTE = "ZFTFRE";
    public static final String FIELDNAME_ID_ANNONCE_51 = "ZFIANN";
    public static final String FIELDNAME_MONT_ANC_RENTE_REMPLACEE = "ZFMARO";
    public static final String FIELDNAME_OBSERVATION_CENTRALE = "ZFLOCE";

    public static final String TABLE_NAME_ANNONCE_51 = "REANN51";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String ancienCasSpec1AvantTransfert = "";
    private String ancienCasSpec2AvantTransfert = "";
    private String ancienCasSpec3AvantTransfert = "";
    private String ancienCodeCasSpecial1 = "";
    private String ancienCodeCasSpecial2 = "";
    private String ancienCodeCasSpecial3 = "";
    private String ancienCodeCasSpecial4 = "";
    private String ancienCodeCasSpecial5 = "";
    private String ancienGenrePrestation = "";
    private String ancienMontantMensuel = "";
    private String ancienMontantPrestationAvantTransfert = "";
    private String ancienneBTEMoyennePrisCompte = "";

    private String ancienneEchelleRente = "";
    private String ancienRAM = "";
    private String ancienRAMAvantTransfert = "";
    private String ancienRAMSansBTE = "";
    private String ancienRevenuPrisCompte = "";
    private String ancienSupplAjournementTransfert = "";
    private String ancienSupplementAjournement = "";
    private String etatNominatif = "";
    private String etatOrigine = "";
    private String fractionRente = "";
    private String montantAncRenteRemplacee = "";
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
        getFrom += TABLE_NAME_ANNONCE_51;
        getFrom += " ON ";
        getFrom += REAnnonceHeader.FIELDNAME_ID_ANNONCE;
        getFrom += "=";
        getFrom += FIELDNAME_ID_ANNONCE_51;

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
        return TABLE_NAME_ANNONCE_51;
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
        montantAncRenteRemplacee = statement.dbReadString(FIELDNAME_MONT_ANC_RENTE_REMPLACEE);
        ancienRAM = statement.dbReadString(FIELDNAME_ANCIEN_RAM);
        ancienMontantMensuel = statement.dbReadString(FIELDNAME_ANCIEN_MONTANT_MENSUEL);
        ancienCodeCasSpecial1 = statement.dbReadString(FIELDNAME_ANCIEN_CODE_CAS_SPEC_1);
        ancienCodeCasSpecial2 = statement.dbReadString(FIELDNAME_ANCIEN_CODE_CAS_SPEC_2);
        ancienCodeCasSpecial3 = statement.dbReadString(FIELDNAME_ANCIEN_CODE_CAS_SPEC_3);
        ancienCodeCasSpecial4 = statement.dbReadString(FIELDNAME_ANCIEN_CODE_CAS_SPEC_4);
        ancienCodeCasSpecial5 = statement.dbReadString(FIELDNAME_ANCIEN_CODE_CAS_SPEC_5);
        observationCentrale = statement.dbReadString(FIELDNAME_OBSERVATION_CENTRALE);

        ancienRAMSansBTE = statement.dbReadString(FIELDNAME_ANCIEN_RAM_SANS_BTE);
        ancienneBTEMoyennePrisCompte = statement.dbReadString(FIELDNAME_ANCIENNE_BTE_MOYENNE_PRIS_COMPTE);
        ancienSupplementAjournement = statement.dbReadString(FIELDNAME_ANCIEN_SUPPL_AJOURN);
        ancienGenrePrestation = statement.dbReadString(FIELDNAME_ANCIEN_GENRE_PRESTATION);
        ancienRAMAvantTransfert = statement.dbReadString(FIELDNAME_ANCIEN_RAM_AVANT_TRANSFERT);
        ancienRevenuPrisCompte = statement.dbReadString(FIELDNAME_ANCIEN_REVENU_PRIS_COMPTE);
        ancienMontantPrestationAvantTransfert = statement.dbReadString(FIELDNAME_ANCIEN_MONTANT_PREST_AV_TRANSFERT);
        ancienCasSpec1AvantTransfert = statement.dbReadString(FIELDNAME_ANCIEN_CAS_SPEC_1_TRANSFERT);
        ancienCasSpec2AvantTransfert = statement.dbReadString(FIELDNAME_ANCIEN_CAS_SPEC_2_TRANSFERT);
        ancienCasSpec3AvantTransfert = statement.dbReadString(FIELDNAME_ANCIEN_CAS_SPEC_3_TRANSFERT);
        ancienSupplAjournementTransfert = statement.dbReadString(FIELDNAME_ANCIEN_SUPP_AJOURN_TRANSFERT);
        ancienneEchelleRente = statement.dbReadString(FIELDNAME_ANCIENNE_ECHELLE_RENTE);

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
        statement.writeKey(FIELDNAME_ID_ANNONCE_51,
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
            statement.writeField(FIELDNAME_ID_ANNONCE_51,
                    _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        }

        statement.writeField(FIELDNAME_ETAT_NOMINATIF,
                _dbWriteString(statement.getTransaction(), etatNominatif, "etatNominatif"));
        statement.writeField(FIELDNAME_ETAT_ORIGINE,
                _dbWriteString(statement.getTransaction(), etatOrigine, "etatOrigine"));
        statement.writeField(FIELDNAME_FRACTION_RENTE,
                _dbWriteString(statement.getTransaction(), fractionRente, "fractionRente"));
        statement.writeField(FIELDNAME_MONT_ANC_RENTE_REMPLACEE,
                _dbWriteString(statement.getTransaction(), montantAncRenteRemplacee, "montantAncRenteRemplacee"));
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

        statement.writeField(FIELDNAME_ANCIEN_RAM_SANS_BTE,
                _dbWriteString(statement.getTransaction(), ancienRAMSansBTE, "ancienRAMSansBTE"));
        statement
                .writeField(
                        FIELDNAME_ANCIENNE_BTE_MOYENNE_PRIS_COMPTE,
                        _dbWriteString(statement.getTransaction(), ancienneBTEMoyennePrisCompte,
                                "ancienneBTEMoyennePrisCompte"));
        statement.writeField(FIELDNAME_ANCIEN_SUPPL_AJOURN,
                _dbWriteString(statement.getTransaction(), ancienSupplementAjournement, "ancienSupplementAjournement"));
        statement.writeField(FIELDNAME_ANCIEN_GENRE_PRESTATION,
                _dbWriteString(statement.getTransaction(), ancienGenrePrestation, "ancienGenrePrestation"));
        statement.writeField(FIELDNAME_ANCIEN_RAM_AVANT_TRANSFERT,
                _dbWriteString(statement.getTransaction(), ancienRAMAvantTransfert, "ancienRAMAvantTransfert"));
        statement.writeField(FIELDNAME_ANCIEN_REVENU_PRIS_COMPTE,
                _dbWriteString(statement.getTransaction(), ancienRevenuPrisCompte, "ancienRevenuPrisCompte"));
        statement.writeField(
                FIELDNAME_ANCIEN_MONTANT_PREST_AV_TRANSFERT,
                _dbWriteString(statement.getTransaction(), ancienMontantPrestationAvantTransfert,
                        "ancienMontantPrestationAvantTransfert"));
        statement
                .writeField(
                        FIELDNAME_ANCIEN_CAS_SPEC_1_TRANSFERT,
                        _dbWriteString(statement.getTransaction(), ancienCasSpec1AvantTransfert,
                                "ancienCasSpec1AvantTransfert"));
        statement
                .writeField(
                        FIELDNAME_ANCIEN_CAS_SPEC_2_TRANSFERT,
                        _dbWriteString(statement.getTransaction(), ancienCasSpec2AvantTransfert,
                                "ancienCasSpec2AvantTransfert"));
        statement
                .writeField(
                        FIELDNAME_ANCIEN_CAS_SPEC_3_TRANSFERT,
                        _dbWriteString(statement.getTransaction(), ancienCasSpec3AvantTransfert,
                                "ancienCasSpec3AvantTransfert"));
        statement.writeField(
                FIELDNAME_ANCIEN_SUPP_AJOURN_TRANSFERT,
                _dbWriteString(statement.getTransaction(), ancienSupplAjournementTransfert,
                        "ancienSupplAjournementTransfert"));
        statement.writeField(FIELDNAME_ANCIENNE_ECHELLE_RENTE,
                _dbWriteString(statement.getTransaction(), ancienneEchelleRente, "ancienneEchelleRente"));
    }

    public String getAncienCasSpec1AvantTransfert() {
        return ancienCasSpec1AvantTransfert;
    }

    public String getAncienCasSpec2AvantTransfert() {
        return ancienCasSpec2AvantTransfert;
    }

    public String getAncienCasSpec3AvantTransfert() {
        return ancienCasSpec3AvantTransfert;
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

    public String getAncienGenrePrestation() {
        return ancienGenrePrestation;
    }

    public String getAncienMontantMensuel() {
        return ancienMontantMensuel;
    }

    public String getAncienMontantPrestationAvantTransfert() {
        return ancienMontantPrestationAvantTransfert;
    }

    public String getAncienneBTEMoyennePrisCompte() {
        return ancienneBTEMoyennePrisCompte;
    }

    public String getAncienneEchelleRente() {
        return ancienneEchelleRente;
    }

    public String getAncienRAM() {
        return ancienRAM;
    }

    public String getAncienRAMAvantTransfert() {
        return ancienRAMAvantTransfert;
    }

    public String getAncienRAMSansBTE() {
        return ancienRAMSansBTE;
    }

    public String getAncienRevenuPrisCompte() {
        return ancienRevenuPrisCompte;
    }

    public String getAncienSupplAjournementTransfert() {
        return ancienSupplAjournementTransfert;
    }

    public String getAncienSupplementAjournement() {
        return ancienSupplementAjournement;
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

    public String getMontantAncRenteRemplacee() {
        return montantAncRenteRemplacee;
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

    public void setAncienCasSpec1AvantTransfert(String ancienCasSpec1AvantTransfert) {
        this.ancienCasSpec1AvantTransfert = ancienCasSpec1AvantTransfert;
    }

    public void setAncienCasSpec2AvantTransfert(String ancienCasSpec2AvantTransfert) {
        this.ancienCasSpec2AvantTransfert = ancienCasSpec2AvantTransfert;
    }

    public void setAncienCasSpec3AvantTransfert(String ancienCasSpec3AvantTransfert) {
        this.ancienCasSpec3AvantTransfert = ancienCasSpec3AvantTransfert;
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

    public void setAncienGenrePrestation(String ancienGenrePrestation) {
        this.ancienGenrePrestation = ancienGenrePrestation;
    }

    public void setAncienMontantMensuel(String ancienMontantMensuel) {
        this.ancienMontantMensuel = ancienMontantMensuel;
    }

    public void setAncienMontantPrestationAvantTransfert(String ancienMontantPrestationAvantTransfert) {
        this.ancienMontantPrestationAvantTransfert = ancienMontantPrestationAvantTransfert;
    }

    public void setAncienneBTEMoyennePrisCompte(String ancienneBTEMoyennePrisCompte) {
        this.ancienneBTEMoyennePrisCompte = ancienneBTEMoyennePrisCompte;
    }

    public void setAncienneEchelleRente(String ancienneEchelleRente) {
        this.ancienneEchelleRente = ancienneEchelleRente;
    }

    public void setAncienRAM(String ancienRAM) {
        this.ancienRAM = ancienRAM;
    }

    public void setAncienRAMAvantTransfert(String ancienRAMAvantTransfert) {
        this.ancienRAMAvantTransfert = ancienRAMAvantTransfert;
    }

    public void setAncienRAMSansBTE(String ancienRAMSansBTE) {
        this.ancienRAMSansBTE = ancienRAMSansBTE;
    }

    public void setAncienRevenuPrisCompte(String ancienRevenuPrisCompte) {
        this.ancienRevenuPrisCompte = ancienRevenuPrisCompte;
    }

    public void setAncienSupplAjournementTransfert(String ancienSupplAjournementTransfert) {
        this.ancienSupplAjournementTransfert = ancienSupplAjournementTransfert;
    }

    public void setAncienSupplementAjournement(String ancienSupplementAjournement) {
        this.ancienSupplementAjournement = ancienSupplementAjournement;
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

    public void setMontantAncRenteRemplacee(String montantAncRenteRemplacee) {
        this.montantAncRenteRemplacee = montantAncRenteRemplacee;
    }

    public void setObservationCentrale(String observationCentrale) {
        this.observationCentrale = observationCentrale;
    }

}
