package globaz.corvus.db.adaptation;

import globaz.corvus.utils.RENumberFormatter;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.tools.PRDateFormater;

/**
 * 
 * @author HPE
 * 
 */
public class REPmtFictif extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_ID_PAIEMENT_FICTIF = "AAIPFI";

    public static final String FIELDNAME_MNT_AI_EXTRAORDINAIRES = "AATMIE";
    public static final String FIELDNAME_MNT_AI_ORDINAIRES = "AATMIO";
    public static final String FIELDNAME_MNT_API_AI = "AATMAI";
    public static final String FIELDNAME_MNT_API_AVS = "AATMAS";
    public static final String FIELDNAME_MNT_AVS_EXTRAORDINAIRES = "AATMSE";
    public static final String FIELDNAME_MNT_AVS_ORDINAIRES = "AATMSO";
    public static final String FIELDNAME_MNT_TOTAL_AI = "AATMTA";
    public static final String FIELDNAME_MNT_TOTAL_API = "AATMTI";
    public static final String FIELDNAME_MNT_TOTAL_AVS = "AATMTS";
    public static final String FIELDNAME_MNT_TOTAL_EXTRAORDINAIRES = "AATMTE";
    public static final String FIELDNAME_MNT_TOTAL_GENERAL = "AATMTG";
    public static final String FIELDNAME_MNT_TOTAL_ORDINAIRES = "AATMTO";
    public static final String FIELDNAME_MOIS_RAPPORT = "AADMRA";
    public static final String FIELDNAME_NB_AI_EXTRAORDINAIRES = "AATNIE";
    public static final String FIELDNAME_NB_AI_ORDINAIRES = "AATNIO";
    public static final String FIELDNAME_NB_API_AI = "AATNAI";
    public static final String FIELDNAME_NB_API_AVS = "AATNAS";
    public static final String FIELDNAME_NB_AVS_EXTRAORDINAIRES = "AATNSE";
    public static final String FIELDNAME_NB_AVS_ORDINAIRES = "AATNSO";
    public static final String FIELDNAME_NB_TOTAL_AI = "AATNTA";
    public static final String FIELDNAME_NB_TOTAL_API = "AATNTI";
    public static final String FIELDNAME_NB_TOTAL_AVS = "AATNTS";
    public static final String FIELDNAME_NB_TOTAL_EXTRAORDINAIRES = "AATNTE";
    public static final String FIELDNAME_NB_TOTAL_GENERAL = "AATNTG";
    public static final String FIELDNAME_NB_TOTAL_ORDINAIRES = "AATNTO";
    public static final String FIELDNAME_TYPE_DONNEES = "AATTYD";
    public static final String TABLE_NAME_PMT_FICTIF = "REPMFIC";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idPaiementFictif = "";
    private String moisRapport = "";
    private String montantAIExtraordinaires = "";
    private String montantAIOrdinaires = "";
    private String montantAPIAI = "";
    private String montantAPIAVS = "";
    private String montantAVSExtraordinaires = "";
    private String montantAVSOrdinaires = "";
    private String montantTotalAI = "";
    private String montantTotalAPI = "";
    private String montantTotalAVS = "";
    private String montantTotalExtraordinaires = "";
    private String montantTotalGeneral = "";
    private String montantTotalOrdinaires = "";
    private String nbAIExtraordinaires = "";
    private String nbAIOrdinaires = "";
    private String nbAPIAI = "";
    private String nbAPIAVS = "";
    private String nbAVSExtraordinaires = "";
    private String nbAVSOrdinaires = "";
    private String nbTotalAI = "";
    private String nbTotalAPI = "";
    private String nbTotalAVS = "";
    private String nbTotaleExtraordinaires = "";
    private String nbTotalGeneral = "";
    private String nbTotalOrdinaires = "";
    private String typeDonnes = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdPaiementFictif(_incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return TABLE_NAME_PMT_FICTIF;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idPaiementFictif = statement.dbReadNumeric(FIELDNAME_ID_PAIEMENT_FICTIF);
        moisRapport = RENumberFormatter.fmt(
                PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement.dbReadNumeric(FIELDNAME_MOIS_RAPPORT)), false,
                false, true, 4, 2);
        montantAVSOrdinaires = statement.dbReadNumeric(FIELDNAME_MNT_AVS_ORDINAIRES);
        nbAVSOrdinaires = statement.dbReadNumeric(FIELDNAME_NB_AVS_ORDINAIRES);
        montantAIOrdinaires = statement.dbReadNumeric(FIELDNAME_MNT_AI_ORDINAIRES);
        nbAIOrdinaires = statement.dbReadNumeric(FIELDNAME_NB_AI_ORDINAIRES);
        montantTotalOrdinaires = statement.dbReadNumeric(FIELDNAME_MNT_TOTAL_ORDINAIRES);
        nbTotalOrdinaires = statement.dbReadNumeric(FIELDNAME_NB_TOTAL_ORDINAIRES);
        montantAVSExtraordinaires = statement.dbReadNumeric(FIELDNAME_MNT_AVS_EXTRAORDINAIRES);
        nbAVSExtraordinaires = statement.dbReadNumeric(FIELDNAME_NB_AVS_EXTRAORDINAIRES);
        montantAIExtraordinaires = statement.dbReadNumeric(FIELDNAME_MNT_AI_EXTRAORDINAIRES);
        nbAIExtraordinaires = statement.dbReadNumeric(FIELDNAME_NB_AI_EXTRAORDINAIRES);
        montantTotalExtraordinaires = statement.dbReadNumeric(FIELDNAME_MNT_TOTAL_EXTRAORDINAIRES);
        nbTotaleExtraordinaires = statement.dbReadNumeric(FIELDNAME_NB_TOTAL_EXTRAORDINAIRES);
        montantAPIAI = statement.dbReadNumeric(FIELDNAME_MNT_API_AI);
        nbAPIAI = statement.dbReadNumeric(FIELDNAME_NB_API_AI);
        montantAPIAVS = statement.dbReadNumeric(FIELDNAME_MNT_API_AVS);
        nbAPIAVS = statement.dbReadNumeric(FIELDNAME_NB_API_AVS);
        montantTotalAPI = statement.dbReadNumeric(FIELDNAME_MNT_TOTAL_API);
        nbTotalAPI = statement.dbReadNumeric(FIELDNAME_NB_TOTAL_API);
        montantTotalAVS = statement.dbReadNumeric(FIELDNAME_MNT_TOTAL_AVS);
        nbTotalAVS = statement.dbReadNumeric(FIELDNAME_NB_TOTAL_AVS);
        montantTotalAI = statement.dbReadNumeric(FIELDNAME_MNT_TOTAL_AI);
        nbTotalAI = statement.dbReadNumeric(FIELDNAME_NB_TOTAL_AI);
        montantTotalGeneral = statement.dbReadNumeric(FIELDNAME_MNT_TOTAL_GENERAL);
        nbTotalGeneral = statement.dbReadNumeric(FIELDNAME_NB_TOTAL_GENERAL);
        typeDonnes = statement.dbReadNumeric(FIELDNAME_TYPE_DONNEES);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_PAIEMENT_FICTIF,
                _dbWriteNumeric(statement.getTransaction(), idPaiementFictif, "idPaiementFictif"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_PAIEMENT_FICTIF,
                _dbWriteNumeric(statement.getTransaction(), idPaiementFictif, "idPaiementFictif"));
        statement.writeField(
                FIELDNAME_MOIS_RAPPORT,
                _dbWriteNumeric(statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(moisRapport),
                        "moisRapport"));
        statement.writeField(FIELDNAME_MNT_AVS_ORDINAIRES,
                _dbWriteNumeric(statement.getTransaction(), montantAVSOrdinaires, "montantAVSOrdinaires"));
        statement.writeField(FIELDNAME_NB_AVS_ORDINAIRES,
                _dbWriteNumeric(statement.getTransaction(), nbAVSOrdinaires, "nbAVSOrdinaires"));
        statement.writeField(FIELDNAME_MNT_AI_ORDINAIRES,
                _dbWriteNumeric(statement.getTransaction(), montantAIOrdinaires, "montantAIOrdinaires"));
        statement.writeField(FIELDNAME_NB_AI_ORDINAIRES,
                _dbWriteNumeric(statement.getTransaction(), nbAIOrdinaires, "nbAIOrdinaires"));
        statement.writeField(FIELDNAME_MNT_TOTAL_ORDINAIRES,
                _dbWriteNumeric(statement.getTransaction(), montantTotalOrdinaires, "montantTotalOrdinaires"));
        statement.writeField(FIELDNAME_NB_TOTAL_ORDINAIRES,
                _dbWriteNumeric(statement.getTransaction(), nbTotalOrdinaires, "nbTotalOrdinaires"));
        statement.writeField(FIELDNAME_MNT_AVS_EXTRAORDINAIRES,
                _dbWriteNumeric(statement.getTransaction(), montantAVSExtraordinaires, "montantAVSExtraordinaires"));
        statement.writeField(FIELDNAME_NB_AVS_EXTRAORDINAIRES,
                _dbWriteNumeric(statement.getTransaction(), nbAVSExtraordinaires, "nbAVSExtraordinaires"));
        statement.writeField(FIELDNAME_MNT_AI_EXTRAORDINAIRES,
                _dbWriteNumeric(statement.getTransaction(), montantAIExtraordinaires, "montantAIExtraordinaires"));
        statement.writeField(FIELDNAME_NB_AI_EXTRAORDINAIRES,
                _dbWriteNumeric(statement.getTransaction(), nbAIExtraordinaires, "nbAIExtraordinaires"));
        statement
                .writeField(
                        FIELDNAME_MNT_TOTAL_EXTRAORDINAIRES,
                        _dbWriteNumeric(statement.getTransaction(), montantTotalExtraordinaires,
                                "montantTotalExtraordinaires"));
        statement.writeField(FIELDNAME_NB_TOTAL_EXTRAORDINAIRES,
                _dbWriteNumeric(statement.getTransaction(), nbTotaleExtraordinaires, "nbTotaleExtraordinaires"));
        statement.writeField(FIELDNAME_MNT_API_AI,
                _dbWriteNumeric(statement.getTransaction(), montantAPIAI, "montantAPIAI"));
        statement.writeField(FIELDNAME_NB_API_AI, _dbWriteNumeric(statement.getTransaction(), nbAPIAI, "nbAPIAI"));
        statement.writeField(FIELDNAME_MNT_API_AVS,
                _dbWriteNumeric(statement.getTransaction(), montantAPIAVS, "montantAPIAVS"));
        statement.writeField(FIELDNAME_NB_API_AVS, _dbWriteNumeric(statement.getTransaction(), nbAPIAVS, "nbAPIAVS"));
        statement.writeField(FIELDNAME_MNT_TOTAL_API,
                _dbWriteNumeric(statement.getTransaction(), montantTotalAPI, "montantTotalAPI"));
        statement.writeField(FIELDNAME_NB_TOTAL_API,
                _dbWriteNumeric(statement.getTransaction(), nbTotalAPI, "nbTotalAPI"));
        statement.writeField(FIELDNAME_MNT_TOTAL_AVS,
                _dbWriteNumeric(statement.getTransaction(), montantTotalAVS, "montantTotalAVS"));
        statement.writeField(FIELDNAME_NB_TOTAL_AVS,
                _dbWriteNumeric(statement.getTransaction(), nbTotalAVS, "nbTotalAVS"));
        statement.writeField(FIELDNAME_MNT_TOTAL_AI,
                _dbWriteNumeric(statement.getTransaction(), montantTotalAI, "montantTotalAI"));
        statement
                .writeField(FIELDNAME_NB_TOTAL_AI, _dbWriteNumeric(statement.getTransaction(), nbTotalAI, "nbTotalAI"));
        statement.writeField(FIELDNAME_MNT_TOTAL_GENERAL,
                _dbWriteNumeric(statement.getTransaction(), montantTotalGeneral, "montantTotalGeneral"));
        statement.writeField(FIELDNAME_NB_TOTAL_GENERAL,
                _dbWriteNumeric(statement.getTransaction(), nbTotalGeneral, "nbTotalGeneral"));
        statement.writeField(FIELDNAME_TYPE_DONNEES,
                _dbWriteNumeric(statement.getTransaction(), typeDonnes, "typeDonnes"));

    }

    // ~ Getter & Setter
    // --------------------------------------------------------------------------------------------------------

    public String getIdPaiementFictif() {
        return idPaiementFictif;
    }

    public String getMoisRapport() {
        return moisRapport;
    }

    public String getMontantAIExtraordinaires() {
        return montantAIExtraordinaires;
    }

    public String getMontantAIOrdinaires() {
        return montantAIOrdinaires;
    }

    public String getMontantAPIAI() {
        return montantAPIAI;
    }

    public String getMontantAPIAVS() {
        return montantAPIAVS;
    }

    public String getMontantAVSExtraordinaires() {
        return montantAVSExtraordinaires;
    }

    public String getMontantAVSOrdinaires() {
        return montantAVSOrdinaires;
    }

    public String getMontantTotalAI() {
        return montantTotalAI;
    }

    public String getMontantTotalAPI() {
        return montantTotalAPI;
    }

    public String getMontantTotalAVS() {
        return montantTotalAVS;
    }

    public String getMontantTotalExtraordinaires() {
        return montantTotalExtraordinaires;
    }

    public String getMontantTotalGeneral() {
        return montantTotalGeneral;
    }

    public String getMontantTotalOrdinaires() {
        return montantTotalOrdinaires;
    }

    public String getNbAIExtraordinaires() {
        return nbAIExtraordinaires;
    }

    public String getNbAIOrdinaires() {
        return nbAIOrdinaires;
    }

    public String getNbAPIAI() {
        return nbAPIAI;
    }

    public String getNbAPIAVS() {
        return nbAPIAVS;
    }

    public String getNbAVSExtraordinaires() {
        return nbAVSExtraordinaires;
    }

    public String getNbAVSOrdinaires() {
        return nbAVSOrdinaires;
    }

    public String getNbTotalAI() {
        return nbTotalAI;
    }

    public String getNbTotalAPI() {
        return nbTotalAPI;
    }

    public String getNbTotalAVS() {
        return nbTotalAVS;
    }

    public String getNbTotaleExtraordinaires() {
        return nbTotaleExtraordinaires;
    }

    public String getNbTotalGeneral() {
        return nbTotalGeneral;
    }

    public String getNbTotalOrdinaires() {
        return nbTotalOrdinaires;
    }

    public String getTypeDonnes() {
        return typeDonnes;
    }

    public void setIdPaiementFictif(String idPaiementFictif) {
        this.idPaiementFictif = idPaiementFictif;
    }

    public void setMoisRapport(String moisRapport) {
        this.moisRapport = moisRapport;
    }

    public void setMontantAIExtraordinaires(String montantAIExtraordinaires) {
        this.montantAIExtraordinaires = montantAIExtraordinaires;
    }

    public void setMontantAIOrdinaires(String montantAIOrdinaires) {
        this.montantAIOrdinaires = montantAIOrdinaires;
    }

    public void setMontantAPIAI(String montantAPIAI) {
        this.montantAPIAI = montantAPIAI;
    }

    public void setMontantAPIAVS(String montantAPIAVS) {
        this.montantAPIAVS = montantAPIAVS;
    }

    public void setMontantAVSExtraordinaires(String montantAVSExtraordinaires) {
        this.montantAVSExtraordinaires = montantAVSExtraordinaires;
    }

    public void setMontantAVSOrdinaires(String montantAVSOrdinaires) {
        this.montantAVSOrdinaires = montantAVSOrdinaires;
    }

    public void setMontantTotalAI(String montantTotalAI) {
        this.montantTotalAI = montantTotalAI;
    }

    public void setMontantTotalAPI(String montantTotalAPI) {
        this.montantTotalAPI = montantTotalAPI;
    }

    public void setMontantTotalAVS(String montantTotalAVS) {
        this.montantTotalAVS = montantTotalAVS;
    }

    public void setMontantTotalExtraordinaires(String montantTotalExtraordinaires) {
        this.montantTotalExtraordinaires = montantTotalExtraordinaires;
    }

    public void setMontantTotalGeneral(String montantTotalGeneral) {
        this.montantTotalGeneral = montantTotalGeneral;
    }

    public void setMontantTotalOrdinaires(String montantTotalOrdinaires) {
        this.montantTotalOrdinaires = montantTotalOrdinaires;
    }

    public void setNbAIExtraordinaires(String nbAIExtraordinaires) {
        this.nbAIExtraordinaires = nbAIExtraordinaires;
    }

    public void setNbAIOrdinaires(String nbAIOrdinaires) {
        this.nbAIOrdinaires = nbAIOrdinaires;
    }

    public void setNbAPIAI(String nbAPIAI) {
        this.nbAPIAI = nbAPIAI;
    }

    public void setNbAPIAVS(String nbAPIAVS) {
        this.nbAPIAVS = nbAPIAVS;
    }

    public void setNbAVSExtraordinaires(String nbAVSExtraordinaires) {
        this.nbAVSExtraordinaires = nbAVSExtraordinaires;
    }

    public void setNbAVSOrdinaires(String nbAVSOrdinaires) {
        this.nbAVSOrdinaires = nbAVSOrdinaires;
    }

    public void setNbTotalAI(String nbTotalAI) {
        this.nbTotalAI = nbTotalAI;
    }

    public void setNbTotalAPI(String nbTotalAPI) {
        this.nbTotalAPI = nbTotalAPI;
    }

    public void setNbTotalAVS(String nbTotalAVS) {
        this.nbTotalAVS = nbTotalAVS;
    }

    public void setNbTotaleExtraordinaires(String nbTotaleExtraordinaires) {
        this.nbTotaleExtraordinaires = nbTotaleExtraordinaires;
    }

    public void setNbTotalGeneral(String nbTotalGeneral) {
        this.nbTotalGeneral = nbTotalGeneral;
    }

    public void setNbTotalOrdinaires(String nbTotalOrdinaires) {
        this.nbTotalOrdinaires = nbTotalOrdinaires;
    }

    public void setTypeDonnes(String typeDonnes) {
        this.typeDonnes = typeDonnes;
    }

}
