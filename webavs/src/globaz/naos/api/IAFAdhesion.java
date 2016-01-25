package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface des adhésion d'affiliation.
 * 
 * @author David Girardin
 */
public interface IAFAdhesion extends BIEntity {

    /**
     * Type d'adhésion "Plan de caisse"
     */
    public static final String ADHESION_CAISSE = "824001";
    /**
     * Type d'adhésion "Caisse principale (unique)"
     */
    public static final String ADHESION_CAISSE_PRINCIPALE = "824003";

    /**
     * Clé de recherche par Affiliation Id.
     */
    public static final String FIND_FOR_AFFILIATION_ID = "setForAffiliationId";

    public static final String FIND_FOR_TYPE_ADHESION = "setForTypeAdhesion";

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
     * Renvoie un tableau d'objet representant des Adhésions.<br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche dans la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_AFFILIATION_ID</li>
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
     * Renvoie un tableau d'adhésions.<br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche ds la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_AFFILIATION_ID</li>
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
    public IAFAdhesion[] findAdhesions(Hashtable params) throws Exception;

    /**
     * Renvoie l'id de l'adhésion.
     * 
     * @return id de l'adhésion
     */
    public java.lang.String getAdhesionId();

    /**
     * Return le code du tiers administration.
     * 
     * @return Le code else null si aucune administration.
     */
    public String getAdministrationCaisseCode();

    /**
     * Return l'id du tiers administration.
     * 
     * @return L'id else null si aucune administration.
     */
    public String getAdministrationCaisseId();

    /**
     * Return le libelle du tiers administration (getNom()).
     * 
     * @return Le libelle else null si aucune administration.
     */
    public String getAdministrationCaisseLibelle();

    /**
     * Renvoie l'id de l'affiliation.
     * 
     * @return id de l'affiliation
     */
    public java.lang.String getAffiliationId();

    /**
     * Renvoie la date de début d'adhésion.
     * 
     * @return la date de début d'adhésion au format jj.mm.aaaa ou vide si indéfini
     */
    public java.lang.String getDateDebut();

    /**
     * Renvoie la date de fin d'adhésion.
     * 
     * @return Renvoie la date de fin d'adhésion au format jj.mm.aaaa ou vide si indéfini
     */
    public java.lang.String getDateFin();

    /**
     * Renvoie l'id du plan de caisse auquel l'affilié adhère.
     * 
     * @return id du plan de caisse auquel l'affilié adhère
     */
    public java.lang.String getPlanCaisseId();

    /**
     * Renvoie le type d'adhésion. Se référer aux constantes {@link #ADHESION_CAISSE types d'adhésion}
     * 
     * @return le type d'adhésion
     */
    public java.lang.String getTypeAdhesion();

    /**
     * Renvoie le libellé du type d'adhésion.
     * 
     * @return le libellé du type d'adhésion
     */
    public java.lang.String getTypeAdhesionLibelle();

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
     * Définit l'id de l'adhésion.
     * 
     * @param newAdhesionId
     *            l'id de l'adhésion
     */
    public void setAdhesionId(java.lang.String newAdhesionId);

    /**
     * Définit l'id de l'affiliation.
     * 
     * @param newAffiliationId
     *            l'id de l'affiliation
     */
    public void setAffiliationId(java.lang.String newAffiliationId);

    /**
     * Définit la date de début d'adhésion.
     * 
     * @param newDateDebut
     *            la date de début d'adhésion avec le format jj.mm.aaaa
     */
    public void setDateDebut(java.lang.String newDateDebut);

    /**
     * Définit la date de fin de l'adhésion.
     * 
     * @param newDateFin
     *            la date de fin de l'adhésion avec le format jj.mm.aaaa
     */
    public void setDateFin(java.lang.String newDateFin);

    /**
     * Définit l'id du plan caisse.
     * 
     * @param newPlanCaisseId
     *            l'id du plan caisse
     */
    public void setPlanCaisseId(java.lang.String newPlanCaisseId);

    /**
     * Définit le type d'adhésion. Se référer aux constantes {@link #ADHESION_CAISSE types d'adhésion}
     * 
     * @param newTypeAdhesion
     *            le type d'adhésion
     */
    public void setTypeAdhesion(java.lang.String newTypeAdhesion);

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
