package globaz.helios.db.comptes;

import globaz.framework.util.FWCurrency;
import globaz.helios.translation.CodeSystem;

/**
 * @author: Administrator
 */
public class CGBilanListViewBean extends CGPlanComptableManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CG_BILAN_ORDER_BY = "IDEXTERNE";
    private boolean groupIdCompteOfas = false;

    /**
     * Commentaire relatif au constructeur CGSoldesDesComptesListViewBean.
     */
    public CGBilanListViewBean() {
        super();
    }

    /**
     * Critères de recherche
     * 
     */
    @Override
    protected void _beforeFind(globaz.globall.db.BTransaction transaction) {
        super._beforeFind(transaction);
        setOrderBy(CG_BILAN_ORDER_BY);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:50:25)
     * 
     * @return String
     */
    public String getActif(int pos) {
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
     * @return String
     */
    public String getPassif(int pos) {
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
