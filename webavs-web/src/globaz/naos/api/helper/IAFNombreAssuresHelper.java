package globaz.naos.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.naos.api.IAFNombreAssures;
import java.util.Hashtable;

/**
 * Classe helper d'une interface d'API
 * 
 * @author sau
 */
public class IAFNombreAssuresHelper extends GlobazHelper implements IAFNombreAssures {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String[] METHODS_TO_LOAD = new String[] { "getAffiliationId", "getAnnee", "getAssuranceId",
            "getNbrAssures", "getIsAPI" };

    /**
     * Constructeur du type IAFNombreAssuresHelper
     */
    public IAFNombreAssuresHelper() {
        super("globaz.naos.db.nombreAssures.AFNombreAssures");
        setMethodsToLoad(IAFNombreAssuresHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFNombreAssuresHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IAFNombreAssuresHelper(GlobazValueObject valueObject) {
        super(valueObject);
        setMethodsToLoad(IAFNombreAssuresHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFNombreAssuresHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IAFNombreAssuresHelper(String implementationClassName) {
        super(implementationClassName);
        setMethodsToLoad(IAFNombreAssuresHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * @see globaz.naos.api.IAFNombreAssures#find(java.util.Hashtable)
     */
    @Override
    public Object[] find(Hashtable params) throws Exception {
        Object[] result = null;
        result = _getArray("find", new Object[] { params });
        return result;
    }

    @Override
    public IAFNombreAssures[] findNombreAssures(Hashtable params) throws Exception {
        IAFNombreAssures[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new IAFNombreAssuresHelper[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject nombre = (GlobazValueObject) objResult[i];
                result[i] = new IAFNombreAssuresHelper(nombre);
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
    public java.lang.String getAnnee() {
        return (java.lang.String) _getValueObject().getProperty("annee");
    }

    @Override
    public java.lang.String getAssuranceId() {
        return (java.lang.String) _getValueObject().getProperty("assuranceId");
    }

    @Override
    public java.lang.String getNbrAssures() {
        return (java.lang.String) _getValueObject().getProperty("nbrAssures");
    }

    @Override
    public java.lang.String getNbrAssuresId() {
        return (java.lang.String) _getValueObject().getProperty("nbrAssuresId");
    }

    // **********************************************
    // Setter
    // **********************************************

    @Override
    public void setAffiliationId(java.lang.String newAffiliationId) {
        _getValueObject().setProperty("affiliationId", newAffiliationId);
    }

    @Override
    public void setAnnee(java.lang.String newAnnee) {
        _getValueObject().setProperty("annee", newAnnee);
    }

    @Override
    public void setAssuranceId(java.lang.String newAssuranceId) {
        _getValueObject().setProperty("assuranceId", newAssuranceId);
    }

    @Override
    public void setNbrAssures(java.lang.String newNbrAssures) {
        _getValueObject().setProperty("nbrAssures", newNbrAssures);
    }

    @Override
    public void setNbrAssuresId(java.lang.String newNbrAssuresId) {
        _getValueObject().setProperty("nbrAssuresId", newNbrAssuresId);
    }
}
