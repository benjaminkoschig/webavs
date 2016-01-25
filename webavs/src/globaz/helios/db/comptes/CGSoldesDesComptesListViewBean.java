package globaz.helios.db.comptes;

import globaz.helios.translation.CodeSystem;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (26.11.2002 12:24:25)
 * 
 * @author: Administrator
 */
public class CGSoldesDesComptesListViewBean extends CGPlanComptableManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean groupIdCompteOfas = false;

    /**
     * Commentaire relatif au constructeur CGSoldesDesComptesListViewBean.
     */
    public CGSoldesDesComptesListViewBean() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.11.2002 12:50:53)
     * 
     * @return java.lang.String
     */
    public java.lang.String getAvoir(int pos) {
        CGPlanComptableViewBean entity = (CGPlanComptableViewBean) getEntity(pos);
        if (CodeSystem.CS_DEFINITIF.equals(getReqComptabilite())) {
            return entity.getAvoir();
        } else {
            return entity.getAvoirProvisoire();
        }
    }

    public java.lang.String getAvoirMonnaie(int pos) {
        CGPlanComptableViewBean entity = (CGPlanComptableViewBean) getEntity(pos);
        if (CodeSystem.CS_DEFINITIF.equals(getReqComptabilite())) {
            return entity.getAvoirMonnaie();
        } else {
            return entity.getAvoirProvisoireMonnaie();
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.11.2002 12:50:25)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDoit(int pos) {
        CGPlanComptableViewBean entity = (CGPlanComptableViewBean) getEntity(pos);
        if (CodeSystem.CS_DEFINITIF.equals(getReqComptabilite())) {
            return entity.getDoit();
        } else {
            return entity.getDoitProvisoire();
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.11.2002 12:50:25)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDoitMonnaie(int pos) {
        CGPlanComptableViewBean entity = (CGPlanComptableViewBean) getEntity(pos);
        if (CodeSystem.CS_DEFINITIF.equals(getReqComptabilite())) {
            return entity.getDoitMonnaie();
        } else {
            return entity.getDoitProvisoireMonnaie();
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.11.2002 12:50:25)
     * 
     * @return java.lang.String
     */
    public java.lang.String getSolde(int pos) {
        CGPlanComptableViewBean entity = (CGPlanComptableViewBean) getEntity(pos);
        if (CodeSystem.CS_DEFINITIF.equals(getReqComptabilite())) {
            return entity.getSolde();
        } else {
            return entity.getSoldeProvisoire();
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.11.2002 12:50:25)
     * 
     * @return java.lang.String
     */
    public java.lang.String getSoldeMonnaie(int pos) {
        CGPlanComptableViewBean entity = (CGPlanComptableViewBean) getEntity(pos);
        if (CodeSystem.CS_DEFINITIF.equals(getReqComptabilite())) {
            return entity.getSoldeMonnaie();
        } else {
            return entity.getSoldeProvisoireMonnaie();
        }
    }

    /**
     * @return
     */
    public boolean isGroupIdCompteOfas() {
        return groupIdCompteOfas;
    }

    /**
     * @param b
     */
    public void setGroupIdCompteOfas(boolean b) {
        groupIdCompteOfas = b;
    }

}
