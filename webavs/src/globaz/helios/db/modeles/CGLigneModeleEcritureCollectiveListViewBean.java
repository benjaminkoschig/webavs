package globaz.helios.db.modeles;

/**
 * Insérez la description du type ici. Date de création : (09.09.2002 15:09:14)
 * 
 * @author: Administrator
 */

public class CGLigneModeleEcritureCollectiveListViewBean extends CGLigneModeleEcritureManager implements
        globaz.framework.bean.FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CGModeleEcritureListViewBean.
     */
    public CGLigneModeleEcritureCollectiveListViewBean() {
        super();
    }

    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGLigneModeleEcritureCollectiveViewBean();
    }

}
