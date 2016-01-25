package globaz.corvus.db.annonces;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.tools.PRDateFormater;

/**
 * Annonce inscriptions de CI (code application : 38)
 */
public class REAnnonceInscriptionCI extends REAnnonceHeader {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * L'inscription CI est en attente d'un CI additionnel
     */
    public static final String CS_ATTENTE_CI_ADDITIONNEL_PROVISOIRE = "52860001";

    /**
     * L'inscription CI à reçu le CI additionnel et est traité
     */
    public static final String CS_ATTENTE_CI_ADDITIONNEL_TRAITE = "52860002";

    public static final String FIELDNAME_ANNEE_COTISATION = "ZBLACO";
    public static final String FIELDNAME_ATTENTE_CI_ADDITIONNEL = "ZBTCIA";
    public static final String FIELDNAME_BRANCHE_ECONOMIQUE = "ZBLBRE";
    public static final String FIELDNAME_CI_ADDITIONNEL = "ZBBCIA";
    public static final String FIELDNAME_CODE_ADS = "ZBACAM";
    public static final String FIELDNAME_CODE_EXTOURNE = "ZBACEX";
    public static final String FIELDNAME_CODE_PARTICULIER = "ZBACPA";
    public static final String FIELDNAME_CODE_SPECIALE = "ZBACSP";
    public static final String FIELDNAME_DATE_CLOTURE = "ZBDCLO";
    public static final String FIELDNAME_DATE_ORDRE = "ZBDORD";
    public static final String FIELDNAME_GENRE_COTISATION = "ZBAGCO";
    public static final String FIELDNAME_ID_ANNONCE_INSCRIPTION_CI = "ZBIAIN";
    public static final String FIELDNAME_ID_TIERS = "ZBITIE";
    public static final String FIELDNAME_ID_TIERS_AYANT_DROIT = "ZBITAD";
    public static final String FIELDNAME_MOIS_DEBUT_COTISATION = "ZBLDCO";
    public static final String FIELDNAME_MOIS_FIN_COTISATION = "ZBLFCO";
    public static final String FIELDNAME_MOTIF = "ZBAMOT";
    public static final String FIELDNAME_NO_AFFILIE = "ZBANAF";
    public static final String FIELDNAME_NO_AGENCE_TENANT_CI = "ZBANAT";
    public static final String FIELDNAME_NO_CAISSE_TENANT_CI = "ZBANCT";
    public static final String FIELDNAME_NUMERO_POSTAL_EMPLOYEUR = "ZBLNPA";
    public static final String FIELDNAME_PART_BONIF_ASSIST = "ZBAPBA";
    public static final String FIELDNAME_PARTIE_INFORMATION = "ZBLINF";
    public static final String FIELDNAME_PROVENANCE = "ZBAPRO";
    public static final String FIELDNAME_REF_INT_CAISSE = "ZBLRIC";
    public static final String FIELDNAME_REVENU = "ZBMREV";

    public static final String TABLE_NAME_ANNONCE_INSCRIPTION_CI = "REANICI";

    private String anneeCotisations = "";
    private String attenteCIAdditionnelCS;
    private String brancheEconomique = "";
    private Boolean ciAdditionnel = Boolean.FALSE;
    private String codeADS = "";
    /** Code diminution dans ACOR ??? */
    private String codeExtourne = "";
    private String codeParticulier = "";
    /** Champ spécial dans ACOR */
    private String codeSpecial = "";
    private String dateCloture = "";
    private String dateOrdre = "";
    private String genreCotisation = "";
    /** Id tiers du requérant */
    private String idTiers = "";
    /** id Tiers de l'ayant droit, ou du partenaire de splitting pour le genre de cotisation 8 */
    private String idTiersAyantDroit = "";
    private String moisDebutCotisations = "";
    private String moisFinCotisations = "";
    private String motif = "";
    private String noAffilie = "";
    private String noAgenceTenantCI = "";
    private String noCaisseTenantCI = "";
    private String noPostalEmployeur = "";
    private String partBonifAssist = "";
    private String partieInformation = "";
    private String provenance = "";
    private String refInterneCaisse = "";
    private String revenu = "";

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tableAnnonceInscriptionCI = _getCollection() + REAnnonceInscriptionCI.TABLE_NAME_ANNONCE_INSCRIPTION_CI;
        String tableEnteteAnnonce = _getCollection() + REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER;

