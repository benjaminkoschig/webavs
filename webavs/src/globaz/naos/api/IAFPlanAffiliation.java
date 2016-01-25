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
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Renvoie un tableau d'objet representant des Plan d'affiliation<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br>
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
     * Renvoie un tableau de plans d'affiliations<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br>
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
     * @return true si le plan est bloqué et donc que les factures seront imprimées dans un lot séparé.
     */
    public Boolean getBlocageEnvoi();

    // **********************************************
    // Getter
    // **********************************************

    /**
     * Renvoie le domaine de courrier utilisé pour ce plan.
     * 
     * @return le domaine de courrier utilisé pour ce plan.
     */
    public java.lang.String getDomaineCourrier();

    /**
     * Renvoie le domaine de recouvrement utilisé pour ce plan.
     * 
     * @return le domaine de recouvrement utilisé pour ce plan.
     */
    public java.lang.String getDomaineRecouvrement();

    /**
     * Renvoie le domaine de remboursement utilisé pour ce plan.
     * 
     * @return le domaine de remboursement utilisé pour ce plan.
     */
    public java.lang.String getDomaineRemboursement();

    /**
     * Inutilisé.
     * 
     * @return l'identifiant d'un autre tiers pour l'adressage.
     */
    public java.lang.String getIdTiersFacturation();

    /**
     * Renvoie l'information sur le status du plan.
     * 
     * @return true si le plan est inactif et donc jamais pris en compte dans un traitement.<br>
     *         Les cotisations associées sont également ignorées.
     */
    public Boolean getInactif();

    /**
     * Renvoie le libellé du plan.
     * 
     * @return le libellé du plan.
     */
    public java.lang.String getLibelle();

    /**
     * Renvoie le libellé de la facture.
     * 
     * @return le libellé de la facture.
     */
    public java.lang.String getLibelleFacture();

    /**
     * Renvoie l'identifiant du plan.
     * 
     * @return l'identifiant du plan.
     */
    public java.lang.String getPlanAffiliationId();

    /**
     * Inutilisé.
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
     *             si la recuperation a échoué
     */
    public void retrieve(BITransaction transaction) throws Exception;

    /**
     * Définit l'id de l'affiliation.
     * 
     * @param idAffiliation
     *            l'id de l'affiliation.
     */
    public void setAffiliationId(String idAffiliation);

    // **********************************************
    // Setter
    // **********************************************

    /**
     * Définit si les factures doivent être imprimées dans un lot séparé.
     * 
     * @param blocage
     *            true pour bloquer le plan.
     */
    public void setBlocageEnvoi(Boolean blocage);

    /**
     * Définit le domaine de courrier.
     * 
     * @param le
     *            domaine de courrier.
     */
    public void setDomaineCourrier(String domaine);

    /**
     * Définit le domaine de recouvrement.
     * 
     * @param le
     *            domaine de recouvrement.
     */
    public void setDomaineRecouvrement(String domaine);

    /**
     * Définit le domaine de remboursement.
     * 
     * @param le
     *            domaine de remboursement.
     */
    public void setDomaineRemboursement(String domaine);

    /**
     * Pas utilisé
     * 
     * @param string
     */
    public void setIdTiersFacturation(String string);

    /**
     * Définit si le plan ainsi que les cotisations associées sont inactifs.
     * 
     * @param inactif
     *            true pour rendre le plan inactif.
     */
    public void setInactif(Boolean inactif);

    /**
     * Définit le libellé du plan.
     * 
     * @param libelle
     *            le libellé du plan
     */
    public void setLibelle(String libelle);

    /**
     * Définit le libellé de la facture.
     * 
     * @param libelle
     *            le libellé de la facture.
     */
    public void setLibelleFacture(String libelle);

    /**
     * Définit l'id du plan d'affiliation.
     * 
     * @param string
     *            l'id du plan d'affiliation.
     */
    public void setPlanAffiliationId(String idPlan);

    /**
     * Pas utilisé
     * 
     * @param string
     */
    public void setTypeAdresse(String string);

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
