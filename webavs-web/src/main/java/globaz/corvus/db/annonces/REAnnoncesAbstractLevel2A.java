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
public class REAnnoncesAbstractLevel2A extends REAnnoncesAbstractLevel1A {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_AGE_DEBUT_INVALIDITE = "YYNAGE";

    public static final String FIELDNAME_ANNEE_COT_CLASSE_AGE = "YYDACC";
    public static final String FIELDNAME_ANNEE_NIVEAU = "YYDANI";
    public static final String FIELDNAME_CAS_SPECIAL_1 = "YYLCS1";
    public static final String FIELDNAME_CAS_SPECIAL_2 = "YYLCS2";
    public static final String FIELDNAME_CAS_SPECIAL_3 = "YYLCS3";
    public static final String FIELDNAME_CAS_SPECIAL_4 = "YYLCS4";
    public static final String FIELDNAME_CAS_SPECIAL_5 = "YYLCS5";
    public static final String FIELDNAME_CODE_INFIRMITE = "YYNCOI";
    public static final String FIELDNAME_DATE_REVOCATION_AJOURNEMENT = "YYDREV";
    public static final String FIELDNAME_DEGRE_INVALIDITE = "YYNDIN";
    public static final String FIELDNAME_DUREE_AJOURNEMENT = "YYNDUR";
    public static final String FIELDNAME_DUREE_COT_ECH_RENTE_AV_73 = "YYDCEC";
    public static final String FIELDNAME_DUREE_COT_ECH_RENTE_DES_73 = "YYDECH";
    public static final String FIELDNAME_DUREE_COT_MANQUANTE_48_72 = "YYDCM1";
    public static final String FIELDNAME_DUREE_COT_MANQUANTE_73_78 = "YYDCM2";
    public static final String FIELDNAME_DUREE_COT_POUR_DET_RAM = "YYNDCO";
    public static final String FIELDNAME_ECHELLE_RENTE = "YYLECR";
    public static final String FIELDNAME_GENRE_DROIT_API = "YYTGEN";
    public static final String FIELDNAME_ID_ANNONCE_ABS_LEV_2A = "YYIANN";
    public static final String FIELDNAME_NOMBRE_ANNEE_BTE = "YYNANN";
    public static final String FIELDNAME_OFFICE_AI_COMPETENT = "YYLOAI";
    public static final String FIELDNAME_RAM_DETERMINANT = "YYMRAM";
    public static final String FIELDNAME_REDUCTION = "YYLRED";
    public static final String FIELDNAME_SUPPLEMENT_AJOURNEMENT = "YYNSUP";
    public static final String FIELDNAME_SURVENANCE_EVT_ASSURE = "YYNSUR";
    public static final String TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_2A = "REAAL2A";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String ageDebutInvalidite = "";
    private String anneeCotClasseAge = "";
    private String anneeNiveau = "";
    private String casSpecial1 = "";
    private String casSpecial2 = "";
    private String casSpecial3 = "";
    private String casSpecial4 = "";
    private String casSpecial5 = "";
    private String codeInfirmite = "";
    private String dateRevocationAjournement = "";
    private String degreInvalidite = "";
    private String dureeAjournement = "";
    private String dureeCoEchelleRenteAv73 = "";
    private String dureeCoEchelleRenteDes73 = "";
    private String dureeCotManquante48_72 = "";
    private String dureeCotManquante73_78 = "";
    private String dureeCotPourDetRAM = "";
    private String echelleRente = "";
    private String genreDroitAPI = "";
    private String nombreAnneeBTE = "";
    private String officeAICompetent = "";
    private String ramDeterminant = "";
    private String reduction = "";
    private String supplementAjournement = "";
    private String survenanceEvenAssure = "";

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
        getFrom += TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_2A;
        getFrom += " ON ";
        getFrom += REAnnonceHeader.FIELDNAME_ID_ANNONCE;
        getFrom += "=";
        getFrom += FIELDNAME_ID_ANNONCE_ABS_LEV_2A;

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
        return TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_2A;
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

