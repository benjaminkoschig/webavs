package globaz.phenix.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface d'API
 * 
 * @author EFLCreateAPITool
 */
public interface ICPDonneesCalcul extends BIEntity {

    public final static String CS_REV_CI = "600021";

    public final static String CS_REV_NET = "600019";
    /** Recherche pour une decision */
    public final java.lang.String FIND_FOR_ID_DECISION = "setForIdDecision";
    /** Recherche pour un type de donnees calcul */
    public final java.lang.String FIND_FOR_ID_DONNEES_CALCUL = "setForIdDonneesCalcul";

    /**
     * Ajoute l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Supprime l'enregistrement de la BD
     * 
     * @exception java.lang.Exception
     *                si la suppression a échouée
     */
    public void delete(BITransaction transaction) throws java.lang.Exception;

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
    public Object[] find(Hashtable params) throws Exception;

    /**
     * Idem méthode Object[] find(Hashtable), mais retourne un tableau typé.
     * 
     * @return ICPDecision[]
     * @param params
     *            params
     * @exception Exception
     *                si echec
     */
    public ICPDonneesCalcul[] findDonneesCalcul(Hashtable params) throws Exception;

    // Getter
    public String getIdDonneesCalcul();

    public String getLibellePeriodicite();

    public String getMontant();

    public String getPeriodicite();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    // Setter
    public void setIdDecision(String newIdDecision);

    public void setIdDonneesCalcul(String newIdDonneesCalcul);

    public void setMontant(String newMontant);

    public void setPeriodicite(String periodicite);

    /**
     * Met à jour l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si la mise à jour a échouée
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
