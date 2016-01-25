package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Interface sur les plans de caisses
 * 
 * @author sau
 */
public interface IAFPlanCaisse extends BIEntity {

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
     * Renvoie le numéro de caisse à laquelle le plan appartient.
     * 
     * @return numéro de caisse à laquelle le plan appartient
     */
    public java.lang.String getAdministrationNo();

    /**
     * Renvoie le libellé du plan.
     * 
     * @return le libellé du plan
     */
    public java.lang.String getLibelle();

    /**
     * @return le numéro d'ordre.
     */
    public String getNumOrdre();

    /**
     * Renvoie l'id du plan.
     * 
     * @return id du plan
     */
    public java.lang.String getPlanCaisseId();

    /**
     * @return le type d'affiliation (colonne AFPLCAP.MSTTAF)
     */
    public String getTypeAffiliation();

    /**
     * @return le code système du type de plan.
     */
    public String getTypePlan();

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
     * Définit l'id tiers de la caisse à laquelle appartient ce plan.
     * 
     * @param string
     *            id tiers de la caisse à laquelle appartient ce plan
     */
    public void setIdTiers(String string);

    /**
     * Définit le libellé du plan.
     * 
     * @param string
     *            le libellé du plan
     */
    public void setLibelle(String string);

    /**
     * Définit le numéro d'ordre.
     * 
     * @param ordre
     */
    public void setNumOrdre(String ordre);

    /**
     * Définit l'id du plan.
     * 
     * @param string
     *            id du plan
     */
    public void setPlanCaisseId(String string);

    /**
     * Définit le type d'affiliation
     * 
     * @param typeAffiliation
     */
    public void setTypeAffiliation(String typeAffiliation);

    /**
     * Définit le type de plan.
     * 
     * @param type
     *            : Code système
     */
    public void setTypePlan(String type);

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