        anneeNiveau = statement.dbReadString(FIELDNAME_ANNEE_NIVEAU);
        echelleRente = statement.dbReadString(FIELDNAME_ECHELLE_RENTE);
        dureeCotManquante48_72 = statement.dbReadString(FIELDNAME_DUREE_COT_MANQUANTE_48_72);
        anneeCotClasseAge = statement.dbReadString(FIELDNAME_ANNEE_COT_CLASSE_AGE);
        officeAICompetent = statement.dbReadString(FIELDNAME_OFFICE_AI_COMPETENT);
        degreInvalidite = statement.dbReadString(FIELDNAME_DEGRE_INVALIDITE);
        codeInfirmite = statement.dbReadString(FIELDNAME_CODE_INFIRMITE);
        survenanceEvenAssure = statement.dbReadString(FIELDNAME_SURVENANCE_EVT_ASSURE);
        ageDebutInvalidite = statement.dbReadString(FIELDNAME_AGE_DEBUT_INVALIDITE);
        genreDroitAPI = statement.dbReadString(FIELDNAME_GENRE_DROIT_API);
        reduction = statement.dbReadString(FIELDNAME_REDUCTION);
        casSpecial1 = statement.dbReadString(FIELDNAME_CAS_SPECIAL_1);
        casSpecial2 = statement.dbReadString(FIELDNAME_CAS_SPECIAL_2);
        casSpecial3 = statement.dbReadString(FIELDNAME_CAS_SPECIAL_3);
        casSpecial4 = statement.dbReadString(FIELDNAME_CAS_SPECIAL_4);
        casSpecial5 = statement.dbReadString(FIELDNAME_CAS_SPECIAL_5);
        dureeCotManquante73_78 = statement.dbReadString(FIELDNAME_DUREE_COT_MANQUANTE_73_78);
        dureeCoEchelleRenteAv73 = statement.dbReadString(FIELDNAME_DUREE_COT_ECH_RENTE_AV_73);
        dureeCoEchelleRenteDes73 = statement.dbReadString(FIELDNAME_DUREE_COT_ECH_RENTE_DES_73);
        nombreAnneeBTE = statement.dbReadString(FIELDNAME_NOMBRE_ANNEE_BTE);
        ramDeterminant = statement.dbReadString(FIELDNAME_RAM_DETERMINANT);
        dureeCotPourDetRAM = statement.dbReadString(FIELDNAME_DUREE_COT_POUR_DET_RAM);
        dureeAjournement = statement.dbReadString(FIELDNAME_DUREE_AJOURNEMENT);
        supplementAjournement = statement.dbReadString(FIELDNAME_SUPPLEMENT_AJOURNEMENT);
        dateRevocationAjournement = statement.dbReadString(FIELDNAME_DATE_REVOCATION_AJOURNEMENT);
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
        statement.writeKey(FIELDNAME_ID_ANNONCE_ABS_LEV_2A,
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
            statement.writeField(FIELDNAME_ID_ANNONCE_ABS_LEV_2A,
                    _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        }

        statement.writeField(FIELDNAME_ANNEE_NIVEAU,
                _dbWriteString(statement.getTransaction(), anneeNiveau, "anneeNiveau"));
        statement.writeField(FIELDNAME_ECHELLE_RENTE,
                _dbWriteString(statement.getTransaction(), echelleRente, "echelleRente"));
        statement.writeField(FIELDNAME_DUREE_COT_MANQUANTE_48_72,
                _dbWriteString(statement.getTransaction(), dureeCotManquante48_72, "dureeCotManquante48_72"));
        statement.writeField(FIELDNAME_ANNEE_COT_CLASSE_AGE,
                _dbWriteString(statement.getTransaction(), anneeCotClasseAge, "anneeCotClasseAge"));
        statement.writeField(FIELDNAME_OFFICE_AI_COMPETENT,
                _dbWriteString(statement.getTransaction(), officeAICompetent, "officeAICompetent"));
        statement.writeField(FIELDNAME_DEGRE_INVALIDITE,
                _dbWriteString(statement.getTransaction(), degreInvalidite, "degreInvalidite"));
        statement.writeField(FIELDNAME_CODE_INFIRMITE,
                _dbWriteString(statement.getTransaction(), codeInfirmite, "codeInfirmite"));
        statement.writeField(FIELDNAME_SURVENANCE_EVT_ASSURE,
                _dbWriteString(statement.getTransaction(), survenanceEvenAssure, "survenanceEvenAssure"));
        statement.writeField(FIELDNAME_AGE_DEBUT_INVALIDITE,
                _dbWriteString(statement.getTransaction(), ageDebutInvalidite, "ageDebutInvalidite"));
        statement.writeField(FIELDNAME_GENRE_DROIT_API,
                _dbWriteString(statement.getTransaction(), genreDroitAPI, "genreDroitAPI"));
        statement.writeField(FIELDNAME_REDUCTION, _dbWriteString(statement.getTransaction(), reduction, "reduction"));
        statement.writeField(FIELDNAME_CAS_SPECIAL_1,
                _dbWriteString(statement.getTransaction(), casSpecial1, "casSpecial1"));
        statement.writeField(FIELDNAME_CAS_SPECIAL_2,
                _dbWriteString(statement.getTransaction(), casSpecial2, "casSpecial2"));
        statement.writeField(FIELDNAME_CAS_SPECIAL_3,
                _dbWriteString(statement.getTransaction(), casSpecial3, "casSpecial3"));
        statement.writeField(FIELDNAME_CAS_SPECIAL_4,
                _dbWriteString(statement.getTransaction(), casSpecial4, "casSpecial4"));
        statement.writeField(FIELDNAME_CAS_SPECIAL_5,
                _dbWriteString(statement.getTransaction(), casSpecial5, "casSpecial5"));
        statement.writeField(FIELDNAME_DUREE_COT_MANQUANTE_73_78,
                _dbWriteString(statement.getTransaction(), dureeCotManquante73_78, "dureeCotManquante73_78"));
        statement.writeField(FIELDNAME_DUREE_COT_ECH_RENTE_AV_73,
                _dbWriteString(statement.getTransaction(), dureeCoEchelleRenteAv73, "dureeCoEchelleRenteAv73"));
        statement.writeField(FIELDNAME_DUREE_COT_ECH_RENTE_DES_73,
                _dbWriteString(statement.getTransaction(), dureeCoEchelleRenteDes73, "dureeCoEchelleRenteDes73"));
        statement.writeField(FIELDNAME_NOMBRE_ANNEE_BTE,
                _dbWriteString(statement.getTransaction(), nombreAnneeBTE, "nombreAnneeBTE"));
        statement.writeField(FIELDNAME_RAM_DETERMINANT,
                _dbWriteString(statement.getTransaction(), ramDeterminant, "ramDeterminant"));
        statement.writeField(FIELDNAME_DUREE_COT_POUR_DET_RAM,
                _dbWriteString(statement.getTransaction(), dureeCotPourDetRAM, "dureeCotPourDetRAM"));
        statement.writeField(FIELDNAME_DUREE_AJOURNEMENT,
                _dbWriteString(statement.getTransaction(), dureeAjournement, "dureeAjournement"));
        statement.writeField(FIELDNAME_SUPPLEMENT_AJOURNEMENT,
                _dbWriteString(statement.getTransaction(), supplementAjournement, "supplementAjournement"));
        statement.writeField(FIELDNAME_DATE_REVOCATION_AJOURNEMENT,
                _dbWriteString(statement.getTransaction(), dateRevocationAjournement, "dateRevocationAjournement"));
    }

