package globaz.cygnus.db.contributions;

import globaz.cygnus.exceptions.RFBusinessException;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Ajouté lors du mandat InfoRom D0034
 * 
 * @author PBA
 */
public class RFContributionsAssistanceAI extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String CODE_API = "GECAPI";
    public static final String DATE_DEBUT_PERIODE = "GEDDEB";
    public static final String DATE_DEBUT_RECOURS = "GEDDER";
    public static final String DATE_DECISION_AI = "GEDDEC";
    public static final String DATE_DEPOT_DEMANDE_CAAI = "GEDDEP";
    public static final String DATE_FIN_PERIODE = "GEDFIN";
    public static final String DATE_FIN_RECOURS = "GEDFIR";
    public static final String DATE_RECEPTION_DECISION_CAAI = "GEDRDE";
    public static final String ID_CONTRIBUTION_ASSISTANCE_AI = "GEICON";
    public static final String ID_DOSSIER_RFM = "GEIDOS";
    public static final String MONTANT_API = "GEMAPI";
    public static final String MONTANT_CONTRIBUTION = "GEMOAT";
    public static final String NOMBRE_HEURES = "GEMNBH";
    public static final String REMARQUE = "GELREM";
    public static final String TABLE_CONTRIBUTION_ASSISTANCE_AI = "RFCONAI";

    private Integer codeAPI = null;
    private String dateDebutPeriode = "";
    private String dateDebutRecours = "";
    private String dateDecisionAI = "";
    private String dateDepotDemandeCAAI = "";
    private String dateFinPeriode = "";
    private String dateFinRecours = "";
    private String dateReceptionDecisionCAAI = "";
    private Integer idContributionAssistanceAI = null;
    private Integer idDossierRFM = null;
    private FWCurrency montantAPI = null;
    private FWCurrency montantContribution = null;
    private Double nombreHeures = null;
    private String remarque = "";

    public RFContributionsAssistanceAI() {
        super();
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdContributionAssistanceAI(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return RFContributionsAssistanceAI.TABLE_CONTRIBUTION_ASSISTANCE_AI;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idContributionAssistanceAI = new Integer(
                statement.dbReadNumeric(RFContributionsAssistanceAI.ID_CONTRIBUTION_ASSISTANCE_AI));
        idDossierRFM = new Integer(statement.dbReadNumeric(RFContributionsAssistanceAI.ID_DOSSIER_RFM));

        codeAPI = new Integer(statement.dbReadNumeric(RFContributionsAssistanceAI.CODE_API));
        dateDebutPeriode = statement.dbReadDateAMJ(RFContributionsAssistanceAI.DATE_DEBUT_PERIODE);
        dateDebutRecours = statement.dbReadDateAMJ(RFContributionsAssistanceAI.DATE_DEBUT_RECOURS);
        dateDecisionAI = statement.dbReadDateAMJ(RFContributionsAssistanceAI.DATE_DECISION_AI);
        dateDepotDemandeCAAI = statement.dbReadDateAMJ(RFContributionsAssistanceAI.DATE_DEPOT_DEMANDE_CAAI);
        dateFinPeriode = statement.dbReadDateAMJ(RFContributionsAssistanceAI.DATE_FIN_PERIODE);
        dateFinRecours = statement.dbReadDateAMJ(RFContributionsAssistanceAI.DATE_FIN_RECOURS);
        dateReceptionDecisionCAAI = statement.dbReadDateAMJ(RFContributionsAssistanceAI.DATE_RECEPTION_DECISION_CAAI);
        montantAPI = new FWCurrency(statement.dbReadNumeric(RFContributionsAssistanceAI.MONTANT_API));
        montantContribution = new FWCurrency(statement.dbReadNumeric(RFContributionsAssistanceAI.MONTANT_CONTRIBUTION));
        nombreHeures = new Double(statement.dbReadNumeric(RFContributionsAssistanceAI.NOMBRE_HEURES));
        remarque = statement.dbReadString(RFContributionsAssistanceAI.REMARQUE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (!JadeDateUtil.isGlobazDate(getDateDebutPeriode())) {
            throw new RFBusinessException(getSession().getLabel("ERROR_DATE_DEBUT_PERIODE_OBLIGATOIRE"));
        }
        if (JadeDateUtil.isGlobazDate(getDateFinPeriode())
                && JadeDateUtil.isDateBefore(getDateFinPeriode(), getDateDebutPeriode())) {
            throw new RFBusinessException(getSession().getLabel("ERROR_DATE_FIN_DOIT_ETRE_ULTERIEURE_A_DATE_DEBUT"));
        }
        if (JadeDateUtil.isGlobazDate(getDateDebutRecours()) && JadeDateUtil.isGlobazDate(getDateFinRecours())
                && JadeDateUtil.isDateBefore(getDateFinRecours(), getDateDebutRecours())) {
            throw new RFBusinessException(getSession().getLabel("ERROR_DATE_FIN_DOIT_ETRE_ULTERIEURE_A_DATE_DEBUT"));
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFContributionsAssistanceAI.ID_CONTRIBUTION_ASSISTANCE_AI, this._dbWriteNumeric(
                statement.getTransaction(), getIdContributionAssistanceAI(), "idContributionAssistanceAI"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFContributionsAssistanceAI.ID_CONTRIBUTION_ASSISTANCE_AI,
                this._dbWriteNumeric(statement.getTransaction(), getIdContributionAssistanceAI()));
        statement.writeField(RFContributionsAssistanceAI.ID_DOSSIER_RFM,
                this._dbWriteNumeric(statement.getTransaction(), getIdDossierRFM()));

        statement.writeField(RFContributionsAssistanceAI.CODE_API,
                this._dbWriteNumeric(statement.getTransaction(), getCodeAPI()));
        statement.writeField(RFContributionsAssistanceAI.DATE_DEBUT_PERIODE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateDebutPeriode()));
        statement.writeField(RFContributionsAssistanceAI.DATE_DEBUT_RECOURS,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateDebutRecours()));
        statement.writeField(RFContributionsAssistanceAI.DATE_DECISION_AI,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateDecisionAI()));
        statement.writeField(RFContributionsAssistanceAI.DATE_DEPOT_DEMANDE_CAAI,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateDepotDemandeCAAI()));
        statement.writeField(RFContributionsAssistanceAI.DATE_FIN_PERIODE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateFinPeriode()));
        statement.writeField(RFContributionsAssistanceAI.DATE_FIN_RECOURS,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateFinRecours()));
        statement.writeField(RFContributionsAssistanceAI.DATE_RECEPTION_DECISION_CAAI,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateReceptionDecisionCAAI()));
        statement.writeField(RFContributionsAssistanceAI.MONTANT_API,
                this._dbWriteBigDecimal(statement.getTransaction(), getMontantAPISansFormat()));
        statement.writeField(RFContributionsAssistanceAI.MONTANT_CONTRIBUTION,
                this._dbWriteBigDecimal(statement.getTransaction(), getMontantContributionSansFormat()));
        statement.writeField(RFContributionsAssistanceAI.NOMBRE_HEURES,
                this._dbWriteBigDecimal(statement.getTransaction(), getNombreHeures()));
        statement.writeField(RFContributionsAssistanceAI.REMARQUE,
                this._dbWriteString(statement.getTransaction(), getRemarque()));
    }

    public String getCodeAPI() {
        if (codeAPI != null) {
            return codeAPI.toString();
        }
        return "";
    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public String getDateDebutRecours() {
        return dateDebutRecours;
    }

    public String getDateDecisionAI() {
        return dateDecisionAI;
    }

    public String getDateDepotDemandeCAAI() {
        return dateDepotDemandeCAAI;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    public String getDateFinRecours() {
        return dateFinRecours;
    }

    public String getDateReceptionDecisionCAAI() {
        return dateReceptionDecisionCAAI;
    }

    public String getIdContributionAssistanceAI() {
        if (idContributionAssistanceAI != null) {
            return idContributionAssistanceAI.toString();
        }
        return "";
    }

    public String getIdDossierRFM() {
        if (idDossierRFM != null) {
            return idDossierRFM.toString();
        }
        return "";
    }

    public String getMontantAPI() {
        if (montantAPI != null) {
            return montantAPI.toStringFormat();
        }
        return "";
    }

    public String getMontantAPISansFormat() {
        if (montantAPI != null) {
            return montantAPI.toString();
        }
        return "";
    }

    public String getMontantContribution() {
        if (montantContribution != null) {
            return montantContribution.toStringFormat();
        }
        return "";
    }

    public String getMontantContributionSansFormat() {
        if (montantContribution != null) {
            return montantContribution.toString();
        }
        return "";
    }

    public String getNombreHeures() {
        if (nombreHeures != null) {
            return nombreHeures.toString();
        }
        return "";
    }

    public String getRemarque() {
        return remarque;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setCodeAPI(String codeAPI) {
        if (JadeStringUtil.isBlank(codeAPI)) {
            this.codeAPI = null;
        } else {
            this.codeAPI = new Integer(codeAPI);
        }
    }

    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    public void setDateDebutRecours(String dateDebutRecours) {
        this.dateDebutRecours = dateDebutRecours;
    }

    public void setDateDecisionAI(String dateDecisionAI) {
        this.dateDecisionAI = dateDecisionAI;
    }

    public void setDateDepotDemandeCAAI(String dateDepotDemandeCAAI) {
        this.dateDepotDemandeCAAI = dateDepotDemandeCAAI;
    }

    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    public void setDateFinRecours(String dateFinRecours) {
        this.dateFinRecours = dateFinRecours;
    }

    public void setDateReceptionDecisionCAAI(String dateReceptionDecisionCAAI) {
        this.dateReceptionDecisionCAAI = dateReceptionDecisionCAAI;
    }

    public void setIdContributionAssistanceAI(String idContributionAssistanceAI) {
        if (JadeStringUtil.isBlank(idContributionAssistanceAI)) {
            this.idContributionAssistanceAI = null;
        } else {
            this.idContributionAssistanceAI = new Integer(idContributionAssistanceAI);
        }
    }

    public void setIdDossierRFM(String idDossierRFM) {
        if (JadeStringUtil.isBlank(idDossierRFM)) {
            this.idDossierRFM = null;
        } else {
            this.idDossierRFM = new Integer(idDossierRFM);
        }
    }

    public void setMontantAPI(String montantAPI) {
        if (JadeStringUtil.isBlank(montantAPI)) {
            this.montantAPI = null;
        } else {
            this.montantAPI = new FWCurrency(montantAPI);
        }
    }

    public void setMontantContribution(String montantContribution) {
        if (JadeStringUtil.isBlank(montantContribution)) {
            this.montantContribution = null;
        } else {
            this.montantContribution = new FWCurrency(montantContribution);
        }
    }

    public void setNombreHeures(String nombreHeures) {
        if (JadeStringUtil.isBlank(nombreHeures)) {
            this.nombreHeures = null;
        } else {
            this.nombreHeures = new Double(nombreHeures);
        }
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }
}
