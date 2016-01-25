package globaz.pavo.db.compte;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.pavo.util.CIUtil;

/**
 * Insérez la description du type ici. Date de création : (22.11.2002 13:37:26)
 * 
 * @author: Administrator
 */
public class CIRassemblementOuvertureListViewBean extends CIRassemblementOuvertureManager implements
        FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getOrder(BStatement statement) {
        return "KKDORD, KKTENR";
    }

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
