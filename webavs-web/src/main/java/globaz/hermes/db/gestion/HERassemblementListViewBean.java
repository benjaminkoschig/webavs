package globaz.hermes.db.gestion;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.hermes.db.access.HERassemblementManager;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HERassemblementListViewBean extends HERassemblementManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for HERassemblementListViewBean.
     */
    public HERassemblementListViewBean() {
        super();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        HERassemblementViewBean n = new HERassemblementViewBean();
        n.setArchivage(Boolean.valueOf(getIsArchivage()).booleanValue());
        return n;
    }
}
