package globaz.naos.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.naos.api.IAFAdhesion;
import java.util.Hashtable;

/**
 * Classe helper d'une interface d'API
 * 
 * @author sau
 */
public class IAFAdhesionHelper extends GlobazHelper implements IAFAdhesion {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String[] METHODS_TO_LOAD = new String[] { "getAffiliationId", "getAdhesionId", "getDateDebut",
            "getDateFin", "getPlanCaisseId", "getTypeAdhesion", "getTypeAdhesionLibelle", "getIsAPI",
            "getAdministrationCaisseLibelle", "getAdministrationCaisseId", "getAdministrationCaisseCode",
            "getTypeAdhesion" };

    /**
     * Constructeur du type IAFAdhesionHelper
     */
    public IAFAdhesionHelper() {
        super("globaz.naos.db.adhesion.AFAdhesion");
        setMethodsToLoad(IAFAdhesionHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFAdhesionHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IAFAdhesionHelper(GlobazValueObject valueObject) {
        super(valueObject);
        setMethodsToLoad(IAFAdhesionHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFAdhesionHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IAFAdhesionHelper(String implementationClassName) {
        super(implementationClassName);
        setMethodsToLoad(IAFAdhesionHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * @see globaz.naos.api.IAFAdhesion#find(java.util.Hashtable)
     */
    @Override
    public Object[] find(Hashtable params) throws Exception {
        Object[] result = null;
        result = _getArray("find", new Object[] { params });
        return result;
    }

    @Override
    public IAFAdhesion[] findAdhesions(Hashtable params) throws Exception {
        IAFAdhesion[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new IAFAdhesionHelper[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject adhesion = (GlobazValueObject) objResult[i];
                result[i] = new IAFAdhesionHelper(adhesion);
            }
        }
        return result;
    }

    // **********************************************
    // Getter
    // **********************************************

    @Override
    public java.lang.String getAdhesionId() {
        return (java.lang.String) _getValueObject().getProperty("adhesionId");
    }

    @Override
    public String getAdministrationCaisseCode() {
        return (String) _getValueObject().getProperty("administrationCaisseCode");
    }

    @Override
    public String getAdministrationCaisseId() {
        return (String) _getValueObject().getProperty("administrationCaisseId");
    }

    @Override
    public String getAdministrationCaisseLibelle() {
        return (String) _getValueObject().getProperty("administrationCaisseLibelle");
    }

    @Override
    public java.lang.String getAffiliationId() {
        return (java.lang.String) _getValueObject().getProperty("affiliationId");
    }

    @Override
    public java.lang.String getDateDebut() {
        return (java.lang.String) _getValueObject().getProperty("dateDebut");
    }

    @Override
    public java.lang.String getDateFin() {
        return (java.lang.String) _getValueObject().getProperty("dateFin");
    }

    @Override
    public java.lang.String getPlanCaisseId() {
        return (java.lang.String) _getValueObject().getProperty("planCaisseId");
    }

    @Override
    public java.lang.String getTypeAdhesion() {
        return (java.lang.String) _getValueObject().getProperty("typeAdhesion");
    }

    @Override
    public java.lang.String getTypeAdhesionLibelle() {
        return (java.lang.String) _getValueObject().getProperty("typeAdhesionLibelle");
    }

    // **********************************************
    // Setter
    // **********************************************

    @Override
    public void setAdhesionId(java.lang.String newAdhesionId) {
        _getValueObject().setProperty("adhesionId", newAdhesionId);
    }

    @Override
    public void setAffiliationId(java.lang.String newAffiliationId) {
        _getValueObject().setProperty("affiliationId", newAffiliationId);
    }

    @Override
    public void setDateDebut(java.lang.String newDateDebut) {
        _getValueObject().setProperty("dateDebut", newDateDebut);
    }

    @Override
    public void setDateFin(java.lang.String newDateFin) {
        _getValueObject().setProperty("dateFin", newDateFin);
    }

    @Override
    public void setPlanCaisseId(java.lang.String newPlanCaisseId) {
        _getValueObject().setProperty("planCaisseId", newPlanCaisseId);
    }

    @Override
    public void setTypeAdhesion(java.lang.String newTypeAdhesion) {
        _getValueObject().setProperty("typeAdhesion", newTypeAdhesion);
    }

}
