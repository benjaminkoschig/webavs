package globaz.osiris.db.access.recouvrement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Représente un container de type EcheancePlan.
 * 
 * @author Arnaud Dostes, 31-mar-2005
 */
public class CAEcheancePlanManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String AND = " AND ";
    private static final String DATEEXIGIBILITE_ASC = "DATEEXIGIBILITE ASC";
    public static final String DESC = " DESC";
    private static final String ZERO = "0";

    private String beforeDateExigibilite = "";
    private String forDateEffective = "";
    private String forDateEffectiveIsNotNull = "";
    private String forDateEffectiveIsNull = "";
    private String forDateExigibilite = "";
    private String forDateRappelIsNotNull = "";
    private String forIdEcheancePlan = "";
    private String forIdPlanRecouvrement = "";
    private String forMontant = "";
    private String fromDateEffective = "";
    private String fromDateExigibilite = "";
    private String fromIdEcheancePlan = "";
    private String fromIdPlanRecouvrement = "";
    private String fromMontant = "";
    private String order = "";
    private String toDateExigibilite = "";
    private String toDateRappel = "";

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAEcheancePlan.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (!JadeStringUtil.isBlank(getOrder())) {
            return getOrder();
        } else {
            return "";
        }
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        StringBuffer sqlWhere = new StringBuffer("");
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForIdEcheancePlan())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_IDECHEANCEPLAN + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForIdEcheancePlan()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForIdPlanRecouvrement())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_IDPLANRECOUVREMENT + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForIdPlanRecouvrement()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForDateExigibilite())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_DATEEXIGIBILITE + "="
                            + this._dbWriteDateAMJ(statement.getTransaction(), getForDateExigibilite()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForDateEffective())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_DATEEFFECTIVE + "="
                            + this._dbWriteDateAMJ(statement.getTransaction(), getForDateEffective()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForDateEffectiveIsNull())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_DATEEFFECTIVE + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForDateEffectiveIsNull()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForDateEffectiveIsNotNull())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_DATEEFFECTIVE + "<>"
                            + this._dbWriteNumeric(statement.getTransaction(), getForDateEffectiveIsNotNull()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForDateRappelIsNotNull())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_DATERAPPEL + "<>"
                            + this._dbWriteNumeric(statement.getTransaction(), getForDateRappelIsNotNull()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForMontant())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_MONTANT + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForMontant()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromIdEcheancePlan())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_IDECHEANCEPLAN + ">="
                            + this._dbWriteNumeric(statement.getTransaction(), getFromIdEcheancePlan()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromIdPlanRecouvrement())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_IDPLANRECOUVREMENT + ">="
                            + this._dbWriteNumeric(statement.getTransaction(), getFromIdPlanRecouvrement()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromDateExigibilite())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_DATEEXIGIBILITE + ">="
                            + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateExigibilite()));
        }
        if (!JadeStringUtil.isBlank(getBeforeDateExigibilite())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_DATEEXIGIBILITE + "<"
                            + this._dbWriteDateAMJ(statement.getTransaction(), getBeforeDateExigibilite()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromDateEffective())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_DATEEFFECTIVE + ">="
                            + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateEffective()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromMontant())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_MONTANT + ">="
                            + this._dbWriteNumeric(statement.getTransaction(), getFromMontant()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getToDateExigibilite())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_DATEEXIGIBILITE + "<="
                            + this._dbWriteDateAMJ(statement.getTransaction(), getToDateExigibilite()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getToDateRappel())) {
            addCondition(
                    sqlWhere,
                    CAEcheancePlan.FIELD_DATERAPPEL + "<="
                            + this._dbWriteDateAMJ(statement.getTransaction(), getToDateRappel()));
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAEcheancePlan();
    }

    /**
     * Permet l'ajout d'une condition dans la clause WHERE. <br>
     * 
     * @param sqlWhere
     * @param condition
     *            à ajouter au where
     */
    protected void addCondition(StringBuffer sqlWhere, String condition) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(CAEcheancePlanManager.AND);
        }
        sqlWhere.append(condition);
    }

    /**
     * @return the beforeDateExigibilite
     */
    public String getBeforeDateExigibilite() {
        return beforeDateExigibilite;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForDateEffective() {
        return forDateEffective;
    }

    /**
     * @return the forDateEffectiveIsNotNull
     */
    public String getForDateEffectiveIsNotNull() {
        return forDateEffectiveIsNotNull;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForDateEffectiveIsNull() {
        return forDateEffectiveIsNull;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForDateExigibilite() {
        return forDateExigibilite;
    }

    /**
     * @return the forDateRappelIsNotNull
     */
    public String getForDateRappelIsNotNull() {
        return forDateRappelIsNotNull;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdEcheancePlan() {
        return forIdEcheancePlan;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdPlanRecouvrement() {
        return forIdPlanRecouvrement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForMontant() {
        return forMontant;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromDateEffective() {
        return fromDateEffective;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromDateExigibilite() {
        return fromDateExigibilite;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdEcheancePlan() {
        return fromIdEcheancePlan;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdPlanRecouvrement() {
        return fromIdPlanRecouvrement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromMontant() {
        return fromMontant;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getOrder() {
        return order;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getToDateExigibilite() {
        return toDateExigibilite;
    }

    /**
     * @return the toDateRappel
     */
    public String getToDateRappel() {
        return toDateRappel;
    }

    /**
     * Active le tri par date d'exigibilité.
     */
    public void orderByDateExigibiliteASC() {
        setOrder(CAEcheancePlanManager.DATEEXIGIBILITE_ASC);
    }

    /**
     * @param beforeDateExigibilite
     *            the beforeDateExigibilite to set
     */
    public void setBeforeDateExigibilite(String beforeDateExigibilite) {
        this.beforeDateExigibilite = beforeDateExigibilite;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForDateEffective(String string) {
        forDateEffective = string;
    }

    /**
     * Active la recherche avec la date à null.
     */
    public void setForDateEffectiveIsNotNull() {
        this.setForDateEffectiveIsNotNull(CAEcheancePlanManager.ZERO);
    }

    /**
     * @param forDateEffectiveIsNotNull
     *            the forDateEffectiveIsNotNull to set
     */
    public void setForDateEffectiveIsNotNull(String forDateEffectiveIsNotNull) {
        this.forDateEffectiveIsNotNull = forDateEffectiveIsNotNull;
    }

    /**
     * Active la recherche avec la date à null.
     */
    public void setForDateEffectiveIsNull() {
        this.setForDateEffectiveIsNull(CAEcheancePlanManager.ZERO);
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForDateEffectiveIsNull(String string) {
        forDateEffectiveIsNull = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForDateExigibilite(String string) {
        forDateExigibilite = string;
    }

    /**
     * Active la recherche avec la date à null.
     */
    public void setForDateRappelIsNotNull() {
        this.setForDateRappelIsNotNull(CAEcheancePlanManager.ZERO);
    }

    /**
     * @param forDateRappelIsNotNull
     *            the forDateRappelIsNotNull to set
     */
    public void setForDateRappelIsNotNull(String forDateRappelIsNotNull) {
        this.forDateRappelIsNotNull = forDateRappelIsNotNull;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdEcheancePlan(String string) {
        forIdEcheancePlan = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdPlanRecouvrement(String string) {
        forIdPlanRecouvrement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForMontant(String string) {
        forMontant = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromDateEffective(String string) {
        fromDateEffective = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromDateExigibilite(String string) {
        fromDateExigibilite = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdEcheancePlan(String string) {
        fromIdEcheancePlan = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdPlanRecouvrement(String string) {
        fromIdPlanRecouvrement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromMontant(String string) {
        fromMontant = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setOrder(String string) {
        order = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setToDateExigibilite(String string) {
        toDateExigibilite = string;
    }

    /**
     * @param toDateRappel
     *            the toDateRappel to set
     */
    public void setToDateRappel(String toDateRappel) {
        this.toDateRappel = toDateRappel;
    }
}
