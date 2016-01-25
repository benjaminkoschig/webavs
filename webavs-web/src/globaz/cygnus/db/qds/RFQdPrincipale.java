/*
 * Créé le 06 janvier 2010
 */
package globaz.cygnus.db.qds;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author jje
 */
public class RFQdPrincipale extends RFQd {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CS_DEGRE_API = "EODAPI";
    public static final String FIELDNAME_CS_GENRE_PC_ACCORDEE = "EOTGPA";
    public static final String FIELDNAME_CS_TYPE_BENEFICIAIRE = "EOTBEN";
    // public static final String FIELDNAME_CS_ETAT_DOSSIER_PC = "EOTEDP";
    public static final String FIELDNAME_CS_TYPE_PC_ACCORDEE = "EOTTPA";
    public static final String FIELDNAME_DATE_DEBUT_PC_ACCORDEE = "EODDPA";
    public static final String FIELDNAME_DATE_FIN_PC_ACCORDEE = "EODFPA";
    // public static final String FIELDNAME_CS_ETAT_PC_ACCORDEE = "EOTEPA";
    public static final String FIELDNAME_EXCEDENT_RECETTE_PC_ACCORDEE = "EOMERP";
    public static final String FIELDNAME_ID_FAM_MOD_AUGMENTATION_QD = "EOIAFA";
    public static final String FIELDNAME_ID_FAM_MOD_SOLDE_EXCEDENT_PRE_DEC = "EOISFA";
    public static final String FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC = "EOIGSF";
    public static final String FIELDNAME_ID_PC_ACCORDEE = "EOIPCA";
    public static final String FIELDNAME_ID_POT_DROIT_PC = "EOIPDP";
    public static final String FIELDNAME_ID_QD_PRINCIPALE = "EOIQDP";
    public static final String FIELDNAME_IS_LAPRAMS = "EOBILA";
    public static final String FIELDNAME_IS_RI = "EOBIRI";
    public static final String FIELDNAME_REMBOURSEMENT_CONJOINT = "EOTREC";
    public static final String FIELDNAME_REMBOURSEMENT_REQUERANT = "EOTRER";

    public static final String TABLE_NAME = "RFQDPRI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csDegreApi = "";
    private String csGenrePCAccordee = "";
    private String csTypeBeneficiaire = "";
    // private String csEtatDossierPC = "";
    private String csTypePCAccordee = "";
    private String dateDebutPCAccordee = "";
    private String dateFinPCAccordee = "";
    // private String csEtatPCAccordee = "";
    private String excedentPCAccordee = "";
    private boolean hasCreationSpy = false;
    private boolean hasSpy = false;
    private String idFamModAugmentationDeQd = "";
    private String idFamModSoldeExcedentPreDec = "";
    private String idGesModSoldeExcedentAugmentationQdPreDec = "";
    private String idPCAccordee = "";
    private String idPotDroitPc = "";
    private String idQdPrincipale = "";
    private Boolean isLAPRAMS = Boolean.FALSE;
    private Boolean isRI = Boolean.FALSE;
    private String remboursementConjoint = "";
    private String remboursementRequerant = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFQdPrincipale.
     */
    public RFQdPrincipale() {
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
        // setId(_incCounter(transaction, "0"));
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += _getCollection();
        getFrom += RFQd.TABLE_NAME;
        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += RFQdPrincipale.TABLE_NAME;
        getFrom += " ON ";
        getFrom += RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE;
        getFrom += "=";
        getFrom += RFQd.FIELDNAME_ID_QD;

        return getFrom;
    }

