package globaz.naos.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.naos.api.IAFSuiviCaisseAffiliation;
import java.util.Hashtable;

/**
 * Classe helper d'une interface d'API
 * 
 * @author dgi
 */
public class IAFSuiviCaisseAffiliationHelper extends GlobazHelper implements IAFSuiviCaisseAffiliation {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String[] METHODS_TO_LOAD = new String[] { "getSuiviCaisseId", "getAffiliationId", "getIdTiersCaisse",
            "getGenreCaisse", "getDateDebut", "getDateFin", "getMotif", "getAttestationIp", "getCategorieSalarie",
            "getNumeroAffileCaisse", "getIsAPI" };

    /**
     * Constructeur du type IAFSuiviCaisseAffiliationHelper
     */
    public IAFSuiviCaisseAffiliationHelper() {
        super("globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation");
        setMethodsToLoad(IAFSuiviCaisseAffiliationHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFSuiviCaisseAffiliationHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IAFSuiviCaisseAffiliationHelper(GlobazValueObject valueObject) {
        super(valueObject);
        setMethodsToLoad(IAFSuiviCaisseAffiliationHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFSuiviCaisseAffiliationHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IAFSuiviCaisseAffiliationHelper(String implementationClassName) {
        super(implementationClassName);
        setMethodsToLoad(IAFSuiviCaisseAffiliationHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * @see globaz.naos.api.IAFSuiviCaisseAffiliation#find(java.util.Hashtable)
     */
    @Override
    public Object[] find(Hashtable params) throws Exception {
        Object[] result = null;
        result = _getArray("find", new Object[] { params });
        return result;
    }

    @Override
    public IAFSuiviCaisseAffiliation[] findSuiviCaisse(Hashtable params) throws Exception {
        IAFSuiviCaisseAffiliation[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new IAFSuiviCaisseAffiliationHelper[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject suivi = (GlobazValueObject) objResult[i];
                result[i] = new IAFSuiviCaisseAffiliationHelper(suivi);
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
    public java.lang.String getCategorieSalarie() {
        return (java.lang.String) _getValueObject().getProperty("categorieSalarie");
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
    public java.lang.String getGenreCaisse() {
        return (java.lang.String) _getValueObject().getProperty("genreCaisse");
    }

    @Override
    public java.lang.String getIdTiersCaisse() {
        return (java.lang.String) _getValueObject().getProperty("idTiersCaisse");
    }

    @Override
    public java.lang.String getMotif() {
        return (java.lang.String) _getValueObject().getProperty("motif");
    }

    @Override
    public java.lang.String getNumeroAffileCaisse() {
        return (java.lang.String) _getValueObject().getProperty("numeroAffileCaisse");
    }

    @Override
    public java.lang.String getSuiviCaisseId() {
        return (java.lang.String) _getValueObject().getProperty("suiviCaisseId");
    }

    @Override
    public java.lang.Boolean isAttestationIp() {
        return (java.lang.Boolean) _getValueObject().getProperty("attestationIp");
    }

    // **********************************************
    // Setter
    // **********************************************

    @Override
    public void setAffiliationId(java.lang.String newAffiliationId) {
        _getValueObject().setProperty("affiliationId", newAffiliationId);
    }

    @Override
    public void setAttestationIp(java.lang.Boolean newAttestationIp) {
        _getValueObject().setProperty("attestationIp", newAttestationIp);
    }

    @Override
    public void setCategorieSalarie(java.lang.String newCategorieSalarie) {
        _getValueObject().setProperty("categorieSalarie", newCategorieSalarie);
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
    public void setGenreCaisse(java.lang.String newGenreCaisse) {
        _getValueObject().setProperty("genreCaisse", newGenreCaisse);
    }

    @Override
    public void setIdTiersCaisse(java.lang.String newIdTiersCaisse) {
        _getValueObject().setProperty("idTiersCaisse", newIdTiersCaisse);
    }

    @Override
    public void setMotif(java.lang.String newMotif) {
        _getValueObject().setProperty("motif", newMotif);
    }

    @Override
    public void setNumeroAffileCaisse(java.lang.String newNumeroAffileCaisse) {
        _getValueObject().setProperty("numeroAffileCaisse", newNumeroAffileCaisse);
    }

    @Override
    public void setSuiviCaisseId(java.lang.String newSuiviCaisseId) {
        _getValueObject().setProperty("suiviCaisseId", newSuiviCaisseId);
    }
}
