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
     *            la transaction � utiliser ou null si non transactionnel
     * @exception Exception
     *                si l'ajout a �chou�
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Renvoie le num�ro de caisse � laquelle le plan appartient.
     * 
     * @return num�ro de caisse � laquelle le plan appartient
     */
    public java.lang.String getAdministrationNo();

    /**
     * Renvoie le libell� du plan.
     * 
     * @return le libell� du plan
     */
    public java.lang.String getLibelle();

    /**
     * @return le num�ro d'ordre.
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
     * @return le code syst�me du type de plan.
     */
    public String getTypePlan();

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
     * D�finit l'id tiers de la caisse � laquelle appartient ce plan.
     * 
     * @param string
     *            id tiers de la caisse � laquelle appartient ce plan
     */
    public void setIdTiers(String string);

    /**
     * D�finit le libell� du plan.
     * 
     * @param string
     *            le libell� du plan
     */
    public void setLibelle(String string);

    /**
     * D�finit le num�ro d'ordre.
     * 
     * @param ordre
     */
    public void setNumOrdre(String ordre);

    /**
     * D�finit l'id du plan.
     * 
     * @param string
     *            id du plan
     */
    public void setPlanCaisseId(String string);

    /**
     * D�finit le type d'affiliation
     * 
     * @param typeAffiliation
     */
    public void setTypeAffiliation(String typeAffiliation);

    /**
     * D�finit le type de plan.
     * 
     * @param type
     *            : Code syst�me
     */
    public void setTypePlan(String type);

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