    /**
     * getter pour le nom de la table des Qd principale
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFQdPrincipale.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des Qd principale
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idQdPrincipale = statement.dbReadNumeric(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
        idPCAccordee = statement.dbReadNumeric(RFQdPrincipale.FIELDNAME_ID_PC_ACCORDEE);
        dateDebutPCAccordee = statement.dbReadDateAMJ(RFQdPrincipale.FIELDNAME_DATE_DEBUT_PC_ACCORDEE);
        dateFinPCAccordee = statement.dbReadDateAMJ(RFQdPrincipale.FIELDNAME_DATE_FIN_PC_ACCORDEE);
        // csEtatPCAccordee =
        // statement.dbReadNumeric(FIELDNAME_CS_ETAT_PC_ACCORDEE);
        excedentPCAccordee = statement.dbReadNumeric(RFQdPrincipale.FIELDNAME_EXCEDENT_RECETTE_PC_ACCORDEE);
        // csEtatDossierPC =
        // statement.dbReadNumeric(FIELDNAME_CS_ETAT_DOSSIER_PC);
        csTypePCAccordee = statement.dbReadNumeric(RFQdPrincipale.FIELDNAME_CS_TYPE_PC_ACCORDEE);
        csGenrePCAccordee = statement.dbReadNumeric(RFQdPrincipale.FIELDNAME_CS_GENRE_PC_ACCORDEE);
        idPotDroitPc = statement.dbReadNumeric(RFQdPrincipale.FIELDNAME_ID_POT_DROIT_PC);
        idFamModSoldeExcedentPreDec = statement
                .dbReadNumeric(RFQdPrincipale.FIELDNAME_ID_FAM_MOD_SOLDE_EXCEDENT_PRE_DEC);
        idFamModAugmentationDeQd = statement.dbReadNumeric(RFQdPrincipale.FIELDNAME_ID_FAM_MOD_AUGMENTATION_QD);
        idGesModSoldeExcedentAugmentationQdPreDec = statement
                .dbReadString(RFQdPrincipale.FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC);
        csTypeBeneficiaire = statement.dbReadNumeric(RFQdPrincipale.FIELDNAME_CS_TYPE_BENEFICIAIRE);
        remboursementConjoint = statement.dbReadNumeric(RFQdPrincipale.FIELDNAME_REMBOURSEMENT_CONJOINT);
        remboursementRequerant = statement.dbReadNumeric(RFQdPrincipale.FIELDNAME_REMBOURSEMENT_REQUERANT);
        isRI = statement.dbReadBoolean(RFQdPrincipale.FIELDNAME_IS_RI);
        isLAPRAMS = statement.dbReadBoolean(RFQdPrincipale.FIELDNAME_IS_LAPRAMS);
        csDegreApi = statement.dbReadNumeric(RFQdPrincipale.FIELDNAME_CS_DEGRE_API);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des Qd principale
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

        statement.writeKey(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE,
                this._dbWriteNumeric(statement.getTransaction(), idQdPrincipale, "idQdPrincipale"));

    }

    /**
     * Méthode d'écriture des champs dans la table des Qd principale
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE,
                this._dbWriteNumeric(statement.getTransaction(), idQdPrincipale, "idQdPrincipale"));
        statement.writeField(RFQdPrincipale.FIELDNAME_ID_PC_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), idPCAccordee, "idPCAccordee"));
        statement.writeField(RFQdPrincipale.FIELDNAME_DATE_DEBUT_PC_ACCORDEE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebutPCAccordee, "dateDebutPCAccordee"));
        statement.writeField(RFQdPrincipale.FIELDNAME_DATE_FIN_PC_ACCORDEE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFinPCAccordee, "dateFinPCAccordee"));
        // statement.writeField(FIELDNAME_CS_ETAT_PC_ACCORDEE,
        // _dbWriteNumeric(statement.getTransaction(),csEtatPCAccordee,"csEtatPCAccordee"));
        statement.writeField(RFQdPrincipale.FIELDNAME_EXCEDENT_RECETTE_PC_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), excedentPCAccordee, "excedentPCAccordee"));
        // statement.writeField(FIELDNAME_CS_ETAT_DOSSIER_PC,
        // _dbWriteNumeric(statement.getTransaction(),csEtatDossierPC,"csEtatDossierPC"));
        statement.writeField(RFQdPrincipale.FIELDNAME_CS_TYPE_PC_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), csTypePCAccordee, "csTypePCAccordee"));
        statement.writeField(RFQdPrincipale.FIELDNAME_CS_GENRE_PC_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), csGenrePCAccordee, "csGenrePCAccordee"));
        statement.writeField(RFQdPrincipale.FIELDNAME_ID_POT_DROIT_PC,
                this._dbWriteNumeric(statement.getTransaction(), idPotDroitPc, "idPotDroitPc"));
        statement.writeField(RFQdPrincipale.FIELDNAME_ID_FAM_MOD_SOLDE_EXCEDENT_PRE_DEC, this._dbWriteNumeric(
                statement.getTransaction(), idFamModSoldeExcedentPreDec, "idFamModSoldeExcedentPreDec"));
        statement.writeField(RFQdPrincipale.FIELDNAME_ID_FAM_MOD_AUGMENTATION_QD,
                this._dbWriteNumeric(statement.getTransaction(), idFamModAugmentationDeQd, "idFamModAugmentationDeQd"));
        statement.writeField(RFQdPrincipale.FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC, this
                ._dbWriteString(statement.getTransaction(), idGesModSoldeExcedentAugmentationQdPreDec,
                        "idGesModSoldeExcedentAugmentationQdPreDec"));
        statement.writeField(RFQdPrincipale.FIELDNAME_CS_TYPE_BENEFICIAIRE,
                this._dbWriteNumeric(statement.getTransaction(), csTypeBeneficiaire, "csTypeBeneficiairePc"));
        statement.writeField(RFQdPrincipale.FIELDNAME_REMBOURSEMENT_CONJOINT,
                this._dbWriteNumeric(statement.getTransaction(), remboursementConjoint, "remboursementConjoint"));
        statement.writeField(RFQdPrincipale.FIELDNAME_REMBOURSEMENT_REQUERANT,
                this._dbWriteNumeric(statement.getTransaction(), remboursementRequerant, "remboursementRequerant"));
        statement.writeField(RFQdPrincipale.FIELDNAME_IS_RI,
                this._dbWriteBoolean(statement.getTransaction(), isRI, BConstants.DB_TYPE_BOOLEAN_CHAR, "isRI"));
        statement.writeField(RFQdPrincipale.FIELDNAME_IS_LAPRAMS, this._dbWriteBoolean(statement.getTransaction(),
                isLAPRAMS, BConstants.DB_TYPE_BOOLEAN_CHAR, "isLAPRAMS"));
        statement.writeField(RFQdPrincipale.FIELDNAME_CS_DEGRE_API,
                this._dbWriteNumeric(statement.getTransaction(), csDegreApi, "csDegreApi"));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getCsDegreApi() {
        return csDegreApi;
    }

    public String getCsGenrePCAccordee() {
        return csGenrePCAccordee;
    }

    public String getCsTypeBeneficiaire() {
        return csTypeBeneficiaire;
    }

    public String getCsTypePCAccordee() {
        return csTypePCAccordee;
    }

    public String getDateDebutPCAccordee() {
        return dateDebutPCAccordee;
    }

    public String getDateFinPCAccordee() {
        return dateFinPCAccordee;
    }

    public String getExcedentPCAccordee() {
        return excedentPCAccordee;
    }

    public String getIdFamModAugmentationDeQd() {
        return idFamModAugmentationDeQd;
    }

    public String getIdFamModSoldeExcedentPreDec() {
        return idFamModSoldeExcedentPreDec;
    }

    public String getIdGesModSoldeExcedentAugmentationQdPreDec() {
        return idGesModSoldeExcedentAugmentationQdPreDec;
    }

    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    public String getIdPotDroitPc() {
        return idPotDroitPc;
    }

    public String getIdQdPrincipale() {
        return idQdPrincipale;
    }

    public Boolean getIsLAPRAMS() {
        return isLAPRAMS;
    }

    public Boolean getIsRI() {
        return isRI;
    }

    public String getRemboursementConjoint() {
        return remboursementConjoint;
    }

    public String getRemboursementRequerant() {
        return remboursementRequerant;
    }

    @Override
    public boolean hasCreationSpy() {
        return hasCreationSpy;
    }

    @Override
    public boolean hasSpy() {
        return hasSpy;
    }

    public void setCsDegreApi(String csDegreApi) {
        this.csDegreApi = csDegreApi;
    }

    public void setCsGenrePCAccordee(String csGenrePCAccordee) {
        this.csGenrePCAccordee = csGenrePCAccordee;
    }

    public void setCsTypeBeneficiaire(String csTypeBeneficiaire) {
        this.csTypeBeneficiaire = csTypeBeneficiaire;
    }

    public void setCsTypePCAccordee(String csTypePCAccordee) {
        this.csTypePCAccordee = csTypePCAccordee;
    }

    public void setDateDebutPCAccordee(String dateDebutPCAccordee) {
        this.dateDebutPCAccordee = dateDebutPCAccordee;
    }

    public void setDateFinPCAccordee(String dateFinPCAccordee) {
        this.dateFinPCAccordee = dateFinPCAccordee;
    }

    public void setExcedentPCAccordee(String excedentPCAccordee) {
        this.excedentPCAccordee = excedentPCAccordee;
    }

    public void setHasCreationSpy(boolean hasCreationSpy) {
        this.hasCreationSpy = hasCreationSpy;
    }

    public void setHasSpy(boolean hasSpy) {
        this.hasSpy = hasSpy;
    }

    public void setIdFamModAugmentationDeQd(String idFamModAugmentationDeQd) {
        this.idFamModAugmentationDeQd = idFamModAugmentationDeQd;
    }

    public void setIdFamModSoldeExcedentPreDec(String idFamModSoldeExcedentPreDec) {
        this.idFamModSoldeExcedentPreDec = idFamModSoldeExcedentPreDec;
    }

    public void setIdGesModSoldeExcedentAugmentationQdPreDec(String idGesModSoldeExcedentAugmentationQdPreDec) {
        this.idGesModSoldeExcedentAugmentationQdPreDec = idGesModSoldeExcedentAugmentationQdPreDec;
    }

    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    public void setIdPotDroitPc(String idPotDroitPc) {
        this.idPotDroitPc = idPotDroitPc;
    }

    public void setIdQdPrincipale(String idQdPrincipale) {
        this.idQdPrincipale = idQdPrincipale;
    }

    public void setIsLAPRAMS(Boolean isLAPRAMS) {
        this.isLAPRAMS = isLAPRAMS;
    }

    public void setIsRI(Boolean isRI) {
        this.isRI = isRI;
    }

    public void setRemboursementConjoint(String remboursementConjoint) {
        this.remboursementConjoint = remboursementConjoint;
    }

    public void setRemboursementRequerant(String remboursementRequerant) {
        this.remboursementRequerant = remboursementRequerant;
    }

}