package globaz.hermes.db.gestion;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (06.01.2003 15:47:51)
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
     * Cr�e une nouvelle entit�
     * 
     * @return la nouvelle entit�
     * @exception java.lang.Exception
     *                si la cr�ation a �chou�e
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new HEInputAnnonceViewBean();
    }
}
