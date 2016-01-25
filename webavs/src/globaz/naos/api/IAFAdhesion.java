package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface des adh�sion d'affiliation.
 * 
 * @author David Girardin
 */
public interface IAFAdhesion extends BIEntity {

    /**
     * Type d'adh�sion "Plan de caisse"
     */
    public static final String ADHESION_CAISSE = "824001";
    /**
     * Type d'adh�sion "Caisse principale (unique)"
     */
    public static final String ADHESION_CAISSE_PRINCIPALE = "824003";

    /**
     * Cl� de recherche par Affiliation Id.
     */
    public static final String FIND_FOR_AFFILIATION_ID = "setForAffiliationId";

    public static final String FIND_FOR_TYPE_ADHESION = "setForTypeAdhesion";

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
     * Renvoie un tableau d'objet representant des Adh�sions.<br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche dans la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_AFFILIATION_ID</li>
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
     * Renvoie un tableau d'adh�sions.<br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche ds la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_AFFILIATION_ID</li>
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
    public IAFAdhesion[] findAdhesions(Hashtable params) throws Exception;

    /**
     * Renvoie l'id de l'adh�sion.
     * 
     * @return id de l'adh�sion
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
     * Renvoie la date de d�but d'adh�sion.
     * 
     * @return la date de d�but d'adh�sion au format jj.mm.aaaa ou vide si ind�fini
     */
    public java.lang.String getDateDebut();

    /**
     * Renvoie la date de fin d'adh�sion.
     * 
     * @return Renvoie la date de fin d'adh�sion au format jj.mm.aaaa ou vide si ind�fini
     */
    public java.lang.String getDateFin();

    /**
     * Renvoie l'id du plan de caisse auquel l'affili� adh�re.
     * 
     * @return id du plan de caisse auquel l'affili� adh�re
     */
    public java.lang.String getPlanCaisseId();

    /**
     * Renvoie le type d'adh�sion. Se r�f�rer aux constantes {@link #ADHESION_CAISSE types d'adh�sion}
     * 
     * @return le type d'adh�sion
     */
    public java.lang.String getTypeAdhesion();

    /**
     * Renvoie le libell� du type d'adh�sion.
     * 
     * @return le libell� du type d'adh�sion
     */
    public java.lang.String getTypeAdhesionLibelle();

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
     * D�finit l'id de l'adh�sion.
     * 
     * @param newAdhesionId
     *            l'id de l'adh�sion
     */
    public void setAdhesionId(java.lang.String newAdhesionId);

    /**
     * D�finit l'id de l'affiliation.
     * 
     * @param newAffiliationId
     *            l'id de l'affiliation
     */
    public void setAffiliationId(java.lang.String newAffiliationId);

    /**
     * D�finit la date de d�but d'adh�sion.
     * 
     * @param newDateDebut
     *            la date de d�but d'adh�sion avec le format jj.mm.aaaa
     */
    public void setDateDebut(java.lang.String newDateDebut);

    /**
     * D�finit la date de fin de l'adh�sion.
     * 
     * @param newDateFin
     *            la date de fin de l'adh�sion avec le format jj.mm.aaaa
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
     * D�finit le type d'adh�sion. Se r�f�rer aux constantes {@link #ADHESION_CAISSE types d'adh�sion}
     * 
     * @param newTypeAdhesion
     *            le type d'adh�sion
     */
    public void setTypeAdhesion(java.lang.String newTypeAdhesion);

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
