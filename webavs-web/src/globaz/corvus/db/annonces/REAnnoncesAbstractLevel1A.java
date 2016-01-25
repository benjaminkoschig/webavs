package globaz.corvus.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;

public class REAnnoncesAbstractLevel1A extends REAnnonceHeader {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CANTON_ETAT_DOMICILE = "YXLCAN";
    public static final String FIELDNAME_CODE_MUTATION = "YXLCOM";
    public static final String FIELDNAME_DEBUT_DROIT = "YXDDEB";
    public static final String FIELDNAME_ETAT_CIVIL = "YXLETC";
    public static final String FIELDNAME_FIN_DROIT = "YXDFIN";
    public static final String FIELDNAME_GENRE_PRESTATION = "YXLGEN";
    public static final String FIELDNAME_ID_ANNONCE_ABS_LEV_1A = "YXIDAN";
    public static final String FIELDNAME_ID_TIERS = "YXITIE";
    public static final String FIELDNAME_IS_REFUGIE = "YXBREF";
    public static final String FIELDNAME_MENSUALITE_PRESTATIONS_FR = "YXMMEN";
    public static final String FIELDNAME_MOIS_RAPPORT = "YXDMRA";
    public static final String FIELDNAME_NO_ASS_AYANT_DROIT = "YXNASS";
    public static final String FIELDNAME_NUMERO_ANNONCE = "YXNNOA";
    public static final String FIELDNAME_PREMIER_NO_ASS_COMPLEMENTAIRE = "YXNPNA";
    public static final String FIELDNAME_REFERENCE_CAISSE_INTERNE = "YXLREI";
    public static final String FIELDNAME_SECOND_NO_ASS_COMPLEMENTAIRE = "YXNDNA";

    public static final String TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_1A = "REAAL1A";

    private String cantonEtatDomicile;
    private String codeMutation;
    private String debutDroit;
    private String etatCivil;
    private String finDroit;
    private String genrePrestation;
    private String idRenteAccordee;
    private String idTiers;
    private String isRefugie;
    private String mensualitePrestationsFrancs;
    private String moisRapport;
    private String noAssAyantDroit;
    private String numeroAnnonce;
    private String premierNoAssComplementaire;
    private String referenceCaisseInterne;
    private String secondNoAssComplementaire;

    public REAnnoncesAbstractLevel1A() {
        super();

        cantonEtatDomicile = "";
        codeMutation = "";
        debutDroit = "";
        etatCivil = "";
        finDroit = "";
        genrePrestation = "";
        idRenteAccordee = "";
        idTiers = "";
        isRefugie = "";
        mensualitePrestationsFrancs = "";
        moisRapport = "";
        noAssAyantDroit = "";
        numeroAnnonce = "";
        premierNoAssComplementaire = "";
        referenceCaisseInterne = "";
        secondNoAssComplementaire = "";
    }

