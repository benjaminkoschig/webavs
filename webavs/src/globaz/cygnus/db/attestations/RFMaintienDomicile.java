/*
 * Créé le 14 avril 2010
 */
package globaz.cygnus.db.attestations;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */
public class RFMaintienDomicile extends RFAttestation {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ACCIDENT = "FLBACC";
    public static final String FIELDNAME_AIDE_DUREE_DETERMINEE = "FLDADD";
    public static final String FIELDNAME_AIDE_DUREE_INDETERMINEE = "FLDADI";
    public static final String FIELDNAME_AIDE_REMUNERE_NECESSAIRE = "FLBARE";
    public static final String FIELDNAME_AUTRE_PERSONNE_ACCIDENT = "FLBAAC";
    public static final String FIELDNAME_AUTRE_PERSONNE_DESCRIPTION_MOTIF = "FLLAMO";
    public static final String FIELDNAME_AUTRE_PERSONNE_INVALIDITE = "FLBAIN";
    public static final String FIELDNAME_AUTRE_PERSONNE_MALADIE = "FLBAMA";
    public static final String FIELDNAME_AUTRE_PERSONNE_TROUBLE_AGE = "FLBAAG";
    public static final String FIELDNAME_DESCRIPTION_MOTIF = "FLLMOT";
    public static final String FIELDNAME_DUREE_AIDE_REMUNERE = "FLMDNR";

    public static final String FIELDNAME_DUREE_AIDE_REMUNERE_TENUE_MENAGE = "FLMDNM";
    public static final String FIELDNAME_HEURES_MOIS_REMUNERER = "FLNHEM";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String FIELDNAME_ID_ATTESTATION_MAINTIEN_DOMICILE = "FLIATT";
    public static final String FIELDNAME_INVALIDITE = "FLBINV";
    public static final String FIELDNAME_LESSIVE = "FLNLES";
    public static final String FIELDNAME_LITS = "FLNLIT";
    public static final String FIELDNAME_MALADIE = "FLBMAL";
    public static final String FIELDNAME_NETTOYAGE_RANGEMENT = "FLNNRA";
    public static final String FIELDNAME_NOMBRE_PERSONNE_LOGEMENT = "FLMNPL";

    public static final String FIELDNAME_NOMBRE_PIECE_UTILISE_BENEF = "FLMNPU";
    public static final String FIELDNAME_NOMBRE_TOTAL_PIECE = "FLMNBP";
    public static final String FIELDNAME_RAISON_PAS_REPAS = "FLLRAI";
    public static final String FIELDNAME_RECOIT_REPAS_CMS = "FLBRPC";
    public static final String FIELDNAME_REPAS_DOMICILE_CMS = "FLBRED";
    public static final String FIELDNAME_REPASSAGE = "FLNRRA";
    public static final String FIELDNAME_TROUBLE_AGE = "FLBTAG";
    public static final String FIELDNAME_VAISSELLE = "FLNVAI";
    public static final String FIELDNAME_VEILLES_PRESENCE = "FLNVPR";

    public static final String TABLE_NAME = "RFMADOM";

    private Boolean aAccident = Boolean.FALSE;
    private String aideDureeDeterminee = "";
    private String aideDureeIndeterminee = "";
    private Boolean aideRemunereNecessaire = Boolean.FALSE;
    private Boolean aInvalidite = Boolean.FALSE;
    private Boolean aMaladie = Boolean.FALSE;

    private Boolean aTroubleAge = Boolean.FALSE;
    private Boolean autrePersonneAAccident = Boolean.FALSE;
    private Boolean autrePersonneAInvalidite = Boolean.FALSE;
    private Boolean autrePersonneAMaladie = Boolean.FALSE;
    private Boolean autrePersonneATroubleAge = Boolean.FALSE;

    private String autrePersonneDescriptionMotif = "";
    private String descriptionMotif = "";
    private String dureeAideRemunere = "";

