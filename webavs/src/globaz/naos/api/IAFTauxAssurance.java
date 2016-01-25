package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface des taux d'assurance.
 * 
 * @author David Girardin, Stépahne Brand
 */
public interface IAFTauxAssurance extends BIEntity {

    /**
     * Clé de recherche par Assurance.
     */
    public static final String FIND_FOR_ASSURANCE_ID = "setForIdAssurance";
    /**
     * Clé de recherche par N° d'affilié
     */
    public static final String FIND_FOR_DATE = "setForDate";
    /**
     * Clé de tri sur la date de début pour la recherche
     */
    public static final String ORDER_BY_RANGE = "setOrderByRangDebutDesc";

    /**
     * Ajoute l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction à utiliser ou null si non transactionnel
     * @exception Exception
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Renvoie un tableau d'objet representant des taux d'assurance.<br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche dans la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_ASSURANCE_ID</li>
     * <li>FIND_FOR_DATE</li>
     * </ul>
     * </p>
     * 100 enregistrements au max. sont retournés.<br>
     * <br>
     * 
     * @return une liste des adhésions trouvées
     * @param params
     *            critères de recherche
     * @exception Exception
     *                si echec
     */
    public Object[] find(Hashtable params) throws Exception;

    /**
     * Renvoie un tableau de taux d'assurance.<br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche ds la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_ASSURANCE_ID</li>
     * <li>FIND_FOR_DATE</li>
     * </ul>
     * </p>
     * 100 enregistrements au max sont retournés.<br>
     * <br>
     * <br>
     * 
     * @return une liste des adhésions trouvées
     * @param params
     *            critères de recherche
     * @exception Exception
     *                si echec
     */
    public IAFTauxAssurance[] findTaux(Hashtable params) throws Exception;

    /**
     * Retourne l'id de l'assurnce liée
     * 
     * @return id de l'assurnce liée
     */
    public java.lang.String getAssuranceId();

    /**
     * Retourne la date de début du taux
     * 
     * @return la date de début du taux
     */
    public java.lang.String getDateDebut();

    /**
     * Retourne la fraction du taux spécifié (valeur=5 et fration=100 pour 5%)
     * 
     * @return la fraction du taux spécifié
     */
    public java.lang.String getFraction();

    /**
     * Retourne la fraction du taux de la partie employé
     * 
     * @return fraction du taux de la partie employé
     */
    public java.lang.String getFractionEmploye();

    /**
     * Retourne la fraction du taux de la partie employeur
     * 
     * @return fraction du taux de la partie employeur
     */
    public java.lang.String getFractionEmployeur();

    /**
     * Retourne le genre de la valeur (montant ou taux)
     * 
     * @return le genre de la valeur (montant ou taux)
     */
    public java.lang.String getGenreValeur();

    /**
     * Retourne la périodicité pour un montant donné (valeur = 6'000 et périodicité = semestriel pour une assurance de
     * 1'000/mois)
     * 
     * @return la périodicité pour un montant donné
     */
    public java.lang.String getPeriodiciteMontant();

    /**
     * Retourne le rang pour taux variables
     * 
     * @return rang pour taux variables
     */
    public java.lang.String getRang();

    /**
     * Retourne le sexe concerné pour le taux donné si spécifique ou vide si global
     * 
     * @return sexe concerné pour le taux donné si spécifique ou vide si global
     */
    public java.lang.String getSexe();

    /**
     * Retourne l'id de l'objet
     * 
     * @return id de l'objet
     */
    public java.lang.String getTauxAssuranceId();

    /**
     * Retourne la tranche suppérieur pour taux variables
     * 
     * @return tranche suppérieur pour taux variables
     */
    public java.lang.String getTranche();

    /**
     * Retourne la valeur de la partie employé
     * 
     * @return la valeur de la partie employé
     */
    public java.lang.String getValeurEmploye();

    /**
     * Retourne la valeur de la partie employeur
     * 
     * @return la valeur de la partie employeur
     */
    public java.lang.String getValeurEmployeur();

    /**
     * Récupère l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction à utiliser ou null si non transactionnel
     * @throws Exception
     *             si la récuperation a échoué
     */
    public void retrieve(BITransaction transaction) throws Exception;

    /**
     * Définit l'id de l'assurnce liée
     * 
     * @param newAssuranceId
     *            id de l'assurnce liée
     */
    public void setAssuranceId(java.lang.String newAssuranceId);

    /**
     * Définit la date de début du taux
     * 
     * @param newDateDebut
     *            la date de début du taux
     */
    public void setDateDebut(java.lang.String newDateDebut);

    /**
     * Définit la fraction du taux spécifié (valeur=5 et fration=100 pour 5%)
     * 
     * @param newFraction
     *            la fraction du taux spécifié
     */
    public void setFraction(java.lang.String newFraction);

    /**
     * Définit la fraction du taux de la partie employé
     * 
     * @param string
     *            fraction du taux de la partie employé
     */
    public void setFractionEmploye(java.lang.String string);

    /**
     * Définit la fraction du taux de la partie employeur
     * 
     * @param string
     *            fraction du taux de la partie employeur
     */
    public void setFractionEmployeur(java.lang.String string);

    /**
     * Définit le genre de la valeur (montant ou taux)
     * 
     * @param string
     *            le genre de la valeur (montant ou taux)
     */
    public void setGenreValeur(java.lang.String string);

    /**
     * Définit la périodicité pour un montant donné (valeur = 6'000 et périodicité = semestriel pour une assurance de
     * 1'000/mois)
     * 
     * @param newPeriodiciteMontant
     *            la périodicité pour un montant donné
     */
    public void setPeriodiciteMontant(java.lang.String newPeriodiciteMontant);

    /**
     * Définit le rang pour taux variables
     * 
     * @param string
     *            rang pour taux variables
     */
    public void setRang(java.lang.String string);

    /**
     * Définit le sexe concerné pour le taux donné si spécifique ou vide si global
     * 
     * @param string
     *            sexe concerné pour le taux donné si spécifique ou vide si global
     */
    public void setSexe(java.lang.String string);

    /**
     * Définit l'id de l'objet
     * 
     * @param newTauxAssuranceId
     *            id de l'objet
     */
    public void setTauxAssuranceId(java.lang.String newTauxAssuranceId);

    /**
     * Définit la tranche suppérieur pour taux variables
     * 
     * @param string
     *            tranche suppérieur pour taux variables
     */
    public void setTranche(java.lang.String string);

    /**
     * Définit la valeur de la partie employé
     * 
     * @param string
     *            la valeur de la partie employé
     */
    public void setValeurEmploye(java.lang.String string);

    /**
     * Définit la valeur de la partie employeur
     * 
     * @param string
     *            la valeur de la partie employeur
     */
    public void setValeurEmployeur(java.lang.String string);

    /**
     * Mise à jour de l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction à utiliser ou null si non transactionnel
     * @throws Exception
     *             si la mise é jour a échoué
     */
    public void update(BITransaction transaction) throws Exception;

}
