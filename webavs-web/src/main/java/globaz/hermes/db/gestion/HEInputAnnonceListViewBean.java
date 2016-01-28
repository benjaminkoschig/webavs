package globaz.hermes.db.gestion;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;

/**
 * Insérez la description du type ici. Date de création : (06.01.2003 15:47:51)
 * 
 * @author: Administrator
 */
public class HEInputAnnonceListViewBean extends BManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur HEInputAnnonceListViewBean.
     */
    public HEInputAnnonceListViewBean() {
        super();
    }

    /**
     * Crée une nouvelle entité
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception
     *                si la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new HEInputAnnonceViewBean();
    }
}
