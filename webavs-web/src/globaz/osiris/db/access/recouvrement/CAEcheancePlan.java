package globaz.osiris.db.access.recouvrement;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;

/**
 * Représente une entité de type EcheancePlan.
 * 
 * @author Arnaud Dostes, 31-mar-2005
 */
public class CAEcheancePlan extends CABEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_DATEEFFECTIVE = "DATEEFFECTIVE";
    public static final String FIELD_DATEEXIGIBILITE = "DATEEXIGIBILITE";
    public static final String FIELD_DATERAPPEL = "DATERAPPEL";
    public static final String FIELD_IDECHEANCEPLAN = "IDECHEANCEPLAN";
    public static final String FIELD_IDPLANRECOUVREMENT = "IDPLANRECOUVREMENT";

    public static final String FIELD_MONTANT = "MONTANT";
    public static final String FIELD_MONTANTPAYE = "MONTANTPAYE";
    protected static final String LABEL_DATE_EXIGIBILITE_INCORRECT = "DATE_EXIGIBILITE_INCORRECT";
    protected static final String LABEL_DATE_EXIGIBILITE_OBLIGATOIRE = "DATE_EXIGIBILITE_OBLIGATOIRE";
    private static final String LABEL_MONTANT_POSITIF = "MONTANT_POSITIF";
    protected static final String LABEL_PLAN_OBLIGATOIRE = "PLAN_OBLIGATOIRE";
    public static final String TABLE_NAME = "CAECHPP";
    private static final String ZERO = "0";

    private String dateEffective = "";
    private String dateExigibilite = "";
    private String dateRappel = "";
    private String idEcheancePlan = "";
    private String idPlanRecouvrement = "";
    private String montant = "";
    /** Montant du paiement */
    private String montantPaye = "";
    private CAPlanRecouvrement planRecouvrement = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        // si plan pas manuel, donc y'a des echeances
        if (!CAPlanRecouvrement.CS_ECHEANCE_MANUELLE.equals(getPlanRecouvrement().getIdTypeEcheance())) {
            // je vérifie si le plan est pas soldé
            CAEcheancePlanManager echeances = new CAEcheancePlanManager();
            echeances.setSession(getSession());
            echeances.setForIdPlanRecouvrement(getIdPlanRecouvrement());
            echeances.setForDateEffectiveIsNull();
            echeances.find(transaction);
            if ((echeances.size() == 0) && !CAPlanRecouvrement.CS_SOLDE.equals(getPlanRecouvrement().getIdEtat())) {
                // toutes les échéances sont soldées
                CAPlanRecouvrement plan = getPlanRecouvrement();
                plan.setIdEtat(CAPlanRecouvrement.CS_SOLDE);
                plan.update(transaction);
            }
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_autoInherits()
     */
    @Override
    protected boolean _autoInherits() {
        // Cette entité n'hérite pas d'une autre entité
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // Empêche la création si aucune section n'est couverte
        CACouvertureSectionManager manager = new CACouvertureSectionManager();
        manager.setSession(getSession());
        manager.setForIdPlanRecouvrement(getIdPlanRecouvrement());
        try {
            manager.find(transaction);
        } catch (Exception e) {
            _addError(transaction, e.toString());
        }
        if (manager.size() > 0) {
            setIdEcheancePlan(this._incCounter(transaction, CAEcheancePlan.ZERO, _getTableName()));
        } else {
            _addError(transaction, getSession().getLabel("AJOUT_ECHEANCE_IMPOSSIBLE"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        // Empêche la suppression si une ou plusieurs opérations comptables sont
        // associées
        if (isOperationAssociee()) {
            _addError(transaction, getSession().getLabel("SUPPR_ECHEANCE_IMPOSSIBLE"));
        }
        if (!JadeStringUtil.isEmpty(getDateEffective()) && !CAEcheancePlan.ZERO.equals(getDateEffective())) {
            _addError(transaction, getSession().getLabel("SUPPR_ECHEANCE_IMPOSSIBLE"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        // Empêche la modification si une ou plusieurs opérations comptables
        // sont associées
        if (isOperationAssociee()) {
            _addError(transaction, getSession().getLabel("MODIF_ECHEANCE_IMPOSSIBLE"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CAEcheancePlan.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idEcheancePlan = statement.dbReadNumeric(CAEcheancePlan.FIELD_IDECHEANCEPLAN);
        idPlanRecouvrement = statement.dbReadNumeric(CAEcheancePlan.FIELD_IDPLANRECOUVREMENT);
        dateExigibilite = statement.dbReadDateAMJ(CAEcheancePlan.FIELD_DATEEXIGIBILITE);
        dateEffective = statement.dbReadDateAMJ(CAEcheancePlan.FIELD_DATEEFFECTIVE);
        dateRappel = statement.dbReadDateAMJ(CAEcheancePlan.FIELD_DATERAPPEL);
        montant = statement.dbReadNumeric(CAEcheancePlan.FIELD_MONTANT, 2);
        montantPaye = statement.dbReadNumeric(CAEcheancePlan.FIELD_MONTANTPAYE, 2);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {
        // idPlanRecouvrement
        _propertyMandatory(statement.getTransaction(), getIdPlanRecouvrement(),
                getSession().getLabel(CAEcheancePlan.LABEL_PLAN_OBLIGATOIRE));
        getPlanRecouvrement(); // Charge le plan
        // dateExigibilite
        _propertyMandatory(statement.getTransaction(), getDateExigibilite(),
                getSession().getLabel(CAEcheancePlan.LABEL_DATE_EXIGIBILITE_OBLIGATOIRE));
        _checkDate(statement.getTransaction(), getDateExigibilite(),
                getSession().getLabel(CAEcheancePlan.LABEL_DATE_EXIGIBILITE_INCORRECT));
        // dateEffective
        _checkDate(statement.getTransaction(), getDateEffective(), getSession().getLabel("DATE_EFFECTIVE_INCORRECT"));
        // dateRappel
        _checkDate(statement.getTransaction(), getDateRappel(), getSession().getLabel("DATE_RAPPEL_INCORRECT"));
        // montant obligatoire sauf si c'est manuel
        if (!CAPlanRecouvrement.CS_ECHEANCE_MANUELLE.equals(getPlanRecouvrement().getIdTypeEcheance())) {
            _propertyMandatory(statement.getTransaction(), getMontant(), getSession().getLabel("MONTANT_OBLIGATOIRE"));
            try {
                if (Float.valueOf(JANumberFormatter.deQuote(getMontant())).floatValue() < 0) {
                    _addError(statement.getTransaction(), getSession().getLabel(CAEcheancePlan.LABEL_MONTANT_POSITIF));
                }
            } catch (Exception e) {
                _addError(statement.getTransaction(), getSession().getLabel(CAEcheancePlan.LABEL_MONTANT_POSITIF));
            }
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CAEcheancePlan.FIELD_IDECHEANCEPLAN,
                this._dbWriteNumeric(statement.getTransaction(), getIdEcheancePlan(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CAEcheancePlan.FIELD_IDECHEANCEPLAN,
                this._dbWriteNumeric(statement.getTransaction(), getIdEcheancePlan(), "idEcheancePlan"));
        statement.writeField(CAEcheancePlan.FIELD_IDPLANRECOUVREMENT,
                this._dbWriteNumeric(statement.getTransaction(), getIdPlanRecouvrement(), "idPlanRecouvrement"));
        statement.writeField(CAEcheancePlan.FIELD_DATEEXIGIBILITE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateExigibilite(), "dateExigibilite"));
        statement.writeField(CAEcheancePlan.FIELD_DATEEFFECTIVE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateEffective(), "dateEffective"));
        statement.writeField(CAEcheancePlan.FIELD_DATERAPPEL,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateRappel(), "dateRappel"));
        statement.writeField(CAEcheancePlan.FIELD_MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));
        statement.writeField(CAEcheancePlan.FIELD_MONTANTPAYE,
                this._dbWriteNumeric(statement.getTransaction(), getMontantPaye(), "montantPaye"));
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDateEffective() {
        return dateEffective;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDateExigibilite() {
        return dateExigibilite;
    }

    /**
     * @return the dateRappel
     */
    public String getDateRappel() {
        return dateRappel;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdEcheancePlan() {
        return idEcheancePlan;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the paiement
     */
    public String getMontantPaye() {
        return montantPaye;
    }

    /**
     * @param paiement
     *            the paiement to set
     */
    public String getMontantPayeNegate() {
        if (JadeStringUtil.isBlankOrZero(montantPaye)) {
            return "";
        } else {
            return (new BigDecimal(JANumberFormatter.deQuote(montantPaye))).negate().toString();
        }
    }

    /**
     * @return Le plan de recouvrement lié
     */
    public CAPlanRecouvrement getPlanRecouvrement() {
        planRecouvrement = (CAPlanRecouvrement) retrieveEntityByIdIfNeeded(null, getIdPlanRecouvrement(),
                CAPlanRecouvrement.class, planRecouvrement);
        return planRecouvrement;
    }

    /**
     * @return true si une ou plusieurs opérations comptables sont associées
     */
    public boolean isOperationAssociee() {
        // @TODO Rechercher les opérations comptables associées et retourner
        // true si il y en a
        return false;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDateEffective(String string) {
        dateEffective = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDateExigibilite(String string) {
        dateExigibilite = string;
    }

    /**
     * @param dateRappel
     *            the dateRappel to set
     */
    public void setDateRappel(String dateRappel) {
        this.dateRappel = dateRappel;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdEcheancePlan(String string) {
        idEcheancePlan = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdPlanRecouvrement(String string) {
        idPlanRecouvrement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setMontant(String string) {
        montant = string;
    }

    /**
     * @param paiement
     *            the paiement to set
     */
    public void setMontantPaye(String paiement) {
        if (JadeStringUtil.isBlankOrZero(paiement)) {
            montantPaye = CAEcheancePlan.ZERO;
        } else {
            montantPaye = (new BigDecimal(JANumberFormatter.deQuote(paiement))).negate().toString();
        }
    }

    /**
     * @param paiement
     *            the paiement to set
     */
    public void setMontantPayeNegatif(String paiement) {
        montantPaye = paiement;
    }

}