    private String dureeAideRemunereTenueMenage = "";
    private String heuresMoisRemunere = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String idAttestationMaintienDomicile = "";
    private String lessive = "";
    private String lits = "";
    private String nettoyageRangement = "";
    private String nombrePersonneLogement = "";
    private String nombrePieceUtilise = "";
    private String nombreTotalPiece = "";
    private String raisonPasRepas = "";
    private Boolean recoitRepasCMS = Boolean.FALSE;
    private Boolean repasDomicileCMS = Boolean.FALSE;
    private String repassage = "";
    private String vaisselle = "";
    private String veillesPresence = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossiers.
     */
    public RFMaintienDomicile() {
        super();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return faux
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += _getCollection();
        getFrom += RFAttestation.TABLE_NAME;
        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += RFMaintienDomicile.TABLE_NAME;
        getFrom += " ON ";
        getFrom += RFMaintienDomicile.FIELDNAME_ID_ATTESTATION_MAINTIEN_DOMICILE;
        getFrom += "=";
        getFrom += RFAttestation.FIELDNAME_ID_ATTESTATION;

        return getFrom;
    }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFMaintienDomicile.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idAttestationMaintienDomicile = statement
                .dbReadNumeric(RFMaintienDomicile.FIELDNAME_ID_ATTESTATION_MAINTIEN_DOMICILE);
        descriptionMotif = statement.dbReadString(RFMaintienDomicile.FIELDNAME_DESCRIPTION_MOTIF);
        autrePersonneDescriptionMotif = statement
                .dbReadString(RFMaintienDomicile.FIELDNAME_AUTRE_PERSONNE_DESCRIPTION_MOTIF);
        nombreTotalPiece = statement.dbReadNumeric(RFMaintienDomicile.FIELDNAME_NOMBRE_TOTAL_PIECE);
        nombrePieceUtilise = statement.dbReadNumeric(RFMaintienDomicile.FIELDNAME_NOMBRE_PIECE_UTILISE_BENEF);
        nombrePersonneLogement = statement.dbReadNumeric(RFMaintienDomicile.FIELDNAME_NOMBRE_PERSONNE_LOGEMENT);
        veillesPresence = statement.dbReadNumeric(RFMaintienDomicile.FIELDNAME_VEILLES_PRESENCE);
        nettoyageRangement = statement.dbReadNumeric(RFMaintienDomicile.FIELDNAME_NETTOYAGE_RANGEMENT);
        vaisselle = statement.dbReadNumeric(RFMaintienDomicile.FIELDNAME_VAISSELLE);
        lits = statement.dbReadNumeric(RFMaintienDomicile.FIELDNAME_LITS);
        lessive = statement.dbReadNumeric(RFMaintienDomicile.FIELDNAME_LESSIVE);
        repassage = statement.dbReadNumeric(RFMaintienDomicile.FIELDNAME_REPASSAGE);
        dureeAideRemunere = statement.dbReadNumeric(RFMaintienDomicile.FIELDNAME_DUREE_AIDE_REMUNERE);
        dureeAideRemunereTenueMenage = statement
                .dbReadNumeric(RFMaintienDomicile.FIELDNAME_DUREE_AIDE_REMUNERE_TENUE_MENAGE);
        raisonPasRepas = statement.dbReadString(RFMaintienDomicile.FIELDNAME_RAISON_PAS_REPAS);
        heuresMoisRemunere = statement.dbReadNumeric(RFMaintienDomicile.FIELDNAME_HEURES_MOIS_REMUNERER);
        aideDureeDeterminee = statement.dbReadDateAMJ(RFMaintienDomicile.FIELDNAME_AIDE_DUREE_DETERMINEE);
        aideDureeIndeterminee = statement.dbReadDateAMJ(RFMaintienDomicile.FIELDNAME_AIDE_DUREE_INDETERMINEE);

