package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface dur la gestion de nombres d'assur�s par genre d'assurance
 * 
 * @author sau
 */
public interface IAFNombreAssures extends BIEntity {

    /**
     * Cl� de recherche par Affiliation Id
     */
    public static final String FIND_FOR_AFFILIATION_ID = "setForAffiliationId";

    /**
     * Ajoute l'enregistrement dans la DB.
     * 
     * @exception Exception
     *                si l'ajout a �chou�
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Renvoie un tableau d'objet representant des Nombre d'assur�<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br>
     * <br>
     * <br>
     * <br>
     * 100 enregistrements au max sont retourn�s<br>
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
     * Renvoie un tableau de nombre d'assur�<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br>
     * <br>
     * <br>
     * <br>
     * 100 enregistrements au max sont retourn�s<br>
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
     * Retourne l'id de l'affiliation li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return id de l'affiliation li�e
     */
    public java.lang.String getAffiliationId();

    /**
     * Retourne l'ann�e concern�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return ann�e concern�e
     */
    public java.lang.String getAnnee();

    /**
     * Retourne l'id de l'assurance li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return id de l'assurance li�e
     */
    public java.lang.String getAssuranceId();

    /**
     * Retourne le nombre d'assur�s Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le nombre d'assur�s
     */
    public java.lang.String getNbrAssures();

    /**
     * Retourne l'id de cet objet Date de cr�ation : (26.02.2003 09:54:44)
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
     *             si la recuperation a �chou�
     */
    public void retrieve(BITransaction transaction) throws Exception;

    /**
     * D�finit l'id de l'affiliation li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newAffiliationId
     *            id de l'affiliation li�e
     */
    public void setAffiliationId(java.lang.String newAffiliationId);

    /**
     * D�finit l'ann�e concern�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newAnnee
     *            ann�e concern�e
     */
    public void setAnnee(java.lang.String newAnnee);

    /**
     * D�finit l'id de l'assurance li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceId
     *            id de l'assurance li�e
     */
    public void setAssuranceId(java.lang.String newAssuranceId);

    /**
     * D�finit le nombre d'assur�s Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newNbrAssures
     *            le nombre d'assur�s
     */
    public void setNbrAssures(java.lang.String newNbrAssures);

    /**
     * D�finit l'id de cet objet Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newNbrAssuresId
     *            id de cet objet
     */
    public void setNbrAssuresId(java.lang.String newNbrAssuresId);

    /**
     * Mise � jour de l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction a utiliser
     * @throws Exception
     *             si la mise � jour a �chou�
     */
    public void update(BITransaction transaction) throws Exception;
}
