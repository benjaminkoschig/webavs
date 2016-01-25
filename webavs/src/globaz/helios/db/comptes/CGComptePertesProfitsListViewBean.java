package globaz.helios.db.comptes;

import globaz.framework.util.FWCurrency;
import globaz.helios.translation.CodeSystem;
import java.util.HashSet;

/**
 * Insérez la description du type ici. Date de création : (26.11.2002 12:24:25)
 * 
 * @author: Administrator
 */
public class CGComptePertesProfitsListViewBean extends CGComptePertesProfitsManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Insérez la description de la méthode ici. Date de création : (03.04.2003 18:03:58)
     * 
     * @return java.util.HashSet
     */
    public static HashSet getExcept() {
        HashSet hash = new HashSet();
        hash.add(CGCompte.CS_COMPTE_BILAN);
        return hash;
    }

    private boolean groupIdCompteOfas = false;

    /**
     * Commentaire relatif au constructeur CGSoldesDesComptesListViewBean.
     */
    public CGComptePertesProfitsListViewBean() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:50:25)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCharge(int pos) {
        CGPlanComptableViewBean entity = (CGPlanComptableViewBean) getEntity(pos);
        if (CodeSystem.CS_DEFINITIF.equals(getReqComptabilite())) {
            if (entity.isSoldeDoit()) {
                FWCurrency temp = new FWCurrency(entity.getSolde());
                return temp.toString();
            }
        } else {
            if (entity.isSoldeProvisoireDoit()) {
                FWCurrency temp = new FWCurrency(entity.getSoldeProvisoire());
                return temp.toString();
            }
        }
        return "";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:50:53)
     * 
     * @return java.lang.String
     */
    public java.lang.String getProduit(int pos) {
        CGPlanComptableViewBean entity = (CGPlanComptableViewBean) getEntity(pos);
        if (CodeSystem.CS_DEFINITIF.equals(getReqComptabilite())) {
            if (entity.isSoldeAvoir()) {
                FWCurrency temp = new FWCurrency(entity.getSolde());
                temp.negate();
                return temp.toString();
            }
        } else {
            if (entity.isSoldeProvisoireAvoir()) {
                FWCurrency temp = new FWCurrency(entity.getSoldeProvisoire());
                temp.negate();
                return temp.toString();
            }
        }
        return "";
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
