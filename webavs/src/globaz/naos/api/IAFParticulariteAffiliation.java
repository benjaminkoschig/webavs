package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface sur les particularités de l'affilaition
 * 
 * @author David Girardin
 */
public interface IAFParticulariteAffiliation extends BIEntity {

    /**
     * Clé de recherche par Affiliation Id
     */
    public static final String FIND_FOR_AFFILIATION_ID = "setForAffiliationId";

    /**
     * Clé de recherche par particularité
     */
    public static final String FIND_FOR_PARTICULARITE_ID = "setForParticularite";

    /**
     * Ajoute l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Renvoie un tableau d'objet representant des particularités d'affiliation<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br> <li>FIND_FOR_PARTICULARITE_ID<br>
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
     * Renvoie un tableau des particularités d'affiliation<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br> <li>FIND_FOR_PARTICULARITE_ID<br>
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
    public IAFParticulariteAffiliation[] findParticularites(Hashtable params) throws Exception;

    /**
     * Retourne l'id de l'affiliation Date de création : (26.02.2003 09:54:44)
     * 
     * @return l'id de l'affiliation
     */
    public java.lang.String getAffiliationId();

    /**
     * Retourne le champ numérique associé à la particularité Date de création : (26.02.2003 09:54:44)
     * 
     * @return le champ numérique associé à la particularité
     */
    public java.lang.String getChampNumerique();

    /**
     * Retourne la date de début de la particularité Date de création : (26.02.2003 09:54:44)
     * 
     * @return la date de début de la particularité
     */
    public java.lang.String getDateDebut();

    /**
     * Retourne la date de fin de la particularité Date de création : (26.02.2003 09:54:44)
     * 
     * @return la date de fin de la particularité
     */
    public java.lang.String getDateFin();

    /**
     * Retourne le code système de la particularité Date de création : (26.02.2003 09:54:44)
     * 
     * @return le code système de la particularité
     */
    public java.lang.String getParticularite();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * Retourne l'id de l'affiliation Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAffiliationId
     *            l'id de l'affiliation
     */
    public void setAffiliationId(java.lang.String newAffiliationId);

    /**
     * Définit la valeur numérique associée à la particularité Date de création : (26.02.2003 09:54:44)
     * 
     * @param newChampNumerique
     *            la valeur numérique associée à la particularité
     */
    public void setChampNumerique(java.lang.String newChampNumerique);

    /**
     * Définit la date de fin de la particularité * Date de création : (26.02.2003 09:54:44)
     * 
     * @param newDateDebut
     *            la date de fin de la particularité
     */
    public void setDateDebut(java.lang.String newDateDebut);

    /**
     * Définit la date de fin de la particularité Date de création : (26.02.2003 09:54:44)
     * 
     * @param newDateFin
     *            la date de fin de la particularité
     */
    public void setDateFin(java.lang.String newDateFin);

    /**
     * Définit le code système de la particularité Date de création : (26.02.2003 09:54:44)
     * 
     * @param newParticularite
     *            le code système de la particularité
     */
    public void setParticularite(java.lang.String newParticularite);

    /**
     * Définit l'id de l'objet Date de création : (26.02.2003 09:54:44)
     * 
     * @param newParticulariteId
     *            id de l'objet
     */
    public void setParticulariteId(java.lang.String newParticulariteId);

    /**
     * Mise à jour de l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction à utiliser ou null si non transactionnel
     * @throws Exception
     *             si la mise é jour a échoué
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
