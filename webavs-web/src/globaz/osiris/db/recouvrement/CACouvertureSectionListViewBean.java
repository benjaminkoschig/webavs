package globaz.osiris.db.recouvrement;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.osiris.db.access.recouvrement.CACouvertureSectionManager;

/**
 * Représente le model de la vue "_rcListe".
 * 
 * @author Pascal Lovy, 10-may-2005
 */
public class CACouvertureSectionListViewBean extends CACouvertureSectionManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CACouvertureSectionViewBean();
    }

}
