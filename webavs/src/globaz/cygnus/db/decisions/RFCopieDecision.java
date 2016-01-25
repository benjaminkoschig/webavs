/*
 * Créé le 03 décembre 2010
 */
package globaz.cygnus.db.decisions;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author fha
 */
public class RFCopieDecision extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_HAS_ANNEXES = "FNBHAN";
    public static final String FIELDNAME_HAS_COPIES = "FNBHCO";
    public static final String FIELDNAME_HAS_DECOMPTE = "FNBHDE";
    public static final String FIELDNAME_HAS_MOYEN_DROIT = "FNBHMD";
    public static final String FIELDNAME_HAS_PAGE_GARDE = "FNBHPG";
    public static final String FIELDNAME_HAS_REMARQUE = "FNBHRE";
    public static final String FIELDNAME_HAS_SIGNATURE = "FNBHSI";
    public static final String FIELDNAME_HAS_VERSEMENT = "FNBHVE";
    public static final String FIELDNAME_ID_COPIE = "FNICOP";
    public static final String FIELDNAME_ID_DECISION = "FNIDEC";
    public static final String FIELDNAME_ID_TIERS = "FNITIE";

    public static final String TABLE_NAME = "RFCOPDE";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean hasAnnexes = Boolean.FALSE;
    private Boolean hasCopies = Boolean.FALSE;
    private Boolean hasDecompte = Boolean.FALSE;
    private Boolean hasMoyensDroit = Boolean.FALSE;
    private Boolean hasPageGarde = Boolean.FALSE;
    private Boolean hasRemarques = Boolean.FALSE;
    private Boolean hasSignature = Boolean.FALSE;
    private Boolean hasVersement = Boolean.FALSE;
    private String idCopie = "";
    private String idDecision = "";
    private String idTiers = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossiers.
     */
    public RFCopieDecision() {
        super();
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
        setIdCopie(_incCounter(transaction, idCopie, TABLE_NAME));
    }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCopie = statement.dbReadNumeric(FIELDNAME_ID_COPIE);
        idDecision = statement.dbReadNumeric(FIELDNAME_ID_DECISION);
        idTiers = statement.dbReadNumeric(FIELDNAME_ID_TIERS);
        hasPageGarde = statement.dbReadBoolean(FIELDNAME_HAS_PAGE_GARDE);
        hasVersement = statement.dbReadBoolean(FIELDNAME_HAS_VERSEMENT);
        hasDecompte = statement.dbReadBoolean(FIELDNAME_HAS_DECOMPTE);
        hasRemarques = statement.dbReadBoolean(FIELDNAME_HAS_REMARQUE);
        hasMoyensDroit = statement.dbReadBoolean(FIELDNAME_HAS_MOYEN_DROIT);
        hasSignature = statement.dbReadBoolean(FIELDNAME_HAS_SIGNATURE);
        hasAnnexes = statement.dbReadBoolean(FIELDNAME_HAS_ANNEXES);
        hasCopies = statement.dbReadBoolean(FIELDNAME_HAS_COPIES);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_COPIE, _dbWriteNumeric(statement.getTransaction(), idCopie, "idCopie"));
    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_COPIE, _dbWriteNumeric(statement.getTransaction(), idCopie, "idCopie"));
        statement.writeField(FIELDNAME_ID_DECISION,
                _dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(FIELDNAME_ID_TIERS, _dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(
                FIELDNAME_HAS_PAGE_GARDE,
                _dbWriteBoolean(statement.getTransaction(), hasPageGarde, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "hasPageGarde"));
        statement.writeField(
                FIELDNAME_HAS_VERSEMENT,
                _dbWriteBoolean(statement.getTransaction(), hasVersement, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "hasVersement"));
        statement
                .writeField(
                        FIELDNAME_HAS_DECOMPTE,
                        _dbWriteBoolean(statement.getTransaction(), hasDecompte, BConstants.DB_TYPE_BOOLEAN_CHAR,
                                "hasDecompte"));
        statement.writeField(
                FIELDNAME_HAS_REMARQUE,
                _dbWriteBoolean(statement.getTransaction(), hasRemarques, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "hasRemarques"));
        statement.writeField(
                FIELDNAME_HAS_MOYEN_DROIT,
                _dbWriteBoolean(statement.getTransaction(), hasMoyensDroit, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "hasMoyensDroit"));
        statement.writeField(
                FIELDNAME_HAS_SIGNATURE,
                _dbWriteBoolean(statement.getTransaction(), hasSignature, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "hasSignature"));
        statement.writeField(FIELDNAME_HAS_ANNEXES,
                _dbWriteBoolean(statement.getTransaction(), hasAnnexes, BConstants.DB_TYPE_BOOLEAN_CHAR, "hasAnnexes"));
        statement.writeField(FIELDNAME_HAS_COPIES,
                _dbWriteBoolean(statement.getTransaction(), hasCopies, BConstants.DB_TYPE_BOOLEAN_CHAR, "hasCopies"));
    }

    public Boolean getHasAnnexes() {
        return hasAnnexes;
    }

    public Boolean getHasCopies() {
        return hasCopies;
    }

    public Boolean getHasDecompte() {
        return hasDecompte;
    }

    public Boolean getHasMoyensDroit() {
        return hasMoyensDroit;
    }

    public Boolean getHasPageGarde() {
        return hasPageGarde;
    }

    public Boolean getHasRemarques() {
        return hasRemarques;
    }

    public Boolean getHasSignature() {
        return hasSignature;
    }

    public Boolean getHasVersement() {
        return hasVersement;
    }

    public String getIdCopie() {
        return idCopie;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdTiers() {
        return idTiers;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setHasAnnexes(Boolean hasAnnexes) {
        this.hasAnnexes = hasAnnexes;
    }

    public void setHasCopies(Boolean hasCopies) {
        this.hasCopies = hasCopies;
    }

    public void setHasDecompte(Boolean hasDecompte) {
        this.hasDecompte = hasDecompte;
    }

    public void setHasMoyensDroit(Boolean hasMoyensDroit) {
        this.hasMoyensDroit = hasMoyensDroit;
    }

    public void setHasPageGarde(Boolean hasPageGarde) {
        this.hasPageGarde = hasPageGarde;
    }

    public void setHasRemarques(Boolean hasRemarques) {
        this.hasRemarques = hasRemarques;
    }

    public void setHasSignature(Boolean hasSignature) {
        this.hasSignature = hasSignature;
    }

    public void setHasVersement(Boolean hasVersement) {
        this.hasVersement = hasVersement;
    }

    public void setIdCopie(String idCopie) {
        this.idCopie = idCopie;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

}