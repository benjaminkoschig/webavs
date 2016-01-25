package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface sur les liaisons d'affiliation
 * 
 * @author sau
 */
public interface IAFLienAffiliation extends BIEntity {

    /**
     * Clé de recherche par Affiliation Id
     */
    public static final String FIND_FOR_AFFILIATION_ID = "setForAffiliationId";

    /**
     * Clé de recherche par Affiliation Id lié
     */
    public static final String FIND_FOR_AFFILIATION_ID_LIEN = "setForAff_AffiliationId";

    /**
     * Ajoute l'enregistrement dans la DB.
     * 
     * @exception Exception
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Renvoie un tableau d'objet representant des Lien d'affiliation<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br> <li>FIND_FOR_AFFILIATION_ID_LIEN<br>
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
     * Renvoie un tableau de liens d'affiliation<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br> <li>FIND_FOR_AFFILIATION_ID_LIEN<br>
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
    public IAFLienAffiliation[] findLiaisons(Hashtable params) throws Exception;

    /**
     * Retourne l'id de l'affiliation liée à celle-ci, idem que getAffiliationLieeId() Date de création : (26.02.2003
     * 09:54:44)
     * 
     * @return id de l'affiliation liée à celle-ci
     */
    public java.lang.String getAff_AffiliationId();

    /**
     * Retourne l'id de l'affiliation Date de création : (26.02.2003 09:54:44)
     * 
     * @return id de l'affiliation
     */
    public java.lang.String getAffiliationId();

    /**
     * Retourne l'id de l'affiliation liée à celle-ci Date de création : (26.02.2003 09:54:44)
     * 
     * @return id de l'affiliation liée à celle-ci
     */
    public java.lang.String getAffiliationLieeId();

    /**
     * Retourne la date de début de la liaison Date de création : (26.02.2003 09:54:44)
     * 
     * @return la date de début de la liaison
     */
    public java.lang.String getDateDebut();

    /**
     * Retourne la date de fin de la liaison Date de création : (26.02.2003 09:54:44)
     * 
     * @return la date de fin de la liaison
     */
    public java.lang.String getDateFin();

    /**
     * Retourne l'id de l'objet liaison Date de création : (26.02.2003 09:54:44)
     * 
     * @return id de l'objet liaison
     */
    public java.lang.String getLienAffiliationId();

    /**
     * Retourne le type de lien Date de création : (26.02.2003 09:54:44)
     * 
     * @return le type de lien
     */
    public java.lang.String getTypeLien();

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
     * Définit l'id de l'affiliation liée à celle-ci, idem que setAffiliationLieeId() Date de création : (26.02.2003
     * 09:54:44)
     * 
     * @param newAffiliationLieeId
     *            id de l'affiliation liée à celle-ci
     */
    public void setAff_AffiliationId(java.lang.String newAffiliationLieeId);

    /**
     * Définit l'id de l'affiliation Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAffiliationId
     *            id de l'affiliation
     */
    public void setAffiliationId(java.lang.String newAffiliationId);

    /**
     * Définit l'id de l'affiliation liée à celle-ci Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAffiliationLieeId
     *            id de l'affiliation liée à celle-ci
     */
    public void setAffiliationLieeId(java.lang.String newAffiliationLieeId);

    /**
     * Définit la date de début de la liaison Date de création : (26.02.2003 09:54:44)
     * 
     * @param newDateDebut
     *            la date de début de la liaison
     */
    public void setDateDebut(java.lang.String newDateDebut);

    /**
     * Définit la date de fin de la liaison Date de création : (26.02.2003 09:54:44)
     * 
     * @param newDateFin
     *            la date de fin de la liaison
     */
    public void setDateFin(java.lang.String newDateFin);

    /**
     * Définit l'id de l'objet liaison Date de création : (26.02.2003 09:54:44)
     * 
     * @param newLienAffiliationId
     *            id de l'objet liaison
     */
    public void setLienAffiliationId(java.lang.String newLienAffiliationId);

    /**
     * Définit le type de lien Date de création : (26.02.2003 09:54:44)
     * 
     * @param newTypeLien
     *            le type de lien
     */
    public void setTypeLien(java.lang.String newTypeLien);

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
