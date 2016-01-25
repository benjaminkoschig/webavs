/*
 * Créé le 20 août. 07
 */
package globaz.corvus.db.decisions;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author BSC
 */
public class RECopieDecision extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_AFFILIE = "ZLIAFF";
    public static final String FIELDNAME_ID_DECISION = "ZLIDEC";
    public static final String FIELDNAME_ID_DECISION_COPIE = "ZLIDCP";
    public static final String FIELDNAME_ID_PROCEDURE_COMMUNICATION = "ZLIPRC";
    public static final String FIELDNAME_ID_TIERS_COPIE = "ZLITIE";
    public static final String FIELDNAME_IS_COPIE_TRONQUEE = "ZLBTRO";
    public static final String FIELDNAME_IS_PAGE_GARDE = "ZLBPGA";
    public static final String FIELDNAME_IS_PART_ANNEXES = "ZLBPAN";
    public static final String FIELDNAME_IS_PART_BASE_CALCUL = "ZLBPBC";
    public static final String FIELDNAME_IS_PART_COPIES = "ZLBPCO";
    public static final String FIELDNAME_IS_PART_DECOMPTE = "ZLBPDE";
    public static final String FIELDNAME_IS_PART_MOYENS_DROIT = "ZLBPMD";
    public static final String FIELDNAME_IS_PART_REMARQUES = "ZLBPRE";
    public static final String FIELDNAME_IS_PART_SIGNATURES = "ZLBPSI";
    public static final String FIELDNAME_IS_PART_VERSEMENT = "ZLBPVE";

    public static final String TABLE_NAME_COPIE_DECISION = "REDECCP";

    private String idAffilie = "";
    private String idDecision = "";
    private String idDecisionCopie = "";
    private String idProcedureCommunication = "";
    private String idTiersCopie = "";
    private Boolean isAnnexes = Boolean.FALSE;
    private Boolean isBaseCalcul = Boolean.FALSE;
    private Boolean isCopies = Boolean.FALSE;
    private Boolean isCopieTronquee = Boolean.FALSE;
    private Boolean isDecompte = Boolean.FALSE;
    private Boolean isMoyensDroit = Boolean.FALSE;
    private Boolean isPageGarde = Boolean.FALSE;
    private Boolean isRemarques = Boolean.FALSE;
    private Boolean isSignature = Boolean.FALSE;
    private Boolean isVersementA = Boolean.FALSE;

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdDecisionCopie(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return RECopieDecision.TABLE_NAME_COPIE_DECISION;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDecisionCopie = statement.dbReadNumeric(RECopieDecision.FIELDNAME_ID_DECISION_COPIE);
        idDecision = statement.dbReadNumeric(RECopieDecision.FIELDNAME_ID_DECISION);
        idTiersCopie = statement.dbReadNumeric(RECopieDecision.FIELDNAME_ID_TIERS_COPIE);
        idAffilie = statement.dbReadNumeric(RECopieDecision.FIELDNAME_ID_AFFILIE);
        isCopieTronquee = statement.dbReadBoolean(RECopieDecision.FIELDNAME_IS_COPIE_TRONQUEE);
        isVersementA = statement.dbReadBoolean(RECopieDecision.FIELDNAME_IS_PART_VERSEMENT);
        isBaseCalcul = statement.dbReadBoolean(RECopieDecision.FIELDNAME_IS_PART_BASE_CALCUL);
        isDecompte = statement.dbReadBoolean(RECopieDecision.FIELDNAME_IS_PART_DECOMPTE);
        isRemarques = statement.dbReadBoolean(RECopieDecision.FIELDNAME_IS_PART_REMARQUES);
        isMoyensDroit = statement.dbReadBoolean(RECopieDecision.FIELDNAME_IS_PART_MOYENS_DROIT);
        isSignature = statement.dbReadBoolean(RECopieDecision.FIELDNAME_IS_PART_SIGNATURES);
        isAnnexes = statement.dbReadBoolean(RECopieDecision.FIELDNAME_IS_PART_ANNEXES);
        isCopies = statement.dbReadBoolean(RECopieDecision.FIELDNAME_IS_PART_COPIES);
        isPageGarde = statement.dbReadBoolean(RECopieDecision.FIELDNAME_IS_PAGE_GARDE);

        // BZ 5536, ajout de l'ID de la copie (procédure de communication) dans la copie de décision
        // pour pouvoir récupérer ultérieurement la remarque
        idProcedureCommunication = statement.dbReadNumeric(RECopieDecision.FIELDNAME_ID_PROCEDURE_COMMUNICATION);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RECopieDecision.FIELDNAME_ID_DECISION_COPIE,
                this._dbWriteNumeric(statement.getTransaction(), idDecisionCopie, "idDecisionCopie"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RECopieDecision.FIELDNAME_ID_DECISION_COPIE,
                this._dbWriteNumeric(statement.getTransaction(), idDecisionCopie, "idDecisionCopie"));
        statement.writeField(RECopieDecision.FIELDNAME_ID_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(RECopieDecision.FIELDNAME_ID_TIERS_COPIE,
                this._dbWriteNumeric(statement.getTransaction(), idTiersCopie, "idTiersCopie"));
        statement.writeField(RECopieDecision.FIELDNAME_ID_AFFILIE,
                this._dbWriteNumeric(statement.getTransaction(), idAffilie, "idAffilie"));
        statement.writeField(RECopieDecision.FIELDNAME_IS_COPIE_TRONQUEE, this._dbWriteBoolean(
                statement.getTransaction(), isCopieTronquee, BConstants.DB_TYPE_BOOLEAN_CHAR, "isCopieTronquee"));
        statement.writeField(RECopieDecision.FIELDNAME_IS_PART_VERSEMENT, this._dbWriteBoolean(
                statement.getTransaction(), isVersementA, BConstants.DB_TYPE_BOOLEAN_CHAR, "isVersementA"));
        statement.writeField(RECopieDecision.FIELDNAME_IS_PART_BASE_CALCUL, this._dbWriteBoolean(
                statement.getTransaction(), isBaseCalcul, BConstants.DB_TYPE_BOOLEAN_CHAR, "isBaseCalcul"));
        statement.writeField(RECopieDecision.FIELDNAME_IS_PART_DECOMPTE, this._dbWriteBoolean(
                statement.getTransaction(), isDecompte, BConstants.DB_TYPE_BOOLEAN_CHAR, "isDecompte"));
        statement.writeField(RECopieDecision.FIELDNAME_IS_PART_REMARQUES, this._dbWriteBoolean(
                statement.getTransaction(), isRemarques, BConstants.DB_TYPE_BOOLEAN_CHAR, "isRemarques"));
        statement.writeField(RECopieDecision.FIELDNAME_IS_PART_MOYENS_DROIT, this._dbWriteBoolean(
                statement.getTransaction(), isMoyensDroit, BConstants.DB_TYPE_BOOLEAN_CHAR, "isMoyensDroit"));
        statement.writeField(RECopieDecision.FIELDNAME_IS_PART_SIGNATURES, this._dbWriteBoolean(
                statement.getTransaction(), isSignature, BConstants.DB_TYPE_BOOLEAN_CHAR, "isSignature"));
        statement.writeField(RECopieDecision.FIELDNAME_IS_PART_ANNEXES, this._dbWriteBoolean(
                statement.getTransaction(), isAnnexes, BConstants.DB_TYPE_BOOLEAN_CHAR, "isAnnexes"));
        statement
                .writeField(RECopieDecision.FIELDNAME_IS_PART_COPIES, this._dbWriteBoolean(statement.getTransaction(),
                        isCopies, BConstants.DB_TYPE_BOOLEAN_CHAR, "isCopies"));
        statement.writeField(RECopieDecision.FIELDNAME_IS_PAGE_GARDE, this._dbWriteBoolean(statement.getTransaction(),
                isPageGarde, BConstants.DB_TYPE_BOOLEAN_CHAR, "isPageGarde"));

        // BZ 5536, ajout de l'ID de la copie (procédure de communication) dans la copie de décision
        // pour pouvoir récupérer ultérieurement la remarque
        statement.writeField(RECopieDecision.FIELDNAME_ID_PROCEDURE_COMMUNICATION,
                this._dbWriteNumeric(statement.getTransaction(), idProcedureCommunication, "idProcedureCommunication"));
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDecisionCopie() {
        return idDecisionCopie;
    }

    public String getIdProcedureCommunication() {
        return idProcedureCommunication;
    }

    public String getIdTiersCopie() {
        return idTiersCopie;
    }

    public Boolean getIsAnnexes() {
        return isAnnexes;
    }

    public Boolean getIsBaseCalcul() {
        return isBaseCalcul;
    }

    public Boolean getIsCopies() {
        return isCopies;
    }

    public Boolean getIsDecompte() {
        return isDecompte;
    }

    public Boolean getIsMoyensDroit() {
        return isMoyensDroit;
    }

    public Boolean getIsPageGarde() {
        return isPageGarde;
    }

    public Boolean getIsRemarques() {
        return isRemarques;
    }

    public Boolean getIsSignature() {
        return isSignature;
    }

    public Boolean getIsVersementA() {
        return isVersementA;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDecisionCopie(String string) {
        idDecisionCopie = string;
    }

    public void setIdProcedureCommunication(String idProcedureCommunication) {
        this.idProcedureCommunication = idProcedureCommunication;
    }

    public void setIdTiersCopie(String string) {
        idTiersCopie = string;
    }

    public void setIsAnnexes(Boolean isAnnexes) {
        this.isAnnexes = isAnnexes;
    }

    public void setIsBaseCalcul(Boolean isBaseCalcul) {
        this.isBaseCalcul = isBaseCalcul;
    }

    public void setIsCopies(Boolean isCopies) {
        this.isCopies = isCopies;
    }

    public void setIsDecompte(Boolean isDecompte) {
        this.isDecompte = isDecompte;
    }

    public void setIsMoyensDroit(Boolean isMoyensDroit) {
        this.isMoyensDroit = isMoyensDroit;
    }

    public void setIsPageGarde(Boolean isPageGarde) {
        this.isPageGarde = isPageGarde;
    }

    public void setIsRemarques(Boolean isRemarques) {
        this.isRemarques = isRemarques;
    }

    public void setIsSignature(Boolean isSignature) {
        this.isSignature = isSignature;
    }

    public void setIsVersementA(Boolean isVersementA) {
        this.isVersementA = isVersementA;
    }
}
