package globaz.phenix.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface d'API
 * 
 * @author EFLCreateAPITool
 */
public interface ICPDecision extends BIEntity {

    /** Recherche pour une annee */
    public final java.lang.String FIND_FOR_ANNEE_DECISION = "setForAnneeDecision";

    /** Recherche pour une affiliation */
    public final java.lang.String FIND_FOR_ID_AFFILIATION = "setForIdAffiliation";

    /** Recherche decisions actives */
    public final java.lang.String FIND_FOR_IS_ACTIVE = "setForIsActive";

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
    public ICPDecision[] findDecisions(Hashtable params) throws Exception;

    // Getter
    public java.lang.String getAnneeDecision();

    public java.lang.String getAnneePrise();

    public java.lang.String getDateInformation();

    public java.lang.String getDebutDecision();

    public java.lang.String getDescriptionDecision();

    public java.lang.String getFinDecision();

    public java.lang.String getGenreAffilie();

    public java.lang.String getIdAffiliation();

    public java.lang.String getIdCommunication();

    public java.lang.String getIdConjoint();

    public java.lang.String getIdDecision();

    public java.lang.String getIdIfdDefinitif();

    public java.lang.String getIdIfdProvisoire();

    public java.lang.String getIdPassage();

    public java.lang.String getIdTiers();

    public java.lang.String getNumIfd();

    public java.lang.String getPeriodicite();

    public java.lang.String getResponsable();

    public java.lang.String getTaxation();

    public java.lang.String getTypeDecision();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    // Setter
    public void setAnneeDecision(java.lang.String newAnneeDecision);

    public void setAnneePrise(java.lang.String newAnneePrise);

    public void setAssujetissement(Boolean newAssujetissement);

    public void setBloque(Boolean newBloque);

    public void setDateInformation(java.lang.String newDateInformation);

    public void setDebutDecision(java.lang.String newDebutDecision);

    public void setFacturation(Boolean newFacturation);

    public void setFinDecision(java.lang.String newFinDecision);

    public void setGenreAffilie(java.lang.String newGenreAffilie);

    public void setIdAffiliation(java.lang.String newIdAffiliation);

    public void setIdCommunication(java.lang.String newIdCommunication);

    public void setIdConjoint(java.lang.String newIdConjoint);

    public void setIdDecision(java.lang.String newIdDecision);

    public void setIdIfdDefinitif(java.lang.String newIdIfdDefinitif);

    public void setIdIfdProvisoire(java.lang.String newIdIfdProvisoire);

    public void setIdPassage(java.lang.String newIdPassage);

    public void setIdTiers(java.lang.String newIdTiers);

    public void setImpression(Boolean newImpression);

    public void setInteret(Boolean newInteret);

    public void setOpposition(Boolean newOpposition);

    public void setResponsable(java.lang.String newResponsable);

    public void setTaxation(java.lang.String newTaxation);

    public void setTypeDecision(java.lang.String newTypeDecision);

    /**
     * Met à jour l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si la mise à jour a échouée
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
