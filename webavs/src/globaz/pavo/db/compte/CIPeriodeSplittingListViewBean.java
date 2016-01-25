package globaz.pavo.db.compte;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.pavo.util.CIUtil;

/**
 * ListViewBean de <tt>CIPeriodeSplittingManager</tt>. Date de création : (21.11.2002 16:18:08)
 * 
 * @author: David Girardin
 */
public class CIPeriodeSplittingListViewBean extends CIPeriodeSplittingManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public boolean isCaisseDiffferente() {
        boolean retour = false;
        try {
            retour = CIUtil.isCaisseDifferente(getSession());
        } catch (Exception e) {
            // potentiellement propertie inexistante => false
            retour = false;
        }
        return retour;
    }

}
