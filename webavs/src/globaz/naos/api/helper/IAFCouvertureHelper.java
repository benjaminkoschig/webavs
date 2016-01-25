package globaz.naos.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.naos.api.IAFCouverture;
import java.util.Hashtable;

/**
 * Classe helper d'une interface d'API
 * 
 * @author sau
 */
public class IAFCouvertureHelper extends GlobazHelper implements IAFCouverture {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String[] METHODS_TO_LOAD = new String[] { "getAssuranceId", "getCouvertureId", "getDateDebut",
            "getDateFin", "getPlanCaisseId", "getIsAPI" };

    /**
     * Constructeur du type IAFCouvertureHelper
     */
    public IAFCouvertureHelper() {
        super("globaz.naos.db.couverture.AFCouverture");
        setMethodsToLoad(IAFCouvertureHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFCouvertureHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IAFCouvertureHelper(GlobazValueObject valueObject) {
        super(valueObject);
        setMethodsToLoad(IAFCouvertureHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFCouvertureHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IAFCouvertureHelper(String implementationClassName) {
        super(implementationClassName);
        setMethodsToLoad(IAFCouvertureHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * @see globaz.naos.api.IAFCouverture#find(java.util.Hashtable)
     */
    @Override
    public Object[] find(Hashtable params) throws Exception {
        Object[] result = null;
        result = _getArray("find", new Object[] { params });
        return result;
    }

    @Override
    public IAFCouverture[] findCouvertures(Hashtable params) throws Exception {
        IAFCouverture[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new IAFCouvertureHelper[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject adhesion = (GlobazValueObject) objResult[i];
                result[i] = new IAFCouvertureHelper(adhesion);
            }
        }
        return result;
    }

    // **********************************************
    // Getter
    // **********************************************

    @Override
    public java.lang.String getAssuranceId() {
        return (java.lang.String) _getValueObject().getProperty("assuranceId");
    }

    @Override
    public java.lang.String getCouvertureId() {
        return (java.lang.String) _getValueObject().getProperty("couvertureId");
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

    // **********************************************
    // Setter
    // **********************************************

    @Override
    public void setAssuranceId(java.lang.String newAssuranceId) {
        _getValueObject().setProperty("assuranceId", newAssuranceId);
    }

    @Override
    public void setCouvertureId(java.lang.String newCouvertureId) {
        _getValueObject().setProperty("couvertureId", newCouvertureId);
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
}
