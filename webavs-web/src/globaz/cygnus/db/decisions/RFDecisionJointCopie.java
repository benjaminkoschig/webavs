/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.decisions;

import globaz.globall.db.BStatement;

/**
 * 
 * @author fha
 */
public class RFDecisionJointCopie extends RFDecision {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDECES = "HPDDEC";
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";

    public static final String FIELDNAME_ID_GESTIONNAIRE = "KUSER";

    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";

    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";
    public static final String FIELDNAME_VISA_GESTIONNAIRE = "FVISA";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";

    public static final String TABLE_GESTIONNAIRES = "FWSUSRP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);

        /************* Faire aussi la jointure sur le gestionnaire *******/

        // jointure entre la table des décisions et la table copie décision
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFCopieDecision.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFCopieDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFCopieDecision.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION);

        return fromClauseBuffer.toString();
    }

    private transient String fromClause = null;
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

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String idTiers = "";

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idCopie = statement.dbReadNumeric(RFCopieDecision.FIELDNAME_ID_COPIE);
        idDecision = statement.dbReadNumeric(RFCopieDecision.FIELDNAME_ID_DECISION);
        idTiers = statement.dbReadNumeric(RFCopieDecision.FIELDNAME_ID_TIERS);
        hasPageGarde = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_PAGE_GARDE);
        hasVersement = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_VERSEMENT);
        hasDecompte = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_DECOMPTE);
        hasRemarques = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_REMARQUE);
        hasMoyensDroit = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_MOYEN_DROIT);
        hasSignature = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_SIGNATURE);
        hasAnnexes = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_ANNEXES);
        hasCopies = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_COPIES);

    }

    public String getFromClause() {
        return fromClause;
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

    @Override
    public String getIdDecision() {
        return idDecision;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
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

    @Override
    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

}
