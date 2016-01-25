package globaz.naos.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.naos.api.IAFParticulariteAffiliation;
import java.util.Hashtable;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class IAFParticulariteAffiliationHelper extends GlobazHelper implements IAFParticulariteAffiliation {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String[] METHODS_TO_LOAD = new String[] { "getAffiliationId", "getChampAlphanumerique",
            "getChampNumerique", "getDateDebut", "getDateFin", "getParticularite", "getParticulariteId", "getIsAPI" };

    /**
     * Constructeur du type IAFAffiliationHelper
     */
    public IAFParticulariteAffiliationHelper() {
        super("globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation");
        setMethodsToLoad(IAFParticulariteAffiliationHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFAffiliationHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IAFParticulariteAffiliationHelper(GlobazValueObject valueObject) {
        super(valueObject);
        setMethodsToLoad(IAFParticulariteAffiliationHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFAffiliationHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IAFParticulariteAffiliationHelper(String implementationClassName) {
        super(implementationClassName);
        setMethodsToLoad(IAFParticulariteAffiliationHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.02.2003 13:50:13)
     */
    @Override
    public Object[] find(Hashtable params) throws Exception {
        Object[] result = null;
        result = _getArray("find", new Object[] { params });
        return result;
    }

    @Override
    public IAFParticulariteAffiliation[] findParticularites(Hashtable params) throws Exception {
        IAFParticulariteAffiliation[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new IAFParticulariteAffiliationHelper[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject particularite = (GlobazValueObject) objResult[i];
                result[i] = new IAFParticulariteAffiliationHelper(particularite);
            }
        }
        return result;
    }

    @Override
    public java.lang.String getAffiliationId() {
        return (java.lang.String) _getValueObject().getProperty("affiliationId");
    }

    public java.lang.String getChampAlphanumerique() {
        return (java.lang.String) _getValueObject().getProperty("champAlphanumerique");
    }

    @Override
    public java.lang.String getChampNumerique() {
        return (java.lang.String) _getValueObject().getProperty("champNumerique");
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
    public java.lang.String getParticularite() {
        return (java.lang.String) _getValueObject().getProperty("particularite");
    }

    public java.lang.String getParticulariteId() {
        return (java.lang.String) _getValueObject().getProperty("particulariteId");
    }

    @Override
    public void setAffiliationId(java.lang.String newAffiliationId) {
        _getValueObject().setProperty("affiliationId", newAffiliationId);
    }

    @Override
    public void setChampNumerique(java.lang.String newChampNumerique) {
        _getValueObject().setProperty("champNumerique", newChampNumerique);
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
    public void setParticularite(java.lang.String newParticularite) {
        _getValueObject().setProperty("particularite", newParticularite);
    }

    @Override
    public void setParticulariteId(java.lang.String newParticulariteId) {
        _getValueObject().setProperty("particulariteId", newParticulariteId);
    }

}
