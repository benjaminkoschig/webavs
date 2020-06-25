/**
 * 
 */
package globaz.apg.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author dde
 */
public class APBreakRule extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_BREAK_RULE_LIBELLE = "VRBRLI";
    public static String FIELDNAME_BREAK_RULE_CODE = "VRBRCO";
    public static String FIELDNAME_DATE_QUITTANCE = "VRDATE";
    public static String FIELDNAME_GESTIONNAIRE = "VRGEST";
    public static String FIELDNAME_ID_BREAK_RULE = "VRIDBR";
    public static String FIELDNAME_ID_DROIT = "VRIDDR";
    public static String FIELDNAME_ID_PRESTATION = "VRIDPR";
    public static String TABLE_NAME = "APBRULES";


    private String libelleBreakCode = "";
    private String breakRuleCode = "";
    private String dateQuittance = "";
    private String gestionnaire = "";
    private String idBreakRule = "";
    private String idDroit = "";
    private String idPrestation = "";

    public APBreakRule() {
        super();
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdBreakRule(this._incCounter(transaction, "0"));
        super._beforeAdd(transaction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return APBreakRule.TABLE_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        breakRuleCode = statement.dbReadNumeric(APBreakRule.FIELDNAME_BREAK_RULE_CODE);
        dateQuittance = statement.dbReadDateAMJ(APBreakRule.FIELDNAME_DATE_QUITTANCE);
        gestionnaire = statement.dbReadString(APBreakRule.FIELDNAME_GESTIONNAIRE);
        idBreakRule = statement.dbReadNumeric(APBreakRule.FIELDNAME_ID_BREAK_RULE);
        idDroit = statement.dbReadNumeric(APBreakRule.FIELDNAME_ID_DROIT);
        idPrestation = statement.dbReadNumeric(APBreakRule.FIELDNAME_ID_PRESTATION);
        libelleBreakCode = statement.dbReadString(APBreakRule.FIELDNAME_BREAK_RULE_LIBELLE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(APBreakRule.FIELDNAME_ID_BREAK_RULE,
                this._dbWriteNumeric(statement.getTransaction(), idBreakRule, "idBreakRule"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(APBreakRule.FIELDNAME_ID_BREAK_RULE,
                this._dbWriteNumeric(statement.getTransaction(), idBreakRule, "idBreakRule"));
        statement.writeField(APBreakRule.FIELDNAME_DATE_QUITTANCE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateQuittance, "dateQuittance"));
        statement.writeField(APBreakRule.FIELDNAME_GESTIONNAIRE,
                this._dbWriteString(statement.getTransaction(), gestionnaire, "gestionnaire"));
        statement.writeField(APBreakRule.FIELDNAME_ID_DROIT,
                this._dbWriteNumeric(statement.getTransaction(), idDroit, "idDroit"));
        statement.writeField(APBreakRule.FIELDNAME_ID_PRESTATION,
                this._dbWriteNumeric(statement.getTransaction(), idPrestation, "idPrestation"));
        statement.writeField(APBreakRule.FIELDNAME_BREAK_RULE_CODE,
                this._dbWriteNumeric(statement.getTransaction(), breakRuleCode, "breakRuleCode"));
        statement.writeField(APBreakRule.FIELDNAME_BREAK_RULE_LIBELLE,
                this._dbWriteString(statement.getTransaction(), libelleBreakCode, "libelleBreakCode"));

    }

    /**
     * @return the breakRuleCode
     */
    public String getBreakRuleCode() {
        return breakRuleCode;
    }

    /**
     * @return the dateQuittance
     */
    public String getDateQuittance() {
        return dateQuittance;
    }

    /**
     * @return the gestionnaire
     */
    public String getGestionnaire() {
        return gestionnaire;
    }

    /**
     * @return the idBreakRule
     */
    public String getIdBreakRule() {
        return idBreakRule;
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * @param breakRuleCode
     *            the breakRuleCode to set
     */
    public void setBreakRuleCode(String breakRuleCode) {
        this.breakRuleCode = breakRuleCode;
    }

    /**
     * @param dateQuittance
     *            the dateQuittance to set
     */
    public void setDateQuittance(String dateQuittance) {
        this.dateQuittance = dateQuittance;
    }

    /**
     * @param gestionnaire
     *            the gestionnaire to set
     */
    public void setGestionnaire(String gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    /**
     * @param idBreakRule
     *            the idBreakRule to set
     */
    public void setIdBreakRule(String idBreakRule) {
        this.idBreakRule = idBreakRule;
    }

    /**
     * @param idDroit
     *            the idDroit to set
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public final String getIdPrestation() {
        return idPrestation;
    }

    public final void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public String getLibelleBreakCode() {
        return libelleBreakCode;
    }

    public void setLibelleBreakCode(String libelleBreakCode) {
        this.libelleBreakCode = libelleBreakCode;
    }

}
