package globaz.helios.db.modeles;

import globaz.globall.db.BEntity;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (09.09.2002 15:09:14)
 * 
 * @author: Administrator
 */

public class CGModeleEcritureListViewBean extends CGModeleEcritureManager implements
        globaz.framework.bean.FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CGModeleEcritureListViewBean.
     */
    public CGModeleEcritureListViewBean() {
        super();
    }

    /**
     * @see globaz.helios.db.modeles.CGModeleEcritureManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CGModeleEcritureViewBean();
    }

}
