package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface des taux d'assurance.
 * 
 * @author David Girardin, St�pahne Brand
 */
public interface IAFTauxAssurance extends BIEntity {

    /**
     * Cl� de recherche par Assurance.
     */
    public static final String FIND_FOR_ASSURANCE_ID = "setForIdAssurance";
    /**
     * Cl� de recherche par N� d'affili�
     */
    public static final String FIND_FOR_DATE = "setForDate";
    /**
     * Cl� de tri sur la date de d�but pour la recherche
     */
    public static final String ORDER_BY_RANGE = "setOrderByRangDebutDesc";

    /**
     * Ajoute l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction � utiliser ou null si non transactionnel
     * @exception Exception
     *                si l'ajout a �chou�
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Renvoie un tableau d'objet representant des taux d'assurance.<br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche dans la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_ASSURANCE_ID</li>
     * <li>FIND_FOR_DATE</li>
     * </ul>
     * </p>
     * 100 enregistrements au max. sont retourn�s.<br>
     * <br>
     * 
     * @return une liste des adh�sions trouv�es
     * @param params
     *            crit�res de recherche
     * @exception Exception
     *                si echec
     */
    public Object[] find(Hashtable params) throws Exception;

    /**
     * Renvoie un tableau de taux d'assurance.<br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche ds la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_ASSURANCE_ID</li>
     * <li>FIND_FOR_DATE</li>
     * </ul>
     * </p>
     * 100 enregistrements au max sont retourn�s.<br>
     * <br>
     * <br>
     * 
     * @return une liste des adh�sions trouv�es
     * @param params
     *            crit�res de recherche
     * @exception Exception
     *                si echec
     */
    public IAFTauxAssurance[] findTaux(Hashtable params) throws Exception;

    /**
     * Retourne l'id de l'assurnce li�e
     * 
     * @return id de l'assurnce li�e
     */
    public java.lang.String getAssuranceId();

    /**
     * Retourne la date de d�but du taux
     * 
     * @return la date de d�but du taux
     */
    public java.lang.String getDateDebut();

    /**
     * Retourne la fraction du taux sp�cifi� (valeur=5 et fration=100 pour 5%)
     * 
     * @return la fraction du taux sp�cifi�
     */
    public java.lang.String getFraction();

    /**
     * Retourne la fraction du taux de la partie employ�
     * 
     * @return fraction du taux de la partie employ�
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
     * Retourne la p�riodicit� pour un montant donn� (valeur = 6'000 et p�riodicit� = semestriel pour une assurance de
     * 1'000/mois)
     * 
     * @return la p�riodicit� pour un montant donn�
     */
    public java.lang.String getPeriodiciteMontant();

    /**
     * Retourne le rang pour taux variables
     * 
     * @return rang pour taux variables
     */
    public java.lang.String getRang();

    /**
     * Retourne le sexe concern� pour le taux donn� si sp�cifique ou vide si global
     * 
     * @return sexe concern� pour le taux donn� si sp�cifique ou vide si global
     */
    public java.lang.String getSexe();

    /**
     * Retourne l'id de l'objet
     * 
     * @return id de l'objet
     */
    public java.lang.String getTauxAssuranceId();

    /**
     * Retourne la tranche supp�rieur pour taux variables
     * 
     * @return tranche supp�rieur pour taux variables
     */
    public java.lang.String getTranche();

    /**
     * Retourne la valeur de la partie employ�
     * 
     * @return la valeur de la partie employ�
     */
    public java.lang.String getValeurEmploye();

    /**
     * Retourne la valeur de la partie employeur
     * 
     * @return la valeur de la partie employeur
     */
    public java.lang.String getValeurEmployeur();

    /**
     * R�cup�re l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction � utiliser ou null si non transactionnel
     * @throws Exception
     *             si la r�cuperation a �chou�
     */
    public void retrieve(BITransaction transaction) throws Exception;

    /**
     * D�finit l'id de l'assurnce li�e
     * 
     * @param newAssuranceId
     *            id de l'assurnce li�e
     */
    public void setAssuranceId(java.lang.String newAssuranceId);

    /**
     * D�finit la date de d�but du taux
     * 
     * @param newDateDebut
     *            la date de d�but du taux
     */
    public void setDateDebut(java.lang.String newDateDebut);

    /**
     * D�finit la fraction du taux sp�cifi� (valeur=5 et fration=100 pour 5%)
     * 
     * @param newFraction
     *            la fraction du taux sp�cifi�
     */
    public void setFraction(java.lang.String newFraction);

    /**
     * D�finit la fraction du taux de la partie employ�
     * 
     * @param string
     *            fraction du taux de la partie employ�
     */
    public void setFractionEmploye(java.lang.String string);

    /**
     * D�finit la fraction du taux de la partie employeur
     * 
     * @param string
     *            fraction du taux de la partie employeur
     */
    public void setFractionEmployeur(java.lang.String string);

    /**
     * D�finit le genre de la valeur (montant ou taux)
     * 
     * @param string
     *            le genre de la valeur (montant ou taux)
     */
    public void setGenreValeur(java.lang.String string);

    /**
     * D�finit la p�riodicit� pour un montant donn� (valeur = 6'000 et p�riodicit� = semestriel pour une assurance de
     * 1'000/mois)
     * 
     * @param newPeriodiciteMontant
     *            la p�riodicit� pour un montant donn�
     */
    public void setPeriodiciteMontant(java.lang.String newPeriodiciteMontant);

    /**
     * D�finit le rang pour taux variables
     * 
     * @param string
     *            rang pour taux variables
     */
    public void setRang(java.lang.String string);

    /**
     * D�finit le sexe concern� pour le taux donn� si sp�cifique ou vide si global
     * 
     * @param string
     *            sexe concern� pour le taux donn� si sp�cifique ou vide si global
     */
    public void setSexe(java.lang.String string);

    /**
     * D�finit l'id de l'objet
     * 
     * @param newTauxAssuranceId
     *            id de l'objet
     */
    public void setTauxAssuranceId(java.lang.String newTauxAssuranceId);

    /**
     * D�finit la tranche supp�rieur pour taux variables
     * 
     * @param string
     *            tranche supp�rieur pour taux variables
     */
    public void setTranche(java.lang.String string);

    /**
     * D�finit la valeur de la partie employ�
     * 
     * @param string
     *            la valeur de la partie employ�
     */
    public void setValeurEmploye(java.lang.String string);

    /**
     * D�finit la valeur de la partie employeur
     * 
     * @param string
     *            la valeur de la partie employeur
     */
    public void setValeurEmployeur(java.lang.String string);

    /**
     * Mise � jour de l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction � utiliser ou null si non transactionnel
     * @throws Exception
     *             si la mise � jour a �chou�
     */
    public void update(BITransaction transaction) throws Exception;

}
