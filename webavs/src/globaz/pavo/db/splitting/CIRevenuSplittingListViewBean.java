package globaz.pavo.db.splitting;

import globaz.framework.bean.FWListViewBeanInterface;

/**
 * Bean de la liste des revenus de splitting. Date de création : (31.10.2002 08:15:22)
 * 
 * @author: dgi
 */
public class CIRevenuSplittingListViewBean extends CIRevenuSplittingManager implements FWListViewBeanInterface {

    private static final long serialVersionUID = 5644462515081273052L;
    private java.lang.String action = null;

    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        // Inforom 426 trier la liste des revenus de splitting par ordre chronologique
        return "KFANNE ASC";
    }

    public java.lang.String getAction() {
        return action;
    }

    public String getAnnee(int pos) {
        CIRevenuSplitting entity = (CIRevenuSplitting) getEntity(pos);
        return entity.getAnnee();
    }

    public String getCotisation(int pos) {
        CIRevenuSplitting entity = (CIRevenuSplitting) getEntity(pos);
        return entity.getCotisation();
    }

    public String getIdRevenuSplitting(int pos) {
        CIRevenuSplitting entity = (CIRevenuSplitting) getEntity(pos);
        return entity.getIdRevenuSplitting();
    }

    public String getRevenu(int pos) {
        CIRevenuSplitting entity = (CIRevenuSplitting) getEntity(pos);
        return entity.getRevenu();
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }
}
