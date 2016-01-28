package globaz.phenix.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.phenix.api.ICPDonneesCalcul;
import java.util.Hashtable;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class ICPDonneesCalculHelper extends GlobazHelper implements ICPDonneesCalcul {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type ICPDecisionHelper
     */
    public ICPDonneesCalculHelper() {
        super("globaz.phenix.db.principale.CPDonneesCalcul");
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type ICPDecisionHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public ICPDonneesCalculHelper(GlobazValueObject valueObject) {
        super(valueObject);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type ICPDecisionHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public ICPDonneesCalculHelper(java.lang.String implementationClassName) {
        super(implementationClassName);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Renvoie un tableau d'objet representant des decisions<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_ANNEE_DECISION <li>FIND_FOR_IS_ACTIVE <li>
     * FIND_FOR_ID_AFFILIATION <br>
     * <br>
     * <br>
     * 100 enregistrements au max sont retournés<br>
     * <br>
     * <br>
     * 
     * @return Object[]
     * @param params
     *            params
     * @exception Exception
     *                si echec
     */
    @Override
    public Object[] find(Hashtable params) throws Exception {
        Object[] result = null;
        result = _getArray("find", new Object[] { params });
        return result;
    }

    /**
     * Idem méthode Object[] find(Hashtable), mais retourne un tableau typé.
     * 
     * @return ICPDecision[]
     * @param params
     *            params
     * @exception Exception
     *                si echec
     */
    @Override
    public ICPDonneesCalcul[] findDonneesCalcul(Hashtable params) throws Exception {
        ICPDonneesCalcul[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new ICPDonneesCalcul[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject obj = (GlobazValueObject) objResult[i];
                result[i] = new ICPDonneesCalculHelper(obj);
                result[i].setISession(getISession());
            }
        }
        return result;
    }

    // Getter
    @Override
    public String getIdDonneesCalcul() {
        return (java.lang.String) _getValueObject().getProperty("idDonneesCalcul");
    }

    @Override
    public String getLibellePeriodicite() {
        return (java.lang.String) _getValueObject().getProperty("libellePeriodicite");
    }

    @Override
    public String getMontant() {
        return (java.lang.String) _getValueObject().getProperty("montant");
    }

    @Override
    public String getPeriodicite() {
        return (java.lang.String) _getValueObject().getProperty("periodicite");
    }

    // Setter
    @Override
    public void setIdDecision(String newIdDecision) {
        _getValueObject().setProperty("idDecision", newIdDecision);
    }

    @Override
    public void setIdDonneesCalcul(String newIdDonneesCalcul) {
        _getValueObject().setProperty("idDonneesCalcul", newIdDonneesCalcul);
    }

    @Override
    public void setMontant(String newMontant) {
        _getValueObject().setProperty("montant", newMontant);
    }

    @Override
    public void setPeriodicite(String periodicite) {
        _getValueObject().setProperty("periodicite", periodicite);
    }
}