    /**
     * @return
     */
    public String getAgeDebutInvalidite() {
        return ageDebutInvalidite;
    }

    /**
     * @return
     */
    public String getAnneeCotClasseAge() {
        return anneeCotClasseAge;
    }

    /**
     * @return
     */
    public String getAnneeNiveau() {
        return anneeNiveau;
    }

    /**
     * @return
     */
    public String getCasSpecial1() {
        return casSpecial1;
    }

    /**
     * @return
     */
    public String getCasSpecial2() {
        return casSpecial2;
    }

    /**
     * @return
     */
    public String getCasSpecial3() {
        return casSpecial3;
    }

    /**
     * @return
     */
    public String getCasSpecial4() {
        return casSpecial4;
    }

    /**
     * @return
     */
    public String getCasSpecial5() {
        return casSpecial5;
    }

    /**
     * @return
     */
    public String getCodeInfirmite() {
        return codeInfirmite;
    }

    /**
     * @return
     */
    public String getDateRevocationAjournement() {
        return dateRevocationAjournement;
    }

    /**
     * @return
     */
    public String getDegreInvalidite() {
        return degreInvalidite;
    }

    /**
     * @return
     */
    public String getDureeAjournement() {
        return dureeAjournement;
    }

    /**
     * @return
     */
    public String getDureeCoEchelleRenteAv73() {
        return dureeCoEchelleRenteAv73;
    }

