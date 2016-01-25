package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface sur les particularit�s de l'affilaition
 * 
 * @author David Girardin
 */
public interface IAFParticulariteAffiliation extends BIEntity {

    /**
     * Cl� de recherche par Affiliation Id
     */
    public static final String FIND_FOR_AFFILIATION_ID = "setForAffiliationId";

    /**
     * Cl� de recherche par particularit�
     */
    public static final String FIND_FOR_PARTICULARITE_ID = "setForParticularite";

    /**
     * Ajoute l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si l'ajout a �chou�
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Renvoie un tableau d'objet representant des particularit�s d'affiliation<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br> <li>FIND_FOR_PARTICULARITE_ID<br>
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
     * Renvoie un tableau des particularit�s d'affiliation<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br> <li>FIND_FOR_PARTICULARITE_ID<br>
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
    public IAFParticulariteAffiliation[] findParticularites(Hashtable params) throws Exception;

    /**
     * Retourne l'id de l'affiliation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return l'id de l'affiliation
     */
    public java.lang.String getAffiliationId();

    /**
     * Retourne le champ num�rique associ� � la particularit� Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le champ num�rique associ� � la particularit�
     */
    public java.lang.String getChampNumerique();

    /**
     * Retourne la date de d�but de la particularit� Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return la date de d�but de la particularit�
     */
    public java.lang.String getDateDebut();

    /**
     * Retourne la date de fin de la particularit� Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return la date de fin de la particularit�
     */
    public java.lang.String getDateFin();

    /**
     * Retourne le code syst�me de la particularit� Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le code syst�me de la particularit�
     */
    public java.lang.String getParticularite();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a �chou�
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * Retourne l'id de l'affiliation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newAffiliationId
     *            l'id de l'affiliation
     */
    public void setAffiliationId(java.lang.String newAffiliationId);

    /**
     * D�finit la valeur num�rique associ�e � la particularit� Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newChampNumerique
     *            la valeur num�rique associ�e � la particularit�
     */
    public void setChampNumerique(java.lang.String newChampNumerique);

    /**
     * D�finit la date de fin de la particularit� * Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newDateDebut
     *            la date de fin de la particularit�
     */
    public void setDateDebut(java.lang.String newDateDebut);

    /**
     * D�finit la date de fin de la particularit� Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newDateFin
     *            la date de fin de la particularit�
     */
    public void setDateFin(java.lang.String newDateFin);

    /**
     * D�finit le code syst�me de la particularit� Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newParticularite
     *            le code syst�me de la particularit�
     */
    public void setParticularite(java.lang.String newParticularite);

    /**
     * D�finit l'id de l'objet Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newParticulariteId
     *            id de l'objet
     */
    public void setParticulariteId(java.lang.String newParticulariteId);

    /**
     * Mise � jour de l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction � utiliser ou null si non transactionnel
     * @throws Exception
     *             si la mise � jour a �chou�
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