        aTroubleAge = statement.dbReadBoolean(RFMaintienDomicile.FIELDNAME_TROUBLE_AGE);
        aMaladie = statement.dbReadBoolean(RFMaintienDomicile.FIELDNAME_MALADIE);
        aAccident = statement.dbReadBoolean(RFMaintienDomicile.FIELDNAME_ACCIDENT);
        aInvalidite = statement.dbReadBoolean(RFMaintienDomicile.FIELDNAME_INVALIDITE);
        autrePersonneATroubleAge = statement.dbReadBoolean(RFMaintienDomicile.FIELDNAME_AUTRE_PERSONNE_TROUBLE_AGE);
        autrePersonneAMaladie = statement.dbReadBoolean(RFMaintienDomicile.FIELDNAME_AUTRE_PERSONNE_MALADIE);
        autrePersonneAAccident = statement.dbReadBoolean(RFMaintienDomicile.FIELDNAME_AUTRE_PERSONNE_ACCIDENT);
        autrePersonneAInvalidite = statement.dbReadBoolean(RFMaintienDomicile.FIELDNAME_AUTRE_PERSONNE_INVALIDITE);
        repasDomicileCMS = statement.dbReadBoolean(RFMaintienDomicile.FIELDNAME_REPAS_DOMICILE_CMS);
        recoitRepasCMS = statement.dbReadBoolean(RFMaintienDomicile.FIELDNAME_RECOIT_REPAS_CMS);
        aideRemunereNecessaire = statement.dbReadBoolean(RFMaintienDomicile.FIELDNAME_AIDE_REMUNERE_NECESSAIRE);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des demande maintien à dom.
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFMaintienDomicile.FIELDNAME_ID_ATTESTATION_MAINTIEN_DOMICILE, this._dbWriteNumeric(
                statement.getTransaction(), idAttestationMaintienDomicile, "idAttestationMaintienDomicile"));

    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFMaintienDomicile.FIELDNAME_ID_ATTESTATION_MAINTIEN_DOMICILE, this._dbWriteNumeric(
                statement.getTransaction(), idAttestationMaintienDomicile, "idAttestationMaintienDomicile"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_TROUBLE_AGE, this._dbWriteBoolean(statement.getTransaction(),
                aTroubleAge, BConstants.DB_TYPE_BOOLEAN_CHAR, "aTroubleAge"));
        statement
                .writeField(RFMaintienDomicile.FIELDNAME_MALADIE, this._dbWriteBoolean(statement.getTransaction(),
                        aMaladie, BConstants.DB_TYPE_BOOLEAN_CHAR, "aMaladie"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_ACCIDENT, this._dbWriteBoolean(statement.getTransaction(),
                aAccident, BConstants.DB_TYPE_BOOLEAN_CHAR, "aAccident"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_INVALIDITE, this._dbWriteBoolean(statement.getTransaction(),
                aInvalidite, BConstants.DB_TYPE_BOOLEAN_CHAR, "aInvalidite"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_DESCRIPTION_MOTIF,
                this._dbWriteString(statement.getTransaction(), descriptionMotif, "descriptionMotif"));

        statement.writeField(RFMaintienDomicile.FIELDNAME_AUTRE_PERSONNE_TROUBLE_AGE, this._dbWriteBoolean(
                statement.getTransaction(), autrePersonneATroubleAge, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "autrePersonneATroubleAge"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_AUTRE_PERSONNE_MALADIE, this._dbWriteBoolean(
                statement.getTransaction(), autrePersonneAMaladie, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "autrePersonneAMaladie"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_AUTRE_PERSONNE_ACCIDENT, this._dbWriteBoolean(
                statement.getTransaction(), autrePersonneAAccident, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "autrePersonneAAccident"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_AUTRE_PERSONNE_INVALIDITE, this._dbWriteBoolean(
                statement.getTransaction(), autrePersonneAInvalidite, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "autrePersonneAInvalidite"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_AUTRE_PERSONNE_DESCRIPTION_MOTIF, this._dbWriteString(
                statement.getTransaction(), autrePersonneDescriptionMotif, "autrePersonneDescriptionMotif"));

        statement.writeField(RFMaintienDomicile.FIELDNAME_AIDE_DUREE_DETERMINEE,
                this._dbWriteDateAMJ(statement.getTransaction(), aideDureeDeterminee, "aideDureeDeterminee"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_AIDE_DUREE_INDETERMINEE,
                this._dbWriteDateAMJ(statement.getTransaction(), aideDureeIndeterminee, "aideDureeIndeterminee"));

        statement.writeField(RFMaintienDomicile.FIELDNAME_REPAS_DOMICILE_CMS, this._dbWriteBoolean(
                statement.getTransaction(), repasDomicileCMS, BConstants.DB_TYPE_BOOLEAN_CHAR, "repasDomicileCMS"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_RECOIT_REPAS_CMS, this._dbWriteBoolean(
                statement.getTransaction(), recoitRepasCMS, BConstants.DB_TYPE_BOOLEAN_CHAR, "recoitRepasCMS"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_AIDE_REMUNERE_NECESSAIRE, this._dbWriteBoolean(
                statement.getTransaction(), aideRemunereNecessaire, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "aideRemunereNecessaire"));

        statement.writeField(RFMaintienDomicile.FIELDNAME_RAISON_PAS_REPAS,
                this._dbWriteString(statement.getTransaction(), raisonPasRepas, "raisonPasRepas"));

        statement.writeField(RFMaintienDomicile.FIELDNAME_NOMBRE_TOTAL_PIECE,
                this._dbWriteNumeric(statement.getTransaction(), nombreTotalPiece, "nombreTotalPiece"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_NOMBRE_PIECE_UTILISE_BENEF,
                this._dbWriteNumeric(statement.getTransaction(), nombrePieceUtilise, "nombrePieceUtilise"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_NOMBRE_PERSONNE_LOGEMENT,
                this._dbWriteNumeric(statement.getTransaction(), nombrePersonneLogement, "nombrePersonneLogement"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_VEILLES_PRESENCE,
                this._dbWriteNumeric(statement.getTransaction(), veillesPresence, "veillesPresence"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_NETTOYAGE_RANGEMENT,
                this._dbWriteNumeric(statement.getTransaction(), nettoyageRangement, "nettoyageRangement"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_VAISSELLE,
                this._dbWriteNumeric(statement.getTransaction(), vaisselle, "vaisselle"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_LITS,
                this._dbWriteNumeric(statement.getTransaction(), lits, "lits"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_LESSIVE,
                this._dbWriteNumeric(statement.getTransaction(), lessive, "lessive"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_REPASSAGE,
                this._dbWriteNumeric(statement.getTransaction(), repassage, "repassage"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_DUREE_AIDE_REMUNERE,
                this._dbWriteNumeric(statement.getTransaction(), dureeAideRemunere, "dureeAideRemunere"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_DUREE_AIDE_REMUNERE_TENUE_MENAGE, this._dbWriteNumeric(
                statement.getTransaction(), dureeAideRemunereTenueMenage, "dureeAideRemunereTenueMenage"));
        statement.writeField(RFMaintienDomicile.FIELDNAME_HEURES_MOIS_REMUNERER,
                this._dbWriteNumeric(statement.getTransaction(), heuresMoisRemunere, "heuresMoisRemunere"));

    }

    public Boolean getaAccident() {
        return aAccident;
    }

    public String getAideDureeDeterminee() {
        return aideDureeDeterminee;
    }

    public String getAideDureeIndeterminee() {
        return aideDureeIndeterminee;
    }

    public Boolean getAideRemunereNecessaire() {
        return aideRemunereNecessaire;
    }

    public Boolean getaInvalidite() {
        return aInvalidite;
    }

    public Boolean getaMaladie() {
        return aMaladie;
    }

    public Boolean getaTroubleAge() {
        return aTroubleAge;
    }

    public Boolean getAutrePersonneAAccident() {
        return autrePersonneAAccident;
    }

    public Boolean getAutrePersonneAInvalidite() {
        return autrePersonneAInvalidite;
    }

    public Boolean getAutrePersonneAMaladie() {
        return autrePersonneAMaladie;
    }

    public Boolean getAutrePersonneATroubleAge() {
        return autrePersonneATroubleAge;
    }

    public String getAutrePersonneDescriptionMotif() {
        return autrePersonneDescriptionMotif;
    }

    public String getDescriptionMotif() {
        return descriptionMotif;
    }

    public String getDureeAideRemunere() {
        return dureeAideRemunere;
    }

    public String getDureeAideRemunereTenueMenage() {
        return dureeAideRemunereTenueMenage;
    }

    public String getHeuresMoisRemunere() {
        return heuresMoisRemunere;
    }

    public String getIdAttestationMaintienDomicile() {
        return idAttestationMaintienDomicile;
    }

    public String getLessive() {
        return lessive;
    }

    public String getLits() {
        return lits;
    }

    public String getNettoyageRangement() {
        return nettoyageRangement;
    }

    public String getNombrePersonneLogement() {
        return nombrePersonneLogement;
    }

    public String getNombrePieceUtilise() {
        return nombrePieceUtilise;
    }

    public String getNombreTotalPiece() {
        return nombreTotalPiece;
    }

    public String getRaisonPasRepas() {
        return raisonPasRepas;
    }

    public Boolean getRecoitRepasCMS() {
        return recoitRepasCMS;
    }

    public Boolean getRepasDomicileCMS() {
        return repasDomicileCMS;
    }

    public String getRepassage() {
        return repassage;
    }

    public String getVaisselle() {
        return vaisselle;
    }

    public String getVeillesPresence() {
        return veillesPresence;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setaAccident(Boolean aAccident) {
        this.aAccident = aAccident;
    }

    public void setAideDureeDeterminee(String aideDureeDeterminee) {
        this.aideDureeDeterminee = aideDureeDeterminee;
    }

    public void setAideDureeIndeterminee(String aideDureeIndeterminee) {
        this.aideDureeIndeterminee = aideDureeIndeterminee;
    }

    public void setAideRemunereNecessaire(Boolean aideRemunereNecessaire) {
        this.aideRemunereNecessaire = aideRemunereNecessaire;
    }

    public void setaInvalidite(Boolean aInvalidite) {
        this.aInvalidite = aInvalidite;
    }

    public void setaMaladie(Boolean aMaladie) {
        this.aMaladie = aMaladie;
    }

    public void setaTroubleAge(Boolean aTroubleAge) {
        this.aTroubleAge = aTroubleAge;
    }

    public void setAutrePersonneAAccident(Boolean autrePersonneAAccident) {
        this.autrePersonneAAccident = autrePersonneAAccident;
    }

    public void setAutrePersonneAInvalidite(Boolean autrePersonneAInvalidite) {
        this.autrePersonneAInvalidite = autrePersonneAInvalidite;
    }

    public void setAutrePersonneAMaladie(Boolean autrePersonneAMaladie) {
        this.autrePersonneAMaladie = autrePersonneAMaladie;
    }

    public void setAutrePersonneATroubleAge(Boolean autrePersonneATroubleAge) {
        this.autrePersonneATroubleAge = autrePersonneATroubleAge;
    }

    public void setAutrePersonneDescriptionMotif(String autrePersonneDescriptionMotif) {
        this.autrePersonneDescriptionMotif = autrePersonneDescriptionMotif;
    }

    public void setDescriptionMotif(String descriptionMotif) {
        this.descriptionMotif = descriptionMotif;
    }

    public void setDureeAideRemunere(String dureeAideRemunere) {
        this.dureeAideRemunere = dureeAideRemunere;
    }

    public void setDureeAideRemunereTenueMenage(String dureeAideRemunereTenueMenage) {
        this.dureeAideRemunereTenueMenage = dureeAideRemunereTenueMenage;
    }

    public void setHeuresMoisRemunere(String heuresMoisRemunere) {
        this.heuresMoisRemunere = heuresMoisRemunere;
    }

    public void setIdAttestationMaintienDomicile(String idAttestationMaintienDomicile) {
        this.idAttestationMaintienDomicile = idAttestationMaintienDomicile;
    }

    public void setLessive(String lessive) {
        this.lessive = lessive;
    }

    public void setLits(String lits) {
        this.lits = lits;
    }

    public void setNettoyageRangement(String nettoyageRangement) {
        this.nettoyageRangement = nettoyageRangement;
    }

    public void setNombrePersonneLogement(String nombrePersonneLogement) {
        this.nombrePersonneLogement = nombrePersonneLogement;
    }

    public void setNombrePieceUtilise(String nombrePieceUtilise) {
        this.nombrePieceUtilise = nombrePieceUtilise;
    }

    public void setNombreTotalPiece(String nombreTotalPiece) {
        this.nombreTotalPiece = nombreTotalPiece;
    }

    public void setRaisonPasRepas(String raisonPasRepas) {
        this.raisonPasRepas = raisonPasRepas;
    }

    public void setRecoitRepasCMS(Boolean recoitRepasCMS) {
        this.recoitRepasCMS = recoitRepasCMS;
    }

    public void setRepasDomicileCMS(Boolean repasDomicileCMS) {
        this.repasDomicileCMS = repasDomicileCMS;
    }

    public void setRepassage(String repassage) {
        this.repassage = repassage;
    }

    public void setVaisselle(String vaisselle) {
        this.vaisselle = vaisselle;
    }

    public void setVeillesPresence(String veillesPresence) {
        this.veillesPresence = veillesPresence;
    }

}
