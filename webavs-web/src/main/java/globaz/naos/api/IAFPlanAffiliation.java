package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface d'API
 * 
 * @author sau
 */
public interface IAFPlanAffiliation extends BIEntity {

    /**
     * Recherche par Affiliation Id
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
     * Renvoie un tableau d'objet representant des Plan d'affiliation<br>
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
     * Renvoie un tableau de plans d'affiliations<br>
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
    public IAFPlanAffiliation[] findPlanAffiliation(Hashtable params) throws Exception;

    /**
     * Renvoie l'identifiant de l'affiliation.
     * 
     * @return l'identifiant de l'affiliation.
     */
    public java.lang.String getAffiliationId();

    /**
     * Renvoie l'information sur le blocage du plan.
     * 
     * @return true si le plan est bloqu� et donc que les factures seront imprim�es dans un lot s�par�.
     */
    public Boolean getBlocageEnvoi();

    // **********************************************
    // Getter
    // **********************************************

    /**
     * Renvoie le domaine de courrier utilis� pour ce plan.
     * 
     * @return le domaine de courrier utilis� pour ce plan.
     */
    public java.lang.String getDomaineCourrier();

    /**
     * Renvoie le domaine de recouvrement utilis� pour ce plan.
     * 
     * @return le domaine de recouvrement utilis� pour ce plan.
     */
    public java.lang.String getDomaineRecouvrement();

    /**
     * Renvoie le domaine de remboursement utilis� pour ce plan.
     * 
     * @return le domaine de remboursement utilis� pour ce plan.
     */
    public java.lang.String getDomaineRemboursement();

    /**
     * Inutilis�.
     * 
     * @return l'identifiant d'un autre tiers pour l'adressage.
     */
    public java.lang.String getIdTiersFacturation();

    /**
     * Renvoie l'information sur le status du plan.
     * 
     * @return true si le plan est inactif et donc jamais pris en compte dans un traitement.<br>
     *         Les cotisations associ�es sont �galement ignor�es.
     */
    public Boolean getInactif();

    /**
     * Renvoie le libell� du plan.
     * 
     * @return le libell� du plan.
     */
    public java.lang.String getLibelle();

    /**
     * Renvoie le libell� de la facture.
     * 
     * @return le libell� de la facture.
     */
    public java.lang.String getLibelleFacture();

    /**
     * Renvoie l'identifiant du plan.
     * 
     * @return l'identifiant du plan.
     */
    public java.lang.String getPlanAffiliationId();

    /**
     * Inutilis�.
     * 
     * @return le type de l'adresse de courrier.
     */
    public java.lang.String getTypeAdresse();

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
     * D�finit l'id de l'affiliation.
     * 
     * @param idAffiliation
     *            l'id de l'affiliation.
     */
    public void setAffiliationId(String idAffiliation);

    // **********************************************
    // Setter
    // **********************************************

    /**
     * D�finit si les factures doivent �tre imprim�es dans un lot s�par�.
     * 
     * @param blocage
     *            true pour bloquer le plan.
     */
    public void setBlocageEnvoi(Boolean blocage);

    /**
     * D�finit le domaine de courrier.
     * 
     * @param le
     *            domaine de courrier.
     */
    public void setDomaineCourrier(String domaine);

    /**
     * D�finit le domaine de recouvrement.
     * 
     * @param le
     *            domaine de recouvrement.
     */
    public void setDomaineRecouvrement(String domaine);

    /**
     * D�finit le domaine de remboursement.
     * 
     * @param le
     *            domaine de remboursement.
     */
    public void setDomaineRemboursement(String domaine);

    /**
     * Pas utilis�
     * 
     * @param string
     */
    public void setIdTiersFacturation(String string);

    /**
     * D�finit si le plan ainsi que les cotisations associ�es sont inactifs.
     * 
     * @param inactif
     *            true pour rendre le plan inactif.
     */
    public void setInactif(Boolean inactif);

    /**
     * D�finit le libell� du plan.
     * 
     * @param libelle
     *            le libell� du plan
     */
    public void setLibelle(String libelle);

    /**
     * D�finit le libell� de la facture.
     * 
     * @param libelle
     *            le libell� de la facture.
     */
    public void setLibelleFacture(String libelle);

    /**
     * D�finit l'id du plan d'affiliation.
     * 
     * @param string
     *            l'id du plan d'affiliation.
     */
    public void setPlanAffiliationId(String idPlan);

    /**
     * Pas utilis�
     * 
     * @param string
     */
    public void setTypeAdresse(String string);

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
