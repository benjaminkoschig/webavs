package globaz.corvus.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class REAnnonce53Adaptation extends BEntity implements IREAnnonceAdaptation {

    private static final long serialVersionUID = 1L;

    private String ancienMontantMensuel = "";
    private String codeCasSpecial1 = "";
    private String codeCasSpecial2 = "";
    private String codeCasSpecial3 = "";
    private String codeCasSpecial4 = "";
    private String codeCasSpecial5 = "";
    private String dateRevocationAjour = "";
    private String dureeAjournement = "";
    private String fractionRente = "";
    private String genrePrestation = "";
    private String idAnnonce01 = "";
    private String idAnnonce02 = "";
    private String idAnnonce03 = "";
    private String mntReducAnticipation = "";
    private String mntSuppAjournement = "";
    private String montantPrestation = "";
    private String nss = "";
    private String observationsCentrale = "";
    private String premierNSSCompl = "";
    private String RAM = "";

    @Override
    protected String _getFields(BStatement statement) {

        String fields = "";

        fields += "L1A.YXMMEN MNTTPREST, L1A.YXNASS NSS, L1A.YXLGEN CODEPREST, L1A.YXNPNA NSSCOMP1, ";
        fields += "L2A2.YYLCS1 CCS1, L2A2.YYLCS2 CCS2, L2A2.YYLCS3 CCS3, L2A2.YYLCS4 CCS4, L2A2.YYLCS5 CCS5, L2A2.YYMRAM, L2A2.YYNSUP, ";
        fields += "L2A2.YYNDUR, L2A2.YYDREV, ";
        fields += "L3A2.YZNRED, ";
        fields += "N533.ZDMAMM ANCMNTPREST, N533.ZDTFRE FRACTION, N533.ZDLOCE OBS, ";
        fields += "HEA1.ZAIANH IDANN01, HEA2.ZAIANH IDANN02, HEA3.ZAIANH IDANN03";

        return fields;
    }

    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        // POUR L'ENREGISTREMENT 01
        getFrom += _getCollection() + REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER + " AS HEA1";

        getFrom += " INNER JOIN ";
        getFrom += _getCollection() + REAnnoncesAbstractLevel1A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_1A + " AS L1A";
        getFrom += " ON HEA1." + REAnnonceHeader.FIELDNAME_ID_ANNONCE + "=L1A."
                + REAnnoncesAbstractLevel1A.FIELDNAME_ID_ANNONCE_ABS_LEV_1A;

        getFrom += " INNER JOIN ";
        getFrom += _getCollection() + REAnnoncesAbstractLevel2A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_2A + " AS L2A";
        getFrom += " ON HEA1." + REAnnonceHeader.FIELDNAME_ID_ANNONCE + "=L2A."
                + REAnnoncesAbstractLevel2A.FIELDNAME_ID_ANNONCE_ABS_LEV_2A;

        getFrom += " INNER JOIN ";
        getFrom += _getCollection() + REAnnoncesAbstractLevel3A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_3A + " AS L3A";
        getFrom += " ON HEA1." + REAnnonceHeader.FIELDNAME_ID_ANNONCE + "=L3A."
                + REAnnoncesAbstractLevel3A.FIELDNAME_ID_ANNONCE_ABS_LEV_3A;

        getFrom += " INNER JOIN ";
        getFrom += _getCollection() + REAnnonce53.TABLE_NAME_ANNONCE_53 + " AS N53";
        getFrom += " ON HEA1." + REAnnonceHeader.FIELDNAME_ID_ANNONCE + "=N53." + REAnnonce53.FIELDNAME_ID_ANNONCE_53;

        getFrom += " INNER JOIN ";
        getFrom += _getCollection() + REFicheAugmentation.TABLE_NAME_FICHE_AUGMENTATION + " AS CHA";
        getFrom += " ON CHA." + REFicheAugmentation.FIELDNAME_ID_ANNONCE_HEADER + "=HEA1."
                + REAnnonceHeader.FIELDNAME_ID_ANNONCE;

        // POUR L'ENREGISTREMENT 02
        getFrom += " INNER JOIN ";
        getFrom += _getCollection() + REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER + " AS HEA2";
        getFrom += " ON HEA1.ZAILIE=HEA2.ZAIANH ";

        getFrom += " INNER JOIN ";
        getFrom += _getCollection() + REAnnoncesAbstractLevel1A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_1A + " AS L1A2";
        getFrom += " ON HEA2." + REAnnonceHeader.FIELDNAME_ID_ANNONCE + "=L1A2."
                + REAnnoncesAbstractLevel1A.FIELDNAME_ID_ANNONCE_ABS_LEV_1A;

        getFrom += " INNER JOIN ";
        getFrom += _getCollection() + REAnnoncesAbstractLevel2A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_2A + " AS L2A2";
        getFrom += " ON HEA2." + REAnnonceHeader.FIELDNAME_ID_ANNONCE + "=L2A2."
                + REAnnoncesAbstractLevel2A.FIELDNAME_ID_ANNONCE_ABS_LEV_2A;

        getFrom += " INNER JOIN ";
        getFrom += _getCollection() + REAnnoncesAbstractLevel3A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_3A + " AS L3A2";
        getFrom += " ON HEA2." + REAnnonceHeader.FIELDNAME_ID_ANNONCE + "=L3A2."
                + REAnnoncesAbstractLevel3A.FIELDNAME_ID_ANNONCE_ABS_LEV_3A;

        getFrom += " INNER JOIN ";
        getFrom += _getCollection() + REAnnonce53.TABLE_NAME_ANNONCE_53 + " AS N532";
        getFrom += " ON HEA2." + REAnnonceHeader.FIELDNAME_ID_ANNONCE + "=N532." + REAnnonce53.FIELDNAME_ID_ANNONCE_53;

        // POUR L'ENREGISTREMENT 03
        getFrom += " INNER JOIN ";
        getFrom += _getCollection() + REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER + " AS HEA3";
        getFrom += " ON HEA2.ZAILIE=HEA3.ZAIANH ";

        getFrom += " INNER JOIN ";
        getFrom += _getCollection() + REAnnoncesAbstractLevel1A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_1A + " AS L1A3";
        getFrom += " ON HEA3." + REAnnonceHeader.FIELDNAME_ID_ANNONCE + "=L1A3."
                + REAnnoncesAbstractLevel1A.FIELDNAME_ID_ANNONCE_ABS_LEV_1A;

        getFrom += " INNER JOIN ";
        getFrom += _getCollection() + REAnnoncesAbstractLevel2A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_2A + " AS L2A3";
        getFrom += " ON HEA3." + REAnnonceHeader.FIELDNAME_ID_ANNONCE + "=L2A3."
                + REAnnoncesAbstractLevel2A.FIELDNAME_ID_ANNONCE_ABS_LEV_2A;

        getFrom += " INNER JOIN ";
        getFrom += _getCollection() + REAnnoncesAbstractLevel3A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_3A + " AS L3A3";
        getFrom += " ON HEA3." + REAnnonceHeader.FIELDNAME_ID_ANNONCE + "=L3A3."
                + REAnnoncesAbstractLevel3A.FIELDNAME_ID_ANNONCE_ABS_LEV_3A;

        getFrom += " INNER JOIN ";
        getFrom += _getCollection() + REAnnonce53.TABLE_NAME_ANNONCE_53 + " AS N533";
        getFrom += " ON HEA3." + REAnnonceHeader.FIELDNAME_ID_ANNONCE + "=N533." + REAnnonce53.FIELDNAME_ID_ANNONCE_53;

        return getFrom;
    }

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        // REANHEA
        idAnnonce01 = statement.dbReadNumeric("IDANN01");
        idAnnonce02 = statement.dbReadNumeric("IDANN02");
        idAnnonce03 = statement.dbReadNumeric("IDANN03");

        // REAAL1A
        montantPrestation = statement.dbReadString("MNTTPREST");
        nss = statement.dbReadString("NSS");
        genrePrestation = statement.dbReadString("CODEPREST");
        premierNSSCompl = statement.dbReadString("NSSCOMP1");

        // REAAL2A
        codeCasSpecial1 = statement.dbReadString("CCS1");
        codeCasSpecial2 = statement.dbReadString("CCS2");
        codeCasSpecial3 = statement.dbReadString("CCS3");
        codeCasSpecial4 = statement.dbReadString("CCS4");
        codeCasSpecial5 = statement.dbReadString("CCS5");
        RAM = statement.dbReadString("YYMRAM");
        mntSuppAjournement = statement.dbReadString("YYNSUP");
        dureeAjournement = statement.dbReadString("YYNDUR");
        dateRevocationAjour = statement.dbReadString("YYDREV");

        // REAAL3A
        mntReducAnticipation = statement.dbReadString("YZNRED");

        // REANN53
        ancienMontantMensuel = statement.dbReadString("ANCMNTPREST");
        fractionRente = statement.dbReadString("FRACTION");
        observationsCentrale = statement.dbReadString("OBS");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    @Override
    public String getAncienMontantMensuel() {
        return ancienMontantMensuel;
    }

    @Override
    public String getCodeCasSpecial1() {
        return codeCasSpecial1;
    }

    @Override
    public String getCodeCasSpecial2() {
        return codeCasSpecial2;
    }

    @Override
    public String getCodeCasSpecial3() {
        return codeCasSpecial3;
    }

    @Override
    public String getCodeCasSpecial4() {
        return codeCasSpecial4;
    }

    @Override
    public String getCodeCasSpecial5() {
        return codeCasSpecial5;
    }

    public String getDateRevocationAjour() {
        return dateRevocationAjour;
    }

    public String getDureeAjournement() {
        return dureeAjournement;
    }

    public String getFractionRente() {
        return fractionRente;
    }

    @Override
    public String getGenrePrestation() {
        return genrePrestation;
    }

    @Override
    public String getIdAnnonce01() {
        return idAnnonce01;
    }

    public String getIdAnnonce02() {
        return idAnnonce02;
    }

    public String getIdAnnonce03() {
        return idAnnonce03;
    }

    public String getMntReducAnticipation() {
        return mntReducAnticipation;
    }

    public String getMntSuppAjournement() {
        return mntSuppAjournement;
    }

    @Override
    public String getMontantPrestation() {
        return montantPrestation;
    }

    @Override
    public String getNss() {
        return nss;
    }

    public String getObservationsCentrale() {
        return observationsCentrale;
    }

    public String getPremierNSSCompl() {
        return premierNSSCompl;
    }

    public String getRAM() {
        return RAM;
    }

    public void setAncienMontantMensuel(String ancienMontantMensuel) {
        this.ancienMontantMensuel = ancienMontantMensuel;
    }

    public void setCodeCasSpecial1(String codeCasSpecial1) {
        this.codeCasSpecial1 = codeCasSpecial1;
    }

    public void setCodeCasSpecial2(String codeCasSpecial2) {
        this.codeCasSpecial2 = codeCasSpecial2;
    }

    public void setCodeCasSpecial3(String codeCasSpecial3) {
        this.codeCasSpecial3 = codeCasSpecial3;
    }

    public void setCodeCasSpecial4(String codeCasSpecial4) {
        this.codeCasSpecial4 = codeCasSpecial4;
    }

    public void setCodeCasSpecial5(String codeCasSpecial5) {
        this.codeCasSpecial5 = codeCasSpecial5;
    }

    public void setDateRevocationAjour(String dateRevocationAjour) {
        this.dateRevocationAjour = dateRevocationAjour;
    }

    public void setDureeAjournement(String dureeAjournement) {
        this.dureeAjournement = dureeAjournement;
    }

    public void setFractionRente(String fractionRente) {
        this.fractionRente = fractionRente;
    }

    public void setGenrePrestation(String genrePrestation) {
        this.genrePrestation = genrePrestation;
    }

    public void setIdAnnonce01(String idAnnonce01) {
        this.idAnnonce01 = idAnnonce01;
    }

    public void setIdAnnonce02(String idAnnonce02) {
        this.idAnnonce02 = idAnnonce02;
    }

    public void setIdAnnonce03(String idAnnonce03) {
        this.idAnnonce03 = idAnnonce03;
    }

    public void setMntReducAnticipation(String mntReducAnticipation) {
        this.mntReducAnticipation = mntReducAnticipation;
    }

    public void setMntSuppAjournement(String mntSuppAjournement) {
        this.mntSuppAjournement = mntSuppAjournement;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setObservationsCentrale(String observationsCentrale) {
        this.observationsCentrale = observationsCentrale;
    }

    public void setPremierNSSCompl(String premierNSSCompl) {
        this.premierNSSCompl = premierNSSCompl;
    }

    public void setRAM(String rAM) {
        RAM = rAM;
    }

    @Override
    public String getCodeApplicationAnnonce() {
        return CODE_APPLICATION_ANNONCE_53;
    }

}