    /**
     * @return
     */
    public String getDureeCoEchelleRenteDes73() {
        return dureeCoEchelleRenteDes73;
    }

    /**
     * @return
     */
    public String getDureeCotManquante48_72() {
        return dureeCotManquante48_72;
    }

    /**
     * @return
     */
    public String getDureeCotManquante73_78() {
        return dureeCotManquante73_78;
    }

    /**
     * @return
     */
    public String getDureeCotPourDetRAM() {
        return dureeCotPourDetRAM;
    }

    /**
     * @return
     */
    public String getEchelleRente() {
        return echelleRente;
    }

    /**
     * @return
     */
    public String getGenreDroitAPI() {
        return genreDroitAPI;
    }

    /**
     * @return
     */
    public String getNombreAnneeBTE() {
        return nombreAnneeBTE;
    }

    /**
     * @return
     */
    public String getOfficeAICompetent() {
        return officeAICompetent;
    }

    /**
     * @return
     */
    public String getRamDeterminant() {
        return ramDeterminant;
    }

    /**
     * @return
     */
    public String getReduction() {
        return reduction;
    }

    /**
     * @return
     */
    public String getSupplementAjournement() {
        return supplementAjournement;
    }

    /**
     * @return
     */
    public String getSurvenanceEvenAssure() {
        return survenanceEvenAssure;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return false;
    }

    /**
     * @param string
     */
    public void setAgeDebutInvalidite(String string) {
        ageDebutInvalidite = string;
    }

    /**
     * @param string
     */
    public void setAnneeCotClasseAge(String string) {
        anneeCotClasseAge = string;
    }

    /**
     * @param string
     */
    public void setAnneeNiveau(String string) {
        anneeNiveau = string;
    }

    /**
     * @param string
     */
    public void setCasSpecial1(String string) {
        casSpecial1 = string;
    }

    /**
     * @param string
     */
    public void setCasSpecial2(String string) {
        casSpecial2 = string;
    }

    /**
     * @param string
     */
    public void setCasSpecial3(String string) {
        casSpecial3 = string;
    }

    /**
     * @param string
     */
    public void setCasSpecial4(String string) {
        casSpecial4 = string;
    }

    /**
     * @param string
     */
    public void setCasSpecial5(String string) {
        casSpecial5 = string;
    }

    /**
     * @param string
     */
    public void setCodeInfirmite(String string) {
        codeInfirmite = string;
    }

    /**
     * @param string
     */
    public void setDateRevocationAjournement(String string) {
        dateRevocationAjournement = string;
    }

    /**
     * @param string
     */
    public void setDegreInvalidite(String string) {
        degreInvalidite = string;
    }

    /**
     * @param string
     */
    public void setDureeAjournement(String string) {
        dureeAjournement = string;
    }

    /**
     * @param string
     */
    public void setDureeCoEchelleRenteAv73(String string) {
        dureeCoEchelleRenteAv73 = string;
    }

    /**
     * @param string
     */
    public void setDureeCoEchelleRenteDes73(String string) {
        dureeCoEchelleRenteDes73 = string;
    }

    /**
     * @param string
     */
    public void setDureeCotManquante48_72(String string) {
        dureeCotManquante48_72 = string;
    }

    /**
     * @param string
     */
    public void setDureeCotManquante73_78(String string) {
        dureeCotManquante73_78 = string;
    }

    /**
     * @param string
     */
    public void setDureeCotPourDetRAM(String string) {
        dureeCotPourDetRAM = string;
    }

    /**
     * @param string
     */
    public void setEchelleRente(String string) {
        echelleRente = string;
    }

    /**
     * @param string
     */
    public void setGenreDroitAPI(String string) {
        genreDroitAPI = string;
    }

    /**
     * @param string
     */
    public void setNombreAnneeBTE(String string) {
        nombreAnneeBTE = string;
    }

    /**
     * @param string
     */
    public void setOfficeAICompetent(String string) {
        officeAICompetent = string;
    }

    /**
     * @param string
     */
    public void setRamDeterminant(String string) {
        ramDeterminant = string;
    }

    /**
     * @param string
     */
    public void setReduction(String string) {
        reduction = string;
    }

    /**
     * @param string
     */
    public void setSupplementAjournement(String string) {
        supplementAjournement = string;
    }

    /**
     * @param string
     */
    public void setSurvenanceEvenAssure(String string) {
        survenanceEvenAssure = string;
    }

}