        sql.append(tableAnnonceInscriptionCI);

        sql.append(" INNER JOIN ").append(tableEnteteAnnonce);
        sql.append(" ON ").append(tableAnnonceInscriptionCI).append(".")
                .append(REAnnonceInscriptionCI.FIELDNAME_ID_ANNONCE_INSCRIPTION_CI).append("=")
                .append(tableEnteteAnnonce).append(".").append(REAnnonceHeader.FIELDNAME_ID_ANNONCE);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return REAnnonceInscriptionCI.TABLE_NAME_ANNONCE_INSCRIPTION_CI;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        setIdAnnonce(statement.dbReadNumeric(REAnnonceInscriptionCI.FIELDNAME_ID_ANNONCE_INSCRIPTION_CI));

        idTiers = statement.dbReadNumeric(REAnnonceInscriptionCI.FIELDNAME_ID_TIERS);
        idTiersAyantDroit = statement.dbReadNumeric(REAnnonceInscriptionCI.FIELDNAME_ID_TIERS_AYANT_DROIT);
        refInterneCaisse = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_REF_INT_CAISSE);
        motif = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_MOTIF);
        dateCloture = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(REAnnonceInscriptionCI.FIELDNAME_DATE_CLOTURE));
        dateOrdre = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(REAnnonceInscriptionCI.FIELDNAME_DATE_ORDRE));
        noCaisseTenantCI = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_NO_CAISSE_TENANT_CI);
        noAgenceTenantCI = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_NO_AGENCE_TENANT_CI);
        noAffilie = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_NO_AFFILIE);
        codeExtourne = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_CODE_EXTOURNE);
        genreCotisation = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_GENRE_COTISATION);
        codeParticulier = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_CODE_PARTICULIER);
        partBonifAssist = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_PART_BONIF_ASSIST);
        codeSpecial = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_CODE_SPECIALE);
        moisDebutCotisations = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_MOIS_DEBUT_COTISATION);
        moisFinCotisations = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_MOIS_FIN_COTISATION);
        anneeCotisations = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_ANNEE_COTISATION);
        revenu = statement.dbReadNumeric(REAnnonceInscriptionCI.FIELDNAME_REVENU);
        codeADS = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_CODE_ADS);
        attenteCIAdditionnelCS = statement.dbReadNumeric(REAnnonceInscriptionCI.FIELDNAME_ATTENTE_CI_ADDITIONNEL);
        provenance = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_PROVENANCE);
        ciAdditionnel = statement.dbReadBoolean(REAnnonceInscriptionCI.FIELDNAME_CI_ADDITIONNEL);
        brancheEconomique = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_BRANCHE_ECONOMIQUE);
        partieInformation = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_PARTIE_INFORMATION);
        noPostalEmployeur = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_NUMERO_POSTAL_EMPLOYEUR);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

        FWCurrency revenu = new FWCurrency(getRevenu());
        if (revenu.isNegative()) {
            _addError(statement.getTransaction(), getSession().getLabel("ANNONCE_INSCRIPTION_CI_ERREUR_REVENU_NEGATIF"));
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(REAnnonceInscriptionCI.FIELDNAME_ID_ANNONCE_INSCRIPTION_CI,
                this._dbWriteNumeric(statement.getTransaction(), getIdAnnonce()));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        if (_getAction() == BEntity.ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(REAnnonceInscriptionCI.FIELDNAME_ID_ANNONCE_INSCRIPTION_CI,
                    this._dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        }

        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_ID_TIERS,
                this._dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_ID_TIERS_AYANT_DROIT,
                this._dbWriteNumeric(statement.getTransaction(), idTiersAyantDroit, "idTiersAyantDroit"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_REF_INT_CAISSE,
                this._dbWriteString(statement.getTransaction(), refInterneCaisse, "refInterneCaisse"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_MOTIF,
                this._dbWriteString(statement.getTransaction(), motif, "motif"));
        statement.writeField(
                REAnnonceInscriptionCI.FIELDNAME_DATE_CLOTURE,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateCloture), "dateCloture"));
        statement.writeField(
                REAnnonceInscriptionCI.FIELDNAME_DATE_ORDRE,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateOrdre), "dateOrdre"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_NO_CAISSE_TENANT_CI,
                this._dbWriteString(statement.getTransaction(), noCaisseTenantCI, "noCaisseTenantCI"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_NO_AGENCE_TENANT_CI,
                this._dbWriteString(statement.getTransaction(), noAgenceTenantCI, "noAgenceTenantCI"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_NO_AFFILIE,
                this._dbWriteString(statement.getTransaction(), noAffilie, "noAffilie"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_CODE_EXTOURNE,
                this._dbWriteString(statement.getTransaction(), codeExtourne, "codeExtourne"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_GENRE_COTISATION,
                this._dbWriteString(statement.getTransaction(), genreCotisation, "genreCotisation"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_CODE_PARTICULIER,
                this._dbWriteString(statement.getTransaction(), codeParticulier, "codeParticulier"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_PART_BONIF_ASSIST,
                this._dbWriteString(statement.getTransaction(), partBonifAssist, "partBonifAssist"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_CODE_SPECIALE,
                this._dbWriteString(statement.getTransaction(), codeSpecial, "codeSpeciale"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_MOIS_DEBUT_COTISATION,
                this._dbWriteString(statement.getTransaction(), moisDebutCotisations, "moisDebutCotisations"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_MOIS_FIN_COTISATION,
                this._dbWriteString(statement.getTransaction(), moisFinCotisations, "moisFinCotisations"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_ANNEE_COTISATION,
                this._dbWriteString(statement.getTransaction(), anneeCotisations, "anneeCotisations"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_REVENU,
                this._dbWriteNumeric(statement.getTransaction(), revenu, "revenu"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_CODE_ADS,
                this._dbWriteString(statement.getTransaction(), codeADS, "codeADS"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_PROVENANCE,
                this._dbWriteString(statement.getTransaction(), provenance, "provenance"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_CI_ADDITIONNEL, this._dbWriteBoolean(
                statement.getTransaction(), ciAdditionnel, BConstants.DB_TYPE_BOOLEAN_CHAR, "ciAdditionnel"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_BRANCHE_ECONOMIQUE,
                this._dbWriteString(statement.getTransaction(), brancheEconomique, "brancheEconomique"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_PARTIE_INFORMATION,
                this._dbWriteString(statement.getTransaction(), partieInformation, "partieInformation"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_NUMERO_POSTAL_EMPLOYEUR,
                this._dbWriteString(statement.getTransaction(), noPostalEmployeur, "noPostalEmployeur"));
        statement.writeField(REAnnonceInscriptionCI.FIELDNAME_ATTENTE_CI_ADDITIONNEL,
                this._dbWriteNumeric(statement.getTransaction(), attenteCIAdditionnelCS, "attenteCIAdditionnelCS"));
    }

    public String getAnneeCotisations() {
        return anneeCotisations;
    }

    /**
     * <p>
     * Retourne le code système qui définit si le CI est
     * <ul>
     * <li>en attente de rien du tout, le champ est vide
     * <li>en attente d'un CI additionnel -> attente CI additionnel = code système attente ...
     * <li>n'est plus en attente de CI -> CI additionnel reçu = code système traité ...
     * </ul>
     * </p>
     */
    public final String getAttenteCIAdditionnelCS() {
        return attenteCIAdditionnelCS;
    }

    public String getBrancheEconomique() {
        return brancheEconomique;
    }

    public Boolean getCiAdditionnel() {
        return ciAdditionnel;
    }

    public String getCodeADS() {
        return codeADS;
    }

    public String getCodeExtourne() {
        return codeExtourne;
    }

    public String getCodeParticulier() {
        return codeParticulier;
    }

    public String getCodeSpecial() {
        return codeSpecial;
    }

    public String getDateCloture() {
        return dateCloture;
    }

    public String getDateOrdre() {
        return dateOrdre;
    }

    public String getGenreCotisation() {
        return genreCotisation;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAyantDroit() {
        return idTiersAyantDroit;
    }

    public String getMoisDebutCotisations() {
        return moisDebutCotisations;
    }

    public String getMoisFinCotisations() {
        return moisFinCotisations;
    }

    public String getMotif() {
        return motif;
    }

    public String getNoAffilie() {
        return noAffilie;
    }

    public String getNoAgenceTenantCI() {
        return noAgenceTenantCI;
    }

    public String getNoCaisseTenantCI() {
        return noCaisseTenantCI;
    }

    public String getNoPostalEmployeur() {
        return noPostalEmployeur;
    }

    public String getPartBonifAssist() {
        return partBonifAssist;
    }

    public String getPartieInformation() {
        return partieInformation;
    }

    public String getProvenance() {
        return provenance;
    }

    public String getRefInterneCaisse() {
        return refInterneCaisse;
    }

    public String getRevenu() {
        return revenu;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setAnneeCotisations(String string) {
        anneeCotisations = string;
    }

    public final void setAttenteCIAdditionnelCS(String attenteCIAdditionnelCS) {
        this.attenteCIAdditionnelCS = attenteCIAdditionnelCS;
    }

    public void setBrancheEconomique(String string) {
        brancheEconomique = string;
    }

    public void setCiAdditionnel(Boolean ciAdditionnel) {
        this.ciAdditionnel = ciAdditionnel;
    }

    public void setCodeADS(String codeADS) {
        this.codeADS = codeADS;
    }

    public void setCodeExtourne(String codeExtourne) {
        this.codeExtourne = codeExtourne;
    }

    public void setCodeParticulier(String codeParticulier) {
        this.codeParticulier = codeParticulier;
    }

    public void setCodeSpeciale(String codeSpeciale) {
        codeSpecial = codeSpeciale;
    }

    public void setDateCloture(String dateCloture) {
        this.dateCloture = dateCloture;
    }

    public void setDateOrdre(String dateOrdre) {
        this.dateOrdre = dateOrdre;
    }

    public void setGenreCotisation(String genreCotisation) {
        this.genreCotisation = genreCotisation;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAyantDroit(String idTiersAyantDroit) {
        this.idTiersAyantDroit = idTiersAyantDroit;
    }

    public void setMoisDebutCotisations(String string) {
        moisDebutCotisations = string;
    }

    public void setMoisFinCotisations(String string) {
        moisFinCotisations = string;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setNoAffilie(String noAffilie) {
        this.noAffilie = noAffilie;
    }

    public void setNoAgenceTenantCI(String noAgenceTenantCI) {
        this.noAgenceTenantCI = noAgenceTenantCI;
    }

    public void setNoCaisseTenantCI(String noCaisseTenantCI) {
        this.noCaisseTenantCI = noCaisseTenantCI;
    }

    public void setNoPostalEmployeur(String string) {
        noPostalEmployeur = string;
    }

    public void setPartBonifAssist(String partBonifAssist) {
        this.partBonifAssist = partBonifAssist;
    }

    public void setPartieInformation(String string) {
        partieInformation = string;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public void setRefInterneCaisse(String refInterneCaisse) {
        this.refInterneCaisse = refInterneCaisse;
    }

    public void setRevenu(String revenu) {
        this.revenu = revenu;
    }
}