    /**
     * initialise la valeur de Id annonce header
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        sql.append(super._getFrom(statement));

        sql.append(" INNER JOIN ").append(_getCollection())
                .append(REAnnoncesAbstractLevel1A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_1A);
        sql.append(" ON ").append(REAnnonceHeader.FIELDNAME_ID_ANNONCE).append("=")
                .append(REAnnoncesAbstractLevel1A.FIELDNAME_ID_ANNONCE_ABS_LEV_1A);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return REAnnoncesAbstractLevel1A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_1A;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        super._readProperties(statement);

        cantonEtatDomicile = statement.dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_CANTON_ETAT_DOMICILE);
        codeMutation = statement.dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_CODE_MUTATION);
        debutDroit = statement.dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_DEBUT_DROIT);
        etatCivil = statement.dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_ETAT_CIVIL);
        finDroit = statement.dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_FIN_DROIT);
        genrePrestation = statement.dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_GENRE_PRESTATION);
        idRenteAccordee = statement.dbReadNumeric(REAnnonceRente.FIELDNAME_ID_RENTE_ACCORDEE);
        idTiers = statement.dbReadNumeric(REAnnoncesAbstractLevel1A.FIELDNAME_ID_TIERS);
        isRefugie = statement.dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_IS_REFUGIE);
        mensualitePrestationsFrancs = statement
                .dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_MENSUALITE_PRESTATIONS_FR);
        moisRapport = PRDateFormater.convertDate_AAAAMM_to_MMAA(statement
                .dbReadNumeric(REAnnoncesAbstractLevel1A.FIELDNAME_MOIS_RAPPORT));
        noAssAyantDroit = statement.dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_NO_ASS_AYANT_DROIT);
        numeroAnnonce = statement.dbReadNumeric(REAnnoncesAbstractLevel1A.FIELDNAME_NUMERO_ANNONCE);
        premierNoAssComplementaire = statement
                .dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_PREMIER_NO_ASS_COMPLEMENTAIRE);
        referenceCaisseInterne = statement.dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_REFERENCE_CAISSE_INTERNE);
        secondNoAssComplementaire = statement
                .dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_SECOND_NO_ASS_COMPLEMENTAIRE);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(REAnnoncesAbstractLevel1A.FIELDNAME_ID_ANNONCE_ABS_LEV_1A,
                this._dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        if (_getAction() == BEntity.ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(REAnnoncesAbstractLevel1A.FIELDNAME_ID_ANNONCE_ABS_LEV_1A,
                    this._dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        }

        statement.writeField(REAnnoncesAbstractLevel1A.FIELDNAME_ID_TIERS,
                this._dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(REAnnoncesAbstractLevel1A.FIELDNAME_NO_ASS_AYANT_DROIT,
                this._dbWriteString(statement.getTransaction(), noAssAyantDroit, "noAssAyantDroit"));

        // Dans certains cas, il semblerait que le mois de rapport arrive dans des format différents.
        // On test donc les deux cas à ce niveau. Pas très beau mais fallait faire juste au départ.
        String mr = PRDateFormater.convertDate_MMAA_to_AAAAMM(moisRapport);
        if (JadeStringUtil.isBlankOrZero(mr)) {
            mr = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(moisRapport);
        }
        statement.writeField(REAnnoncesAbstractLevel1A.FIELDNAME_MOIS_RAPPORT,
                this._dbWriteNumeric(statement.getTransaction(), mr, "moisRapport"));
        statement.writeField(REAnnoncesAbstractLevel1A.FIELDNAME_FIN_DROIT,
                this._dbWriteString(statement.getTransaction(), finDroit, "finDroit"));
        statement.writeField(REAnnoncesAbstractLevel1A.FIELDNAME_PREMIER_NO_ASS_COMPLEMENTAIRE, this._dbWriteString(
                statement.getTransaction(), premierNoAssComplementaire, "premierNoAssComplementaire"));
        statement
                .writeField(REAnnoncesAbstractLevel1A.FIELDNAME_SECOND_NO_ASS_COMPLEMENTAIRE, this._dbWriteString(
                        statement.getTransaction(), secondNoAssComplementaire, "secondNoAssComplementaire"));
        statement.writeField(REAnnoncesAbstractLevel1A.FIELDNAME_ETAT_CIVIL,
                this._dbWriteString(statement.getTransaction(), etatCivil, "etatCivil"));
        statement.writeField(REAnnoncesAbstractLevel1A.FIELDNAME_REFERENCE_CAISSE_INTERNE,
                this._dbWriteString(statement.getTransaction(), referenceCaisseInterne, "referenceCaisseInterne"));
        statement.writeField(REAnnoncesAbstractLevel1A.FIELDNAME_IS_REFUGIE,
                this._dbWriteString(statement.getTransaction(), isRefugie, "isRefugie"));
        statement.writeField(REAnnoncesAbstractLevel1A.FIELDNAME_CANTON_ETAT_DOMICILE,
                this._dbWriteString(statement.getTransaction(), cantonEtatDomicile, "cantonEtatDomicile"));
        statement.writeField(REAnnoncesAbstractLevel1A.FIELDNAME_GENRE_PRESTATION,
                this._dbWriteString(statement.getTransaction(), genrePrestation, "genrePrestation"));
        statement.writeField(REAnnoncesAbstractLevel1A.FIELDNAME_DEBUT_DROIT,
                this._dbWriteString(statement.getTransaction(), debutDroit, "debutDroit"));
        statement.writeField(REAnnoncesAbstractLevel1A.FIELDNAME_MENSUALITE_PRESTATIONS_FR, this._dbWriteString(
                statement.getTransaction(), mensualitePrestationsFrancs, "mensualitePrestationsFrancs"));
        statement.writeField(REAnnoncesAbstractLevel1A.FIELDNAME_CODE_MUTATION,
                this._dbWriteString(statement.getTransaction(), codeMutation, "codeMutation"));
    }

    public String getCantonEtatDomicile() {
        return cantonEtatDomicile;
    }

    public String getCodeMutation() {
        return codeMutation;
    }

    public String getDebutDroit() {
        return debutDroit;
    }

    public String getEtatCivil() {
        return etatCivil;
    }

    public String getFinDroit() {
        return finDroit;
    }

    public String getGenrePrestation() {
        return genrePrestation;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIsRefugie() {
        return isRefugie;
    }

    public String getMensualitePrestationsFrancs() {
        return mensualitePrestationsFrancs;
    }

    public String getMoisRapport() {
        return moisRapport;
    }

    public String getNoAssAyantDroit() {
        return noAssAyantDroit;
    }

    public String getNumeroAnnonce() {
        return numeroAnnonce;
    }

    public String getPremierNoAssComplementaire() {
        return premierNoAssComplementaire;
    }

    public String getReferenceCaisseInterne() {
        return referenceCaisseInterne;
    }

    public String getSecondNoAssComplementaire() {
        return secondNoAssComplementaire;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setCantonEtatDomicile(String cantonEtatDomicile) {
        this.cantonEtatDomicile = cantonEtatDomicile;
    }

    public void setCodeMutation(String codeMutation) {
        this.codeMutation = codeMutation;
    }

    public void setDebutDroit(String debutDroit) {
        this.debutDroit = debutDroit;
    }

    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    public void setFinDroit(String finDroit) {
        this.finDroit = finDroit;
    }

    public void setGenrePrestation(String genrePrestation) {
        this.genrePrestation = genrePrestation;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsRefugie(String isRefugie) {
        this.isRefugie = isRefugie;
    }

    public void setMensualitePrestationsFrancs(String mensualitePrestationsFrancs) {
        this.mensualitePrestationsFrancs = mensualitePrestationsFrancs;
    }

    public void setMoisRapport(String moisRapport) {
        this.moisRapport = moisRapport;
    }

    public void setNoAssAyantDroit(String noAssAyantDroit) {
        this.noAssAyantDroit = noAssAyantDroit;
    }

    public void setNumeroAnnonce(String numeroAnnonce) {
        this.numeroAnnonce = numeroAnnonce;
    }

    public void setPremierNoAssComplementaire(String premierNoAssComplementaire) {
        this.premierNoAssComplementaire = premierNoAssComplementaire;
    }

    public void setReferenceCaisseInterne(String referenceCaisseInterne) {
        this.referenceCaisseInterne = referenceCaisseInterne;
    }

    public void setSecondNoAssComplementaire(String secondNoAssComplementaire) {
        this.secondNoAssComplementaire = secondNoAssComplementaire;
    }
}
