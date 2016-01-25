package globaz.naos.db.affiliation;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.naos.db.affiliation.access.AFAffiliationNonProvisoiresManager;

/**
 * ListViewBean pour les Affiliation Non-Provisoires.
 * 
 * @author ado 22 avr. 04
 */
public class AFAffiliationNonProvisoiresListViewBean extends AFAffiliationNonProvisoiresManager implements
        FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAffiliationNonProvisoiresViewBean();
    }

}
