package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface dur la gestion de nombres d'assurés par genre d'assurance
 * 
 * @author sau
 */
public interface IAFNombreAssures extends BIEntity {

    /**
     * Clé de recherche par Affiliation Id
     */
    public static final String FIND_FOR_AFFILIATION_ID = "setForAffiliationId";

    /**
     * Ajoute l'enregistrement dans la DB.
     * 
     * @exception Exception
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Renvoie un tableau d'objet representant des Nombre d'assuré<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br>
     * <br>
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
     * Renvoie un tableau de nombre d'assuré<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br>
     * <br>
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
    public IAFNombreAssures[] findNombreAssures(Hashtable params) throws Exception;

    /**
     * Retourne l'id de l'affiliation liée Date de création : (26.02.2003 09:54:44)
     * 
     * @return id de l'affiliation liée
     */
    public java.lang.String getAffiliationId();

    /**
     * Retourne l'année concernée Date de création : (26.02.2003 09:54:44)
     * 
     * @return année concernée
     */
    public java.lang.String getAnnee();

    /**
     * Retourne l'id de l'assurance liée Date de création : (26.02.2003 09:54:44)
     * 
     * @return id de l'assurance liée
     */
    public java.lang.String getAssuranceId();

    /**
     * Retourne le nombre d'assurés Date de création : (26.02.2003 09:54:44)
     * 
     * @return le nombre d'assurés
     */
    public java.lang.String getNbrAssures();

    /**
     * Retourne l'id de cet objet Date de création : (26.02.2003 09:54:44)
     * 
     * @return id de cet objet
     */
    public java.lang.String getNbrAssuresId();

    /**
     * Recuperer l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction a utiliser
     * @throws Exception
     *             si la recuperation a échoué
     */
    public void retrieve(BITransaction transaction) throws Exception;

    /**
     * Définit l'id de l'affiliation liée Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAffiliationId
     *            id de l'affiliation liée
     */
    public void setAffiliationId(java.lang.String newAffiliationId);

    /**
     * Définit l'année concernée Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAnnee
     *            année concernée
     */
    public void setAnnee(java.lang.String newAnnee);

    /**
     * Définit l'id de l'assurance liée Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceId
     *            id de l'assurance liée
     */
    public void setAssuranceId(java.lang.String newAssuranceId);

    /**
     * Définit le nombre d'assurés Date de création : (26.02.2003 09:54:44)
     * 
     * @param newNbrAssures
     *            le nombre d'assurés
     */
    public void setNbrAssures(java.lang.String newNbrAssures);

    /**
     * Définit l'id de cet objet Date de création : (26.02.2003 09:54:44)
     * 
     * @param newNbrAssuresId
     *            id de cet objet
     */
    public void setNbrAssuresId(java.lang.String newNbrAssuresId);

    /**
     * Mise à jour de l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction a utiliser
     * @throws Exception
     *             si la mise é jour a échoué
     */
    public void update(BITransaction transaction) throws Exception;
}
