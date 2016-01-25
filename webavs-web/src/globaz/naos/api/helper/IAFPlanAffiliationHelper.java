package globaz.naos.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.naos.api.IAFPlanAffiliation;
import java.util.Hashtable;

/**
 * Classe helper d'une interface d'API
 * 
 * @author sau
 */
public class IAFPlanAffiliationHelper extends GlobazHelper implements IAFPlanAffiliation {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String[] METHODS_TO_LOAD = new String[] { "getAffiliationId", "getDomaineCourrier",
            "getDomaineRecouvrement", "getDomaineRemboursement", "getIdTiersFacturation", "getLibelle",
            "getLibelleFacture", "getPlanAffiliationId", "getTypeAdresse", "getBlocageEnvoi", "getInactif", "getIsAPI" };

    /**
     * Constructeur du type IAFPlanAffiliationHelper
     */
    public IAFPlanAffiliationHelper() {
        super("globaz.naos.db.planAffiliation.AFPlanAffiliation");
        setMethodsToLoad(IAFPlanAffiliationHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFPlanAffiliationHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IAFPlanAffiliationHelper(GlobazValueObject valueObject) {
        super(valueObject);
        setMethodsToLoad(IAFPlanAffiliationHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFPlanAffiliationHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IAFPlanAffiliationHelper(String implementationClassName) {
        super(implementationClassName);
        setMethodsToLoad(IAFPlanAffiliationHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * @see globaz.naos.api.IAFPlanAffiliation#find(java.util.Hashtable)
     */
    @Override
    public Object[] find(Hashtable params) throws Exception {
        Object[] result = null;
        result = _getArray("find", new Object[] { params });
        return result;
    }

    @Override
    public IAFPlanAffiliation[] findPlanAffiliation(Hashtable params) throws Exception {
        IAFPlanAffiliation[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new IAFPlanAffiliationHelper[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject plan = (GlobazValueObject) objResult[i];
                result[i] = new IAFPlanAffiliationHelper(plan);
            }
        }
        return result;
    }

    // **********************************************
    // Getter
    // **********************************************

    @Override
    public java.lang.String getAffiliationId() {
        return (java.lang.String) _getValueObject().getProperty("affiliationId");
    }

    @Override
    public Boolean getBlocageEnvoi() {
        return (java.lang.Boolean) _getValueObject().getProperty("blocageEnvoi");
    }

    @Override
    public java.lang.String getDomaineCourrier() {
        return (java.lang.String) _getValueObject().getProperty("domaineCourrier");
    }

    @Override
    public java.lang.String getDomaineRecouvrement() {
        return (java.lang.String) _getValueObject().getProperty("domaineRecouvrement");
    }

    @Override
    public java.lang.String getDomaineRemboursement() {
        return (java.lang.String) _getValueObject().getProperty("domaineRemboursement");
    }

    @Override
    public java.lang.String getIdTiersFacturation() {
        return (java.lang.String) _getValueObject().getProperty("idTiersFacturation");
    }

    @Override
    public Boolean getInactif() {
        return (java.lang.Boolean) _getValueObject().getProperty("inactif");
    }

    @Override
    public java.lang.String getLibelle() {
        return (java.lang.String) _getValueObject().getProperty("libelle");
    }

    @Override
    public java.lang.String getLibelleFacture() {
        return (java.lang.String) _getValueObject().getProperty("libelleFacture");
    }

    @Override
    public java.lang.String getPlanAffiliationId() {
        return (java.lang.String) _getValueObject().getProperty("planAffiliationId");
    }

    @Override
    public java.lang.String getTypeAdresse() {
        return (java.lang.String) _getValueObject().getProperty("typeAdresse");
    }

    // **********************************************
    // Setter
    // **********************************************

    @Override
    public void setAffiliationId(java.lang.String newAffiliationId) {
        _getValueObject().setProperty("affiliationId", newAffiliationId);
    }

    @Override
    public void setBlocageEnvoi(Boolean boolean1) {
        _getValueObject().setProperty("blocageEnvoi", boolean1);
    }

    @Override
    public void setDomaineCourrier(String string) {
        _getValueObject().setProperty("domaineCourrier", string);
    }

    @Override
    public void setDomaineRecouvrement(String string) {
        _getValueObject().setProperty("domaineRecouvrement", string);
    }

    @Override
    public void setDomaineRemboursement(String string) {
        _getValueObject().setProperty("domaineRemboursement", string);
    }

    @Override
    public void setIdTiersFacturation(String string) {
        _getValueObject().setProperty("idTiersFacturation", string);
    }

    @Override
    public void setInactif(Boolean boolean1) {
        _getValueObject().setProperty("inactif", boolean1);
    }

    @Override
    public void setLibelle(String string) {
        _getValueObject().setProperty("libelle", string);
    }

    @Override
    public void setLibelleFacture(String string) {
        _getValueObject().setProperty("libelleFacture", string);
    }

    @Override
    public void setPlanAffiliationId(java.lang.String newPlanAffiliationId) {
        _getValueObject().setProperty("planAffiliationId", newPlanAffiliationId);
    }

    @Override
    public void setTypeAdresse(String string) {
        _getValueObject().setProperty("typeAdresse", string);
    }
}
