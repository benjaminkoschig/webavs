package globaz.naos.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.naos.api.IAFPlanCaisse;

/**
 * Classe helper d'une interface d'API
 * 
 * @author sau
 */
public class IAFPlanCaisseHelper extends GlobazHelper implements IAFPlanCaisse {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String[] METHODS_TO_LOAD = new String[] { "getPlanCaisseId", "getLibelle", "getAdministrationNo",
            "getIsAPI", "getIdTiers" };

    /**
     * Constructeur du type IAFPlanCaisseHelper
     */
    public IAFPlanCaisseHelper() {
        super("globaz.naos.db.planCaisse.AFPlanCaisse");
        setMethodsToLoad(IAFPlanCaisseHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFPlanCaisseHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IAFPlanCaisseHelper(GlobazValueObject valueObject) {
        super(valueObject);
        setMethodsToLoad(IAFPlanCaisseHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFPlanCaisseHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IAFPlanCaisseHelper(String implementationClassName) {
        super(implementationClassName);
        setMethodsToLoad(IAFPlanCaisseHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    // **********************************************
    // Getter
    // **********************************************

    @Override
    public java.lang.String getAdministrationNo() {
        return (java.lang.String) _getValueObject().getProperty("administrationNo");
    }

    public java.lang.String getIdTiers() {
        return (java.lang.String) _getValueObject().getProperty("idTiers");
    }

    @Override
    public java.lang.String getLibelle() {
        return (java.lang.String) _getValueObject().getProperty("libelle");
    }

    @Override
    public String getNumOrdre() {
        return (String) _getValueObject().getProperty("numOrdre");
    }

    // **********************************************
    // Setter
    // **********************************************

    @Override
    public java.lang.String getPlanCaisseId() {
        return (java.lang.String) _getValueObject().getProperty("planCaisseId");
    }

    @Override
    public String getTypeAffiliation() {
        return (java.lang.String) _getValueObject().getProperty("typeAffiliation");
    }

    @Override
    public String getTypePlan() {
        return (String) _getValueObject().getProperty("typePlan");
    }

    @Override
    public void setIdTiers(String string) {
        _getValueObject().setProperty("idTiers", string);
    }

    @Override
    public void setLibelle(String string) {
        _getValueObject().setProperty("libelle", string);
    }

    @Override
    public void setNumOrdre(String ordre) {
        _getValueObject().setProperty("numOrdre", ordre);
    }

    @Override
    public void setPlanCaisseId(String string) {
        _getValueObject().setProperty("planCaisseId", string);
    }

    @Override
    public void setTypeAffiliation(String typeAffiliation) {
        _getValueObject().setProperty("typeAffiliation", typeAffiliation);
    }

    @Override
    public void setTypePlan(String type) {
        _getValueObject().setProperty("typePlan", type);
    }
}
