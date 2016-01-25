package globaz.helios.db.modeles;

import globaz.globall.db.BEntity;

/**
 * Insérez la description du type ici. Date de création : (09.09.2002 15:09:14)
 * 
 * @author: Administrator
 */

public class CGLigneModeleEcritureListViewBean extends CGLigneModeleEcritureManager implements
        globaz.framework.bean.FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CGModeleEcritureListViewBean.
     */
    public CGLigneModeleEcritureListViewBean() {
        super();
    }

    /**
     * @see globaz.helios.db.modeles.CGLigneModeleEcritureManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CGLigneModeleEcritureViewBean();
    }

    public String getEMailAddress() {
        if (getSession() != null) {
            return getSession().getUserEMail();
        } else {
            return "";
        }
    }
}
