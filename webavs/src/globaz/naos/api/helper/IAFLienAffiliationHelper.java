package globaz.naos.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.naos.api.IAFLienAffiliation;
import java.util.Hashtable;

/**
 * Classe helper d'une interface d'API
 * 
 * @author sau
 */
public class IAFLienAffiliationHelper extends GlobazHelper implements IAFLienAffiliation {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String[] METHODS_TO_LOAD = new String[] { "getAffiliationId", "getDateDebut", "getDateFin",
            "getLienAffiliationId", "getAff_AffiliationId", "getTypeLien", "getIsAPI" };

    /**
     * Constructeur du type IAFLienAffiliationHelper
     */
    public IAFLienAffiliationHelper() {
        super("globaz.naos.db.lienAffiliation.AFLienAffiliation");
        setMethodsToLoad(IAFLienAffiliationHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFLienAffiliationHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IAFLienAffiliationHelper(GlobazValueObject valueObject) {
        super(valueObject);
        setMethodsToLoad(IAFLienAffiliationHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFLienAffiliationHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IAFLienAffiliationHelper(String implementationClassName) {
        super(implementationClassName);
        setMethodsToLoad(IAFLienAffiliationHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * @see globaz.naos.api.IAFLienAffiliation#find(java.util.Hashtable)
     */
    @Override
    public Object[] find(Hashtable params) throws Exception {
        Object[] result = null;
        result = _getArray("find", new Object[] { params });
        return result;
    }

    @Override
    public IAFLienAffiliation[] findLiaisons(Hashtable params) throws Exception {
        IAFLienAffiliation[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new IAFLienAffiliationHelper[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject lien = (GlobazValueObject) objResult[i];
                result[i] = new IAFLienAffiliationHelper(lien);
            }
        }
        return result;
    }

    // **********************************************
    // Getter
    // **********************************************

    @Override
    public java.lang.String getAff_AffiliationId() {
        return (java.lang.String) _getValueObject().getProperty("aff_AffiliationId");
    }

    @Override
    public java.lang.String getAffiliationId() {
        return (java.lang.String) _getValueObject().getProperty("affiliationId");
    }

    @Override
    public java.lang.String getAffiliationLieeId() {
        return getAff_AffiliationId();
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
    public java.lang.String getLienAffiliationId() {
        return (java.lang.String) _getValueObject().getProperty("lienAffiliationId");
    }

    @Override
    public java.lang.String getTypeLien() {
        return (java.lang.String) _getValueObject().getProperty("typeLien");
    }

    /*
     * public FWParametersSystemCode getCsTypeLien() { return
     * (FWParametersSystemCode)_getValueObject().getProperty("csTypeLien"); }
     */

    // **********************************************
    // Setter
    // **********************************************

    @Override
    public void setAff_AffiliationId(java.lang.String newAffiliationLieeId) {
        _getValueObject().setProperty("aff_AffiliationId", newAffiliationLieeId);
    }

    @Override
    public void setAffiliationId(java.lang.String newAffiliationId) {
        _getValueObject().setProperty("affiliationId", newAffiliationId);
    }

    @Override
    public void setAffiliationLieeId(java.lang.String newAffiliationLieeId) {
        setAff_AffiliationId(newAffiliationLieeId);
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
    public void setLienAffiliationId(java.lang.String newLienAffiliationId) {
        _getValueObject().setProperty("lienAffiliationId", newLienAffiliationId);
    }

    @Override
    public void setTypeLien(java.lang.String newTypeLien) {
        _getValueObject().setProperty("typeLien", newTypeLien);
    }
}
