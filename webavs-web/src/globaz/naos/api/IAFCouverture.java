package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface des adh�sion d'affiliation.
 * 
 * @author David Girardin
 */
public interface IAFCouverture extends BIEntity {

    /**
     * Cl� de recherche par plan de caisse.
     */
    public static final String FIND_FOR_PLAN_CAISSE_ID = "setForPlanCaisseId";

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
     * Renvoie un tableau d'objet representant des Couvertures.<br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche dans la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_PLAN_CAISSE_ID</li>
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
     * Renvoie un tableau de couvertures.<br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche ds la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_PLAN_CAISSE_IDD</li>
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
    public IAFCouverture[] findCouvertures(Hashtable params) throws Exception;

    /**
     * Renvoie l'id de l'assurance concern�e.
     * 
     * @return id de l'assurance concern�e
     */
    public java.lang.String getAssuranceId();

    /**
     * Renvoie l'id de la couverture
     * 
     * @return id de la couverture
     */
    public java.lang.String getCouvertureId();

    /**
     * Renvoie la date de d�but de la couverture.
     * 
     * @return la date de d�but de la couverture au format jj.mm.aaaa ou vide si ind�fini
     */
    public java.lang.String getDateDebut();

    /**
     * Renvoie la date de fin de couverture.
     * 
     * @return Renvoie la date de fin de couverture au format jj.mm.aaaa ou vide si ind�fini
     */
    public java.lang.String getDateFin();

    /**
     * Renvoie l'id du plan de caisse concern�.
     * 
     * @return id du plan de caisse concern�
     */
    public java.lang.String getPlanCaisseId();

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
     * D�finit l'id de l'assurance.
     * 
     * @param newAssuranceId
     *            l'id de l'assurance
     */
    public void setAssuranceId(java.lang.String newAssuranceId);

    /**
     * D�finit l'id de la couverture.
     * 
     * @param newCouvertureId
     *            l'id de la couverture
     */
    public void setCouvertureId(java.lang.String newCouvertureId);

    /**
     * D�finit la date de d�but de couverture.
     * 
     * @param newDateDebut
     *            la date de d�but de couverture avec le format jj.mm.aaaa
     */
    public void setDateDebut(java.lang.String newDateDebut);

    /**
     * D�finit la date de fin de couverture.
     * 
     * @param newDateFin
     *            la date de fin de de couverture avec le format jj.mm.aaaa
     */
    public void setDateFin(java.lang.String newDateFin);

    /**
     * D�finit l'id du plan caisse.
     * 
     * @param newPlanCaisseId
     *            l'id du plan caisse
     */
    public void setPlanCaisseId(java.lang.String newPlanCaisseId);

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
